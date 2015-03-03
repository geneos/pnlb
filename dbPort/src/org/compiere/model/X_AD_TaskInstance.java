/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_TaskInstance
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.453
 */
public class X_AD_TaskInstance extends PO {
	/** Standard Constructor */
	public X_AD_TaskInstance(Properties ctx, int AD_TaskInstance_ID,
			String trxName) {
		super(ctx, AD_TaskInstance_ID, trxName);
		/**
		 * if (AD_TaskInstance_ID == 0) { setAD_TaskInstance_ID (0);
		 * setAD_Task_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_AD_TaskInstance(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_TaskInstance */
	public static final String Table_Name = "AD_TaskInstance";

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
		StringBuffer sb = new StringBuffer("X_AD_TaskInstance[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/** Set Task Instance */
	public void setAD_TaskInstance_ID(int AD_TaskInstance_ID) {
		if (AD_TaskInstance_ID < 1)
			throw new IllegalArgumentException(
					"AD_TaskInstance_ID is mandatory.");
		set_ValueNoCheck("AD_TaskInstance_ID", new Integer(AD_TaskInstance_ID));
	}

	/** Get Task Instance */
	public int getAD_TaskInstance_ID() {
		Integer ii = (Integer) get_Value("AD_TaskInstance_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getAD_TaskInstance_ID()));
	}

	/**
	 * Set OS Task. Operation System Task
	 */
	public void setAD_Task_ID(int AD_Task_ID) {
		if (AD_Task_ID < 1)
			throw new IllegalArgumentException("AD_Task_ID is mandatory.");
		set_Value("AD_Task_ID", new Integer(AD_Task_ID));
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
}
