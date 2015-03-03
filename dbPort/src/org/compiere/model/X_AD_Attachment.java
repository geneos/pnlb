/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Attachment
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.125
 */
public class X_AD_Attachment extends PO {
	/** Standard Constructor */
	public X_AD_Attachment(Properties ctx, int AD_Attachment_ID, String trxName) {
		super(ctx, AD_Attachment_ID, trxName);
		/**
		 * if (AD_Attachment_ID == 0) { setAD_Attachment_ID (0); setAD_Table_ID
		 * (0); setRecord_ID (0); setTitle (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_Attachment(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Attachment */
	public static final String Table_Name = "AD_Attachment";

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
		StringBuffer sb = new StringBuffer("X_AD_Attachment[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Attachment. Attachment for the document
	 */
	public void setAD_Attachment_ID(int AD_Attachment_ID) {
		if (AD_Attachment_ID < 1)
			throw new IllegalArgumentException("AD_Attachment_ID is mandatory.");
		set_ValueNoCheck("AD_Attachment_ID", new Integer(AD_Attachment_ID));
	}

	/**
	 * Get Attachment. Attachment for the document
	 */
	public int getAD_Attachment_ID() {
		Integer ii = (Integer) get_Value("AD_Attachment_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Table. Table for the Fields
	 */
	public void setAD_Table_ID(int AD_Table_ID) {
		if (AD_Table_ID < 1)
			throw new IllegalArgumentException("AD_Table_ID is mandatory.");
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
	 * Set BinaryData. Binary Data
	 */
	public void setBinaryData(byte[] BinaryData) {
		set_ValueNoCheck("BinaryData", BinaryData);
	}

	/**
	 * Get BinaryData. Binary Data
	 */
	public byte[] getBinaryData() {
		return (byte[]) get_Value("BinaryData");
	}

	/**
	 * Set Record ID. Direct internal record ID
	 */
	public void setRecord_ID(int Record_ID) {
		if (Record_ID < 0)
			throw new IllegalArgumentException("Record_ID is mandatory.");
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

	/**
	 * Set Title. Name this entity is referred to as
	 */
	public void setTitle(String Title) {
		if (Title == null)
			throw new IllegalArgumentException("Title is mandatory.");
		if (Title.length() > 60) {
			log.warning("Length > 60 - truncated");
			Title = Title.substring(0, 59);
		}
		set_Value("Title", Title);
	}

	/**
	 * Get Title. Name this entity is referred to as
	 */
	public String getTitle() {
		return (String) get_Value("Title");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getTitle());
	}
}
