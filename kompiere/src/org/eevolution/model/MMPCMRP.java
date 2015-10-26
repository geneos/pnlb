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
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.eevolution.model;

import org.compiere.model.*;
import org.compiere.model.MDocType;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.wf.MWorkflow;
import org.compiere.model.X_MPC_MRP;
//import org.compiere.model.X_MPC_Order;
import org.eevolution.model.*;
import org.compiere.model.X_MPC_Order_BOMLine;

import org.compiere.util.DB;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.compiere.util.TimeUtil;

import java.util.logging.*;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;


/**
 *	MPC_MRP
 *	
 *  @author Victor Perez
 *  @version $Id: MMPCMRP.java,v 1.4 2004/05/13 06:05:22 jjanke Exp $
 */
public class MMPCMRP extends X_MPC_MRP
{

    
	/**	Cache						*/
	//private static CCache	s_cache = new CCache ("M_Product_Costing", 20);	             	
	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param M_Product_Costing_ID id
	 */
	public MMPCMRP(Properties ctx, int MPC_MRP_ID,String trxName)
	{
		super(ctx, MPC_MRP_ID,trxName);
		if (MPC_MRP_ID == 0)
		{
                 setDateSimulation(new Timestamp (System.currentTimeMillis()));    
                /*    
		setC_AcctSchema_ID(0);
                setCostCumAmt(); 
                setCostCumQty();
                setCostLLAmt();
                setCostTLAmt();
                setM_Product_ID();
                setM_Warehouse_ID();
                setMPC_Cost_Element_ID();
                stS_Resource_ID();*/
		}
	}	//	MPCCostElement

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MMPCMRP(Properties ctx, ResultSet rs,String trxName)
	{
		super(ctx, rs,trxName);
	}
        
        /**	Logger			*/
	private static CLogger log = CLogger.getCLogger(MMPCMRP.class);
        
        protected boolean afterSave(boolean newRecord, boolean success) 
        {
        
              if (!newRecord)
	      return success;
              
              /*MMPCProductPlanning pp = MMPCProductPlanning.getSupplyWarehouse( Env.getCtx() , getAD_Org_ID() , getM_Product_ID() , getM_Warehouse_ID());
              
              if(pp != null)
              {
                  setS_Resource_ID(pp.getS_Resource_ID());
                  setPlanner_ID(pp.getPlanner_ID());
              } */             
              return true;
        }
        
        public static int M_ForecastLine(MForecastLine fl,String trxName, boolean delete)
        {
        	String sql = null;
        	if (delete)
        	{
        		sql = "DELETE FROM MPC_MRP WHERE M_ForecastLine_ID = "+ fl.getM_ForecastLine_ID()  +" AND AD_Client_ID = " + fl.getAD_Client_ID();
				
                DB.executeUpdate(sql, trxName);
                return 0;
        	}
        	
            MMPCMRP mrp = null;
            //MPeriod period = new MPeriod(Env.getCtx(),fl.getC_Period_ID(),null);
            MWarehouse[] w = MWarehouse.getForOrg(Env.getCtx(),fl.getAD_Org_ID());

            
            X_M_Forecast f = new X_M_Forecast(Env.getCtx(),fl.getM_Forecast_ID(), null);
            sql =  new String("SELECT mrp.MPC_MRP_ID FROM MPC_MRP mrp WHERE mrp.M_ForecastLine_ID = ? ");            

                PreparedStatement pstmt = null;
                try
                {
                	/** BISion - 22/04/2009 - Santiago Ibañez
                     * Modificacion realizada: se agrego la transaccion trxName en prepareStatement
                     * para que al consultar los datos, tenga una version actualizada de los mismos.
                     * Desde MRPUpdate se borraban los registros y recien al final del proceso se comiteaban los
                     * cambios. Entonces la consulta anterior actualizaba los registros MRP encontrados
                     * en lugar de crearlos nuevamente.
                     */
                    pstmt = DB.prepareStatement (sql,trxName);
                    pstmt.setInt(1, fl.getM_ForecastLine_ID());
                    ResultSet rs = pstmt.executeQuery ();                        
                    boolean records = false;
                    
                    while (rs.next())
                    {                                                         
                        records = true; 
                        mrp = new MMPCMRP(Env.getCtx(), rs.getInt(1),null);
                        mrp.setDescription(f.getDescription());
                        mrp.setName("MRP");
                        mrp.setQty(fl.getQty());                                                
                        mrp.setDatePromised(fl.getDatePromised());                       
                        mrp.setDateStartSchedule(fl.getDatePromised());
                        mrp.setDateFinishSchedule(fl.getDatePromised());
                        mrp.setDateOrdered(fl.getDatePromised());
                        mrp.setM_Product_ID(fl.getM_Product_ID());
                        if(fl.getM_Warehouse_ID() == 0)
                        {	
                        int M_Warehouse_ID = DB.getSQLValue(null,"SELECT M_Warehouse_ID FROM ", fl.getAD_Org_ID());                       
                        mrp.setM_Warehouse_ID(w[0].getM_Warehouse_ID());
                        }
                        else
                        {
                        	mrp.setM_Warehouse_ID(fl.getM_Warehouse_ID());
                        }
                        mrp.setDocStatus("IP");
                        mrp.save(trxName);
                        System.out.println("MRP actualizado para el producto: "+mrp.getM_Product_ID());
                    }
                    
                    if (!records)
                    {
                         mrp = new MMPCMRP(Env.getCtx(), 0,trxName);
                         mrp.setM_ForecastLine_ID(fl.getM_ForecastLine_ID());
                         mrp.setName("MRP");
                         mrp.setDescription(f.getDescription());
                         mrp.setM_Forecast_ID(f.getM_Forecast_ID());
                         mrp.setQty(fl.getQty());
                         mrp.setDatePromised(fl.getDatePromised());
                         mrp.setDateStartSchedule(fl.getDatePromised());
                         mrp.setDateFinishSchedule(fl.getDatePromised());
                         mrp.setDateOrdered(fl.getDatePromised());
                         mrp.setM_Product_ID(fl.getM_Product_ID());
                         mrp.setM_Warehouse_ID(fl.getM_Warehouse_ID()); 
                         mrp.setDocStatus("IP");
                         mrp.setType("D");
                         mrp.setTypeMRP("FCT");                         
                         mrp.save(trxName);
                         System.out.println("MRP actualizado para el producto: "+mrp.getM_Product_ID());
                    }
                    rs.close();
                    rs = null;
                    pstmt.close();
                    pstmt = null;

                }
                catch (SQLException ex)
                {			
                    log.log(Level.SEVERE, "doIt - " + sql , ex); 
                }
                /** BISion - 07/05/2009 - Santiago Ibañez
                 * Modificacion realizada para cerrar el prepared statement 
                 * independientemente de que si haya ocurrido alguna excepcion o no
                 */
                finally{
                    try{
                        if (pstmt!=null)
                            pstmt.close();
                        pstmt = null;
                    }
                    catch(Exception e){

                    }
                }
                return mrp.getMPC_MRP_ID();
        }
        
