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
public class X_C_INVOICEPERCEP_SOTRX extends PO {
	/** Standard Constructor */
	public X_C_INVOICEPERCEP_SOTRX(Properties ctx, int C_INVOICEPERCEP_ID,
			String trxName) {
		super(ctx, C_INVOICEPERCEP_ID, trxName);

	}

	/** Load Constructor */
	public X_C_INVOICEPERCEP_SOTRX(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_PRODUCT_PERCEPTION */
	public static final String Table_Name = "C_INVOICEPERCEP_SOTRX";

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

	/** Set DESCRIPCION */
	public void setDescripcion(String DESCRIPCION) {
		set_Value("DESCRIPCION", DESCRIPCION);
	}

	/** Get DESCRIPCION */
	public String getDescripcion() {
		return (String) get_Value("DESCRIPCION");
	}

	/** Get MONTO */
	public BigDecimal getMonto() {
		return (BigDecimal) get_Value("MONTO");
	}

	/** Set MONTO */
	public void setMonto(BigDecimal amount) {
		set_Value("MONTO", amount);
	}

	/**
	 * Get Jurisdiccion. Jurisdiccion Identifier
	 */
	public int getC_Jurisdiccion_ID() {
		Integer ii = (Integer) get_Value("C_JURISDICCION_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Jurisdiccion. Jurisdiccion Identifier
	 */
	public void setC_Jurisdiccion_ID(int C_Jurisdiccion_ID) {
		set_Value("C_JURISDICCION_ID", C_Jurisdiccion_ID);
	}

	/** Set Otros. */
	public void setOtros(boolean Otros) {
		set_Value("OTROS", new Boolean(Otros));
	}

	/** Get Otros */
	public boolean isOtros() {
		Object oo = get_Value("OTROS");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Get Alicuota */
	public BigDecimal getAlicuota() {
		return (BigDecimal) get_Value("ALICUOTA");
	}

	/** Set Alicuota */
	public void setAlicuota(BigDecimal Alicuota) {
		set_Value("ALICUOTA", Alicuota);
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
	public int getC_InvoicePercep_SOTRX_ID() {
		Integer ii = (Integer) get_Value("C_INVOICEPERCEP_SOTRX_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set InvoicePercep. Invoice Identifier
	 */
	public void setC_InvoicePercep_ID(int C_InvoicePercep_ID) {
		if (C_InvoicePercep_ID != 0)
			set_Value("C_INVOICEPERCEP_SOTRX_ID", C_InvoicePercep_ID);
	}

}
