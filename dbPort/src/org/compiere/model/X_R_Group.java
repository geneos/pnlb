/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for R_Group
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.39
 */
public class X_R_Group extends PO {
	/** Standard Constructor */
	public X_R_Group(Properties ctx, int R_Group_ID, String trxName) {
		super(ctx, R_Group_ID, trxName);
		/**
		 * if (R_Group_ID == 0) { setName (null); setR_Group_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_R_Group(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=R_Group */
	public static final String Table_Name = "R_Group";

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
		StringBuffer sb = new StringBuffer("X_R_Group[").append(get_ID())
				.append("]");
		return sb.toString();
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
	 * Set BOM. Bill of Material
	 */
	public void setM_BOM_ID(int M_BOM_ID) {
		if (M_BOM_ID <= 0)
			set_Value("M_BOM_ID", null);
		else
			set_Value("M_BOM_ID", new Integer(M_BOM_ID));
	}

	/**
	 * Get BOM. Bill of Material
	 */
	public int getM_BOM_ID() {
		Integer ii = (Integer) get_Value("M_BOM_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Change Notice. Bill of Materials (Engineering) Change Notice
	 * (Version)
	 */
	public void setM_ChangeNotice_ID(int M_ChangeNotice_ID) {
		if (M_ChangeNotice_ID <= 0)
			set_Value("M_ChangeNotice_ID", null);
		else
			set_Value("M_ChangeNotice_ID", new Integer(M_ChangeNotice_ID));
	}

	/**
	 * Get Change Notice. Bill of Materials (Engineering) Change Notice
	 * (Version)
	 */
	public int getM_ChangeNotice_ID() {
		Integer ii = (Integer) get_Value("M_ChangeNotice_ID");
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

	/**
	 * Set Group. Request Group
	 */
	public void setR_Group_ID(int R_Group_ID) {
		if (R_Group_ID < 1)
			throw new IllegalArgumentException("R_Group_ID is mandatory.");
		set_ValueNoCheck("R_Group_ID", new Integer(R_Group_ID));
	}

	/**
	 * Get Group. Request Group
	 */
	public int getR_Group_ID() {
		Integer ii = (Integer) get_Value("R_Group_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