        /*
        public static int C_Order(MOrder o, String trxName)
        {
                
            	MOrderLine[] lines = o.getLines();                 	
            	for (int i = 0 ;  i < lines.length  ; i++)
            	{            	 
            	 MMPCMRP.C_OrderLine(lines[i],null,false);
            	}
                MMPCMRP mrp = null;
                int retval=0;
                String sql =  new String("SELECT mrp.MPC_MRP_ID FROM MPC_MRP mrp WHERE mrp.C_Order_ID = ? ");
                

                    PreparedStatement pstmt = null;
                    try
                    {
                    	pstmt = DB.prepareStatement (sql);
                        pstmt.setInt(1, o.getC_Order_ID());
                        ResultSet rs = pstmt.executeQuery ();                        
                        boolean records = false;
                        
                        while (rs.next())
                        {   
                        	records = true;
                        	mrp = new MMPCMRP(Env.getCtx(), rs.getInt(1),null);
                        	mrp.setDocStatus(o.getDocStatus());                           
                            mrp.save(trxName);
                                                       
                        }                        
                        rs.close();
                        pstmt.close();        
                        
                        //if (!records)
                        if (records)
                            retval=mrp.getMPC_MRP_ID();

                    }
                    catch (SQLException ex)
                    {			
                        log.log(Level.SEVERE, "doIt - " + sql , ex); 
                    }                    

                    
                    
                    
                    return retval;
        }*/
        
