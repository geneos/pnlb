/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Table_Access
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.421
 */
public class X_AD_Table_Access extends PO {
	/** Standard Constructor */
	public X_AD_Table_Access(Properties ctx, int AD_Table_Access_ID,
			String trxName) {
		super(ctx, AD_Table_Access_ID, trxName);
		/**
		 * if (AD_Table_Access_ID == 0) { setAD_Role_ID (0); setAD_Table_ID (0);
		 * setAccessTypeRule (null); // A setIsCanExport (false); setIsCanReport
		 * (false); setIsExclude (true); // Y setIsReadOnly (false); }
		 */
	}

	/** Load Constructor */
	public X_AD_Table_Access(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Table_Access */
	public static final String Table_Name = "AD_Table_Access";

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
		StringBuffer sb = new StringBuffer("X_AD_Table_Access[").append(
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
	 * Set Table. Table for the Fields
	 */
	public void setAD_Table_ID(int AD_Table_ID) {
		if (AD_Table_ID < 1)
			throw new IllegalArgumentException("AD_Table_ID is mandatory.");
		set_ValueNoCheck("AD_Table_ID", new Integer(AD_Table_ID));
	}

	/**
	 * Get Table. Table for the Fields
	 */
	public int getAD_Table_ID() {
		Integer ii = (Integer) get_Value("AD_Table_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getAD_Table_ID()));
	}

	/** AccessTypeRule AD_Reference_ID=293 */
	public static final int ACCESSTYPERULE_AD_Reference_ID = 293;

	/** Accessing = A */
	public static final String ACCESSTYPERULE_Accessing = "A";

	/** Exporting = E */
	public static final String ACCESSTYPERULE_Exporting = "E";

	/** Reporting = R */
	public static final String ACCESSTYPERULE_Reporting = "R";

	/**
	 * Set Access Type. The type of access for this rule
	 */
	public void setAccessTypeRule(String AccessTypeRule) {
		if (AccessTypeRule == null)
			throw new IllegalArgumentException("AccessTypeRule is mandatory");
		if (AccessTypeRule.equals("A") || AccessTypeRule.equals("E")
				|| AccessTypeRule.equals("R"))
			;
		else
			throw new IllegalArgumentException(
					"AccessTypeRule Invalid value - " + AccessTypeRule
							+ " - Reference_ID=293 - A - E - R");
		if (AccessTypeRule.length() > 1) {
			log.warning("Length > 1 - truncated");
			AccessTypeRule = AccessTypeRule.substring(0, 0);
		}
		set_ValueNoCheck("AccessTypeRule", AccessTypeRule);
	}

	/**
	 * Get Access Type. The type of access for this rule
	 */
	public String getAccessTypeRule() {
		return (String) get_Value("AccessTypeRule");
	}

	/**
	 * Set Can Export. Users with this role can export data
	 */
	public void setIsCanExport(boolean IsCanExport) {
		set_Value("IsCanExport", new Boolean(IsCanExport));
	}

	/**
	 * Get Can Export. Users with this role can export data
	 */
	public boolean isCanExport() {
		Object oo = get_Value("IsCanExport");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Can Report. Users with this role can create reports
	 */
	public void setIsCanReport(boolean IsCanReport) {
		set_Value("IsCanReport", new Boolean(IsCanReport));
	}

	/**
	 * Get Can Report. Users with this role can create reports
	 */
	public boolean isCanReport() {
		Object oo = get_Value("IsCanReport");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Exclude. Exclude access to the data - if not selected Include access
	 * to the data
	 */
	public void setIsExclude(boolean IsExclude) {
		set_Value("IsExclude", new Boolean(IsExclude));
	}

	/**
	 * Get Exclude. Exclude access to the data - if not selected Include access
	 * to the data
	 */
	public boolean isExclude() {
		Object oo = get_Value("IsExclude");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
