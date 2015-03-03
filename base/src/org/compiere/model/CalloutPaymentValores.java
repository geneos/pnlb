/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.NamePair;

/**
 * 	Callout for Allocate Payments
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutPaymentAllocate.java,v 1.2 2005/11/06 01:17:27 jjanke Exp $
 */
public class CalloutPaymentValores extends CalloutEngine
{
	/**
	 *  30/12/2008 - Daniel Gini
	 *  
	 *  	Si es un cheque común las fechas deben ser iguales
	 *  
	 */
	private String COMUN = "C";
	
	public String dates (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue)
	{
		if (isCalloutActive())		//	assuming it is resetting value
			return "";
		setCalloutActive(true);
		
		//	Get Info from Tab
		String Tipo = (String)mTab.getValue("TIPOCHEQUE");
		Timestamp FPago = (Timestamp)mTab.getValue("PAYMENTDATE");
		Timestamp FEmision = (Timestamp)mTab.getValue("REALEASEDDATE");
		
		if ((Tipo!=null) && (FPago!=null || FEmision!=null))
		{
			if (Tipo.equals(COMUN))
			{
				String colName = mField.getColumnName();
				if (colName.equals("PAYMENTDATE"))
					mTab.setValue("REALEASEDDATE", FPago);
				else    //  fecha de emisión
					mTab.setValue("PAYMENTDATE", FEmision);
			}
		}
		setCalloutActive(false);
		return "";
	}	//	dates
	
	/** BISion - 13/07/2009 - Daniel Gini
	 * 
     * Metodo creado para no incluir en el combo de Seleccion de Monedas,
     * la moneda con la que se trabaja en el esquema contable actual. 
     */
    public String loadCurrency(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue){
		
    	if (isCalloutActive())		//	assuming it is resetting value
			return "";
		setCalloutActive(true);
	  /*
		if (value!=null && value.equals(MPAYMENTVALORES.MEXT))
        {	// MONEDA EXTRANJERA
			
			//MField fieldCurr = mTab.getField("C_Currency_ID");
	        MLookup mlookup = (MLookup) mTab.getField("C_Currency_ID").getLookup();
	        
	        int C_AcctSchema_ID = Env.getContextAsInt(Env.getCtx(), WindowNo, "$C_AcctSchema_ID");
	        int C_Payment_ID = (Integer)mTab.getValue("C_Payment_ID");
	        MAcctSchema acctSchema = new MAcctSchema(Env.getCtx(),C_AcctSchema_ID,null);
	        MPayment payment = new MPayment(Env.getCtx(),C_Payment_ID,null);
	        
	        MCurrency currAcctS = new MCurrency(Env.getCtx(),acctSchema.getC_Currency_ID(),null);
	        MCurrency currPay = new MCurrency(Env.getCtx(),payment.getC_Currency_ID(),null);
	        
	        NamePair knp;
	        if (currPay.equals(currAcctS))
	        {
	        	knp = new KeyNamePair(currAcctS.getC_Currency_ID(),currAcctS.getISO_Code());
	        	if (mlookup.getIndexOf(knp)>=0)
	        		mlookup.removeElementAt(mlookup.getIndexOf(knp));	
	        }
	        else
	        {
	        	mlookup.removeAllElements();
	        	knp = new KeyNamePair(currPay.getC_Currency_ID(),currPay.getISO_Code());
	        	mlookup.addElement(knp);
	        }
	        
	        mlookup.refresh();
		}
	    */
		setCalloutActive(false);
		return "";
    }
	