        public static int C_OrderLine(MOrderLine ol, String trxName, boolean delete)
        {
                String sql = null;
        	if (delete)
                {
        		sql = "DELETE FROM MPC_MRP WHERE C_OrderLine_ID = "+ ol.getC_OrderLine_ID()  +" AND AD_Client_ID = " + ol.getAD_Client_ID();				
                        DB.executeUpdate(sql ,trxName);
                        int MPC_Order_ID = DB.getSQLValue(trxName,"SELECT MPC_Order_ID FROM MPC_Order o WHERE o.AD_Client_ID = ? AND o.C_OrderLine_ID = ? ", ol.getAD_Client_ID(),ol.getC_OrderLine_ID());            
                        if (MPC_Order_ID != -1 )
                        {
                            MMPCOrder order = new MMPCOrder(Env.getCtx(), MPC_Order_ID,trxName);
                            if (order.DOCSTATUS_Completed != order.getDocStatus() || order.DOCSTATUS_Closed != order.getDocStatus())
                                order.delete(true,trxName);
                        }
                        return 0;
        	}
        	
                MMPCMRP mrp = null;
                sql =  new String("SELECT mrp.MPC_MRP_ID FROM MPC_MRP mrp WHERE mrp.C_OrderLine_ID = ? ");
                MOrder o = new MOrder(Env.getCtx(),ol.getC_Order_ID(),null);

                    PreparedStatement pstmt = null;
                    try
                    {
                    	pstmt = DB.prepareStatement (sql,trxName);
                        pstmt.setInt(1, ol.getC_OrderLine_ID());
                        ResultSet rs = pstmt.executeQuery ();                        
                        boolean records = false;
                        
                        while (rs.next())
                        {                                                         
                            records = true; 
                            mrp = new MMPCMRP(Env.getCtx(), rs.getInt(1),trxName);
                            mrp.setDescription(ol.getDescription());
                            mrp.setName("MRP");
                                                        
                            //mrp.setQty(ol.getQtyOrdered().subtract(ol.getQtyDelivered()));
                            
                            /*      Vit4B - 07/09/2007
                             *      Modificaci�n para solucionar el problema que genera que tome QtyOrdered
                             *      miestras que deber�a toma QtyEntered porque el ordenado se va restando.
                             *      
                             *      Debe de tomar lo pendiente de entregar por una orden que es lo que se
                             *      lleva en Ordered o lo mismo ser�a la diferencia de Entered y Delivered con 
                             *      la salvedad de que no sea menor que 0
                             *
                             *      Control adicional
                             *
                             */
                            
                            

                            
                            BigDecimal diff = ol.getQtyEntered().subtract(ol.getQtyDelivered());
                            
                            
                            if(diff.compareTo(Env.ZERO) < 0)
                                mrp.setQty(Env.ZERO);
                            else
                                mrp.setQty(diff);
                            
                            
                            mrp.setDatePromised(ol.getDatePromised());
                            mrp.setDateStartSchedule(ol.getDatePromised());
                            mrp.setDateFinishSchedule(ol.getDatePromised());
                            mrp.setDateOrdered(ol.getDateOrdered());
                            mrp.setM_Product_ID(ol.getM_Product_ID());
                            mrp.setM_Warehouse_ID(ol.getM_Warehouse_ID()); 
                            mrp.setDocStatus(o.getDocStatus());
                            //mrp.setIsAvailable(true);
                            mrp.save(trxName);
                        }
                        
                        if (!records)
                        {
                             mrp = new MMPCMRP(Env.getCtx(), 0,null);                                                          
                             mrp.setC_OrderLine_ID(ol.getC_OrderLine_ID());
                             mrp.setName("MRP");
                             mrp.setDescription(ol.getDescription());
                             mrp.setC_Order_ID(ol.getC_Order_ID());

                             //mrp.setQty(ol.getQtyOrdered().subtract(ol.getQtyDelivered()));
                             
                             /*      Vit4B - 07/09/2007
                             *      Modificaci�n para solucionar el problema que genera que tome QtyOrdered
                             *      miestras que deber�a toma QtyEntered porque el ordenado se va restando.
                             *      
                             *      Debe de tomar lo pendiente de entregar por una orden que es lo que se
                             *      lleva en Ordered o lo mismo ser�a la diferencia de Entered y Delivered con 
                             *      la salvedad de que no sea menor que 0
                             *
                             *      Control adicional
                             *
                             */
                            
                             
                             
                             if(ol.getM_Product_ID() == 1006413)
                                System.out.println("PRODUCTO EN CUESTIONNNNN");
                             
                             
                             
                             
                             BigDecimal diff = ol.getQtyEntered().subtract(ol.getQtyDelivered());
                            
                            
                             if(diff.compareTo(Env.ZERO) < 0)
                                    mrp.setQty(Env.ZERO);
                             else
                                    mrp.setQty(diff);
                             
                             
                             
                             
                             mrp.setDatePromised(ol.getDatePromised());
                             mrp.setDateStartSchedule(ol.getDatePromised());
                             mrp.setDateFinishSchedule(ol.getDatePromised());
                             mrp.setDateOrdered(ol.getDateOrdered());
                             mrp.setM_Product_ID(ol.getM_Product_ID());
                             mrp.setM_Warehouse_ID(ol.getM_Warehouse_ID());
                             mrp.setDocStatus(o.getDocStatus());
                             //mrp.setS_Resource_ID();
                             
                             //String isSoTrx = Env.getContext(Env.getCtx(), "isSOTrx");                                              
                             
                             if (o.isSOTrx())
                             {    
                             mrp.setType("D");
                             mrp.setTypeMRP("SOO");
                             }
                             else
                             {
                             mrp.setType("S");
                             mrp.setTypeMRP("POO");                                 
                             }
                             mrp.save(trxName);
                             
                        }
                        
                        rs.close();
                        pstmt.close();                                               

                    }
                    catch (SQLException ex)
                    {			
                        log.log(Level.SEVERE, "doIt - " + sql , ex); 
                    }
                    /** BISion - 07/05/2009 - Santiago Iba�ez
                     * Modificacion realizada para cerrar el prepared statement
                     * independientemente de que si haya ocurrido alguna excepcion o no
                     */
                    finally{
                        try{
                            if (pstmt!=null)
                                pstmt.close();
                            pstmt = null;
                        }
                        catch(Exception e){

                        }
                    }
                    
                    int MPC_Order_ID = DB.getSQLValue(trxName,"SELECT MPC_Order_ID FROM MPC_Order o WHERE o.AD_Client_ID = ? AND o.C_OrderLine_ID = ? ", ol.getAD_Client_ID(),ol.getC_OrderLine_ID());            
                    if (MPC_Order_ID == -1 )
                    {
                        MProduct product = MProduct.get(Env.getCtx(),ol.getM_Product_ID());                    
                        int MPC_Product_BOM_ID = DB.getSQLValue(trxName,"SELECT MPC_Product_BOM_ID FROM MPC_Product_BOM bom WHERE bom.AD_Client_ID = ?  AND bom.Value = ? ", ol.getAD_Client_ID(),product.getValue());           
                        if (MPC_Product_BOM_ID != -1) 
                        {
                            X_MPC_Product_BOM bom = new X_MPC_Product_BOM(Env.getCtx(),MPC_Product_BOM_ID, null);
                            if (bom.getBOMType().equals(bom.BOMTYPE_Make_To_Order))
                            {
                                int S_Resource_ID = DB.getSQLValue(trxName,"SELECT S_Resource_ID FROM S_Resource r WHERE r.ManufacturingResourceType = 'PT' AND r.IsManufacturingResource = 'Y' AND r.AD_Client_ID = ? AND r.M_Warehouse_ID = ? AND ROWNUM = 1", ol.getAD_Client_ID(),ol.getM_Warehouse_ID());            
                                int AD_Workflow_ID = DB.getSQLValue(trxName,"SELECT AD_Workflow_ID FROM AD_Workflow wf WHERE wf.AD_Client_ID = ?  AND wf.Value = ? ", ol.getAD_Client_ID(),product.getValue());           
                                //System.out.print("---------S_Resource_ID"+ S_Resource_ID);
                                //System.out.print("---------AD_Workflow_ID"+ AD_Workflow_ID);
                                if (S_Resource_ID != -1 && AD_Workflow_ID != -1)
                                {
                                    MDocType[] doc = MDocType.getOfDocBaseType(Env.getCtx(),MDocType.DOCBASETYPE_ManufacturingOrder);
                                    int C_DocType_ID = doc[0].getC_DocType_ID();
                                    //int C_DocType_ID = MMPCMRP.getDocType(MDocType.DOCBASETYPE_ManufacturingOrder, false);     
                                    MMPCOrder order = new MMPCOrder(Env.getCtx(), 0 , trxName);                                     
                                    order.setC_OrderLine_ID(ol.getC_OrderLine_ID());
                                    order.setDocumentNo(MSequence.getDocumentNo(C_DocType_ID,null));
                                    order.setS_Resource_ID(S_Resource_ID);
                                    order.setM_Warehouse_ID(ol.getM_Warehouse_ID());
                                    order.setM_Product_ID(ol.getM_Product_ID());
                                    order.setM_AttributeSetInstance_ID(ol.getM_AttributeSetInstance_ID());
                                    order.setMPC_Product_BOM_ID(MPC_Product_BOM_ID);
                                    order.setAD_Workflow_ID(AD_Workflow_ID);
                                    //order.setPlanner_ID(SupplyPlanner_ID);
                                    order.setLine(10);
                                    order.setQtyDelivered(Env.ZERO);
                                    order.setQtyReject(Env.ZERO);
                                    order.setQtyScrap(Env.ZERO);                                                        
                                    order.setDateOrdered(ol.getDateOrdered());                       
                                    order.setDatePromised(ol.getDatePromised());
                                    order.setDateStartSchedule(TimeUtil.addDays(ol.getDatePromised(), (MMPCMRP.getDays(S_Resource_ID,AD_Workflow_ID, ol.getQtyOrdered())).negate().intValue()));                                                       
                                    order.setDateFinishSchedule(ol.getDatePromised());
                                    order.setQtyEntered(ol.getQtyEntered());
                                    order.setQtyOrdered(ol.getQtyOrdered());
                                    order.setC_UOM_ID(ol.getC_UOM_ID());
                                    order.setPosted(false);
                                    order.setProcessed(false);
                                    order.setC_DocTypeTarget_ID(C_DocType_ID);
                                    order.setC_DocType_ID(C_DocType_ID);
                                    order.setPriorityRule(order.PRIORITYRULE_High);                                
                                    order.save(trxName);  
                                    order.prepareIt(); 
                                    order.setDocAction(order.DOCSTATUS_Completed);
                                    order.save( trxName);
                                }
                            }                            
                        }    
                    }
                    else
                    {    
                        
                         MMPCOrder order = new MMPCOrder(Env.getCtx(), MPC_Order_ID , trxName); 
                         if (order.DOCSTATUS_Completed != order.getDocStatus() || order.DOCSTATUS_Closed != order.getDocStatus())
                         {
                             order.setQtyEntered(ol.getQtyEntered());
                             order.setDatePromised(ol.getDatePromised());
                             order.save(trxName);
                         }    
                    }    
                    
                    return mrp.getMPC_MRP_ID();
        }
        
