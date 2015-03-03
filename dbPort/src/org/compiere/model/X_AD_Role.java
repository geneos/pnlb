/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Role
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.203
 */
public class X_AD_Role extends PO {
	/** Standard Constructor */
	public X_AD_Role(Properties ctx, int AD_Role_ID, String trxName) {
		super(ctx, AD_Role_ID, trxName);
		/**
		 * if (AD_Role_ID == 0) { setAD_Role_ID (0); setConfirmQueryRecords (0); //
		 * 0 setIsAccessAllOrgs (false); // N setIsCanApproveOwnDoc (false);
		 * setIsCanExport (true); // Y setIsCanReport (true); // Y
		 * setIsChangeLog (false); // N setIsManual (false); setIsPersonalAccess
		 * (false); // N setIsPersonalLock (false); // N setIsShowAcct (false); //
		 * N setIsUseUserOrgAccess (false); // N setMaxQueryRecords (0); // 0
		 * setName (null); setOverwritePriceLimit (false); // N
		 * setPreferenceType (null); // O setUserLevel (null); // O }
		 */
	}

	/** Load Constructor */
	public X_AD_Role(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Role */
	public static final String Table_Name = "AD_Role";

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
		StringBuffer sb = new StringBuffer("X_AD_Role[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Role. Responsibility Role
	 */
	public void setAD_Role_ID(int AD_Role_ID) {
		if (AD_Role_ID < 0)
			throw new IllegalArgumentException("AD_Role_ID is mandatory.");
		set_ValueNoCheck("AD_Role_ID", new Integer(AD_Role_ID));
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

	/** AD_Tree_Menu_ID AD_Reference_ID=184 */
	public static final int AD_TREE_MENU_ID_AD_Reference_ID = 184;

	/**
	 * Set Menu Tree. Tree of the menu
	 */
	public void setAD_Tree_Menu_ID(int AD_Tree_Menu_ID) {
		if (AD_Tree_Menu_ID <= 0)
			set_Value("AD_Tree_Menu_ID", null);
		else
			set_Value("AD_Tree_Menu_ID", new Integer(AD_Tree_Menu_ID));
	}

	/**
	 * Get Menu Tree. Tree of the menu
	 */
	public int getAD_Tree_Menu_ID() {
		Integer ii = (Integer) get_Value("AD_Tree_Menu_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_Tree_Org_ID AD_Reference_ID=184 */
	public static final int AD_TREE_ORG_ID_AD_Reference_ID = 184;

	/**
	 * Set Organization Tree. Tree to determine organizational hierarchy
	 */
	public void setAD_Tree_Org_ID(int AD_Tree_Org_ID) {
		if (AD_Tree_Org_ID <= 0)
			set_Value("AD_Tree_Org_ID", null);
		else
			set_Value("AD_Tree_Org_ID", new Integer(AD_Tree_Org_ID));
	}

	/**
	 * Get Organization Tree. Tree to determine organizational hierarchy
	 */
	public int getAD_Tree_Org_ID() {
		Integer ii = (Integer) get_Value("AD_Tree_Org_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Approval Amount. The approval amount limit for this role
	 */
	public void setAmtApproval(BigDecimal AmtApproval) {
		set_Value("AmtApproval", AmtApproval);
	}

	/**
	 * Get Approval Amount. The approval amount limit for this role
	 */
	public BigDecimal getAmtApproval() {
		BigDecimal bd = (BigDecimal) get_Value("AmtApproval");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Currency. The Currency for this record
	 */
	public void setC_Currency_ID(int C_Currency_ID) {
		if (C_Currency_ID <= 0)
			set_Value("C_Currency_ID", null);
		else
			set_Value("C_Currency_ID", new Integer(C_Currency_ID));
	}

	/**
	 * Get Currency. The Currency for this record
	 */
	public int getC_Currency_ID() {
		Integer ii = (Integer) get_Value("C_Currency_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Confirm Query Records. Require Confirmation if more records will be
	 * returned by the query (If not defined 500)
	 */
	public void setConfirmQueryRecords(int ConfirmQueryRecords) {
		set_Value("ConfirmQueryRecords", new Integer(ConfirmQueryRecords));
	}

	/**
	 * Get Confirm Query Records. Require Confirmation if more records will be
	 * returned by the query (If not defined 500)
	 */
	public int getConfirmQueryRecords() {
		Integer ii = (Integer) get_Value("ConfirmQueryRecords");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** ConnectionProfile AD_Reference_ID=364 */
	public static final int CONNECTIONPROFILE_AD_Reference_ID = 364;

	/** LAN = L */
	public static final String CONNECTIONPROFILE_LAN = "L";

	/** Terminal Server = T */
	public static final String CONNECTIONPROFILE_TerminalServer = "T";

	/** VPN = V */
	public static final String CONNECTIONPROFILE_VPN = "V";

	/** WAN = W */
	public static final String CONNECTIONPROFILE_WAN = "W";

	/**
	 * Set Connection Profile. How a Java Client connects to the server(s)
	 */
	public void setConnectionProfile(String ConnectionProfile) {
		if (ConnectionProfile != null && ConnectionProfile.length() > 1) {
			log.warning("Length > 1 - truncated");
			ConnectionProfile = ConnectionProfile.substring(0, 0);
		}
		set_Value("ConnectionProfile", ConnectionProfile);
	}

	/**
	 * Get Connection Profile. How a Java Client connects to the server(s)
	 */
	public String getConnectionProfile() {
		return (String) get_Value("ConnectionProfile");
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
	 * Set Access all Orgs. Access all Organizations (no org access control) of
	 * the client
	 */
	public void setIsAccessAllOrgs(boolean IsAccessAllOrgs) {
		set_Value("IsAccessAllOrgs", new Boolean(IsAccessAllOrgs));
	}

	/**
	 * Get Access all Orgs. Access all Organizations (no org access control) of
	 * the client
	 */
	public boolean isAccessAllOrgs() {
		Object oo = get_Value("IsAccessAllOrgs");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Approve own Documents. Users with this role can approve their own
	 * documents
	 */
	public void setIsCanApproveOwnDoc(boolean IsCanApproveOwnDoc) {
		set_Value("IsCanApproveOwnDoc", new Boolean(IsCanApproveOwnDoc));
	}

	/**
	 * Get Approve own Documents. Users with this role can approve their own
	 * documents
	 */
	public boolean isCanApproveOwnDoc() {
		Object oo = get_Value("IsCanApproveOwnDoc");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Can Export. Users with this role can export data
	 */
	public void setIsCanExport(boolean IsCanExport) {
		set_Value("IsCanExport", new Boolean(IsCanExport));
	}

	/**
	 * Get Can Export. Users with this role can export data
	 */
	public boolean isCanExport() {
		Object oo = get_Value("IsCanExport");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Can Report. Users with this role can create reports
	 */
	public void setIsCanReport(boolean IsCanReport) {
		set_Value("IsCanReport", new Boolean(IsCanReport));
	}

	/**
	 * Get Can Report. Users with this role can create reports
	 */
	public boolean isCanReport() {
		Object oo = get_Value("IsCanReport");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Maintain Change Log. Maintain a log of changes
	 */
	public void setIsChangeLog(boolean IsChangeLog) {
		set_Value("IsChangeLog", new Boolean(IsChangeLog));
	}

	/**
	 * Get Maintain Change Log. Maintain a log of changes
	 */
	public boolean isChangeLog() {
		Object oo = get_Value("IsChangeLog");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Manual. This is a manual process
	 */
	public void setIsManual(boolean IsManual) {
		set_Value("IsManual", new Boolean(IsManual));
	}

	/**
	 * Get Manual. This is a manual process
	 */
	public boolean isManual() {
		Object oo = get_Value("IsManual");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Personal Access. Allow access to all personal records
	 */
	public void setIsPersonalAccess(boolean IsPersonalAccess) {
		set_Value("IsPersonalAccess", new Boolean(IsPersonalAccess));
	}

	/**
	 * Get Personal Access. Allow access to all personal records
	 */
	public boolean isPersonalAccess() {
		Object oo = get_Value("IsPersonalAccess");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Personal Lock. Allow users with role to lock access to personal
	 * records
	 */
	public void setIsPersonalLock(boolean IsPersonalLock) {
		set_Value("IsPersonalLock", new Boolean(IsPersonalLock));
	}

	/**
	 * Get Personal Lock. Allow users with role to lock access to personal
	 * records
	 */
	public boolean isPersonalLock() {
		Object oo = get_Value("IsPersonalLock");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Show Accounting. Users with this role can see accounting information
	 */
	public void setIsShowAcct(boolean IsShowAcct) {
		set_Value("IsShowAcct", new Boolean(IsShowAcct));
	}

	/**
	 * Get Show Accounting. Users with this role can see accounting information
	 */
	public boolean isShowAcct() {
		Object oo = get_Value("IsShowAcct");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Use User Org Access. Use Org Access defined by user instead of Role
	 * Org Access
	 */
	public void setIsUseUserOrgAccess(boolean IsUseUserOrgAccess) {
		set_Value("IsUseUserOrgAccess", new Boolean(IsUseUserOrgAccess));
	}

	/**
	 * Get Use User Org Access. Use Org Access defined by user instead of Role
	 * Org Access
	 */
	public boolean isUseUserOrgAccess() {
		Object oo = get_Value("IsUseUserOrgAccess");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Max Query Records. If defined, you cannot query more records as
	 * defined - the query criteria needs to be changed to query less records
	 */
	public void setMaxQueryRecords(int MaxQueryRecords) {
		set_Value("MaxQueryRecords", new Integer(MaxQueryRecords));
	}

	/**
	 * Get Max Query Records. If defined, you cannot query more records as
	 * defined - the query criteria needs to be changed to query less records
	 */
	public int getMaxQueryRecords() {
		Integer ii = (Integer) get_Value("MaxQueryRecords");
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

	/**
	 * Set Overwrite Price Limit. Overwrite Price Limit if the Price List
	 * enforces the Price Limit
	 */
	public void setOverwritePriceLimit(boolean OverwritePriceLimit) {
		set_Value("OverwritePriceLimit", new Boolean(OverwritePriceLimit));
	}

	/**
	 * Get Overwrite Price Limit. Overwrite Price Limit if the Price List
	 * enforces the Price Limit
	 */
	public boolean isOverwritePriceLimit() {
		Object oo = get_Value("OverwritePriceLimit");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** PreferenceType AD_Reference_ID=330 */
	public static final int PREFERENCETYPE_AD_Reference_ID = 330;

	/** Client = C */
	public static final String PREFERENCETYPE_Client = "C";

	/** None = N */
	public static final String PREFERENCETYPE_None = "N";

	/** Organization = O */
	public static final String PREFERENCETYPE_Organization = "O";

	/** User = U */
	public static final String PREFERENCETYPE_User = "U";

	/**
	 * Set Preference Level. Determines what preferences the user can set
	 */
	public void setPreferenceType(String PreferenceType) {
		if (PreferenceType == null)
			throw new IllegalArgumentException("PreferenceType is mandatory");
		if (PreferenceType.equals("C") || PreferenceType.equals("N")
				|| PreferenceType.equals("O") || PreferenceType.equals("U"))
			;
		else
			throw new IllegalArgumentException(
					"PreferenceType Invalid value - " + PreferenceType
							+ " - Reference_ID=330 - C - N - O - U");
		if (PreferenceType.length() > 1) {
			log.warning("Length > 1 - truncated");
			PreferenceType = PreferenceType.substring(0, 0);
		}
		set_Value("PreferenceType", PreferenceType);
	}

	/**
	 * Get Preference Level. Determines what preferences the user can set
	 */
	public String getPreferenceType() {
		return (String) get_Value("PreferenceType");
	}

	/** Supervisor_ID AD_Reference_ID=286 */
	public static final int SUPERVISOR_ID_AD_Reference_ID = 286;

	/**
	 * Set Supervisor. Supervisor for this user/organization - used for
	 * escalation and approval
	 */
	public void setSupervisor_ID(int Supervisor_ID) {
		if (Supervisor_ID <= 0)
			set_Value("Supervisor_ID", null);
		else
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

	/** UserLevel AD_Reference_ID=226 */
	public static final int USERLEVEL_AD_Reference_ID = 226;

	/** Organization = O */
	public static final String USERLEVEL_Organization = "  O";

	/** Client = C */
	public static final String USERLEVEL_Client = " C ";

	/** Client+Organization = CO */
	public static final String USERLEVEL_ClientPlusOrganization = " CO";

	/** System = S */
	public static final String USERLEVEL_System = "S  ";

	/**
	 * Set User Level. System Client Organization
	 */
	public void setUserLevel(String UserLevel) {
		if (UserLevel == null)
			throw new IllegalArgumentException("UserLevel is mandatory");
		if (UserLevel.equals("  O") || UserLevel.equals(" C ")
				|| UserLevel.equals(" CO") || UserLevel.equals("S  "))
			;
		else
			throw new IllegalArgumentException("UserLevel Invalid value - "
					+ UserLevel + " - Reference_ID=226 -   O -  C  -  CO - S  ");
		if (UserLevel.length() > 3) {
			log.warning("Length > 3 - truncated");
			UserLevel = UserLevel.substring(0, 2);
		}
		set_Value("UserLevel", UserLevel);
	}

	/**
	 * Get User Level. System Client Organization
	 */
	public String getUserLevel() {
		return (String) get_Value("UserLevel");
	}
}
