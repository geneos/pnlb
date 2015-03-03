/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;
import java.math.BigDecimal;
import java.sql.SQLException;

import org.compiere.model.MBankAccount;
import org.compiere.model.MCurrency;
import org.compiere.model.MDocType;
import org.compiere.model.MPAYMENTVALORES;
import org.compiere.model.MPAYMENTRET;
import org.compiere.model.MPayment;
import org.compiere.model.MVALORPAGO;
import org.compiere.process.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.logging.*;
import org.compiere.apps.ADialog;
import org.compiere.model.*;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.print.Viewer;

import org.compiere.util.*;
import org.eevolution.tools.DateTimeUtil;
import org.eevolution.tools.Numero2Letras;
import org.eevolution.tools.UtilProcess;
/**
 *
 * @author Bision
 */
public class GeneratePagos extends SvrProcess{

	int C_Currency_ID;
	int p_instance;
        long org;
        BigDecimal sumaTotal = Env.ZERO;

     protected String doIt() throws Exception{
        listado();
        pie();
        cabecera();
        total();
        
        UtilProcess.initViewer("PF_PAGOS",p_instance,getProcessInfo());
        
        /*
         *  Desarrollo Etapa 6 - Impresión conjunta de OP y COmprobantes de Retenciones
         *  Zynnia 21/09/2012
         *  José Fantasia
         * 
         * 
         */
        
        /*
         *  Recupero las retenciones y segun el tipo,
         *  llamo a cada uno de los formatos de impresión que corresponden.
         * 
         */
        
        MPayment payment = new MPayment(Env.getCtx(),getRecord_ID(),null);
        List<PO> reten = payment.getRetenciones();
        MPrintFormat format = null;
        Language language = Language.getLoginLanguage();
        MPAYMENTRET ret = null;
        PrintInfo info = null;
        ReportEngine re = null;
        
        for (int i=0; i<reten.size(); i++) {
        
            ret = (MPAYMENTRET)reten.get(i);
            
            /*            
                Tipos en tipo_ret

                G = Ganancias
                B = Ingresos Brutos
                I = IVA
                S = SUSS            
            * 
            */
            
            if(ret.getTIPO_RET().equals("G")) {

                format = MPrintFormat.get(Env.getCtx(), 1000910, false);
                format.setLanguage(language);
                format.setTranslationLanguage(language);

                MQuery query = new MQuery("RV_PAGO_RETENCIONESGAN");
                query.addRestriction("C_Payment_ID", MQuery.EQUAL, getRecord_ID());

                info = new PrintInfo("RV_PAGO_RETENCIONESGAN",1000224, getRecord_ID());
                re = new ReportEngine(Env.getCtx(), format, query, info);

                new Viewer(re);                
                
            } else if(ret.getTIPO_RET().equals("I")) {

                format = MPrintFormat.get(Env.getCtx(), 1000991, false);
                format.setLanguage(language);
                format.setTranslationLanguage(language);

                MQuery query = new MQuery("RV_PAGO_RETENCIONESIVA");
                query.addRestriction("C_Payment_ID", MQuery.EQUAL, getRecord_ID());

                info = new PrintInfo("RV_PAGO_RETENCIONESIVA",1000291, getRecord_ID());
                re = new ReportEngine(Env.getCtx(), format, query, info);

                new Viewer(re);                
                
            } else if(ret.getTIPO_RET().equals("S")) {

                format = MPrintFormat.get(Env.getCtx(), 1000992, false);
                format.setLanguage(language);
                format.setTranslationLanguage(language);

                MQuery query = new MQuery("RV_PAGO_RETENCIONESSUSS");
                query.addRestriction("C_Payment_ID", MQuery.EQUAL, getRecord_ID());

                info = new PrintInfo("RV_PAGO_RETENCIONESSUSS",1000292, getRecord_ID());
                re = new ReportEngine(Env.getCtx(), format, query, info);

                new Viewer(re);                
                
            } else if(ret.getTIPO_RET().equals("B")) {

                format = MPrintFormat.get(Env.getCtx(), 1000909, false);
                format.setLanguage(language);
                format.setTranslationLanguage(language);

                MQuery query = new MQuery("RV_PAGO_RETENCIONESIB");
                query.addRestriction("C_Payment_ID", MQuery.EQUAL, getRecord_ID());

                info = new PrintInfo("RV_PAGO_RETENCIONESIB",1000223, getRecord_ID());
                re = new ReportEngine(Env.getCtx(), format, query, info);

                new Viewer(re);                
                
            }

        }
	
        

        return "success";
        
    }

