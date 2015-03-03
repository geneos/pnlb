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
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Account Model Lookup - Maintains ValidCombination Info for Display & Edit - not cached
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: MAccountLookup.java,v 1.7 2005/10/26 00:38:16 jjanke Exp $
 */
public final class MAccountLookup extends Lookup implements Serializable
{
	/**
	 *	Constructor
	 *  @param ctx context
	 *  @param WindowNo window no
	 */
	public MAccountLookup (Properties ctx, int WindowNo)
	{
		super (DisplayType.TableDir, WindowNo);
		m_ctx = ctx;
	}	//	MAccountLookup

	/**	Context				*/
	private Properties  m_ctx;

	public int 		    C_ValidCombination_ID;
	private String		Combination;
	private String		Description;

	/**
	 *	Get Display for Value
	 *  @param value value
	 *  @return String
	 */
	public String getDisplay (Object value)
	{
		if (!containsKey (value))
			return "<" + value.toString() + ">";
		return toString();
	}	//	getDisplay

	/**
	 *	Get Object of Key Value
	 *  @param value value
	 *  @return Object or null
	 */
	public NamePair get (Object value)
	{
		if (value == null)
			return null;
		if (!containsKey (value))
			return null;
		return new KeyNamePair (C_ValidCombination_ID, toString());
	}	//	get

	/**
	 *  The Lookup contains the key
	 *  @param key key
	 *  @return true if exists
	 */
	public boolean containsKey (Object key)
	{
		int intValue = 0;
		if (key instanceof Integer)
			intValue = ((Integer)key).intValue();
		else if (key != null)
			intValue = Integer.parseInt(key.toString());
		//
		return load (intValue);
	}   //  containsKey

	/**
	 *  Get Description
	 *  @return Description
	 */
	public String getDescription()
	{
		return Description;
	}   //  getDescription

	/**
	 *	Return String representation
	 *  @return Combination
	 */
	public String toString()
	{
		if (C_ValidCombination_ID == 0)
			return "";
		return Combination;
	}	//	toString

	/**
	 *	Load C_ValidCombination with ID
	 *  @param ID C_ValidCombination_ID
	 *  @return true if found
	 */
	public boolean load (int ID)
	{
		if (ID == 0)						//	new
		{
			C_ValidCombination_ID = 0;
			Combination = "";
			Description = "";
			return true;
		}
		if (ID == C_ValidCombination_ID)	//	already loaded
			return true;

		String	SQL = "SELECT C_ValidCombination_ID, Combination, Description "
			+ "FROM C_ValidCombination WHERE C_ValidCombination_ID=?";
		try
		{
			//	Prepare Statement
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			pstmt.setInt(1, ID);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next())
			{
				rs.close();
				pstmt.close();
				return false;
			}
			//
			C_ValidCombination_ID = rs.getInt(1);
			Combination = rs.getString(2);
			Description = rs.getString(3);
			//
			rs.close();
			pstmt.close();

		}
		catch (SQLException e)
		{
			return false;
		}
		return true;
	}	//	load

	/**
	 *	Get underlying fully qualified Table.Column Name
	 *  @return ""
	 */
	public String getColumnName()
	{
		return "";
	}   //  getColumnName

	/**
	 *	Return data as sorted Array.
	 *  Used in Web Interface
	 *  @param mandatory mandatory
	 *  @param onlyValidated only valid
	 *  @param onlyActive only active
	 * 	@param temporary force load for temporary display
	 *  @return ArrayList with KeyNamePair
	 */
	public ArrayList<Object> getData (boolean mandatory, boolean onlyValidated, 
		boolean onlyActive, boolean temporary)
	{
		ArrayList<Object> list = new ArrayList<Object>();
		if (!mandatory)
			list.add(new KeyNamePair (-1, ""));
		//
		StringBuffer sql = new StringBuffer ("SELECT C_ValidCombination_ID, Combination, Description "
			+ "FROM C_ValidCombination WHERE AD_Client_ID=?");
		if (onlyActive)
			sql.append(" AND IsActive='Y'");
		sql.append(" ORDER BY 2");
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, Env.getAD_Client_ID(m_ctx));
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add (new KeyNamePair(rs.getInt(1), rs.getString(2) + " - " + rs.getString(3)));
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}

		//  Sort & return
		return list;
	}   //  getData

}	//	MAccountLookup
