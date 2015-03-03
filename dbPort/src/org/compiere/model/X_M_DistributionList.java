/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_DistributionList
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.671
 */
public class X_M_DistributionList extends PO {
	/** Standard Constructor */
	public X_M_DistributionList(Properties ctx, int M_DistributionList_ID,
			String trxName) {
		super(ctx, M_DistributionList_ID, trxName);
		/**
		 * if (M_DistributionList_ID == 0) { setM_DistributionList_ID (0);
		 * setName (null); }
		 */
	}

	/** Load Constructor */
	public X_M_DistributionList(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_DistributionList */
	public static final String Table_Name = "M_DistributionList";

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
		StringBuffer sb = new StringBuffer("X_M_DistributionList[").append(
				get_ID()).append("]");
		return sb.toString();
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
	 * Set Comment/Help. Comment or Hint
	 */
	public void setHelp(String Help) {
		if (Help != null && Help.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Help = Help.substring(0, 1999);
		}
		set_Value("Help", Help);
	}

	/**
	 * Get Comment/Help. Comment or Hint
	 */
	public String getHelp() {
		return (String) get_Value("Help");
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
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name == null)
			throw new IllegalArgumentException("Name is mandatory.");
		if (Name.length() > 60) {
			log.warning("Length > 60 - truncated");
			Name = Name.substring(0, 59);
		}
		set_Value("Name", Name);
	}

	/**
	 * Get Name. Alphanumeric identifier of the entity
	 */
	public String getName() {
		return (String) get_Value("Name");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getName());
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

	/**
	 * Set Total Ratio. Total of relative weight in a distribution
	 */
	public void setRatioTotal(BigDecimal RatioTotal) {
		set_Value("RatioTotal", RatioTotal);
	}

	/**
	 * Get Total Ratio. Total of relative weight in a distribution
	 */
	public BigDecimal getRatioTotal() {
		BigDecimal bd = (BigDecimal) get_Value("RatioTotal");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
