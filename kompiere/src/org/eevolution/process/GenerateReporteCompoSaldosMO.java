/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eevolution.process;

import org.compiere.model.MBPartner;
import org.compiere.process.*;
import java.math.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.compiere.model.MBPGroup;
import org.compiere.model.MInvoice;
import org.compiere.util.*;
import org.eevolution.tools.UtilProcess;

/**
 *  Clase que ingresa las filas a la tabla T_CC_BARTNER_DETALLE
 * @author Daniel BISion
 */
public class GenerateReporteCompoSaldosMO extends SvrProcess {

//PARAMETROS DE ENTRADA
    int p_instance;
    private String fromBPartner = null;
    private String toBPartner = null;
    private BigDecimal C_Currency_ID = new BigDecimal(0);
    private BigDecimal C_BPartner_Location_ID = new BigDecimal(0);
    private Boolean isSOTrx = false;
    private String group = null;
    private BigDecimal AD_USER_ID = new BigDecimal(0);
    private String parametros = "";

    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        p_instance = 0;
        boolean flagCliente = false;
        String sqlOrg = "SELECT DISTINCT(ad_org_id) FROM c_invoice";
        PreparedStatement psO = DB.prepareStatement(sqlOrg, null);
        try {
            ResultSet rsO = psO.executeQuery();
            rsO.next();
            int org = rsO.getInt(1);
            psO.close();
            rsO.close();
            for (int i = 0; i < para.length; i++) {
                String name = para[i].getParameterName();
                if (name.equals("C_Currency_ID")) {
                    C_Currency_ID = ((BigDecimal) para[i].getParameter());
                    String nameC = "SELECT ISO_CODE FROM C_CURRENCY WHERE c_currency_id = " + C_Currency_ID;
                    PreparedStatement psC = DB.prepareStatement(nameC, null);
                    ResultSet rs = psC.executeQuery();
                    if (rs.next()) {
                        parametros = parametros.concat(" Moneda: " + rs.getString(1));
                    } else {
                        parametros = parametros.concat(" Socio Desde: " + "");
                    }
                    rs.close();
                    psC.close();
                } else {
                    if (name.equals("isSOTrx")) {
                        if (((String) para[i].getParameter()).equals("Y")) {
                            isSOTrx = true;
                            parametros = parametros.concat(" Cliente: " + "Y");
                        } else {
                            isSOTrx = false;
                            parametros = parametros.concat(" Cliente: " + "N");
                        }
                    } else {
                        if (name.equals("C_BPartner_ID")) {
                            if (flagCliente == false) {
                                fromBPartner = para[i].getParameter().toString();
                                String nameBPartner = "SELECT NAME FROM C_BPARTNER WHERE c_bpartner_id = " + fromBPartner;
                                PreparedStatement psBP = DB.prepareStatement(nameBPartner, null);
                                ResultSet rs = psBP.executeQuery();
                                if (rs.next()) {
                                    parametros = parametros.concat(" Socio Desde: " + rs.getString(1));
                                } else {
                                    parametros = parametros.concat(" Socio Desde: " + "");
                                }
                                rs.close();
                                psBP.close();
                                flagCliente = true;
                            } else {
                                toBPartner = para[i].getParameter().toString();
                                String nameBPartner = "SELECT NAME FROM C_BPARTNER WHERE c_bpartner_id = " + toBPartner;
                                PreparedStatement psBP = DB.prepareStatement(nameBPartner, null);
                                ResultSet rs = psBP.executeQuery();
                                if (rs.next()) {
                                    parametros = parametros.concat(" Socio Hasta: " + rs.getString(1));
                                } else {
                                    parametros = parametros.concat(" Socio Hasta: " + "");
                                }
                                psBP.close();
                                rs.close();
                            }
                        } else {
                            if (name.equals("C_BPartner_Location_ID")) {
                                C_BPartner_Location_ID = ((BigDecimal) para[i].getParameter());
                                String nameBPartnerLoc = "SELECT NAME FROM C_BPARTNER_LOCATION WHERE c_bpartner_location_id = " + C_BPartner_Location_ID;
                                PreparedStatement psBPL = DB.prepareStatement(nameBPartnerLoc, null);
                                ResultSet rs = psBPL.executeQuery();
                                if (rs.next()) {
                                    parametros = parametros.concat(" Localizacion: " + rs.getString(1));
                                } else {
                                    parametros = parametros.concat(" Localizacion: " + "");
                                }
                                psBPL.close();
                                rs.close();
                            } else {
                                /*
                                 * 
                                 *   Agregamos la asignacion del grupo de Socio de Negocio 
                                 *   por el cual se eligio filtrar.
                                 * 
                                 *   Maria Jesus Martin 22/06/2012 
                                 * 
                                 */
                                if (name.equals("C_BP_Group_ID")) {
                                    group = para[i].getParameter().toString();
                                    String nameBPGroup = "SELECT NAME FROM C_BP_GROUP WHERE c_bp_group_id = " + group;
                                    PreparedStatement psBPG = DB.prepareStatement(nameBPGroup, null);
                                    ResultSet rs = psBPG.executeQuery();
                                    if (rs.next()) {
                                        parametros = parametros.concat(" Grupo: " + rs.getString(1));
                                    } else {
                                        parametros = parametros.concat(" Grupo: " + "");
                                    }
                                    psBPG.close();
                                    rs.close();
                                } else {
                                    /*
                                     * 
                                     *   Agregamos la asignacion del Vendedor  
                                     *   por el cual se eligio filtrar.
                                     * 
                                     *   Maria Jesus Martin 25/06/2012 
                                     * 
                                     */
                                    if (name.equals("AD_User_ID")) {
                                        AD_USER_ID = ((BigDecimal) para[i].getParameter());
                                        String nameAdUser = "SELECT NAME FROM AD_USER WHERE ad_user_id = " + AD_USER_ID;
                                        PreparedStatement psADU = DB.prepareStatement(nameAdUser, null);
                                        ResultSet rs = psADU.executeQuery();
                                        if (rs.next()) {
                                            parametros = parametros.concat(" Vendedor: " + rs.getString(1));
                                        } else {
                                            parametros = parametros.concat(" Vendedor: " + "");
                                        }
                                        psADU.close();
                                        rs.close();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenerateReporteCtaCteMonedaOriginal31.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    protected String doIt() throws Exception {
        //BORRADO DE LA TABLA T_COMPOSALDOS_BPARTNER
        String sql;
        log.info("Comienzo del proceso de Composici�n de Saldos");

        log.info("Borrado de la tabla temporal T_COMPSALDOS_BPARTNER");
        DB.executeUpdate("Delete from T_COMPSALDOS_BPARTNER", null);

        log.info("Borrado de la tabla temporal T_COMPSALDOS_BPARTNER_DETALLE");
        DB.executeUpdate("Delete from T_COMPSALDOS_BPARTNER_DETALLE", null);

        boolean param = true;

        ResultSet rs = getComprobantes();

        int CS_BPARTNER_DETALLE_ID = 1000000;
        int CS_BPARTNER_ID = 1000000;

        if (rs.next()) {
            while (!rs.isAfterLast()) {

                int cliente = rs.getInt(1);
                int organizacion = rs.getInt(2);
                int C_BPartner_ID = rs.getInt(10);
                int C_Currency_ID = rs.getInt(9);

                int C_Location_ID = rs.getInt(18);
                String C_Location_Name = rs.getString(19);

                // 	  Ingreso en la tabla cabecera
                sql = "INSERT INTO T_COMPSALDOS_BPARTNER VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement ps = DB.prepareStatement(sql, null);
                ps.setInt(1, cliente);						//	CLIENTE
                ps.setInt(2, organizacion);					//	ORGANIZACION
                if (fromBPartner != null) // 	C_BPARTNER_ID
                {
                    ps.setInt(3, Integer.parseInt(fromBPartner));
                } else if (toBPartner != null) {
                    ps.setInt(3, Integer.parseInt(toBPartner));
                } else {
                    ps.setInt(3, C_BPartner_ID);		//	C_BPARTNER_ID
                }
                ps.setInt(4, p_instance);					//	INSTANCE
                ps.setInt(5, C_Currency_ID);				//	C_CURRENCY_ID
                ps.setString(6, rs.getString(12));			//	NAME
                if (fromBPartner != null) // 	VALUE
                {
                    MBPartner partner = new MBPartner(getCtx(), Integer.parseInt(fromBPartner), null);
                    ps.setString(7, partner.getValue());
                } else if (toBPartner != null) {
                    MBPartner partner = new MBPartner(getCtx(), Integer.parseInt(toBPartner), null);
                    ps.setString(7, partner.getValue());
                } else {
                    ps.setString(7, rs.getString(9));	// 	VALUE
                }
                if (isSOTrx.booleanValue() == true) {
                    ps.setString(8, "Y");
                } else {
                    ps.setString(8, "N");					// 	ISSOTRX
                }
                ps.setDate(9, rs.getDate(3));				//	FECHA
                ps.setInt(10, C_Location_ID);				//	BP_LOCATION_ID
                ps.setString(11, C_Location_Name);			// 	BP_LOCATION_NAME
                ps.setInt(12, CS_BPARTNER_ID);				//	T_CS_BPARTNER_ID
                ps.setString(13, rs.getString(11));			// 	CODE_BP
                ps.setBigDecimal(14, null);					// 	COTIZACION
                /*
                 * 
                 *   Agregamos el grupo de Socio de Negocio que se utiliza para el filtro.
                 * 
                 *   Maria Jesus Martin 22/06/2012 
                 * 
                 */
                ps.setString(15, rs.getString(20));                            //      GROUP
		/*
                 * 
                 *   Agregamos el Vendedor que se utiliza para el filtro.
                 * 
                 *   Maria Jesus Martin 25/06/2012 
                 * 
                 */
                ps.setInt(16, rs.getInt(21));                             //   VENDEDOR
                if (param) {
                    ps.setString(17, parametros);
                    param = false;
                } else {
                    ps.setString(17, null);
                }


                ps.executeUpdate();
                ps.close();
                ps = null;

                BigDecimal saldoPendiente = new BigDecimal(0);
                //BigDecimal saldoTotal = new BigDecimal(0);

                //Si cambia de socio o de localizacion entonces comienza un nuevo detalle
                while (!(rs.isAfterLast() || (C_BPartner_ID != rs.getInt(10)) || (C_Location_ID != rs.getInt(18)))) {
                    // 	Ingreso en la tabla el detalle los totales
                    sql = "INSERT INTO T_COMPSALDOS_BPARTNER_DETALLE VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    ps = DB.prepareStatement(sql, null);
                    ps.setInt(1, cliente);						//	CLIENTE
                    ps.setInt(2, organizacion);					//	ORGANIZACION
                    //Luego del 'Y'
                    ps.setInt(3, C_BPartner_ID);				//	C_BPARTNER_ID
                    ps.setInt(4, p_instance);					//	INSTANCE
                    ps.setDate(5, rs.getDate(3));				//	FECHA
                    ps.setString(6, rs.getString(4));			//	CONCEPTO
                    ps.setString(7, rs.getString(5));			//	NUMERO
                    ps.setDate(8, rs.getDate(6));				//	VENCIMIENTO
                    ps.setString(9, rs.getString(7));			//	CONDICIONES

                    ps.setBigDecimal(10, rs.getBigDecimal(13));	//	MONTO
                    //	Actualizo el saldo acumulado total       
                    //	saldoTotal = saldoTotal.add(rs.getBigDecimal(13));

                    ps.setBigDecimal(11, getSaldoAbierto(rs.getInt(14), rs.getInt(15), rs.getBigDecimal(13)));	//	PENDIENTE
                    //	Actualizo el saldo acumulado pendiente           
                    saldoPendiente = saldoPendiente.add(getSaldoAbierto(rs.getInt(14), rs.getInt(15), rs.getBigDecimal(13)));

                    ps.setInt(12, rs.getInt(8));				//	MORA
                    ps.setInt(13, CS_BPARTNER_DETALLE_ID);		//	T_CS_BPARTNER_DETALLE_ID
                    ps.setString(14, rs.getString(17));			//	CODIGO_MONEDA
                    ps.setInt(15, CS_BPARTNER_ID);				//	T_CS_BPARTNER_ID
                    
                    String relatedOCs = "";
                    //Factura
                    if (rs.getInt(15) == 0 ) {
                        //Seteo OCS
                        MInvoice invoice = new MInvoice(getCtx(),rs.getInt(14),null);
                        relatedOCs =  invoice.getAllocatedOCs();
                    }

                    ps.setString(16, relatedOCs);

                    ps.executeUpdate();
                    ps.close();
                    ps = null;

                    //Incremento CS_BARTNER_DETALLE_ID
                    CS_BPARTNER_DETALLE_ID++;

                    rs.next();
                }

                // 	Ingreso en la tabla el subtotal
                sql = "INSERT INTO T_COMPSALDOS_BPARTNER_DETALLE VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                ps = DB.prepareStatement(sql, null);
                ps.setInt(1, cliente);					//	CLIENTE
                ps.setInt(2, organizacion);				//	ORGANIZACION
                //Luego del 'Y'
                ps.setInt(3, C_BPartner_ID);			//	C_BPARTNER_ID
                ps.setInt(4, p_instance);				//	INSTANCE
                ps.setDate(5, null);						//	FECHA
                ps.setString(6, "Total:");				//	CONCEPTO
                ps.setString(7, null);					//	NUMERO
                ps.setDate(8, null);					//	VENCIMIENTO
                ps.setString(9, null);					//	CONDICIONES
                ps.setBigDecimal(10, null);				//	MONTO
                ps.setBigDecimal(11, saldoPendiente);	//	PENDIENTE
                ps.setInt(12, 0);						//	MORA
                ps.setInt(13, CS_BPARTNER_DETALLE_ID);	//	CS_BPARTNER_DETALLE_ID
                ps.setString(14, null);					//	CODIGO_MONEDA
                ps.setInt(15, CS_BPARTNER_ID);			//	CS_BPARTNER_ID
                ps.setString(16, null);	

                ps.executeUpdate();
                ps.close();
                ps = null;

                //Incremento CS_BARTNER_DETALLE_ID
                CS_BPARTNER_DETALLE_ID++;

                //	Incremento CS_BPARTNER_ID
                CS_BPARTNER_ID++;

            }

            rs.close();
            rs = null;
            UtilProcess.initViewer("Composicion Saldos en Moneda Original", 0, getProcessInfo());
            return "";
        }

        return "El Reporte no arrojo Datos para los parámetros ingresados";
    }
    
    /** BISion - 08/05/2009 - Daniel Gini
     * Metodo que retorna los comprobantes por socio de negocio, filtrando por los parametros ingresados
     * ordenados por fecha del comprobante.
     * @return ResultSet
     * @throws Exception
     */
    private ResultSet getComprobantes() throws Exception {
        //CONSULTA PARA OBTENER LOS DATOS
        String sql =
                "SELECT * "
                + "FROM ( "
                + "SELECT  i.AD_CLient_ID as CLIENTE, "
                + " 		 i.AD_ORG_ID AS ORG, "
                + "		 i.DATEINVOICED as FECHA, "
                + "        d.NAME as TIPO, "
                + "        i.DOCUMENTNO as NUMERO, "
                + "        paymentTermDueDate (t.C_PaymentTerm_ID, i.DATEINVOICED) as VENCIMIENTO, "
                + "        t.name as CONDICION, "
                + "        trunc(sysdate - paymentTermDueDate (t.C_PaymentTerm_ID, i.DATEINVOICED)) as MORA, "
                + "        i.C_CURRENCY_ID as MONEDA, "
                + "        b.C_BPartner_ID as BP_ID, "
                + "        b.VALUE as CLAVE, "
                + "        b.name as NOMBRE, "
                + "        CASE d.docbasetype "
                + "        	WHEN 'ARI' THEN i.grandtotal "
                + "        	WHEN 'API' THEN -i.grandtotal "
                + "        	WHEN 'ARF' THEN i.grandtotal "
                + "        	WHEN 'ARC' THEN -i.grandtotal "
                + "        	WHEN 'APC' THEN i.grandtotal "
                + "        	ELSE 0 "
                + " 		 END as MONTO, "
                + "		 i.C_Invoice_ID as ID, "
                + "		 0 as PAGO, "
                + "		 CASE"
                + "			 WHEN i.COTIZACION is null THEN 1 "
                + "			 ELSE i.COTIZACION"
                + "            END as COTIZACION,"
                + "        y.ISO_CODE as CODMONEDA,"
                + "        CASE"
                + "       	 WHEN bpl.C_BPartner_Location_ID is null"
                + "        	 THEN 0"
                + "		  	 ELSE bpl.C_BPartner_Location_ID"
                + "		 END as C_BPartner_Location_ID,"
                + "		 CASE"
                + "		 	 WHEN bpl.C_BPartner_Location_ID is null"
                + "  		 	 THEN 'SIN ASIGNAR'"
                + "            ELSE TO_CHAR(bpl.name)"
                + "		 END as C_BPartner_Location_Name ,"
                + /*
                 *   
                 *   Se agrego a la sentencia b.C_BP_Group_ID as GROUPBP ya que agregamos
                 *   el grupo de socio de negocio como nuevo filtro.
                 * 
                 *   Maria Jesus Martin 22/06/2012 
                 * 
                 */ "            b.C_BP_Group_ID as GROUPBP, "
                /*
                 *   
                 *   Se agrego a la sentencia bpl.sellresp_ID as VENDEDOR ya que agregamos
                 *   el vendedor como nuevo filtro.
                 * 
                 *   Maria Jesus Martin 25/06/2012 
                 * 
                 */
                + " bpl.sellresp_ID as VENDEDOR "
                + "FROM C_Invoice i "
                + " INNER JOIN C_BPARTNER b ON(i.C_BPARTNER_ID = b.C_BPARTNER_ID) "
                + " INNER JOIN C_DocType d ON(i.C_DocType_ID = d.C_DocType_ID) "
                + " INNER JOIN C_PaymentTerm t ON(i.C_PaymentTerm_ID = t.C_PaymentTerm_ID) "
                + " INNER JOIN C_Currency y ON(y.C_Currency_ID = i.C_Currency_ID) "
                + " LEFT JOIN C_Location l ON (i.BILL_LOCATION_ID = l.C_Location_ID)"
                + /* 05-05-2011 Camarzana Mariano
                 * Se agrego a la sentencia
                 * LEFT JOIN C_BPartner_Location bpl ON (bpl.C_LOCATION_ID = l.C_Location_ID 
                 * la restriccion bpl.c_bpartner_id = b.c_bpartner_id, debido a que en el caso 
                 * en que una misma direccion este asignada a dos socios
                 * 
                 */ " LEFT JOIN C_BPartner_Location bpl ON (bpl.C_LOCATION_ID = l.C_Location_ID and bpl.c_bpartner_id = b.c_bpartner_id)"
                + getSqlWhere("i.ISSOTRX", "i.C_CURRENCY_ID", "i.DocStatus", "i.IsPaid", "i.DATEINVOICED")
                + /*                         
                 * 
                 *  Anexo para considerar los documentos activos en modificación para ajuste de 
                 *  la composición de saldos.
                 * 
                 *  José Fantasia 26/12/2011                         
                 */ " and i.isactive = 'Y' "
                + " UNION "
                + "(SELECT C_Payment.AD_CLient_ID as CLIENTE, "
                + "		 C_Payment.AD_ORG_ID AS ORG, "
                + "		 C_Payment.DATETRX as FECHA, "
                + "        d.NAME as TIPO, "
                + "		 C_Payment.DOCUMENTNO as NUMERO, "
                + "		 null as VENCIMIENTO, "
                + "		 null as CONDICION, "
                + "		 null as MORA, "
                + "        C_Payment.C_CURRENCY_ID as MONEDA, "
                + "        b.C_BPartner_ID as BP_ID, "
                + "        b.VALUE as CLAVE, "
                + "        b.name as NOMBRE, "
                + "        CASE d.docbasetype "
                + "        	WHEN 'ARR' THEN -C_Payment.PAYAMT "
                + "        	WHEN 'APP' THEN C_Payment.PAYAMT "
                + "        	ELSE 0 "
                + " 		 END as MONTO, "
                + "		 C_Payment.C_Payment_ID as ID, "
                + "		 1 as PAGO, "
                + "		 CASE"
                + "			WHEN C_Payment.COTIZACION is null THEN 1 "
                + "			ELSE C_Payment.COTIZACION "
                + "			END as COTIZACION, "
                + "		y.ISO_CODE as CODMONEDA, "
                + "            CASE"
                + "       	       WHEN bpl.C_BPartner_Location_ID is null"
                + "        	       THEN 0"
                + "		 ELSE bpl.C_BPartner_Location_ID"
                + "		 END as C_BPartner_Location_ID,"
                + "		 CASE"
                + "		 	 WHEN bpl.C_BPartner_Location_ID is null"
                + "  		 	 THEN 'SIN ASIGNAR'"
                + "            ELSE TO_CHAR(bpl.name)"
                + "		 END as C_BPartner_Location_Name ,"
                + " b.C_BP_Group_ID as GROUPBP, "
                + " bpl.sellresp_ID as VENDEDOR "
                + " FROM C_Payment "
                + " INNER JOIN C_BPARTNER b ON(C_Payment.C_BPARTNER_ID = b.C_BPARTNER_ID) "
                + " INNER JOIN C_DocType d ON(C_Payment.C_DocType_ID = d.C_DocType_ID) "
                + " INNER JOIN C_Currency y ON(y.C_Currency_ID = C_Payment.C_Currency_ID) "
                /*
                 *  Zynnia 13/08/2012
                 *  Cambiamos ON (bpl.c_bpartner_id = b.c_bpartner_id)
                 *  ya que en los casos en los que los socios tienen mas de una localizacion
                 *  duplicaba las OP tantas veces como localizaciones tenga el socio.
                 *  El JOIN lo tenia que hacer por el campo de la localizacion de la OP.
                 *  
                 */
                + "LEFT JOIN C_BPartner_Location bpl ON (bpl.c_bpartner_location_id = C_Payment.c_bpartner_location_id)"
                + getSqlWhere("C_Payment.ISRECEIPT", "C_Payment.C_CURRENCY_ID", "C_Payment.DocStatus", "C_Payment.ISALLOCATED", "C_Payment.DATETRX")
                + /*                         
                 * 
                 *  Anexo para considerar los documentos activos en modificación para ajuste de 
                 *  la composición de saldos.
                 * 
                 *  José Fantasia 26/12/2011                         
                 */ " and C_Payment.isactive = 'Y'"
                + ") "
                + ") ORDER BY CLAVE,C_BPartner_Location_ID,FECHA";

        PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
        int paramIndex = 1;

        //INVOICE
        if (isSOTrx.booleanValue() == true) {
            pstmt.setString(paramIndex, "Y");
        } else {
            pstmt.setString(paramIndex, "N");
        }
        paramIndex++;

        if (fromBPartner != null) {
            MBPartner partner = new MBPartner(getCtx(), Integer.parseInt(fromBPartner), null);
            pstmt.setString(paramIndex, partner.getValue());
            paramIndex++;
        }
        if (toBPartner != null) {
            MBPartner partner = new MBPartner(getCtx(), Integer.parseInt(toBPartner), null);
            pstmt.setString(paramIndex, partner.getValue());
            paramIndex++;
        }

        if (C_Currency_ID != null) {
            pstmt.setInt(paramIndex, C_Currency_ID.intValue());
            paramIndex++;
        }


        //PAYMENT
        if (isSOTrx.booleanValue() == true) {
            pstmt.setString(paramIndex, "Y");
        } else {
            pstmt.setString(paramIndex, "N");
        }
        paramIndex++;

        if (fromBPartner != null) {
            MBPartner partner = new MBPartner(getCtx(), Integer.parseInt(fromBPartner), null);
            pstmt.setString(paramIndex, partner.getValue());
            paramIndex++;
        }
        if (toBPartner != null) {
            MBPartner partner = new MBPartner(getCtx(), Integer.parseInt(toBPartner), null);
            pstmt.setString(paramIndex, partner.getValue());
            paramIndex++;
        }

        if (C_Currency_ID != null) {
            pstmt.setInt(paramIndex, C_Currency_ID.intValue());
            paramIndex++;
        }

        return pstmt.executeQuery();
    }

    /** BISion - 08/05/2009 - Daniel Gini
     * Metodo que retorna la clausula "where" dependiendo de los parametros ingresados.
     * @return String
     */
    private String getSqlWhere(String param1, String param2, String param3, String param4, String param5) {
        String BP = "VALUE";
        String sqlWhere = " WHERE " + param1 + " = ?";

        if (toBPartner != null && fromBPartner != null) {
            sqlWhere += " AND b." + BP + "  >= ? AND b." + BP + " <= ?";
        } else if (toBPartner != null && fromBPartner == null) {
            sqlWhere += " AND b." + BP + " <= ?";
        } else if (toBPartner == null && fromBPartner != null) {
            sqlWhere += " AND b." + BP + " >= ?";
        }
        if (group != null) {
            sqlWhere += " AND b.C_BP_Group_ID = " + group;
        }

        if (C_Currency_ID != null) {
            sqlWhere += " AND " + param2 + " = ?";
        }

        if (!AD_USER_ID.equals(BigDecimal.ZERO)) {
            sqlWhere += " AND bpl.sellresp_ID = " + AD_USER_ID;
        }

        sqlWhere += " AND " + param3 + " IN ('CO','CL')";

        sqlWhere += " AND " + param4 + " = 'N'";

        if (!C_BPartner_Location_ID.equals(BigDecimal.ZERO)) {
            sqlWhere += " AND C_BPartner_Location_ID = " + C_BPartner_Location_ID;
        }
        /*
         *  02/08/2013 Maria Jesus Martin
         *  Se comenta la verificación de fecha, para que traiga todos los comprobantes.
         */
        /*
         *  17/06/2013 Maria Jesus Martin
         *  Modificacion para que tome los comprobantes con fechas mayores al 01/12/2011
         */
        /*
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(2011, Calendar.NOVEMBER, 30, 00, 00, 00);
        Date desde = cal.getTime();
        
        sqlWhere+= " AND "+param5+" >= to_date('01/12/2011','DD/MM/RRRR')";
         */

        return sqlWhere;
    }
    private int FACTURA = 0;
    private int PAGO = 1;

    /** BISion - 08/05/2009 - Daniel Gini
     * Retorna el saldo pendiente para una factura o pago, ingresado por par�metro.
     * @param C_Comprobante_ID
     * @param Tipo
     * @throws Exception
     * @return BigDecimal
     */
    private BigDecimal getSaldoAbierto(int C_Comprobante_ID, int tipo, BigDecimal monto) throws Exception {
        String sql = null;
        if (tipo == FACTURA) {
            sql = "Select case when (sum(AMOUNT) is Null) then 0 else sum(AMOUNT) end as monto From C_AllocationLine Where C_INVOICE_ID = ?";
        } else if (tipo == PAGO) {
            sql = "Select case when (sum(AMOUNT) is Null) then 0 else sum(AMOUNT) end as monto From C_AllocationLine Where C_PAYMENT_ID = ?";
        }

        PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
        pstmt.setInt(1, C_Comprobante_ID);

        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            BigDecimal bd = rs.getBigDecimal(1);
            pstmt.close();
            rs.close();

            if ((bd.compareTo(BigDecimal.ZERO) < 0 && monto.compareTo(BigDecimal.ZERO) > 0) || (bd.compareTo(BigDecimal.ZERO) > 0 && monto.compareTo(BigDecimal.ZERO) < 0)) {
                return monto.add(bd);
            } else {
                return monto.subtract(bd);
            }

        }

        return monto;
    }
}