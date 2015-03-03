/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for R_IssueSystem
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.453
 */
public class X_R_IssueSystem extends PO {
	/** Standard Constructor */
	public X_R_IssueSystem(Properties ctx, int R_IssueSystem_ID, String trxName) {
		super(ctx, R_IssueSystem_ID, trxName);
		/**
		 * if (R_IssueSystem_ID == 0) { setDBAddress (null); setR_IssueSystem_ID
		 * (0); setSystemStatus (null); }
		 */
	}

	/** Load Constructor */
	public X_R_IssueSystem(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=R_IssueSystem */
	public static final String Table_Name = "R_IssueSystem";

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
		StringBuffer sb = new StringBuffer("X_R_IssueSystem[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Asset. Asset used internally or by customers
	 */
	public void setA_Asset_ID(int A_Asset_ID) {
		if (A_Asset_ID <= 0)
			set_Value("A_Asset_ID", null);
		else
			set_Value("A_Asset_ID", new Integer(A_Asset_ID));
	}

	/**
	 * Get Asset. Asset used internally or by customers
	 */
	public int getA_Asset_ID() {
		Integer ii = (Integer) get_Value("A_Asset_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set DB Address. JDBC URL of the database server
	 */
	public void setDBAddress(String DBAddress) {
		if (DBAddress == null)
			throw new IllegalArgumentException("DBAddress is mandatory.");
		if (DBAddress.length() > 255) {
			log.warning("Length > 255 - truncated");
			DBAddress = DBAddress.substring(0, 254);
		}
		set_ValueNoCheck("DBAddress", DBAddress);
	}

	/**
	 * Get DB Address. JDBC URL of the database server
	 */
	public String getDBAddress() {
		return (String) get_Value("DBAddress");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getDBAddress());
	}

	/**
	 * Set Profile. Information to help profiling the system for solving support
	 * issues
	 */
	public void setProfileInfo(String ProfileInfo) {
		if (ProfileInfo != null && ProfileInfo.length() > 60) {
			log.warning("Length > 60 - truncated");
			ProfileInfo = ProfileInfo.substring(0, 59);
		}
		set_ValueNoCheck("ProfileInfo", ProfileInfo);
	}

	/**
	 * Get Profile. Information to help profiling the system for solving support
	 * issues
	 */
	public String getProfileInfo() {
		return (String) get_Value("ProfileInfo");
	}

	/**
	 * Set Issue System. System creating the issue
	 */
	public void setR_IssueSystem_ID(int R_IssueSystem_ID) {
		if (R_IssueSystem_ID < 1)
			throw new IllegalArgumentException("R_IssueSystem_ID is mandatory.");
		set_ValueNoCheck("R_IssueSystem_ID", new Integer(R_IssueSystem_ID));
	}

	/**
	 * Get Issue System. System creating the issue
	 */
	public int getR_IssueSystem_ID() {
		Integer ii = (Integer) get_Value("R_IssueSystem_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Statistics. Information to help profiling the system for solving
	 * support issues
	 */
	public void setStatisticsInfo(String StatisticsInfo) {
		if (StatisticsInfo != null && StatisticsInfo.length() > 60) {
			log.warning("Length > 60 - truncated");
			StatisticsInfo = StatisticsInfo.substring(0, 59);
		}
		set_ValueNoCheck("StatisticsInfo", StatisticsInfo);
	}

	/**
	 * Get Statistics. Information to help profiling the system for solving
	 * support issues
	 */
	public String getStatisticsInfo() {
		return (String) get_Value("StatisticsInfo");
	}

	/** SystemStatus AD_Reference_ID=374 */
	public static final int SYSTEMSTATUS_AD_Reference_ID = 374;

	/** Evaluation = E */
	public static final String SYSTEMSTATUS_Evaluation = "E";

	/** Implementation = I */
	public static final String SYSTEMSTATUS_Implementation = "I";

	/** Production = P */
	public static final String SYSTEMSTATUS_Production = "P";

	/**
	 * Set System Status. Status of the system - Support priority depends on
	 * system status
	 */
	public void setSystemStatus(String SystemStatus) {
		if (SystemStatus == null)
			throw new IllegalArgumentException("SystemStatus is mandatory");
		if (SystemStatus.equals("E") || SystemStatus.equals("I")
				|| SystemStatus.equals("P"))
			;
		else
			throw new IllegalArgumentException("SystemStatus Invalid value - "
					+ SystemStatus + " - Reference_ID=374 - E - I - P");
		if (SystemStatus.length() > 1) {
			log.warning("Length > 1 - truncated");
			SystemStatus = SystemStatus.substring(0, 0);
		}
		set_Value("SystemStatus", SystemStatus);
	}

	/**
	 * Get System Status. Status of the system - Support priority depends on
	 * system status
	 */
	public String getSystemStatus() {
		return (String) get_Value("SystemStatus");
	}
}
