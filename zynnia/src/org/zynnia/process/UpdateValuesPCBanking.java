/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zynnia.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.compiere.model.MBankAccount;
import org.compiere.model.MVALORPAGO;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * This process performs an update on Payment Values with type PC Banking
 * Can change the payment date, release date, or check number
 * If check number is changed, then the account facts for PC Banking payment value
 * is updated too, following the current format Banco_Número_Fecha Vto
 *
 * @author Ezequiel Scott @ Zynnia
 */
public class UpdateValuesPCBanking extends SvrProcess{

    private int C_VALORPAGO_ID;
//    private Timestamp paymentDate;
//    private Timestamp releaseDate;
    private int C_Payment_ID;

    private String nroCheque;
    private BigDecimal importe;

    /**
	 *  Perform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
    protected String doIt() throws Exception{
        String msg = "Cambios realizados para el valor PC Banking ";

        // Update the fields only if is not null

        if (importe != null) {
            String sql = "UPDATE C_VALORPAGO"
                + " SET importe = ?"
                + " WHERE IsActive='Y' And C_VALORPAGO_ID=?";

            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setBigDecimal(1, importe);
            pstmt.setInt(2, C_VALORPAGO_ID);

            int count = pstmt.executeUpdate();

            log.info("Updated " + count + " with "  + sql);
            msg += " - Nuevo Importe = " + importe;
        }
        if (nroCheque != null) {
            updateFacts(C_VALORPAGO_ID);

            String sql = "UPDATE C_VALORPAGO"
                + " SET NROCHEQUE = ?"
                + " WHERE IsActive='Y' And C_VALORPAGO_ID=?";

            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setString(1, nroCheque);
            pstmt.setInt(2, C_VALORPAGO_ID);

            int count = pstmt.executeUpdate();

            log.info("Updated " + count + " with "  + sql);
            msg += " - Nuevo Nro. Cheque = " + nroCheque + "\n";
        }
//        if (paymentDate != null) {
//            String sql = "UPDATE C_VALORPAGO"
//                + " SET PAYMENTDATE = ?"
//                + " WHERE IsActive='Y' And C_VALORPAGO_ID=?";
//            PreparedStatement pstmt = DB.prepareStatement(sql, null);
//            pstmt.setTimestamp(1, paymentDate);
//            pstmt.setInt(2, C_VALORPAGO_ID);
//
//            int count = pstmt.executeUpdate();
//
//            log.info("Updated " + count + " with "  + sql);
//            msg += " - Nueva Fecha de Pago = " + paymentDate + "\n";
//        }
//        if (releaseDate != null) {
//            String sql = "UPDATE C_VALORPAGO"
//                + " SET PAYMENTDATE = ?"
//                + " WHERE IsActive='Y' And C_VALORPAGO_ID=?";
//            PreparedStatement pstmt = DB.prepareStatement(sql, null);
//            pstmt.setTimestamp(1, releaseDate);
//            pstmt.setInt(2, C_VALORPAGO_ID);
//
//            int count = pstmt.executeUpdate();
//
//            log.info("Updated " + count + " with "  + sql);
//            msg += " - Nueva Fecha de Emisión = " + releaseDate + "\n";
//        }

        return msg;
    }

    @Override
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_Payment_ID"))
                C_Payment_ID =  para[i].getParameterAsInt();
            else if (name.equals("C_VALORPAGO_ID"))
				C_VALORPAGO_ID = para[i].getParameterAsInt();
			else if (name.equals("IMPORTE"))
				importe =  (BigDecimal) para[i].getParameter();
            else if (name.equals("NROCHEQUE"))
				nroCheque = (String) para[i].getParameter();
//			else if (name.equals("PAYMENTDATE"))
//				paymentDate = (Timestamp)para[i].getParameter();
//            else if (name.equals("RELEASEDATE"))
//				releaseDate = (Timestamp)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
    }

    /**
     * Update Account Facts for the given payment value
     * that match with description as Banco_Número_Fecha Vto
     *
     * @param C_ValorPago_ID
     * @return
     */
    private boolean updateFacts(int C_ValorPago_ID){
        // First, select the facts to update
        MVALORPAGO pay = new MVALORPAGO(Env.getCtx(), C_ValorPago_ID, null);

        //new and old descriptions follow the format: Banco_Número_Fecha Vto
        String oldDescription;
        String newDescription;
        MBankAccount bank = MBankAccount.get(Env.getCtx(), pay.getC_BankAccount_ID());
//        if (pay.getVencimientoDate() != null) {
//            oldDescription = bank.getName() + "_" + pay.getNroCheque() + "_" + pay.getVencimientoDate().toString();
//            newDescription = bank.getName() + "_" + nroCheque + "_" + pay.getVencimientoDate().toString();
//        }
        oldDescription = bank.getName() + "_" + pay.getNroCheque();
        newDescription = bank.getName() + "_" + nroCheque;

        String sql = "SELECT fact_acct_id FROM fact_acct WHERE description LIKE '" + oldDescription + "'";
        PreparedStatement pstmt = DB.prepareStatement(sql, null);
        log.log(Level.INFO, "Search for FACT_ACT with description: {0}", oldDescription);

        ResultSet rs;
        try {
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int fact_acct_id = rs.getInt(1);

                // AD_TABLE_ID = 335 Payment table
                String sql2 = "UPDATE FACT_ACCT"
                    + " SET DESCRIPTION=?"
                    + " WHERE fact_acct_id=?";
                PreparedStatement pstmt2 = DB.prepareStatement(sql2, null);
                pstmt2.setString(1, newDescription);
                pstmt2.setInt(2, fact_acct_id);
                ResultSet rs1 = pstmt2.executeQuery();

                if (rs1.next()) {
                    log.log(Level.INFO, "Updated FACT_ACT description: {0}", fact_acct_id);
                    return true;
                } else {
                    log.log(Level.INFO, "No FACT_ACT with description: {0}", oldDescription);
                    return false;
                }
            } else {
                log.log(Level.INFO, "No FACT_ACT with description: {0}", oldDescription);
                return false;
            }

        } catch (SQLException ex) {
            log.log(Level.SEVERE, null, ex);
        }

        return false;
    }
}
