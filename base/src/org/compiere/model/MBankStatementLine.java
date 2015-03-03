/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ActFact BV.
 *****************************************************************************/
 package org.compiere.model;
 
import java.math.*;
import java.sql.*;
import java.util.*;
import org.compiere.model.MBankStatement;
import org.compiere.util.*;

 
/**
 *	Bank Statement Line Model
 *
 *	@author Eldir Tomassen/Jorg Janke
 *	@version $Id: MBankStatementLine.java,v 1.14 2005/09/19 04:49:47 jjanke Exp $
 */
 public class MBankStatementLine extends X_C_BankStatementLine
 {
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_BankStatementLine_ID id
	 */
	public MBankStatementLine (Properties ctx, int C_BankStatementLine_ID, String trxName)
	{
		super (ctx, C_BankStatementLine_ID, trxName);
		if (C_BankStatementLine_ID == 0)
		{
		//	setC_BankStatement_ID (0);		//	Parent
		//	setC_Charge_ID (0);
		//	setC_Currency_ID (0);	//	Bank Acct Currency
		//	setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM C_BankStatementLine WHERE C_BankStatement_ID=@C_BankStatement_ID@
			setStmtAmt(Env.ZERO);
			setTrxAmt(Env.ZERO);
			setInterestAmt(Env.ZERO);
			setChargeAmt(Env.ZERO);
			setIsReversal (false);
		//	setValutaDate (new Timestamp(System.currentTimeMillis()));	// @StatementDate@
		//	setDateAcct (new Timestamp(System.currentTimeMillis()));	// @StatementDate@
		}
	}	//	MBankStatementLine
	
	/**
	 *	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MBankStatementLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MBankStatementLine
	
	/**
	 * 	Parent Constructor
	 * 	@param statement Bank Statement that the line is part of
	 */
	public MBankStatementLine(MBankStatement statement)
	{
		this (statement.getCtx(), 0, statement.get_TrxName());
		setClientOrg(statement);
		setC_BankStatement_ID(statement.getC_BankStatement_ID());
		setStatementLineDate(statement.getStatementDate());
	}	//	MBankStatementLine

	/**
	 * 	Parent Constructor
	 * 	@param statement Bank Statement that the line is part of
	 * 	@param lineNo position of the line within the statement
	 */
	public MBankStatementLine(MBankStatement statement, int lineNo)
	{
		this (statement);
		setLine(lineNo);
	}	//	MBankStatementLine


	/**
	 * 	Set Statement Line Date and all other dates (Valuta, Acct)
	 *	@param StatementLineDate date
	 */
	public void setStatementLineDate(Timestamp StatementLineDate)
	{
		super.setStatementLineDate(StatementLineDate);
		setValutaDate (StatementLineDate);
		setDateAcct (StatementLineDate);
	}	//	setStatementLineDate

	/**
	 * 	Set Payment
	 *	@param payment payment
	 */
	public void setPayment (MPayment payment)
	{
		setC_Payment_ID (payment.getC_Payment_ID());
		
                //setC_Currency_ID (payment.getC_Currency_ID());
		
                //
                
                /*
                 *          Vit4B - 11/02/2008 Modificación para soportar la funcionalidad siguiente:
                 *
                 *          El estado de cuentas bancario debe tener cada una de sus lineas en la moneda 
                 *          de la cuenta de manera que al momento de completar el estado el calculo este 
                 *          en la misma moneda, actualmente las líneas al completar no toma en cuenta la 
                 *          moneda por lo tanto queda mal actualizado el saldo de la cuenta banco.
                 *
                 *
                 */
                
                
                
                MBankStatement bkst =  new MBankStatement(this.getCtx(), this.getC_BankStatement_ID(), null);                
                int C_Currency_To_ID = bkst.getBankAccount().getC_Currency_ID();
                int C_Currency_From_ID = payment.getC_Currency_ID();
                
                // se cambia para que tome la unidad de la cuenta
                
                setC_Currency_ID (C_Currency_To_ID);
                
                BigDecimal amt = payment.getPayAmt(true); 
		
                
                String sqlConvert = "SELECT currencyConvert(" + amt.toString() + "," +
                        C_Currency_From_ID + "," + C_Currency_To_ID + ",SysDate,114, " + 
                        this.getAD_Client_ID() + "," + this.getAD_Org_ID() + ")" +
                        "FROM C_Invoice";
                
                BigDecimal amtConvert = Env.ZERO;
                
                PreparedStatement pstmt = DB.prepareStatement(sqlConvert, null);
                ResultSet rs = null;
                try {
                    rs = pstmt.executeQuery();
                    
                    if (rs.next())
                    {
                        amtConvert = rs.getBigDecimal(1);
                    }    
                
	                        
                    rs.close();  
                    pstmt.close(); 

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }  

		
                // setTrxAmt(amt);
		// setStmtAmt(amt);
		
                
                if(amtConvert == null)
                {
                    setTrxAmt(amt);
                    setStmtAmt(amt);
		}
                
                setTrxAmt(amtConvert);
                setStmtAmt(amtConvert);

		setDescription(payment.getDescription());
                
	}	//	setPayment

	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else
			setDescription(desc + " | " + description);
	}	//	addDescription

	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (getChargeAmt().signum() != 0 && getC_Charge_ID() == 0)
		{
			log.saveError("FillMandatory", Msg.getElement(getCtx(), "C_Charge_ID"));
			return false;
		}
		//	Set Line No
		if (getLine() == 0)
		{
			String sql = "SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM C_BankStatementLine WHERE C_BankStatement_ID=?";
			int ii = DB.getSQLValue (get_TrxName(), sql, getC_BankStatement_ID());
			setLine (ii);
		}
		
		//	Set References
		if (getC_Payment_ID() != 0 && getC_BPartner_ID() == 0)
		{
			MPayment payment = new MPayment (getCtx(), getC_Payment_ID(), get_TrxName());
			setC_BPartner_ID(payment.getC_BPartner_ID());
			if (payment.getC_Invoice_ID() != 0)
				setC_Invoice_ID(payment.getC_Invoice_ID());
		}
		if (getC_Invoice_ID() != 0 && getC_BPartner_ID() == 0)
		{
			MInvoice invoice = new MInvoice (getCtx(), getC_Invoice_ID(), get_TrxName());
			setC_BPartner_ID(invoice.getC_BPartner_ID());
		}
		
		//	Calculate Charge = Statement - trx - Interest  
		BigDecimal amt = getStmtAmt();
		amt = amt.subtract(getTrxAmt());
		amt = amt.subtract(getInterestAmt());
		if (amt.compareTo(getChargeAmt()) != 0)
			setChargeAmt (amt);
		//
		
		return true;
	}	//	beforeSave
	
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		updateHeader();
		return success;
	}	//	afterSave
	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterDelete (boolean success)
	{
		updateHeader();
		return success;
	}	//	afterSave

	/**
	 * 	Update Header
	 */
	private void updateHeader()
	{
		String sql = "UPDATE C_BankStatement bs"
			+ " SET StatementDifference=(SELECT SUM(StmtAmt) FROM C_BankStatementLine bsl " 
				+ "WHERE bsl.C_BankStatement_ID=bs.C_BankStatement_ID AND bsl.IsActive='Y') "
			+ "WHERE C_BankStatement_ID=" + getC_BankStatement_ID();
		DB.executeUpdate(sql, get_TrxName());
		sql = "UPDATE C_BankStatement bs"
			+ " SET EndingBalance=BeginningBalance+StatementDifference "
			+ "WHERE C_BankStatement_ID=" + getC_BankStatement_ID();
		DB.executeUpdate(sql, get_TrxName());
	}	//	updateHeader
	
 }	//	MBankStatementLine
