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

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

import javax.swing.JOptionPane;

import org.compiere.model.MAttributeSet;
import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrder;
import org.compiere.model.MProduct;
import org.compiere.model.MStorage;
import org.compiere.model.MUOM;
import org.compiere.util.*;

/**
 * 	InOut Line
 *
 *  @author Jorg Janke
 *  @version $Id: MInOutLine.java,v 1.44 2005/10/28 00:59:28 jjanke Exp $
 */
public class MInOutLine extends org.compiere.model.MInOutLine
{
		
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_InOutLine_ID id
	 *	@param trxName trx name
	 */
	public MInOutLine (Properties ctx, int M_InOutLine_ID, String trxName)
	{
		super(ctx,M_InOutLine_ID,trxName);
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
		super(inout);
	}	//	MInOutLine
	
	private int MAX_PRODUCTOS = 18;
	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord new
	 *	@return save
	 */
	protected boolean beforeSave (boolean newRecord)
	{

		/**
		 * VERIFICAR QUE NO SUPERE LAS 18 LINEAS
		 * @author Daniel
		 */	
		
		org.compiere.model.MInOut inOut = new org.compiere.model.MInOut(getCtx(),getM_InOut_ID(),null);
       	if (is_new() && inOut.isSOTrx() && (!inOut.getDocAction().equals(MOrder.DOCACTION_Re_Activate) && 
				!inOut.getDocAction().equals(MOrder.DOCACTION_Invalidate) && 
				!inOut.getDocAction().equals(MOrder.DOCACTION_Void)&& 
				!inOut.getDocAction().equals(MOrder.ACTION_Close)))
        	{	
				org.compiere.model.MInOutLine[] lines = inOut.getLines();
				int count = 0;
				for (int i=0; i<lines.length; i++)
				{
					/**
	    			 * 02-11-2010 Camarzana Mariano
	    			 * Comentado provisoriamente para poder ingresar las facturas con la misma modalidad que 
	    			 * Bejerman (ingresar 18 items independientemente de las descripciones) 
	    			 */
				/*	if (lines[i].getDescription()!=null)
						count += 2;
					else*/
						count ++;
				}
				
				// Se incrementa por la linea que se quiere guardar ahora
				count++;
				// Uno más si la linea tiene descripción
				/*if (getDescription()!=null)
					count++;*/
				
				if (count>MAX_PRODUCTOS)
				{
					JOptionPane.showMessageDialog(null, "No se ha podido guardar la linea, ha alcanzado el límite para este remito.", "Salvando", JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
        	}
		
		/**
		 * FIN
		 */
		
		//fin modificacion BISion
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
		
		//	Order Line
		//BISion - 16/07/2009 - Santiago IbaÃ±ez
        //Modificado para que no requiera una OV
        /*if (getC_OrderLine_ID() == 0)
		{
			MDocType doc = MDocType.get(getCtx(),getParent().getC_DocType_ID());	
			
			if (getParent().isSOTrx() && doc.getDocBaseType().equals(MDocType.DOCBASETYPE_MaterialDelivery))
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
		
		/*
		 * 10-02-2011 Camarzana Mariano
		 * Valida que la cantidad ingresada no supere el stock del producto 
		 * Preguntar por el tipo de documento (si es Recepcion de Materiales)
		 */
		
		/*
         * 22-06-2011 Camarzana Mariano
         * Si el tipo de documento es Remito Famatina, Remito Loma Hermosa o Remito Florida
         * entonces no debe controlar stock
         */
		MDocType docType = new MDocType(getCtx(), getParent().getC_DocType_ID(), get_TrxName());
		if (getM_AttributeSetInstance_ID() != 0 && !docType.getDocBaseType().equals(MDocType.DOCBASETYPE_RecepcionMaterialesTerceros) && 
				!docType.getName().equals("Remito Loma Hermosa")&&
			    !docType.getName().equals("Remito Florida")&&
			    !docType.getName().equals("Remito Famatina"))//&& (is_ValueChanged("M_Product_ID") || is_ValueChanged("M_AttributeSetInstance_ID"))) 
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
				MStorage[] storages = MStorage.getWarehouse(getCtx(), 
					getM_Warehouse_ID(), getM_Product_ID(), getM_AttributeSetInstance_ID(), 
					M_AttributeSet_ID, false, null, true, get_TrxName());
				BigDecimal qty = Env.ZERO;
				for (int i = 0; i < storages.length; i++)
				{
					if (storages[i].getM_AttributeSetInstance_ID() == getM_AttributeSetInstance_ID())
						qty = qty.add(storages[i].getQtyOnHand());
				}
                                BigDecimal qtyOrd = getQtyEntered();

				if (qtyOrd.compareTo(qty) > 0)
				{
					log.warning("Qty - Stock=" + qty + ", Ordered=" + getQtyEntered());
					log.saveError("QtyInsufficient", "=" + qty); 
					return false;
				}
			}
	}
		return true;
	}	//	beforeSave
}	//	MInOutLine
