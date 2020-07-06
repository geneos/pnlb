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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.compiere.apps.ProcessParameter;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MDocType;
import org.compiere.model.MLocation;
import org.compiere.model.MLocator;
import org.compiere.model.MMovementConfirm;
import org.compiere.model.MMovementLine;
import org.compiere.model.MMovementLineMA;
import org.compiere.model.MNote;
import org.compiere.model.MPInstance;
import org.compiere.model.MProduct;
import org.compiere.model.MProductCategory;
import org.compiere.model.MSequence;
import org.compiere.model.MStorage;
import org.compiere.model.MTransaction;
import org.compiere.model.MUser;
import org.compiere.model.MWarehouse;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.process.*;
import org.compiere.util.*;
import org.eevolution.process.GenerateRemitosFromMovimiento;

/**
 *	Inventory Movement Model
 *  Clase creada para resolver los problemas de dependencias al generar la version,
 *  ya que esta clase invoca a un proceso (org.eevolution.process.GenerateRemitosFromMovimiento)
 *  @author Santiago Ibañez
 *  @version $Id: MMovement.java,v 1.2 2006/10/09 13:41:00 SIGArg-01 Exp $
 */
public class MMovement extends org.compiere.model.MMovement implements DocAction {

    /**
     * 	Standard Constructor
     *	@param ctx context
     *	@param M_Movement_ID id
     */
    public MMovement(Properties ctx, int M_Movement_ID, String trxName) {
        super(ctx, M_Movement_ID, trxName);
        if (M_Movement_ID == 0) {
            //	setC_DocType_ID (0);
            setDocAction(DOCACTION_Complete);	// CO
            setDocStatus(DOCSTATUS_Drafted);	// DR
            setIsApproved(false);
            setIsInTransit(false);
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
    public MMovement(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }	//	MMovement

    /**
     * 	Complete Document
     * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
     */
    public String completeIt() {
        //	Re-Check
        System.out.println("Clase nueva...");
        if (!m_justPrepared) {
            String status = prepareIt();
            if (!DocAction.STATUS_InProgress.equals(status)) {
                return status;
            }
        }



        //	Outstanding (not processed) Incoming Confirmations ?
        MMovementConfirm[] confirmations = getConfirmations(true);
        for (int i = 0; i < confirmations.length; i++) {
            MMovementConfirm confirm = confirmations[i];
            if (!confirm.isProcessed()) {
                m_processMsg = "Open: @M_MovementConfirm_ID@ - "
                        + confirm.getDocumentNo();
                return DocAction.STATUS_InProgress;
            }
        }

        //	Implicit Approval
        if (!isApproved()) {
            approveIt();
        }
        log.info(toString());

        /**
         * BISion - 04/12/2009 - Santiago Ibañez
         * COM-PAN-BUG-08.003.01
         */
        /*
         * GENEOS - Pablo Velazquez
         * 20/11/2013
         * Se agregan validaciones para movimiento de reservados, creando el siguiente comportamiento:
         * Se ingresa una cantidad, una ubicacion origen y una ubicacion destino.
         * Se valida que la ubicacion destino sea de un almacen de surtimiento.
         * Si se carga la partida se mueve de la partida a la nueva ubicacion.
         * Si no se carga la partida se obtienen todas las partidas por Fefo para esa ubicacion del storage
         * y se mueven a la nueva ubicacion, actualizando el control de reservados. Se chequea disponibilidad
         */
        MMovementLine[] lines = getLines(false);
        if (isQtyReservedMovement()) {
            if (!alcanzaStockReservado()) {
                return DocAction.STATUS_Invalid;
            }
            if (!checkTargetLocator(lines)) {
                return DocAction.STATUS_Invalid;
            }
            if (!moveReserved(lines)) {
                return DocAction.STATUS_Invalid;
            }
        } else {
            if (!alcanzaStock()) {
                return DocAction.STATUS_Invalid;
            }


            for (int i = 0; i < lines.length; i++) {
                MMovementLine line = lines[i];
                MTransaction trxFrom = null;
                if (line.getM_AttributeSetInstance_ID() == 0) {
                    MMovementLineMA mas[] = MMovementLineMA.get(getCtx(),
                            line.getM_MovementLine_ID(), get_TrxName());
                    for (int j = 0; j < mas.length; j++) {
                        MMovementLineMA ma = mas[j];
                        //
                        MStorage storageFrom = MStorage.get(getCtx(), line.getM_Locator_ID(),
                                line.getM_Product_ID(), ma.getM_AttributeSetInstance_ID(), get_TrxName());
                        if (storageFrom == null) {
                            storageFrom = MStorage.getCreate(getCtx(), line.getM_Locator_ID(),
                                    line.getM_Product_ID(), ma.getM_AttributeSetInstance_ID(), get_TrxName());
                        }
                        //
                        MStorage storageTo = MStorage.get(getCtx(), line.getM_LocatorTo_ID(),
                                line.getM_Product_ID(), ma.getM_AttributeSetInstance_ID(), get_TrxName());
                        if (storageTo == null) {
                            storageTo = MStorage.getCreate(getCtx(), line.getM_LocatorTo_ID(),
                                    line.getM_Product_ID(), ma.getM_AttributeSetInstance_ID(), get_TrxName());
                        }
                        //
                        storageFrom.setQtyOnHand(storageFrom.getQtyOnHand().subtract(ma.getMovementQty()));
                        if (!storageFrom.save(get_TrxName())) {
                            m_processMsg = "Storage From not updated (MA)";
                            return DocAction.STATUS_Invalid;
                        }
                        //
                        storageTo.setQtyOnHand(storageTo.getQtyOnHand().add(ma.getMovementQty()));
                        if (!storageTo.save(get_TrxName())) {
                            m_processMsg = "Storage To not updated (MA)";
                            return DocAction.STATUS_Invalid;
                        }

                        //
                        trxFrom = new MTransaction(getCtx(), MTransaction.MOVEMENTTYPE_MovementFrom,
                                line.getM_Locator_ID(), line.getM_Product_ID(), ma.getM_AttributeSetInstance_ID(),
                                ma.getMovementQty().negate(), getMovementDate(), get_TrxName());
                        trxFrom.setM_MovementLine_ID(line.getM_MovementLine_ID());
                        if (!trxFrom.save()) {
                            m_processMsg = "Transaction From not inserted (MA)";
                            return DocAction.STATUS_Invalid;
                        }
                        //
                        MTransaction trxTo = new MTransaction(getCtx(), MTransaction.MOVEMENTTYPE_MovementTo,
                                line.getM_LocatorTo_ID(), line.getM_Product_ID(), ma.getM_AttributeSetInstance_ID(),
                                ma.getMovementQty(), getMovementDate(), get_TrxName());
                        trxTo.setM_MovementLine_ID(line.getM_MovementLine_ID());
                        if (!trxTo.save()) {
                            m_processMsg = "Transaction To not inserted (MA)";
                            return DocAction.STATUS_Invalid;
                        }

                        /*
                         **  Vit4B - 20/04/2007
                         **
                         **  Emisi�n de un aviso a compras para notificar el rechazo de material para tomar las
                         **  medidas correspondientes.
                         **
                         **  Debe enviarse un mensaje para cada usuario de compras ... lo ideal seria mandarselo a todos los usuarios del rol compras ...
                         **
                         */

                        MProduct product = new MProduct(Env.getCtx(), line.getM_Product_ID(), get_TrxName());


                        int LocatorTo = line.getM_LocatorTo_ID();

                        if (isRejection(LocatorTo)) {
                            sendMessageRejection(product, getMovementDate(), ma.getM_AttributeSetInstance_ID());

                        }

                    }
                }
                //	Fallback - We have ASI
                if (trxFrom == null) {
                    MStorage storageFrom = MStorage.get(getCtx(), line.getM_Locator_ID(),
                            line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(), get_TrxName());
                    if (storageFrom == null) {
                        storageFrom = MStorage.getCreate(getCtx(), line.getM_Locator_ID(),
                                line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(), get_TrxName());
                    }
                    //
                    MStorage storageTo = MStorage.get(getCtx(), line.getM_LocatorTo_ID(),
                            line.getM_Product_ID(), line.getM_AttributeSetInstanceTo_ID(), get_TrxName());
                    if (storageTo == null) {
                        storageTo = MStorage.getCreate(getCtx(), line.getM_LocatorTo_ID(),
                                line.getM_Product_ID(), line.getM_AttributeSetInstanceTo_ID(), get_TrxName());
                    }
                    //
                    storageFrom.setQtyOnHand(storageFrom.getQtyOnHand().subtract(line.getMovementQty()));
                    if (!storageFrom.save(get_TrxName())) {
                        m_processMsg = "Storage From not updated";
                        return DocAction.STATUS_Invalid;
                    }
                    //
                    storageTo.setQtyOnHand(storageTo.getQtyOnHand().add(line.getMovementQty()));
                    if (!storageTo.save(get_TrxName())) {
                        m_processMsg = "Storage To not updated";
                        return DocAction.STATUS_Invalid;
                    }

                    //
                    trxFrom = new MTransaction(getCtx(), MTransaction.MOVEMENTTYPE_MovementFrom,
                            line.getM_Locator_ID(), line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
                            line.getMovementQty().negate(), getMovementDate(), get_TrxName());
                    trxFrom.setM_MovementLine_ID(line.getM_MovementLine_ID());
                    if (!trxFrom.save()) {
                        m_processMsg = "Transaction From not inserted";
                        return DocAction.STATUS_Invalid;
                    }
                    //
                    MTransaction trxTo = new MTransaction(getCtx(), MTransaction.MOVEMENTTYPE_MovementTo,
                            line.getM_LocatorTo_ID(), line.getM_Product_ID(), line.getM_AttributeSetInstanceTo_ID(),
                            line.getMovementQty(), getMovementDate(), get_TrxName());
                    trxTo.setM_MovementLine_ID(line.getM_MovementLine_ID());
                    if (!trxTo.save()) {
                        m_processMsg = "Transaction To not inserted";
                        return DocAction.STATUS_Invalid;
                    }

                    /*
                     **  Vit4B - 20/04/2007
                     **
                     **  Emision de un aviso a compras para notificar el rechazo de material para tomar las
                     **  medidas correspondientes.
                     **
                     **  Debe enviarse un mensaje para cada usuario de compras ... lo ideal seria mandarselo a todos los usuarios del rol compras ...
                     **
                     */

                    MProduct product = new MProduct(Env.getCtx(), line.getM_Product_ID(), get_TrxName());


                    int LocatorTo = line.getM_LocatorTo_ID();

                    if (isRejection(LocatorTo)) {
                        sendMessageRejection(product, getMovementDate(), line.getM_AttributeSetInstance_ID());
                    }
                }	//	Fallback
                        /*
                 * 07/06/2013 Maria Jesus Martin
                 * Agregado para que genere un aviso al sistema por cada movimiento de
                 * rechazo.
                 */
                MProduct product = new MProduct(Env.getCtx(), line.getM_Product_ID(), get_TrxName());
                if (isRechazoMaterial(this.getC_DocType_ID())) {
                    try {
                        enviarMensajeRechazoMaterial(product, getMovementDate(), get_TrxName(), line.getM_AttributeSetInstance_ID(), line.getMovementQty(), line.getM_LocatorTo_ID());
                    } catch (SQLException ex) {
                        System.out.println("No se emitieron avisos");
                    }

                }

                //Si el movimiento es por liberacion de producto terminado valido el campo LOTE ANDREANI
                //Y lo traslado a la partida.
                if (getC_DocType_ID() == 5000030 && line.getM_AttributeSetInstanceTo_ID() != 0) {
                    if (line.getLoteAndreani() == null || line.getLoteAndreani().equals("")) {
                        m_processMsg = "Error en linea: " + line + ". Lote andreani obligatorio";
                        return DocAction.STATUS_Invalid;
                    }
                    MAttributeSetInstance masi = new MAttributeSetInstance(getCtx(), line.getM_AttributeSetInstanceTo_ID(), get_TrxName());
                    masi.setLoteAndreani(line.getLoteAndreani());
                    if (!masi.save()) {
                        m_processMsg = "Error en linea: " + line + ". No se pudo actualizar lote andreani en partida";
                        return DocAction.STATUS_Invalid;
                    }
                }
            }	//	for all lines
        }
        //	User Validation
        String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
        if (valid != null) {
            m_processMsg = valid;
            return DocAction.STATUS_Invalid;
        }
        //Genero remito solo si el tipo de documento es Movimiento de Producto terminado
        if (getDocTypeName().equals("Transfer a Distribuidor") || isQtyReservedMovement()) {
            generarRemito();
        }
        //
        setProcessed(true);
        setDocAction(DOCACTION_Close);
        /**
         * BISion - 07/12/2009 - Santiago Ibañez
         * COM-PAN-REQ-09.006.01
         */
        asignarNumeroDocumento();

        /**
         * BISion - 07/12/2009 - Santiago Ibañez
         * COM-PAN-REQ-09.006.01
         */
        MDocType dt = new MDocType(getCtx(), getC_DocType_ID(), get_TrxName());

        if (dt.getName().equals("Movimiento de Aprobacion de Material")
                || dt.getName().equals("Movimiento por Aprobacion de Producto Terminado")
                || dt.getName().equals("Movimiento por Liberación de Producto Terminado")
                || dt.getName().equals("Movimiento por No Liberación de Producto Terminado")) {
            /*
             *  17/07/2013 Maria Jesus Martin
             *  Modificación para que el tipo de Documento "Movimiento de Producto Terminado"
             *  no emita avisos al sistema.(se comenta la linea)
             */
            //|| dt.getName().equals("Movimiento de Producto Terminado")){
            //para cada linea envio un mje
            MMovementLine[] lineas = getLines(true);
            for (int i = 0; i < lineas.length; i++) {
                try {
                    enviarMensajeAprobado(lineas[i].getM_Product_ID(), lineas[i].getM_AttributeSetInstanceTo_ID(), lineas[i].getMovementQty(), lineas[i].getM_LocatorTo_ID(), lineas[i].getM_Locator_ID());
                } catch (SQLException ex) {
                    System.out.println("No se emitieron avisos");
                }
            }
        }
        return DocAction.STATUS_Completed;
    }	//	completeIt

    protected void enviarMensajeAprobado(int M_Product_ID, int instanceId, BigDecimal qty, int M_LocatorTo_ID, int M_LocatorFrom_ID) throws SQLException {

        //Esta lista es porque un usuario puede tener N roles y evitar que reciba N avisos identicos
        ArrayList lista_usuario = new ArrayList();

        //  Cargo los usuarios de acuerdo a los roles
        //Gestion de control de procesos.
        ArrayList usuarios = getUsuarios(1000062);
        //Gestion de planeamiento
        usuarios.addAll(getUsuarios(1000063));
        //Gestion de Inventarios
        usuarios.addAll(getUsuarios(1000068));
        //Gestion de Manufactura
        usuarios.addAll(getUsuarios(1000069));
        //Gestion de abastecimientos
        usuarios.addAll(getUsuarios(1000060));
        //Gestion de Costos
        usuarios.addAll(getUsuarios(1000079));
        //Pago a proveedores
        usuarios.addAll(getUsuarios(1000075));
        //Supervision de la gestion
        usuarios.addAll(getUsuarios(5000006));
        //Supervision de la Gestion de Planeamiento
        usuarios.addAll(getUsuarios(5000005));
        //Supervision de la Gestion de abastecimientos
        usuarios.addAll(getUsuarios(1000055));
        //Supervision de la Gestion de Inventarios
        usuarios.addAll(getUsuarios(1000065));
        //Supervision de la Gestion de Manufactura
        usuarios.addAll(getUsuarios(1000070));
        //Supervision de la Gestion de Control de Procesos
        usuarios.addAll(getUsuarios(1000073));


        Integer[] users = new Integer[usuarios.size()];
        usuarios.toArray(users);
        int idMessageLiberacion = getIDMessage("Liberación de Producto Terminado");
        int idMessageNoLiberacion = getIDMessage("No Liberación de Producto Terminado");
        for (int i = 0; i < users.length; i++) {
            if (!lista_usuario.contains(users[i])) {
                lista_usuario.add(new Integer(users[i]));
                MProduct p = new MProduct(p_ctx, M_Product_ID, get_TrxName());
                String titulo = getMensajeByCategoria(p.getM_Product_Category_ID());
                MAttributeSetInstance asi = new MAttributeSetInstance(p_ctx, instanceId, get_TrxName());
                MLocator locTo = new MLocator(p_ctx, M_LocatorTo_ID, get_TrxName());
                MLocator locFrom = new MLocator(p_ctx, M_LocatorFrom_ID, get_TrxName());
                String TextMsg = "Se ha aprobado el producto " + p.getValue() + " " + p.getName() + " su partida " + asi.getDescription() + " con una cantidad de " + qty + " en el deposito " + locTo.getValue() + " desde el deposito " + locFrom.getValue();
                MDocType dt = new MDocType(getCtx(), getC_DocType_ID(), get_TrxName());
                MNote note = null;
                if (dt.getName().equals("Movimiento por Aprobacion de Producto Terminado")) {
                    note = new MNote(Env.getCtx(), 1000038, users[i], MMovement.getTableId(MMovement.Table_Name), this.getM_Movement_ID(), "Codigo de Producto: " + p.getValue(), TextMsg, null);
                    note.setDescription("Movimiento de material por Aprobación de Producto Terminado.");
                } else if (dt.getName().equals("Movimiento de Aprobacion de Material")) {
                    note = new MNote(Env.getCtx(), 1000042, users[i], MMovement.getTableId(MMovement.Table_Name), this.getM_Movement_ID(), "Codigo de Producto: " + p.getValue(), TextMsg, null);
                    note.setDescription("Movimiento de material por Aprobación.");

                } else if (dt.getName().equals("Movimiento por Liberación de Producto Terminado")) {
                    TextMsg = "Se ha liberado el producto " + p.getValue() + " " + p.getName() + " su partida " + asi.getDescription() + " con una cantidad de " + qty + " en el deposito " + locTo.getValue() + " desde el deposito " + locFrom.getValue();
                    note = new MNote(Env.getCtx(), idMessageLiberacion, users[i], MMovement.getTableId(MMovement.Table_Name), this.getM_Movement_ID(), "Codigo de Producto: " + p.getValue(), TextMsg, null);
                    note.setDescription("Movimiento por Liberación de Producto Terminado.");
                } else if (dt.getName().equals("Movimiento por No Liberación de Producto Terminado")) {
                    TextMsg = "No se ha liberado el producto " + p.getValue() + " " + p.getName() + " su partida " + asi.getDescription() + " con una cantidad de " + qty + " en el deposito " + locTo.getValue() + " desde el deposito " + locFrom.getValue();
                    note = new MNote(Env.getCtx(), idMessageNoLiberacion, users[i], MMovement.getTableId(MMovement.Table_Name), this.getM_Movement_ID(), "Codigo de Producto: " + p.getValue(), TextMsg, null);
                    note.setDescription("Movimiento por no Liberación de Producto Terminado.");
                }
                /*
                 *  03/06/2013 Maria Jesus
                 *  Se agrega para que guarde en el log en caso de que no pueda guardar la nota.
                 */
                if (!note.save()) {
                    log.log(Level.SEVERE, "No se pudo guardar la Nota: " + "Se ha aprobado el producto " + p.getValue() + " " + p.getName() + " su partida " + asi.getDescription() + " con una cantidad de " + qty + " en el deposito " + locTo.getValue() + " desde el deposito " + locFrom.getValue());
                }
            }
        }

    }

    /** BISion - 01/06/2009 - Santiago Ibañez
     * Metodo realizado para invocar al proceso de generar remitos
     * a partir de este movimiento de materiales
     */
    private boolean generarRemito() {
        //Pregunta si quiere generar un Remito a partir de este movimiento
        Object[] options = {"SI", "NO"};
        int n = JOptionPane.showOptionDialog(null,
                "�Crear Remito a partir de este Movimiento? ",
                "Crear Remito",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        //Si se eligio la primera opcion, entonces hay que crear Remito
        if (n == 0) {
            System.out.println("Generando remito desde este movimiento...2.0");
            //Identificador del proceso de generar remitos
            int AD_Process_ID = 0;
            //Obtengo el proceso de generar remitos
            PreparedStatement ps = DB.prepareStatement("SELECT AD_Process_ID FROM AD_PROCESS WHERE classname LIKE 'org.eevolution.process.GenerateRemitosFromMovimiento'", get_TrxName());
            try {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    AD_Process_ID = rs.getInt(1);
                }
            } catch (Exception e) {
                System.out.println("MMovement.generarRemito: " + e.getMessage());
                AD_Process_ID = 0;
            }
            if (AD_Process_ID != 0) {
                //Obtengo la informacion del proceso de generar remitos
                ProcessInfo info = new ProcessInfo("Generar Remito", AD_Process_ID);
                //obtengo la transaccion del movimiento
                Trx trx = Trx.get(get_TrxName(), true);
                //Ejecuto el proceso
                ejecutarProceso(info);
                //ProcessCtl.process(null, 0, info, trx);
            } //No existe el proceso
            else {
                JOptionPane.showMessageDialog(null, "El proceso Generar Remitos a partir de un movimiento no existe", "Proceso inexistente", JOptionPane.ERROR_MESSAGE);
                //VERIFICAR SI SE DEBE COMPLETAR EL MOVIMIENTO A PESAR DE TODO O NO.
                return false;
            }
        }
        /*
        proceso.startProcess(p_ctx, info, trx);*/
        return true;
    }

    /** BISion - 02/06/2009- Santiago Ibañez
     * Metodo que permite el ingreso de parametros y luego ejecuta el proceso
     * @param pinfo
     */
    private void ejecutarProceso(ProcessInfo pinfo) {
        MPInstance instance = new MPInstance(Env.getCtx(), pinfo.getAD_Process_ID(), pinfo.getRecord_ID());
        if (!instance.save()) {
            pinfo.setSummary(Msg.getMsg(Env.getCtx(), "ProcessNoInstance"));
            pinfo.setError(true);
            return;
        }
        pinfo.setAD_PInstance_ID(instance.getAD_PInstance_ID());
        ProcessParameter param = new ProcessParameter(null, 1, pinfo);
        param.initDialog();
        param.setVisible(true);
        GenerateRemitosFromMovimiento proceso = new GenerateRemitosFromMovimiento();
        proceso.setParamFromOut(getM_Movement_ID(), getVALORIZACION());
        proceso.startProcess(p_ctx, pinfo, Trx.get(get_TrxName(), false));
    }

    /** BISion - 10/06/2009 - Santiago Ibañez
     * Retorna el nombre del doctype asociado a este movimiento
     * @return
     */
    private String getDocTypeName() {
        String sql = "select name from c_doctype where c_doctype_id = " + getC_DocType_ID();
        PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (Exception e) {
            System.out.println("No se pudo econtrar el doctype asociado. " + e.getMessage());
        }
        return "";
    }

    /** BISion - 10/06/2009 - Santiago Ibañez
     * Retorna el id del message asociado a este movimiento
     * @return
     */
    private int getIDMessage(String message) {
        String sql = "select ad_message_id from ad_message where value = '" + message + "'";
        PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("No se pudo econtrar el ad_message_id asociado. " + e.getMessage());
        }
        return 0;
    }

    /** BISion - 04/12/2009 - santiago Ibañez
     * Metodo que comprueba si cada una de las lineas del movimiento pueden ser
     * cubiertas con el stock existente.
     * @return
     */
    private boolean alcanzaStock() {
        MMovementLine lines[] = getLines(false);
        for (int i = 0; i < lines.length; i++) {
            if (!alcanzaStockProducto(lines[i].getM_Product_ID(), lines[i].getM_AttributeSetInstance_ID(), lines[i].getM_Locator_ID())) {
                MProduct p = new MProduct(getCtx(), lines[i].getM_Product_ID(), get_TrxName());
                MAttributeSetInstance asi = new MAttributeSetInstance(getCtx(), lines[i].getM_AttributeSetInstance_ID(), get_TrxName());
                JOptionPane.showMessageDialog(null, "Stock insuficiente", "No hay stock suficiente para mover: \nPoducto: " + p.getValue() + "\nPartida: " + asi.getDescription(), JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
        }
        return true;
    }

    /* GENEOS - Pablo Velazquez
     * 20/11/2013
     * Metodo que comprueba si cada una de las lineas del movimiento pueden ser
     * cubiertas con el stock de reservados existente. (Solo para movimiento de reservados)
     */
    private boolean alcanzaStockReservado() {
        MMovementLine lines[] = getLines(false);
        for (int i = 0; i < lines.length; i++) {
            if (!alcanzaStockReservadoProducto(lines[i].getM_Product_ID(), lines[i].getM_AttributeSetInstance_ID(), lines[i].getM_Locator_ID())) {
                MProduct p = new MProduct(getCtx(), lines[i].getM_Product_ID(), get_TrxName());
                MAttributeSetInstance asi = new MAttributeSetInstance(getCtx(), lines[i].getM_AttributeSetInstance_ID(), get_TrxName());
                JOptionPane.showMessageDialog(null, "Stock Reservado insuficiente", "No hay stock reservado suficiente para mover: \nPoducto: " + p.getValue() + "\nPartida: " + asi.getDescription(), JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
        }
        return true;
    }

    /**
     * BISion - 04/12/2009 - Santiago Ibañez
     * Metodo que comprueba si alcanza la cantidad existente para cubrir lo necesario
     * @param M_Product_ID
     * @param M_AttributeSetInstance_ID
     * @param M_Locator_ID
     * @return
     */
    private boolean alcanzaStockProducto(int M_Product_ID, int M_AttributeSetInstance_ID, int M_Locator_ID) {
        if (getCantidadNecesaria(M_Product_ID, M_AttributeSetInstance_ID, M_Locator_ID).compareTo(getQtyDisponible(M_Product_ID, M_AttributeSetInstance_ID, M_Locator_ID, get_TrxName())) > 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean alcanzaStockReservadoProducto(int M_Product_ID, int M_AttributeSetInstance_ID, int M_Locator_ID) {
        if (getCantidadNecesaria(M_Product_ID, M_AttributeSetInstance_ID, M_Locator_ID).compareTo(getQtyDisponibleReservada(M_Product_ID, M_AttributeSetInstance_ID, M_Locator_ID, get_TrxName())) > 0) {
            return false;
        } else {
            return true;
        }
    }

    /** BISion - 04/12/2009 - Santiago Ibañez
     * Dado un producto y su partida retorna la cantidad total que se necesita
     * considerando otras lineas del mismo movimiento
     * @param M_Product_ID
     * @param M_AttributeSetInstance_ID
     * @return
     */
    private BigDecimal getCantidadNecesaria(int M_Product_ID, int M_AttributeSetInstance_ID, int M_Locator_ID) {
        BigDecimal qty = BigDecimal.ZERO;
        MMovementLine lines[] = getLines(false);
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].getM_Product_ID() == M_Product_ID
                    && lines[i].getM_AttributeSetInstance_ID() == M_AttributeSetInstance_ID
                    && M_Locator_ID == lines[i].getM_Locator_ID()) {
                qty = qty.add(lines[i].getMovementQty());
            }
        }
        return qty;
    }

    private void asignarNumeroDocumento() {
        MDocType dt = new MDocType(getCtx(), getC_DocType_ID(), get_TrxName());
        setDocumentNo(MSequence.getDocumentNo(getC_DocType_ID(), get_TrxName()));
    }

    public static BigDecimal getQtyDisponible(int M_Product_ID, int M_AttributeSetInstance_ID, int M_Locator_ID, String trxName) {
        try {
            BigDecimal qty = BigDecimal.ZERO;
            String sql = "select sum(qtyonhand)-sum(qtyreserved) from m_storage "
                    + "where M_Product_ID = ? and M_AttributeSetInstance_ID = ? and M_Locator_ID = ?";
            PreparedStatement ps = DB.prepareStatement(sql, trxName);
            ps.setInt(1, M_Product_ID);
            ps.setInt(2, M_AttributeSetInstance_ID);
            ps.setInt(3, M_Locator_ID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                qty = rs.getBigDecimal(1);
            }
            rs.close();
            ps.close();
            return qty;
        } catch (SQLException ex) {
            Logger.getLogger(MStorage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

    public static BigDecimal getQtyDisponibleReservada(int M_Product_ID, int M_AttributeSetInstance_ID, int M_Locator_ID, String trxName) {
        try {
            BigDecimal qty = BigDecimal.ZERO;
            String sql = "select sum(qtyreserved) from m_storage "
                    + "where M_Product_ID = ? and M_Locator_ID = ?";

            //Si no tengo partida entonces chequeo disponibilidad total
            if (true) {
                sql += " and M_AttributeSetInstance_ID = ? ";
            }

            PreparedStatement ps = DB.prepareStatement(sql, trxName);
            ps.setInt(1, M_Product_ID);
            ps.setInt(2, M_Locator_ID);
            if (true) {
                ps.setInt(3, M_AttributeSetInstance_ID);
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                qty = rs.getBigDecimal(1);
            }
            rs.close();
            ps.close();
            return qty;
        } catch (SQLException ex) {
            Logger.getLogger(MStorage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

    /**
     * BISion - 03/05/2010 - Santiago Ibañez
     * Método que retorna un mensaje dada una categoria de producto
     * @param M_ProductCategory_ID
     * @return Search key del mensaje
     */
    private String getMensajeByCategoria(int M_ProductCategory_ID) {
        MProductCategory pc = new MProductCategory(getCtx(), M_ProductCategory_ID, get_TrxName());
        if (pc.getName().equals("Excipientes")
                || pc.getName().equals("FOLIA")
                || pc.getName().equals("Material de Llenado")
                || pc.getName().equals("Temporal")
                || pc.getName().equals("Material de Acondicionamiento")) {
            return "Aprobacion de Insumos";
        } else if (pc.getName().equals("Granel") || pc.getName().equals("Producto Envasado")) {
            return "Aprobacion de Graneles y Semielaborados";
        }
        if (pc.getName().equals("Original") || pc.getName().equals("Muestra")) {
            return "Aprobacion de Producto Terminado";
        } //
        else {
            return "";
        }
    }

    public boolean isQtyReservedMovement() {
        MDocType dt = new MDocType(getCtx(), getC_DocType_ID(), get_TrxName());
        if (dt.getName().equals("Movimiento de Reservado para Surtimiento")) {
            return true;
        } else {
            return false;
        }

    }

    private boolean checkTargetLocator(MMovementLine[] lines) {
        String msg = "";
        boolean check = true;
        for (int i = 0; i < lines.length; i++) {
            MMovementLine line = lines[i];
            MLocator aLocator = new MLocator(getCtx(), line.getM_LocatorTo_ID(), get_TrxName());
            if (!aLocator.isProductionIssue()) {
                check = false;
                msg += "La linea: " + line.getLine() + " no tiene una ubicacion destino que pertenezca a un almacen de surtimiento\n";
            }
        }
        if (!check) {
            JOptionPane.showMessageDialog(null, "Ubicacion de destino invalida", msg, JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        return check;
    }

    private boolean moveReserved(MMovementLine[] lines) {
        for (int i = 0; i < lines.length; i++) {
            MMovementLine line = lines[i];
            // Si no especifica partida, obtengo por FeFo todas las partidas de esa ubicacion y voy moviendo en cascada
            // los reservados hasta completar 
            if (line.getM_AttributeSetInstance_ID() == 0) {
                BigDecimal qtyTotalResToMove = line.getMovementQty();
                MAttributeSetInstance[] asis = null;//getAllForLocatorFEFO();
                    /*MMovementLineMA mas[] = MMovementLineMA.get(getCtx(),
                line.getM_MovementLine_ID(), get_TrxName());*/
                for (int j = 0; j < asis.length && qtyTotalResToMove.signum() != 0; j++) {
                    MAttributeSetInstance asi = asis[j];


                    //Obtego storage From
                    MStorage storageFrom = MStorage.get(getCtx(), line.getM_Locator_ID(),
                            line.getM_Product_ID(), asi.getM_AttributeSetInstance_ID(), get_TrxName());

                    if (storageFrom == null) {//Nunca puede ser nulo, debe existir si o si!
                        m_processMsg = "Storage From not Found (FeFo)" + " Linea: " + line.getLine();
                        return false;
                    }
                    //Obtengo/Creo storage To
                    MStorage storageTo = MStorage.getCreate(getCtx(), line.getM_LocatorTo_ID(),
                            line.getM_Product_ID(), asi.getM_AttributeSetInstance_ID(), get_TrxName());

                    BigDecimal qtyResAvailable = storageFrom.getQtyReserved();
                    BigDecimal qtyResToMove = BigDecimal.ZERO;


                    if (qtyResAvailable.signum() != 0) {
                        //Si quiero mover mas de lo que hay en este storage, muevo lo que me queda
                        if (qtyTotalResToMove.compareTo(qtyResAvailable) > 0) {
                            qtyResToMove = qtyResAvailable;
                        } else {
                            qtyResToMove = qtyTotalResToMove;
                        }

                        //Actualizo Storages
                        if (!moveReservedFromTo(storageFrom, storageTo, qtyResToMove, line)) {
                            return false;
                        }

                        //Decremento la cantidad restante a mover
                        qtyTotalResToMove = qtyTotalResToMove.subtract(qtyResToMove);
                    }
                }
            } //	Si tenemos Partida, entonces muevo solo de esa partida
            else {
                BigDecimal qtyResToMove = line.getMovementQty();

                //Obtego storage From
                MStorage storageFrom = MStorage.get(getCtx(), line.getM_Locator_ID(),
                        line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(), get_TrxName());

                if (storageFrom == null) {//Nunca puede ser nulo, debe existir si o si!
                    m_processMsg = "Storage From not Found" + " Linea: " + line.getLine();
                    return false;
                }
                //Obtengo/Creo storage To
                MStorage storageTo = MStorage.getCreate(getCtx(), line.getM_LocatorTo_ID(),
                        line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(), get_TrxName());

                //Actualizo Storages
                if (!moveReservedFromTo(storageFrom, storageTo, qtyResToMove, line)) {
                    return false;
                }
            }
        }	//	for all lines
        return true;
    }

    private boolean moveReservedFromTo(MStorage storageFrom, MStorage storageTo, BigDecimal qtyResToMove, MMovementLine line) {

        //Chequeo disponibilidad en Storage From
        if (storageFrom.getQtyOnHand().compareTo(qtyResToMove) == -1) {
            m_processMsg = "Cantidad en almacen insuficiente" + " Linea: " + line.getLine();
            return false;
        }
        //Actualizo Storage From
        storageFrom.setQtyOnHand(storageFrom.getQtyOnHand().subtract(qtyResToMove));
        storageFrom.setQtyReserved(storageFrom.getQtyReserved().subtract(qtyResToMove));
        if (!storageFrom.save(get_TrxName())) {
            m_processMsg = "Storage From not updated (FeFo)" + " Linea: " + line.getLine();
            return false;
        }

        //Actualizo Storage To
        storageTo.setQtyOnHand(storageTo.getQtyOnHand().add(qtyResToMove));
        storageTo.setQtyReserved(storageTo.getQtyReserved().add(qtyResToMove));
        if (!storageTo.save(get_TrxName())) {
            m_processMsg = "Storage To not updated (FeFo)" + " Linea: " + line.getLine();
            return false;
        }

        //
        MTransaction trxFrom = new MTransaction(getCtx(), MTransaction.MOVEMENTTYPE_MovementFrom,
                line.getM_Locator_ID(), line.getM_Product_ID(), storageFrom.getM_AttributeSetInstance_ID(),
                qtyResToMove.negate(), getMovementDate(), get_TrxName());
        trxFrom.setM_MovementLine_ID(line.getM_MovementLine_ID());
        if (!trxFrom.save()) {
            m_processMsg = "Transaction From not inserted (FeFo)" + " Linea: " + line.getLine();
            return false;
        }
        //
        MTransaction trxTo = new MTransaction(getCtx(), MTransaction.MOVEMENTTYPE_MovementTo,
                line.getM_LocatorTo_ID(), line.getM_Product_ID(), storageFrom.getM_AttributeSetInstance_ID(),
                qtyResToMove, getMovementDate(), get_TrxName());
        trxTo.setM_MovementLine_ID(line.getM_MovementLine_ID());
        if (!trxTo.save()) {
            m_processMsg = "Transaction To not inserted (FeFo)" + " Linea: " + line.getLine();
            return false;
        }

        if (!updateReserveControl(storageFrom, storageTo, qtyResToMove)) {
            return false;
        }
        return true;
    }

    private boolean updateReserveControl(MStorage storageFrom, MStorage storageTo, BigDecimal qtyResToMove) {
        //Obtengo todos los controles del StorageFrom, priorizando segun fecha en la que se emitio la orden
        MMPCOrderQtyReserved[] resCtrls = MMPCOrderQtyReserved.getAllForStorage(getCtx(), storageFrom, get_TrxName());

        //Actualizo Control de reservados del StorageFrom
        BigDecimal ctrlTotalToMove = qtyResToMove;
        for (int i = 0; i < resCtrls.length; i++) {
            MMPCOrderQtyReserved resCtrlFrom = resCtrls[i];
            BigDecimal ctrlToMove = BigDecimal.ZERO;
            if (ctrlTotalToMove.compareTo(resCtrlFrom.getRemainingQty()) > 0) {
                ctrlToMove = resCtrlFrom.getRemainingQty();
            } else {
                ctrlToMove = ctrlTotalToMove;
            }

            resCtrlFrom.setRemainingQty(resCtrlFrom.getRemainingQty().subtract(ctrlToMove));
            resCtrlFrom.setTotalQty(resCtrlFrom.getTotalQty().subtract(ctrlToMove));
            if (!resCtrlFrom.save()) {
                log.log(Level.SEVERE, "Error al guardar control de reservado: " + resCtrlFrom + "del storage From");
                return false;
            }

            //Obtengo/Creo el control
            MMPCOrderQtyReserved resCtrlTo = MMPCOrderQtyReserved.getCreate(getCtx(), storageTo, resCtrlFrom.getMPC_Order_BOMLine_ID(), get_TrxName());
            resCtrlTo.setRemainingQty(resCtrlTo.getRemainingQty().add(ctrlToMove));
            resCtrlTo.setTotalQty(resCtrlTo.getTotalQty().add(ctrlToMove));
            resCtrlTo.setUseOrder(resCtrlFrom.getUseOrder());
            if (!resCtrlTo.save()) {
                log.log(Level.SEVERE, "Error al guardar control de reservado: " + resCtrlTo + " del storage To");
                return false;
            }

            ctrlTotalToMove = ctrlTotalToMove.subtract(ctrlToMove);

        }

        //Puede sobrar cantidad a reservar, ya que otros documentos pueden reservar en los Storages
        if (ctrlTotalToMove.signum() > 0) {
            log.log(Level.SEVERE, "Se hizo movimiento de reservado, pero hay cantidades reservadas que no estan vinculadas a ordenes");
        }
        return true;
    }
}	//	MMovement

