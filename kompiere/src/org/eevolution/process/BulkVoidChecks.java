/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eevolution.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
/*
 *  21/03/2013 Maria Jesus
 *  Modificacion de org.compiere.model.MOrder a 
 *  org.eevolution.model.MOrder para que luego del save() haga el 
 *  afterSave() que es el que modifica el docStatus en la tabla
 *  MPC_MRP y de este modo no aparezcan en Revisión a Detalle.
 */
import java.util.logging.Level;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MBankAccount;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MMOVIMIENTOFONDOS;
import org.compiere.model.MMOVIMIENTOFONDOSCRE;
import org.compiere.model.MMOVIMIENTOFONDOSDEB;
import org.compiere.model.MPayment;
import org.compiere.model.MVALORPAGO;
import org.eevolution.model.MOrder;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

/**
 * Proceso que anula cheques. Recibe por parametro un listado de cheques separado por coma
 * Es decir que para cada cheque:
 * 1) Verifique que el estado del cheque sea emitido
 * 2) Localize a que pago corresponde para poder utilizarlo en la descripcion y saber sobre que socio de negocio armar los contradocumentos.
 * 3) Generar el movimiento de fondo y completarlo
 * 4) Generar el ajuste de debito y completarlo
 * 
 * @author Pablo
 */
public class BulkVoidChecks extends SvrProcess {

    private String cheques = null;
    private Timestamp voidDate = null;
    private int p_CHREC_Chargue = 5000037;
    private int p_AcctSchema_ID = 1000002;

    @Override
    protected void prepare() {
        ProcessInfoParameter parametros[] = getParameter();
        for (int i = 0; i < parametros.length; i++) {
            if (parametros[i].getParameterName().equals("VoidDate")) {
                voidDate = (Timestamp) parametros[i].getParameter();
            }
            if (parametros[i].getParameterName().equals("Cheques")) {
                cheques =(String)parametros[i].getParameter();
            }

        }
    } 

