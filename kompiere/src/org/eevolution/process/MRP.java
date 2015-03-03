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

package org.eevolution.process;


import java.text.SimpleDateFormat;
import org.eevolution.model.MMPCMRP;
import org.eevolution.model.MMPCOrder;
import org.eevolution.model.MMPCProductBOM;
import org.eevolution.model.MMPCProductPlanning;
import org.eevolution.model.MMPCOrderBOMLine;
import org.eevolution.model.*;
import org.compiere.model.MNote;
import org.compiere.model.MProduct;
import org.compiere.model.MProductPO;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.MSequence;
import org.compiere.model.MDocType;


import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

import org.compiere.util.CompiereSystemError;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;


import java.math.BigDecimal;
import java.sql.Connection;
import java.util.logging.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Vector;
import org.compiere.util.Trx;
        
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.ArrayList;
import org.compiere.model.MLocator;
import org.compiere.model.MMessage;
import org.compiere.model.MStorage;
import org.compiere.model.MWarehouse;
import org.compiere.model.MZYNCATEGORYTOLERANCE;

/**
 *	Re-Open Order Process (from Closed to Completed)
 *	
 *  @author Victor P�rez, e-Evolution, S.C.
 *  @version $Id: MRP.java,v 1.3 2006/08/11 13:46:30 SIGArg-01 Exp $
 */
public class MRP extends SvrProcess
{

        //private int               p_M_Warehouse_ID = 0;
        private int               p_AD_Org_ID = 0;
        private int               p_S_Resource_ID = 0 ;
        //
        private String             p_Version = "1";        
        private int AD_Client_ID = 0;
        
        //Global Variables
        private BigDecimal QtyProjectOnHand = Env.ZERO;
        private BigDecimal QtyStock =Env.ZERO;
        private BigDecimal Scrap = Env.ZERO;
    	private BigDecimal QtyNetReqs = Env.ZERO;
    	private BigDecimal QtyPlanned = Env.ZERO;
    	private BigDecimal QtyGrossReqs = Env.ZERO;
    	private BigDecimal DeliveryTime_Promised = Env.ZERO;
    	private BigDecimal TransfertTime = Env.ZERO;
    	private BigDecimal Order_Period = Env.ZERO;
    	private BigDecimal Order_Max  = Env.ZERO;
    	private BigDecimal Order_Min  = Env.ZERO;
    	private BigDecimal Order_Pack  = Env.ZERO;
    	private BigDecimal Order_Qty = Env.ZERO;
    	private BigDecimal QtyScheduledReceipts = Env.ZERO;
    	private int M_Product_ID = 0;
    	private int MPC_Product_BOM_ID = 0;
    	private int AD_Workflow_ID = 0;
    	private int S_Resource_ID = 0;    	
    	private String Order_Policy = MMPCProductPlanning.ORDER_POLICY_OrderFixedQuantity;
    	private int SupplyPlanner_ID = 0;
    	private int SupplyM_Warehouse_ID = 0;
    	private int DemandPlanner_ID = 0;
    	private int M_Warehouse_ID = 0;
    	private int Yield = 0;
    	private Timestamp DatePromisedFrom = null;
    	private Timestamp DatePromisedTo = null;
    	private boolean IsCreatePlan = true;

        private BigDecimal QtyDemandadaSatisfecha = BigDecimal.ZERO;
        private BigDecimal QtyAlmacenadaInicial = BigDecimal.ZERO;
    	
    	private int Order_Id;

        private boolean tienePlanificacion;
        
        private int indneg = 0;

        
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
                ProcessInfoParameter[] para = getParameter();
                
                
               
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();

			if (para[i].getParameter() == null)
				;
			else if (name.equals("AD_Org_ID"))
                        {    
				p_AD_Org_ID = ((BigDecimal)para[i].getParameter()).intValue();
                                
                        }                       
                        else if (name.equals("S_Resource_ID"))
                        {    
				p_S_Resource_ID = ((BigDecimal)para[i].getParameter()).intValue();
                                
                        }

                        else if (name.equals("Version"))
                        {    
				p_Version = (String)para[i].getParameter();
                                
                        }
                        else
				log.log(Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}
	}	//	prepare


       
     protected String doIt() throws Exception                
     { 
         /*if (true)
             throw new RuntimeException("TEST");*/
          /*
           *  31/05/2013 Maria Jesus
           *  Creacion de una variable del contexto que me indica que se esta corriendo
           *  en MRP, para que se puedan borrar las OM.
           */
         Env.setContext(Env.getCtx(), "RUN_MRP", "true");
         System.out.println("Nueva version");
         return runMRP();
     } 
     
     public boolean deleteMRP() throws Exception
     {
         
                /** BISion - 18/11/2008 - Santiago Iba�ez
                 * Se comento cada delete y se agregaron delete transaccionales.
                 * A cada orden y requisicion se le asigna la transaccion global
                 * definida en SvrProcess.
                 * En lugar de retornar false, propaga la excepcion capturada.
                 */
                System.out.println("begin MRP-deleteMRP()");

                AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));

                p_AD_Org_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Org_ID"));

                Trx trx = Trx.get(Trx.createTrxName("deleteMRP"), true);

                						                
                /*
                 * 03-05-2011 Camarzana Mariano
                 * Se agrego que elimine los registros MPC_MRP cuando se trate de orden de ventas
                 */
                String sql = "DELETE FROM MPC_MRP  WHERE TypeMRP = 'SOO' AND AD_Client_ID=" + AD_Client_ID  + " AND AD_Org_ID=" + p_AD_Org_ID;
                DB.executeUpdate(sql, trx.getTrxName());
                
                
                //String sql = "DELETE FROM MPC_MRP  WHERE TypeMRP = 'MOP' AND EXISTS(SELECT MPC_Order_ID FROM MPC_Order o WHERE o.MPC_Order_ID = MPC_Order_ID AND o.DocStatus IN ('DR','CL')) AND AD_Client_ID=" + AD_Client_ID;
                sql = "DELETE FROM MPC_MRP  WHERE TypeMRP = 'MOP' AND DocStatus IN ('DR','CL') AND AD_Client_ID=" + AD_Client_ID  + " AND AD_Org_ID=" + p_AD_Org_ID;
				
                DB.executeUpdate(sql, trx.getTrxName());
                //DB.executeUpdate(sql);
                
                //sql = "DELETE FROM MPC_MRP mrp WHERE mrp.TypeMRP = 'POR' AND EXISTS(SELECT M_Requisition_ID FROM M_Requisition r WHERE r.M_Requisition_ID = mrp.M_Requisition_ID AND (r.DocStatus='DR' AND r.DocStatus='CL') ) AND mrp.AD_Client_ID = " + AD_Client_ID;
                sql = "DELETE FROM MPC_MRP WHERE TypeMRP = 'POR' AND DocStatus='DR' AND AD_Client_ID = " + AD_Client_ID +  " AND AD_Org_ID=" + p_AD_Org_ID;
		
                DB.executeUpdate(sql, trx.getTrxName());

                //Se borran todos los registros MRP asociados a lineas de requisiciones ya inexistentes
                sql = " delete from mpc_mrp where mpc_mrp_id in (select mrp.mpc_mrp_id from mpc_mrp mrp"+
                      " left join m_requisitionline rl on rl.m_requisitionline_id = mrp.m_requisitionline_id"+
                      " where rl.m_requisitionline_id is null and mrp.m_requisitionline_id is not null)";
                DB.executeUpdate(sql, trx.getTrxName());
                //Se borran todos los registros MRP asociados a lineas de ordenes de compra ya inexistentes
                sql = " delete from mpc_mrp where mpc_mrp_id in (select mrp.mpc_mrp_id from mpc_mrp mrp "+
                      " left join c_orderline ol on ol.c_orderline_id = mrp.c_orderline_id"+
                      " where ol.c_orderline_id is null and mrp.c_orderline_id is not null)";
                DB.executeUpdate(sql, trx.getTrxName());
                //Se borran todos los registros MRP asociados a lineas de formulas ya inexistentes
                sql = " delete from mpc_mrp where mpc_mrp_id in (select mrp.mpc_mrp_id from mpc_mrp mrp"+
                      " left join mpc_order_bomline bomline on bomline.mpc_order_bomline_id= mrp.mpc_order_bomline_id"+
                      " where bomline.mpc_order_bomline_id is null and mrp.mpc_order_bomline_id is not null)";
                DB.executeUpdate(sql, trx.getTrxName());
                //Se borran todos los registros MRP asociados a lineas de pronosticos ya inexistentes
                sql = " delete from mpc_mrp where mpc_mrp_id in (select mrp.mpc_mrp_id from mpc_mrp mrp "+
                      " left join m_forecastline f on f.m_forecastline_id= mrp.m_forecastline_id "+
                      " where f.m_forecastline_id is null and mrp.m_forecastline_id is not null)";
                DB.executeUpdate(sql, trx.getTrxName());
                
                /**
                 * BISion - 25/08/2009 - Santiago Iba�ez
                 * Modificacion realizada para borrar unicamente las notas cuya descripcion
                 * sea 'Proceso Generar Plan de Materiales'
                 */

                int mrpTolerance01_message_ID = MMessage.getAD_Message_ID(Env.getCtx(), "MRP-TOLERANCE-01");
                
                //Modificacion para que no elimine las notas cuyo ID sea mrpTolerance01_message_ID, ya que las mismas se generan en el MRPUpdate
                sql = "DELETE FROM AD_Note WHERE description =  'Proceso Generar Plan de Materiales' AND AD_Client_ID = " + AD_Client_ID  + " AND AD_Org_ID=" + p_AD_Org_ID + " AND AD_message_ID <> "+mrpTolerance01_message_ID;
				
                DB.executeUpdate(sql, trx.getTrxName());

                sql = "SELECT o.MPC_Order_ID FROM MPC_Order o" +
                	  " WHERE o.DocStatus = 'DR' AND o.AD_Client_ID = " + AD_Client_ID  +
                	  " AND AD_Org_ID=" + p_AD_Org_ID;
                
                ResultSet rs = null;
                PreparedStatement pstmt = null;
                
                try
		{
                        pstmt = DB.prepareStatement (sql,trx.getTrxName());
                        rs = pstmt.executeQuery();
                        while (rs.next())
                        {
                            MMPCOrder order = new  MMPCOrder(Env.getCtx(), rs.getInt(1),trx.getTrxName());
                            System.out.println("Borrando OM nro: "+order.getDocumentNo());
                            if (!order.delete(true,trx.getTrxName()))
                            //Genero nota
                            enviarNotaFalloDelete(trx.getTrxName(),"La Orden de Manufactura ",order.getDocumentNo(), MMPCOrder.getTableId(MMPCOrder.Table_Name), rs.getInt(1));

                        }
                        
		}
                catch (Exception e)
		{
                	log.log(Level.SEVERE,"doIt - " + sql, e);
                        
		}
                
                finally{
                    if (rs!=null)
                        rs.close();
                    if (pstmt!=null)
                        pstmt.close();
                }
                
                pstmt = null;
                rs = null;
                
                sql = "SELECT  M_Requisition_ID FROM M_Requisition r WHERE  r.DocStatus = 'DR' AND r.AD_Client_ID = " + AD_Client_ID + " AND AD_Org_ID=" + p_AD_Org_ID;
                try
		{
                        
                        pstmt = DB.prepareStatement (sql,trx.getTrxName());
                        //pstmt.setInt(1, p_M_Warehouse_ID);
                        rs = pstmt.executeQuery();
                        while (rs.next())
                        {
                            MRequisition r = new  MRequisition(Env.getCtx(), rs.getInt(1),trx.getTrxName());
                            
                            MRequisitionLine[] rlines = r. getLines();
                            for ( int i= 0 ; i < rlines.length; i++ )
                            {
                            	MRequisitionLine line =  rlines[i];
                            	if (!line.delete(true,trx.getTrxName()))
                                    //Genero nota
                                    enviarNotaFalloDelete(trx.getTrxName(),"Una de las lineas de la Requisición ",r.getDocumentNo(),MRequisitionLine.getTableId(MRequisitionLine.Table_Name), line.getM_Requisition_ID());
                            }
                            if (!r.delete(true,trx.getTrxName()))
                                //Genero nota
                                enviarNotaFalloDelete(trx.getTrxName(),"La Requisición ",r.getDocumentNo(), MRequisition.getTableId(MRequisition.Table_Name),rs.getInt(1));
                        }
                        
                        System.out.println("end deleteMRP()");

		}
                catch (Exception e)
		{
                	log.log(Level.SEVERE,"doIt - " + sql, e);
                        
		}
                finally{
                    rs.close();
                    rs = null;
                    pstmt.close();
                    pstmt = null;

                    if (trx.commit()){
                        System.out.println("Cambios Commiteados");
                        trx.close();
                        return true;
                    }
                    else
                    {
                        trx.rollback();
                        trx.close();
                        log.log(Level.SEVERE, "No se pudieron Commitear los cambios generados al eliminar OM y Requisicion");
                        return false;
                    }
                    
                }                
     }
     
     public boolean createGrossRequirements(int MPC_Order_ID)
     {
      //Get work order requirements
                
        //String sql = "SELECT ol.MPC_Order_BOMLine_ID FROM MPC_Order_BOMLine ol  WHERE o.M_Warehouse_ID = ? AND o.MPC_Order_ID = ? AND ol.AD_Client_ID = " + AD_Client_ID;
    	 String sql = "SELECT MPC_Order_BOMLine_ID" +
    	 	      " FROM MPC_Order_BOMLine ol" +
    	 	      "  WHERE o.MPC_Order_ID = ? AND ol.AD_Client_ID = " + AD_Client_ID+ " AND AD_Org_ID=" + p_AD_Org_ID;
    	//String sql = "SELECT ol.MPC_Order_BOMLine_ID FROM MPC_Order_BOMLine ol  WHERE o.MPC_Order_ID = ? AND ol.AD_Client_ID = " + AD_Client_ID;        
         
         MMPCOrder o = new  MMPCOrder(Env.getCtx(),MPC_Order_ID,get_TrxName());
		PreparedStatement pstmt = null;                
		try
		{
			pstmt = DB.prepareStatement (sql,get_TrxName());
                        pstmt.setInt(1, MPC_Order_ID);                      
                        
                        ResultSet rs = pstmt.executeQuery ();
                        while (rs.next())
                        {
                        MMPCOrderBOMLine ol = new MMPCOrderBOMLine(Env.getCtx(),rs,get_TrxName());
                        MMPCMRP.MPC_Order_BOMLine(ol,o ,get_TrxName());
                        }
                        rs.close();
                        rs = null;
                        pstmt.close();
                        pstmt = null;
                        return true;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"doIt - " + sql, e);
                        return false;
		}
                
     }           
    
  
  public boolean createScheduledReceipts(MMPCOrder o)
  {

                         MMPCOrder order = new MMPCOrder(getCtx(), o.getMPC_Order_ID(),null);               
                         int MPC_MRP_ID = MMPCMRP.MPC_Order(order,null);
                         MNote note = new MNote (Env.getCtx(),  1000015, o.getPlanner_ID() , MMPCMRP.getTableId(MMPCMRP.Table_Name) , MPC_MRP_ID ,  "Order:" + o.getDocumentNo() + " Release:" + o.getDateStartSchedule() , Msg.getMsg(Env.getCtx(), "MRP-060"),null);
                         note.setDescription("Proceso Generar Plan de Materiales");
                         /*
                         *  03/06/2013 Maria Jesus
                         *  Se agrega para que guarde en el log en caso de que no pueda guardar la nota.
                         */
                         if (!note.save()){
                            log.log(Level.SEVERE, "No se pudo guardar la Nota: "+ note.getTextMsg());
                            }
                         return true;
                
  }
  
  