        /** BISion - 12/01/2009 - Santiago Ibañez
         * Metodo creado para resetear los campos que no son propios al tipo
         * seteado.
         */
        public String resetFields(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue){
    		
        	if (isCalloutActive())		//	assuming it is resetting value
    			return "";
    		setCalloutActive(true);
    		
        	//Resetea los campos siempre que se cambie el tipo
            
            // TODOS LOS TIPOS
            //Reseteo la fecha de debito
            mTab.setValue("DEBITODATE", null);
            //Reseteo el importe
            mTab.setValue("IMPORTE", BigDecimal.ZERO);
            //Reseteo el importe en moneda original
            mTab.setValue("CONVERTIDO", BigDecimal.ZERO); 
            //Reseteo el importe en moneda extranjera
            mTab.setValue("EXTRANJERA", BigDecimal.ZERO); 
            //Reseteo el Banco
            mTab.setValue("BANCO", null);
            //Reseteo el concepto
            mTab.setValue("CONCEPTO", null);
            //Reseteo la Cuenta Bancaria
            mTab.setValue("C_BankAccount_ID", null); 
            //Reseteo la Moneda
            mTab.setValue("C_Currency_ID", null);
            //Reseteo los Días de Clearing
            mTab.setValue("CLEARING", null);
            //Reseteo el CUIT del Firmante
          //  mTab.setValue("CUITFIRM", null);
            //Reseteo el campo es de Terceros
            mTab.setValue("TERCEROS", null);
            //Reseteo la Fecha de Pago
            mTab.setValue("PAYMENTDATE", null);
            
         //   if (value!=null && ((String)value).equals("Q"))
          //  {
		        //Seteo el tipo de cheque por defecto
		        MTable mTable = new MTable(ctx,mTab.getAD_Tab_ID(),mTab.getTableName(),WindowNo,mTab.getTabNo(),true);
		        
		        try
		        {
		        	MField field = mTable.getField("TIPOCHEQUE");
		        	PreparedStatement pstm = DB.prepareStatement(field.getDefaultValue(),null);
		        	ResultSet rs = pstm.executeQuery();
		        
		        	if (rs.next())
		        		mTab.setValue("TIPOCHEQUE", rs.getString(1));	
		        	else	
		        		mTab.setValue("TIPOCHEQUE", null);
		        	
		        	pstm.close();
		        	rs.close();
		        	
		        	field = mTable.getField("REALEASEDDATE");
		        	pstm = DB.prepareStatement(field.getDefaultValue(),null);
		        	rs = pstm.executeQuery();
		        
		        	if (rs.next())
		        		mTab.setValue("REALEASEDDATE", rs.getDate(1));	
		        	else	
		        		mTab.setValue("REALEASEDDATE", null);
		
		        	pstm.close();
		        	rs.close();
		        }
		        catch (Exception e){}
		/*    }
            else
            {
            	//Reseteo el Fecha de Emisi�n
                mTab.setValue("REALEASEDDATE", null);
                //Reseteo el Tipo de Cheque
                mTab.setValue("TIPOCHEQUE", null);
            }
        */    
            if (value!=null)
            {	//CHEQUES		//TODO PASAR A CONSTANTE
            	if (value.equals("Q")){
                mTab.setValue("STATE", "C");
                mTab.setValue("NROCHEQUE","00000000");
                /*
                try
                {	
                	int C_Payment_ID = Env.getContextAsInt(ctx, WindowNo, "C_Payment_ID");
	                int C_BPartner_ID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID");
	                
                	String SQL= "select b.TaxId " +
                			"from C_BPartner b, C_Payment p " +
                			"WHERE b.C_BPartner_Id=p.C_BPartner_Id and p.C_Payment_Id = " + 
                			C_Payment_ID +" and b.C_BPartner_ID = " + C_BPartner_ID;
                
	                PreparedStatement pstm = DB.prepareStatement(SQL,null);
	                ResultSet rs = pstm.executeQuery();
		        
		        	if (rs.next())
		        		mTab.setValue("CUITFIRM", rs.getString(1));
                }
                catch (Exception e)	{}
                */
            	}
            	else
            		mTab.setValue("STATE", null);
            	//      TRANSFERENCIAS BANCARIAS
            	if (value.equals("B"))		//TODO PASAR A CONSTANTE
            		mTab.setValue("NROTRANSFERENCIA", "0000000000");
            	//Seteo el numero de transferencia en null
            	else
            		mTab.setValue("NROTRANSFERENCIA", null);
        	}
                
    		setCalloutActive(false);
    		return "";
        }

        /*
         * 	Convierte de Moneda Original a Moneda Nacional
         */
        public String convertirOriginal(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue)
    	{
    		if (isCalloutActive())		//	assuming it is resetting value
    			return "";
    		setCalloutActive(true);
    		
    		//	Get Info from Tab
    		Integer c_payment_id = (Integer)mTab.getValue("C_Payment_ID");
    		String monedaExt = Env.getContext(Env.getCtx(), WindowNo, "FOREINGCURR"); 
    		
    		if (c_payment_id!=null && monedaExt.equals("Y")) 
    		{
    			MPayment pay = new MPayment(Env.getCtx(),c_payment_id.intValue(),null);
    			mTab.setValue("IMPORTE", ((BigDecimal)value).multiply(pay.getCotizacion()));
    		}
    		
    		setCalloutActive(false);
    		return "";
    	}	//	dates
        
        /*
         * 	Convierte de Moneda Extranjera a Moneda Nacional 
         */
        public String convertirExtranjera(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue)
    	{
    		if (isCalloutActive())		//	assuming it is resetting value
    			return "";
    		setCalloutActive(true);

    		//	Get Info from Tab
    		Integer C_Payment_ID = (Integer)mTab.getValue("C_Payment_ID");
    		//Integer C_PaymentValores_ID = (Integer)mTab.getValue("C_PAYMENTVALORES_ID");
    		BigDecimal amount = (BigDecimal)mTab.getValue("EXTRANJERA");
    		Integer C_Currency_ID = (Integer)mTab.getValue("C_Currency_ID");
    		
    		if (C_Payment_ID!=null && C_Currency_ID!=null) 
    		{
    			//MPAYMENTVALORES payval = new MPAYMENTVALORES(Env.getCtx(),C_PaymentValores_ID,null);
    			MPayment pay = new MPayment(Env.getCtx(),C_Payment_ID,null);
    			
    			if (!pay.isForeingCurrency())
    			{	amount = MConversionRate.convert(Env.getCtx(),amount,C_Currency_ID,pay.getC_Currency_ID(),pay.getDateTrx(),pay.getC_ConversionType_ID(), pay.getAD_Client_ID(), pay.getAD_Org_ID());
    				mTab.setValue("IMPORTE", amount);
    			}
    		}
    		
    		setCalloutActive(false);
    		return "";
    	}	//	dates
        
}	//	CalloutPaymentValores