        public static int MPC_Order(MMPCOrder o, String trxName)
        {

        	
                String sql =  new String("SELECT MPC_MRP_ID FROM MPC_MRP WHERE Type = 'S' AND TypeMRP='MOP' AND MPC_Order_ID = ? ");        		
                //MMPCOrder o = new MMPCOrder(Env.getCtx(), MPC_Order_ID);
                MMPCMRP mrp = null;

                    PreparedStatement pstmt = null;
                    ResultSet rs = null;
                    try
                    {
                    	pstmt = DB.prepareStatement (sql,trxName);
                        pstmt.setInt(1, o.getMPC_Order_ID());
                        rs = pstmt.executeQuery ();
                        boolean records = false; 
                        
                        while (rs.next())
                        {   
                            records = true; 
                            mrp = new MMPCMRP(Env.getCtx(), rs.getInt("MPC_MRP_ID") ,trxName);                           
                            mrp.setDescription(o.getDescription());
                            mrp.setName("MRP");
                            
                            //mrp.setQty(o.getQtyOrdered().subtract(o.getQtyDelivered()));
                            
                            /*      Vit4B - 07/09/2007
                             *      Modificacion para solucionar el problema que genera que tome QtyOrdered
                             *      miestras que deberia toma QtyEntered porque el ordenado se va restando.
                             *      
                             *      Debe de tomar lo pendiente de entregar por una orden que es lo que se
                             *      lleva en Ordered o lo mismo seria la diferencia de Entered y Delivered con 
                             *      la salvedad de que no sea menor que 0
                             *
                             *      Control adicional
                             *
                             */
                            
                            BigDecimal diff = o.getQtyEntered().subtract(o.getQtyDelivered());
                            
                            
                            if(diff.compareTo(Env.ZERO) < 0)
                                mrp.setQty(Env.ZERO);
                            else
                                mrp.setQty(diff);
                            
                            
                            mrp.setDatePromised(o.getDatePromised());
                            mrp.setDateOrdered(o.getDateOrdered());
                            mrp.setDateStartSchedule(o.getDateStartSchedule());
                            mrp.setDateFinishSchedule(o.getDateFinishSchedule());
                            mrp.setM_Product_ID(o.getM_Product_ID());
                            mrp.setM_Warehouse_ID(o.getM_Warehouse_ID());
                            mrp.setS_Resource_ID(o.getS_Resource_ID());                            
                            mrp.setDocStatus(o.getDocStatus());                            
                            mrp.save(trxName);
                        }
                        
                        if (!records)
                        {
                             mrp = new MMPCMRP(Env.getCtx(), 0, trxName);                                                                                                                 
                             mrp.setMPC_Order_ID(o.getMPC_Order_ID());
                             mrp.setDescription(o.getDescription());
                             mrp.setName("MRP");
                             

                            //mrp.setQty(o.getQtyOrdered().subtract(o.getQtyDelivered()));
                             
                            /*      Vit4B - 07/09/2007
                             *      Modificaci�n para solucionar el problema que genera que tome QtyOrdered
                             *      miestras que deber�a toma QtyEntered porque el ordenado se va restando.
                             *      
                             *      Debe de tomar lo pendiente de entregar por una orden que es lo que se
                             *      lleva en Ordered o lo mismo ser�a la diferencia de Entered y Delivered con 
                             *      la salvedad de que no sea menor que 0
                             *
                             *      Control adicional
                             *
                             */
                            
                            BigDecimal diff = o.getQtyEntered().subtract(o.getQtyDelivered());
                            
                            
                            if(diff.compareTo(Env.ZERO) < 0)
                                mrp.setQty(Env.ZERO);
                            else
                                mrp.setQty(diff);
                             
                             
                             
                             
                             mrp.setDatePromised(o.getDatePromised());
                             mrp.setDateOrdered(o.getDateOrdered());
                             mrp.setDateStartSchedule(o.getDateStartSchedule());
 			     mrp.setDateFinishSchedule(o.getDateStartSchedule());
                             mrp.setM_Product_ID(o.getM_Product_ID());
                             mrp.setM_Warehouse_ID(o.getM_Warehouse_ID());
                             mrp.setS_Resource_ID(o.getS_Resource_ID());
                             mrp.setType("S");
                             mrp.setTypeMRP("MOP");
                             mrp.setDocStatus(o.getDocStatus());
                             rs.close();
                             pstmt.close();
                             mrp.save(trxName);
                             
                        }                        
                        rs.close();
                        rs = null;
                        pstmt.close();
                        pstmt = null;
                    }
                    catch (SQLException ex)
					{			
                        log.log(Level.SEVERE, "doIt - " + sql , ex); 
                    }
                    /** BISion - 07/05/2009 - Santiago Ibañez
                     * Modificacion realizada para cerrar el prepared statement
                     * independientemente de que si haya ocurrido alguna excepcion o no
                     */
                    finally{
                        try{
                            if (pstmt!=null)
                                pstmt.close();
                            pstmt = null;
                        }
                        catch(Exception e){

                        }
                    }
                    //Comentado por BISion - 08/05/2009 
                    /*sql =  new String("SELECT MPC_Order_BOMLine_ID FROM MPC_Order o INNER JOIN MPC_Order_BOMLine ol ON (ol.MPC_Order_ID=o.MPC_Order_ID) WHERE o.MPC_Order_ID = ? ");

                    pstmt = null;
                    try
                    {
                    	pstmt = DB.prepareStatement (sql,trxName);
                        pstmt.setInt(1, o.getMPC_Order_ID());
                        rs = pstmt.executeQuery ();                        
                        
                        while (rs.next())
                        {  
                          X_MPC_Order_BOMLine ol = new X_MPC_Order_BOMLine(Env.getCtx(),rs.getInt(1),trxName); 
                          MPC_Order_BOMLine(ol,o ,trxName);
                        }
                        rs.close();
                        rs = null;
                        pstmt.close();
                        pstmt = null;
                       
                    }
                    catch (SQLException ex)
                    {			
                        log.log(Level.SEVERE, "doIt - " + sql , ex); 
                    }
                    /** BISion - 07/05/2009 - Santiago Ibañez
                     * Modificacion realizada para cerrar el prepared statement
                     * independientemente de que si haya ocurrido alguna excepcion o no
                     */
                    /*finally{
                        try{
                            if (pstmt!=null)
                                pstmt.close();
                            pstmt = null;
                        }
                        catch(Exception e){

                        }
                    }*/
                    MMPCOrderBOMLine lines[] = MMPCOrder.getLines(o.getMPC_Order_ID(),trxName);
                    for (int i=0;i<lines.length;i++)
                        MPC_Order_BOMLine(lines[i], o, trxName);
                            			
                    return mrp.getMPC_MRP_ID();
        }
        
