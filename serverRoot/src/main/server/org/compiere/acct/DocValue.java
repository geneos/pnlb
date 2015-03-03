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
package org.compiere.acct;

import java.math.*;
import java.sql.*;

import org.compiere.model.*;

import java.util.logging.*;

import org.compiere.util.*;

/**
 *  Document Percep Line
 *
 *  @author 	DANIEL GINI
 *
 *		Creado para registrar contabilidad en cobranzas
 *
 *  @version 	$Id: DocPercep.java,v 1.0 2008/11/20 12:15:02 dgini Exp $
 */
public final class DocValue
{
	/**
	 *	Create Percep
	 *  @param C_Cobranza_Ret_Id retencion
	 *  @param C_RegReten_Recib_ID name
	 *  @param amount amount
	 */
	public DocValue (int C_PaymentValores_Id, int C_BankAccount_ID, int C_Currency_ID, int C_Charge_ID, String tipo)
	{
		m_C_PaymentValores_Id = C_PaymentValores_Id;
		
		m_C_BankAccount_ID = C_BankAccount_ID;
		
		m_C_Currency_ID = C_Currency_ID;
		
		m_C_Charge_ID = C_Charge_ID;
		
		switch (tipo.charAt(0))
		{ 
		case 'C': 
			m_tipo = ACCTTYPE_CAJA;		
			break;
		case 'B': 
			m_tipo = ACCTTYPE_BANCO;	
			break;
		case 'M': 
			m_tipo = ACCTTYPE_MONEXT;	
			break;
		case 'O': 
			m_tipo = ACCTTYPE_BONO;		
			break;
		case 'Q': 
			m_tipo = ACCTTYPE_CHEQUE;	
			break;
		default:
			m_tipo = ACCTTYPE_ERROR;
		}
		
	}	//	DocPercep

	/** InvoicePercepcion ID              */
	private int		m_C_PaymentValores_Id = 0;
	
	/** Percepcion ID              */
	private int 	m_C_BankAccount_ID = 0;
	private int 	m_C_Currency_ID = 0;
	private int 	m_C_Charge_ID = 0;
	
	private int		ACCTTYPE_ERROR = 0;
	private int		ACCTTYPE_CAJA = 1;
	private int		ACCTTYPE_BANCO = 2;
	private int		ACCTTYPE_MONEXT = 3;
	private int		ACCTTYPE_BONO = 4;
	private int		ACCTTYPE_CHEQUE = 5;
	
	/** Amount              */
	private BigDecimal 	m_amount = BigDecimal.ZERO;
	/** Tipo                */
	private int 	m_tipo = 0;
	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(DocValue.class);
	
	/** Percepcion Acct        */
	
