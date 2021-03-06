/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Task_Access
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.468
 */
public class X_AD_Task_Access extends PO {
	/** Standard Constructor */
	public X_AD_Task_Access(Properties ctx, int AD_Task_Access_ID,
			String trxName) {
		super(ctx, AD_Task_Access_ID, trxName);
		/**
		 * if (AD_Task_Access_ID == 0) { setAD_Role_ID (0); setAD_Task_ID (0);
		 * setIsReadWrite (false); }
		 */
	}

	/** Load Constructor */
	public X_AD_Task_Access(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Task_Access */
	public static final String Table_Name = "AD_Task_Access";

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
		StringBuffer sb = new StringBuffer("X_AD_Task_Access[")
				.append(get_ID()).append("]");
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
	 * Set OS Task. Operation System Task
	 */
	public void setAD_Task_ID(int AD_Task_ID) {
		if (AD_Task_ID < 1)
			throw new IllegalArgumentException("AD_Task_ID is mandatory.");
		set_ValueNoCheck("AD_Task_ID", new Integer(AD_Task_ID));
	}

	/**
	 * Get OS Task. Operation System Task
	 */
	public int getAD_Task_ID() {
		Integer ii = (Integer) get_Value("AD_Task_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Read Write. Field is read / write
	 */
	public void setIsReadWrite(boolean IsReadWrite) {
		set_Value("IsReadWrite", new Boolean(IsReadWrite));
	}

	/**
	 * Get Read Write. Field is read / write
	 */
	public boolean isReadWrite() {
		Object oo = get_Value("IsReadWrite");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}
}
