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
import java.math.*;
import java.sql.*;
import javax.swing.JOptionPane;


import org.compiere.model.*;
import org.compiere.process.*;
import compiere.model.*;
import org.compiere.print.*;
import org.compiere.util.*;
import org.eevolution.model.MMPCOrder;
import org.eevolution.model.MMPCOrderNode;
import org.eevolution.model.MMPCOrderParentOrder;

/**
 *  CompletePrintOrder
 *
 *	@author Victor Pèrez
 *	@version $Id: CompletePrintOrder.java,v 1.4 2006/10/09 13:41:45 SIGArg-01 Exp $
 */
public class CompletePrintOrder extends SvrProcess {
    /**	The Order				*/
    private int		p_MPC_Order_ID = 0;
    private int		p_M_AttributeSetInstance_ID = 0;
    private boolean		p_IsPrintPickList = false;
    private boolean		p_IsPrintUnavailable = false;
    private boolean		p_IsPrintWorkflow = false;
    private boolean		p_IsPrintPackList = false;
    private boolean		p_IsComplete = false;
    private String      p_AttributeSetInstance_Desc = "";
    boolean IsDirectPrint = false;
    private int     p_PInstance_ID;
    
    /**
     *  Prepare - e.g., get Parameters.
     */
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (para[i].getParameter() == null)
                ;
            else if (name.equals("MPC_Order_ID"))
                p_MPC_Order_ID = ((BigDecimal)para[i].getParameter()).intValue();
            else if (name.equals("AttributeSetInstanceDesc"))
                p_AttributeSetInstance_Desc = (String)para[i].getParameter();
            else if (name.equals("M_AttributeSetInstance_ID"))
                p_M_AttributeSetInstance_ID = ((BigDecimal)para[i].getParameter()).intValue();
            else if (name.equals("IsPrintPickList"))
                p_IsPrintPickList = "Y".equals(para[i].getParameter());
            else if (name.equals("IsPrintWorkflow"))
                p_IsPrintWorkflow = "Y".equals(para[i].getParameter());
            else if (name.equals("IsPrintPackList"))
                p_IsPrintPackList = "Y".equals(para[i].getParameter());
            else if (name.equals("IsComplete"))
                p_IsComplete = "Y".equals(para[i].getParameter());
            else if (name.equals("IsPrintUnavailable"))
                p_IsPrintUnavailable = "Y".equals(para[i].getParameter());
            else
                log.log(Level.SEVERE,"prepare - Unknown Parameter: " + name);
        }
        p_PInstance_ID = getAD_PInstance_ID();
    }	//	prepare
    
    /**
     *  Perrform process.
     *  @return Message (clear text)
     *  @throws Exception if not successful
     */
    protected String doIt() throws Exception {
        
        MPrintFormat format = null;
        Language language = Language.getLoginLanguage();		//	Base Language
        java.util.Date today =new java.util.Date();
        java.sql.Timestamp now =new java.sql.Timestamp(today.getTime());
        
        if (p_MPC_Order_ID == 0)
            throw new IllegalArgumentException("Manufactuing Order == 0");    
        
        if (p_IsComplete) {
            
            
            /*
             * Chequeo partidas ya vencidas
             */
            
            Trx trx = Trx.get("reporteVencidas", true);
            //Instance de la orden con una trx distinta para el informe de las partidas no utilizadas
            MMPCOrder orderCheck = new MMPCOrder(getCtx(), p_MPC_Order_ID,trx.getTrxName());
            if (orderCheck.isUnavailable()) {
                //do print form
                System.out.println("Print Out");

                //Commit para que se vea reflejado en el informe
                trx.commit();
                int AD_PrintFormat_ID = getUnavailablePrintFormat();
                format = MPrintFormat.get(Env.getCtx(), AD_PrintFormat_ID, false);
                format.setLanguage(language);
                format.setTranslationLanguage(language);

                //	query
                MQuery query = new MQuery("RV_ORDER_NOT_AVAILABLE");
                query.addRestriction("MPC_ORDER_ID", MQuery.EQUAL, orderCheck.getMPC_Order_ID());

                System.out.println("QUERY DEL FORMATO DE IMPRESION");
                System.out.println(query);

                //	Engine
                PrintInfo info = new PrintInfo("MPC_ORDER",X_MPC_Order.getTableId(X_MPC_Order.Table_Name),getRecord_ID());
                ReportEngine re = new ReportEngine(Env.getCtx(), format, query, info);
                new Viewer(re);

            }
            else {
                JOptionPane.showMessageDialog(null,"No se imprime reporte porque no existe ninguna partida que no se pueda utilizar para esta OM", "Reporte partidas no utilizadas" , JOptionPane.INFORMATION_MESSAGE);
            }
            
            
            /*
             * Chequeo partidas prontas a vencer  
             */
            
            Trx trxSoonExpires = Trx.get("reporteProntasAVencer", true);
            //Instance de la orden con una trx distinta para el informe de las partidas no utilizadas
            MMPCOrder orderCheckSoonExpires = new MMPCOrder(getCtx(), p_MPC_Order_ID,trxSoonExpires.getTrxName());
            if (orderCheckSoonExpires.isSoonExpires()) {
                //do print form
                System.out.println("Print Out");

                //Commit para que se vea reflejado en el informe
                trxSoonExpires.commit();
                int AD_PrintFormat_ID = getSoonExpiresPrintFormat();
                format = MPrintFormat.get(Env.getCtx(), AD_PrintFormat_ID, false);
                format.setLanguage(language);
                format.setTranslationLanguage(language);

                //	query
                MQuery query = new MQuery("RV_ORDER_SOON_EXPIRES");
                query.addRestriction("MPC_ORDER_ID", MQuery.EQUAL, orderCheck.getMPC_Order_ID());

                System.out.println("QUERY DEL FORMATO DE IMPRESION");
                System.out.println(query);
                
                PrintInfo info = new PrintInfo("MPC_Order",X_MPC_Order.getTableId(X_MPC_Order.Table_Name), getRecord_ID());
                
                //	Engine
                ReportEngine re = new ReportEngine(Env.getCtx(), format, query, info);
                new Viewer(re);

            }
            else {
                JOptionPane.showMessageDialog(null,"No se imprime reporte porque no existe ninguna partida pronta a vencer para esta OM", "Reporte partidas prontas a vencer" , JOptionPane.INFORMATION_MESSAGE);
            }
            
            /*
             * Apruebo, chequeo disponibilidad y libero la orden
             */
            
            MMPCOrder order = new MMPCOrder(getCtx(), p_MPC_Order_ID,get_TrxName());
            
            /*
             * GENEOS - Pablo Velazquez
             * 27/09/2013
             * Si la orden no esta aprobada la apruebo 
             * y valido que haya podido ser aprobada
             */
            
            if ( !order.getDocStatus().equals(order.DOCSTATUS_Approved)) {
                order.setDocStatus(order.prepareItfromaprove());
                order.setDateConfirm(now);
                order.setDocAction(order.DOCACTION_Complete);
            }
            if ( !order.getDocStatus().equals(order.DOCSTATUS_Approved) )
                throw new IllegalArgumentException("La orden no pudo ser aprobada");
            
            //Chequeo que haya insumos disponibles para la orden
            
            if (order.isAvailable()) {
                
                /*
                Verifico Saldos y los informo para ver si el usuario quiere continuar
                */
                if ( notificarExistencia(order) ) {
              
                    MAttributeSetInstance mAttributeSetInstance = null;

                    //Si no tiene M_AttributeSetInstance genero una de forma automatica.
                    if (order.getM_AttributeSetInstance_ID() == 0) {

                        /*
                         * GENEOS - Pablo Velazquez
                         * 17/07/2013
                         * Se crea la instacia de atributos de forma automatica siguiendo la siguiente logica:
                         * Granel -> Toma secuencia automatica segun corresponda
                         * Semielaborado -> Hereda de Granel el primer LOTE por FeFo, y si todos los graneles de los que
                         *      depende no han sido ya emitidos se arroja un error. Si no tiene tiene de quien heredar 
                         *      (porque no existe puntero -> la orden se creo manualmente y no a traves del MRP) toma el primer LOTE
                         *      por FeFo del producto GRANEL del que dependa.
                         * Producto Terminado -> Hereda de Semielaborado,  y si todos los semielaborados de los que
                         *      depende no han sido ya emitidos se arroja un error.
                         * 
                         * Luego este lote se guarda y se genera efectivamente solo si la orden puede ser Emitida!
                         */

                        int M_Product_ID = order.getM_Product_ID();
                        MProduct product = new MProduct(getCtx(),M_Product_ID,get_TrxName());

                        // Get Model
                        mAttributeSetInstance = MAttributeSetInstance.getWhitTrx(Env.getCtx(),
                                        0, M_Product_ID,get_TrxName());
                        if (mAttributeSetInstance == null)
                             throw new IllegalArgumentException("No Model for M_AttributeSetInstance_ID="
                                                + 0 + ", M_Product_ID="
                                                + M_Product_ID);

                        //Granel -> Deberia tener siempre el valor en true
                        if ( product.isFinal() ) //Es final -> Genero lote automatico (Granel o caso especial)
                        {
                            KeyNamePair pp = mAttributeSetInstance.createLotTrx(M_Product_ID,get_TrxName());
                            if (pp == null) 
                                 throw new IllegalArgumentException("Error creando el LOTE");
                        }
                        else { //Es Producto Terminado o Semielaborado -> Heredo Lote
                            MMPCOrder[] childOrders = MMPCOrderParentOrder.getChilds(getCtx(),order.getMPC_Order_ID(),get_TrxName());

                            MAttributeSetInstance childMAttributeSetInstance = null;

                            if (childOrders.length == 0){ //Heredo Lote por FeFo de productos BOM de la Orden o Granel (Segun corresponda)
                                //Obtengo Partida

                                childMAttributeSetInstance = getMAttributeSetInstanceByGranel();

                                if (childMAttributeSetInstance == null)
                                    childMAttributeSetInstance = getMAttributeSetInstanceByBOM();
                                //Busco MMPCOrder que tenga ese MAttributeSetInstance
                                /*int parentMPCOrder = getMPCOrderByMAttributeSetInstance(childMAttributeSetInstance.getM_AttributeSetInstance_ID());
                                //Creo vinculo
                                MMPCOrderParentOrder parentOrder = new MMPCOrderParentOrder(getCtx(),0,get_TrxName());
                                parentOrder.setMPC_Order_ID(order.getMPC_Order_ID());
                                parentOrder.setMPC_PARENTORDER_ID( parentMPCOrder );
                                if (!parentOrder.save())
                                    throw new IllegalArgumentException("No se pudo guardar la relacion entre: "+ order +" y "+parentMPCOrder);*/
                            }
                            else { //Heredo Lote por FeFo de los hijos
                                boolean canRelease = true;
                                String neededOrders = "";
                                //Chequeo que todos los hijos esten emitidos
                                for (int i=0; i<childOrders.length;i++){
                                    MMPCOrder childOrder = childOrders[i];
                                    if ( !( childOrder.getDocStatus().equals(DocAction.ACTION_Complete) ||
                                                childOrder.getDocStatus().equals(DocAction.ACTION_Close) ) ||
                                                    childOrder.getM_AttributeSetInstance_ID() == 0) {
                                        canRelease = false;
                                        neededOrders += childOrder.getDocumentNo() + " ";

                                    }
                                }
                                if ( !canRelease )
                                    throw new IllegalArgumentException("Se debe/n emitir primero la/s Orden/es: "+neededOrders);
                                //Obtengo Partida
                                childMAttributeSetInstance = getMAttributeSetInstanceByChilds(childOrders);
                            }
                            if (childMAttributeSetInstance == null)
                                throw new IllegalArgumentException("No se pudo obtener Partida por FeFo para heredar Lote");
                            //Heredo Lote
                            mAttributeSetInstance.duplicateLot(new MLot(getCtx(),childMAttributeSetInstance.getM_Lot_ID(),get_TrxName() ),childMAttributeSetInstance.getLot());
                            //Heredo Fecha de vencimiento
                            mAttributeSetInstance.setGuaranteeDate(childMAttributeSetInstance.getGuaranteeDate());
                        }


                        //Guardo el Lote HEREDADO
                        if ( !mAttributeSetInstance.save(get_TrxName()) )
                            throw new RuntimeException(Msg.translate(Env.getCtx(), "No se pudo crear partida"));
                        order.setM_AttributeSetInstance_ID(mAttributeSetInstance.getM_AttributeSetInstance_ID());
                    }
                    order.setDateConfirm(now);
                    //order.setDocStatus(DocAction.STATUS_Completed);


                    if (!order.completeIt().equals(DocAction.STATUS_Completed))
                        throw new RuntimeException(Msg.translate(Env.getCtx(), "NoCompleted"));             

                    order.setAttributeSetInstanceDesc(p_AttributeSetInstance_Desc);
                    order.save(get_TrxName());
                } else {
                    throw new RuntimeException(Msg.translate(Env.getCtx(), "Proceso cancelado por el Usuario"));
                }
            } else {
                //return Msg.translate(Env.getCtx(), "NoQtyAvailable");
                throw new RuntimeException(Msg.translate(Env.getCtx(), "NoQtyAvailable"));
            }
            
        }
        /*
        if (p_IsPrintUnavailable){
            MMPCOrder order = new MMPCOrder(getCtx(), p_MPC_Order_ID,get_TrxName());
            if (order.isUnavailable()) {
                //do print form
                System.out.println("Print Out");
            }
        }
        */
        
        if (p_IsPrintPickList) {
            //	Get Format & Data
            // fjviejo e-evolution T_ComponentCheck
            String sqlupd;
            String sqlins;
            int cntu = 0;
            int cnti = 0;
            
            boolean isQTYPorcent = false;
            int acumQTYPorcent = 0;
            int acumQTYBOM = 0;
            int QTYSuf = 0;
            
            log.info("Inventory Valuation Temporary Table");
            
            
            log.info("Delete MO");
            String sqldel ="delete from T_ComponentCheck where MPC_Order_ID="+p_MPC_Order_ID;
            cntu = DB.executeUpdate(sqldel,null);
            System.out.println("*****  Created=" +cntu);
            // Clear
            //	v_ResultStr := 'ClearTable';
            //	DELETE T_InventoryValue WHERE M_Warehouse_ID=p_M_Warehouse_ID;
            //	COMMIT;
            
            // Insert Products
            sqlins = "INSERT INTO T_ComponentCheck "
                    + "(AD_Client_ID,AD_Org_ID, AD_PInstance_ID, MPC_Order_ID,MPC_Order_BOM_ID,MPC_Order_BOMLine_ID,M_Product_ID,Name, Value,C_UOM_ID  ,QtyOnhand , QtyRequiered "
                    + ",QtyReserved,QtyAvailable ,M_Warehouse_ID  ,QtyBom  ,QtyBatch ,M_Locator_ID ,m_attributesetinstance_id,x ,y ,z) ";
            // + "SELECT AD_Client_ID,AD_Org_ID," + p_PInstance_ID + "," + p_MPC_Order_ID + ",M_Product_ID "
            //+ "FROM M_Storage "
            //+ "WHERE IsStocked='Y'";
//		cnti = DB.executeUpdate(sqlins);
//		if (cnti == 0) {
//			return "@Created@ = 0";
//		}
//		if (cnti < 0) {
//			raiseError("InsertStockedProducts:ERROR", sqlins);
//		}
            int q_M_Product_ID=0;
            int q_M_Warehouse_ID=0;
            int q_MPC_Order_ID=0;
            float q_QTYConvert=0;
            float q_QTYConvertBOM=0;
            BigDecimal req1=Env.ZERO;
            BigDecimal requp=Env.ZERO;
            
            StringBuffer sqlobl = new StringBuffer("Select obl.M_Product_ID, o.M_Warehouse_ID, o.MPC_Order_ID, obl.MPC_Order_BOM_ID, obl.MPC_Order_BOMLine_ID, obl.QtyRequiered,BOMQtyReserved(obl.M_Product_ID,obl.M_Warehouse_ID,0),BOMQtyAvailable(obl.M_Product_ID,obl.M_Warehouse_ID,0),obl.QtyBom ,obl.isQtyPercentage, obl.QtyBatch, p.Value,p.Name, p.C_UOM_ID, obl.AD_Client_ID, obl.AD_Org_ID, (obl.QTYBATCH * o.QTYENTERED / 100) as QTYConvert, (obl.QTYBOM * o.QTYENTERED) as QTYConvertBOM  from MPC_Order_BOMLine obl INNER Join MPC_Order o ON(o.MPC_Order_ID=obl.MPC_Order_ID) Inner Join M_Product p ON(p.M_Product_ID=obl.M_Product_ID) where obl.MPC_Order_ID=" +p_MPC_Order_ID);
            
            System.out.println("***** Imprime primer sql" +sqlobl.toString());
            try {
                
                PreparedStatement pstmtobl = DB.prepareStatement(sqlobl.toString(),null);
                //pstmt.setInt(1, AD_Client_ID);
                ResultSet rsobl = pstmtobl.executeQuery();
                //
                while (rsobl.next()) {
                    System.out.println("***** entra primer sql" +rsobl.getInt(1));
                    q_M_Product_ID = rsobl.getInt(1);
                    q_M_Warehouse_ID = rsobl.getInt(2);
                    q_MPC_Order_ID=rsobl.getInt(3);
                    req1=rsobl.getBigDecimal(6);
                    
                    q_QTYConvert = rsobl.getFloat(17);
                    q_QTYConvertBOM = rsobl.getFloat(18);
                    
                    System.out.println("***** resultado de la comparativa de % " + rsobl.getString(10));
                    
                    if(rsobl.getString(10).equals("Y"))
                        isQTYPorcent = true;
                    
                    if(isQTYPorcent) {
                        if(rsobl.getInt(11) == -1)
                            QTYSuf = 100 - acumQTYPorcent;
                        else
                            acumQTYPorcent += rsobl.getInt(11);
                    } else {
                        if(rsobl.getInt(9) == -1)
                            QTYSuf = 100 - acumQTYBOM;
                        else
                            acumQTYBOM += rsobl.getInt(9);
                    }
                    
                    
                    
                    
                    System.out.println("***** resultado de la operacion " + q_QTYConvert);
                    
                    
                    
                    int count1=0;
                    StringBuffer sql = new StringBuffer("SELECT s.AD_Client_ID, s.AD_Org_ID,s.M_Product_ID , s.QtyOnHand, s.Updated, p.Name ,p.Value, masi.Description, l.Value, w.Value, w.M_warehouse_ID, p.C_UOM_ID,s.M_Locator_ID, s.M_AttributeSetInstance_ID, l.x,l.y,l.z ");
                    sql.append("  FROM M_Storage s ");
                    sql.append(" INNER JOIN M_Product p ON (s.M_Product_ID = p.M_Product_ID) ");
                    sql.append(" INNER JOIN M_AttributeSetInstance masi ON (masi.M_AttributeSetInstance_ID = s.M_AttributeSetInstance_ID) ");
                    sql.append(" Inner Join M_Locator l ON (s.M_Locator_ID=l.M_Locator_ID and l.M_Locator_ID in (select loc.m_locator_id from m_locator loc JOIN m_warehouse ware ON (ware.m_warehouse_id = loc.m_warehouse_id and ware.ISRELEASE = 'Y') ) )" );
                    sql.append(" inner join m_warehouse w on (w.m_warehouse_id = l.m_warehouse_id) ");
                    sql.append(" WHERE s.M_Product_ID = " +q_M_Product_ID + " and s.QtyOnHand-s.QtyReserved > 0 AND ( masi.guaranteedate > current_date OR masi.guaranteedate is null ) ORDER BY masi.guaranteedate asc, masi.m_attributesetinstance_id asc " );
                    
                    log.log(Level.INFO, "TComponentCheck.executeQuery - SQL", sql.toString());
                    //  reset table
                    //  Execute
                    
                    
                    PreparedStatement pstmt1 = DB.prepareStatement(sql.toString(),null);
                    //pstmt.setInt(1, AD_Client_ID);

                    ResultSet rs1 = pstmt1.executeQuery();
                    //
                    
                    while (rs1.next()) {
                        if (req1.compareTo(rs1.getBigDecimal(4)) <0) {
                            if (req1.compareTo(Env.ZERO)<=0)
                                requp=Env.ZERO;
                            else
                                requp=req1;
                            req1=req1.subtract(rs1.getBigDecimal(4));
                        } else {
                            
                            requp=rs1.getBigDecimal(4);
                            req1=req1.subtract(rs1.getBigDecimal(4));
                        }
                        
                        
                        System.out.println("*****  req1=" +req1);
                        System.out.println("*****  Id de la instancia de attributos = " + rs1.getBigDecimal(14));
                        String sqlins2="";
                        
                        if(isQTYPorcent) {
                            if(rsobl.getInt(11) == -1)
                                sqlins2 = sqlins +"Values("+rs1.getInt(1) +"," +rs1.getInt(2) +"," +p_PInstance_ID +"," +q_MPC_Order_ID +"," +rsobl.getInt(4) +"," +rsobl.getInt(5) +"," +q_M_Product_ID +",'" +rs1.getString(6) +"','" +rs1.getString(7) +"',"
                                        +rs1.getInt(12) +"," +rs1.getBigDecimal(4) +"," +requp +"," +rsobl.getBigDecimal(7) +"," +rsobl.getBigDecimal(8) +"," +q_M_Warehouse_ID +"," +rsobl.getBigDecimal(9) +"," + QTYSuf +"," +rs1.getInt(13) +"," +rs1.getInt(14) +"," +rs1.getString(15) +"," +rs1.getString(16) + "," + rs1.getString(17) + ")";
                            else
                                sqlins2 = sqlins +"Values("+rs1.getInt(1) +"," +rs1.getInt(2) +"," +p_PInstance_ID +"," +q_MPC_Order_ID +"," +rsobl.getInt(4) +"," +rsobl.getInt(5) +"," +q_M_Product_ID +",'" +rs1.getString(6) +"','" +rs1.getString(7) +"',"
                                        +rs1.getInt(12) +"," +rs1.getBigDecimal(4) +"," +requp +"," +rsobl.getBigDecimal(7) +"," +rsobl.getBigDecimal(8) +"," +q_M_Warehouse_ID +"," +rsobl.getBigDecimal(9) +"," +rsobl.getBigDecimal(11) +"," +rs1.getInt(13) +"," +rs1.getInt(14) +"," +rs1.getString(15) +"," +rs1.getString(16) + "," + rs1.getString(17) + ")";
                        } else {
                            if(rsobl.getInt(9) == -1)
                                sqlins2 = sqlins +"Values("+rs1.getInt(1) +"," +rs1.getInt(2) +"," +p_PInstance_ID +"," +q_MPC_Order_ID +"," +rsobl.getInt(4) +"," +rsobl.getInt(5) +"," +q_M_Product_ID +",'" +rs1.getString(6) +"','" +rs1.getString(7) +"',"
                                        +rs1.getInt(12) +"," +rs1.getBigDecimal(4) +"," +requp +"," +rsobl.getBigDecimal(7) +"," +rsobl.getBigDecimal(8) +"," +q_M_Warehouse_ID +"," + QTYSuf +"," + rsobl.getBigDecimal(11) +"," +rs1.getInt(13) +"," +rs1.getInt(14) +"," +rs1.getString(15) +"," +rs1.getString(16) + "," + rs1.getString(17) + ")";
                            else
                                sqlins2 = sqlins +"Values("+rs1.getInt(1) +"," +rs1.getInt(2) +"," +p_PInstance_ID +"," +q_MPC_Order_ID +"," +rsobl.getInt(4) +"," +rsobl.getInt(5) +"," +q_M_Product_ID +",'" +rs1.getString(6) +"','" +rs1.getString(7) +"',"
                                        +rs1.getInt(12) +"," +rs1.getBigDecimal(4) +"," +requp +"," +rsobl.getBigDecimal(7) +"," +rsobl.getBigDecimal(8) +"," +q_M_Warehouse_ID +"," +rsobl.getBigDecimal(9) +"," +rsobl.getBigDecimal(11) +"," +rs1.getInt(13) +"," +rs1.getInt(14) +"," +rs1.getString(15) +"," +rs1.getString(16) + "," + rs1.getString(17) + ")";
                        }
                        
                        System.out.println("***** inserta lineas " +sqlins2 );
                        cnti = DB.executeUpdate(sqlins2,null);
                        
                        
                        count1++;
                        System.out.println("*****  Created=" +cnti);
                        
                        
                    }
                    rs1.close();
                    pstmt1.close();
                    
                    if (count1==0) {
                        
                        String sqlins2="";
                        
                        
                        
                        if(isQTYPorcent) {
                            if(rsobl.getInt(11) == -1)
                                sqlins2 = sqlins +"Values("+rsobl.getInt(15) +"," +rsobl.getInt(16) +"," +p_PInstance_ID +"," +q_MPC_Order_ID +"," +rsobl.getInt(4) +"," +rsobl.getInt(5) +"," +q_M_Product_ID +",'" +rsobl.getString(13) +"','" +rsobl.getString(12) +"',"
                                        +rsobl.getInt(14) +",0," +rsobl.getBigDecimal(6) +"," +rsobl.getBigDecimal(7) +"," +rsobl.getBigDecimal(8) +"," +q_M_Warehouse_ID +"," +rsobl.getBigDecimal(9) +"," + QTYSuf +",0,0,'','','')";
                            else
                                sqlins2 = sqlins +"Values("+rsobl.getInt(15) +"," +rsobl.getInt(16) +"," +p_PInstance_ID +"," +q_MPC_Order_ID +"," +rsobl.getInt(4) +"," +rsobl.getInt(5) +"," +q_M_Product_ID +",'" +rsobl.getString(13) +"','" +rsobl.getString(12) +"',"
                                        +rsobl.getInt(14) +",0," +rsobl.getBigDecimal(6) +"," +rsobl.getBigDecimal(7) +"," +rsobl.getBigDecimal(8) +"," +q_M_Warehouse_ID +"," +rsobl.getBigDecimal(9) +"," +rsobl.getBigDecimal(11) +",0,0,'','','')";
                        }
                        
                        else {
                            if(rsobl.getInt(9) == -1)
                                sqlins2 = sqlins +"Values("+rsobl.getInt(15) +"," +rsobl.getInt(16) +"," +p_PInstance_ID +"," +q_MPC_Order_ID +"," +rsobl.getInt(4) +"," +rsobl.getInt(5) +"," +q_M_Product_ID +",'" +rsobl.getString(13) +"','" +rsobl.getString(12) +"',"
                                        +rsobl.getInt(14) +",0," +rsobl.getBigDecimal(6) +"," +rsobl.getBigDecimal(7) +"," +rsobl.getBigDecimal(8) +"," +q_M_Warehouse_ID +"," + QTYSuf +"," +rsobl.getBigDecimal(11) +",0,0,'','','')";
                            else
                                sqlins2 = sqlins +"Values("+rsobl.getInt(15) +"," +rsobl.getInt(16) +"," +p_PInstance_ID +"," +q_MPC_Order_ID +"," +rsobl.getInt(4) +"," +rsobl.getInt(5) +"," +q_M_Product_ID +",'" +rsobl.getString(13) +"','" +rsobl.getString(12) +"',"
                                        +rsobl.getInt(14) +",0," +rsobl.getBigDecimal(6) +"," +rsobl.getBigDecimal(7) +"," +rsobl.getBigDecimal(8) +"," +q_M_Warehouse_ID +"," +rsobl.getBigDecimal(9) +"," +rsobl.getBigDecimal(11) +",0,0,'','','')";
                            
                        }
                        
                        System.out.println("***** inserta lineas " +sqlins2 );
                        cnti = DB.executeUpdate(sqlins2,null);
                    } else {
                        
                        if (req1.compareTo(Env.ZERO)>0) {
                            String sqlins2="";
                            sqlins2 = sqlins +"Values("+rsobl.getInt(15) +"," +rsobl.getInt(16) +"," +p_PInstance_ID +"," +q_MPC_Order_ID +"," +rsobl.getInt(4) +"," +rsobl.getInt(5) +"," +q_M_Product_ID +",'" +rsobl.getString(13) +"','" +rsobl.getString(12) +"',"
                                    +rsobl.getInt(14) +",0," +req1 +"," +rsobl.getBigDecimal(7) +"," +rsobl.getBigDecimal(8) +"," +q_M_Warehouse_ID +"," +rsobl.getBigDecimal(9) +"," +rsobl.getBigDecimal(11) +",0,0,'','','')";
                            
                            System.out.println("***** inserta lineas " +sqlins2 );
                            cnti = DB.executeUpdate(sqlins2,null);
                        }
                        
                    }
                    
                    
                }
                rsobl.close();
                pstmtobl.close();
            } catch(SQLException obl) {
            }
//                  fjviejo e-evolution T_ComponentCheck
            
            if(isQTYPorcent == true) {
                System.out.println("***** ES POR PORCENTAJE ******* valor: " + isQTYPorcent);
                format = MPrintFormat.get(getCtx(), 1000639, false);
            } else {
                System.out.println("***** NO ES POR PORCENTAJE ******* valor: " + isQTYPorcent);
                format = MPrintFormat.get(getCtx(), 1000099, false);
            }
            
            format.setLanguage(language);
            format.setTranslationLanguage(language);
            //	query
            MQuery query = new MQuery("MPC_Order");
            query.addRestriction("MPC_Order_ID", MQuery.EQUAL, new Integer(p_MPC_Order_ID));
            
            //	Engine
            
            
            System.out.println("QUERY DEL FORMATO DE IMPRESION");
            System.out.println(query);
            PrintInfo info = new PrintInfo("MPC_Order",X_MPC_Order.getTableId(X_MPC_Order.Table_Name), getRecord_ID());
            ReportEngine re = new ReportEngine(getCtx(), format, query, info);
            //new Viewer(re);
            
            if (IsDirectPrint) {
                re.print();
                //ReportEngine.printConfirm ( 1000282 , Record_ID);
            } else
                new Viewer(re);
            
        }
        if (p_IsPrintWorkflow) {
            //	Get Format & Data
            
            format = MPrintFormat.get(getCtx(), 1000177, false);
            
            format.setLanguage(language);
            format.setTranslationLanguage(language);
            //	query
            MQuery query = new MQuery("MPC_Order");
            query.addRestriction("MPC_Order_ID", MQuery.EQUAL, new Integer(p_MPC_Order_ID));
            
            //	Engine
            PrintInfo info = new PrintInfo("MPC_Order",X_MPC_Order.getTableId(X_MPC_Order.Table_Name), getRecord_ID());
            ReportEngine re = new ReportEngine(getCtx(), format, query, info);
            //new Viewer(re);
            
            if (IsDirectPrint) {
                re.print();	//	prints only original
            } else
                new Viewer(re);
        }
        //                             //fjv e-evolution Operation Activity Report begin
        try {
            StringBuffer sql=new StringBuffer("SELECT MPC_Order_Node_ID FROM MPC_Order_Node WHERE IsActive='Y' AND  MPC_Order_ID=? Order By Value");
            PreparedStatement pstmt = DB.prepareStatement(sql.toString(),null);
            // pstmt.setInt(1, AD_Client_ID);
            pstmt.setInt(1, p_MPC_Order_ID);
            //pstmt.setInt(2, m_M_PriceList_ID);
            ResultSet rs = pstmt.executeQuery();
            //while (!m_calculated && rsplv.next())
            while (rs.next()) {
                MMPCOrderNode onode =new MMPCOrderNode(Env.getCtx(),rs.getInt(1),null);
                
                /*  Comentado por fviejo */    
                //onode.setDocStatus("IP");
                onode.save();
            }
            rs.close();
            pstmt.close();
        } catch (SQLException enode) {
        }
        //fjv e-evolution Operation Activity Report end
        
        return Msg.translate(Env.getCtx(), "Ok");
        
    }	//	doIt
        
        public MAttributeSetInstance getMAttributeSetInstanceByGranel(){
            MMPCOrder order = new MMPCOrder(getCtx(), p_MPC_Order_ID,get_TrxName());
            MProduct[] products = order.getGRANELProducts();
            
            if ( products.length == 0 )
                   return null;
            
            String sql = 
                    "SELECT str.m_attributesetinstance_id" +
                    "  FROM m_storage str" +
                    "  JOIN m_attributesetinstance asi on (str.m_attributesetinstance_id = asi.m_attributesetinstance_id)" +
                    "  JOIN m_locator l on (str.m_locator_id = l.m_locator_id)" +
                    "  WHERE qtyonhand - qtyreserved > 0"+
                    //Fecha de vencimiento debe ser Mayor a la de Hoy
                    "  AND asi.guaranteedate > current_date"+
                    // Solo en mi almacen
                    //"  AND l.m_warehouse_id = "+order.getM_Warehouse_ID()+" AND ( ";
                    //Solo almacen de emision
                    " AND l.m_locator_id in (select loc.m_locator_id from m_locator loc "
                    + "join m_warehouse ware on (ware.m_warehouse_id = loc.m_warehouse_id and ware.ISRELEASE='Y') "
                    + ")"
                    + " AND ( ";
            for (int i = 0; i<products.length; i++){
                    if (i > 0)
                        sql += " OR";   
                    sql += " m_product_id = "+products[i].getM_Product_ID();
            }
            sql += "        ) ";
            sql +="  ORDER BY asi.guaranteedate";
            PreparedStatement pstmt = null;
            int m_attributesetinstance_id = 0;
            try {
                pstmt = DB.prepareStatement(sql.toString(),get_TrxName());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next())
                    m_attributesetinstance_id = rs.getInt(1);
                rs.close();
                pstmt.close();
                pstmt = null;
            }
            catch (Exception e) {
                log.log(Level.SEVERE, "getMAttributeSetInstanceByGranel - " + sql, e);
            }
        MAttributeSetInstance returnValue = null;
        if (m_attributesetinstance_id != 0)
            returnValue = new MAttributeSetInstance(getCtx(), m_attributesetinstance_id, get_TrxName());
        else
            log.log(Level.SEVERE, "getMAttributeSetInstanceByGranel - AttributeSetInstance not Found");
        return returnValue;
        }
    
        public MAttributeSetInstance getMAttributeSetInstanceByBOM(){
            MMPCOrder order = new MMPCOrder(getCtx(), p_MPC_Order_ID,get_TrxName());
            MProduct[] products = order.getBOMProducts();
            
            if ( products.length == 0 )
                   return null;
            
            String sql = 
                    "SELECT str.m_attributesetinstance_id" +
                    "  FROM m_storage str" +
                    "  JOIN m_attributesetinstance asi on (str.m_attributesetinstance_id = asi.m_attributesetinstance_id)" +
                    "  JOIN m_locator l on (str.m_locator_id = l.m_locator_id)" +
                    "  WHERE qtyonhand - qtyreserved > 0"+
                    //Fecha de vencimiento debe ser Mayor a la de Hoy
                    "  AND asi.guaranteedate > current_date"+
                    // Solo en mi almacen
                    //"  AND l.m_warehouse_id = "+order.getM_Warehouse_ID()+" AND ( ";
                    //Solo almacen de emision
                    " AND l.m_locator_id in (select loc.m_locator_id from m_locator loc "
                    + "join m_warehouse ware on (ware.m_warehouse_id = loc.m_warehouse_id and ware.ISRELEASE='Y') "
                    + ")"
                    + " AND ( ";
            for (int i = 0; i<products.length; i++){
                    if (i > 0)
                        sql += " OR";   
                    sql += " m_product_id = "+products[i].getM_Product_ID();
            }
            sql += "        ) ";
            sql +="  ORDER BY asi.guaranteedate";
            PreparedStatement pstmt = null;
            int m_attributesetinstance_id = 0;
            System.out.println(sql);
            try {
                pstmt = DB.prepareStatement(sql.toString(),get_TrxName());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next())
                    m_attributesetinstance_id = rs.getInt(1);
                rs.close();
                pstmt.close();
                pstmt = null;
            }
            catch (Exception e) {
                log.log(Level.SEVERE, "getMAttributeSetInstanceByBOM - " + sql, e);
            }
        MAttributeSetInstance returnValue = null;
        if (m_attributesetinstance_id != 0)
            returnValue = new MAttributeSetInstance(getCtx(), m_attributesetinstance_id, get_TrxName());
        else
            log.log(Level.SEVERE, "getMAttributeSetInstanceByBOM - AttributeSetInstance not Found");
        return returnValue;
        }
    
        public MAttributeSetInstance getMAttributeSetInstanceByChilds(MMPCOrder[] childs){
            MMPCOrder order = new MMPCOrder(getCtx(), p_MPC_Order_ID,get_TrxName());
            String sql = 
                    "SELECT m_attributesetinstance_id" +
                    "  FROM m_attributesetinstance" +
                    "  WHERE m_attributesetinstance_id in ( ";
            for (int i = 0; i<childs.length; i++){
                    if (i > 0)
                        sql += ", ";    
                    sql += childs[i].getM_AttributeSetInstance_ID();
            }
            sql += "        ) ";
            sql +="  ORDER BY guaranteedate";
            PreparedStatement pstmt = null;
            int m_attributesetinstance_id = 0;
            try {
                pstmt = DB.prepareStatement(sql.toString(),get_TrxName());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next())
                    m_attributesetinstance_id = rs.getInt(1);
                rs.close();
                pstmt.close();
                pstmt = null;
            }
            catch (Exception e) {
                log.log(Level.SEVERE, "getMAttributeSetInstanceByChild - " + sql, e);
            }
        MAttributeSetInstance returnValue = null;
        if (m_attributesetinstance_id != 0)
            returnValue = new MAttributeSetInstance(getCtx(), m_attributesetinstance_id, get_TrxName());
        else
            log.log(Level.SEVERE, "getMAttributeSetInstanceByChild - AttributeSetInstance not Found");
        return returnValue;
        }
        
        public int getMPCOrderByMAttributeSetInstance(int M_AttributeSetInstace_ID){
            String sql = 
                    "SELECT mpc_order_id" +
                    "  FROM mpc_order " +
                    "  WHERE m_attributesetinstance_id = "+M_AttributeSetInstace_ID;
            int mpc_order_id = 0;            
            try {
                PreparedStatement pstmt = DB.prepareStatement(sql.toString(),get_TrxName());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next())
                    mpc_order_id = rs.getInt(1);
                rs.close();
                pstmt.close();
                pstmt = null;
            }
            catch (Exception e) {
                log.log(Level.SEVERE, "getMPCOrderByMAttributeSetInstance - " + sql, e);
            }
        return mpc_order_id;
        }
        
        private int getUnavailablePrintFormat(){
            String sql = "select ad_printformat_id from ad_printformat where name = 'PartidasVencidasMPCOrder'";
            int id = 0;
            PreparedStatement ps = DB.prepareStatement(sql, null);
            try{
                ResultSet rs = ps.executeQuery();
                if (rs.next())
                    id = rs.getInt(1);
            }
            catch(SQLException ex){
            }
            return id;
        }
        
        private boolean notificarExistencia(MMPCOrder order){
                MProduct[] products = order.getBOMProducts();
                boolean hayPartidas = false;
                //Si no tiene componentes manufacturados, no es necesario validar
                if (products == null || products.length == 0)
                    return true;
                String mensaje = "";
                for (int i=0; i<products.length; i++){
                    String partidas = "";
                    //Obtengo existencia por FeFo (Solo almacenes de surtimiento), teniendo en cuenta tambien las vencidas
                    MStorage[] existencias = MStorage.getAllWithASIFEFO(getCtx(),products[i].getM_Product_ID(), true, true, get_TrxName());
                    if (existencias.length > 0) {
                        hayPartidas = true;
                        partidas += "Existen las siguientes partidas para le producto "+products[i].getValue()+":\n";
                    }
                    for (int j=0; j<existencias.length; j++){
                        MAttributeSetInstance asi = new MAttributeSetInstance(getCtx(), existencias[j].getM_AttributeSetInstance_ID(), get_TrxName());
                        partidas += "\tPartida: "+ asi.getDescription()+" | Cantidad: "+existencias[j].getQtyOnHand()+"\n";
                    }
                    mensaje += partidas;
                }
                if (hayPartidas){
                    int aux = JOptionPane.showConfirmDialog(null,mensaje, "Hay stock existente de nivel inferior", JOptionPane.ERROR_MESSAGE);
                    if ( aux==JOptionPane.NO_OPTION ) {
                        return false;
                    }    
                }
                return true;
        }
        
        private int getSoonExpiresPrintFormat(){
            String sql = "select ad_printformat_id from ad_printformat where name = 'PartidasProntasVencerMPCOrder'";
            int id = 0;
            PreparedStatement ps = DB.prepareStatement(sql, null);
            try{
                ResultSet rs = ps.executeQuery();
                if (rs.next())
                    id = rs.getInt(1);
            }
            catch(SQLException ex){
            }
            return id;
        }
    
}	//	CompletePrintOrder
