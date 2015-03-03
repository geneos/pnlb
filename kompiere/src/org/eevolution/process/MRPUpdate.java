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

import java.util.logging.*;

import java.sql.*;



import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.process.*;
import compiere.model.*;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.eevolution.model.MForecastLine;
import org.eevolution.model.MMPCMRP;
import org.eevolution.model.MMPCOrder;


/**
 *	MRPUpdate
 *	
 *  @author Victor P�rez, e-Evolution, S.C.
 *  @version $Id: CreateCost.java,v 1.1 2004/06/22 05:24:03 vpj-cd Exp $
 */
public class MRPUpdate extends SvrProcess
{
	/**					*/
        	/**					*/
        /*private int		  p_AD_Org_ID = 0;
        //private int               p_M_Warehouse_ID = 0;
        private int               p_S_Resource_ID = 0 ;
        //
        private String             p_Version = "1";*/
       private int AD_Client_ID = 0;
	
        
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		AD_Client_ID = getAD_Client_ID();
                           
                
              
	}	//	prepare


       
     protected String doIt() throws Exception                
     {
         deleteMRP();
         update();
         return "ok";
     } 
     
     public boolean deleteMRP() throws SQLException
     {
         
         
                System.out.println("begin MRPUpdate-deleteMRP()");

                Trx trx = Trx.get(Trx.createTrxName("deleteMRP"), true);
                
                
                //String sql = "DELETE FROM MPC_MRP mrp WHERE mrp.TypeMRP = 'MOP' AND EXISTS(SELECT MPC_Order_ID FROM MPC_Order o WHERE o.MPC_Order_ID = mrp.MPC_Order_ID AND o.DocStatus IN ('NA','CL')) AND mrp.AD_Client_ID = " + AD_Client_ID;

                /*  
                 *      VIT4B 20/11/2007
                 *      Agregado para que elimnine los registro de 'VO'
                 *
                 */
                
                /** BISion - 19/11/2008 - Santiago Iba�ez
                 * Se modific� este m�todo para que los cambios en la BD se 
                 * hagan efectivos al momento de comitear
                 */
                
                
                String sql = "DELETE FROM MPC_MRP mrp WHERE mrp.TypeMRP = 'MOP' AND EXISTS(SELECT MPC_Order_ID FROM MPC_Order o WHERE o.MPC_Order_ID = mrp.MPC_Order_ID AND o.DocStatus IN ('NA','CL','VO')) AND mrp.AD_Client_ID = " + AD_Client_ID;
                
                //String sql = "DELETE FROM MPC_MRP  WHERE TypeMRP = 'MOP'  AND AD_Client_ID=" + AD_Client_ID;
                
                DB.executeUpdate(sql,trx.getTrxName());
                
                sql = "DELETE FROM MPC_MRP mrp WHERE mrp.TypeMRP = 'FCT' AND mrp.AD_Client_ID = " + AD_Client_ID;
				
                DB.executeUpdate(sql,trx.getTrxName());
                
                //sql = "DELETE FROM MPC_MRP mrp WHERE mrp.TypeMRP = 'POR' AND EXISTS(SELECT M_Requisition_ID FROM M_Requisition r WHERE r.M_Requisition_ID = mrp.M_Requisition_ID AND (r.DocStatus='DR' AND r.DocStatus='CL')) AND mrp.AD_Client_ID = " + AD_Client_ID;		
                sql = "DELETE FROM MPC_MRP mrp WHERE mrp.TypeMRP = 'POR'  AND mrp.AD_Client_ID = " + AD_Client_ID;		
				
                DB.executeUpdate(sql,trx.getTrxName());
                
                sql = "DELETE FROM AD_Note n WHERE AD_Table_ID =  " + MMPCMRP.getTableId(MMPCMRP.Table_Name) +  " AND AD_Client_ID = " + AD_Client_ID;		
				
                DB.executeUpdate(sql,trx.getTrxName());

                //Borro aquellos registros MRP asociados a OM ya inexistentes o a Formulas ya inexistentes

                sql = "delete from mpc_mrp where mpc_mrp_id in (select mrp.mpc_mrp_id from mpc_mrp mrp left join mpc_order o on o.mpc_order_id = mrp.mpc_order_id where o.mpc_order_id is null and mrp.mpc_order_id is not null)";

                DB.executeUpdate(sql,trx.getTrxName());

                sql = "delete from mpc_mrp where mpc_mrp_id in (select mrp.mpc_mrp_id from mpc_mrp mrp left join mpc_order_bomline o on o.mpc_order_bomline_id = mrp.mpc_order_bomline_id where o.mpc_order_bomline_id is null and mrp.mpc_order_bomline_id is not null)";

                DB.executeUpdate(sql,trx.getTrxName());
                
                //Agregado para eliminar las notas con id mrpTolerance01_message_ID ya que se regeneran en el metodo update()
                int mrpTolerance01_message_ID = MMessage.getAD_Message_ID(Env.getCtx(), "MRP-TOLERANCE-01");
                sql = "DELETE FROM AD_Note WHERE description =  'Proceso Generar Plan de Materiales' AND AD_Client_ID = " + AD_Client_ID  + " AND AD_message_ID = "+mrpTolerance01_message_ID;
				
                DB.executeUpdate(sql, trx.getTrxName());
                
                /*  
                 *      VIT4B 25/09/2007
                 *      Agregado para que elimnine los registro de Ordenes de Compra POO
                 *
                 */
                
                //BISion - 15/09/2009 - Santiago Ibañez
                //Comentado para que borre los MRP de OC completas tambien para que los cree nuevamente
                //sql = "DELETE FROM MPC_MRP  WHERE TypeMRP = 'POO'  AND DocStatus = 'DR' AND AD_Client_ID=" + AD_Client_ID;
                sql = "DELETE FROM MPC_MRP  WHERE TypeMRP = 'POO' AND AD_Client_ID=" + AD_Client_ID;
                System.out.println("NUEVA VERSION");
                DB.executeUpdate(sql,trx.getTrxName());
                
                if (Trx.get(trx.getTrxName(),false)!=null)
                    Trx.get(trx.getTrxName(),false).commit();

                 
                sql = "SELECT o.MPC_Order_ID FROM MPC_Order o WHERE o.DocStatus = 'DR' AND o.AD_Client_ID = " + AD_Client_ID;
                
                PreparedStatement pstmt = null;
                ResultSet rs = null;
                
                try
		{
                        pstmt = DB.prepareStatement (sql,trx.getTrxName());
                        rs = pstmt.executeQuery();     
                        while (rs.next())
                        {
                            MMPCOrder order = new  MMPCOrder(getCtx(), rs.getInt(1), trx.getTrxName());
                            System.out.println("Borrando Orden de Manufactura nº: "+order.getDocumentNo()+"...");
                            if (!order.delete(true, trx.getTrxName())){
                                //Genero nota para avisar que no pudo ser eliminado la OM.
                                enviarNotaFalloDelete(trx.getTrxName(),"La Orden de Manufactura ", order.getDocumentNo(), MMPCOrder.getTableId(MMPCOrder.Table_Name), rs.getInt(1));
                            }
                        }
                        
                      
		}
                catch (Exception e)
		{
                	log.log(Level.SEVERE,"doIt - " + sql, e);
                        //Genero nota
                        enviarNotaFalloQuery(trx.getTrxName(),"Fallo al obtener Ordenes de Manufactura en borrador para ser eliminadas",e.toString(), MMPCMRP.getTableId(MMPCMRP.Table_Name),0); 
		}
                
                finally{
                    if (pstmt != null)
                        pstmt.close();
                    if (rs != null)
                        rs.close();
                }                
                
                sql = "SELECT r.M_Requisition_ID FROM M_Requisition r WHERE  r.DocStatus = 'DR' AND r.AD_Client_ID = " + AD_Client_ID;
		pstmt = null;
                rs = null;
                try
		{
                        pstmt = DB.prepareStatement (sql,trx.getTrxName());
                        rs = pstmt.executeQuery();
                        while (rs.next())
                        {
                            MRequisition r = new  MRequisition(getCtx(), rs.getInt(1),trx.getTrxName());
                            System.out.println("Borrando Requisicion nº: "+r.getDocumentNo());
                            MRequisitionLine[] rlines = r. getLines();
                            for ( int i= 0 ; i < rlines.length; i++ )
                            {
                                MRequisitionLine line =  rlines[i];
                                System.out.println("   Borrando  Linea: "+line.getLine()+"...");
                            	if (!line.delete(true,trx.getTrxName()))
                                    //Genero nota.
                                    enviarNotaFalloDelete(trx.getTrxName(),"Una de las lineas de la requisición ",r.getDocumentNo(), MRequisitionLine.getTableId(MRequisitionLine.Table_Name), line.getM_Requisition_ID());
                            }
							
                            if (!r.delete(true,trx.getTrxName()))
                                //genero nota
                                enviarNotaFalloDelete(trx.getTrxName(),"La requisición ",r.getDocumentNo(), MRequisition.getTableId(MRequisition.Table_Name),rs.getInt(1));

                        }

                        System.out.println("end deleteMRP()");
                        
		}
                catch (Exception e)
		{
                	log.log(Level.SEVERE,"doIt - " + sql, e);                           
                        //Genero nota
                        enviarNotaFalloQuery(trx.getTrxName(),"Fallo al obtener requisiciones en borrador para ser eliminadas",e.toString(), MMPCMRP.getTableId(MMPCMRP.Table_Name),0);                
		}                
                finally{
                    if (pstmt != null)
                        pstmt.close();
                    if (rs != null)
                        rs.close();
                    if (trx.commit()){
                        trx.close();
                        return true;
                    } else {
                        trx.rollback();
                        trx.close();
                        log.log(Level.SEVERE, "No se pudieron Commitear los cambios generados al eliminar OM y Requisicion");
                        return false;
                    }
                }
     }
     

     
  public boolean update() throws Exception
  {
                System.out.println("MRPUpdate con transaccion local");
                Trx trx = Trx.get(Trx.createTrxName("updateMRP"), true);
                /*  
                 *      VIT4B 28/11/2007
                 *      Modificaci�n para que contemple solo las activas isActive = 'Y'
                 *
                 */
  		//  Get Forcast  		
//  		String sql = "SELECT fl.M_FORECASTLINE_ID  FROM M_FORECASTLINE fl WHERE fl.Qty > 0  AND fl.AD_Client_ID = " + AD_Client_ID; 							                                
  		//String sql = "SELECT fl.M_FORECASTLINE_ID  FROM M_FORECASTLINE fl WHERE fl.Qty > 0  and fl.IsActive='Y' AND fl.AD_Client_ID = " + AD_Client_ID; 							                                
                String sql = "SELECT fl.M_FORECASTLINE_ID  FROM M_FORECASTLINE fl inner Join M_Forecast f on(f.M_Forecast_ID=fl.M_Forecast_ID) WHERE fl.Qty > 0 and  fl.IsActive='Y' and f.IsActive='Y' and f.IsRequiredMRP='Y' AND fl.AD_Client_ID = " + AD_Client_ID;
                // fjviejo e-evolution cambio para panalab forecast diferente para prod y ventas begin
  		PreparedStatement pstmt = null;
                ResultSet rs = null;
		try
		{
                    pstmt = DB.prepareStatement (sql,trx.getTrxName());
                    //pstmt.setInt(1, p_M_Warehouse_ID);
                    
                    rs = pstmt.executeQuery();                        
                    while (rs.next())
                    {
                        MForecastLine fl = new MForecastLine(Env.getCtx(),rs.getInt(1),trx.getTrxName());
                        /**
                         * Modificacion realizada por BISion para debug
                         */
                        int M_Product_ID = fl.getM_Product_ID();
                        if (M_Product_ID==1008251)
                            M_Product_ID = 0;
                        int result = MMPCMRP.M_ForecastLine(fl,trx.getTrxName(),false);
                        if ( result == 0 ){
                            enviarNotaFalloUpdate(trx.getTrxName(),"Fallo al actualizar MPC_MRP para ForecastLine: "+fl.toString(), MMPCMRP.getTableId(MMPCMRP.Table_Name),0);               
                        }
                    }                 
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"doIt - " + sql, e);
                        //Genero nota.
                        enviarNotaFalloQuery(trx.getTrxName(),"Fallo al obtener Lineas de pronostico activas para actualizar MPC_MRP",e.toString(), MMPCMRP.getTableId(MMPCMRP.Table_Name),0);
                        //throw e;
		}
                finally{
                    if (rs!=null)
                        rs.close();
                    if (pstmt!=null)
                        pstmt.close();
                }
                pstmt = null;
                rs = null;
  		//Get scheduled work order receipts		
                
                //sql = "SELECT * FROM MPC_Order o WHERE  (o.QtyOrdered - o.QtyDelivered) <> 0 AND o.DocStatus IN ('IP','CO') AND o.AD_Client_ID = " + AD_Client_ID;        
                
                
                /*      Vit4B 30/08/2007
                 *      Modificaci�n para que solo tome las ordenes que tienen algun pendiente
                 *      Entered es la cantidad original de la orden
                 *      Delivered es la cantidad entregada
                 *      La diferencia tiene que ser > 0
                
                 */
                
                
                /*  
                 *      VIT4B 20/11/2007
                 *      Agregado para que genere las ordenes en 'AP'
                 *
                 */
                
                //sql = "SELECT * FROM MPC_Order o WHERE  (o.QtyEntered - o.QtyDelivered) > 0 AND o.DocStatus IN ('IP','CO') AND o.AD_Client_ID = " + AD_Client_ID;        
                
                sql = "SELECT * FROM MPC_Order o WHERE  (o.QtyEntered - o.QtyDelivered) > 0 AND o.DocStatus IN ('IP','CO','AP') AND o.AD_Client_ID = " + AD_Client_ID;        
                
                try
		{
			pstmt = DB.prepareStatement (sql,trx.getTrxName());
                        //pstmt.setInt(1, p_M_Warehouse_ID);                        
                        rs = pstmt.executeQuery ();
                        while (rs.next())
                        {
                            /*
                            Modificacion para que si la cantidad pendiente de recibir en una orden de manufactura cae dentro del margen de tolerancia
                            de la categoria del producto, entonces no se genere el registro MPC_MRP
                            */
                            
                            MMPCOrder o = new MMPCOrder(getCtx(),rs,trx.getTrxName());
                            MProduct p = new MProduct(getCtx(),o.getM_Product_ID(),trx.getTrxName());
                            MZYNCATEGORYTOLERANCE pct = MZYNCATEGORYTOLERANCE.getZYNCategoryTolerance(getCtx(), p.getM_Product_Category_ID(), get_TrxName());
                            BigDecimal tolerance = new BigDecimal(0);
                            if (pct != null)
                                tolerance = pct.getTOLERANCE().divide(  new BigDecimal(100) );
                            BigDecimal minQtyTolerance = o.getQtyEntered().subtract(o.getQtyEntered().multiply(tolerance));
                            boolean isIntegratedMRP = Env.getContext(getCtx(), "RUN_MRP").equals("true");
                            if (  (o.getQtyDelivered().compareTo(minQtyTolerance) < 0 &&  isIntegratedMRP) 
                                    || !isIntegratedMRP){
                                int result = MMPCMRP.MPC_Order(o,trx.getTrxName());
                                if ( result == 0 ){
                                    enviarNotaFalloUpdate(trx.getTrxName(),"Fallo al actualizar MPC_MRP para MMPCOrder: "+o.toString(), MMPCMRP.getTableId(MMPCMRP.Table_Name),0);
                                }
                            }
                            else{
                                //Borro registros de MPC_MRP
                                String sqlDel = "DELETE FROM MPC_MRP mrp WHERE MPC_Order_ID  = " + o.getMPC_Order_ID();
                                DB.executeUpdate(sqlDel,trx.getTrxName());
                                enviarNotaOrdenOmitida(trx.getTrxName(),"MMPCOrder: "+o.getDocumentNo()+" no fue tenida en cuenta en el MRP ya que satisface las cantidades minimas.\nCantidad Entregada: "+o.getQtyDelivered()+"\nCantidad Requerida: "+o.getQtyEntered()+" Cantidad minima requerida: "+minQtyTolerance+"\nTolerancia: "+tolerance, MMPCMRP.getTableId(MMPCMRP.Table_Name),0,o);            
                            }
                            
                        }
		}
		catch (Exception e)
		{			
                        log.log(Level.SEVERE ,"doIt - " + sql, e);
                        //Genero nota.
                        enviarNotaFalloQuery(trx.getTrxName(),"Fallo al obtener Ordenes de Manufactura para actualizar MPC_MRP",e.toString(), MMPCMRP.getTableId(MMPCMRP.Table_Name),0);
                        //throw e;
		}
                
                finally{
                    if (rs!=null)
                        rs.close();
                    if (pstmt!=null)
                        pstmt.close();
                }
                pstmt = null;
                rs = null;
                
                //Get sales order requirements and Get scheduled purchase order receipts            
                /**BISion - 28/04/2009 - Santiago Ibañez
                 * Modificacion realizada para que en lugar de considerar aquellas lineas con qtyOrdered-qtyDelivered>0
                 * considere en realidad qtyEntered-qtyDelivered>0
                 */
                sql = "SELECT ol.C_OrderLine_ID FROM C_OrderLine ol INNER JOIN C_Order o ON (o.C_Order_ID = ol.C_Order_ID) WHERE (ol.QtyEntered - ol.QtyDelivered) > 0 AND o.DocStatus IN ('IP','CO','DR') AND ol.AD_Client_ID = " + AD_Client_ID;
 
		try
		{
			pstmt = DB.prepareStatement (sql,trx.getTrxName());
                        //pstmt.setInt(1, p_M_Warehouse_ID);

                        rs = pstmt.executeQuery();                        
                        while (rs.next())
                        {
                            MOrderLine ol = new MOrderLine(Env.getCtx(),rs.getInt(1),trx.getTrxName());
                            int result = MMPCMRP.C_OrderLine(ol,trx.getTrxName(),false);
                            if ( result == 0 ){
                                enviarNotaFalloUpdate(trx.getTrxName(),"Fallo al actualizar MPC_MRP para OrderLine: "+ol.toString(), MMPCMRP.getTableId(MMPCMRP.Table_Name),0);
                            }
                        }
                        //return true;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"doIt - " + sql, e);
                        //Genero nota.
                        enviarNotaFalloQuery(trx.getTrxName(),"Fallo al obtener Lineas de Ordenes de Compra/Venta para actualizar MPC_MRP",e.toString(), MMPCMRP.getTableId(MMPCMRP.Table_Name),0);
                        //throw e;
                        //return false;
		}
                
                finally{
                    if (rs!=null)
                        rs.close();
                    if (pstmt!=null)
                        pstmt.close();
                }
                pstmt = null;
                rs = null;
                
                //Get sales order requirements and Get scheduled purchase order receipts
                sql = "SELECT rl.M_RequisitionLine_ID  FROM M_RequisitionLine rl INNER JOIN M_Requisition r ON (r.M_Requisition_ID = rl.M_Requisition_ID) WHERE rl.Qty > 0 AND r.DocStatus <>'CL' AND rl.AD_Client_ID = " + AD_Client_ID;              
 
		try
		{
                        pstmt = DB.prepareStatement (sql,trx.getTrxName());
                        //pstmt.setInt(1, p_M_Warehouse_ID);

                        rs = pstmt.executeQuery();                        
                        while (rs.next())
                        {
                            MRequisitionLine rl = new MRequisitionLine(Env.getCtx(),rs.getInt(1),trx.getTrxName());
                            int result = MMPCMRP.M_RequisitionLine(rl,trx.getTrxName(),false);
                            if ( result == 0 ){
                                enviarNotaFalloUpdate(trx.getTrxName(),"Fallo al actualizar MPC_MRP para RequisitionLine: "+rl.toString(), MMPCMRP.getTableId(MMPCMRP.Table_Name),0);
                            }
                        }
                      //  return true;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"doIt - " + sql, e);
                        //return false;
                        //Genero nota
                        enviarNotaFalloQuery(trx.getTrxName(),"Fallo al obtener Lineas de requisiciones activas para MPC_MRP",e.toString(), MMPCMRP.getTableId(MMPCMRP.Table_Name),0);
                        
                        //throw e;
		
                }
                finally{
                    if (pstmt != null)
                        pstmt.close();
                    if (trx.commit()){
                        trx.close();
                        return true;
                    }
                    else
                    {
                        trx.rollback();
                        trx.close();
                        log.log(Level.SEVERE, "No se pudieron Commitear los cambios al realizar Update");
                        return false;
                    }
                    
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
    
    /*
    Actualiza los registros MPC_MRP para las ordenes que ya estan satisfechas
    */
    public boolean updateMPCOrdersSatisfechas() throws Exception {
        Trx trx = Trx.get(Trx.createTrxName("updateMPCOrders"), true);
        String sql = "SELECT * FROM MPC_Order o WHERE  (o.QtyEntered - o.QtyDelivered) > 0 AND o.DocStatus IN ('IP','CO','AP') AND o.AD_Client_ID = " + AD_Client_ID;        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            pstmt = DB.prepareStatement (sql,null);                      
            rs = pstmt.executeQuery ();
            while (rs.next())
            {
                MMPCOrder o = new MMPCOrder(getCtx(),rs,trx.getTrxName());
                MProduct p = new MProduct(getCtx(),o.getM_Product_ID(),trx.getTrxName());
                MZYNCATEGORYTOLERANCE pct = MZYNCATEGORYTOLERANCE.getZYNCategoryTolerance(getCtx(), p.getM_Product_Category_ID(),trx.getTrxName());
                BigDecimal tolerance = new BigDecimal(0);

                if (pct != null)
                    tolerance = pct.getTOLERANCE().divide(  new BigDecimal(100) );

                BigDecimal minQtyTolerance = o.getQtyEntered().subtract(o.getQtyEntered().multiply(tolerance));
                if ( o.getQtyDelivered().compareTo(minQtyTolerance) >= 0 ){
                    int result = MMPCMRP.MPC_Order(o,trx.getTrxName());
                    if ( result == 0 ){
                        enviarNotaFalloUpdate(trx.getTrxName(),"Fallo al actualizar MPC_MRP para MMPCOrder: "+o.toString(), MMPCMRP.getTableId(MMPCMRP.Table_Name),0);
                    }
                }
            }

        }
        catch (SQLException e)
        {			
            log.log(Level.SEVERE ,"doIt - " + sql, e);
            enviarNotaFalloQuery(trx.getTrxName(),"Fallo al obtener Ordenes de Manufactura para actualizar MPC_MRP",e.toString(), MMPCMRP.getTableId(MMPCMRP.Table_Name),0);

        }

        if (pstmt != null)
            pstmt.close();
        if (trx.commit()){
            trx.close();
            return true;
        }
        else
        {
            trx.rollback();
            trx.close();
            log.log(Level.SEVERE, "No se pudieron Commitear los cambios al realizar Update");
            return false;
        }
    }
    
    private void enviarNotaFalloQuery(String trx, String mensaje, String error, int tableId, int recordId) throws SQLException {
        MNote note = new MNote (Env.getCtx(), "MRP-INTERRUPTION-002", Env.getAD_User_ID(Env.getCtx()), trx);
        note.setTextMsg(mensaje + "\n Error: "+ error);
        note.setDescription("Proceso Generar Plan de Materiales");
        note.setAD_Table_ID(tableId);
        note.setRecord_ID(recordId);
        if (!note.save(trx)){
            log.log(Level.SEVERE, "No se pudo guardar la Nota: " + note.getDescription());
        }
    }
    
    private void enviarNotaFalloUpdate(String trx, String mensaje, int tableId, int recordId) throws SQLException {
        MNote note = new MNote (Env.getCtx(), "MRP-INTERRUPTION-001", Env.getAD_User_ID(Env.getCtx()), trx);
        note.setTextMsg(mensaje);
        note.setDescription("Proceso Generar Plan de Materiales");
        note.setAD_Table_ID(tableId);
        note.setRecord_ID(recordId);
        if (!note.save(trx)){
            log.log(Level.SEVERE, "No se pudo guardar la Nota: " + note.getDescription());
        }
    }
    
    private void enviarNotaOrdenOmitida(String trx, String mensaje, int tableId, int recordId, MMPCOrder o) throws SQLException {
        MNote note = new MNote (Env.getCtx(), "MRP-TOLERANCE-01", Env.getAD_User_ID(Env.getCtx()), trx);
        note.setTextMsg(mensaje);
        note.setDescription("Proceso Generar Plan de Materiales");
        note.setAD_Table_ID(tableId);
        note.setRecord_ID(recordId);
        
        MProduct p = new MProduct(Env.getCtx(),o.getM_Product_ID(),trx);
        note.setReference(o.getDocumentNo()+ " - " +p.getValue());
        if (!note.save()){
            log.log(Level.SEVERE, "No se pudo guardar la Nota: " + note.getDescription());
        }
    }
    
}
  
