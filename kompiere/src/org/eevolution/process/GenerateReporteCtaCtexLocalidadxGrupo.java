/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;
import org.compiere.model.MBPGroup;
import org.compiere.model.MBPartner;
import org.compiere.process.*;
import java.math.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import org.compiere.util.*;
import org.eevolution.tools.UtilProcess;

/**
 *  Clase que ingresa las filas a la tabla T_CC_BARTNER_DETALLE
 * @author Daniel - BISion
 */
public class GenerateReporteCtaCtexLocalidadxGrupo extends SvrProcess{

//PARAMETROS DE ENTRADA
int p_instance;
private Timestamp fromDate = null;
private Timestamp toDate = null;
private String fromGroupPartner = null;
private String toGroupPartner = null;
private BigDecimal C_Currency_ID = new BigDecimal(0);
private Boolean isSOTrx = false;

protected void prepare(){
    ProcessInfoParameter[] para = getParameter();
    boolean flagCliente = false;
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
    				if(name.equals("C_BP_Group_ID"))
            			if (flagCliente == false){
                			fromGroupPartner=para[i].getParameter().toString();
    	                    flagCliente = true;
            			}
    	                else
    	                	toGroupPartner=para[i].getParameter().toString();
        }
    }
    
    p_instance = 0;
}

