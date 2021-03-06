/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.model;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Natural Account (HashMap) Management.
 *  <String,MElementValue>
 *  <pre>
 *  The key is a String of the column name (e.g. SUSPENSEBALANCING_ACCT)
 *  The value is an NaturalAccount
 *
 *  a) Account information are loaded via the parse functions
 *  b) Accounts are created via the createAccounts function
 *  c) retrieve the C_ElementValue_ID for the given key
 *  </pre>
 *
 *  @author Jorg Janke
 *  @version $Id: NaturalAccountMap.java,v 1.21 2005/10/08 02:02:29 jjanke Exp $
 */
public final class NaturalAccountMap<K,V> extends CCache<K,V>
{
	/**
	 *  Constructor.
	 *  Parse File does the processing
	 */
	public NaturalAccountMap(Properties ctx, String trxName)
	{
		super("NaturalAccountMap", 100);
		m_ctx = ctx;
		m_trxName = trxName;
	}   //  NaturalAccountMap

	/** Delimiter       */
	private String      m_delim = ",";
	/** KeyNo           */
	private static int  s_keyNo = 0;
	/** Context			*/
	private Properties	m_ctx = null;
	/** Transaction		*/
	private String		m_trxName = null;
	/** Map of Values and Element	*/
	private HashMap<String,MElementValue> 	m_valueMap = new HashMap<String,MElementValue>();
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(NaturalAccountMap.class);

	/**
	 *  Read and Parse File
	 * 	@param file Accounts file
	 *  @return error message or "" if OK
	 */
	public String parseFile (File file)
	{
		log.config(file.getAbsolutePath());
		String line = null;
		try
		{
			//  see FileImport
			BufferedReader in = new BufferedReader(new FileReader(file), 10240);
			//	not safe see p108 Network pgm
			String errMsg = "";

			//  read lines
			while ((line = in.readLine()) != null && errMsg.length() == 0)
				errMsg = parseLine(line);
			line = "";
			in.close();

			//  Error
			if (errMsg.length() != 0)
				return errMsg;
		}
		catch (Exception ioe)
		{
			String s = ioe.getLocalizedMessage();
			if (s == null || s.length() == 0)
				s = ioe.toString();
			return "Parse Error: Line=" + line + " - " + s;
		}
		return "";
	}   //  parse

