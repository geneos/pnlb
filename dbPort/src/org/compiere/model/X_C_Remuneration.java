/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Remuneration
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.39
 */
public class X_C_Remuneration extends PO {
	/** Standard Constructor */
	public X_C_Remuneration(Properties ctx, int C_Remuneration_ID,
			String trxName) {
		super(ctx, C_Remuneration_ID, trxName);
		/**
		 * if (C_Remuneration_ID == 0) { setC_Remuneration_ID (0); setGrossRAmt
		 * (Env.ZERO); setGrossRCost (Env.ZERO); setName (null); setOvertimeAmt
		 * (Env.ZERO); setOvertimeCost (Env.ZERO); setRemunerationType (null);
		 * setStandardHours (0); }
		 */
	}

	/** Load Constructor */
	public X_C_Remuneration(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Remuneration */
	public static final String Table_Name = "C_Remuneration";

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
		StringBuffer sb = new StringBuffer("X_C_Remuneration[")
				.append(get_ID()).append("]");
		return sb.toString();
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

	/** RemunerationType AD_Reference_ID=346 */
	public static final int REMUNERATIONTYPE_AD_Reference_ID = 346;

	/** Bi-Weekly = B */
	public static final String REMUNERATIONTYPE_Bi_Weekly = "B";

	/** Daily = D */
	public static final String REMUNERATIONTYPE_Daily = "D";

	/** Hourly = H */
	public static final String REMUNERATIONTYPE_Hourly = "H";

	/** Monthly = M */
	public static final String REMUNERATIONTYPE_Monthly = "M";

	/** Twice Monthly = T */
	public static final String REMUNERATIONTYPE_TwiceMonthly = "T";

	/** Weekly = W */
	public static final String REMUNERATIONTYPE_Weekly = "W";

	/**
	 * Set Remuneration Type. Type of Remuneration
	 */
	public void setRemunerationType(String RemunerationType) {
		if (RemunerationType == null)
			throw new IllegalArgumentException("RemunerationType is mandatory");
		if (RemunerationType.equals("B") || RemunerationType.equals("D")
				|| RemunerationType.equals("H") || RemunerationType.equals("M")
				|| RemunerationType.equals("T") || RemunerationType.equals("W"))
			;
		else
			throw new IllegalArgumentException(
					"RemunerationType Invalid value - " + RemunerationType
							+ " - Reference_ID=346 - B - D - H - M - T - W");
		if (RemunerationType.length() > 1) {
			log.warning("Length > 1 - truncated");
			RemunerationType = RemunerationType.substring(0, 0);
		}
		set_Value("RemunerationType", RemunerationType);
	}

	/**
	 * Get Remuneration Type. Type of Remuneration
	 */
	public String getRemunerationType() {
		return (String) get_Value("RemunerationType");
	}

	/**
	 * Set Standard Hours. Standard Work Hours based on Remuneration Type
	 */
	public void setStandardHours(int StandardHours) {
		set_Value("StandardHours", new Integer(StandardHours));
	}

	/**
	 * Get Standard Hours. Standard Work Hours based on Remuneration Type
	 */
	public int getStandardHours() {
		Integer ii = (Integer) get_Value("StandardHours");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
