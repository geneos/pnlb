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
import org.compiere.util.*;
import org.compiere.model.*;

/**
 *	Order Callouts.
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutOrder.java,v 1.31 2005/04/20 04:55:24 jjanke Exp $
 */
public class CalloutOrderBodevidrio extends CalloutEngine
{
	/**	Debug Steps			*/
	private boolean steps = false;

	/**
	 *	Order Line - Quantity.
	 *		- called from C_UOM_ID, QtyEntered, QtyOrdered
	 *		- enforces qty UOM relationship
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 */
	public String qty (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		
		int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, "M_Product_ID");
		if (steps) log.warning("init - M_Product_ID=" + M_Product_ID + " - " );
		BigDecimal QtyOrdered, QtyEntered, PriceActual, PriceEntered;
		
		//	No Product
		if (M_Product_ID == 0)
		{
			QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
			mTab.setValue("QtyOrdered", QtyEntered);
		}
		
		
  // fjv e-evolution to bodevridio 
                if (("Y".equals(Env.getContext(ctx, WindowNo, "IsSOTrx"))) && (M_Product_ID != 0))
		{
			MProduct product = MProduct.get (ctx, M_Product_ID);
			if (product.isStocked())
			{
				int M_Warehouse_ID = Env.getContextAsInt(ctx, WindowNo, "M_Warehouse_ID");
                                QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
				BigDecimal available = MStorage.getQtyAvailable
					(M_Warehouse_ID, M_Product_ID, null);
                                BigDecimal newavailable=Env.ZERO;
                                System.out.println("QtyEntered" +QtyEntered +" " +available);
                                  if (available != null)
                                  {
                                    newavailable = available.subtract(QtyEntered);
			            
                                  }
                                  else
                                  {   
                                    newavailable = Env.ZERO;
				    available=Env.ZERO;
				    	
                                  }
				if (available == null)
					javax.swing.JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"NoQtyAvailable"), available.toString() , javax.swing.JOptionPane.INFORMATION_MESSAGE);						
				else if (newavailable.compareTo(Env.ZERO) < 0)
					javax.swing.JOptionPane.showMessageDialog(null,"No hay cantidad disponible, solo hay disponibles: ", available.toString() , javax.swing.JOptionPane.INFORMATION_MESSAGE);
					
			}
		}
		// end fjv e-evolution bodevidrio

		
		
		return "";
	}	//	qty

   // fjv e-evolution bodevidrio
	
        /**
	 *	Order Line - metros 2 bodevidrio
	 * 		- multiplies the fields qty, horizontal and vertical
	 * 		- sets Description and QtyOrdered
	
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 */
	public String metros2 (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		Integer C_UOM_ID = (Integer)mTab.getValue("C_UOM_ID");;
		if (C_UOM_ID == null || C_UOM_ID.intValue() == 0)
			return "";
		BigDecimal QtyOrdered, QtyEntered, Qty,Horizontal,Vertical,Total;
		Total=Env.ONE;
                
	  // fjv e-evolution bodevidrio
                        if (C_UOM_ID.intValue()==1000017)
                        {
                            Qty= (BigDecimal)mTab.getValue("Qty");
                            Horizontal= (BigDecimal)mTab.getValue("Horizontal");
                            Vertical= (BigDecimal)mTab.getValue("Vertical");
                            
                            
                            Total =Qty.multiply(Horizontal).multiply(Vertical);
                            Total=Total.divide(Env.ONE,3,BigDecimal.ROUND_HALF_UP);
                             Qty=Qty.divide(Env.ONE,0,BigDecimal.ROUND_HALF_UP);
                              Horizontal=Horizontal.divide(Env.ONE,3,BigDecimal.ROUND_HALF_UP);
                              Vertical=Vertical.divide(Env.ONE,3,BigDecimal.ROUND_HALF_UP);
                              System.out.println("total ************** " +Total);
                            mTab.setValue("QtyOrdered", Total);
                            String Description = Qty +"  Pieza(s) de " +Horizontal +"  X  " +Vertical;
                            System.out.println("desc ************** " +Total);
                            mTab.setValue("Description", Description);
                        }
                        // end e-evolution bodevidrio
		//
		return "";
	}	//	metros2   
// end e-evolution bodevidrio
	
}	//	CalloutOrderBv