	/**
	 *	Get Account
	 *  @param AcctType see ACCTTYPE_*
	 *  @param as account schema
	 *  @return Account
	 */
	public MAccount getAccount (int AcctType, MAcctSchema as)
	{
		int para_1 = 0;     //  first parameter (second is always AcctSchema)
		String sql = null;
		
		if (AcctType == ACCTTYPE_BANCO)
		{
			sql = "SELECT B_Asset_Acct FROM C_BankAccount_Acct WHERE C_BankAccount_ID=? AND C_AcctSchema_ID=?";
			para_1 = getC_BankAccount_ID();
		}
		else if (AcctType == ACCTTYPE_CAJA)
		{
			sql = "SELECT CB_Asset_Acct FROM C_AcctSchema_Default WHERE C_AcctSchema_ID=?";
			para_1 = -1;
		}
		else if (AcctType == ACCTTYPE_MONEXT)
		{
			sql = "SELECT Asset_Acct FROM C_Currency_Acct WHERE C_Currency_ID=? AND C_AcctSchema_ID=?";
			para_1 = getC_Currency_ID();
		}
		else if (AcctType == ACCTTYPE_BONO)
		{
			sql = "SELECT CH_Revenue_Acct FROM C_Charge_Acct WHERE C_Charge_ID=? AND C_AcctSchema_ID=?";
			para_1 = getC_Charge_ID();
		}
		else if (AcctType == ACCTTYPE_CHEQUE)
		{
			sql = "SELECT B_Unidentified_Acct FROM C_AcctSchema_Default WHERE C_AcctSchema_ID=?";
			para_1 = -1;
		}
		else
			return null;
			
		int validCombination_ID = 0;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			if (para_1 == -1)   //  GL Accounts
				pstmt.setInt (1, as.getC_AcctSchema_ID());
			else
			{
				pstmt.setInt (1, para_1);
				pstmt.setInt (2, as.getC_AcctSchema_ID());
			}
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				validCombination_ID = rs.getInt(1);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "AcctType=" + AcctType + " - SQL=" + sql, e);
			return null;
		}
		if (validCombination_ID == 0)
			return null;
		
		return MAccount.get(as.getCtx(), validCombination_ID);
	}   //  getAccount

	/**
	 *	Get Amount
	 *  @return amount
	 */
	public BigDecimal getAmount(MAcctSchema as)
	{
		MPAYMENTVALORES payVal = new MPAYMENTVALORES(Env.getCtx(),m_C_PaymentValores_Id,null);
                
		// Old: if (as.getC_Currency_ID()==m_C_Currency_ID || getValueType()==ACCTTYPE_MONEXT)
                //Modificado 4/12/2012 , Cobranzas con Tipo Valor: MONEDA EXTRANJERA no balancean
                if (as.getC_Currency_ID()==m_C_Currency_ID)
			m_amount = payVal.getIMPORTE();
		else
			m_amount = payVal.getConvertido();
		
		return m_amount;
	}	//getAmount

	/**
	 *  Get Name of Percepcion
	 *  @return name
	 */
	public int getValueType()
	{
		return m_tipo;
	}	 //getValueType

	/**
	 * 	Get C_InvoicePercep_ID
	 *	@return percepcion id
	 */
	public int getC_PaymentValores_Id()
	{
		return m_C_PaymentValores_Id;
	}	//	getC_PaymentValores_Id
	
	/**
	 *	Return String representation
	 *  @return percepcion name and base amount
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("Valor=(");
		sb.append(m_tipo);
		sb.append(" Amt=").append(m_amount);
		sb.append(")");
		return sb.toString();
	}	//	toString
	
	public int getC_BankAccount_ID()
	{
		return m_C_BankAccount_ID;
	}	//	getC_BankAccount_ID
	
	public int getC_Charge_ID()
	{
		return m_C_Charge_ID;
	}	//	getC_BankAccount_ID
	
	public int getC_Currency_ID()
	{
		return m_C_Currency_ID;
	}	//	getC_BankAccount_ID
	
	public String getDescription()
	{
		MPAYMENTVALORES pay = new MPAYMENTVALORES(Env.getCtx(), m_C_PaymentValores_Id, null);
		
		if (getValueType() == ACCTTYPE_BANCO)			//Banco_N�mero
		{
			MBankAccount bank = MBankAccount.get(Env.getCtx(), m_C_BankAccount_ID);
			return bank.getName() + "_" + pay.getNroTransferencia();
		}
		else if (getValueType() == ACCTTYPE_MONEXT)	//Nombre_Importe
		{
			return MCurrency.getISO_Code(Env.getCtx(), m_C_Currency_ID) + "_" + pay.getExtranjera().toString();
		}
		else if (getValueType() == ACCTTYPE_CHEQUE)	//Banco_N�mero_ Fecha Vto
		{
			if (pay.getVencimientoDate()!=null)
				return pay.getBank() + "_" + pay.getNroCheque() + "_" + pay.getVencimientoDate().toString();
			return pay.getBank() + "_" + pay.getNroCheque();
		}
		
		return "";
	}	//	getDescription

}	//	DocValue
