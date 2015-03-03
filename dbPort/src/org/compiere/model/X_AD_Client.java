/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Client
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.187
 */
public class X_AD_Client extends PO {
	/** Standard Constructor */
	public X_AD_Client(Properties ctx, int AD_Client_ID, String trxName) {
		super(ctx, AD_Client_ID, trxName);
		/**
		 * if (AD_Client_ID == 0) { setAutoArchive (null); // N
		 * setIsCostImmediate (false); // N setIsMultiLingualDocument (false);
		 * setIsPostImmediate (false); // N setIsServerEMail (false);
		 * setIsSmtpAuthorization (false); // N setIsUseBetaFunctions (true); //
		 * Y setMMPolicy (null); // F setName (null); setValue (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_Client(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Client */
	public static final String Table_Name = "AD_Client";

	/** AD_Table_ID */
	public int Table_ID;

	protected KeyNamePair Model;

	/** Load Meta Data */
	protected POInfo initPO(Properties ctx) {
		POInfo info = initPO(ctx, Table_Name);
		Table_ID = info.getAD_Table_ID();
		Model = new KeyNamePair(Table_ID,Table_Name);
		return info;
	}

	protected BigDecimal accessLevel = new BigDecimal(6);

	/** AccessLevel 6 - System - Client */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_AD_Client[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** AD_Language AD_Reference_ID=327 */
	public static final int AD_LANGUAGE_AD_Reference_ID = 327;

	/**
	 * Set Language. Language for this entity
	 */
	public void setAD_Language(String AD_Language) {
		if (AD_Language != null && AD_Language.length() > 6) {
			log.warning("Length > 6 - truncated");
			AD_Language = AD_Language.substring(0, 5);
		}
		set_Value("AD_Language", AD_Language);
	}

	/**
	 * Get Language. Language for this entity
	 */
	public String getAD_Language() {
		return (String) get_Value("AD_Language");
	}

	/** AutoArchive AD_Reference_ID=334 */
	public static final int AUTOARCHIVE_AD_Reference_ID = 334;

	/** All (Reports, Documents) = 1 */
	public static final String AUTOARCHIVE_AllReportsDocuments = "1";

	/** Documents = 2 */
	public static final String AUTOARCHIVE_Documents = "2";

	/** External Documents = 3 */
	public static final String AUTOARCHIVE_ExternalDocuments = "3";

	/** None = N */
	public static final String AUTOARCHIVE_None = "N";

	/**
	 * Set Auto Archive. Enable and level of automatic Archive of documents
	 */
	public void setAutoArchive(String AutoArchive) {
		if (AutoArchive == null)
			throw new IllegalArgumentException("AutoArchive is mandatory");
		if (AutoArchive.equals("1") || AutoArchive.equals("2")
				|| AutoArchive.equals("3") || AutoArchive.equals("N"))
			;
		else
			throw new IllegalArgumentException("AutoArchive Invalid value - "
					+ AutoArchive + " - Reference_ID=334 - 1 - 2 - 3 - N");
		if (AutoArchive.length() > 1) {
			log.warning("Length > 1 - truncated");
			AutoArchive = AutoArchive.substring(0, 0);
		}
		set_Value("AutoArchive", AutoArchive);
	}

	/**
	 * Get Auto Archive. Enable and level of automatic Archive of documents
	 */
	public String getAutoArchive() {
		return (String) get_Value("AutoArchive");
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

	/** Set DocumentDir */
	public void setDocumentDir(String DocumentDir) {
		if (DocumentDir != null && DocumentDir.length() > 60) {
			log.warning("Length > 60 - truncated");
			DocumentDir = DocumentDir.substring(0, 59);
		}
		set_Value("DocumentDir", DocumentDir);
	}

	/** Get DocumentDir */
	public String getDocumentDir() {
		return (String) get_Value("DocumentDir");
	}

	/**
	 * Set EMail Test. Test EMail
	 */
	public void setEMailTest(String EMailTest) {
		if (EMailTest != null && EMailTest.length() > 1) {
			log.warning("Length > 1 - truncated");
			EMailTest = EMailTest.substring(0, 0);
		}
		set_Value("EMailTest", EMailTest);
	}

	/**
	 * Get EMail Test. Test EMail
	 */
	public String getEMailTest() {
		return (String) get_Value("EMailTest");
	}

	/**
	 * Set Cost Immediately. Update Costs immediately for testing
	 */
	public void setIsCostImmediate(boolean IsCostImmediate) {
		set_Value("IsCostImmediate", new Boolean(IsCostImmediate));
	}

	/**
	 * Get Cost Immediately. Update Costs immediately for testing
	 */
	public boolean isCostImmediate() {
		Object oo = get_Value("IsCostImmediate");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Multi Lingual Documents. Documents are Multi Lingual
	 */
	public void setIsMultiLingualDocument(boolean IsMultiLingualDocument) {
		set_Value("IsMultiLingualDocument", new Boolean(IsMultiLingualDocument));
	}

	/**
	 * Get Multi Lingual Documents. Documents are Multi Lingual
	 */
	public boolean isMultiLingualDocument() {
		Object oo = get_Value("IsMultiLingualDocument");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Post Immediately. Post the accounting immediately for testing
	 */
	public void setIsPostImmediate(boolean IsPostImmediate) {
		set_Value("IsPostImmediate", new Boolean(IsPostImmediate));
	}

	/**
	 * Get Post Immediately. Post the accounting immediately for testing
	 */
	public boolean isPostImmediate() {
		Object oo = get_Value("IsPostImmediate");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Server EMail. Send EMail from Server
	 */
	public void setIsServerEMail(boolean IsServerEMail) {
		set_Value("IsServerEMail", new Boolean(IsServerEMail));
	}

	/**
	 * Get Server EMail. Send EMail from Server
	 */
	public boolean isServerEMail() {
		Object oo = get_Value("IsServerEMail");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set SMTP Authentification. Your mail server requires Authentification
	 */
	public void setIsSmtpAuthorization(boolean IsSmtpAuthorization) {
		set_Value("IsSmtpAuthorization", new Boolean(IsSmtpAuthorization));
	}

	/**
	 * Get SMTP Authentification. Your mail server requires Authentification
	 */
	public boolean isSmtpAuthorization() {
		Object oo = get_Value("IsSmtpAuthorization");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Use Beta Functions. Enable the use of Beta Functionality
	 */
	public void setIsUseBetaFunctions(boolean IsUseBetaFunctions) {
		set_Value("IsUseBetaFunctions", new Boolean(IsUseBetaFunctions));
	}

	/**
	 * Get Use Beta Functions. Enable the use of Beta Functionality
	 */
	public boolean isUseBetaFunctions() {
		Object oo = get_Value("IsUseBetaFunctions");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** MMPolicy AD_Reference_ID=335 */
	public static final int MMPOLICY_AD_Reference_ID = 335;

	/** FiFo = F */
	public static final String MMPOLICY_FiFo = "F";

	/** LiFo = L */
	public static final String MMPOLICY_LiFo = "L";

	/**
	 * Set Material Policy. Material Movement Policy
	 */
	public void setMMPolicy(String MMPolicy) {
		if (MMPolicy == null)
			throw new IllegalArgumentException("MMPolicy is mandatory");
		if (MMPolicy.equals("F") || MMPolicy.equals("L"))
			;
		else
			throw new IllegalArgumentException("MMPolicy Invalid value - "
					+ MMPolicy + " - Reference_ID=335 - F - L");
		if (MMPolicy.length() > 1) {
			log.warning("Length > 1 - truncated");
			MMPolicy = MMPolicy.substring(0, 0);
		}
		set_Value("MMPolicy", MMPolicy);
	}

	/**
	 * Get Material Policy. Material Movement Policy
	 */
	public String getMMPolicy() {
		return (String) get_Value("MMPolicy");
	}

	/**
	 * Set Model Validation Classes. List of data model validation classes
	 * separated by ;
	 */
	public void setModelValidationClasses(String ModelValidationClasses) {
		if (ModelValidationClasses != null
				&& ModelValidationClasses.length() > 255) {
			log.warning("Length > 255 - truncated");
			ModelValidationClasses = ModelValidationClasses.substring(0, 254);
		}
		set_Value("ModelValidationClasses", ModelValidationClasses);
	}

	/**
	 * Get Model Validation Classes. List of data model validation classes
	 * separated by ;
	 */
	public String getModelValidationClasses() {
		return (String) get_Value("ModelValidationClasses");
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
	 * Set Request EMail. EMail address to send automated mails from or receive
	 * mails for automated processing (fully qualified)
	 */
	public void setRequestEMail(String RequestEMail) {
		if (RequestEMail != null && RequestEMail.length() > 60) {
			log.warning("Length > 60 - truncated");
			RequestEMail = RequestEMail.substring(0, 59);
		}
		set_Value("RequestEMail", RequestEMail);
	}

	/**
	 * Get Request EMail. EMail address to send automated mails from or receive
	 * mails for automated processing (fully qualified)
	 */
	public String getRequestEMail() {
		return (String) get_Value("RequestEMail");
	}

	/**
	 * Set Request Folder. EMail folder to process incoming emails; if empty
	 * INBOX is used
	 */
	public void setRequestFolder(String RequestFolder) {
		if (RequestFolder != null && RequestFolder.length() > 20) {
			log.warning("Length > 20 - truncated");
			RequestFolder = RequestFolder.substring(0, 19);
		}
		set_Value("RequestFolder", RequestFolder);
	}

	/**
	 * Get Request Folder. EMail folder to process incoming emails; if empty
	 * INBOX is used
	 */
	public String getRequestFolder() {
		return (String) get_Value("RequestFolder");
	}

	/**
	 * Set Request User. User Name (ID) of the email owner
	 */
	public void setRequestUser(String RequestUser) {
		if (RequestUser != null && RequestUser.length() > 60) {
			log.warning("Length > 60 - truncated");
			RequestUser = RequestUser.substring(0, 59);
		}
		set_Value("RequestUser", RequestUser);
	}

	/**
	 * Get Request User. User Name (ID) of the email owner
	 */
	public String getRequestUser() {
		return (String) get_Value("RequestUser");
	}

	/**
	 * Set Request User Password. Password of the user name (ID) for mail
	 * processing
	 */
	public void setRequestUserPW(String RequestUserPW) {
		if (RequestUserPW != null && RequestUserPW.length() > 20) {
			log.warning("Length > 20 - truncated");
			RequestUserPW = RequestUserPW.substring(0, 19);
		}
		set_Value("RequestUserPW", RequestUserPW);
	}

	/**
	 * Get Request User Password. Password of the user name (ID) for mail
	 * processing
	 */
	public String getRequestUserPW() {
		return (String) get_Value("RequestUserPW");
	}

	/**
	 * Set Mail Host. Hostname of Mail Server for SMTP and IMAP
	 */
	public void setSMTPHost(String SMTPHost) {
		if (SMTPHost != null && SMTPHost.length() > 60) {
			log.warning("Length > 60 - truncated");
			SMTPHost = SMTPHost.substring(0, 59);
		}
		set_Value("SMTPHost", SMTPHost);
	}

	/**
	 * Get Mail Host. Hostname of Mail Server for SMTP and IMAP
	 */
	public String getSMTPHost() {
		return (String) get_Value("SMTPHost");
	}

	/**
	 * Set Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public void setValue(String Value) {
		if (Value == null)
			throw new IllegalArgumentException("Value is mandatory.");
		if (Value.length() > 40) {
			log.warning("Length > 40 - truncated");
			Value = Value.substring(0, 39);
		}
		set_Value("Value", Value);
	}

	/**
	 * Get Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public String getValue() {
		return (String) get_Value("Value");
	}
}
