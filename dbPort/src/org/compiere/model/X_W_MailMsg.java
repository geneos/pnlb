/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for W_MailMsg
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:05.39
 */
public class X_W_MailMsg extends PO {
	/** Standard Constructor */
	public X_W_MailMsg(Properties ctx, int W_MailMsg_ID, String trxName) {
		super(ctx, W_MailMsg_ID, trxName);
		/**
		 * if (W_MailMsg_ID == 0) { setMailMsgType (null); setMessage (null);
		 * setName (null); setSubject (null); setW_MailMsg_ID (0); setW_Store_ID
		 * (0); }
		 */
	}

	/** Load Constructor */
	public X_W_MailMsg(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=W_MailMsg */
	public static final String Table_Name = "W_MailMsg";

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

	protected BigDecimal accessLevel = new BigDecimal(2);

	/** AccessLevel 2 - Client */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_W_MailMsg[").append(get_ID())
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

	/** MailMsgType AD_Reference_ID=342 */
	public static final int MAILMSGTYPE_AD_Reference_ID = 342;

	/** Subscribe = LS */
	public static final String MAILMSGTYPE_Subscribe = "LS";

	/** UnSubscribe = LU */
	public static final String MAILMSGTYPE_UnSubscribe = "LU";

	/** Order Acknowledgement = OA */
	public static final String MAILMSGTYPE_OrderAcknowledgement = "OA";

	/** Payment Acknowledgement = PA */
	public static final String MAILMSGTYPE_PaymentAcknowledgement = "PA";

	/** Payment Error = PE */
	public static final String MAILMSGTYPE_PaymentError = "PE";

	/** User Account = UA */
	public static final String MAILMSGTYPE_UserAccount = "UA";

	/** User Password = UP */
	public static final String MAILMSGTYPE_UserPassword = "UP";

	/** User Validation = UV */
	public static final String MAILMSGTYPE_UserValidation = "UV";

	/** Request = WR */
	public static final String MAILMSGTYPE_Request = "WR";

	/**
	 * Set Message Type. Mail Message Type
	 */
	public void setMailMsgType(String MailMsgType) {
		if (MailMsgType == null)
			throw new IllegalArgumentException("MailMsgType is mandatory");
		if (MailMsgType.equals("LS") || MailMsgType.equals("LU")
				|| MailMsgType.equals("OA") || MailMsgType.equals("PA")
				|| MailMsgType.equals("PE") || MailMsgType.equals("UA")
				|| MailMsgType.equals("UP") || MailMsgType.equals("UV")
				|| MailMsgType.equals("WR"))
			;
		else
			throw new IllegalArgumentException(
					"MailMsgType Invalid value - "
							+ MailMsgType
							+ " - Reference_ID=342 - LS - LU - OA - PA - PE - UA - UP - UV - WR");
		if (MailMsgType.length() > 2) {
			log.warning("Length > 2 - truncated");
			MailMsgType = MailMsgType.substring(0, 1);
		}
		set_Value("MailMsgType", MailMsgType);
	}

	/**
	 * Get Message Type. Mail Message Type
	 */
	public String getMailMsgType() {
		return (String) get_Value("MailMsgType");
	}

	/**
	 * Set Message. EMail Message
	 */
	public void setMessage(String Message) {
		if (Message == null)
			throw new IllegalArgumentException("Message is mandatory.");
		if (Message.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Message = Message.substring(0, 1999);
		}
		set_Value("Message", Message);
	}

	/**
	 * Get Message. EMail Message
	 */
	public String getMessage() {
		return (String) get_Value("Message");
	}

	/**
	 * Set Message 2. Optional second part of the EMail Message
	 */
	public void setMessage2(String Message2) {
		if (Message2 != null && Message2.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Message2 = Message2.substring(0, 1999);
		}
		set_Value("Message2", Message2);
	}

	/**
	 * Get Message 2. Optional second part of the EMail Message
	 */
	public String getMessage2() {
		return (String) get_Value("Message2");
	}

	/**
	 * Set Message 3. Optional third part of the EMail Message
	 */
	public void setMessage3(String Message3) {
		if (Message3 != null && Message3.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Message3 = Message3.substring(0, 1999);
		}
		set_Value("Message3", Message3);
	}

	/**
	 * Get Message 3. Optional third part of the EMail Message
	 */
	public String getMessage3() {
		return (String) get_Value("Message3");
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
	 * Set Subject. Email Message Subject
	 */
	public void setSubject(String Subject) {
		if (Subject == null)
			throw new IllegalArgumentException("Subject is mandatory.");
		if (Subject.length() > 255) {
			log.warning("Length > 255 - truncated");
			Subject = Subject.substring(0, 254);
		}
		set_Value("Subject", Subject);
	}

	/**
	 * Get Subject. Email Message Subject
	 */
	public String getSubject() {
		return (String) get_Value("Subject");
	}

	/**
	 * Set Mail Message. Web Store Mail Message Template
	 */
	public void setW_MailMsg_ID(int W_MailMsg_ID) {
		if (W_MailMsg_ID < 1)
			throw new IllegalArgumentException("W_MailMsg_ID is mandatory.");
		set_ValueNoCheck("W_MailMsg_ID", new Integer(W_MailMsg_ID));
	}

	/**
	 * Get Mail Message. Web Store Mail Message Template
	 */
	public int getW_MailMsg_ID() {
		Integer ii = (Integer) get_Value("W_MailMsg_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Web Store. A Web Store of the Client
	 */
	public void setW_Store_ID(int W_Store_ID) {
		if (W_Store_ID < 1)
			throw new IllegalArgumentException("W_Store_ID is mandatory.");
		set_Value("W_Store_ID", new Integer(W_Store_ID));
	}

	/**
	 * Get Web Store. A Web Store of the Client
	 */
	public int getW_Store_ID() {
		Integer ii = (Integer) get_Value("W_Store_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
