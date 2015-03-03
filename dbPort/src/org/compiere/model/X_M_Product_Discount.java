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
public class X_M_Product_Discount extends PO {
	/** Standard Constructor */
	public X_M_Product_Discount(Properties ctx, int M_Product_Tax_ID,
			String trxName) {
		super(ctx, M_Product_Tax_ID, trxName);

	}

	/** Load Constructor */
	public X_M_Product_Discount(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_PRODUCT_TAX */
	public static final String Table_Name = "M_Product_Discount";

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

	/** Get Discount. */
	public BigDecimal getDiscount() {
		BigDecimal ii = (BigDecimal) get_Value("Discount");
		if (ii == null)
			return BigDecimal.ZERO;
		return ii;
	}

	/** Set Discount. */
	public void setDiscount(BigDecimal discount) {
		set_Value("Discount", discount);
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

	/** Get Product Discount Identifier */
	public int getM_Product_Discount_ID() {
		Integer ii = (Integer) get_Value("M_Product_Discount_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Product Discount Identifier */
	public void setM_Product_Discount_ID(int M_Product_Discount_ID) {
		if (M_Product_Discount_ID != 0)
			set_Value("M_Product_Discount_ID", M_Product_Discount_ID);
	}

	/** Set Exclusive */
	public void setExclusivo(boolean exclusivo) {
		set_Value("Exclusivo", new Boolean(exclusivo));
	}

	/** Get Exclusive */
	public boolean isExclusivo() {
		Object oo = get_Value("Exclusivo");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

}
