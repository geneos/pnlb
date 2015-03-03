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
 * Contributor(s): Victor Perez
 
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
 *  @version $Id: CalloutOrder.java,v 1.11 2004/03/22 07:15:03 jjanke Exp $
 */
public class CalloutMRP extends CalloutEngine
{
/**	Debug Steps			*/
	private boolean steps = false;
        
        public String OrderLine(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
                setCalloutActive(true);
                String sql =  new String("SELECT mrp.MPC_MRP_ID FROM MPC_MRP mrp WHERE mrp.C_OrderLine_ID = ? ");
                //MOrderLine ol = new MOrderLine(Env.getCtx(), C_OrderLine_ID);
                
                Integer C_OrderLine_ID = (Integer)mTab.getValue("C_OrderLine_ID");
                Integer M_Product_ID = (Integer)mTab.getValue("M_Product_ID");
                
                if (C_OrderLine_ID !=  null)
                {    
                String Desc = (String)mTab.getValue("Description");
                Timestamp Today = new Timestamp(System.currentTimeMillis());
                String Name = Today.toString();
                BigDecimal QtyOrdered = (BigDecimal)mTab.getValue("QtyOrdered");
                BigDecimal QtyDelivered = (BigDecimal)mTab.getValue("QtyDelivered");
                Timestamp  DatePromised  = (Timestamp)mTab.getValue("DatePromised");
                Timestamp  DateOrdered  = (Timestamp)mTab.getValue("DateOrdered");
                //int M_Product_ID = ((Integer)mTab.getValue("M_Product_ID")).intValue();
                int M_Warehouse_ID = ((Integer)mTab.getValue("M_Warehouse_ID")).intValue();
                int C_Order_ID = ((Integer)mTab.getValue("C_Order_ID")).intValue();
                int C_BPartner_ID = ((Integer)mTab.getValue("C_BPartner_ID")).intValue();
                boolean IsSOTrx = "Y".equals(Env.getContext(ctx, WindowNo, "IsSOTrx"));
                
                  
                
                
                    PreparedStatement pstmt = null;
                    try
                    {
			pstmt = DB.prepareStatement (sql);
                        pstmt.setInt(1, C_OrderLine_ID.intValue());
                        ResultSet rs = pstmt.executeQuery ();                        
                        
                        while (rs.next())
                        {   
                            MMPCMRP mrp = new MMPCMRP(Env.getCtx(), rs.getInt(1),"MPC_MRP");                            
                            if(QtyOrdered.subtract(QtyDelivered).compareTo(Env.ZERO) > 0)
                            {                           
                            mrp.setDescription(Desc);
                            mrp.setC_BPartner_ID(C_BPartner_ID);
                            mrp.setQty(QtyOrdered.subtract(QtyDelivered));
                            mrp.setDatePromised(DatePromised);
                            mrp.setDateOrdered(DateOrdered);
                            mrp.setM_Product_ID(M_Product_ID.intValue());
                            mrp.setM_Warehouse_ID(M_Warehouse_ID);                            
                            mrp.save();
                            }
                            else
                            mrp.delete(true);
                            
                        }
                        
                        if (rs.getRow() == 0 && QtyOrdered.subtract(QtyDelivered).compareTo(Env.ZERO) > 0)
                        {
                             MMPCMRP mrp = new MMPCMRP(Env.getCtx(), 0,"MPC_MRP");                                          
                             mrp.setC_OrderLine_ID(C_OrderLine_ID.intValue());
                             mrp.setC_BPartner_ID(C_BPartner_ID);
                             mrp.setName(Name);
                             mrp.setDescription(Desc);
                             mrp.setC_Order_ID(C_Order_ID);
                             mrp.setQty(QtyOrdered.subtract(QtyDelivered));
                             mrp.setDatePromised(DatePromised);
                             mrp.setDateOrdered(DateOrdered);
                             mrp.setM_Product_ID(M_Product_ID.intValue());
                             mrp.setM_Warehouse_ID(M_Warehouse_ID);
                             
                             //mrp.setS_Resource_ID();
                             
                                                                                                       
                             if (IsSOTrx)
                             {    
                             mrp.setType("D");
                             mrp.setTypeMRP("SOO");
                             }
                             else
                             {
                             mrp.setType("S");
                             mrp.setTypeMRP("POO");                                 
                             }
                             mrp.save();                             
                        }
                        
                        rs.close();
                        pstmt.close();                                               

                    }
                    catch (Exception e)
                    {
                        //log.error ("doIt - " + sql, e);
                        System.out.println("doIt - " + sql + e);
                    }
                    
                } // C_OrderLine_ID !=  null                 
                    
            return "";
        }
        
