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
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.swing.JOptionPane;

import org.compiere.util.*;

import java.sql.ResultSet;

/**
 * 	valores de la cobranza
 *	
 *  @author vit4b
 *  @version $Id: MMeasure.java,v 1.3 2008/01/22 $
 */
public class MPAYMENTVALORES extends  X_C_PAYMENTVALORES
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param PA_Measure_ID id
	 *	@param trxName trx
	 */
	
	public MPAYMENTVALORES (Properties ctx, int C_PaymentValores_ID, String trxName)
	{
		super (ctx, C_PaymentValores_ID, trxName);
	}	

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MPAYMENTVALORES (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MActionPermission
	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return succes
	 */
	protected boolean afterDelete (boolean success)
	{
		//	Actualiza la cobranza
		if (success)
		{
			MPayment pay = new MPayment(getCtx(),getC_Payment_ID(),get_TrxName());
			pay.updatepayment();
		}
		
		return success;
	}	//	afterDelete
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return succes
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	Actualiza la cobranza
		if (success)
			if (getC_Payment_ID()!=0 && getC_MOVIMIENTOFONDOS_ID()==0)
			{	
				MPayment pay = new MPayment(getCtx(),getC_Payment_ID(),get_TrxName());
				pay.updatepayment();
			}	
	
		return success;
	}	//	afterSave
	
	/**
	 * 	Modificaci�n REQ-035.
	 *
	 *		Contabilidad en conbranza, ingresar cuenta bancaria.
	 *
	 */
	public static String BANCO = "B";
	public static String MEXT = "M";
	public static String EFECTIVO = "C";
	public static String CHEQUE = "Q";
	
	public static String CARTERA = "C";
	public static String PERCIBIDO = "P";
	public static String DEPOSITADO = "D";
	public static String ENTREGADOPROVEEDOR = "E";
	public static String VENCIDO = "V";
	public static String RECHAZADO = "R";
	public static String DEVUELTOCLIENTE = "L";
	
	private int LEY_DIAS_FA = 30;
	private int LEY_DIAS_FE = 360;
	
	private String COMUN = "C";
	
	protected boolean beforeSave (boolean newRecord)
	{
		MPayment pay = null;
		
		if (getC_Payment_ID()!=0 && getC_MOVIMIENTOFONDOS_ID()==0)
		{	pay = new MPayment(getCtx(),getC_Payment_ID(),get_TrxName());
			MDocType dt = new MDocType(getCtx(),pay.getC_DocType_ID(),get_TrxName());
			
			if (dt.getDocBaseType().equals(MPayment.CERTIFICADO))
			{
				JOptionPane.showMessageDialog(null,"No es posible crear Valores de Cobranza para un Certificado de Retenci�n","Error - Tipo de Documento", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		
			if (pay.isForeingCurrency())
			{	
				if (getTIPO().equals(MEXT) && pay.getC_Currency_ID()!=getMoneda())
				{
					JOptionPane.showMessageDialog(null,"La Moneda seleccionada difiere de la ingresada en la cabecera.","Error - Verificaci�n de Par�metro", JOptionPane.ERROR_MESSAGE);
					return false;
				}
				setIMPORTE(getConvertido().multiply(pay.getCotizacion()));
			}
		}
				
		//---
		if (!isProcessed())
		{
			//	VerificaTipo la cobranza
			if (getTIPO().equals(BANCO))
			{	
				//	Verificaci�n Par�metros
				if ((getC_BankAccount_ID()==null) || (getC_BankAccount_ID().equals(0)))
				{
					JOptionPane.showMessageDialog(null,"Ingrese Cuenta Bancaria","Error - Falta Par�metro", JOptionPane.ERROR_MESSAGE);
					return false;
				}
				if ((getNroTransferencia()==null) || (new Integer(getNroTransferencia())==0))
				{
					JOptionPane.showMessageDialog(null,"Ingrese Nro de Transferencia","Error - Falta Par�metro", JOptionPane.ERROR_MESSAGE);
					return false;
				}
				//	Verificaci�n de Datos
				//	L�gica
				setEstado("");
			}	//Banco
			else
			{	if (getTIPO().equals(MEXT))
				{
					//	Verificaci�n Par�metros
					if ((getMoneda()==null) || (getMoneda().equals(0)))
					{
						JOptionPane.showMessageDialog(null,"Ingrese Moneda","Error - Falta Par�metro", JOptionPane.ERROR_MESSAGE);
						return false;
					}
					//	Verificaci�n de Datos
					//	L�gica
					setEstado("");
				}	// Moneda Extranjera
				else
				{
				  if (getTIPO().equals(CHEQUE))
				  {
					//	Verificaci�n Par�metros
					if (getIMPORTE().equals(Env.ZERO) && getConvertido().equals(Env.ZERO))
					{
						JOptionPane.showMessageDialog(null,"Ingrese Importe","Error - Falta Par�metro", JOptionPane.ERROR_MESSAGE);
						return false;
					}
					if ((getBank()==null) || (getBank().equals("")))
					{
						JOptionPane.showMessageDialog(null,"Ingrese Banco","Error - Falta Par�metro", JOptionPane.ERROR_MESSAGE);
						return false;
					}
					if ((getNroCheque()==null) || (new Integer(getNroCheque())==0))
					{
						JOptionPane.showMessageDialog(null,"Ingrese Nro de Cheque","Error - Falta Par�metro", JOptionPane.ERROR_MESSAGE);
						return false;
					}
					if ((getTipoCheque()==null) || (getTipoCheque().equals("")))
					{
						JOptionPane.showMessageDialog(null,"Ingrese Tipo de Cheque","Error - Falta Par�metro", JOptionPane.ERROR_MESSAGE);
						return false;
					}
					if ((getClearing()==null) || (getClearing().equals("")))
					{
						JOptionPane.showMessageDialog(null,"Ingrese Días de Clearing","Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
						return false;
					}
					if (getReleasedDate()==null)
					{
						JOptionPane.showMessageDialog(null,"Ingrese Fecha de Emisión","Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
						return false;
					}
					if (getPaymentDate()==null)
					{
						JOptionPane.showMessageDialog(null,"Ingrese Fecha de Pago","Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
						return false;
					}
					
					//	Verificaci�n de Datos
										
					Date datePay = new Date(getPaymentDate().getTime());
					Date dateEmi = new Date(getReleasedDate().getTime());
					
					Calendar date = Calendar.getInstance();
					
					Date dateAct = null;
					if (pay!=null)
						dateAct = new Date(pay.getDateTrx().getTime());
					
					date.setTime(dateEmi);
					date.add(Calendar.DATE, LEY_DIAS_FE);
					Date dateVerFE = date.getTime();
					
					date.setTime(datePay);
					date.add(Calendar.DATE, LEY_DIAS_FA);
					Date dateVerFA = date.getTime();
	
					if (dateAct!=null && dateAct.after(dateVerFA))
					{
						JOptionPane.showMessageDialog(null,"La Fecha de Pago supera en al menos " + LEY_DIAS_FA + " d�as la Fecha de la Cobranza","Error - Verificaci�n", JOptionPane.ERROR_MESSAGE);
						return false;						
					}
					else
						if (getTipoCheque().equals(COMUN))
						{
							if (!datePay.equals(dateEmi))
							{
								JOptionPane.showMessageDialog(null,"Verifique que la Fecha de Emisi�n sea igual a la Fecha de Pago","Error - Verificaci�n", JOptionPane.ERROR_MESSAGE);
								return false;
							}
						}
						else
						{
							date.setTime(dateEmi);
							date.add(Calendar.DATE, 1);
							Date EmiTomorrow = date.getTime();
							if (EmiTomorrow.after(datePay))
							{
								JOptionPane.showMessageDialog(null,"Verifique que la Fecha de Pago sea mayor a la Fecha de Emisi�n","Error - Verificaci�n", JOptionPane.ERROR_MESSAGE);
								return false;						
							}
							if (datePay.after(dateVerFE))
							{
								JOptionPane.showMessageDialog(null,"Verifique que la Fecha de Pago no supere la Fecha de Emisi�n por m�s de " + LEY_DIAS_FE + " d�as","Error - Verificaci�n", JOptionPane.ERROR_MESSAGE);
								return false;						
							}
						}
					
					try 
					{	
						/*
						 * 01-02-2011 Camarzana Mariano
						 * Se le agrego la verificacion que los bancos sean distintos 
						 * Ticket - Cheques que no controlan el banco entonces no permite ingresar los que tienen igual numeraci�n
						 */		
					
					/*	String query = "select C_PAYMENTVALORES_ID from C_PAYMENTVALORES where NROCHEQUE = '" + getNroCheque() +
										"' AND ISACTIVE='Y'" +	"AND banco like '" + getBank() + "'";*/
					
						/*
						 * 04-05-2011 Camarzana Mariano
						 * Se agrego la condicion que en el caso de existir:
						 *  	Mismo numero de cheque con el mismo banco -> verificar que los socios de negocio sean distintos
						 */
						String query = " select v.C_PAYMENTVALORES_ID " +
									   " from C_PAYMENTVALORES v " +
									   " inner join C_PAYMENT p on (p.c_payment_id = v.c_payment_id) " +
									   " where NROCHEQUE = '" + getNroCheque() + "'and v.ISACTIVE='Y' " +	
									   " and banco like '" + getBank() + "' " + 
									   " and p.c_bpartner_id = " + pay.getC_BPartner_ID();
						
						
						PreparedStatement pstmt = DB.prepareStatement(query, null);
						ResultSet rs = pstmt.executeQuery();
					
						if (rs.next() && (getC_PAYMENTVALORES_ID() != rs.getInt(1)))
						{
							JOptionPane.showMessageDialog(null,"El Nro de Cheque ingresado ya existe.","Error - Nro de Cheque duplicado", JOptionPane.ERROR_MESSAGE);
							return false;
						}
					
						rs.close();
						pstmt.close();
					}
					catch (Exception e){}
					
					//	L�gica
					
					setC_MOVIMIENTOFONDOS_ID(0);
					
				  }		// CHEQUE
				 				  				  
				}
			}
		}	// DocStatus = DR
		
		if ((getEstado()=="P") && (getDebitoDate()==null))
		{
			JOptionPane.showMessageDialog(null,"Ingrese Fecha de D�bito","Error - Falta Par�metro", JOptionPane.ERROR_MESSAGE);
			return false;	
		}
				
		return true;
	}	//	beforeSave
		
	/**
	 * 	Update updatepayment
	 * 	@return true if updated
	 */
	private boolean updatepayment()
	{

	/**
	 * 	Modificaci�n REQ-034 (Parte 2).
	 *
	 *		Calcular el Neto de cobranza.
	 *
	 *		Modificaci�n con REQ-039 - Conversion de Moneda Extranjera
	 *
	 */
	 
		MPayment pay = new MPayment(getCtx(),getC_Payment_ID(),get_TrxName());

		if (pay.isReceipt())
		{	
			try {
			
				BigDecimal monto = new BigDecimal(0);
				
				String sql = "SELECT C_PaymentValores_Id " +
			  			 "FROM C_PaymentValores " +
			  			 "WHERE C_Payment_ID = ? AND IsActive = 'Y'";
			
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				pstmt.setLong(1, getC_Payment_ID());
		
				ResultSet rs = pstmt.executeQuery();
			
				if (rs.next()) 
				{
					/*					
					 * 		S�lo si hay valores asignados
					 * 	 en la pesta�a Asignaci�n.
					 */
				    sql = "SELECT COALESCE(SUM(a.Importe),0) " +
					  "FROM C_Payment p " +
					  "LEFT OUTER JOIN C_PaymentValores a ON (p.C_Payment_ID=a.C_Payment_ID) " +
					  "WHERE p.C_Payment_ID = ? AND a.IsActive = 'Y' AND a.TIPO!='M'"; 
			
					pstmt = DB.prepareStatement(sql, null);
					pstmt.setLong(1, getC_Payment_ID());
				
					rs = pstmt.executeQuery();
					
					if (rs.next())
					{	
						monto = rs.getBigDecimal(1);
					}
					
					sql = "SELECT a.Importe,a.C_Currency_ID " +
					  "FROM C_Payment p " +
					  "LEFT OUTER JOIN C_PaymentValores a ON (p.C_Payment_ID=a.C_Payment_ID) " +
					  "WHERE p.C_Payment_ID = ? AND a.IsActive = 'Y' AND a.TIPO='M'"; 
			
					pstmt = DB.prepareStatement(sql, null);
					pstmt.setLong(1, getC_Payment_ID());
				
					rs = pstmt.executeQuery();
					
					while (rs.next())
					{	
						BigDecimal conversion = MConversionRate.convert(Env.getCtx(),rs.getBigDecimal(1),rs.getInt(2),pay.getC_Currency_ID(),
			 					   new Timestamp(pay.getDateTrx().getTime()),0, pay.getAD_Client_ID(), pay.getAD_Org_ID());
						
						monto = monto.add(conversion);
					}
					
				}

                pay.setPAYNET(monto);
                pay.save();
			
			}
			catch (Exception e){return false;}
		}
		return true;
	}	
	
	public static MPAYMENTVALORES get(String bank, String nroCheque, String trxName)	{

		String sql = " SELECT C_PaymentValores_ID " +
		  			 " FROM C_PaymentValores " +
		  			 " WHERE Banco = ? AND IsActive = 'Y' AND NroCheque=?"; 
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setString(1, bank);
			pstmt.setString(2, nroCheque);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next())
			{
				return new MPAYMENTVALORES(Env.getCtx(),rs.getInt(1),trxName);			
			}
		
		}
		catch (Exception e) {
			return null;
		}
		
		return null;
	}
	
}	//	MCPaymentValores
