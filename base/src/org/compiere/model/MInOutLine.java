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

import javax.swing.JOptionPane;

import org.compiere.util.*;

/**
 * 	InOut Line
 *
 *  @author Jorg Janke
 *  @version $Id: MInOutLine.java,v 1.44 2005/10/28 00:59:28 jjanke Exp $
 */
public class MInOutLine extends X_M_InOutLine
{
	/**
	 * 	Get Ship lines Of Order Line
	 *	@param ctx context
	 *	@param C_OrderLine_ID line
	 *	@param where optional addition where clause
	 *	@return array of receipt lines
	 */
	public static MInOutLine[] getOfOrderLine (Properties ctx, 
		int C_OrderLine_ID, String where, String trxName)
	{
		ArrayList<MInOutLine> list = new ArrayList<MInOutLine>();
		String sql = "SELECT * FROM M_InOutLine WHERE C_OrderLine_ID=?";
		if (where != null && where.length() > 0)
			sql += " AND " + where;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, C_OrderLine_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MInOutLine(ctx, rs, trxName));
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
		MInOutLine[] retValue = new MInOutLine[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getOfOrderLine

	/**
	 * 	Get Ship lines Of Order Line
	 *	@param ctx context
	 *	@param C_OrderLine_ID line
	 *	@return array of receipt lines2
	 */
	public static MInOutLine[] get (Properties ctx, int C_OrderLine_ID, String trxName)
	{
		ArrayList<MInOutLine> list = new ArrayList<MInOutLine>();
		String sql = "SELECT * FROM M_InOutLine WHERE C_OrderLine_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, C_OrderLine_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MInOutLine(ctx, rs, trxName));
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
		MInOutLine[] retValue = new MInOutLine[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getOfOrderLine

	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MInOutLine.class);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_InOutLine_ID id
	 *	@param trxName trx name
	 */
	public MInOutLine (Properties ctx, int M_InOutLine_ID, String trxName)
	{
		super (ctx, M_InOutLine_ID, trxName);
		if (M_InOutLine_ID == 0)
		{
		//	setLine (0);
		//	setM_Locator_ID (0);
		//	setC_UOM_ID (0);
		//	setM_Product_ID (0);
			setM_AttributeSetInstance_ID(0);
		//	setMovementQty (Env.ZERO);
			setConfirmedQty(Env.ZERO);
			setPickedQty(Env.ZERO);
			setScrappedQty(Env.ZERO);
			setTargetQty(Env.ZERO);
			setIsInvoiced (false);
			setIsDescription (false);
		}
	}	//	MInOutLine

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 */
	public MInOutLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MInOutLine

	/**
	 *  Parent Constructor
	 *  @param inout parent
	 */
	public MInOutLine (MInOut inout)
	{
		this (inout.getCtx(), 0, inout.get_TrxName());
		setClientOrg (inout);
		setM_InOut_ID (inout.getM_InOut_ID());
		setM_Warehouse_ID (inout.getM_Warehouse_ID());
		m_parent = inout;
	}	//	MInOutLine

	/**	Product					*/
	private MProduct 		m_product = null;
	/** Warehouse				*/
	private int				m_M_Warehouse_ID = 0;
	/** Parent					*/
	private MInOut			m_parent = null;
	
	/**
	 * 	Get Parent
	 *	@return parent
	 */
	public MInOut getParent()
	{
		if (m_parent == null)
			m_parent = new MInOut (getCtx(), getM_InOut_ID(), get_TrxName());
		return m_parent;
	}	//	getParent
	
	/**
	 * 	Set Order Line.
	 * 	Does not set Quantity!
	 *	@param oLine order line
	 *	@param M_Locator_ID locator
	 * 	@param Qty used only to find suitable locator
	 */
	public void setOrderLine (MOrderLine oLine, int M_Locator_ID, BigDecimal Qty)
	{
		setC_OrderLine_ID(oLine.getC_OrderLine_ID());
		setLine(oLine.getLine());
		setC_UOM_ID(oLine.getC_UOM_ID());
		MProduct product = oLine.getProduct();
		if (product == null)
		{
			set_ValueNoCheck("M_Product_ID", null);
			set_ValueNoCheck("M_AttributeSetInstance_ID", null);
			set_ValueNoCheck("M_Locator_ID", null);
		}
		else
		{
			setM_Product_ID(oLine.getM_Product_ID());
			setM_AttributeSetInstance_ID(oLine.getM_AttributeSetInstance_ID());
			//
			if (product.isItem())
			{
				if (M_Locator_ID == 0)
					setM_Locator_ID(Qty);	//	requires warehouse, product, asi
				else
					setM_Locator_ID(M_Locator_ID);
			}
			else
				set_ValueNoCheck("M_Locator_ID", null);
		}
		setC_Charge_ID(oLine.getC_Charge_ID());
		setDescription(oLine.getDescription());
		setIsDescription(oLine.isDescription());
	}	//	setOrderLine
	
	/**
	 * 	Set Invoice Line.
	 * 	Does not set Quantity!
	 *	@param iLine invoice line
	 *	@param M_Locator_ID locator
	 *	@param Qty qty only fo find suitable locator
	 */
	public void setInvoiceLine (MInvoiceLine iLine, int M_Locator_ID, BigDecimal Qty)
	{
		setC_OrderLine_ID(iLine.getC_OrderLine_ID());
		setLine(iLine.getLine());
		setC_UOM_ID(iLine.getC_UOM_ID());
		int M_Product_ID = iLine.getM_Product_ID();
		if (M_Product_ID == 0)
		{
			set_ValueNoCheck("M_Product_ID", null);
			set_ValueNoCheck("M_Locator_ID", null);
			set_ValueNoCheck("M_AttributeSetInstance_ID", null);
		}
		else
		{
			setM_Product_ID(M_Product_ID);
			setM_AttributeSetInstance_ID(iLine.getM_AttributeSetInstance_ID());			
			if (M_Locator_ID == 0)
				setM_Locator_ID(Qty);	//	requires warehouse, product, asi
			else
				setM_Locator_ID(M_Locator_ID);
		}
		setC_Charge_ID(iLine.getC_Charge_ID());
		setDescription(iLine.getDescription());
		setIsDescription(iLine.isDescription());
	}	//	setOrderLine
	
	/**
	 * 	Get Warehouse
	 *	@return Returns the m_Warehouse_ID.
	 */
	public int getM_Warehouse_ID()
	{
		if (m_M_Warehouse_ID == 0)
			m_M_Warehouse_ID = getParent().getM_Warehouse_ID();
		return m_M_Warehouse_ID;
	}	//	getM_Warehouse_ID
	
	/**
	 * 	Set Warehouse
	 *	@param warehouse_ID The m_Warehouse_ID to set.
	 */
	public void setM_Warehouse_ID (int warehouse_ID)
	{
		m_M_Warehouse_ID = warehouse_ID;
	}	//	setM_Warehouse_ID

	/**
	 * 	Set (default) Locator.
	 * 	@param Qty quantity
	 * 	Assumes Warehouse is set
	 */
	public void setM_Locator_ID(BigDecimal Qty)
	{
		//	Locator esatblished
		if (getM_Locator_ID() != 0)
			return;
		//	No Product
		if (getM_Product_ID() == 0)
		{
			set_ValueNoCheck("M_Locator_ID", null);
			return;
		}
		
		//	Get existing Location
		int M_Locator_ID = MStorage.getM_Locator_ID (getM_Warehouse_ID(), 
				getM_Product_ID(), getM_AttributeSetInstance_ID(), 
				Qty, get_TrxName());
		//	Get default Location
		if (M_Locator_ID == 0)
		{
			MWarehouse wh = MWarehouse.get(getCtx(), getM_Warehouse_ID());
			M_Locator_ID = wh.getDefaultLocator().getM_Locator_ID();
		}
		setM_Locator_ID(M_Locator_ID);
	}	//	setM_Locator_ID
	
	/**
	 * 	Set Movement/Movement Qty
	 *	@param Qty Entered/Movement Qty
	 */
	public void setQty (BigDecimal Qty)
	{
		setQtyEntered(Qty);
		setMovementQty(Qty);
	}	//	setQtyInvoiced

	/**
	 * 	Get Product
	 *	@return product or null
	 */
	public MProduct getProduct()
	{
		if (m_product == null && getM_Product_ID() != 0)
			m_product = MProduct.get (getCtx(), getM_Product_ID());
		return m_product;
	}	//	getProduct
	
	/**
	 * 	Set Product
	 *	@param product product
	 */
	public void setProduct (MProduct product)
	{
		m_product = product;
		if (m_product != null)
		{
			setM_Product_ID(m_product.getM_Product_ID());
			setC_UOM_ID (m_product.getC_UOM_ID());
		}
		else
		{
			setM_Product_ID(0);
			setC_UOM_ID (0);
		}
		setM_AttributeSetInstance_ID(0);
	}	//	setProduct
	
	/**
	 * 	Set M_Product_ID
	 *	@param M_Product_ID product
	 */
	public void setM_Product_ID (int M_Product_ID, boolean setUOM)
	{
		if (setUOM)
			setProduct(MProduct.get(getCtx(), M_Product_ID));
		else
			super.setM_Product_ID (M_Product_ID);
		setM_AttributeSetInstance_ID(0);
	}	//	setM_Product_ID
	
	/**
	 * 	Set Product and UOM
	 *	@param M_Product_ID product
	 *	@param C_UOM_ID uom
	 */
	public void setM_Product_ID (int M_Product_ID, int C_UOM_ID)
	{
		if (M_Product_ID != 0)
			super.setM_Product_ID (M_Product_ID);
		super.setC_UOM_ID(C_UOM_ID);
		setM_AttributeSetInstance_ID(0);
		m_product = null;
	}	//	setM_Product_ID

	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else
			setDescription(desc + " | " + description);
	}	//	addDescription
	
	
	private int MAX_PRODUCTOS = 18;
	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord new
	 *	@return save
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		
                MInOut inOut = new MInOut(getCtx(),getM_InOut_ID(),get_TrxName());

                /**
		 * VERIFICAR QUE NO SUPERE LAS 18 LINEAS
		 * @author Daniel
		 */	
		
                if (is_new() && inOut.isSOTrx() /*&& (!inOut.getDocAction().equals(MOrder.DOCACTION_Re_Activate) && 
				!inOut.getDocAction().equals(MInOut.DOCACTION_Invalidate) && 
				!inOut.getDocAction().equals(MInOut.DOCACTION_Void)&& 
				!inOut.getDocAction().equals(MInOut.ACTION_Close))*/)
        	{	
				MInOutLine[] lines = inOut.getLines();
				int count = 0;
				for (int i=0; i<lines.length; i++)
				{
					/*if (lines[i].getDescription()!=null)
						count += 2;
					else*/
						count ++;
				}
//				 Se incrementa por la linea que se quiere guardar ahora
	    		count++;
				
		    	//if (inOut.getDocAction().equals(MInOut.DOCACTION_Close))
		    		if (count>MAX_PRODUCTOS)
						{
							JOptionPane.showMessageDialog(null, "No se ha podido guardar la línea, ha alcanzado el límite para este remito.", "Salvando", JOptionPane.INFORMATION_MESSAGE);
							return false;
						}
        	}
		
		/**
		 * FIN
		 */
		
		log.fine("");
		//	Get Line No
		if (getLine() == 0)
		{
			String sql = "SELECT COALESCE(MAX(Line),0)+10 FROM M_InOutLine WHERE M_InOut_ID=?";
			int ii = DB.getSQLValue (get_TrxName(), sql, getM_InOut_ID());
			setLine (ii);
		}
		//	UOM
		if (getC_UOM_ID() == 0)
			setC_UOM_ID (Env.getContextAsInt(getCtx(), "#C_UOM_ID"));
		if (getC_UOM_ID() == 0)
		{
			int C_UOM_ID = MUOM.getDefault_UOM_ID(getCtx());
			if (C_UOM_ID > 0)
				setC_UOM_ID (C_UOM_ID);
		}
		int order = Env.getContextAsInt(getCtx(), "#C_Order_ID");
		//	Order Line
		//BISion - 16/07/2009 - Santiago Ibañez
        //Comentado para que no requiera una orden de venta
        /*if (getC_OrderLine_ID() == 0)
		{
			if (getParent().isSOTrx())
			{
				log.saveError("FillMandatory", Msg.translate(getCtx(), "C_Order_ID"));
				return false;
			}
		}*/
		
	//	if (getC_Charge_ID() == 0 && getM_Product_ID() == 0)
	//		;
		
		/**	 Qty on instance ASI
		if (getM_AttributeSetInstance_ID() != 0)
		{
			MProduct product = getProduct();
			int M_AttributeSet_ID = product.getM_AttributeSet_ID();
			boolean isInstance = M_AttributeSet_ID != 0;
			if (isInstance)
			{
				MAttributeSet mas = MAttributeSet.get(getCtx(), M_AttributeSet_ID);
				isInstance = mas.isInstanceAttribute();
			}
			//	Max
			if (isInstance)
			{
				MStorage storage = MStorage.get(getCtx(), getM_Locator_ID(), 
					getM_Product_ID(), getM_AttributeSetInstance_ID(), get_TrxName());
				if (storage != null)
				{
					BigDecimal qty = storage.getQtyOnHand();
					if (getMovementQty().compareTo(qty) > 0)
					{
						log.warning("Qty - Stock=" + qty + ", Movement=" + getMovementQty());
						log.saveError("QtyInsufficient", "=" + qty); 
						return false;
					}
				}
			}
		}	/**/
		
		return true;
	}	//	beforeSave
	
