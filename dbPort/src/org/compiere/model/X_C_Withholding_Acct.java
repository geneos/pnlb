/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Withholding_Acct
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.89
 */
public class X_C_Withholding_Acct extends PO {
	/** Standard Constructor */
	public X_C_Withholding_Acct(Properties ctx, int C_Withholding_Acct_ID,
			String trxName) {
		super(ctx, C_Withholding_Acct_ID, trxName);
		/**
		 * if (C_Withholding_Acct_ID == 0) { setC_AcctSchema_ID (0);
		 * setC_Withholding_ID (0); setWithholding_Acct (0); }
		 */
	}

	/** Load Constructor */
	public X_C_Withholding_Acct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Withholding_Acct */
	public static final String Table_Name = "C_Withholding_Acct";

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
		StringBuffer sb = new StringBuffer("X_C_Withholding_Acct[").append(
				get_ID()).append("]");
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
	 * Set Withholding. Withholding type defined
	 */
	public void setC_Withholding_ID(int C_Withholding_ID) {
		if (C_Withholding_ID < 1)
			throw new IllegalArgumentException("C_Withholding_ID is mandatory.");
		set_ValueNoCheck("C_Withholding_ID", new Integer(C_Withholding_ID));
	}

	/**
	 * Get Withholding. Withholding type defined
	 */
	public int getC_Withholding_ID() {
		Integer ii = (Integer) get_Value("C_Withholding_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Withholding. Account for Withholdings
	 */
	public void setWithholding_Acct(int Withholding_Acct) {
		set_Value("Withholding_Acct", new Integer(Withholding_Acct));
	}

	/**
	 * Get Withholding. Account for Withholdings
	 */
	public int getWithholding_Acct() {
		Integer ii = (Integer) get_Value("Withholding_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
