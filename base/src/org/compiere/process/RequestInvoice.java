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
package org.compiere.process;

import java.sql.*;
import java.math.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Create Invoices for Requests
 *	
 *	
 *  @author Jorg Janke
 *  @version $Id: RequestInvoice.java,v 1.3 2005/10/26 00:37:42 jjanke Exp $
 */
public class RequestInvoice extends SvrProcess
{
	/** Request Type				*/
	private int		p_R_RequestType_ID = 0;
	/**	Request Group (opt)			*/
	private int		p_R_Group_ID = 0;
	/** Request Categpry (opt)		*/
	private int		p_R_Category_ID = 0;
	/** Business Partner (opt)		*/
	private int		p_C_BPartner_ID = 0;
	/** Default product				*/
	private int		p_M_Product_ID = 0;

	/** The invoice					*/
	private MInvoice m_invoice = null;
	/**	Line Count					*/
	private int		m_linecount = 0;
	
	/**
	 * 	Prepare
	 */
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("R_RequestType_ID"))
				p_R_RequestType_ID = para[i].getParameterAsInt();
			else if (name.equals("R_Group_ID"))
				p_R_Group_ID = para[i].getParameterAsInt();
			else if (name.equals("R_Category_ID"))
				p_R_Category_ID = para[i].getParameterAsInt();
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = para[i].getParameterAsInt();
			else if (name.equals("M_Product_ID"))
				p_M_Product_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare
	
	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		log.info("R_RequestType_ID=" + p_R_RequestType_ID + ", R_Group_ID=" + p_R_Group_ID
			+ ", R_Category_ID=" + p_R_Category_ID + ", C_BPartner_ID=" + p_C_BPartner_ID
			+ ", p_M_Product_ID=" + p_M_Product_ID);
		
		MRequestType type = MRequestType.get (getCtx(), p_R_RequestType_ID);
		if (type.get_ID() == 0)
			throw new CompiereSystemError("@R_RequestType_ID@ @NotFound@ " + p_R_RequestType_ID);
		if (!type.isInvoiced())
			throw new CompiereSystemError("@R_RequestType_ID@ <> @IsInvoiced@");
		
		String sql = "SELECT * FROM R_Request r"
			+ " INNER JOIN R_Status s ON (r.R_Status_ID=s.R_Status_ID) "
			+ "WHERE s.IsClosed='Y'"
			+ " AND r.R_RequestType_ID=?";
		if (p_R_Group_ID != 0)
			sql += " AND r.R_Group_ID=?";
		if (p_R_Category_ID != 0)
			sql += " AND r.R_Category_ID=?";
		if (p_C_BPartner_ID != 0)
			sql += " AND r.C_BPartner_ID=?";
		sql += " AND r.IsInvoiced='Y' "
			+ "ORDER BY C_BPartner_ID";
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			int index = 1;
			pstmt.setInt (index++, p_R_RequestType_ID);
			if (p_R_Group_ID != 0)
				pstmt.setInt (index++, p_R_Group_ID);
			if (p_R_Category_ID != 0)
				pstmt.setInt (index++, p_R_Category_ID);
			if (p_C_BPartner_ID != 0)
				pstmt.setInt (index++, p_C_BPartner_ID);
			ResultSet rs = pstmt.executeQuery ();
			int oldC_BPartner_ID = 0;
			while (rs.next ())
			{
				MRequest request = new MRequest (getCtx(), rs, get_TrxName());
				if (!request.isInvoiced())
					continue;
				if (oldC_BPartner_ID != request.getC_BPartner_ID())
					invoiceDone();
				if (m_invoice == null)
				{
					invoiceNew(request);
					oldC_BPartner_ID = request.getC_BPartner_ID();
				}
				invoiceLine(request);
			}
			invoiceDone();
			//
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
		//	R_Category_ID
		return null;
	}	//	doIt
	
	/**
	 * 	Done with Invoice
	 */
	private void invoiceDone()
	{
		//	Close Old
		if (m_invoice != null)
		{
			if (m_linecount == 0)
				m_invoice.delete(false);
			else
			{
				m_invoice.processIt(MInvoice.ACTION_Prepare);
				m_invoice.save();
				addLog(0, null, m_invoice.getGrandTotal(), m_invoice.getDocumentNo());
			}
		}
		m_invoice = null;
	}	//	invoiceDone
	
	/**
	 * 	New Invoice
	 *	@param request request
	 */
	private void invoiceNew (MRequest request)
	{
		m_invoice = new MInvoice (getCtx(), 0, get_TrxName());
		m_invoice.setIsSOTrx(true);
		
		MBPartner partner = new MBPartner (getCtx(), request.getC_BPartner_ID(), null);
		m_invoice.setBPartner(partner);
		
		m_invoice.save();
		m_linecount = 0;
	}	//	invoiceNew
	
	/**
	 * 	Invoice Line
	 *	@param request request
	 */
	private void invoiceLine (MRequest request)
	{
		MRequestUpdate[] updates = request.getUpdates(null);
		for (int i = 0; i < updates.length; i++)
		{
			BigDecimal qty = updates[i].getQtyInvoiced();
			if (qty == null || qty.signum() == 0)
				continue;
			
			MInvoiceLine il = new MInvoiceLine(m_invoice);
			m_linecount++;
			il.setLine(m_linecount*10);
			//
			il.setQty(qty);
			//	Product
			int M_Product_ID = updates[i].getM_ProductSpent_ID();
			if (M_Product_ID == 0)
				M_Product_ID = p_M_Product_ID;
			il.setM_Product_ID(M_Product_ID);
			//
			il.setPrice();
			il.save();
		}
	}	//	invoiceLine
	
}	//	RequestInvoice
