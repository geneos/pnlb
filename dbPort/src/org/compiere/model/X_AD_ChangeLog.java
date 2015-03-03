/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_ChangeLog
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.171
 */
public class X_AD_ChangeLog extends PO {
	/** Standard Constructor */
	public X_AD_ChangeLog(Properties ctx, int AD_ChangeLog_ID, String trxName) {
		super(ctx, AD_ChangeLog_ID, trxName);
		/**
		 * if (AD_ChangeLog_ID == 0) { setAD_ChangeLog_ID (0); setAD_Column_ID
		 * (0); setAD_Session_ID (0); setAD_Table_ID (0); setIsCustomization
		 * (false); setRecord_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_AD_ChangeLog(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_ChangeLog */
	public static final String Table_Name = "AD_ChangeLog";

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
		StringBuffer sb = new StringBuffer("X_AD_ChangeLog[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Change Log. Log of data changes
	 */
	public void setAD_ChangeLog_ID(int AD_ChangeLog_ID) {
		if (AD_ChangeLog_ID < 1)
			throw new IllegalArgumentException("AD_ChangeLog_ID is mandatory.");
		set_ValueNoCheck("AD_ChangeLog_ID", new Integer(AD_ChangeLog_ID));
	}

	/**
	 * Get Change Log. Log of data changes
	 */
	public int getAD_ChangeLog_ID() {
		Integer ii = (Integer) get_Value("AD_ChangeLog_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Column. Column in the table
	 */
	public void setAD_Column_ID(int AD_Column_ID) {
		if (AD_Column_ID < 1)
			throw new IllegalArgumentException("AD_Column_ID is mandatory.");
		set_ValueNoCheck("AD_Column_ID", new Integer(AD_Column_ID));
	}

	/**
	 * Get Column. Column in the table
	 */
	public int getAD_Column_ID() {
		Integer ii = (Integer) get_Value("AD_Column_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
	 * Set Customization. The change is a customization of the data dictionary
	 * and can be applied after Migration
	 */
	public void setIsCustomization(boolean IsCustomization) {
		set_Value("IsCustomization", new Boolean(IsCustomization));
	}

	/**
	 * Get Customization. The change is a customization of the data dictionary
	 * and can be applied after Migration
	 */
	public boolean isCustomization() {
		Object oo = get_Value("IsCustomization");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set New Value. New field value
	 */
	public void setNewValue(String NewValue) {
		if (NewValue != null && NewValue.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			NewValue = NewValue.substring(0, 1999);
		}
		set_ValueNoCheck("NewValue", NewValue);
	}

	/**
	 * Get New Value. New field value
	 */
	public String getNewValue() {
		return (String) get_Value("NewValue");
	}

	/**
	 * Set Old Value. The old file data
	 */
	public void setOldValue(String OldValue) {
		if (OldValue != null && OldValue.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			OldValue = OldValue.substring(0, 1999);
		}
		set_ValueNoCheck("OldValue", OldValue);
	}

	/**
	 * Get Old Value. The old file data
	 */
	public String getOldValue() {
		return (String) get_Value("OldValue");
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

	/** Set Redo */
	public void setRedo(String Redo) {
		if (Redo != null && Redo.length() > 1) {
			log.warning("Length > 1 - truncated");
			Redo = Redo.substring(0, 0);
		}
		set_Value("Redo", Redo);
	}

	/** Get Redo */
	public String getRedo() {
		return (String) get_Value("Redo");
	}

	/**
	 * Set Transaction. Name of the transaction
	 */
	public void setTrxName(String TrxName) {
		if (TrxName != null && TrxName.length() > 60) {
			log.warning("Length > 60 - truncated");
			TrxName = TrxName.substring(0, 59);
		}
		set_ValueNoCheck("TrxName", TrxName);
	}

	/**
	 * Get Transaction. Name of the transaction
	 */
	public String getTrxName() {
		return (String) get_Value("TrxName");
	}

	/** Set Undo */
	public void setUndo(String Undo) {
		if (Undo != null && Undo.length() > 1) {
			log.warning("Length > 1 - truncated");
			Undo = Undo.substring(0, 0);
		}
		set_Value("Undo", Undo);
	}

	/** Get Undo */
	public String getUndo() {
		return (String) get_Value("Undo");
	}
}
