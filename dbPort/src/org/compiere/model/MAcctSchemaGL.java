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
 *	Accounting Schema GL info
 *	
 *  @author Jorg Janke
 *  @version $Id: MAcctSchemaGL.java,v 1.8 2005/11/25 21:58:29 jjanke Exp $
 */
public class MAcctSchemaGL extends X_C_AcctSchema_GL
{
	/**
	 * 	Get Accounting Schema GL Info
	 *	@param ctx context
	 *	@param C_AcctSchema_ID id
	 *	@return defaults
	 */
	public static MAcctSchemaGL get (Properties ctx, int C_AcctSchema_ID)
	{
		MAcctSchemaGL retValue = null;
		String sql = "SELECT * FROM C_AcctSchema_GL WHERE C_AcctSchema_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_AcctSchema_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				retValue = new MAcctSchemaGL (ctx, rs, null);
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
	protected static CLogger			s_log = CLogger.getCLogger(MAcctSchemaGL.class);
	
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param C_AcctSchema_ID AcctSchema 
	 */
	public MAcctSchemaGL (Properties ctx, int C_AcctSchema_ID, String trxName)
	{
		super(ctx, C_AcctSchema_ID, trxName);
		if (C_AcctSchema_ID == 0)
		{
			setUseCurrencyBalancing (false);
			setUseSuspenseBalancing (false);
			setUseSuspenseError (false);
		}
	}	//	MAcctSchemaGL

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MAcctSchemaGL (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MAcctSchemaGL

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

}	//	MAcctSchemaGL
