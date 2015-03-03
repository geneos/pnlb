package org.eevolution.process;
import org.compiere.model.MBPGroup;
import org.compiere.process.*;
import java.math.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.compiere.util.*;
import org.eevolution.tools.UtilProcess;

/**
 *  Clase que ingresa las filas a la tabla T_CC_BARTNER_DETALLE
 * @author Daniel BISion
 */
public class GenerateReporteCompoSaldosGrupo extends SvrProcess{

//PARAMETROS DE ENTRADA
int p_instance;
private String fromGroupPartner = null;
private String toGroupPartner = null;
private BigDecimal C_Currency_ID = new BigDecimal(0);
private BigDecimal C_BPartner_Location_ID = new BigDecimal(0);
private Boolean isSOTrx = false;
private BigDecimal cotizacion = new BigDecimal(0);

protected void prepare(){
    ProcessInfoParameter[] para = getParameter();
    boolean flagCliente = false;
    for (int i = 0; i < para.length; i++)
	{
        String name = para[i].getParameterName();
      	if (name.equals("C_Currency_ID"))
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
           		else
           			if(name.equals("C_BPartner_Location_ID"))
           				C_BPartner_Location_ID = ((BigDecimal)para[i].getParameter());
           			else
               			if(name.equals("COTIZACION"))
               				cotizacion = ((BigDecimal)para[i].getParameter());
      	
    }
	
    p_instance = 0;
}

protected String doIt() throws Exception {
    //BORRADO DE LA TABLA T_COMPOSALDOS_BPARTNER
    String sql;
    log.info("Comienzo del proceso de Composici�n de Saldos");
    
    log.info("Borrado de la tabla temporal T_COMPSALDOS_BPARTNER");
    DB.executeUpdate("Delete from T_COMPSALDOS_BPARTNER", null);
    
    log.info("Borrado de la tabla temporal T_COMPSALDOS_BPARTNER_DETALLE");
    DB.executeUpdate("Delete from T_COMPSALDOS_BPARTNER_DETALLE", null);
    
    ResultSet rs = getComprobantes();
    
    int CS_BPARTNER_DETALLE_ID=1000000;
    int CS_BPARTNER_ID=1000000;
    
    if (rs.next())
    {
    	while (!rs.isAfterLast())	{
    	
	    	int cliente = rs.getInt(1);
	    	int organizacion = rs.getInt(2);
	    	int C_BPartner_ID=rs.getInt(10);
	    	int C_Currency_ID = rs.getInt(9);
	    	
	    	int C_Location_ID = rs.getInt(18);
    		String C_Location_Name = rs.getString(19);
    		int C_BGroup_ID=rs.getInt(20);
    		
			// 	  Ingreso en la tabla cabecera
    		sql = "INSERT INTO T_COMPSALDOS_BPARTNER VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?)";
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
	        ps.setString(6, rs.getString(12));			//	NAME
	        ps.setString(6, rs.getString(21) +  " - " + rs.getString(12));	//	NAME
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
	        ps.setDate(9,rs.getDate(3));				//	FECHA
	        ps.setInt(10, C_Location_ID);				//	BP_LOCATION_ID
	        ps.setString(11, C_Location_Name);			// 	BP_LOCATION_NAME
	        ps.setInt(12, CS_BPARTNER_ID);				//	T_CS_BPARTNER_ID
	        ps.setString(13, rs.getString(11));			// 	CODE_BP
	        ps.setBigDecimal(14, cotizacion);			// 	COTIZACION
	        
	        ps.executeUpdate();
	        ps.close();
	        
	        ps = null;


            BigDecimal saldoPendiente = new BigDecimal(0);
	        //BigDecimal saldoTotal = new BigDecimal(0);
	        
	        while (!(rs.isAfterLast() || (C_BGroup_ID!=rs.getInt(20)) || (C_BPartner_ID!=rs.getInt(10)) || (C_Location_ID!=rs.getInt(18))))
	    	{
	    		// 	Ingreso en la tabla el detalle los totales
	            sql = "INSERT INTO T_COMPSALDOS_BPARTNER_DETALLE VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?,?)";
	            ps = DB.prepareStatement(sql, null);
	            ps.setInt(1, cliente);						//	CLIENTE
	            ps.setInt(2, organizacion);					//	ORGANIZACION
	            //Luego del 'Y'
	            ps.setInt(3, C_BPartner_ID);				//	C_BPARTNER_ID
	            ps.setInt(4, p_instance);					//	INSTANCE
	            ps.setDate(5,rs.getDate(3));				//	FECHA
	            ps.setString(6, rs.getString(4));			//	CONCEPTO
	            ps.setString(7, rs.getString(5));			//	NUMERO
	            ps.setDate(8, rs.getDate(6));				//	VENCIMIENTO
	            ps.setString(9, rs.getString(7));			//	CONDICIONES
	
	            BigDecimal tasa = cotizacion;
	            if (tasa.equals(BigDecimal.ZERO))
	            	tasa = rs.getBigDecimal(16);
	            
	            ps.setBigDecimal(10, rs.getBigDecimal(13).multiply(tasa));	//	MONTO
	            //	Actualizo el saldo acumulado total       
	            //	saldoTotal = saldoTotal.add(rs.getBigDecimal(13));
	           	ps.setBigDecimal(11, getSaldoAbierto(rs.getInt(14),rs.getInt(15),rs.getBigDecimal(13)).multiply(tasa));	//	PENDIENTE
	           	//	Actualizo el saldo acumulado pendiente           
	           	saldoPendiente = saldoPendiente.add(getSaldoAbierto(rs.getInt(14),rs.getInt(15),rs.getBigDecimal(13).multiply(tasa)));
	            
	           	ps.setInt(12, rs.getInt(8));				//	MORA
	            ps.setInt(13, CS_BPARTNER_DETALLE_ID);		//	T_CS_BPARTNER_DETALLE_ID
	            ps.setString(14, rs.getString(17));			//	CODIGO_MONEDA
	            ps.setInt(15, CS_BPARTNER_ID);				//	T_CS_BPARTNER_ID
	            
	            ps.executeUpdate();
	            ps.close();

	
	            //Incremento CS_BARTNER_DETALLE_ID
	            CS_BPARTNER_DETALLE_ID++;
	            
	            rs.next();
	    	}
	    	
	    	// 	Ingreso en la tabla el subtotal
	    	sql = "INSERT INTO T_COMPSALDOS_BPARTNER_DETALLE VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?,?)";
	        ps = DB.prepareStatement(sql, null);
	        ps.setInt(1, cliente);					//	CLIENTE
	        ps.setInt(2, organizacion);				//	ORGANIZACION
	        //Luego del 'Y'
	        ps.setInt(3, C_BPartner_ID);			//	C_BPARTNER_ID
	        ps.setInt(4, p_instance);				//	INSTANCE
	        ps.setDate(5,null);						//	FECHA
	        ps.setString(6, "Total en Pesos:");		//	CONCEPTO
	        ps.setString(7, null);					//	NUMERO
	        ps.setDate(8, null);					//	VENCIMIENTO
	        ps.setString(9, null);					//	CONDICIONES
	        ps.setBigDecimal(10, null);				//	MONTO
	        ps.setBigDecimal(11, saldoPendiente);	//	PENDIENTE
	       	ps.setInt(12, 0);						//	MORA
	        ps.setInt(13, CS_BPARTNER_DETALLE_ID);	//	CS_BPARTNER_DETALLE_ID
	        ps.setString(14, null);					//	CODIGO_MONEDA
	        ps.setInt(15, CS_BPARTNER_ID);			//	CS_BPARTNER_ID
	        
	        ps.executeUpdate();
	        ps.close();

	
	        //Incremento CS_BARTNER_DETALLE_ID
	        CS_BPARTNER_DETALLE_ID++;
	        
	        //	Incremento CS_BPARTNER_ID
	        CS_BPARTNER_ID++;
    		
	    	}
    	
    	rs.close();

	      
	    UtilProcess.initViewer("Composicion Saldos Grupo Socio de Negocio en Pesos",0,getProcessInfo());
	    return "";
            
	}
	
	return "El Reporte no arrojo Datos para los parametros ingresados";
}

/*	
	private int getLocation(int C_BPartner_ID, int C_Location_ID){
		
		String sql = "Select T_CS_BPARTNER_ID FROM T_CS_BPARTNER WHERE C_BPARTNER_ID = ? AND BP_LOCATION_ID = ?";
		
		PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
	
		try {
			pstmt.setInt(1, C_BPartner_ID);
			pstmt.setInt(2, C_Location_ID);
		
			ResultSet rs = pstmt.executeQuery();
	    
		    if (rs.next())
		    {
		    	int T_CS_BPARTNER_ID = rs.getInt(1);
		    	pstmt.close();
		    	rs.close();
		    	
		    	return T_CS_BPARTNER_ID;
		    	
		    }
		    
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	/** BISion - 08/05/2009 - Daniel Gini
     * Retorna el saldo pendiente para una factura o pago, ingresado por par�metro.
     * @param C_Comprobante_ID
     * @param Tipo
     * @throws Exception
     * @return BigDecimal
     */

/*
    private Hashtable<Integer,String> getBPLocation(int C_Comprobante_ID, int tipo) throws Exception
    {
    	Hashtable<Integer,String> BP_Location = new Hashtable<Integer,String>();
    	
    	String sql = null;
    	if (tipo==FACTURA)
    		sql = "SELECT DISTINCT" +
    			  "		  CASE" +
    			  "	        WHEN bpl.C_BPartner_Location_ID is null" +
    			  "	        THEN 0" +
    			  "         ELSE bpl.C_BPartner_Location_ID" +
    			  "	      END as C_BPartner_Location_ID," +
    			  "	      CASE" +
    			  "       	WHEN bpl.C_BPartner_Location_ID is null" +
    			  "     	THEN 'SIN ASIGNAR'" +
    			  " 	    ELSE TO_CHAR(bpl.name)" +
    			  "	      END as C_BPartner_Location_Name" +
    			  "       FROM C_Invoice i" +
    			  "		  LEFT JOIN C_Location l ON (i.BILL_LOCATION_ID = l.C_Location_ID)" +
    			  "		  LEFT JOIN C_BPartner_Location bpl ON (bpl.C_LOCATION_ID = l.C_Location_ID)" +
    			  "		  WHERE C_Invoice_ID = ?";
        else
        	if (tipo==PAGO)
        		sql = "Select DISTINCT" +
        			  "       CASE" +
        			  "			WHEN bpl.C_BPartner_Location_ID is null" +
        			  "			THEN 0" +
        			  "			ELSE bpl.C_BPartner_Location_ID" +
        			  "		  END as C_BPartner_Location_ID," +
        			  "      CASE" +
        			  "			WHEN bpl.C_BPartner_Location_ID is null" +
        			  "         THEN 'SIN ASIGNAR'" +
        			  "         ELSE TO_CHAR(bpl.name)" +
        			  "      END as C_BPartner_Location_Name" +
        			  "		 FROM C_Invoice i" +
        			  "		 LEFT JOIN C_Location l ON (i.BILL_LOCATION_ID = l.C_Location_ID)" +
        			  "	     LEFT JOIN C_BPartner_Location bpl ON (bpl.C_LOCATION_ID = l.C_Location_ID)" +
        			  "		 INNER JOIN C_AllocationLine al ON (al.C_Invoice_ID = i.C_Invoice_ID)" +
        			  "		 INNER JOIN C_Payment p ON (p.C_Payment_ID = al.C_Payment_ID)" +
        			  "		 INNER JOIN C_Allocationhdr ah ON (ah.C_AllocationHdr_ID = al.C_AllocationHdr_ID)" +
        			  "		 WHERE C_Payment_ID = ?";
        	
    	PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
    	pstmt.setInt(1, C_Comprobante_ID);
    	
    	ResultSet rs = pstmt.executeQuery();
        
        while (rs.next())
            BP_Location.put(rs.getInt(1), rs.getString(2));
        
        pstmt.close();
        rs.close();
        
        return BP_Location;
    }
	
	*/

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
			"        i.DOCUMENTNO as NUMERO, " +
			"        paymentTermDueDate (t.C_PaymentTerm_ID, i.DATEINVOICED) as VENCIMIENTO, " +
			"        t.name as CONDICION, " +
			"        trunc(sysdate - paymentTermDueDate (t.C_PaymentTerm_ID, i.DATEINVOICED)) as MORA, " +
			"        i.C_CURRENCY_ID as MONEDA, " +
			"        b.C_BPartner_ID as BP_ID, " +
			"        b.VALUE as CLAVE, " +
			"        b.name as NOMBRE, " +
			"        CASE d.docbasetype " +
			"        	WHEN 'ARI' THEN i.grandtotal " +
			"        	WHEN 'API' THEN -i.grandtotal " +
			"        	WHEN 'ARF' THEN i.grandtotal " +
			"        	WHEN 'ARC' THEN -i.grandtotal " +
			"        	WHEN 'APC' THEN i.grandtotal " +
			"        	ELSE 0 " +
            " 		 END as MONTO, " +
            "		 i.C_Invoice_ID as ID, " +
            "		 0 as PAGO, " +
            "		 CASE" +
            "			 WHEN i.COTIZACION is null THEN 1 " +
            "			 ELSE i.COTIZACION" +
            "            END as COTIZACION," +
            "        y.ISO_CODE as CODMONEDA," +
            "        CASE" +
            "       	 WHEN bpl.C_BPartner_Location_ID is null" +
            "        	 THEN 0" +
            "		  	 ELSE bpl.C_BPartner_Location_ID" +
            "		 END as C_BPartner_Location_ID," +
            "		 CASE" +
            "		 	 WHEN bpl.C_BPartner_Location_ID is null" +
            "  		 	 THEN 'SIN ASIGNAR'" +
            "            ELSE TO_CHAR(bpl.name)" +
    		"			END as C_BPartner_Location_Name," +
    	    "			cbp.C_BP_GROUP_ID as grupo,"	+		
    		"			cbp.name as nombreGrupo,"	+
    		"	        cbp.VALUE as CLAVEGRUPO" +
    		
