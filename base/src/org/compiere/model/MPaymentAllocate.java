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

import java.sql.*;
import java.util.*;
import java.math.*;
import java.util.logging.*;

import javax.swing.JOptionPane;

import org.compiere.util.*;

/**
 * 	Payment Allocate Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MPaymentAllocate.java,v 1.3 2005/10/17 23:41:55 jjanke Exp $
 */
public class MPaymentAllocate extends X_C_PaymentAllocate
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get Payment Allocation of Payment
	 *	@param parent payment
	 *	@return array of allocations
	 */
	public static MPaymentAllocate[] get (MPayment parent)
	{
		ArrayList<MPaymentAllocate> list = new ArrayList<MPaymentAllocate>();
		String sql = "SELECT * FROM C_PaymentAllocate WHERE C_Payment_ID=? AND IsActive='Y'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, parent.getC_Payment_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MPaymentAllocate (parent.getCtx(), rs, parent.get_TrxName()));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		
		MPaymentAllocate[] retValue = new MPaymentAllocate[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	get

	/**	Logger	*/
	private static CLogger log = CLogger.getCLogger (MPaymentAllocate.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_PaymentAllocate_ID id
	 *	@param trxName trx
	 */
	public MPaymentAllocate (Properties ctx, int C_PaymentAllocate_ID, String trxName)
	{
		super (ctx, C_PaymentAllocate_ID, trxName);
		if (C_PaymentAllocate_ID == 0)
		{
		//	setC_Payment_ID (0);	//	Parent
		//	setC_Invoice_ID (0);
			setAmount (Env.ZERO);
			setDiscountAmt (Env.ZERO);
			setOverUnderAmt (Env.ZERO);
			setWriteOffAmt (Env.ZERO);
			setInvoiceAmt(Env.ZERO);
		}	
	}	//	MPaymentAllocate

	/**	The Invoice				*/
	private MInvoice	m_invoice = null;
	
	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MPaymentAllocate (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MPaymentAllocate
	
	/**
	 * 	Set C_Invoice_ID
	 *	@param C_Invoice_ID id
	 */
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		super.setC_Invoice_ID (C_Invoice_ID);
		m_invoice = null;
	}	//	setC_Invoice_ID
	
	/**
	 * 	Get Invoice
	 *	@return invoice
	 */
	public MInvoice getInvoice()
	{
		if (m_invoice == null)
			m_invoice = new MInvoice(getCtx(), getC_Invoice_ID(), get_TrxName());
		return m_invoice;
	}	//	getInvoice
	
	/**
	 * 	Get BPartner of Invoice
	 *	@return bp
	 */
	public int getC_BPartner_ID()
	{
		if (m_invoice == null)
			getInvoice();
		return m_invoice.getC_BPartner_ID();
	}	//	getC_BPartner_ID
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		// REQ-065 - Verificaci�n Monto de Asignaci�n.
		MPayment payment = new MPayment (getCtx(), getC_Payment_ID(), get_TrxName());
		
		if (payment.isReceipt())
		{
			BigDecimal allocated = BigDecimal.ZERO;
			if (newRecord)
				allocated = payment.getAmountAllocate();
			else
			{
				List<MPaymentAllocate> payAll = payment.getAllocate();
				for (int i = 0; i<payAll.size();i++)
				{
					MPaymentAllocate pa = payAll.get(i);
					if (pa.getC_PaymentAllocate_ID()!=getC_PaymentAllocate_ID())
						allocated = allocated.add(pa.getAmount());
				}
			}
			allocated = allocated.add(getAmount());
			if (allocated.compareTo(payment.getPayAmt()) == 1)
			{
				JOptionPane.showMessageDialog(null,"El importe asignado supera el total de valores recibidos","ERROR - Verificaci�n Asignaci�n", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		// REQ-065 - FIN Verificaci�n 
		
		if (payment.getC_Charge_ID() != 0 || payment.getC_Invoice_ID() != 0 || payment.getC_Order_ID() != 0)
		{
			log.saveError("PaymentIsAllocated", "");
			return false;
		}
		//	Check BPartner
		if (payment.getC_BPartner_ID() != 0)
		{
			getInvoice();
			if (m_invoice.getC_BPartner_ID() != payment.getC_BPartner_ID())
			{
				log.saveError("Error", Msg.parseTranslation(getCtx(), 
					"@C_BPartner_ID@ - @C_Payment_ID@ <> @C_Invoice_ID@"));
				return false;
			}
		}
		
		/*VERIFICAR CONSISTENCIA*
		BigDecimal check = getAmount()
			.add(getDiscountAmt())
			.add(getWriteOffAmt())
			.add(getOverUnderAmt());
		if (check.compareTo(getInvoiceAmt()) != 0)
		{
			log.saveError("Error", Msg.parseTranslation(getCtx(), 
				"@InvoiceAmt@(" + getInvoiceAmt()
				+ ") <> @Totals@(" + check + ")"));
			return false;
		}
		*/
		
		/*VERIFICAR QUE EL TOTAL NO SEA MAYOR A AL MONTO DE LA FACTURA*/
		if (getWriteOffAmt().floatValue() < 0)
		{
			JOptionPane.showMessageDialog(null,"El monto asignado supera el Total de la Factura","Sobre Pago", JOptionPane.ERROR_MESSAGE);
			return false;
		}
                
                return true;                

                
	}	//	beforeSave
	
	
	/**	Agregado por DANIEL 15/12/2008
	 * 	
	 *		REQ-034 (Parte 1).
	 *
	 * 		C�lculo de Monto del Pago.
	 */
	/*
	protected boolean afterSave (boolean newRecord, boolean sucess)
	{
		MPayment pay = new MPayment(getCtx(),getC_Payment_ID(),get_TrxName());

		if (pay.isReceipt() && sucess)
		{	
			try {
				
				BigDecimal monto = new BigDecimal(0);
					
				String sql = "SELECT C_Invoice_Id " +
			  			 "FROM C_PaymentAllocate " +
			  			 "WHERE C_Payment_ID = ? AND IsActive = 'Y'";
			
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				pstmt.setLong(1, getC_Payment_ID());
		
				ResultSet rs = pstmt.executeQuery();
			
				if (rs.next()) 
				{
					sql = "SELECT COALESCE(SUM(a.Amount),0) " +
					  "FROM C_Payment p " +
					  "LEFT OUTER JOIN C_PaymentAllocate a ON (p.C_Payment_ID=a.C_Payment_ID) " +
					  "WHERE p.C_Payment_ID = ? AND a.IsActive = 'Y'"; 
			
					pstmt = DB.prepareStatement(sql, null);
					pstmt.setLong(1, getC_Payment_ID());
				
					rs = pstmt.executeQuery();
					
					if (rs.next())
					{
						monto = rs.getBigDecimal(1);
					}
				}
				
                pay.setPayAmt(monto);
                pay.save();

			}
			catch (Exception e){return false;}
		}
		return true;
	}	//	afterSave
	
	protected boolean afterDelete (boolean sucess)
	{
		MPayment pay = new MPayment(getCtx(),getC_Payment_ID(),get_TrxName());

		if (pay.isReceipt() && sucess)
		{	
			try {
				
				BigDecimal monto = new BigDecimal(0);
					
				String sql = "SELECT C_Invoice_Id " +
			  			 "FROM C_PaymentAllocate " +
			  			 "WHERE C_Payment_ID = ? AND IsActive = 'Y'";
			
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				pstmt.setLong(1, getC_Payment_ID());
		
				ResultSet rs = pstmt.executeQuery();
			
				if (rs.next()) 
				{
					sql = "SELECT COALESCE(SUM(a.Amount),0) " +
					  "FROM C_Payment p " +
					  "LEFT OUTER JOIN C_PaymentAllocate a ON (p.C_Payment_ID=a.C_Payment_ID) " +
					  "WHERE p.C_Payment_ID = ? AND a.IsActive = 'Y'"; 
			
					pstmt = DB.prepareStatement(sql, null);
					pstmt.setLong(1, getC_Payment_ID());
				
					rs = pstmt.executeQuery();
					
					if (rs.next())
					{
						monto = rs.getBigDecimal(1);
					}
				}
				
                pay.setPayAmt(monto);
                pay.save();
			}
			catch (Exception e){return false;}
		}
		return true;
	}	//	afterDelete
	*/
	
}	//	MPaymentAllocate
