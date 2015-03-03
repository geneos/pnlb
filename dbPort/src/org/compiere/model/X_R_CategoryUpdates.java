/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for R_CategoryUpdates
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.359
 */
public class X_R_CategoryUpdates extends PO {
	/** Standard Constructor */
	public X_R_CategoryUpdates(Properties ctx, int R_CategoryUpdates_ID,
			String trxName) {
		super(ctx, R_CategoryUpdates_ID, trxName);
		/**
		 * if (R_CategoryUpdates_ID == 0) { setAD_User_ID (0); setIsSelfService
		 * (false); setR_Category_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_R_CategoryUpdates(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=R_CategoryUpdates */
	public static final String Table_Name = "R_CategoryUpdates";

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
		StringBuffer sb = new StringBuffer("X_R_CategoryUpdates[").append(
				get_ID()).append("]");
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
	 * Set Category. Request Category
	 */
	public void setR_Category_ID(int R_Category_ID) {
		if (R_Category_ID < 1)
			throw new IllegalArgumentException("R_Category_ID is mandatory.");
		set_ValueNoCheck("R_Category_ID", new Integer(R_Category_ID));
	}

	/**
	 * Get Category. Request Category
	 */
	public int getR_Category_ID() {
		Integer ii = (Integer) get_Value("R_Category_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
