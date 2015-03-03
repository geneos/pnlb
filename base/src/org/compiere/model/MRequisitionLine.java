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
 *	Requisition Line Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRequisitionLine.java,v 1.12 2005/10/26 00:37:41 jjanke Exp $
 */
public class MRequisitionLine extends X_M_RequisitionLine
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_RequisitionLine_ID id
	 */
	public MRequisitionLine (Properties ctx, int M_RequisitionLine_ID, String trxName)
	{
		super (ctx, M_RequisitionLine_ID, trxName);
		if (M_RequisitionLine_ID == 0)
		{
		//	setM_Requisition_ID (0);
			setLine (0);	// @SQL=SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM M_RequisitionLine WHERE M_Requisition_ID=@M_Requisition_ID@
			setLineNetAmt (Env.ZERO);
			setPriceActual (Env.ZERO);
			setQty (Env.ONE);	// 1
		}
		
	}	//	MRequisitionLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRequisitionLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRequisitionLine

	/**
	 * 	Parent Constructor
	 *	@param req requisition
	 */
	public MRequisitionLine (MRequisition req)
	{
		this (req.getCtx(), 0, req.get_TrxName());
		setClientOrg(req);
		setM_Requisition_ID(req.getM_Requisition_ID());
		m_M_PriceList_ID = req.getM_PriceList_ID();
		m_parent = req;
	}	//	MRequisitionLine

	/** Parent					*/
	private MRequisition	m_parent = null;
	
	/**	PriceList				*/
	private int 	m_M_PriceList_ID = 0;
	/** Temp BPartner			*/
	private int		m_C_BPartner_ID = 0;
	
	/**
	 * @return Returns the c_BPartner_ID.
	 */
	public int getC_BPartner_ID ()
	{
		return m_C_BPartner_ID;
	}
	/**
	 * @param partner_ID The c_BPartner_ID to set.
	 */
	public void setC_BPartner_ID (int partner_ID)
	{
		m_C_BPartner_ID = partner_ID;
	}
	
	/**
	 * 	Get Parent
	 *	@return parent
	 */
	public MRequisition getParent()
	{
		if (m_parent == null)
			m_parent = new MRequisition (getCtx(), getM_Requisition_ID(), get_TrxName());
		return m_parent;
	}	//	getParent
	
	/**
	 * 	Set Price
	 */
	public void setPrice()
	{
		if (getM_Product_ID() == 0)
			return;
		if (m_M_PriceList_ID == 0)
			m_M_PriceList_ID = getParent().getM_PriceList_ID();
		if (m_M_PriceList_ID == 0)
		{
			log.log(Level.SEVERE, "PriceList unknown!");
			return;
		}
		setPrice (m_M_PriceList_ID);
	}	//	setPrice
	
	/**
	 * 	Set Price for Product and PriceList
	 * 	@param M_PriceList_ID price list
	 */
	public void setPrice (int M_PriceList_ID)
	{
		if (getM_Product_ID() == 0)
			return;
		//
		log.fine("M_PriceList_ID=" + M_PriceList_ID);
		boolean isSOTrx = false;
		MProductPricing pp = new MProductPricing (getM_Product_ID(), 
			getC_BPartner_ID(), getQty(), isSOTrx);
		pp.setM_PriceList_ID(M_PriceList_ID);
	//	pp.setPriceDate(getDateOrdered());
		//
		setPriceActual (pp.getPriceStd());
	}	//	setPrice

	/**
	 * 	Calculate Line Net Amt
	 */
	public void setLineNetAmt ()
	{
		BigDecimal lineNetAmt = getQty().multiply(getPriceActual());
		super.setLineNetAmt (lineNetAmt);
	}	//	setLineNetAmt
	
	
	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (getLine() == 0)
		{
			String sql = "SELECT COALESCE(MAX(Line),0)+10 FROM M_RequisitionLine WHERE M_Requisition_ID=?";
			int ii = DB.getSQLValue (get_TrxName(), sql, getM_Requisition_ID());
			setLine (ii);
		}
		if (getPriceActual().compareTo(Env.ZERO) == 0)
			setPrice();
		setLineNetAmt();
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save.
	 * 	Update Total on Header
	 *	@param newRecord if new record
	 *	@param success save was success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		return updateHeader();
	}	//	afterSave

	
	/**
	 * 	After Delete
	 *	@param success
	 *	@return true/false
	 */
	protected boolean afterDelete (boolean success)
	{
		if (!success)
			return success;
		return updateHeader();
	}	//	afterDelete
	
	/**
	 * 	Update Header
	 *	@return header updated
	 */
	private boolean updateHeader()
	{
		log.fine("");
		String sql = "UPDATE M_Requisition r"
			+ " SET TotalLines="
				+ "(SELECT SUM(LineNetAmt) FROM M_RequisitionLine rl "
				+ "WHERE r.M_Requisition_ID=rl.M_Requisition_ID) "
			+ "WHERE M_Requisition_ID=" + getM_Requisition_ID();
		int no = DB.executeUpdate(sql, get_TrxName());
		if (no != 1)
			log.log(Level.SEVERE, "Header update #" + no);
		m_parent = null;
		return no == 1;
	}	//	updateHeader
	
}	//	MRequisitionLine