    @Override
    protected String doIt() throws Exception {
        Env.setContext(getCtx(), "OmitAutomaticPayment", "Y");
        int countE = 0;
        int countOK = 0;
        String invoicesCreated = "";
        String movFondosCreated ="";
        String chequesError ="";
            
        try {
            
            String[] arrCheques = cheques.split(",");
            
            for (int i = 0 ; i < arrCheques.length ; i ++ ) {
                Trx trx = Trx.get(Trx.createTrxName("BulkVoidChecks"), true);
                try {
                    
                    //Obtengo cheque
                    MVALORPAGO cheque = MVALORPAGO.getCheque(getCtx(), arrCheques[i].trim(),trx.getTrxName());

                    if (cheque == null){
                       throw new Exception( "Cheque no encontrado");
                    }

                    //Reviso estado (Debe ser emitido)
                    if (!cheque.getSTATE().equals(MVALORPAGO.EMITIDO)) {
                        throw new Exception( "Cheque no se encuentra en estado emitido");
                    }

                    //Obtengo el pago que lo genero
                    MPayment payment = new MPayment(getCtx(),cheque.getC_Payment_ID(),trx.getTrxName());
                    if (payment.getC_Payment_ID() == 0 ){
                        throw new Exception( "No se encontro Pago para cheque");
                    }

                    MBPartner partner = new MBPartner(getCtx(),payment.getC_BPartner_ID(),trx.getTrxName());

                    String description = "Anula OP "+payment.getDocumentNo()+" "+partner.getName() + " (Proceso Automático)";

                    //Genero movimiento de Fondos
                    MMOVIMIENTOFONDOS mov = new MMOVIMIENTOFONDOS(getCtx(),0,trx.getTrxName());
                    mov.setDateTrx(voidDate);
                    mov.setDateAcct(voidDate);
                    mov.setTIPO(MMOVIMIENTOFONDOS.TIPO_RechCqPropio);
                    mov.setC_DocType_ID(1000223);
                    mov.setDescription(description);

                    if (!mov.save()) {
                        throw new Exception( "Error al genera cabecera del movimiento");
                    }

                    MBankAccount bankAccount = new MBankAccount(getCtx(),cheque.getC_BankAccount_ID(),trx.getTrxName());

                    MMOVIMIENTOFONDOSDEB movDeb = new MMOVIMIENTOFONDOSDEB(getCtx(),0,trx.getTrxName()) ;
                    movDeb.setC_MOVIMIENTOFONDOS_ID(mov.getC_MOVIMIENTOFONDOS_ID());
                    movDeb.setC_AcctSchema_ID(p_AcctSchema_ID);
                    movDeb.setC_BankAccount_ID(cheque.getC_BankAccount_ID());
                    movDeb.setNroCheque(cheque.getNroCheque());
                    movDeb.setDEBITO(cheque.getIMPORTE());
                    movDeb.setMV_DEBITO_ACCT( getAccount(bankAccount.getAcct()));

                    movDeb.setC_VALORPAGO_ID(cheque.get_ID());
                    movDeb.setPAYMENTDATE(cheque.getPaymentDate());
                    movDeb.setTIPOCHEQUE(cheque.getTipoCheque());

                    if (!movDeb.save()){
                        throw new Exception( "Error al genera linea de debito para el movimiento");
                    }

                    MMOVIMIENTOFONDOSCRE movCre = new MMOVIMIENTOFONDOSCRE(getCtx(),0,trx.getTrxName()) ;
                    movCre.setC_MOVIMIENTOFONDOS_ID(mov.getC_MOVIMIENTOFONDOS_ID());
                    movCre.setC_AcctSchema_ID(p_AcctSchema_ID);
                    movCre.setCREDITO(cheque.getIMPORTE());
                    movCre.setMV_CREDITO_ACCT(getAccount(getChequesRechazadosAccount(p_AcctSchema_ID)));

                    if (!movCre.save()){
                        throw new Exception("Error al genera linea de debito para el movimiento" );
                    }

                    //Completo el movimiento de fondos
                    if (!mov.processIt(mov.DOCSTATUS_Completed)) {
                        throw new Exception("Error al completar Movimiento de Fondos");
                    }

                    //Guardo estado movimiento de fondos
                    if (!mov.save()) {
                        throw new Exception("Error al actualizar estado Movimiento de Fondos ");
                    }

                    /*cheque.setEstado(MVALORPAGO.RECHAZADO);
                    if (!cheque.save(get_TrxName())) {
                        countE++;
                        log.log(Level.SEVERE, "No ha sido posible actualizar el estado del cheque " + arrCheques[i].trim());
                        continue;
                    }*/

                    //Genero el ajuste de debito.
                    MInvoice ajd = new MInvoice(getCtx(),0,trx.getTrxName());
                    ajd.setC_DocTypeTarget_ID(1000246);
                    ajd.setC_DocType_ID(1000246);
                    ajd.setC_BPartner_ID(payment.getC_BPartner_ID());
                    ajd.setDateAcct(voidDate);
                    ajd.setDateInvoiced(voidDate);

                    Integer year = (Integer) voidDate.getYear() + 1900;
                    ajd.setDocumentNo(year.toString() + "-" + Integer.parseInt(payment.getDocumentNo())) ;

                    MBPartnerLocation[] locations = MBPartnerLocation.getForBPartner(getCtx(), payment.getC_BPartner_ID());

                    if (locations.length == 0) {
                        throw new Exception("Error al generar el ajuste de debito. No se encontro una direccion para el socio de negocio");
                    }

                    ajd.setC_BPartner_Location_ID(locations[0].getC_BPartner_Location_ID());
                    ajd.setC_Location_ID(locations[0].getC_Location_ID());
                    ajd.setBill_Location_ID(locations[0].getC_Location_ID());
                    ajd.setDescription(description);
                    ajd.setGrandTotal(cheque.getIMPORTE());
                    ajd.setTotalLines(cheque.getIMPORTE());
                    ajd.setC_PaymentTerm_ID(1000121);
                    ajd.setIsSOTrx(false);
                    if (!ajd.save()) {
                        //Se reintenta pero con un nuevo numero de documento.
                        ajd.setDocumentNo(year.toString() + "-" + Integer.parseInt(cheque.getNroCheque()));
                        if (!ajd.save()) {
                           throw new Exception("Error al generar el ajuste de debito");
                        }
                    }

                    //Creo linea del ajuste
                    MInvoiceLine line = new MInvoiceLine(getCtx(),0,trx.getTrxName());
                    line.setC_Invoice_ID(ajd.get_ID());
                    line.setC_Charge_ID(p_CHREC_Chargue);
                    line.setPrice(cheque.getIMPORTE());
                    line.setQty(1);
                    //EXCENTO
                    line.setC_Tax_ID(1000055);
                    if (!line.save()) {
                        throw new Exception("Error al generar linea del ajuste de credito");
                    }

                    //Completo Ajuste de Debito
                    if (!ajd.processIt(mov.DOCSTATUS_Completed)) {
                        throw new Exception("Error al completar Ajuste de Debito");
                    }

                    //Completo Ajuste de Debito
                    if (!ajd.save()) {
                        throw new Exception("Error al actualizar estado Ajuste de Debito");
                    }
                    
                     
                     trx.commit();
                     invoicesCreated += ajd.getDocumentNo() +',';
                     movFondosCreated += mov.getDocumentNo()+", ";                    
                }
                catch (Exception e) {
                    countE++;
                    log.log(Level.SEVERE, "Error al procesar cheque:" + arrCheques[i].trim() + ". Error: "+e.getMessage());
                    chequesError +=  arrCheques[i].trim() +',';
                    trx.rollback();
                }

                countOK++;
            }
            
        }
        catch (Exception e) {
            Env.setContext(getCtx(), "OmitAutomaticPayment", "N");
            throw e;
        }
            
        
        System.out.println("invoicesCreated: "+invoicesCreated);
        System.out.println("movFondosCreated: "+movFondosCreated);
        System.out.println("chequesError: "+chequesError);
        return "@Created@:"+countOK+"\n @Error@:"+countE + "\n Facturas: "+invoicesCreated+". \n MovFondos: "+movFondosCreated;

    }

    private int getChequesRechazadosAccount(int C_AccountSchema_ID) {
        int retValue = 0;
        
        String sql = "SELECT CQ_OWNREJ_ACCT "
                            + "FROM C_AcctSchema_Default "
                            + "WHERE C_AcctSchema_ID = ?";
        PreparedStatement pstm = DB.prepareStatement(sql, null);
        
        try {
            pstm.setInt(1, C_AccountSchema_ID);
            ResultSet rs = pstm.executeQuery();
            if (rs.next())
                retValue = rs.getInt(1);
        } catch (Exception e) {	
            log.log(Level.SEVERE, "Error al obtener cuenta contable para rechazo de cheques propios");
            e.printStackTrace();
        }     
        
        return retValue;
    }
    
    private int getAccount(int cuenta) {
        String sql = " SELECT  ACCOUNT_ID "
                     + " FROM C_VALIDCOMBINATION "
                     + " WHERE C_VALIDCOMBINATION_ID = ?";
        try {
            PreparedStatement pstmQuery = DB.prepareStatement(sql, null);
            pstmQuery.setLong(1, cuenta);
            ResultSet rs = pstmQuery.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