            " FROM C_Invoice i " +
			" INNER JOIN C_BPARTNER b ON(i.C_BPARTNER_ID = b.C_BPARTNER_ID) " +
			" INNER JOIN c_bp_group cbp on (b.C_BP_GROUP_ID = cbp.C_BP_GROUP_ID)" +
			" INNER JOIN C_DocType d ON(i.C_DocType_ID = d.C_DocType_ID) " +
			" INNER JOIN C_PaymentTerm t ON(i.C_PaymentTerm_ID = t.C_PaymentTerm_ID) " +
			" INNER JOIN C_Currency y ON(y.C_Currency_ID = i.C_Currency_ID) " +
			" LEFT JOIN C_Location l ON (i.BILL_LOCATION_ID = l.C_Location_ID)" +
			
			/* 05-05-2011 Camarzana Mariano
			 * Se agrego a la sentencia
			 * LEFT JOIN C_BPartner_Location bpl ON (bpl.C_LOCATION_ID = l.C_Location_ID 
			 * la restriccion bpl.c_bpartner_id = b.c_bpartner_id, debido a que en el caso 
			 * en que una misma direccion este asignada a dos socios
			 * 
			 */
			
			" LEFT JOIN C_BPartner_Location bpl ON (bpl.C_LOCATION_ID = l.C_Location_ID and bpl.c_bpartner_id = b.c_bpartner_id)" +
			getSqlWhere("i.ISSOTRX","i.C_CURRENCY_ID","i.DocStatus","i.IsPaid") +
			" UNION " +
			"(SELECT C_Payment.AD_CLient_ID as CLIENTE, " +
			"		 C_Payment.AD_ORG_ID AS ORG, " +
			"		 C_Payment.DATETRX as FECHA, " +
			"        d.NAME as TIPO, " +
			"		 C_Payment.DOCUMENTNO as NUMERO, " +
			"		 null as VENCIMIENTO, " +
			"		 null as CONDICION, " +
			"		 null as MORA, " +
			"        C_Payment.C_CURRENCY_ID as MONEDA, " +
			"        b.C_BPartner_ID as BP_ID, " +
			"        b.VALUE as CLAVE, " +
			"        b.name as NOMBRE, " +
			"        CASE d.docbasetype " +
			"        	WHEN 'ARR' THEN -C_Payment.PAYAMT " +
			"        	WHEN 'APP' THEN C_Payment.PAYAMT " +
			"        	ELSE 0 " +
            " 		 END as MONTO, " +			
			"		 C_Payment.C_Payment_ID as ID, " +
            "		 1 as PAGO, " +
            "		 CASE" +
            "			WHEN C_Payment.COTIZACION is null THEN 1 " +
            "			ELSE C_Payment.COTIZACION " +
            "			END as COTIZACION, " +
            "		y.ISO_CODE as CODMONEDA, " +
            "		0 as C_BPartner_Location_ID," +
            "  		'SIN ASIGNAR' as C_BPartner_Location_Name," +
    	    "			cbp.C_BP_GROUP_ID as grupo,"	+		
    		"			cbp.name as nombreGrupo,"	+
    		"	        cbp.VALUE as CLAVEGRUPO" +
    		
