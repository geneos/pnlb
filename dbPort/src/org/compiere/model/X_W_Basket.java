/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for W_Basket
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:05.312
 */
public class X_W_Basket extends PO {
	/** Standard Constructor */
	public X_W_Basket(Properties ctx, int W_Basket_ID, String trxName) {
		super(ctx, W_Basket_ID, trxName);
		/**
		 * if (W_Basket_ID == 0) { setAD_User_ID (0); setSession_ID (0);
		 * setW_Basket_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_W_Basket(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=W_Basket */
	public static final String Table_Name = "W_Basket";

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
		StringBuffer sb = new StringBuffer("X_W_Basket[").append(get_ID())
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
	 * Set Price List. Unique identifier of a Price List
	 */
	public void setM_PriceList_ID(int M_PriceList_ID) {
		if (M_PriceList_ID <= 0)
			set_Value("M_PriceList_ID", null);
		else
			set_Value("M_PriceList_ID", new Integer(M_PriceList_ID));
	}

	/**
	 * Get Price List. Unique identifier of a Price List
	 */
	public int getM_PriceList_ID() {
		Integer ii = (Integer) get_Value("M_PriceList_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Session ID */
	public void setSession_ID(int Session_ID) {
		if (Session_ID < 1)
			throw new IllegalArgumentException("Session_ID is mandatory.");
		set_Value("Session_ID", new Integer(Session_ID));
	}

	/** Get Session ID */
	public int getSession_ID() {
		Integer ii = (Integer) get_Value("Session_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getSession_ID()));
	}

	/**
	 * Set W_Basket_ID. Web Basket
	 */
	public void setW_Basket_ID(int W_Basket_ID) {
		if (W_Basket_ID < 1)
			throw new IllegalArgumentException("W_Basket_ID is mandatory.");
		set_ValueNoCheck("W_Basket_ID", new Integer(W_Basket_ID));
	}

	/**
	 * Get W_Basket_ID. Web Basket
	 */
	public int getW_Basket_ID() {
		Integer ii = (Integer) get_Value("W_Basket_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
