/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_InvoiceBatch
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.515
 */
public class X_C_InvoiceBatch extends PO {
	/** Standard Constructor */
	public X_C_InvoiceBatch(Properties ctx, int C_InvoiceBatch_ID,
			String trxName) {
		super(ctx, C_InvoiceBatch_ID, trxName);
		/**
		 * if (C_InvoiceBatch_ID == 0) { setC_Currency_ID (0); //
		 * 
		 * @$C_Currency_ID@ setC_InvoiceBatch_ID (0); setControlAmt (Env.ZERO); //
		 *                  0 setDateDoc (new
		 *                  Timestamp(System.currentTimeMillis())); //
		 * @#Date@ setDocumentAmt (Env.ZERO); setDocumentNo (null); setIsSOTrx
		 *         (false); // N setProcessed (false); setSalesRep_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_C_InvoiceBatch(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_InvoiceBatch */
	public static final String Table_Name = "C_InvoiceBatch";

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
		StringBuffer sb = new StringBuffer("X_C_InvoiceBatch[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Currency Type. Currency Conversion Rate Type
	 */
	public void setC_ConversionType_ID(int C_ConversionType_ID) {
		if (C_ConversionType_ID <= 0)
			set_Value("C_ConversionType_ID", null);
		else
			set_Value("C_ConversionType_ID", new Integer(C_ConversionType_ID));
	}

	/**
	 * Get Currency Type. Currency Conversion Rate Type
	 */
	public int getC_ConversionType_ID() {
		Integer ii = (Integer) get_Value("C_ConversionType_ID");
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
		set_Value("C_Currency_ID", new Integer(C_Currency_ID));
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
	 * Set Invoice Batch. Expense Invoice Batch Header
	 */
	public void setC_InvoiceBatch_ID(int C_InvoiceBatch_ID) {
		if (C_InvoiceBatch_ID < 1)
			throw new IllegalArgumentException(
					"C_InvoiceBatch_ID is mandatory.");
		set_ValueNoCheck("C_InvoiceBatch_ID", new Integer(C_InvoiceBatch_ID));
	}

	/**
	 * Get Invoice Batch. Expense Invoice Batch Header
	 */
	public int getC_InvoiceBatch_ID() {
		Integer ii = (Integer) get_Value("C_InvoiceBatch_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Control Amount. If not zero, the Debit amount of the document must be
	 * equal this amount
	 */
	public void setControlAmt(BigDecimal ControlAmt) {
		if (ControlAmt == null)
			throw new IllegalArgumentException("ControlAmt is mandatory.");
		set_Value("ControlAmt", ControlAmt);
	}

	/**
	 * Get Control Amount. If not zero, the Debit amount of the document must be
	 * equal this amount
	 */
	public BigDecimal getControlAmt() {
		BigDecimal bd = (BigDecimal) get_Value("ControlAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Document Date. Date of the Document
	 */
	public void setDateDoc(Timestamp DateDoc) {
		if (DateDoc == null)
			throw new IllegalArgumentException("DateDoc is mandatory.");
		set_Value("DateDoc", DateDoc);
	}

	/**
	 * Get Document Date. Date of the Document
	 */
	public Timestamp getDateDoc() {
		return (Timestamp) get_Value("DateDoc");
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
	 * Set Document Amt. Document Amount
	 */
	public void setDocumentAmt(BigDecimal DocumentAmt) {
		if (DocumentAmt == null)
			throw new IllegalArgumentException("DocumentAmt is mandatory.");
		set_ValueNoCheck("DocumentAmt", DocumentAmt);
	}

	/**
	 * Get Document Amt. Document Amount
	 */
	public BigDecimal getDocumentAmt() {
		BigDecimal bd = (BigDecimal) get_Value("DocumentAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Document No. Document sequence number of the document
	 */
	public void setDocumentNo(String DocumentNo) {
		if (DocumentNo == null)
			throw new IllegalArgumentException("DocumentNo is mandatory.");
		if (DocumentNo.length() > 30) {
			log.warning("Length > 30 - truncated");
			DocumentNo = DocumentNo.substring(0, 29);
		}
		set_Value("DocumentNo", DocumentNo);
	}

	/**
	 * Get Document No. Document sequence number of the document
	 */
	public String getDocumentNo() {
		return (String) get_Value("DocumentNo");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getDocumentNo());
	}

	/**
	 * Set Sales Transaction. This is a Sales Transaction
	 */
	public void setIsSOTrx(boolean IsSOTrx) {
		set_Value("IsSOTrx", new Boolean(IsSOTrx));
	}

	/**
	 * Get Sales Transaction. This is a Sales Transaction
	 */
	public boolean isSOTrx() {
		Object oo = get_Value("IsSOTrx");
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

	/** Set Process Now */
	public void setProcessing(boolean Processing) {
		set_Value("Processing", new Boolean(Processing));
	}

	/** Get Process Now */
	public boolean isProcessing() {
		Object oo = get_Value("Processing");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** SalesRep_ID AD_Reference_ID=190 */
	public static final int SALESREP_ID_AD_Reference_ID = 190;

	/**
	 * Set Sales Representative. Sales Representative or Company Agent
	 */
	public void setSalesRep_ID(int SalesRep_ID) {
		if (SalesRep_ID < 1)
			throw new IllegalArgumentException("SalesRep_ID is mandatory.");
		set_Value("SalesRep_ID", new Integer(SalesRep_ID));
	}

	/**
	 * Get Sales Representative. Sales Representative or Company Agent
	 */
	public int getSalesRep_ID() {
		Integer ii = (Integer) get_Value("SalesRep_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