			" FROM C_Payment " +
			" INNER JOIN C_BPARTNER b ON(C_Payment.C_BPARTNER_ID = b.C_BPARTNER_ID) " +
			" INNER JOIN c_bp_group cbp on (b.C_BP_GROUP_ID = cbp.C_BP_GROUP_ID)" +
			" INNER JOIN C_DocType d ON(C_Payment.C_DocType_ID = d.C_DocType_ID) " +
			" INNER JOIN C_Currency y ON(y.C_Currency_ID = C_Payment.C_Currency_ID) " +
			getSqlWhere("C_Payment.ISRECEIPT","C_Payment.C_CURRENCY_ID","C_Payment.DocStatus","C_Payment.ISALLOCATED") +
			") " +
		") ORDER BY grupo,NOMBRE,C_BPartner_Location_ID,FECHA";	
		
        PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
        int paramIndex = 1;
        
        //INVOICE
        if (isSOTrx.booleanValue() == true)
        	pstmt.setString(paramIndex, "Y");
        else
        	pstmt.setString(paramIndex, "N");
        paramIndex++;
        
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
        
        if (C_Currency_ID!=null){
        	pstmt.setInt(paramIndex, C_Currency_ID.intValue());
            paramIndex++;
        }
        
        //PAYMENT
        if (isSOTrx.booleanValue() == true)
        	pstmt.setString(paramIndex, "Y");
        else
        	pstmt.setString(paramIndex, "N");
        paramIndex++;
        
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
        