public String runMRP() throws Exception
{      
	  
        try {               
        // Get Gross Requieriments Independence Demand
        StringBuffer result = new StringBuffer("");

        // Maneja las ordenes asociadas a un producto cuando creo las relaciones entre las ordenes de los diferentes niveles

        Vector allOrders = new Vector();

        result.append("Run MRP .......................................\n");

        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try
        {
            //String sql = "SELECT LowLevel FROM MPC_MRP mrp INNER JOIN M_Product p ON (p.M_Product_ID =  mrp.M_Product_ID) WHERE mrp.M_Warehouse_ID = ? ORDER BY  p.LowLevel DESC ";

            MProduct product =  null;
            Timestamp DatePromised = null;
            Timestamp Today = new Timestamp (System.currentTimeMillis());
            String sql = null;


            int BeforeMPC_MRP_ID = 0;
            BigDecimal CurrentQtyGrossReqs = Env.ZERO;
            Timestamp DateStartSchedule = null;
            //Timestamp DateFinishSchedule = null;
            //Timestamp  BeforeDateFinishSchedule = null;
            Timestamp  BeforeDateStartSchedule = null;
            Timestamp  POQDateStartSchedule = null;

            DB.executeUpdate("UPDATE MPC_MRP SET IsAvailable ='Y'" +
                             " WHERE Type = 'S'  AND AD_Client_ID = " +
                             AD_Client_ID+" AND AD_Org_ID=" + p_AD_Org_ID,get_TrxName());


            Order_Policy = MMPCProductPlanning.ORDER_POLICY_LoteForLote;;

            int lowlevel = MMPCMRP.getMaxLowLevel(get_TrxName());
            //int lowlevel = 0;
            int Levels = lowlevel; //MMPCMRP.getMaxLowLevel(get_TrxName()); //lowlevel;                            ;
            //System.out.println("Low Level >>>>>>>>>>>>>>>>"+lowlevel);
            // Calculate MRP for all levels
            for (int index = 0 ; index <= lowlevel ; index++)
            {

                //System.out.println(".............Levels :" + Levels);

                /*
                 *   SQL Modificado para hacer el control de la instancia de attributo del material de composicion para el
                 *   producto.
                 *
                 *   Order_ID para manejar como parametro la Orden Origen del Producto.
                 *
                 *   Autor: JMF
                 **/


                /*sql = "SELECT p.M_Product_ID ,p.Name , p.LowLevel , mrp.Qty , mrp.DatePromised, mrp.Type ," +
                          " mrp.TypeMRP , mrp.DateOrdered , mrp.M_Warehouse_ID , mrp.MPC_MRP_ID ,  mrp.DateStartSchedule ," +
                          " mrp.DateFinishSchedule, mrp.MPC_Order_ID, mrp.M_FORECASTLINE_ID" +
                          " FROM MPC_MRP mrp INNER JOIN M_Product p ON (p.M_Product_ID =  mrp.M_Product_ID)" +
                          " WHERE mrp.Type='D' AND p.LowLevel = "+ index + " AND mrp.AD_Client_ID = " + AD_Client_ID +
                          " AND mrp.AD_Org_ID=" + p_AD_Org_ID +" ORDER BY  p.LowLevel DESC ,  p.M_Product_ID , mrp.DatePromised  ";
                */
               /*
                * 02-05-2011 Camarzana Mariano
                * Modificado para que no me tome los registros MPC_MRP de tipo 'SOO'
                */
                
                sql = "SELECT p.M_Product_ID ,p.Name , p.LowLevel , mrp.Qty , mrp.DatePromised, mrp.Type ," +
                  " mrp.TypeMRP , mrp.DateOrdered , mrp.M_Warehouse_ID , mrp.MPC_MRP_ID ,  mrp.DateStartSchedule ," +
                  " mrp.DateFinishSchedule, mrp.MPC_Order_ID, mrp.M_FORECASTLINE_ID" +
                  " FROM MPC_MRP mrp INNER JOIN M_Product p ON (p.M_Product_ID =  mrp.M_Product_ID)" +
                  " WHERE mrp.Type='D' and mrp.typemrp <> 'SOO' AND p.LowLevel = "+ index + " AND mrp.AD_Client_ID = " + AD_Client_ID +
                  //Para que no tome todo
                 // " AND mrp.CREATED >= to_date('2013/07/15', 'yyyy/mm/dd')" +
                  " AND mrp.AD_Org_ID=" + p_AD_Org_ID +" ORDER BY  p.LowLevel DESC ,  p.M_Product_ID , mrp.DatePromised, mrp.mpc_mrp_id  ";

                pstmt = DB.prepareStatement (sql,get_TrxName());
                rs = pstmt.executeQuery();

                
                int count = 0;
                while (rs.next())
                {
                    count++;
                    //System.out.println("Registro MRP nro: "+count);
                    String Type = rs.getString("Type");
                    String TypeMRP = rs.getString("TypeMRP");
                    //Set Global Variable
                    DatePromised = rs.getTimestamp("DatePromised");
                    DateStartSchedule =   rs.getTimestamp("DateStartSchedule");

                    Order_Id = rs.getInt("MPC_Order_ID");


                    /*System.out.println("REFERENCIAS DE LAS ORDENES - Order_Id: " + Order_Id + " BeforeMPC_MRP_ID: " +
                                BeforeMPC_MRP_ID + " PRODUCT_ID: " + rs.getInt("M_Product_ID"));*/




                    //BigDecimal Qty = rs.getBigDecimal("Qty");
                    M_Warehouse_ID = rs.getInt("M_Warehouse_ID");

                    if (Type.equals("D") && TypeMRP.equals("FCT") && DatePromised.compareTo(Today) <= 0)
                    {
                      /** BISion - 23/07/2009 - Santiago Iba�ez
                       * Modificacion realizada para que muestre aviso de que este pronostico no se tomo en cuenta
                       */
                        MProduct p = new MProduct(getCtx(), rs.getInt("M_Product_ID"), get_TrxName());
                        MNote nota = new MNote(Env.getCtx(), "MRP-130", Env.getAD_User_ID(Env.getCtx()), null);
                        nota.setRecord_ID(rs.getInt("M_FORECASTLINE_ID"));
                        nota.setReference(p.getValue() + " " + p.getName());
                        nota.setDescription("Proceso Generar Plan de Materiales");
                        nota.setAD_Table_ID(MForecastLine.getTableId(MForecastLine.Table_Name));
                        nota.setTextMsg("El producto "+p.getValue()+" - "+p.getName()+" tiene un pronostico cuya fecha prometida ("+getFechaFromTimeStamp(DatePromised)+") es anterior a la actual ("+getFechaFromTimeStamp(Today)+").");
                        nota.save();
                        //fin modificacion BISion
                        continue;
                    }
                    int pM_Product_ID = rs.getInt(1);
                    int hoy = 0;
                    if (pM_Product_ID==1006752||pM_Product_ID==1008008)//||pM_Product_ID==1010372||pM_Product_ID==1009595)
                        hoy = 3;
                    
                    /*if ( pM_Product_ID==1004991||pM_Product_ID==1005027||pM_Product_ID==1005010||pM_Product_ID==1005037||pM_Product_ID==1006868||pM_Product_ID==1008650||pM_Product_ID==1010012||pM_Product_ID==1010014){
                        hoy = 1;
                    }*/

                    if ( product == null || product.getM_Product_ID() != rs.getInt("M_Product_ID"))
                    {
                        //System.out.println("Cantidad Remanente = " + QtyGrossReqs);
                        //if exist QtyGrossReq of last Demand verify plan
                        if (!QtyGrossReqs.equals(Env.ZERO))
                        {
                            //QtyGrossReqs = BeforeQty.add(QtyGrossReqs);
                            //calculatePlan(BeforeMPC_MRP_ID ,product, QtyGrossReqs ,BeforeDateStartSchedule,BeforeDateFinishSchedule);
                            /**
                             * BISIon - 18/08/2009 - Santiago Ibañez
                             * Modificacion realizada para que si no tiene planificacion el producto
                             * no mande a comprar o producir.
                             */
                            if (SupplyPlanner_ID!=0 && DemandPlanner_ID!=0)
                                calculatePlan(Order_Id, BeforeMPC_MRP_ID , allOrders, product, QtyGrossReqs ,BeforeDateStartSchedule,DatePromised);                           
                            QtyGrossReqs = Env.ZERO;
                        }
                        
                        //Antes de cambiar el Producto si la politica era agrupar por periodo POQ
                        //entonces chequeo TODAS las demandas a programar
                        try{
                            if (product!=null&&Order_Policy.equals(MMPCProductPlanning.ORDER_POLICY_PeriodOrderQuantity)){
                                Vector demandas = this.getDemandasReprogramar(M_Product_ID,QtyAlmacenadaInicial, Today);
                                this.generarAvisosPosponerDemanda(product, demandas);
                            }
                        }
                        catch(Exception e){
                            System.out.println("No se pudo crear aviso de posponer demanda para el producto "+M_Product_ID);
                        }
                        product = new MProduct(Env.getCtx(), rs.getInt("M_Product_ID"),get_TrxName());
                        //System.out.println("Nuevo producto:" + product.getName());

                        setProduct(product);
                        allOrders.removeAllElements();
                        if (!tienePlanificacion)
                            continue;

                        /**
                         * BISion - 18/08/2009 - Santiago Ibañez
                         * Modificacion realizada para que si un producto no tiene planeacion
                         * evite de considerarlo y se continue con el proceso.
                         */
                         if (SupplyPlanner_ID==0 && DemandPlanner_ID==0)
                             continue;
                        //fin modificacion BISion

                        //first DatePromised.compareTo for ORDER_POLICY_PeriodOrderQuantity
                        if (Order_Policy.equals(MMPCProductPlanning.ORDER_POLICY_PeriodOrderQuantity))
                        {
                            DatePromisedFrom = DatePromised;
                            DatePromisedTo = TimeUtil.addDays(DatePromised , Order_Period.intValue());

                            if (index == 0 )
                            POQDateStartSchedule = DatePromised;
                            else
                            POQDateStartSchedule = DateStartSchedule;
                        }

                        //if (Order_Policy.equals(MMPCProductPlanning.ORDER_POLICY_LoteForLote))
                        //{
                        // DateStartSchedule =  rs.getTimestamp("DateStartSchedule");
                        //}


                    }
                    if (!tienePlanificacion)
                        continue;

                    /**
                     * BISion - 17/09/2009- Santiago Iba�ez
                     * Modificacion realizada para que si la politica es POQ
                     * en el ultimo calculatePlan tome la fecha de la primer demanda del periodo
                     */
                    //BeforeDateStartSchedule =  DateStartSchedule;
                    if (Order_Policy.equals(MMPCProductPlanning.ORDER_POLICY_PeriodOrderQuantity))
                        BeforeDateStartSchedule = POQDateStartSchedule;
                    else
                        BeforeDateStartSchedule =  DateStartSchedule;
                    //fin modificacion BISion
                    BeforeMPC_MRP_ID = rs.getInt("MPC_MRP_ID");

                    // Creae Notice for Demand due
                    if(DatePromised.compareTo(Today) < 0)
                    {
                        MNote note = new MNote (Env.getCtx(),  1000013, DemandPlanner_ID, MMPCMRP.getTableId(MMPCMRP.Table_Name), rs.getInt("MPC_MRP_ID") ,  product.getValue() + " " + product.getName()  , Msg.getMsg(Env.getCtx(), "MRP-040"),null);
                        MMPCOrder order = new MMPCOrder(getCtx(), Order_Id, get_TrxName());
                        /*if (Order_Id==0||order.getDocumentNo()==null)
                            System.out.println("Orden de Manufactura cero");*/
                        //OJO!!: ACA ���QUE SE HACE CON LAS COMPLETAS???
                        note.setTextMsg("La Orden de Manufactura nro: "+order.getDocumentNo()+" tiene fecha prometida ("+getFechaFromTimeStamp(DatePromised)+") anterior a la actual ("+getFechaFromTimeStamp(Today)+")");
                        note.setAD_Table_ID(MMPCOrder.getTableId(MMPCOrder.Table_Name));
                        note.setRecord_ID(Order_Id);
                        note.setDescription("Proceso Generar Plan de Materiales");
                        /*
                         *  03/06/2013 Maria Jesus
                         *  Se agrega para que guarde en el log en caso de que no pueda guardar la nota.
                         */
                        if (!note.save()){
                            log.log(Level.SEVERE, "No se pudo guardar la Nota: ");
                        }
                    }



                    /*
                     *   Lo que hago es con cada pasada agrego la orden actual al arreglo, luego si tengo
                     *   como politica asociar por cantidades el arreglo va a tener tantas ordenes como requisitos
                     *   del producto existan con lo cual al momento de crear la orden en calculatePlan puedo tener
                     *   todas las asociaciones y quer me queden las relaciones consistentes.
                     *
                     *   TENER EN CUENTA ESTE ECHO DE GRABAR LA INSTANCIA DE LA ORDEN O REQUISICION CUANDO VOY POR EL REGISTRO
                     *   SIGUIENTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                     *
                     *
                     *
                     */
                     if (SupplyPlanner_ID==0 && DemandPlanner_ID==0)
                        continue;

                    // Verify if is ORDER_POLICY_PeriodOrderQuantity and DatePromised < DatePromisedTo then Accumaltion QtyGrossReqs
                    if (Order_Policy.equals(MMPCProductPlanning.ORDER_POLICY_PeriodOrderQuantity) && DatePromised.compareTo(DatePromisedTo) < 0 )
                    {
                        allOrders.add(new Integer(Order_Id));
                        QtyGrossReqs = QtyGrossReqs.add(rs.getBigDecimal("Qty"));
                        //BeforeQty = BeforeQty + Env.ZERO;
                        //System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX  Acumulation   QtyGrossReqs:" + QtyGrossReqs);
                        continue;
                    }
                    else if (Order_Policy.equals(MMPCProductPlanning.ORDER_POLICY_PeriodOrderQuantity))// if not then create new range for next period
                    {
                        calculatePlan(Order_Id, rs.getInt("MPC_MRP_ID"), allOrders, product, QtyGrossReqs ,POQDateStartSchedule,DatePromised);
                        allOrders.removeAllElements();

                                QtyGrossReqs = rs.getBigDecimal("Qty");
                                DatePromisedFrom = DatePromised;
                                DatePromisedTo = TimeUtil.addDays(DatePromised, Order_Period.intValue());
                                allOrders.add(new Integer(Order_Id));
                                if (index == 0 )
                                POQDateStartSchedule = DatePromised;
                                else
                                POQDateStartSchedule = DateStartSchedule;

                                if (Order_Policy.equals(MMPCProductPlanning.ORDER_POLICY_PeriodOrderQuantity))
                                    BeforeDateStartSchedule = POQDateStartSchedule;
                                else
                                    BeforeDateStartSchedule =  DateStartSchedule;

                                continue;
                    }
                    // If  Order_Policy = LoteForLote then always create new range for next period and put QtyGrossReqs
                    if (Order_Policy.equals(MMPCProductPlanning.ORDER_POLICY_LoteForLote))
                    {
                        allOrders.add(new Integer(Order_Id));
                        QtyGrossReqs = rs.getBigDecimal("Qty");
                        calculatePlan(Order_Id, rs.getInt("MPC_MRP_ID"), allOrders, product,  QtyGrossReqs , rs.getTimestamp("DateStartSchedule"),DatePromised);
                        allOrders.removeAllElements();
                        continue;
                    }
                    /**
                     * BISion - 18/08/2009 - Santiago Iba�ez
                     * Agregado para considerar la Politica de Cantidad Fija
                     */
                    if (Order_Policy.equals(MMPCProductPlanning.ORDER_POLICY_OrderFixedQuantity)){
                        allOrders.add(new Integer(Order_Id));
                        QtyGrossReqs = rs.getBigDecimal("Qty");
                        calculatePlan(Order_Id, rs.getInt("MPC_MRP_ID"), allOrders, product,  QtyGrossReqs , rs.getTimestamp("DateStartSchedule"),DatePromised);
                        allOrders.removeAllElements();
                        continue;
                    }
                    //fin modificacion BISion



                } // end while
                rs.close();
                rs = null;
                pstmt.close();
                pstmt = null;
                //if exist QtyGrossReq of last Demand after finish while verify plan
                if (!QtyGrossReqs.equals(Env.ZERO))
                {
                    //QtyGrossReqs = BeforeQty.add(QtyGrossReqs);
                    //calculatePlan(BeforeMPC_MRP_ID ,product, QtyGrossReqs ,BeforeDateStartSchedule,BeforeDateFinishSchedule);
                    
                    allOrders.add(new Integer(Order_Id));
                    calculatePlan(Order_Id, BeforeMPC_MRP_ID, allOrders, product, QtyGrossReqs ,BeforeDateStartSchedule,DatePromised);
                    allOrders.removeAllElements();
                    //QtyGrossReqs = Env.ZERO;
                }
                Levels = Levels - 1;
            } // end for
      } // try
      catch (SQLException ex)
      {
            log.log(Level.SEVERE,"getLines", ex);
            //throw ex;
      }
       
      Trx trx = Trx.get(get_TrxName(), false);
      if (trx.commit())
          log.log(Level.SEVERE, "Se commitiaron exitosamente los cambios del Calculate Plan");
      else
          log.log(Level.SEVERE, "Hubo un error al commitear los cambios del Calculate Plan");
        
        
      System.out.println("Resultado:" + result.toString());
      
      
      //Actualizo tabla MStorage volviendo a activo los registros desactivados previos a la corrida
      String slqUpdate = "UPDATE M_Storage ms SET isactive = 'Y'"
              + " WHERE ms.isactive = 'N' "
              + " AND M_Locator_ID in ( SELECT ml.m_locator_id from M_Locator ml "
                                        + "Inner Join M_Warehouse mw ON(mw.M_Warehouse_ID=ml.M_Warehouse_ID and mw.IsRequiredMRP='Y') )"
              + " AND CREATED >  to_date('2014/01/01', 'yyyy/mm/dd')";
      DB.executeUpdate(slqUpdate,trx.getTrxName());

      if (trx.commit())
          log.log(Level.SEVERE, "Se actualizaron exitosamente los registros MStorage");
      else
          log.log(Level.SEVERE, "Hubo un error al actualizar los registros MStorage");
      
      
       }
       catch (Exception e){
           log.log(Level.SEVERE,"RUN MRP", e);
       }
      return "ok";
}
  
  	private void setProduct(MProduct product) throws Exception
	{
       
            //System.out.println("!!!!!!!!!!!!!!!Nuevo producto:");
            //M_Product_ID = rs.getInt("M_Product_ID");
            //product = new MProduct(getCtx(), M_Product_ID);                                         
            M_Product_ID = product.getM_Product_ID();
            // Demand Date
            //M_Warehouse_ID = rs.getInt("M_Warehouse_ID");
            MMPCProductPlanning ppd = MMPCProductPlanning.getDemandWarehouse(getCtx(), p_AD_Org_ID , M_Product_ID , M_Warehouse_ID,get_TrxName());
            
            QtyDemandadaSatisfecha = BigDecimal.ZERO;

            DatePromisedTo = null;
            DatePromisedFrom = null;
            //QtyGrossReqs = Env.ZERO;
            //public static MMPCProductPlanning getDemandWarehouse(Properties ctx , int AD_Org_ID , int M_Product_ID, int M_Warehouse_ID)
            
            if (ppd != null)
            {    
            TransfertTime = ppd.getTransfertTime();                                      
            MPC_Product_BOM_ID = ppd.getMPC_Product_BOM_ID();
            S_Resource_ID = ppd.getS_Resource_ID();
            DemandPlanner_ID = ppd.getPlanner_ID();
            
            System.out.println("TransfertTime" + TransfertTime);
            System.out.println("MPC_Product_BOM_ID" + MPC_Product_BOM_ID);
            System.out.println("S_Resource_ID" + S_Resource_ID);                                        
            tienePlanificacion = true;
            if (MPC_Product_BOM_ID == 0)
            {
              MPC_Product_BOM_ID = MMPCProductBOM.getBOMSearchKey(M_Product_ID);
            }
            }
            else
            {
                
            MNote note = new MNote (Env.getCtx(),  1000020, 0 , MMPCMRP.getTableId(MMPCMRP.Table_Name), 0 ,  product.getValue() + " " + product.getName()  , Msg.getMsg(Env.getCtx(), "MRP-110"),null);
            note.setDescription("Proceso Generar Plan de Materiales");
            /*
             *  03/06/2013 Maria Jesus
             *  Se agrega para que guarde en el log en caso de que no pueda guardar la nota.
             */
            if (!note.save()){
                log.log(Level.SEVERE, "No se pudo guardar la Nota: "+ note.getTextMsg());
            }
            //System.out.println("Error:no existe datos del almacen de demanda");    
            tienePlanificacion = false;
            return;
            //continue;
            }
            
            //Supply Data
            MMPCProductPlanning pps = MMPCProductPlanning.getDemandSupplyResource(getCtx(), p_AD_Org_ID , M_Product_ID, S_Resource_ID);                                       
            if (pps != null)
            {            
            AD_Workflow_ID = pps.getAD_Workflow_ID();
            DeliveryTime_Promised = pps.getDeliveryTime_Promised();
            IsCreatePlan = pps.isCreatePlan();     
            Order_Max = pps.getOrder_Max();
            Order_Min = pps.getOrder_Min();
            Order_Pack = pps.getOrder_Pack();
            Order_Qty = pps.getOrder_Qty();
            Order_Period = pps.getOrder_Period();
            Order_Policy = pps.getOrder_Policy();            
            SupplyM_Warehouse_ID =  pps.getM_Warehouse_ID();
            SupplyPlanner_ID = pps.getPlanner_ID();
            Yield = pps.getYield();
            System.out.println("S_Resource_ID" + S_Resource_ID);
            System.out.println("AD_Workflow_I" + AD_Workflow_ID);
            System.out.println("DeliveryTime_Promised" + DeliveryTime_Promised);
            System.out.println("IsCreatePlan" + IsCreatePlan);
            System.out.println("Order_Max" + Order_Max);
            System.out.println("Order_Min" + Order_Min);
            System.out.println("Order_Pack" + Order_Pack);
            System.out.println("Order_Period" + Order_Period);
            System.out.println("Order_Policy" + Order_Policy);
            System.out.println("SupplyM_Warehouse_ID" + SupplyM_Warehouse_ID);
            tienePlanificacion = true;
            }
            else
            {    
                MNote note = new MNote (Env.getCtx(),  1000021, 0 , MMPCMRP.getTableId(MMPCMRP.Table_Name), 0 ,  product.getValue() + " " + product.getName()  , Msg.getMsg(Env.getCtx(), "MRP-120"),null);
                note.setDescription("Proceso Generar Plan de Materiales");
                /*
                 *  03/06/2013 Maria Jesus
                 *  Se agrega para que guarde en el log en caso de que no pueda guardar la nota.
                 */
                if (!note.save()){
                    log.log(Level.SEVERE, "No se pudo guardar la Nota: "+ note.getTextMsg());
                }
                //System.out.println("Erro:no existe almacen de suministro");
                tienePlanificacion = false;
                return;
            }    
            
//        	Find Vendor
    		//m_product = MProduct.get(getCtx(), rLine.getM_Product_ID());
                if(product.isPurchased())
                {    
    		int C_BPartner_ID = 0;
    		MProductPO[] ppos = MProductPO.getOfProduct(getCtx(), product.getM_Product_ID(), null);
    		for (int i = 0; i < ppos.length; i++)
    		{
    			if (ppos[i].isCurrentVendor() && ppos[i].getC_BPartner_ID() != 0)
    			{
    				//C_BPartner_ID = ppos[i].getC_BPartner_ID();
    				DeliveryTime_Promised = new BigDecimal(ppos[i].getDeliveryTime_Promised());    	                	            
                                Order_Min = ppos[i].getOrder_Min();
                                Order_Pack = ppos[i].getOrder_Pack();    	               	                			
    			}
    		}
                }
    		    		
    		
            /*if (AD_Workflow_ID == 0 )
            System.out.println("Error: No existe Flujo de Trabajo para el Producto" + M_Product_ID);*/
            
            if (Order_Policy == null)
            	Order_Policy = MMPCProductPlanning.ORDER_POLICY_LoteForLote;
            
            //QtyOnHand = getOnHand(M_Product_ID);
            

            QtyProjectOnHand = MMPCMRP.getOnHand( p_AD_Org_ID , M_Product_ID, get_TrxName());
            if(QtyProjectOnHand == null)
                QtyProjectOnHand = Env.ZERO;
            QtyAlmacenadaInicial = QtyProjectOnHand;
           
            
            //result.append("---------------------------------------------------------------\n");
            //result.append("Product " + rs.getString("Name") + " On Hand: " + QtyOnHand + "\n");
            //result.append("--------------------   -------------------------------------------\n");                                        
            //result.append("        Due Date        Gross Reqs     Sched Rcpt     Project On Hand     Order Plan \n");
                    QtyScheduledReceipts = Env.ZERO;  

                    /** BISion - 22/07/2009 - Santiago Ibañez
                     * Es necesario realizar la siguiente consideracion:
                     * Los registros MRP de tipo suministro a considerar deben tener una fecha como parametro ya que
                     * puede existir una demanda de x cantidad de un producto y va a mandar a comprar o fabricar si no hay.
                     * El hecho de que haya o no depende de:
                     *   1 - la existencia real
                     *   2 - la cantidad programada a recibir que se obtiene de los registros MRP de tipo suministro.
                     * El parametro 2 debe dividirse en:
                     *   2.1 - Cantidad programada a recibir antes de la fecha prometida de la demanda
                     *   2.2 - Cantidad programada a recibir despues de la fecha prometida de la demanda
                     * Si 1 + 2.1 supera o equipara lo demandado no hace nada.
                     * Sino si 1 + 2.2 supera o equipara lo demandado generar aviso para reprogramar
                     * Sino mandar a comprar o fabricar.
                     */

                    //System.out.println("SELECT SUM(Qty) FROM MPC_MRP mrp WHERE mrp.DocStatus = 'CO' AND mrp.IsAvailable = 'Y' AND mrp.Type = 'S' AND mrp.M_Product_ID = " + M_Product_ID  + " AND mrp.AD_Client_ID = " + AD_Client_ID + " AND mrp.AD_Org_ID="+ p_AD_Org_ID);
                    //PreparedStatement pstmt = DB.prepareStatement("SELECT nvl(SUM(Qty),0) FROM MPC_MRP mrp WHERE mrp.DocStatus = 'CO' AND mrp.IsAvailable = 'Y' AND mrp.Type = 'S' AND mrp.M_Product_ID = " + M_Product_ID  + " AND mrp.AD_Client_ID = " + AD_Client_ID + " AND mrp.AD_Org_ID="+ p_AD_Org_ID,get_TrxName());
                   // System.out.println("SELECT SUM(Qty) FROM MPC_MRP mrp WHERE (mrp.DocStatus = 'CO' or mrp.docstatus='DR') AND mrp.IsAvailable = 'Y' AND mrp.Type = 'S' AND mrp.M_Product_ID = " + M_Product_ID  + " AND mrp.AD_Client_ID = " + AD_Client_ID + " AND mrp.AD_Org_ID="+ p_AD_Org_ID);
                    PreparedStatement pstmt = DB.prepareStatement("SELECT nvl(SUM(Qty),0) FROM MPC_MRP mrp WHERE (mrp.DocStatus = 'CO' or mrp.docstatus='DR' and mrp.typemrp = 'POO') AND mrp.IsAvailable = 'Y' AND mrp.Type = 'S' AND mrp.M_Product_ID = " + M_Product_ID  + " AND mrp.AD_Client_ID = " + AD_Client_ID + " AND mrp.AD_Org_ID="+ p_AD_Org_ID,get_TrxName());
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()){
                        QtyScheduledReceipts = rs.getBigDecimal(1);
                        if (QtyScheduledReceipts==null)
                            QtyScheduledReceipts = Env.ZERO;
                    }
                    rs.close();
                    rs = null;
                    pstmt.close();
                    pstmt = null;

                    DB.executeUpdate("UPDATE MPC_MRP mrp SET IsAvailable = 'N' WHERE (mrp.DocStatus = 'CO' or mrp.DocStatus ='DR' and mrp.typemrp = 'POO') AND mrp.IsAvailable = 'Y' AND mrp.Type = 'S' AND mrp.M_Product_ID = " + M_Product_ID  + " AND mrp.AD_Client_ID = " + AD_Client_ID + " AND mrp.AD_Org_ID="+ p_AD_Org_ID,get_TrxName());
					
                    /** BISion - 07/05/2009 - Santiago Ibañez
                     * Bloque comentado
                     */
                    /*}
                    
                    
                    catch (SQLException ex)
                    {
                          log.log(Level.SEVERE,"getLines" + sqlsupply, ex);
                          //throw ex;
                    } fin modificacion BISion*/
                    
                    
            //QtyProjectOnHand =  QtyProjectOnHand.add(QtyScheduledReceipts);
            //QtyScheduledReceipts = Env.ZERO;

  		}
  
        
    //private void calculatePlan(int MPC_Order_ID, int MPC_MPR_ID , MProduct product , BigDecimal Qty , Timestamp DemandDateStartSchedule , Timestamp DemandDateFinishSchedule)
    private void calculatePlan(int MPC_Order_ID, int MPC_MPR_ID, Vector allOrders , MProduct product , BigDecimal Qty , Timestamp DemandDateStartSchedule, Timestamp DatePromised) throws Exception
        {        	

                //Ahora las notas se van a crear en el momento en que se crearn las ordenes o requsiciones ya
                //que deben tener una referencia a las mismas.
                //Estas variables son las que se van a consultar para saber si crear o no el aviso.
                boolean crearNotaMax = false;
                boolean crearNotaMin = false;
                Vector parentOrders = new Vector();
                /*
                 * GENEOS - Pablo Velazquez
                 * 08/10/2013
                 * Se agrega vector parentOrders que contiene todas las ordenes de manufactura
                 * las cuales se acumularon para crear la actual
                 * (Creo una copia local para remover las repetidas)
                 */
                for (int i=0; i<allOrders.size();i++){
                    if ( ! parentOrders.contains((Integer)allOrders.get(i)) )
                        parentOrders.add((Integer)allOrders.get(i));
                }
        		//Set Yield o QtyGrossReqs
                        // Note : the viariaves  DemandDateStartSchedule , DemandDateFinishSchedule are same DatePromised to Demands Sales Order Type
        		Timestamp Today = new Timestamp (System.currentTimeMillis());
        		
			//System.out.println("DateFinishSchedule:"+ DemandDateFinishSchedule);

        		BigDecimal  DecimalYield = new BigDecimal(Yield/100);
        		if (!DecimalYield.equals(Env.ZERO))
        		QtyGrossReqs = QtyGrossReqs.divide(DecimalYield, 4 ,BigDecimal.ROUND_HALF_UP);

                        //System.out.println("Producto Renglon" + rs.getRow());
                        System.out.println("###################### Requisition Poduct:" + product.getName() + "Create Plan:" + IsCreatePlan + " OrderPlan:" + QtyPlanned);
                        System.out.println("DemandDateStartSchedule:"+ DemandDateStartSchedule);
                        System.out.println("DeliveryTime_Promised"+ DeliveryTime_Promised);
                        //System.out.println(" DatePromisedFrom:" +  DatePromisedFrom + " DatePromisedTo:" +   DatePromisedTo);    
                        System.out.println("QtyScheduledReceipts:" + QtyScheduledReceipts);
                        System.out.println("    QtyProjectOnHand:" + QtyProjectOnHand);
                        System.out.println("        QtyGrossReqs:" + QtyGrossReqs);
                        System.out.println("              Supply:" + (QtyScheduledReceipts).add(QtyProjectOnHand));
                        

                        /**
                         * BISion - 22/07/2009 - Santiago Iba�ez
                         * Modificacion realizada para calcular correctamente la cantidad neta requerida
                         */
                        //QtyNetReqs = Lo que espero recibir (antes y despues de lo demandado) + lo que tengo - lo que necesito
                        QtyNetReqs = ((QtyScheduledReceipts).add(QtyProjectOnHand)).subtract(QtyGrossReqs);
                        
                        System.out.println("=         QtyNetReqs:" + QtyNetReqs);    
                        
                        //Calculo tolerancia
                        MZYNCATEGORYTOLERANCE pct = MZYNCATEGORYTOLERANCE.getZYNCategoryTolerance(getCtx(), product.getM_Product_Category_ID(), get_TrxName());
                        BigDecimal tolerance =  new BigDecimal(0);
                        if (pct != null)
                            tolerance = pct.getTOLERANCE().divide(  new BigDecimal(100) );                     
                        //Calculo minimo necesario de demanda
                        BigDecimal minQtyTolerance = QtyGrossReqs.subtract(QtyGrossReqs.multiply(tolerance));                    
                        //Calculo si satisfago el minimo
                        BigDecimal minQtyNetReqs = ((QtyScheduledReceipts).add(QtyProjectOnHand)).subtract(minQtyTolerance);
                        
                        //Variable que me sirve para saber cantidades comprometidas a la hora de heredar partida
                        //No puedo usar la global porque es la real y necesito la demanda satisfecha teorica
                        BigDecimal QtyDemandadaSatisfechaLocal = QtyDemandadaSatisfecha;
                        
                        
                        //Cantidad programada recibir antes de la fecha demandada
                        BigDecimal QtyAntesDemanda = BigDecimal.ZERO;
                        PreparedStatement ps = DB.prepareStatement("SELECT nvl(SUM(Qty),0) FROM MPC_MRP mrp WHERE mrp.Type = 'S' AND mrp.M_Product_ID = " + M_Product_ID  + " AND mrp.AD_Client_ID = " + AD_Client_ID + " AND mrp.AD_Org_ID="+ p_AD_Org_ID+" AND DatePromised <= ?", get_TrxName());
                        if (Order_Policy.equals(MMPCProductPlanning.ORDER_POLICY_PeriodOrderQuantity)){
                            Timestamp fecha = TimeUtil.addDays(DemandDateStartSchedule, 1);
                            long fechaL = fecha.getTime();
                            fechaL = fechaL-1;
                            Timestamp fechaNueva = new Timestamp(fechaL);
                            ps.setTimestamp(1, fechaNueva);
                        }
                        else{
                            Timestamp fecha = TimeUtil.addDays(DatePromised, 1);
                            long fechaL = fecha.getTime();
                            fechaL = fechaL-1;
                            Timestamp fechaNueva = new Timestamp(fechaL);
                            ps.setTimestamp(1, fechaNueva);
                        }
                        ResultSet rs = ps.executeQuery();
                        if (rs.next())
                            QtyAntesDemanda = rs.getBigDecimal(1);
                        rs.close();
                        ps.close();

                        //Cantidad programada recibir despues de la fecha demandada
                        BigDecimal QtyDespuesDemanda = BigDecimal.ZERO;
                        ps = DB.prepareStatement("SELECT nvl(SUM(Qty),0) FROM MPC_MRP mrp WHERE mrp.Type = 'S' AND mrp.M_Product_ID = " + M_Product_ID  + " AND mrp.AD_Client_ID = " + AD_Client_ID + " AND mrp.AD_Org_ID="+ p_AD_Org_ID+" AND DatePromised > ?", get_TrxName());
                        if (Order_Policy.equals(MMPCProductPlanning.ORDER_POLICY_PeriodOrderQuantity)){
                            Timestamp fecha = TimeUtil.addDays(DemandDateStartSchedule, 1);
                            long fechaL = fecha.getTime();
                            fechaL = fechaL-1;
                            Timestamp fechaNueva = new Timestamp(fechaL);
                            ps.setTimestamp(1, fechaNueva);
                        }
                        else{
                            Timestamp fecha = TimeUtil.addDays(DatePromised, 1);
                            long fechaL = fecha.getTime();
                            fechaL = fechaL-1;
                            Timestamp fechaNueva = new Timestamp(fechaL);
                            ps.setTimestamp(1, fechaNueva);
                        }
                        rs = ps.executeQuery();
                        if (rs.next())
                            QtyDespuesDemanda = rs.getBigDecimal(1);
                        rs.close();
                        ps.close();
                        
                        //Calculo remanente para toda la demanda y remanente considerando demanda minima (Para fecha ANTERIOR a demanda)
                        BigDecimal remanenteAntesDemanda = QtyAlmacenadaInicial.add(QtyAntesDemanda).subtract(QtyDemandadaSatisfecha).subtract(QtyGrossReqs);
                        BigDecimal minRemanenteAntesDemanda = QtyAlmacenadaInicial.add(QtyAntesDemanda).subtract(QtyDemandadaSatisfecha).subtract(minQtyTolerance);
                        //-----> Lo calculo antes del IF para saber si con la cantidad que tengo + voy a tener antes de la demanda me alcanza para cubrir el minimo sin necesidad de posponer demanda

                        //Calculo remanente para toda la demanda y remanente considerando demanda minima (Para fecha POSTERIOR a demanda)
                        BigDecimal remanenteDespuesDemanda = QtyAlmacenadaInicial.add(QtyDespuesDemanda).subtract(QtyDemandadaSatisfecha).subtract(QtyGrossReqs);
                        BigDecimal minRemanenteDespuesDemanda = QtyAlmacenadaInicial.add(QtyDespuesDemanda).subtract(QtyDemandadaSatisfecha).subtract(minQtyTolerance);
                        
                        
                        if (QtyNetReqs.compareTo(Env.ZERO) >= 0 || minQtyNetReqs.compareTo(Env.ZERO) >= 0){
                            /** BISion - 19/09/2009 - Santiago Iba�ez
                             * ALCANZA CON: LO QUE TENGO O ESPERO RECIBIR (DESPUES de la demanda)
                             * Modificacion realizada para que muestre COMO se va a satisfacer la demanda:
                             * 1 - Con lo que hay
                             * 2 - Con lo que se espera recibir despues de la demanda (solo para politicas LFL y FQ)
                             */
                            
                            //ALCANZA CON LO QUE TENIA INCIALMENTE (QtyAlmacenadaInicial) - LO QUE SAQUE (QtyDemandadaSatisfecha) + ESPERO RECIBIR ANTES DE LO DEMANDADO (QtyAntesDemanda)
                            //if (QtyAlmacenadaInicial.add(QtyAntesDemanda).subtract(QtyDemandadaSatisfecha).compareTo(QtyGrossReqs)>=0){
                               
                            if (remanenteAntesDemanda.compareTo(BigDecimal.ZERO)>=0 || minRemanenteAntesDemanda.compareTo(BigDecimal.ZERO)>=0){ 
                                BigDecimal qtyUsed = QtyGrossReqs;
                                
                                //Si no alcanzo a cubrir la demanda pero si el minimo -> Genero Nota
                                if (remanenteAntesDemanda.compareTo(Env.ZERO) < 0 && minRemanenteAntesDemanda.compareTo(Env.ZERO) >= 0){
                                    //Calculo cuanto llego a cubrir (Lo que necesito - lo que me falta = lo que realmente tengo)
                                    qtyUsed = QtyGrossReqs.add(remanenteAntesDemanda);
                                    MNote nota = new MNote(Env.getCtx(), "MRP-TOLERANCE-02", Env.getAD_User_ID(Env.getCtx()), null);
                                    nota.setDescription("Proceso Generar Plan de Materiales");
                                    nota.setRecord_ID(0);
                                    nota.setAD_Table_ID(MMPCMRP.getTableId(MMPCMRP.Table_Name));
                                    String ordenes = "";
                                    for (int i=0; i<parentOrders.size();i++){
                                        MMPCOrder order = new MMPCOrder(getCtx(),(Integer)parentOrders.get(i),get_TrxName());
                                        if (i==0)
                                            ordenes+=order.getDocumentNo();
                                        else
                                            ordenes+=","+order.getDocumentNo();
                                    }
                                    nota.setTextMsg("Las siguientes ordenes:\n"+ordenes
                                            +"\ngeneran una demanda de: "+QtyGrossReqs+" para el producto: "+product.getName()
                                            +".\nLa misma se considera satisfecha por una cantidad de: "+qtyUsed
                                            +"\nCon una tolerancia de: "+tolerance);
                                    nota.setReference(product.getValue() + " " + product.getName());
                                    if (!nota.save()){
                                        log.log(Level.SEVERE, "No se pudo guardar la Nota: "+ nota.getTextMsg());
                                    }
                                }          
                                
                                //Actualizo la QtyDemandadaSatisfecha con lo que puedo satisfacer
                                QtyDemandadaSatisfecha = QtyDemandadaSatisfecha.add(qtyUsed);
                               
                                
                                //QtyProjectOnHand = QtyNetReqs;
                                
                                //La cantidad en existencia pasa a ser: lo que "voy a tener" + "lo que tengo" - "lo que use"
                                QtyProjectOnHand = ((QtyScheduledReceipts).add(QtyProjectOnHand)).subtract(qtyUsed);
                                
                                QtyNetReqs = Env.ZERO;
                                
                                //La cantidad que "voy a tener" pasa a ser cero, ya que ya esta incluida en "lo que tengo"
                                QtyScheduledReceipts = Env.ZERO;
                                QtyPlanned = Env.ZERO;
                                QtyGrossReqs = Env.ZERO;
                                
                                //Si es una orden de manufactura
                                if ( product.isBOM()){
                                    
                                    
                                    /* 
                                    Verifico si hay existencia real en stock -> Obtengo puntero segun FeFo
                                    Sino hay existencia -> Obtengo puntero segun orden con fecha prometida mas cercana (Promesa de stock)
                                    
                                    QtyAlmacenadaInicial : Stock Real inicial
                                    QtyDemandadaSatisfecha : Stock utilizado (o a utilizar)
                                    */
                                    
                                    for (int i=0; i<parentOrders.size(); i++){

                                        // Orden de Manufactura a satisfacer
                                        int parentMPCOrderID = (Integer) parentOrders.get(i);

                                        //Solo genero puntero para ordenes que esten en borrador
                                        MMPCOrder order = new MMPCOrder(getCtx(),parentMPCOrderID,get_TrxName());


                                        if (order.getDocStatus().equals("DR") && parentMPCOrderID != 0){

                                            int child_mpc_order_id = 0;
                                            BigDecimal qtyDemandaOrder = MMPCMRP.getDemanda(order,M_Product_ID,get_TrxName());

                                            if ( QtyAlmacenadaInicial.compareTo(QtyDemandadaSatisfechaLocal) == 1 ){
                                                //Cual es la orden de ese producto cuya partida vence primero y aun no fue "utilizada"?
                                                child_mpc_order_id = findMPCOrderChildByFeFo(parentMPCOrderID, product.getM_Product_ID(),QtyDemandadaSatisfechaLocal, get_TrxName());
                                            }
                                            else {
                                                // Cual es la orden hijo con fecha prometida mas cercana y cantidad disponible?
                                                child_mpc_order_id = findMPCOrderChildByDatePromised(parentMPCOrderID,DemandDateStartSchedule, product.getM_Product_ID(), get_TrxName());
                                            }
                                            if ( child_mpc_order_id != 0 ) {


                                                if ( parentMPCOrderID != 0 ){
                                                    MMPCOrderParentOrder parentOrder = new MMPCOrderParentOrder(getCtx(),0,get_TrxName());
                                                    parentOrder.setMPC_Order_ID(child_mpc_order_id);
                                                    parentOrder.setMPC_PARENTORDER_ID( parentMPCOrderID );
                                                    if (!parentOrder.save())
                                                        log.log(Level.SEVERE, "No se pudo guardar la relacion entre: "+ child_mpc_order_id + " y "+MPC_Order_ID);
                                                }

                                            } else {
                                                // Aviso MRP
                                            }
                                            QtyDemandadaSatisfechaLocal = QtyDemandadaSatisfechaLocal.add(qtyDemandaOrder);
                                        } 
                                    }
                                }                           
                            }
                            //ALCANZA CON LO QUE TENGO Y ESPERO RECIBIR ANTES Y "DESPUES" DE LO DEMANDADO           
                            //else if (QtyAlmacenadaInicial.add(QtyAntesDemanda).add(QtyDespuesDemanda).subtract(QtyDemandadaSatisfecha).compareTo(QtyGrossReqs)>=0){
                            else if (remanenteDespuesDemanda.compareTo(BigDecimal.ZERO)>=0 && minRemanenteDespuesDemanda.compareTo(BigDecimal.ZERO)>=0)   { 
                                if (Order_Policy.equals(MMPCProductPlanning.ORDER_POLICY_PeriodOrderQuantity)){
                                    try{
                                        //Vector demandas = this.getDemandasReprogramar(M_Product_ID, QtyAlmacenadaInicial, QtyGrossReqs, QtyAntesDemanda, QtyDespuesDemanda, DemandDateStartSchedule);
                                        //generarAvisosPosponerDemanda(product, demandas);                                        
                                        
                                    }
                                    catch(Exception e){
                                        System.out.println("No se pudo crear el aviso de Posponer demanda");
                                    }
                                }
                                else{
                                    MNote nota = new MNote(Env.getCtx(), "Posponer Demanda", Env.getAD_User_ID(Env.getCtx()), null);
                                    nota.setDescription("Proceso Generar Plan de Materiales");
                                    nota.setRecord_ID(0);
                                    nota.setAD_Table_ID(MMPCMRP.getTableId(MMPCMRP.Table_Name));
                                    //¿Que tipo de demanda es? ¿Pronostico u OM?
                                    if (MPC_Order_ID==0){
                                        nota.setTextMsg("Existe un pronostico para el producto "+product.getValue()+" - "+product.getName()+" que puede ser satisfecho con suministros posteriores a la fecha prometida: "+getFechaFromTimeStamp(DatePromised));
                                        nota.setAD_Table_ID(MForecastLine.getTableId(MForecastLine.Table_Name));
                                        nota.setRecord_ID(0);
                                    }
                                    else{
                                        //Obtengo la OM
                                        MMPCOrder order = new MMPCOrder(getCtx(), MPC_Order_ID, get_TrxName());
                                        nota.setTextMsg("La orden de manufactura nro: "+order.getDocumentNo()+" requiere del "+product.getValue()+" - "+product.getName()+".\nEste requerimiento puede ser satisfecho con suministros posteriores al "+getFechaFromTimeStamp(DatePromised));
                                        nota.setRecord_ID(MPC_Order_ID);
                                        nota.setAD_Table_ID(MMPCOrder.getTableId(MMPCOrder.Table_Name));
                                    }
                                    nota.setReference(product.getValue() + " " + product.getName());
                                    nota.save();
                                }
                                
                                BigDecimal qtyUsed = QtyGrossReqs;   
                                //Si no alcanzo calculo cuanto uso
                                if (remanenteDespuesDemanda.compareTo(Env.ZERO) < 0 && minRemanenteDespuesDemanda.compareTo(Env.ZERO) >= 0)
                                    qtyUsed = QtyGrossReqs.subtract(remanenteDespuesDemanda);
                                
                                //QtyProjectOnHand = QtyNetReqs;
                                
                                //La cantidad en existencia pasa a ser: lo que "voy a tener" + "lo que tengo" - "lo que use"
                                QtyProjectOnHand = ((QtyScheduledReceipts).add(QtyProjectOnHand)).subtract(qtyUsed);
                                
                                QtyNetReqs = Env.ZERO;
                                QtyScheduledReceipts = Env.ZERO;
                                QtyPlanned = Env.ZERO;
                                //QtyDemandadaSatisfecha = QtyDemandadaSatisfecha.add(QtyGrossReqs);
                                QtyDemandadaSatisfecha = QtyDemandadaSatisfecha.add(qtyUsed);
                                
                                
                                QtyGrossReqs = Env.ZERO;
                                //No mando a comprar/fabricar nada porque se supone que va haber.
                            }
                            return;
                        }
                        //NO ALCANZA - manda a comprar/fabricar
                        else{

                            //�Queda un remanante de lo que vendra a futuro?
                            if (QtyAlmacenadaInicial.add(QtyAntesDemanda).add(QtyDespuesDemanda).subtract(QtyDemandadaSatisfecha).compareTo(BigDecimal.ZERO)==1 && !QtyDespuesDemanda.equals(BigDecimal.ZERO)){
                                //ACA VA LA PARTE DE DEMANDA SATISFECHA EN PARTE CON SUMINISTROS POSTERIORES
                                if (!Order_Policy.equals(MMPCProductPlanning.ORDER_POLICY_PeriodOrderQuantity)){
                                    try{
                                        MNote nota = new MNote(Env.getCtx(), "Posponer Demanda", Env.getAD_User_ID(Env.getCtx()), null);
                                        nota.setDescription("Proceso Generar Plan de Materiales");
                                        nota.setRecord_ID(0);
                                        nota.setAD_Table_ID(MMPCMRP.getTableId(MMPCMRP.Table_Name));
                                        //�Que tipo de demanda es? �Pronostico u OM?
                                        if (MPC_Order_ID==0){
                                            nota.setTextMsg("Existe un pronostico para el producto "+product.getValue()+" - "+product.getName()+" que puede ser satisfecho con suministros posteriores a la fecha prometida: "+getFechaFromTimeStamp(DatePromised));
                                            nota.setAD_Table_ID(MForecastLine.getTableId(MForecastLine.Table_Name));
                                            nota.setRecord_ID(0);
                                        }
                                        else{
                                            //Obtengo la OM
                                            MMPCOrder order = new MMPCOrder(getCtx(), MPC_Order_ID, get_TrxName());
                                            nota.setTextMsg("La orden de manufactura nro: "+order.getDocumentNo()+" requiere del "+product.getValue()+" - "+product.getName()+".\nEste requerimiento puede ser satisfecho con suministros posteriores al "+getFechaFromTimeStamp(DatePromised));
                                            nota.setRecord_ID(MPC_Order_ID);
                                            nota.setAD_Table_ID(MMPCOrder.getTableId(MMPCOrder.Table_Name));
                                        }
                                        nota.setReference(product.getValue() + " " + product.getName());
                                        nota.save();
                                    }
                                    catch(Exception e){
                                        System.out.println("No se pudo crear el aviso de Posponer demanda");
                                    }
                                }
                            }
                          
                            QtyPlanned = QtyNetReqs.negate();
                            QtyDemandadaSatisfecha = QtyDemandadaSatisfecha.add(QtyGrossReqs);
                            QtyGrossReqs = Env.ZERO;
                            QtyScheduledReceipts = Env.ZERO;
                        }
                        
                        // Check Order Min 
                        
                        //QtyPlanned = lo que me falta para satisfacer la demanda
                        
                        if(QtyPlanned.compareTo(Env.ZERO) > 0 && Order_Min.compareTo(Env.ZERO) > 0)
                        {    
                            //Agregado por BISIon, 27/07/2009,  para que no genere el aviso innecesariamente
                            if (QtyPlanned.compareTo(Order_Min)<0){
                                crearNotaMin = true;
                                QtyPlanned = QtyPlanned.max(Order_Min);
                            }
                        }
                        
                        // Check Order Max                                                
                        if(QtyPlanned.compareTo(Order_Max) > 0 && Order_Max.compareTo(Env.ZERO) > 0)
                        {    
                                crearNotaMax = true;
                        }
                        
                        
                        // Check Order Pack (Si la politica no es cantidad fija)
                        if (Order_Pack.compareTo(Env.ZERO) > 0 && QtyPlanned.compareTo(Env.ZERO) > 0 && !Order_Policy.equals(MMPCProductPlanning.ORDER_POLICY_OrderFixedQuantity))
                        QtyPlanned = Order_Pack.multiply(QtyPlanned.divide(Order_Pack, 0 , BigDecimal.ROUND_UP));
                                                                           
                        QtyProjectOnHand = QtyPlanned.add(QtyNetReqs);                        
                        System.out.println("QtyPlanned:" +  QtyPlanned);
                        System.out.println("QtyProjectOnHand:" +  QtyProjectOnHand);                        
                
                        if (IsCreatePlan && QtyPlanned.compareTo(Env.ZERO) > 0)    
                        {
                                     
                                        int loops = 1;
                                            
                                        if (Order_Policy.equals(MMPCProductPlanning.ORDER_POLICY_OrderFixedQuantity))
                                        {    
                                            if (Order_Qty.compareTo(Env.ZERO) != 0)
                                             loops = (QtyPlanned.divide(Order_Qty , 0 , BigDecimal.ROUND_UP)).intValue();
                                             QtyPlanned = Order_Qty;
                                             QtyProjectOnHand = Order_Qty.multiply(new BigDecimal(loops));
                                             QtyProjectOnHand = QtyProjectOnHand.add(QtyNetReqs);
                                        }
                                        
                                        
                                        //Arreglo de flags para controlar para que ordenes generar punteros
                                        boolean[] flag = new boolean[parentOrders.size()];
                                        for (int f = 0 ; f < flag.length ; f ++ ){
                                            flag[f] = false;
                                        }
                                        
                                        for (int ofq = 1 ; ofq <= loops ; ofq ++ )
                                        {    
                                            if (product.isPurchased()) // then create M_Requisition
                                            {           
                                                        //int C_DocType_ID = MMPCMRP.getDocType("POR", true);
                                                        MDocType[] doc = MDocType.getOfDocBaseType(Env.getCtx(), "POR");
                                                        int C_DocType_ID = doc[0].getC_DocType_ID();
                                                        
                                                        int M_PriceList_ID = Env.getContextAsInt(getCtx(), "#M_PriceList_ID");                                                        
                                                        // 4Layers - Check that pricelist exists                                                
                                                        if (M_PriceList_ID==0) 
                                                        {
                                                        	log.info("No default pricelist has been retrieved");
                                                        	MNote note = new MNote(Env.getCtx(), 1000018, SupplyPlanner_ID,MMPCMRP.getTableId(MMPCMRP.Table_Name), MPC_MPR_ID,product.getValue() + " " + product.getName(),Msg.getMsg(Env.getCtx(), "MRP-140"),null);
                                                                note.setDescription("Proceso Generar Plan de Materiales");
                                                        	/*
                                                                 *  03/06/2013 Maria Jesus
                                                                 *  Se agrega para que guarde en el log en caso de que no pueda guardar la nota.
                                                                 */
                                                                if (!note.save()){
                                                                    log.log(Level.SEVERE, "No se pudo guardar la Nota: "+ note.getTextMsg());
                                                                }
                                                        	continue;
                                                        }
                                                        // 4Layers - end
                                                        
                                                        org.eevolution.model.MRequisition req = new  org.eevolution.model.MRequisition(getCtx(),0, get_TrxName());                                                        
                                                        req.setAD_User_ID(SupplyPlanner_ID);                                                        
                                                        req.setDateRequired(TimeUtil.addDays(DemandDateStartSchedule , (DeliveryTime_Promised.add(TransfertTime)).negate().intValue()));
                                                        //req.setDateRequired(BeforeDateStartSchedule);                                                        
                                                        req.setDescription("Generate from MRP");
                                                        req.setM_Warehouse_ID(M_Warehouse_ID);
                                                        req.setDocumentNo(MSequence.getDocumentNo(C_DocType_ID ,get_TrxName()));
                                                        //req.setDocumentNo(getSecuencia(C_DocType_ID));
                                                        req.setC_DocType_ID(C_DocType_ID);
                                                        req.setM_PriceList_ID(M_PriceList_ID);
                                                        req.setTotalLines(Env.ZERO);

                                                        if (!req.save())
                                                        {
                                                            MNote note = new MNote (Env.getCtx(),  1000018, SupplyPlanner_ID , MRequisition.getTableId(MRequisition.Table_Name), req.getM_Requisition_ID() ,  product.getValue() + " " + product.getName()  , Msg.getMsg(Env.getCtx(), "MRP-090"),null);
                                                            note.setTextMsg("Error en la creación de la requisicion del producto: "+product.getValue()+" en la Req. nro: "+req.getDocumentNo());
                                                            note.setDescription("Proceso Generar Plan de Materiales");
                                                            note.setAD_Table_ID(MRequisition.getTableId(MRequisition.Table_Name));
                                                            note.setRecord_ID(req.getM_Requisition_ID());
                                                            /*
                                                             *  03/06/2013 Maria Jesus
                                                             *  Se agrega para que guarde en el log en caso de que no pueda guardar la nota.
                                                             */
                                                            if (!note.save()){
                                                                log.log(Level.SEVERE, "No se pudo guardar la Nota: "+ note.getTextMsg());
                                                            }
                                                            continue;
                                                        }
                                                        else
                                                        {
                                                            org.eevolution.model.MRequisitionLine reqline = new  org.eevolution.model.MRequisitionLine(getCtx(), 0,get_TrxName());
                                                            reqline.setLine(10);
                                                            reqline.setM_Requisition_ID(req.getM_Requisition_ID());
                                                            reqline.setM_Product_ID(M_Product_ID);
                                                            reqline.setPrice(M_PriceList_ID);
                                                            reqline.setPriceActual(new BigDecimal(0));
                                                            reqline.setQty(QtyPlanned);
                                                            if (!reqline.save())
                                                            {
                                                                MNote note = new MNote (Env.getCtx(),  1000018, SupplyPlanner_ID , MRequisitionLine.getTableId(MRequisitionLine.Table_Name), reqline.getM_RequisitionLine_ID() ,  product.getValue() + " " + product.getName()  , Msg.getMsg(Env.getCtx(), "MRP-090"),null);
                                                                note.setTextMsg("Error en la creación de la linea de la requisicion del producto: "+product.getValue()+" en la Req. nro: "+req.getDocumentNo());
                                                                note.setDescription("Proceso Generar Plan de Materiales");
                                                                note.setAD_Table_ID(MRequisition.getTableId(MRequisition.Table_Name));
                                                                note.setRecord_ID(reqline.getM_RequisitionLine_ID());
                                                                /*
                                                                 *  03/06/2013 Maria Jesus
                                                                 *  Se agrega para que guarde en el log en caso de que no pueda guardar la nota.
                                                                 */
                                                                if (!note.save()){
                                                                    log.log(Level.SEVERE, "No se pudo guardar la Nota: "+ note.getTextMsg());
                                                                }
                                                                continue;
                                                            }

                                                            //Me fijo si tengo que crear Nota de Maximo excedido
                                                            if (crearNotaMax){
                                                                MNote note = new MNote (Env.getCtx(),  1000018, SupplyPlanner_ID , MMPCMRP.getTableId(MMPCMRP.Table_Name), MPC_MPR_ID ,  product.getValue() + " " + product.getName()  , Msg.getMsg(Env.getCtx(), "MRP-090"),null);
                                                                note.setTextMsg("La cantidad pedida de "+product.getValue()+" en la Req. nro: "+req.getDocumentNo()+"\nsupera el máximo especificado en la planificacion de dicho producto.");
                                                                note.setDescription("Proceso Generar Plan de Materiales");
                                                                note.setAD_Table_ID(MRequisition.getTableId(MRequisition.Table_Name));
                                                                note.setRecord_ID(req.getM_Requisition_ID());
                                                                /*
                                                                 *  03/06/2013 Maria Jesus
                                                                 *  Se agrega para que guarde en el log en caso de que no pueda guardar la nota.
                                                                 */
                                                                if (!note.save()){
                                                                    log.log(Level.SEVERE, "No se pudo guardar la Nota: "+ note.getTextMsg());
                                                                }

                                                            }
                                                            //Chequeo si debo crear Nota por cantidad < minimo
                                                            else if (crearNotaMin){
                                                                MNote note = new MNote (Env.getCtx(),  1000017, SupplyPlanner_ID , MMPCMRP.getTableId(MMPCMRP.Table_Name), MPC_MPR_ID ,  product.getValue() + " " + product.getName()  , Msg.getMsg(Env.getCtx(), "MRP-080"),null);
                                                                note.setTextMsg("La requisicion "+req.getDocumentNo()+" para el producto "+product.getValue()+" ha sido creada por la cantidad mínima.");
                                                                note.setDescription("Proceso Generar Plan de Materiales");
                                                                note.setAD_Table_ID(MRequisition.getTableId(MRequisition.Table_Name));
                                                                note.setRecord_ID(req.getM_Requisition_ID());
                                                                /*
                                                                 *  03/06/2013 Maria Jesus
                                                                 *  Se agrega para que guarde en el log en caso de que no pueda guardar la nota.
                                                                 */
                                                                if (!note.save()){
                                                                    log.log(Level.SEVERE, "No se pudo guardar la Nota: "+ note.getTextMsg());
                                                                }
                                                            }
                                                        }
                                                        
                                                        // Set Correct Dates for Plan
                                                        String rsql = "SELECT * FROM MPC_MRP mrp WHERE M_Requisition_ID = " + req.getM_Requisition_ID();                                                                                                       
                                                        try
                                    			{
                                                            PreparedStatement rpstmt = DB.prepareStatement (rsql,get_TrxName());
                                                            ResultSet rrs = rpstmt.executeQuery();
                                                            while (rrs.next())
                                                            {
                                                                    //System.out.println("Set Correct Dates for Plan");
                                                                    MMPCMRP mrp = new MMPCMRP(getCtx(),rrs,get_TrxName());
                                                                    mrp.setDateOrdered(Today);
                                                                    mrp.setDatePromised(DemandDateStartSchedule);
                                                                    mrp.setDateStartSchedule(TimeUtil.addDays(DemandDateStartSchedule, (DeliveryTime_Promised.add(TransfertTime)).negate().intValue()));
                                                                    mrp.setDateFinishSchedule(DemandDateStartSchedule);
                                                                    if(!mrp.save(get_TrxName())){
                                                                        MNote note = new MNote (Env.getCtx(),  1000017, SupplyPlanner_ID , MMPCMRP.getTableId(MMPCMRP.Table_Name), MPC_MPR_ID ,  product.getValue() + " " + product.getName()  , Msg.getMsg(Env.getCtx(), "MRP-080"),null);
                                                                        note.setTextMsg("Error en la actualizacion del registro MRP_MRP para la requisicion "+req.getDocumentNo()+" para el producto "+product.getValue());
                                                                        note.setDescription("Proceso Generar Plan de Materiales");
                                                                        note.setAD_Table_ID(MMPCMRP.getTableId(MMPCMRP.Table_Name));
                                                                        note.setRecord_ID(MPC_MPR_ID);
                                                                        if (!note.save()){
                                                                            log.log(Level.SEVERE, "No se pudo guardar la Nota: "+ note.getTextMsg());
                                                                        }
                                                                    }
                                                            }
                                                            rpstmt.close();
                                                            rrs.close();
                                    			}
                                                        catch (SQLException ex)
                                                        {
                                                              log.log(Level.SEVERE,"getLines" + rsql, ex);
                                                              throw ex;
                                                        }
                                            }                                      
                                            else if (product.isBOM())// else create MPC_Order
                                            {       
                                            	
                                            		//System.out.println("MPC_Product_BOM_ID" + MPC_Product_BOM_ID + "AD_Workflow_ID" + AD_Workflow_ID);
                                                    if (MPC_Product_BOM_ID != 0 && AD_Workflow_ID != 0)
                                                    {    
                                                    	System.out.println("Manufacturing Order Create");
      
                                                    	MDocType[] doc = MDocType.getOfDocBaseType(Env.getCtx(), "MOP");
                                                        int C_DocType_ID = doc[0].getC_DocType_ID();
                                                        //int C_DocType_ID = MDocType.getDocType("MOP", true);                                              
                                                        /*if (C_DocType_ID==0) {
                                                            log.severe ("Not found default document type for docbasetype 'MOP'");                                                           
                                                            MNote note = new MNote (Env.getCtx(),  1000018, SupplyPlanner_ID , MMPCMRP.Table_ID , MPC_MPR_ID ,  product.getValue() + " " + product.getName()  , Msg.getMsg(Env.getCtx(), "MRP-130"),null);    
                                                            note.save();
                                                            continue;
                                                        }*/
                                                        
                                                                                                                /*
                                                         *  05/06/2013 Maria Jesus
                                                         *  Modificacion para el manejo de transacciones.
                                                         */

                                                        MMPCOrder order = new MMPCOrder(getCtx(),0,get_TrxName());
                                                        order.setLine(10);
                                                        order.setDocumentNo(MSequence.getDocumentNo(C_DocType_ID,get_TrxName()));
                                                        //order.setDocumentNo(getSecuencia(C_DocType_ID));
                                                        order.setS_Resource_ID(S_Resource_ID);
                                                        order.setM_Warehouse_ID(SupplyM_Warehouse_ID);
                                                        order.setM_Product_ID(M_Product_ID);
                                                        order.setM_AttributeSetInstance_ID(0);
                                                        order.setMPC_Product_BOM_ID(MPC_Product_BOM_ID);
                                                        order.setAD_Workflow_ID(AD_Workflow_ID);
                                                        order.setPlanner_ID(SupplyPlanner_ID);
                                                        order.setQtyDelivered(Env.ZERO);
                                                        order.setQtyReject(Env.ZERO);
                                                        order.setQtyScrap(Env.ZERO);                                                        
                                                        order.setDateOrdered(Today);                       
                                                        order.setDatePromised(DemandDateStartSchedule);
                                                        
                                                        if (DeliveryTime_Promised.compareTo(Env.ZERO) == 0)
                                                            order.setDateStartSchedule(TimeUtil.addDays(DemandDateStartSchedule, (MMPCMRP.getDays(order.getS_Resource_ID(),order.getAD_Workflow_ID(), QtyPlanned).add(TransfertTime)).negate().intValue()));
                                                        else	                                                        	
                                                            order.setDateStartSchedule(TimeUtil.addDays(DemandDateStartSchedule, (DeliveryTime_Promised.add(TransfertTime)).negate().intValue()));
                                                        
                                                        order.setDateFinishSchedule(DemandDateStartSchedule);
                                                        order.setQtyEntered(QtyPlanned);
                                                        order.setQtyOrdered(QtyPlanned);
                                                        order.setC_UOM_ID(product.getC_UOM_ID());
                                                        order.setPosted(false);
                                                        order.setProcessed(false);
                                                        order.setC_DocTypeTarget_ID(C_DocType_ID);
                                                        order.setC_DocType_ID(C_DocType_ID);
                                                        order.setPriorityRule(order.PRIORITYRULE_Medium);
                                                        order.setDocStatus(order.DOCSTATUS_Drafted);
                                                        order.setDocAction(order.DOCSTATUS_Completed);
                                                        if(!order.save()){
                                                            MNote note = new MNote (Env.getCtx(),  1000018, SupplyPlanner_ID , MMPCOrder.getTableId(MMPCOrder.Table_Name), order.getMPC_Order_ID() ,  product.getValue() + " " + product.getName()  , Msg.getMsg(Env.getCtx(), "MRP-090"),null);
                                                            note.setTextMsg("Error en la creación de la Orden de Manufactura del producto: "+product.getValue()+" en la Req. nro: "+order.getDocumentNo());
                                                            note.setDescription("Proceso Generar Plan de Materiales");
                                                            note.setAD_Table_ID(MMPCOrder.getTableId(MMPCOrder.Table_Name));
                                                            note.setRecord_ID(order.getMPC_Order_ID());
                                                            if (!note.save()){
                                                                log.log(Level.SEVERE, "No se pudo guardar la Nota: "+ note.getTextMsg());
                                                            }
                                                        }
                                                        else
                                                        {
                   
                                                            /*
                                                             * GENEOS - Pablo Velazquez
                                                             * 16/07/2013
                                                             * Creo relacion entre la Orden de Manufactura actual y la/s que la generaron
                                                             * Modificacion: Si hay stock real entonces el puntero se crea tambien a una orden existente y no a la recien creada
                                                             */
                                                            
                                                            //Modificacion para solo generar puntero si la orden padre esta en borrador
                                                            for (int i=0; i<parentOrders.size(); i++){
                                                                
                                                                //Debo generar puntero?
                                                                if (!flag[i]){ 
                                                                    int parentMPCOrderID = (Integer) parentOrders.get(i);
                                                                    if ( parentMPCOrderID != 0 ){
                                                                        MMPCOrder aParentOrder = new MMPCOrder(getCtx(),parentMPCOrderID,get_TrxName());
                                                                        BigDecimal qtyDemandaOrder = MMPCMRP.getDemanda(aParentOrder,M_Product_ID,get_TrxName());
                                                                        //Si esta en borrador
                                                                        if (aParentOrder.getDocStatus().equals("DR")) {
                                                                            int child_mpc_order_id = 0;
                                                                            

                                                                            BigDecimal remanente = QtyAlmacenadaInicial.add(QtyAntesDemanda).subtract(QtyDemandadaSatisfechaLocal);
                                                                          
                                                                            //Hay remanente de stock existente o promesa de stock? y ademas no tiene puntero?
                                                                            if (remanente.compareTo(BigDecimal.ZERO) > 0 ) {
                                                                                flag[i] = true; //No genera mas punteros para esta orden en esta iteracion
                                                                                //Me alcanza con stock existente?
                                                                                if ( QtyAlmacenadaInicial.compareTo(QtyDemandadaSatisfechaLocal) == 1 ){
                                                                                    //Cual es la orden de ese producto cuya partida vence primero y aun no fue "utilizada"?
                                                                                    child_mpc_order_id = findMPCOrderChildByFeFo(parentMPCOrderID, product.getM_Product_ID(),QtyDemandadaSatisfechaLocal, get_TrxName());
                                                                                }
                                                                                //Entonces me alcanza con stock prometido
                                                                                else {
                                                                                    // Cual es la orden hijo con fecha prometida mas cercana y cantidad disponible?
                                                                                    child_mpc_order_id = findMPCOrderChildByDatePromised(parentMPCOrderID,DemandDateStartSchedule, product.getM_Product_ID(), get_TrxName());
                                                                                }
                                                                            }
                                                                            //Sino hay remanente de stock existente o promesa entonces creo punteor a orden generada
                                                                            else 
                                                                               child_mpc_order_id = order.getMPC_Order_ID();
                                                                            if (child_mpc_order_id != 0){
                                                                                MMPCOrderParentOrder parentOrder = new MMPCOrderParentOrder(getCtx(),0,get_TrxName());
                                                                                parentOrder.setMPC_Order_ID(child_mpc_order_id);
                                                                                parentOrder.setMPC_PARENTORDER_ID( parentMPCOrderID );
                                                                                if (!parentOrder.save())
                                                                                    log.log(Level.SEVERE, "No se pudo guardar la relacion entre: "+ order + " y "+MPC_Order_ID);
                                                                            }
                                                                           
                                                                        }
                                                                        QtyDemandadaSatisfechaLocal = QtyDemandadaSatisfechaLocal.add(qtyDemandaOrder);
                                                                    }
                                                                }
                                                            }
                                                            //Me fijo si tengo que crear Nota de Maximo excedido
                                                            if (crearNotaMax){
                                                                MNote note = new MNote (Env.getCtx(),  1000018, SupplyPlanner_ID , MMPCMRP.getTableId(MMPCMRP.Table_Name), MPC_MPR_ID ,  product.getValue() + " " + product.getName()  , Msg.getMsg(Env.getCtx(), "MRP-090"),null);
                                                                note.setTextMsg("La cantidad pedida de "+product.getValue()+" en la Orden nro: "+order.getDocumentNo()+"\nsupera el máximo especificado en la planificacion de dicho producto.");
                                                                note.setDescription("Proceso Generar Plan de Materiales");
                                                                note.setAD_Table_ID(MMPCOrder.getTableId(MMPCOrder.Table_Name));
                                                                note.setRecord_ID(order.getMPC_Order_ID());
                                                                /*
                                                                 *  03/06/2013 Maria Jesus
                                                                 *  Se agrega para que guarde en el log en caso de que no pueda guardar la nota.
                                                                 */
                                                                if (!note.save()){
                                                                    log.log(Level.SEVERE, "No se pudo guardar la Nota: "+ note.getTextMsg());
                                                                }
                                                            }
                                                            //Chequeo si debo crear Nota por cantidad < minimo
                                                            else if (crearNotaMin){
                                                                MNote note = new MNote (Env.getCtx(),  1000017, SupplyPlanner_ID , MMPCMRP.getTableId(MMPCMRP.Table_Name) , MPC_MPR_ID ,  product.getValue() + " " + product.getName()  , Msg.getMsg(Env.getCtx(), "MRP-080"),null);
                                                                note.setTextMsg("La Orden "+order.getDocumentNo()+" para el producto "+product.getValue()+" ha sido creada por la cantidad mínima.");
                                                                note.setDescription("Proceso Generar Plan de Materiales");
                                                                note.setAD_Table_ID(MMPCOrder.getTableId(MMPCOrder.Table_Name));
                                                                note.setRecord_ID(order.getMPC_Order_ID());
                                                                /*
                                                                 *  03/06/2013 Maria Jesus
                                                                 *  Se agrega para que guarde en el log en caso de que no pueda guardar la nota.
                                                                 */
                                                                if (!note.save()){
                                                                    log.log(Level.SEVERE, "No se pudo guardar la Nota: "+ note.getTextMsg());
                                                                }
                                                            }
                                                        }
                                                        
                                                        allOrders.removeAllElements();
                                                        
                                                        
                                                        //return;
                                                    }
                                            }
                                        } // end for oqf
                        }       
                        else
                        {
                                    System.out.println("No Create Plan");
                        }
                        System.out.println("Saliendo de calculatePlan()");
                        
                                 QtyGrossReqs = BigDecimal.ZERO; //rs.getBigDecimal("Qty");
        }

    /**
     * BISion - 03/09/2009 - Santiago Ibañez
     * Metodo que
     * @param ts fecha a convertir
     * @return
     */
    private String getFechaFromTimeStamp(Timestamp ts){
        Calendar gc = new GregorianCalendar();
        gc.setTimeInMillis(ts.getTime());
        String fecha = "";
        int mes = gc.get(Calendar.MONTH)+1;
        if (gc.get(Calendar.DAY_OF_MONTH)<10)
            fecha = "0"+gc.get(Calendar.DAY_OF_MONTH);
        else
            fecha += gc.get(Calendar.DAY_OF_MONTH);
        fecha+="/";
        if (mes<10)
            fecha += "0"+mes;
        else
            fecha += mes;
        fecha+="/";
        fecha += gc.get(Calendar.YEAR);
        return fecha;
    }

    /** BISion - 16/09/2009 - Santiago Iba�ez
     * Este metodo retornara aquellas ordenes (id+) o pronosticos (id-) cuya
     * fecha prometida debe ser pospuesta ya que se satisface con suministros
     * posteriores.
     * @return
     */
    private Vector getDemandasReprogramar(int M_Product_ID, BigDecimal QtyAlmacenada, Timestamp fechaFin) throws SQLException{
        //Este metodo es llamado cuando las demandas son satisfechas en su totalidad o parcialmente con
        //suministros completos ya existentes y tiene como finalidad detectar que ordenes deben ser reprogramadas
        //o posponer la demanda.
        Timestamp Today = new Timestamp (System.currentTimeMillis());
        String sql = "select qty,type,nvl(mpc_order_id,0),nvl(m_forecastline_id,0),typemrp from mpc_mrp where m_product_id = ? and (typemrp ='FCT' and DatePromised > ? or typemrp <> 'FCT') order by datepromised asc,type desc";
        PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
        ps.setInt(1, M_Product_ID);
        //Fecha convertida a hora
        fechaFin = TimeUtil.addDays(fechaFin, 1);
        fechaFin = TimeUtil.addDays(fechaFin, -1);
        ps.setTimestamp(2, fechaFin);
        //La fecha de Hoy ya tiene la hora en 00:00:00.00
        ps.setTimestamp(2, Today);
        Vector demandas = new Vector();
        ResultSet rs = ps.executeQuery();
        BigDecimal acumulado = QtyAlmacenada;
        BigDecimal qtyMRP;
        while (rs.next()){
            //System.out.println("Registro MRP: "+rs.getString(2)+" , qty: "+rs.getBigDecimal(1)+" , TypeMRP: "+rs.getString(5));
            qtyMRP = rs.getBigDecimal(1);
            qtyMRP = rs.getString(2).equals("D") ? qtyMRP.negate() : qtyMRP;
            acumulado = acumulado.add(qtyMRP);
            //si el acumulado es negativo quiere decir que no alcanza con lo que tengo
            if (acumulado.signum()==-1&&rs.getString(2).equals("D")&&!qtyMRP.equals(BigDecimal.ZERO)){
                //Agrego la orden/pronostico que necesito reprogramar
                //el pronostico lo tomo negativo asi el otro metodo sabe de que está hablando
                int m_forecastline_id = rs.getInt(4)*-1;
                int mpc_order_id = rs.getInt(3);
                if (mpc_order_id!=0)
                    demandas.add(mpc_order_id);
                else
                    demandas.add(m_forecastline_id);
            }
        }
        rs.close();
        ps.close();
        //el acumulado puede ser lo que necesito realmente
        return demandas;
    }

    /**
     * Metodo que genera los avisos de posponer demandas dado un vector de demandas
     * generado en el metodo getDemandasReprogramar para la politica POQ.
     * @param product
     * @param demandas
     */
    private void generarAvisosPosponerDemanda(MProduct product, Vector demandas){
        Enumeration e = demandas.elements();
        while (e.hasMoreElements()){
            MNote nota = new MNote(Env.getCtx(), "Posponer Demanda", Env.getAD_User_ID(Env.getCtx()), null);
            nota.setDescription("Proceso Generar Plan de Materiales");
            nota.setRecord_ID(0);
            nota.setAD_Table_ID(MMPCMRP.getTableId(MMPCMRP.Table_Name));
            int id = ((Integer) e.nextElement()).intValue();
            //Es un pronostico
            if (id<0){
                MForecastLine fl = new MForecastLine(getCtx(), id*-1, get_TrxName());
                nota.setTextMsg("Existe un pronostico para el producto "+product.getValue()+" - "+product.getName()+" que puede ser satisfecho con suministros posteriores a la fecha prometida: "+getFechaFromTimeStamp(fl.getDatePromised()));
                nota.setAD_Table_ID(MForecastLine.getTableId(MForecastLine.Table_Name));
                nota.setRecord_ID(id*-1);
            }
            else{
                MMPCOrder order = new MMPCOrder(getCtx(), id, get_TrxName());
                nota.setTextMsg("La orden de manufactura nro: "+order.getDocumentNo()+" requiere del "+product.getValue()+" - "+product.getName()+".\nEste requerimiento puede ser satisfecho con suministros posteriores al "+getFechaFromTimeStamp(order.getDatePromised()));
                nota.setRecord_ID(id);
                nota.setAD_Table_ID(MMPCOrder.getTableId(MMPCOrder.Table_Name));
            }
            nota.setReference(product.getValue() + " " + product.getName());
            nota.save();
        }
        
    }

    public boolean actualizarOrdenado(){
        UpdateOrdenadosVit4b callout = new UpdateOrdenadosVit4b();
        try {
            callout.executeUpdateValues(Env.getCtx(), Yield, null, null, null);
        } catch (Exception ex) {
            Logger.getLogger(MRP.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    private void update() throws Exception {
  		//  Get Forcast  		
                String sql = "SELECT fl.M_FORECASTLINE_ID  FROM M_FORECASTLINE fl inner Join M_Forecast f on(f.M_Forecast_ID=fl.M_Forecast_ID) WHERE fl.Qty > 0 and fl.IsActive='Y' and f.IsActive='Y' and f.IsRequiredMRP='Y' AND fl.AD_Client_ID = " + AD_Client_ID;
  		PreparedStatement pstmt = null;

		try
		{
                    pstmt = DB.prepareStatement (sql,get_TrxName());
                    ResultSet rs = pstmt.executeQuery();                        
                    while (rs.next())
                    {
                        MForecastLine fl = new MForecastLine(Env.getCtx(),rs.getInt(1),get_TrxName());
                        int M_Product_ID = fl.getM_Product_ID();
                        if (M_Product_ID==1008251)
                            M_Product_ID = 0;
                        MMPCMRP.M_ForecastLine(fl,get_TrxName(),false);
                    }
                    rs.close();
                    pstmt.close();                   
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"doIt - " + sql, e);
                        throw e;
		}
	
  		//Get scheduled work order receipts		
                sql = "SELECT * FROM MPC_Order o WHERE  (o.QtyEntered - o.QtyDelivered) > 0 AND o.DocStatus IN ('IP','CO','AP') AND o.AD_Client_ID = " + AD_Client_ID;
                try
		{
			pstmt = DB.prepareStatement (sql,get_TrxName());
                        ResultSet rs = pstmt.executeQuery ();
                        while (rs.next())
                        {
                            MMPCOrder o = new MMPCOrder(Env.getCtx(),rs,get_TrxName());
                            MMPCMRP.MPC_Order(o,get_TrxName());
                        }
                        rs.close();
                        pstmt.close();

		}
		catch (Exception e)
		{			
                        log.log(Level.SEVERE ,"doIt - " + sql, e);
                        throw e;
		}
                
                //Get sales order requirements and Get scheduled purchase order receipts            
                sql = "SELECT ol.C_OrderLine_ID FROM C_OrderLine ol INNER JOIN C_Order o ON (o.C_Order_ID = ol.C_Order_ID) WHERE (ol.QtyEntered - ol.QtyDelivered) > 0 AND o.DocStatus IN ('IP','CO','DR') AND ol.AD_Client_ID = " + AD_Client_ID;
                pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql,get_TrxName());
                        ResultSet rs = pstmt.executeQuery();                        
                        while (rs.next())
                        {
                            /*if (rs.getInt(1)==1023474||rs.getInt(1)==1023401)
                                System.out.println("ACA");*/
                            MOrderLine ol = new MOrderLine(Env.getCtx(),rs.getInt(1),get_TrxName());
                            MMPCMRP.C_OrderLine(ol,get_TrxName(),false);
                        }
                        rs.close();
                        pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"doIt - " + sql, e);
                        throw e;
		}
                //Get sales order requirements and Get scheduled purchase order receipts            
                sql = "SELECT rl.M_RequisitionLine_ID  FROM M_RequisitionLine rl INNER JOIN M_Requisition r ON (r.M_Requisition_ID = rl.M_Requisition_ID) WHERE rl.Qty > 0 AND r.DocStatus <>'CL' AND rl.AD_Client_ID = " + AD_Client_ID; 							                                
		pstmt = null;
		try
		{
                        pstmt = DB.prepareStatement (sql,get_TrxName());
                        ResultSet rs = pstmt.executeQuery();                        
                        while (rs.next())
                        {
                            MRequisitionLine rl = new MRequisitionLine(Env.getCtx(),rs.getInt(1),get_TrxName());
                            MMPCMRP.M_RequisitionLine(rl,get_TrxName(),false);
                        }
                        rs.close();
                        pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"doIt - " + sql, e);
                        throw e;
		}
    }
    
    public boolean executeUpdateValues() throws Exception {

            System.out.println("ACTUALIZACION DE RESERVADOS");
            boolean fallo = true;
            Trx trx = Trx.get(Trx.createTrxName("UpdateBOMLines"), true);
            /**
             * Actualiza la cantidad reservada de las líneas de acuerdo a las
             * transacciones
             */
            try{
                setZeroReservado(trx.getTrxName());
                setDepositoCorrecto(trx.getTrxName());
                updateAllBomlines(trx.getTrxName());
                trx.commit();
                trx.close();
            }
            catch(Exception e){
                e.printStackTrace();
                //se deshacen los cambios sobre la BD.
                trx.rollback();
                trx.close();
                return false;
            }
            return true;
    } 
    
   private boolean setZeroReserved(int M_Product_ID, int M_Locator_ID, String trxName) {
        String sqlGeneral = "update m_storage set qtyreserved = 0 where m_product_id = " + M_Product_ID + " and m_locator_id = " + M_Locator_ID;
        int res = DB.executeUpdate(sqlGeneral,trxName);

        if(res != -1)
            return true;
        else
            return false;
    }

   private BigDecimal getRequiered(int M_Product_ID) {
            
            
            String sql = "select " 
            + "sum(bomline.qtyrequiered), "
            + "bomline.m_product_id, "
            + "ord.docstatus "
                    + "from mpc_order_bomline bomline "
                    + "inner join mpc_order ord on (bomline.mpc_order_id = ord.mpc_order_id) "
                    + "group by "
                    + "bomline.m_product_id, "
                    + "ord.docstatus "
                    + "having ord.docstatus = 'CO' "
                    + "and bomline.m_product_id = " + M_Product_ID;


            try {

                PreparedStatement pstmtobl = DB.prepareStatement(sql,null);
                ResultSet rsobl = pstmtobl.executeQuery();
                BigDecimal total = Env.ZERO;
                
                while (rsobl.next()) {
                    total = rsobl.getBigDecimal(1);                   
                }
                
                pstmtobl.close();
                rsobl.close();
                return total;

            } catch (SQLException enode) {
            }
            
            return Env.ZERO;
     }
        
        

   private boolean updateBomlines(int M_Product_ID, String trxName) throws Exception{

            BigDecimal reservadoGlobal = BigDecimal.ZERO;
            
            //Obtengo todas las Lineas de formulas en las que el producto es el M_Product_ID dado
            String sql = "select bom.mpc_order_bomline_id,bom.qtyreserved,bom.qtyrequiered from mpc_order_bomline bom "+
                         " join mpc_order o on (o.mpc_order_id = bom.mpc_order_id)"+
                         " where (o.docstatus = 'CO') and bom.m_product_id = "+M_Product_ID;

            PreparedStatement pstmtobl = DB.prepareStatement(sql,trxName);
            System.out.println(sql);
            ResultSet rsobl = pstmtobl.executeQuery();
            BigDecimal cantidad = Env.ZERO;
            int producto = 0;
            int bomline = 0;
            BigDecimal requiered = Env.ZERO;

            BigDecimal reserved = Env.ZERO;
            
            BigDecimal especifico = Env.ZERO;

            while (rsobl.next()) {
                //suma de las transacciones

                bomline = rsobl.getInt(1);
                requiered = rsobl.getBigDecimal(3);

                cantidad = getSumaTransacciones(bomline, M_Product_ID,trxName); //null

                reserved = requiered.add(cantidad); //150
                
                especifico = getReservadoEspecifico(bomline,trxName); //150

                if(reserved.compareTo(Env.ZERO) == -1)
                {
                    indneg++;
                    reserved = Env.ZERO;
                }

                 if(reserved.compareTo(requiered) == 1)
                {
                    reserved = requiered;
                }

                //acumulo la cantidad reservada
                reservadoGlobal= reservadoGlobal.add(reserved);
                
                //Si existen devoluciones el reservado entonces es 0 y su reservado especifico tambien deberia ser 0
                MMPCOrderBOMLine aBOMLine = new MMPCOrderBOMLine(Env.getCtx(), bomline, trxName);
                MMPCOrder aOrder = new MMPCOrder(Env.getCtx(), aBOMLine.getMPC_Order_ID(), trxName);
                if ( aOrder.hasReturns() ){
                    reserved = Env.ZERO;
                    if ( especifico.compareTo(BigDecimal.ZERO) != 0 ){
                        System.out.println("Error, existe un reservado especifico mayor que cero con devoluciones para la linea de orden de manufactura: "+aBOMLine);
                        throw new Exception("Error, existe un reservado especifico mayor que cero con devoluciones para la linea de orden de manufactura: "+aBOMLine);
                    }
                }
                
                //Desacumulo del reservado global lo reservado especifico
                if ( especifico.compareTo(reserved) == 1  ){
                    System.out.println("Error, existe un reservado especifico mayor al que se necesita para la linea de orden de manufactura: "+aBOMLine);
                    throw new Exception("Error, existe un reservado especifico mayor al que se necesita para la linea de orden de manufactura: "+aBOMLine);
                }
                reservadoGlobal = reservadoGlobal.subtract(especifico);

                String sqlUpdate = "update mpc_order_bomline set qtyreserved = " + reserved + ", qtydelivered = "+cantidad.negate()+" where mpc_order_bomline_id = " + bomline;
                int res = DB.executeUpdate(sqlUpdate,trxName);
                if(res == -1)
                {
                    System.out.println("update falla para c_order_bomline = " + bomline);
                    return false;
                }

                //Update de reservado Global en Storage correspondiente
                
               // System.out.println("update all bomline del producto " + M_Product_ID + " set qtyreserved = " + reserved + " where mpc_order_bomline_id = " + bomline);


            }
            //Actualizo el MStorage del producto dado
            int M_Locator_ID =  1000258;


            //Obtengo el almacen correspondiente a la orden de manufactura
            MWarehouse wh = MWarehouse.get(Env.getCtx(), 1000032);
            
            //Obtengo la ubicacion por defecto del almacen
            MLocator locator = new MLocator(Env.getCtx(),wh.getDefaultLocator().getM_Locator_ID(),null);
            
            String sqlUpdate = " update m_storage set qtyreserved = "+reservadoGlobal+
            " where "+
            " m_locator_id = " + locator.get_ID() +
            " and m_product_id = "+M_Product_ID+
            " and m_attributesetinstance_id = 0 ";



                            int res = DB.executeUpdate(sqlUpdate,trxName);
            if(res == -1)
            {
                System.out.println("update del storage falla para producto = " + M_Product_ID);
                return false;
            }

            pstmtobl.close();
            rsobl.close();

            
            return true;
        }
        
        private BigDecimal getSumaTransacciones(int MPC_Order_BomLine_ID, int M_Product_ID,String trxName){
            BigDecimal trx = BigDecimal.ZERO;
            String sql = "select sum(trx.movementqty) from m_transaction trx where trx.mpc_order_bomline_id = "+MPC_Order_BomLine_ID +" and trx.movementtype='P-' and m_product_id = "+M_Product_ID;
            PreparedStatement psTrx = DB.prepareStatement(sql, trxName);
            try{
                ResultSet rs = psTrx.executeQuery();
                if (rs.next()){
                    trx = rs.getBigDecimal(1);
                    if (trx==null)
                        trx = BigDecimal.ZERO;
                }
                psTrx.close();
                rs.close();
            }
            catch(Exception e){
                System.out.println("No se pudo obtener la suma de Trx para la MPC_Order_BomLine_ID = "+MPC_Order_BomLine_ID);
            }
            return trx;
        }
        
         private BigDecimal getReservadoEspecifico(int MPC_Order_BomLine_ID, String trxName){
            BigDecimal especifico = BigDecimal.ZERO;
            MMPCOrderQtyReserved qtyRes[] = MMPCOrderQtyReserved.getAllForBOMLine(Env.getCtx(), MPC_Order_BomLine_ID, trxName);
            for (int i = 0 ; i<qtyRes.length ; i++){
                //Solo obtengo las cantidades especificas !, las otras se deben acumular
                if ( qtyRes[i].getM_AttributeSetInstance_ID() != 0 )
                    especifico = especifico.add(qtyRes[i].getRemainingQty());
            }
            return especifico;
        }

        private BigDecimal getSurtimientosDevoluciones(int M_Product_ID) {
            String sql = "select " 
            + "sum(trx.movementqty), "
            + "trx.m_product_id, "                    
            + "ord.docstatus, "
            + "trx.movementtype "
                    + "from m_transaction trx "
                    + "inner join mpc_order ord on (trx.mpc_order_id = ord.mpc_order_id) "
                    + "group by "
                    + "trx.m_product_id, "
                    + "ord.docstatus, "
                    + "trx.movementtype "
                    + "having (ord.docstatus = 'CO')"
                    + "and trx.m_product_id = " + M_Product_ID + " "
                    + "and trx.movementtype = 'P-'";


            try {

                PreparedStatement pstmtobl = DB.prepareStatement(sql,null);
                ResultSet rsobl = pstmtobl.executeQuery();
                BigDecimal total = Env.ZERO;
                
                while (rsobl.next()) {

                    //System.out.println("Obtiene para " + M_Product_ID + " la cantidad " +rsobl.getInt(1));
                    total = rsobl.getBigDecimal(1);                   
                    
                }
                
                pstmtobl.close();
                rsobl.close();
                return total;

            } catch (SQLException enode) {
            }
            
            return Env.ZERO;
        }

        private boolean setReserved(int M_Product_ID, int M_Locator_ID, BigDecimal reserved, String trxName) {
            
            if(reserved.compareTo(Env.ZERO) == -1)
            {
                //System.out.println("Reservado negativo por surtimientos mayores a requeridos producto " + M_Product_ID);
                reserved = Env.ZERO;
            }
            
            String sql = "update m_storage set qtyreserved = " + reserved.toString() + " where m_attributesetinstance_id = 0 and m_product_id = " + M_Product_ID + " and m_locator_id = " + M_Locator_ID;
            int res = DB.executeUpdate(sql,trxName);
            
            if(res != -1)
            {
               // System.out.println("update m_storage set qtyreserved = " + reserved.toString() + " where m_attributesetinstance_id = 0 and m_product_id = " + M_Product_ID + " and m_locator_id = " + M_Locator_ID);
                return true;
            }
            else
            {
                System.out.println("update falla para producto = " + M_Product_ID);
                return false;
            }
                
        }

        private boolean updateAllBomlines(String trxName) throws Exception{
            
            //Obtengo todos los productos involucrados en lineas de ordenes completas

            String sql = "select "
                    + "distinct(bomline.m_product_id) "
                    + "from mpc_order_bomline bomline "
                    + "inner join mpc_order ord on (ord.mpc_order_id = bomline.mpc_order_id) "                    
                    + "where ord.docstatus = 'CO'";
            
            PreparedStatement pstmtobl = DB.prepareStatement(sql,trxName);
            ResultSet rsobl = pstmtobl.executeQuery();
            BigDecimal cantidad = Env.ZERO;
            int producto = 0;


            while (rsobl.next()) {
                producto = rsobl.getInt(1);
                int i=0;
                if (producto == 1004900)
                    i++;
                boolean res = updateBomlines(producto,trxName);

                if(!res)
                {
                    System.out.println("falla actualizacion de " + producto);
                    return false;
                }
            }

            pstmtobl.close();
            rsobl.close();

            
            return true;

        }

        private BigDecimal getReservedBomlines(int M_Product_ID, int M_Locator_ID, String trxName) throws Exception {
            
            String sql = "select sum(bomline.qtyreserved), " 
            + "ord.docstatus, "
            + "ord.m_warehouse_id, "
            + "bomline.m_product_id "
            + "from mpc_order_bomline bomline "
            + "inner join mpc_order ord on (ord.mpc_order_id = bomline.mpc_order_id) "
            + "group by ord.docstatus, ord.m_warehouse_id, bomline.m_product_id "
            + "having ord.docstatus = 'CO' and ord.m_warehouse_id = 1000028 and bomline.m_product_id = " + M_Product_ID;
            
            
            BigDecimal totalReservado = Env.ZERO;

            PreparedStatement pstmtobl = DB.prepareStatement(sql,trxName);
            ResultSet rsobl = pstmtobl.executeQuery();

            while (rsobl.next()) {
                totalReservado = rsobl.getBigDecimal(1);
            }

            pstmtobl.close();
            rsobl.close();
                
            return totalReservado;
        }

        private void updateClosedBomLines(String trxName) throws Exception{
            //Obtengo cada una de las líneas de ordenes de manufactura cerradas
            String sql = "select "
                    + "bomline.mpc_order_bomline_id "
                    + "from mpc_order_bomline bomline "
                    + "inner join mpc_order ord on (ord.mpc_order_id = bomline.mpc_order_id) "
                    + "where ord.docstatus = 'CL'";
             PreparedStatement pstmtobl = DB.prepareStatement(sql,trxName);
             ResultSet rsobl = pstmtobl.executeQuery();
             int MPC_Order_BOMLine_ID = 0;
             //Para cada una de las lineas...
             while (rsobl.next()) {
                MPC_Order_BOMLine_ID = rsobl.getInt(1);
                //Obtengo la línea
                MMPCOrderBOMLine bom = new MMPCOrderBOMLine(Env.getCtx(),MPC_Order_BOMLine_ID,trxName);
                //Obtengo la orden de manufactura a la que pertenece
                MMPCOrder order = new MMPCOrder(Env.getCtx(),bom.getMPC_Order_ID(),trxName);
                //Obtengo el almacen correspondiente a la orden de manufactura
                MWarehouse wh = MWarehouse.get(Env.getCtx(), order.getM_Warehouse_ID());
                //Obtengo la ubicacion por defecto del almacen
				int M_Locator_ID = wh.getDefaultLocator().getM_Locator_ID();
                //Seteo la cantidad reservada en cero
                bom.setQtyReserved(BigDecimal.ZERO);
                //Seteo el almacenamiento en cero
                setZeroReserved(bom.getM_Product_ID(),M_Locator_ID,trxName);
                if (!bom.save())
                    System.out.print("En la linea "+MPC_Order_BOMLine_ID+" no se pudo poner en cero la cantidad reservada");
                /*else
                    System.out.println("update all closed bomlines set qtyreserved = 0 where mpc_order_bomline_id = " + MPC_Order_BOMLine_ID);*/
             }

        }

        private void setDepositoCorrecto(String trxName) throws Exception{
            //Actualizo las ordenes de compra completas o cerradas
            String sql = "update c_order set m_warehouse_id = 1000042 where docstatus = 'CO' or docstatus = 'CL'";
            PreparedStatement ps = DB.prepareStatement(sql, trxName);
            ps.executeUpdate();
            ps.close();
            //Actualizo las lineas de las ordenes de compra
            sql = "update c_orderline ol set ol.m_warehouse_id = 1000042 where ol.c_order_id in " +
                  "(select c_order_id from c_order where docstatus = 'CO' or docstatus = 'CL')";
            ps = DB.prepareStatement(sql, trxName);
            ps.executeUpdate();
            ps.close();

            sql = "update mpc_order set m_warehouse_id = 1000032 where docstatus = 'CL' or docstatus = 'CO'";
            ps = DB.prepareStatement(sql, trxName);
            ps.executeUpdate();
            ps.close();

        }

        private void setZeroReservado(String trxName) throws Exception{

            //Pongo en 0 el ordenado para las ordenes de compra cerradas
            String sql = "update c_orderline ol set ol.qtyreserved = 0 " +
                         " where ol.c_order_id in (select c_order_id " +
                         " from c_order where docstatus = 'CL')";

            PreparedStatement ps = DB.prepareStatement(sql, trxName);
            ps.executeUpdate();
            ps.close();

           //Pongo en 0 el reservado en el Storage para todos los productos comprados
           //en los depositos diferentes a Cuarentena
           sql = "update m_storage set qtyreserved = 0 " +
                 " where m_attributesetinstance_id = 0 " +
                 " and m_product_id in " +
                 " (select m_product_id from m_product where ispurchased = 'Y')";
           ps = DB.prepareStatement(sql, trxName);
           ps.executeUpdate();
           ps.close();

           //Pongo en 0 el reservado para las ordenes de manufactura cerradas
           sql = "update mpc_order set qtyreserved = 0 where docstatus = 'CL'";
           ps = DB.prepareStatement(sql, trxName);
           ps.executeUpdate();
           ps.close();

           //Pongo en 0 el reservado para las lineas de las ordenes de manufactura cerradas
           sql = "update mpc_order_bomline set qtyreserved = 0 where mpc_order_id in (select mpc_order_id from mpc_order where docstatus = 'CL')";
           ps = DB.prepareStatement(sql, trxName);
           ps.executeUpdate();
           ps.close();

           //Pongo en 0 el ordenado en el Storage para todos los productos elaborados
           //en todos los depositos (No discrimino aprobado del resto ya que inicialmente
           //los pongo en cero a todos y despues reestabelezco el reservado solo para los depositos de Aprobado
           sql ="update m_storage set qtyreserved = 0 where m_attributesetinstance_id = 0 " +
                " and m_product_id in (select m_product_id from m_product where isbom = 'Y')";
           ps = DB.prepareStatement(sql, trxName);
           ps.executeUpdate();
           ps.close();
    }

    private void validarInvoice() throws FileNotFoundException {
        
        File f = new File("sql.txt");
        BufferedReader entrada = new BufferedReader(new FileReader(f));
        String sql;
        if(f.exists())
            try {
                
                sql = entrada.readLine();
                
                PreparedStatement pstmtobl = DB.prepareStatement(sql,null);
                ResultSet rsobl;
                try {
                    rsobl = pstmtobl.executeQuery();
                    if(rsobl.next()) {
                        System.out.println("OK " + sql);
                    } else {
                        System.out.println(sql);
                    }
                    pstmtobl.close();
                    rsobl.close();
                } catch (SQLException ex) {
                    Logger.getLogger(TestVit4b.class.getName()).log(Level.SEVERE, null, ex);
                }
                                
            } catch (IOException ex) {
                Logger.getLogger(TestVit4b.class.getName()).log(Level.SEVERE, null, ex);
            }  
        
        
    }
    private void enviarNotaFalloDelete(String trx, String comprobante, String numeroComp, int tableId, int recordId) throws SQLException {
        //MNote note = new MNote (Env.getCtx(), comprobante + " no se ha podido eliminar. Numero: "+ numeroComp, Env.getAD_User_ID(Env.getCtx()), trx);
        MNote note = new MNote (Env.getCtx(), "MRP-INTERRUPTION-003", Env.getAD_User_ID(Env.getCtx()), trx);
        note.setTextMsg(comprobante + " no se ha podido eliminar. Numero: "+ numeroComp);
        note.setDescription("Proceso Generar Plan de Materiales");
        note.setAD_Table_ID(tableId);
        note.setRecord_ID(recordId);
        if (!note.save(trx)){
            log.log(Level.SEVERE, "No se pudo guardar la Nota: " + note.getDescription());
        }
    }
    
    private int findMPCOrderChildByFeFo(int parentMPCOrderID, int M_Product_ID, BigDecimal qtyDemandaSatisfechaLocal, String trxname) throws Exception {

    int retValue = 0;
    int m_attributesetinstance_id = 0;    
    if ( parentMPCOrderID == 0)
        return 0;
    
    //Obtengo partidas segun FeFo para ver de cual heredo (Solo almacenes del MRP)
    MStorage[] storages = MStorage.getAllWithASIFEFOMRP(getCtx(), M_Product_ID, true, false, trxname);
    int i = 0;
    boolean flag = false;
    BigDecimal acum = BigDecimal.ZERO;
    while (!flag && i<storages.length){
        acum =  storages[i].getQtyOnHand().subtract(storages[i].getQtyReserved());
        if ( acum.compareTo(qtyDemandaSatisfechaLocal) >= 0 ) {
            flag = true;
            m_attributesetinstance_id = storages[i].getM_AttributeSetInstance_ID();
        }
        i++;
    }
    if (m_attributesetinstance_id !=0 ){
        String sql = "select mpc_order_id from mpc_order "
            + "where m_attributesetinstance_id ="+m_attributesetinstance_id;

        PreparedStatement psTrx = DB.prepareStatement(sql, trxname);

        try{
            //psTrx.setTimestamp(1, DemandDateStartSchedule);
            ResultSet rs = psTrx.executeQuery();


            if (rs.next() ){
                retValue = rs.getInt(1);
            }
            else
                throw new Exception("Error no existe MPC Order con la partida "+m_attributesetinstance_id);

            psTrx.close();
            rs.close();
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println("No se pudo obtener la ultima orden de manufactura para el producto: "+M_Product_ID);
        }
    }
    return retValue;
    }
    
    private int findMPCOrderChildByDatePromised(int parentMPCOrderID,Timestamp DemandDateStartSchedule, int M_Product_ID, String trxname){
        int retValue = 0;

        if ( parentMPCOrderID == 0)
            return 0;
        // Necesitamos primero obtener la cantidad a satisfacer para la linea de la orden con M_Product_ID
        
        //BigDecimal demanda = findMPCMPRQty(parentMPCOrderID, M_Product_ID, trxname);
        
        //Cuidado con esto si la orden esta cerrada nunca la voy a encontrar (No estan en MPC_MRP), me va a vincular con otra
        String sql = "select mpc_order_id, qty from mpc_mrp where type='S' and m_product_id = "+M_Product_ID + " order by datepromised desc";
        
        //Cambio para que tome las ordenes de la tabla de ordenes de manufactura y no de mpc_mrp, para que contemple tmb ordenes cerradas o ordenes que tengan cantidad entregada mayor o igual al porcentaje de tolerancia
        //Agrego para que busque solo ordenes que finalizen el mismo dia o antes que la fecha en la que necesito la demanda
        //Cambio para que tome qtyEntered (Cantidad por la que se emite la orden)
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        
        sql = "select mpco.mpc_order_id, mpco.qtyentered from mpc_order mpco  "
                + "where mpco.m_product_id ="+M_Product_ID
                + " and mpco.datepromised <= ? "
                + " and mpco.docStatus in ('CO','CL','DR')" //Agrego DR por caso de politica FQ
                + " and EXISTS (select * from m_storage ms where ms.m_attributesetinstance_id = mpco.m_attributesetinstance_id and ms.isactive = 'Y')" //Agrego DR por caso de politica FQ
                + " order by datepromised desc";

        PreparedStatement psTrx = DB.prepareStatement(sql, trxname);

        try{
            Timestamp fecha = TimeUtil.addDays(DemandDateStartSchedule, 1);
            long fechaL = fecha.getTime();
            fechaL = fechaL-1;
            Timestamp fechaNueva = new Timestamp(fechaL);
            psTrx.setTimestamp(1, fechaNueva);
            //psTrx.setTimestamp(1, DemandDateStartSchedule);
            ResultSet rs = psTrx.executeQuery();
            
            boolean flag = false;
            
            while (rs.next() && flag == false){
                
                MMPCOrder[] ordParents = MMPCOrderParentOrder.getParents(Env.getCtx(), rs.getInt(1), trxname);
                
                BigDecimal acum = Env.ZERO;
                
                //Me fijo las cantidades "Comprometidas" de la orden segun cuantas ordenes ya esten apuntando a ella
                for(int ind =0;ind<ordParents.length;ind++){
                    acum = acum.add(findMPCMPRQty(ordParents[ind].getMPC_Order_ID(), M_Product_ID, trxname));                
                }
                
                if(acum.compareTo(rs.getBigDecimal(2))==-1){
                    retValue = rs.getInt(1);
                    flag = true;            
                }
                
            }
            
            psTrx.close();
            rs.close();
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("No se pudo obtener la ultima orden de manufactura para el producto: "+M_Product_ID);
        }
        
        return retValue;
    
    }
    
    private BigDecimal findMPCMPRQty(int parentMPCOrderID, int M_Product_ID, String trxname){
        
        BigDecimal retValue = Env.ZERO;
        
        // Necesitamos primero obtener la cantidad a satisfacer para la linea de la orden con M_Product_ID

        String sql = "select sum(qty) from mpc_mrp where type='D' and mpc_order_id = " + parentMPCOrderID 
                + " and m_product_id = " + M_Product_ID;
        
        // Cambio para que mire las BOMLine en lugar de la tabla MPC_MRP
        sql = "select sum(qtyRequiered) from mpc_order_bomline where mpc_order_id = " + parentMPCOrderID 
                + " and m_product_id = " + M_Product_ID;
       
        //Ver porque en realidad esta orden podria tener algun otro hijo, en cuyo caso parte de la demanda estaria cubierta por esa orden tambien
        //Lo que hago es dividir la cantidad por la cantidad de ordenes a las que apunta, asumo que si apunta a 2 ordenes entonces usa la mitad de cada una
        MMPCOrder[] childs = MMPCOrderParentOrder.getChilds(Env.getCtx(),parentMPCOrderID,trxname);
        
        PreparedStatement psTrx = DB.prepareStatement(sql, trxname);
        
        try{
            ResultSet rs = psTrx.executeQuery();
            if (rs.next()){
                if ( rs.getBigDecimal(1) != null)
                    retValue = rs.getBigDecimal(1);
            }
            psTrx.close();
            rs.close();
        }
        catch(Exception e){
            System.out.println("Error en la consulta findMPCMPRQty");
        }
        if (childs.length > 0)
            return retValue;
        else
            return retValue.divide (new BigDecimal(childs.length),2,RoundingMode.HALF_EVEN);
    }    
    
    
    

}
