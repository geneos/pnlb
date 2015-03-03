/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for B_BidComment
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.281
 */
public class X_B_BidComment extends PO {
	/** Standard Constructor */
	public X_B_BidComment(Properties ctx, int B_BidComment_ID, String trxName) {
		super(ctx, B_BidComment_ID, trxName);
		/**
		 * if (B_BidComment_ID == 0) { setAD_User_ID (0); setB_BidComment_ID
		 * (0); setB_Topic_ID (0); setTextMsg (null); }
		 */
	}

	/** Load Constructor */
	public X_B_BidComment(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=B_BidComment */
	public static final String Table_Name = "B_BidComment";

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

	protected BigDecimal accessLevel = new BigDecimal(3);

	/** AccessLevel 3 - Client - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_B_BidComment[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public void setAD_User_ID(int AD_User_ID) {
		if (AD_User_ID < 1)
			throw new IllegalArgumentException("AD_User_ID is mandatory.");
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
	 * Set Bid Comment. Make a comment to a Bid Topic
	 */
	public void setB_BidComment_ID(int B_BidComment_ID) {
		if (B_BidComment_ID < 1)
			throw new IllegalArgumentException("B_BidComment_ID is mandatory.");
		set_ValueNoCheck("B_BidComment_ID", new Integer(B_BidComment_ID));
	}

	/**
	 * Get Bid Comment. Make a comment to a Bid Topic
	 */
	public int getB_BidComment_ID() {
		Integer ii = (Integer) get_Value("B_BidComment_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Topic. Auction Topic
	 */
	public void setB_Topic_ID(int B_Topic_ID) {
		if (B_Topic_ID < 1)
			throw new IllegalArgumentException("B_Topic_ID is mandatory.");
		set_Value("B_Topic_ID", new Integer(B_Topic_ID));
	}

	/**
	 * Get Topic. Auction Topic
	 */
	public int getB_Topic_ID() {
		Integer ii = (Integer) get_Value("B_Topic_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Text Message. Text Message
	 */
	public void setTextMsg(String TextMsg) {
		if (TextMsg == null)
			throw new IllegalArgumentException("TextMsg is mandatory.");
		if (TextMsg.length() > 2000) {
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
