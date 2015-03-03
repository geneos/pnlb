/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_InvoiceTax
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.609
 */
public class X_C_InvoiceTax extends PO {
	/** Standard Constructor */
	public X_C_InvoiceTax(Properties ctx, int C_InvoiceTax_ID, String trxName) {
		super(ctx, C_InvoiceTax_ID, trxName);
		/**
		 * if (C_InvoiceTax_ID == 0) { setC_Invoice_ID (0); setC_Tax_ID (0);
		 * setIsTaxIncluded (false); setProcessed (false); setTaxAmt (Env.ZERO);
		 * setTaxBaseAmt (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_C_InvoiceTax(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_InvoiceTax */
	public static final String Table_Name = "C_InvoiceTax";

	/** AD_Table_ID */
	public int Table_ID;

	protected KeyNamePair Model;

	/** Load Meta Data */
	protected POInfo initPO(Properties ctx) {
		POInfo info = initPO(ctx, Table_Name);
		Table_ID = info.getAD_Table_ID();
		Model = new KeyNamePair(Table_ID, Table_Name);
		return info;
	}

	protected BigDecimal accessLevel = new BigDecimal(1);

	/** AccessLevel 1 - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_C_InvoiceTax[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Invoice. Invoice Identifier
	 */
	public void setC_Invoice_ID(int C_Invoice_ID) {
		if (C_Invoice_ID < 1)
			throw new IllegalArgumentException("C_Invoice_ID is mandatory.");
		set_ValueNoCheck("C_Invoice_ID", new Integer(C_Invoice_ID));
	}

	/**
	 * Get Invoice. Invoice Identifier
	 */
	public int getC_Invoice_ID() {
		Integer ii = (Integer) get_Value("C_Invoice_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Tax. Tax identifier
	 */
	public void setC_Tax_ID(int C_Tax_ID) {
		if (C_Tax_ID < 1)
			throw new IllegalArgumentException("C_Tax_ID is mandatory.");
		set_ValueNoCheck("C_Tax_ID", new Integer(C_Tax_ID));
	}

	/**
	 * Get Tax. Tax identifier
	 */
	public int getC_Tax_ID() {
		Integer ii = (Integer) get_Value("C_Tax_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Price includes Tax. Tax is included in the price
	 */
	public void setIsTaxIncluded(boolean IsTaxIncluded) {
		set_Value("IsTaxIncluded", new Boolean(IsTaxIncluded));
	}

	/**
	 * Get Price includes Tax. Tax is included in the price
	 */
	public boolean isTaxIncluded() {
		Object oo = get_Value("IsTaxIncluded");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Processed. The document has been processed
	 */
	public void setProcessed(boolean Processed) {
		set_Value("Processed", new Boolean(Processed));
	}

	/**
	 * Get Processed. The document has been processed
	 */
	public boolean isProcessed() {
		Object oo = get_Value("Processed");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Tax Amount. Tax Amount for a document
	 */
	public void setTaxAmt(BigDecimal TaxAmt) {
		if (TaxAmt == null)
			throw new IllegalArgumentException("TaxAmt is mandatory.");
		set_ValueNoCheck("TaxAmt", TaxAmt);
	}

	/**
	 * Get Tax Amount. Tax Amount for a document
	 */
	public BigDecimal getTaxAmt() {
		BigDecimal bd = (BigDecimal) get_Value("TaxAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Tax base Amount. Base for calculating the tax amount
	 */
	public void setTaxBaseAmt(BigDecimal TaxBaseAmt) {
		if (TaxBaseAmt == null)
			throw new IllegalArgumentException("TaxBaseAmt is mandatory.");
		set_ValueNoCheck("TaxBaseAmt", TaxBaseAmt);
	}

	/**
	 * Get Tax base Amount. Base for calculating the tax amount
	 */
	public BigDecimal getTaxBaseAmt() {
		BigDecimal bd = (BigDecimal) get_Value("TaxBaseAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
