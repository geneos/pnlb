/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Scheduler
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.25
 */
public class X_AD_Scheduler extends PO {
	/** Standard Constructor */
	public X_AD_Scheduler(Properties ctx, int AD_Scheduler_ID, String trxName) {
		super(ctx, AD_Scheduler_ID, trxName);
		/**
		 * if (AD_Scheduler_ID == 0) { setAD_Process_ID (0); setAD_Scheduler_ID
		 * (0); setFrequency (0); setFrequencyType (null); setKeepLogDays (0); //
		 * 7 setName (null); setScheduleType (null); // F setSupervisor_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_AD_Scheduler(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Scheduler */
	public static final String Table_Name = "AD_Scheduler";

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
		StringBuffer sb = new StringBuffer("X_AD_Scheduler[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Process. Process or Report
	 */
	public void setAD_Process_ID(int AD_Process_ID) {
		if (AD_Process_ID < 1)
			throw new IllegalArgumentException("AD_Process_ID is mandatory.");
		set_ValueNoCheck("AD_Process_ID", new Integer(AD_Process_ID));
	}

	/**
	 * Get Process. Process or Report
	 */
	public int getAD_Process_ID() {
		Integer ii = (Integer) get_Value("AD_Process_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Scheduler. Schedule Processes
	 */
	public void setAD_Scheduler_ID(int AD_Scheduler_ID) {
		if (AD_Scheduler_ID < 1)
			throw new IllegalArgumentException("AD_Scheduler_ID is mandatory.");
		set_ValueNoCheck("AD_Scheduler_ID", new Integer(AD_Scheduler_ID));
	}

	/**
	 * Get Scheduler. Schedule Processes
	 */
	public int getAD_Scheduler_ID() {
		Integer ii = (Integer) get_Value("AD_Scheduler_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Date last run. Date the process was last run.
	 */
	public void setDateLastRun(Timestamp DateLastRun) {
		set_Value("DateLastRun", DateLastRun);
	}

	/**
	 * Get Date last run. Date the process was last run.
	 */
	public Timestamp getDateLastRun() {
		return (Timestamp) get_Value("DateLastRun");
	}

	/**
	 * Set Date next run. Date the process will run next
	 */
	public void setDateNextRun(Timestamp DateNextRun) {
		set_Value("DateNextRun", DateNextRun);
	}

	/**
	 * Get Date next run. Date the process will run next
	 */
	public Timestamp getDateNextRun() {
		return (Timestamp) get_Value("DateNextRun");
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
	 * Set Frequency. Frequency of events
	 */
	public void setFrequency(int Frequency) {
		set_Value("Frequency", new Integer(Frequency));
	}

	/**
	 * Get Frequency. Frequency of events
	 */
	public int getFrequency() {
		Integer ii = (Integer) get_Value("Frequency");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** FrequencyType AD_Reference_ID=221 */
	public static final int FREQUENCYTYPE_AD_Reference_ID = 221;

	/** Day = D */
	public static final String FREQUENCYTYPE_Day = "D";

	/** Hour = H */
	public static final String FREQUENCYTYPE_Hour = "H";

	/** Minute = M */
	public static final String FREQUENCYTYPE_Minute = "M";

	/**
	 * Set Frequency Type. Frequency of event
	 */
	public void setFrequencyType(String FrequencyType) {
		if (FrequencyType == null)
			throw new IllegalArgumentException("FrequencyType is mandatory");
		if (FrequencyType.equals("D") || FrequencyType.equals("H")
				|| FrequencyType.equals("M"))
			;
		else
			throw new IllegalArgumentException("FrequencyType Invalid value - "
					+ FrequencyType + " - Reference_ID=221 - D - H - M");
		if (FrequencyType.length() > 1) {
			log.warning("Length > 1 - truncated");
			FrequencyType = FrequencyType.substring(0, 0);
		}
		set_Value("FrequencyType", FrequencyType);
	}

	/**
	 * Get Frequency Type. Frequency of event
	 */
	public String getFrequencyType() {
		return (String) get_Value("FrequencyType");
	}

	/**
	 * Set Days to keep Log. Number of days to keep the log entries
	 */
	public void setKeepLogDays(int KeepLogDays) {
		set_Value("KeepLogDays", new Integer(KeepLogDays));
	}

	/**
	 * Get Days to keep Log. Number of days to keep the log entries
	 */
	public int getKeepLogDays() {
		Integer ii = (Integer) get_Value("KeepLogDays");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Day of the Month. Day of the month 1 to 28/29/30/31
	 */
	public void setMonthDay(int MonthDay) {
		set_Value("MonthDay", new Integer(MonthDay));
	}

	/**
	 * Get Day of the Month. Day of the month 1 to 28/29/30/31
	 */
	public int getMonthDay() {
		Integer ii = (Integer) get_Value("MonthDay");
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

	/** ScheduleType AD_Reference_ID=318 */
	public static final int SCHEDULETYPE_AD_Reference_ID = 318;

	/** Frequency = F */
	public static final String SCHEDULETYPE_Frequency = "F";

	/** Month Day = M */
	public static final String SCHEDULETYPE_MonthDay = "M";

	/** Week Day = W */
	public static final String SCHEDULETYPE_WeekDay = "W";

	/**
	 * Set Schedule Type. Type of schedule
	 */
	public void setScheduleType(String ScheduleType) {
		if (ScheduleType == null)
			throw new IllegalArgumentException("ScheduleType is mandatory");
		if (ScheduleType.equals("F") || ScheduleType.equals("M")
				|| ScheduleType.equals("W"))
			;
		else
			throw new IllegalArgumentException("ScheduleType Invalid value - "
					+ ScheduleType + " - Reference_ID=318 - F - M - W");
		if (ScheduleType.length() > 1) {
			log.warning("Length > 1 - truncated");
			ScheduleType = ScheduleType.substring(0, 0);
		}
		set_Value("ScheduleType", ScheduleType);
	}

	/**
	 * Get Schedule Type. Type of schedule
	 */
	public String getScheduleType() {
		return (String) get_Value("ScheduleType");
	}

	/** Supervisor_ID AD_Reference_ID=316 */
	public static final int SUPERVISOR_ID_AD_Reference_ID = 316;

	/**
	 * Set Supervisor. Supervisor for this user/organization - used for
	 * escalation and approval
	 */
	public void setSupervisor_ID(int Supervisor_ID) {
		if (Supervisor_ID < 1)
			throw new IllegalArgumentException("Supervisor_ID is mandatory.");
		set_Value("Supervisor_ID", new Integer(Supervisor_ID));
	}

	/**
	 * Get Supervisor. Supervisor for this user/organization - used for
	 * escalation and approval
	 */
	public int getSupervisor_ID() {
		Integer ii = (Integer) get_Value("Supervisor_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** WeekDay AD_Reference_ID=167 */
	public static final int WEEKDAY_AD_Reference_ID = 167;

	/** Monday = 1 */
	public static final String WEEKDAY_Monday = "1";

	/** Tuesday = 2 */
	public static final String WEEKDAY_Tuesday = "2";

	/** Wednesday = 3 */
	public static final String WEEKDAY_Wednesday = "3";

	/** Thursday = 4 */
	public static final String WEEKDAY_Thursday = "4";

	/** Friday = 5 */
	public static final String WEEKDAY_Friday = "5";

	/** Saturday = 6 */
	public static final String WEEKDAY_Saturday = "6";

	/** Sunday = 7 */
	public static final String WEEKDAY_Sunday = "7";

	/**
	 * Set Day of the Week. Day of the Week
	 */
	public void setWeekDay(String WeekDay) {
		if (WeekDay != null && WeekDay.length() > 1) {
			log.warning("Length > 1 - truncated");
			WeekDay = WeekDay.substring(0, 0);
		}
		set_Value("WeekDay", WeekDay);
	}

	/**
	 * Get Day of the Week. Day of the Week
	 */
	public String getWeekDay() {
		return (String) get_Value("WeekDay");
	}
}
