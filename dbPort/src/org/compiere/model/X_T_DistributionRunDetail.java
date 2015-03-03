/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_DistributionRunDetail
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.937
 */
public class X_T_DistributionRunDetail extends PO {
	/** Standard Constructor */
	public X_T_DistributionRunDetail(Properties ctx,
			int T_DistributionRunDetail_ID, String trxName) {
		super(ctx, T_DistributionRunDetail_ID, trxName);
		/**
		 * if (T_DistributionRunDetail_ID == 0) { setC_BPartner_ID (0);
		 * setC_BPartner_Location_ID (0); setM_DistributionListLine_ID (0);
		 * setM_DistributionList_ID (0); setM_DistributionRunLine_ID (0);
		 * setM_DistributionRun_ID (0); setM_Product_ID (0); setMinQty
		 * (Env.ZERO); setQty (Env.ZERO); setRatio (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_T_DistributionRunDetail(Properties ctx, ResultSet rs,
			String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_DistributionRunDetail */
	public static final String Table_Name = "T_DistributionRunDetail";

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
		StringBuffer sb = new StringBuffer("X_T_DistributionRunDetail[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Business Partner . Identifies a Business Partner
	 */
	public void setC_BPartner_ID(int C_BPartner_ID) {
		if (C_BPartner_ID < 1)
			throw new IllegalArgumentException("C_BPartner_ID is mandatory.");
		set_Value("C_BPartner_ID", new Integer(C_BPartner_ID));
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
	 * Set Partner Location. Identifies the (ship to) address for this Business
	 * Partner
	 */
	public void setC_BPartner_Location_ID(int C_BPartner_Location_ID) {
		if (C_BPartner_Location_ID < 1)
			throw new IllegalArgumentException(
					"C_BPartner_Location_ID is mandatory.");
		set_Value("C_BPartner_Location_ID", new Integer(C_BPartner_Location_ID));
	}

	/**
	 * Get Partner Location. Identifies the (ship to) address for this Business
	 * Partner
	 */
	public int getC_BPartner_Location_ID() {
		Integer ii = (Integer) get_Value("C_BPartner_Location_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Distribution List Line. Distribution List Line with Business Partner
	 * and Quantity/Percentage
	 */
	public void setM_DistributionListLine_ID(int M_DistributionListLine_ID) {
		if (M_DistributionListLine_ID < 1)
			throw new IllegalArgumentException(
					"M_DistributionListLine_ID is mandatory.");
		set_ValueNoCheck("M_DistributionListLine_ID", new Integer(
				M_DistributionListLine_ID));
	}

	/**
	 * Get Distribution List Line. Distribution List Line with Business Partner
	 * and Quantity/Percentage
	 */
	public int getM_DistributionListLine_ID() {
		Integer ii = (Integer) get_Value("M_DistributionListLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Distribution List. Distribution Lists allow to distribute products to
	 * a selected list of partners
	 */
	public void setM_DistributionList_ID(int M_DistributionList_ID) {
		if (M_DistributionList_ID < 1)
			throw new IllegalArgumentException(
					"M_DistributionList_ID is mandatory.");
		set_ValueNoCheck("M_DistributionList_ID", new Integer(
				M_DistributionList_ID));
	}

	/**
	 * Get Distribution List. Distribution Lists allow to distribute products to
	 * a selected list of partners
	 */
	public int getM_DistributionList_ID() {
		Integer ii = (Integer) get_Value("M_DistributionList_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Distribution Run Line. Distribution Run Lines define Distribution
	 * List, the Product and Quantiries
	 */
	public void setM_DistributionRunLine_ID(int M_DistributionRunLine_ID) {
		if (M_DistributionRunLine_ID < 1)
			throw new IllegalArgumentException(
					"M_DistributionRunLine_ID is mandatory.");
		set_ValueNoCheck("M_DistributionRunLine_ID", new Integer(
				M_DistributionRunLine_ID));
	}

	/**
	 * Get Distribution Run Line. Distribution Run Lines define Distribution
	 * List, the Product and Quantiries
	 */
	public int getM_DistributionRunLine_ID() {
		Integer ii = (Integer) get_Value("M_DistributionRunLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Distribution Run. Distribution Run create Orders to distribute
	 * products to a selected list of partners
	 */
	public void setM_DistributionRun_ID(int M_DistributionRun_ID) {
		if (M_DistributionRun_ID < 1)
			throw new IllegalArgumentException(
					"M_DistributionRun_ID is mandatory.");
		set_ValueNoCheck("M_DistributionRun_ID", new Integer(
				M_DistributionRun_ID));
	}

	/**
	 * Get Distribution Run. Distribution Run create Orders to distribute
	 * products to a selected list of partners
	 */
	public int getM_DistributionRun_ID() {
		Integer ii = (Integer) get_Value("M_DistributionRun_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getM_DistributionRun_ID()));
	}

	/**
	 * Set Product. Product, Service, Item
	 */
	public void setM_Product_ID(int M_Product_ID) {
		if (M_Product_ID < 1)
			throw new IllegalArgumentException("M_Product_ID is mandatory.");
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
	 * Set Minimum Quantity. Minimum quantity for the business partner
	 */
	public void setMinQty(BigDecimal MinQty) {
		if (MinQty == null)
			throw new IllegalArgumentException("MinQty is mandatory.");
		set_Value("MinQty", MinQty);
	}

	/**
	 * Get Minimum Quantity. Minimum quantity for the business partner
	 */
	public BigDecimal getMinQty() {
		BigDecimal bd = (BigDecimal) get_Value("MinQty");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Quantity. Quantity
	 */
	public void setQty(BigDecimal Qty) {
		if (Qty == null)
			throw new IllegalArgumentException("Qty is mandatory.");
		set_Value("Qty", Qty);
	}

	/**
	 * Get Quantity. Quantity
	 */
	public BigDecimal getQty() {
		BigDecimal bd = (BigDecimal) get_Value("Qty");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Ratio. Relative Ratio for Distributions
	 */
	public void setRatio(BigDecimal Ratio) {
		if (Ratio == null)
			throw new IllegalArgumentException("Ratio is mandatory.");
		set_Value("Ratio", Ratio);
	}

	/**
	 * Get Ratio. Relative Ratio for Distributions
	 */
	public BigDecimal getRatio() {
		BigDecimal bd = (BigDecimal) get_Value("Ratio");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
