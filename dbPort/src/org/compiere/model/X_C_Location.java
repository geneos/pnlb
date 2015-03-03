/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Location
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.671
 */
@SuppressWarnings("serial")
public class X_C_Location extends PO {
	/** Standard Constructor */
	public X_C_Location(Properties ctx, int C_Location_ID, String trxName) {
		super(ctx, C_Location_ID, trxName);
		/**
		 * if (C_Location_ID == 0) { setC_Country_ID (0); setC_Location_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_C_Location(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Location */
	public static final String Table_Name = "C_Location";

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

	protected BigDecimal accessLevel = new BigDecimal(7);

	/** AccessLevel 7 - System - Client - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_C_Location[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	public void setC_Jurisdiccion_ID(int C_Jurisdiccion_ID) {
		set_Value("C_JURISDICCION_ID", new Integer(C_Jurisdiccion_ID));
	}

	/** Get C_Jurisdiccion_ID */
	public int getC_Jurisdiccion_ID() {
		Integer ii = (Integer) get_Value("C_JURISDICCION_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Address 1. Address line 1 for this location
	 */
	public void setAddress1(String Address1) {
		if (Address1 != null && Address1.length() > 60) {
			log.warning("Length > 60 - truncated");
			Address1 = Address1.substring(0, 59);
		}
		set_Value("Address1", Address1);
	}

	/**
	 * Get Address 1. Address line 1 for this location
	 */
	public String getAddress1() {
		return (String) get_Value("Address1");
	}

	/**
	 * Set Address 2. Address line 2 for this location
	 */
	public void setAddress2(String Address2) {
		if (Address2 != null && Address2.length() > 60) {
			log.warning("Length > 60 - truncated");
			Address2 = Address2.substring(0, 59);
		}
		set_Value("Address2", Address2);
	}

	/**
	 * Get Address 2. Address line 2 for this location
	 */
	public String getAddress2() {
		return (String) get_Value("Address2");
	}

	/**
	 * Set Address 3. Address Line 3 for the location
	 */
	public void setAddress3(String Address3) {
		if (Address3 != null && Address3.length() > 60) {
			log.warning("Length > 60 - truncated");
			Address3 = Address3.substring(0, 59);
		}
		set_Value("Address3", Address3);
	}

	/**
	 * Get Address 3. Address Line 3 for the location
	 */
	public String getAddress3() {
		return (String) get_Value("Address3");
	}

	/**
	 * Set Address 4. Address Line 4 for the location
	 */
	public void setAddress4(String Address4) {
		if (Address4 != null && Address4.length() > 60) {
			log.warning("Length > 60 - truncated");
			Address4 = Address4.substring(0, 59);
		}
		set_Value("Address4", Address4);
	}

	/**
	 * Get Address 4. Address Line 4 for the location
	 */
	public String getAddress4() {
		return (String) get_Value("Address4");
	}

	/**
	 * Set City. City
	 */
	public void setC_City_ID(int C_City_ID) {
		if (C_City_ID <= 0)
			set_Value("C_City_ID", null);
		else
			set_Value("C_City_ID", new Integer(C_City_ID));
	}

	/**
	 * Get City. City
	 */
	public int getC_City_ID() {
		Integer ii = (Integer) get_Value("C_City_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Country. Country
	 */
	public void setC_Country_ID(int C_Country_ID) {
		if (C_Country_ID < 1)
			throw new IllegalArgumentException("C_Country_ID is mandatory.");
		set_Value("C_Country_ID", new Integer(C_Country_ID));
	}

	/**
	 * Get Country. Country
	 */
	public int getC_Country_ID() {
		Integer ii = (Integer) get_Value("C_Country_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Address. Location or Address
	 */
	public void setC_Location_ID(int C_Location_ID) {
		if (C_Location_ID < 1)
			throw new IllegalArgumentException("C_Location_ID is mandatory.");
		set_ValueNoCheck("C_Location_ID", new Integer(C_Location_ID));
	}

	/**
	 * Get Address. Location or Address
	 */
	public int getC_Location_ID() {
		Integer ii = (Integer) get_Value("C_Location_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Region. Identifies a geographical Region
	 */
	public void setC_Region_ID(int C_Region_ID) {
		if (C_Region_ID <= 0)
			set_Value("C_Region_ID", null);
		else
			set_Value("C_Region_ID", new Integer(C_Region_ID));
	}

	/**
	 * Get Region. Identifies a geographical Region
	 */
	public int getC_Region_ID() {
		Integer ii = (Integer) get_Value("C_Region_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set City. Identifies a City
	 */
	public void setCity(String City) {
		if (City != null && City.length() > 60) {
			log.warning("Length > 60 - truncated");
			City = City.substring(0, 59);
		}
		set_Value("City", City);
	}

	/**
	 * Get City. Identifies a City
	 */
	public String getCity() {
		return (String) get_Value("City");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getCity());
	}

	/**
	 * Set ZIP. Postal code
	 */
	public void setPostal(String Postal) {
		if (Postal != null && Postal.length() > 10) {
			log.warning("Length > 10 - truncated");
			Postal = Postal.substring(0, 9);
		}
		set_Value("Postal", Postal);
	}

	/**
	 * Get ZIP. Postal code
	 */
	public String getPostal() {
		return (String) get_Value("Postal");
	}

	/**
	 * Set -. Additional ZIP or Postal code
	 */
	public void setPostal_Add(String Postal_Add) {
		if (Postal_Add != null && Postal_Add.length() > 10) {
			log.warning("Length > 10 - truncated");
			Postal_Add = Postal_Add.substring(0, 9);
		}
		set_Value("Postal_Add", Postal_Add);
	}

	/**
	 * Get -. Additional ZIP or Postal code
	 */
	public String getPostal_Add() {
		return (String) get_Value("Postal_Add");
	}

	/**
	 * Set Region. Name of the Region
	 */
	public void setRegionName(String RegionName) {
		if (RegionName != null && RegionName.length() > 40) {
			log.warning("Length > 40 - truncated");
			RegionName = RegionName.substring(0, 39);
		}
		set_Value("RegionName", RegionName);
	}

	/**
	 * Get Region. Name of the Region
	 */
	public String getRegionName() {
		return (String) get_Value("RegionName");
	}
}
