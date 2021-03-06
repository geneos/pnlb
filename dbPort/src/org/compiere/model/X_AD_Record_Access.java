/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Record_Access
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.046
 */
public class X_AD_Record_Access extends PO {
	/** Standard Constructor */
	public X_AD_Record_Access(Properties ctx, int AD_Record_Access_ID,
			String trxName) {
		super(ctx, AD_Record_Access_ID, trxName);
		/**
		 * if (AD_Record_Access_ID == 0) { setAD_Role_ID (0); setAD_Table_ID
		 * (0); setIsDependentEntities (false); // N setIsExclude (true); // Y
		 * setIsReadOnly (false); setRecord_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_AD_Record_Access(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Record_Access */
	public static final String Table_Name = "AD_Record_Access";

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
		StringBuffer sb = new StringBuffer("X_AD_Record_Access[").append(
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

	/**
	 * Set Dependent Entities. Also check access in dependent entities
	 */
	public void setIsDependentEntities(boolean IsDependentEntities) {
		set_Value("IsDependentEntities", new Boolean(IsDependentEntities));
	}

	/**
	 * Get Dependent Entities. Also check access in dependent entities
	 */
	public boolean isDependentEntities() {
		Object oo = get_Value("IsDependentEntities");
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
}
