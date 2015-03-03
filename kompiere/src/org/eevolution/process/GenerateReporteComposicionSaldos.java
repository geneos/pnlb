/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;
import org.compiere.process.*;
import java.math.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.util.*;
import org.eevolution.tools.UtilProcess;

/**
 *  Clase que ingresa las filas a la tabla T_CC_BARTNER_DETALLE
 * @author santiago
 */
public class GenerateReporteComposicionSaldos extends SvrProcess{

//PARAMETROS DE ENTRADA
int p_instance;
private String fromBPartner = null;
private String toBPartner = null;
private BigDecimal C_Currency_ID = new BigDecimal(0);
private Boolean isSOTrx = false;

protected void prepare(){
    ProcessInfoParameter[] para = getParameter();
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
           		if(name.equals("VALUE"))
           		{
           			fromBPartner=(String)para[i].getParameter();
           			toBPartner=(String)para[i].getParameter_To();
           		}
    }
	
    p_instance = getAD_PInstance_ID();
}

protected String doIt() throws Exception {
    //BORRADO DE LA TABLA T_LIBROMAYORDETALLE
    String sql;
    log.info("Comienzo del proceso de Composición de Saldos");
    
    log.info("Borrado de la tabla temporal T_CS_BPARTNER");
    DB.executeUpdate("Delete from T_CS_BPARTNER", null);
    
    log.info("Borrado de la tabla temporal T_CS_BPARTNER_DETALLE");
    DB.executeUpdate("Delete from T_CS_BPARTNER_DETALLE", null);
    
    ResultSet rs = getComprobantes();
    
    int CS_BPARTNER_DETALLE_ID=1000000;
    
    if (rs.next())
    {
    
    	while (!rs.isAfterLast()){
    	
	    	int cliente = rs.getInt(1);
	    	int organizacion = rs.getInt(2);
	    	int C_BPartner_ID=rs.getInt(10);
	    	
	    	//  Ingreso en la tabla cabecera
	    	sql = "INSERT INTO T_CS_BPARTNER VALUES(?,?,'Y',?,?,?,?,?,?,?,?)";
	    	PreparedStatement ps = DB.prepareStatement(sql, null);
	        ps.setInt(1, cliente);						//	CLIENTE
	        ps.setInt(2, organizacion);					//	ORGANIZACION
	        ps.setInt(3, C_BPartner_ID);				//	C_BPARTNER_ID
	        ps.setInt(4, p_instance);					//	INSTANCE
	        ps.setInt(5, rs.getInt(9));					//	C_CURRENCY_ID
	        ps.setString(6, rs.getString(12));			//	NAME
	        if (fromBPartner!=null)						// 	VALUE
	        	ps.setString(7, fromBPartner);			
	        else
	        	if (toBPartner!=null)
		        	ps.setString(7, toBPartner);		
	        	else
        			ps.setString(7, rs.getString(11));	// 	VALUE
	        if (isSOTrx.booleanValue() == true)
	        	ps.setString(8, "Y");
	        else
	        	ps.setString(8, "N");					// 	ISSOTRX
	        ps.setDate(9,rs.getDate(3));				//	FECHA
	        ps.setString(10, rs.getString(11));			// 	CODE_BP
	        
	        ps.executeUpdate();
	        ps.close();
	        ps=null;
	
	        BigDecimal saldoPendiente = new BigDecimal(0);
	        BigDecimal saldoTotal = new BigDecimal(0);
	    	
	    	while (!(rs.isAfterLast() || (C_BPartner_ID!=rs.getInt(10))))
	    	{
	    		// 	Ingreso en la tabla el detalle los totales
	            sql = "INSERT INTO T_CS_BPARTNER_DETALLE VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?)";
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
	
	            ps.setBigDecimal(10, rs.getBigDecimal(13));	//	MONTO
	            //	Actualizo el saldo acumulado total       
	            	saldoTotal = saldoTotal.add(rs.getBigDecimal(13));
	           	
	            ps.setBigDecimal(11, getSaldoAbierto(rs.getInt(14),rs.getInt(15),rs.getBigDecimal(13)));	//	PENDIENTE
	           	//	Actualizo el saldo acumulado pendiente           
	           		saldoPendiente = saldoPendiente.add(getSaldoAbierto(rs.getInt(14),rs.getInt(15),rs.getBigDecimal(13)));
	            
	           	ps.setInt(12, rs.getInt(8));				//	MORA
	            ps.setInt(13, CS_BPARTNER_DETALLE_ID);		//	CC_BPARTNER_DETALLE_ID
	            
	            ps.executeUpdate();
	            ps.close();
	            ps=null;
	
	            //Incremento CC_BARTNER_DETALLE_ID
	            CS_BPARTNER_DETALLE_ID++;
	            
	            rs.next();
	    	}
	    	
	    	// 	Ingreso en la tabla el subtotal
	    	sql = "INSERT INTO T_CS_BPARTNER_DETALLE VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?)";
	        ps = DB.prepareStatement(sql, null);
	        ps.setInt(1, cliente);					//	CLIENTE
	        ps.setInt(2, organizacion);				//	ORGANIZACION
	        //Luego del 'Y'
	        ps.setInt(3, C_BPartner_ID);			//	C_BPARTNER_ID
	        ps.setInt(4, p_instance);				//	INSTANCE
	        ps.setDate(5,null);						//	FECHA
	        ps.setString(6, "Total:");				//	CONCEPTO
	        ps.setString(7, null);					//	NUMERO
	        ps.setDate(8, null);					//	VENCIMIENTO
	        ps.setString(9, null);					//	CONDICIONES
	        ps.setBigDecimal(10, null);				//	MONTO
	        ps.setBigDecimal(11, saldoPendiente);	//	PENDIENTE
	       	ps.setInt(12, 0);						//	MORA
	        ps.setInt(13, CS_BPARTNER_DETALLE_ID);	//	CC_BPARTNER_DETALLE_ID
	        
	        ps.executeUpdate();
	        ps.close();
	        ps=null;
	
	        //Incremento CC_BARTNER_DETALLE_ID
	        CS_BPARTNER_DETALLE_ID++;
	
	    }
    	
    	rs.close();
    	rs=null;
	      
	    UtilProcess.initViewer("Composicion Saldos Socio de Negocio",getAD_PInstance_ID(),getProcessInfo());
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
            "		 0 as PAGO " +
			" FROM C_Invoice i " +
			" INNER JOIN C_BPARTNER b ON(i.C_BPARTNER_ID = b.C_BPARTNER_ID) " +
			" INNER JOIN C_DocType d ON(i.C_DocType_ID = d.C_DocType_ID) " +
			" INNER JOIN C_PaymentTerm t ON(i.C_PaymentTerm_ID = t.C_PaymentTerm_ID) " + 
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
            "		 1 as PAGO " +
			" FROM C_Payment " +
			" INNER JOIN C_BPARTNER b ON(C_Payment.C_BPARTNER_ID = b.C_BPARTNER_ID) " +
			" INNER JOIN C_DocType d ON(C_Payment.C_DocType_ID = d.C_DocType_ID) " +
			getSqlWhere("C_Payment.ISRECEIPT","C_Payment.C_CURRENCY_ID","C_Payment.DocStatus","C_Payment.ISALLOCATED") +
			") " +
		") ORDER BY CLAVE,FECHA";
		
        PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
        int paramIndex = 1;
        
        if (isSOTrx.booleanValue() == true)
        	pstmt.setString(paramIndex, "Y");
        else
        	pstmt.setString(paramIndex, "N");
        paramIndex++;
        
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
      
		if (toBPartner!=null && fromBPartner!=null)
			sqlWhere+= " AND b.VALUE >= ? AND b.VALUE <= ?";
		else if (toBPartner!=null && fromBPartner==null)
			sqlWhere+= " AND b.VALUE <= ?";
		else if (toBPartner==null && fromBPartner!=null)
			sqlWhere+= " AND b.VALUE >= ?";
		
		sqlWhere+= " AND "+param2+" = ?";
		
		sqlWhere+= " AND "+param3+" IN ('CO','CL')";
		
		sqlWhere+= " AND "+param4+" = 'N'";
    	
		return sqlWhere;
    }

    private int FACTURA = 0;
    private int PAGO = 1;
    
   /** BISion - 08/05/2009 - Daniel Gini
     * Retorna el saldo pendiente para una factura o pago, ingresado por parámetro.
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
            
            /*if (tipo==FACTURA)
            {
            	if (bd.compareTo(BigDecimal.ZERO)<0)
            		return monto.add(bd);
            	else
            		return monto.subtract(bd);
            }
            else
            	if (tipo==PAGO)
            	{	if (bd.compareTo(BigDecimal.ZERO)<0)
                		return monto.add(bd);
                	else
                		return monto.subtract(bd);
            	}*/

            if ((bd.compareTo(BigDecimal.ZERO)<0 && monto.compareTo(BigDecimal.ZERO)>0) || (bd.compareTo(BigDecimal.ZERO)>0 && monto.compareTo(BigDecimal.ZERO)<0))
        		return monto.add(bd);
        	else
        		return monto.subtract(bd);

        }
        
        return monto;
    }
    
}
