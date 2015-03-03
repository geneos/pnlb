/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Bank
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.843
 */
public class X_C_Bank extends PO {
	/** Standard Constructor */
	public X_C_Bank(Properties ctx, int C_Bank_ID, String trxName) {
		super(ctx, C_Bank_ID, trxName);
		/**
		 * if (C_Bank_ID == 0) { setC_Bank_ID (0); setIsOwnBank (true); // Y
		 * setName (null); setRoutingNo (null); }
		 */
	}

	/** Load Constructor */
	public X_C_Bank(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Bank */
	public static final String Table_Name = "C_Bank";

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
		StringBuffer sb = new StringBuffer("X_C_Bank[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Bank. Bank
	 */
	public void setC_Bank_ID(int C_Bank_ID) {
		if (C_Bank_ID < 1)
			throw new IllegalArgumentException("C_Bank_ID is mandatory.");
		set_ValueNoCheck("C_Bank_ID", new Integer(C_Bank_ID));
	}

	/**
	 * Get Bank. Bank
	 */
	public int getC_Bank_ID() {
		Integer ii = (Integer) get_Value("C_Bank_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Address. Location or Address
	 */
	public void setC_Location_ID(int C_Location_ID) {
		if (C_Location_ID <= 0)
			set_Value("C_Location_ID", null);
		else
			set_Value("C_Location_ID", new Integer(C_Location_ID));
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
	 * Set Own Bank. Bank for this Organization
	 */
	public void setIsOwnBank(boolean IsOwnBank) {
		set_Value("IsOwnBank", new Boolean(IsOwnBank));
	}

	/**
	 * Get Own Bank. Bank for this Organization
	 */
	public boolean isOwnBank() {
		Object oo = get_Value("IsOwnBank");
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
	 * Set Routing No. Bank Routing Number
	 */
	public void setRoutingNo(String RoutingNo) {
		if (RoutingNo == null)
			throw new IllegalArgumentException("RoutingNo is mandatory.");
		if (RoutingNo.length() > 20) {
			log.warning("Length > 20 - truncated");
			RoutingNo = RoutingNo.substring(0, 19);
		}
		set_Value("RoutingNo", RoutingNo);
	}

	/**
	 * Get Routing No. Bank Routing Number
	 */
	public String getRoutingNo() {
		return (String) get_Value("RoutingNo");
	}

	/**
	 * Set Swift code. Swift Code or BIC
	 */
	public void setSwiftCode(String SwiftCode) {
		if (SwiftCode != null && SwiftCode.length() > 20) {
			log.warning("Length > 20 - truncated");
			SwiftCode = SwiftCode.substring(0, 19);
		}
		set_Value("SwiftCode", SwiftCode);
	}

	/**
	 * Get Swift code. Swift Code or BIC
	 */
	public String getSwiftCode() {
		return (String) get_Value("SwiftCode");
	}
}
