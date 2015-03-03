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
public class X_M_PRODUCT_PERCEPTION extends PO {
	/** Standard Constructor */
	public X_M_PRODUCT_PERCEPTION(Properties ctx, int M_PRODUCT_PERCEPTION_ID,
			String trxName) {
		super(ctx, M_PRODUCT_PERCEPTION_ID, trxName);

	}

	/** Load Constructor */
	public X_M_PRODUCT_PERCEPTION(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_PRODUCT_PERCEPTION */
	public static final String Table_Name = "M_PRODUCT_PERCEPTION";

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

	/** Set ALICUOTAMT */
	public void setAlicuotaMT(BigDecimal alicuota) {
		set_Value("ALICUOTAMT", alicuota);
	}

	/** Get ALICUOTAMT */
	public BigDecimal getAlicuotaMT() {
		BigDecimal bd = (BigDecimal) get_Value("ALICUOTAMT");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set ALICUOTARI */
	public void setAlicuotaRI(BigDecimal alicuota) {
		set_Value("ALICUOTARI", alicuota);
	}

	/** Get ALICUOTARI */
	public BigDecimal getAlicuotaRI() {
		BigDecimal bd = (BigDecimal) get_Value("ALICUOTARI");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set NAME */
	public void setName(String name) {
		set_Value("NAME", name);
	}

	/** Get NAME */
	public String getName() {
		return (String) get_Value("NAME");
	}

	/**
	 * Get Product. Product Identifier
	 */
	public int getM_Product_ID() {
		Integer ii = (Integer) get_Value("M_PRODUCT_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Product. Product Identifier
	 */
	public void setM_Product_ID(int M_Product_ID) {
		if (M_Product_ID != 0)
			set_Value("M_PRODUCT_ID", M_Product_ID);
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
		if (C_Jurisdiccion_ID != 0)
			set_Value("C_JURISDICCION_ID", C_Jurisdiccion_ID);
	}

}