     protected void listado(){
         try {
            String sqlQuery = "",sqlInsert = "";
            PreparedStatement pstmtInsert = null;
            String sqlRemove = "delete from T_PAGO_LISTADO";
            DB.executeUpdate(sqlRemove, null);
            sqlRemove = "delete from T_PAGO_VALORES";
            DB.executeUpdate(sqlRemove, null);
            sqlQuery = "select ad_client_id,ad_org_id,nro,fecha,tipo,total,formapago,c_bankaccount_id,pago,total,c_payment_id from RV_PAGO_LISTADO" + " where c_payment_id=" + getRecord_ID()+ " order by fecha";
            PreparedStatement pstmt = DB.prepareStatement(sqlQuery,get_TrxName());
            ResultSet rs = pstmt.executeQuery();
            BigDecimal total = Env.ZERO;
            long AD_Client_ID = 0;
            long AD_Org_ID = 0;
            while (rs.next()) {
                sqlInsert = "insert into T_PAGO_LISTADO values(?,?,'Y',?,?,?,?,?,?,?)";
                pstmtInsert = DB.prepareStatement(sqlInsert,get_TrxName());
                AD_Client_ID = rs.getLong(1);
                AD_Org_ID = rs.getLong(2);
                pstmtInsert.setLong(1, AD_Client_ID);
                pstmtInsert.setLong(2, AD_Org_ID);
                pstmtInsert.setInt(3, p_instance);
                pstmtInsert.setDate(4, rs.getDate(4));
                pstmtInsert.setString(5, rs.getString(5));
                pstmtInsert.setString(6, rs.getString(3));
                pstmtInsert.setString(7, null);
                pstmtInsert.setBigDecimal(8, rs.getBigDecimal(10));
                total = total.add(rs.getBigDecimal(10));
                pstmtInsert.setLong(9, rs.getLong(11));

                pstmtInsert.executeQuery();
                DB.commit(true, get_TrxName());
            }
            if (!total.equals(Env.ZERO))
            {
            	sqlInsert = "insert into T_PAGO_LISTADO values(?,?,'Y',?,?,?,?,?,?,?)";
                pstmtInsert = DB.prepareStatement(sqlInsert,get_TrxName());
                pstmtInsert.setLong(1, AD_Client_ID);
                pstmtInsert.setLong(2, AD_Org_ID);
                pstmtInsert.setInt(3, p_instance);
                pstmtInsert.setDate(4, null);
                pstmtInsert.setString(5, "Total");
                pstmtInsert.setString(6, null);
                pstmtInsert.setString(7, MCurrency.getISO_Code(getCtx(), C_Currency_ID));
                pstmtInsert.setBigDecimal(8, total);
                pstmtInsert.setLong(9, getRecord_ID());

                pstmtInsert.executeQuery();
                DB.commit(true, get_TrxName());
            }

            // para completar la forma de pago
            total = Env.ZERO;
            
            // Agregado de columna de fecha de pago paymentdate listar la fecha para los cheques
            // José Fantasia
            // Zynnia
            
            
            sqlQuery =  "select ad_client_id,ad_org_id,C_BANKACCOUNT_ID,IMPORTE, tipo, "+
                        "nrocheque, nrotransferencia, vencimientodate, banco, debitodate, c_paymentvalores_id, paymentdate, concepto "+
                        "from c_valorpago where c_payment_id="+getRecord_ID();
            pstmt = DB.prepareStatement(sqlQuery,get_TrxName());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                sqlInsert = "insert into T_PAGO_VALORES values(?,?,'Y',?,?,?,?,?,?)";
                pstmtInsert = DB.prepareStatement(sqlInsert,get_TrxName());
                pstmtInsert.setLong(1, rs.getLong(1));
                pstmtInsert.setLong(2, rs.getLong(2));
                pstmtInsert.setInt(3, p_instance);
                if (rs.getString(5).equals(MVALORPAGO.CHEQUEPROPIO))
                {
                	pstmtInsert.setString(4, "Cheque Propio");
                	MBankAccount bank = new MBankAccount(getCtx(),rs.getInt(3),get_TrxName());
                	if (rs.getString(8) != null)
                		pstmtInsert.setString(5, bank.getName() + " - " + rs.getString(6) + " - " + DateTimeUtil.parserFecha(rs.getTimestamp(8)));
                	else
                		pstmtInsert.setString(5, bank.getName() + " - " + rs.getString(6));
                        
                        System.out.println(rs.getDate(12));
                        pstmtInsert.setDate(8, rs.getDate(12));
                        
                } else if (rs.getString(5).equals(MVALORPAGO.CHEQUERECIBIDO)) {
                	pstmtInsert.setString(4, "Cheque de Tercero");
                	MPAYMENTVALORES payval = new MPAYMENTVALORES(getCtx(),rs.getInt(11),get_TrxName());

                        // Agregado de columna de fecha de pago paymentdate listar la fecha para los cheques
                        // José Fantasia
                        // Zynnia                        

                        pstmtInsert.setString(5, rs.getString(9) + " - " + payval.getNroCheque());
                        
                } else if (rs.getString(5).equals(MVALORPAGO.BANCO)) {
                	pstmtInsert.setString(4, "Transferencia " );
                        
                        // Agregado de columna de fecha de pago paymentdate listar la fecha para los cheques
                        // José Fantasia
                        // Zynnia
                        
                        pstmtInsert.setDate(8, rs.getDate(10));

                        /*
                         * Ticket 88 - Added the bank name into field "Concept"
                         * Ezequiel Scott @ Zynnia
                         */
                        MBankAccount bank = new MBankAccount(getCtx(),rs.getInt(3),get_TrxName());
                	pstmtInsert.setString(5, bank.getName() + " - " + rs.getString(7) + " - " + DateTimeUtil.parserFecha(rs.getTimestamp(10)));
                } else if (rs.getString(5).equals(MVALORPAGO.EFECTIVO)) {
                	pstmtInsert.setString(4, "Efectivo");
                	pstmtInsert.setString(5, rs.getString(13));
                        pstmtInsert.setDate(8, null);
                } else if (rs.getString(5).equals(MVALORPAGO.TARJETACREDITO)) {
                	pstmtInsert.setString(4, "Tarjeta de Crédito");
                	pstmtInsert.setString(5, rs.getString(13));
                        pstmtInsert.setDate(8, null);
                /*
                 *  Anexo para impresión de PC Bancking
                 *  Zynnia 29/03/2012
                 * 
                 */
                
                } else if (rs.getString(5).equals(MVALORPAGO.PCBANKING)) {
                	pstmtInsert.setString(4, "PC Banking");
                	MBankAccount bank = new MBankAccount(getCtx(),rs.getInt(3),get_TrxName());
                	if (rs.getString(8) != null)
                		pstmtInsert.setString(5, bank.getName() + " - " + rs.getString(6) + " - " + DateTimeUtil.parserFecha(rs.getTimestamp(8)));
                	else
                		pstmtInsert.setString(5, bank.getName() + " - " + rs.getString(6));
                        
                        System.out.println(rs.getDate(12));
                        pstmtInsert.setDate(8, rs.getDate(12));
                }

                MPayment pay = new MPayment(getCtx(),getRecord_ID(),null);
                
                BigDecimal importe = rs.getBigDecimal(4);
                
                /*
                if (rs.getString(5).equals(MVALORPAGO.BANCO)) {
                    importe = rs.getBigDecimal(4).multiply(pay.getCotizacion());
                } else {
                    importe = rs.getBigDecimal(4);
                }
                */
                    
                
                pstmtInsert.setBigDecimal(6, importe);
               
                total = total.add(importe);
                pstmtInsert.setLong(7, getRecord_ID());

                pstmtInsert.executeQuery();
                DB.commit(true, get_TrxName());
            }
            if (!total.equals(Env.ZERO))
            {
            	sqlInsert = "insert into T_PAGO_VALORES values(?,?,'Y',?,?,?,?,?,?)";
                pstmtInsert = DB.prepareStatement(sqlInsert,get_TrxName());
                pstmtInsert.setLong(1, AD_Client_ID);
                pstmtInsert.setLong(2, AD_Org_ID);
                pstmtInsert.setInt(3, p_instance);
                pstmtInsert.setString(4, null);
                pstmtInsert.setString(5, "Total Pesos");
                MPayment pay = new MPayment(getCtx(),getRecord_ID(),null);
                MCurrency moneda = MCurrency.get (Env.getCtx(), pay.getC_Currency_ID());
                if (moneda != null) {                    
                    total = total.setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
                }                    
                pstmtInsert.setBigDecimal(6, total);
                pstmtInsert.setLong(7, getRecord_ID());
                pstmtInsert.setDate(8, null);

                pstmtInsert.executeQuery();
                DB.commit(true, get_TrxName());
                sumaTotal = sumaTotal.add(total);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GeneratePagos.class.getName()).log(Level.SEVERE, null, ex);
        }
     }

     protected void cabecera(){
        try {
            
            // para el pago total
            BigDecimal total=Env.ZERO;
            
            /* Esto no sirve puesto que luego redefine total = pay.getPAYNET();
             * 
            String sql = "select RETENCIONIVA,RETENCIONGANANCIAS,RETENCIONSUSS,RETENCIONIB,TOTAL from RV_PAGO_PIE where c_payment_id="+ getRecord_ID();
            PreparedStatement pstmt2 = DB.prepareStatement(sql,get_TrxName());
            ResultSet rs2 = pstmt2.executeQuery();
            if(rs2.next()){
                total = rs2.getBigDecimal(5) - rs2.getBigDecimal(1) - rs2.getBigDecimal(2) - rs2.getBigDecimal(3) - rs2.BigDecimal(4);
            }
            rs2.close();
            pstmt2.close();             
             * 
             */
            
            MPayment pay = new MPayment(Env.getCtx(),getRecord_ID(),get_TrxName());
            total = pay.getPAYNET();

            String sqlQuery = "",sqlInsert = "",importe="";
            PreparedStatement pstmtInsert = null;
            String sqlRemove = "delete from T_PAGO_CABECERA";
            DB.executeUpdate(sqlRemove, null);
            Numero2Letras conver = new Numero2Letras();
            
            /*
             *  Zynnia 14/06/2012
             *  Agregado DOCSTATUS para mostrar el estado del Pago
             *  Majo
             * 
             */
            
            sqlQuery = "select AD_CLIENT_ID,AD_ORG_ID,C_PAYMENT_ID,NOMBRE,NRO,FECHA,CUIT,IIBB,DIR,CPCUI,PAIS,IDPROV,PROVEEDOR,CUITPROV,DIRPROV,CPCUIPROV,PAISPR,LEYENDA1,LEYENDA2,LEYENDA3,DOCSTATUS from RV_PAGO_CABECERA" + " where c_payment_id=" + getRecord_ID();
            PreparedStatement pstmt = DB.prepareStatement(sqlQuery,get_TrxName());
            ResultSet rs = pstmt.executeQuery();
            try {
                if (rs.next()) {
                    /*
                     *  Zynnia 14/06/2012
                     *  Agregado ? para insertar el estado del Pago
                     *  Majo
                     * 
                     */                    
                    sqlInsert = "insert into T_PAGO_CABECERA values(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pstmtInsert = DB.prepareStatement(sqlInsert,get_TrxName());
                    pstmtInsert.setLong(1, rs.getLong(1));
                    pstmtInsert.setLong(2, rs.getLong(2));
                    pstmtInsert.setInt(3, p_instance);
                    pstmtInsert.setLong(4, rs.getLong(3));
                    pstmtInsert.setString(5, rs.getString(5));
                    pstmtInsert.setString(6, rs.getString(9));
                    pstmtInsert.setDate(7, rs.getDate(6));
                    pstmtInsert.setString(8, rs.getString(10));
                    pstmtInsert.setString(9, rs.getString(7));
                    pstmtInsert.setString(10, rs.getString(8));
                    pstmtInsert.setString(11, rs.getString(11));
                    pstmtInsert.setString(12, rs.getString(13));
                    pstmtInsert.setString(13, rs.getString(15));
                    pstmtInsert.setString(14, rs.getString(16));
                    pstmtInsert.setString(15, rs.getString(17));
                    pstmtInsert.setString(16, rs.getString(12));
                    pstmtInsert.setString(17, rs.getString(14));
                    pstmtInsert.setString(18, rs.getString(4));
                    pstmtInsert.setString(19, rs.getString(18));
                    org = rs.getLong(2);
                    //para calculo del importe
                    String moneda;
                    if(rs.getString(19)!=null)
                        moneda = rs.getString(19);
                    else
                        moneda = " ";
                    
                    /*
                     *  Modificado para que haga el control de los decimales de presición (a dos decimales)
                     *  Zynnia - José Fantasia
                     *  19/03/2012
                     * 
                     */

                    MCurrency curr = MCurrency.get (Env.getCtx(), pay.getC_Currency_ID());
                    if (curr != null) {
                        total = total.setScale(curr.getStdPrecision(),BigDecimal.ROUND_HALF_UP);                        
                    }                        
                    
                   
                    BigDecimal aux = total.subtract(BigDecimal.valueOf(total.intValue()));
                    aux = aux.multiply(BigDecimal.valueOf(100));              

                    
                    if(total.equals(Env.ZERO))
                        importe=" de "+ moneda + " cero en concepto";
                    else{
                        if(aux.equals(Env.ZERO))
                            importe = " de " + moneda + " " + conver.convertirLetras(total.intValue()) + " en concepto";
                        else
                            importe = " de " + moneda + " " + conver.convertirLetras(total.intValue()) + " con " + conver.convertirLetras(aux.intValue())+ " centavos en concepto";
                    }
                    pstmtInsert.setString(20, importe);
                    pstmtInsert.setString(21, rs.getString(20));
                    
                    /*
                     *  Zynnia 14/06/2012
                     *  Agregado para mostrar el estado del Pago
                     *  Majo
                     * 
                     */
                    
                    pstmtInsert.setString(22, rs.getString(21));
                    pstmtInsert.executeQuery();
                    DB.commit(true, get_TrxName());
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
           } catch (SQLException ex) {
            Logger.getLogger(GeneratePagos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void pie(){
        try {
            String sqlQuery = "",sqlInsert = "";
            PreparedStatement pstmtInsert = null;
            String sqlRemove = "delete from T_PAGO_PIE";
            DB.executeUpdate(sqlRemove, null);
            BigDecimal total = Env.ZERO;
            sqlQuery = "select ad_client_id,ad_org_id,c_payment_id,datetrx,importe,tipo_ret,documentno,c_doctype_id, tipo_ret, alicuota FROM C_PaymentRet where c_payment_id=" + getRecord_ID();
            PreparedStatement pstmt = DB.prepareStatement(sqlQuery,get_TrxName());
            ResultSet rs = pstmt.executeQuery();
            long AD_Client_ID = 0;
            long AD_Org_ID = 0;
            while (rs.next()) {
                sqlInsert = "insert into T_PAGO_PIE values(?,?,'Y',?,?,?,?,?,?)";
                pstmtInsert = DB.prepareStatement(sqlInsert,get_TrxName());
                AD_Client_ID = rs.getLong(1);
                AD_Org_ID = rs.getLong(2);
                pstmtInsert.setLong(1, AD_Client_ID);
                pstmtInsert.setLong(2, AD_Org_ID);
                pstmtInsert.setInt(3, p_instance);
                pstmtInsert.setDate(4, rs.getDate(4));
                MDocType dt = MDocType.get(getCtx(), rs.getInt(8));
                String detalle = dt.getName();

                if (rs.getString(9).equals(MPAYMENTRET.TIPO_RET_Ganancias))
                {
                	MPayment pay = new MPayment(getCtx(),getRecord_ID(),get_TrxName());
                	String sql = " Select l.NAME FROM AD_REF_LIST l " +
                				 " INNER JOIN AD_REFERENCE r ON (r.ad_reference_id = l.ad_reference_id)" +
                				 " WHERE l.VALUE = '" + pay.getC_REGIMENGANANCIAS_V_ID() + "'" +
                				 "   AND r.name = 'C_REGIMENGANANCIAS_V'";
                    PreparedStatement ps = DB.prepareStatement(sql,get_TrxName());
                    ResultSet rts = ps.executeQuery();
                    if (rts.next())
                    {	detalle += " - " + rts.getString(1);
                    	rts.close();
                    	ps.close();
                    }
                }
                if (rs.getString(9).equals(MPAYMENTRET.TIPO_RET_IB))
                	detalle += " - " + rs.getBigDecimal(10) + "%";

                pstmtInsert.setString(5, detalle);
                pstmtInsert.setString(6, rs.getString(7));
                pstmtInsert.setBigDecimal(7, rs.getBigDecimal(5));
                total = total.add(rs.getBigDecimal(5));
                pstmtInsert.setLong(8, rs.getLong(3));
                pstmtInsert.executeQuery();
                DB.commit(true, get_TrxName());
                sumaTotal = sumaTotal.add(rs.getBigDecimal(5));
            }
            if (!total.equals(Env.ZERO)) {
                sqlInsert = "insert into T_PAGO_PIE values(?,?,'Y',?,?,?,?,?,?)";
                pstmtInsert = DB.prepareStatement(sqlInsert,get_TrxName());
                pstmtInsert.setLong(1, AD_Client_ID);
                pstmtInsert.setLong(2, AD_Org_ID);
                pstmtInsert.setInt(3, p_instance);
                pstmtInsert.setDate(4, null);
                pstmtInsert.setString(5, "Total Pesos");
                pstmtInsert.setString(6, null);
                pstmtInsert.setBigDecimal(7, total);
                pstmtInsert.setLong(8, getRecord_ID());
                pstmtInsert.executeQuery();
                DB.commit(true, get_TrxName());
            }
        } catch (SQLException ex) {
            Logger.getLogger(GeneratePagos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void total() throws SQLException{
       try{
            String sqlRemove = "delete from T_PAGO_TOTAL";
            DB.executeUpdate(sqlRemove, null);
            String sqlInsert = "", sqlQuery = "";
            PreparedStatement pstmtInsert = null;
            sqlQuery = "select ad_client_id,ad_org_id FROM C_PAYMENT where c_payment_id=" + getRecord_ID();
            PreparedStatement pstmt = DB.prepareStatement(sqlQuery,get_TrxName());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                sqlInsert = "insert into T_PAGO_TOTAL values(?,?,'Y',?,?,?)";
                pstmtInsert = DB.prepareStatement(sqlInsert,get_TrxName());
                pstmtInsert.setLong(1, rs.getInt(1));
                pstmtInsert.setLong(2, rs.getInt(2));
                pstmtInsert.setInt(3, p_instance);
                pstmtInsert.setInt(4, getRecord_ID());
                pstmtInsert.setBigDecimal(5, sumaTotal);
                pstmtInsert.executeQuery();
                DB.commit(true, get_TrxName());
            }
       }
       catch (SQLException ex) {
            Logger.getLogger(GeneratePagos.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
    
    protected void prepare() {
         p_instance = getAD_PInstance_ID();
         MPayment pay = new MPayment(getCtx(),getRecord_ID(),get_TrxName());
         C_Currency_ID = pay.getC_Currency_ID();
    }

}
