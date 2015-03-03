/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for GL_FundRestriction
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:01.109
 */
public class X_GL_FundRestriction extends PO {
	/** Standard Constructor */
	public X_GL_FundRestriction(Properties ctx, int GL_FundRestriction_ID,
			String trxName) {
		super(ctx, GL_FundRestriction_ID, trxName);
		/**
		 * if (GL_FundRestriction_ID == 0) { setC_ElementValue_ID (0);
		 * setGL_FundRestriction_ID (0); setGL_Fund_ID (0); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_GL_FundRestriction(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=GL_FundRestriction */
	public static final String Table_Name = "GL_FundRestriction";

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

	protected BigDecimal accessLevel = new BigDecimal(2);

	/** AccessLevel 2 - Client */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_GL_FundRestriction[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Account Element. Account Element
	 */
	public void setC_ElementValue_ID(int C_ElementValue_ID) {
		if (C_ElementValue_ID < 1)
			throw new IllegalArgumentException(
					"C_ElementValue_ID is mandatory.");
		set_Value("C_ElementValue_ID", new Integer(C_ElementValue_ID));
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
	 * Set Fund Restriction. Restriction of Funds
	 */
	public void setGL_FundRestriction_ID(int GL_FundRestriction_ID) {
		if (GL_FundRestriction_ID < 1)
			throw new IllegalArgumentException(
					"GL_FundRestriction_ID is mandatory.");
		set_ValueNoCheck("GL_FundRestriction_ID", new Integer(
				GL_FundRestriction_ID));
	}

	/**
	 * Get Fund Restriction. Restriction of Funds
	 */
	public int getGL_FundRestriction_ID() {
		Integer ii = (Integer) get_Value("GL_FundRestriction_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set GL Fund. General Ledger Funds Control
	 */
	public void setGL_Fund_ID(int GL_Fund_ID) {
		if (GL_Fund_ID < 1)
			throw new IllegalArgumentException("GL_Fund_ID is mandatory.");
		set_ValueNoCheck("GL_Fund_ID", new Integer(GL_Fund_ID));
	}

	/**
	 * Get GL Fund. General Ledger Funds Control
	 */
	public int getGL_Fund_ID() {
		Integer ii = (Integer) get_Value("GL_Fund_ID");
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
}
