/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_User
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.531
 */
public class X_AD_User extends PO {
	/** Standard Constructor */
	public X_AD_User(Properties ctx, int AD_User_ID, String trxName) {
		super(ctx, AD_User_ID, trxName);
		/**
		 * if (AD_User_ID == 0) { setAD_User_ID (0); setIsFullBPAccess (true); //
		 * Y setName (null); setNotificationType (null); // E }
		 */
	}

	/** Load Constructor */
	public X_AD_User(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	
	/** TableName=AD_User */
	public static final String Table_Name = "AD_User";
	
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

	

	protected BigDecimal accessLevel = new BigDecimal(7);

	/** AccessLevel 7 - System - Client - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}


	public String toString() {
		StringBuffer sb = new StringBuffer("X_AD_User[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** AD_OrgTrx_ID AD_Reference_ID=130 */
	public static final int AD_ORGTRX_ID_AD_Reference_ID = 130;

	/**
	 * Set Trx Organization. Performing or initiating organization
	 */
	public void setAD_OrgTrx_ID(int AD_OrgTrx_ID) {
		if (AD_OrgTrx_ID <= 0)
			set_Value("AD_OrgTrx_ID", null);
		else
			set_Value("AD_OrgTrx_ID", new Integer(AD_OrgTrx_ID));
	}

	/**
	 * Get Trx Organization. Performing or initiating organization
	 */
	public int getAD_OrgTrx_ID() {
		Integer ii = (Integer) get_Value("AD_OrgTrx_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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

	/**
	 * Set Birthday. Birthday or Anniversary day
	 */
	public void setBirthday(Timestamp Birthday) {
		set_Value("Birthday", Birthday);
	}

	/**
	 * Get Birthday. Birthday or Anniversary day
	 */
	public Timestamp getBirthday() {
		return (Timestamp) get_Value("Birthday");
	}

	/** Set C_BPARTNER_CONTACT_ID */
	public void setC_BPARTNER_CONTACT_ID(int C_BPARTNER_CONTACT_ID) {
		if (C_BPARTNER_CONTACT_ID <= 0)
			set_Value("C_BPARTNER_CONTACT_ID", null);
		else
			set_Value("C_BPARTNER_CONTACT_ID", new Integer(
					C_BPARTNER_CONTACT_ID));
	}

	/** Get C_BPARTNER_CONTACT_ID */
	public int getC_BPARTNER_CONTACT_ID() {
		Integer ii = (Integer) get_Value("C_BPARTNER_CONTACT_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Business Partner . Identifies a Business Partner
	 */
	public void setC_BPartner_ID(int C_BPartner_ID) {
		if (C_BPartner_ID <= 0)
			set_Value("C_BPartner_ID", null);
		else
			set_Value("C_BPartner_ID", new Integer(C_BPartner_ID));
	}

	/**
	 * Get Business Partner . Identifies a Business Partner
	 */
	public int getC_BPartner_ID() {
		Integer ii = (Integer) get_Value("C_BPartner_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Partner Location. Identifies the (ship to) address for this Business
	 * Partner
	 */
	public void setC_BPartner_Location_ID(int C_BPartner_Location_ID) {
		if (C_BPartner_Location_ID <= 0)
			set_Value("C_BPartner_Location_ID", null);
		else
			set_Value("C_BPartner_Location_ID", new Integer(
					C_BPartner_Location_ID));
	}

	/**
	 * Get Partner Location. Identifies the (ship to) address for this Business
	 * Partner
	 */
	public int getC_BPartner_Location_ID() {
		Integer ii = (Integer) get_Value("C_BPartner_Location_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Greeting. Greeting to print on correspondence
	 */
	public void setC_Greeting_ID(int C_Greeting_ID) {
		if (C_Greeting_ID <= 0)
			set_Value("C_Greeting_ID", null);
		else
			set_Value("C_Greeting_ID", new Integer(C_Greeting_ID));
	}

	/**
	 * Get Greeting. Greeting to print on correspondence
	 */
	public int getC_Greeting_ID() {
		Integer ii = (Integer) get_Value("C_Greeting_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Position. Job Position
	 */
	public void setC_Job_ID(int C_Job_ID) {
		if (C_Job_ID <= 0)
			set_Value("C_Job_ID", null);
		else
			set_Value("C_Job_ID", new Integer(C_Job_ID));
	}

	/**
	 * Get Position. Job Position
	 */
	public int getC_Job_ID() {
		Integer ii = (Integer) get_Value("C_Job_ID");
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
	 * Set EMail Address. Electronic Mail Address
	 */
	public void setEMail(String EMail) {
		if (EMail != null && EMail.length() > 60) {
			log.warning("Length > 60 - truncated");
			EMail = EMail.substring(0, 59);
		}
		set_Value("EMail", EMail);
	}

	/**
	 * Get EMail Address. Electronic Mail Address
	 */
	public String getEMail() {
		return (String) get_Value("EMail");
	}

	/**
	 * Set EMail User ID. User Name (ID) in the Mail System
	 */
	public void setEMailUser(String EMailUser) {
		if (EMailUser != null && EMailUser.length() > 60) {
			log.warning("Length > 60 - truncated");
			EMailUser = EMailUser.substring(0, 59);
		}
		set_Value("EMailUser", EMailUser);
	}

	/**
	 * Get EMail User ID. User Name (ID) in the Mail System
	 */
	public String getEMailUser() {
		return (String) get_Value("EMailUser");
	}

	/**
	 * Set EMail User Password. Password of your email user id
	 */
	public void setEMailUserPW(String EMailUserPW) {
		if (EMailUserPW != null && EMailUserPW.length() > 20) {
			log.warning("Length > 20 - truncated");
			EMailUserPW = EMailUserPW.substring(0, 19);
		}
		set_ValueE("EMailUserPW", EMailUserPW);
	}

	/**
	 * Get EMail User Password. Password of your email user id
	 */
	public String getEMailUserPW() {
		return (String) get_ValueE("EMailUserPW");
	}

	/**
	 * Set Verification Info. Verification information of EMail Address
	 */
	public void setEMailVerify(String EMailVerify) {
		if (EMailVerify != null && EMailVerify.length() > 40) {
			log.warning("Length > 40 - truncated");
			EMailVerify = EMailVerify.substring(0, 39);
		}
		set_ValueNoCheck("EMailVerify", EMailVerify);
	}

	/**
	 * Get Verification Info. Verification information of EMail Address
	 */
	public String getEMailVerify() {
		return (String) get_Value("EMailVerify");
	}

	/**
	 * Set EMail Verify. Date Email was verified
	 */
	public void setEMailVerifyDate(Timestamp EMailVerifyDate) {
		set_ValueNoCheck("EMailVerifyDate", EMailVerifyDate);
	}

	/**
	 * Get EMail Verify. Date Email was verified
	 */
	public Timestamp getEMailVerifyDate() {
		return (Timestamp) get_Value("EMailVerifyDate");
	}

	/**
	 * Set Fax. Facsimile number
	 */
	public void setFax(String Fax) {
		if (Fax != null && Fax.length() > 40) {
			log.warning("Length > 40 - truncated");
			Fax = Fax.substring(0, 39);
		}
		set_Value("Fax", Fax);
	}

	/**
	 * Get Fax. Facsimile number
	 */
	public String getFax() {
		return (String) get_Value("Fax");
	}

	/**
	 * Set Full BP Access. The user/concat has full access to Business Partner
	 * information and resources
	 */
	public void setIsFullBPAccess(boolean IsFullBPAccess) {
		set_Value("IsFullBPAccess", new Boolean(IsFullBPAccess));
	}

	/**
	 * Get Full BP Access. The user/concat has full access to Business Partner
	 * information and resources
	 */
	public boolean isFullBPAccess() {
		Object oo = get_Value("IsFullBPAccess");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set LDAP User Name. User Name used for authorization via LDAP (directory)
	 * services
	 */
	public void setLDAPUser(String LDAPUser) {
		if (LDAPUser != null && LDAPUser.length() > 60) {
			log.warning("Length > 60 - truncated");
			LDAPUser = LDAPUser.substring(0, 59);
		}
		set_Value("LDAPUser", LDAPUser);
	}

	/**
	 * Get LDAP User Name. User Name used for authorization via LDAP (directory)
	 * services
	 */
	public String getLDAPUser() {
		return (String) get_Value("LDAPUser");
	}

	/**
	 * Set Last Contact. Date this individual was last contacted
	 */
	public void setLastContact(Timestamp LastContact) {
		set_Value("LastContact", LastContact);
	}

	/**
	 * Get Last Contact. Date this individual was last contacted
	 */
	public Timestamp getLastContact() {
		return (Timestamp) get_Value("LastContact");
	}

	/**
	 * Set Last Result. Result of last contact
	 */
	public void setLastResult(String LastResult) {
		if (LastResult != null && LastResult.length() > 255) {
			log.warning("Length > 255 - truncated");
			LastResult = LastResult.substring(0, 254);
		}
		set_Value("LastResult", LastResult);
	}

	/**
	 * Get Last Result. Result of last contact
	 */
	public String getLastResult() {
		return (String) get_Value("LastResult");
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

	/** NotificationType AD_Reference_ID=344 */
	public static final int NOTIFICATIONTYPE_AD_Reference_ID = 344;

	/** EMail+Notice = B */
	public static final String NOTIFICATIONTYPE_EMailPlusNotice = "B";

	/** EMail = E */
	public static final String NOTIFICATIONTYPE_EMail = "E";

	/** Notice = N */
	public static final String NOTIFICATIONTYPE_Notice = "N";

	/** None = X */
	public static final String NOTIFICATIONTYPE_None = "X";

	/**
	 * Set Notification Type. Type of Notifications
	 */
	public void setNotificationType(String NotificationType) {
		if (NotificationType == null)
			throw new IllegalArgumentException("NotificationType is mandatory");
		if (NotificationType.equals("B") || NotificationType.equals("E")
				|| NotificationType.equals("N") || NotificationType.equals("X"))
			;
		else
			throw new IllegalArgumentException(
					"NotificationType Invalid value - " + NotificationType
							+ " - Reference_ID=344 - B - E - N - X");
		if (NotificationType.length() > 1) {
			log.warning("Length > 1 - truncated");
			NotificationType = NotificationType.substring(0, 0);
		}
		set_Value("NotificationType", NotificationType);
	}

	/**
	 * Get Notification Type. Type of Notifications
	 */
	public String getNotificationType() {
		return (String) get_Value("NotificationType");
	}

	/**
	 * Set Password. Password of any length (case sensitive)
	 */
	public void setPassword(String Password) {
		if (Password != null && Password.length() > 40) {
			log.warning("Length > 40 - truncated");
			Password = Password.substring(0, 39);
		}
		set_ValueE("Password", Password);
	}

	/**
	 * Get Password. Password of any length (case sensitive)
	 */
	public String getPassword() {
		return (String) get_ValueE("Password");
	}

	/**
	 * Set Phone. Identifies a telephone number
	 */
	public void setPhone(String Phone) {
		if (Phone != null && Phone.length() > 40) {
			log.warning("Length > 40 - truncated");
			Phone = Phone.substring(0, 39);
		}
		set_Value("Phone", Phone);
	}

	/**
	 * Get Phone. Identifies a telephone number
	 */
	public String getPhone() {
		return (String) get_Value("Phone");
	}

	/**
	 * Set 2nd Phone. Identifies an alternate telephone number.
	 */
	public void setPhone2(String Phone2) {
		if (Phone2 != null && Phone2.length() > 40) {
			log.warning("Length > 40 - truncated");
			Phone2 = Phone2.substring(0, 39);
		}
		set_Value("Phone2", Phone2);
	}

	/**
	 * Get 2nd Phone. Identifies an alternate telephone number.
	 */
	public String getPhone2() {
		return (String) get_Value("Phone2");
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

	/** Supervisor_ID AD_Reference_ID=110 */
	public static final int SUPERVISOR_ID_AD_Reference_ID = 110;

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

	/**
	 * Set Title. Name this entity is referred to as
	 */
	public void setTitle(String Title) {
		if (Title != null && Title.length() > 40) {
			log.warning("Length > 40 - truncated");
			Title = Title.substring(0, 39);
		}
		set_Value("Title", Title);
	}

	/**
	 * Get Title. Name this entity is referred to as
	 */
	public String getTitle() {
		return (String) get_Value("Title");
	}
}
