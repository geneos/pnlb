/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.compiere.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Properties;
import java.util.logging.Level;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class MZYNDYNAMICMOVFONDOS extends X_ZYN_DYNAMIC_MOVFONDOS {

    private static CLogger s_log = CLogger.getCLogger(MZYNDYNAMICMOVFONDOS.class);
    private int AD_Reference_ID = 1000087;

    public MZYNDYNAMICMOVFONDOS(Properties ctx, int ZYN_DYNAMIC_MOVFONDOS_ID, String trxName) {
        super(ctx, ZYN_DYNAMIC_MOVFONDOS_ID, trxName);
    }

    public MZYNDYNAMICMOVFONDOS(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    public static MZYNDYNAMICMOVFONDOS get(Properties ctx, String type) {
        MZYNDYNAMICMOVFONDOS retValue = null;
        String sql = "SELECT * FROM zyn_dynamic_movfondos WHERE code = ?";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql, null);
            pstmt.setString(1, type);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                retValue = new MZYNDYNAMICMOVFONDOS(ctx, rs, null);
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            s_log.log(Level.SEVERE, sql, e);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }
        return retValue;
    }	//	get

    private boolean findPrevListValue(String code) {

        String sql = "SELECT AD_REF_LIST_ID FROM AD_REF_LIST WHERE AD_Reference_ID = ? AND VALUE = ?";
        PreparedStatement pstmt = null;
        boolean retValue = false;
        try {
            pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, AD_Reference_ID);
            pstmt.setString(2, code);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                retValue = true;
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            s_log.log(Level.SEVERE, sql, e);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }
        return retValue;
    }	//	get

    private boolean findExistentsMovements(String code) {

        String sql = "SELECT 1 FROM C_MOVIMIENTOFONDOS WHERE TIPO = ?";
        PreparedStatement pstmt = null;
        boolean retValue = false;
        try {
            pstmt = DB.prepareStatement(sql, null);
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                retValue = true;
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            s_log.log(Level.SEVERE, sql, e);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (Exception e) {
            pstmt = null;
        }
        return retValue;
    }	//	get

    private boolean validateDebito() {
        int amountTrue = 0;
        if (isDEB_CUENTA_DEBITO()) {
            amountTrue++;
        }

         if (amountTrue < 2 && isDEB_CUENTA_DEBITO_FIJA()) {
            amountTrue++;
            if (getDEB_C_Element_ID() == 0){
                log.saveError("Error", "Debe seleccionar una cuenta contable de Debito Fija");
                return false;
            }
        }

        if (amountTrue < 2 && isDEB_CHEQUE_REC()) {
            amountTrue++;
        }

        if (amountTrue < 2 && isDEB_DEPOSITO()) {
            amountTrue++;
        }
       
        if (amountTrue < 2 && isDEB_CUENTA_BANCO()) {
            amountTrue++;
        }
        if (amountTrue < 2 && isDEB_CHEQUE_TERCERO()) {
            amountTrue++;
        }
        if (amountTrue < 2 && isDEB_CREDITO_BANCO()) {
            amountTrue++;
        }
        if (amountTrue < 2 && isDEB_DEPOSITO_PEND()) {
            amountTrue++;
        }
        if (amountTrue < 2 && isDEB_CHEQUE_PRO_RECH()) {
            amountTrue++;
        }
        if (amountTrue < 2 && isDEB_CHEQUE_PRO_VENC()) {
            amountTrue++;
        }
        if (amountTrue < 2 && isDEB_CHEQUE_TER_RECH()) {
            amountTrue++;
        }
        if (amountTrue < 2 && isDEB_CHEQUE_TER_VENC()) {
            amountTrue++;
        }
        if (amountTrue == 0) {
            log.saveError("Error", "Debe seleccionar al menos un elemento para Débito");
            return false;
        }
        if (amountTrue > 1) {
            log.saveError("Error", "No debe seleccionar mas de un elemento para Débito");
            return false;
        }
        return true;
    }

    private boolean validateCredito() {
        int amountTrue = 0;
        if (isCRE_TRANSFERENCIA()) {
            amountTrue++;
        }
        if (isCRE_CHEQUE_RECI()) {
            amountTrue++;
        }
        if (amountTrue < 2 && isCRE_CHEQUE_DEPO()) {
            amountTrue++;
        }
        if (amountTrue < 2 && isCRE_VALORES_NEG()) {
            amountTrue++;
        }
        if (amountTrue < 2 && isCRE_CANCEL_FACT()) {
            amountTrue++;
        }
        if (amountTrue < 2 && isCRE_CHEQUE_PROPIO()) {
            amountTrue++;
        }
        if (amountTrue < 2 && isCRE_EFECTIVO()) {
            amountTrue++;
        }
        if (amountTrue < 2 && isCRE_CHEQUE_TER_RECH()) {
            amountTrue++;
        }
        if (amountTrue < 2 && isCRE_CHEQUE_TER_VENC()) {
            amountTrue++;
        }
        if (amountTrue < 2 && isCRE_CHEQUE_PRO_RECH()) {
            amountTrue++;
        }
        if (amountTrue < 2 && isCRE_CHEQUE_PRO_VENC()) {
            amountTrue++;
        }
        if (amountTrue < 2 && isCRE_DEPOSITO_PEND()) {
            amountTrue++;
        }
        if (amountTrue < 2 && isCRE_CUENTA_BANCO_DESC()) {
            amountTrue++;
        }
        if (amountTrue < 2 && isCRE_CUENTA_CREDITO()) {
            amountTrue++;
        }
        if (amountTrue < 2 && isCRE_CUENTA_CREDITO_FIJA()) {
            amountTrue++;
            if (getC_Element_ID() == 0){
                log.saveError("Error", "Debe seleccionar una cuenta contable de Credito Fija");
                return false;
            }
        }
        if (amountTrue == 0) {
            log.saveError("Error", "Debe seleccionar al menos un elemento para Crédito");
            return false;
        }
        if (amountTrue > 1) {
            log.saveError("Error", "No debe seleccionar mas de un elemento para Crédito");
            return false;
        }
        return true;
    }

    /**
     * After Save
     *
     * @param newRecord new
     * @param success success
     * @return success
     */
    protected boolean beforeSave(boolean newRecord) {
        //	Validate Code List
        if (getCode() == null || getCode().trim().length() <= 0) {
            log.saveError("FillMandatory", "Code");
            return false;
        }

        if ( getCode() != null && getCode().trim().length() > 1) {
            log.saveError("Error", "El Codigo debe ser solo de 1 Caracter");
            return false;
        }


        if (!validateDebito() || !validateCredito()) {
            return false;
        }

        MRefList refList = null;
        boolean existsCode = false;
        if (newRecord) {
            if (getCode() != null && getCode().length() > 0) {
                existsCode = findPrevListValue(getCode());
                if (existsCode) {
                    log.saveError("Error", "Codigo existente en movimiento de fondos");
                    return false;
                }
            } else {
                log.saveError("FillMandatory", "Code");
            }

            refList = new MRefList(getCtx(), 0, get_TrxName());
            refList.setAD_Reference_ID(AD_Reference_ID);

            MSequence sequence = new MSequence(getCtx(), 0, get_TrxName());
            sequence.setName("MV " + getName());
            if (!sequence.save()) {
                log.saveError("Error", "La secuencia no se pudo crear");
                return false;
            }

            MDocType docType = new MDocType(getCtx(), 0, get_TrxName());
            docType.setName(getName());
            docType.setPrintName("MVF " + getCode());
            docType.setDocBaseType("AMM");
            docType.setHasProforma(false);
            docType.setIsDocNoControlled(true);
            docType.setGL_Category_ID(1000021);
            docType.setDocumentCopies(1);
            docType.setIsInTransit(false);
            docType.setDocNoSequence_ID(sequence.getAD_Sequence_ID());

            if (!docType.save()) {
                log.saveError("Error", "El tipo de documento no pudo ser creado");
                return false;
            }

        } else {
            existsCode = findExistentsMovements((String) get_ValueOld("Code"));
            if (existsCode) {
                log.saveError("Error", "No se puede modificar. Existen movimiento de fondos de este tipo");
                return false;
            }

            if (is_ValueChanged("Name") || is_ValueChanged("Code")) {
                if (is_ValueChanged("Code")) {
                    existsCode = findPrevListValue(getCode());
                    if (existsCode) {
                        log.saveError("Error", "Codigo existente en movimiento de fondos");
                        return false;
                    }
                }
                refList = new MRefList(getCtx(), getAD_Ref_List_ID(), get_TrxName());
            }
        }
        if (refList != null) {
            refList.setValue(getCode());
            refList.setName(getName());
            refList.save();

            if (newRecord) {
                setAD_Ref_List_ID(refList.getAD_Ref_List_ID());
            }
        }
        return true;
    }	//	afterSave

    /**
     * After Delete
     *
     * @param success success
     * @return success
     */
    protected boolean afterDelete(boolean success) {
        MRefList reflist = new MRefList(getCtx(), getAD_Ref_List_ID(), get_TrxName());
        reflist.delete(success);

        MDocType docType = MDocType.getDocTypeBy(getCtx(), "MVF " + getCode());
        if (docType != null) {
            MSequence sequence = new MSequence(getCtx(), docType.getDocNoSequence_ID(), get_TrxName());
            docType.delete(success);
            sequence.delete(success);
        }
        return success;
    }	//	afterDelete

    /**
     * Before Delete.
     *
     * @return true if acct was deleted
     */
    protected boolean beforeDelete() {
        boolean existsCode = findExistentsMovements(getCode());
        if (existsCode) {
            log.saveError("Error", "No se puede eliminar. Existen movimiento de fondos de este tipo");
            return false;
        }
        return true;
    }	//	beforeDelete
}
