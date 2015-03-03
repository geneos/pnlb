/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Scheduler_Para
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.296
 */
public class X_AD_Scheduler_Para extends PO {
	/** Standard Constructor */
	public X_AD_Scheduler_Para(Properties ctx, int AD_Scheduler_Para_ID,
			String trxName) {
		super(ctx, AD_Scheduler_Para_ID, trxName);
		/**
		 * if (AD_Scheduler_Para_ID == 0) { setAD_Process_Para_ID (0);
		 * setAD_Scheduler_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_AD_Scheduler_Para(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Scheduler_Para */
	public static final String Table_Name = "AD_Scheduler_Para";

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
		StringBuffer sb = new StringBuffer("X_AD_Scheduler_Para[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/** Set Process Parameter */
	public void setAD_Process_Para_ID(int AD_Process_Para_ID) {
		if (AD_Process_Para_ID < 1)
			throw new IllegalArgumentException(
					"AD_Process_Para_ID is mandatory.");
		set_ValueNoCheck("AD_Process_Para_ID", new Integer(AD_Process_Para_ID));
	}

	/** Get Process Parameter */
	public int getAD_Process_Para_ID() {
		Integer ii = (Integer) get_Value("AD_Process_Para_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Scheduler. Schedule Processes
	 */
	public void setAD_Scheduler_ID(int AD_Scheduler_ID) {
		if (AD_Scheduler_ID < 1)
			throw new IllegalArgumentException("AD_Scheduler_ID is mandatory.");
		set_ValueNoCheck("AD_Scheduler_ID", new Integer(AD_Scheduler_ID));
	}

	/**
	 * Get Scheduler. Schedule Processes
	 */
	public int getAD_Scheduler_ID() {
		Integer ii = (Integer) get_Value("AD_Scheduler_ID");
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
	 * Set Default Parameter. Default value of the parameter
	 */
	public void setParameterDefault(String ParameterDefault) {
		if (ParameterDefault != null && ParameterDefault.length() > 60) {
			log.warning("Length > 60 - truncated");
			ParameterDefault = ParameterDefault.substring(0, 59);
		}
		set_Value("ParameterDefault", ParameterDefault);
	}

	/**
	 * Get Default Parameter. Default value of the parameter
	 */
	public String getParameterDefault() {
		return (String) get_Value("ParameterDefault");
	}
}
