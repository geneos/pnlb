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

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Invoice Tax Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MInvoiceTax.java,v 1.18 2005/10/08 02:02:29 jjanke Exp $
 */
public class MInvoiceTax extends X_C_InvoiceTax
{
	/**
	 * 	Get Tax Line for Invoice Line
	 *	@param line invoice line
	 *	@param precision currency precision
	 *	@param oldTax if true old tax is returned
	 *	@param trxName transaction name
	 *	@return existing or new tax
	 */
	public static MInvoiceTax get (MInvoiceLine line, int precision, 
		boolean oldTax, String trxName)
	{
		MInvoiceTax retValue = null;
		if (line == null || line.getC_Invoice_ID() == 0)
			return null;
		int C_Tax_ID = line.getC_Tax_ID();
		if (oldTax && line.is_ValueChanged("C_Tax_ID"))
		{
			Object old = line.get_ValueOld("C_Tax_ID");
			if (old == null)
				return null;
			C_Tax_ID = ((Integer)old).intValue();
		}
		if (C_Tax_ID == 0)
		{
			s_log.warning("get C_Tax_ID=0");
			return null;
		}
		
		String sql = "SELECT * FROM C_InvoiceTax WHERE C_Invoice_ID=? AND C_Tax_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, line.getC_Invoice_ID());
			pstmt.setInt (2, C_Tax_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				retValue = new MInvoiceTax (line.getCtx(), rs, trxName);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "get", e); 
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
		if (retValue != null)
		{
			retValue.set_TrxName(trxName);
			retValue.setPrecision(precision);
			s_log.fine("get (old=" + oldTax + ") " + retValue);
			return retValue;
		}
		
		//	Create New
		retValue = new MInvoiceTax(line.getCtx(), 0, trxName);
		retValue.set_TrxName(trxName);
		retValue.setClientOrg(line);
		retValue.setC_Invoice_ID(line.getC_Invoice_ID());
		retValue.setC_Tax_ID(line.getC_Tax_ID());
		retValue.setPrecision(precision);
		retValue.setIsTaxIncluded(line.isTaxIncluded());
		s_log.fine("get (new) " + retValue);
		return retValue;
	}	//	get
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MInvoiceTax.class);
	
	
	/**************************************************************************
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 */
	public MInvoiceTax (Properties ctx, int ignored, String trxName)
	{
		super(ctx, 0, trxName);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
		setTaxAmt (Env.ZERO);
		setTaxBaseAmt (Env.ZERO);
		setIsTaxIncluded(false);
	}	//	MInvoiceTax

	/**
	 * 	Load Constructor.
	 * 	Set Precision and TaxIncluded for tax calculations!
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MInvoiceTax (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MInvoiceTax
	
	/** Tax							*/
	private MTax 		m_tax = null;
	/** Cached Precision			*/
	private Integer		m_precision = null;
	
	
	/**
	 * 	Get Precision
	 * 	@return Returns the precision or 2
	 */
	private int getPrecision ()
	{
		if (m_precision == null)
			return 2;
		return m_precision.intValue();
	}	//	getPrecision

	/**
	 * 	Set Precision
	 *	@param precision The precision to set.
	 */
	protected void setPrecision (int precision)
	{
		m_precision = new Integer(precision);
	}	//	setPrecision

	/**
	 * 	Get Tax
	 *	@return tax
	 */
	protected MTax getTax()
	{
		if (m_tax == null)
			m_tax = MTax.get(getCtx(), getC_Tax_ID(), get_TrxName());
		return m_tax;
	}	//	getTax
	
	
	/**************************************************************************
	 * 	Calculate/Set Tax Base Amt from Invoice Lines
	 */
	public boolean calculateTaxFromLines ()
	{
		BigDecimal taxBaseAmt = Env.ZERO;
		BigDecimal taxAmt = Env.ZERO;
		//
		boolean documentLevel = getTax().isDocumentLevel();
		MTax tax = getTax();
		//
		String sql = "SELECT LineNetAmt, COALESCE(TaxAmt,0) FROM C_InvoiceLine WHERE C_Invoice_ID=? AND C_Tax_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getC_Invoice_ID());
			pstmt.setInt (2, getC_Tax_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				BigDecimal baseAmt = rs.getBigDecimal(1);
				BigDecimal amt = rs.getBigDecimal(2);
				if (amt == null)
					amt = Env.ZERO;
				//
				taxBaseAmt = taxBaseAmt.add(baseAmt);
				if (!documentLevel && amt.signum() == 0)	// calculate line tax
					amt = tax.calculateTax(baseAmt, isTaxIncluded(), getPrecision());
				taxAmt = taxAmt.add(amt);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "setTaxBaseAmt", e);
			taxBaseAmt = null;
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
		if (taxBaseAmt == null)
			return false;
		
		//	Calculate Tax
		if (taxAmt.signum() == 0)
			taxAmt = tax.calculateTax(taxBaseAmt, isTaxIncluded(), getPrecision());
		setTaxAmt(taxAmt);

		//	Set Base
		if (isTaxIncluded())
			setTaxBaseAmt (taxBaseAmt.subtract(taxAmt));
		else
			setTaxBaseAmt (taxBaseAmt);
		return true;
	}	//	calculateTaxFromLines

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MInvoiceTax[");
		sb.append("C_Invoice_ID=").append(getC_Invoice_ID())
			.append(",C_Tax_ID=").append(getC_Tax_ID())
			.append(", Base=").append(getTaxBaseAmt()).append(",Tax=").append(getTaxAmt())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	MInvoiceTax
