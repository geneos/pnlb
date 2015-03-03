/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2003 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * created by Victor Perez are Copyright (C) e-Evolution,SC. All Rights Reserved.
 * Contributor(s): Victor Perez.
 *****************************************************************************/
//package org.compiere.mfg.model;
package org.eevolution.model;

import java.math.*;
import java.sql.*;
import java.util.*;

import org.compiere.util.*;
import org.compiere.model.*;
import org.compiere.wf.*;

/**
 *	Order Callouts.
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutOrder.java,v 1.23 2004/08/27 21:24:12 jjanke Exp $
 */
public class CalloutOrder extends CalloutEngine
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
		if (isCalloutActive() || value == null)
			return "";
		//setCalloutActive(true);

		int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, "M_Product_ID");
		if (steps) log.warning("qty - init - M_Product_ID=" + M_Product_ID + " - " );
		BigDecimal QtyOrdered = Env.ZERO ; 
                BigDecimal QtyEntered = Env.ZERO ; //, PriceActual, PriceEntered;
		
		//	No Product
		if (M_Product_ID == 0)
		{
			QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
			mTab.setValue("QtyOrdered", QtyEntered);
		}
		//	UOM Changed - convert from Entered -> Product
		else if (mField.getColumnName().equals("C_UOM_ID"))
		{
                        
			int C_UOM_To_ID = ((Integer)value).intValue();
			QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
			QtyOrdered = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, QtyEntered);
			if (QtyOrdered == null)
				QtyOrdered = QtyEntered;
			boolean conversion = QtyEntered.compareTo(QtyOrdered) != 0;
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyOrdered", QtyOrdered);
		}
		//	QtyEntered changed - calculate QtyOrdered
		else if (mField.getColumnName().equals("QtyEntered"))
		{
			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, "C_UOM_ID");
			QtyEntered = (BigDecimal)value;
			QtyOrdered = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, QtyEntered);
			if (QtyOrdered == null)
				QtyOrdered = QtyEntered;
			boolean conversion = QtyEntered.compareTo(QtyOrdered) != 0;
			log.fine("qty - UOM=" + C_UOM_To_ID 
				+ ", QtyEntered=" + QtyEntered
				+ " -> " + conversion 
				+ " QtyOrdered=" + QtyOrdered);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyOrdered", QtyOrdered);
		}
		//	QtyOrdered changed - calculate QtyEntered
		else if (mField.getColumnName().equals("QtyOrdered"))
		{
			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, "C_UOM_ID");
			QtyOrdered = (BigDecimal)value;
			QtyEntered = MUOMConversion.convertProductTo (ctx, M_Product_ID, 
				C_UOM_To_ID, QtyOrdered);
			if (QtyEntered == null)
				QtyEntered = QtyOrdered;
			boolean conversion = QtyOrdered.compareTo(QtyEntered) != 0;
			log.fine("qty - UOM=" + C_UOM_To_ID 
				+ ", QtyOrdered=" + QtyOrdered
				+ " -> " + conversion 
				+ " QtyEntered=" + QtyEntered);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyEntered", QtyEntered);
		}
                
        String DocStatus = (String) mTab.getValue("DocStatus");
        if (!DocStatus.equals(MMPCOrder.STATUS_Completed))
        {               
                    
                	Integer MPC_Order_ID = (Integer)mTab.getValue("MPC_Order_ID");
                	if (MPC_Order_ID==null)
                		return "";
                	
                    QtyOrdered = ((BigDecimal)mTab.getValue("QtyOrdered"));
                    MMPCOrder order = new  MMPCOrder(ctx, MPC_Order_ID,null);
                    MMPCOrderBOMLine[] obl = MMPCOrder.getLines(MPC_Order_ID,null);
                    for (int i = 0 ; i < obl.length ; i ++) 
                    { 
                    	order.setBOMLineQtys(obl[i]);
                    	obl[i].save(null);
                    }                    
        }

                //setCalloutActive(true);
                return qtyBatch(ctx,WindowNo,mTab,mField,value);
                //return "";
	}	//	qty
        
    public String qtyBatch (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
                Integer AD_Workflow_ID = ((Integer)mTab.getValue("AD_Workflow_ID"));
                
                BigDecimal p_QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
                if ( AD_Workflow_ID==null)
                return "Data found";
                
                MWorkflow wf = new  MWorkflow(ctx , AD_Workflow_ID.intValue() ,null);
                BigDecimal Qty = null;
                BigDecimal QtyBatchSize = wf.getQtyBatchSize().divide(new BigDecimal(1),0,BigDecimal.ROUND_UP);
                //System.out.println(">>>>>>>>>>>>>>>>>>> p_QtyEntered" + p_QtyEntered + " QtyBatchSize" + QtyBatchSize + "Env.ZERO " + Env.ZERO + "QtyBatchSize.equals(Env.ZERO)" +  QtyBatchSize.equals(Env.ZERO) );
                if (p_QtyEntered.equals(Env.ZERO))
                    return ""; 
                if (QtyBatchSize.equals(Env.ZERO))
                    Qty = Env.ONE;
                else   
                    Qty = p_QtyEntered.divide(QtyBatchSize , 0, BigDecimal.ROUND_UP); 
                
               
 
                  
                mTab.setValue("QtyBatchs", Qty);
                mTab.setValue("QtyBatchSize", p_QtyEntered.divide(Qty , BigDecimal.ROUND_HALF_UP));
                
            return "";
        }
	
}	//	CalloutOrder

