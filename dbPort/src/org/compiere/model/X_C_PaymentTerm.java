/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_PaymentTerm
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.156
 */
public class X_C_PaymentTerm extends PO {
	/** Standard Constructor */
	public X_C_PaymentTerm(Properties ctx, int C_PaymentTerm_ID, String trxName) {
		super(ctx, C_PaymentTerm_ID, trxName);
		/**
		 * if (C_PaymentTerm_ID == 0) { setAfterDelivery (false);
		 * setC_PaymentTerm_ID (0); setDiscount (Env.ZERO); setDiscount2
		 * (Env.ZERO); setDiscountDays (0); setDiscountDays2 (0); setGraceDays
		 * (0); setIsDueFixed (false); setIsValid (false); setName (null);
		 * setNetDays (0); setValue (null); }
		 */
	}

	/** Load Constructor */
	public X_C_PaymentTerm(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_PaymentTerm */
	public static final String Table_Name = "C_PaymentTerm";

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
		StringBuffer sb = new StringBuffer("X_C_PaymentTerm[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set After Delivery. Due after delivery rather than after invoicing
	 */
	public void setAfterDelivery(boolean AfterDelivery) {
		set_Value("AfterDelivery", new Boolean(AfterDelivery));
	}

	/**
	 * Get After Delivery. Due after delivery rather than after invoicing
	 */
	public boolean isAfterDelivery() {
		Object oo = get_Value("AfterDelivery");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
	 * Set Discount 2 %. Discount in percent
	 */
	public void setDiscount2(BigDecimal Discount2) {
		if (Discount2 == null)
			throw new IllegalArgumentException("Discount2 is mandatory.");
		set_Value("Discount2", Discount2);
	}

	/**
	 * Get Discount 2 %. Discount in percent
	 */
	public BigDecimal getDiscount2() {
		BigDecimal bd = (BigDecimal) get_Value("Discount2");
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
	 * Set Discount Days 2. Number of days from invoice date to be eligible for
	 * discount
	 */
	public void setDiscountDays2(int DiscountDays2) {
		set_Value("DiscountDays2", new Integer(DiscountDays2));
	}

	/**
	 * Get Discount Days 2. Number of days from invoice date to be eligible for
	 * discount
	 */
	public int getDiscountDays2() {
		Integer ii = (Integer) get_Value("DiscountDays2");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Document Note. Additional information for a Document
	 */
	public void setDocumentNote(String DocumentNote) {
		if (DocumentNote != null && DocumentNote.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			DocumentNote = DocumentNote.substring(0, 1999);
		}
		set_Value("DocumentNote", DocumentNote);
	}

	/**
	 * Get Document Note. Additional information for a Document
	 */
	public String getDocumentNote() {
		return (String) get_Value("DocumentNote");
	}

	/**
	 * Set Fix month cutoff. Last day to include for next due date
	 */
	public void setFixMonthCutoff(int FixMonthCutoff) {
		set_Value("FixMonthCutoff", new Integer(FixMonthCutoff));
	}

	/**
	 * Get Fix month cutoff. Last day to include for next due date
	 */
	public int getFixMonthCutoff() {
		Integer ii = (Integer) get_Value("FixMonthCutoff");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Fix month day. Day of the month of the due date
	 */
	public void setFixMonthDay(int FixMonthDay) {
		set_Value("FixMonthDay", new Integer(FixMonthDay));
	}

	/**
	 * Get Fix month day. Day of the month of the due date
	 */
	public int getFixMonthDay() {
		Integer ii = (Integer) get_Value("FixMonthDay");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Fix month offset. Number of months (0=same, 1=following)
	 */
	public void setFixMonthOffset(int FixMonthOffset) {
		set_Value("FixMonthOffset", new Integer(FixMonthOffset));
	}

	/**
	 * Get Fix month offset. Number of months (0=same, 1=following)
	 */
	public int getFixMonthOffset() {
		Integer ii = (Integer) get_Value("FixMonthOffset");
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
	 * Set Default. Default value
	 */
	public void setIsDefault(boolean IsDefault) {
		set_Value("IsDefault", new Boolean(IsDefault));
	}

	/**
	 * Get Default. Default value
	 */
	public boolean isDefault() {
		Object oo = get_Value("IsDefault");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Fixed due date. Payment is due on a fixed date
	 */
	public void setIsDueFixed(boolean IsDueFixed) {
		set_Value("IsDueFixed", new Boolean(IsDueFixed));
	}

	/**
	 * Get Fixed due date. Payment is due on a fixed date
	 */
	public boolean isDueFixed() {
		Object oo = get_Value("IsDueFixed");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Next Business Day. Payment due on the next business day
	 */
	public void setIsNextBusinessDay(boolean IsNextBusinessDay) {
		set_Value("IsNextBusinessDay", new Boolean(IsNextBusinessDay));
	}

	/**
	 * Get Next Business Day. Payment due on the next business day
	 */
	public boolean isNextBusinessDay() {
		Object oo = get_Value("IsNextBusinessDay");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
}
