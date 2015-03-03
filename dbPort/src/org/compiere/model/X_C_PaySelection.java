/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_PaySelection
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-01-30 09:48:31.812
 */
public class X_C_PaySelection extends PO {
	/** Standard Constructor */
	public X_C_PaySelection(Properties ctx, int C_PaySelection_ID,
			String trxName) {
		super(ctx, C_PaySelection_ID, trxName);
		/**
		 * if (C_PaySelection_ID == 0) { setC_BankAccount_ID (0);
		 * setC_PaySelection_ID (0); setIsApproved (false); setName (null); //
		 * 
		 * @#Date@ setPayDate (new Timestamp(System.currentTimeMillis())); //
		 * @#Date@ setProcessed (false); setProcessing (false); setTotalAmt
		 *         (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_C_PaySelection(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_PaySelection */
	public static final String Table_Name = "C_PaySelection";

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
		StringBuffer sb = new StringBuffer("X_C_PaySelection[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Bank Account. Account at the Bank
	 */
	public void setC_BankAccount_ID(int C_BankAccount_ID) {
		if (C_BankAccount_ID < 1)
			throw new IllegalArgumentException("C_BankAccount_ID is mandatory.");
		set_Value("C_BankAccount_ID", new Integer(C_BankAccount_ID));
	}

	/**
	 * Get Bank Account. Account at the Bank
	 */
	public int getC_BankAccount_ID() {
		Integer ii = (Integer) get_Value("C_BankAccount_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Payment Selection. Payment Selection
	 */
	public void setC_PaySelection_ID(int C_PaySelection_ID) {
		if (C_PaySelection_ID < 1)
			throw new IllegalArgumentException(
					"C_PaySelection_ID is mandatory.");
		set_ValueNoCheck("C_PaySelection_ID", new Integer(C_PaySelection_ID));
	}

	/**
	 * Get Payment Selection. Payment Selection
	 */
	public int getC_PaySelection_ID() {
		Integer ii = (Integer) get_Value("C_PaySelection_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set C_REGIMENGANANCIAS_V_ID */
	/*
	 * Vit4B 25/03/2008 Modificacion para cambiar el campo de int a String
	 */

	public void setC_REGIMENGANANCIAS_V_ID(String C_REGIMENGANANCIAS_V_ID) {

		if (C_REGIMENGANANCIAS_V_ID != null
				&& C_REGIMENGANANCIAS_V_ID.length() > 30) {
			log.warning("Length > 30 - truncated");
			C_REGIMENGANANCIAS_V_ID = C_REGIMENGANANCIAS_V_ID.substring(0, 29);
		}
		set_Value("C_REGIMENGANANCIAS_V_ID", C_REGIMENGANANCIAS_V_ID);

	}

	/** Get C_REGIMENGANANCIAS_V_ID */
	public String getC_REGIMENGANANCIAS_V_ID() {
		return (String) get_Value("C_REGIMENGANANCIAS_V_ID");
	}

	/**
	 * Set Create lines from. Process which will generate a new document lines
	 * based on an existing document
	 */
	public void setCreateFrom(String CreateFrom) {
		if (CreateFrom != null && CreateFrom.length() > 1) {
			log.warning("Length > 1 - truncated");
			CreateFrom = CreateFrom.substring(0, 0);
		}
		set_Value("CreateFrom", CreateFrom);
	}

	/**
	 * Get Create lines from. Process which will generate a new document lines
	 * based on an existing document
	 */
	public String getCreateFrom() {
		return (String) get_Value("CreateFrom");
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
	 * Set Approved. Indicates if this document requires approval
	 */
	public void setIsApproved(boolean IsApproved) {
		set_Value("IsApproved", new Boolean(IsApproved));
	}

	/**
	 * Get Approved. Indicates if this document requires approval
	 */
	public boolean isApproved() {
		Object oo = get_Value("IsApproved");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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

	/**
	 * Set Payment date. Date Payment made
	 */
	public void setPayDate(Timestamp PayDate) {
		if (PayDate == null)
			throw new IllegalArgumentException("PayDate is mandatory.");
		set_Value("PayDate", PayDate);
	}

	/**
	 * Get Payment date. Date Payment made
	 */
	public Timestamp getPayDate() {
		return (Timestamp) get_Value("PayDate");
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

	/** Set REGIMENGANANCIAS */
	public void setREGIMENGANANCIAS(int REGIMENGANANCIAS) {
		set_Value("REGIMENGANANCIAS", new Integer(REGIMENGANANCIAS));
	}

	/** Get REGIMENGANANCIAS */
	public int getREGIMENGANANCIAS() {
		Integer ii = (Integer) get_Value("REGIMENGANANCIAS");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Total Amount. Total Amount
	 */
	public void setTotalAmt(BigDecimal TotalAmt) {
		if (TotalAmt == null)
			throw new IllegalArgumentException("TotalAmt is mandatory.");
		set_Value("TotalAmt", TotalAmt);
	}

	/**
	 * Get Total Amount. Total Amount
	 */
	public BigDecimal getTotalAmt() {
		BigDecimal bd = (BigDecimal) get_Value("TotalAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
