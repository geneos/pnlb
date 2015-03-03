/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_PInstance
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.671
 */
public class X_AD_PInstance extends PO {
	/** Standard Constructor */
	public X_AD_PInstance(Properties ctx, int AD_PInstance_ID, String trxName) {
		super(ctx, AD_PInstance_ID, trxName);
		/**
		 * if (AD_PInstance_ID == 0) { setAD_PInstance_ID (0); setAD_Process_ID
		 * (0); setIsProcessing (false); setRecord_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_AD_PInstance(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_PInstance */
	public static final String Table_Name = "AD_PInstance";

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
		StringBuffer sb = new StringBuffer("X_AD_PInstance[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Process Instance. Instance of the process
	 */
	public void setAD_PInstance_ID(int AD_PInstance_ID) {
		if (AD_PInstance_ID < 1)
			throw new IllegalArgumentException("AD_PInstance_ID is mandatory.");
		set_ValueNoCheck("AD_PInstance_ID", new Integer(AD_PInstance_ID));
	}

	/**
	 * Get Process Instance. Instance of the process
	 */
	public int getAD_PInstance_ID() {
		Integer ii = (Integer) get_Value("AD_PInstance_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getAD_PInstance_ID()));
	}

	/**
	 * Set Process. Process or Report
	 */
	public void setAD_Process_ID(int AD_Process_ID) {
		if (AD_Process_ID < 1)
			throw new IllegalArgumentException("AD_Process_ID is mandatory.");
		set_Value("AD_Process_ID", new Integer(AD_Process_ID));
	}

	/**
	 * Get Process. Process or Report
	 */
	public int getAD_Process_ID() {
		Integer ii = (Integer) get_Value("AD_Process_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public void setAD_User_ID(int AD_User_ID) {
		if (AD_User_ID <= 0)
			set_Value("AD_User_ID", null);
		else
			set_Value("AD_User_ID", new Integer(AD_User_ID));
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

	/** Set Error Msg */
	public void setErrorMsg(String ErrorMsg) {
		if (ErrorMsg != null && ErrorMsg.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			ErrorMsg = ErrorMsg.substring(0, 1999);
		}
		set_Value("ErrorMsg", ErrorMsg);
	}

	/** Get Error Msg */
	public String getErrorMsg() {
		return (String) get_Value("ErrorMsg");
	}

	/** Set Processing */
	public void setIsProcessing(boolean IsProcessing) {
		set_Value("IsProcessing", new Boolean(IsProcessing));
	}

	/** Get Processing */
	public boolean isProcessing() {
		Object oo = get_Value("IsProcessing");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Record ID. Direct internal record ID
	 */
	public void setRecord_ID(int Record_ID) {
		if (Record_ID < 0)
			throw new IllegalArgumentException("Record_ID is mandatory.");
		set_ValueNoCheck("Record_ID", new Integer(Record_ID));
	}

	/**
	 * Get Record ID. Direct internal record ID
	 */
	public int getRecord_ID() {
		Integer ii = (Integer) get_Value("Record_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Result. Result of the action taken
	 */
	public void setResult(int Result) {
		set_Value("Result", new Integer(Result));
	}

	/**
	 * Get Result. Result of the action taken
	 */
	public int getResult() {
		Integer ii = (Integer) get_Value("Result");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
