/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_DiscountSchemaBreak
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.625
 */
public class X_M_DiscountSchemaBreak extends PO {
	/** Standard Constructor */
	public X_M_DiscountSchemaBreak(Properties ctx,
			int M_DiscountSchemaBreak_ID, String trxName) {
		super(ctx, M_DiscountSchemaBreak_ID, trxName);
		/**
		 * if (M_DiscountSchemaBreak_ID == 0) { setBreakDiscount (Env.ZERO);
		 * setBreakValue (Env.ZERO); setIsBPartnerFlatDiscount (false); // N
		 * setM_DiscountSchemaBreak_ID (0); setM_DiscountSchema_ID (0); setSeqNo
		 * (0); //
		 * 
		 * @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM
		 *             M_DiscountSchemaBreak WHERE
		 *             M_DiscountSchema_ID=@M_DiscountSchema_ID@ }
		 */
	}

	/** Load Constructor */
	public X_M_DiscountSchemaBreak(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_DiscountSchemaBreak */
	public static final String Table_Name = "M_DiscountSchemaBreak";

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
		StringBuffer sb = new StringBuffer("X_M_DiscountSchemaBreak[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Break Discount %. Trade Discount in Percent for the break level
	 */
	public void setBreakDiscount(BigDecimal BreakDiscount) {
		if (BreakDiscount == null)
			throw new IllegalArgumentException("BreakDiscount is mandatory.");
		set_Value("BreakDiscount", BreakDiscount);
	}

	/**
	 * Get Break Discount %. Trade Discount in Percent for the break level
	 */
	public BigDecimal getBreakDiscount() {
		BigDecimal bd = (BigDecimal) get_Value("BreakDiscount");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Break Value. Low Value of trade discount break level
	 */
	public void setBreakValue(BigDecimal BreakValue) {
		if (BreakValue == null)
			throw new IllegalArgumentException("BreakValue is mandatory.");
		set_Value("BreakValue", BreakValue);
	}

	/**
	 * Get Break Value. Low Value of trade discount break level
	 */
	public BigDecimal getBreakValue() {
		BigDecimal bd = (BigDecimal) get_Value("BreakValue");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set B.Partner Flat Discount. Use flat discount defined on Business
	 * Partner Level
	 */
	public void setIsBPartnerFlatDiscount(boolean IsBPartnerFlatDiscount) {
		set_Value("IsBPartnerFlatDiscount", new Boolean(IsBPartnerFlatDiscount));
	}

	/**
	 * Get B.Partner Flat Discount. Use flat discount defined on Business
	 * Partner Level
	 */
	public boolean isBPartnerFlatDiscount() {
		Object oo = get_Value("IsBPartnerFlatDiscount");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Discount Schema Break. Trade Discount Break
	 */
	public void setM_DiscountSchemaBreak_ID(int M_DiscountSchemaBreak_ID) {
		if (M_DiscountSchemaBreak_ID < 1)
			throw new IllegalArgumentException(
					"M_DiscountSchemaBreak_ID is mandatory.");
		set_ValueNoCheck("M_DiscountSchemaBreak_ID", new Integer(
				M_DiscountSchemaBreak_ID));
	}

	/**
	 * Get Discount Schema Break. Trade Discount Break
	 */
	public int getM_DiscountSchemaBreak_ID() {
		Integer ii = (Integer) get_Value("M_DiscountSchemaBreak_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Discount Schema. Schema to calculate the trade discount percentage
	 */
	public void setM_DiscountSchema_ID(int M_DiscountSchema_ID) {
		if (M_DiscountSchema_ID < 1)
			throw new IllegalArgumentException(
					"M_DiscountSchema_ID is mandatory.");
		set_ValueNoCheck("M_DiscountSchema_ID",
				new Integer(M_DiscountSchema_ID));
	}

	/**
	 * Get Discount Schema. Schema to calculate the trade discount percentage
	 */
	public int getM_DiscountSchema_ID() {
		Integer ii = (Integer) get_Value("M_DiscountSchema_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Product Category. Category of a Product
	 */
	public void setM_Product_Category_ID(int M_Product_Category_ID) {
		if (M_Product_Category_ID <= 0)
			set_Value("M_Product_Category_ID", null);
		else
			set_Value("M_Product_Category_ID", new Integer(
					M_Product_Category_ID));
	}

	/**
	 * Get Product Category. Category of a Product
	 */
	public int getM_Product_Category_ID() {
		Integer ii = (Integer) get_Value("M_Product_Category_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Product. Product, Service, Item
	 */
	public void setM_Product_ID(int M_Product_ID) {
		if (M_Product_ID <= 0)
			set_Value("M_Product_ID", null);
		else
			set_Value("M_Product_ID", new Integer(M_Product_ID));
	}

	/**
	 * Get Product. Product, Service, Item
	 */
	public int getM_Product_ID() {
		Integer ii = (Integer) get_Value("M_Product_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Sequence. Method of ordering records; lowest number comes first
	 */
	public void setSeqNo(int SeqNo) {
		set_Value("SeqNo", new Integer(SeqNo));
	}

	/**
	 * Get Sequence. Method of ordering records; lowest number comes first
	 */
	public int getSeqNo() {
		Integer ii = (Integer) get_Value("SeqNo");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getSeqNo()));
	}
}
