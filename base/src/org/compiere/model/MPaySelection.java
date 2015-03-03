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
 *	AP Payment Selection
 *	
 *  @author Jorg Janke
 *  @version $Id: MPaySelection.java,v 1.8 2005/10/14 00:44:31 jjanke Exp $
 */
public class MPaySelection extends X_C_PaySelection
{
	/**
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param C_PaySelection_ID id
	 */
	public MPaySelection (Properties ctx, int C_PaySelection_ID, String trxName)
	{
		super(ctx, C_PaySelection_ID, trxName);
		if (C_PaySelection_ID == 0)
		{
		//	setC_BankAccount_ID (0);
		//	setName (null);	// @#Date@
		//	setPayDate (new Timestamp(System.currentTimeMillis()));	// @#Date@
			setTotalAmt (Env.ZERO);
			setIsApproved (false);
			setProcessed (false);
			setProcessing (false);
		}
	}	//	MPaySelection

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MPaySelection(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MPaySelection

	/**	Lines						*/
	private MPaySelectionLine[]	m_lines = null;
	/**	Currency of Bank Account	*/
	private int					m_C_Currency_ID = 0;
	
	/**
	 * 	Get Lines
	 *	@param requery requery
	 *	@return lines
	 */
	public MPaySelectionLine[] getLines(boolean requery)
	{
		if (m_lines != null && !requery)
			return m_lines;
		ArrayList<MPaySelectionLine> list = new ArrayList<MPaySelectionLine>();
		String sql = "SELECT * FROM C_PaySelectionLine WHERE C_PaySelection_ID=? ORDER BY Line";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getC_PaySelection_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MPaySelectionLine(getCtx(), rs, get_TrxName()));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getLines", e); 
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		//
		m_lines = new MPaySelectionLine[list.size ()];
		list.toArray (m_lines);
		return m_lines;
	}	//	getLines
	
	/**
	 * 	Get Currency of Bank Account
	 *	@return C_Currency_ID
	 */
	public int getC_Currency_ID()
	{
		if (m_C_Currency_ID == 0)
		{
			String sql = "SELECT C_Currency_ID FROM C_BankAccount " 
				+ "WHERE C_BankAccount_ID=?";
			m_C_Currency_ID = DB.getSQLValue(null, sql, getC_BankAccount_ID());
		}
		return m_C_Currency_ID;
	}	//	getC_Currency_ID
	
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MPaySelection[");
		sb.append(get_ID()).append(",").append(getName())
			.append("]");
		return sb.toString();
	}	//	toString

	
	
}	//	MPaySelection
