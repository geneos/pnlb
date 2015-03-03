/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;
import org.compiere.process.*;
import java.math.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import org.compiere.util.*;
import org.eevolution.tools.UtilProcess;

/**
 *  Clase que ingresa las filas a la tabla T_CC_BARTNER_DETALLE
 * @author santiago
 */
public class GenerateReporteCCSocioNegocio extends SvrProcess{

//PARAMETROS DE ENTRADA
int p_instance;
private Timestamp fromDate = null;
private Timestamp toDate = null;
private String fromBPartner = null;
private String toBPartner = null;
private BigDecimal C_Currency_ID = new BigDecimal(0);
private Boolean isSOTrx = false;

protected void prepare(){
    ProcessInfoParameter[] para = getParameter();
    for (int i = 0; i < para.length; i++)
	{
        String name = para[i].getParameterName();
        if(name.equals("DateTrx")){
            fromDate=(Timestamp)para[i].getParameter();
            toDate=(Timestamp)para[i].getParameter_To();
        }
        else
        {	if (name.equals("C_Currency_ID"))
    			C_Currency_ID = ((BigDecimal)para[i].getParameter());
    		else
    			if (name.equals("isSOTrx"))
    			{
    				if (((String)para[i].getParameter()).equals("Y"))
    					isSOTrx = true;
    				else
    					isSOTrx = false;
    			}
    			else
            		if(name.equals("VALUE")){
            			fromBPartner=(String)para[i].getParameter();
            			toBPartner=(String)para[i].getParameter_To();
    				}
        }
    }
    
    p_instance = getAD_PInstance_ID();
}

protected String doIt() throws Exception {
    //BORRADO DE LA TABLA T_LIBROMAYORDETALLE
    String sql;
    log.info("Comienzo del proceso de Cuenta Corriente");
    
    log.info("Borrado de la tabla temporal T_CC_BPARTNER");
    DB.executeUpdate("Delete from T_CC_BPARTNER", null);
    
    log.info("Borrado de la tabla temporal T_CC_BPARTNER_DETALLE");
    DB.executeUpdate("Delete from T_CC_BPARTNER_DETALLE", null);
    
    ResultSet rs = getComprobantes();
    
    int CC_BPARTNER_DETALLE_ID=1000000;
    
    if (rs.next())
    {
	    while (!rs.isAfterLast()){
	    	
	    	int cliente = rs.getInt(1);
	    	int organizacion = rs.getInt(2);
	    	int C_BPartner_ID=rs.getInt(8);
	    	
	    	//  Ingreso en la tabla cabecera
	    	sql = "INSERT INTO T_CC_BPARTNER VALUES(?,?,'Y',?,?,?,?,?,?,?,?)";
	    	PreparedStatement ps = DB.prepareStatement(sql, null);
	        ps.setInt(1, cliente);						//	CLIENTE
	        ps.setInt(2, organizacion);					//	ORGANIZACION
	        ps.setInt(3, C_BPartner_ID);				//	C_BPARTNER_ID
	        ps.setInt(4, p_instance);					//	INSTANCE
	        ps.setInt(5, rs.getInt(7));					//	C_CURRENCY_ID
	        ps.setString(6, rs.getString(10));			//	NAME
	        if (fromBPartner!=null)						// 	VALUE
	        	ps.setString(7, fromBPartner);			
	        else
	        	if (toBPartner!=null)
		        	ps.setString(7, toBPartner);		
	        	else
        			ps.setString(7, rs.getString(9));	// 	VALUE
	        if (isSOTrx.booleanValue() == true)
	        	ps.setString(8, "Y");
	        else
	        	ps.setString(8, "N");					// 	ISSOTRX
	        ps.setDate(9, rs.getDate(3));				// 	DATE
	        ps.setString(10, rs.getString(9));			// 	CODE_BP
	        
	        ps.executeUpdate();
	        ps.close();
	
	        //   Calculo el saldo inicial para la cuenta
	    	BigDecimal saldoInicial = obtenerSaldoInicial(C_BPartner_ID);
	    	
	    	// 	Ingreso en la tabla el detalle del saldo inicial
	        sql = "INSERT INTO T_CC_BPARTNER_DETALLE VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?)";
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
	        }
	        //	Saldo negativo al HABER
	        else{
	            ps.setBigDecimal(7, null);	//	DEBE
	            ps.setBigDecimal(12, saldoInicial);		//	HABER
	        }        
	        ps.setBigDecimal(8, saldoInicial);			//	SALDO
	        ps.setInt(9, CC_BPARTNER_DETALLE_ID);		//	CC_BPARTNER_DETALLE_ID
	        
	        ps.setString(10, null);						//	NUMERO
	        ps.setString(11, null);						//	CONDICION
	        
	        ps.executeUpdate();
	        ps.close();
	
	        //Incremento CC_BARTNER_DETALLE_ID
	        CC_BPARTNER_DETALLE_ID++;
	        
	        BigDecimal saldo = saldoInicial;
	        
	        while (!(rs.isAfterLast() || (C_BPartner_ID!=rs.getInt(8))))
	    	{
	    		// 	Ingreso en la tabla el detalle los totales
	            sql = "INSERT INTO T_CC_BPARTNER_DETALLE VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?)";
	            ps = DB.prepareStatement(sql, null);
	            ps.setInt(1, cliente);						//	CLIENTE
	            ps.setInt(2, organizacion);					//	ORGANIZACION
	            //Luego del 'Y'
	            ps.setInt(3, C_BPartner_ID);				//	C_BPARTNER_ID
	            ps.setInt(4, p_instance);					//	INSTANCE
	            ps.setDate(5,rs.getDate(3));				//	FECHA
	            ps.setString(6, rs.getString(4));			//	CONCEPTO
	            ps.setBigDecimal(7, rs.getBigDecimal(11));	//	DEBE
	            //	Actualizo el saldo acumulado            
	            if (rs.getBigDecimal(11)!=null)
	            	saldo = saldo.add(rs.getBigDecimal(11));
	            else
	            	saldo = saldo.subtract(rs.getBigDecimal(12));
	            ps.setBigDecimal(8, saldo);					//	SALDO
	            ps.setInt(9, CC_BPARTNER_DETALLE_ID);		//	CC_BPARTNER_DETALLE_ID
	            ps.setString(10, rs.getString(5));			//	NUMERO
	            ps.setString(11, rs.getString(6));			//	CONDICION
	            ps.setBigDecimal(12, rs.getBigDecimal(12));	//	HABER
	            
	            ps.executeUpdate();
	            ps.close();
	
	            //Incremento CC_BARTNER_DETALLE_ID
	            CC_BPARTNER_DETALLE_ID++;
	            
	            rs.next();
	    	}
	    	
	        /*
	    	// 	Ingreso en la tabla el subtotal
	    	sql = "INSERT INTO T_CC_BPARTNER_DETALLE VALUES(?,?,'Y',?,?,?,?,?,?,?)";
	        ps = DB.prepareStatement(sql, null);
	        ps.setInt(1, cliente);						//	CLIENTE
	        ps.setInt(2, organizacion);					//	ORGANIZACION
	        //Luego del 'Y'
	        ps.setInt(3, C_BPartner_ID);					//	C_BPARTNER_ID
	        ps.setInt(4, p_instance);					//	INSTANCE
	        ps.setDate(5,null);							//	FECHA
	        ps.setString(6, "Total Cuenta");			//	CONCEPTO
	        ps.setBigDecimal(7, null);					//	MONTO
	        ps.setBigDecimal(8, saldo);					//	SALDO
	        ps.setInt(9, CC_BPARTNER_DETALLE_ID);		//	CC_BPARTNER_DETALLE_ID
	        
	        ps.executeUpdate();
	        ps.close();
	
	        //Incremento CC_BARTNER_DETALLE_ID
	        CC_BPARTNER_DETALLE_ID++;*/
	
	    }
      
	    UtilProcess.initViewer("Cuenta Corriente BP",getAD_PInstance_ID(),getProcessInfo());
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
	private ResultSet getComprobantes()		throws Exception{
        //CONSULTA PARA OBTENER LOS DATOS
		String sql =
		"SELECT * " +
        "FROM ( " +
			"SELECT  i.AD_CLient_ID as CLIENTE, " +
			" 		 i.AD_ORG_ID AS ORG, " +
			"		 i.DATEINVOICED as FECHA, " +
			"        d.NAME as TIPO, " +
			"		 i.DocumentNo as NUMERO, " +
            "		 t.name as CONDICION, " +
			"        i.C_CURRENCY_ID as MONEDA, " +
			"        b.C_BPartner_ID as BP_ID, " +
			"        b.VALUE as CLAVE, " +
			"        b.name as NOMBRE, " +
			" CASE d.docbasetype " +
          	"	 WHEN 'ARI' THEN i.grandtotal " +
          	"	 WHEN 'API' THEN null " +
          	"	 WHEN 'ARF' THEN i.grandtotal " +
          	"	 WHEN 'ARC' THEN null " +
          	"	 WHEN 'APC' THEN i.grandtotal " +
          	"	 ELSE 0 " +
            " END as DEBE, " +
          	" CASE d.docbasetype " + 
          	"	 WHEN 'ARI' THEN null " +
            "	 WHEN 'API' THEN i.grandtotal " + 
            "	 WHEN 'ARF' THEN null " +
            "	 WHEN 'ARC' THEN i.grandtotal " +
            "	 WHEN 'APC' THEN null " +
            "	 ELSE 0 " +
            " END as HABER " +
			" FROM C_Invoice i " +
			" INNER JOIN C_BPARTNER b ON(i.C_BPARTNER_ID = b.C_BPARTNER_ID) " +
			" INNER JOIN C_DocType d ON(i.C_DocType_ID = d.C_DocType_ID) " +
			" INNER JOIN C_PaymentTerm t ON(i.C_PaymentTerm_ID = t.C_PaymentTerm_ID) " +
			getSqlWhere("i.ISSOTRX","i.DATEINVOICED","i.C_CURRENCY_ID","i.DocStatus") +
			"UNION " +
			"(SELECT C_Payment.AD_CLient_ID as CLIENTE, " +
			"		 C_Payment.AD_ORG_ID AS ORG, " +
			"		 C_Payment.DATETRX as FECHA, " +
			"        d.NAME as TIPO, " +
			"		 C_Payment.DOCUMENTNO as NUMERO, " + 
            "		 null as CONDICION, " +
			"        C_Payment.C_CURRENCY_ID as MONEDA, " +
			"        b.C_BPartner_ID as BP_ID, " +
			"        b.VALUE as CLAVE, " +
			"        b.name as NOMBRE, " +
			" CASE d.docbasetype " +
            " WHEN 'ARR' THEN null " +
            " WHEN 'APP' THEN C_Payment.PAYAMT " +
            " ELSE 0 " +
            " END as DEBE, " +
          	" CASE d.docbasetype " +
            " WHEN 'ARR' THEN C_Payment.PAYAMT " +
            " WHEN 'APP' THEN null " +
            " ELSE 0 " +
            " END as HABER " +
			" FROM C_Payment " +
			" INNER JOIN C_BPARTNER b ON(C_Payment.C_BPARTNER_ID = b.C_BPARTNER_ID) " +
			" INNER JOIN C_DocType d ON(C_Payment.C_DocType_ID = d.C_DocType_ID) " +
			getSqlWhere("C_Payment.ISRECEIPT","C_Payment.DATETRX","C_Payment.C_CURRENCY_ID","C_Payment.DocStatus") +
			") " +
		") ORDER BY CLAVE,FECHA";
		
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
            pstmt.setString(paramIndex, fromBPartner);
            paramIndex++;
        }
        if (toBPartner!=null){
            pstmt.setString(paramIndex, toBPartner);
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
            pstmt.setString(paramIndex, fromBPartner);
            paramIndex++;
        }
        if (toBPartner!=null){
            pstmt.setString(paramIndex, toBPartner);
            paramIndex++;
        }
        
    	pstmt.setInt(paramIndex, C_Currency_ID.intValue());
    	
        return pstmt.executeQuery();
    }
	
