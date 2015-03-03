package org.compiere.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.compiere.util.DB;
import org.compiere.util.Env;

public class MMOVIMIENTOFONDOSDEB extends X_C_MOVIMIENTOFONDOS_DEB {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private boolean acreditar = true;

    /**
     * ************************************************************************
     * Standard Constructor
     *
     * @param ctx context
     * @param C_MOVIMIENTOFONDOS_ID
     * @param trxName rx name
     */
    public MMOVIMIENTOFONDOSDEB(Properties ctx, int C_MOVIMIENTOFONDOS_DEB_ID, String trxName) {
        super(ctx, C_MOVIMIENTOFONDOS_DEB_ID, trxName);
    }

    /**
     * Load Constructor
     *
     * @param ctx context
     * @param rs result set record
     */
    public MMOVIMIENTOFONDOSDEB(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }	//	MMOVIMIENTOFONDOS_DEB

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
    private int LEY_DIAS_FA = 30;
    private int LEY_DIAS_FE = 360;
    private String COMUN = "C";

    protected boolean afterSave(boolean newRecord, boolean success) {
        MZYNDYNAMICMOVFONDOS dynMovFondos = MZYNDYNAMICMOVFONDOS.get(Env.getCtx(), getTipo());
        /*
        if (success && newRecord && (MMOVIMIENTOFONDOS.TIPO_DepositoPendiente.equals(getTipo())
                                     || dynMovFondos.isDEB_CUENTA_BANCO()
                                     || dynMovFondos.isCRE_CUENTA_BANCO()) && acreditar) {
        */
        
        if(dynMovFondos == null) {
        
            if (success && newRecord && MMOVIMIENTOFONDOS.TIPO_DepositoPendiente.equals(getTipo()) && acreditar) {
                MMOVIMIENTOFONDOSCRE cred = new MMOVIMIENTOFONDOSCRE(getCtx(), 0, get_TrxName());

                cred.setC_MOVIMIENTOFONDOS_ID(getC_MOVIMIENTOFONDOS_ID());
                cred.setC_BankAccount_ID(getC_BankAccount_ID());
                cred.setCREDITO(getDEBITO());
                cred.setC_AcctSchema_ID(getC_AcctSchema_ID());
                cred.setMV_CREDITO_ACCT(getMV_DEBITO_ACCT());
                cred.setDebitar(false);

                if (!cred.save(get_TrxName())) {
                    JOptionPane.showMessageDialog(null, "No se pudo Registrar Crédito", "Información", JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
            }            
        
        } else {
        
            if (success && newRecord && dynMovFondos.isCRE_DEPOSITO_PEND() && acreditar) {
                MMOVIMIENTOFONDOSCRE cred = new MMOVIMIENTOFONDOSCRE(getCtx(), 0, get_TrxName());

                cred.setC_MOVIMIENTOFONDOS_ID(getC_MOVIMIENTOFONDOS_ID());
                cred.setC_BankAccount_ID(getC_BankAccount_ID());
                cred.setCREDITO(getDEBITO());
                cred.setC_AcctSchema_ID(getC_AcctSchema_ID());
                cred.setMV_CREDITO_ACCT(getMV_DEBITO_ACCT());
                cred.setDebitar(false);

                if (!cred.save(get_TrxName())) {
                    JOptionPane.showMessageDialog(null, "No se pudo Registrar Crédito", "Información", JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
            }            
        
        }
        


        return success;
    }	// afterSave

    protected boolean beforeSave(boolean newRecord) {

        MZYNDYNAMICMOVFONDOS dynMovFondos = MZYNDYNAMICMOVFONDOS.get(Env.getCtx(), getTipo());
         
        if(dynMovFondos == null) {
            
            if (TIPO_RechCqPropio.equals(getTipo())) {
                if ((getC_BankAccount_ID() == null) || (getC_BankAccount_ID().equals(0))) {
                    JOptionPane.showMessageDialog(null, "Ingrese Cuenta Bancaria", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if (getC_VALORPAGO_ID() == 0) {
                    JOptionPane.showMessageDialog(null, "Ingrese Nro de Cheque de Propio", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                //	Verificación de Datos
                try {
                    String query = "select C_BankAccount_ID from C_VALORPAGO where C_VALORPAGO_ID = ?";

                    PreparedStatement pstmt = DB.prepareStatement(query, null);
                    pstmt.setInt(1, getC_VALORPAGO_ID());
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        if ((rs.getString(1) == null) || ((rs.getString(1) != null) && (rs.getInt(1) != getC_BankAccount_ID().intValue()))) {
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

            if (TIPO_DepCheque.equals(getTipo())) {
                if ((getC_BankAccount_ID() == null) || (getC_BankAccount_ID().equals(0))) {
                    JOptionPane.showMessageDialog(null, "Ingrese Cuenta Bancaria", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                return true;
            }

            if (TIPO_RechCqTercero.equals(getTipo())) {
                if ((getBank() == null) || (getBank().equals(""))) {
                    JOptionPane.showMessageDialog(null, "Ingrese Banco", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
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
                        if ((!rs.getString(1).equals("Q")) || (!((rs.getString(2).equals("D")) || (rs.getString(2).equals("E")))) || (!rs.getString(3).equals(getBank()))) {
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

            if (TIPO_VencCqPropio.equals(getTipo())) {
                if ((getC_BankAccount_ID() == null) || (getC_BankAccount_ID().equals(0))) {
                    JOptionPane.showMessageDialog(null, "Ingrese Cuenta Bancaria", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if (getC_VALORPAGO_ID() == 0) {
                    JOptionPane.showMessageDialog(null, "Ingrese Nro de Cheque de Propio", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                //	Verificación de Datos

                try {
                    String query = "select C_BankAccount_ID from C_VALORPAGO where C_VALORPAGO_ID = ?";

                    PreparedStatement pstmt = DB.prepareStatement(query, null);
                    pstmt.setInt(1, getC_VALORPAGO_ID());
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        if ((rs.getString(1) != null) && (rs.getInt(1) != getC_BankAccount_ID().intValue())) {
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

            if (TIPO_VencCqTercero.equals(getTipo())) {
                if ((getBank() == null) || (getBank().equals(""))) {
                    JOptionPane.showMessageDialog(null, "Ingrese Banco", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
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
                        if ((!rs.getString(1).equals("Q")) || (!((rs.getString(2).equals("C")) || (rs.getString(2).equals("E")))) || (!rs.getString(3).equals(getBank()))) {
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

            if (TIPO_CambioCheque.equals(getTipo())) {

                //	Verificación Parámetros
                if ((getBank() == null) || (getBank().equals(""))) {
                    JOptionPane.showMessageDialog(null, "Ingrese Banco", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if ((getNroCheque() == null) || (new Integer(getNroCheque()) == 0)) {
                    JOptionPane.showMessageDialog(null, "Ingrese Nro de Cheque", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if ((getTipoCheque() == null) || (getTipoCheque().equals(""))) {
                    JOptionPane.showMessageDialog(null, "Ingrese Tipo de Cheque", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if ((getClearing() == null) || (getClearing().equals(""))) {
                    JOptionPane.showMessageDialog(null, "Ingrese Días de Clearing", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
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

                //	Verificación de Datos

                MMOVIMIENTOFONDOS mov = new MMOVIMIENTOFONDOS(getCtx(), getC_MOVIMIENTOFONDOS_ID(), get_TrxName());

                Date datePay = new Date(getPaymentDate().getTime());
                Date dateEmi = new Date(getReleasedDate().getTime());

                Calendar date = Calendar.getInstance();

                Date dateAct = new Date(mov.getDateTrx().getTime());

                date.setTime(dateEmi);
                date.add(Calendar.DATE, LEY_DIAS_FE);
                Date dateVerFE = date.getTime();

                date.setTime(datePay);
                date.add(Calendar.DATE, LEY_DIAS_FA);
                Date dateVerFA = date.getTime();

                if (dateAct.after(dateVerFA)) {
                    JOptionPane.showMessageDialog(null, "La Fecha de Pago supera en al menos " + LEY_DIAS_FA + " días la Fecha del Movimiento", "Error - Verificación", JOptionPane.ERROR_MESSAGE);
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
                    String query = "select C_PAYMENTVALORES_ID from C_PAYMENTVALORES where NROCHEQUE = '" + getNroCheque() + "' AND ISACTIVE='Y'";

                    PreparedStatement pstmt = DB.prepareStatement(query, null);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next() && (getC_PAYMENTVALORES_ID() != rs.getInt(1))) {
                        JOptionPane.showMessageDialog(null, "El Nro de Cheque ingresado ya existe.", "Error - Nro de Cheque duplicado", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    rs.close();
                    pstmt.close();
                } catch (Exception e) {
                }

                //	Lógica
                setEstado("En Cartera");

                return true;
            }    
            
        } else {
        
            if (dynMovFondos.isDEB_CHEQUE_PRO_RECH()) {
                if ((getC_BankAccount_ID() == null) || (getC_BankAccount_ID().equals(0))) {
                    JOptionPane.showMessageDialog(null, "Ingrese Cuenta Bancaria", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if (getC_VALORPAGO_ID() == 0) {
                    JOptionPane.showMessageDialog(null, "Ingrese Nro de Cheque de Propio", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                //	Verificación de Datos
                try {
                    String query = "select C_BankAccount_ID from C_VALORPAGO where C_VALORPAGO_ID = ?";

                    PreparedStatement pstmt = DB.prepareStatement(query, null);
                    pstmt.setInt(1, getC_VALORPAGO_ID());
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        if ((rs.getString(1) == null) || ((rs.getString(1) != null) && (rs.getInt(1) != getC_BankAccount_ID().intValue()))) {
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

            if (dynMovFondos.isDEB_DEPOSITO()) {
                if ((getC_BankAccount_ID() == null) || (getC_BankAccount_ID().equals(0))) {
                    JOptionPane.showMessageDialog(null, "Ingrese Cuenta Bancaria", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                return true;
            }

            if (dynMovFondos.isDEB_CHEQUE_TER_RECH()) {
                if ((getBank() == null) || (getBank().equals(""))) {
                    JOptionPane.showMessageDialog(null, "Ingrese Banco", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
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
                        if ((!rs.getString(1).equals("Q")) || (!((rs.getString(2).equals("D")) || (rs.getString(2).equals("E")))) || (!rs.getString(3).equals(getBank()))) {
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

            if (dynMovFondos.isDEB_CHEQUE_PRO_VENC()) {
                if ((getC_BankAccount_ID() == null) || (getC_BankAccount_ID().equals(0))) {
                    JOptionPane.showMessageDialog(null, "Ingrese Cuenta Bancaria", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if (getC_VALORPAGO_ID() == 0) {
                    JOptionPane.showMessageDialog(null, "Ingrese Nro de Cheque de Propio", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                //	Verificación de Datos

                try {
                    String query = "select C_BankAccount_ID from C_VALORPAGO where C_VALORPAGO_ID = ?";

                    PreparedStatement pstmt = DB.prepareStatement(query, null);
                    pstmt.setInt(1, getC_VALORPAGO_ID());
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        if ((rs.getString(1) != null) && (rs.getInt(1) != getC_BankAccount_ID().intValue())) {
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

            if (dynMovFondos.isDEB_CHEQUE_TER_VENC()) {
                if ((getBank() == null) || (getBank().equals(""))) {
                    JOptionPane.showMessageDialog(null, "Ingrese Banco", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
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
                        if ((!rs.getString(1).equals("Q")) || (!((rs.getString(2).equals("C")) || (rs.getString(2).equals("E")))) || (!rs.getString(3).equals(getBank()))) {
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

            if (dynMovFondos.isDEB_CHEQUE_REC()) {

                //	Verificación Parámetros
                if ((getBank() == null) || (getBank().equals(""))) {
                    JOptionPane.showMessageDialog(null, "Ingrese Banco", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if ((getNroCheque() == null) || (new Integer(getNroCheque()) == 0)) {
                    JOptionPane.showMessageDialog(null, "Ingrese Nro de Cheque", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if ((getTipoCheque() == null) || (getTipoCheque().equals(""))) {
                    JOptionPane.showMessageDialog(null, "Ingrese Tipo de Cheque", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if ((getClearing() == null) || (getClearing().equals(""))) {
                    JOptionPane.showMessageDialog(null, "Ingrese Días de Clearing", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
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

                //	Verificación de Datos

                MMOVIMIENTOFONDOS mov = new MMOVIMIENTOFONDOS(getCtx(), getC_MOVIMIENTOFONDOS_ID(), get_TrxName());

                Date datePay = new Date(getPaymentDate().getTime());
                Date dateEmi = new Date(getReleasedDate().getTime());

                Calendar date = Calendar.getInstance();

                Date dateAct = new Date(mov.getDateTrx().getTime());

                date.setTime(dateEmi);
                date.add(Calendar.DATE, LEY_DIAS_FE);
                Date dateVerFE = date.getTime();

                date.setTime(datePay);
                date.add(Calendar.DATE, LEY_DIAS_FA);
                Date dateVerFA = date.getTime();

                if (dateAct.after(dateVerFA)) {
                    JOptionPane.showMessageDialog(null, "La Fecha de Pago supera en al menos " + LEY_DIAS_FA + " días la Fecha del Movimiento", "Error - Verificación", JOptionPane.ERROR_MESSAGE);
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
                    String query = "select C_PAYMENTVALORES_ID from C_PAYMENTVALORES where NROCHEQUE = '" + getNroCheque() + "' AND ISACTIVE='Y'";

                    PreparedStatement pstmt = DB.prepareStatement(query, null);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next() && (getC_PAYMENTVALORES_ID() != rs.getInt(1))) {
                        JOptionPane.showMessageDialog(null, "El Nro de Cheque ingresado ya existe.", "Error - Nro de Cheque duplicado", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    rs.close();
                    pstmt.close();
                } catch (Exception e) {
                }

                //	Lógica
                setEstado("En Cartera");

                return true;
            }         
        
        }      


        return true;
    }

    public boolean isAcreditar() {
        return acreditar;
    }

    public void setAcreditar(boolean acreditar) {
        this.acreditar = acreditar;
    }
}
