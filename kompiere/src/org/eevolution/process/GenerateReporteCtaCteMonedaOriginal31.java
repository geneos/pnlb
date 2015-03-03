/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;
import java.math.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.compiere.model.MBPartner;
import org.compiere.process.*;
import org.compiere.util.*;
import org.eevolution.tools.UtilProcess;

/**
 *  Clase que ingresa las filas a la tabla T_CC_BARTNER_DETALLE
 * @author Daniel - BISion
 */
public class GenerateReporteCtaCteMonedaOriginal31 extends SvrProcess{

//PARAMETROS DE ENTRADA
int p_instance;
private Timestamp fromDate = null;
private Timestamp toDate = null;
private Timestamp fromVenc = null;
private Timestamp toVenc = null;
private String fromBPartner = null;
private String toBPartner = null;
private BigDecimal C_Currency_ID = new BigDecimal(0);
private Boolean isSOTrx = false;
private Hashtable conSaldo = new Hashtable();
private String parametros = "";

protected void prepare(){
    p_instance = 0;
    ProcessInfoParameter[] para = getParameter();
    boolean flagCliente = false;
    PreparedStatement psI = null;
    DB.executeUpdate("Delete from T_Parametros_PrintFormat", null);
    String sqlOrg = "SELECT DISTINCT(ad_org_id) FROM c_invoice";
    int org = 0;
    PreparedStatement psO = DB.prepareStatement(sqlOrg, null);
    try {
        ResultSet rsO = psO.executeQuery();
        rsO.next();
        org = rsO.getInt(1);
        psO.close();
        rsO.close();
        for (int i = 0; i < para.length; i++)
            {
            String name = para[i].getParameterName();
            if(name.equals("DateTrx")){
                fromDate=(Timestamp)para[i].getParameter();
                java.sql.Date date = new java.sql.Date(fromDate.getTime());
                parametros = parametros.concat(" Fecha Desde: "+ date.toString());
                toDate=(Timestamp)para[i].getParameter_To();
                date = new java.sql.Date(toDate.getTime());
                parametros = parametros.concat(" Fecha Hasta: "+ date.toString());
            }
            else
            {	
                if (name.equals("C_Currency_ID")){
                            C_Currency_ID = ((BigDecimal)para[i].getParameter());
                            String nameC = "SELECT ISO_CODE FROM C_CURRENCY WHERE c_currency_id = "+ C_Currency_ID;
                            PreparedStatement psC = DB.prepareStatement(nameC,null);
                            ResultSet rs = psC.executeQuery();
                            if(rs.next()){
                                parametros = parametros.concat(" Moneda: "+ rs.getString(1));
                            }
                            else 
                            {
                                parametros = parametros.concat(" Socio Desde: "+ "");
                            }
                            rs.close();
                            psC.close();
                }
                else
                {
                   if (name.equals("isSOTrx"))
                   {
                       if (((String)para[i].getParameter()).equals("Y"))
                       {
                            isSOTrx = true;
                            parametros = parametros.concat(" Cliente: "+ "Y");
                       }
                       else
                       {
                            isSOTrx = false;
                            parametros = parametros.concat(" Cliente: "+ "N");
                       }
                   }
                   else
                       if(name.equals("C_BPartner_ID"))
                       {
                          if (flagCliente == false)
                          {
                                fromBPartner=para[i].getParameter().toString();
                                String nameBPartner = "SELECT NAME FROM C_BPARTNER WHERE c_bpartner_id = "+ fromBPartner;
                                PreparedStatement psBP = DB.prepareStatement(nameBPartner,null);
                                ResultSet rs = psBP.executeQuery();
                                if(rs.next()){
                                    parametros = parametros.concat(" Socio Desde: "+ rs.getString(1));
                                }
                                else 
                                {
                                    parametros = parametros.concat(" Socio Desde: "+ "");
                                }
                                rs.close();
                                psBP.close();
                                flagCliente = true;
                          }
                          else
                          {
                                toBPartner=para[i].getParameter().toString();
                                String nameBPartner = "SELECT NAME FROM C_BPARTNER WHERE c_bpartner_id = "+ toBPartner;
                                PreparedStatement psBP = DB.prepareStatement(nameBPartner,null);
                                ResultSet rs = psBP.executeQuery();
                                if(rs.next()){
                                    parametros = parametros.concat(" Socio Hasta: "+ rs.getString(1));
                                }
                                else
                                {
                                    parametros = parametros.concat(" Socio Hasta: "+ "");
                                }
                                psBP.close();
                                rs.close();
                          }
                       }
                       else
                       /*
                       *  11/09/2012 Zynnia.
                       *  Agregamos el parametro de fecha de vencimiento, el cual es calculado a partir
                       *  del termino de pago del socio de negocio. Se calcula la fecha de contabilidad + 
                       *  los dias del termino de pago.(entre que fechas de vencimiento se quiere el reporte)
                       * 
                       */
                            if(name.equals("DateVenc")){
                                fromVenc=(Timestamp)para[i].getParameter();
                                java.sql.Date date = new java.sql.Date(fromVenc.getTime());
                                parametros = parametros.concat(" Vencimiento Desde: "+ date.toString());
                                toVenc=(Timestamp)para[i].getParameter_To();
                                date = new java.sql.Date(toVenc.getTime());
                                parametros = parametros.concat(" Vencimiento Hasta: "+ date.toString());
                            }
                }
            }
        }
    }
    catch (SQLException ex) 
    {
        Logger.getLogger(GenerateReporteCtaCteMonedaOriginal31.class.getName()).log(Level.SEVERE, null, ex);
    }
}

protected String doIt() throws Exception {

    // Valido primero las fechas, si la fecha de inicio es menor al 1/9/2011 saco error.

    // Agregar mensaje con sistema de compiere


    GregorianCalendar cal = new GregorianCalendar();
    cal.set(2011, Calendar.NOVEMBER, 30, 00, 00, 00);
    Date desde = cal.getTime();

    if( fromDate!= null && fromDate.before(desde))
        try {
            throw new Exception ("Fecha anterior al 01/12/2011 no son válidas");
        } catch (Exception ex) {
            Logger.getLogger(GenerateReporteCtaCteMonedaOriginal31.class.getName()).log(Level.SEVERE, null, ex);
            return "Fecha anterior al 01/12/2011 no son válidas";
        }
    /*
     *
     * 23/05/2013 Maria Jesus Martin
     * Agregado para que verifique que la fecha "Desde" sea anterior que la de "Hasta".
     *
     */
    if(toDate!=null && fromDate !=null && toDate.before(fromDate))
        try {
            throw new Exception ("La fecha Desde es posterior a la fecha Hasta.");
        } catch (Exception ex) {
            Logger.getLogger(GenerateReporteCtaCteMonedaOriginal31.class.getName()).log(Level.SEVERE, null, ex);
            return "La fecha Desde es posterior a la fecha Hasta.";
        }

    
    boolean flag = true;
    String sqlBloqueo;
    PreparedStatement psBloqueo;
    ResultSet rsBloqueo;

    while(flag) {

        sqlBloqueo = "SELECT bloqueado FROM T_Bloqueos WHERE AD_Process_ID = 1000235";
        psBloqueo = DB.prepareStatement(sqlBloqueo, get_TrxName());
            try {
                rsBloqueo = psBloqueo.executeQuery();

                if(rsBloqueo.next()) {

                        if(rsBloqueo.getString(1).equals("N")){
                            log.info("Bloqueo de doIt para Reporte de ctas ctes");
                            String sqlUpdateBloqueo ="update T_Bloqueos set bloqueado = 'Y' where AD_Process_ID = 1000235";
                            DB.executeUpdate(sqlUpdateBloqueo,null);
                            flag = false;
                        }

                }
                rsBloqueo.close();
                psBloqueo.close();
                
            } catch (SQLException ex) {
                Logger.getLogger(GenerateReporteCtaCteMonedaOriginal31.class.getName()).log(Level.SEVERE, null, ex);
            }


    }    

    //BORRADO DE LA TABLA T_CTACTE_BPARTNER
    String sql;
    log.info("Comienzo del proceso de Cuenta Corriente");

    log.info("Borrado de la tabla temporal T_CTACTE_BPARTNER");
    DB.executeUpdate("Delete from T_CTACTE_BPARTNER", null);

    //BORRADO DE LA TABLA T_CTACTE_BPARTNER_DETALLE
    log.info("Borrado de la tabla temporal T_CTACTE_BPARTNER_DETALLE");
    DB.executeUpdate("Delete from T_CTACTE_BPARTNER_DETALLE", null);

    //BORRADO DE LA TABLA T_CTACTE_BPARTNER_TOTAL
    log.info("Borrado de la tabla temporal T_CTACTE_BPARTNER_TOTAL");
    DB.executeUpdate("Delete from T_CTACTE_BPARTNER_TOTAL", null);
    
    ResultSet rs = null;
    try {
        rs = getComprobantes();
    } catch (Exception ex) {
        log.info("Bloqueo de doIt para Reporte de ctas ctes");
        String sqlUpdateBloqueo ="update T_Bloqueos set bloqueado = 'N' where AD_Process_ID = 1000235";
        DB.executeUpdate(sqlUpdateBloqueo,null);         
        Logger.getLogger(GenerateReporteCtaCteMonedaOriginal31.class.getName()).log(Level.SEVERE, null, ex);
    }

    ResultSet rsProvSinMov = null;
    try {
        rsProvSinMov = getProvSinMov();
    } catch (Exception ex) {
        log.info("Bloqueo de doIt para Reporte de ctas ctes");
        String sqlUpdateBloqueo ="update T_Bloqueos set bloqueado = 'N' where AD_Process_ID = 1000235";
        DB.executeUpdate(sqlUpdateBloqueo,null);         
        Logger.getLogger(GenerateReporteCtaCteMonedaOriginal31.class.getName()).log(Level.SEVERE, null, ex);
    }

    String dateString = "";
    if (fromDate!=null && toDate!=null)
    	dateString = "			Desde: " + fromDate.toString().substring(0, 10) + " - Hasta: " + toDate.toString().substring(0, 10);
    else
    	if (fromDate!=null)
    		dateString = "			Desde: " + fromDate.toString().substring(0, 10);
    	else
	    	if (toDate!=null)
	    		dateString = "			Hasta: " + toDate.toString().substring(0, 10);


    int CC_BPARTNER_DETALLE_ID=1000000;
    int CC_BPARTNER_ID=1000000;

    /*
     *  Anexo para registrar la totalización del informe
     *  Zynnia 18/04/2012
     *
     */

    BigDecimal sumDebe = Env.ZERO;
    BigDecimal sumHaber = Env.ZERO;
    BigDecimal sumTotal = Env.ZERO;
    PreparedStatement ps = null;
    boolean param = true;
    
    int cli = 0;
    int org = 0;
    
    CC_BPARTNER_ID++;
    try {
        if (rs.next())
        {
                while (!rs.isAfterLast()){

                        int cliente = rs.getInt(1);
                        int organizacion = rs.getInt(2);
                        cli = rs.getInt(1);
                        org = rs.getInt(2);
                        int C_Currency_ID = rs.getInt(7);
                        int C_BPartner_ID=rs.getInt(8);

                        int BPartner_Location_ID = rs.getInt(15);
                        String BPartner_Location_Name = rs.getString(16);

                        //  Ingreso en la tabla cabecera
                        sql = "INSERT INTO T_CTACTE_BPARTNER VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?)";
                        ps = DB.prepareStatement(sql, null);
                    ps.setInt(1, cliente);						//	CLIENTE
                    ps.setInt(2, organizacion);					//	ORGANIZACION
                    if (fromBPartner!=null)						// 	C_BPARTNER_ID
                            ps.setInt(3, Integer.parseInt(fromBPartner));
                    else
                            if (toBPartner!=null)
                                    ps.setInt(3, Integer.parseInt(toBPartner));
                            else
                                    ps.setInt(3, C_BPartner_ID);		//	C_BPARTNER_ID
                    ps.setInt(4, p_instance);					//	INSTANCE
                    ps.setInt(5, C_Currency_ID);				//	C_CURRENCY_ID
                    ps.setString(6, rs.getString(10) + dateString);	//	NAME
                    if (fromBPartner!=null)						// 	VALUE
                    {
                            MBPartner partner = new MBPartner(getCtx(),Integer.parseInt(fromBPartner),null);
                                ps.setString(7, partner.getValue());
                    }
                    else
                            if (toBPartner!=null)
                            {
                                    MBPartner partner = new MBPartner(getCtx(),Integer.parseInt(toBPartner),null);
                                        ps.setString(7, partner.getValue());
                            }
                            else
                                    ps.setString(7, rs.getString(9));	// 	VALUE
                    if (isSOTrx.booleanValue() == true)
                            ps.setString(8, "Y");
                    else
                            ps.setString(8, "N");					// 	ISSOTRX
                    ps.setDate(9, rs.getDate(3));				// 	DATE

                    ps.setInt(10, BPartner_Location_ID);		//	BP_LOCATION_ID
                    ps.setString(11, BPartner_Location_Name);	// 	BP_LOCATION_NAME
                    ps.setInt(12, CC_BPARTNER_ID);				//	T_CTACTE_BPARTNER_ID
                    ps.setString(13, rs.getString(9));			// 	CODE_BP
                    if (param)
                    {
                        ps.setString(14, parametros);
                        param = false;
                    }
                    else
                        ps.setString(14, null);

                    ps.executeUpdate();
                    ps.close();

                    //   Calculo el saldo inicial para la cuenta
                    BigDecimal saldoInicial  = Env.ZERO;
                    try {
                        saldoInicial= obtenerSaldoInicial(C_BPartner_ID);
                    } catch (Exception ex) {
                        log.info("Bloqueo de doIt para Reporte de ctas ctes");
                        String sqlUpdateBloqueo ="update T_Bloqueos set bloqueado = 'N' where AD_Process_ID = 1000235";
                        DB.executeUpdate(sqlUpdateBloqueo,null);                         
                        Logger.getLogger(GenerateReporteCtaCteMonedaOriginal31.class.getName()).log(Level.SEVERE, null, ex);
                    }

                        // 	Ingreso en la tabla el detalle del saldo inicial

                    // Anexo para que tome la tasa de conversión y el monto (,?,?)
                    // Jose Fantasia
                    // Zynnia

                    sql = "INSERT INTO T_CTACTE_BPARTNER_DETALLE VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    ps = DB.prepareStatement(sql, null);
                    ps.setInt(1, cliente);						//	CLIENTE
                    ps.setInt(2, organizacion);					//	ORGANIZACION
                    //Luego del 'Y'
                    ps.setInt(3, rs.getInt(8));					//	C_BPARTNER_ID
                    ps.setInt(4, p_instance);					//	INSTANCE
                    ps.setDate(5, null);						//	FECHA
                    ps.setString(6, "Saldo de Inicio");			//	CONCEPTO
                    // 	Si saldo es positivo va en el DEBE
                    if (saldoInicial.compareTo(BigDecimal.ZERO)>0){
                        ps.setBigDecimal(7, saldoInicial);		//	DEBE
                        ps.setBigDecimal(12, null);	//	HABER
                        sumDebe = sumDebe.add(saldoInicial);
                    }
                    //	Saldo negativo al HABER
                    else{
                        /*
                         *  15/03/2013 Maria Jesus Martin
                         *  Cambio para que en el haber aparezca el saldo inicial en positivo
                         *  mas haya de que sea con signo negativo. En la exposicion, tanto en el debe
                         *  como en el haber no puede haber montos en negativo.
                         * 
                         */
                        ps.setBigDecimal(7, null);	//	DEBE
                        ps.setBigDecimal(12, saldoInicial.negate());		//	HABER
                        sumHaber = sumHaber.add(saldoInicial);
                    }
                    ps.setBigDecimal(8, saldoInicial);			//	SALDO
                    ps.setInt(9, CC_BPARTNER_DETALLE_ID);		//	CC_BPARTNER_DETALLE_ID

                    ps.setString(10, null);						//	NUMERO
                    ps.setString(11, null);						//	CONDICION

                    ps.setString(13, null);						//	CODIGO_MONEDA
                    ps.setInt(14, CC_BPARTNER_ID);				//	T_CS_BPARTNER_ID
                    ps.setString(15, null);						//	TASA
                    ps.setString(16, null);						//	MONEDA ORIGEN


                    ps.executeUpdate();
                    ps.close();

                    //Incremento CC_BARTNER_DETALLE_ID
                    CC_BPARTNER_DETALLE_ID++;

                    BigDecimal saldo = saldoInicial;
                    BigDecimal conversion1;
                    BigDecimal conversion2;


                    while (!(rs.isAfterLast() || (C_BPartner_ID!=rs.getInt(8)) || (BPartner_Location_ID!=rs.getInt(15))))
                        {

                        // Anexo para que tome la tasa de conversión y el monto (,?,?)
                        // Jose Fantasia
                        // Zynnia

                        // 	Ingreso en la tabla el detalle los totales
                        sql = "INSERT INTO T_CTACTE_BPARTNER_DETALLE VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        ps = DB.prepareStatement(sql, null);
                        ps.setInt(1, cliente);						//	CLIENTE
                        ps.setInt(2, organizacion);					//	ORGANIZACION
                        //Luego del 'Y'
                        ps.setInt(3, C_BPartner_ID);				//	C_BPARTNER_ID
                        ps.setInt(4, p_instance);					//	INSTANCE
                        ps.setDate(5,rs.getDate(3));				//	FECHA
                        ps.setString(6, rs.getString(4));			//	CONCEPTO

                        if(!conSaldo.containsKey(C_BPartner_ID))
                            conSaldo.put(C_BPartner_ID, C_BPartner_ID);

    //                    System.out.println("moneda " + rs.getString(14) + " tasa " + rs.getBigDecimal(13));
    //                    System.out.println("11 " + rs.getString(11) + " 12 " + rs.getBigDecimal(12));



                        if (rs.getBigDecimal(11)!=null)
                        {

                            // En este punto se hacía un tratamiento para convertir de moneda de origen a $
                            // que queda sin efecto el 07/03/2012

                            //if(rs.getString(14).equals("ARS")){
                                ps.setBigDecimal(7, rs.getBigDecimal(11));	//	DEBE
                                ps.setBigDecimal(12, rs.getBigDecimal(12));	//	HABER
                                saldo = saldo.add(rs.getBigDecimal(11));
                                sumDebe = sumDebe.add(rs.getBigDecimal(11));
                        }
                        else
                        {
                                // En este punto se hacía un tratamiento para convertir de moneda de origen a $
                            // que queda sin efecto el 07/03/2012
                            //if(rs.getString(14).equals("ARS")){

                                ps.setBigDecimal(7, rs.getBigDecimal(11));	//	DEBE
                                ps.setBigDecimal(12, rs.getBigDecimal(12));	//	HABER
                                saldo = saldo.subtract(rs.getBigDecimal(12));
                                sumHaber = sumHaber.add(rs.getBigDecimal(12).negate());
                        }
                        ps.setBigDecimal(8, saldo);					//	SALDO
                        ps.setInt(9, CC_BPARTNER_DETALLE_ID);		//	CC_BPARTNER_DETALLE_ID
                        ps.setString(10, rs.getString(5));			//	NUMERO
                        ps.setString(11, rs.getString(6));			//	CONDICION

                        ps.setString(13, rs.getString(14));			//	CODIGO_MONEDA
                        ps.setInt(14, CC_BPARTNER_ID);				//	T_CS_BPARTNER_ID

                        ps.setBigDecimal(15, rs.getBigDecimal(13));          //	TASA

                        if (rs.getBigDecimal(11)!=null)
                        {
                                ps.setBigDecimal(16, rs.getBigDecimal(11));         //	MONTO ORIGEN
                        }
                        else
                        {
                                ps.setBigDecimal(16, rs.getBigDecimal(12));         //	MONTO ORIGEN
                        }
                        ps.executeUpdate();
                        ps.close();

                        //Incremento CC_BARTNER_DETALLE_ID
                        CC_BPARTNER_DETALLE_ID++;

                        rs.next();
                        }

                    //	Incremento CC_BPARTNER_ID
                    CC_BPARTNER_ID++;

                }

                while (rsProvSinMov.next()){

                    int cliente = rsProvSinMov.getInt(1);
                    int organizacion = rsProvSinMov.getInt(2);
                    int C_BPartner_ID = rsProvSinMov.getInt(3);
                    String C_BPartner_name = rsProvSinMov.getString(4);
                    String C_BPartner_value = rsProvSinMov.getString(5);

                    if(!conSaldo.containsKey(C_BPartner_ID)) {
                        //  Ingreso en la tabla cabecera
                        sql = "INSERT INTO T_CTACTE_BPARTNER VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?)";
                        ps = DB.prepareStatement(sql, null);
                        ps.setInt(1, cliente);				//	CLIENTE
                        ps.setInt(2, organizacion);			//	ORGANIZACION
                        ps.setInt(3, C_BPartner_ID);                    //	C_BPARTNER_ID
                        ps.setInt(4, p_instance);                       //	INSTANCE
                        ps.setInt(5, 0);				//	C_CURRENCY_ID
                        ps.setString(6, C_BPartner_name + dateString);	//	NAME
                        ps.setString(7, C_BPartner_value);		// 	VALUE
                        ps.setString(8, "N");				// 	ISSOTRX
                        ps.setDate(9, null);                            // 	DATE
                        ps.setInt(10, 0);                               //	BP_LOCATION_ID
                        ps.setString(11, "");                           // 	BP_LOCATION_NAME
                        ps.setInt(12, CC_BPARTNER_ID);			//	T_CTACTE_BPARTNER_ID
                        ps.setString(13, C_BPartner_value);		// 	CODE_BP
                        if (param)
                        {
                            ps.setString(14, parametros);
                            param = false;
                        }
                        else
                            ps.setString(14, null);
                        ps.executeUpdate();
                        ps.close();

                        //   Calculo el saldo inicial para la cuenta
                        BigDecimal saldoInicial = Env.ZERO;
                        try {
                            saldoInicial=obtenerSaldoInicial(C_BPartner_ID);
                        } catch (Exception ex) {
                            log.info("Bloqueo de doIt para Reporte de ctas ctes");
                            String sqlUpdateBloqueo ="update T_Bloqueos set bloqueado = 'N' where AD_Process_ID = 1000235";
                            DB.executeUpdate(sqlUpdateBloqueo,null);                             
                            Logger.getLogger(GenerateReporteCtaCteMonedaOriginal31.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        // 	Ingreso en la tabla el detalle del saldo inicial

                        sql = "INSERT INTO T_CTACTE_BPARTNER_DETALLE VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        ps = DB.prepareStatement(sql, null);
                        ps.setInt(1, cliente);						//	CLIENTE
                        ps.setInt(2, organizacion);					//	ORGANIZACION
                        //Luego del 'Y'
                        ps.setInt(3, C_BPartner_ID);					//	C_BPARTNER_ID
                        ps.setInt(4, p_instance);					//	INSTANCE
                        ps.setDate(5, null);						//	FECHA
                        ps.setString(6, "Saldo de Inicio");			//	CONCEPTO
                        // 	Si saldo es positivo va en el DEBE
                        if (saldoInicial.compareTo(BigDecimal.ZERO)>0){
                            ps.setBigDecimal(7, saldoInicial);		//	DEBE
                            ps.setBigDecimal(12, null);	//	HABER
                            sumDebe = sumDebe.add(saldoInicial);
                        }
                        //	Saldo negativo al HABER
                        else{
                            ps.setBigDecimal(7, null);	//	DEBE
                            ps.setBigDecimal(12, saldoInicial.negate());		//	HABER
                            sumHaber = sumHaber.add(saldoInicial);
                        }
                        ps.setBigDecimal(8, saldoInicial);			//	SALDO
                        ps.setInt(9, CC_BPARTNER_DETALLE_ID);		//	CC_BPARTNER_DETALLE_ID

                        ps.setString(10, null);						//	NUMERO
                        ps.setString(11, null);						//	CONDICION

                        ps.setString(13, null);						//	CODIGO_MONEDA
                        ps.setInt(14, CC_BPARTNER_ID);				//	T_CS_BPARTNER_ID
                        ps.setString(15, null);						//	TASA
                        ps.setString(16, null);						//	MONEDA ORIGEN


                        ps.executeUpdate();
                        ps.close();

                        //Incremento CC_BARTNER_DETALLE_ID
                        CC_BPARTNER_DETALLE_ID++;

                        //	Incremento CC_BPARTNER_ID
                        CC_BPARTNER_ID++;
                    }
                }
        }
            
        rs.close();
        
    } catch (SQLException ex) {
        log.info("Bloqueo de doIt para Reporte de ctas ctes");
        String sqlUpdateBloqueo ="update T_Bloqueos set bloqueado = 'N' where AD_Process_ID = 1000235";
        DB.executeUpdate(sqlUpdateBloqueo,null);         
        Logger.getLogger(GenerateReporteCtaCteMonedaOriginal31.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    try {
        while (rsProvSinMov.next()){

            int cliente = rsProvSinMov.getInt(1);
            int organizacion = rsProvSinMov.getInt(2);
            int C_BPartner_ID = rsProvSinMov.getInt(3);
            String C_BPartner_name = rsProvSinMov.getString(4);
            String C_BPartner_value = rsProvSinMov.getString(5);

            if(!conSaldo.containsKey(C_BPartner_ID)) {
                //  Ingreso en la tabla cabecera
                sql = "INSERT INTO T_CTACTE_BPARTNER VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?)";
                ps = DB.prepareStatement(sql, null);
                ps.setInt(1, cliente);				//	CLIENTE
                ps.setInt(2, organizacion);			//	ORGANIZACION
                ps.setInt(3, C_BPartner_ID);                    //	C_BPARTNER_ID
                ps.setInt(4, p_instance);                       //	INSTANCE
                ps.setInt(5, 0);				//	C_CURRENCY_ID
                ps.setString(6, C_BPartner_name + dateString);	//	NAME
                ps.setString(7, C_BPartner_value);		// 	VALUE
                ps.setString(8, "N");				// 	ISSOTRX
                ps.setDate(9, null);                            // 	DATE
                ps.setInt(10, 0);                               //	BP_LOCATION_ID
                ps.setString(11, "");                           // 	BP_LOCATION_NAME
                ps.setInt(12, CC_BPARTNER_ID);			//	T_CTACTE_BPARTNER_ID
                ps.setString(13, C_BPartner_value);		// 	CODE_BP

                if (param)
                {
                    ps.setString(14, parametros);
                    param = false;
                }
                else
                    ps.setString(14, null);

                ps.executeUpdate();
                ps.close();

                //   Calculo el saldo inicial para la cuenta
                BigDecimal saldoInicial = Env.ZERO;
                try {
                    saldoInicial = obtenerSaldoInicial(C_BPartner_ID);
                } catch (Exception ex) {
                    log.info("Bloqueo de doIt para Reporte de ctas ctes");
                    String sqlUpdateBloqueo ="update T_Bloqueos set bloqueado = 'N' where AD_Process_ID = 1000235";
                    DB.executeUpdate(sqlUpdateBloqueo,null);                     
                    Logger.getLogger(GenerateReporteCtaCteMonedaOriginal31.class.getName()).log(Level.SEVERE, null, ex);
                }

                // 	Ingreso en la tabla el detalle del saldo inicial

                sql = "INSERT INTO T_CTACTE_BPARTNER_DETALLE VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                ps = DB.prepareStatement(sql, null);
                ps.setInt(1, cliente);						//	CLIENTE
                ps.setInt(2, organizacion);					//	ORGANIZACION
                //Luego del 'Y'
                ps.setInt(3, C_BPartner_ID);					//	C_BPARTNER_ID
                ps.setInt(4, p_instance);					//	INSTANCE
                ps.setDate(5, null);						//	FECHA
                ps.setString(6, "Saldo de Inicio");			//	CONCEPTO
                // 	Si saldo es positivo va en el DEBE
                if (saldoInicial.compareTo(BigDecimal.ZERO)>0){
                    ps.setBigDecimal(7, saldoInicial);		//	DEBE
                    ps.setBigDecimal(12, null);	//	HABER
                    sumDebe = sumDebe.add(saldoInicial);
                }
                //	Saldo negativo al HABER
                else{
                    ps.setBigDecimal(7, null);	//	DEBE
                    ps.setBigDecimal(12, saldoInicial.negate());		//	HABER
                    sumHaber = sumHaber.add(saldoInicial);
                }
                ps.setBigDecimal(8, saldoInicial);			//	SALDO
                ps.setInt(9, CC_BPARTNER_DETALLE_ID);		//	CC_BPARTNER_DETALLE_ID

                ps.setString(10, null);						//	NUMERO
                ps.setString(11, null);						//	CC_BPARTNER_DETALLE_ID

                ps.setString(13, null);						//	CODIGO_MONEDA
                ps.setInt(14, CC_BPARTNER_ID);				//	CONDICION
                ps.setString(15, null);						//	TASA
                ps.setString(16, null);						//	MONEDA ORIGEN


                ps.executeUpdate();
                ps.close();

                //Incremento CC_BARTNER_DETALLE_ID
                CC_BPARTNER_DETALLE_ID++;

                //	Incremento CC_BPARTNER_ID
                CC_BPARTNER_ID++;
            }

        }
        rsProvSinMov.close();
        
    } catch (SQLException ex) {
        log.info("Bloqueo de doIt para Reporte de ctas ctes");
        String sqlUpdateBloqueo ="update T_Bloqueos set bloqueado = 'N' where AD_Process_ID = 1000235";
        DB.executeUpdate(sqlUpdateBloqueo,null);         
        Logger.getLogger(GenerateReporteCtaCteMonedaOriginal31.class.getName()).log(Level.SEVERE, null, ex);
    }

    sumTotal = sumDebe.add(sumHaber);

    //  Ingreso en la tabla de total
    sql = "INSERT INTO T_CTACTE_BPARTNER_TOTAL VALUES(" + cli + "," + org + ",'Y',"
            + p_instance + "," + sumDebe + "," + sumHaber + "," + sumTotal + ")";
    ps = DB.prepareStatement(sql, null);
    try {
        ps.executeUpdate();
        ps.close();
    } catch (SQLException ex) {
        log.info("Bloqueo de doIt para Reporte de ctas ctes");
        String sqlUpdateBloqueo ="update T_Bloqueos set bloqueado = 'N' where AD_Process_ID = 1000235";
        DB.executeUpdate(sqlUpdateBloqueo,null); 
        Logger.getLogger(GenerateReporteCtaCteMonedaOriginal31.class.getName()).log(Level.SEVERE, null, ex);
    }
    



    UtilProcess.initViewer("Cuenta Corriente en Moneda Original",0,getProcessInfo());
    UtilProcess.initViewer("Cuenta Corriente en Moneda Original Totalizada",0,getProcessInfo());

    log.info("Bloqueo de doIt para Reporte de ctas ctes");
    String sqlUpdateBloqueo ="update T_Bloqueos set bloqueado = 'N' where AD_Process_ID = 1000235";
    DB.executeUpdate(sqlUpdateBloqueo,null);  

    return "";


}

    /** BISion - 08/05/2009 - Daniel Gini
     * Metodo que retorna los comprobantes por socio de negocio, filtrando por los parametros ingresados
     * ordenados por fecha del comprobante.
     * @return ResultSet
     * @throws Exception
     */
	private ResultSet getComprobantes()		throws Exception{
        //CONSULTA PARA OBTENER LOS DATOS
		String sql =
		"SELECT * " +
		"FROM " +
		"(	SELECT  i.AD_CLient_ID as CLIENTE," +
		"	 		i.AD_ORG_ID AS ORG," +
		"			i.DATEACCT as FECHA," +
		"	        d.NAME as TIPO," +
		"			i.DocumentNo as NUMERO," +
		"			t.name as CONDICION," +
		"	        i.C_CURRENCY_ID as MONEDA," +
		"	        b.C_BPartner_ID as BP_ID," +
		"	        b.VALUE as CLAVE," +
		"	        b.name as NOMBRE," +
		"			CASE d.docbasetype" +
		"				WHEN 'ARI' THEN i.grandtotal" +
		"                               WHEN 'API' THEN null" +
		" 				WHEN 'ARF' THEN i.grandtotal" +
		"				WHEN 'ARC' THEN null" +
		"				WHEN 'APC' THEN i.grandtotal" +
		"				ELSE 0" +
		"                       END as DEBE," +
		"			CASE d.docbasetype" +
		"				WHEN 'ARI' THEN null" +
		"				WHEN 'API' THEN i.grandtotal" +
		"				WHEN 'ARF' THEN null" +
		"				WHEN 'ARC' THEN i.grandtotal" +
		"				WHEN 'APC' THEN null" +
		"				ELSE 0" +
		"                       END as HABER," +
		"           CASE" +
		"				WHEN i.COTIZACION is null THEN 1" +
		"				ELSE i.COTIZACION" +
		"           END as COTIZACION," +
		"           y.ISO_CODE as CODMONEDA," +
		"           CASE" +
		"				WHEN bpl.C_BPartner_Location_ID is null THEN 0" +
		"				ELSE bpl.C_BPartner_Location_ID" +
		"			END as C_BPartner_Location_ID," +
		"			CASE" +
		"				WHEN bpl.C_BPartner_Location_ID is null THEN 'SIN ASIGNAR'" +
		"				ELSE TO_CHAR(bpl.name)" +
		"			END as C_BPartner_Location_Name " + 

		"	FROM C_Invoice i" +
		"		INNER JOIN C_BPARTNER b ON(i.C_BPARTNER_ID = b.C_BPARTNER_ID)" +
		"		INNER JOIN C_DocType d ON(i.C_DocType_ID = d.C_DocType_ID)" +
		"		INNER JOIN C_PaymentTerm t ON(i.C_PaymentTerm_ID = t.C_PaymentTerm_ID)" +
		"   	INNER JOIN C_Currency y ON(y.C_Currency_ID = i.C_Currency_ID)" +
                        /*
                         *  Modificacion 28/06/2012  Maria Jesus Martin
                         *  Sacamos el Join con Location ya que C_Invoice tiene el C_BPartner_Location_ID
                         *  y podemos hacer directamente el Join con C_BPartner_Location
                         *
                         *  Modificacion : LEFT JOIN C_BPartner_Location bpl ON (bpl.C_LOCATION_ID = l.C_Location_ID and bpl.c_bpartner_id = b.c_bpartner_id)
                         *
                         */
	//	"		LEFT JOIN C_Location l ON (i.BILL_LOCATION_ID = l.C_Location_ID)" +

		/* 05-05-2011 Camarzana Mariano
		 * Se agrego a la sentencia
		 * LEFT JOIN C_BPartner_Location bpl ON (bpl.C_LOCATION_ID = l.C_Location_ID
		 * la restriccion bpl.c_bpartner_id = b.c_bpartner_id, debido a que en el caso
		 * en que una misma direccion este asignada a dos socios
		 *
		 */

		"		LEFT JOIN C_BPartner_Location bpl ON (bpl.C_Location_ID = i.Bill_Location_ID and bpl.c_bpartner_id = b.c_bpartner_id)" +

			getSqlWhere("i.ISSOTRX","i.DATEACCT","i.C_CURRENCY_ID","i.DocStatus","i.DateAcct","t.netDays") +

		"	UNION ALL " +
		"	(	SELECT p.AD_CLient_ID as CLIENTE," +
		"              p.AD_ORG_ID AS ORG," +
		"              p.DATEACCT as FECHA," +
		"              d.NAME as TIPO," +
		"              p.DOCUMENTNO as NUMERO," +
		"              null as CONDICION," +
		"              p.C_CURRENCY_ID as MONEDA," +
		"              b.C_BPartner_ID as BP_ID," +
		"              b.VALUE as CLAVE," +
		"              b.name as NOMBRE," +
		"              CASE d.docbasetype" +
		"	              WHEN 'ARR' THEN null" +
		"                 WHEN 'APP' THEN -al.AMOUNT" +
		"                 ELSE 0" +
		"              END as DEBE," +
		"              CASE d.docbasetype" +
		"	              WHEN 'ARR' THEN al.AMOUNT" +
		"   	          WHEN 'APP' THEN null" +
		"                 ELSE 0" +
		"              END as HABER," +
		"              CASE" +
		"                 WHEN p.COTIZACION is null THEN 1" +
		"                 ELSE p.COTIZACION" +
		"              END as COTIZACION," +
		"	           y.ISO_CODE as CODMONEDA," +
		"              CASE" +
		"	              WHEN bpl.C_BPartner_Location_ID is null THEN 0" +
		"                 ELSE bpl.C_BPartner_Location_ID" +
		"              END as C_BPartner_Location_ID," +
		"              CASE" +
		"	              WHEN bpl.C_BPartner_Location_ID is null THEN 'SIN ASIGNAR'" +
		"                 ELSE TO_CHAR(bpl.name)" +
		"              END as C_BPartner_Location_Name" +

                        /*
                         *  Modificacion 28/06/2012 Maria Jesus Martin
                         *
                         *  Como la localizacion que se toma es la del pago, no debemos hacer el Join por localizacion con
                         *  la factura, ya que pueden tener localizaciones diferentes. Solo tomamos la localizacion del pago.
                         *  Es por esto que hacemos el Join con C_BPartner_Location a partir del C_Payment, que antes lo tenia
                         *  con el C_Location.
                         *
                         *
                         */

		"		FROM C_Payment p" +

		"	       INNER JOIN C_DocType d ON(p.C_DocType_ID = d.C_DocType_ID)" +
		" 		   INNER JOIN C_BPARTNER b ON(p.C_BPARTNER_ID = b.C_BPARTNER_ID)" +
		"          INNER JOIN C_Currency y ON(y.C_Currency_ID = p.C_Currency_ID)" +
		"          INNER JOIN C_AllocationLine al ON (al.C_Payment_ID = p.C_Payment_ID)" +
		"          INNER JOIN C_Allocationhdr ah ON (ah.C_AllocationHdr_ID = al.C_AllocationHdr_ID)" +
	//	"          LEFT JOIN C_Invoice i ON (al.C_Invoice_ID = i.C_Invoice_ID)" +
	//	"          LEFT JOIN C_Location l ON (i.BILL_LOCATION_ID = l.C_Location_ID)" +

		/* 05-05-2011 Camarzana Mariano
		 * Se agrego a la sentencia
		 * LEFT JOIN C_BPartner_Location bpl ON (bpl.C_LOCATION_ID = l.C_Location_ID
		 * la restriccion bpl.c_bpartner_id = b.c_bpartner_id, debido a que en el caso
		 * en que una misma direccion este asignada a dos socios
		 *
		 */

		"		   LEFT JOIN C_BPartner_Location bpl ON (bpl.C_BPartner_Location_ID = p.C_BPartner_Location_ID and bpl.c_bpartner_id = b.c_bpartner_id)" +

	            getSqlWhere("p.ISRECEIPT","p.DATEACCT","p.C_CURRENCY_ID","p.DocStatus","p.dateAcct","0") +

                        /*
                         *  Zynnia 09/03/2012
                         *  Modificado de UNION a UNION ALL para que tome los registros duplicados en la
                         *  consulta ya que por las características de la misma puede darse que dos
                         *  facturas tengan mismo importe en un mismo pago y se duplica exactamente el
                         *  registro.
                         *
                         */



	    "		UNION ALL " +
	    "		(	SELECT t.AD_CLient_ID as CLIENTE," +
	    "                  t.AD_ORG_ID AS ORG," +
	    "                  t.DATEACCT as FECHA," +
	    "                  d.NAME as TIPO," +
	    "                  t.DOCUMENTNO as NUMERO," +
	    "                  null as CONDICION," +
	    "                  t.C_CURRENCY_ID as MONEDA," +
	    "                  b.C_BPartner_ID as BP_ID," +
	    "                  b.VALUE as CLAVE," +
	    "                  b.name as NOMBRE," +
	    "                  CASE d.docbasetype" +
	    "	                  WHEN 'ARR' THEN null" +
	    "                     WHEN 'APP' THEN PAYMENTAVAILABLE(t.C_Payment_ID) * -1" +
	    "                     ELSE 0" +
	    "                  END as DEBE," +
	    "                  CASE d.docbasetype" +
	    "                     WHEN 'ARR' THEN PAYMENTAVAILABLE(t.C_Payment_ID)" +
	    "                     WHEN 'APP' THEN null" +
	    "	                  ELSE 0" +
	    "                  END as HABER," +
	    "                  CASE" +
	    "                     WHEN t.COTIZACION is null THEN 1" +
	    "                     ELSE t.COTIZACION" +
	    "                  END as COTIZACION," +
	    "                  y.ISO_CODE as CODMONEDA," +
                        /*
                         *  Modificacion 28/06/2012 Maria Jesus Martin
                         *
                         *  Cambiamos lo que retorna en C_BPartner, ya que siempre va a tener una localizacion
                         *  el pago.
                         *
                         *
                         */
                        "  CASE " +
		"	              WHEN bpl.C_BPartner_Location_ID is null THEN 0" +
		"                 ELSE bpl.C_BPartner_Location_ID" +
		"              END as C_BPartner_Location_ID," +
		"              CASE" +
		"	              WHEN bpl.C_BPartner_Location_ID is null THEN 'SIN ASIGNAR'" +
		"                 ELSE TO_CHAR(bpl.name)" +
		"              END as C_BPartner_Location_Name" +
//	    "                  0 as C_BPartner_Location_ID," +
//	    "                  'SIN ASIGNAR' as C_BPartner_Location_Name" +

	    "           FROM C_Payment t" +
	    "	           INNER JOIN C_DocType d ON(t.C_DocType_ID = d.C_DocType_ID)" +
	    "			   INNER JOIN C_BPARTNER b ON(t.C_BPARTNER_ID = b.C_BPARTNER_ID)" +
	    "              INNER JOIN C_Currency y ON(y.C_Currency_ID = t.C_Currency_ID)" +
                        /*
                         *  Modificacion 28/06/2012 Maria Jesus Martin
                         *
                         *  Agregamos el C_BPartner_Location, ya que no importa si no tiene una factura asignadada.
                         *  Un pago siempre tiene una direccion.
                         *
                         *
                         */

            "              LEFT JOIN C_BPartner_Location bpl ON (bpl.C_BPartner_Location_ID = t.C_BPartner_Location_ID and bpl.c_bpartner_id = b.c_bpartner_id)" +

	                getSqlWhere("t.ISRECEIPT","t.DATEACCT","t.C_CURRENCY_ID","t.DocStatus","t.dateAcct","0") +
	    "         	AND t.isAllocated = 'N' AND PAYMENTAVAILABLE(t.C_Payment_ID) <> 0" +

	    "       )" +
	    "   )" +
	    ") ORDER BY CLAVE,C_BPartner_Location_ID,FECHA";


        PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
        int paramIndex = 1;

        if (isSOTrx.booleanValue() == true)
        	pstmt.setString(paramIndex, "Y");
        else
        	pstmt.setString(paramIndex, "N");
        paramIndex++;

        if (fromDate!=null){
            pstmt.setTimestamp(paramIndex, fromDate);
            paramIndex++;
        }
        if (toDate!=null){
            pstmt.setTimestamp(paramIndex, toDate);
            paramIndex++;
        }
        if (fromBPartner!=null){
        	MBPartner partner = new MBPartner(getCtx(),Integer.parseInt(fromBPartner),null);
    		pstmt.setString(paramIndex, partner.getValue());
            paramIndex++;
        }
        if (toBPartner!=null){
        	MBPartner partner = new MBPartner(getCtx(),Integer.parseInt(toBPartner),null);
        	pstmt.setString(paramIndex, partner.getValue());
            paramIndex++;
        }
        if (fromVenc!=null){
                pstmt.setTimestamp(paramIndex, fromVenc);
                paramIndex++;
        }
        
        if (toVenc!=null){
                pstmt.setTimestamp(paramIndex, toVenc);
                paramIndex++;
        }

        pstmt.setInt(paramIndex, C_Currency_ID.intValue());
        paramIndex++;

        if (isSOTrx.booleanValue() == true)
        	pstmt.setString(paramIndex, "Y");
        else
        	pstmt.setString(paramIndex, "N");
        paramIndex++;

        if (fromDate!=null){
            pstmt.setTimestamp(paramIndex, fromDate);
            paramIndex++;
        }
        if (toDate!=null){
            pstmt.setTimestamp(paramIndex, toDate);
            paramIndex++;
        }
        if (fromBPartner!=null){
        	MBPartner partner = new MBPartner(getCtx(),Integer.parseInt(fromBPartner),null);
    		pstmt.setString(paramIndex, partner.getValue());
            paramIndex++;
        }
        
        if (toBPartner!=null){
        	MBPartner partner = new MBPartner(getCtx(),Integer.parseInt(toBPartner),null);
        	pstmt.setString(paramIndex, partner.getValue());
            paramIndex++;
        }
        
        if (fromVenc!=null){
                pstmt.setTimestamp(paramIndex, fromVenc);
                paramIndex++;
        }
        
        if (toVenc!=null){
                pstmt.setTimestamp(paramIndex, toVenc);
                paramIndex++;
        }

        pstmt.setInt(paramIndex, C_Currency_ID.intValue());
        paramIndex++;

        if (isSOTrx.booleanValue() == true)
        	pstmt.setString(paramIndex, "Y");
        else
        	pstmt.setString(paramIndex, "N");
        paramIndex++;

        if (fromDate!=null){
            pstmt.setTimestamp(paramIndex, fromDate);
            paramIndex++;
        }
        if (toDate!=null){
            pstmt.setTimestamp(paramIndex, toDate);
            paramIndex++;
        }
        if (fromBPartner!=null){
        	MBPartner partner = new MBPartner(getCtx(),Integer.parseInt(fromBPartner),null);
    		pstmt.setString(paramIndex, partner.getValue());
            paramIndex++;
        }
        if (toBPartner!=null){
        	MBPartner partner = new MBPartner(getCtx(),Integer.parseInt(toBPartner),null);
        	pstmt.setString(paramIndex, partner.getValue());
            paramIndex++;
        }
        
        if (fromVenc!=null){
                pstmt.setTimestamp(paramIndex, fromVenc);
                paramIndex++;
        }
        
        if (toVenc!=null){
                pstmt.setTimestamp(paramIndex, toVenc);
                paramIndex++;
        }

        pstmt.setInt(paramIndex, C_Currency_ID.intValue());

        return pstmt.executeQuery();
    }

    /** Zynnia - 03/04/2012 - José Fantasia
     * Metodo que retorna los socios de negocio que no registran movimientos para informar su saldo de inicio.
     * @return ResultSet
     * @throws Exception
     */
	private ResultSet getProvSinMov()		throws Exception{


            String sql = "SELECT  AD_Client_ID," +
            "	 	AD_ORG_ID," +
            "	        C_BPartner_ID," +
            "	        VALUE," +
            "	        name " +
            "	FROM C_BPARTNER";

            if (toBPartner!=null && fromBPartner!=null)
                    sql+= " WHERE value >= ? AND value <= ?";
            else if (toBPartner!=null && fromBPartner==null)
                    sql+= " WHERE value <= ?";
            else if (toBPartner==null && fromBPartner!=null)
                    sql+= " WHERE value >= ?";

            PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());

            int paramIndex = 1;

            if (fromBPartner!=null){
                    MBPartner partner = new MBPartner(getCtx(),Integer.parseInt(fromBPartner),null);
                    pstmt.setString(paramIndex, partner.getValue());
                    paramIndex++;
            }
            if (toBPartner!=null){
                    MBPartner partner = new MBPartner(getCtx(),Integer.parseInt(toBPartner),null);
                    pstmt.setString(paramIndex, partner.getValue());
                    paramIndex++;
            }

        return pstmt.executeQuery();
    }

	/** BISion - 08/05/2009 - Daniel Gini
     * Metodo que retorna la clausula "where" dependiendo de los parametros ingresados.
     * @return String
     */
        
     private BigDecimal obtenerSaldoInicial(int BPartner_ID) throws Exception
    {
        //Obtengo el monto
    	if (fromDate==null)
    		return (new BigDecimal(0));

        String sql = "";
        PreparedStatement pstmt = null;
		
        sql =
                "SELECT sum(MONTO) " +
                "FROM ( " +
                " SELECT   " +
                " CASE d.docbasetype " +
                " WHEN 'ARI' THEN i.grandtotal " +
                " WHEN 'API' THEN -i.grandtotal " +
                " WHEN 'ARF' THEN i.grandtotal " +
                " WHEN 'ARC' THEN -i.grandtotal " +
                " WHEN 'APC' THEN i.grandtotal " +
                " ELSE 0 " +
                " END as MONTO, " +
                " i.documentno as DOCUMENTNRO " +
                " FROM C_Invoice i " +
                " INNER JOIN C_BPARTNER b ON(i.C_BPARTNER_ID = b.C_BPARTNER_ID) " +
                " INNER JOIN C_DocType d ON(i.C_DocType_ID = d.C_DocType_ID) " +
                " INNER JOIN C_PaymentTerm t ON(i.C_PaymentTerm_ID = t.C_PaymentTerm_ID)" +
                " INNER JOIN C_Currency y ON(y.C_Currency_ID = i.C_Currency_ID)" +
                " LEFT JOIN C_BPartner_Location bpl ON (bpl.C_Location_ID = i.Bill_Location_ID and bpl.c_bpartner_id = b.c_bpartner_id)" +
                " WHERE i.DocStatus IN ('CO','CL') AND i.ISSOTRX=? AND b.C_BPARTNER_ID = ? AND i.C_CURRENCY_ID = ? AND i.DATEACCT>to_date('30/11/2011 00:00:00', 'dd,mm,yyyy  HH24:MI:SS') AND i.DATEACCT <? ";
                                /*
                 *  11/09/2012 Zynnia.
                 *  Agregamos el parametro de fecha de vencimiento, el cual es calculado a partir
                 *  del termino de pago del socio de negocio. Se calcula la fecha de contabilidad + 
                 *  los dias del termino de pago.
                 * 
                 */
        
                int ind = 0;
                
                if (toVenc!=null && fromVenc!=null){
			sql += " AND adddays(i.DATEACCT,t.netDays) " + "BETWEEN ? AND ?";
                        ind = 2;
                } else if (toVenc!=null && fromVenc==null){
			sql += " AND adddays(i.DATEACCT,t.netDays) " + "<= ?";
                        ind = 1;
                } else if (toVenc==null && fromVenc!=null) {
			sql += " AND adddays(i.DATEACCT,t.netDays) " + ">= ?";
                        ind = 1;
                }
                
                sql += " UNION ALL " +
                "( " +
                " SELECT " +
                " 	CASE d.docbasetype " +
                "		WHEN 'ARR' THEN al.AMOUNT " +
                "		WHEN 'APP' THEN -al.AMOUNT " +
                "		ELSE 0 " +
                "	END as MONTO, " +
                "	p.DOCUMENTNO as DOCUMENTNRO " +
                " FROM C_Payment p" +
                " INNER JOIN C_BPARTNER b ON(p.C_BPARTNER_ID = b.C_BPARTNER_ID) " +
                " INNER JOIN C_DocType d ON(p.C_DocType_ID = d.C_DocType_ID) " +
                " INNER JOIN C_AllocationLine al ON (al.C_Payment_ID = p.C_Payment_ID)" +
                " INNER JOIN C_Allocationhdr ah ON (ah.C_AllocationHdr_ID = al.C_AllocationHdr_ID)" +
                " LEFT JOIN C_BPartner_Location bpl ON (bpl.C_BPartner_Location_ID = p.C_BPartner_Location_ID and bpl.c_bpartner_id = b.c_bpartner_id)" +
                " WHERE p.DocStatus IN ('CO','CL') AND p.ISRECEIPT=? AND b.C_BPARTNER_ID = ? AND p.C_CURRENCY_ID = ? AND p.DATEACCT>to_date('30/11/2011 00:00:00', 'dd,mm,yyyy  HH24:MI:SS') AND p.DATEACCT<? " +
                ") "+
                "UNION ALL (SELECT CASE d.docbasetype " +
                "WHEN 'ARR' THEN PAYMENTAVAILABLE(p.C_Payment_ID) " +
                "WHEN 'APP' THEN -PAYMENTAVAILABLE(p.C_Payment_ID) " +
                "ELSE 0 	END as MONTO," 	+
                "p.DOCUMENTNO as DOCUMENTNRO  "+
                "FROM C_Payment p INNER JOIN C_BPARTNER b ON(p.C_BPARTNER_ID = b.C_BPARTNER_ID) "+
                "INNER JOIN C_DocType d ON(p.C_DocType_ID = d.C_DocType_ID) "+
                "LEFT JOIN C_BPartner_Location bpl ON (bpl.C_BPartner_Location_ID = p.C_BPartner_Location_ID and bpl.c_bpartner_id = b.c_bpartner_id) " +                
                "WHERE p.DocStatus IN ('CO','CL') AND p.ISRECEIPT=? AND b.C_BPARTNER_ID = ? AND p.C_CURRENCY_ID = ? "+
                "AND p.DATEACCT>to_date('30/11/2011 00:00:00', 'dd,mm,yyyy  HH24:MI:SS') "+
                "AND p.DATEACCT<? "+
                "AND p.isAllocated = 'N'))";

	        pstmt = DB.prepareStatement(sql, get_TrxName());

	        if (isSOTrx.booleanValue() == true)
	        {	pstmt.setString(1, "Y");
	        	pstmt.setString(5+ind, "Y");
                        pstmt.setString(9+ind, "Y");
	        }
	        else
	        {	pstmt.setString(1, "N");
	        	pstmt.setString(5+ind, "N");
                        pstmt.setString(9+ind, "N");
	        }

	    	pstmt.setInt(2, BPartner_ID);
	        pstmt.setInt(3, C_Currency_ID.intValue());
	        pstmt.setTimestamp(4, fromDate);

	        pstmt.setInt(6+ind, BPartner_ID);
	        pstmt.setInt(7+ind, C_Currency_ID.intValue());
	        pstmt.setTimestamp(8+ind, fromDate);
                
                pstmt.setInt(10+ind, BPartner_ID);
	        pstmt.setInt(11+ind, C_Currency_ID.intValue());
	        pstmt.setTimestamp(12+ind, fromDate);

		ResultSet rs = pstmt.executeQuery();

		rs.next();

		if (rs.getBigDecimal(1)!=null)
		{
                    BigDecimal bd = rs.getBigDecimal(1);
		    pstmt.close();
		    rs.close();
                    String sqlSaldoInicial = "select SALDO from C_SALDOINIPROV sal"
                        + " inner join C_BPARTNER bp on (sal.C_BPARTNER_VALUE = bp.value)"
                        + " where bp.C_BPartner_ID = " + BPartner_ID + " and C_CURRENCY_ID = " + C_Currency_ID;

                    PreparedStatement pstmtSaldoInicial = DB.prepareStatement(sqlSaldoInicial, get_TrxName());

                    ResultSet rsSaldoInicial = pstmtSaldoInicial.executeQuery();

                    if(!rsSaldoInicial.next()){
                        pstmtSaldoInicial.close();
                        rsSaldoInicial.close();
                        return bd;
                    }

                    if (rsSaldoInicial.getBigDecimal(1)!= null) {
                        String sqlExisteSaldo = "SELECT count(*) "
                               + " FROM T_CTACTE_BPARTNER_DETALLE"
                               + " WHERE c_bpartner_id = "+ BPartner_ID
                               + " AND NAME LIKE '%Saldo de Inicio%'";
                        PreparedStatement pstmtExisteSaldo = DB.prepareStatement(sqlExisteSaldo, get_TrxName());
                        ResultSet rsExisteSaldo = pstmtExisteSaldo.executeQuery();
                    
                        BigDecimal bdSaldoInicial = rsSaldoInicial.getBigDecimal(1);
                        rsExisteSaldo.next();
                        if (!rsExisteSaldo.getBigDecimal(1).equals(Env.ZERO)){
                           // int cant = rsExisteSaldo.getInt(1);
                           // if (cant > 0)
                           //     bdSaldoInicial = Env.ZERO;
                            bd = Env.ZERO;
                        }
                        else
                        {
                            bd = bd.add(bdSaldoInicial);
                        }
                        rsExisteSaldo.close();
                        pstmtExisteSaldo.close();
                    }
                    pstmtSaldoInicial.close();
                    rsSaldoInicial.close();
                    return bd;

                } 
                else 
                {
                        pstmt.close();
                        rs.close();


                        String sqlSaldoInicial = "select SALDO from C_SALDOINIPROV sal"
                            + " inner join C_BPARTNER bp on (sal.C_BPARTNER_VALUE = bp.value)"
                            + " where bp.C_BPartner_ID = " + BPartner_ID + " and C_CURRENCY_ID = " + C_Currency_ID;

                        PreparedStatement pstmtSaldoInicial = DB.prepareStatement(sqlSaldoInicial, get_TrxName());

                        ResultSet rsSaldoInicial = pstmtSaldoInicial.executeQuery();


                        if(!rsSaldoInicial.next()){
                           return Env.ZERO;
                        }
                        BigDecimal bdSaldoInicial = Env.ZERO;

                        if (rsSaldoInicial.getBigDecimal(1)!= null) {
                             bdSaldoInicial = rsSaldoInicial.getBigDecimal(1);
    //                    System.out.println("Saldo inicial: " + bdSaldoInicial);

                        } else {
    //                    System.out.println("No hay saldo inicial cargado");

                        }

                        if (rsSaldoInicial.getBigDecimal(1)!= null) {
                            String sqlExisteSaldo = "SELECT count(*) "
                                   + " FROM T_CTACTE_BPARTNER_DETALLE"
                                   + " WHERE c_bpartner_id = "+ BPartner_ID
                                   + " AND NAME LIKE '%Saldo de Inicio%'";
                            PreparedStatement pstmtExisteSaldo = DB.prepareStatement(sqlExisteSaldo, get_TrxName());
                            ResultSet rsExisteSaldo = pstmtExisteSaldo.executeQuery();
                            bdSaldoInicial = rsSaldoInicial.getBigDecimal(1);
                            rsExisteSaldo.next();
                            if (!rsExisteSaldo.getBigDecimal(1).equals(Env.ZERO)){
                                //int cant = rsExisteSaldo.getInt(1);
                               // if (cant > 0)
                                    bdSaldoInicial = Env.ZERO;
                            }
                            rsExisteSaldo.close();
                            pstmtExisteSaldo.close();
                        }
                        pstmtSaldoInicial.close();
                        rsSaldoInicial.close();
                        return bdSaldoInicial;
                    }
    }
     
    private String getSqlWhere(String param1, String param2, String param3, String param4, String param5, String param6){
    	String BP = "VALUE";
    	String sqlWhere = " WHERE "+param1+" = ?";

		if (toDate!=null && fromDate!=null)
			sqlWhere+= " AND "+param2+" BETWEEN ? AND ?";
		else if (toDate!=null && fromDate==null)
			sqlWhere+= " AND "+param2+" <= ?";
		else if (toDate==null && fromDate!=null)
			sqlWhere+= " AND "+param2+" >= ?";

		if (toBPartner!=null && fromBPartner!=null)
			sqlWhere+= " AND b." + BP + "  >= ? AND b." + BP + " <= ?";
		else if (toBPartner!=null && fromBPartner==null)
			sqlWhere+= " AND b." + BP + " <= ?";
		else if (toBPartner==null && fromBPartner!=null)
			sqlWhere+= " AND b." + BP + " >= ?";
                
                /*
                 *  11/09/2012 Zynnia.
                 *  Agregamos el parametro de fecha de vencimiento, el cual es calculado a partir
                 *  del termino de pago del socio de negocio. Se calcula la fecha de contabilidad + 
                 *  los dias del termino de pago.
                 * 
                 */
                if (toVenc!=null && fromVenc!=null)
			sqlWhere+= " AND adddays("+param5+","+param6+")"+ "BETWEEN ? AND ?";
		else if (toVenc!=null && fromVenc==null)
			sqlWhere+= " AND adddays("+param5+","+param6+")"+ "<= ?";
		else if (toVenc==null && fromVenc!=null)
			sqlWhere+= " AND adddays("+param5+","+param6+")"+ ">= ?";

		// Eliminado para que tome todos los comprobantes y no filtre por monedas
                // Jose Fantasia
                // Zynnia
                // Vuelta atrás en el requerimiento se habilitó nuevamente 07/03/2012

                sqlWhere+= " AND "+param3+" = ?";

		sqlWhere+= " AND "+param4+" IN ('CO','CL')";

		return sqlWhere;
    }


}