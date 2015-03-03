/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Session
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.343
 */
public class X_AD_Session extends PO {
	/** Standard Constructor */
	public X_AD_Session(Properties ctx, int AD_Session_ID, String trxName) {
		super(ctx, AD_Session_ID, trxName);
		/**
		 * if (AD_Session_ID == 0) { setAD_Session_ID (0); setProcessed (false); }
		 */
	}

	/** Load Constructor */
	public X_AD_Session(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Session */
	public static final String Table_Name = "AD_Session";

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
		StringBuffer sb = new StringBuffer("X_AD_Session[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Session. User Session Online or Web
	 */
	public void setAD_Session_ID(int AD_Session_ID) {
		if (AD_Session_ID < 1)
			throw new IllegalArgumentException("AD_Session_ID is mandatory.");
		set_ValueNoCheck("AD_Session_ID", new Integer(AD_Session_ID));
	}

	/**
	 * Get Session. User Session Online or Web
	 */
	public int getAD_Session_ID() {
		Integer ii = (Integer) get_Value("AD_Session_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getAD_Session_ID()));
	}

	/**
	 * Set Processed. The document has been processed
	 */
	public void setProcessed(boolean Processed) {
		set_ValueNoCheck("Processed", new Boolean(Processed));
	}

	/**
	 * Get Processed. The document has been processed
	 */
	public boolean isProcessed() {
		Object oo = get_Value("Processed");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Remote Addr. Remote Address
	 */
	public void setRemote_Addr(String Remote_Addr) {
		if (Remote_Addr != null && Remote_Addr.length() > 60) {
			log.warning("Length > 60 - truncated");
			Remote_Addr = Remote_Addr.substring(0, 59);
		}
		set_ValueNoCheck("Remote_Addr", Remote_Addr);
	}

	/**
	 * Get Remote Addr. Remote Address
	 */
	public String getRemote_Addr() {
		return (String) get_Value("Remote_Addr");
	}

	/**
	 * Set Remote Host. Remote host Info
	 */
	public void setRemote_Host(String Remote_Host) {
		if (Remote_Host != null && Remote_Host.length() > 120) {
			log.warning("Length > 120 - truncated");
			Remote_Host = Remote_Host.substring(0, 119);
		}
		set_ValueNoCheck("Remote_Host", Remote_Host);
	}

	/**
	 * Get Remote Host. Remote host Info
	 */
	public String getRemote_Host() {
		return (String) get_Value("Remote_Host");
	}

	/**
	 * Set Web Session. Web Session ID
	 */
	public void setWebSession(String WebSession) {
		if (WebSession != null && WebSession.length() > 40) {
			log.warning("Length > 40 - truncated");
			WebSession = WebSession.substring(0, 39);
		}
		set_ValueNoCheck("WebSession", WebSession);
	}

	/**
	 * Get Web Session. Web Session ID
	 */
	public String getWebSession() {
		return (String) get_Value("WebSession");
	}
}
