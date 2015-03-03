/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_PaySchedule
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.937
 */
public class X_C_PaySchedule extends PO {
	/** Standard Constructor */
	public X_C_PaySchedule(Properties ctx, int C_PaySchedule_ID, String trxName) {
		super(ctx, C_PaySchedule_ID, trxName);
		/**
		 * if (C_PaySchedule_ID == 0) { setC_PaySchedule_ID (0);
		 * setC_PaymentTerm_ID (0); setDiscount (Env.ZERO); setDiscountDays (0);
		 * setGraceDays (0); setIsValid (false); setNetDays (0); setPercentage
		 * (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_C_PaySchedule(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_PaySchedule */
	public static final String Table_Name = "C_PaySchedule";

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
		StringBuffer sb = new StringBuffer("X_C_PaySchedule[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Payment Schedule. Payment Schedule Template
	 */
	public void setC_PaySchedule_ID(int C_PaySchedule_ID) {
		if (C_PaySchedule_ID < 1)
			throw new IllegalArgumentException("C_PaySchedule_ID is mandatory.");
		set_ValueNoCheck("C_PaySchedule_ID", new Integer(C_PaySchedule_ID));
	}

	/**
	 * Get Payment Schedule. Payment Schedule Template
	 */
	public int getC_PaySchedule_ID() {
		Integer ii = (Integer) get_Value("C_PaySchedule_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Payment Term. The terms of Payment (timing, discount)
	 */
	public void setC_PaymentTerm_ID(int C_PaymentTerm_ID) {
		if (C_PaymentTerm_ID < 1)
			throw new IllegalArgumentException("C_PaymentTerm_ID is mandatory.");
		set_ValueNoCheck("C_PaymentTerm_ID", new Integer(C_PaymentTerm_ID));
	}

	/**
	 * Get Payment Term. The terms of Payment (timing, discount)
	 */
	public int getC_PaymentTerm_ID() {
		Integer ii = (Integer) get_Value("C_PaymentTerm_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getC_PaymentTerm_ID()));
	}

	/**
	 * Set Discount %. Discount in percent
	 */
	public void setDiscount(BigDecimal Discount) {
		if (Discount == null)
			throw new IllegalArgumentException("Discount is mandatory.");
		set_Value("Discount", Discount);
	}

	/**
	 * Get Discount %. Discount in percent
	 */
	public BigDecimal getDiscount() {
		BigDecimal bd = (BigDecimal) get_Value("Discount");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Discount Days. Number of days from invoice date to be eligible for
	 * discount
	 */
	public void setDiscountDays(int DiscountDays) {
		set_Value("DiscountDays", new Integer(DiscountDays));
	}

	/**
	 * Get Discount Days. Number of days from invoice date to be eligible for
	 * discount
	 */
	public int getDiscountDays() {
		Integer ii = (Integer) get_Value("DiscountDays");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Grace Days. Days after due date to send first dunning letter
	 */
	public void setGraceDays(int GraceDays) {
		set_Value("GraceDays", new Integer(GraceDays));
	}

	/**
	 * Get Grace Days. Days after due date to send first dunning letter
	 */
	public int getGraceDays() {
		Integer ii = (Integer) get_Value("GraceDays");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Valid. Element is valid
	 */
	public void setIsValid(boolean IsValid) {
		set_Value("IsValid", new Boolean(IsValid));
	}

	/**
	 * Get Valid. Element is valid
	 */
	public boolean isValid() {
		Object oo = get_Value("IsValid");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** NetDay AD_Reference_ID=167 */
	public static final int NETDAY_AD_Reference_ID = 167;

	/** Monday = 1 */
	public static final String NETDAY_Monday = "1";

	/** Tuesday = 2 */
	public static final String NETDAY_Tuesday = "2";

	/** Wednesday = 3 */
	public static final String NETDAY_Wednesday = "3";

	/** Thursday = 4 */
	public static final String NETDAY_Thursday = "4";

	/** Friday = 5 */
	public static final String NETDAY_Friday = "5";

	/** Saturday = 6 */
	public static final String NETDAY_Saturday = "6";

	/** Sunday = 7 */
	public static final String NETDAY_Sunday = "7";

	/**
	 * Set Net Day. Day when payment is due net
	 */
	public void setNetDay(String NetDay) {
		if (NetDay != null && NetDay.length() > 1) {
			log.warning("Length > 1 - truncated");
			NetDay = NetDay.substring(0, 0);
		}
		set_Value("NetDay", NetDay);
	}

	/**
	 * Get Net Day. Day when payment is due net
	 */
	public String getNetDay() {
		return (String) get_Value("NetDay");
	}

	/**
	 * Set Net Days. Net Days in which payment is due
	 */
	public void setNetDays(int NetDays) {
		set_Value("NetDays", new Integer(NetDays));
	}

	/**
	 * Get Net Days. Net Days in which payment is due
	 */
	public int getNetDays() {
		Integer ii = (Integer) get_Value("NetDays");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Percentage. Percent of the entire amount
	 */
	public void setPercentage(BigDecimal Percentage) {
		if (Percentage == null)
			throw new IllegalArgumentException("Percentage is mandatory.");
		set_Value("Percentage", Percentage);
	}

	/**
	 * Get Percentage. Percent of the entire amount
	 */
	public BigDecimal getPercentage() {
		BigDecimal bd = (BigDecimal) get_Value("Percentage");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
