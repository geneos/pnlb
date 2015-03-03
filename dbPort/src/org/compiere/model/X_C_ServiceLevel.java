/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_ServiceLevel
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.546
 */
public class X_C_ServiceLevel extends PO {
	/** Standard Constructor */
	public X_C_ServiceLevel(Properties ctx, int C_ServiceLevel_ID,
			String trxName) {
		super(ctx, C_ServiceLevel_ID, trxName);
		/**
		 * if (C_ServiceLevel_ID == 0) { setC_RevenueRecognition_Plan_ID (0);
		 * setC_ServiceLevel_ID (0); setM_Product_ID (0);
		 * setServiceLevelInvoiced (Env.ZERO); setServiceLevelProvided
		 * (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_C_ServiceLevel(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_ServiceLevel */
	public static final String Table_Name = "C_ServiceLevel";

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
		StringBuffer sb = new StringBuffer("X_C_ServiceLevel[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Revenue Recognition Plan. Plan for recognizing or recording revenue
	 */
	public void setC_RevenueRecognition_Plan_ID(int C_RevenueRecognition_Plan_ID) {
		if (C_RevenueRecognition_Plan_ID < 1)
			throw new IllegalArgumentException(
					"C_RevenueRecognition_Plan_ID is mandatory.");
		set_ValueNoCheck("C_RevenueRecognition_Plan_ID", new Integer(
				C_RevenueRecognition_Plan_ID));
	}

	/**
	 * Get Revenue Recognition Plan. Plan for recognizing or recording revenue
	 */
	public int getC_RevenueRecognition_Plan_ID() {
		Integer ii = (Integer) get_Value("C_RevenueRecognition_Plan_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Service Level. Product Revenue Recognition Service Level
	 */
	public void setC_ServiceLevel_ID(int C_ServiceLevel_ID) {
		if (C_ServiceLevel_ID < 1)
			throw new IllegalArgumentException(
					"C_ServiceLevel_ID is mandatory.");
		set_ValueNoCheck("C_ServiceLevel_ID", new Integer(C_ServiceLevel_ID));
	}

	/**
	 * Get Service Level. Product Revenue Recognition Service Level
	 */
	public int getC_ServiceLevel_ID() {
		Integer ii = (Integer) get_Value("C_ServiceLevel_ID");
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getDescription());
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

	/**
	 * Set Processed. The document has been processed
	 */
	public void setProcessed(boolean Processed) {
		set_Value("Processed", new Boolean(Processed));
	}

	/**
	 * Get Processed. The document has been processed
	 */
	public boolean isProcessed() {
		Object oo = get_Value("Processed");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
	 * Set Quantity Invoiced. Quantity of product or service invoiced
	 */
	public void setServiceLevelInvoiced(BigDecimal ServiceLevelInvoiced) {
		if (ServiceLevelInvoiced == null)
			throw new IllegalArgumentException(
					"ServiceLevelInvoiced is mandatory.");
		set_ValueNoCheck("ServiceLevelInvoiced", ServiceLevelInvoiced);
	}

	/**
	 * Get Quantity Invoiced. Quantity of product or service invoiced
	 */
	public BigDecimal getServiceLevelInvoiced() {
		BigDecimal bd = (BigDecimal) get_Value("ServiceLevelInvoiced");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Quantity Provided. Quantity of service or product provided
	 */
	public void setServiceLevelProvided(BigDecimal ServiceLevelProvided) {
		if (ServiceLevelProvided == null)
			throw new IllegalArgumentException(
					"ServiceLevelProvided is mandatory.");
		set_ValueNoCheck("ServiceLevelProvided", ServiceLevelProvided);
	}

	/**
	 * Get Quantity Provided. Quantity of service or product provided
	 */
	public BigDecimal getServiceLevelProvided() {
		BigDecimal bd = (BigDecimal) get_Value("ServiceLevelProvided");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