        if (C_Currency_ID!=null){
        	pstmt.setInt(paramIndex, C_Currency_ID.intValue());
            paramIndex++;
        }

        return pstmt.executeQuery();
    }
	
	/** BISion - 08/05/2009 - Daniel Gini
     * Metodo que retorna la clausula "where" dependiendo de los parametros ingresados.
     * @return String
     */
    private String getSqlWhere(String param1, String param2, String param3, String param4){
    	String CBP = "VALUE";
    	String sqlWhere = " WHERE "+param1+" = ?";
      
		if (toGroupPartner!=null && fromGroupPartner!=null)
			sqlWhere+= " AND cbp." + CBP + "  >= ? AND cbp." + CBP + " <= ?";
		else if (toGroupPartner!=null && fromGroupPartner==null)
			sqlWhere+= " AND cbp." + CBP + " <= ?";
		else if (toGroupPartner==null && fromGroupPartner!=null)
			sqlWhere+= " AND cbp." + CBP + " >= ?";
		
		if (C_Currency_ID!=null)
			sqlWhere+= " AND "+param2+" = ?";
		
		sqlWhere+= " AND "+param3+" IN ('CO','CL')";
		
		sqlWhere+= " AND "+param4+" = 'N' AND B.ISACTIVE LIKE 'Y'";
		
		if (!C_BPartner_Location_ID.equals(BigDecimal.ZERO))
			sqlWhere+= " AND C_BPartner_Location_ID = " + C_BPartner_Location_ID;
		
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
    private BigDecimal getSaldoAbierto(int C_Comprobante_ID, int tipo, BigDecimal monto) throws Exception
    {
    	String sql = null;
    	if (tipo==FACTURA)
        	sql = "Select case when (sum(AMOUNT) is Null) then 0 else sum(AMOUNT) end as monto From C_AllocationLine Where C_INVOICE_ID = ?";
        else
        	if (tipo==PAGO)
        		sql = "Select case when (sum(AMOUNT) is Null) then 0 else sum(AMOUNT) end as monto From C_AllocationLine Where C_PAYMENT_ID = ?"; 
        	
    	PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
    	pstmt.setInt(1, C_Comprobante_ID);
    	
    	ResultSet rs = pstmt.executeQuery();
        
        if (rs.next())
        {
        	BigDecimal bd = rs.getBigDecimal(1);
        	pstmt.close();
            rs.close();

            if ((bd.compareTo(BigDecimal.ZERO)<0 && monto.compareTo(BigDecimal.ZERO)>0) || (bd.compareTo(BigDecimal.ZERO)>0 && monto.compareTo(BigDecimal.ZERO)<0))
        		return monto.add(bd);
        	else
        		return monto.subtract(bd);

        }
        
        return monto;
    }
    
}
