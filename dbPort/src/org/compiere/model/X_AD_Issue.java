/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Issue
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.515
 */
public class X_AD_Issue extends PO {
	/** Standard Constructor */
	public X_AD_Issue(Properties ctx, int AD_Issue_ID, String trxName) {
		super(ctx, AD_Issue_ID, trxName);
		/**
		 * if (AD_Issue_ID == 0) { setAD_Issue_ID (0); setIssueSummary (null);
		 * setName (null); // . setProcessed (false); // N setReleaseNo (null); // .
		 * setSystemStatus (null); // E setUserName (null); // . setVersion
		 * (null); // . }
		 */
	}

	/** Load Constructor */
	public X_AD_Issue(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Issue */
	public static final String Table_Name = "AD_Issue";

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
		StringBuffer sb = new StringBuffer("X_AD_Issue[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set System Issue. Automatically created or manually entered System Issue
	 */
	public void setAD_Issue_ID(int AD_Issue_ID) {
		if (AD_Issue_ID < 1)
			throw new IllegalArgumentException("AD_Issue_ID is mandatory.");
		set_ValueNoCheck("AD_Issue_ID", new Integer(AD_Issue_ID));
	}

	/**
	 * Get System Issue. Automatically created or manually entered System Issue
	 */
	public int getAD_Issue_ID() {
		Integer ii = (Integer) get_Value("AD_Issue_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Asset. Asset used internally or by customers
	 */
	public void setA_Asset_ID(int A_Asset_ID) {
		if (A_Asset_ID <= 0)
			set_ValueNoCheck("A_Asset_ID", null);
		else
			set_ValueNoCheck("A_Asset_ID", new Integer(A_Asset_ID));
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
	 * Set Comments. Comments or additional information
	 */
	public void setComments(String Comments) {
		if (Comments != null && Comments.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Comments = Comments.substring(0, 1999);
		}
		set_Value("Comments", Comments);
	}

	/**
	 * Get Comments. Comments or additional information
	 */
	public String getComments() {
		return (String) get_Value("Comments");
	}

	/**
	 * Set DB Address. JDBC URL of the database server
	 */
	public void setDBAddress(String DBAddress) {
		if (DBAddress != null && DBAddress.length() > 255) {
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

	/**
	 * Set Database. Database Information
	 */
	public void setDatabaseInfo(String DatabaseInfo) {
		if (DatabaseInfo != null && DatabaseInfo.length() > 255) {
			log.warning("Length > 255 - truncated");
			DatabaseInfo = DatabaseInfo.substring(0, 254);
		}
		set_ValueNoCheck("DatabaseInfo", DatabaseInfo);
	}

	/**
	 * Get Database. Database Information
	 */
	public String getDatabaseInfo() {
		return (String) get_Value("DatabaseInfo");
	}

	/**
	 * Set Error Trace. System Error Trace
	 */
	public void setErrorTrace(String ErrorTrace) {
		if (ErrorTrace != null && ErrorTrace.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			ErrorTrace = ErrorTrace.substring(0, 1999);
		}
		set_Value("ErrorTrace", ErrorTrace);
	}

	/**
	 * Get Error Trace. System Error Trace
	 */
	public String getErrorTrace() {
		return (String) get_Value("ErrorTrace");
	}

	/** IsReproducible AD_Reference_ID=319 */
	public static final int ISREPRODUCIBLE_AD_Reference_ID = 319;

	/** No = N */
	public static final String ISREPRODUCIBLE_No = "N";

	/** Yes = Y */
	public static final String ISREPRODUCIBLE_Yes = "Y";

	/**
	 * Set Reproducible. Problem can re reproduced in Gardenworld
	 */
	public void setIsReproducible(String IsReproducible) {
		if (IsReproducible != null && IsReproducible.length() > 1) {
			log.warning("Length > 1 - truncated");
			IsReproducible = IsReproducible.substring(0, 0);
		}
		set_Value("IsReproducible", IsReproducible);
	}

	/**
	 * Get Reproducible. Problem can re reproduced in Gardenworld
	 */
	public String getIsReproducible() {
		return (String) get_Value("IsReproducible");
	}

	/** IsVanillaSystem AD_Reference_ID=319 */
	public static final int ISVANILLASYSTEM_AD_Reference_ID = 319;

	/** No = N */
	public static final String ISVANILLASYSTEM_No = "N";

	/** Yes = Y */
	public static final String ISVANILLASYSTEM_Yes = "Y";

	/**
	 * Set Vanilla System. The system was NOT compiled from Source - i.e.
	 * standard distribution
	 */
	public void setIsVanillaSystem(String IsVanillaSystem) {
		if (IsVanillaSystem != null && IsVanillaSystem.length() > 1) {
			log.warning("Length > 1 - truncated");
			IsVanillaSystem = IsVanillaSystem.substring(0, 0);
		}
		set_Value("IsVanillaSystem", IsVanillaSystem);
	}

	/**
	 * Get Vanilla System. The system was NOT compiled from Source - i.e.
	 * standard distribution
	 */
	public String getIsVanillaSystem() {
		return (String) get_Value("IsVanillaSystem");
	}

	/**
	 * Set Issue Summary. Issue Summary
	 */
	public void setIssueSummary(String IssueSummary) {
		if (IssueSummary == null)
			throw new IllegalArgumentException("IssueSummary is mandatory.");
		if (IssueSummary.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			IssueSummary = IssueSummary.substring(0, 1999);
		}
		set_Value("IssueSummary", IssueSummary);
	}

	/**
	 * Get Issue Summary. Issue Summary
	 */
	public String getIssueSummary() {
		return (String) get_Value("IssueSummary");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getIssueSummary());
	}

	/**
	 * Set Java Info. Java Version Info
	 */
	public void setJavaInfo(String JavaInfo) {
		if (JavaInfo != null && JavaInfo.length() > 255) {
			log.warning("Length > 255 - truncated");
			JavaInfo = JavaInfo.substring(0, 254);
		}
		set_ValueNoCheck("JavaInfo", JavaInfo);
	}

	/**
	 * Get Java Info. Java Version Info
	 */
	public String getJavaInfo() {
		return (String) get_Value("JavaInfo");
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
	 * Set Local Host. Local Host Info
	 */
	public void setLocal_Host(String Local_Host) {
		if (Local_Host != null && Local_Host.length() > 120) {
			log.warning("Length > 120 - truncated");
			Local_Host = Local_Host.substring(0, 119);
		}
		set_ValueNoCheck("Local_Host", Local_Host);
	}

	/**
	 * Get Local Host. Local Host Info
	 */
	public String getLocal_Host() {
		return (String) get_Value("Local_Host");
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

	/**
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name == null)
			throw new IllegalArgumentException("Name is mandatory.");
		if (Name.length() > 120) {
			log.warning("Length > 120 - truncated");
			Name = Name.substring(0, 119);
		}
		set_ValueNoCheck("Name", Name);
	}

	/**
	 * Get Name. Alphanumeric identifier of the entity
	 */
	public String getName() {
		return (String) get_Value("Name");
	}

	/**
	 * Set Operating System. Operating System Info
	 */
	public void setOperatingSystemInfo(String OperatingSystemInfo) {
		if (OperatingSystemInfo != null && OperatingSystemInfo.length() > 255) {
			log.warning("Length > 255 - truncated");
			OperatingSystemInfo = OperatingSystemInfo.substring(0, 254);
		}
		set_ValueNoCheck("OperatingSystemInfo", OperatingSystemInfo);
	}

	/**
	 * Get Operating System. Operating System Info
	 */
	public String getOperatingSystemInfo() {
		return (String) get_Value("OperatingSystemInfo");
	}

	/**
	 * Set Processed. The document has been processed
	 */
	public void setProcessed(boolean Processed) {
		set_ValueNoCheck("Processed", new Boolean(Processed));
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
	 * Set Profile. Information to help profiling the system for solving support
	 * issues
	 */
	public void setProfileInfo(String ProfileInfo) {
		if (ProfileInfo != null && ProfileInfo.length() > 255) {
			log.warning("Length > 255 - truncated");
			ProfileInfo = ProfileInfo.substring(0, 254);
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
	 * Set Known Issue. Known Issue
	 */
	public void setR_IssueKnown_ID(int R_IssueKnown_ID) {
		if (R_IssueKnown_ID <= 0)
			set_Value("R_IssueKnown_ID", null);
		else
			set_Value("R_IssueKnown_ID", new Integer(R_IssueKnown_ID));
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
	 * Set Issue Project. Implementation Projects
	 */
	public void setR_IssueProject_ID(int R_IssueProject_ID) {
		if (R_IssueProject_ID <= 0)
			set_Value("R_IssueProject_ID", null);
		else
			set_Value("R_IssueProject_ID", new Integer(R_IssueProject_ID));
	}

	/**
	 * Get Issue Project. Implementation Projects
	 */
	public int getR_IssueProject_ID() {
		Integer ii = (Integer) get_Value("R_IssueProject_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Issue System. System creating the issue
	 */
	public void setR_IssueSystem_ID(int R_IssueSystem_ID) {
		if (R_IssueSystem_ID <= 0)
			set_Value("R_IssueSystem_ID", null);
		else
			set_Value("R_IssueSystem_ID", new Integer(R_IssueSystem_ID));
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
	 * Set IssueUser. User who reported issues
	 */
	public void setR_IssueUser_ID(int R_IssueUser_ID) {
		if (R_IssueUser_ID <= 0)
			set_Value("R_IssueUser_ID", null);
		else
			set_Value("R_IssueUser_ID", new Integer(R_IssueUser_ID));
	}

	/**
	 * Get IssueUser. User who reported issues
	 */
	public int getR_IssueUser_ID() {
		Integer ii = (Integer) get_Value("R_IssueUser_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Request. Request from a Business Partner or Prospect
	 */
	public void setR_Request_ID(int R_Request_ID) {
		if (R_Request_ID <= 0)
			set_ValueNoCheck("R_Request_ID", null);
		else
			set_ValueNoCheck("R_Request_ID", new Integer(R_Request_ID));
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
	 * Set Record ID. Direct internal record ID
	 */
	public void setRecord_ID(int Record_ID) {
		if (Record_ID <= 0)
			set_ValueNoCheck("Record_ID", null);
		else
			set_ValueNoCheck("Record_ID", new Integer(Record_ID));
	}

	/**
	 * Get Record ID. Direct internal record ID
	 */
	public int getRecord_ID() {
		Integer ii = (Integer) get_Value("Record_ID");
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
		set_ValueNoCheck("ReleaseNo", ReleaseNo);
	}

	/**
	 * Get Release No. Internal Release Number
	 */
	public String getReleaseNo() {
		return (String) get_Value("ReleaseNo");
	}

	/**
	 * Set Release Tag. Release Tag
	 */
	public void setReleaseTag(String ReleaseTag) {
		if (ReleaseTag != null && ReleaseTag.length() > 60) {
			log.warning("Length > 60 - truncated");
			ReleaseTag = ReleaseTag.substring(0, 59);
		}
		set_Value("ReleaseTag", ReleaseTag);
	}

	/**
	 * Get Release Tag. Release Tag
	 */
	public String getReleaseTag() {
		return (String) get_Value("ReleaseTag");
	}

	/**
	 * Set Remote Addr. Remote Address
	 */
	public void setRemote_Addr(String Remote_Addr) {
		if (Remote_Addr != null && Remote_Addr.length() > 60) {
			log.warning("Length > 60 - truncated");
			Remote_Addr = Remote_Addr.substring(0, 59);
		}
		set_ValueNoCheck("Remote_Addr", Remote_Addr);
	}

	/**
	 * Get Remote Addr. Remote Address
	 */
	public String getRemote_Addr() {
		return (String) get_Value("Remote_Addr");
	}

	/**
	 * Set Remote Host. Remote host Info
	 */
	public void setRemote_Host(String Remote_Host) {
		if (Remote_Host != null && Remote_Host.length() > 120) {
			log.warning("Length > 120 - truncated");
			Remote_Host = Remote_Host.substring(0, 119);
		}
		set_ValueNoCheck("Remote_Host", Remote_Host);
	}

	/**
	 * Get Remote Host. Remote host Info
	 */
	public String getRemote_Host() {
		return (String) get_Value("Remote_Host");
	}

	/**
	 * Set Request Document No. Compiere Request Document No
	 */
	public void setRequestDocumentNo(String RequestDocumentNo) {
		if (RequestDocumentNo != null && RequestDocumentNo.length() > 30) {
			log.warning("Length > 30 - truncated");
			RequestDocumentNo = RequestDocumentNo.substring(0, 29);
		}
		set_ValueNoCheck("RequestDocumentNo", RequestDocumentNo);
	}

	/**
	 * Get Request Document No. Compiere Request Document No
	 */
	public String getRequestDocumentNo() {
		return (String) get_Value("RequestDocumentNo");
	}

	/**
	 * Set Response Text. Request Response Text
	 */
	public void setResponseText(String ResponseText) {
		if (ResponseText != null && ResponseText.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			ResponseText = ResponseText.substring(0, 1999);
		}
		set_ValueNoCheck("ResponseText", ResponseText);
	}

	/**
	 * Get Response Text. Request Response Text
	 */
	public String getResponseText() {
		return (String) get_Value("ResponseText");
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

	/**
	 * Set Stack Trace. System Log Trace
	 */
	public void setStackTrace(String StackTrace) {
		if (StackTrace != null && StackTrace.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			StackTrace = StackTrace.substring(0, 1999);
		}
		set_Value("StackTrace", StackTrace);
	}

	/**
	 * Get Stack Trace. System Log Trace
	 */
	public String getStackTrace() {
		return (String) get_Value("StackTrace");
	}

	/**
	 * Set Statistics. Information to help profiling the system for solving
	 * support issues
	 */
	public void setStatisticsInfo(String StatisticsInfo) {
		if (StatisticsInfo != null && StatisticsInfo.length() > 255) {
			log.warning("Length > 255 - truncated");
			StatisticsInfo = StatisticsInfo.substring(0, 254);
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

	/**
	 * Set Support EMail. EMail address to send support information and updates
	 * to
	 */
	public void setSupportEMail(String SupportEMail) {
		if (SupportEMail != null && SupportEMail.length() > 60) {
			log.warning("Length > 60 - truncated");
			SupportEMail = SupportEMail.substring(0, 59);
		}
		set_Value("SupportEMail", SupportEMail);
	}

	/**
	 * Get Support EMail. EMail address to send support information and updates
	 * to
	 */
	public String getSupportEMail() {
		return (String) get_Value("SupportEMail");
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

	/**
	 * Set Registered EMail. Email of the responsible for the System
	 */
	public void setUserName(String UserName) {
		if (UserName == null)
			throw new IllegalArgumentException("UserName is mandatory.");
		if (UserName.length() > 60) {
			log.warning("Length > 60 - truncated");
			UserName = UserName.substring(0, 59);
		}
		set_ValueNoCheck("UserName", UserName);
	}

	/**
	 * Get Registered EMail. Email of the responsible for the System
	 */
	public String getUserName() {
		return (String) get_Value("UserName");
	}

	/**
	 * Set Version. Version of the table definition
	 */
	public void setVersion(String Version) {
		if (Version == null)
			throw new IllegalArgumentException("Version is mandatory.");
		if (Version.length() > 40) {
			log.warning("Length > 40 - truncated");
			Version = Version.substring(0, 39);
		}
		set_ValueNoCheck("Version", Version);
	}

	/**
	 * Get Version. Version of the table definition
	 */
	public String getVersion() {
		return (String) get_Value("Version");
	}
}
