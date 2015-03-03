/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_BP_Vendor_Acct
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.718
 */
public class X_C_BP_Vendor_Acct extends PO {
	/** Standard Constructor */
	public X_C_BP_Vendor_Acct(Properties ctx, int C_BP_Vendor_Acct_ID,
			String trxName) {
		super(ctx, C_BP_Vendor_Acct_ID, trxName);
		/**
		 * if (C_BP_Vendor_Acct_ID == 0) { setC_AcctSchema_ID (0);
		 * setC_BPartner_ID (0); setV_Liability_Acct (0); setV_Prepayment_Acct
		 * (0); }
		 */
	}

	/** Load Constructor */
	public X_C_BP_Vendor_Acct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_BP_Vendor_Acct */
	public static final String Table_Name = "C_BP_Vendor_Acct";

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
		StringBuffer sb = new StringBuffer("X_C_BP_Vendor_Acct[").append(
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
	 * Set Business Partner . Identifies a Business Partner
	 */
	public void setC_BPartner_ID(int C_BPartner_ID) {
		if (C_BPartner_ID < 1)
			throw new IllegalArgumentException("C_BPartner_ID is mandatory.");
		set_ValueNoCheck("C_BPartner_ID", new Integer(C_BPartner_ID));
	}

	/**
	 * Get Business Partner . Identifies a Business Partner
	 */
	public int getC_BPartner_ID() {
		Integer ii = (Integer) get_Value("C_BPartner_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Vendor Liability. Account for Vendor Liability
	 */
	public void setV_Liability_Acct(int V_Liability_Acct) {
		set_Value("V_Liability_Acct", new Integer(V_Liability_Acct));
	}

	/**
	 * Get Vendor Liability. Account for Vendor Liability
	 */
	public int getV_Liability_Acct() {
		Integer ii = (Integer) get_Value("V_Liability_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Vendor Service Liability. Account for Vender Service Liability
	 */
	public void setV_Liability_Services_Acct(int V_Liability_Services_Acct) {
		set_Value("V_Liability_Services_Acct", new Integer(
				V_Liability_Services_Acct));
	}

	/**
	 * Get Vendor Service Liability. Account for Vender Service Liability
	 */
	public int getV_Liability_Services_Acct() {
		Integer ii = (Integer) get_Value("V_Liability_Services_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Vendor Prepayment. Account for Vendor Prepayments
	 */
	public void setV_Prepayment_Acct(int V_Prepayment_Acct) {
		set_Value("V_Prepayment_Acct", new Integer(V_Prepayment_Acct));
	}

	/**
	 * Get Vendor Prepayment. Account for Vendor Prepayments
	 */
	public int getV_Prepayment_Acct() {
		Integer ii = (Integer) get_Value("V_Prepayment_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
