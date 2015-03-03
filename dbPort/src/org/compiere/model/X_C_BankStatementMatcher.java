/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_BankStatementMatcher
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.953
 */
public class X_C_BankStatementMatcher extends PO {
	/** Standard Constructor */
	public X_C_BankStatementMatcher(Properties ctx,
			int C_BankStatementMatcher_ID, String trxName) {
		super(ctx, C_BankStatementMatcher_ID, trxName);
		/**
		 * if (C_BankStatementMatcher_ID == 0) { setC_BankStatementMatcher_ID
		 * (0); setClassname (null); setName (null); setSeqNo (0); }
		 */
	}

	/** Load Constructor */
	public X_C_BankStatementMatcher(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_BankStatementMatcher */
	public static final String Table_Name = "C_BankStatementMatcher";

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
		StringBuffer sb = new StringBuffer("X_C_BankStatementMatcher[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Bank Statement Matcher. Algorithm to match Bank Statement Info to
	 * Business Partners, Invoices and Payments
	 */
	public void setC_BankStatementMatcher_ID(int C_BankStatementMatcher_ID) {
		if (C_BankStatementMatcher_ID < 1)
			throw new IllegalArgumentException(
					"C_BankStatementMatcher_ID is mandatory.");
		set_ValueNoCheck("C_BankStatementMatcher_ID", new Integer(
				C_BankStatementMatcher_ID));
	}

	/**
	 * Get Bank Statement Matcher. Algorithm to match Bank Statement Info to
	 * Business Partners, Invoices and Payments
	 */
	public int getC_BankStatementMatcher_ID() {
		Integer ii = (Integer) get_Value("C_BankStatementMatcher_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Classname. Java Classname
	 */
	public void setClassname(String Classname) {
		if (Classname == null)
			throw new IllegalArgumentException("Classname is mandatory.");
		if (Classname.length() > 60) {
			log.warning("Length > 60 - truncated");
			Classname = Classname.substring(0, 59);
		}
		set_Value("Classname", Classname);
	}

	/**
	 * Get Classname. Java Classname
	 */
	public String getClassname() {
		return (String) get_Value("Classname");
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
	 * Set Sequence. Method of ordering records; lowest number comes first
	 */
	public void setSeqNo(int SeqNo) {
		set_Value("SeqNo", new Integer(SeqNo));
	}

	/**
	 * Get Sequence. Method of ordering records; lowest number comes first
	 */
	public int getSeqNo() {
		Integer ii = (Integer) get_Value("SeqNo");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
