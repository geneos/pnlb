/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_AccessLog
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.015
 */
public class X_AD_AccessLog extends PO {
	/** Standard Constructor */
	public X_AD_AccessLog(Properties ctx, int AD_AccessLog_ID, String trxName) {
		super(ctx, AD_AccessLog_ID, trxName);
		/**
		 * if (AD_AccessLog_ID == 0) { setAD_AccessLog_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_AD_AccessLog(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_AccessLog */
	public static final String Table_Name = "AD_AccessLog";

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
		StringBuffer sb = new StringBuffer("X_AD_AccessLog[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Access Log. Log of Access to the System
	 */
	public void setAD_AccessLog_ID(int AD_AccessLog_ID) {
		if (AD_AccessLog_ID < 1)
			throw new IllegalArgumentException("AD_AccessLog_ID is mandatory.");
		set_ValueNoCheck("AD_AccessLog_ID", new Integer(AD_AccessLog_ID));
	}

	/**
	 * Get Access Log. Log of Access to the System
	 */
	public int getAD_AccessLog_ID() {
		Integer ii = (Integer) get_Value("AD_AccessLog_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getAD_AccessLog_ID()));
	}

	/**
	 * Set Column. Column in the table
	 */
	public void setAD_Column_ID(int AD_Column_ID) {
		if (AD_Column_ID <= 0)
			set_Value("AD_Column_ID", null);
		else
			set_Value("AD_Column_ID", new Integer(AD_Column_ID));
	}

	/**
	 * Get Column. Column in the table
	 */
	public int getAD_Column_ID() {
		Integer ii = (Integer) get_Value("AD_Column_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Table. Table for the Fields
	 */
	public void setAD_Table_ID(int AD_Table_ID) {
		if (AD_Table_ID <= 0)
			set_Value("AD_Table_ID", null);
		else
			set_Value("AD_Table_ID", new Integer(AD_Table_ID));
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
	 * Set Record ID. Direct internal record ID
	 */
	public void setRecord_ID(int Record_ID) {
		if (Record_ID <= 0)
			set_Value("Record_ID", null);
		else
			set_Value("Record_ID", new Integer(Record_ID));
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
	 * Set Remote Addr. Remote Address
	 */
	public void setRemote_Addr(String Remote_Addr) {
		if (Remote_Addr != null && Remote_Addr.length() > 60) {
			log.warning("Length > 60 - truncated");
			Remote_Addr = Remote_Addr.substring(0, 59);
		}
		set_Value("Remote_Addr", Remote_Addr);
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
		if (Remote_Host != null && Remote_Host.length() > 60) {
			log.warning("Length > 60 - truncated");
			Remote_Host = Remote_Host.substring(0, 59);
		}
		set_Value("Remote_Host", Remote_Host);
	}

	/**
	 * Get Remote Host. Remote host Info
	 */
	public String getRemote_Host() {
		return (String) get_Value("Remote_Host");
	}

	/**
	 * Set Reply. Reply or Answer
	 */
	public void setReply(String Reply) {
		if (Reply != null && Reply.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Reply = Reply.substring(0, 1999);
		}
		set_Value("Reply", Reply);
	}

	/**
	 * Get Reply. Reply or Answer
	 */
	public String getReply() {
		return (String) get_Value("Reply");
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