        public static int MPC_Order_BOMLine(X_MPC_Order_BOMLine obl,MMPCOrder o, String trxName)
        {        	   
               String sql =  new String("SELECT MPC_MRP_ID FROM MPC_MRP mrp WHERE mrp.Type = 'D' AND mrp.TypeMRP='MOP' AND mrp.MPC_Order_BOMLine_ID = ? ");
        	   //String sql =  new String("SELECT mrp.MPC_MRP_ID FROM MPC_MRP mrp WHERE mrp.Type = 'D' AND mrp.MPC_Order_BOMLine_ID = ? ");
                    MMPCMRP mrp = null;
                    PreparedStatement pstmt = null;
                    try
                    {
			pstmt = DB.prepareStatement (sql,trxName);
                        pstmt.setInt(1, obl.getMPC_Order_BOMLine_ID());
                        ResultSet rs = pstmt.executeQuery ();                        
                        boolean records = false; 
                        while (rs.next())
                        {   
                            records = true; 
                            
                            /** BISion - 19/11/2008 - Santiago Iba�ez
                             * Modificacion realizada porque producia excep. de
                             * columnas invalidas.
                             */
                            mrp = new MMPCMRP(Env.getCtx(), rs.getInt("MPC_MRP_ID"),trxName);
                            mrp.setName("MRP"); 
                            mrp.setDescription(o.getDescription());                            
                            //mrp.setQty(obl.getQtyRequiered().subtract(obl.getQtyDelivered()));
                            //mrp.setQtyRequiered(obl.getQtyRequiered());

                            
                            /*      Vit4B - 13/09/2007
                             *
                             *      Control adicional
                             *
                             */
                            
                            BigDecimal diff = obl.getQtyRequiered().subtract(obl.getQtyDelivered());
                            
                            if(diff.compareTo(Env.ZERO) < 0)
                                mrp.setQty(Env.ZERO);
                            else
                                mrp.setQty(diff);

                            
                            
                            
                            mrp.setDatePromised(o.getDatePromised());
                            mrp.setDateOrdered(o.getDateOrdered());
                            mrp.setDateStartSchedule(o.getDateStartSchedule());
                            mrp.setDateFinishSchedule(o.getDateFinishSchedule());
                            mrp.setM_Product_ID(obl.getM_Product_ID());
                            mrp.setM_Warehouse_ID(obl.getM_Warehouse_ID()); 
                            mrp.setS_Resource_ID(o.getS_Resource_ID());
                            mrp.setDocStatus(o.getDocStatus());
                            mrp.save(trxName);
                        }
                        
                        if (!records)
                        {
                             mrp = new MMPCMRP(Env.getCtx(), 0,trxName);                                                         
                             //MOrder o = new MOrder(Env.getCtx(), ol.getC_Order_ID());
                             
                             mrp.setMPC_Order_BOMLine_ID(obl.getMPC_Order_BOMLine_ID());
                             mrp.setName("MRP");
                             mrp.setDescription(o.getDescription());
                             mrp.setMPC_Order_ID(o.getMPC_Order_ID());
                             //mrp.setQty(obl.getQtyRequiered().subtract(obl.getQtyDelivered()));
                             
                             
                             /*      Vit4B - 13/09/2007
                             *
                             *      Control adicional
                             *
                             */
                            
                            BigDecimal diff = obl.getQtyRequiered().subtract(obl.getQtyDelivered());
                            
                            if(diff.compareTo(Env.ZERO) < 0)
                                mrp.setQty(Env.ZERO);
                            else
                                mrp.setQty(diff);   
                             
                             
                             
                             
                             
                             mrp.setDatePromised(o.getDatePromised());
                             mrp.setDateOrdered(o.getDateOrdered());
                             mrp.setDateStartSchedule(o.getDateStartSchedule());
 							 mrp.setDateFinishSchedule(o.getDateFinishSchedule());
                             mrp.setM_Product_ID(obl.getM_Product_ID());
                             mrp.setM_Warehouse_ID(obl.getM_Warehouse_ID());
                             mrp.setS_Resource_ID(o.getS_Resource_ID());
                             mrp.setDocStatus(o.getDocStatus());
                             mrp.setType("D");
                             mrp.setTypeMRP("MOP");
                             rs.close();
                             rs = null;
                             pstmt.close();
                             pstmt = null;
                             mrp.save();    
                        }
                        if (rs!=null)
                            rs.close();
                        rs = null;
                        if (pstmt!= null)
                            pstmt.close();
                        pstmt = null;

                    }
                    catch (SQLException ex)
					{			
                        log.log(Level.SEVERE, "doIt - " + sql , ex); 
                    }
                    /** BISion - 07/05/2009 - Santiago Ibañez
                     * Modificacion realizada para cerrar el prepared statement
                     * independientemente de que si haya ocurrido alguna excepcion o no
                     */
                    finally{
                        try{
                            if (pstmt!=null)
                                pstmt.close();
                            pstmt = null;
                        }
                        catch(Exception e){

                        }
                    }
                    return mrp.getMPC_MRP_ID();
                                       
        }
        
        public static BigDecimal getDemanda(MMPCOrder o,int M_Product_ID, String trxName)
        {        	   
               String sql =  new String("SELECT qty FROM MPC_MRP mrp WHERE mrp.Type = 'D' AND mrp.TypeMRP='MOP' AND mrp.MPC_Order_ID = ?  AND mrp.M_Product_ID = ? ");
                    BigDecimal returnValue = null;
                    PreparedStatement pstmt = null;
                    try
                    {
			pstmt = DB.prepareStatement (sql,trxName);
                        pstmt.setInt(1, o.getMPC_Order_ID());
                        pstmt.setInt(2, M_Product_ID);
                        ResultSet rs = pstmt.executeQuery ();                        
                        if ( rs.next() )
                            returnValue = rs.getBigDecimal(1);
                    }
                    catch (SQLException ex)
					{			
                        log.log(Level.SEVERE, "doIt - " + sql , ex); 
                    }
                    
                    finally{
                        try{
                            if (pstmt!=null)
                                pstmt.close();
                            pstmt = null;
                        }
                        catch(Exception e){

                        }
                    }
                    return returnValue;
                                       
        }
        
