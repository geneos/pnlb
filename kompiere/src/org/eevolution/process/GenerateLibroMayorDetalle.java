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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.eevolution.tools.UtilProcess;
import org.compiere.util.Env;
import javax.swing.JOptionPane;
/**
 * Clase que ingresa las filas a la tabla T_LIBROMAYORDETALLE
 *
 * @author santiago
 */
public class GenerateLibroMayorDetalle extends SvrProcess {

//PARAMETROS DE ENTRADA
    int p_instance;
    private Timestamp fromDate;
    private Timestamp toDate;
    private BigDecimal C_ElementValue_IDFROM = new BigDecimal(0);
    private BigDecimal C_ElementValue_IDTO = new BigDecimal(0);
    private BigDecimal C_AcctSchema_ID = new BigDecimal(0);
    private String fromCta = "";
    private String toCta = "";

    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        int flag = 0;
        String nroCta = "";
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (name.equals("DateAcct")) {
                fromDate = (Timestamp) para[i].getParameter();
                toDate = (Timestamp) para[i].getParameter_To();
            } else if (name.equals("VALUE")) {
                fromCta = (String) para[i].getParameter();
                toCta = (String) para[i].getParameter_To();
                /*
                 *  03/09/2012 Zynnia.
                 *  Agregamos para que se pueda buscar por nro. de cuenta
                 *  ya que antes se tenia que poner manual y no se podia desplegar 
                 *  el listado de todas las cuentas.
                 * 
                 */
            } else if (name.equals("C_ElementValue_ID")) {
                if(flag == 0) {
                    String sql = "Select value from c_elementvalue where c_elementvalue_id = "+ para[i].getParameter();
                    PreparedStatement ps = DB.prepareStatement(sql, null);
                    try {
                        ResultSet rs = ps.executeQuery();
                        if (rs.next())
                            nroCta = rs.getString(1);
                        ps.close();
                        rs.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(GenerateLibroMayorDetalle.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    fromCta = nroCta;
                    flag = 1;
                } else {
                    String sql = "Select value from c_elementvalue where c_elementvalue_id = "+ para[i].getParameter();
                    PreparedStatement ps = DB.prepareStatement(sql, null);
                    try {
                        ResultSet rs = ps.executeQuery();
                        if (rs.next())
                            nroCta = rs.getString(1);
                        ps.close();
                        rs.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(GenerateLibroMayorDetalle.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    toCta = nroCta;
                }                  
            } else if (name.equals("C_AcctSchema_ID")) {
                C_AcctSchema_ID = (BigDecimal) para[i].getParameter();
            }
        }
        
        String sqlRemove = "delete from AD_PInstance_Para where AD_PInstance_ID = " + getAD_PInstance_ID();
        DB.executeUpdate(sqlRemove, null);

        p_instance = getAD_PInstance_ID();
    }

    protected String doIt() throws Exception {
        // Validar datos de cuentas ingresados
        
        String val_cta_from [];
        val_cta_from = new String[5];
        val_cta_from = fromCta.split("\\.");  
        
        String val_cta_to [];
        val_cta_to = new String[5];
        val_cta_to = toCta.split("\\.");
        int valFrom;
        int valTo;
        int flagOK = 0;
        for(int ind=0;ind<5;ind++) {
            
            try {
                valFrom = Integer.parseInt(val_cta_from[ind]);
                valTo = Integer.parseInt(val_cta_to[ind]);
            }
            catch (Exception e) {
                return "Error en la parametrización de cuentas contables para " + fromCta + " hasta " + toCta;
            }

            if(valFrom>valTo & flagOK == 0)
                return "Error en la parametrización de cuentas contables";
            else if(valFrom<valTo)
                flagOK = 1;
            }
        
        
        //BORRADO DE LA TABLA T_LIBROMAYORDETALLE
        String sql;
        log.info("Comienzo del proceso del detalle de diario de caja");
        log.info("Borrado de la tabla temporal T_LIBROMAYORDETALLE");
        String sqlRemove = "delete from T_LIBROMAYORDETALLE";
        DB.executeUpdate(sqlRemove, null);

        log.info("Borrado de la tabla temporal T_LIBROMAYORHEADER");
        sqlRemove = "delete from T_LIBROMAYORHEADER";
        DB.executeUpdate(sqlRemove, null);
        //fin borrado

        ResultSet rs = getAsientos(true);
        int account_id = 0;
        int LIBROMAYORDETALLE_ID = 1000000;
        /*
         *  03/05/2013 Maria Jesus
         *  Agregamos la condicion del if para que primero verifique si tiene resultados y
         *  luego entre en el while.
         * 
         */
        
        if(rs.next()){

            while (!rs.isAfterLast()) { 

                int cliente = rs.getInt(1);
                int organizacion = rs.getInt(2);
                int orden = 0;

                account_id = rs.getInt(9);

                //  Ingreso en la tabla cabecera
                sql = "INSERT INTO T_LIBROMAYORHEADER VALUES(?,?,'Y',?,?,?,?,?,?)";
                PreparedStatement ps = DB.prepareStatement(sql, null);
                ps.setInt(1, cliente);						//	CLIENTE
                ps.setInt(2, organizacion);					//	ORGANIZACION
                ps.setInt(3, rs.getInt(14));                //	ACCT SCHEMA
                ps.setInt(4, p_instance);					//	INSTANCE
                ps.setInt(5, account_id);					//	ELEMENT VALUE
                ps.setString(6, rs.getString(8));			//	NAME
                ps.setDate(7, rs.getDate(5));				// 	FECHA
                ps.setString(8, rs.getString(11));			// 	VALUE

                ps.executeUpdate();
                ps.close();

                //   Calculo el saldo inicial para la cuenta
                BigDecimal saldoInicial = obtenerSaldoInicial(account_id);

                BigDecimal debeTotal = new BigDecimal(0);
                BigDecimal haberTotal = new BigDecimal(0);

                // 	Ingreso en la tabla el detalle del saldo inicial
                sql = "INSERT INTO T_LIBROMAYORDETALLE VALUES(?,?,?,?,?,?,?,'Y',?,?,?,?,?,?,?)";
                ps = DB.prepareStatement(sql, null);
                ps.setInt(1, cliente);						//	CLIENTE
                ps.setInt(2, organizacion);					//	ORGANIZACION
                ps.setInt(3, 0);							//	FACTACCT_ID
                ps.setString(4, null);						//	RECORD_ID
                //	Si saldo es positivo va en el DEBE
                if (saldoInicial.compareTo(BigDecimal.ZERO) > 0) {
                    ps.setBigDecimal(5, saldoInicial);		//	DEBE
                    ps.setBigDecimal(6, null);	//	HABER
                    debeTotal = debeTotal.add(saldoInicial);
                } else { //	Saldo negativo al HABER
                    ps.setBigDecimal(6, saldoInicial.multiply(new BigDecimal(-1)));	//HABER
                    ps.setBigDecimal(5, null);	//	DEBE
                    haberTotal = haberTotal.add(saldoInicial.multiply(new BigDecimal(-1)));
                }
                ps.setDate(7, null);						//	FECHA
                //Luego del 'Y'
                ps.setString(8, "Saldo de Inicio");			//	CONCEPTO
                ps.setBigDecimal(9, saldoInicial);			//	SALDO
                ps.setInt(10, rs.getInt(9));				//	ELEMENT VALUE
                ps.setInt(11, rs.getInt(14));           	//	ACCT SCHEMA
                ps.setInt(12, p_instance);					//	INSTANCE
                ps.setInt(13, LIBROMAYORDETALLE_ID);		//	LIBROMAYORDETALLE_ID
                ps.setInt(14, orden);						//	ORDEN

                ps.executeUpdate();
                ps.close();

                //Incremento LIBROMAYORDETALLE_ID
                LIBROMAYORDETALLE_ID++;
                orden++;

                BigDecimal saldo = saldoInicial;

                while (!(rs.isAfterLast() || (account_id != rs.getInt(9)))) {
                    // 	Ingreso en la tabla el detalle los totales
                    sql = "INSERT INTO T_LIBROMAYORDETALLE VALUES(?,?,?,?,?,?,?,'Y',?,?,?,?,?,?,?)";
                    ps = DB.prepareStatement(sql, null);
                    ps.setInt(1, cliente);						//	CLIENTE
                    ps.setInt(2, organizacion);					//	ORGANIZACION
                    ps.setInt(3, rs.getInt(10));				//	FACTACCT_ID
                    ps.setString(4, rellenarNro(rs.getString(13)));			//	RECORD_ID

                    //	Actualizo el saldo acumulado
                    if (rs.getBigDecimal(3).equals(BigDecimal.ZERO)) {
                        ps.setBigDecimal(5, null);					//	DEBE
                        ps.setBigDecimal(6, rs.getBigDecimal(4));	// 	HABER
                        saldo = saldo.subtract(rs.getBigDecimal(4));
                        haberTotal = haberTotal.add(rs.getBigDecimal(4));
                    } else {
                        ps.setBigDecimal(5, rs.getBigDecimal(3));	//	DEBE
                        ps.setBigDecimal(6, null);					// 	HABER
                        saldo = saldo.add(rs.getBigDecimal(3));
                        debeTotal = debeTotal.add(rs.getBigDecimal(3));
                    }
                    ps.setDate(7, rs.getDate(5));				// 	FECHA
                    ps.setString(8, getDescripcion(rs.getInt(12), rs.getInt(6), rs.getString(7)));	//	CONCEPTO
                    ps.setBigDecimal(9, saldo);					//	SALDO
                    ps.setInt(10, rs.getInt(9));				//	ELEMENT VALUE
                    ps.setInt(11, rs.getInt(14));               //	ACCT SCHEMA
                    ps.setInt(12, p_instance);					//	INSTANCE
                    ps.setInt(13, LIBROMAYORDETALLE_ID);		//	LIBROMAYORDETALLE_ID
                    ps.setInt(14, orden);						//	ORDEN

                    ps.executeUpdate();
                    ps.close();

                    //Incremento LIBROMAYORDETALLE_ID
                    LIBROMAYORDETALLE_ID++;
                    orden++;

                    rs.next();
                }

                // 	Ingreso en la tabla el detalle los totales
                sql = "INSERT INTO T_LIBROMAYORDETALLE VALUES(?,?,?,?,?,?,?,'Y',?,?,?,?,?,?,?)";
                ps = DB.prepareStatement(sql, null);
                ps.setInt(1, cliente);								//	CLIENTE
                ps.setInt(2, organizacion);							//	ORGANIZACION
                ps.setInt(3, 0);									//	FACTACCT_ID
                ps.setString(4, null);								//	RECORD_ID
                ps.setBigDecimal(5, debeTotal);						//	DEBE
                ps.setBigDecimal(6, haberTotal);					//	HABER
                ps.setDate(7, null);								//	FECHA
                ps.setString(8, "Total Cuenta");					//	CONCEPTO
                ps.setBigDecimal(9, null);							//	SALDO
                ps.setInt(10, account_id);							//	ELEMENT VALUE
                ps.setInt(11, C_AcctSchema_ID.intValue());                       //	ACCT SCHEMA
                ps.setInt(12, p_instance);							//	INSTANCE
                ps.setInt(13, LIBROMAYORDETALLE_ID);				//	LIBROMAYORDETALLE_ID
                ps.setInt(14, orden);								//	ORDEN

                ps.executeUpdate();
                ps.close();



                //Incremento LIBROMAYORDETALLE_ID
                LIBROMAYORDETALLE_ID++;
                orden++;

            }
        }
         /*
         *  20/05/2013 Maria Jesus Martin
         *  Agregamos para que, si el reporte no arroja datos para los parametros que se
         *  han ingresado, el reporte muestre un cartel con esta informacion.
         *
         */
        else
        {
             JOptionPane.showMessageDialog(null,"El reporte no arrojo datos para los parámetros ingresados.", "Reporte sin datos.", JOptionPane.INFORMATION_MESSAGE);
             System.out.println("El reporte no arrojo datos para los parámetros ingresados.");
             return "";
        }
        rs.close();

        UtilProcess.initViewer("Libro Mayor", getAD_PInstance_ID(), getProcessInfo());
        return "";
    }

    /**
     * BISion - 11/02/2009 - Santiago Ibañez Metodo que retorna los asientos filtrando por los parametros ingresados
     * ordenados por cuenta y luego por fecha del asiento.
     *
     * @param debito
     * @return
     * @throws Exception
     */
    private ResultSet getAsientos(boolean debito) throws Exception {
        //CONSULTA PARA OBTENER LOS DATOS
        String sql = "SELECT "
                + "fa.AD_CLIENT_ID, " + //CIA
                "fa.AD_ORG_ID, " + //ORGANIZACION
                "fa.AMTACCTDR," + //DEBE
                "fa.AMTACCTCR," + //HABER
                "fa.DATEACCT," + //FECHA ASIENTO
                "fa.RECORD_ID," + //NUMERO DE ASIENTO
                "fa.DESCRIPTION, "
                + "ev.NAME, "
                + "fa.ACCOUNT_ID, "
                + "fa.FACT_ACCT_ID, "
                + "ev.VALUE, "
                + "fa.AD_TABLE_ID, "
                + "fa.FACTNO, "
                + "fa.C_AcctSchema_ID "
                + "FROM FACT_ACCT fa "
                + "LEFT JOIN C_ELEMENTVALUE ev ON (ev.C_ELEMENTVALUE_ID = fa.ACCOUNT_ID) ";
                
        sql += getSqlWhere();
        sql += " ORDER BY fa.C_AcctSchema_ID ASC, fa.ACCOUNT_ID, fa.DATEACCT, fa.FACTNO";
        PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
        int paramIndex = 1;
        if (fromDate != null) {
            pstmt.setTimestamp(paramIndex, fromDate);
            paramIndex++;
        }
        if (toDate != null) {
            pstmt.setTimestamp(paramIndex, toDate);
            paramIndex++;
        }
        
        /*
        if (!C_ElementValue_IDFROM.equals(BigDecimal.ZERO)) {
            pstmt.setInt(paramIndex, C_ElementValue_IDFROM.intValue());
            paramIndex++;
        }
        if (!C_ElementValue_IDTO.equals(BigDecimal.ZERO)) {
            pstmt.setInt(paramIndex, C_ElementValue_IDTO.intValue());
            paramIndex++;
        }
        */
        
        if (!C_AcctSchema_ID.equals(BigDecimal.ZERO)) {
            pstmt.setInt(paramIndex, C_AcctSchema_ID.intValue());
            paramIndex++;
        }
        return pstmt.executeQuery();
    }

    /**
     * BISion - 06/02/2009 - Santiago Ibañez Metodo que retorna la clausula "where" dependiendo de los parametros
     * ingresados.
     *
     * @return
     */
    private String getSqlWhere() {
        String sqlWhere = "WHERE ";

        if (toDate != null && fromDate != null) {
            sqlWhere += "fa.DATEACCT BETWEEN ? AND ? ";
        } else if (toDate != null && fromDate == null) {
            sqlWhere += "fa.DATEACCT =< ? ";
        } else if (toDate == null && fromDate != null) {
            sqlWhere += "fa.DATEACCT >= ? ";
        }
        
        if (!fromCta.equals("") && !toCta.equals("")) {
            sqlWhere += " AND ev.VALUE BETWEEN '" + fromCta + "' AND '" + toCta + "'";
        } else if (!toCta.equals("") && fromCta.equals("")) {
            sqlWhere += " AND ev.VALUE =< '" + fromCta + "'";
        } else if (toCta.equals("") && !fromCta.equals("")) {
            sqlWhere += " AND ev.VALUE >= '" + toCta + "'";
        }         
        
        
        if (!C_AcctSchema_ID.equals(BigDecimal.ZERO)) {
            sqlWhere += " AND fa.C_AcctSchema_ID = ? ";
        }        

        return sqlWhere;
    }

    private BigDecimal obtenerSaldoInicial(int account_id) throws Exception {
        //Obtengo el debe
        String sql =
                " SELECT SUM(fa.AMTACCTDR)"
                + " FROM FACT_ACCT fa "
            //    + " LEFT JOIN FACT_ACCT_RESUMEN ON (fa.RECORD_ID = FACT_ACCT_RESUMEN.RECORD_FACT_ID)"
                + " WHERE fa.DATEACCT >= to_date('29/11/2011', 'dd,mm,yyyy') AND fa.DATEACCT < ? AND fa.ACCOUNT_ID = ? ";  //AND FACT_ACCT_RESUMEN.RECORD_FACT_ID is null";
        PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
        pstmt.setTimestamp(1, fromDate);
        pstmt.setInt(2, account_id);
        ResultSet rs = pstmt.executeQuery();
        BigDecimal debe = BigDecimal.ZERO;
        if (rs.next()) {
            debe = rs.getBigDecimal(1);
        }
        if (debe == null) {
            debe = BigDecimal.ZERO;
        }
        //Obtengo el haber
        sql = " SELECT SUM(fa.AMTACCTCR) "
                + " FROM FACT_ACCT fa "
                + " WHERE fa.DATEACCT >= to_date('29/11/2011', 'dd,mm,yyyy') AND fa.DATEACCT < ? AND fa.ACCOUNT_ID = ? "; // AND FACT_ACCT_RESUMEN.RECORD_FACT_ID is null";
        pstmt = DB.prepareStatement(sql, get_TrxName());
        pstmt.setTimestamp(1, fromDate);
        pstmt.setInt(2, account_id);
        rs = pstmt.executeQuery();
        BigDecimal haber = BigDecimal.ZERO;
        if (rs.next()) {
            haber = rs.getBigDecimal(1);
        }
        if (haber == null) {
            haber = BigDecimal.ZERO;
        }
        rs.close();
        pstmt.close();
        return debe.add(haber.multiply(new BigDecimal(-1)));
    }

     private String getDescripcion(int AD_TABLE_ID, int RECORD_ID, String nro) {
        String detalle = null;

        try {
            if (AD_TABLE_ID == MJournal.getTableId(MJournal.Table_Name)) {
                MJournal journal = new MJournal(getCtx(), RECORD_ID, null);
                detalle = journal.getDescription();
            } else if (AD_TABLE_ID == MInvoice.getTableId(MInvoice.Table_Name)) {
                MInvoice invoice = new MInvoice(getCtx(), RECORD_ID, null);
                MDocType docType = new MDocType(getCtx(), invoice.getC_DocTypeTarget_ID(), null);
                // Modificado para agregar el proveedor al listado
                // Ticket de soporte #169 24/01/2011
                // José Fantasia
                MBPartner prov = new MBPartner(getCtx(), invoice.getC_BPartner_ID(), null);
                detalle = docType.getPrintName() + " - " + invoice.getDocumentNo() + " - (Código de Proveedor): " + prov.getValue() + " " + prov.getName();
            } else if (AD_TABLE_ID == MFactAcct.getTableId(MFactAcct.Table_Name)) {

                String sqlQuery = "SELECT RECORD_FACT_ID FROM FACT_ACCT_RESUMEN WHERE RECORD_RES_ID = ? AND TABLE_FACT_ID = " + MPayment.getTableId(MPayment.Table_Name);
                PreparedStatement ps = DB.prepareStatement(sqlQuery, get_TrxName());
                ps.setInt(1, RECORD_ID);
                ResultSet set = ps.executeQuery();

                if (set.next()) {
                    MPayment payment = new MPayment(getCtx(), set.getInt(1), null);
                    if (payment.isReceipt()) {
                        detalle = "Resumen de Cobranza - " + payment.getDocumentNo();
                    } else {
                        detalle = "Resumen de Pago - " + payment.getDocumentNo();
                    }
                } else {
                    set.close();
                    ps.close();
                    sqlQuery = "SELECT RECORD_FACT_ID FROM FACT_ACCT_RESUMEN WHERE RECORD_RES_ID = ? AND TABLE_FACT_ID = " + MInvoice.getTableId(MInvoice.Table_Name);
                    ps = DB.prepareStatement(sqlQuery, get_TrxName());
                    ps.setInt(1, RECORD_ID);
                    set = ps.executeQuery();

                    if (set.next()) {
                        MInvoice invoice = new MInvoice(getCtx(), set.getInt(1), null);
                        detalle = "Resumen de Ventas - " + invoice.getDocumentNo();
                    }
                }
                set.close();
                ps.close();
            } else if (AD_TABLE_ID == MMOVIMIENTOFONDOS.getTableId(MMOVIMIENTOFONDOS.Table_Name)) {
                MMOVIMIENTOFONDOS movFondos = new MMOVIMIENTOFONDOS(getCtx(), RECORD_ID, null);
                movFondos.getStringTipo();
                MZYNDYNAMICMOVFONDOS dynMovFondos = MZYNDYNAMICMOVFONDOS.get(Env.getCtx(), movFondos.getTIPO());
                detalle = movFondos.getStringTipo() + "_" + movFondos.getDocumentNo();
                
                
                
                if (dynMovFondos == null) {

                    /*
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_ValNegociados)
                    || dynMovFondos.isDEB_CUENTA_BANCO()
                    || dynMovFondos.isCRE_CHEQUE_TERCERO()) {
                    */
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_ValNegociados)) {
                        List list = movFondos.getC_MOVIMIENTOFONDOS_CRE_ID();
                        for (int i = 0; i < list.size(); i++) {
                            MPAYMENTVALORES valor = new MPAYMENTVALORES(getCtx(), ((MMOVIMIENTOFONDOSCRE) list.get(i)).getC_PAYMENTVALORES_ID(), null);;
                            detalle += "_" + valor.getBank() + "_" + valor.getNroCheque();
                            if (valor.getVencimientoDate() != null) {
                                detalle += "_" + valor.getVencimientoDate();
                            }
                        }
                    }
                    /*
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_RechCqPropio)
                    || dynMovFondos.isDEB_CHEQUE_PROPIO()
                    || dynMovFondos.isCRE_CUENTA_CREDITO()) {
                    */
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_RechCqPropio)) {
                        List list = movFondos.getC_MOVIMIENTOFONDOS_DEB_ID();
                        for (int i = 0; i < list.size(); i++) {
                            MBankAccount bank = new MBankAccount(getCtx(), ((MMOVIMIENTOFONDOSDEB) list.get(i)).getC_BankAccount_ID(), null);
                            MVALORPAGO valor = new MVALORPAGO(getCtx(), ((MMOVIMIENTOFONDOSDEB) list.get(i)).getC_VALORPAGO_ID(), null);;
                            detalle += "_" + bank.getName() + "_" + valor.getNroCheque();
                        }
                    }
                    /*
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_RechCqTercero)
                    || movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_VencCqTercero)
                    || dynMovFondos.isDEB_CHEQUE_TERCERO()
                    || dynMovFondos.isCRE_CUENTA_CREDITO()) {
                    */
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_RechCqTercero)
                        || movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_VencCqTercero)) {
                        List list = movFondos.getC_MOVIMIENTOFONDOS_DEB_ID();
                        for (int i = 0; i < list.size(); i++) {
                            MPAYMENTVALORES valor = new MPAYMENTVALORES(getCtx(), ((MMOVIMIENTOFONDOSDEB) list.get(i)).getC_PAYMENTVALORES_ID(), null);;
                            detalle += "_" + ((MMOVIMIENTOFONDOSDEB) list.get(i)).getBank() + "_" + valor.getNroCheque();
                            if (valor.getVencimientoDate() != null) {
                                detalle += "_" + valor.getVencimientoDate();
                            }
                        }
                    }
                    /*
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_VencCqPropio)
                    || dynMovFondos.isDEB_CHEQUE_TERCERO()
                    || dynMovFondos.isCRE_CUENTA_CREDITO()) {
                    */
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_VencCqPropio)) {
                        List list = movFondos.getC_MOVIMIENTOFONDOS_DEB_ID();
                        for (int i = 0; i < list.size(); i++) {
                            MBankAccount bank = new MBankAccount(getCtx(), ((MMOVIMIENTOFONDOSDEB) list.get(i)).getC_BankAccount_ID(), null);
                            MVALORPAGO valor = new MVALORPAGO(getCtx(), ((MMOVIMIENTOFONDOSDEB) list.get(i)).getC_VALORPAGO_ID(), null);;
                            detalle += "_" + bank.getName() + "_" + valor.getNroCheque();
                            if (valor.getVencimientoDate() != null) {
                                detalle += "_" + valor.getVencimientoDate();
                            }
                        }
                    }
                    /*
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_CreditoBancario)
                    || dynMovFondos.isDEB_CUENTA_BANCO()
                    || dynMovFondos.isCRE_CUENTA_BANCO_DESC()) {
                    */
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_CreditoBancario)) {
                        List list = movFondos.getC_MOVIMIENTOFONDOS_CRE_ID();
                        for (int i = 0; i < list.size(); i++) {
                            detalle += "_" + ((MMOVIMIENTOFONDOSCRE) list.get(i)).getDescription();;
                        }
                    }
                    /*
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_TraBancaria)
                    || dynMovFondos.isDEB_CUENTA_DEBITO() || dynMovFondos.isCRE_TRANSFERENCIA()) {
                    */
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_TraBancaria)) {
                        List list = movFondos.getC_MOVIMIENTOFONDOS_CRE_ID();
                        for (int i = 0; i < list.size(); i++) {
                            MBankAccount bank = new MBankAccount(getCtx(), ((MMOVIMIENTOFONDOSCRE) list.get(i)).getC_BankAccount_ID(), null);
                            detalle += " - " + bank.getName() + "_" + ((MMOVIMIENTOFONDOSCRE) list.get(i)).getNroTransferencia();
                        }
                    }
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_CambioCheque)) {
                        List list = movFondos.getC_MOVIMIENTOFONDOS_DEB_ID();
                        for (int i = 0; i < list.size(); i++) {
                            MPAYMENTVALORES valor = MPAYMENTVALORES.get(((MMOVIMIENTOFONDOSDEB) list.get(i)).getBank(), ((MMOVIMIENTOFONDOSDEB) list.get(i)).getNroCheque(), null);
                            if (valor != null) {
                                detalle += "_" + valor.getBank() + "_" + valor.getNroCheque();
                                if (valor.getVencimientoDate() != null) {
                                    detalle += "_" + valor.getVencimientoDate();
                                }
                            } else {
                                detalle += "_" + ((MMOVIMIENTOFONDOSDEB) list.get(i)).getBank() + "_" + ((MMOVIMIENTOFONDOSDEB) list.get(i)).getNroCheque();
                            }
                        }
                        list = movFondos.getC_MOVIMIENTOFONDOS_CRE_ID();
                        for (int i = 0; i < list.size(); i++) {
                            MPAYMENTVALORES valor = new MPAYMENTVALORES(getCtx(), ((MMOVIMIENTOFONDOSCRE) list.get(i)).getC_PAYMENTVALORES_ID(), null);
                            detalle += "_" + valor.getBank() + "_" + valor.getNroCheque();
                            if (valor.getVencimientoDate() != null) {
                                detalle += "_" + valor.getVencimientoDate();
                            }
                        }
                    }

                    /*
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_DepCheque)
                    || dynMovFondos.isDEB_CUENTA_BANCO()
                    || dynMovFondos.isCRE_CHEQUE_TERCERO()) {
                    */
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_DepCheque)) {
                        List list = movFondos.getC_MOVIMIENTOFONDOS_CRE_ID();
                        for (int i = 0; i < list.size(); i++) {
                            MPAYMENTVALORES valor = new MPAYMENTVALORES(getCtx(), ((MMOVIMIENTOFONDOSCRE) list.get(i)).getC_PAYMENTVALORES_ID(), null);
                            detalle += "_" + valor.getBank() + "_" + valor.getNroCheque();
                            if (valor.getVencimientoDate() != null) {
                                detalle += "_" + valor.getVencimientoDate();
                            }
                        }
                    }
                    /*
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_EmiCheque)
                    || dynMovFondos.isDEB_CUENTA_DEBITO()
                    || dynMovFondos.isCRE_CHEQUE_PROPIO()) {
                    */
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_EmiCheque)) {
                        List list = movFondos.getC_MOVIMIENTOFONDOS_CRE_ID();
                        for (int i = 0; i < list.size(); i++) {
                            MVALORPAGO valor = MVALORPAGO.get(((MMOVIMIENTOFONDOSCRE) list.get(i)).getC_BankAccount_ID(), ((MMOVIMIENTOFONDOSCRE) list.get(i)).getNroCheque(), null);
                            MBankAccount bank = new MBankAccount(getCtx(), ((MMOVIMIENTOFONDOSCRE) list.get(i)).getC_BankAccount_ID(), null);
                            if (valor != null) {
                                detalle += "_" + bank.getName() + "_" + valor.getNroCheque() + "_" + valor.getVencimientoDate();
                            } else {
                                detalle += "_" + bank.getName() + "_" + ((MMOVIMIENTOFONDOSCRE) list.get(i)).getNroCheque();
                            }
                        }
                    }
                    /*
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_DepositoPendiente)
                    || dynMovFondos.isDEB_CUENTA_BANCO()
                    || dynMovFondos.isCRE_CUENTA_BANCO()) {
                    */
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_DepositoPendiente)) {
                        List list = movFondos.getC_MOVIMIENTOFONDOS_CRE_ID();
                        for (int i = 0; i < list.size(); i++) {
                            MBankAccount bank = new MBankAccount(getCtx(), ((MMOVIMIENTOFONDOSCRE) list.get(i)).getC_BankAccount_ID(), null);
                            detalle += "_" + bank.getName();
                        }
                    }
                    
                } else {

                    /*
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_ValNegociados)
                    || dynMovFondos.isDEB_CUENTA_BANCO()
                    || dynMovFondos.isCRE_CHEQUE_TERCERO()) {
                    */
                    if (dynMovFondos.isCRE_VALORES_NEG()) {
                        List list = movFondos.getC_MOVIMIENTOFONDOS_CRE_ID();
                        for (int i = 0; i < list.size(); i++) {
                            MPAYMENTVALORES valor = new MPAYMENTVALORES(getCtx(), ((MMOVIMIENTOFONDOSCRE) list.get(i)).getC_PAYMENTVALORES_ID(), null);;
                            detalle += "_" + valor.getBank() + "_" + valor.getNroCheque();
                            if (valor.getVencimientoDate() != null) {
                                detalle += "_" + valor.getVencimientoDate();
                            }
                        }
                    }
                    /*
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_RechCqPropio)
                    || dynMovFondos.isDEB_CHEQUE_PROPIO()
                    || dynMovFondos.isCRE_CUENTA_CREDITO()) {
                    */
                    if (dynMovFondos.isDEB_CHEQUE_PRO_RECH()) {
                        List list = movFondos.getC_MOVIMIENTOFONDOS_DEB_ID();
                        for (int i = 0; i < list.size(); i++) {
                            MBankAccount bank = new MBankAccount(getCtx(), ((MMOVIMIENTOFONDOSDEB) list.get(i)).getC_BankAccount_ID(), null);
                            MVALORPAGO valor = new MVALORPAGO(getCtx(), ((MMOVIMIENTOFONDOSDEB) list.get(i)).getC_VALORPAGO_ID(), null);;
                            detalle += "_" + bank.getName() + "_" + valor.getNroCheque();
                        }
                    }
                    /*
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_RechCqTercero)
                    || movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_VencCqTercero)
                    || dynMovFondos.isDEB_CHEQUE_TERCERO()
                    || dynMovFondos.isCRE_CUENTA_CREDITO()) {
                    */
                    if (dynMovFondos.isDEB_CHEQUE_TER_RECH() || dynMovFondos.isDEB_CHEQUE_TER_VENC()) {
                        List list = movFondos.getC_MOVIMIENTOFONDOS_DEB_ID();
                        for (int i = 0; i < list.size(); i++) {
                            MPAYMENTVALORES valor = new MPAYMENTVALORES(getCtx(), ((MMOVIMIENTOFONDOSDEB) list.get(i)).getC_PAYMENTVALORES_ID(), null);;
                            detalle += "_" + ((MMOVIMIENTOFONDOSDEB) list.get(i)).getBank() + "_" + valor.getNroCheque();
                            if (valor.getVencimientoDate() != null) {
                                detalle += "_" + valor.getVencimientoDate();
                            }
                        }
                    }
                    /*
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_VencCqPropio)
                    || dynMovFondos.isDEB_CHEQUE_TERCERO()
                    || dynMovFondos.isCRE_CUENTA_CREDITO()) {
                    */
                    if (dynMovFondos.isDEB_CHEQUE_PRO_VENC()) {
                        List list = movFondos.getC_MOVIMIENTOFONDOS_DEB_ID();
                        for (int i = 0; i < list.size(); i++) {
                            MBankAccount bank = new MBankAccount(getCtx(), ((MMOVIMIENTOFONDOSDEB) list.get(i)).getC_BankAccount_ID(), null);
                            MVALORPAGO valor = new MVALORPAGO(getCtx(), ((MMOVIMIENTOFONDOSDEB) list.get(i)).getC_VALORPAGO_ID(), null);;
                            detalle += "_" + bank.getName() + "_" + valor.getNroCheque();
                            if (valor.getVencimientoDate() != null) {
                                detalle += "_" + valor.getVencimientoDate();
                            }
                        }
                    }
                    /*
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_CreditoBancario)
                    || dynMovFondos.isDEB_CUENTA_BANCO()
                    || dynMovFondos.isCRE_CUENTA_BANCO_DESC()) {
                    */
                    if (dynMovFondos.isCRE_CUENTA_BANCO_DESC()) {
                        List list = movFondos.getC_MOVIMIENTOFONDOS_CRE_ID();
                        for (int i = 0; i < list.size(); i++) {
                            detalle += "_" + ((MMOVIMIENTOFONDOSCRE) list.get(i)).getDescription();;
                        }
                    }
                    /*
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_TraBancaria)
                    || dynMovFondos.isDEB_CUENTA_DEBITO() || dynMovFondos.isCRE_TRANSFERENCIA()) {
                    */
                    if (dynMovFondos.isCRE_TRANSFERENCIA()) {
                        List list = movFondos.getC_MOVIMIENTOFONDOS_CRE_ID();
                        for (int i = 0; i < list.size(); i++) {
                            MBankAccount bank = new MBankAccount(getCtx(), ((MMOVIMIENTOFONDOSCRE) list.get(i)).getC_BankAccount_ID(), null);
                            detalle += " - " + bank.getName() + "_" + ((MMOVIMIENTOFONDOSCRE) list.get(i)).getNroTransferencia();
                        }
                    }
                    if (dynMovFondos.isDEB_CHEQUE_REC()
                        || dynMovFondos.isCRE_CHEQUE_RECI()) {
                        List list = movFondos.getC_MOVIMIENTOFONDOS_DEB_ID();
                        for (int i = 0; i < list.size(); i++) {
                            MPAYMENTVALORES valor = MPAYMENTVALORES.get(((MMOVIMIENTOFONDOSDEB) list.get(i)).getBank(), ((MMOVIMIENTOFONDOSDEB) list.get(i)).getNroCheque(), null);
                            if (valor != null) {
                                detalle += "_" + valor.getBank() + "_" + valor.getNroCheque();
                                if (valor.getVencimientoDate() != null) {
                                    detalle += "_" + valor.getVencimientoDate();
                                }
                            } else {
                                detalle += "_" + ((MMOVIMIENTOFONDOSDEB) list.get(i)).getBank() + "_" + ((MMOVIMIENTOFONDOSDEB) list.get(i)).getNroCheque();
                            }
                        }
                        list = movFondos.getC_MOVIMIENTOFONDOS_CRE_ID();
                        for (int i = 0; i < list.size(); i++) {
                            MPAYMENTVALORES valor = new MPAYMENTVALORES(getCtx(), ((MMOVIMIENTOFONDOSCRE) list.get(i)).getC_PAYMENTVALORES_ID(), null);
                            detalle += "_" + valor.getBank() + "_" + valor.getNroCheque();
                            if (valor.getVencimientoDate() != null) {
                                detalle += "_" + valor.getVencimientoDate();
                            }
                        }
                    }

                    /*
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_DepCheque)
                    || dynMovFondos.isDEB_CUENTA_BANCO()
                    || dynMovFondos.isCRE_CHEQUE_TERCERO()) {
                    */
                    if (dynMovFondos.isCRE_CHEQUE_DEPO()) {
                        List list = movFondos.getC_MOVIMIENTOFONDOS_CRE_ID();
                        for (int i = 0; i < list.size(); i++) {
                            MPAYMENTVALORES valor = new MPAYMENTVALORES(getCtx(), ((MMOVIMIENTOFONDOSCRE) list.get(i)).getC_PAYMENTVALORES_ID(), null);
                            detalle += "_" + valor.getBank() + "_" + valor.getNroCheque();
                            if (valor.getVencimientoDate() != null) {
                                detalle += "_" + valor.getVencimientoDate();
                            }
                        }
                    }
                    /*
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_EmiCheque)
                    || dynMovFondos.isDEB_CUENTA_DEBITO()
                    || dynMovFondos.isCRE_CHEQUE_PROPIO()) {
                    */
                    if (dynMovFondos.isCRE_CHEQUE_PROPIO()) {
                        List list = movFondos.getC_MOVIMIENTOFONDOS_CRE_ID();
                        for (int i = 0; i < list.size(); i++) {
                            MVALORPAGO valor = MVALORPAGO.get(((MMOVIMIENTOFONDOSCRE) list.get(i)).getC_BankAccount_ID(), ((MMOVIMIENTOFONDOSCRE) list.get(i)).getNroCheque(), null);
                            MBankAccount bank = new MBankAccount(getCtx(), ((MMOVIMIENTOFONDOSCRE) list.get(i)).getC_BankAccount_ID(), null);
                            if (valor != null) {
                                detalle += "_" + bank.getName() + "_" + valor.getNroCheque() + "_" + valor.getVencimientoDate();
                            } else {
                                detalle += "_" + bank.getName() + "_" + ((MMOVIMIENTOFONDOSCRE) list.get(i)).getNroCheque();
                            }
                        }
                    }
                    /*
                    if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_DepositoPendiente)
                    || dynMovFondos.isDEB_CUENTA_BANCO()
                    || dynMovFondos.isCRE_CUENTA_BANCO()) {
                    */
                    if (dynMovFondos.isCRE_DEPOSITO_PEND()) {
                        List list = movFondos.getC_MOVIMIENTOFONDOS_CRE_ID();
                        for (int i = 0; i < list.size(); i++) {
                            MBankAccount bank = new MBankAccount(getCtx(), ((MMOVIMIENTOFONDOSCRE) list.get(i)).getC_BankAccount_ID(), null);
                            detalle += "_" + bank.getName();
                        }
                    }
                    
                    
                }

            } else if (AD_TABLE_ID == MPayment.getTableId(MPayment.Table_Name)) {
                MPayment payment = new MPayment(getCtx(), RECORD_ID, null);
                MDocType docType = new MDocType(getCtx(), payment.getC_DocType_ID(), null);
                detalle = docType.getName() + " - " + payment.getDocumentNo();
                if (nro != null && nro.trim().length() > 0) {
                    detalle += " - " + nro;
                }
            } else if (AD_TABLE_ID == MAllocationHdr.getTableId(MAllocationHdr.Table_Name)) {
                MAllocationHdr allocate = new MAllocationHdr(getCtx(), RECORD_ID, null);
                String desc = allocate.getDescription();
                int index = desc.indexOf(":");
                if (index != -1) {
                    detalle = "Asignación " + allocate.getDocumentNo() + " - " + desc.substring(0, index);
                } else {
                    detalle = "Asignación " + allocate.getDocumentNo();
                }
            } else {
                detalle = "";
                if (nro != null && nro.trim().length() > 0) {
                    detalle = nro;
                } else {
                    nro = "";
                }
                if (nro.indexOf("#") > 0) {
                    detalle = nro.substring(0, nro.indexOf("#"));
                }
                PreparedStatement pstm = DB.prepareStatement("SELECT NAME FROM AD_TABLE_TRL WHERE AD_TABLE_ID = " + AD_TABLE_ID, null);
                ResultSet rs = pstm.executeQuery();
                if (rs.next()) {
                    if (detalle != null && detalle.trim().length() > 0) {
                        detalle = rs.getString(1) + " " + detalle;
                    } else {
                        detalle = rs.getString(1);
                    }
                }
                rs.close();
                pstm.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (detalle.length() > 3000)
            detalle = detalle.substring(0, 2999);
        return detalle;
    }

    protected String rellenarNro(String nro) {
        if (nro != null) {
            switch (nro.length()) {
                case 1:
                    return "0000000" + nro;
                case 2:
                    return "000000" + nro;
                case 3:
                    return "00000" + nro;
                case 4:
                    return "0000" + nro;
                case 5:
                    return "000" + nro;
                case 6:
                    return "00" + nro;
                case 7:
                    return "0" + nro;
                default:
                    return nro;
            }
        } else {
            return "00000000";
        }
    }
}