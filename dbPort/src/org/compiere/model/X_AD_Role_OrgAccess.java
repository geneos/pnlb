/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Role_OrgAccess
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.25
 */
public class X_AD_Role_OrgAccess extends PO {
	/** Standard Constructor */
	public X_AD_Role_OrgAccess(Properties ctx, int AD_Role_OrgAccess_ID,
			String trxName) {
		super(ctx, AD_Role_OrgAccess_ID, trxName);
		/**
		 * if (AD_Role_OrgAccess_ID == 0) { setAD_Role_ID (0); setIsReadOnly
		 * (false); }
		 */
	}

	/** Load Constructor */
	public X_AD_Role_OrgAccess(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Role_OrgAccess */
	public static final String Table_Name = "AD_Role_OrgAccess";

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
		StringBuffer sb = new StringBuffer("X_AD_Role_OrgAccess[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Role. Responsibility Role
	 */
	public void setAD_Role_ID(int AD_Role_ID) {
		if (AD_Role_ID < 0)
			throw new IllegalArgumentException("AD_Role_ID is mandatory.");
		set_ValueNoCheck("AD_Role_ID", new Integer(AD_Role_ID));
	}

	/**
	 * Get Role. Responsibility Role
	 */
	public int getAD_Role_ID() {
		Integer ii = (Integer) get_Value("AD_Role_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Read Only. Field is read only
	 */
	public void setIsReadOnly(boolean IsReadOnly) {
		set_Value("IsReadOnly", new Boolean(IsReadOnly));
	}

	/**
	 * Get Read Only. Field is read only
	 */
	public boolean isReadOnly() {
		Object oo = get_Value("IsReadOnly");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}
}
