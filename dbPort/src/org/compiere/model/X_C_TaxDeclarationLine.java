/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_TaxDeclarationLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.765
 */
public class X_C_TaxDeclarationLine extends PO {
	/** Standard Constructor */
	public X_C_TaxDeclarationLine(Properties ctx, int C_TaxDeclarationLine_ID,
			String trxName) {
		super(ctx, C_TaxDeclarationLine_ID, trxName);
		/**
		 * if (C_TaxDeclarationLine_ID == 0) { setC_BPartner_ID (0);
		 * setC_Currency_ID (0); setC_TaxDeclarationLine_ID (0);
		 * setC_TaxDeclaration_ID (0); setC_Tax_ID (0); setDateAcct (new
		 * Timestamp(System.currentTimeMillis())); setIsManual (true); // Y
		 * setLine (0); setTaxAmt (Env.ZERO); setTaxBaseAmt (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_C_TaxDeclarationLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_TaxDeclarationLine */
	public static final String Table_Name = "C_TaxDeclarationLine";

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

	protected BigDecimal accessLevel = new BigDecimal(3);

	/** AccessLevel 3 - Client - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_C_TaxDeclarationLine[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Allocation Line. Allocation Line
	 */
	public void setC_AllocationLine_ID(int C_AllocationLine_ID) {
		if (C_AllocationLine_ID <= 0)
			set_ValueNoCheck("C_AllocationLine_ID", null);
		else
			set_ValueNoCheck("C_AllocationLine_ID", new Integer(
					C_AllocationLine_ID));
	}

	/**
	 * Get Allocation Line. Allocation Line
	 */
	public int getC_AllocationLine_ID() {
		Integer ii = (Integer) get_Value("C_AllocationLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Business Partner . Identifies a Business Partner
	 */
	public void setC_BPartner_ID(int C_BPartner_ID) {
		if (C_BPartner_ID < 1)
			throw new IllegalArgumentException("C_BPartner_ID is mandatory.");
		set_ValueNoCheck("C_BPartner_ID", new Integer(C_BPartner_ID));
	}

	/**
	 * Get Business Partner . Identifies a Business Partner
	 */
	public int getC_BPartner_ID() {
		Integer ii = (Integer) get_Value("C_BPartner_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Currency. The Currency for this record
	 */
	public void setC_Currency_ID(int C_Currency_ID) {
		if (C_Currency_ID < 1)
			throw new IllegalArgumentException("C_Currency_ID is mandatory.");
		set_ValueNoCheck("C_Currency_ID", new Integer(C_Currency_ID));
	}

	/**
	 * Get Currency. The Currency for this record
	 */
	public int getC_Currency_ID() {
		Integer ii = (Integer) get_Value("C_Currency_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Invoice Line. Invoice Detail Line
	 */
	public void setC_InvoiceLine_ID(int C_InvoiceLine_ID) {
		if (C_InvoiceLine_ID <= 0)
			set_ValueNoCheck("C_InvoiceLine_ID", null);
		else
			set_ValueNoCheck("C_InvoiceLine_ID", new Integer(C_InvoiceLine_ID));
	}

	/**
	 * Get Invoice Line. Invoice Detail Line
	 */
	public int getC_InvoiceLine_ID() {
		Integer ii = (Integer) get_Value("C_InvoiceLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Invoice. Invoice Identifier
	 */
	public void setC_Invoice_ID(int C_Invoice_ID) {
		if (C_Invoice_ID <= 0)
			set_ValueNoCheck("C_Invoice_ID", null);
		else
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
	 * Set Tax Declaration Line. Tax Declaration Document Information
	 */
	public void setC_TaxDeclarationLine_ID(int C_TaxDeclarationLine_ID) {
		if (C_TaxDeclarationLine_ID < 1)
			throw new IllegalArgumentException(
					"C_TaxDeclarationLine_ID is mandatory.");
		set_ValueNoCheck("C_TaxDeclarationLine_ID", new Integer(
				C_TaxDeclarationLine_ID));
	}

	/**
	 * Get Tax Declaration Line. Tax Declaration Document Information
	 */
	public int getC_TaxDeclarationLine_ID() {
		Integer ii = (Integer) get_Value("C_TaxDeclarationLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Tax Declaration. Define the declaration to the tax authorities
	 */
	public void setC_TaxDeclaration_ID(int C_TaxDeclaration_ID) {
		if (C_TaxDeclaration_ID < 1)
			throw new IllegalArgumentException(
					"C_TaxDeclaration_ID is mandatory.");
		set_ValueNoCheck("C_TaxDeclaration_ID",
				new Integer(C_TaxDeclaration_ID));
	}

	/**
	 * Get Tax Declaration. Define the declaration to the tax authorities
	 */
	public int getC_TaxDeclaration_ID() {
		Integer ii = (Integer) get_Value("C_TaxDeclaration_ID");
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
	 * Set Account Date. Accounting Date
	 */
	public void setDateAcct(Timestamp DateAcct) {
		if (DateAcct == null)
			throw new IllegalArgumentException("DateAcct is mandatory.");
		set_ValueNoCheck("DateAcct", DateAcct);
	}

	/**
	 * Get Account Date. Accounting Date
	 */
	public Timestamp getDateAcct() {
		return (Timestamp) get_Value("DateAcct");
	}

	/**
	 * Set Description. Optional short description of the record
	 */
	public void setDescription(String Description) {
		if (Description != null && Description.length() > 255) {
			log.warning("Length > 255 - truncated");
			Description = Description.substring(0, 254);
		}
		set_Value("Description", Description);
	}

	/**
	 * Get Description. Optional short description of the record
	 */
	public String getDescription() {
		return (String) get_Value("Description");
	}

	/**
	 * Set Manual. This is a manual process
	 */
	public void setIsManual(boolean IsManual) {
		set_ValueNoCheck("IsManual", new Boolean(IsManual));
	}

	/**
	 * Get Manual. This is a manual process
	 */
	public boolean isManual() {
		Object oo = get_Value("IsManual");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Line No. Unique line for this document
	 */
	public void setLine(int Line) {
		set_Value("Line", new Integer(Line));
	}

	/**
	 * Get Line No. Unique line for this document
	 */
	public int getLine() {
		Integer ii = (Integer) get_Value("Line");
		if (ii == null)
			return 0;
		return ii.intValue();
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
