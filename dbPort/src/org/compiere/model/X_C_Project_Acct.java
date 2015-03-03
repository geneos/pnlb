/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Project_Acct
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.328
 */
public class X_C_Project_Acct extends PO {
	/** Standard Constructor */
	public X_C_Project_Acct(Properties ctx, int C_Project_Acct_ID,
			String trxName) {
		super(ctx, C_Project_Acct_ID, trxName);
		/**
		 * if (C_Project_Acct_ID == 0) { setC_AcctSchema_ID (0); setC_Project_ID
		 * (0); setPJ_Asset_Acct (0); setPJ_WIP_Acct (0); }
		 */
	}

	/** Load Constructor */
	public X_C_Project_Acct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Project_Acct */
	public static final String Table_Name = "C_Project_Acct";

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
		StringBuffer sb = new StringBuffer("X_C_Project_Acct[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Accounting Schema. Rules for accounting
	 */
	public void setC_AcctSchema_ID(int C_AcctSchema_ID) {
		if (C_AcctSchema_ID < 1)
			throw new IllegalArgumentException("C_AcctSchema_ID is mandatory.");
		set_ValueNoCheck("C_AcctSchema_ID", new Integer(C_AcctSchema_ID));
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
	 * Set Project. Financial Project
	 */
	public void setC_Project_ID(int C_Project_ID) {
		if (C_Project_ID < 1)
			throw new IllegalArgumentException("C_Project_ID is mandatory.");
		set_ValueNoCheck("C_Project_ID", new Integer(C_Project_ID));
	}

	/**
	 * Get Project. Financial Project
	 */
	public int getC_Project_ID() {
		Integer ii = (Integer) get_Value("C_Project_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Project Asset. Project Asset Account
	 */
	public void setPJ_Asset_Acct(int PJ_Asset_Acct) {
		set_Value("PJ_Asset_Acct", new Integer(PJ_Asset_Acct));
	}

	/**
	 * Get Project Asset. Project Asset Account
	 */
	public int getPJ_Asset_Acct() {
		Integer ii = (Integer) get_Value("PJ_Asset_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Work In Progress. Account for Work in Progress
	 */
	public void setPJ_WIP_Acct(int PJ_WIP_Acct) {
		set_Value("PJ_WIP_Acct", new Integer(PJ_WIP_Acct));
	}

	/**
	 * Get Work In Progress. Account for Work in Progress
	 */
	public int getPJ_WIP_Acct() {
		Integer ii = (Integer) get_Value("PJ_WIP_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
