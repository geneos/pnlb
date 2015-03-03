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
public final class DocPercep
{
	/**
	 *	Create Percep
	 *  @param C_InvoicePercep_ID percepcion
	 *  @param name name
	 *  @param amount amount
	 */
	public DocPercep (int C_InvoicePercep_ID, int C_RegPercep_Recib_ID, BigDecimal amount)
	{
		m_C_InvoicePercep_ID = C_InvoicePercep_ID;
		
		m_name = "";
		
		m_C_RegPercep_Recib_ID = C_RegPercep_Recib_ID;
		
		String sql = "SELECT NAME "
			+ "FROM C_RegPercep_Recib WHERE C_RegPercep_Recib_ID=?";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, m_C_RegPercep_Recib_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				m_name = rs.getString(1);
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
	private int		m_C_InvoicePercep_ID = 0;
	
	/** Percepcion ID              */
	private int 	m_C_RegPercep_Recib_ID = 0;
	
	/** Amount              */
	private BigDecimal 	m_amount = null;
	/** Name                */
	private String 		m_name = null;
	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(DocPercep.class);
	
	/** Percepcion Acct        */
	public static final int    ACCTTYPE_Percepcion = 0;
	
	/**
	 *	Get Account
	 *  @param AcctType see ACCTTYPE_*
	 *  @param as account schema
	 *  @return Account
	 */
	public MAccount getAccount (int AcctType, MAcctSchema as)
	{
		if (AcctType != 0)
			return null;

		String sql = "SELECT A_Percep_Acct "
			+ "FROM C_RegPercep_Recib_Acct WHERE C_RegPercep_Recib_ID=? AND C_AcctSchema_ID=? AND IsActive = 'Y'";
		int validCombination_ID = 0;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, m_C_RegPercep_Recib_ID);
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
	 * 	Get C_InvoicePercep_ID
	 *	@return percepcion id
	 */
	public int getC_InvoicePercep_ID()
	{
		return m_C_InvoicePercep_ID;
	}	//	getC_Tax_ID
	
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
	}	//	getAPPercepcionAcctType
	
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
	
	public int getC_RegPercep_Recib_ID()
	{
		return m_C_RegPercep_Recib_ID;
	}	//	getAPPercepcionAcctType
	

}	//	DocPercep