	/** BISion - 08/05/2009 - Daniel Gini
     * Metodo que retorna la clausula "where" dependiendo de los parametros ingresados.
     * @return String
     */
    private String getSqlWhere(String param1, String param2, String param3, String param4){
    	String sqlWhere = " WHERE "+param1+" = ?";
      
		if (toDate!=null && fromDate!=null)
			sqlWhere+= " AND "+param2+" BETWEEN ? AND ?";
		else if (toDate!=null && fromDate==null)
			sqlWhere+= " AND "+param2+" <= ?";
		else if (toDate==null && fromDate!=null)
			sqlWhere+= " AND "+param2+" >= ?";
	
		if (toBPartner!=null && fromBPartner!=null)
			sqlWhere+= " AND b.VALUE >= ? AND b.VALUE <= ?";
		else if (toBPartner!=null && fromBPartner==null)
			sqlWhere+= " AND b.VALUE <= ?";
		else if (toBPartner==null && fromBPartner!=null)
			sqlWhere+= " AND b.VALUE >= ?";
		
		sqlWhere+= " AND "+param3+" = ?";
		
		sqlWhere+= " AND "+param4+" IN ('CO','CL')";
    	
		return sqlWhere;
    }

    /** BISion - 08/05/2009 - Daniel Gini
     * Retorna el saldo inicial para un Socio de Negocio, es decir el monto acumulado hasta la "Fecha Desde", ingresada por parametro.
     * @param BPartner_ID
     * @throws Exception
     * @return BigDecimal
     */
    private BigDecimal obtenerSaldoInicial(int BPartner_ID) throws Exception
    {
        //Obtengo el monto
    	if (fromDate==null)
    		return (new BigDecimal(0));
    	
        String sql =
        	
        "SELECT sum(MONTO) " +
        "FROM ( " +
			" SELECT   " +
        	"	CASE d.docbasetype " +
        	"		WHEN 'ARI' THEN i.grandtotal " +
        	"		WHEN 'API' THEN -i.grandtotal " +
        	"		WHEN 'ARF' THEN i.grandtotal " +
        	"		WHEN 'ARC' THEN -i.grandtotal " +
        	"		WHEN 'APC' THEN i.grandtotal " +
        	"		ELSE 0 " +
        	"	END as MONTO, " +
        	"	i.documentno as DOCUMENTNRO " +
			" FROM C_Invoice i " +
			" INNER JOIN C_BPARTNER b ON(i.C_BPARTNER_ID = b.C_BPARTNER_ID) " +
			" INNER JOIN C_DocType d ON(i.C_DocType_ID = d.C_DocType_ID) " + 
			" WHERE i.DocStatus IN ('CO','CL') AND i.ISSOTRX=? AND b.C_BPARTNER_ID = ? AND i.C_CURRENCY_ID = ? AND i.DATEINVOICED<? " +
			"UNION " +
			"( " +
			" SELECT " +
            " 	CASE d.docbasetype " +
            "		WHEN 'ARR' THEN -C_Payment.PAYAMT " +
            "		WHEN 'APP' THEN C_Payment.PAYAMT " +
            "		ELSE 0 " +
            "	END as MONTO, " +
            "	C_Payment.DOCUMENTNO as DOCUMENTNRO " +
			" FROM C_Payment " +
			" INNER JOIN C_BPARTNER b ON(C_Payment.C_BPARTNER_ID = b.C_BPARTNER_ID) " +
			" INNER JOIN C_DocType d ON(C_Payment.C_DocType_ID = d.C_DocType_ID) " +
			" WHERE C_Payment.DocStatus IN ('CO','CL') AND C_Payment.ISRECEIPT=? AND b.C_BPARTNER_ID = ? AND C_Payment.C_CURRENCY_ID = ? AND C_Payment.DATETRX<? " +
			") )";
        
        PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
        
        if (isSOTrx.booleanValue() == true)
        {	pstmt.setString(1, "Y");
        	pstmt.setString(5, "Y");
        }
        else
        {	pstmt.setString(1, "N");
        	pstmt.setString(5, "N");
        }
        
    	pstmt.setInt(2, BPartner_ID);
        pstmt.setInt(3, C_Currency_ID.intValue());
        pstmt.setTimestamp(4, fromDate);
        pstmt.setInt(6, BPartner_ID);
        pstmt.setInt(7, C_Currency_ID.intValue());
        pstmt.setTimestamp(8, fromDate);
        
        ResultSet rs = pstmt.executeQuery();
        
        rs.next();
        
        if (rs.getBigDecimal(1)!=null)
        {
        	BigDecimal bd = rs.getBigDecimal(1);
        	
        	pstmt.close();
        	rs.close();
        	
        	return bd;
        }
        	
        return (new BigDecimal(0));
    
    }
    
}
