/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_PAYMENTVALORES
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.906
 */
public class X_C_INVOICEPERCEP extends PO {
	/** Standard Constructor */
	public X_C_INVOICEPERCEP(Properties ctx, int C_INVOICEPERCEP_ID,
			String trxName) {
		super(ctx, C_INVOICEPERCEP_ID, trxName);

	}

	/** Load Constructor */
	public X_C_INVOICEPERCEP(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_REGPERCEP_RECIB */
	public static final String Table_Name = "C_INVOICEPERCEP";

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

	/** Set IMPUESTO */
	public void setIMPUESTO(String IMPUESTO) {
		set_Value("IMPUESTO", IMPUESTO);
	}

	/** Get IMPUESTO */
	public String getIMPUESTO() {
		return (String) get_Value("IMPUESTO");
	}

	/** Get AMOUNT */
	public BigDecimal getAMOUNT() {
		return (BigDecimal) get_Value("Amount");
	}

	/** Set AMOUNT */
	public void setAMOUNT(BigDecimal amount) {
		set_Value("Amount", amount);
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
	 * Set Invoice. Invoice Identifier
	 */
	public void setC_Invoice_ID(int C_Invoice_ID) {
		if (C_Invoice_ID != 0)
			set_Value("C_Invoice_ID", C_Invoice_ID);
	}

	/**
	 * Get InvoicePercep. Invoice Identifier
	 */
	public int getC_InvoicePercep_ID() {
		Integer ii = (Integer) get_Value("C_InvoicePercep_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set InvoicePercep. Invoice Identifier
	 */
	public void setC_InvoicePercep_ID(int C_InvoicePercep_ID) {
		if (C_InvoicePercep_ID != 0)
			set_Value("C_InvoicePercep_ID", C_InvoicePercep_ID);
	}

	/**
	 * Get REGPERCEP_RECIB. Invoice Identifier
	 */
	public int getC_REGPERCEP_RECIB_ID() {
		Integer ii = (Integer) get_Value("C_REGPERCEP_RECIB_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set REGPERCEP_RECIB. Invoice Identifier
	 */
	public void setC_REGPERCEP_RECIB_ID(int C_REGPERCEP_RECIB_ID) {
		if (C_REGPERCEP_RECIB_ID != 0)
			set_Value("C_REGPERCEP_RECIB_ID", C_REGPERCEP_RECIB_ID);
	}

	/**
	 * Set Date. Date Trx.
	 */
	public void setDateTrx(Timestamp DATETRX) {
		set_Value("DateTrx", DATETRX);
	}

	/**
	 * Get Date. Date Trx.
	 */
	public Timestamp getDateTrx() {
		return (Timestamp) get_Value("DateTrx");
	}

	/**
	 * Set Aduanera.
	 */
	public void setAduanera(boolean ESADUANERA) {
		set_ValueNoCheck("ESADUANERA", new Boolean(ESADUANERA));
	}

	/**
	 * Get Aduanera.
	 */
	public boolean IsAduanera() {
		Object oo = get_Value("ESADUANERA");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Bancaria.
	 */
	public void setBancaria(boolean ESBANCARIA) {
		set_ValueNoCheck("ESBANCARIA", new Boolean(ESBANCARIA));
	}

	/**
	 * Get Bancaria.
	 */
	public boolean IsBancaria() {
		Object oo = get_Value("ESBANCARIA");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

}
