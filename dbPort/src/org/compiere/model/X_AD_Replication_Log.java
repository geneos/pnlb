/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Replication_Log
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.171
 */
public class X_AD_Replication_Log extends PO {
	/** Standard Constructor */
	public X_AD_Replication_Log(Properties ctx, int AD_Replication_Log_ID,
			String trxName) {
		super(ctx, AD_Replication_Log_ID, trxName);
		/**
		 * if (AD_Replication_Log_ID == 0) { setAD_Replication_Log_ID (0);
		 * setAD_Replication_Run_ID (0); setIsReplicated (false); // N }
		 */
	}

	/** Load Constructor */
	public X_AD_Replication_Log(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Replication_Log */
	public static final String Table_Name = "AD_Replication_Log";

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
		StringBuffer sb = new StringBuffer("X_AD_Replication_Log[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Replication Table. Data Replication Strategy Table Info
	 */
	public void setAD_ReplicationTable_ID(int AD_ReplicationTable_ID) {
		if (AD_ReplicationTable_ID <= 0)
			set_Value("AD_ReplicationTable_ID", null);
		else
			set_Value("AD_ReplicationTable_ID", new Integer(
					AD_ReplicationTable_ID));
	}

	/**
	 * Get Replication Table. Data Replication Strategy Table Info
	 */
	public int getAD_ReplicationTable_ID() {
		Integer ii = (Integer) get_Value("AD_ReplicationTable_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Replication Log. Data Replication Log Details
	 */
	public void setAD_Replication_Log_ID(int AD_Replication_Log_ID) {
		if (AD_Replication_Log_ID < 1)
			throw new IllegalArgumentException(
					"AD_Replication_Log_ID is mandatory.");
		set_ValueNoCheck("AD_Replication_Log_ID", new Integer(
				AD_Replication_Log_ID));
	}

	/**
	 * Get Replication Log. Data Replication Log Details
	 */
	public int getAD_Replication_Log_ID() {
		Integer ii = (Integer) get_Value("AD_Replication_Log_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Replication Run. Data Replication Run
	 */
	public void setAD_Replication_Run_ID(int AD_Replication_Run_ID) {
		if (AD_Replication_Run_ID < 1)
			throw new IllegalArgumentException(
					"AD_Replication_Run_ID is mandatory.");
		set_ValueNoCheck("AD_Replication_Run_ID", new Integer(
				AD_Replication_Run_ID));
	}

	/**
	 * Get Replication Run. Data Replication Run
	 */
	public int getAD_Replication_Run_ID() {
		Integer ii = (Integer) get_Value("AD_Replication_Run_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getAD_Replication_Run_ID()));
	}

	/**
	 * Set Replicated. The data is successfully replicated
	 */
	public void setIsReplicated(boolean IsReplicated) {
		set_Value("IsReplicated", new Boolean(IsReplicated));
	}

	/**
	 * Get Replicated. The data is successfully replicated
	 */
	public boolean isReplicated() {
		Object oo = get_Value("IsReplicated");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Process Message */
	public void setP_Msg(String P_Msg) {
		if (P_Msg != null && P_Msg.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			P_Msg = P_Msg.substring(0, 1999);
		}
		set_Value("P_Msg", P_Msg);
	}

	/** Get Process Message */
	public String getP_Msg() {
		return (String) get_Value("P_Msg");
	}
}
