/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Message
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.609
 */
public class X_AD_Message extends PO {
	/** Standard Constructor */
	public X_AD_Message(Properties ctx, int AD_Message_ID, String trxName) {
		super(ctx, AD_Message_ID, trxName);
		/**
		 * if (AD_Message_ID == 0) { setAD_Message_ID (0); setEntityType (null); //
		 * U setMsgText (null); setMsgType (null); // I setValue (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_Message(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Message */
	public static final String Table_Name = "AD_Message";

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

	protected BigDecimal accessLevel = new BigDecimal(4);

	/** AccessLevel 4 - System */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_AD_Message[").append(get_ID())
				.append("]");
		return sb.toString();
	}

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

	/** EntityType AD_Reference_ID=245 */
	public static final int ENTITYTYPE_AD_Reference_ID = 245;

	/** Applications = A */
	public static final String ENTITYTYPE_Applications = "A";

	/** Compiere = C */
	public static final String ENTITYTYPE_Compiere = "C";

	/** Customization = CUST */
	public static final String ENTITYTYPE_Customization = "CUST";

	/** Dictionary = D */
	public static final String ENTITYTYPE_Dictionary = "D";

	/** User maintained = U */
	public static final String ENTITYTYPE_UserMaintained = "U";

	/**
	 * Set Entity Type. Dictionary Entity Type; Determines ownership and
	 * synchronization
	 */
	public void setEntityType(String EntityType) {
		if (EntityType == null)
			throw new IllegalArgumentException("EntityType is mandatory");
		if (EntityType.length() > 4) {
			log.warning("Length > 4 - truncated");
			EntityType = EntityType.substring(0, 3);
		}
		set_Value("EntityType", EntityType);
	}

	/**
	 * Get Entity Type. Dictionary Entity Type; Determines ownership and
	 * synchronization
	 */
	public String getEntityType() {
		return (String) get_Value("EntityType");
	}

	/**
	 * Set Message Text. Textual Informational, Menu or Error Message
	 */
	public void setMsgText(String MsgText) {
		if (MsgText == null)
			throw new IllegalArgumentException("MsgText is mandatory.");
		if (MsgText.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			MsgText = MsgText.substring(0, 1999);
		}
		set_Value("MsgText", MsgText);
	}

	/**
	 * Get Message Text. Textual Informational, Menu or Error Message
	 */
	public String getMsgText() {
		return (String) get_Value("MsgText");
	}

	/**
	 * Set Message Tip. Additional tip or help for this message
	 */
	public void setMsgTip(String MsgTip) {
		if (MsgTip != null && MsgTip.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			MsgTip = MsgTip.substring(0, 1999);
		}
		set_Value("MsgTip", MsgTip);
	}

	/**
	 * Get Message Tip. Additional tip or help for this message
	 */
	public String getMsgTip() {
		return (String) get_Value("MsgTip");
	}

	/** MsgType AD_Reference_ID=103 */
	public static final int MSGTYPE_AD_Reference_ID = 103;

	/** Error = E */
	public static final String MSGTYPE_Error = "E";

	/** Information = I */
	public static final String MSGTYPE_Information = "I";

	/** Menu = M */
	public static final String MSGTYPE_Menu = "M";

	/**
	 * Set Message Type. Type of message (Informational, Menu or Error)
	 */
	public void setMsgType(String MsgType) {
		if (MsgType == null)
			throw new IllegalArgumentException("MsgType is mandatory");
		if (MsgType.equals("E") || MsgType.equals("I") || MsgType.equals("M"))
			;
		else
			throw new IllegalArgumentException("MsgType Invalid value - "
					+ MsgType + " - Reference_ID=103 - E - I - M");
		if (MsgType.length() > 1) {
			log.warning("Length > 1 - truncated");
			MsgType = MsgType.substring(0, 0);
		}
		set_Value("MsgType", MsgType);
	}

	/**
	 * Get Message Type. Type of message (Informational, Menu or Error)
	 */
	public String getMsgType() {
		return (String) get_Value("MsgType");
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getValue());
	}
}
