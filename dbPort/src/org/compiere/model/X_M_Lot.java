/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_Lot
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.078
 */
public class X_M_Lot extends PO {
	/** Standard Constructor */
	public X_M_Lot(Properties ctx, int M_Lot_ID, String trxName) {
		super(ctx, M_Lot_ID, trxName);
		/**
		 * if (M_Lot_ID == 0) { setM_Lot_ID (0); setM_Product_ID (0); setName
		 * (null); }
		 */
	}

	/** Load Constructor */
	public X_M_Lot(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_Lot */
	public static final String Table_Name = "M_Lot";

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
		StringBuffer sb = new StringBuffer("X_M_Lot[").append(get_ID()).append(
				"]");
		return sb.toString();
	}

	/**
	 * Set Date From. Starting date for a range
	 */
	public void setDateFrom(Timestamp DateFrom) {
		set_Value("DateFrom", DateFrom);
	}

	/**
	 * Get Date From. Starting date for a range
	 */
	public Timestamp getDateFrom() {
		return (Timestamp) get_Value("DateFrom");
	}

	/**
	 * Set Date To. End date of a date range
	 */
	public void setDateTo(Timestamp DateTo) {
		set_Value("DateTo", DateTo);
	}

	/**
	 * Get Date To. End date of a date range
	 */
	public Timestamp getDateTo() {
		return (Timestamp) get_Value("DateTo");
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
	 * Set Lot Control. Product Lot Control
	 */
	public void setM_LotCtl_ID(int M_LotCtl_ID) {
		if (M_LotCtl_ID <= 0)
			set_ValueNoCheck("M_LotCtl_ID", null);
		else
			set_ValueNoCheck("M_LotCtl_ID", new Integer(M_LotCtl_ID));
	}

	/**
	 * Get Lot Control. Product Lot Control
	 */
	public int getM_LotCtl_ID() {
		Integer ii = (Integer) get_Value("M_LotCtl_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Lot. Product Lot Definition
	 */
	public void setM_Lot_ID(int M_Lot_ID) {
		if (M_Lot_ID < 1)
			throw new IllegalArgumentException("M_Lot_ID is mandatory.");
		set_ValueNoCheck("M_Lot_ID", new Integer(M_Lot_ID));
	}

	/**
	 * Get Lot. Product Lot Definition
	 */
	public int getM_Lot_ID() {
		Integer ii = (Integer) get_Value("M_Lot_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Product. Product, Service, Item
	 */
	public void setM_Product_ID(int M_Product_ID) {
		if (M_Product_ID < 1)
			throw new IllegalArgumentException("M_Product_ID is mandatory.");
		set_ValueNoCheck("M_Product_ID", new Integer(M_Product_ID));
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getM_Product_ID()));
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
}
