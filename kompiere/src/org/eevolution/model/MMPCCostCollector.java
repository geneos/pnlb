/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2004 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * created by Victor Perez are Copyright (C) e-Evolution,SC. All Rights Reserved.
 * Contributor(s): Victor Perez
 *****************************************************************************/
//package org.compiere.mfg.model;
package org.eevolution.model;


import java.util.*;
import java.sql.*;
import java.math.*;
import java.util.logging.*;
import java.io.*;
import javax.swing.JOptionPane;

import org.compiere.process.*;
import org.compiere.util.*;
import org.compiere.model.*;
import org.compiere.wf.*;
import org.compiere.print.*;
import org.compiere.process.*;
import org.compiere.util.*;
/**
 *	Inventory Movement Model
 *
 *  @author Jorg Janke
 *  @version $Id: MMPCCostCollector.java,v 1.5 2006/10/09 13:41:45 SIGArg-01 Exp $
 */
public class MMPCCostCollector extends X_MPC_Cost_Collector implements DocAction {
    /**
     * 	Standard Constructor
     *	@param ctx context
     *	@param MPC_Cost_Collector id
     */
    public MMPCCostCollector(Properties ctx, int MPC_Cost_Collector_ID, String trxName) {
        super(ctx, MPC_Cost_Collector_ID,trxName);
        if (MPC_Cost_Collector_ID == 0) {
            //	setC_DocType_ID (0);
            setDocAction(DOCACTION_Complete);	// CO
            setDocStatus(DOCSTATUS_Drafted);	// DR
            //setIsApproved (false);
            //setIsInTransit (false);
            setMovementDate(new Timestamp(System.currentTimeMillis()));	// @#Date@
            setPosted(false);
            super.setProcessed(false);
        }
    }	//	MMovement
    
    /**
     * 	Load Constructor
     *	@param ctx context
     *	@param rs result set
     */
    public MMPCCostCollector(Properties ctx, ResultSet rs,String trxName) {
        super(ctx, rs,trxName);
    }	//	MMovement
    
    
    /**
     * 	Add to Description
     *	@param description text
     */
    public void addDescription(String description) {
        String desc = getDescription();
        if (desc == null)
            setDescription(description);
        else
            setDescription(desc + " | " + description);
    }	//	addDescription
    
    /**
     * 	Set Processed.
     * 	Propergate to Lines/Taxes
     *	@param processed processed
     */
    public void setProcessed(boolean processed) {
        super.setProcessed(processed);
        if (get_ID() == 0)
            return;
        String sql = "UPDATE MPC_Cost_Collector SET Processed='"
                + (processed ? "Y" : "N")
                + "' WHERE MPC_Cost_Collector_ID =" + getMPC_Cost_Collector_ID();
        int noLine = DB.executeUpdate(sql, get_TrxName());
        //m_lines = null;
        log.fine("setProcessed - " + processed + " - Lines=" + noLine);
    }	//	setProcessed
    
    
    /**************************************************************************
     * 	Process document
     *	@param processAction document action
     *	@return true if performed
     */
    public boolean processIt(String processAction) {
        m_processMsg = null;
        DocumentEngine engine = new DocumentEngine(this, getDocStatus());
        return engine.processIt(processAction, getDocAction());
    }	//	processIt
    
    /**	Process Message 			*/
    private String		m_processMsg = null;
    /**	Just Prepared Flag			*/
    private boolean		m_justPrepared = false;
    
    /**
     * 	Unlock Document.
     * 	@return true if success
     */
    public boolean unlockIt() {
        log.info("unlockIt - " + toString());
        setProcessing(false);
        return true;
    }	//	unlockIt
    
    /**
     * 	Invalidate Document
     * 	@return true if success
     */
    public boolean invalidateIt() {
        log.info("invalidateIt - " + toString());
        setDocAction(DOCACTION_Prepare);
        return true;
    }	//	invalidateIt
    
    /**
     *	Prepare Document
     * 	@return new status (In Progress or Invalid)
     */
    public String prepareIt() {
        log.info("prepareIt - " + toString());
        
        MDocType dt = MDocType.get(getCtx(), getC_DocType_ID()); //getC_DocType_ID()
        
        //	Std Period open?
        if (!MPeriod.isOpen(getCtx(), getMovementDate(), dt.getDocBaseType())) {
            m_processMsg = "@PeriodClosed@";
            log.severe("@PeriodClosed@ al completar Cost Collector:"+toString());
            return DocAction.STATUS_Invalid;
        }
        
        
        
        m_justPrepared = true;
        if (!DOCACTION_Complete.equals(getDocAction()))
            setDocAction(DOCACTION_Complete);
        return DocAction.STATUS_InProgress;
    }	//	prepareIt
    
    /**
     * 	Approve Document
     * 	@return true if success
     */
    
    public boolean  approveIt() {
        log.info("approveIt - " + toString());
        //setIsApproved(true);
        return true;
    }	//	approveIt
    
    /**
     * 	Reject Approval
     * 	@return true if success
     */
    
    public boolean rejectIt() {
        log.info("rejectIt - " + toString());
        //setIsApproved(false);
        return true;
    }	//	rejectIt
    
    /**
     * 	Complete Document
     * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
     */
    public String completeIt() {
        
        
        /*
        **  Agregado VIT4B
        */
        String  conultStatus = "SELECT * FROM MPC_Order WHERE MPC_Order_ID = " + getMPC_Order_ID() + " AND MPC_Order.DocStatus = 'CO'";
        PreparedStatement pstmtStatus = DB.prepareStatement(conultStatus,get_TrxName());
        ResultSet rsStatus = null;
        try {
            rsStatus = pstmtStatus.executeQuery();
            
            if (!rsStatus.next()) {
                JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"Operaci�n inv�lida por Orden no Aprobada"), "Info" , JOptionPane.INFORMATION_MESSAGE);
                log.severe("Orden No aprobada, "+toString());
                return "Orden Incompleta";
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            /** BISion - 14/11/2008 - Santiago Iba�ez
             * Lanza la excepci�n para que sea capturada y tratada por 
             * SvrProcess
             */
            return "No se pudo comprobar que la orden de manufactura esta aprobada";
        }
        
        //	Re-Check
        if (!m_justPrepared) {
            String status = prepareIt();
            if (!DocAction.STATUS_InProgress.equals(status))
                return status;
        }
        
        
        MProduct product = new MProduct(getCtx(),getM_Product_ID(),get_TrxName());
        
        //	Qty & Type
        String MovementType = getMovementType();
        
        BigDecimal QtyIssue = Env.ZERO;
        BigDecimal QtyReceipt = Env.ZERO;
        
        BigDecimal Qty = getMovementQty();
        
        if (MovementType.charAt(1) == '-') {
            Qty = Qty.negate();
            QtyIssue = Qty;
        } else {
            QtyReceipt = Qty.negate();
        }
        
        //	Update Order Line
        MMPCOrderBOMLine obomline = null;
        if (getMPC_Order_BOMLine_ID()!= 0) {
            obomline = new MMPCOrderBOMLine(getCtx(),getMPC_Order_BOMLine_ID(),get_TrxName());
        }
        
        log.info(" Qty=" + getMovementQty());
        
