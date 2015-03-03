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
 
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

import javax.swing.JOptionPane;

import org.compiere.util.*;

 
/**
 *	Bank Statement Line Model
 *
 *	@author Eldir Tomassen/Jorg Janke
 *	@version $Id: MBankStatementLine.java,v 1.14 2005/09/19 04:49:47 jjanke Exp $
 */
 @SuppressWarnings("serial")
public class MMOVIMIENTOPOSTERIOR extends X_C_MOVIMIENTOPOSTERIOR
 {
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_BankStatementLine_ID id
	 */
	public MMOVIMIENTOPOSTERIOR (Properties ctx, int C_MMOVIMIENTOPOSTERIOR_ID, String trxName)
	{
		super (ctx, C_MMOVIMIENTOPOSTERIOR_ID, trxName);
		if (C_MMOVIMIENTOPOSTERIOR_ID == 0)
		{
			setC_ConciliacionBancaria_ID (0);
			setC_MovimientoFondos_ID (0);
			setC_ValorPago_ID (0);
			setC_Payment_ID (0);
			setC_PaymentValores_ID (0);
			setAmt(Env.ZERO);
		}
	}	//	MMOVIMIENTOCONCILIACION
	
	/**
	 *	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MMOVIMIENTOPOSTERIOR (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MBankStatementLine
	
	/**
	 * 	Parent Constructor
	 * 	@param statement Bank Statement that the line is part of
	 */
	public MMOVIMIENTOPOSTERIOR(MCONCILIACIONBANCARIA conciliacion)
	{
		this (conciliacion.getCtx(), 0, conciliacion.get_TrxName());
		setC_ConciliacionBancaria_ID(conciliacion.getC_ConciliacionBancaria_ID());
	}	//	MBankStatementLine
        
        
	public void setAmt(BigDecimal Amt) {
            
                MBankAccount account = new MBankAccount(Env.getCtx(),getC_BankAccount_ID(),null);

                MCurrency moneda = MCurrency.get (Env.getCtx(), account.getC_Currency_ID());
                if (moneda != null) {
                    BigDecimal valueRound = (BigDecimal)Amt;
                    valueRound = valueRound.setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
                    set_Value("IMPORTE", valueRound);
                } else {
                    set_Value("IMPORTE", Amt);
                }
                
	}        
        

 }	//	MMOVIMIENTOPOSTERIOR