        public static int  M_RequisitionLine( MRequisitionLine rl , String trxName, boolean delete)
        {
	    	String sql = null;
	    	if (delete)
	    	{
	    		sql = "DELETE FROM MPC_MRP WHERE M_RequisitionLine_ID = "+ rl.getM_RequisitionLine_ID()  +" AND AD_Client_ID = " + rl.getAD_Client_ID();				
                        DB.executeUpdate(sql,trxName); //reorder by hamed
                        return 0;
	    	}
	    		
               sql =  new String("SELECT * FROM MPC_MRP mrp WHERE mrp.M_RequisitionLine_ID = ? ");
               MRequisition r = new MRequisition(Env.getCtx(), rl.getM_Requisition_ID(),trxName);
               MMPCMRP mrp = null;
               //MMPCOrder o = new MMPCOrder(Env.getCtx(), ol.getMPC_Order_ID());

                    PreparedStatement pstmt = null;
                    try
                    {
                    	pstmt = DB.prepareStatement (sql,trxName);
                        pstmt.setInt(1, rl.getM_RequisitionLine_ID());
                        ResultSet rs = pstmt.executeQuery ();                        
                        boolean records = false; 
                        while (rs.next())
                        {   
                            records = true; 
                            mrp = new MMPCMRP(Env.getCtx(), rs,trxName);
                            mrp.setName("MRP");
                            mrp.setDescription(rl.getDescription());                                                        
                            mrp.setQty(rl.getQty());

                            /** BISion - 27/08/2009 - Santiago Ibañez
                             * Modificacion realizada por Ticket Fecha Prometida Requisiciones
                             */
                            BigDecimal TiempoEntregaPrometido = BigDecimal.ZERO;
                            BigDecimal TiempoTransferencia = BigDecimal.ZERO;
                            int S_Resource_ID = 0;
                            MMPCProductPlanning ppd = MMPCProductPlanning.getDemandWarehouse(Env.getCtx(), rl.getAD_Org_ID(), rl.getM_Product_ID(), r.getM_Warehouse_ID(),trxName);
                            if (ppd!=null){
                                TiempoTransferencia = ppd.getTransfertTime();
                                S_Resource_ID = ppd.getS_Resource_ID();
                                MMPCProductPlanning pps = MMPCProductPlanning.getDemandSupplyResource(Env.getCtx(), rl.getAD_Org_ID(),rl.getM_Product_ID(),S_Resource_ID);
                                if (pps!=null)
                                    TiempoEntregaPrometido = pps.getDeliveryTime_Promised();
                            }
                            //Fecha requerida de la requisicion
                            mrp.setDateStartSchedule(r.getDateRequired());
                            //Le sumo la cantidad de dias que tarda en llegar
                            mrp.setDatePromised(TimeUtil.addDays(r.getDateRequired(), TiempoEntregaPrometido.add(TiempoTransferencia).intValue()));
							mrp.setDateFinishSchedule(r.getDateRequired());
                            //fin modificacion BISion
                            mrp.setDateOrdered(r.getDateRequired());
                            mrp.setM_Product_ID(rl.getM_Product_ID());
                            mrp.setM_Warehouse_ID(r.getM_Warehouse_ID()); 
                            mrp.setDocStatus(r.getDocStatus());
                            mrp.save(trxName);
                        }
                        
                        if (!records)
                        {
                            mrp = new MMPCMRP(Env.getCtx(), 0,trxName);  
                           mrp.setM_Requisition_ID(rl.getM_Requisition_ID());
                            mrp.setM_RequisitionLine_ID(rl.getM_RequisitionLine_ID());
                            mrp.setName("MRP");
                            mrp.setDescription(rl.getDescription());                                                        
                            mrp.setQty(rl.getQty());
                            /** BISion - 27/08/2009 - Santiago Ibañez
                             * Modificacion realizada por Ticket Fecha Prometida Requisiciones
                             */
                            BigDecimal TiempoEntregaPrometido = BigDecimal.ZERO;
                            BigDecimal TiempoTransferencia = BigDecimal.ZERO;
                            int S_Resource_ID = 0;
                            MMPCProductPlanning ppd = MMPCProductPlanning.getDemandWarehouse(Env.getCtx(), rl.getAD_Org_ID(), rl.getM_Product_ID(), r.getM_Warehouse_ID(),trxName);
                            if (ppd!=null){
                                TiempoTransferencia = ppd.getTransfertTime();
                                S_Resource_ID = ppd.getS_Resource_ID();
                                MMPCProductPlanning pps = MMPCProductPlanning.getDemandSupplyResource(Env.getCtx(), rl.getAD_Org_ID(),rl.getM_Product_ID(),S_Resource_ID);
                                if (pps!=null)
                                    TiempoEntregaPrometido = pps.getDeliveryTime_Promised();
                            }
                            mrp.setDateStartSchedule(r.getDateRequired());
                            //Se anticipa la fecha de comienzo programada
                            mrp.setDatePromised(TimeUtil.addDays(r.getDateRequired(), TiempoEntregaPrometido.add(TiempoTransferencia).intValue()));
							mrp.setDateFinishSchedule(r.getDateRequired());
                            //fin modificacion BISion
                            mrp.setDateOrdered(r.getDateRequired());
                            mrp.setM_Product_ID(rl.getM_Product_ID());
                            mrp.setM_Warehouse_ID(r.getM_Warehouse_ID()); 
                            mrp.setDocStatus(r.getDocStatus());
                            mrp.setType("S");
                            mrp.setTypeMRP("POR");
                            mrp.setIsAvailable(true);
                            mrp.save(trxName);
                             
                        }
                        
                        rs.close();
                        rs = null;
                        pstmt.close();                                               
                        pstmt = null;
                    }
                    catch (SQLException ex)
					{			
                        log.log(Level.SEVERE, "doIt - " + sql , ex); 
                    }
                    /** BISion - 07/05/2009 - Santiago Ibañez
                     * Modificacion realizada para cerrar el prepared statement
                     * independientemente de que si haya ocurrido alguna excepcion o no
                     */
                    finally{
                        try{
                            if (pstmt!=null)
                                pstmt.close();
                            pstmt = null;
                        }
                        catch(Exception e){

                        }
                    }
                    return mrp.getMPC_MRP_ID();
                                       
        }
        
        public static int  M_Requisition( MRequisition r , String trxName)
        {

    		
               String sql =  new String("SELECT * FROM MPC_MRP mrp WHERE mrp.M_Requisition_ID = ? ");
               //MRequisition r = new MRequisition(Env.getCtx(), rl.getM_Requisition_ID());
               MMPCMRP mrp = null;
               //MMPCOrder o = new MMPCOrder(Env.getCtx(), ol.getMPC_Order_ID());

                    PreparedStatement pstmt = null;
                    try
                    {
                    	pstmt = DB.prepareStatement (sql,trxName);
                        pstmt.setInt(1, r.getM_Requisition_ID());
                        ResultSet rs = pstmt.executeQuery ();                        
                        boolean records = false; 
                        while (rs.next())
                        {   
                            records = true; 
                            mrp = new MMPCMRP(Env.getCtx(), rs ,trxName);                            
                            mrp.setDocStatus(r.getDocStatus());
                            mrp.save(trxName);
                        }                                       
                        
                        rs.close();
                        rs = null;
                        pstmt.close();
                        pstmt = null;

                    }
                    catch (SQLException ex)
					{			
                        log.log(Level.SEVERE, "doIt - " + sql , ex); 
                    }
                    /** BISion - 07/05/2009 - Santiago Ibañez
                     * Modificacion realizada para cerrar el prepared statement
                     * independientemente de que si haya ocurrido alguna excepcion o no
                     */
                    finally{
                        try{
                            if (pstmt!=null)
                                pstmt.close();
                            pstmt = null;
                        }
                        catch(Exception e){

                        }
                    }
                    return mrp.getMPC_MRP_ID();
                                       
        }    
        
    /*public static int getDocType(String DocBaseType, boolean IsDefault)
    {
            MDocType[] doc = MDocType.getOfDocBaseType(Env.getCtx(), DocBaseType);
            int C_DocType_ID = 0;
            System.out.println("doc" + doc.toString());
            System.out.println("doc.length " + doc.length );
            System.out.println("doc" + doc[0].getC_DocType_ID());
            if(doc!=null && !IsDefault)
            C_DocType_ID = doc[0].getC_DocType_ID();
            
            else if (doc!=null && IsDefault)
            {
                for(int i = 0 ; i <= doc.length ; i++)
                {
                    if (doc[i].isDefault())
                    {    
                    C_DocType_ID = doc[i].getC_DocType_ID();
                    break;
                    }
                }
            }
            
            return C_DocType_ID;
    }*/
    
