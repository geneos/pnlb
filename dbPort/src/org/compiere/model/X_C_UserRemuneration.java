/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_UserRemuneration
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.812
 */
public class X_C_UserRemuneration extends PO {
	/** Standard Constructor */
	public X_C_UserRemuneration(Properties ctx, int C_UserRemuneration_ID,
			String trxName) {
		super(ctx, C_UserRemuneration_ID, trxName);
		/**
		 * if (C_UserRemuneration_ID == 0) { setAD_User_ID (0);
		 * setC_Remuneration_ID (0); setC_UserRemuneration_ID (0); setGrossRAmt
		 * (Env.ZERO); setGrossRCost (Env.ZERO); setOvertimeAmt (Env.ZERO);
		 * setOvertimeCost (Env.ZERO); setValidFrom (new
		 * Timestamp(System.currentTimeMillis())); }
		 */
	}

	/** Load Constructor */
	public X_C_UserRemuneration(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_UserRemuneration */
	public static final String Table_Name = "C_UserRemuneration";

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

	protected BigDecimal accessLevel = new BigDecimal(2);

	/** AccessLevel 2 - Client */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_C_UserRemuneration[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public void setAD_User_ID(int AD_User_ID) {
		if (AD_User_ID < 1)
			throw new IllegalArgumentException("AD_User_ID is mandatory.");
		set_ValueNoCheck("AD_User_ID", new Integer(AD_User_ID));
	}

	/**
	 * Get User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public int getAD_User_ID() {
		Integer ii = (Integer) get_Value("AD_User_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getAD_User_ID()));
	}

	/**
	 * Set Remuneration. Wage or Salary
	 */
	public void setC_Remuneration_ID(int C_Remuneration_ID) {
		if (C_Remuneration_ID < 1)
			throw new IllegalArgumentException(
					"C_Remuneration_ID is mandatory.");
		set_ValueNoCheck("C_Remuneration_ID", new Integer(C_Remuneration_ID));
	}

	/**
	 * Get Remuneration. Wage or Salary
	 */
	public int getC_Remuneration_ID() {
		Integer ii = (Integer) get_Value("C_Remuneration_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Employee Remuneration. Employee Wage or Salary Overwrite
	 */
	public void setC_UserRemuneration_ID(int C_UserRemuneration_ID) {
		if (C_UserRemuneration_ID < 1)
			throw new IllegalArgumentException(
					"C_UserRemuneration_ID is mandatory.");
		set_ValueNoCheck("C_UserRemuneration_ID", new Integer(
				C_UserRemuneration_ID));
	}

	/**
	 * Get Employee Remuneration. Employee Wage or Salary Overwrite
	 */
	public int getC_UserRemuneration_ID() {
		Integer ii = (Integer) get_Value("C_UserRemuneration_ID");
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
	 * Set Gross Amount. Gross Remuneration Amount
	 */
	public void setGrossRAmt(BigDecimal GrossRAmt) {
		if (GrossRAmt == null)
			throw new IllegalArgumentException("GrossRAmt is mandatory.");
		set_Value("GrossRAmt", GrossRAmt);
	}

	/**
	 * Get Gross Amount. Gross Remuneration Amount
	 */
	public BigDecimal getGrossRAmt() {
		BigDecimal bd = (BigDecimal) get_Value("GrossRAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Gross Cost. Gross Remuneration Costs
	 */
	public void setGrossRCost(BigDecimal GrossRCost) {
		if (GrossRCost == null)
			throw new IllegalArgumentException("GrossRCost is mandatory.");
		set_Value("GrossRCost", GrossRCost);
	}

	/**
	 * Get Gross Cost. Gross Remuneration Costs
	 */
	public BigDecimal getGrossRCost() {
		BigDecimal bd = (BigDecimal) get_Value("GrossRCost");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Overtime Amount. Hourly Overtime Rate
	 */
	public void setOvertimeAmt(BigDecimal OvertimeAmt) {
		if (OvertimeAmt == null)
			throw new IllegalArgumentException("OvertimeAmt is mandatory.");
		set_Value("OvertimeAmt", OvertimeAmt);
	}

	/**
	 * Get Overtime Amount. Hourly Overtime Rate
	 */
	public BigDecimal getOvertimeAmt() {
		BigDecimal bd = (BigDecimal) get_Value("OvertimeAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Overtime Cost. Hourly Overtime Cost
	 */
	public void setOvertimeCost(BigDecimal OvertimeCost) {
		if (OvertimeCost == null)
			throw new IllegalArgumentException("OvertimeCost is mandatory.");
		set_Value("OvertimeCost", OvertimeCost);
	}

	/**
	 * Get Overtime Cost. Hourly Overtime Cost
	 */
	public BigDecimal getOvertimeCost() {
		BigDecimal bd = (BigDecimal) get_Value("OvertimeCost");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Valid from. Valid from including this date (first day)
	 */
	public void setValidFrom(Timestamp ValidFrom) {
		if (ValidFrom == null)
			throw new IllegalArgumentException("ValidFrom is mandatory.");
		set_Value("ValidFrom", ValidFrom);
	}

	/**
	 * Get Valid from. Valid from including this date (first day)
	 */
	public Timestamp getValidFrom() {
		return (Timestamp) get_Value("ValidFrom");
	}

	/**
	 * Set Valid to. Valid to including this date (last day)
	 */
	public void setValidTo(Timestamp ValidTo) {
		set_Value("ValidTo", ValidTo);
	}

	/**
	 * Get Valid to. Valid to including this date (last day)
	 */
	public Timestamp getValidTo() {
		return (Timestamp) get_Value("ValidTo");
	}
}
