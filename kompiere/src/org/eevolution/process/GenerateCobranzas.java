/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;
import java.sql.SQLException;
import org.compiere.process.*;
import java.math.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.*;
import org.compiere.model.*;

import org.compiere.util.*;
import org.eevolution.tools.Numero2Letras;
import org.eevolution.tools.UtilProcess;
/**
 * Esta clase inserta tuplas en las tablas temporales T_COBRANZA_RETENCIONES,T_COBRANZA_LISTADO,T_COBRANZA_CABECERA luego de un previo filtrado por pago 
 *  y calculos posteriores.
 * @author Bision
 */
public class GenerateCobranzas extends SvrProcess{
    
    int p_instance;
    BigDecimal totalfacturas;
    
    protected void prepare() {
        p_instance = getAD_PInstance_ID();   
    }

    
    protected String doIt(){
        facturas();
        retenciones();
        valores();
        cabecera();
        UtilProcess.initViewer("PF_COBRANZA",p_instance,getProcessInfo());
        return "success";
    }
         
    protected void facturas(){
    try{
        // para la parte de la tabla que contiene las facturas
        totalfacturas = BigDecimal.ZERO;
        
        String sqlQuery = "",sqlInsert = "";
        PreparedStatement pstmtInsert = null;
        String sqlRemove = "delete from T_COBRANZA_LISTADO";
        
        DB.executeUpdate(sqlRemove, null);
        int i=0;
        Long client=new Long(0),org=new Long(0);            
        sqlQuery = "select distinct fac.c_invoice_id,cd.name,fac.DOCUMENTNO,fac.dateinvoiced,pay.amount,pay.ad_client_id,pay.ad_org_id,cd.docbasetype from C_PaymentAllocate pay,c_invoice fac,c_doctype cd where pay.c_invoice_id=fac.c_invoice_id and cd.c_doctype_id=fac.c_doctype_id and pay.C_Payment_ID=" + getRecord_ID()+ " order by fac.dateinvoiced";
        PreparedStatement pstmt = DB.prepareStatement(sqlQuery,get_TrxName());
        ResultSet rs = pstmt.executeQuery();
        try {
            while (rs.next()) {
                i+=1;
                sqlInsert = "insert into T_COBRANZA_LISTADO values(?,?,'Y',?,?,?,?,?,"+i+")";
                pstmtInsert = DB.prepareStatement(sqlInsert,get_TrxName());
                pstmtInsert.setLong(1, rs.getLong(6));
                pstmtInsert.setLong(2, rs.getLong(7));
                pstmtInsert.setInt(3, p_instance);
                pstmtInsert.setDate(4, rs.getDate(4));
                pstmtInsert.setString(5, rs.getString(2));
                pstmtInsert.setString(6, rs.getString(3));
                if (rs.getString(8).equals(MDocType.DOCBASETYPE_APCreditMemo) || rs.getString(8).equals(MDocType.DOCBASETYPE_APCreditMemo)){
                    totalfacturas= totalfacturas.subtract(rs.getBigDecimal(5));
                    pstmtInsert.setBigDecimal(7, rs.getBigDecimal(5).negate());
                }
                else{
                    pstmtInsert.setBigDecimal(7, rs.getBigDecimal(5));
                    totalfacturas= totalfacturas.add(rs.getBigDecimal(5));    
                }                    
                pstmtInsert.executeQuery();
                DB.commit(true, get_TrxName());
                client=rs.getLong(6);org=rs.getLong(7);                    
            }
            // para la fila del total
            if(i>0){
                i+=1;
                sqlInsert = "insert into T_COBRANZA_LISTADO values("+client.toString()+","+org.toString()+",'Y',"+p_instance+",null,'Total',null,"+totalfacturas+","+i+")";
                pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
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
            
    protected void retenciones(){
        try{
        	// pala la parte de LAS RETENCIONES
        	String sqlRemove = "delete from T_COBRANZA_RETENCIONES";
            DB.executeUpdate(sqlRemove, null);
            BigDecimal totalreten= BigDecimal.ZERO;
            
            String sqlInsert="";
            int j=0;
            Long client=new Long(0),org=new Long(0);
            
            String sqlQuery = "SELECT ret.AD_CLIENT_ID,ret.AD_ORG_ID,ret.DATETRX,reg.NAME,ret.DOCUMENTNO,ret.IMPORTE " +
            				  "FROM C_Cobranza_Ret ret, C_REGRETEN_RECIB reg " +
            				  "WHERE ret.C_REGRETEN_RECIB_ID = reg.C_REGRETEN_RECIB_ID and ret.c_payment_id=" + getRecord_ID() + " order by ret.DATETRX";
            PreparedStatement pstmt = DB.prepareStatement(sqlQuery,get_TrxName());
            ResultSet rs = pstmt.executeQuery();
            try {
                while (rs.next()) {
                	j+=1;
                	sqlInsert = "insert into T_COBRANZA_RETENCIONES values(?,?,'Y',?,?,?,?,?,"+j+")";
                    PreparedStatement pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
                    pstmtInsert.setLong(1, rs.getLong(1));
                    pstmtInsert.setLong(2, rs.getLong(2));
                    // Luego del IsActive
                    pstmtInsert.setInt(3, p_instance);
                    pstmtInsert.setDate(4, rs.getDate(3));				// FECHA
                    pstmtInsert.setString(5, rs.getString(4));			// TIPO RETENCION
                    pstmtInsert.setString(6, rs.getString(5));			// NRO DOCUMENTO
                    pstmtInsert.setBigDecimal(7, rs.getBigDecimal(6));	// IMPORTE                                                
                
                    totalreten = totalreten.add(rs.getBigDecimal(6));
                    
                    pstmtInsert.executeQuery();
                    DB.commit(true, get_TrxName());
                    client=rs.getLong(1);org=rs.getLong(2);
                }
	            // para la fila del total
	            if(j>0){
	            	 j+=1;
	            	 sqlInsert = "insert into T_COBRANZA_RETENCIONES values("+client.toString()+","+org.toString()+",'Y',"+p_instance+",null,'Total',null," + totalreten+","+(j+1)+")";
	            	 PreparedStatement pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
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
     
    
    protected void valores(){
        try{
            String sqlRemove = "delete from T_COBRANZA_VALORES";
            DB.executeUpdate(sqlRemove, null);
            BigDecimal totalvalores= BigDecimal.ZERO;
            
            String tipo="",sqlInsert="",concepto="";
            Long client=new Long(0),org=new Long(0);            
            
            int j=0;
            String sqlQuery = " SELECT AD_CLIENT_ID,AD_ORG_ID,TIPO,CONCEPTO,IMPORTE,C_BANKACCOUNT_ID," +
            				  "	 	   DEBITODATE,NROTRANSFERENCIA,BANCO,NROCHEQUE,PAYMENTDATE,VENCIMIENTODATE " +
            				  " FROM C_PAYMENTVALORES where c_payment_id=" + getRecord_ID();
            PreparedStatement pstmt = DB.prepareStatement(sqlQuery,get_TrxName());
            ResultSet rs = pstmt.executeQuery();
            try {
                while (rs.next()) {
                	j+=1;
                	concepto = rs.getString(4);
                	if(rs.getString(3).equals(MPAYMENTVALORES.MEXT))
                		tipo="Moneda Extranjera";
                	else 
                    	if(rs.getString(3).equals(MPAYMENTVALORES.BANCO))
                    	{
                    		tipo="Transferencia";
                    		concepto = MBankAccount.get(getCtx(), rs.getInt(6)).getName();
                    		if (rs.getString(8)!=null)
                    			concepto += " - " + rs.getString(8);
                    		if (rs.getString(7)!=null)
                    			concepto += " - " + rs.getString(7);
                    	}
                    	else
                    		if(rs.getString(3).equals(MPAYMENTVALORES.EFECTIVO))
                    			tipo="Efectivo";
                    		else
                    			if(rs.getString(3).equals(MPAYMENTVALORES.CHEQUE))
                    			{
                    				tipo="Cheque";
                    				sqlQuery = " SELECT AD_REFERENCE_ID " +
                    						   " FROM AD_REFERENCE where NAME = 'Bancos'";
                    				PreparedStatement ps = DB.prepareStatement(sqlQuery,get_TrxName());
                    				ResultSet rSet = ps.executeQuery();
                    				if (rSet.next())
                    					concepto = MRefList.getListName(getCtx(),rSet.getInt(1),rs.getString(9)) + 
                    					           " - " + rs.getString(10) + " - " + rs.getDate(11).toString();	
                    			}
                	
                	sqlInsert = "Insert into T_COBRANZA_VALORES values(?,?,'Y',?,?,?,?,"+j+")";
                	PreparedStatement pstmtInsert = DB.prepareStatement(sqlInsert,get_TrxName());

					pstmtInsert.setLong(1, rs.getLong(1));
                    pstmtInsert.setLong(2, rs.getLong(2));
                    // Luego del IsActive
                    pstmtInsert.setInt(3, p_instance);
                    pstmtInsert.setString(4, tipo);

                    pstmtInsert.setString(5, concepto);
                    pstmtInsert.setBigDecimal(6, rs.getBigDecimal(5));
                    
                    totalvalores = totalvalores.add(rs.getBigDecimal(5));
                    
                    pstmtInsert.executeQuery();
                    DB.commit(true, get_TrxName());
                    client=rs.getLong(1);org=rs.getLong(2);
                }
                // para la fila del total
                if(j>0){
	            	 j+=1;
	            	 sqlInsert = "insert into T_COBRANZA_VALORES values("+client.toString()+","+org.toString()+",'Y',"+p_instance+",null,'Total',"+totalvalores+","+j+")";
	            	 PreparedStatement pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
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
    
    protected void cabecera(){
        try {
            String sqlQuery = "",sqlInsert = "",importe="";
            PreparedStatement pstmtInsert = null;
            String sqlRemove = "delete from T_COBRANZA_CABECERA";
            DB.executeUpdate(sqlRemove, null);
            Numero2Letras conver = new Numero2Letras();
            sqlQuery = "select AD_CLIENT_ID,AD_ORG_ID,C_PAYMENT_ID,NOMBRE,NRO,FECHA,CUIT,IIBB,DIR,CPCUI,PAIS,IDPROV,PROVEEDOR,CUITPROV,DIRPROV,CPCUIPROV,PAISPR,LEYENDA1,LEYENDA2,LEYENDA3,PAGONETO from RV_PAGO_CABECERA" + " where c_payment_id=" + getRecord_ID();
            PreparedStatement pstmt = DB.prepareStatement(sqlQuery,get_TrxName());
            ResultSet rs = pstmt.executeQuery();
            try {
                if (rs.next()) {
                    sqlInsert = "insert into T_COBRANZA_CABECERA values(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                    //para el encabezado
                    String encabezado = rs.getString(18);
                    encabezado = encabezado.substring(9);
                    pstmtInsert.setString(19, "Recibimos de"+encabezado);
                    //para calculo del importe
                    double aux = rs.getDouble(21) - (int)rs.getDouble(21);
                    aux = aux * 100;
                    if(rs.getDouble(21) == 0)
                        importe=" de "+ rs.getString(19) + " cero en concepto";
                    else{
                        if((int)aux==0)
                            importe=" de "+ rs.getString(19) + " "+ conver.convertirLetras((int)rs.getDouble(21)) + " en concepto";
                        else
                            importe=" de "+ rs.getString(19) + " " +conver.convertirLetras((int)rs.getDouble(21)) + " con "+ conver.convertirLetras((int)aux)+ " centavos en concepto";
                    }
                    pstmtInsert.setString(20, importe);
                    pstmtInsert.setString(21, rs.getString(20));
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
}
