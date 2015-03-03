/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for MPC_Cost_Group
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:01.796
 */
public class X_MPC_Cost_Group extends PO {
	/** Standard Constructor */
	public X_MPC_Cost_Group(Properties ctx, int MPC_Cost_Group_ID,
			String trxName) {
		super(ctx, MPC_Cost_Group_ID, trxName);
		/**
		 * if (MPC_Cost_Group_ID == 0) { setIsGL (false); setMPC_Cost_Group_ID
		 * (0); setName (null); setValue (null); }
		 */
	}

	/** Load Constructor */
	public X_MPC_Cost_Group(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=MPC_Cost_Group */
	public static final String Table_Name = "MPC_Cost_Group";

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
		StringBuffer sb = new StringBuffer("X_MPC_Cost_Group[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Description. Optional short description of the record
	 */
	public void setDescription(String Description) {
		if (Description != null && Description.length() > 1020) {
			log.warning("Length > 1020 - truncated");
			Description = Description.substring(0, 1019);
		}
		set_Value("Description", Description);
	}

	/**
	 * Get Description. Optional short description of the record
	 */
	public String getDescription() {
		return (String) get_Value("Description");
	}

	/** Set IsGL */
	public void setIsGL(boolean IsGL) {
		set_Value("IsGL", new Boolean(IsGL));
	}

	/** Get IsGL */
	public boolean isGL() {
		Object oo = get_Value("IsGL");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Cost Group CMPCS */
	public void setMPC_Cost_Group_ID(int MPC_Cost_Group_ID) {
		if (MPC_Cost_Group_ID < 1)
			throw new IllegalArgumentException(
					"MPC_Cost_Group_ID is mandatory.");
		set_ValueNoCheck("MPC_Cost_Group_ID", new Integer(MPC_Cost_Group_ID));
	}

	/** Get Cost Group CMPCS */
	public int getMPC_Cost_Group_ID() {
		Integer ii = (Integer) get_Value("MPC_Cost_Group_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getName());
	}

	/**
	 * Set Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public void setValue(String Value) {
		if (Value == null)
			throw new IllegalArgumentException("Value is mandatory.");
		if (Value.length() > 80) {
			log.warning("Length > 80 - truncated");
			Value = Value.substring(0, 79);
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
}
