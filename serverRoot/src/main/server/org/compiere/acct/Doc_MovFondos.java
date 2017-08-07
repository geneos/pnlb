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
import java.util.*;

import org.compiere.model.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 *  Post Invoice Documents.
 *  <pre>
 *  Table:              C_Cash (407)
 *  Document Types:     CMC
 *  </pre>
 *  @author Jorg Janke
 *  @version  $Id: Doc_Cash.java,v 1.7 2005/10/26 00:40:02 jjanke Exp $
 */
public class Doc_MovFondos extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@parem trxName trx
	 */
	protected Doc_MovFondos (MAcctSchema[] ass, ResultSet rs, String trxName)
	{
		super(ass, MMOVIMIENTOFONDOS.class, rs, DOCTYPE_MovFondos, trxName);
	}	//	Doc_MovFondos

	/** Contained Optional Valores Lines    */
	private DocLine_Debito[]  deb_lines = null;
	
	/** Contained Optional Valores Lines    */
	private DocLine_Debito[]  cred_lines = null;
	
	/**
	 *  Load Specific Document Details
	 *  @return error message or null
	 */
	protected String loadDocumentDetails ()
	{
		MMOVIMIENTOFONDOS mov = (MMOVIMIENTOFONDOS)getPO();
		mov.setDateAcct(mov.getDateTrx());
		mov.save();
		
		setDateDoc(mov.getDateAcct());
		setC_Currency_ID(NO_CURRENCY);
		//	Contained Objects
		deb_lines = loadLinesDebito();
		cred_lines = loadLinesCredito();
		
		log.fine("Lines=" + (deb_lines.length + cred_lines.length));
		return null;
	}   //  loadDocumentDetails

	/*
	 *	Agregado por DANIEL GINI @BISion It Solutions
	 */
	
	/**
	 *	Load Debito Line
	 *  @return DocLine_Debito Array
	 */
	private DocLine_Debito[] loadLinesDebito()
	{
		ArrayList<DocLine_Debito> list = new ArrayList<DocLine_Debito>();
		
		String sql =  "SELECT C_MovimientoFondos_Deb_Id, DEBITO, CONVERTIDO "
					+ "FROM C_MovimientoFondos_Deb "
					+ "WHERE C_MovimientoFondos_Id= ? AND IsActive = 'Y'";
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, getTrxName());
			pstmt.setInt(1, get_ID());
			ResultSet rs = pstmt.executeQuery();
			//
			while (rs.next())
			{
				int C_MovimientoFondos_Id = rs.getInt(1);
				BigDecimal amount = rs.getBigDecimal(2);
				
				//
                                                                MMOVIMIENTOFONDOS mov = new MMOVIMIENTOFONDOS(getCtx(),get_ID(),getTrxName());
                                                                // Agrego soporte para moneda extranjera  -> Convertido es el monto en pesos ARS
                                                                // - Transferencia entre cuentas
                                                                // - credito bancario
                                                                if ( (mov.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_TransferenciaCuentasBancarias) 
                                                                      || mov.getTIPO().equals(MMOVIMIENTOFONDOS.MOV_CREDITO_BANCARIO)) 
                                                                        && mov.getC_Currency_ID() != 118 ){
                                                                    amount=rs.getBigDecimal(3);
                                                                }
				DocLine_Debito valueLine = new DocLine_Debito(get_ID(),C_MovimientoFondos_Id, amount, "D",true);
				log.fine(valueLine.toString());
				list.add(valueLine);
			}
			//
			
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return null;
		}

		//	Return Array
		DocLine_Debito[] tl = new DocLine_Debito[list.size()];
		list.toArray(tl);
		return tl;
	}

	/**
	 *	Load Credito Line
	 *  @return DocLine_Debito Array
	 */
	private DocLine_Debito[] loadLinesCredito()
	{
		ArrayList<DocLine_Debito> list = new ArrayList<DocLine_Debito>();
		
		String sql =  "SELECT C_MovimientoFondos_Cre_Id, CREDITO, CONVERTIDO "
					+ "FROM C_MovimientoFondos_Cre "
					+ "WHERE C_MovimientoFondos_Id= ? AND IsActive = 'Y'";
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, getTrxName());
			pstmt.setInt(1, get_ID());
			ResultSet rs = pstmt.executeQuery();
                                                MMOVIMIENTOFONDOS mov = new MMOVIMIENTOFONDOS(getCtx(),get_ID(),getTrxName());
			//
			while (rs.next())
			{
				int C_MovimientoFondos_Id = rs.getInt(1);
				BigDecimal amount = rs.getBigDecimal(2);
				
				//
                                                                // Agrego soporte para moneda extranjera (Solo Transferencia entre cuentas) -> Convertido es el monto en pesos ARS
                                                                if (mov.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_TransferenciaCuentasBancarias) && mov.getC_Currency_ID() != 118){
                                                                    amount=rs.getBigDecimal(3);
                                                                }
				DocLine_Debito valueLine = new DocLine_Debito(get_ID(),C_MovimientoFondos_Id, amount, "C",false);
				log.fine(valueLine.toString());
				list.add(valueLine);
			}
			//
			
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return null;
		}

		//	Return Array
		DocLine_Debito[] tl = new DocLine_Debito[list.size()];
		list.toArray(tl);
		return tl;
	}
	
	/**************************************************************************
	 *  Get Source Currency Balance - subtracts line amounts from total - no rounding
	 *  @return positive amount, if total invoice is bigger than lines
	 */
	public BigDecimal getBalance()
	{
		/*BigDecimal retValue = Env.ZERO;
		StringBuffer sb = new StringBuffer (" [");
		//  Total
		retValue = retValue.add(getAmount(Doc.AMTTYPE_Gross));
		sb.append(getAmount(Doc.AMTTYPE_Gross));
		//  - Lines
		for (int i = 0; i < p_lines.length; i++)
		{
			retValue = retValue.subtract(p_lines[i].getAmtSource());
			sb.append("-").append(p_lines[i].getAmtSource());
		}
		sb.append("]");
		//
		log.fine(toString() + " Balance=" + retValue + sb.toString());
	*/
	//	return retValue;
		return Env.ZERO;    //  Lines are balanced
	}   //  getBalance

	/**
	 *  Create Facts (the accounting logic) for
	 *  CMC.
	 *  <pre>
	 *          D�bito		   DR
	 *          Cr�dito               CR
	 *  </pre>
	 *  @param as account schema
	 *  @return Fact
	 */
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
		//  create Fact Header
		Fact fact = new Fact(this, as, Fact.POST_Actual);
		
		//  D�bito Lines
		for (int i = 0; i < deb_lines.length; i++)
		{
			FactLine fl = fact.createLine(null, deb_lines[i].getAccount(deb_lines[i].getValueType(), as),
			as.getC_Currency_ID(), deb_lines[i].getAmount(), null);
			if (fl != null)
			{	
				fl.setDescription(deb_lines[i].getDescription());
				fl.save(getTrxName());
			}
		}	
		
		//  Credito Lines
		for (int i = 0; i < cred_lines.length; i++)
		{
			FactLine fl = fact.createLine(null, cred_lines[i].getAccount(cred_lines[i].getValueType(), as),
			as.getC_Currency_ID(), null, cred_lines[i].getAmount());
			if (fl != null)
			{	
				fl.setDescription(cred_lines[i].getDescription());
				fl.save(getTrxName());
			}
		}	
		
		//
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
                                System.out.print("Lineas creadas");

		return facts;
	}   //  createFact

}   //  Doc_MovFondos
