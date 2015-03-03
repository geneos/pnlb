/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for R_GroupUpdates
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.39
 */
public class X_R_GroupUpdates extends PO {
	/** Standard Constructor */
	public X_R_GroupUpdates(Properties ctx, int R_GroupUpdates_ID,
			String trxName) {
		super(ctx, R_GroupUpdates_ID, trxName);
		/**
		 * if (R_GroupUpdates_ID == 0) { setAD_User_ID (0); setIsSelfService
		 * (false); setR_Group_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_R_GroupUpdates(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=R_GroupUpdates */
	public static final String Table_Name = "R_GroupUpdates";

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
		StringBuffer sb = new StringBuffer("X_R_GroupUpdates[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public void setAD_User_ID(int AD_User_ID) {
		if (AD_User_ID < 1)
			throw new IllegalArgumentException("AD_User_ID is mandatory.");
		set_ValueNoCheck("AD_User_ID", new Integer(AD_User_ID));
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
	 * Set Self-Service. This is a Self-Service entry or this entry can be
	 * changed via Self-Service
	 */
	public void setIsSelfService(boolean IsSelfService) {
		set_Value("IsSelfService", new Boolean(IsSelfService));
	}

	/**
	 * Get Self-Service. This is a Self-Service entry or this entry can be
	 * changed via Self-Service
	 */
	public boolean isSelfService() {
		Object oo = get_Value("IsSelfService");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Group. Request Group
	 */
	public void setR_Group_ID(int R_Group_ID) {
		if (R_Group_ID < 1)
			throw new IllegalArgumentException("R_Group_ID is mandatory.");
		set_ValueNoCheck("R_Group_ID", new Integer(R_Group_ID));
	}

	/**
	 * Get Group. Request Group
	 */
	public int getR_Group_ID() {
		Integer ii = (Integer) get_Value("R_Group_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