protected String doIt() throws Exception {
    //BORRADO DE LA TABLA T_CTACTE_BPARTNER
    String sql;
    log.info("Comienzo del proceso de Cuenta Corriente");
    
    log.info("Borrado de la tabla temporal T_CTACTE_BPARTNER");
    DB.executeUpdate("Delete from T_CTACTE_BPARTNER", null);
    
    //BORRADO DE LA TABLA T_CTACTE_BPARTNER_DETALLE
    log.info("Borrado de la tabla temporal T_CTACTE_BPARTNER_DETALLE");
    DB.executeUpdate("Delete from T_CTACTE_BPARTNER_DETALLE", null);
    
    ResultSet rs = getComprobantes();
    
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
    
    if (rs.next())
    {
	    while (!rs.isAfterLast()){
	    	
	    	int cliente = rs.getInt(1);
	    	int organizacion = rs.getInt(2);
	    	int C_Currency_ID = rs.getInt(7);
	    	int C_BPartner_ID=rs.getInt(8);
	    	int C_BGroup_ID=rs.getInt(17);

	    	
	    	int BPartner_Location_ID = rs.getInt(15);
    		String BPartner_Location_Name = rs.getString(16);

    		//  Ingreso en la tabla cabecera
	    	sql = "INSERT INTO T_CTACTE_BPARTNER VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?)";
	    	PreparedStatement ps = DB.prepareStatement(sql, null);
	        ps.setInt(1, cliente);						//	CLIENTE
	        ps.setInt(2, organizacion);					//	ORGANIZACION
	        
	        if (fromGroupPartner!=null)						// 	C_BPARTNER_ID
	        	ps.setInt(3, Integer.parseInt(fromGroupPartner));			
	        else
	        	if (toGroupPartner!=null)
	        		ps.setInt(3, Integer.parseInt(toGroupPartner));			
	        	else
	        		ps.setInt(3, C_BPartner_ID);		//	C_BPARTNER_ID
	        ps.setInt(4, p_instance);					//	INSTANCE
	        ps.setInt(5, C_Currency_ID);				//	C_CURRENCY_ID
	        ps.setString(6, rs.getString(18) +  " - " + rs.getString(10) + dateString);	//	NAME
	        if (fromGroupPartner!=null)						// 	VALUE
	        {	
	        	MBPGroup partner = new MBPGroup(getCtx(),Integer.parseInt(fromGroupPartner),null);
	    		ps.setString(7, partner.getValue());
	        }	
	        else
	        	if (toGroupPartner!=null)
	        	{	
	        		MBPGroup partner = new MBPGroup(getCtx(),Integer.parseInt(toGroupPartner),null);
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
	        
	        ps.executeUpdate();
	        ps.close();
	
	        //   Calculo el saldo inicial para la cuenta
	    	BigDecimal saldoInicial = obtenerSaldoInicial(C_BPartner_ID,BPartner_Location_ID);
	    	
	    	// 	Ingreso en la tabla el detalle del saldo inicial
	        sql = "INSERT INTO T_CTACTE_BPARTNER_DETALLE VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?)";
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
	        
            ps.setString(13, null);						//	CODIGO_MONEDA
            ps.setInt(14, CC_BPARTNER_ID);				//	T_CS_BPARTNER_ID
	        
	        ps.executeUpdate();
	        ps.close();
	
	        //Incremento CC_BARTNER_DETALLE_ID
	        CC_BPARTNER_DETALLE_ID++;
	        
	        BigDecimal saldo = saldoInicial;
	        
	        while (!(rs.isAfterLast() || (C_BGroup_ID!=rs.getInt(17)) ||(C_BPartner_ID!=rs.getInt(8)) ||(BPartner_Location_ID!=rs.getInt(15))))
	    	{
	    		// 	Ingreso en la tabla el detalle los totales
	            sql = "INSERT INTO T_CTACTE_BPARTNER_DETALLE VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?)";
	            ps = DB.prepareStatement(sql, null);
	            ps.setInt(1, cliente);						//	CLIENTE
	            ps.setInt(2, organizacion);					//	ORGANIZACION
	            //Luego del 'Y'
	            ps.setInt(3, C_BPartner_ID);				//	C_BPARTNER_ID
	            ps.setInt(4, p_instance);					//	INSTANCE
	            ps.setDate(5,rs.getDate(3));				//	FECHA
	            ps.setString(6, rs.getString(4));			//	CONCEPTO
	            if (rs.getBigDecimal(11)!=null)
	            {	
	            	ps.setBigDecimal(7, rs.getBigDecimal(11).multiply(rs.getBigDecimal(13)));	//	DEBE
	            	ps.setBigDecimal(12, rs.getBigDecimal(12));	//	HABER
	            	saldo = saldo.add(rs.getBigDecimal(11).multiply(rs.getBigDecimal(13)));
	            }	
	            else
	            {	
	            	ps.setBigDecimal(7, rs.getBigDecimal(11));	//	DEBE
	            	ps.setBigDecimal(12, rs.getBigDecimal(12).multiply(rs.getBigDecimal(13)));	//	HABER
	            	saldo = saldo.subtract(rs.getBigDecimal(12).multiply(rs.getBigDecimal(13)));
	            }
	            ps.setBigDecimal(8, saldo);					//	SALDO
	            ps.setInt(9, CC_BPARTNER_DETALLE_ID);		//	CC_BPARTNER_DETALLE_ID
	            ps.setString(10, rs.getString(5));			//	NUMERO
	            ps.setString(11, rs.getString(6));			//	CONDICION
	            
	            ps.setString(13, rs.getString(14));			//	CODIGO_MONEDA
	            ps.setInt(14, CC_BPARTNER_ID);				//	T_CS_BPARTNER_ID
	            
	            ps.executeUpdate();
	            ps.close();
	
	            //Incremento CC_BARTNER_DETALLE_ID
	            CC_BPARTNER_DETALLE_ID++;
	            
	            rs.next();
	    	}
	    	
	        //	Incremento CC_BPARTNER_ID
	        CC_BPARTNER_ID++;
	
	    }
      
	    UtilProcess.initViewer("Cuenta Corriente en Pesos por Grupo",0,getProcessInfo());
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
		"FROM " +
		"(	SELECT  i.AD_CLient_ID as CLIENTE," +
		"	 		i.AD_ORG_ID AS ORG," +
		"			i.DATEINVOICED as FECHA," +
		"	        d.NAME as TIPO," +
		"			i.DocumentNo as NUMERO," +
		"			t.name as CONDICION," +
		"	        i.C_CURRENCY_ID as MONEDA," +
		"	        b.C_BPartner_ID as BP_ID," +
		"	        b.VALUE as CLAVE," +
		"	        b.name as NOMBRE," +
		"			CASE d.docbasetype" +
		"				WHEN 'ARI' THEN i.grandtotal" +
		"             	WHEN 'API' THEN null" +
		" 				WHEN 'ARF' THEN i.grandtotal" +
		"				WHEN 'ARC' THEN null" +
		"				WHEN 'APC' THEN i.grandtotal" +
		"				ELSE 0" +
		"           END as DEBE," +
		"			CASE d.docbasetype" +
		"				WHEN 'ARI' THEN null" +
		"				WHEN 'API' THEN i.grandtotal" +
		"				WHEN 'ARF' THEN null" +
		"				WHEN 'ARC' THEN i.grandtotal" +
		"				WHEN 'APC' THEN null" +
		"				ELSE 0" +
		"           END as HABER," +
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
		"			END as C_BPartner_Location_Name," +
		"			cbp.C_BP_GROUP_ID as grupo,"	+		
		"			cbp.name as nombreGrupo,"	+
		"	        cbp.VALUE as CLAVEGRUPO" +
		"	FROM C_Invoice i" +
		"		INNER JOIN C_BPARTNER b ON(i.C_BPARTNER_ID = b.C_BPARTNER_ID)" +
		"		INNER JOIN c_bp_group cbp on (b.C_BP_GROUP_ID = cbp.C_BP_GROUP_ID)" +	
		"		INNER JOIN C_DocType d ON(i.C_DocType_ID = d.C_DocType_ID)" +
		"		INNER JOIN C_PaymentTerm t ON(i.C_PaymentTerm_ID = t.C_PaymentTerm_ID)" +
		"   	INNER JOIN C_Currency y ON(y.C_Currency_ID = i.C_Currency_ID)" +	
		"		LEFT JOIN C_Location l ON (i.BILL_LOCATION_ID = l.C_Location_ID)" +
		"		LEFT JOIN C_BPartner_Location bpl ON (bpl.C_LOCATION_ID = l.C_Location_ID)" +
					
			getSqlWhere("i.ISSOTRX","i.DATEINVOICED","i.C_CURRENCY_ID","i.DocStatus") +
	                
		"	UNION" +
		"	(	SELECT C_Payment.AD_CLient_ID as CLIENTE," +
		"              C_Payment.AD_ORG_ID AS ORG," +
		"              C_Payment.DATETRX as FECHA," +
		"              d.NAME as TIPO," +
		"              C_Payment.DOCUMENTNO as NUMERO," +
		"              null as CONDICION," +
		"              C_Payment.C_CURRENCY_ID as MONEDA," +
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
		"                 WHEN C_Payment.COTIZACION is null THEN 1" +
		"                 ELSE C_Payment.COTIZACION" +
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
		"			   cbp.C_BP_GROUP_ID as grupo,"	+
		"			   cbp.name as nombreGrupo,"	+
		"	           cbp.VALUE as CLAVEGRUPO" +
		
		"		FROM C_Payment" +
		
		"	       INNER JOIN C_DocType d ON(C_Payment.C_DocType_ID = d.C_DocType_ID)" +
		" 		   INNER JOIN C_BPARTNER b ON(C_Payment.C_BPARTNER_ID = b.C_BPARTNER_ID)" +
		"		   INNER JOIN c_bp_group cbp on (b.C_BP_GROUP_ID = cbp.C_BP_GROUP_ID)" +	
		"          INNER JOIN C_Currency y ON(y.C_Currency_ID = C_Payment.C_Currency_ID)" +
		"          INNER JOIN C_AllocationLine al ON (al.C_Payment_ID = C_Payment.C_Payment_ID)" +
		"          INNER JOIN C_Allocationhdr ah ON (ah.C_AllocationHdr_ID = al.C_AllocationHdr_ID)" +
		"          LEFT JOIN C_Invoice i ON (al.C_Invoice_ID = i.C_Invoice_ID)" +
		"          LEFT JOIN C_Location l ON (i.BILL_LOCATION_ID = l.C_Location_ID)" +
		"		   LEFT JOIN C_BPartner_Location bpl ON (bpl.C_LOCATION_ID = l.C_Location_ID)" +
		                       
	            getSqlWhere("C_Payment.ISRECEIPT","C_Payment.DATETRX","C_Payment.C_CURRENCY_ID","C_Payment.DocStatus") +
        
	    "		UNION" +
	    "		(	SELECT C_Payment.AD_CLient_ID as CLIENTE," +
	    "                  C_Payment.AD_ORG_ID AS ORG," +
	    "                  C_Payment.DATETRX as FECHA," +
	    "                  d.NAME as TIPO," +
	    "                  C_Payment.DOCUMENTNO as NUMERO," +
	    "                  null as CONDICION," +
	    "                  C_Payment.C_CURRENCY_ID as MONEDA," +
	    "                  b.C_BPartner_ID as BP_ID," +
	    "                  b.VALUE as CLAVE," +
	    "                  b.name as NOMBRE," +
	    "                  CASE d.docbasetype" +
	    "	                  WHEN 'ARR' THEN null" +
	    "                     WHEN 'APP' THEN PAYMENTAVAILABLE(C_Payment.C_Payment_ID)" +
	    "                     ELSE 0" +
	    "                  END as DEBE," +
	    "                  CASE d.docbasetype" +
	    "                     WHEN 'ARR' THEN PAYMENTAVAILABLE(C_Payment.C_Payment_ID)" +
	    "                     WHEN 'APP' THEN null" +
	    "	                  ELSE 0" +
	    "                  END as HABER," +
	    "                  CASE" +
	    "                     WHEN C_Payment.COTIZACION is null THEN 1" +
	    "                     ELSE C_Payment.COTIZACION" +
	    "                  END as COTIZACION," +
	    "                  y.ISO_CODE as CODMONEDA," +
	    "                  0 as C_BPartner_Location_ID," +
	    "                  'SIN ASIGNAR' as C_BPartner_Location_Name," +
	    "				   cbp.C_BP_GROUP_ID as grupo,"	+
	    "				   cbp.name as nombreGrupo,"	+
	    "	               cbp.VALUE as CLAVEGRUPO" +
	    
	    "           FROM C_Payment" +
	    "	           INNER JOIN C_DocType d ON(C_Payment.C_DocType_ID = d.C_DocType_ID)" +
	    "			   INNER JOIN C_BPARTNER b ON(C_Payment.C_BPARTNER_ID = b.C_BPARTNER_ID)" +
	    "			   INNER JOIN c_bp_group cbp on (b.C_BP_GROUP_ID = cbp.C_BP_GROUP_ID)" +	
	    "              INNER JOIN C_Currency y ON(y.C_Currency_ID = C_Payment.C_Currency_ID)" +
	                  
	                getSqlWhere("C_Payment.ISRECEIPT","C_Payment.DATETRX","C_Payment.C_CURRENCY_ID","C_Payment.DocStatus") +
	    "         	AND C_Payment.isAllocated = 'N' AND PAYMENTAVAILABLE(C_Payment.C_Payment_ID) <> 0" +
	    
	    "       )" +
	    "   )" +
	    ") ORDER BY grupo,NOMBRE,C_BPartner_Location_ID,FECHA";	
			
		
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
        if (fromGroupPartner!=null){
        	MBPGroup partner = new MBPGroup(getCtx(),Integer.parseInt(fromGroupPartner),null);
    		pstmt.setString(paramIndex, partner.getValue());
            paramIndex++;
        }
        if (toGroupPartner!=null){
        	MBPGroup partner = new MBPGroup(getCtx(),Integer.parseInt(toGroupPartner),null);
        	pstmt.setString(paramIndex, partner.getValue());
            paramIndex++;
        }
        
        if (C_Currency_ID.compareTo(BigDecimal.ZERO)>0){
        	pstmt.setInt(paramIndex, C_Currency_ID.intValue());
        	paramIndex++;
		}
        
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
        if (fromGroupPartner!=null){
        	MBPGroup partner = new MBPGroup(getCtx(),Integer.parseInt(fromGroupPartner),null);
    		pstmt.setString(paramIndex, partner.getValue());
            paramIndex++;
        }
        if (toGroupPartner!=null){
        	MBPGroup partner = new MBPGroup(getCtx(),Integer.parseInt(toGroupPartner),null);
        	pstmt.setString(paramIndex, partner.getValue());
            paramIndex++;
        }
        
        if (C_Currency_ID.compareTo(BigDecimal.ZERO)>0){
        	pstmt.setInt(paramIndex, C_Currency_ID.intValue());
        	paramIndex++;
		}
        
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
        if (fromGroupPartner!=null){
        	MBPGroup partner = new MBPGroup(getCtx(),Integer.parseInt(fromGroupPartner),null);
    		pstmt.setString(paramIndex, partner.getValue());
            paramIndex++;
        }
        if (toGroupPartner!=null){
        	MBPGroup partner = new MBPGroup(getCtx(),Integer.parseInt(toGroupPartner),null);
        	pstmt.setString(paramIndex, partner.getValue());
            paramIndex++;
        }
        
        if (C_Currency_ID.compareTo(BigDecimal.ZERO)>0)
        	pstmt.setInt(paramIndex, C_Currency_ID.intValue());
        
        return pstmt.executeQuery();
    }
	
	/** BISion - 08/05/2009 - Daniel Gini
     * Metodo que retorna la clausula "where" dependiendo de los parametros ingresados.
     * @return String
     */
    private String getSqlWhere(String param1, String param2, String param3, String param4){
    	String CBP = "VALUE";
    	String sqlWhere = " WHERE "+param1+" = ?";
      
		if (toDate!=null && fromDate!=null)
			sqlWhere+= " AND "+param2+" BETWEEN ? AND ?";
		else if (toDate!=null && fromDate==null)
			sqlWhere+= " AND "+param2+" <= ?";
		else if (toDate==null && fromDate!=null)
			sqlWhere+= " AND "+param2+" >= ?";
	
		if (toGroupPartner!=null && fromGroupPartner!=null)
			sqlWhere+= " AND cbp." + CBP + "  >= ? AND cbp." + CBP + " <= ?";
		else if (toGroupPartner!=null && fromGroupPartner==null)
			sqlWhere+= " AND cbp." + CBP + " <= ?";
		else if (toGroupPartner==null && fromGroupPartner!=null)
			sqlWhere+= " AND cbp." + CBP + " >= ?";
		
		if (C_Currency_ID.compareTo(BigDecimal.ZERO)>0)
			sqlWhere+= " AND "+param3+" = ?";
		
		sqlWhere+= " AND "+param4+" IN ('CO','CL') AND B.ISACTIVE LIKE 'Y'";
    	
		return sqlWhere;
    }

    /** BISion - 08/05/2009 - Daniel Gini
     * Retorna el saldo inicial para un Socio de Negocio, es decir el monto acumulado hasta la "Fecha Desde", ingresada por parametro.
     * @param BPartner_ID
     * @throws Exception
     * @return BigDecimal
     */
    private BigDecimal obtenerSaldoInicial(int BPartner_ID, int BPartner_Location_ID) throws Exception
    {
        //Obtengo el monto
    	if (fromDate==null)
    		return (new BigDecimal(0));
    	
        String sql = "";
        PreparedStatement pstmt;
        
        if (BPartner_Location_ID!=0)
        {	sql = 
	        "SELECT sum(MONTO) " +
	        "FROM ( " +
				" SELECT   " +
	        	"	CASE d.docbasetype " +
	        	"		WHEN 'ARI' THEN i.grandtotal*i.COTIZACION " +
	        	"		WHEN 'API' THEN -i.grandtotal*i.COTIZACION " +
	        	"		WHEN 'ARF' THEN i.grandtotal*i.COTIZACION " +
	        	"		WHEN 'ARC' THEN -i.grandtotal*i.COTIZACION " +
	        	"		WHEN 'APC' THEN i.grandtotal*i.COTIZACION " +
	        	"		ELSE 0 " +
	        	"	END as MONTO, " +
	        	"	i.documentno as DOCUMENTNRO " +
				" FROM C_Invoice i " +
				" INNER JOIN C_BPARTNER b ON(i.C_BPARTNER_ID = b.C_BPARTNER_ID) " +
				" INNER JOIN C_DocType d ON(i.C_DocType_ID = d.C_DocType_ID) " + 
				" INNER JOIN C_Location l ON (i.BILL_LOCATION_ID = l.C_Location_ID) " +
				" INNER JOIN C_BPartner_Location bpl ON (bpl.C_LOCATION_ID = l.C_Location_ID) " +
				" WHERE i.DocStatus IN ('CO','CL') AND i.ISSOTRX=? AND b.C_BPARTNER_ID = ? AND i.DATEINVOICED<? AND bpl.C_BPartner_Location_ID = ? ";
				
				if (C_Currency_ID.compareTo(BigDecimal.ZERO)>0)
					sql = sql + " AND i.C_CURRENCY_ID = ? ";
        
        		sql = sql + 
				"UNION " +
				"( " +
				" SELECT " +
	            " 	CASE d.docbasetype " +
	            "		WHEN 'ARR' THEN -al.AMOUNT*C_Payment.COTIZACION " +
	            "		WHEN 'APP' THEN al.AMOUNT*C_Payment.COTIZACION " +
	            "		ELSE 0 " +
	            "	END as MONTO, " +
	            "	C_Payment.DOCUMENTNO as DOCUMENTNRO " +
				" FROM C_Payment " +
				" INNER JOIN C_BPARTNER b ON(C_Payment.C_BPARTNER_ID = b.C_BPARTNER_ID) " +
				" INNER JOIN C_DocType d ON(C_Payment.C_DocType_ID = d.C_DocType_ID) " +
				" INNER JOIN C_AllocationLine al ON (al.C_Payment_ID = C_Payment.C_Payment_ID)" +
				" INNER JOIN C_Invoice i ON (al.C_Invoice_ID = i.C_Invoice_ID)" +
				" INNER JOIN C_Location l ON (i.BILL_LOCATION_ID = l.C_Location_ID) " +
				" INNER JOIN C_BPartner_Location bpl ON (bpl.C_LOCATION_ID = l.C_Location_ID) " +
				" WHERE C_Payment.DocStatus IN ('CO','CL') AND C_Payment.ISRECEIPT=? AND b.C_BPARTNER_ID = ? " +
				"   AND C_Payment.DATETRX<? AND bpl.C_BPartner_Location_ID = ? ";
				
				if (C_Currency_ID.compareTo(BigDecimal.ZERO)>0)
					sql = sql + " AND C_Payment.C_CURRENCY_ID = ? ";	
				
				sql = sql + ") )";
        
	        pstmt = DB.prepareStatement(sql, get_TrxName());
	        
	        if (C_Currency_ID.compareTo(BigDecimal.ZERO)>0)
			{
	        	if (isSOTrx.booleanValue() == true)
		        {	pstmt.setString(1, "Y");
		        	pstmt.setString(6, "Y");
		        }
		        else
		        {	pstmt.setString(1, "N");
		        	pstmt.setString(6, "N");
		        }
	        	
	        	pstmt.setInt(2, BPartner_ID);
		        pstmt.setTimestamp(3, fromDate);
		        pstmt.setInt(4, BPartner_Location_ID);
		        pstmt.setInt(5, C_Currency_ID.intValue());
		        
		        pstmt.setInt(7, BPartner_ID);
		        pstmt.setTimestamp(8, fromDate);
		        pstmt.setInt(9, BPartner_Location_ID);
		        pstmt.setInt(10, C_Currency_ID.intValue());
			}
			else
			{
				if (isSOTrx.booleanValue() == true)
		        {	pstmt.setString(1, "Y");
		        	pstmt.setString(5, "Y");
		        }
		        else
		        {	pstmt.setString(1, "N");
		        	pstmt.setString(5, "N");
		        }
	        	
	        	pstmt.setInt(2, BPartner_ID);
		        pstmt.setTimestamp(3, fromDate);
		        pstmt.setInt(4, BPartner_Location_ID);
		        
		        pstmt.setInt(6, BPartner_ID);
		        pstmt.setTimestamp(7, fromDate);
		        pstmt.setInt(8, BPartner_Location_ID);
			}
        }
        else
        {
        	sql = 
	        "SELECT sum(MONTO) " +
	        "FROM ( " +
				" SELECT " +
	        	"	CASE d.docbasetype " +
	        	"		WHEN 'ARI' THEN i.grandtotal*i.COTIZACION " +
	        	"		WHEN 'API' THEN -i.grandtotal*i.COTIZACION " +
	        	"		WHEN 'ARF' THEN i.grandtotal*i.COTIZACION " +
	        	"		WHEN 'ARC' THEN -i.grandtotal*i.COTIZACION " +
	        	"		WHEN 'APC' THEN i.grandtotal*i.COTIZACION " +
	        	"		ELSE 0 " +
	        	"	END as MONTO, " +
	        	"	i.documentno as DOCUMENTNRO " +
				" FROM C_Invoice i " +
				" INNER JOIN C_BPARTNER b ON(i.C_BPARTNER_ID = b.C_BPARTNER_ID) " +
				" INNER JOIN C_DocType d ON(i.C_DocType_ID = d.C_DocType_ID) " + 
				" WHERE i.DocStatus IN ('CO','CL') AND i.ISSOTRX=? AND b.C_BPARTNER_ID = ?" +
				"		AND i.DATEINVOICED<? AND i.BILL_LOCATION_ID is null ";
				
				if (C_Currency_ID.compareTo(BigDecimal.ZERO)>0)
					sql = sql + " AND i.C_CURRENCY_ID = ? ";	
        		
        		sql = sql + 
				"UNION " +
				"( " +
				" SELECT " +
	            " 	CASE d.docbasetype " +
	            "		WHEN 'ARR' THEN -al.AMOUNT*C_Payment.COTIZACION " +
	            "		WHEN 'APP' THEN al.AMOUNT*C_Payment.COTIZACION " +
	            "		ELSE 0 " +
	            "	END as MONTO, " +
	            "	C_Payment.DOCUMENTNO as DOCUMENTNRO " +
				" FROM C_Payment " +
				" INNER JOIN C_BPARTNER b ON(C_Payment.C_BPARTNER_ID = b.C_BPARTNER_ID) " +
				" INNER JOIN C_DocType d ON(C_Payment.C_DocType_ID = d.C_DocType_ID) " +
				" INNER JOIN C_AllocationLine al ON (al.C_Payment_ID = C_Payment.C_Payment_ID)" +
				" INNER JOIN C_Invoice i ON (al.C_Invoice_ID = i.C_Invoice_ID)" +
				" WHERE C_Payment.DocStatus IN ('CO','CL') AND C_Payment.ISRECEIPT=? AND b.C_BPARTNER_ID = ?" +
				"		AND C_Payment.DATETRX<? AND i.BILL_LOCATION_ID is null ";
				
				if (C_Currency_ID.compareTo(BigDecimal.ZERO)>0)
					sql = sql + " AND C_Payment.C_CURRENCY_ID = ? ";	
        		
        		sql = sql +
				" UNION " +
				" ( " +
				"  SELECT " +
	            " 	 CASE d.docbasetype " +
	            "	 	WHEN 'ARR' THEN -PAYMENTAVAILABLE(C_Payment.C_Payment_ID)*C_Payment.COTIZACION " +
	            "	 	WHEN 'APP' THEN PAYMENTAVAILABLE(C_Payment.C_Payment_ID)*C_Payment.COTIZACION " +
	            "		ELSE 0 " +
	            "	 END as MONTO, " +
	            "	 C_Payment.DOCUMENTNO as DOCUMENTNRO " +
				"  FROM C_Payment " +
				"  INNER JOIN C_BPARTNER b ON(C_Payment.C_BPARTNER_ID = b.C_BPARTNER_ID) " +
				"  INNER JOIN C_DocType d ON(C_Payment.C_DocType_ID = d.C_DocType_ID) " +
				"  WHERE C_Payment.DocStatus IN ('CO','CL') AND C_Payment.ISRECEIPT=? AND b.C_BPARTNER_ID = ?" +
				"		 AND C_Payment.DATETRX<? AND C_Payment.isAllocated = 'N' AND PAYMENTAVAILABLE(C_Payment.C_Payment_ID) <> 0 ";
				
				if (C_Currency_ID.compareTo(BigDecimal.ZERO)>0)
					sql = sql + " AND C_Payment.C_CURRENCY_ID = ? ";
        		
				sql = sql +
				" )" +
				") )";
        	
            pstmt = DB.prepareStatement(sql, get_TrxName());
            
            
            if (C_Currency_ID.compareTo(BigDecimal.ZERO)>0)
			{
            	if (isSOTrx.booleanValue() == true)
                {	pstmt.setString(1, "Y");
                	pstmt.setString(5, "Y");
                	pstmt.setString(9, "Y");
                }
                else
                {	pstmt.setString(1, "N");
                	pstmt.setString(5, "N");
                	pstmt.setString(9, "N");
                }
	        	
            	pstmt.setInt(2, BPartner_ID);
                pstmt.setTimestamp(3, fromDate);
                pstmt.setInt(4, C_Currency_ID.intValue());
                
                pstmt.setInt(6, BPartner_ID);
                pstmt.setTimestamp(7, fromDate);
                pstmt.setInt(8, C_Currency_ID.intValue());
                
                pstmt.setInt(10, BPartner_ID);
                pstmt.setTimestamp(11, fromDate);
                pstmt.setInt(12, C_Currency_ID.intValue());
			}
			else
			{
            	if (isSOTrx.booleanValue() == true)
                {	pstmt.setString(1, "Y");
                	pstmt.setString(4, "Y");
                	pstmt.setString(7, "Y");
                }
                else
                {	pstmt.setString(1, "N");
                	pstmt.setString(4, "N");
                	pstmt.setString(7, "N");
                }
	        	
            	pstmt.setInt(2, BPartner_ID);
                pstmt.setTimestamp(3, fromDate);
                
                pstmt.setInt(5, BPartner_ID);
                pstmt.setTimestamp(6, fromDate);
                
                pstmt.setInt(8, BPartner_ID);
                pstmt.setTimestamp(9, fromDate);
			}
        }
        
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
