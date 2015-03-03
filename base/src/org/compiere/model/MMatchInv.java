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

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Match Invoice (Receipt<>Invoice) Model.
 *	Accounting:
 *	- Not Invoiced Receipts (relief)
 *	- IPV
 *	
 *  @author Jorg Janke
 *  @version $Id: MMatchInv.java,v 1.20 2005/11/28 03:36:03 jjanke Exp $
 */
public class MMatchInv extends X_M_MatchInv
{
	/**
	 * 	Get InOut-Invoice Matches
	 *	@param ctx context
	 *	@param M_InOutLine_ID shipment
	 *	@param C_InvoiceLine_ID invoice
	 *	@return array of matches
	 */
	public static MMatchInv[] get (Properties ctx, 
		int M_InOutLine_ID, int C_InvoiceLine_ID, String trxName)
	{
		if (M_InOutLine_ID == 0 || C_InvoiceLine_ID == 0)
			return new MMatchInv[]{};
		//
		String sql = "SELECT * FROM M_MatchInv WHERE M_InOutLine_ID=? AND C_InvoiceLine_ID=?";
		ArrayList<MMatchInv> list = new ArrayList<MMatchInv>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_InOutLine_ID);
			pstmt.setInt (2, C_InvoiceLine_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MMatchInv (ctx, rs, trxName));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e); 
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
		MMatchInv[] retValue = new MMatchInv[list.size()];
		list.toArray (retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get Inv Matches for InOut
	 *	@param ctx context
	 *	@param M_InOut_ID shipment
	 *	@return array of matches
	 */
	public static MMatchInv[] getInOut (Properties ctx, 
		int M_InOut_ID, String trxName)
	{
		if (M_InOut_ID == 0)
			return new MMatchInv[]{};
		//
		String sql = "SELECT * FROM M_MatchInv m"
			+ " INNER JOIN M_InOutLine l ON (m.M_InOutLine_ID=l.M_InOutLine_ID) "
			+ "WHERE l.M_InOut_ID=?"; 
		ArrayList<MMatchInv> list = new ArrayList<MMatchInv>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_InOut_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MMatchInv (ctx, rs, trxName));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e); 
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
		MMatchInv[] retValue = new MMatchInv[list.size()];
		list.toArray (retValue);
		return retValue;
	}	//	getInOut

	/**
	 * 	Get Inv Matches for Invoice
	 *	@param ctx context
	 *	@param C_Invoice_ID invoice
	 *	@return array of matches
	 */
	public static MMatchInv[] getInvoice (Properties ctx, 
		int C_Invoice_ID, String trxName)
	{
		if (C_Invoice_ID == 0)
			return new MMatchInv[]{};
		//
		String sql = "SELECT * FROM M_MatchInv mi"
			+ " INNER JOIN C_InvoiceLine il ON (mi.C_InvoiceLine_ID=il.C_InvoiceLine_ID) "
			+ "WHERE il.C_Invoice_ID=?";
		ArrayList<MMatchInv> list = new ArrayList<MMatchInv>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, C_Invoice_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MMatchInv (ctx, rs, trxName));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e); 
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
		MMatchInv[] retValue = new MMatchInv[list.size()];
		list.toArray (retValue);
		return retValue;
	}	//	getInvoice

	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MMatchInv.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_MatchInv_ID id
	 *	@param trxName transaction
	 */
	public MMatchInv (Properties ctx, int M_MatchInv_ID, String trxName)
	{
		super (ctx, M_MatchInv_ID, trxName);
		if (M_MatchInv_ID == 0)
		{
		//	setDateTrx (new Timestamp(System.currentTimeMillis()));
		//	setC_InvoiceLine_ID (0);
		//	setM_InOutLine_ID (0);
		//	setM_Product_ID (0);
			setM_AttributeSetInstance_ID(0);
		//	setQty (Env.ZERO);
			setPosted (false);
			setProcessed (false);
			setProcessing (false);
		}
	}	//	MMatchInv

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MMatchInv (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MMatchInv
	
	/**
	 * 	Invoice Line Constructor
	 *	@param iLine invoice line
	 *	@param dateTrx optional date
	 *	@param qty matched quantity
	 */
	public MMatchInv (MInvoiceLine iLine, Timestamp dateTrx, BigDecimal qty)
	{
		this (iLine.getCtx(), 0, iLine.get_TrxName());
		setClientOrg(iLine);
		setC_InvoiceLine_ID(iLine.getC_InvoiceLine_ID());
		setM_InOutLine_ID(iLine.getM_InOutLine_ID());
		if (dateTrx != null)
			setDateTrx (dateTrx);
		setM_Product_ID (iLine.getM_Product_ID());
		setM_AttributeSetInstance_ID(iLine.getM_AttributeSetInstance_ID());
		setQty (qty);
		setProcessed(true);		//	auto
	}	//	MMatchInv

	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Set Trx Date
		if (getDateTrx() == null)
			setDateTrx (new Timestamp(System.currentTimeMillis()));
		//	Set Acct Date
		if (getDateAcct() == null)
		{
			Timestamp ts = getNewerDateAcct();
			if (ts == null)
				ts = getDateTrx();
			setDateAcct (ts);
		}
		if (getM_AttributeSetInstance_ID() == 0 && getM_InOutLine_ID() != 0)
		{
			MInOutLine iol = new MInOutLine (getCtx(), getM_InOutLine_ID(), get_TrxName());
			setM_AttributeSetInstance_ID(iol.getM_AttributeSetInstance_ID());
		}
		return true;
	}	//	beforeSave
	
	/**
	 * 	Get the later Date Acct from invoice or shipment
	 *	@return date or null
	 */
	private Timestamp getNewerDateAcct()
	{
		Timestamp invoiceDate = null;
		Timestamp shipDate = null;
		
		String sql = "SELECT i.DateAcct "
			+ "FROM C_InvoiceLine il"
			+ " INNER JOIN C_Invoice i ON (i.C_Invoice_ID=il.C_Invoice_ID) "
			+ "WHERE C_InvoiceLine_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, getC_InvoiceLine_ID());
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				invoiceDate = rs.getTimestamp(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		sql = "SELECT io.DateAcct "
			+ "FROM M_InOutLine iol"
			+ " INNER JOIN M_InOut io ON (io.M_InOut_ID=iol.M_InOut_ID) "
			+ "WHERE iol.M_InOutLine_ID=?";
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, getM_InOutLine_ID());
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				shipDate = rs.getTimestamp(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		//
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
		
		if (invoiceDate == null)
			return shipDate;
		if (shipDate == null)
			return invoiceDate;
		if (invoiceDate.after(shipDate))
			return invoiceDate;
		return shipDate;
	}	//	getNewerDateAcct
	
	
	/**
	 * 	Before Delete
	 *	@return true if acct was deleted
	 */
	protected boolean beforeDelete ()
	{
		if (isPosted())
		{
			if (!MPeriod.isOpen(getCtx(), getDateTrx(), MDocType.DOCBASETYPE_MatchInvoice))
				return false;
			setPosted(false);
			return MFactAcct.delete (Table_ID, get_ID(), get_TrxName()) >= 0;
		}
		return true;
	}	//	beforeDelete

	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterDelete (boolean success)
	{
		if (success)
		{
			//	Get Order and decrease invoices
			MInvoiceLine iLine = new MInvoiceLine (getCtx(), getC_InvoiceLine_ID(), get_TrxName());
			int C_OrderLine_ID = iLine.getC_OrderLine_ID();
			if (C_OrderLine_ID == 0)
			{
				MInOutLine ioLine = new MInOutLine (getCtx(), getM_InOutLine_ID(), get_TrxName());
				C_OrderLine_ID = ioLine.getC_OrderLine_ID();
			}
			//	No Order Found
			if (C_OrderLine_ID == 0)
				return success;
			//	Find MatchPO
			MMatchPO[] mPO = MMatchPO.get(getCtx(), C_OrderLine_ID, 
				getC_InvoiceLine_ID(), get_TrxName());
			for (int i = 0; i < mPO.length; i++)
			{
				if (mPO[i].getM_InOutLine_ID() == 0)
					mPO[i].delete(true);
				else
				{
					mPO[i].setC_InvoiceLine_ID(null);
					mPO[i].save();
				}
			}
		}
		return success;
	}	//	afterDelete
	
}	//	MMatchInv
