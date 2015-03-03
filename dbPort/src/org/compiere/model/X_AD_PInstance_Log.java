/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_PInstance_Log
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.671
 */
public class X_AD_PInstance_Log extends PO {
	/** Standard Constructor */
	public X_AD_PInstance_Log(Properties ctx, int AD_PInstance_Log_ID,
			String trxName) {
		super(ctx, AD_PInstance_Log_ID, trxName);
		/**
		 * if (AD_PInstance_Log_ID == 0) { setAD_PInstance_ID (0); setLog_ID
		 * (0); }
		 */
	}

	/** Load Constructor */
	public X_AD_PInstance_Log(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_PInstance_Log */
	public static final String Table_Name = "AD_PInstance_Log";

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
		StringBuffer sb = new StringBuffer("X_AD_PInstance_Log[").append(
				get_ID()).append("]");
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

	/** Set Log */
	public void setLog_ID(int Log_ID) {
		if (Log_ID < 1)
			throw new IllegalArgumentException("Log_ID is mandatory.");
		set_ValueNoCheck("Log_ID", new Integer(Log_ID));
	}

	/** Get Log */
	public int getLog_ID() {
		Integer ii = (Integer) get_Value("Log_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Process Date. Process Parameter
	 */
	public void setP_Date(Timestamp P_Date) {
		set_ValueNoCheck("P_Date", P_Date);
	}

	/**
	 * Get Process Date. Process Parameter
	 */
	public Timestamp getP_Date() {
		return (Timestamp) get_Value("P_Date");
	}

	/** Set Process ID */
	public void setP_ID(int P_ID) {
		if (P_ID <= 0)
			set_ValueNoCheck("P_ID", null);
		else
			set_ValueNoCheck("P_ID", new Integer(P_ID));
	}

	/** Get Process ID */
	public int getP_ID() {
		Integer ii = (Integer) get_Value("P_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Process Message */
	public void setP_Msg(String P_Msg) {
		if (P_Msg != null && P_Msg.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			P_Msg = P_Msg.substring(0, 1999);
		}
		set_ValueNoCheck("P_Msg", P_Msg);
	}

	/** Get Process Message */
	public String getP_Msg() {
		return (String) get_Value("P_Msg");
	}

	/**
	 * Set Process Number. Process Parameter
	 */
	public void setP_Number(BigDecimal P_Number) {
		set_ValueNoCheck("P_Number", P_Number);
	}

	/**
	 * Get Process Number. Process Parameter
	 */
	public BigDecimal getP_Number() {
		BigDecimal bd = (BigDecimal) get_Value("P_Number");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
