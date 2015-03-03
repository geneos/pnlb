/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Note
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.625
 */
public class X_AD_Note extends PO {
	/** Standard Constructor */
	public X_AD_Note(Properties ctx, int AD_Note_ID, String trxName) {
		super(ctx, AD_Note_ID, trxName);
		/**
		 * if (AD_Note_ID == 0) { setAD_Message_ID (0); setAD_Note_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_AD_Note(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Note */
	public static final String Table_Name = "AD_Note";

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

	/** AD_Message_ID AD_Reference_ID=102 */
	public static final int AD_MESSAGE_ID_AD_Reference_ID = 102;

	/**
	 * Set Message. System Message
	 */
	public void setAD_Message_ID(int AD_Message_ID) {
		if (AD_Message_ID < 1)
			throw new IllegalArgumentException("AD_Message_ID is mandatory.");
		set_ValueNoCheck("AD_Message_ID", new Integer(AD_Message_ID));
	}

	/**
	 * Get Message. System Message
	 */
	public int getAD_Message_ID() {
		Integer ii = (Integer) get_Value("AD_Message_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getAD_Message_ID()));
	}

	/**
	 * Set Notice. System Notice
	 */
	public void setAD_Note_ID(int AD_Note_ID) {
		if (AD_Note_ID < 1)
			throw new IllegalArgumentException("AD_Note_ID is mandatory.");
		set_ValueNoCheck("AD_Note_ID", new Integer(AD_Note_ID));
	}

	/**
	 * Get Notice. System Notice
	 */
	public int getAD_Note_ID() {
		Integer ii = (Integer) get_Value("AD_Note_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Table. Table for the Fields
	 */
	public void setAD_Table_ID(int AD_Table_ID) {
		if (AD_Table_ID <= 0)
			set_ValueNoCheck("AD_Table_ID", null);
		else
			set_ValueNoCheck("AD_Table_ID", new Integer(AD_Table_ID));
	}

	/**
	 * Get Table. Table for the Fields
	 */
	public int getAD_Table_ID() {
		Integer ii = (Integer) get_Value("AD_Table_ID");
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
	 * Set Workflow Activity. Workflow Activity
	 */
	public void setAD_WF_Activity_ID(int AD_WF_Activity_ID) {
		if (AD_WF_Activity_ID <= 0)
			set_Value("AD_WF_Activity_ID", null);
		else
			set_Value("AD_WF_Activity_ID", new Integer(AD_WF_Activity_ID));
	}

	/**
	 * Get Workflow Activity. Workflow Activity
	 */
	public int getAD_WF_Activity_ID() {
		Integer ii = (Integer) get_Value("AD_WF_Activity_ID");
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
	 * Set Processed. The document has been processed
	 */
	public void setProcessed(boolean Processed) {
		set_Value("Processed", new Boolean(Processed));
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
	 * Set Reference. Reference for this record
	 */
	public void setReference(String Reference) {
		if (Reference != null && Reference.length() > 60) {
			log.warning("Length > 60 - truncated");
			Reference = Reference.substring(0, 59);
		}
		set_Value("Reference", Reference);
	}

	/**
	 * Get Reference. Reference for this record
	 */
	public String getReference() {
		return (String) get_Value("Reference");
	}

	/**
	 * Set Text Message. Text Message
	 */
	public void setTextMsg(String TextMsg) {
		if (TextMsg != null && TextMsg.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			TextMsg = TextMsg.substring(0, 1999);
		}
		set_Value("TextMsg", TextMsg);
	}

	/**
	 * Get Text Message. Text Message
	 */
	public String getTextMsg() {
		return (String) get_Value("TextMsg");
	}
}
