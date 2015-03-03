/*
 * MPAYMENTRET.java
 *
 * Created on January 28, 2008, 2:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.compiere.model;

import java.sql.ResultSet;
import java.util.*;

import org.compiere.util.DB;

/**
 *
 * @author vit4b
 * 2008-01-28
 */
@SuppressWarnings("serial")
public class MINVOICEPERCEP extends X_C_INVOICEPERCEP {
    
   /**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param PA_Measure_ID id
	 *	@param trxName trx
	 */
	
	public MINVOICEPERCEP (Properties ctx, int C_INVOICEPERCEP_ID, String trxName)
	{
		super (ctx, C_INVOICEPERCEP_ID, trxName);
	}	
	
	public MINVOICEPERCEP (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}

	protected boolean afterSave (boolean newRecord, boolean success)
	{
		return (success && updateHeader() == 1);
	}	//	afterSave
	
	protected boolean afterDelete (boolean success)
	{
		return (success && updateHeader() == 1);
	} 	//	afterDelete
	
	protected int updateHeader ()
	{
        /**
         * Agregada la actualizacion de los campos PercepcionIB y PercepcionIVA para el registro de la factura
         * @author Ezequiel Scott @ Zynnia
         */
        
		String sql = "UPDATE C_Invoice i "
				+ "SET GrandTotal=TotalLines + "
				+ "(SELECT COALESCE(SUM(AMOUNT),0) FROM C_InvoicePercep ip WHERE i.C_Invoice_ID=ip.C_Invoice_ID AND ip.IsActive = 'Y' AND C_Invoice_ID=" + getC_Invoice_ID() + ") + "	
				+ "(SELECT COALESCE(SUM(TaxAmt),0) FROM C_InvoiceTax it WHERE i.C_Invoice_ID=it.C_Invoice_ID AND it.IsActive = 'Y' AND C_Invoice_ID=" + getC_Invoice_ID() + ") "
				+ ", percepcionib = (SELECT COALESCE(SUM(AMOUNT),0) FROM C_InvoicePercep WHERE C_Invoice_ID=" + getC_Invoice_ID() + " AND IsActive = 'Y' AND IMPUESTO = 'Ingresos Brutos') " 
                + ", percepcioniva = (SELECT COALESCE(SUM(AMOUNT),0) FROM C_InvoicePercep WHERE C_Invoice_ID=" + getC_Invoice_ID() + " AND IsActive = 'Y' AND IMPUESTO = 'IVA') "
                + "WHERE C_Invoice_ID=" + getC_Invoice_ID();
			
	    int no = DB.executeUpdate(sql, null);
	    if (no != 1)
	    	log.warning("(2) #" + no);
	    
	    return no;
	}	//	afterSave
	
	public static MINVOICEPERCEP copyFrom(Properties ctx, MINVOICEPERCEP from, int C_Invoice_ID, String trxName)
	{
		MINVOICEPERCEP invper = new MINVOICEPERCEP(ctx,0,trxName);
		
		invper.setAD_Client_ID(from.getAD_Client_ID());
		invper.setAD_Org_ID(from.getAD_Org_ID());
		invper.setIsActive(from.isActive());
		invper.setDateTrx(from.getDateTrx());
		invper.setAMOUNT(from.getAMOUNT());
		invper.setC_REGPERCEP_RECIB_ID(from.getC_REGPERCEP_RECIB_ID());
		invper.setAduanera(from.IsAduanera());
		invper.setBancaria(from.IsBancaria());
		invper.setC_Invoice_ID(C_Invoice_ID);
		invper.setIMPUESTO(from.getIMPUESTO());
		
		return invper;
	}
	
}
