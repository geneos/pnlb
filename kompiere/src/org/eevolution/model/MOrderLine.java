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
package org.eevolution.model;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.JOptionPane;

import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.util.*;

/**
 *  Order Line Model.
 * 	<code>
 * 			MOrderLine ol = new MOrderLine(m_order);
			ol.setM_Product_ID(wbl.getM_Product_ID());
			ol.setQtyOrdered(wbl.getQuantity());
			ol.setPrice();
			ol.setPriceActual(wbl.getPrice());
			ol.setTax();
			ol.save();

 *	</code>
 *  @author Jorg Janke
 *  @version $Id: MOrderLine.java,v 1.63 2005/12/13 00:15:27 jjanke Exp $
 */
public class MOrderLine extends org.compiere.model.MOrderLine
{
	/**
	 *  Default Constructor
	 *  @param ctx context
	 *  @param  C_OrderLine_ID  order line to load
	 *  @param trxName trx name
	 */
	public MOrderLine (Properties ctx, int C_OrderLine_ID, String trxName)
	{
		super (ctx, C_OrderLine_ID, trxName);		
	}	//	MOrderLine
	
	/**
	 *  Parent Constructor.
	 		ol.setM_Product_ID(wbl.getM_Product_ID());
			ol.setQtyOrdered(wbl.getQuantity());
			ol.setPrice();
			ol.setPriceActual(wbl.getPrice());
			ol.setTax();
			ol.save();
	 *  @param  order parent order
	 */
	public MOrderLine (MOrder order)
	{
		super(order);
	}	//	MOrderLine
        
        
	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 */
	public MOrderLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MOrderLine
	
	
	
	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord
	 *	@return true if it can be sabed
	 */
	protected boolean beforeSave (boolean newRecord)
	{
        if (getCantidadLineas()==MOrder.getMAXLines()&&newRecord&&!getParent().isSOTrx()){
            JOptionPane.showMessageDialog(null, "Se alcanzo el limite de lineas para esta orden", "Linea no guardada", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return super.beforeSave(newRecord);
	}	//	beforeSave

	
	/**
	 * 	Before Delete
	 *	@return true if it can be deleted
	 */
	protected boolean beforeDelete ()
	{
		
		return super.beforeDelete();
	}	//	beforeDelete
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return saved
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		
		/*
		 * 03-05-2011 Camarzana Mariano
		 * Modificacion realizada para que cargue los registros MPC_MRP solo en el caso que sea una
		 * orden de compras 
		 */
		MOrder order = new MOrder(getCtx(),getC_Order_ID(),get_TrxName());        
		if (!order.isSOTrx())
                                      MMPCMRP.C_OrderLine(this,get_TrxName(),false);
                                return super.afterSave (newRecord, success);                
	}	//	afterSave

	/**
	 * 	After Delete
	 *	@param success success
	 *	@return deleted
	 */
	protected boolean afterDelete (boolean success)
	{		                
		MOrder order = new MOrder(getCtx(),getC_Order_ID(),get_TrxName());        
		if (!order.isSOTrx())        
			MMPCMRP.C_OrderLine(this,get_TrxName(),true);		
		return super.afterDelete(success);
	}	//	afterDelete

    private int getCantidadLineas(){
        MOrder o = this.getParent();
        return o.getLines().length;
    }
}	//	MOrderLine