  public static BigDecimal getOnHand(int AD_Org_ID , int M_Product_ID, String trxName)
  {
                BigDecimal OnHand = Env.ZERO;
                //e-evolution migracion 252b
                //String sql = "SELECT SUM(BOM_Qty_OnHand (M_Product_ID, M_Warehouse_ID)) AS OnHand FROM MPC_Product_Planning pp WHERE pp.M_Product_ID = " + M_Product_ID ;
                
                //String sql = "SELECT SUM(bomQtyOnHand (pp.M_Product_ID ,pp.M_Warehouse_ID,0)) AS OnHand FROM MPC_Product_Planning pp Inner Join M_Warehouse mw ON(mw.M_Warehouse_ID=pp.M_Warehouse_ID and mw.IsRequiredMRP='Y') WHERE  pp.IsSupply =  'Y' AND  pp.AD_Org_ID = " + AD_Org_ID + " AND pp.M_Product_ID=" + M_Product_ID;
                

                 /*      Vit4B - 13/09/2007
                 *
                 *      Control adicional para que obtenga el onHand de los depositos marcados como
                 *      IsRequiredMRP='Y' pero no considerando como hasta ahora el planeamiento
                 *
                 *
                 */                
                
                
                //String sql = "SELECT SUM(bomQtyOnHand (ms.M_Product_ID ,ml.M_Warehouse_ID,0)) AS OnHand FROM M_Storage ms Inner Join M_Locator ml on (ms.M_Locator_Id = ml.M_Locator_Id) Inner Join M_Warehouse mw ON(mw.M_Warehouse_ID=ml.M_Warehouse_ID) WHERE  mw.IsRequiredMRP='Y' AND  ms.AD_Org_ID = " + AD_Org_ID + " AND ms.M_Product_ID=" + M_Product_ID;
                
                /*
                Geneos - Pablo Velazquez
                4/12/2014
                Solo se deben incluir los STORAGE activos
                */
                //String sql = "SELECT distinct(mw.M_Warehouse_ID),bomQtyOnHandActive (ms.M_Product_ID ,ml.M_Warehouse_ID,0) AS OnHand FROM M_Storage ms Inner Join M_Locator ml on (ms.M_Locator_Id = ml.M_Locator_Id) Inner Join M_Warehouse mw ON(mw.M_Warehouse_ID=ml.M_Warehouse_ID) WHERE  mw.IsRequiredMRP='Y' AND ms.isactive = 'Y' AND  ms.AD_Org_ID = " + AD_Org_ID + " AND ms.M_Product_ID=" + M_Product_ID;
                String sql = "SELECT distinct(mw.M_Warehouse_ID),ms.M_Product_ID ,ml.M_Warehouse_ID FROM M_Storage ms Inner Join M_Locator ml on (ms.M_Locator_Id = ml.M_Locator_Id) Inner Join M_Warehouse mw ON(mw.M_Warehouse_ID=ml.M_Warehouse_ID) WHERE  mw.IsRequiredMRP='Y' AND ms.isactive = 'Y' AND  ms.AD_Org_ID = " + AD_Org_ID + " AND ms.M_Product_ID=" + M_Product_ID;
                
                
                
                
                
                
                //String sql = "SELECT SUM(bomQtyOnHand (pp.M_Product_ID ,pp.M_Warehouse_ID,0)) AS OnHand FROM MPC_Product_Planning pp WHERE  pp.IsSupply =  'Y' AND  pp.AD_Org_ID = " + AD_Org_ID + " AND pp.M_Product_ID=" + M_Product_ID;
                // fjviejo e-evolution warehouse for mrp
                // end e-evolutionpp.M_Product_ID = " + M_Product_ID + "
                
		PreparedStatement pstmt = null;
                
                
		try
		{
			pstmt = DB.prepareStatement (sql,trxName);
                        //pstmt.setInt(1, p_M_Warehouse_ID);                        
                        ResultSet rs = pstmt.executeQuery ();
                        while (rs.next())
                        {
                            
                            
                           //OnHand = rs.getBigDecimal("OnHand");
                           OnHand = OnHand.add(bomQtyActive (rs.getInt(2) ,rs.getInt(3),trxName));
                           
                        }
                        rs.close();
                        pstmt.close();

		}
		catch (SQLException ex)
		{			
                        log.log(Level.SEVERE, "doIt - " + sql , ex); 
                        return null;
		}
        /** BISion - 07/05/2009 - Santiago Ibañez
         * Modificacion realizada para cerrar el prepared statement
         * independientemente de que si haya ocurrido alguna excepcion o no
         */
        finally{
            try{
                if (pstmt!=null)
                    pstmt.close();
                pstmt = null;
            }
            catch(Exception e){

            }
        }
        if (OnHand == null)
            OnHand = Env.ZERO;
      return OnHand;
  }
  
  public static int getMaxLowLevel(String trxName)
  {
  				  int LowLevel = 0;
  				  int AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
                  PreparedStatement pstmt = null;
                  try
                  {
                      String sql = "SELECT Max(LowLevel) FROM M_Product WHERE AD_Client_ID = " + AD_Client_ID + " AND LowLevel IS NOT NULL";                      
                      System.out.println("MaxLowLevel SQL:" + sql);
                      //pstmt.setInt(1, AD_Client_ID);
                      pstmt = DB.prepareStatement (sql,trxName);
                      ResultSet rs = pstmt.executeQuery();                            
                      rs.next();
                      LowLevel = rs.getInt(1); 
                      log.info("MaxLowLevel" + LowLevel);
                      rs.close();
                      pstmt.close();
                      return LowLevel + 1;
                  }
                  catch (SQLException ex)
                  {
                      log.log(Level.SEVERE,"not found MaxLowLevel", ex);
                      return LowLevel;
                  }
                /** BISion - 07/05/2009 - Santiago Iba�ez
                 * Modificacion realizada para cerrar el prepared statement
                 * independientemente de que si haya ocurrido alguna excepcion o no
                 */
                finally{
                    try{
                        if (pstmt!=null)
                            pstmt.close();
                        pstmt = null;
                    }
                    catch(Exception e){

                    }
                }   
   }
  
  public static BigDecimal getDays(int S_Resource_ID, int AD_Workflow_ID, BigDecimal QtyOrdered)
  {
  	if (S_Resource_ID == 0)
  		return Env.ZERO;
  	
  	MResource S_Resource = new MResource(Env.getCtx(),S_Resource_ID,null);
  	MResourceType S_ResourceType = new MResourceType(Env.getCtx(),S_Resource.getS_ResourceType_ID(),null);  	
  	
  	BigDecimal AvailableDayTime  = Env.ZERO;
  	int AvailableDays = 0;
  	
  	
  	long hours = 0;
    
  	
  	if (S_ResourceType.isDateSlot())
  		AvailableDayTime = new BigDecimal(getHoursAvailable(S_ResourceType.getTimeSlotStart(),S_ResourceType.getTimeSlotEnd()));
  	else
  		AvailableDayTime  = new BigDecimal(24); 
  	
  	if (S_ResourceType.isOnMonday())
  		AvailableDays =+ 1; 
		
  	if (S_ResourceType.isOnTuesday())
  		AvailableDays =+ 1;
  	
  	if (S_ResourceType.isOnThursday())
  		AvailableDays =+ 1;
  	
  	if (S_ResourceType.isOnTuesday())
  		AvailableDays =+ 1;
  	
  	if (S_ResourceType.isOnWednesday())	
  		AvailableDays =+ 1;
  	
  	if (S_ResourceType.isOnFriday())	 
  		AvailableDays =+ 1;
  	
  	if (S_ResourceType.isOnSaturday())	
  		AvailableDays =+ 1;
  	
  	if (S_ResourceType.isOnSunday())
  		AvailableDays =+ 1;
  	
  	MWorkflow wf = new MWorkflow(Env.getCtx(),AD_Workflow_ID,null);
  	BigDecimal RequiredTime = Env.ZERO ;//wf.getQueuingTime().add(wf.getSetupTime()).add(wf.getDuration().multiply(QtyOrdered)).add(wf.getWaitingTime()).add(wf.getMovingTime());
  	 	
  	// Weekly Factor  	
  	BigDecimal WeeklyFactor = new BigDecimal(7).divide(new BigDecimal(AvailableDays),BigDecimal.ROUND_UNNECESSARY);
	
  	return (RequiredTime.multiply(WeeklyFactor)).divide(AvailableDayTime,BigDecimal.ROUND_UP);
  }  
  
