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

                    sql = "INSERT INTO T_CTACTE_BPARTNER_DETALLE VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    ps = DB.prepareStatement(sql, null);
                    ps.setInt(1, cliente);						//	CLIENTE
                    ps.setInt(2, organizacion);					//	ORGANIZACION
                    //Luego del 'Y'
                    ps.setInt(3, rs.getInt(8));					//	C_BPARTNER_ID
                    ps.setInt(4, p_instance);					//	INSTANCE
                    ps.setDate(5, null);						//	FECHA TRANSACCION
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
                    ps.setString(16, null);
                    ps.setDate(17, null); // Fecha ACCT	

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
                        sql = "INSERT INTO T_CTACTE_BPARTNER_DETALLE VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        ps = DB.prepareStatement(sql, null);
                        ps.setInt(1, cliente);						//	CLIENTE
                        ps.setInt(2, organizacion);					//	ORGANIZACION
                        //Luego del 'Y'
                        ps.setInt(3, C_BPartner_ID);				//	C_BPARTNER_ID
                        ps.setInt(4, p_instance);					//	INSTANCE
                        ps.setDate(5,rs.getDate(17));				//	FECHA TRAMSACCION
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
                        
                        ps.setDate(17,rs.getDate(3)); //FECHA ACCT
                        
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
                        ps.setDate(9, null);                            // 	DATE TRX
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

                        sql = "INSERT INTO T_CTACTE_BPARTNER_DETALLE VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                        ps.setString(16, null);
                        ps.setDate(17, null);//	FECHA ACCT


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

                sql = "INSERT INTO T_CTACTE_BPARTNER_DETALLE VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                ps.setDate(17, null);	// DATE ACCT

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
		"			END as C_BPartner_Location_Name, " + 
                                "           i.DATEINVOICED as fecha_cbte" +

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
		"              END as C_BPartner_Location_Name," +
                                "               p.DATETRX as fecha_cbte " +

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
		"              END as C_BPartner_Location_Name, " +
//	    "                  0 as C_BPartner_Location_ID," +
//	    "                  'SIN ASIGNAR' as C_BPartner_Location_Name" +
                    "                    t.DATETRX as fecha_cbte" +

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

        System.out.println(sql);
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
                //String BP = "C_BPartner_ID";
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
                
                                /** Modificacion auxiliar para sacar solo un rango de socios **/
                                //sqlWhere+= " AND b." + BP + "  in (1002186,6003128,1006924,6006507,1006732,6007118,1006916,6007183,6006018,1002131,6004525,1006759,6004546,6003780,6003462,6007313,1006771,6007273,1008443,6003614,1002481,1008384,1003180,1008192,6000694,1002356,5000609,6004119,6006923,6002014,1002417,6004324,6001062,1002187,5000272,1002810,1002811,6006982,6000839,6001069,1002134,6004171,6007056,1003737,6004896,1008444,1003784,6005576,5000185,6007206,1006793,5000938,1006675,6000596,6004773,1003344,6003342,6004284,6002267,1004381,1006932,5000178,1006749,5000391,1002694,6006705,6004870,1002036,1002424,1008219,5000233,1002037,6002907,1002135,6007184,6007237,1002425,1004286,1006388,6002023,1003067,5000075,1002662,6000515,6006508,6003548,6003892,6005586,1002760,1002485,1002605,6000143,1006769,1008228,1006733,1008325,1004432,6003559,6001745,1008247,1002039,1008381,6001094,1004418,1002762,6003674,1002041,5000066,1004313,1006656,1008345,1002763,1002429,5000238,1002430,6005909,1003166,6002126,6004727,1002212,1002044,1002764,6003081,1002431,6003787,1006655,1008423,6004523,6003898,6005275,6001295,6007122,6006383,1006392,6001550,1006856,1006356,1004350,1002766,1006850,1002767,1006865,6007207,6006154,6003344,1006335,6000310,1003677,6005001,1002145,6004079,6003675,1002048,3006154,1004052,6007117,6005935,1003112,6005019,1004380,6004770,5000187,1004309,6003154,6001545,1002700,1006654,6003481,1006406,6003891,6003763,5000236,6006618,1002213,6001541,1004358,6001613,6006224,6007182,1003041,1002054,5000268,6006024,1003110,1008439,1002769,1004489,6006706,1002607,1003462,6002615,6002720,1002444,3005239,1002770,6005276,1002650,1003347,6002667,1008348,1002057,6002025,1002188,1002058,1004500,6003350,6007208,5000133,1002215,6002982,1006895,1002222,1002150,1003799,1004424,1004461,6001850,1006336,6006828,6005416,1002205,6003482,6004885,1006382,1006687,6003274,1002206,6004492,1006665,1006658,6007123,6003484,1002495,6006499,5000244,1002703,6000296,6004897,1002704,1002446,6006855,6000777,1003790,6002983,6004520,1004267,1004277,6003071,6000132,5000108,1003168,6004183,6006385,1002636,1002448,1006635,1002497,1002498,6001617,5000253,1008451,1004528,6001365,6004609,6006264,1002160,1002774,6007062,6007240,6007150,6004704,5000295,1002452,5000251,6002906,1003678,6005925,1006660,6006454,5000217,6007259,1002223,6007209,1008425,6005824,6007236,1002628,1002063,1006887,1002776,1002506,1002678,6000410,6000142,6000311,1002453,1002668,1008258,5000177,1002163,6006687,1002454,1003087,1008135,6002621,1003088,6004762,1008397,6004745,1006334,1002456,1002229,1006709,6007107,6002013,5000220,5000317,6007395,1004460,6003485,1003743,1006736,1003511,1003505,6003640,6004744,1006634,6004899,6003619,6003673,6006778,1002457,6001846,1006893,6007241,5000376,1008351,6007154,6006922,6004290,1006785,6000577,1008373,5000208,1004541,6005791,6005552,6002367,6001368,1004272,1008203,1003132,6003347,6004768,6006384,6001607,1006390,6003096,1002460,6004833,6003282,6006238,6001148,6005810,1003170,6001539,1006347,6001677,1002170,1008150,1006387,1004525,1006876,1008374,1002173,6005244,6004981,6003849,1004297,6006686,1006836,6005496,1002682,5000132,1002465,1002783,1002068,6002996,5000172,5000074,6007385,6007157,1002069,1008265,6002985,1008338,5000143,6000511,1003119,1002711,1002786,6000520,6003881,6000237,6002499,6003200,1006372,6004150,6005948,1002070,6001678,6005557,1002209,6004061,5000216,1006846,6004938,6001769,6001948,6000316,5000264,6006509,6002884,1008138,1008307,1006773,6006019,6005922,1002513,6006337,1006333,6005920,6003068,6002620,1002793,6005751,1002794,6001363,1003760,1002178,6000324,1003465,1002179,6004771,6003967,1002415,1006783,6001089,6006404,1002471,1008287,1008452,6003609,1002181,6003097,6002494,6001457,1008379,1002075,6007238,5000285,1002516,1002615,6006685,1003423,1002518,6002614,1006358,6004847,1004459,1002519,1008235,1003636,6005353,1002183,6007272,1002520,6001867,1008433,6006979,6004644,1008182,6003276,6004446,6001110,6001042,1002800,1002472,1002635,6005168,1006389,1002475,1002078,6000821,6001364,6006609,6004009,1006754,1006819,6004686,1002654,1004520,1003763,1004421,1002658,1002511,5000266,1004552,6002182,6007103,6004114,6005278,6004743)";
                             
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