	/**
	 * 	Before Delete
	 *	@return true if drafted
	 */
	protected boolean beforeDelete ()
	{
		if (getParent().getDocStatus().equals(MInOut.DOCSTATUS_Drafted))
			return true;
		log.saveError("Error", Msg.getMsg(getCtx(), "CannotDelete"));
		return false;
	}	//	beforeDelete

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MInOutLine[").append (get_ID())
			.append(",M_Product_ID=").append(getM_Product_ID())
			.append(",QtyEntered=").append(getQtyEntered())
			.append(",MovementQty=").append(getMovementQty())
			.append(",M_AttributeSetInstance_ID=").append(getM_AttributeSetInstance_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Get Base value for Cost Distribution
	 *	@param CostDistribution cost Distribution
	 *	@return base number
	 */
	public BigDecimal getBase (String CostDistribution)
	{
		if (MLandedCost.LANDEDCOSTDISTRIBUTION_Costs.equals(CostDistribution))
		{
			//	TODO Costs!
			log.severe("Not Implemented yet - Cost");
			return Env.ZERO;
		}
		else if (MLandedCost.LANDEDCOSTDISTRIBUTION_Line.equals(CostDistribution))
			return Env.ONE;
		else if (MLandedCost.LANDEDCOSTDISTRIBUTION_Quantity.equals(CostDistribution))
			return getMovementQty();
		else if (MLandedCost.LANDEDCOSTDISTRIBUTION_Volume.equals(CostDistribution))
		{
			MProduct product = getProduct();
			if (product == null)
			{
				log.severe("No Product");
				return Env.ZERO;
			}
			return getMovementQty().multiply(product.getVolume());
		}
		else if (MLandedCost.LANDEDCOSTDISTRIBUTION_Weight.equals(CostDistribution))
		{
			MProduct product = getProduct();
			if (product == null)
			{
				log.severe("No Product");
				return Env.ZERO;
			}
			return getMovementQty().multiply(product.getWeight());
		}
		//
		log.severe("Invalid Criteria: " + CostDistribution);
		return Env.ZERO;
	}	//	getBase
	
}	//	MInOutLine
