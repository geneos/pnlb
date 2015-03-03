/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for B_SellerFunds
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.328
 */
public class X_B_SellerFunds extends PO {
	/** Standard Constructor */
	public X_B_SellerFunds(Properties ctx, int B_SellerFunds_ID, String trxName) {
		super(ctx, B_SellerFunds_ID, trxName);
		/**
		 * if (B_SellerFunds_ID == 0) { setAD_User_ID (0); setB_SellerFunds_ID
		 * (0); setCommittedAmt (Env.ZERO); setNonCommittedAmt (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_B_SellerFunds(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=B_SellerFunds */
	public static final String Table_Name = "B_SellerFunds";

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
		StringBuffer sb = new StringBuffer("X_B_SellerFunds[").append(get_ID())
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getAD_User_ID()));
	}

	/**
	 * Set Seller Funds. Seller Funds from Offers on Topics
	 */
	public void setB_SellerFunds_ID(int B_SellerFunds_ID) {
		if (B_SellerFunds_ID < 1)
			throw new IllegalArgumentException("B_SellerFunds_ID is mandatory.");
		set_ValueNoCheck("B_SellerFunds_ID", new Integer(B_SellerFunds_ID));
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
	 * Set Order. Order
	 */
	public void setC_Order_ID(int C_Order_ID) {
		if (C_Order_ID <= 0)
			set_Value("C_Order_ID", null);
		else
			set_Value("C_Order_ID", new Integer(C_Order_ID));
	}

	/**
	 * Get Order. Order
	 */
	public int getC_Order_ID() {
		Integer ii = (Integer) get_Value("C_Order_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Payment. Payment identifier
	 */
	public void setC_Payment_ID(int C_Payment_ID) {
		if (C_Payment_ID <= 0)
			set_Value("C_Payment_ID", null);
		else
			set_Value("C_Payment_ID", new Integer(C_Payment_ID));
	}

	/**
	 * Get Payment. Payment identifier
	 */
	public int getC_Payment_ID() {
		Integer ii = (Integer) get_Value("C_Payment_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Committed Amount. The (legal) commitment amount
	 */
	public void setCommittedAmt(BigDecimal CommittedAmt) {
		if (CommittedAmt == null)
			throw new IllegalArgumentException("CommittedAmt is mandatory.");
		set_Value("CommittedAmt", CommittedAmt);
	}

	/**
	 * Get Committed Amount. The (legal) commitment amount
	 */
	public BigDecimal getCommittedAmt() {
		BigDecimal bd = (BigDecimal) get_Value("CommittedAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Not Committed Aount. Amount not committed yet
	 */
	public void setNonCommittedAmt(BigDecimal NonCommittedAmt) {
		if (NonCommittedAmt == null)
			throw new IllegalArgumentException("NonCommittedAmt is mandatory.");
		set_Value("NonCommittedAmt", NonCommittedAmt);
	}

	/**
	 * Get Not Committed Aount. Amount not committed yet
	 */
	public BigDecimal getNonCommittedAmt() {
		BigDecimal bd = (BigDecimal) get_Value("NonCommittedAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
