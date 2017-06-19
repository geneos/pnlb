package org.compiere.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import java.util.logging.Level;
import javax.swing.JOptionPane;

import org.compiere.util.DB;
import org.compiere.util.Env;

public class MMOVIMIENTOFONDOSCRE extends X_C_MOVIMIENTOFONDOS_CRE {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private boolean debitar = true;

    /**
     * ************************************************************************
     * Standard Constructor
     *
     * @param ctx context
     * @param C_MOVIMIENTOFONDOS_ID
     * @param trxName rx name
     */
    public MMOVIMIENTOFONDOSCRE(Properties ctx, int C_MOVIMIENTOFONDOS_CRE_ID, String trxName) {
        super(ctx, C_MOVIMIENTOFONDOS_CRE_ID, trxName);
    }

    /**
     * Load Constructor
     *
     * @param ctx context
     * @param rs result set record
     */
    public MMOVIMIENTOFONDOSCRE(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }	//	MMOVIMIENTOFONDOS_CRE

    private String getTipo() {
        try {
            String query = "select TIPO from C_MOVIMIENTOFONDOS where C_MOVIMIENTOFONDOS_ID = ?";

            PreparedStatement pstmt = DB.prepareStatement(query, null);
            pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString(1);
            }

            rs.close();
            pstmt.close();
        } catch (Exception n) {
        }

