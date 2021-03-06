/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for S_ResourceType
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.75
 */
public class X_S_ResourceType extends PO {
	/** Standard Constructor */
	public X_S_ResourceType(Properties ctx, int S_ResourceType_ID,
			String trxName) {
		super(ctx, S_ResourceType_ID, trxName);
		/**
		 * if (S_ResourceType_ID == 0) { setAllowUoMFractions (false); // N
		 * setC_TaxCategory_ID (0); setC_UOM_ID (0); setIsDateSlot (false);
		 * setIsSingleAssignment (false); setIsTimeSlot (false);
		 * setM_Product_Category_ID (0); setName (null); setOnFriday (true); //
		 * Y setOnMonday (true); // Y setOnSaturday (false); setOnSunday
		 * (false); setOnThursday (true); // Y setOnTuesday (true); // Y
		 * setOnWednesday (true); // Y setS_ResourceType_ID (0); setValue
		 * (null); }
		 */
	}

	/** Load Constructor */
	public X_S_ResourceType(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=S_ResourceType */
	public static final String Table_Name = "S_ResourceType";

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
		StringBuffer sb = new StringBuffer("X_S_ResourceType[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Allow UoM Fractions. Allow Unit of Measure Fractions
	 */
	public void setAllowUoMFractions(boolean AllowUoMFractions) {
		set_Value("AllowUoMFractions", new Boolean(AllowUoMFractions));
	}

	/**
	 * Get Allow UoM Fractions. Allow Unit of Measure Fractions
	 */
	public boolean isAllowUoMFractions() {
		Object oo = get_Value("AllowUoMFractions");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Tax Category. Tax Category
	 */
	public void setC_TaxCategory_ID(int C_TaxCategory_ID) {
		if (C_TaxCategory_ID < 1)
			throw new IllegalArgumentException("C_TaxCategory_ID is mandatory.");
		set_Value("C_TaxCategory_ID", new Integer(C_TaxCategory_ID));
	}

	/**
	 * Get Tax Category. Tax Category
	 */
	public int getC_TaxCategory_ID() {
		Integer ii = (Integer) get_Value("C_TaxCategory_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set UOM. Unit of Measure
	 */
	public void setC_UOM_ID(int C_UOM_ID) {
		if (C_UOM_ID < 1)
			throw new IllegalArgumentException("C_UOM_ID is mandatory.");
		set_Value("C_UOM_ID", new Integer(C_UOM_ID));
	}

	/**
	 * Get UOM. Unit of Measure
	 */
	public int getC_UOM_ID() {
		Integer ii = (Integer) get_Value("C_UOM_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Chargeable Quantity */
	public void setChargeableQty(int ChargeableQty) {
		set_Value("ChargeableQty", new Integer(ChargeableQty));
	}

	/** Get Chargeable Quantity */
	public int getChargeableQty() {
		Integer ii = (Integer) get_Value("ChargeableQty");
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
	 * Set Day Slot. Resource has day slot availability
	 */
	public void setIsDateSlot(boolean IsDateSlot) {
		set_Value("IsDateSlot", new Boolean(IsDateSlot));
	}

	/**
	 * Get Day Slot. Resource has day slot availability
	 */
	public boolean isDateSlot() {
		Object oo = get_Value("IsDateSlot");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Single Assignment only. Only one assignment at a time (no
	 * double-booking or overlapping)
	 */
	public void setIsSingleAssignment(boolean IsSingleAssignment) {
		set_Value("IsSingleAssignment", new Boolean(IsSingleAssignment));
	}

	/**
	 * Get Single Assignment only. Only one assignment at a time (no
	 * double-booking or overlapping)
	 */
	public boolean isSingleAssignment() {
		Object oo = get_Value("IsSingleAssignment");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Time Slot. Resource has time slot availability
	 */
	public void setIsTimeSlot(boolean IsTimeSlot) {
		set_Value("IsTimeSlot", new Boolean(IsTimeSlot));
	}

	/**
	 * Get Time Slot. Resource has time slot availability
	 */
	public boolean isTimeSlot() {
		Object oo = get_Value("IsTimeSlot");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Product Category. Category of a Product
	 */
	public void setM_Product_Category_ID(int M_Product_Category_ID) {
		if (M_Product_Category_ID < 1)
			throw new IllegalArgumentException(
					"M_Product_Category_ID is mandatory.");
		set_Value("M_Product_Category_ID", new Integer(M_Product_Category_ID));
	}

	/**
	 * Get Product Category. Category of a Product
	 */
	public int getM_Product_Category_ID() {
		Integer ii = (Integer) get_Value("M_Product_Category_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
	 * Set Friday. Available on Fridays
	 */
	public void setOnFriday(boolean OnFriday) {
		set_Value("OnFriday", new Boolean(OnFriday));
	}

	/**
	 * Get Friday. Available on Fridays
	 */
	public boolean isOnFriday() {
		Object oo = get_Value("OnFriday");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Monday. Available on Mondays
	 */
	public void setOnMonday(boolean OnMonday) {
		set_Value("OnMonday", new Boolean(OnMonday));
	}

	/**
	 * Get Monday. Available on Mondays
	 */
	public boolean isOnMonday() {
		Object oo = get_Value("OnMonday");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Saturday. Available on Saturday
	 */
	public void setOnSaturday(boolean OnSaturday) {
		set_Value("OnSaturday", new Boolean(OnSaturday));
	}

	/**
	 * Get Saturday. Available on Saturday
	 */
	public boolean isOnSaturday() {
		Object oo = get_Value("OnSaturday");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Sunday. Available on Sundays
	 */
	public void setOnSunday(boolean OnSunday) {
		set_Value("OnSunday", new Boolean(OnSunday));
	}

	/**
	 * Get Sunday. Available on Sundays
	 */
	public boolean isOnSunday() {
		Object oo = get_Value("OnSunday");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Thursday. Available on Thursdays
	 */
	public void setOnThursday(boolean OnThursday) {
		set_Value("OnThursday", new Boolean(OnThursday));
	}

	/**
	 * Get Thursday. Available on Thursdays
	 */
	public boolean isOnThursday() {
		Object oo = get_Value("OnThursday");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Tuesday. Available on Tuesdays
	 */
	public void setOnTuesday(boolean OnTuesday) {
		set_Value("OnTuesday", new Boolean(OnTuesday));
	}

	/**
	 * Get Tuesday. Available on Tuesdays
	 */
	public boolean isOnTuesday() {
		Object oo = get_Value("OnTuesday");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Wednesday. Available on Wednesdays
	 */
	public void setOnWednesday(boolean OnWednesday) {
		set_Value("OnWednesday", new Boolean(OnWednesday));
	}

	/**
	 * Get Wednesday. Available on Wednesdays
	 */
	public boolean isOnWednesday() {
		Object oo = get_Value("OnWednesday");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Resource Type */
	public void setS_ResourceType_ID(int S_ResourceType_ID) {
		if (S_ResourceType_ID < 1)
			throw new IllegalArgumentException(
					"S_ResourceType_ID is mandatory.");
		set_ValueNoCheck("S_ResourceType_ID", new Integer(S_ResourceType_ID));
	}

	/** Get Resource Type */
	public int getS_ResourceType_ID() {
		Integer ii = (Integer) get_Value("S_ResourceType_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Slot End. Time when timeslot ends
	 */
	public void setTimeSlotEnd(Timestamp TimeSlotEnd) {
		set_Value("TimeSlotEnd", TimeSlotEnd);
	}

	/**
	 * Get Slot End. Time when timeslot ends
	 */
	public Timestamp getTimeSlotEnd() {
		return (Timestamp) get_Value("TimeSlotEnd");
	}

	/**
	 * Set Slot Start. Time when timeslot starts
	 */
	public void setTimeSlotStart(Timestamp TimeSlotStart) {
		set_Value("TimeSlotStart", TimeSlotStart);
	}

	/**
	 * Get Slot Start. Time when timeslot starts
	 */
	public Timestamp getTimeSlotStart() {
		return (Timestamp) get_Value("TimeSlotStart");
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
