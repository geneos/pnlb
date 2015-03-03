/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.model;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;


/**
 *	Invoice Batch Header Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MInvoiceBatch.java,v 1.4 2005/09/28 01:34:03 jjanke Exp $
 */
public class MInvoiceBatch extends X_C_InvoiceBatch
{

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_InvoiceBatch_ID id
	 *	@param trxName trx
	 */
	public MInvoiceBatch (Properties ctx, int C_InvoiceBatch_ID, String trxName)
	{
		super (ctx, C_InvoiceBatch_ID, trxName);
		if (C_InvoiceBatch_ID == 0)
		{
		//	setDocumentNo (null);
		//	setC_Currency_ID (0);	// @$C_Currency_ID@
			setControlAmt (Env.ZERO);	// 0
			setDateDoc (new Timestamp(System.currentTimeMillis()));	// @#Date@
			setDocumentAmt (Env.ZERO);
			setIsSOTrx (false);	// N
			setProcessed (false);
		//	setSalesRep_ID (0);
		}
	}	//	MInvoiceBatch

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MInvoiceBatch (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MInvoiceBatch
	
	/**	The Lines						*/
	private MInvoiceBatchLine[]	m_lines	= null;

	
	/**
	 * 	Get Lines
	 *	@param reload reload data
	 *	@return array of lines
	 */
	public MInvoiceBatchLine[] getLines (boolean reload)
	{
		if (m_lines != null && !reload)
			return m_lines;
		String sql = "SELECT * FROM C_InvoiceBatchLine WHERE C_InvoiceBatch_ID=? ORDER BY Line";
		ArrayList<MInvoiceBatchLine> list = new ArrayList<MInvoiceBatchLine>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getC_InvoiceBatch_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MInvoiceBatchLine (getCtx(), rs, get_TrxName()));
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
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
		m_lines = new MInvoiceBatchLine[list.size ()];
		list.toArray (m_lines);
		return m_lines;
	}	//	getLines

	
	/**
	 * 	Set Processed
	 *	@param processed processed
	 */
	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
		String set = "SET Processed='"
			+ (processed ? "Y" : "N")
			+ "' WHERE C_InvoiceBatch_ID=" + getC_InvoiceBatch_ID();
		int noLine = DB.executeUpdate("UPDATE C_InvoiceBatchLine " + set, get_TrxName());
		m_lines = null;
		log.fine(processed + " - Lines=" + noLine);
	}	//	setProcessed
	
}	//	MInvoiceBatch
