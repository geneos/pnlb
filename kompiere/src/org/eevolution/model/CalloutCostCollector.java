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
import org.eevolution.model.*;

/**
 *	Order Callouts.
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutOrder.java,v 1.31 2005/04/20 04:55:24 jjanke Exp $
 */
public class CalloutCostCollector extends CalloutEngine
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
	public String Manufacture (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		setCalloutActive(true);
		int MPC_Order_ID = Env.getContextAsInt(ctx, WindowNo, "MPC_Order_ID");
		if (steps) log.warning("init - MPC_Order_ID=" + MPC_Order_ID + " - " );
		BigDecimal QtyOrdered, QtyEntered, PriceActual, PriceEntered;
		
//		//	No Product
//		if (MPC_Order_ID == 0)
//		{
//			QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
//			mTab.setValue("QtyOrdered", QtyEntered);
//		}
		
		
  // fjv e-evolution to bodevridio 
                if ((MPC_Order_ID != 0))
		{
                    String sql =  new String("SELECT MPC_Order_Workflow_ID FROM MPC_Order_Workflow  WHERE MPC_Order_ID = ? ");
			MMPCOrder order =  new MMPCOrder(Env.getCtx(), MPC_Order_ID, "MPC_Order_ID");
			//order.getS_Resource_ID();
                        mTab.setValue("S_Resource_ID", new Integer(order.getS_Resource_ID()));
                        mTab.setValue("M_Product_ID", new Integer(order.getM_Product_ID()));
                        mTab.setValue("M_AttributeSetInstance_ID", new Integer(order.getM_AttributeSetInstance_ID()));
                        mTab.setValue("M_Warehouse_ID", new Integer(order.getM_Warehouse_ID()));
                        mTab.setValue("MovementQty", order.getQtyEntered());
                        
                         PreparedStatement pstmt = null;
                    try
                    {
			pstmt = DB.prepareStatement (sql,null);
                        pstmt.setInt(1, MPC_Order_ID);
                        ResultSet rs = pstmt.executeQuery ();                        
                        
                        while (rs.next())
                        {                                                         
                            //MMPCMRP mrp = new MMPCMRP(Env.getCtx(), rs.getInt(1),"MPC_MRP");
                            MMPCOrderWorkflow wflow = new MMPCOrderWorkflow(Env.getCtx(),rs.getInt(1),"MPC_Order_Workflow");
                           mTab.setValue("MPC_Order_Workflow_ID", wflow.getMPC_Order_Workflow_ID());
                           mTab.setValue("DurationUnit",wflow.getDurationUnit());
                           //mTab.setValue("C_UOM_ID",wflow.getDurationUnit());
                           
                        }
                        rs.close();
                        pstmt.close();                                               

                    }
                    catch (SQLException e)
                    {
                        //log.error ("doIt - " + sql, e);
                        System.out.println("doIt - " + sql + e);
                    }
                        
                       // mTab.setValue("MPC_Order_Workflow_ID", order.getAD_Workflow_ID());
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
	public String node (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
            setCalloutActive(true);
		Integer MPC_Order_Node_ID = (Integer)mTab.getValue("MPC_Order_Node_ID");
               // 
		if (MPC_Order_Node_ID == null || MPC_Order_Node_ID.intValue() == 0)
			return "";
		
                if(MPC_Order_Node_ID.intValue()!=0)
                {
                    //System.out.println("****** Node " +MPC_Order_Node_ID.toString());
                    MMPCOrderNode ordernode =  new MMPCOrderNode(Env.getCtx(), MPC_Order_Node_ID.intValue(), "MPC_Order_Node_ID");
                    //System.out.println("****** Node 2" +ordernode.isSubcontracting());
                    if (ordernode.isSubcontracting())  
                        mTab.setValue("IsSubcontracting", "Y");
                    else
                        mTab.setValue("IsSubcontracting", "N");
//crp temp
//                     if (ordernode.isBatchTime())  
//                        mTab.setValue("IsBatchTime", "Y");
//                    else
//                        mTab.setValue("IsBatchTime", "N");
                }
                
               //   mTab.setValue("IsBatchTime",ordernode.get.getIsSubcontracting());
                
		//
		return "";
	}	//	node  
        
        public String MovementType (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
            setCalloutActive(true);
		Integer C_DocType_ID = (Integer)mTab.getValue("C_DocType_ID");
               // 
		if (C_DocType_ID == null || C_DocType_ID.intValue() == 0)
			return "";
		
                if(C_DocType_ID.intValue()!=0)
                {
                     String sql =  new String("SELECT DocBaseType FROM C_DocType  WHERE C_DocType_ID = ? ");
                          PreparedStatement pstmt = null;
                    try
                    {
			pstmt = DB.prepareStatement (sql,null);
                        pstmt.setInt(1, C_DocType_ID.intValue());
                        ResultSet rs = pstmt.executeQuery ();                        
                        
                        while (rs.next())
                        {                                                         
                            //MMPCMRP mrp = new MMPCMRP(Env.getCtx(), rs.getInt(1),"MPC_MRP");
                           if (rs.getString(1).equals("MOA")||rs.getString(1).equals("MOR")||rs.getString(1).equals("MOP"))
                           {
                               mTab.setValue("MovementType", "P+");
                           }
                           else if (rs.getString(1).equals("MOI"))
                               mTab.setValue("MovementType", "P-");
                           
                        }
                        rs.close();
                        pstmt.close();                                               

                    }
                    catch (SQLException e)
                    {
                        //log.error ("doIt - " + sql, e);
                        System.out.println("doIt - " + sql + e);
                    }
                }
                
               //   mTab.setValue("IsBatchTime",ordernode.get.getIsSubcontracting());
                
		//
		return "";
	}	//	node  
// end e-evolution 
	
}	//	CalloutCostCollector