	/**
	 *  Create Account Entry for Default Accounts only.
	 *  @param line line with info
	 *  Line format (9 fields)
	 *   1	A   [Account Value]
	 *   2	B   [Account Name]
	 *   3	C   [Description]
	 *   4	D   [Account Type]
	 *   5	E   [Account Sign]
	 *   6	F   [Document Controlled]
	 *   7	G   [Summary Account]
	 * 	 8	H   [Default_Account]
	 * 	 9	I   [Parent Value] - ignored
	 *
	 *  @return error message or "" if OK
	 */
	@SuppressWarnings("unchecked")
	public String parseLine (String line) throws Exception
	{
		log.config(line);

		//  Fields with ',' are enclosed in "
		StringBuffer newLine = new StringBuffer();
		StringTokenizer st = new StringTokenizer(line, "\"", false);
		newLine.append(st.nextToken());         //  first part
		while (st.hasMoreElements())
		{
			String s = st.nextToken();          //  enclosed part
			newLine.append(s.replace(',',' ')); //  remove ',' with space
			if (st.hasMoreTokens())
				newLine.append(st.nextToken()); //  unenclosed
		}
		//  add space at the end        - tokenizer does not count empty fields
		newLine.append(" ");

		//  Parse Line - replace ",," with ", ,"    - tokenizer does not count empty fields
		String pLine = Util.replace(newLine.toString(), ",,", ", ,");
		pLine = Util.replace(pLine, ",,", ", ,");
		st = new StringTokenizer(pLine, ",", false);
		//  All fields there ?
		if (st.countTokens() == 1)
		{
			log.log(Level.SEVERE, "Ignored: Require ',' as separator - " + pLine);
			return "";
		}
		if (st.countTokens() < 9)
		{
			log.log(Level.SEVERE, "Ignored: FieldNumber wrong: " + st.countTokens() + " - " + pLine);
			return "";
		}

		//  Fill variables
		String Value = null, Name = null, Description = null,
			AccountType = null, AccountSign = null, IsDocControlled = null,
			IsSummary = null, Default_Account = null;
		//
		for (int i = 0; i < 8 && st.hasMoreTokens(); i++)
		{
			String s = st.nextToken().trim();
			//  Ignore, if is it header line
			if (s.startsWith("[") && s.endsWith("]"))
				return "";
			if (s == null)
				s = "";
			//
			if (i == 0)			//	A - Value
				Value = s;
			else if (i == 1)	//	B - Name
				Name = s;
			else if (i == 2)	//	C - Description
				Description = s;
			else if (i == 3)	//	D - Type
				AccountType = s.length()>0 ? String.valueOf(s.charAt(0)) : "E";
			else if (i == 4)	//	E - Sign
				AccountSign = s.length()>0 ? String.valueOf(s.charAt(0)) : "N";
			else if (i == 5)	//	F - DocControlled
				IsDocControlled = s.length()>0 ? String.valueOf(s.charAt(0)) : "N";
			else if (i == 6)	//	G - IsSummary
				IsSummary = s.length()>0 ? String.valueOf(s.charAt(0)) : "N";
			else if (i == 7)	//	H - Default_Account
				Default_Account = s;
		}

		//	Ignore if Value & Name are empty (no error message)
		if ((Value == null || Value.length() == 0) && (Name == null || Name.length() == 0))
			return "";

		//  Default Account may be blank
		if (Default_Account == null || Default_Account.length() == 0)
		//	Default_Account = String.valueOf(s_keyNo++);
			return "";

		//	No Summary Account
		if (IsSummary == null || IsSummary.length() == 0)
			IsSummary = "N";
		if (!IsSummary.equals("N"))
			return "";
			
		//  Validation
		if (AccountType == null || AccountType.length() == 0)
			AccountType = "E";
			
		if (AccountSign == null || AccountSign.length() == 0)
			AccountSign = "N";
		if (IsDocControlled == null || IsDocControlled.length() == 0)
			IsDocControlled = "N";


	//	log.config( "Value=" + Value + ", AcctType=" + AccountType
	//		+ ", Sign=" + AccountSign + ", Doc=" + docControlled
	//		+ ", Summary=" + summary + " - " + Name + " - " + Description);

		try
		{
			//	Try to find - allows to use same natutal account for multiple default accounts 
			MElementValue na = (MElementValue)m_valueMap.get(Value);
			if (na == null)
			{
				//  Create Account - save later
				na = new MElementValue(m_ctx, Value, Name, Description,
					AccountType, AccountSign,
					IsDocControlled.toUpperCase().startsWith("Y"), 
					IsSummary.toUpperCase().startsWith("Y"), m_trxName);
				m_valueMap.put(Value, na);
			}
			
			//  Add to Cache
			put((K)Default_Account.toUpperCase(), (V)na);
		}
		catch (Exception e)
		{
			return (e.getMessage());
		}

		return "";
	}   //  parseLine

	/**
	 *  Save all Accounts
	 *
	 * 	@param AD_Client_ID client
	 * 	@param AD_Org_ID org
	 * 	@param C_Element_ID element
	 * 	@return true if created
	 */
	public boolean saveAccounts (int AD_Client_ID, int AD_Org_ID, int C_Element_ID)
	{
		log.config("");
		Iterator iterator = this.values().iterator();
		while (iterator.hasNext())
		{
			MElementValue na = (MElementValue)iterator.next();
			na.setAD_Client_ID(AD_Client_ID);
			na.setAD_Org_ID(AD_Org_ID);
			na.setC_Element_ID(C_Element_ID);
			if (!na.save())
				return false;
		}
		return true;
	}   //  saveAccounts

	/**
	 *  Get ElementValue
	 * 	@param key key
	 *  @return 0 if error
	 */
	public int getC_ElementValue_ID (String key)
	{
		MElementValue na = (MElementValue)this.get(key);
		if (na == null)
			return 0;
		return na.getC_ElementValue_ID();
	}   //  getC_ElementValue_ID

}   //  NaturalAccountMap
