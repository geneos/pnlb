/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_InterOrg_Acct
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.437
 */
public class X_C_InterOrg_Acct extends PO {
	/** Standard Constructor */
	public X_C_InterOrg_Acct(Properties ctx, int C_InterOrg_Acct_ID,
			String trxName) {
		super(ctx, C_InterOrg_Acct_ID, trxName);
		/**
		 * if (C_InterOrg_Acct_ID == 0) { setAD_OrgTo_ID (0); setC_AcctSchema_ID
		 * (0); setIntercompanyDueFrom_Acct (0); setIntercompanyDueTo_Acct (0); }
		 */
	}

	/** Load Constructor */
	public X_C_InterOrg_Acct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_InterOrg_Acct */
	public static final String Table_Name = "C_InterOrg_Acct";

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
		StringBuffer sb = new StringBuffer("X_C_InterOrg_Acct[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/** AD_OrgTo_ID AD_Reference_ID=130 */
	public static final int AD_ORGTO_ID_AD_Reference_ID = 130;

	/**
	 * Set Inter-Organization. Organization valid for intercompany documents
	 */
	public void setAD_OrgTo_ID(int AD_OrgTo_ID) {
		if (AD_OrgTo_ID < 1)
			throw new IllegalArgumentException("AD_OrgTo_ID is mandatory.");
		set_ValueNoCheck("AD_OrgTo_ID", new Integer(AD_OrgTo_ID));
	}

	/**
	 * Get Inter-Organization. Organization valid for intercompany documents
	 */
	public int getAD_OrgTo_ID() {
		Integer ii = (Integer) get_Value("AD_OrgTo_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
	 * Set Intercompany Due From Acct. Intercompany Due From / Receivables
	 * Account
	 */
	public void setIntercompanyDueFrom_Acct(int IntercompanyDueFrom_Acct) {
		set_Value("IntercompanyDueFrom_Acct", new Integer(
				IntercompanyDueFrom_Acct));
	}

	/**
	 * Get Intercompany Due From Acct. Intercompany Due From / Receivables
	 * Account
	 */
	public int getIntercompanyDueFrom_Acct() {
		Integer ii = (Integer) get_Value("IntercompanyDueFrom_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Intercompany Due To Acct. Intercompany Due To / Payable Account
	 */
	public void setIntercompanyDueTo_Acct(int IntercompanyDueTo_Acct) {
		set_Value("IntercompanyDueTo_Acct", new Integer(IntercompanyDueTo_Acct));
	}

	/**
	 * Get Intercompany Due To Acct. Intercompany Due To / Payable Account
	 */
	public int getIntercompanyDueTo_Acct() {
		Integer ii = (Integer) get_Value("IntercompanyDueTo_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
