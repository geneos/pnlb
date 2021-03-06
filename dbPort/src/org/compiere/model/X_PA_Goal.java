/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for PA_Goal
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.875
 */
public class X_PA_Goal extends PO {
	/** Standard Constructor */
	public X_PA_Goal(Properties ctx, int PA_Goal_ID, String trxName) {
		super(ctx, PA_Goal_ID, trxName);
		/**
		 * if (PA_Goal_ID == 0) { setGoalPerformance (Env.ZERO); setIsSummary
		 * (false); setMeasureActual (Env.ZERO); setMeasureScope (null);
		 * setMeasureTarget (Env.ZERO); setName (null); setPA_ColorSchema_ID
		 * (0); setPA_Goal_ID (0); setRelativeWeight (Env.ZERO); // 1 setSeqNo
		 * (0); }
		 */
	}

	/** Load Constructor */
	public X_PA_Goal(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=PA_Goal */
	public static final String Table_Name = "PA_Goal";

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

	protected BigDecimal accessLevel = new BigDecimal(6);

	/** AccessLevel 6 - System - Client */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_PA_Goal[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Role. Responsibility Role
	 */
	public void setAD_Role_ID(int AD_Role_ID) {
		if (AD_Role_ID <= 0)
			set_Value("AD_Role_ID", null);
		else
			set_Value("AD_Role_ID", new Integer(AD_Role_ID));
	}

	/**
	 * Get Role. Responsibility Role
	 */
	public int getAD_Role_ID() {
		Integer ii = (Integer) get_Value("AD_Role_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public void setAD_User_ID(int AD_User_ID) {
		if (AD_User_ID <= 0)
			set_Value("AD_User_ID", null);
		else
			set_Value("AD_User_ID", new Integer(AD_User_ID));
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
	 * Set Date last run. Date the process was last run.
	 */
	public void setDateLastRun(Timestamp DateLastRun) {
		set_ValueNoCheck("DateLastRun", DateLastRun);
	}

	/**
	 * Get Date last run. Date the process was last run.
	 */
	public Timestamp getDateLastRun() {
		return (Timestamp) get_Value("DateLastRun");
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
	 * Set Performance Goal. Target achievement from 0..1
	 */
	public void setGoalPerformance(BigDecimal GoalPerformance) {
		if (GoalPerformance == null)
			throw new IllegalArgumentException("GoalPerformance is mandatory.");
		set_ValueNoCheck("GoalPerformance", GoalPerformance);
	}

	/**
	 * Get Performance Goal. Target achievement from 0..1
	 */
	public BigDecimal getGoalPerformance() {
		BigDecimal bd = (BigDecimal) get_Value("GoalPerformance");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Summary Level. This is a summary entity
	 */
	public void setIsSummary(boolean IsSummary) {
		set_Value("IsSummary", new Boolean(IsSummary));
	}

	/**
	 * Get Summary Level. This is a summary entity
	 */
	public boolean isSummary() {
		Object oo = get_Value("IsSummary");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Measure Actual. Actual value that has been measured.
	 */
	public void setMeasureActual(BigDecimal MeasureActual) {
		if (MeasureActual == null)
			throw new IllegalArgumentException("MeasureActual is mandatory.");
		set_ValueNoCheck("MeasureActual", MeasureActual);
	}

	/**
	 * Get Measure Actual. Actual value that has been measured.
	 */
	public BigDecimal getMeasureActual() {
		BigDecimal bd = (BigDecimal) get_Value("MeasureActual");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** MeasureDisplay AD_Reference_ID=367 */
	public static final int MEASUREDISPLAY_AD_Reference_ID = 367;

	/** Total = 0 */
	public static final String MEASUREDISPLAY_Total = "0";

	/** Year = 1 */
	public static final String MEASUREDISPLAY_Year = "1";

	/** Quarter = 3 */
	public static final String MEASUREDISPLAY_Quarter = "3";

	/** Month = 5 */
	public static final String MEASUREDISPLAY_Month = "5";

	/** Week = 7 */
	public static final String MEASUREDISPLAY_Week = "7";

	/** Day = 8 */
	public static final String MEASUREDISPLAY_Day = "8";

	/**
	 * Set Measure Display. Measure Scope initially displayed
	 */
	public void setMeasureDisplay(String MeasureDisplay) {
		if (MeasureDisplay != null && MeasureDisplay.length() > 1) {
			log.warning("Length > 1 - truncated");
			MeasureDisplay = MeasureDisplay.substring(0, 0);
		}
		set_Value("MeasureDisplay", MeasureDisplay);
	}

	/**
	 * Get Measure Display. Measure Scope initially displayed
	 */
	public String getMeasureDisplay() {
		return (String) get_Value("MeasureDisplay");
	}

	/** MeasureScope AD_Reference_ID=367 */
	public static final int MEASURESCOPE_AD_Reference_ID = 367;

	/** Total = 0 */
	public static final String MEASURESCOPE_Total = "0";

	/** Year = 1 */
	public static final String MEASURESCOPE_Year = "1";

	/** Quarter = 3 */
	public static final String MEASURESCOPE_Quarter = "3";

	/** Month = 5 */
	public static final String MEASURESCOPE_Month = "5";

	/** Week = 7 */
	public static final String MEASURESCOPE_Week = "7";

	/** Day = 8 */
	public static final String MEASURESCOPE_Day = "8";

	/**
	 * Set Measure Scope. Performance Measure Scope
	 */
	public void setMeasureScope(String MeasureScope) {
		if (MeasureScope == null)
			throw new IllegalArgumentException("MeasureScope is mandatory");
		if (MeasureScope.equals("0") || MeasureScope.equals("1")
				|| MeasureScope.equals("3") || MeasureScope.equals("5")
				|| MeasureScope.equals("7") || MeasureScope.equals("8"))
			;
		else
			throw new IllegalArgumentException("MeasureScope Invalid value - "
					+ MeasureScope
					+ " - Reference_ID=367 - 0 - 1 - 3 - 5 - 7 - 8");
		if (MeasureScope.length() > 1) {
			log.warning("Length > 1 - truncated");
			MeasureScope = MeasureScope.substring(0, 0);
		}
		set_Value("MeasureScope", MeasureScope);
	}

	/**
	 * Get Measure Scope. Performance Measure Scope
	 */
	public String getMeasureScope() {
		return (String) get_Value("MeasureScope");
	}

	/**
	 * Set Measure Target. Target value for measure
	 */
	public void setMeasureTarget(BigDecimal MeasureTarget) {
		if (MeasureTarget == null)
			throw new IllegalArgumentException("MeasureTarget is mandatory.");
		set_Value("MeasureTarget", MeasureTarget);
	}

	/**
	 * Get Measure Target. Target value for measure
	 */
	public BigDecimal getMeasureTarget() {
		BigDecimal bd = (BigDecimal) get_Value("MeasureTarget");
		if (bd == null)
			return Env.ZERO;
		return bd;
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
	 * Set Note. Optional additional user defined information
	 */
	public void setNote(String Note) {
		if (Note != null && Note.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Note = Note.substring(0, 1999);
		}
		set_Value("Note", Note);
	}

	/**
	 * Get Note. Optional additional user defined information
	 */
	public String getNote() {
		return (String) get_Value("Note");
	}

	/**
	 * Set Color Schema. Performance Color Schema
	 */
	public void setPA_ColorSchema_ID(int PA_ColorSchema_ID) {
		if (PA_ColorSchema_ID < 1)
			throw new IllegalArgumentException(
					"PA_ColorSchema_ID is mandatory.");
		set_Value("PA_ColorSchema_ID", new Integer(PA_ColorSchema_ID));
	}

	/**
	 * Get Color Schema. Performance Color Schema
	 */
	public int getPA_ColorSchema_ID() {
		Integer ii = (Integer) get_Value("PA_ColorSchema_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** PA_GoalParent_ID AD_Reference_ID=230 */
	public static final int PA_GOALPARENT_ID_AD_Reference_ID = 230;

	/**
	 * Set Parent Goal. Parent Goal
	 */
	public void setPA_GoalParent_ID(int PA_GoalParent_ID) {
		if (PA_GoalParent_ID <= 0)
			set_Value("PA_GoalParent_ID", null);
		else
			set_Value("PA_GoalParent_ID", new Integer(PA_GoalParent_ID));
	}

	/**
	 * Get Parent Goal. Parent Goal
	 */
	public int getPA_GoalParent_ID() {
		Integer ii = (Integer) get_Value("PA_GoalParent_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Goal. Performance Goal
	 */
	public void setPA_Goal_ID(int PA_Goal_ID) {
		if (PA_Goal_ID < 1)
			throw new IllegalArgumentException("PA_Goal_ID is mandatory.");
		set_ValueNoCheck("PA_Goal_ID", new Integer(PA_Goal_ID));
	}

	/**
	 * Get Goal. Performance Goal
	 */
	public int getPA_Goal_ID() {
		Integer ii = (Integer) get_Value("PA_Goal_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Measure. Concrete Performance Measurement
	 */
	public void setPA_Measure_ID(int PA_Measure_ID) {
		if (PA_Measure_ID <= 0)
			set_Value("PA_Measure_ID", null);
		else
			set_Value("PA_Measure_ID", new Integer(PA_Measure_ID));
	}

	/**
	 * Get Measure. Concrete Performance Measurement
	 */
	public int getPA_Measure_ID() {
		Integer ii = (Integer) get_Value("PA_Measure_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Relative Weight. Relative weight of this step (0 = ignored)
	 */
	public void setRelativeWeight(BigDecimal RelativeWeight) {
		if (RelativeWeight == null)
			throw new IllegalArgumentException("RelativeWeight is mandatory.");
		set_Value("RelativeWeight", RelativeWeight);
	}

	/**
	 * Get Relative Weight. Relative weight of this step (0 = ignored)
	 */
	public BigDecimal getRelativeWeight() {
		BigDecimal bd = (BigDecimal) get_Value("RelativeWeight");
		if (bd == null)
			return Env.ZERO;
		return bd;
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
}
