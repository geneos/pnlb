/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_PackageLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.281
 */
public class X_M_PackageLine extends PO {
	/** Standard Constructor */
	public X_M_PackageLine(Properties ctx, int M_PackageLine_ID, String trxName) {
		super(ctx, M_PackageLine_ID, trxName);
		/**
		 * if (M_PackageLine_ID == 0) { setM_InOutLine_ID (0);
		 * setM_PackageLine_ID (0); setM_Package_ID (0); setQty (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_M_PackageLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_PackageLine */
	public static final String Table_Name = "M_PackageLine";

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

	protected BigDecimal accessLevel = new BigDecimal(1);

	/** AccessLevel 1 - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_M_PackageLine[").append(get_ID())
				.append("]");
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
	 * Set Shipment/Receipt Line. Line on Shipment or Receipt document
	 */
	public void setM_InOutLine_ID(int M_InOutLine_ID) {
		if (M_InOutLine_ID < 1)
			throw new IllegalArgumentException("M_InOutLine_ID is mandatory.");
		set_ValueNoCheck("M_InOutLine_ID", new Integer(M_InOutLine_ID));
	}

	/**
	 * Get Shipment/Receipt Line. Line on Shipment or Receipt document
	 */
	public int getM_InOutLine_ID() {
		Integer ii = (Integer) get_Value("M_InOutLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Package Line. The detail content of the Package
	 */
	public void setM_PackageLine_ID(int M_PackageLine_ID) {
		if (M_PackageLine_ID < 1)
			throw new IllegalArgumentException("M_PackageLine_ID is mandatory.");
		set_ValueNoCheck("M_PackageLine_ID", new Integer(M_PackageLine_ID));
	}

	/**
	 * Get Package Line. The detail content of the Package
	 */
	public int getM_PackageLine_ID() {
		Integer ii = (Integer) get_Value("M_PackageLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Package. Shipment Package
	 */
	public void setM_Package_ID(int M_Package_ID) {
		if (M_Package_ID < 1)
			throw new IllegalArgumentException("M_Package_ID is mandatory.");
		set_ValueNoCheck("M_Package_ID", new Integer(M_Package_ID));
	}

	/**
	 * Get Package. Shipment Package
	 */
	public int getM_Package_ID() {
		Integer ii = (Integer) get_Value("M_Package_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getM_Package_ID()));
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
}
