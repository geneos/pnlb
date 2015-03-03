/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_UserDef_Tab
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.593
 */
public class X_AD_UserDef_Tab extends PO {
	/** Standard Constructor */
	public X_AD_UserDef_Tab(Properties ctx, int AD_UserDef_Tab_ID,
			String trxName) {
		super(ctx, AD_UserDef_Tab_ID, trxName);
		/**
		 * if (AD_UserDef_Tab_ID == 0) { setAD_Tab_ID (0); setAD_UserDef_Tab_ID
		 * (0); setAD_UserDef_Win_ID (0); setIsMultiRowOnly (false);
		 * setIsReadOnly (false); setIsSingleRow (false); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_UserDef_Tab(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_UserDef_Tab */
	public static final String Table_Name = "AD_UserDef_Tab";

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

	protected BigDecimal accessLevel = new BigDecimal(4);

	/** AccessLevel 4 - System */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_AD_UserDef_Tab[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Tab. Tab within a Window
	 */
	public void setAD_Tab_ID(int AD_Tab_ID) {
		if (AD_Tab_ID < 1)
			throw new IllegalArgumentException("AD_Tab_ID is mandatory.");
		set_Value("AD_Tab_ID", new Integer(AD_Tab_ID));
	}

	/**
	 * Get Tab. Tab within a Window
	 */
	public int getAD_Tab_ID() {
		Integer ii = (Integer) get_Value("AD_Tab_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set User defined Tab */
	public void setAD_UserDef_Tab_ID(int AD_UserDef_Tab_ID) {
		if (AD_UserDef_Tab_ID < 1)
			throw new IllegalArgumentException(
					"AD_UserDef_Tab_ID is mandatory.");
		set_ValueNoCheck("AD_UserDef_Tab_ID", new Integer(AD_UserDef_Tab_ID));
	}

	/** Get User defined Tab */
	public int getAD_UserDef_Tab_ID() {
		Integer ii = (Integer) get_Value("AD_UserDef_Tab_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set User defined Window */
	public void setAD_UserDef_Win_ID(int AD_UserDef_Win_ID) {
		if (AD_UserDef_Win_ID < 1)
			throw new IllegalArgumentException(
					"AD_UserDef_Win_ID is mandatory.");
		set_ValueNoCheck("AD_UserDef_Win_ID", new Integer(AD_UserDef_Win_ID));
	}

	/** Get User defined Window */
	public int getAD_UserDef_Win_ID() {
		Integer ii = (Integer) get_Value("AD_UserDef_Win_ID");
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
	 * Set Comment/Help. Comment or Hint
	 */
	public void setHelp(String Help) {
		if (Help != null && Help.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Help = Help.substring(0, 1999);
		}
		set_Value("Help", Help);
	}

	/**
	 * Get Comment/Help. Comment or Hint
	 */
	public String getHelp() {
		return (String) get_Value("Help");
	}

	/**
	 * Set Multi Row Only. This applies to Multi-Row view only
	 */
	public void setIsMultiRowOnly(boolean IsMultiRowOnly) {
		set_Value("IsMultiRowOnly", new Boolean(IsMultiRowOnly));
	}

	/**
	 * Get Multi Row Only. This applies to Multi-Row view only
	 */
	public boolean isMultiRowOnly() {
		Object oo = get_Value("IsMultiRowOnly");
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
	 * Set Single Row Layout. Default for toggle between Single- and Multi-Row
	 * (Grid) Layout
	 */
	public void setIsSingleRow(boolean IsSingleRow) {
		set_Value("IsSingleRow", new Boolean(IsSingleRow));
	}

	/**
	 * Get Single Row Layout. Default for toggle between Single- and Multi-Row
	 * (Grid) Layout
	 */
	public boolean isSingleRow() {
		Object oo = get_Value("IsSingleRow");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name == null)
			throw new IllegalArgumentException("Name is mandatory.");
		if (Name.length() > 60) {
			log.warning("Length > 60 - truncated");
			Name = Name.substring(0, 59);
		}
		set_Value("Name", Name);
	}

	/**
	 * Get Name. Alphanumeric identifier of the entity
	 */
	public String getName() {
		return (String) get_Value("Name");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getName());
	}
}