        public String MPCOrder(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
            
                setCalloutActive(true);
                String sql =  new String("SELECT mrp.MPC_MRP_ID FROM MPC_MRP mrp WHERE mrp.MPC_Order_ID = ? ");
                //MOrderLine ol = new MOrderLine(Env.getCtx(), C_OrderLine_ID);
                
                Integer MPC_Order_ID = ((Integer)mTab.getValue("MPC_Order_ID"));
                
                if (MPC_Order_ID !=  null)
                {                 
                String Desc = (String)mTab.getValue("Description");
                
                Timestamp Today = new Timestamp(System.currentTimeMillis());
                String Name = Today.toString();
                
                BigDecimal QtyOrdered = (BigDecimal)mTab.getValue("QtyOrdered");
                BigDecimal QtyDelivered = (BigDecimal)mTab.getValue("QtyDelivered");
                Timestamp  DatePromised  = (Timestamp)mTab.getValue("DatePromised");
                Timestamp  DateOrdered  = (Timestamp)mTab.getValue("DateOrdered");
                int M_Product_ID = ((Integer)mTab.getValue("M_Product_ID")).intValue();
                int M_Warehouse_ID = ((Integer)mTab.getValue("M_Warehouse_ID")).intValue();
                //int C_Order_ID = ((Integer)mTab.getValue("C_Order_ID")).intValue();
                
                
                   
                    MMPCOrder o = new MMPCOrder(Env.getCtx(), MPC_Order_ID.intValue(),"MPC_Order");

                    PreparedStatement pstmt = null;
                    try
                    {
			pstmt = DB.prepareStatement (sql);
                        pstmt.setInt(1, MPC_Order_ID.intValue());
                        ResultSet rs = pstmt.executeQuery ();                        
                        
                        while (rs.next())
                        {                                                         
                            MMPCMRP mrp = new MMPCMRP(Env.getCtx(), rs.getInt(1),"MPC_MRP");
                            if(QtyOrdered.subtract(QtyDelivered).compareTo(Env.ZERO) > 0)
                            { 
                                mrp.setDescription(Desc);                            
                                mrp.setQty(QtyOrdered.subtract(QtyDelivered));
                                mrp.setDatePromised(DatePromised);
                                mrp.setDateOrdered(DateOrdered);
                                mrp.setM_Product_ID(M_Product_ID);
                                mrp.setM_Warehouse_ID(M_Warehouse_ID);                            
                                mrp.save();
                            }
                            else
                                mrp.delete(true);
                        }
                        
                        if (rs.getRow() == 0 || QtyOrdered.subtract(QtyDelivered).compareTo(Env.ZERO) > 0)
                        {
                             MMPCMRP mrp = new MMPCMRP(Env.getCtx(), 0,"MPC_MRP");
                             
                            
                             mrp.setMPC_Order_ID(MPC_Order_ID.intValue());
                             mrp.setDescription(Desc);
                             mrp.setName(Name);                             
                             //mrp.setC_Order_ID(o.getC_Order_ID());
                             mrp.setQty(QtyOrdered.subtract(QtyDelivered));
                             mrp.setDatePromised(DatePromised);
                             mrp.setDateOrdered(DateOrdered);
                             mrp.setM_Product_ID(M_Product_ID);
                             mrp.setM_Warehouse_ID(M_Warehouse_ID);
                             //mrp.setS_Resource_ID(); 
                             mrp.setType("S");
                             mrp.setTypeMRP("MOP");
                             mrp.save(); 

                        }
                        
                        rs.close();
                        pstmt.close();                                               

                    }
                    catch (Exception e)
                    {
                        //log.error ("doIt - " + sql, e);
                        System.out.println("doIt - " + sql + e);
                    }
                    
                }
                    
            return "";
        } 
        
