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
package org.compiere.model;

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.JOptionPane;

import org.compiere.process.*;
import org.compiere.util.*;

/**
 *	Inventory Movement Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MMovement.java,v 1.2 2006/10/09 13:41:00 SIGArg-01 Exp $
 */
public class MMovement extends X_M_Movement implements DocAction {

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
    /**	Lines						*/
    private MMovementLine[] m_lines = null;
    /** Confirmations				*/
    private MMovementConfirm[] m_confirms = null;

    /**
     * 	Get Lines
     *	@param requery requery
     *	@return array of lines
     */
    public MMovementLine[] getLines(boolean requery) {
        if (m_lines != null && !requery) {
            return m_lines;
        }
        //
        ArrayList<MMovementLine> list = new ArrayList<MMovementLine>();
        String sql = "SELECT * FROM M_MovementLine WHERE M_Movement_ID=? ORDER BY Line";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql, get_TrxName());
            pstmt.setInt(1, getM_Movement_ID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new MMovementLine(getCtx(), rs, get_TrxName()));
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, "getLines", e);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }

        m_lines = new MMovementLine[list.size()];
        list.toArray(m_lines);
        return m_lines;
    }	//	getLines

    /**
     * 	Get Confirmations
     * 	@param requery requery
     *	@return array of Confirmations
     */
    public MMovementConfirm[] getConfirmations(boolean requery) {
        if (m_confirms != null && !requery) {
            return m_confirms;
        }

        ArrayList<MMovementConfirm> list = new ArrayList<MMovementConfirm>();
        String sql = "SELECT * FROM M_MovementConfirm WHERE M_Movement_ID=?";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql, get_TrxName());
            pstmt.setInt(1, getM_Movement_ID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new MMovementConfirm(getCtx(), rs, get_TrxName()));
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, "getConfirmations", e);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }

        m_confirms = new MMovementConfirm[list.size()];
        list.toArray(m_confirms);
        return m_confirms;
    }	//	getConfirmations

    /**
     * 	Add to Description
     *	@param description text
     */
    public void addDescription(String description) {
        String desc = getDescription();
        if (desc == null) {
            setDescription(description);
        } else {
            setDescription(desc + " | " + description);
        }
    }	//	addDescription

    /**
     * 	Get Document Info
     *	@return document info (untranslated)
     */
    public String getDocumentInfo() {
        MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
        return dt.getName() + " " + getDocumentNo();
    }	//	getDocumentInfo

    /**
     * 	Create PDF
     *	@return File or null
     */
    public File createPDF() {
        try {
            File temp = File.createTempFile(get_TableName() + get_ID() + "_", ".pdf");
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
        //	ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.INVOICE, getC_Invoice_ID());
        //	if (re == null)
        return null;
        //	return re.getPDF(file);
    }	//	createPDF

    /**
     * 	Before Save
     *	@param newRecord new
     *	@return true
     */
    protected boolean beforeSave(boolean newRecord) {



        //El metdo completeIt vuelve a llamar a este metodo, por eso se agrega esta comprobacion
        if (getDocStatus().equals(DOCSTATUS_Drafted)) {
            setDocumentNo("PENDIENTE");
        }

        if (getC_DocType_ID() == 0) {
            MDocType types[] = MDocType.getOfDocBaseType(getCtx(), MDocType.DOCBASETYPE_MaterialMovement);
            if (types.length > 0) //	get first
            {
                setC_DocType_ID(types[0].getC_DocType_ID());
            } else {
                log.saveError("Error", Msg.parseTranslation(getCtx(), "@NotFound@ @C_DocType_ID@"));
                return false;
            }

        }
        return true;
    }	//	beforeSave

    /**
     * 	Set Processed.
     * 	Propergate to Lines/Taxes
     *	@param processed processed
     */
    public void setProcessed(boolean processed) {
        super.setProcessed(processed);
        if (get_ID() == 0) {
            return;
        }
        String sql = "UPDATE M_MovementLine SET Processed='"
                + (processed ? "Y" : "N")
                + "' WHERE M_Movement_ID=" + getM_Movement_ID();
        int noLine = DB.executeUpdate(sql, get_TrxName());
        m_lines = null;
        log.fine("Processed=" + processed + " - Lines=" + noLine);
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
    protected String m_processMsg = null;
    /**	Just Prepared Flag			*/
    protected boolean m_justPrepared = false;

    /**
     * 	Unlock Document.
     * 	@return true if success 
     */
    public boolean unlockIt() {
        log.info(toString());
        setProcessing(false);
        return true;
    }	//	unlockIt

    /**
     * 	Invalidate Document
     * 	@return true if success 
     */
    public boolean invalidateIt() {
        log.info(toString());
        setDocAction(DOCACTION_Prepare);
        return true;
    }	//	invalidateIt

    /**
     * 28-07-2011 Camarzana Mariano
     * Metodo empleado para verificar que no exista un flujo de orden de venta activo, ya que se 
     * producian deadlock con el transfer al generar los remitos corrspondientes 
     * @return
     */
    private int get() {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT record_id FROM AD_WF_Activity WHERE AD_Table_ID=  " + MOrder.getTableId(MOrder.Table_Name)
                + " AND UPDATED like getdate() "
                + " AND Processed<>'Y' ORDER BY AD_WF_Activity_ID";
        try {
            pstmt = DB.prepareStatement(sql, null);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                rs.close();
                pstmt.close();
                rs = null;
                pstmt = null;
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     *	Prepare Document
     * 	@return new status (In Progress or Invalid) 
     */
    public String prepareIt() {

        /*
         *28-07-2011 Camarzana Mariano
         *Verifico que no haya un flujo de Orden de Venta activo 
         */
        int registro = get();
        if (registro > 0) {
            JOptionPane.showMessageDialog(null, "Existe un Flujo de Orden de Venta Activo Nro. Registro = " + registro + ", Intente nuevamente", "ERROR ", JOptionPane.ERROR_MESSAGE);
            return DocAction.STATUS_Invalid;
        }

        log.info(toString());
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
        if (m_processMsg != null) {
            return DocAction.STATUS_Invalid;
        }
        MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());

        //	Std Period open?
        if (!MPeriod.isOpen(getCtx(), getMovementDate(), dt.getDocBaseType())) {
            m_processMsg = "@PeriodClosed@";
            return DocAction.STATUS_Invalid;
        }
        MMovementLine[] lines = getLines(false);
        if (lines.length == 0) {
            m_processMsg = "@NoLines@";
            return DocAction.STATUS_Invalid;
        }

        /*
         * GENEOS - Pablo Velazquez
         * 27/11/2013
         * Se agregan validaciones sobre los movimientos para:
         * Rol: Que el rol actual pueda efectuar ese movimiento
         * Almacen desde: Que el movimiento pueda sacar de ese almacen
         * Almacen A: Que el movimiento pueda mover a ese almacen
         */

        //Check Role
        String Role = getCtx().getProperty("#AD_Role_ID");
        MRole role = new MRole(getCtx(), Integer.parseInt(Role), get_TrxName());
        if (!MZYNDocTypeRolAct.roleHasPermission(getCtx(), dt.getC_DocType_ID(), role.getAD_Role_ID(), get_TrxName())) {
            m_processMsg = "Sin Permiso";
            JOptionPane.showMessageDialog(null, "El Rol " + role.getName() + " no puede realizar este tipo de movimientos", "Error: Sin permiso", JOptionPane.ERROR_MESSAGE);
            return DocAction.STATUS_Invalid;
        }

        //Me fijo que tipo de restricciones existen sobre el movimiento para no chequear innecesariamente
        boolean checkWarehouseFrom = MZYNMovementWarehouse.hasWarehouseFromRestriction(getCtx(), dt.getC_DocType_ID(), get_TrxName());
        boolean checkWarehouseTo = MZYNMovementWarehouse.hasWarehouseToRestriction(getCtx(), dt.getC_DocType_ID(), get_TrxName());

        for (int i = 0; i < lines.length; i++) {
            //Check Warehouse from 
            if (checkWarehouseFrom) {
                MLocator locator = new MLocator(getCtx(), lines[i].getM_Locator_ID(), get_TrxName());
                if (!MZYNMovementWarehouse.isWarehouseFrom(getCtx(), dt.getC_DocType_ID(), locator.getM_Warehouse_ID(), get_TrxName())) {
                    m_processMsg = "Sin Permiso";
                    JOptionPane.showMessageDialog(null, "Para este movimiento no se puede mover desde la ubicaćion: " + locator, "Error: Sin permiso", JOptionPane.ERROR_MESSAGE);
                    return DocAction.STATUS_Invalid;
                }

            }

            if (checkWarehouseTo) {
                MLocator locator = new MLocator(getCtx(), lines[i].getM_LocatorTo_ID(), get_TrxName());
                if (!MZYNMovementWarehouse.isWarehouseTo(getCtx(), dt.getC_DocType_ID(), locator.getM_Warehouse_ID(), get_TrxName())) {
                    m_processMsg = "Sin Permiso";
                    JOptionPane.showMessageDialog(null, "Para este movimiento no se puede mover hacia la ubicaćion: " + locator, "Error: Sin permiso", JOptionPane.ERROR_MESSAGE);
                    return DocAction.STATUS_Invalid;
                }

            }

            if (lines[i].getM_AttributeSetInstance_ID() != 0) {
                continue;
            }
            MProduct product = new MProduct(Env.getCtx(), lines[i].getM_Product_ID(), get_TrxName());
            if (product != null) {
                System.out.println("***** MASI mandatory product");
                int M_AttributeSet_ID = product.getM_AttributeSet_ID();
                if (M_AttributeSet_ID != 0) {
                    System.out.println("***** MASI mandatory mas");
                    MAttributeSet mas = MAttributeSet.get(getCtx(), M_AttributeSet_ID);
                    if (mas != null
                            && (mas.isMandatory()) //	|| (!isSOTrx() && mas.isMandatoryAlways())) 
                            ) {
                        System.out.println("***** MASI mandatory invalid");
                        m_processMsg = "@M_AttributeSet_ID@ @IsMandatory@";
                        return DocAction.STATUS_Invalid;
                    }
                }
            }
        }
        //fjviejo e-evolution end
        //	Add up Amounts



        /*
         * Geneos - 22/05/2013 - Pablo Velazquez
         * Check Guarantee Date for Products
         */

        if (dt.getName().equals("Movimiento de Aprobacion de Material")
                || dt.getName().equals("Movimiento por Aprobacion de Producto Terminado")
                || dt.getName().equals("Movimiento de Rechazo de Material")) {
            //For each Line
            int errLines = 0;
            String errMsjPartidas = "";
            for (int i = 0; i < lines.length; i++) {
                //Get Line
                MMovementLine line = lines[i];
                if (line.getM_Product_ID() != 0) {
                    MProduct aProduct = new MProduct(getCtx(), line.getM_Product_ID(), get_TrxName());
                    if (aProduct.getAttributeSet() != null) {
                        MAttributeSet aAttributeSet = aProduct.getAttributeSet();//new MAttributeSet(getCtx(),aProduct.get_ID(),get_TrxName());
                        //If Product need Guarantee Date then check it
                        if (aAttributeSet.isGuaranteeDate()) {
                            int mAttributeSetInstanceId = line.getM_AttributeSetInstance_ID();
                            if (mAttributeSetInstanceId != 0) {
                                MAttributeSetInstance aAttributeSetInstance = new MAttributeSetInstance(getCtx(), mAttributeSetInstanceId, get_TrxName());
                                if (aAttributeSetInstance.getGuaranteeDate() == null) {
                                    errMsjPartidas += "La Partida: " + aAttributeSetInstance.getDescription() + " debe tener Fecha de vencimiento. \n";
                                    errLines++;
                                }
                            }
                        }
                    }
                }
            }
            if (errLines > 0) {
                JOptionPane.showMessageDialog(null, errMsjPartidas, "Error: Partidas sin vencimiento", JOptionPane.ERROR_MESSAGE);
                m_processMsg = "Partidas sin vencimiento";
                return DocAction.STATUS_Invalid;
            }
        }

        checkMaterialPolicy();

        //	Confirmation
        if (dt.isInTransit()) //createConfirmation();
        {
            m_justPrepared = true;
        }
        if (!DOCACTION_Complete.equals(getDocAction())) {
            setDocAction(DOCACTION_Complete);
        }
        return DocAction.STATUS_InProgress;
    }	//	prepareIt

    /**
     * 	Create Movement Confirmation
     */
    /*private void createConfirmation()
    {
    MMovementConfirm[] confirmations = getConfirmations(false);
    if (confirmations.length > 0)
    return;
    
    //	Create Confirmation
    MMovementConfirm.create (this, false);
    }	//	createConfirmation
    
    /**
     * 	Approve Document
     * 	@return true if success 
     */
    public boolean approveIt() {
        log.info(toString());
        setIsApproved(true);
        return true;
    }	//	approveIt

    /**
     * 	Reject Approval
     * 	@return true if success 
     */
    public boolean rejectIt() {
        log.info(toString());
        setIsApproved(false);
        return true;
    }	//	rejectIt

    /**
     * 	Complete Document
     * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
     */
    public String completeIt() {

        //	Re-Check
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

        MMovementLine[] lines = getLines(false);


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
                    //14-06-2011 Camarzana Mariano Se le agrego el nombre de la transaccion
                    if (!trxFrom.save(get_TrxName())) {
                        m_processMsg = "Transaction From not inserted (MA)";
                        return DocAction.STATUS_Invalid;
                    }
                    //
                    MTransaction trxTo = new MTransaction(getCtx(), MTransaction.MOVEMENTTYPE_MovementTo,
                            line.getM_LocatorTo_ID(), line.getM_Product_ID(), ma.getM_AttributeSetInstance_ID(),
                            ma.getMovementQty(), getMovementDate(), get_TrxName());
                    trxTo.setM_MovementLine_ID(line.getM_MovementLine_ID());
//					14-06-2011 Camarzana Mariano Se le agrego el nombre de la transaccion
                    if (!trxTo.save(get_TrxName())) {
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
                //14-06-2011 Camarzana Mariano Se le agrego el nombre de la transaccion
                if (!trxFrom.save(get_TrxName())) {
                    m_processMsg = "Transaction From not inserted";
                    return DocAction.STATUS_Invalid;
                }
                //
                MTransaction trxTo = new MTransaction(getCtx(), MTransaction.MOVEMENTTYPE_MovementTo,
                        line.getM_LocatorTo_ID(), line.getM_Product_ID(), line.getM_AttributeSetInstanceTo_ID(),
                        line.getMovementQty(), getMovementDate(), get_TrxName());
                trxTo.setM_MovementLine_ID(line.getM_MovementLine_ID());
                //14-06-2011 Camarzana Mariano Se le agrego el nombre de la transaccion
                if (!trxTo.save(get_TrxName())) {
                    m_processMsg = "Transaction To not inserted";
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
                    sendMessageRejection(product, getMovementDate(), line.getM_AttributeSetInstance_ID());
                }



            }	//	Fallback
        }	//	for all lines
        //	User Validation
        String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
        if (valid != null) {
            m_processMsg = valid;
            return DocAction.STATUS_Invalid;
        }
        //
        setProcessed(true);
        setDocAction(DOCACTION_Close);
        return DocAction.STATUS_Completed;
    }	//	completeIt

    /**
     * 	Check Material Policy
     * 	Sets line ASI
     */
    private void checkMaterialPolicy() {
        int no = MMovementLineMA.deleteMovementMA(getM_Movement_ID(), get_TrxName());
        if (no > 0) {
            log.config("Delete old #" + no);
        }
        MMovementLine[] lines = getLines(false);

        MClient client = MClient.get(getCtx());

        //	Check Lines
        for (int i = 0; i < lines.length; i++) {
            MMovementLine line = lines[i];
            boolean needSave = false;

            //	Attribute Set Instance
            if (line.getM_AttributeSetInstance_ID() == 0) {
                MProduct product = MProduct.get(getCtx(), line.getM_Product_ID());
                MProductCategory pc = MProductCategory.get(getCtx(), product.getM_Product_Category_ID());
                String MMPolicy = pc.getMMPolicy();
                if (MMPolicy == null || MMPolicy.length() == 0) {
                    MMPolicy = client.getMMPolicy();
                }
                //
                MStorage[] storages = MStorage.getAllWithASI(getCtx(),
                        line.getM_Product_ID(), line.getM_Locator_ID(),
                        MClient.MMPOLICY_FiFo.equals(MMPolicy), get_TrxName());
                BigDecimal qtyToDeliver = line.getMovementQty();
                for (int ii = 0; ii < storages.length; ii++) {
                    MStorage storage = storages[ii];
                    if (ii == 0) {
                        if (storage.getQtyOnHand().compareTo(qtyToDeliver) >= 0) {
                            line.setM_AttributeSetInstance_ID(storage.getM_AttributeSetInstance_ID());
                            needSave = true;
                            log.config("Direct - " + line);
                            qtyToDeliver = Env.ZERO;
                        } else {
                            log.config("Split - " + line);
                            MMovementLineMA ma = new MMovementLineMA(line,
                                    storage.getM_AttributeSetInstance_ID(),
                                    storage.getQtyOnHand());
                            if (!ma.save())
								;
                            qtyToDeliver = qtyToDeliver.subtract(storage.getQtyOnHand());
                            log.fine("#" + ii + ": " + ma + ", QtyToDeliver=" + qtyToDeliver);
                        }
                    } else //	 create addl material allocation
                    {
                        MMovementLineMA ma = new MMovementLineMA(line,
                                storage.getM_AttributeSetInstance_ID(),
                                qtyToDeliver);
                        if (storage.getQtyOnHand().compareTo(qtyToDeliver) >= 0) {
                            qtyToDeliver = Env.ZERO;
                        } else {
                            ma.setMovementQty(storage.getQtyOnHand());
                            qtyToDeliver = qtyToDeliver.subtract(storage.getQtyOnHand());
                        }
                        if (!ma.save())
							;
                        log.fine("#" + ii + ": " + ma + ", QtyToDeliver=" + qtyToDeliver);
                    }
                    if (qtyToDeliver.signum() == 0) {
                        break;
                    }
                }	//	 for all storages

                //	No AttributeSetInstance found for remainder
                if (qtyToDeliver.signum() != 0) {
                    MMovementLineMA ma = new MMovementLineMA(line,
                            0, qtyToDeliver);
                    if (!ma.save())
						;
                    log.fine("##: " + ma);
                }
            }	//	attributeSetInstance

            if (needSave && !line.save()) {
                log.severe("NOT saved " + line);
            }
        }	//	for all lines

    }	//	checkMaterialPolicy

    /**
     * 	Void Document.
     * 	@return true if success 
     */
    public boolean voidIt() {
        log.info(toString());
        if (DOCSTATUS_Closed.equals(getDocStatus())
                || DOCSTATUS_Reversed.equals(getDocStatus())
                || DOCSTATUS_Voided.equals(getDocStatus())) {
            m_processMsg = "Document Closed: " + getDocStatus();
            return false;
        }

        //	Not Processed
        if (DOCSTATUS_Drafted.equals(getDocStatus())
                || DOCSTATUS_Invalid.equals(getDocStatus())
                || DOCSTATUS_InProgress.equals(getDocStatus())
                || DOCSTATUS_Approved.equals(getDocStatus())
                || DOCSTATUS_NotApproved.equals(getDocStatus())) {
            //	Set lines to 0
            MMovementLine[] lines = getLines(false);
            for (int i = 0; i < lines.length; i++) {
                MMovementLine line = lines[i];
                BigDecimal old = line.getMovementQty();
                if (old.compareTo(Env.ZERO) != 0) {
                    line.setMovementQty(Env.ZERO);
                    line.addDescription("Void (" + old + ")");
                    line.save(get_TrxName());
                }
            }
        } else {
            return reverseCorrectIt();
        }

        setProcessed(true);
        setDocAction(DOCACTION_None);
        return true;
    }	//	voidIt

    /**
     * 	Close Document.
     * 	@return true if success 
     */
    public boolean closeIt() {
        log.info(toString());

        //	Close Not delivered Qty
        setDocAction(DOCACTION_None);
        return true;
    }	//	closeIt

    /**
     * 	Reverse Correction
     * 	@return false 
     */
    public boolean reverseCorrectIt() {
        log.info(toString());
        MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
        if (!MPeriod.isOpen(getCtx(), getMovementDate(), dt.getDocBaseType())) {
            m_processMsg = "@PeriodClosed@";
            return false;
        }

        //	Deep Copy
        MMovement reversal = new MMovement(getCtx(), 0, get_TrxName());
        copyValues(this, reversal, getAD_Client_ID(), getAD_Org_ID());
        reversal.setDocStatus(DOCSTATUS_Drafted);
        reversal.setDocAction(DOCACTION_Complete);
        reversal.setIsApproved(false);
        reversal.setIsInTransit(false);
        reversal.setPosted(false);
        reversal.setProcessed(false);
        reversal.addDescription("{->" + getDocumentNo() + ")");
        if (!reversal.save()) {
            m_processMsg = "Could not create Movement Reversal";
            return false;
        }

        //	Reverse Line Qty
        MMovementLine[] oLines = getLines(true);
        for (int i = 0; i < oLines.length; i++) {
            MMovementLine oLine = oLines[i];
            MMovementLine rLine = new MMovementLine(getCtx(), 0, get_TrxName());
            copyValues(oLine, rLine, oLine.getAD_Client_ID(), oLine.getAD_Org_ID());
            rLine.setM_Movement_ID(reversal.getM_Movement_ID());
            //
            rLine.setMovementQty(rLine.getMovementQty().negate());
            rLine.setTargetQty(Env.ZERO);
            rLine.setScrappedQty(Env.ZERO);
            rLine.setConfirmedQty(Env.ZERO);
            rLine.setProcessed(false);
            if (!rLine.save()) {
                m_processMsg = "Could not create Movement Reversal Line";
                return false;
            }
        }
        //
        if (!reversal.processIt(DocAction.ACTION_Complete)) {
            m_processMsg = "Reversal ERROR: " + reversal.getProcessMsg();
            return false;
        }
        reversal.closeIt();
        reversal.setDocStatus(DOCSTATUS_Reversed);
        reversal.setDocAction(DOCACTION_None);
        reversal.save();
        m_processMsg = reversal.getDocumentNo();

        //	Update Reversed (this)
        addDescription("(" + reversal.getDocumentNo() + "<-)");
        setProcessed(true);
        setDocStatus(DOCSTATUS_Reversed);	//	may come from void
        setDocAction(DOCACTION_None);
        return true;
    }	//	reverseCorrectionIt

    /**
     * 	Reverse Accrual - none
     * 	@return false 
     */
    public boolean reverseAccrualIt() {
        log.info(toString());
        return false;
    }	//	reverseAccrualIt

    /** 
     * 	Re-activate
     * 	@return false 
     */
    public boolean reActivateIt() {
        log.info(toString());
        return false;
    }	//	reActivateIt

    /*************************************************************************
     * 	Get Summary
     *	@return Summary of Document
     */
    public String getSummary() {
        StringBuffer sb = new StringBuffer();
        sb.append(getDocumentNo());
        //	: Total Lines = 123.00 (#1)
        sb.append(": ").append(Msg.translate(getCtx(), "ApprovalAmt")).append("=").append(getApprovalAmt()).append(" (#").append(getLines(false).length).append(")");
        //	 - Description
        if (getDescription() != null && getDescription().length() > 0) {
            sb.append(" - ").append(getDescription());
        }
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
    }	//	getC_Currency_ID

    /*
     **  Vit4B 24/04/2007
     **  Agregado para manejar si el almacen es un almacen de rechazo razon por la cual el movimiento deber� de genera un aviso a compras
     **
     */
    protected boolean isRejection(int LocatorTo) {


        String sqlCollector = "SELECT IS_REJECTION FROM M_WAREHOUSE w INNER JOIN M_LOCATOR l ON (w.M_WAREHOUSE_ID = l.M_WAREHOUSE_ID) WHERE l.M_LOCATOR_ID = " + LocatorTo;

        try {
            PreparedStatement pstmt1 = DB.prepareStatement(sqlCollector, null);
            ResultSet rs1 = pstmt1.executeQuery();
            if (rs1.next()) {

                String val = rs1.getString("IS_REJECTION");
                if (val.equals("Y")) {
                    rs1.close();
                    pstmt1.close();
                    return true;
                }
            }
            rs1.close();
            pstmt1.close();
        } catch (SQLException obl) {
        }
        return false;
    }


    /*
     **  Vit4B 30/04/2007
     **  Agregado para genera un aviso a todos los usuarios de compras
     **
     */
    protected void sendMessageRejection(MProduct product, Timestamp movementDate, int instanceId) {

        String sqlCollector = "SELECT AD_USER_ID FROM AD_USER_ALERT_MESSAGE WHERE AD_ALERT_MESSAGE_ID = 1000000";

        try {
            PreparedStatement pstmt1 = DB.prepareStatement(sqlCollector, null);
            ResultSet rs1 = pstmt1.executeQuery();
            while (rs1.next()) {
                int val = rs1.getInt("AD_USER_ID");
                String desc = instanceNumber(instanceId);
                String ocNumber = orderNumber(instanceId);
                String ocDate = orderDate(instanceId);
                String ocProv = orderProv(instanceId);
                MNote note = new MNote(Env.getCtx(), 1000035, val, 0, 0, "Codigo de Producto: " + product.getValue(), "Producto: " + product.getName() + "  Partida: " + desc + "  Orden: " + ocNumber + " Fecha de Orden: " + ocDate + " Fecha de Rechazo: " + movementDate.toString() + " Proveedor: " + ocProv, null);
                /*
                 *  03/06/2013 Maria Jesus
                 *  Se agrega para que guarde en el log en caso de que no pueda guardar la nota.
                 */
                if (!note.save()) {
                    log.log(Level.SEVERE, "No se pudo guardar la Nota para el Codigo de Producto: " + product.getValue(), "Producto: " + product.getName() + "  Partida: " + desc + "  Orden: " + ocNumber + " Fecha de Orden: " + ocDate + " Fecha de Rechazo: " + movementDate.toString() + " Proveedor: " + ocProv);
                }
            }
            rs1.close();
            pstmt1.close();
        } catch (SQLException obl) {
        }

    }
    /*
     * 18/07/2013 Maria Jesus Martin
     * Se agrega para verificar si el Movimiento es por rechazo de material. 
     */

    protected boolean isRechazoMaterial(int C_DocType) {
        String doctype = "SELECT C_DocType_ID FROM C_DocType WHERE name like '%Movimiento de Rechazo de Material%'";
        PreparedStatement pstmt = DB.prepareStatement(doctype, null);;
        ResultSet rs;
        try {
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (C_DocType == rs.getInt(1)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MMovement.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /*
     * 07/06/2013 Maria Jesus Martin
     * Se agrega para que si es un rechazo de material se avise al sistema(crea una MNote).
     *
     */

    /*
     * GENEOS - Pablo Velazquez
     * 07/06/2013
     * Se agregan usuarios para que el mensaje le llegue a los mismos, antes no llegaban a ninugn usuario
     */
    protected void enviarMensajeRechazoMaterial(MProduct product, Timestamp movementDate, String trx, int instanceId, BigDecimal qty, int M_LocatorTo_ID) throws SQLException {

        //Esta lista es porque un usuario puede tener N roles y evitar que reciba N avisos identicos
        ArrayList lista_usuario = new ArrayList();

        //          Cargo los usuarios de acuerdo a los roles
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
        for (int i = 0; i < users.length; i++) {
            if (!lista_usuario.contains(users[i])) {
                lista_usuario.add(new Integer(users[i]));
                MAttributeSetInstance asi = new MAttributeSetInstance(p_ctx, instanceId, get_TrxName());
                MLocator locTo = new MLocator(p_ctx, M_LocatorTo_ID, get_TrxName());
                String TextMsg = "Se ha rechazado el producto " + product.getValue() + " " + product.getName() + " su partida " + asi.getDescription() + " con una cantidad de " + qty + " en el deposito " + locTo.getValue();
                MNote note = new MNote(Env.getCtx(), 1000035, users[i], MMovement.getTableId(MMovement.Table_Name), this.getM_Movement_ID(), "Codigo de Producto: " + product.getValue(), TextMsg, null);
                note.setDescription("Movimiento de material por rechazo");
                if (!note.save(trx)) {
                    log.log(Level.SEVERE, "No se pudo guardar la Nota: " + note.getDescription());
                }

            }
        }

    }

    private String orderNumber(int instanceId) {
        String sqlCollector = "SELECT oc.DOCUMENTNO FROM C_Order oc INNER JOIN C_Orderline ocl ON (oc.C_Order_ID = ocl.C_Order_ID) WHERE ocl.M_AttributeSetInstance_ID = " + instanceId;
        String val = "";

        try {
            PreparedStatement pstmt1 = DB.prepareStatement(sqlCollector, null);
            ResultSet rs1 = pstmt1.executeQuery();
            while (rs1.next()) {
                val = rs1.getString("DOCUMENTNO");
            }
            rs1.close();
            pstmt1.close();
        } catch (SQLException obl) {
        }

        return val;
    }

    private String instanceNumber(int instanceId) {
        String sqlCollector = "SELECT DESCRIPTION FROM M_AttributeSetInstance WHERE M_AttributeSetInstance_ID = " + instanceId;
        String val = "";

        try {
            PreparedStatement pstmt1 = DB.prepareStatement(sqlCollector, null);
            ResultSet rs1 = pstmt1.executeQuery();
            while (rs1.next()) {
                val = rs1.getString("DESCRIPTION");
            }
            rs1.close();
            pstmt1.close();
        } catch (SQLException obl) {
        }

        return val;
    }

    private String orderDate(int instanceId) {
        String sqlCollector = "SELECT oc.DATEORDERED FROM C_Order oc INNER JOIN C_Orderline ocl ON (oc.C_Order_ID = ocl.C_Order_ID) WHERE ocl.M_AttributeSetInstance_ID = " + instanceId;
        String val = "";

        try {
            PreparedStatement pstmt1 = DB.prepareStatement(sqlCollector, null);
            ResultSet rs1 = pstmt1.executeQuery();
            while (rs1.next()) {
                val = rs1.getString("DATEORDERED");
            }
            rs1.close();
            pstmt1.close();
        } catch (SQLException obl) {
        }

        return val;
    }

    private String orderProv(int instanceId) {

        String sqlCollector = "SELECT p.NAME FROM C_Order oc INNER JOIN C_Orderline ocl ON (oc.C_Order_ID = ocl.C_Order_ID) INNER JOIN C_BPartner p ON (oc.C_BPartner_ID = p.C_BPartner_ID) WHERE ocl.M_AttributeSetInstance_ID = " + instanceId;
        String val = "";

        try {
            PreparedStatement pstmt1 = DB.prepareStatement(sqlCollector, null);
            ResultSet rs1 = pstmt1.executeQuery();
            while (rs1.next()) {
                val = rs1.getString("NAME");
            }
            rs1.close();
            pstmt1.close();
        } catch (SQLException obl) {
        }

        return val;
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

    /** BISion - 07/04/2010 - Santiago Ibañez
     * Metodo que retorna el id de un rol determinado
     * @param name
     * @return
     * @throws java.sql.SQLException
     */
    public int getAD_Role_IDByName(String name) throws SQLException {
        String sql = "select ad_role_id from ad_role where name = ?";
        PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        int id = -1;
        if (rs.next()) {
            id = rs.getInt(1);
        }
        rs.close();
        ps.close();
        return id;
    }

    /** BISion - 07/04/2010 - Santiago Ibañez
     * Obtengo todos los usuarios de acuerdo al rol
     * @param AD_Role_ID
     * @return
     * @throws java.sql.SQLException
     */
    public ArrayList getUsuarios(int AD_Role_ID) throws SQLException {
        String sql = "select ad_user_id from ad_user_roles where ad_role_id = ?";
        PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
        ArrayList users = new ArrayList();
        ps.setInt(1, AD_Role_ID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            users.add(rs.getInt(1));
        }
        return users;
        //Integer[] usuarios = new Integer[users.size()];
        //return (Integer[]) users.toArray(usuarios);
    }
}	//	MMovement

