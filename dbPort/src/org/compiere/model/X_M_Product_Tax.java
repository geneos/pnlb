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
@SuppressWarnings("serial")
public class X_M_Product_Tax extends PO {
	/** Standard Constructor */
	public X_M_Product_Tax(Properties ctx, int M_Product_Tax_ID, String trxName) {
		super(ctx, M_Product_Tax_ID, trxName);

	}

	/** Load Constructor */
	public X_M_Product_Tax(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_PRODUCT_TAX */
	public static final String Table_Name = "M_Product_Tax";

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

	/**
	 * Get Impuesto. Tax Identifier
	 */
	public int getC_Tax_ID() {
		Integer ii = (Integer) get_Value("C_Tax_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Impuesto. Tax Identifier
	 */
	public void setC_Tax_ID(int C_Tax_ID) {
		if (C_Tax_ID != 0)
			set_Value("C_Tax_ID", C_Tax_ID);
	}

	/**
	 * Get Producto. Product Identifier
	 */
	public int getM_Product_ID() {
		Integer ii = (Integer) get_Value("M_Product_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Producto. Product Identifier
	 */
	public void setM_Product_ID(int M_Product_ID) {
		set_Value("M_Product_ID", M_Product_ID);
	}

	/**
	 * Get Cliente. BPartner Identifier
	 */
	public int getC_BPartner_ID() {
		Integer ii = (Integer) get_Value("C_BPartner_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Cliente. BPartner Identifier
	 */
	public void setC_BPartner_ID(int C_BPartner_ID) {
		set_Value("C_BPartner_ID", C_BPartner_ID);
	}

	/**
	 * Get Jurisdiccion. DocType Identifier
	 */
	public int getC_Jurisdiccion_ID() {
		Integer ii = (Integer) get_Value("C_JURISDICCION_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Jurisdiccion. DocType Identifier
	 */
	public void setC_Jurisdiccion_ID(int C_Jurisdiccion_ID) {
		set_Value("C_JURISDICCION_ID", C_Jurisdiccion_ID);
	}

	/**
	 * Get Condicion IVA. DocType Identifier
	 */
	public int getCondicionIVA_ID() {
		Integer ii = (Integer) get_Value("CONDICIONIVA_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Condicion IVA. DocType Identifier
	 */
	public void setCondicionIVA_ID(int CondicionIVA_ID) {
		set_Value("CONDICIONIVA_ID", CondicionIVA_ID);
	}

	/**
	 * Get Condicion. Product Tax Identifier
	 */
	public int getM_Product_Tax_ID() {
		Integer ii = (Integer) get_Value("M_Product_Tax_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Condicion. Product Tax Identifier
	 */
	public void setM_Product_Tax_ID(int M_Product_Tax_ID) {
		if (M_Product_Tax_ID != 0)
			set_Value("M_Product_Tax_ID", M_Product_Tax_ID);
	}
}