        //fjviejo e-evoltion Operation activity begin
        MDocType doc = new MDocType(Env.getCtx(),getC_DocType_ID(),get_TrxName());
        String doct ="";
        doct=doc.getDocBaseType();
        if(!doct.equals("MOA")) {
            //fjviejo e-evoltion Operation activity end
            
            //	Stock Movement - Counterpart MOrder.reserveStock
            if (product != null && product.isStocked() ) {
                
                log.fine("Material Transaction");
                MTransaction mtrx = null;
                int reservationAttributeSetInstance_ID = getM_AttributeSetInstance_ID();
                
                /*
                 * Nunca entra por aca
                 */
                if (getM_AttributeSetInstance_ID() == 0) {
                    
                    MMPCOrderBOMLineMA mas[] = MMPCOrderBOMLineMA.get(getCtx(),
                            getMPC_Cost_Collector_ID(), get_TrxName());
                    for (int j = 0; j < mas.length; j++) {
                        MMPCOrderBOMLineMA ma = mas[j];
                        BigDecimal QtyMA = ma.getMovementQty();
                        
                        if (MovementType.charAt(1) == '-') {
                            QtyMA = QtyMA.negate();
                            QtyIssue = QtyMA;
                        } else {
                            QtyReceipt = QtyMA.negate();
                            
                            /*
                             *      Vit4B 10/08/2007
                             *
                             *      Modificaci�n para que la recepci�n de terminado no actualice el ordenado en M_Storage por una
                             *      cantidad mayor a la que deber�a que es como maximo lo reservado que es la diferencia
                             *      entre lo requerido de la orden y lo entregado de terminado.
                             *
                             *      Reviso que la cantidad que va a actualizar en el ordenado QtyReceipt
                             *      no sea mayor que lo que tiene pendiente la orden.
                             *
                             *
                             */
                            
                            MMPCOrder order = new MMPCOrder(getCtx(), getMPC_Order_ID(),get_TrxName());
                            
                            //Si lo que recibo es mayor
                            if(QtyReceipt.compareTo(order.getQtyReserved()) == 1) 
                                QtyReceipt = order.getQtyReserved();
                            
                            
                        }
                                  
                        if (MovementType.charAt(1) == '-') {
                        
                            /*
                             *      Ac� deber�a controlar que la actualizacion del M_Storage a partir de
                             *      la linea no deje en la devolucion una cantidad mayor al reservado de la linea y
                             *      no deje la entrega un reservado menor al reservado total menos el reservado de la linea.
                             *      lo que significa que se esta devolviendo mas de lo que se entrego.
                             *      Arreglos para arreglar la operatoria del reservado y las devoluciones.
                             *      Vit4b 26/06/2007
                             *
                             */                   

                            //      QtyMA

                            Properties ctx = getCtx();
                            String trxName = get_TrxName();
                            MStorage st = null;                          
                            
                            st = MStorage.get (ctx, getM_Locator_ID(),getM_Product_ID(), 0, trxName);

                            BigDecimal QtyReal = Qty.negate();
                            BigDecimal st_reserved = Env.ZERO;
                            BigDecimal bomline_requiered = Env.ZERO;
                            BigDecimal bomline_reserved = Env.ZERO;
                            BigDecimal bomline_entered = Env.ZERO;
                            BigDecimal diff = Env.ZERO;
                            BigDecimal necesary = Env.ZERO;

                            if (st != null)	//	create if not existing - should not happen
                            {
                                st_reserved = st.getQtyReserved();
                                bomline_requiered = obomline.getQtyRequiered();
                                bomline_reserved = obomline.getQtyReserved();
                                bomline_entered = obomline.getQtyDelivered();


                                // cantidad negativa indica devolucion
                                if(QtyReal.compareTo(Env.ZERO) == -1)
                                {
                                    // La devolucion lo que hace es sumar al reservado por lo tanto esa suma nunca
                                    // debe superar lo requerido por la linea.
                                    //
                                    // Ademas debe considerarse que hay que verificar 
                                    // lo entregado menos lo devuelto contra lo requerido.

                                    diff = bomline_reserved.add(QtyReal.negate());                               
                                    necesary = bomline_entered.add(QtyReal);   

                                    if(necesary.compareTo(bomline_requiered) != -1)
                                    {
                                        QtyIssue = Env.ZERO;
                                    }
                                    else if(diff.compareTo(bomline_requiered) == 1)
                                    {
                                        QtyIssue = bomline_requiered.subtract(bomline_reserved);
                                    }

                                }
                                // sino cantidad positiva surtimiento
                                else
                                {
                                    // si surtimiento es de una cantidad mayor a la reservada
                                    // no debo restar mas que lo que tengo reservado al reservado de M_Storage
                                    if(QtyReal.compareTo(bomline_reserved) > 0)
                                    {
                                        QtyIssue = bomline_reserved;
                                    }
                                    else
                                    {
                                        QtyIssue = QtyReal;
                                    }

                                    QtyIssue = QtyIssue.negate();    

                                }            
        

                            }
                            else
                                log.severe("Error al obtener MStorage para "+toString());              
                        }
                        
                        if (!MStorage.add(getCtx(), getM_Warehouse_ID(),
                                getM_Locator_ID(),
                                getM_Product_ID(),
                                ma.getM_AttributeSetInstance_ID(), ma.getM_AttributeSetInstance_ID(), // fjviejo e-evolution cambio qtyorder
                                //ma.getM_AttributeSetInstance_ID(), ma.getM_AttributeSetInstance_ID(),
                                QtyMA, QtyIssue, QtyReceipt, get_TrxName())) {
                            m_processMsg = "Cannot correct Inventory (MA)";
                            return DocAction.STATUS_Invalid;
                        } else {
                            System.out.println("***** getattibute=0 reservation " +reservationAttributeSetInstance_ID +" ma getattribute " +ma.getM_AttributeSetInstance_ID());
                        }
                        
                        //	Create Transaction
                        
                        mtrx = new MTransaction(getCtx(),
                                MovementType,getM_Locator_ID(),
                                getM_Product_ID(), ma.getM_AttributeSetInstance_ID(),
                                QtyMA, getMovementDate(), get_TrxName());
                        mtrx.setMPC_Order_ID(getMPC_Order_ID());
                        mtrx.setMPC_Order_BOMLine_ID(getMPC_Order_BOMLine_ID());
                        if (!mtrx.save(get_TrxName())) {
                            m_processMsg = "Could not create Material Transaction (MA)";
                            return DocAction.STATUS_Invalid;
                        }
                        
                    }
                }
                //	sLine.getM_AttributeSetInstance_ID() != 0
                if (mtrx == null) {
                    //	Fallback: Update Storage - see also VMatch.createMatchRecord
                    
                    BigDecimal diffReserved = BigDecimal.ZERO;
                    int M_Locator_ID = getM_Locator_ID();
                    
                    if (MovementType.charAt(1) == '-') {
                        
                        /*
                         *      Ac� deber�a controlar que la actualizacion del M_Storage a partir de
                         *      la linea no deje en la devolucion una cantidad mayor al reservado de la linea y
                         *      no deje la entrega un reservado menor al reservado total menos el reservado de la linea.
                         *      lo que significa que se esta devolviendo mas de lo que se entrego.
                         *      Arreglos para arreglar la operatoria del reservado y las devoluciones.
                         *      Vit4b 26/06/2007
                         *
                         */                   

                        //      QtyMA
                        /*
                        Properties ctx = getCtx();
                        String trxName = get_TrxName();
                        MStorage st = null;
                        st = MStorage.get (ctx, getM_Locator_ID(),getM_Product_ID(), 0, trxName);

                        BigDecimal QtyReal = Qty.negate();
                        BigDecimal st_reserved = Env.ZERO;
                        BigDecimal bomline_requiered = Env.ZERO;
                        BigDecimal bomline_reserved = Env.ZERO;
                        BigDecimal bomline_entered = Env.ZERO;
                        BigDecimal diff = Env.ZERO;
                        BigDecimal necesary = Env.ZERO;

                        if (st != null)	//	create if not existing - should not happen
                        {
                            st_reserved = st.getQtyReserved();
                            bomline_requiered = obomline.getQtyRequiered();
                            bomline_reserved = obomline.getQtyReserved();
                            bomline_entered = obomline.getQtyDelivered();
                            // cantidad negativa indica devolucion
                            if(QtyReal.compareTo(Env.ZERO) == -1)
                            {
                                /** BISion - 04/05/2009 - Santiago Ibañez
                                 * Actualizar correctamente QtyIssue que es la variable
                                 * que actualiza el Storage
                                 *//*
                                BigDecimal entregado = obomline.getQtyDelivered();
                                BigDecimal devolucion = getMovementQty();
                                BigDecimal requerido =  obomline.getQtyRequiered();

                                //Cantidad Entregada considerando esta devolucion
                                BigDecimal entregadoAhora = obomline.getQtyDelivered().add(QtyReal);
                                //Escenario 1: CE = CReq
                                if (entregado.equals(requerido)){
                                    QtyIssue = QtyReal.negate();
                                }
                                //Escenario 2: CE < CReq
                                else if (entregado.compareTo(requerido)==-1){
                                    QtyIssue = QtyReal.negate();
                                //Escenario 3: CE > CReq
                                }
                                else if (entregadoAhora.compareTo(requerido)>=0){
                                    QtyIssue = BigDecimal.ZERO;
                                }
                                else
                                    QtyIssue = requerido.subtract(entregadoAhora);

                                // La devolucion lo que hace es sumar al reservado por lo tanto esa suma nunca
                                // debe superar lo requerido por la linea.
                                // Ademas debe considerarse que hay que verificar 
                                // lo entregado menos lo devuelto contra lo requerido.
                            }
                            // sino cantidad positiva surtimiento
                            else
                            {
                                // si surtimiento es de una cantidad mayor a la reservada
                                // no debo restar mas que lo que tengo reservado al reservado de M_Storage
                                if(QtyReal.compareTo(bomline_reserved) > 0)
                                {
                                    QtyIssue = bomline_reserved;
                                }
                                else
                                {
                                    QtyIssue = QtyReal;
                                }
                                
                                QtyIssue = QtyIssue.negate();    

                            }            

                        }
                        /*
                         * 18/06/2013 Maria Jesus Martin
                         *
                         * Modificacion para que si no existe la linea en Storage para una dupla de
                         * almacen y producto, que igual se contemple el caso de cuales son las cantidades
                         * que se estan trabajando en el Reservado que luego se va a sustraer de la linea del
                         * Storage.
                         *//*
                        else
                        {
                            BigDecimal requiered = obomline.getQtyRequiered();
                            BigDecimal delivered = obomline.getQtyDelivered();
                            BigDecimal reserved = obomline.getQtyReserved();
                            if ((QtyIssue.negate().add(delivered)).compareTo(requiered) > 0){
                                QtyIssue = reserved.negate();
                            }
                        }*/
                        
                        /*
                        * GENEOS - Pablo Velazquez
                        * 31/07/2013
                        * Actualizo el control de reservados MMPCOrderQtyReserved y  obtengo el reservado a actualizar
                        * de MStorage
                        */
                        MMPCOrderQtyReserved qtyRes = null; 
                        
                        try {
                            
                            
                            if ( getMovementQty().signum() == 1 )
                                qtyRes = getMMPCOrderQtyReserved();

                            //Pueder ser que no exista el qtyRes, porque esta partida no estaba comprometida
                            //O porque estoy en una devolucion entonces no es necesario instanciar
                           
                            if (qtyRes != null) {

                                BigDecimal storageReservedBefore = qtyRes.getRemainingQty();
                                BigDecimal storageReservedAfter = storageReservedBefore.subtract(getMovementQty());
                                

                                //Si entrego mas de lo que tenia disponible del reservado para la partida, entonces mi reservado pasa a CERO
                                if ( storageReservedAfter.signum() == -1 )
                                    storageReservedAfter=BigDecimal.ZERO;

                                //Si lo que devuelvo + lo que tenia, es mayor a lo que habia reservado al liberarla orden, entonces el reservado
                                //pasa a ser ese total
                                if ( storageReservedAfter.compareTo(qtyRes.getTotalQty()) > 0 )
                                    storageReservedAfter=qtyRes.getTotalQty();
                                
                                //Cantidad en la que se modifica el reserved del storage
                                diffReserved = storageReservedAfter.subtract(storageReservedBefore);
                                        
                                qtyRes.setRemainingQty(storageReservedAfter);
                                if (!qtyRes.save()){
                                    log.severe("No se pudieron actualizar los reservados en "+qtyRes+" para MMPCCostCollector: "+toString());
                                    return DocAction.STATUS_Invalid;
                                }
                            }
                            else {
                                // Si no existen partidas comprometidas para la linea, entonces sus cantidades reservadas estan en MStorage Cero
                                if (  MMPCOrderQtyReserved.getAllForBOMLine(getCtx(), getMPC_Order_BOMLine_ID(), get_TrxName()).length == 0  ) {
                            
                                    BigDecimal storageReservedBefore = obomline.getQtyReserved();
                                    BigDecimal storageReservedAfter = storageReservedBefore.subtract(getMovementQty());


                                    //Si entrego mas de lo que tenia disponible del reservado para la partida, entonces mi reservado pasa a CERO
                                    if ( storageReservedAfter.signum() == -1 )
                                        storageReservedAfter=BigDecimal.ZERO;

                                    //Cantidad en la que se modifica el reserved del storage
                                    diffReserved = storageReservedAfter.subtract(storageReservedBefore);
                                    
                                    //Se modifica el storage CERO con Default locator (Solo el reservado)                                  
                                    MWarehouse wh = MWarehouse.get(getCtx(), obomline.getM_Warehouse_ID());
                                    int defaultLocator = wh.getDefaultLocator().getM_Locator_ID();                               
                                   
                                    if (!MStorage.addDist(getCtx(), getM_Warehouse_ID(),
                                        defaultLocator, getM_Product_ID(), getM_AttributeSetInstance_ID(), 0, //fjviejo e-evolution cambio qtyordered
                                        //reservationAttributeSetInstance_ID, reservationAttributeSetInstance_ID,
                                        BigDecimal.ZERO, diffReserved, BigDecimal.ZERO, get_TrxName())) {
                                        m_processMsg = "Cannot correct Inventory";
                                            return DocAction.STATUS_Invalid;
                                        }
                                    
                                    //Seteo reservado en CERO para que no lo actualize nuevamente
                                    diffReserved = BigDecimal.ZERO;
                                }
                             }

                        }
                        catch (Exception e){
                            log.severe(e.getMessage());
                            return DocAction.STATUS_Invalid;
                        }                     

                    }
                    /** BISion - 09/03/2009 - Santiago Ibañez
                     * Agregado para actualizar correctamente el storage
                     */
                    //si el tipo de movimiento es 'P+'
                    else{
                        //Obtengo la orden de manufactura en cuestion
                        //null porque quiero la cantidad entregada antes de la recepcion)
                        MMPCOrder order = new MMPCOrder(Env.getCtx(),this.getMPC_Order_ID(),null);
                        //Si ya la cantidad entregada > cantidad
                        if (order.getQtyDelivered().compareTo(order.getQtyEntered())==1)
                            //nada que decrementar
                            QtyReceipt = BigDecimal.ZERO;
                        //Si ahora con esta recepcion la cantidad recibida supera la cantidad ingresada
                        else if (Qty.compareTo(order.getQtyOrdered())==1){
                            //Cantidad Ordenada = Cantidad - Entregada
                            QtyReceipt = order.getQtyEntered().subtract(order.getQtyDelivered()).negate();
                        }
                            
                    }
                    //fin modificacion BISion 
                    
                    
                    /*
                     * GENEOS - Pablo Velaquez
                     * 29/07/2013
                     * Modificacion para que actualize los reservados de partidas especificas
                     */
                          
                    //if (!MStorage.add(getCtx(), getM_Warehouse_ID(),
                    /*
                    if (!MStorage.addDist(getCtx(), getM_Warehouse_ID(),
                            getM_Locator_ID(),
                            getM_Product_ID(),
                            getM_AttributeSetInstance_ID(), reservationAttributeSetInstance_ID, //fjviejo e-evolution cambio qtyordered
                            //reservationAttributeSetInstance_ID, reservationAttributeSetInstance_ID,
                            Qty, QtyIssue, QtyReceipt, get_TrxName())) {*/
                    if (!MStorage.addDist(getCtx(), getM_Warehouse_ID(),
                            M_Locator_ID,
                            getM_Product_ID(),
                            getM_AttributeSetInstance_ID(), reservationAttributeSetInstance_ID, //fjviejo e-evolution cambio qtyordered
                            //reservationAttributeSetInstance_ID, reservationAttributeSetInstance_ID,
                            Qty, diffReserved, QtyReceipt, get_TrxName())) {
                        m_processMsg = "Cannot correct Inventory";
                        return DocAction.STATUS_Invalid;
                    } else {
                        System.out.println("***** mtrx=null reservation " +reservationAttributeSetInstance_ID +" getattribute " +getM_AttributeSetInstance_ID());
                    }
                    
                    //	FallBack: Create Transaction
                    
                    mtrx = new MTransaction(getCtx(),
                            MovementType,getM_Locator_ID(),
                            getM_Product_ID(),getM_AttributeSetInstance_ID(),
                            Qty, getMovementDate(), get_TrxName());
                    mtrx.setMPC_Order_ID(getMPC_Order_ID());
                    mtrx.setMPC_Order_BOMLine_ID(getMPC_Order_BOMLine_ID());
                    if (!mtrx.save(get_TrxName())) {
                        m_processMsg = "Could not create Material Transaction";
                        return DocAction.STATUS_Invalid;
                    }
                    
                }
                
            }	//	stock movement
            
            //Actualizo Lineas de la Orden de Manufactura
            if (MovementType.charAt(1) == '-') {
                //	Update Order Line
                if (getMPC_Order_BOMLine_ID()!= 0) {
                    
                    obomline = new MMPCOrderBOMLine(getCtx(),getMPC_Order_BOMLine_ID(),get_TrxName());

                    /*
                     *      Ac� deber�a controlar que el calculo del reservado nunca sea negativo en la linea,
                     *      lo que significa que se esta devolviendo mas de lo que se entrego.
                     *      Arreglos para arreglar la operatoria del reservado y las devoluciones.
                     *      Vit4b 26/06/2007
                     *
                     */

                    BigDecimal dif_delivered = obomline.getQtyDelivered().add(getMovementQty());
                    
                    // en caso de significa que lo que tengo entregado es menor que lo que voy a devolver
                    if(dif_delivered.compareTo(Env.ZERO) == -1)
                    {           
                        obomline.setQtyDelivered(Env.ZERO);
                    }
                    // en caso de seguir entregando voy actualizando el entregado sumando lo que sigo entregando
                    else
                    {
                        obomline.setQtyDelivered(obomline.getQtyDelivered().add(getMovementQty()));
                    }
                    
                    
                    obomline.setQtyScrap(obomline.getQtyScrap().add(getScrappedQty()));
                    obomline.setQtyReject(obomline.getQtyReject().add(getQtyReject()));
                    obomline.setDateDelivered(getMovementDate());	//	overwrite=last
                    obomline.setM_AttributeSetInstance_ID(getM_AttributeSetInstance_ID()); // fjv e-evolution
                    log.fine("OrderLine - Reserved=" + obomline.getQtyReserved() + ", Delivered=" + obomline.getQtyDelivered());
                    
                    /*
                     *      Ac� deber�a controlar que el calculo del reservado nunca sea negativo en la linea.
                     *      Ademas de controlar que nunca sea mayor que el requerido.
                     *      Arreglos para arreglar la operatoria del reservado y las devoluciones.
                     *      Vit4b 26/06/2007
                     *
                     */
                    
                    BigDecimal bomline_requiered = obomline.getQtyRequiered();
                    BigDecimal bomline_reserved = obomline.getQtyReserved();
                    BigDecimal diff = Env.ZERO;
                    
                    /*
                     * GENEOS - Pablo Velazquez
                     * 24/10/2013
                     * Las devoluciones no afectan el reservado de la orden (La devoluciion
                     * se efectua luego de la recepcion, por lo que los reservados ya no existen en
                     * esta instancia)
                     */
                    if (getMovementQty().signum() == 1) {
                    
                        if(dif_delivered.compareTo(Env.ZERO) != -1)
                        {
                            if(dif_delivered.compareTo(bomline_requiered) >= 0)
                            {
                                obomline.setQtyReserved(Env.ZERO);       
                            }
                            else 
                            {
                                diff = bomline_requiered.subtract(dif_delivered);
                                /** BISion - 30/04/2009 - Santiago Ibañez
                                 * Modificacion realizada para actualizar correctamente el reservado
                                 */
                                BigDecimal reservado = obomline.getQtyRequiered().subtract(obomline.getQtyDelivered());
                                obomline.setQtyReserved(diff); 
                            }
                        }
                        else
                        {
                            obomline.setQtyReserved(bomline_requiered);
                        }
                    }
                    
                    
                    /*
                     *      Fin
                     *      Vit4b 26/06/2007
                     *
                     */
                    
                    
                    if (!obomline.save(get_TrxName())) {
                        m_processMsg = "Could not update Order Line";
                        return DocAction.STATUS_Invalid;
                    } else
                        log.fine("OrderLine -> Reserved=" + obomline.getQtyReserved()
                        + ", Delivered=" + obomline.getQtyDelivered());
                }
            } 
            //Actualizo La Cabezera de la Orden de Manufactura
            else if  (MovementType.charAt(1) == '+') {
                MMPCOrder order = new MMPCOrder(getCtx(), getMPC_Order_ID(),get_TrxName());
                order.setQtyDelivered(order.getQtyDelivered().add(getMovementQty()));
                order.setQtyScrap(order.getQtyScrap().add(getScrappedQty()));
                order.setQtyReject(order.getQtyReject().add(getQtyReject()));
                order.setDateDelivered(getMovementDate());	//	overwrite=last
                log.fine("OrderLine - Reserved=" + order.getQtyReserved() + ", Delivered=" + order.getQtyDelivered());
                order.setQtyReserved(order.getQtyReserved().subtract(getMovementQty()));
                
                /** BISion - 06/03/2009 - Santiago Ibañez
                 * Modificacion realizada para actualizar tambien el ordenado
                 */
                order.setQtyOrdered(order.getQtyOrdered().subtract(getMovementQty()));
                if (order.getQtyOrdered().compareTo(Env.ZERO)==-1)
                    order.setQtyOrdered(Env.ZERO);

                /*
                 *  Vit4B 10/08/2207
                 *  Controlo que el reservado no sea negativo.                 *
                 *
                 */
                
                if(order.getQtyReserved().compareTo(Env.ZERO) == -1)
                {
                    order.setQtyReserved(Env.ZERO);
                }
                
                
                if (!order.save(get_TrxName())) {
                    m_processMsg = "Could not update Order";
                    return DocAction.STATUS_Invalid;
                } else
                    log.fine("Order -> Delivered=" + order.getQtyDelivered());
            }
            //                             //fjv e-evolution Operation Activity Report begin
            
        }
        //MDocType doc = new MDocType(Env.getCtx(),getC_DocType_ID(),get_TrxName());
        //String doct ="";
        doct=doc.getDocBaseType();
        if(doct.equals("MOA")) {
            MMPCOrderNode onodeact =new MMPCOrderNode(Env.getCtx(),getMPC_Order_Node_ID(),null);
            // comentado fviejo
            //onodeact.setDocStatus("CO");
            onodeact.setQtyScrap(onodeact.getQtyScrap().add(getScrappedQty()));
            onodeact.setQtyReject(onodeact.getQtyReject().add(getQtyReject()));
            onodeact.setQtyDelivered(onodeact.getQtyDelivered().add(getMovementQty()));
            onodeact.setDurationReal(onodeact.getDurationReal()+getDurationReal().intValue());
            onodeact.setSetupTimeReal(onodeact.getSetupTimeReal()+getSetupTimeReal().intValue());
            onodeact.save();
            
            MMPCOrder m_mpc_order = new MMPCOrder(Env.getCtx(),getMPC_Order_ID(),get_TrxName());
            
            Timestamp fecha1 = null;
            Timestamp fecha2 = null;
            
            fecha1 = m_mpc_order.getDateStart();
            fecha2 = getMovementDate();
            
            
            if(m_mpc_order.getDateStart() == null || m_mpc_order.getDateStart().compareTo(getMovementDate()) > 0)
            {
                m_mpc_order.setDateStart(getMovementDate());
                m_mpc_order.save();
            }     
            
            
            ArrayList list = new ArrayList();
            int count =0;
            try {
                StringBuffer sql=new StringBuffer("SELECT MPC_Order_Node_ID FROM MPC_Order_Node WHERE IsActive='Y' AND  MPC_Order_ID=? Order By Value");
                PreparedStatement pstmt = DB.prepareStatement(sql.toString(),get_TrxName());
                // pstmt.setInt(1, AD_Client_ID);
                pstmt.setInt(1, getMPC_Order_ID());
                //pstmt.setInt(2, m_M_PriceList_ID);
                ResultSet rs = pstmt.executeQuery();
                //while (!m_calculated && rsplv.next())
                while (rs.next()) {
                    
                    Integer nodeid = new Integer(rs.getInt(1));
                    list.add(count,nodeid.toString());
                    
                    count++;
                    
                }
                rs.close();
                pstmt.close();
            } catch (SQLException enode) {
            }
            boolean ultimonodo = false;
            
            for (int v =0 ; v < list.size(); v++) {
                if (list.get(v).equals(new Integer(getMPC_Order_Node_ID()).toString())) {
                    //String nextnode = new String(list.get(v+1));
                    try {
                        StringBuffer sqlnn=new StringBuffer("SELECT MPC_Order_Node_ID FROM MPC_Order_NodeNext WHERE IsActive='Y' AND  MPC_Order_ID=? and MPC_Order_Node_ID=?");
                        PreparedStatement pstmtnn = DB.prepareStatement(sqlnn.toString(),get_TrxName());
                        // pstmt.setInt(1, AD_Client_ID);
                        pstmtnn.setInt(1, getMPC_Order_ID());
                        pstmtnn.setInt(2, getMPC_Order_Node_ID());
                        ResultSet rsnn = pstmtnn.executeQuery();
                        //while (!m_calculated && rsplv.next())
                        System.out.println("***** SQL ultm nodo " +sqlnn.toString());
                        if (rsnn.next()) {
                            
                            ultimonodo=false;
                            
                            
                            
                        } else {
                            ultimonodo=true;
                        }
                        rsnn.close();
                        pstmtnn.close();
                    } catch (SQLException enodenn) {
                    }
                    if (!ultimonodo) {
                        System.out.println("***** No ES EL ULTIMO NODO");
                    } else {
                        try {
                            StringBuffer sql1=new StringBuffer("SELECT DocStatus,MPC_Order_Node_ID,DurationRequiered FROM MPC_Order_Node WHERE IsActive='Y' AND  MPC_Order_ID=? and MPC_Order_Node_ID!=?");
                            PreparedStatement pstmt1 = DB.prepareStatement(sql1.toString(),get_TrxName());
                            // pstmt.setInt(1, AD_Client_ID);
                            pstmt1.setInt(1, getMPC_Order_ID());
                            pstmt1.setInt(2, getMPC_Order_Node_ID());
                            ResultSet rs1 = pstmt1.executeQuery();
                            //while (!m_calculated && rsplv.next())
                            System.out.println("***** SQL1 " +sql1 + " variable " +getMPC_Order_ID());
                            while (rs1.next()) {
                                System.out.println("***** Nodo " +rs1.getInt(2) +" status " +rs1.getString(1));
//                                                        if(!rs1.getString(1).equals("CL"))
//                                                        {
//
//                                                            MMPCOrderNode onodenext =new MMPCOrderNode(Env.getCtx(),rs1.getInt(2),get_TrxName());
//                                                        onodenext.setDocStatus("CL");
//                                                        onodenext.save();
//                                                        }
                                createnewnode(rs1.getInt(2),rs1.getBigDecimal(3));
                                
                            }
                            rs1.close();
                            pstmt1.close();
                            
                            
                        } catch (SQLException enode) {
                        }
                    }
                }
                
            }
            
            // crear orden de compra al cmpletar
            int p_MPC_Order_Node_ID=0;
            BigDecimal m_MovementQty=Env.ZERO;
            
            try {
                StringBuffer sql=new StringBuffer("SELECT MPC_Order_Node_ID,MovementQty FROM MPC_Cost_Collector WHERE IsActive='Y' AND AD_Client_ID=? and MPC_Cost_Collector_ID=? ");
                PreparedStatement pstmt = DB.prepareStatement(sql.toString(),get_TrxName());
                pstmt.setInt(1, getAD_Client_ID());
                pstmt.setInt(2, getMPC_Cost_Collector_ID());
                //pstmt.setInt(2, m_M_PriceList_ID);
                ResultSet rs = pstmt.executeQuery();
                //while (!m_calculated && rsplv.next())
                while (rs.next()) {
                    p_MPC_Order_Node_ID= rs.getInt(1);
                    m_MovementQty=rs.getBigDecimal(2);
                }
                rs.close();
                pstmt.close();
            } catch (SQLException e) {
            }
            
            //   MMPCProfileBOMSelected profilebomsel = new MMPCProfileBOMSelected(Env.getCtx(),profilebom.getMPC_ProfileBOM_ID());
            //MMPCProductBOM prodbom = new MMPCProductBOM(Env.getCtx(),0);
            
            //   MPCProfileBOM.atributos(profilebom.getM_Product_ID());
            
            if(isSubcontracting()) {
                int M_Product_ID =0;
                String salvado="";
                BigDecimal DeliveryTime=Env.ZERO;
                try {
                    StringBuffer plv=new StringBuffer("SELECT M_Product_ID FROM MPC_Order_Node WHERE IsActive='Y' AND MPC_Order_Node_ID=? ");
                    PreparedStatement pstmtplv = DB.prepareStatement(plv.toString(),get_TrxName());
                    pstmtplv.setInt(1, p_MPC_Order_Node_ID);
                    //pstmt.setInt(2, m_M_PriceList_ID);
                    ResultSet rsplv = pstmtplv.executeQuery();
                    //while (!m_calculated && rsplv.next())
                    if (rsplv.next()) {
                        M_Product_ID= rsplv.getInt(1);
                    }
                    rsplv.close();
                    pstmtplv.close();
                } catch (SQLException e) {
                }
                
                if (M_Product_ID==0) {
                    
               /*
                *   AVISO QUE SE DEBE A QUE No hay un servicio asociado a este subcontrato ...
                *
                *
                **/
                    
                    
                    JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"No hay un servicio asociado a este subcontrato"), "Info" , JOptionPane.INFORMATION_MESSAGE);
                    
                    
                    salvado="No hay un servicio asociado a este subcontrato";
                    return salvado;
                } else {
                    try {
                        StringBuffer pp=new StringBuffer("SELECT DeliveryTime_Promised FROM MPC_Product_Planning WHERE IsActive='Y' AND M_Product_ID=? ");
                        PreparedStatement pstmtpp = DB.prepareStatement(pp.toString(),get_TrxName());
                        pstmtpp.setInt(1, M_Product_ID);
                        //pstmt.setInt(2, m_M_PriceList_ID);
                        ResultSet rspp = pstmtpp.executeQuery();
                        //while (!m_calculated && rsplv.next())
                        if (rspp.next()) {
                            DeliveryTime= rspp.getBigDecimal(1);
                        }
                        rspp.close();
                        pstmtpp.close();
                    } catch (SQLException e) {
                    }
                }
                //   MMPCProfileBOM profileorder = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID);
                int m_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
                int m_AD_Org_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Org_ID"));
                //   int m_M_Warehouse_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#M_Warehouse_ID"));
                //    MProfileBOM profileorder = new MProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID);
                int C_BPartner_ID=0;
                try {
                    StringBuffer sqlpo=new StringBuffer("SELECT C_BPartner_ID FROM M_Product_PO WHERE IsActive='Y' AND M_Product_ID=? ");
                    PreparedStatement pstmtpo = DB.prepareStatement(sqlpo.toString(),get_TrxName());
                    pstmtpo.setInt(1, M_Product_ID);
                    //pstmt.setInt(2, m_M_PriceList_ID);
                    ResultSet rspo = pstmtpo.executeQuery();
                    //while (!m_calculated && rsplv.next())
                    while (rspo.next()) {
                        C_BPartner_ID= rspo.getInt(1);
                    }
                    rspo.close();
                    pstmtpo.close();
                } catch (SQLException epo) {
                }
                
