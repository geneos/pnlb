/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for GL_Fund
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:01.109
 */
public class X_GL_Fund extends PO {
	/** Standard Constructor */
	public X_GL_Fund(Properties ctx, int GL_Fund_ID, String trxName) {
		super(ctx, GL_Fund_ID, trxName);
		/**
		 * if (GL_Fund_ID == 0) { setAmt (Env.ZERO); setC_AcctSchema_ID (0);
		 * setGL_Fund_ID (0); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_GL_Fund(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=GL_Fund */
	public static final String Table_Name = "GL_Fund";

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
		StringBuffer sb = new StringBuffer("X_GL_Fund[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Amount. Amount
	 */
	public void setAmt(BigDecimal Amt) {
		if (Amt == null)
			throw new IllegalArgumentException("Amt is mandatory.");
		set_Value("Amt", Amt);
	}

	/**
	 * Get Amount. Amount
	 */
	public BigDecimal getAmt() {
		BigDecimal bd = (BigDecimal) get_Value("Amt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Accounting Schema. Rules for accounting
	 */
	public void setC_AcctSchema_ID(int C_AcctSchema_ID) {
		if (C_AcctSchema_ID < 1)
			throw new IllegalArgumentException("C_AcctSchema_ID is mandatory.");
		set_Value("C_AcctSchema_ID", new Integer(C_AcctSchema_ID));
	}

	/**
	 * Get Accounting Schema. Rules for accounting
	 */
	public int getC_AcctSchema_ID() {
		Integer ii = (Integer) get_Value("C_AcctSchema_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Date From. Starting date for a range
	 */
	public void setDateFrom(Timestamp DateFrom) {
		set_Value("DateFrom", DateFrom);
	}

	/**
	 * Get Date From. Starting date for a range
	 */
	public Timestamp getDateFrom() {
		return (Timestamp) get_Value("DateFrom");
	}

	/**
	 * Set Date To. End date of a date range
	 */
	public void setDateTo(Timestamp DateTo) {
		set_Value("DateTo", DateTo);
	}

	/**
	 * Get Date To. End date of a date range
	 */
	public Timestamp getDateTo() {
		return (Timestamp) get_Value("DateTo");
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getName());
	}
}
