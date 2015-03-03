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

import java.sql.*;
import java.util.*;

import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Default Accounts for MAcctSchema
 *	
 *  @author Jorg Janke
 *  @version $Id: MAcctSchemaDefault.java,v 1.10 2005/12/19 01:20:46 jjanke Exp $
 */
public class MAcctSchemaDefault extends X_C_AcctSchema_Default
{
	/**
	 * 	Get Accounting Schema Default Info
	 *	@param ctx context
	 *	@param C_AcctSchema_ID id
	 *	@return defaults
	 */
	public static MAcctSchemaDefault get (Properties ctx, int C_AcctSchema_ID)
	{
		MAcctSchemaDefault retValue = null;
		String sql = "SELECT * FROM C_AcctSchema_Default WHERE C_AcctSchema_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_AcctSchema_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				retValue = new MAcctSchemaDefault (ctx, rs, null);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		return retValue;
	}	//	get
	
	/**	Logger							*/
	protected static CLogger			s_log = CLogger.getCLogger(MAcctSchemaDefault.class);

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param C_AcctSchema_ID parent
	 */
	public MAcctSchemaDefault(Properties ctx, int C_AcctSchema_ID, String trxName)
	{
		super(ctx, C_AcctSchema_ID, trxName);
	}	//	MAcctSchemaDefault

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MAcctSchemaDefault(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MAcctSchemaDefault

	/**
	 * 	Get Realized Gain Acct for currency
	 *	@param C_Currency_ID currency
	 *	@return gain acct
	 */
	public int getRealizedGain_Acct (int C_Currency_ID)
	{
		MCurrencyAcct acct = MCurrencyAcct.get (this, C_Currency_ID);
		if (acct != null)
			return acct.getRealizedGain_Acct(); 
		return super.getRealizedGain_Acct();
	}	//	getRealizedGain_Acct
	
	/**
	 * 	Get Realized Loss Acct for currency
	 *	@param C_Currency_ID currency
	 *	@return loss acct
	 */
	public int getRealizedLoss_Acct (int C_Currency_ID) 
	{
		MCurrencyAcct acct = MCurrencyAcct.get (this, C_Currency_ID);
		if (acct != null)
			return acct.getRealizedLoss_Acct(); 
		return super.getRealizedLoss_Acct();
	}	//	getRealizedLoss_Acct

	/**
	 * 	Get Acct Info list 
	 *	@return list
	 */
	public ArrayList<KeyNamePair> getAcctInfo()
	{
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		for (int i = 0; i < get_ColumnCount(); i++)
		{
			String columnName = get_ColumnName(i);
			if (columnName.endsWith("Acct"))
			{
				int id = ((Integer)get_Value(i));
				list.add(new KeyNamePair (id, columnName));
			}
		}
		return list;
	}	//	getAcctInfo

	/**
	 * 	Set Value (don't use)
	 *	@param columnName column name
	 *	@param value value
	 */
	public boolean setValue (String columnName, Integer value)
	{
		return super.set_Value (columnName, value);
	}	//	setValue

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (getAD_Org_ID() != 0)
			setAD_Org_ID(0);
		return true;
	}	//	beforeSave

}	//	MAcctSchemaDefault