        public String MPCOrderLine(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
                setCalloutActive(true);
                String sql =  new String("SELECT mrp.MPC_MRP_ID FROM MPC_MRP mrp WHERE mrp.MPC_Order_BOMLine_ID = ? ");
                //MOrderLine ol = new MOrderLine(Env.getCtx(), C_OrderLine_ID);
                Integer MPC_Order_BOMLine_ID = ((Integer)mTab.getValue("MPC_Order_ID"));
                
                if (MPC_Order_BOMLine_ID != null)
                {    
                String Desc = (String)mTab.getValue("Description");
                
                Timestamp Today = new Timestamp(System.currentTimeMillis());
                String Name = Today.toString();
                
                BigDecimal QtyRequiered = (BigDecimal)mTab.getValue("QtyRequiered");
                BigDecimal QtyDelivered = (BigDecimal)mTab.getValue("QtyDelivered");
                Timestamp  DatePromised  = (Timestamp)mTab.getValue("DatePromised");
                Timestamp  DateOrdered  = (Timestamp)mTab.getValue("DateOrdered");
                int M_Product_ID = ((Integer)mTab.getValue("M_Product_ID")).intValue();
                int M_Warehouse_ID = ((Integer)mTab.getValue("M_Warehouse_ID")).intValue();
                
                
                  
                    
                MMPCOrderBOMLine ol = new MMPCOrderBOMLine(Env.getCtx(), MPC_Order_BOMLine_ID.intValue(),"MPC_Order_BOM_Line");
                MMPCOrder o = new MMPCOrder(Env.getCtx(), ol.getMPC_Order_ID(),"MPC_Order");

                    PreparedStatement pstmt = null;
                    try
                    {
			pstmt = DB.prepareStatement (sql);
                        pstmt.setInt(1, MPC_Order_BOMLine_ID.intValue());
                        ResultSet rs = pstmt.executeQuery ();                        
                        
                        while (rs.next())
                        {                                                         
                            MMPCMRP mrp = new MMPCMRP(Env.getCtx(), rs.getInt(1),"MPC_MRP");
                            
                            if(QtyRequiered.subtract(QtyDelivered).compareTo(Env.ZERO) > 0)
                            {
                            mrp.setDescription(o.getDescription());                            
                            mrp.setQty(ol.getQtyRequiered().subtract(ol.getQtyDelivered()));
                            mrp.setDatePromised(o.getDatePromised());
                            mrp.setDateOrdered(o.getDateOrdered());
                            mrp.setM_Product_ID(ol.getM_Product_ID());
                            mrp.setM_Warehouse_ID(ol.getM_Warehouse_ID());                            
                            mrp.save();
                            }
                            else 
                            mrp.delete(true);    
                        }
                        
                        if (rs.getRow() == 0 || QtyRequiered.subtract(QtyDelivered).compareTo(Env.ZERO) > 0)
                        {
                             MMPCMRP mrp = new MMPCMRP(Env.getCtx(), 0,"MPC_MRP");                              
                             //MOrder o = new MOrder(Env.getCtx(), ol.getC_Order_ID());
                             
                             mrp.setMPC_Order_BOMLine_ID(MPC_Order_BOMLine_ID.intValue());
                             mrp.setDescription(Desc);
                             mrp.setName(Name);
                             mrp.setMPC_Order_ID(o.getMPC_Order_ID());
                             mrp.setQty(QtyRequiered.subtract(QtyDelivered));
                             mrp.setDatePromised(DatePromised);
                             mrp.setDateOrdered(DateOrdered);
                             mrp.setM_Product_ID(M_Product_ID);
                             mrp.setM_Warehouse_ID(M_Warehouse_ID);
                             //mrp.setS_Resource_ID(); 
                             mrp.setType("D");
                             mrp.setTypeMRP("MOP");
                             mrp.save();         
                        }
                        
                        rs.close();
                        pstmt.close();                                               

                    }
                    catch (Exception e)
                    {
                        //log.error ("doIt - " + sql, e);
                        System.out.println("doIt - " + sql + e);
                    }
                    
                }    
                                    
                return "";
        }
}	