        return "";
    }

    protected boolean afterSave(boolean newRecord, boolean success) {

        if (!success) {
            return false;
        }

        MZYNDYNAMICMOVFONDOS dynMovFondos = MZYNDYNAMICMOVFONDOS.get(Env.getCtx(), getTipo());

        if (dynMovFondos == null) {

            if (TIPO_EmiCheque.equals(getTipo()) && newRecord) {
                //Now in MMOVIMIENTOFONDOS.completeIt()
                /*
                int key = getC_BankAccount_ID();
                
                String nextNroCheque = getNroCheque();
                int nroCheque = Integer.valueOf(nextNroCheque).intValue() + 1;
                nextNroCheque = String.valueOf(nroCheque);
                
                DB.executeUpdate("UPDATE C_BankAccountDoc SET CURRENTNEXT = '" + nextNroCheque + "' WHERE C_BankAccount_ID = " + key, null);*/
            }	//TIPO_EmiCheque

            if (TIPO_CambioCheque.equals(getTipo())) {
                MMOVIMIENTOFONDOS mov = new MMOVIMIENTOFONDOS(getCtx(), getC_MOVIMIENTOFONDOS_ID(), get_TrxName());
                ArrayList<MMOVIMIENTOFONDOSDEB> deb = mov.getC_MOVIMIENTOFONDOS_DEB_ID();
                if (deb != null) {
                    for (int i = 0; i < deb.size(); i++) {
                        deb.get(0).setCuitFirmante(getCuitFirm());
                        deb.get(0).save();
                    }
                } //deb!=null
            }	//TIPO_CambioCheque

            /*
            if (newRecord && (MMOVIMIENTOFONDOS.TIPO_DepositoPendiente.equals(getTipo())
            || dynMovFondos.isDEB_CUENTA_BANCO()
            || dynMovFondos.isCRE_CUENTA_BANCO()) && debitar) {
             */
            if (newRecord && MMOVIMIENTOFONDOS.TIPO_DepositoPendiente.equals(getTipo()) && debitar) {
                MMOVIMIENTOFONDOSDEB deb = new MMOVIMIENTOFONDOSDEB(getCtx(), 0, get_TrxName());

                deb.setC_MOVIMIENTOFONDOS_ID(getC_MOVIMIENTOFONDOS_ID());
                deb.setC_BankAccount_ID(getC_BankAccount_ID());
                deb.setDEBITO(getCREDITO());
                deb.setC_AcctSchema_ID(getC_AcctSchema_ID());
                deb.setMV_DEBITO_ACCT(getMV_CREDITO_ACCT());
                deb.setAcreditar(false);

                if (!deb.save(get_TrxName())) {
                    JOptionPane.showMessageDialog(null, "No se pudo Registrar Débito", "Información", JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
            }

        } else {

            if (dynMovFondos.isCRE_CHEQUE_PROPIO() && newRecord) {
                //Now in MMOVIMIENTOFONDOS.completeIt()
                    /*
                int key = getC_BankAccount_ID();
                
                String nextNroCheque = getNroCheque();
                int nroCheque = Integer.valueOf(nextNroCheque).intValue() + 1;
                nextNroCheque = String.valueOf(nroCheque);
                
                DB.executeUpdate("UPDATE C_BankAccountDoc SET CURRENTNEXT = '" + nextNroCheque + "' WHERE C_BankAccount_ID = " + key, null);*/
            }	//TIPO_EmiCheque

            if (dynMovFondos.isCRE_CHEQUE_RECI()) {
                MMOVIMIENTOFONDOS mov = new MMOVIMIENTOFONDOS(getCtx(), getC_MOVIMIENTOFONDOS_ID(), get_TrxName());
                ArrayList<MMOVIMIENTOFONDOSDEB> deb = mov.getC_MOVIMIENTOFONDOS_DEB_ID();
                if (deb != null) {
                    for (int i = 0; i < deb.size(); i++) {
                        deb.get(0).setCuitFirmante(getCuitFirm());
                        deb.get(0).save();
                    }
                } //deb!=null
            }	//TIPO_CambioCheque

            /*
            if (newRecord && (MMOVIMIENTOFONDOS.TIPO_DepositoPendiente.equals(getTipo())
            || dynMovFondos.isDEB_CUENTA_BANCO()
            || dynMovFondos.isCRE_CUENTA_BANCO()) && debitar) {
             */
            if (newRecord && dynMovFondos.isDEB_DEPOSITO_PEND() && debitar) {
                MMOVIMIENTOFONDOSDEB deb = new MMOVIMIENTOFONDOSDEB(getCtx(), 0, get_TrxName());

                deb.setC_MOVIMIENTOFONDOS_ID(getC_MOVIMIENTOFONDOS_ID());
                deb.setC_BankAccount_ID(getC_BankAccount_ID());
                deb.setDEBITO(getCREDITO());
                deb.setC_AcctSchema_ID(getC_AcctSchema_ID());
                deb.setMV_DEBITO_ACCT(getMV_CREDITO_ACCT());
                deb.setAcreditar(false);

                if (!deb.save(get_TrxName())) {
                    JOptionPane.showMessageDialog(null, "No se pudo Registrar Débito", "Información", JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
            }

        }

        return success;
    }	//	afterSave

    public String getCuitFirm() {
        MPAYMENTVALORES pay = new MPAYMENTVALORES(getCtx(), getC_PAYMENTVALORES_ID(), get_TrxName());
        if (pay != null) {
            return pay.getCuitFirmante();
        }
        return "";
    }
    private int LEY_DIAS_FA = 30;
    private int LEY_DIAS_FE = 360;
    private boolean saveForce = false;
    private String COMUN = "C";

    public boolean beforeSave(boolean newRecord) {

        if (isSaveForce() == false) {

            MZYNDYNAMICMOVFONDOS dynMovFondos = MZYNDYNAMICMOVFONDOS.get(Env.getCtx(), getTipo());

            if (dynMovFondos == null) {

                if (TIPO_EmiCheque.equals(getTipo())) {

                    //			Verificación Parámetros
                    if ((getC_BankAccount_ID() == null) || (getC_BankAccount_ID().equals(0))) {
                        JOptionPane.showMessageDialog(null, "Ingrese Cuenta Bancaria", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    /*if ((getNroCheque() == null) || (new Integer(getNroCheque()) == 0)) {
                    JOptionPane.showMessageDialog(null, "Ingrese Nro de Cheque", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                    }*/
                    if ((getTipoCheque() == null) || (getTipoCheque().equals(""))) {
                        JOptionPane.showMessageDialog(null, "Ingrese Tipo de Cheque", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if (getReleasedDate() == null) {
                        JOptionPane.showMessageDialog(null, "Ingrese Fecha de Emisión", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if (getPaymentDate() == null) {
                        JOptionPane.showMessageDialog(null, "Ingrese Fecha de Pago", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if ((getAFavor() == null) || (getAFavor().equals(""))) {
                        JOptionPane.showMessageDialog(null, "Ingrese A favor de", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    Date datePay = new Date(getPaymentDate().getTime());
                    Date dateEmi = new Date(getReleasedDate().getTime());

                    Calendar date = Calendar.getInstance();

                    MMOVIMIENTOFONDOS mov = new MMOVIMIENTOFONDOS(getCtx(), getC_MOVIMIENTOFONDOS_ID(), get_TrxName());
                    Date dateAct = new Date(mov.getDateTrx().getTime());

                    date.setTime(dateEmi);
                    date.add(Calendar.DATE, LEY_DIAS_FE);
                    Date dateVerFE = date.getTime();

                    date.setTime(datePay);
                    date.add(Calendar.DATE, LEY_DIAS_FA);
                    Date dateVerFA = date.getTime();

                    if (dateAct.after(dateVerFA)) {
                        JOptionPane.showMessageDialog(null, "La Fecha de Pago supera en al menos " + LEY_DIAS_FA + " días la Fecha de Movimiento", "Error - Verificación", JOptionPane.ERROR_MESSAGE);
                        return false;
                    } else if (getTipoCheque().equals(COMUN)) {
                        if (!datePay.equals(dateEmi)) {
                            JOptionPane.showMessageDialog(null, "Verifique que la Fecha de Emisión sea igual a la Fecha de Pago", "Error - Verificación", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    } else {
                        date.setTime(dateEmi);
                        date.add(Calendar.DATE, 1);
                        Date EmiTomorrow = date.getTime();
                        if (EmiTomorrow.after(datePay)) {
                            JOptionPane.showMessageDialog(null, "Verifique que la Fecha de Pago sea mayor a la Fecha de Emisión", "Error - Verificación", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                        if (datePay.after(dateVerFE)) {
                            JOptionPane.showMessageDialog(null, "Verifique que la Fecha de Pago no supere la Fecha de Emisión por más de " + LEY_DIAS_FE + " días", "Error - Verificación", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }

                    /*
                    try {
                    String query = "select C_VALORPAGO_ID from C_VALORPAGO where NROCHEQUE = '" + getNroCheque() + "'";
                    
                    PreparedStatement pstmt = DB.prepareStatement(query, null);
                    ResultSet rs = pstmt.executeQuery();
                    
                    if (rs.next() && (getC_VALORPAGO_ID() != rs.getInt(1))) {
                    JOptionPane.showMessageDialog(null, "El Nro de Cheque ingresado ya existe.", "Error - Verificación", JOptionPane.ERROR_MESSAGE);
                    return false;
                    }
                    
                    rs.close();
                    pstmt.close();
                    } catch (Exception n) {
                    }
                     *
                     */

                    //	Lógica
                    //TODO PASAR A CONSTANTE
                    setEstado("Emitido");

                    return true;
                }

                if (TIPO_TraBancaria.equals(getTipo())) {
                    if ((getC_BankAccount_ID() == null) || (getC_BankAccount_ID().equals(0))) {
                        JOptionPane.showMessageDialog(null, "Ingrese Cuenta Bancaria", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if ((getNroTransferencia() == null) || (new Integer(getNroTransferencia()) == 0)) {
                        JOptionPane.showMessageDialog(null, "Ingrese Nro de Transferencia", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    return true;
                }

                if (TIPO_DepCheque.equals(getTipo())) {
                    //				if ((getBank()==null) || (getBank().equals("")))
                    //				{
                    //					JOptionPane.showMessageDialog(null,"Ingrese Banco","Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    //					return false;
                    //				}
                    if (getC_PAYMENTVALORES_ID() == 0) {
                        JOptionPane.showMessageDialog(null, "Ingrese Nro de Cheque de Tercero", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    
                   if  ( !verificarChequeDuplicdo() ) {
                         JOptionPane.showMessageDialog(null, "El Cheque de Tercero ya fue ingresado en esta conciliacion", "Error - Cheque ya ingresado", JOptionPane.ERROR_MESSAGE);
                        return false;
                   }

                    //Verificación de Datos

                    try {
                        String query = "select TIPO,STATE,BANCO from C_PAYMENTVALORES where C_PAYMENTVALORES_ID = ?";

                        PreparedStatement pstmt = DB.prepareStatement(query, null);
                        pstmt.setInt(1, getC_PAYMENTVALORES_ID());
                        ResultSet rs = pstmt.executeQuery();
                        //TODO PASAR A CONSTANTE
                        if (rs.next()) {
                            //						if ((!rs.getString(1).equals("Q")) || (!rs.getString(2).equals("C")) || (!rs.getString(3).equals(getBank())))
                            if ((!rs.getString(1).equals("Q")) || (!rs.getString(2).equals("C"))) {
                                JOptionPane.showMessageDialog(null, "El Nro de Cheque seleccionado es Incorrecto.", "Error - Verificación", JOptionPane.ERROR_MESSAGE);
                                return false;
                            }
                        }

                        rs.close();
                        pstmt.close();
                    } catch (Exception n) {
                    }

                    return true;
                }

                if (TIPO_ValNegociados.equals(getTipo())) {
                     if  ( !verificarChequeDuplicdo() ) {
                         JOptionPane.showMessageDialog(null, "El Cheque de Tercero ya fue ingresado en esta conciliacion", "Error - Cheque ya ingresado", JOptionPane.ERROR_MESSAGE);
                        return false;
                   }
                    return true;
                }

                if (TIPO_CambioCheque.equals(getTipo())) {

                    return true;
                }

                // Transferencia entre cuentas 
                if (MMOVIMIENTOFONDOS.TIPO_TransferenciaCuentasBancarias.equals(getTipo())) {
                    MMOVIMIENTOFONDOS mov = new MMOVIMIENTOFONDOS(getCtx(), getC_MOVIMIENTOFONDOS_ID(), get_TrxName());

                    if (mov.getDocStatus().equals(mov.DOCSTATUS_Drafted)
                            || mov.getDocStatus().equals(mov.DOCSTATUS_InProgress)) {

                        setConvertido(mov.getC_Currency_ID(), mov.getCotizacion());
                    }
                }



            } else {

                if (dynMovFondos.isCRE_CHEQUE_PROPIO()) {

                    //			Verificación Parámetros
                    if ((getC_BankAccount_ID() == null) || (getC_BankAccount_ID().equals(0))) {
                        JOptionPane.showMessageDialog(null, "Ingrese Cuenta Bancaria", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    /*if ((getNroCheque() == null) || (new Integer(getNroCheque()) == 0)) {
                    JOptionPane.showMessageDialog(null, "Ingrese Nro de Cheque", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                    }*/
                    if ((getTipoCheque() == null) || (getTipoCheque().equals(""))) {
                        JOptionPane.showMessageDialog(null, "Ingrese Tipo de Cheque", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if (getReleasedDate() == null) {
                        JOptionPane.showMessageDialog(null, "Ingrese Fecha de Emisión", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if (getPaymentDate() == null) {
                        JOptionPane.showMessageDialog(null, "Ingrese Fecha de Pago", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if ((getAFavor() == null) || (getAFavor().equals(""))) {
                        JOptionPane.showMessageDialog(null, "Ingrese A favor de", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    Date datePay = new Date(getPaymentDate().getTime());
                    Date dateEmi = new Date(getReleasedDate().getTime());

                    Calendar date = Calendar.getInstance();

                    MMOVIMIENTOFONDOS mov = new MMOVIMIENTOFONDOS(getCtx(), getC_MOVIMIENTOFONDOS_ID(), get_TrxName());
                    Date dateAct = new Date(mov.getDateTrx().getTime());

                    date.setTime(dateEmi);
                    date.add(Calendar.DATE, LEY_DIAS_FE);
                    Date dateVerFE = date.getTime();

                    date.setTime(datePay);
                    date.add(Calendar.DATE, LEY_DIAS_FA);
                    Date dateVerFA = date.getTime();

                    if (dateAct.after(dateVerFA)) {
                        JOptionPane.showMessageDialog(null, "La Fecha de Pago supera en al menos " + LEY_DIAS_FA + " días la Fecha de Movimiento", "Error - Verificación", JOptionPane.ERROR_MESSAGE);
                        return false;
                    } else if (getTipoCheque().equals(COMUN)) {
                        if (!datePay.equals(dateEmi)) {
                            JOptionPane.showMessageDialog(null, "Verifique que la Fecha de Emisión sea igual a la Fecha de Pago", "Error - Verificación", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    } else {
                        date.setTime(dateEmi);
                        date.add(Calendar.DATE, 1);
                        Date EmiTomorrow = date.getTime();
                        if (EmiTomorrow.after(datePay)) {
                            JOptionPane.showMessageDialog(null, "Verifique que la Fecha de Pago sea mayor a la Fecha de Emisión", "Error - Verificación", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                        if (datePay.after(dateVerFE)) {
                            JOptionPane.showMessageDialog(null, "Verifique que la Fecha de Pago no supere la Fecha de Emisión por más de " + LEY_DIAS_FE + " días", "Error - Verificación", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }

                    try {
                        String query = "select C_VALORPAGO_ID from C_VALORPAGO where NROCHEQUE = '" + getNroCheque() + "'";

                        PreparedStatement pstmt = DB.prepareStatement(query, null);
                        ResultSet rs = pstmt.executeQuery();

                        if (rs.next() && (getC_VALORPAGO_ID() != rs.getInt(1)) && !getNroCheque().equals("00000000")) {
                            JOptionPane.showMessageDialog(null, "El Nro de Cheque ingresado ya existe.", "Error - Verificación", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }

                        rs.close();
                        pstmt.close();
                    } catch (Exception n) {
                    }

                    //	Lógica
                    //TODO PASAR A CONSTANTE
                    setEstado("Emitido");

                    return true;
                }

                if (dynMovFondos.isCRE_TRANSFERENCIA()) {
                    if ((getC_BankAccount_ID() == null) || (getC_BankAccount_ID().equals(0))) {
                        JOptionPane.showMessageDialog(null, "Ingrese Cuenta Bancaria", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if ((getNroTransferencia() == null) || (new Integer(getNroTransferencia()) == 0)) {
                        JOptionPane.showMessageDialog(null, "Ingrese Nro de Transferencia", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    return true;
                }

                if (dynMovFondos.isCRE_CHEQUE_DEPO()) {
                    //				if ((getBank()==null) || (getBank().equals("")))
                    //				{
                    //					JOptionPane.showMessageDialog(null,"Ingrese Banco","Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    //					return false;
                    //				}
                    if (getC_PAYMENTVALORES_ID() == 0) {
                        JOptionPane.showMessageDialog(null, "Ingrese Nro de Cheque de Tercero", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    //Verificación de Datos

                    try {
                        String query = "select TIPO,STATE,BANCO from C_PAYMENTVALORES where C_PAYMENTVALORES_ID = ?";

                        PreparedStatement pstmt = DB.prepareStatement(query, null);
                        pstmt.setInt(1, getC_PAYMENTVALORES_ID());
                        ResultSet rs = pstmt.executeQuery();
                        //TODO PASAR A CONSTANTE
                        if (rs.next()) {
                            //						if ((!rs.getString(1).equals("Q")) || (!rs.getString(2).equals("C")) || (!rs.getString(3).equals(getBank())))
                            if ((!rs.getString(1).equals("Q")) || (!rs.getString(2).equals("C"))) {
                                JOptionPane.showMessageDialog(null, "El Nro de Cheque seleccionado es Incorrecto.", "Error - Verificación", JOptionPane.ERROR_MESSAGE);
                                return false;
                            }
                        }

                        rs.close();
                        pstmt.close();
                    } catch (Exception n) {
                    }

                    return true;
                }

                if (dynMovFondos.isCRE_VALORES_NEG()) {

                    return true;
                }

                if (dynMovFondos.isCRE_CHEQUE_RECI()) {

                    return true;
                }


            }

        } else // saveForse = true
        {
            setSaveForce(false);
        }

        return true;
    }

    public boolean isSaveForce() {
        return saveForce;
    }

    public void setSaveForce(boolean saveForce) {
        this.saveForce = saveForce;
    }

    public boolean isDebitar() {
        return debitar;
    }

    public void setDebitar(boolean debitar) {
        this.debitar = debitar;
    }

    public void setConvertido(int c_Currency_ID, BigDecimal cotizacion) {
        //Si es extranjera y la cuenta actual es extranjera
        MBankAccount ba = new MBankAccount(getCtx(), getC_BankAccount_ID(), get_TrxName());

        if (c_Currency_ID != 118
                && ba.getC_Currency_ID() != 118) {
            //Actualizo Convertido segun cotizacion
            setConvertido(getCREDITO().multiply(cotizacion));
        } else {
            setConvertido(getCREDITO());
        }
    }

    private boolean verificarChequeDuplicdo() {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean retValue = true;
         try {
            String query = "select 1 from c_movimientofondos_cre where c_movimientofondos_id = ? and  C_PAYMENTVALORES_id = ? and  c_movimientofondos_cre_id <> ?";

            pstmt = DB.prepareStatement(query, null);
            pstmt.setInt(1,getC_MOVIMIENTOFONDOS_ID());
            pstmt.setInt(2, getC_PAYMENTVALORES_ID());
            pstmt.setInt(3, getC_MOVIMIENTOFONDOS_CRE_ID());

             rs = pstmt.executeQuery();
            if (rs.next()) {
                 retValue = false;
            }

            rs.close();
            pstmt.close();
         } catch (Exception e) {
             log.log(Level.SEVERE, "Error al validar cheques duplicados", e);
              retValue = false;
         }
         return retValue;
    }
}