                if (C_BPartner_ID==0) {
                    
                             /*
                              *   AVISO QUE SE DEBE A QUE No hay un proveedor asociado a este servicio de subcontrato ...
                              *
                              *
                              **/
                    
                    
                    JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"No hay un proveedor asociado a este servicio de subcontrato"), "Info" , JOptionPane.INFORMATION_MESSAGE);
                    
                    salvado="No hay un proveedor asociado a este servicio de subcontrato";
                    return salvado;
                }
                MMPCOrder mpcorder = new MMPCOrder(Env.getCtx(),getMPC_Order_ID(),get_TrxName());
                String documentno = mpcorder.getDocumentNo();
                
                Timestamp today=new Timestamp(System.currentTimeMillis());
                
                MOrder order = new MOrder(Env.getCtx(),0,get_TrxName());
                order.setC_BPartner_ID(C_BPartner_ID);
                order.setIsSOTrx(false);
                order.setC_DocTypeTarget_ID();
                order.setDatePromised(TimeUtil.addDays(today, DeliveryTime.intValue()));
                order.setDescription(documentno);
                if (order.save()) {
                    MOrderLine oline = new MOrderLine(order);
                    oline.setM_Product_ID(M_Product_ID);
                    oline.setQtyEntered(m_MovementQty);
                    oline.setQtyOrdered(m_MovementQty);
                    oline.setDatePromised(TimeUtil.addDays(today, DeliveryTime.intValue()));
                    if(oline.save()) {
                        MOrderLine oline1 = new MOrderLine(order);
                        oline1.setPriceEntered(oline.getPriceActual());
                        oline1.setQtyOrdered(oline.getQtyEntered());
                        oline1.save();
                    }
                    
                    MMPCCostCollector cc = new MMPCCostCollector(Env.getCtx(),getMPC_Cost_Collector_ID(),get_TrxName());
                    cc.setProcessing(true);
                    cc.setDescription(order.getDocumentNo());
                    cc.save();
                } else {
                    return DocAction.STATUS_Invalid;
                }
                //fjv e-evolution Operation Activity Report end
            }
        }
        //	for all lines
        setProcessed(true);
        setDocAction(DOCACTION_Close);
        setDocStatus(DOCSTATUS_Completed);    //fjv e-evolution add field Docstatus
        /** BISion - 27/02/2009 - Santiago Ibañez
         * Modificacion realizada porque no actualizaba el estado 'Completo'
         */
        saveUpdate();
        return DocAction.STATUS_Completed;
    }	//	completeIt
    
    /**
     * 	Post Document - nothing
     * 	@return true if success
     */
    public boolean postIt() {
        log.info("postIt - " + toString());
        return false;
    }	//	postIt
    
    /**
     * 	Void Document.
     * 	@return true if success
     */
    public boolean voidIt() {
        log.info("voidIt - " + toString());
        return false;
    }	//	voidIt
    
    /**
     * 	Close Document.
     * 	@return true if success
     */
    public boolean closeIt() {
        log.info("closeIt - " + toString());
        
        //	Close Not delivered Qty
        // fjviejo e-evolution operation activity
        boolean ultimonodo=false;
        try {
            StringBuffer sqlnn=new StringBuffer("SELECT MPC_Order_Node_ID FROM MPC_Order_NodeNext WHERE IsActive='Y' AND  MPC_Order_ID=? and MPC_Order_Node_ID=?");
            PreparedStatement pstmtnn = DB.prepareStatement(sqlnn.toString(),null);
            // pstmt.setInt(1, AD_Client_ID);
            pstmtnn.setInt(1, getMPC_Order_ID());
            pstmtnn.setInt(2, getMPC_Order_Node_ID());
            ResultSet rsnn = pstmtnn.executeQuery();
            //while (!m_calculated && rsplv.next())
            System.out.println("***** SQL ultm nodo " +sqlnn.toString());
            if (rsnn.next()) {
                
                ultimonodo=false;
                
                
                
            } else {
                ultimonodo=true;
            }
            rsnn.close();
            pstmtnn.close();
        } catch (SQLException enodenn) {
        }
        
        if(!ultimonodo) {
            
            MMPCOrderNode onodeact =new MMPCOrderNode(Env.getCtx(),getMPC_Order_Node_ID(),null);
            // comentado fviejo
            //onodeact.setDocStatus("CL");
            //   onodeact.setAction(DOCACTION_None);
            onodeact.save();
            try {
                StringBuffer sql1=new StringBuffer("SELECT MPC_Cost_Collector_ID FROM MPC_Cost_Collector WHERE IsActive='Y' AND  MPC_Order_ID=? AND MPC_Order_Node_ID=?");
                PreparedStatement pstmt1 = DB.prepareStatement(sql1.toString(),null);
                // pstmt.setInt(1, AD_Client_ID);
                pstmt1.setInt(1, getMPC_Order_ID());
                pstmt1.setInt(2, getMPC_Order_Node_ID());
                ResultSet rs1 = pstmt1.executeQuery();
                //while (!m_calculated && rsplv.next())
                System.out.println("***** SQL1 " +sql1 + " variable " +getMPC_Order_ID());
                while (rs1.next()) {
                    MMPCCostCollector costcoll = new MMPCCostCollector(Env.getCtx(),rs1.getInt(1),get_TrxName());
                    costcoll.setDocStatus("CL");
                    costcoll.setDocAction(DOCACTION_None);
                    costcoll.save();
                    
                }
                rs1.close();
                pstmt1.close();
                
                
            } catch (SQLException enode1) {
            }
        } else {
            try {
                StringBuffer sql1=new StringBuffer("SELECT DocStatus,MPC_Order_Node_ID,DurationRequiered FROM MPC_Order_Node WHERE IsActive='Y' AND  MPC_Order_ID=?");
                PreparedStatement pstmt1 = DB.prepareStatement(sql1.toString(),null);
                // pstmt.setInt(1, AD_Client_ID);
                pstmt1.setInt(1, getMPC_Order_ID());
                //pstmt1.setInt(2, getMPC_Order_Node_ID());
                ResultSet rs1 = pstmt1.executeQuery();
                //while (!m_calculated && rsplv.next())
                System.out.println("***** SQL1 " +sql1 + " variable " +getMPC_Order_ID());
                while (rs1.next()) {
                    System.out.println("***** Nodo " +rs1.getInt(2) +" status " +rs1.getString(1));
                    if(!rs1.getString(1).equals("CL")) {
                        
                        MMPCOrderNode onodenext =new MMPCOrderNode(Env.getCtx(),rs1.getInt(2),get_TrxName());
                        // comentado fviejo
                        //onodenext.setDocStatus("CL");
                        onodenext.save();
                        
                        
                    }
                    
                    
                }
                rs1.close();
                pstmt1.close();
                
                
            } catch (SQLException enode) {
            }
            closenew(getMPC_Order_ID(),getMPC_Order_Node_ID());
        }
        // fjviejo e-evolution operation activity end
        setDocAction(DOCACTION_None);
        return true;
    }	//	closeIt
    
    /**
     * 	Reverse Correction
     * 	@return false
     */
    public boolean reverseCorrectIt() {
        log.info("reverseCorrectIt - " + toString());
        return false;
    }	//	reverseCorrectionIt
    
    /**
     * 	Reverse Accrual - none
     * 	@return false
     */
    public boolean reverseAccrualIt() {
        log.info("reverseAccrualIt - " + toString());
        return false;
    }	//	reverseAccrualIt
    
    /**
     * 	Re-activate
     * 	@return false
     */
    public boolean reActivateIt() {
        log.info("reActivateIt - " + toString());
        return false;
    }	//	reActivateIt
    
    
    /*************************************************************************
     * 	Get Summary
     *	@return Summary of Document
     */
    public String getSummary() {
        StringBuffer sb = new StringBuffer();
        sb.append(getDescription());
        //	: Total Lines = 123.00 (#1)
        //sb.append(": ")
        //	.append(Msg.translate(getCtx(),"ApprovalAmt")).append("=").append(getApprovalAmt())
        //	.append(" (#").append(")");
        //	 - Description
        if (getDescription() != null && getDescription().length() > 0)
            sb.append(" - ").append(getDescription());
        return sb.toString();
    }	//	getSummary
    
    /**
     * 	Get Process Message
     *	@return clear text error message
     */
    public String getProcessMsg() {
        return m_processMsg;
    }	//	getProcessMsg
    
    /**
     * 	Get Document Owner (Responsible)
     *	@return AD_User_ID
     */
    public int getDoc_User_ID() {
        return getCreatedBy();
    }	//	getDoc_User_ID
    
    /**
     * 	Get Document Currency
     *	@return C_Currency_ID
     */
    public int getC_Currency_ID() {
        //	MPriceList pl = MPriceList.get(getCtx(), getM_PriceList_ID());
        //	return pl.getC_Currency_ID();
        return 0;
    }
    
    /**
     * 	Get Document Approval Amount
     *	@return amount
     */
    public BigDecimal getApprovalAmt() {
        return new  BigDecimal(0);
    }	//	getApprovalAmt
    
    /**
     * 	Create PDF
     *	@return File or null
     */
    public File createPDF() {
        try {
            File temp = File.createTempFile(get_TableName()+get_ID()+"_", ".pdf");
            return createPDF(temp);
        } catch (Exception e) {
            log.severe("Could not create PDF - " + e.getMessage());
        }
        return null;
    }	//	getPDF
    
    /**
     * 	Create PDF file
     *	@param file output file
     *	@return file if success
     */
    public File createPDF(File file) {
        ReportEngine re = ReportEngine.get(getCtx(), ReportEngine.ORDER, getMPC_Order_ID());
        if (re == null)
            return null;
        return re.getPDF(file);
    }	//	createPDF
    
    /**
     * 	Get Document Info
     *	@return document info (untranslated)
     */
    public String getDocumentInfo() {
        MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
        return dt.getName() + " " + getDocumentNo();
    }	//	getDocumentInfo
    
    public String getDocumentNo() {
        return "";
    }
    
    protected void createnewnode(int node, BigDecimal duration) {
        
        
        //fjv e-evolution Operation Activity Report begin
        try {
            String sqlar="SELECT MPC_Cost_Collector_ID FROM MPC_Cost_Collector WHERE IsActive='Y' AND  MPC_Order_ID="+getMPC_Order_ID() +"  and MPC_Order_Node_ID="+node;
            PreparedStatement pstmtar = DB.prepareStatement(sqlar,null);
            // pstmt.setInt(1, AD_Client_ID);
            //  pstmtar.setInt(1, getMPC_Order_ID());
            //pstmtar.setInt(2, rs1.getInt(2));
            System.out.println("***** SQLar " +sqlar + " variables " +getMPC_Order_ID() +" nodo "+node);
            ResultSet rsar = pstmtar.executeQuery();
            
            //while (!m_calculated && rsplv.next())
            if(rsar.next()) {
                System.out.println("***** NODO Ya Existe");
            } else {
                System.out.println("***** ENTRA AL eLSE ");
                MMPCCostCollector costnew = new MMPCCostCollector(Env.getCtx(),0,get_TrxName());
                costnew.setMPC_Order_ID(getMPC_Order_ID());
                costnew.setC_DocTypeTarget_ID(getC_DocTypeTarget_ID());
                costnew.setC_DocType_ID(getC_DocType_ID());
                costnew.setS_Resource_ID(getS_Resource_ID());
                costnew.setM_Warehouse_ID(getM_Warehouse_ID());
                costnew.setM_Locator_ID(getM_Locator_ID());
                costnew.setM_Product_ID(getM_Product_ID());
                costnew.setM_AttributeSetInstance_ID(getM_AttributeSetInstance_ID());
                costnew.setMPC_Order_Workflow_ID(getMPC_Order_Workflow_ID());
                costnew.setAD_User_ID(getAD_User_ID());
                costnew.setMovementDate(getMovementDate());
                costnew.setDateAcct(getDateAcct());
                costnew.setMPC_Order_Node_ID(node);
                costnew.setMovementQty(getMovementQty());
                // comentado fviejo                costnew.setDurationReal(duration);
                costnew.setDurationUnit(getDurationUnit());
                costnew.setMovementType(getMovementType());
                costnew.save();
                //    costnew.completeIt();
                
            }
            
            rsar.close();
            pstmtar.close();
            
        } catch (SQLException exnode) {
        }
        //completenew(getMPC_Order_ID(),node);
        //fjv e-evolution Operation Activity Report end
        
        
    }
    
    protected boolean beforeSave(boolean newRecord) {
        
        
        
        //fjv e-evolution Operation Activity Report begin
//                                    MMPCOrderNode onodeact =new MMPCOrderNode(Env.getCtx(),getMPC_Order_Node_ID(),null);
//                                        onodeact.setDocStatus("IP");
//                                        onodeact.save();
        if (newRecord)
            setDocStatus("IP");
        
        //fjv e-evolution Operation Activity Report end
        
        return true;
    }
    
    protected boolean afterSave(boolean newRecord, boolean success) {
        
        if (!newRecord)
            return success;
        
        //fjv e-evolution Operation Activity Report begin
            MMPCOrderNode onodeact =new MMPCOrderNode(Env.getCtx(),getMPC_Order_Node_ID(),null);
        // comentado fviejo     onodeact.setDocStatus("IP");
        //onodeact.setAD_WF_Node_ID(getMPC_Order_Workflow_ID());
        onodeact.save();
        
        // setDocStatus("IP");
        
        //fjv e-evolution Operation Activity Report end
        
        return true;
    } //aftersave
    protected void closenew(int order, int node) {
        try {
            String sqlcom="SELECT MPC_Cost_Collector_ID FROM MPC_Cost_Collector WHERE IsActive='Y' AND  MPC_Order_ID="+order;
            PreparedStatement pstmtcom = DB.prepareStatement(sqlcom,null);
            // pstmt.setInt(1, AD_Client_ID);
            //  pstmtar.setInt(1, getMPC_Order_ID());
            //pstmtar.setInt(2, rs1.getInt(2));
            System.out.println("***** SQLar " +sqlcom + " variables " +order +" nodo "+node);
            ResultSet rscom = pstmtcom.executeQuery();
            while(rscom.next()) {
                MDocType doc = new MDocType(Env.getCtx(),getC_DocType_ID(),get_TrxName());
                String doct ="";
                doct=doc.getDocBaseType();
                if(doct.equals("MOA")) {
                    MMPCCostCollector costcoll = new MMPCCostCollector(Env.getCtx(),rscom.getInt(1),get_TrxName());
                    costcoll.setDocStatus("CL");
                    costcoll.setDocAction(DOCACTION_None);
                    costcoll.save();
                }
            }
        } catch (SQLException excom) {
        }
    }
    
    protected void completenew(int order, int node) {
        try {
            String sqlcom="SELECT MPC_Cost_Collector_ID,DocStatus FROM MPC_Cost_Collector WHERE IsActive='Y' AND  MPC_Order_ID="+order;
            PreparedStatement pstmtcom = DB.prepareStatement(sqlcom,null);
            // pstmt.setInt(1, AD_Client_ID);
            //  pstmtar.setInt(1, getMPC_Order_ID());
            //pstmtar.setInt(2, rs1.getInt(2));
            System.out.println("***** SQLar " +sqlcom + " variables " +order +" nodo "+node);
            ResultSet rscom = pstmtcom.executeQuery();
            while(rscom.next()) {
                MDocType doc = new MDocType(Env.getCtx(),getC_DocType_ID(),get_TrxName());
                String doct ="";
                doct=doc.getDocBaseType();
                if(doct.equals("MOA")) {
                    if(!rscom.getString(2).equals("C0") && !rscom.getString(2).equals("CL")) {
                        MMPCCostCollector costcoll = new MMPCCostCollector(Env.getCtx(),rscom.getInt(1),get_TrxName());
                        costcoll.completeIt();
                    }
                }
            }
        } catch (SQLException excom) {
        }
    }
    /** BISion - 25/03/2009 - Santiago Ibañez
	 * 	Get Duration Base in Seconds
	 *	@return duration unit in seconds
	 */
	public long getDurationBaseSec ()
	{
		if (getDurationUnit() == null)
			return 0;
		else if (DURATIONUNIT_Second.equals(getDurationUnit()))
			return 1;
		else if (DURATIONUNIT_Minute.equals(getDurationUnit()))
			return 60;
		else if (DURATIONUNIT_Hour.equals(getDurationUnit()))
			return 3600;
		else if (DURATIONUNIT_Day.equals(getDurationUnit()))
			return 86400;
		else if (DURATIONUNIT_Month.equals(getDurationUnit()))
			return 2592000;
		else if (DURATIONUNIT_Year.equals(getDurationUnit()))
			return 31536000;
		return 0;
	}	//	getDurationBaseSec 
        
        public MMPCOrderQtyReserved getMMPCOrderQtyReserved() throws Exception {
            
            MStorage storage = MStorage.get(getCtx(),getM_Locator_ID() ,getM_Product_ID() , getM_AttributeSetInstance_ID(), get_TrxName());
            
            if (storage == null) {
                throw new Exception("No se pudo obtener Storage para MMPCCostCollector: "+toString());
            }


            MMPCOrderQtyReserved qtyRes = MMPCOrderQtyReserved.get(getCtx(),storage,getMPC_Order_BOMLine_ID(),get_TrxName());
            return qtyRes;
        }
    
}	//	MMovement

