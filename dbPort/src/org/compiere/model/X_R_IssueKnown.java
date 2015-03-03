/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for R_IssueKnown
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.406
 */
public class X_R_IssueKnown extends PO {
	/** Standard Constructor */
	public X_R_IssueKnown(Properties ctx, int R_IssueKnown_ID, String trxName) {
		super(ctx, R_IssueKnown_ID, trxName);
		/**
		 * if (R_IssueKnown_ID == 0) { setIssueSummary (null);
		 * setR_IssueKnown_ID (0); setReleaseNo (null); }
		 */
	}

	/** Load Constructor */
	public X_R_IssueKnown(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=R_IssueKnown */
	public static final String Table_Name = "R_IssueKnown";

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
		StringBuffer sb = new StringBuffer("X_R_IssueKnown[").append(get_ID())
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
	 * Set Issue Status. Current Status of the Issue
	 */
	public void setIssueStatus(String IssueStatus) {
		if (IssueStatus != null && IssueStatus.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			IssueStatus = IssueStatus.substring(0, 1999);
		}
		set_Value("IssueStatus", IssueStatus);
	}

	/**
	 * Get Issue Status. Current Status of the Issue
	 */
	public String getIssueStatus() {
		return (String) get_Value("IssueStatus");
	}

	/**
	 * Set Issue Summary. Issue Summary
	 */
	public void setIssueSummary(String IssueSummary) {
		if (IssueSummary == null)
			throw new IllegalArgumentException("IssueSummary is mandatory.");
		if (IssueSummary.length() > 255) {
			log.warning("Length > 255 - truncated");
			IssueSummary = IssueSummary.substring(0, 254);
		}
		set_Value("IssueSummary", IssueSummary);
	}

	/**
	 * Get Issue Summary. Issue Summary
	 */
	public String getIssueSummary() {
		return (String) get_Value("IssueSummary");
	}

	/**
	 * Set Line. Line No
	 */
	public void setLineNo(int LineNo) {
		set_Value("LineNo", new Integer(LineNo));
	}

	/**
	 * Get Line. Line No
	 */
	public int getLineNo() {
		Integer ii = (Integer) get_Value("LineNo");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Logger. Logger Name
	 */
	public void setLoggerName(String LoggerName) {
		if (LoggerName != null && LoggerName.length() > 60) {
			log.warning("Length > 60 - truncated");
			LoggerName = LoggerName.substring(0, 59);
		}
		set_Value("LoggerName", LoggerName);
	}

	/**
	 * Get Logger. Logger Name
	 */
	public String getLoggerName() {
		return (String) get_Value("LoggerName");
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
	 * Set Known Issue. Known Issue
	 */
	public void setR_IssueKnown_ID(int R_IssueKnown_ID) {
		if (R_IssueKnown_ID < 1)
			throw new IllegalArgumentException("R_IssueKnown_ID is mandatory.");
		set_ValueNoCheck("R_IssueKnown_ID", new Integer(R_IssueKnown_ID));
	}

	/**
	 * Get Known Issue. Known Issue
	 */
	public int getR_IssueKnown_ID() {
		Integer ii = (Integer) get_Value("R_IssueKnown_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Issue Recommendation. Recommendations how to fix an Issue
	 */
	public void setR_IssueRecommendation_ID(int R_IssueRecommendation_ID) {
		if (R_IssueRecommendation_ID <= 0)
			set_Value("R_IssueRecommendation_ID", null);
		else
			set_Value("R_IssueRecommendation_ID", new Integer(
					R_IssueRecommendation_ID));
	}

	/**
	 * Get Issue Recommendation. Recommendations how to fix an Issue
	 */
	public int getR_IssueRecommendation_ID() {
		Integer ii = (Integer) get_Value("R_IssueRecommendation_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Issue Status. Status of an Issue
	 */
	public void setR_IssueStatus_ID(int R_IssueStatus_ID) {
		if (R_IssueStatus_ID <= 0)
			set_Value("R_IssueStatus_ID", null);
		else
			set_Value("R_IssueStatus_ID", new Integer(R_IssueStatus_ID));
	}

	/**
	 * Get Issue Status. Status of an Issue
	 */
	public int getR_IssueStatus_ID() {
		Integer ii = (Integer) get_Value("R_IssueStatus_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Request. Request from a Business Partner or Prospect
	 */
	public void setR_Request_ID(int R_Request_ID) {
		if (R_Request_ID <= 0)
			set_Value("R_Request_ID", null);
		else
			set_Value("R_Request_ID", new Integer(R_Request_ID));
	}

	/**
	 * Get Request. Request from a Business Partner or Prospect
	 */
	public int getR_Request_ID() {
		Integer ii = (Integer) get_Value("R_Request_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Release No. Internal Release Number
	 */
	public void setReleaseNo(String ReleaseNo) {
		if (ReleaseNo == null)
			throw new IllegalArgumentException("ReleaseNo is mandatory.");
		if (ReleaseNo.length() > 4) {
			log.warning("Length > 4 - truncated");
			ReleaseNo = ReleaseNo.substring(0, 3);
		}
		set_Value("ReleaseNo", ReleaseNo);
	}

	/**
	 * Get Release No. Internal Release Number
	 */
	public String getReleaseNo() {
		return (String) get_Value("ReleaseNo");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getReleaseNo());
	}

	/**
	 * Set Source Class. Source Class Name
	 */
	public void setSourceClassName(String SourceClassName) {
		if (SourceClassName != null && SourceClassName.length() > 60) {
			log.warning("Length > 60 - truncated");
			SourceClassName = SourceClassName.substring(0, 59);
		}
		set_Value("SourceClassName", SourceClassName);
	}

	/**
	 * Get Source Class. Source Class Name
	 */
	public String getSourceClassName() {
		return (String) get_Value("SourceClassName");
	}

	/**
	 * Set Source Method. Source Method Name
	 */
	public void setSourceMethodName(String SourceMethodName) {
		if (SourceMethodName != null && SourceMethodName.length() > 60) {
			log.warning("Length > 60 - truncated");
			SourceMethodName = SourceMethodName.substring(0, 59);
		}
		set_Value("SourceMethodName", SourceMethodName);
	}

	/**
	 * Get Source Method. Source Method Name
	 */
	public String getSourceMethodName() {
		return (String) get_Value("SourceMethodName");
	}
}
