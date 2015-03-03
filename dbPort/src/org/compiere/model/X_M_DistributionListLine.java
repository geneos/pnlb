/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_DistributionListLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.687
 */
public class X_M_DistributionListLine extends PO {
	/** Standard Constructor */
	public X_M_DistributionListLine(Properties ctx,
			int M_DistributionListLine_ID, String trxName) {
		super(ctx, M_DistributionListLine_ID, trxName);
		/**
		 * if (M_DistributionListLine_ID == 0) { setC_BPartner_ID (0);
		 * setC_BPartner_Location_ID (0); setM_DistributionListLine_ID (0);
		 * setM_DistributionList_ID (0); setMinQty (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_M_DistributionListLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_DistributionListLine */
	public static final String Table_Name = "M_DistributionListLine";

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
		StringBuffer sb = new StringBuffer("X_M_DistributionListLine[").append(
				get_ID()).append("]");
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getM_DistributionList_ID()));
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
	 * Set Ratio. Relative Ratio for Distributions
	 */
	public void setRatio(BigDecimal Ratio) {
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
