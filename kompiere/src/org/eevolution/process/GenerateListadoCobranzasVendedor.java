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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.compiere.model.*;

import org.compiere.util.*;
import org.eevolution.tools.ListadoCobranzasMang;
import org.eevolution.tools.ListadoCobranzasUtil;
import org.eevolution.tools.UtilProcess;

/**
 * Esta clase inserta tuplas en las tablas temporales T_COBRANZA_RETENCIONES,T_COBRANZA_LISTADO,T_COBRANZA_CABECERA luego de un previo filtrado por pago 
 *  y calculos posteriores.
 * @author Bision
 */
public class GenerateListadoCobranzasVendedor extends SvrProcess{
    
    int p_instance;
    BigDecimal totalPesos = BigDecimal.ZERO;
    BigDecimal totalOriginal = BigDecimal.ZERO;
    Timestamp fromDate = null;
    Timestamp toDate = null;
    BigDecimal C_BPartner_ID = null;
    int CC_VENDOR_DETALLE_ID=1000000;
    int CC_VENDOR_ID=1000000;
    int org = 0;
    
    ListadoCobranzasMang manager = null;
    
    protected void prepare() {
        p_instance = getAD_PInstance_ID(); 
        ProcessInfoParameter[] para = getParameter();
        
        for (int i = 0; i < para.length; i++)
    	{
                String name = para[i].getParameterName();
                if(name.equals("COBRADOR"))
                	C_BPartner_ID= (BigDecimal) para[i].getParameter();
                else{
                    fromDate=(Timestamp)para[i].getParameter();
                    toDate=(Timestamp)para[i].getParameter_To();
                }
         }
        
        manager = new ListadoCobranzasMang();
    }

    
   private String getNombreVendedor(int vendedor){ 
	   try {
		   	PreparedStatement pstmt = null;
		    String sql = " SELECT name" +
		    			 " FROM ad_user" +
		    			 " WHERE ad_user_id = "+ vendedor;
		    pstmt = DB.prepareStatement(sql,null);
		    ResultSet rs = pstmt.executeQuery();
		    if (rs.next())
		    	return rs.getString(1);
	   		}
	   catch(Exception e)	{
   }
   return null;
   }
   
   
    protected String doIt() {
        
    	String sqlRemove = "delete from T_LISTADOCOBRANZASVEN";
    	DB.executeUpdate(sqlRemove, null);
    	sqlRemove = "delete from T_SUBTOTALESCOBRANZASVEN";
    	DB.executeUpdate(sqlRemove, null);
    	sqlRemove = "delete from T_CABECERACOBRANZASVEN";
    	DB.executeUpdate(sqlRemove, null);
    	long indice = 0;
    	
    		
    	
    	int cobrador = 0;
        indice=1000000;
        CC_VENDOR_ID=1000000;
    	List<MPayment> cobranzas = getComprobantes();
    	for (int i = 0; i<cobranzas.size(); i++)    		
    	{	
    		if (cobranzas.get(i).getCobrador()!= cobrador){
    			if (i>0)
    				{
    				 insertarSubtotales(CC_VENDOR_ID);
        			 manager = new ListadoCobranzasMang();
    				}
    			CC_VENDOR_ID = CC_VENDOR_ID+1;
    			cobrador = cobranzas.get(i).getCobrador();
    			cabecera(cobrador,getNombreVendedor(cobrador),CC_VENDOR_ID);
    		}
    		indice = addCobranza(cobranzas.get(i),indice,CC_VENDOR_ID);
    		try {
				DB.commit(false, null);
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    	insertarSubtotales(CC_VENDOR_ID);
    	try {
	    	String sqlInsert = "Insert into T_LISTADOCOBRANZASVEN values(?,?,'Y',?,?,?,?,?,?,?,?,?,'','',?)";
			PreparedStatement pstmtInsert = DB.prepareStatement(sqlInsert,null);
	        pstmtInsert.setLong(1, getAD_Client_ID());
	        pstmtInsert.setLong(2, 0);
	        pstmtInsert.setInt(3, p_instance);
	        pstmtInsert.setTimestamp(4, null);
	        pstmtInsert.setTimestamp(5, fromDate);
	        pstmtInsert.setString(6, null);
	       	pstmtInsert.setString(7, "TOTAL GENERAL");
	       	pstmtInsert.setBigDecimal(8, totalOriginal);
	       	pstmtInsert.setBigDecimal(9, totalPesos);
	        pstmtInsert.setLong(10, indice);
	        
	        pstmtInsert.setInt(12, CC_VENDOR_ID);
	        if (C_BPartner_ID==null)
	        	pstmtInsert.setInt(11, 0);
	        else
	        	pstmtInsert.setInt(11, C_BPartner_ID.intValue());
	        
	        indice++;
	        pstmtInsert.execute();
    	}catch(Exception e)	{
        }
    	
    	//insertarSubtotales();
    	
    	try {
			DB.commit(false, null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	UtilProcess.initViewer("Listado de Cobranzas Vendedor",p_instance,getProcessInfo());
        return "success";
    }
         
    private List<MPayment> getComprobantes() {
    	List<MPayment> cobranzas = new ArrayList<MPayment>();
        try {
        	PreparedStatement pstmt = null;
        	String sqlQuery = "SELECT ad_user.name,ad_user.ad_user_id,c_payment.C_Payment_ID FROM C_Payment" +
        			" JOIN ad_user ON (C_Payment.cobrador = ad_user.ad_user_id)" +
        			" WHERE c_payment.IsActive = 'Y' and c_payment.isReceipt='Y' and docStatus in ('CO','CL')";
        			
	    	
        	if (fromDate!=null && toDate!=null && C_BPartner_ID!=null)
	    	{
	    		sqlQuery += "and dateacct between ? and ? and ad_user.ad_user_id = ?" +
	    					" Order By ad_user.name,C_Payment.dateacct";
	    		pstmt = DB.prepareStatement(sqlQuery,null);
	    		pstmt.setTimestamp(1, fromDate);
	    		pstmt.setTimestamp(2, toDate);
	    		pstmt.setInt(3, C_BPartner_ID.intValue());
	    	}
	    	if (fromDate!=null && toDate!=null && C_BPartner_ID==null)
	    	{
	    		sqlQuery += "and dateacct between ? and ? " +
	    					" Order By ad_user.name,C_Payment.dateacct";
	    		pstmt = DB.prepareStatement(sqlQuery,null);
	    		pstmt.setTimestamp(1, fromDate);
	    		pstmt.setTimestamp(2, toDate);
	    	}
	    	if (fromDate!=null && toDate==null && C_BPartner_ID==null)
	    	{
	    		sqlQuery += "and dateacct >= ? Order By ad_user.name,C_Payment.dateacct";
	    		pstmt = DB.prepareStatement(sqlQuery,null);
	    		pstmt.setTimestamp(1, fromDate);
	    	}
	    	if (fromDate!=null && toDate==null && C_BPartner_ID!=null)
	    	{
	    		sqlQuery += "and dateacct >= ? and ad_user.ad_user_id = ? Order By dateacct";
	    		pstmt = DB.prepareStatement(sqlQuery,null);
	    		pstmt.setTimestamp(1, fromDate);
	    		pstmt.setInt(2, C_BPartner_ID.intValue());
	    	}
	    	if (fromDate==null && toDate!=null && C_BPartner_ID==null)
	    	{
	    		sqlQuery += "and dateacct <= ? Order By ad_user.name,C_Payment.dateacct";
	    		pstmt = DB.prepareStatement(sqlQuery,null);
	    		pstmt.setTimestamp(1, toDate);
	    	}
	    	if (fromDate!=null && toDate==null && C_BPartner_ID!=null)
	    	{
	    		sqlQuery += "and dateacct <= ? and ad_user.ad_user_id = ? Order By dateacct";
	    		pstmt = DB.prepareStatement(sqlQuery,null);
	    		pstmt.setTimestamp(1, toDate);
	    		pstmt.setInt(2, C_BPartner_ID.intValue());
	    	}
	    	if (fromDate==null && toDate==null && C_BPartner_ID!=null)
	    	{
	    		sqlQuery += "and ad_user.ad_user_id = ? Order By dateacct";
	    		pstmt = DB.prepareStatement(sqlQuery,null);
	    		pstmt.setInt(1, C_BPartner_ID.intValue());
	    	}
	    	if (fromDate==null && toDate==null && C_BPartner_ID==null)
	    	{
	    		sqlQuery += "Order By ad_user.name,C_Payment.dateacct";
	    		pstmt = DB.prepareStatement(sqlQuery,null);
	    	}
	    		
        	ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                MPayment cobranza = new MPayment(Env.getCtx(),rs.getInt(3),null);
            	cobranzas.add(cobranza);
            }
        }
        catch(Exception e)	{
        }
        
		return cobranzas;
	}
    
	private long addCobranza(MPayment cobranza, long indice,int CC_VENDOR_ID)	{
		
		try{
			
			long cabecera = indice;
			indice++;
			
			BigDecimal pesos = BigDecimal.ZERO;
			BigDecimal original = BigDecimal.ZERO;
			
			List<MPAYMENTVALORES> valores = cobranza.getValoresCobranza();
			List<PO> retenciones = cobranza.getRetenciones();
			
			if (valores !=null)
		        for (int i = 0; i<valores.size(); i++)
				{
		        	String sqlInsert = "Insert into T_LISTADOCOBRANZASVEN values(?,?,'Y',?,?,?,?,?,?,?,?,?,'','',?)";
		        	PreparedStatement pstmtInsert = DB.prepareStatement(sqlInsert,null);
					pstmtInsert.setLong(1, cobranza.getAD_Client_ID());
			        pstmtInsert.setLong(2, cobranza.getAD_Org_ID());
			        pstmtInsert.setInt(3, p_instance);
			        pstmtInsert.setTimestamp(4, null);
			        pstmtInsert.setTimestamp(5, fromDate);
			        pstmtInsert.setString(6, null);
	            	pstmtInsert.setString(7, getDescripcion(valores.get(i),cobranza));
	            	pstmtInsert.setBigDecimal(8, valores.get(i).getIMPORTE());
	            	pstmtInsert.setBigDecimal(9, valores.get(i).getIMPORTE().multiply(cobranza.getCotizacion()));
	            	pstmtInsert.setLong(10, indice);
	            	pstmtInsert.setLong(11, cobranza.getC_BPartner_ID());
	            	
	            	pstmtInsert.setInt(12, CC_VENDOR_ID);
	            		
	            	
	            	indice++;
	    	        pstmtInsert.execute();
	    	        pesos = pesos.add(valores.get(i).getIMPORTE().multiply(cobranza.getCotizacion()));
	            	original = original.add(valores.get(i).getIMPORTE());
				}
			
			if (retenciones !=null)
				for (int i = 0; i<retenciones.size(); i++)
				{
					String sqlInsert = "Insert into T_LISTADOCOBRANZASVEN values(?,?,'Y',?,?,?,?,?,?,?,?,?,'','',?)";
					PreparedStatement pstmtInsert = DB.prepareStatement(sqlInsert,null);
					pstmtInsert.setLong(1, cobranza.getAD_Client_ID());
			        pstmtInsert.setLong(2, cobranza.getAD_Org_ID());
			        pstmtInsert.setInt(3, p_instance);
			        pstmtInsert.setTimestamp(4, null);
			        pstmtInsert.setTimestamp(5, fromDate);
			        pstmtInsert.setString(6, null);
			        MCOBRANZARET reten = (MCOBRANZARET)retenciones.get(i);
	        		MREGRETENRECIB reg = new MREGRETENRECIB(Env.getCtx(),reten.getC_RegRetenRecib_ID(),null);
	            	pstmtInsert.setString(7, reg.getNAME() + "_" + reten.getDocumentNo());
	            	pstmtInsert.setBigDecimal(8, reten.getImporte());
	            	pstmtInsert.setBigDecimal(9, reten.getImporte().multiply(cobranza.getCotizacion()));
	            	pstmtInsert.setLong(10, indice);
	            	pstmtInsert.setLong(11, cobranza.getC_BPartner_ID());
	            	pstmtInsert.setInt(12, CC_VENDOR_ID);

	    	        indice++;
	    	        pstmtInsert.execute();
	    	        pesos = pesos.add(reten.getImporte().multiply(cobranza.getCotizacion()));
	            	original = original.add(reten.getImporte());
	            	manager.add(ListadoCobranzasMang.RETENCION, reg.getNAME(), reten.getImporte().multiply(cobranza.getCotizacion()));
				}
			
			String sqlInsert = "Insert into T_LISTADOCOBRANZASVEN values(?,?,'Y',?,?,?,?,?,?,?,?,?,'RC',?,?)";
			PreparedStatement pstmtInsert = DB.prepareStatement(sqlInsert,null);
	        pstmtInsert.setLong(1, cobranza.getAD_Client_ID());
	        pstmtInsert.setLong(2, cobranza.getAD_Org_ID());
	        pstmtInsert.setInt(3, p_instance);
	        pstmtInsert.setTimestamp(4, cobranza.getDateAcct());
	        pstmtInsert.setTimestamp(5, fromDate);
	        MBPartner bpartner = new MBPartner(Env.getCtx(),cobranza.getC_BPartner_ID(),null);
	        pstmtInsert.setString(6, bpartner.getName());
	        pstmtInsert.setString(7, null);
        	pstmtInsert.setBigDecimal(8, original);
        	pstmtInsert.setBigDecimal(9, pesos);
	        pstmtInsert.setLong(10, cabecera);
	        pstmtInsert.setLong(11, cobranza.getC_BPartner_ID());
	        pstmtInsert.setString(12, cobranza.getDocumentNo());
	        
	        pstmtInsert.setInt(13, CC_VENDOR_ID);

	        
	        pstmtInsert.execute();
	        
	        totalPesos = totalPesos.add(pesos);
	        totalOriginal = totalOriginal.add(original);
	        
	        
		}
		catch (Exception e)	{
		}
		
		return indice;
	}
	
	private void insertarSubtotales(int CC_VENDOR_ID)	{
		
		try	{
        
	        List totales = manager.getAll();
	        for (int i=0; i<totales.size();i++)
	        {
	        	ListadoCobranzasUtil elem = (ListadoCobranzasUtil)totales.get(i);
	        	String sqlInsert = "Insert into T_SUBTOTALESCOBRANZASVEN values(?,?,'Y',?,?,?,?,?,?)";
	        	PreparedStatement pstmtInsert = DB.prepareStatement(sqlInsert,null);
	        	pstmtInsert.setLong(1, getAD_Client_ID());
	            pstmtInsert.setLong(2, org);
	            pstmtInsert.setInt(3, p_instance);
	            pstmtInsert.setString(4, elem.getTipo());
	            pstmtInsert.setString(5, elem.getDescripcion());
	        	pstmtInsert.setBigDecimal(6, elem.getMonto());
	        	pstmtInsert.setInt(7, i);
	        	pstmtInsert.setInt(8, CC_VENDOR_ID);
	        	pstmtInsert.execute();
	        }
        }
		catch (Exception e)	{
		}
	}
	
	private void cabecera(int vendedor,String name,int CC_VENDOR_ID)	{
		
		try	{
        
        	String sqlInsert = "Insert into T_CABECERACOBRANZASVEN values(?,?,'Y',?,?,?,?,?)";
        	PreparedStatement pstmtInsert = DB.prepareStatement(sqlInsert,null);
        	pstmtInsert.setLong(1, getAD_Client_ID());
	        pstmtInsert.setLong(2, org);
	        pstmtInsert.setInt(3, p_instance);
	        pstmtInsert.setTimestamp(4, fromDate);
	        pstmtInsert.setInt(7, CC_VENDOR_ID);
	        CC_VENDOR_ID++;
	        if (vendedor!=0){
	        	pstmtInsert.setInt(5, vendedor);
	        	pstmtInsert.setString(6, name);
	        }
	        else
	        	pstmtInsert.setInt(5, 0);
	        pstmtInsert.execute();
	        
        }
		catch (Exception e)	{
		}
	}
	
	private String getDescripcion(MPAYMENTVALORES valor, MPayment cobranza)
	{
		if (valor.getTIPO().equals(MPAYMENTVALORES.CHEQUE))
		{
			manager.add(MPAYMENTVALORES.CHEQUE, null, valor.getIMPORTE().multiply(cobranza.getCotizacion()));			
			if (valor.getTipoCheque().equals("C"))
				return "CHPC_" + valor.getNroCheque() + "_" + getFecha(valor.getPaymentDate());
			if (valor.getTipoCheque().equals("D"))
				return "CHPD_" + valor.getNroCheque() + "_" + getFecha(valor.getPaymentDate());
		}
		if (valor.getTIPO().equals(MPAYMENTVALORES.BANCO))
		{
			MBankAccount bank = MBankAccount.get(Env.getCtx(), valor.getC_BankAccount_ID());
			manager.add(MPAYMENTVALORES.BANCO, bank.getName(), valor.getIMPORTE().multiply(cobranza.getCotizacion()));
			return bank.getName() + "_" + valor.getNroTransferencia();
		}
		if (valor.getTIPO().equals(MPAYMENTVALORES.EFECTIVO))
		{	
			manager.add(MPAYMENTVALORES.EFECTIVO, null, valor.getIMPORTE().multiply(cobranza.getCotizacion()));
			return "Efectivo";
		}
		if (valor.getTIPO().equals(MPAYMENTVALORES.MEXT))
		{	
			manager.add(MPAYMENTVALORES.MEXT, MCurrency.getISO_Code(Env.getCtx(), valor.getMoneda()), valor.getIMPORTE().multiply(cobranza.getCotizacion()));
			return "Moneda: " + MCurrency.getISO_Code(Env.getCtx(), valor.getMoneda());
		}
		
		return null;
	}
	
	@SuppressWarnings("deprecation")
	protected String getFecha(Timestamp date){
        String fecha = "";
        if (date!=null)
        {	
        	if(date.getDate()<10) fecha = "0"+date.getDate(); else fecha += date.getDate();
        	if(date.getMonth()+1 < 10)  fecha+= "/0"+(date.getMonth()+1); else fecha+= "/"+(date.getMonth()+1);
        	fecha +="/"+(date.getYear()+1900);
        }
        return fecha;
    }
	
}
