/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for A_Asset_Retirement
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.218
 */
public class X_A_Asset_Retirement extends PO {
	/** Standard Constructor */
	public X_A_Asset_Retirement(Properties ctx, int A_Asset_Retirement_ID,
			String trxName) {
		super(ctx, A_Asset_Retirement_ID, trxName);
		/**
		 * if (A_Asset_Retirement_ID == 0) { setA_Asset_ID (0);
		 * setA_Asset_Retirement_ID (0); setAssetMarketValueAmt (Env.ZERO);
		 * setAssetValueAmt (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_A_Asset_Retirement(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=A_Asset_Retirement */
	public static final String Table_Name = "A_Asset_Retirement";

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
		StringBuffer sb = new StringBuffer("X_A_Asset_Retirement[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Asset. Asset used internally or by customers
	 */
	public void setA_Asset_ID(int A_Asset_ID) {
		if (A_Asset_ID < 1)
			throw new IllegalArgumentException("A_Asset_ID is mandatory.");
		set_ValueNoCheck("A_Asset_ID", new Integer(A_Asset_ID));
	}

	/**
	 * Get Asset. Asset used internally or by customers
	 */
	public int getA_Asset_ID() {
		Integer ii = (Integer) get_Value("A_Asset_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Asset Retirement. Internally used asset is not longer used.
	 */
	public void setA_Asset_Retirement_ID(int A_Asset_Retirement_ID) {
		if (A_Asset_Retirement_ID < 1)
			throw new IllegalArgumentException(
					"A_Asset_Retirement_ID is mandatory.");
		set_ValueNoCheck("A_Asset_Retirement_ID", new Integer(
				A_Asset_Retirement_ID));
	}

	/**
	 * Get Asset Retirement. Internally used asset is not longer used.
	 */
	public int getA_Asset_Retirement_ID() {
		Integer ii = (Integer) get_Value("A_Asset_Retirement_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getA_Asset_Retirement_ID()));
	}

	/**
	 * Set Market value Amount. Market value of the asset
	 */
	public void setAssetMarketValueAmt(BigDecimal AssetMarketValueAmt) {
		if (AssetMarketValueAmt == null)
			throw new IllegalArgumentException(
					"AssetMarketValueAmt is mandatory.");
		set_Value("AssetMarketValueAmt", AssetMarketValueAmt);
	}

	/**
	 * Get Market value Amount. Market value of the asset
	 */
	public BigDecimal getAssetMarketValueAmt() {
		BigDecimal bd = (BigDecimal) get_Value("AssetMarketValueAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Asset value. Book Value of the asset
	 */
	public void setAssetValueAmt(BigDecimal AssetValueAmt) {
		if (AssetValueAmt == null)
			throw new IllegalArgumentException("AssetValueAmt is mandatory.");
		set_Value("AssetValueAmt", AssetValueAmt);
	}

	/**
	 * Get Asset value. Book Value of the asset
	 */
	public BigDecimal getAssetValueAmt() {
		BigDecimal bd = (BigDecimal) get_Value("AssetValueAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Invoice Line. Invoice Detail Line
	 */
	public void setC_InvoiceLine_ID(int C_InvoiceLine_ID) {
		if (C_InvoiceLine_ID <= 0)
			set_Value("C_InvoiceLine_ID", null);
		else
			set_Value("C_InvoiceLine_ID", new Integer(C_InvoiceLine_ID));
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
}
