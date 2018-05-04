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
public class MMOVIMIENTOCONCILIACION extends X_C_MOVIMIENTOCONCILIACION
 {
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_BankStatementLine_ID id
	 */
	public MMOVIMIENTOCONCILIACION (Properties ctx, int C_MMOVIMIENTOCONCILIACION_ID, String trxName)
	{
		super (ctx, C_MMOVIMIENTOCONCILIACION_ID, trxName);
		if (C_MMOVIMIENTOCONCILIACION_ID == 0)
		{
			setC_ConciliacionBancaria_ID (0);
			setC_MovimientoFondos_ID (0);
			setC_ValorPago_ID (0);
			setC_Payment_ID (0);
			setC_PaymentValores_ID (0);
			setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM C_BankStatementLine WHERE C_BankStatement_ID=@C_BankStatement_ID@
			setConciliado (false);
			setAmt(Env.ZERO);
		}
	}	//	MMOVIMIENTOCONCILIACION
	
	/**
	 *	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MMOVIMIENTOCONCILIACION (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MBankStatementLine
	
	/**
	 * 	Parent Constructor
	 * 	@param statement Bank Statement that the line is part of
	 */
	public MMOVIMIENTOCONCILIACION(MCONCILIACIONBANCARIA conciliacion)
	{
		this (conciliacion.getCtx(), 0, conciliacion.get_TrxName());
		setC_ConciliacionBancaria_ID(conciliacion.getC_ConciliacionBancaria_ID());
	}	//	MBankStatementLine

	/**
	 * 	Parent Constructor
	 * 	@param statement Bank Statement that the line is part of
	 * 	@param lineNo position of the line within the statement
	 */
	public MMOVIMIENTOCONCILIACION(MCONCILIACIONBANCARIA conciliacion, int lineNo)
	{
		this (conciliacion);
		setLine(lineNo);
	}	//	MBankStatementLine

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	public static int getNextLine(int C_ConciliacionBancaria_ID)
	{
		String sql = "SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM C_MOVIMIENTOCONCILIACION WHERE C_CONCILIACIONBANCARIA_ID=?";
		int ii = DB.getSQLValue (null, sql, C_ConciliacionBancaria_ID);
		return ii;
		
	}	//	getNextLine
	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return success
	 */
	protected boolean beforeDelete ()
	{
		MCONCILIACIONBANCARIA concBancaria = new MCONCILIACIONBANCARIA (Env.getCtx(), getC_ConciliacionBancaria_ID(), null);
	
		MMOVIMIENTOCONCILIACION movConcNew = new MMOVIMIENTOCONCILIACION (concBancaria);
		
		movConcNew.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
		movConcNew.setC_ConciliacionBancaria_ID(getC_ConciliacionBancaria_ID());
		
		movConcNew.setC_ValorPago_ID(getC_ValorPago_ID());
		movConcNew.setC_Payment_ID(getC_Payment_ID());
		movConcNew.setC_PaymentValores_ID(getC_PaymentValores_ID());
		movConcNew.setC_MovimientoFondos_ID(getC_MovimientoFondos_ID());
		movConcNew.setDocumentNo(getDocumentNo());
		movConcNew.setMovimiento(getMovimiento());
		movConcNew.setAmt(getAmt());
		movConcNew.setNroCheque(getNroCheque());
		movConcNew.setAFavor(getAFavor());
		movConcNew.setVencimientoDate(getVencimientoDate());
		movConcNew.setEstado(getEstado());
		movConcNew.setTipo(getTipo());
		movConcNew.setREG_MovimientoFondos(getREG_MovimientoFondos());
		movConcNew.setEfectivaDate(getEfectivaDate());
                /*
                 *  28/08/2013 Maria Jesus
                 *  Cuando eliminaba un movimiento de conciliacion para que luego pase
                 *  a pendiente, dejaba el mismo estado de Pendiente que en la anterior.
                 *  Esto hacia que si un movimiento se eligio para conciliar y luego se
                 *  elimino, que no aparezca como pendiente en la proxima conciliacion.
                 */
                //movConcNew.setOld(getOld());
		movConcNew.setOld(false);
		movConcNew.setConciliado(false);
		movConcNew.setLine(0);
		
		if (!movConcNew.save())
			return false;
		
		updateHeader();
		
		return true;
	}	//	afterSave

	/**
	 * 	Update Header
	 */

	private void updateHeader()
	{
		MCONCILIACIONBANCARIA concBanc = new MCONCILIACIONBANCARIA(getCtx(),getC_ConciliacionBancaria_ID(),get_TrxName());
		
		concBanc.setSaldoConciliado(concBanc.getSaldoConciliado().subtract(getAmt()));
		//concBanc.setSaldoCierre(concBanc.getSaldoConciliado().add(concBanc.getSaldoInicial()));
		//concBanc.setSaldoAConciliar(concBanc.getSaldoInicial().add(concBanc.getSaldoConciliado()).subtract(concBanc.getSaldoCierre()));
                /*
                 *  16/10/2013 Maria Jesus
                 *  Modificacion en el calculo del saldo a conciliar luego de que se elimina un movimiento.
                 */
                concBanc.setSaldoAConciliar(concBanc.getSaldoCierre().subtract(concBanc.getSaldoInicial()).subtract(concBanc.getSaldoConciliado()));
		concBanc.setSaldoPendiente(concBanc.getSaldoPendiente().add(getAmt()));
		//concBanc.setSaldoContable(concBanc.getSaldoPendiente().add(concBanc.getSaldoCierre()));
		
		concBanc.save();
	}	//	updateHeader
        
        
	/**
	 * Set Amount.
	 */
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
        
	/**
	 * 	isConcCompleteForPayment
         *      Determina si algún elemento del pago se concilió
	 *	@param newRecord new
	 *	@return true
	 */
        
	public static boolean isConcCompleteForPayment(int M_Payment_ID) throws SQLException {
            
            String sql = "SELECT * FROM C_MOVIMIENTOCONCILIACION "
                    + "WHERE c_payment_id = ? AND CONCILIADO = 'Y'";

            
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, M_Payment_ID);

            ResultSet rs = pstmt.executeQuery();
            
            boolean flag = false;

            if (rs.next()) {
                    flag = true;
            }
            
            rs.close();
            pstmt.close();

            return flag;

		
	}	//	isConcCompleteForPayment
        
	/**
	 * 	isConcCompleteForPayment
         *      Determina si algún elemento del pago se concilió
	 *	@param newRecord new
	 *	@return true
	 */
        
	public static int deleteMov(int M_Payment_ID) throws SQLException {
            
            String sql = "DELETE FROM C_MOVIMIENTOCONCILIACION "
                    + "WHERE c_payment_id = "+M_Payment_ID;
            
            return DB.executeUpdate(sql, null);

		
	}	//	isConcCompleteForPayment            
	
 }	//	MMOVIMIENTOCONCILIACION