	/**
	 * 	Return horus in 
	 * 	@param Time Start
	 * 	@param Time End
	 * 	@return hours
	 */
	public static long getHoursAvailable(Timestamp time1 , Timestamp time2)
	{
		//System.out.println("Start" +  time1);
		//System.out.println("end" +  time2);
      GregorianCalendar g1 = new GregorianCalendar();
      g1.setTimeInMillis(time1.getTime());
      g1.set(Calendar.HOUR_OF_DAY, 0);
      g1.set(Calendar.MINUTE, 0);
      g1.set(Calendar.SECOND, 0);
      g1.set(Calendar.MILLISECOND, 0);
      GregorianCalendar g2 = new GregorianCalendar();
      g2.set(Calendar.HOUR_OF_DAY, 0);
      g2.set(Calendar.MINUTE, 0);
      g2.set(Calendar.SECOND, 0);
      g2.set(Calendar.MILLISECOND, 0);
      g2.setTimeInMillis(time2.getTime());
      //System.out.println("start"+ g1.getTimeInMillis());
      //System.out.println("end"+ g2.getTimeInMillis());
      long difference = g2.getTimeInMillis() - g1.getTimeInMillis(); 
      //System.out.println("Elapsed milliseconds: " + difference);
      return difference / 6750000;        
   }
        
        
    private static final BigDecimal UNLIMITED = new BigDecimal((double)99999.0);
        
    static BigDecimal bomQtyActive (int p_M_Product_ID, 
		int p_M_Warehouse_ID, String trxName) 
		throws SQLException
	{
		//	Check Parameters
		int M_Warehouse_ID = p_M_Warehouse_ID;
                
                
		
		if (M_Warehouse_ID == 0)
			return Env.ZERO;
		
		//	Check, if product exists and if it is stocked
		boolean isBOM = false;
		String ProductType = null;
		boolean isStocked = false;
		
                String sql = "SELECT IsBOM, ProductType, IsStocked "
			+ "FROM M_Product "
			+ "WHERE M_Product_ID=" + p_M_Product_ID;
		
                PreparedStatement pstmt = DB.prepareStatement(sql, trxName);
                
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
		{
			isBOM = "Y".equals(rs.getString(1));
			ProductType = rs.getString(2);
			isStocked = "Y".equals(rs.getString(3));
		}
                
		rs.close();
		pstmt.close();
		
                //	No Product
		if (ProductType == null)
			return Env.ZERO;
		//	Unlimited capacity if no item
		if (!isBOM && (!ProductType.equals("I") || !isStocked))
			return UNLIMITED;
		//	Get Qty
		if (isStocked)
			return getStorageQtyActive(p_M_Product_ID, M_Warehouse_ID, trxName);
		
		//	Go through BOM
		BigDecimal quantity = UNLIMITED;
		BigDecimal productQuantity = null;
		sql = "SELECT b.M_ProductBOM_ID, b.BOMQty, p.IsBOM, p.IsStocked, p.ProductType "
			+ "FROM M_Product_BOM b, M_Product p "
			+ "WHERE b.M_ProductBOM_ID=p.M_Product_ID"
			+ " AND b.M_Product_ID=?";
                
		pstmt = DB.prepareStatement(sql,trxName);
		pstmt.setInt(1, p_M_Product_ID);
		rs = pstmt.executeQuery();
		while (rs.next())
		{
			int M_ProductBOM_ID = rs.getInt(1);
			BigDecimal bomQty = rs.getBigDecimal(2);
			isBOM = "Y".equals(rs.getString(3));
			isStocked = "Y".equals(rs.getString(4)); 
			ProductType = rs.getString(5);
			
			//	Stocked Items "leaf node"
			if (ProductType.equals("I") && isStocked)
			{
				//	Get ProductQty
				productQuantity = getStorageQtyActive(M_ProductBOM_ID, M_Warehouse_ID, trxName);
				//	Get Rounding Precision
				int uomPrecision = getUOMPrecision(M_ProductBOM_ID, trxName);
				//	How much can we make with this product
				productQuantity = productQuantity.setScale(uomPrecision)
					.divide(bomQty, uomPrecision, BigDecimal.ROUND_HALF_UP);
				//	How much can we make overall
				if (productQuantity.compareTo(quantity) < 0)
					quantity = productQuantity;
			}
			else if (isBOM)	//	Another BOM
			{
				productQuantity = bomQtyActive (M_ProductBOM_ID, M_Warehouse_ID, trxName);
				//	How much can we make overall
				if (productQuantity.compareTo(quantity) < 0)
					quantity = productQuantity;
			}
		}
		rs.close();
		pstmt.close();
		
		if (quantity.signum() != 0)
		{
			int uomPrecision = getUOMPrecision(p_M_Product_ID, trxName);
			return quantity.setScale(uomPrecision, BigDecimal.ROUND_HALF_UP);
		}
		return Env.ZERO;
	}	//	bomQtyOnHand
	     
    	static BigDecimal getStorageQtyActive (int p_M_Product_ID, 
		int M_Warehouse_ID, String trxName)
		throws SQLException
	{
		BigDecimal quantity = null;
		String sql = "SELECT SUM(qtyonhand) "
			+ "FROM M_Storage s "
			+ "WHERE M_Product_ID=?";
		sql += " AND s.isactive = 'Y'";
                sql += " AND EXISTS (SELECT * FROM M_Locator l WHERE s.M_Locator_ID=l.M_Locator_ID"
				+ " AND l.M_Warehouse_ID=?)";
		PreparedStatement pstmt = DB.prepareStatement(sql,trxName);
		pstmt.setInt(1, p_M_Product_ID);
		pstmt.setInt(2, M_Warehouse_ID);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
			quantity = rs.getBigDecimal(1);
		rs.close();
		pstmt.close();
		//	Not found
		if (quantity == null)
			return Env.ZERO;
		return quantity;
	}	//	getStorageQty
        
	static int getUOMPrecision (int p_M_Product_ID, String trxName) throws SQLException
	{
		int precision = 0;
		String sql = "SELECT u.StdPrecision "
			+ "FROM C_UOM u"
			+ " INNER JOIN M_Product p ON (u.C_UOM_ID=p.C_UOM_ID) "
			+ "WHERE p.M_Product_ID=?";
		PreparedStatement pstmt = DB.prepareStatement(sql,trxName);
		pstmt.setInt(1, p_M_Product_ID);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
			precision = rs.getInt(1);
		rs.close();
		pstmt.close();
		return precision;
	}	//	getStdPrecision
        
	
	
        
}	//	MPC_MRP
