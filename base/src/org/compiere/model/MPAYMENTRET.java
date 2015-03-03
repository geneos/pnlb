/*
 * MPAYMENTRET.java
 *
 * Created on January 28, 2008, 2:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.compiere.model;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.JOptionPane;

import org.compiere.util.*;
/**
 *
 * @author vit4b
 * 2008-01-28
 */
public class MPAYMENTRET extends X_C_PAYMENTRET {
    
   /**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param PA_Measure_ID id
	 *	@param trxName trx
	 */
	public MPAYMENTRET (Properties ctx, int C_Paymentret_ID, String trxName)
	{
		super (ctx, C_Paymentret_ID, trxName);
	}	
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return succes
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	Actualiza la cobranza
		MPayment pay = new MPayment(Env.getCtx(),getC_Payment_ID(),get_TrxName());
		if (((X_C_Payment)pay).isRetenciones())
			if (success)
			return updatepayment(pay);
		
		return success;
	}	//	afterSave
	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return succes
	 */
	protected boolean afterDelete (boolean success)
	{
		//	Actualiza el pago
		MPayment pay = new MPayment(Env.getCtx(),getC_Payment_ID(),get_TrxName());
		if (success)
			updatepayment(pay);
		
		return success;
	}	//	afterSave
	
     /**
      * 26/11/2010 Camarzana Mariano
      * 
      */
	private void actualizarRetenciones(BigDecimal valor, BigDecimal valorAnterior,String nombreColumna){
		
		/**
		 * 26-11-2010 Camarzana Mariano
		 * Comentado para que no me sume los montos cuando modifico el pago manualmente
		 */
		
		/*String sqlUpdateImporteTotal = "update c_payment " +
		"set payamt = payamt - " + valorAnterior + " + " + valor +
		" where c_payment_id = " + getC_Payment_ID();*/
		
		String sqlUpdateImporteTotal = "update c_payment " +
		"set payamt = 0 " +
		" where c_payment_id = " + getC_Payment_ID();
		
		String sqlUpdateNetoTotal = "update c_payment " +
		"set paynet = 0 " +
		" where c_payment_id = " + getC_Payment_ID();
		
		String sqlUpdateImpuestoTotal = "update c_payment " +
		"set " + nombreColumna + "= " + nombreColumna + " - " + valorAnterior + " + " + valor +
		" where c_payment_id = " + getC_Payment_ID();
		
		String sqlUpdateImpuesto = "update c_paymentret " +
		  "set importe = " + valor +
		  " where c_paymentret_id = " + getC_PAYMENTRET_ID(); 
		
		
		int no = DB.executeUpdate(sqlUpdateImporteTotal,get_TrxName() );
		log.fine("setTranslation #" + no);
		no = DB.executeUpdate(sqlUpdateNetoTotal,get_TrxName() );
		log.fine("setTranslation #" + no);
		no = DB.executeUpdate(sqlUpdateImpuestoTotal,get_TrxName() );
		log.fine("setTranslation #" + no);
		no = DB.executeUpdate(sqlUpdateImpuesto,get_TrxName() );
		log.fine("setTranslation #" + no);
	}
	
	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord new
	 *	@return save
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		String documentno = this.getDocumentNo();
		
        if(documentno == null)
        {
                JOptionPane.showMessageDialog(null,"Ingrese Número de Documento", "Info", JOptionPane.INFORMATION_MESSAGE);
                return false;                
        }
		MPayment payment = new MPayment(getCtx(),getC_Payment_ID(),null);
		
		/*
		 * 26/11/2010 Camarzana Mariano
		 * Comentado para que actualice los montos cuando modifico el pago manualmente
		 */
		if (!((X_C_Payment)payment).isRetenciones())
			{
		        int c_payment_ret = getC_PAYMENTRET_ID(); 
					
				BigDecimal valor = (BigDecimal) get_Value("IMPORTE");
				String sql = "select  c_payment.retencionganancias," +
									  "c_payment.retencionib, " +
									  "c_payment.retencioniva," +
									  "c_payment.retencionsuss," +
									  "c_paymentret.importe, " +
									  "c_paymentret.tipo_ret, " +
									  "c_payment.c_payment_id " +
						     "from c_paymentret " +
						     "join c_payment on (c_payment.c_payment_id = c_paymentret.c_payment_id) " +
						     "where c_paymentret_id = ? ";
				
				try
				{
					PreparedStatement pstmt = DB.prepareStatement(sql, null);
					pstmt.setInt(1, c_payment_ret);
					ResultSet rs = pstmt.executeQuery();
					if (rs.next())
					{
						String tipoRet = rs.getString(6);
						if (tipoRet.equals("B")){
							actualizarRetenciones(valor,rs.getBigDecimal(2),"RetencionIB");
		
						}
						if (tipoRet.equals("G")){
							actualizarRetenciones(valor,rs.getBigDecimal(1),"RetencionGanancias");
						}
						
						if (tipoRet.equals("I")){
							actualizarRetenciones(valor,rs.getBigDecimal(3),"RetencionIVA");
							
						}
						
						if (tipoRet.equals("B")){
							actualizarRetenciones(valor,rs.getBigDecimal(4),"RetencionSUSS");
						}
					}
					
					rs.close();
					pstmt.close();
					pstmt = null;
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, sql, e);
				}
	}     
		return true;
                
                
	}	//	beforeSave        
        
        
	/**
	 * 	Before Delete
	 *	@return true
	 
	protected boolean beforeDelete ()
	{
		try 
		{
             String espago = "";
             PreparedStatement psval = DB.prepareStatement("select ISRECEIPT from C_PAYMENT where C_PAYMENT_ID=?", null);  
             psval.setInt(1, getC_Payment_ID()); 
             ResultSet rsval = psval.executeQuery();  
        
             if (rsval.next())
                espago= rsval.getString(1);
                       
             rsval.close();  
		     psval.close();
                    
             if (espago.equals("N")){
                        
                MPayment pay = new MPayment(Env.getCtx(),getC_Payment_ID(),get_TrxName());
                pay.updatepayment();
                pay.setPAYNET(pay.getPAYNET().subtract(getIMPORTE()));
                pay.save(get_TrxName());
                    	 
                        //String sql = new String("update c_payment set PAYAMT=PAYNET-(select sum(IMPORTE)" +
                    	// 		" from C_PAYMENTRET where C_PAYMENT_ID="+ getC_Payment_ID() + ") " +
                    	// 				"where C_PAYMENT_ID="+ getC_Payment_ID());                            
                        
                        //Statement pstmt = null;
                        //pstmt = DB.createStatement();
                        //pstmt.executeUpdate(sql);
                        //pstmt.close();                         
                        
             }
             else
             {
                String sql = "select PAYAMT from C_Payment where C_PAYMENT_ID= " + getC_Payment_ID();
                try
                {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
	                ResultSet rs = pstmt.executeQuery();
	                BigDecimal payamt = Env.ZERO;
	                if (rs.next())
	                    payamt = rs.getBigDecimal("PAYAMT");

                    BigDecimal val = Env.ZERO;
                    val = this.getIMPORTE();

                    payamt = payamt.subtract(val);
                    
                    
                    String update = "UPDATE C_Payment set PAYAMT = " + payamt + " WHERE C_PAYMENT_ID= " + getC_Payment_ID(); 
                    Statement pstmupdate = null;
                    pstmupdate = DB.createStatement();
                    pstmupdate.executeUpdate(update);
                    pstmupdate.close();

                    rs.close();
                    pstmt.close();
                }
                catch (SQLException e)
                {
                    log.log(Level.SEVERE, sql, e);
                    return false;
                }

             }
             return true;                        
        }
        catch (Exception exc) {System.out.println(exc.toString());};
        return false;  
	}	//	beforeDelete        
	 */
	
	/**
	 * 	Update updatepayment
	 * 	@return true if updated
	 */
	private boolean updatepayment(MPayment payment)
	{
		try	{
			if (!payment.isReceipt()){            	 
	             List<PO> reten = payment.getRetenciones();
	             List<MVALORPAGO> valor = payment.getValoresPago();
	             BigDecimal neto = BigDecimal.ZERO;
	             BigDecimal amt = payment.getPayAmt();
	             for (int i=0; i<reten.size(); i++)
	            	 amt = amt.subtract(((MPAYMENTRET)reten.get(i)).getIMPORTE());
	             for (int i=0; i<valor.size(); i++)
	            	 neto = neto.add((valor.get(i)).getIMPORTE());
	             
	             payment.setPAYNET(amt);
	             payment.setRetencion(false);
	             payment.save(get_TrxName());
	         }
	         else
	         {
	             BigDecimal payamt = payment.getPayAmt().add(this.getIMPORTE());
	             payment.setPayAmt(payamt);
	             payment.save(get_TrxName());
	         }
		}
		catch (Exception e)	{
			e.printStackTrace();
			return false;
		}
     return true;                        
	}	//	updatepayment
    
}
