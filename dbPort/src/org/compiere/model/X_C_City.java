/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_City
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.093
 */
public class X_C_City extends PO {
	/** Standard Constructor */
	public X_C_City(Properties ctx, int C_City_ID, String trxName) {
		super(ctx, C_City_ID, trxName);
		/**
		 * if (C_City_ID == 0) { setC_City_ID (0); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_C_City(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_City */
	public static final String Table_Name = "C_City";

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

	protected BigDecimal accessLevel = new BigDecimal(6);

	/** AccessLevel 6 - System - Client */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_C_City[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Area Code. Phone Area Code
	 */
	public void setAreaCode(String AreaCode) {
		if (AreaCode != null && AreaCode.length() > 10) {
			log.warning("Length > 10 - truncated");
			AreaCode = AreaCode.substring(0, 9);
		}
		set_Value("AreaCode", AreaCode);
	}

	/**
	 * Get Area Code. Phone Area Code
	 */
	public String getAreaCode() {
		return (String) get_Value("AreaCode");
	}

	/**
	 * Set City. City
	 */
	public void setC_City_ID(int C_City_ID) {
		if (C_City_ID < 1)
			throw new IllegalArgumentException("C_City_ID is mandatory.");
		set_ValueNoCheck("C_City_ID", new Integer(C_City_ID));
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
		if (C_Country_ID <= 0)
			set_ValueNoCheck("C_Country_ID", null);
		else
			set_ValueNoCheck("C_Country_ID", new Integer(C_Country_ID));
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

	/** C_Region_ID AD_Reference_ID=157 */
	public static final int C_REGION_ID_AD_Reference_ID = 157;

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
	 * Set Coordinates. Location coordinate
	 */
	public void setCoordinates(String Coordinates) {
		if (Coordinates != null && Coordinates.length() > 15) {
			log.warning("Length > 15 - truncated");
			Coordinates = Coordinates.substring(0, 14);
		}
		set_Value("Coordinates", Coordinates);
	}

	/**
	 * Get Coordinates. Location coordinate
	 */
	public String getCoordinates() {
		return (String) get_Value("Coordinates");
	}

	/**
	 * Set Locode. Location code - UN/LOCODE
	 */
	public void setLocode(String Locode) {
		if (Locode != null && Locode.length() > 10) {
			log.warning("Length > 10 - truncated");
			Locode = Locode.substring(0, 9);
		}
		set_Value("Locode", Locode);
	}

	/**
	 * Get Locode. Location code - UN/LOCODE
	 */
	public String getLocode() {
		return (String) get_Value("Locode");
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
}
