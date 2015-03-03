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
 *  @version 	$Id: DocPercep.java,v 1.0 2008/11/20 12:15:02 dgini Exp $
 */
public final class DocPercepRealizadasIB
{
	/**
	 *	Create Percep
	 *  @param C_InvoicePercep_ID percepcion
	 *  @param name name
	 *  @param amount amount
	 */
	public DocPercepRealizadasIB (int InvoicePercep_SOTrx, int C_Jurisdiccion_ID, BigDecimal amount, String name)
	{
		m_InvoicePercep_SOTrx = InvoicePercep_SOTrx;
		
		m_name = name;
		
		m_jurisdiccion = "";
		
		m_C_Jurisdiccion_ID = C_Jurisdiccion_ID;
		
		
		String sql = "SELECT NAME "
			+ "FROM C_Jurisdiccion WHERE C_Jurisdiccion_ID=?";
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, m_C_Jurisdiccion_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				m_jurisdiccion = rs.getString(1);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		m_amount = amount;
	}	//	DocPercep

	/** InvoicePercepcion ID              */
	private int		m_InvoicePercep_SOTrx = 0;
	
	/** Percepcion ID              */
	private int 	m_C_Jurisdiccion_ID = 0;
	
	/** Amount              */
	private BigDecimal 	m_amount = null;
	/** Name Percepcion     */
	private String 		m_name = null;
	/** Name Jurisdiccion   */
	private String 		m_jurisdiccion = null;
	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(DocPercepRealizadasIB.class);
	
	/** Percepcion Acct        */
	public static final int    ACCTTYPE_Percepcion = 0;
	
	/**
	 *	Get Account
	 *  //@param AcctType see ACCTTYPE_*
	 *  @param as account schema
	 *  @return Account
	 */
	public MAccount getAccount (int AcctType, MAcctSchema as)
	{
		if (AcctType != 0)
			return null;
		
		String sql = "SELECT A_Percep_Acct "
			+ "FROM C_Jurisdiccion_Acct WHERE C_Jurisdiccion_ID=? AND C_AcctSchema_ID=? AND IsActive = 'Y'";
		
		int validCombination_ID = 0;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, m_C_Jurisdiccion_ID);
			pstmt.setInt(2, as.getC_AcctSchema_ID());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				validCombination_ID = rs.getInt(AcctType+1);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		if (validCombination_ID == 0)
			return null;
		
		return MAccount.get(as.getCtx(), validCombination_ID);
	}   //  getAccount

	/**
	 *	Get Amount
	 *  @return amount
	 */
	public BigDecimal getAmount()
	{
		return m_amount;
	}

	/**
	 *  Get Name of Percepcion
	 *  @return name
	 */
	public String getName()
	{
		return m_name;
	}
	
	/**
	 *  Get Name of Jurisdiccion
	 *  @return name
	 */
	public String getJurisdiccion()
	{
		return m_jurisdiccion;
	}

	/**
	 * 	Get C_InvoicePercep_SOTrx_ID
	 *	@return percepcion id
	 */
	public int getInvoicePercep_SOTrx()
	{
		return m_InvoicePercep_SOTrx;
	}	//	getInvoicePercep_SOTrx
	
	/**
	 *  Get Description (Percepcion Name and Base Amount)
	 *  @return percepcion name and amount
	 */
	public String getDescription()
	{
		return m_name + " " + m_amount.toString();
	}   //  getDescription
	
	/**
	 * 	Get AP Percepcion Type
	 *	@return AP percepcion type 
	 */
	public int getAPPercepcionType()
	{
		return ACCTTYPE_Percepcion;
	}	//	getAPPercepcionType
	
	/**
	 *	Return String representation
	 *  @return percepcion name and base amount
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("Percepcion=(");
		sb.append(m_name);
		sb.append(" Amt=").append(m_amount);
		sb.append(")");
		return sb.toString();
	}	//	toString
	
	public int getC_Jurisdiccion_ID()
	{
		return m_C_Jurisdiccion_ID;
	}	//	getC_Jurisdiccion_ID
	

}	//	DocPercepRealizadasIB
