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
package org.compiere.process;

import java.math.*;
import java.sql.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Import ICBC Check from I_ICBC_CHECK
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ImportGLJournal.java,v 1.23 2005/11/25 21:57:27 jjanke Exp $
 */
public class ImportICBCCheck extends SvrProcess {

    /**	Delete old Imported				*/
    private boolean m_DeleteOldImported = false;
    /**	Delete inactive			*/
    private boolean m_DeleteInactive = true;
    /**	Complete Payments			*/
    private boolean m_CompletePayments = true;
    /**	Complete Payments			*/
    private int ICBC_Account = 1000029;
    final String tableName = "I_ICBC_CHECK";

    /**
     *  Prepare - e.g., get Parameters.
     */
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (para[i].getParameter() == null)
            ; else if (name.equals("DeleteOldImported")) {
                m_DeleteOldImported = "Y".equals(para[i].getParameter());
            } else if (name.equals("DeleteInactive")) {
                m_DeleteInactive = "Y".equals(para[i].getParameter());
            } else if (name.equals("CompletePayments")) {
                m_CompletePayments = "Y".equals(para[i].getParameter());
            } else if (name.equals("ICBC_Account")) {
                ICBC_Account = para[i].getParameterAsInt();
            } else {
                log.log(Level.SEVERE, "Unknown Parameter: " + name);
            }
        }
    }	//	prepare

    /**
     *  Perrform process.
     *  @return Message
     *  @throws Exception
     */
    protected String doIt() throws java.lang.Exception {
        log.info("DeleteOldImported=" + m_DeleteOldImported + " + DeleteInactive" + m_DeleteInactive);
        StringBuffer sql = null;
        String clientCheck = " AND AD_Client_ID=" + getAD_Client_ID();
        String isActive = " AND i.IsActive= 'Y'";

        int no = 0;
        int maxNroCheque= 0;


        MPayment.blockCreatePayments('Y', MPayment.AD_PROCESS_ImportICBCCheck_ID);

        //	****	Prepare	****

        //	Delete Old Imported
        if (m_DeleteOldImported) {
            sql = new StringBuffer("DELETE FROM " + tableName
                    + " WHERE I_IsImported='Y'");
            no = DB.executeUpdate(sql.toString(), get_TrxName());
            log.fine("Delete Old Impored =" + no);
        }

        //	Delete Inactive record
        if (m_DeleteInactive) {
            sql = new StringBuffer("DELETE FROM " + tableName
                    + " WHERE isActive is null OR isActive='N'");
            no = DB.executeUpdate(sql.toString(), get_TrxName());
            log.fine("Delete Inactive =" + no);
        }

        //	Set IsActive, Created/Updated
        sql = new StringBuffer("UPDATE " + tableName
                + " SET IsActive = COALESCE (IsActive, 'Y'),"
                + " Created = COALESCE (Created, SysDate),"
                + " CreatedBy = COALESCE (CreatedBy, 0),"
                + " Updated = COALESCE (Updated, SysDate),"
                + " UpdatedBy = COALESCE (UpdatedBy, 0),"
                + " I_ErrorMsg = NULL,"
                + " I_IsImported = 'N' "
                + "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
        no = DB.executeUpdate(sql.toString(), get_TrxName());
        log.info("Reset=" + no);

        //	Set Business Partner
        sql = new StringBuffer("UPDATE " + tableName + " i "
                + "SET C_BPartner_ID=(SELECT C_BPartner_ID FROM C_BPartner bp"
                + " WHERE REPLACE(bp.taxid,'-','')=i.NUMERO_DOCUMENTO_BENEFICIARIO AND ROWNUM=1) "
                + "WHERE C_BPartner_ID IS NULL AND NUMERO_DOCUMENTO_BENEFICIARIO IS NOT NULL"
                + " AND I_IsImported<>'Y'").append(clientCheck).append(isActive);
        no = DB.executeUpdate(sql.toString(), get_TrxName());
        log.fine("Set Business Partner=" + no);


        sql = new StringBuffer("UPDATE " + tableName + " i "
                + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Business Partner, '"
                + "WHERE (C_BPartner_ID IS NULL OR C_BPartner_ID=0)"
                + " AND I_IsImported<>'Y'").append(clientCheck).append(isActive);
        no = DB.executeUpdate(sql.toString(), get_TrxName());
        if (no != 0) {
            log.warning("Invalid Business Partner=" + no);
        }

        //Validate nro cheque
        sql = new StringBuffer("UPDATE " + tableName + " i "
                + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Duplicated Check Number, '"
                + "WHERE EXISTS (SELECT 1 from C_ValorPago where NROCHEQUE = i.NUMERO_CHEQUE AND tipo = 'P' AND C_BankAccount_id = " + ICBC_Account + ")"
                + " AND I_IsImported<>'Y'").append(clientCheck).append(isActive);
        no = DB.executeUpdate(sql.toString(), get_TrxName());
        if (no != 0) {
            log.warning("Duplicated Check Number=" + no);
        }

        //	Count Errors
        int errors = DB.getSQLValue(get_TrxName(),
                "SELECT COUNT(*) FROM " + tableName + " i WHERE I_IsImported NOT IN ('Y','N')" + clientCheck + isActive);

        log.info("Validation Errors=" + errors);
        commit();

        /*********************************************************************/
        int noInsertLine = 0;
        int noInsertPayment = 0;
        int noProcessPayment = 0;

        //	Go through ICBC Check Records
        sql = new StringBuffer("SELECT * FROM " + tableName + " i "
                + "WHERE I_IsImported='N' ").append(clientCheck).append(isActive);

        PreparedStatement pstmt = null;

        try {
            pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
            ResultSet rs = pstmt.executeQuery();

            //
            while (rs.next()) {
                X_I_ICBC_CHECK imp = new X_I_ICBC_CHECK(getCtx(), rs, get_TrxName());
                MPayment payment = null;

                if (imp.getC_Payment_ID() == 0) //Create new Payment
                {
                    payment = new MPayment(getCtx(), 0, get_TrxName());
                } else {
                    payment = new MPayment(getCtx(), imp.getC_Payment_ID(), get_TrxName());
                }

                payment.byPassPaymentsBlock = true;
                payment.setAD_Org_ID(imp.getAD_Org_ID());
                payment.setC_DocType_ID(false);
                payment.setTrxType(MPayment.TRXTYPE_CreditPayment);
                payment.setAmount(118, imp.getMONTO());
                payment.setDateTrx(imp.getUpdated());
                payment.setDateAcct(imp.getUpdated());
                payment.setIsPrepayment(true);
                payment.setDescription("Pago generado automaticamente desde proceso \"Importar Cheques ICBC\"");
                payment.setC_BPartner_ID(imp.getC_BPartner_ID());

                MBPartnerLocation[] locations = MBPartnerLocation.getForBPartner(getCtx(), imp.getC_BPartner_ID());

                if (locations.length == 0) {
                    imp.setI_ErrorMsg("ERROR creating Payment: No Location por BusinessPartner");
                    imp.setI_IsImported(false);
                    imp.save();
                    continue;
                }

                payment.setC_BPartner_Location_ID(locations[0].getC_BPartner_Location_ID());

                try {
                    PreparedStatement pstm = DB.prepareStatement("SELECT C_JURISDICCION_ID FROM C_BPartner_Jurisdiccion"
                            + " WHERE C_BPartner_ID = ? ORDER BY C_JURISDICCION_ID", get_TrxName());
                    pstm.setInt(1, payment.getC_BPartner_ID());
                    ResultSet rs2 = pstm.executeQuery();

                    if (rs2.next()) {
                        payment.setC_JURISDICCION_ID(rs2.getInt(1));
                    } else {
                        payment.setC_JURISDICCION_ID(0);
                    }

                    pstm.close();
                    rs2.close();
                } catch (Exception e) {
                }

                MBPartner bPartner = new MBPartner(getCtx(), imp.getC_BPartner_ID(), get_TrxName());
                if (!bPartner.isEXENCIONGANANCIAS()) {
                    payment.setC_REGIMENGANANCIAS_V_ID("116 Profesionales");
                }

                if (!payment.save()) {
                    imp.setI_ErrorMsg("ERROR creating Payment");
                    imp.setI_IsImported(false);
                    imp.save();
                    continue;
                } else if (imp.getC_Payment_ID() == 0) {
                    noInsertPayment++;
                }

                imp.setC_Payment_ID(payment.getC_Payment_ID());
                imp.save();

                //	Lines
                MVALORPAGO line = null;
                if (imp.getC_VALORPAGO_ID() == 0) {
                    line = new MVALORPAGO(getCtx(), 0, get_TrxName());
                } else {
                    line = new MVALORPAGO(getCtx(), imp.getC_VALORPAGO_ID(), get_TrxName());
                }

                line.setC_Payment_ID(payment.getC_Payment_ID());
                line.setIMPORTE(imp.getMONTO());
                line.setAFavor(imp.getBENEFICIARIO());
                line.setC_BankAccount_ID(ICBC_Account); //ICBC Hardcoded
                String nroCheque = removePrefix(imp.getNUMERO_CHEQUE());
                line.setNroCheque(nroCheque);
                int currentNumeroCheque = Integer.parseInt(nroCheque);
                if (currentNumeroCheque > maxNroCheque)
                    maxNroCheque = currentNumeroCheque;
                
                line.setOrden(true);
                line.setTipoCheque(MVALORPAGO.DIFERIDO);
                line.setTIPO(MVALORPAGO.PROPIO);
                line.setEstado(MVALORPAGO.EMITIDO);
              
                line.setPaymentDate(imp.getFECHA_PAGO());
                //line.setReleasedDate(imp.getFECHA_EMICION_LISTA());
                line.setReleasedDate(payment.getDateTrx());

                if (!line.save()) {
                    imp.setI_ErrorMsg("ERROR creating Check");
                    imp.setI_IsImported(false);
                    imp.save();
                    continue;
                }
                
                if (imp.getC_VALORPAGO_ID() == 0) 
                    noInsertLine++;
                
                if (line.getC_VALORPAGO_ID() != 0) {
                    imp.setC_VALORPAGO_ID(line.getC_VALORPAGO_ID());
                    imp.save();
                }

                if (m_CompletePayments) { //CHeck for process payment
                    if (!payment.processIt(DocAction.ACTION_Complete)) {
                        imp.setI_ErrorMsg("ERROR processing payment: " + payment.getProcessMsg());
                        imp.setI_IsImported(false);
                        imp.save();
                        continue;
                    } else {
                        noProcessPayment++;
                    }
                }

                payment.save();
                imp.setI_IsImported(true);
                imp.setI_ErrorMsg(null);
                imp.setProcessed(true);

                imp.save();

            }	//	while records
            
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            MPayment.blockCreatePayments('N', MPayment.AD_PROCESS_ImportICBCCheck_ID);
            e.printStackTrace();
        }
        //	clean up
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException ex1) {
        }

        pstmt = null;
        
        //Finally update Sequence for BankAccount
        PreparedStatement pstm = DB.prepareStatement("SELECT * FROM C_BankAccountDoc"
                + " WHERE C_BankAccount_ID = ? ", get_TrxName());
        pstm.setInt(1, ICBC_Account);
        ResultSet rs2 = pstm.executeQuery();

        if (rs2.next()) {
            X_C_BankAccountDoc doc = new X_C_BankAccountDoc(getCtx(), rs2, get_TrxName());
            int currentNext = doc.getCurrentNext();
            if (maxNroCheque+1 > currentNext){
                 doc.setCurrentNext(maxNroCheque+1);
                 doc.save();
            }
           
        } 

        //	Set Error to indicator to not imported
        sql = new StringBuffer("UPDATE " + tableName + " i "
                + "SET I_IsImported='N', Updated=SysDate "
                + "WHERE I_IsImported<>'Y'").append(clientCheck).append(isActive);
        no = DB.executeUpdate(sql.toString(), get_TrxName());
        addLog(0, null, new BigDecimal(no), "@Errors@");
        //
        addLog(0, null, new BigDecimal(noInsertPayment), "Pagos creados");
        addLog(0, null, new BigDecimal(noInsertLine), "Cheques creados");
        addLog(0, null, new BigDecimal(noProcessPayment), "Pagos Procesados");
        MPayment.blockCreatePayments('N', MPayment.AD_PROCESS_ImportICBCCheck_ID);

        return "";
    }	//	doIt

    private String removePrefix(String value) {
        String regex = "^0+";
        return value.replaceAll(regex, "");
    }
    
    private BigDecimal getGananciasRate(MPayment payment, boolean tExencionGAN, BigDecimal MSR){
        
        String consulta = "SELECT CONDICIONGAN_ID, IVA "
                    + "FROM C_BPartner "
                    + "WHERE C_BPartner_ID = ?";

        PreparedStatement pstmt = DB.prepareStatement(consulta, get_TrxName());
       
        ResultSet rs;
        
        boolean tExento = false;
        boolean tRespInsc = false;
        boolean tNoContr = false;
        
        try {
            pstmt.setInt(1, payment.getC_BPartner_ID());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) == MBPartner.CGAN_EXENTO) {
                    tExento = true;
                } else if (rs.getInt(1) == MBPartner.CGAN_RESPINSCRIPTO) {
                    tRespInsc = true;
                } else if (rs.getInt(1) == MBPartner.CGAN_NOCATEGORIZADO) {
                    tNoContr = true;
                }
            }
       
        
         if ((!tExencionGAN) && (!tExento) && (tRespInsc || tNoContr)) {
                /*
                 *      Calculo de los pagos anteriores con ese tipo de retencion.
                 */

                consulta = "SELECT pago.C_Payment_Id, pago.PAYAMT, pago.RETENCIONGANANCIAS FROM C_PAYMENT pago "
                        + "INNER JOIN AD_ORGINFO orginfo ON pago.AD_ORG_ID = orginfo.AD_ORG_ID "
                        + "WHERE pago.C_REGIMENGANANCIAS_V_ID = '116 Profesionales' AND orginfo.AGENTE = 'Y' "
                        + "AND pago.C_DocType_ID = 1000138 AND to_char(pago.datetrx, 'yy') = to_char(?, 'yy') "
                        + "AND to_char(pago.datetrx, 'mm') = to_char(?, 'mm') "
                        + "AND pago.C_Payment_ID <> ? AND (pago.DOCSTATUS = 'CO' OR pago.DOCSTATUS = 'CL') AND pago.C_BPARTNER_ID = " + payment.getC_BPartner_ID();

                pstmt = DB.prepareStatement(consulta, get_TrxName());
                pstmt.setTimestamp(1, payment.getDateTrx());
                pstmt.setTimestamp(2, payment.getDateTrx());
                pstmt.setInt(3, payment.getC_Payment_ID());

                rs = pstmt.executeQuery();

                MPayment paymentAnt;
                BigDecimal tSumant = BigDecimal.ZERO;
                BigDecimal tSumretencionganancias =  BigDecimal.ZERO;

                while (rs.next()) {


                    paymentAnt = new MPayment(Env.getCtx(), rs.getInt(1), get_TrxName());

                    BigDecimal valueRoundAnt = rs.getBigDecimal(2).multiply(paymentAnt.getCotizacion());

                    tSumant = tSumant.add(valueRoundAnt);
                    tSumretencionganancias = tSumretencionganancias.add(rs.getBigDecimal(3));

                }

                rs.close();
                pstmt.close();
                
                // INICIO - Buscar el Monto No Sujeto a Retenci�n.
                consulta = "SELECT MNSR "
                        + "FROM C_WITHHOLDING "
                        + "WHERE ISACTIVE='Y' AND REGIMENGANANCIAS = ? AND NAME = 'RETGAN'";

                pstmt = DB.prepareStatement(consulta, get_TrxName());
                pstmt.setString(1, payment.getC_REGIMENGANANCIAS_V_ID());

                rs = pstmt.executeQuery();

                BigDecimal MNSR = Env.ZERO;

                if (rs.next()) {
                    MNSR = rs.getBigDecimal(1);
                }
                //	FIN - Buscar el Monto No Sujeto a Retenci�n.
                
                /*
                 *  MSRFINAL - MONTO SUJETO A RETENER FINAL PARA GANANCIAS, TENIENDO EN CUENTA
                 *  		LOS PAGOS ANTERIORES Y EL MONTO NO SUJETO A RETENER.	
                 */
                BigDecimal MSRFINAL = tSumant.add(MSR).subtract(MNSR);

                /*
                 *  Modificado para que haga el control de los decimales de presición (a dos decimales)
                 *  Zynnia - José Fantasia
                 *  19/03/2012
                 * 
                 */

                // Conseguir Datos para realizar el c�lculo.
                if (tRespInsc) {

                    consulta = "SELECT FIXAMT, PERCENT, MINAMT, THRESHOLDMIN, THRESHOLDMAX "
                            + "FROM C_WITHHOLDING "
                            + "WHERE ISACTIVE='Y' AND REGIMENGANANCIAS = ? AND NAME = 'RETGAN' "
                            + "ORDER BY THRESHOLDMIN";

                } else {

                    consulta = "SELECT FIXAMT, PERCENT, MINAMT, THRESHOLDMIN, THRESHOLDMAX "
                            + "FROM C_WITHHOLDING "
                            + "WHERE ISACTIVE='Y' AND REGIMENGANANCIAS = ? AND NAME = 'RETGANNC' "
                            + "ORDER BY THRESHOLDMIN";

                }

                pstmt = DB.prepareStatement(consulta, get_TrxName());
                pstmt.setString(1, payment.getC_REGIMENGANANCIAS_V_ID());

                rs = pstmt.executeQuery();

                boolean cont = true;

                while (rs.next() && cont) {

                    BigDecimal tTotalfijoGAN = rs.getBigDecimal(1);
                    BigDecimal tPorcentajeGAN = rs.getBigDecimal(2);
                    BigDecimal tTotalminimoGAN = rs.getBigDecimal(3);
                    BigDecimal tLimiteminimoGAN = rs.getBigDecimal(4);
                    BigDecimal tLimitemaximoGAN = rs.getBigDecimal(5);

                    int compararLimiteMinimo = MSRFINAL.compareTo(tLimiteminimoGAN);
                    int compararLimiteMaximo = MSRFINAL.compareTo(tLimitemaximoGAN);



                    if (((compararLimiteMinimo == 0 || compararLimiteMinimo == 1)
                            && compararLimiteMaximo == -1) || (tLimiteminimoGAN.equals(Env.ZERO)
                            && tLimitemaximoGAN.equals(Env.ZERO))) {

                        //Necesito saber de cuanto sera la retencion teniendo en cuenta el monto final
                        BigDecimal RETGAN = ((MSR.subtract(tLimiteminimoGAN)).multiply((tPorcentajeGAN.divide(BigDecimal.valueOf(100))))).add(tTotalfijoGAN).subtract(tSumretencionganancias);

                        // Si mi pago es de 5000 neto, y la retencion me da 1000 entonces el monto final debe ser 6000?
                        // NO -> Con 6000 la retencion ganancias ya seria distinta
                        // GANANCIAS = ( ( SUMAANT + NETO/1,21 + GANANCIAS/1,21 - MNSR - tLimiteminimoGAN ) *  tPorcentajeGAN/100  ) + tTotalfijoGAN - tSumretencionganancias
                        // GANANCIAS = ( ( GANANCIAS + NETO - tLimiteminimoGAN ) *  tPorcentajeGAN/100  ) + tTotalfijoGAN - tSumretencionganancias
                        // GANANCIAS = GANANCIAS  *  tPorcentajeGAN/100 + ( ( NETO - tLimiteminimoGAN ) *  tPorcentajeGAN/100  ) + tTotalfijoGAN - tSumretencionganancias
                        // GANANCIAS - ( GANANCIAS  *  tPorcentajeGAN/100 )  = ( ( NETO - tLimiteminimoGAN ) *  tPorcentajeGAN/100  ) + tTotalfijoGAN - tSumretencionganancias
                        // GANANCIAS * ( 1/1,21 -  tPorcentajeGAN/100 )  = ( ( SUMAANT + NETO/1,21 - MNSR - tLimiteminimoGAN ) *  tPorcentajeGAN/100  ) + tTotalfijoGAN - tSumretencionganancias
                        
                        // GANANCIAS  = ( ( ( SUMAANT + NETO/1,21 - MNSR - tLimiteminimoGAN ) *  tPorcentajeGAN/100  ) + tTotalfijoGAN - tSumretencionganancias) / ( 1/1,21 -  tPorcentajeGAN/100 )
                        //   (( 49356 - 32000 ) * 0,19 + 3280 + 0 ) / 0,81 = 8120,54
                        // TOTAL PAGO = 49.356,00 + 8120,54
                        
                        // MODIFICACION TENIENDO EN CUENTA MSRFINAL
                        // GANANCIAS  = ( ( ( SUMAANT + NETO/1,21 - MNSR - tLimiteminimoGAN ) *  tPorcentajeGAN/100  ) + tTotalfijoGAN - tSumretencionganancias) / ( 1/1,21 -  tPorcentajeGAN/100 )
                        //   (( 8264,46 + 49356/1,21 - 16830 - 32000 ) * 0,19 + 3280 + 0 ) / 0,64 = 8120,54
                        //   (( 8264,46 + 40790,08 - 16830 - 32000 ) * 0,19 + 3280 + 0 ) / 0,64 = 5191,66   
                        
                         // TOTAL PAGO = 49.356,00 +  5191,66= 

                        
                    }
                }
                
            }
                
        } catch (SQLException ex) {
            Logger.getLogger(ImportICBCCheck.class.getName()).log(Level.SEVERE, null, ex);
        }

       
        return BigDecimal.ZERO;
    }
}	//	ImportGLJournalLines