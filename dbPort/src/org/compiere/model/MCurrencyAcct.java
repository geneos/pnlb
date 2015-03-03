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
 *	Currency Account Model 
 *	
 *  @author Jorg Janke
 *  @version $Id: MCurrencyAcct.java,v 1.6 2005/10/26 00:38:17 jjanke Exp $
 */
public class MCurrencyAcct extends X_C_Currency_Acct
{
	/** Static Logger					*/
	private static CLogger s_log = CLogger.getCLogger(MCurrencyAcct.class);
	
	/**
	 * 	Get Currency Account for Currency
	 *	@param as accounting schema default
	 *	@param C_Currency_ID currency
	 *	@return Currency Account or null
	 */
	public static MCurrencyAcct get (MAcctSchemaDefault as, int C_Currency_ID)
	{
		MCurrencyAcct retValue = null;
		String sql = "SELECT * FROM C_Currency_Acct "
			+ "WHERE C_AcctSchema_ID=? AND C_Currency_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, as.getC_AcctSchema_ID());
			pstmt.setInt(2, C_Currency_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				retValue = new MCurrencyAcct (as.getCtx(), rs, null);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "get", e);
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
	
	
	/**************************************************************************
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MCurrencyAcct(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MCurrencyAcct

}	//	MCurrencyAcct
