/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for B_Offer
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.312
 */
public class X_B_Offer extends PO {
	/** Standard Constructor */
	public X_B_Offer(Properties ctx, int B_Offer_ID, String trxName) {
		super(ctx, B_Offer_ID, trxName);
		/**
		 * if (B_Offer_ID == 0) { setAD_User_ID (0); setB_Offer_ID (0);
		 * setB_SellerFunds_ID (0); setB_Topic_ID (0); setIsWillingToCommit
		 * (false); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_B_Offer(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=B_Offer */
	public static final String Table_Name = "B_Offer";

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
		StringBuffer sb = new StringBuffer("X_B_Offer[").append(get_ID())
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
	 * Set Offer. Offer for a Topic
	 */
	public void setB_Offer_ID(int B_Offer_ID) {
		if (B_Offer_ID < 1)
			throw new IllegalArgumentException("B_Offer_ID is mandatory.");
		set_ValueNoCheck("B_Offer_ID", new Integer(B_Offer_ID));
	}

	/**
	 * Get Offer. Offer for a Topic
	 */
	public int getB_Offer_ID() {
		Integer ii = (Integer) get_Value("B_Offer_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Seller Funds. Seller Funds from Offers on Topics
	 */
	public void setB_SellerFunds_ID(int B_SellerFunds_ID) {
		if (B_SellerFunds_ID < 1)
			throw new IllegalArgumentException("B_SellerFunds_ID is mandatory.");
		set_Value("B_SellerFunds_ID", new Integer(B_SellerFunds_ID));
	}

	/**
	 * Get Seller Funds. Seller Funds from Offers on Topics
	 */
	public int getB_SellerFunds_ID() {
		Integer ii = (Integer) get_Value("B_SellerFunds_ID");
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

	/** Set Willing to commit */
	public void setIsWillingToCommit(boolean IsWillingToCommit) {
		set_Value("IsWillingToCommit", new Boolean(IsWillingToCommit));
	}

	/** Get Willing to commit */
	public boolean isWillingToCommit() {
		Object oo = get_Value("IsWillingToCommit");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
	 * Set Private Note. Private Note - not visible to the other parties
	 */
	public void setPrivateNote(String PrivateNote) {
		if (PrivateNote != null && PrivateNote.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			PrivateNote = PrivateNote.substring(0, 1999);
		}
		set_Value("PrivateNote", PrivateNote);
	}

	/**
	 * Get Private Note. Private Note - not visible to the other parties
	 */
	public String getPrivateNote() {
		return (String) get_Value("PrivateNote");
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
