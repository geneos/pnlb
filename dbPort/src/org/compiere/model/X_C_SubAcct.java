/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_SubAcct
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.578
 */
public class X_C_SubAcct extends PO {
	/** Standard Constructor */
	public X_C_SubAcct(Properties ctx, int C_SubAcct_ID, String trxName) {
		super(ctx, C_SubAcct_ID, trxName);
		/**
		 * if (C_SubAcct_ID == 0) { setC_ElementValue_ID (0); setC_SubAcct_ID
		 * (0); setName (null); setValue (null); }
		 */
	}

	/** Load Constructor */
	public X_C_SubAcct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_SubAcct */
	public static final String Table_Name = "C_SubAcct";

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

	protected BigDecimal accessLevel = new BigDecimal(3);

	/** AccessLevel 3 - Client - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_C_SubAcct[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Account Element. Account Element
	 */
	public void setC_ElementValue_ID(int C_ElementValue_ID) {
		if (C_ElementValue_ID < 1)
			throw new IllegalArgumentException(
					"C_ElementValue_ID is mandatory.");
		set_ValueNoCheck("C_ElementValue_ID", new Integer(C_ElementValue_ID));
	}

	/**
	 * Get Account Element. Account Element
	 */
	public int getC_ElementValue_ID() {
		Integer ii = (Integer) get_Value("C_ElementValue_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Sub Account. Sub account for Element Value
	 */
	public void setC_SubAcct_ID(int C_SubAcct_ID) {
		if (C_SubAcct_ID < 1)
			throw new IllegalArgumentException("C_SubAcct_ID is mandatory.");
		set_ValueNoCheck("C_SubAcct_ID", new Integer(C_SubAcct_ID));
	}

	/**
	 * Get Sub Account. Sub account for Element Value
	 */
	public int getC_SubAcct_ID() {
		Integer ii = (Integer) get_Value("C_SubAcct_ID");
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
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name == null)
			throw new IllegalArgumentException("Name is mandatory.");
		if (Name.length() > 120) {
			log.warning("Length > 120 - truncated");
			Name = Name.substring(0, 119);
		}
		set_Value("Name", Name);
	}

	/**
	 * Get Name. Alphanumeric identifier of the entity
	 */
	public String getName() {
		return (String) get_Value("Name");
	}

	/**
	 * Set Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public void setValue(String Value) {
		if (Value == null)
			throw new IllegalArgumentException("Value is mandatory.");
		if (Value.length() > 40) {
			log.warning("Length > 40 - truncated");
			Value = Value.substring(0, 39);
		}
		set_Value("Value", Value);
	}

	/**
	 * Get Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public String getValue() {
		return (String) get_Value("Value");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getValue());
	}
}
