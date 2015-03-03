/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_BP_Group_Acct
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.687
 */
public class X_C_BP_Group_Acct extends PO {
	/** Standard Constructor */
	public X_C_BP_Group_Acct(Properties ctx, int C_BP_Group_Acct_ID,
			String trxName) {
		super(ctx, C_BP_Group_Acct_ID, trxName);
		/**
		 * if (C_BP_Group_Acct_ID == 0) { setC_AcctSchema_ID (0);
		 * setC_BP_Group_ID (0); setC_Prepayment_Acct (0); setC_Receivable_Acct
		 * (0); setC_Receivable_Services_Acct (0); setNotInvoicedReceipts_Acct
		 * (0); setNotInvoicedReceivables_Acct (0); setNotInvoicedRevenue_Acct
		 * (0); setPayDiscount_Exp_Acct (0); setPayDiscount_Rev_Acct (0);
		 * setUnEarnedRevenue_Acct (0); setV_Liability_Acct (0);
		 * setV_Liability_Services_Acct (0); setV_Prepayment_Acct (0);
		 * setWriteOff_Acct (0); }
		 */
	}

	/** Load Constructor */
	public X_C_BP_Group_Acct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_BP_Group_Acct */
	public static final String Table_Name = "C_BP_Group_Acct";

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
		StringBuffer sb = new StringBuffer("X_C_BP_Group_Acct[").append(
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
	 * Set Business Partner Group. Business Partner Group
	 */
	public void setC_BP_Group_ID(int C_BP_Group_ID) {
		if (C_BP_Group_ID < 1)
			throw new IllegalArgumentException("C_BP_Group_ID is mandatory.");
		set_ValueNoCheck("C_BP_Group_ID", new Integer(C_BP_Group_ID));
	}

	/**
	 * Get Business Partner Group. Business Partner Group
	 */
	public int getC_BP_Group_ID() {
		Integer ii = (Integer) get_Value("C_BP_Group_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getC_BP_Group_ID()));
	}

	/**
	 * Set Customer Prepayment. Account for customer prepayments
	 */
	public void setC_Prepayment_Acct(int C_Prepayment_Acct) {
		set_Value("C_Prepayment_Acct", new Integer(C_Prepayment_Acct));
	}

	/**
	 * Get Customer Prepayment. Account for customer prepayments
	 */
	public int getC_Prepayment_Acct() {
		Integer ii = (Integer) get_Value("C_Prepayment_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Customer Receivables. Account for Customer Receivables
	 */
	public void setC_Receivable_Acct(int C_Receivable_Acct) {
		set_Value("C_Receivable_Acct", new Integer(C_Receivable_Acct));
	}

	/**
	 * Get Customer Receivables. Account for Customer Receivables
	 */
	public int getC_Receivable_Acct() {
		Integer ii = (Integer) get_Value("C_Receivable_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Receivable Services. Customer Accounts Receivables Services Account
	 */
	public void setC_Receivable_Services_Acct(int C_Receivable_Services_Acct) {
		set_Value("C_Receivable_Services_Acct", new Integer(
				C_Receivable_Services_Acct));
	}

	/**
	 * Get Receivable Services. Customer Accounts Receivables Services Account
	 */
	public int getC_Receivable_Services_Acct() {
		Integer ii = (Integer) get_Value("C_Receivable_Services_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Not-invoiced Receipts. Account for not-invoiced Material Receipts
	 */
	public void setNotInvoicedReceipts_Acct(int NotInvoicedReceipts_Acct) {
		set_Value("NotInvoicedReceipts_Acct", new Integer(
				NotInvoicedReceipts_Acct));
	}

	/**
	 * Get Not-invoiced Receipts. Account for not-invoiced Material Receipts
	 */
	public int getNotInvoicedReceipts_Acct() {
		Integer ii = (Integer) get_Value("NotInvoicedReceipts_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Not-invoiced Receivables. Account for not invoiced Receivables
	 */
	public void setNotInvoicedReceivables_Acct(int NotInvoicedReceivables_Acct) {
		set_Value("NotInvoicedReceivables_Acct", new Integer(
				NotInvoicedReceivables_Acct));
	}

	/**
	 * Get Not-invoiced Receivables. Account for not invoiced Receivables
	 */
	public int getNotInvoicedReceivables_Acct() {
		Integer ii = (Integer) get_Value("NotInvoicedReceivables_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Not-invoiced Revenue. Account for not invoiced Revenue
	 */
	public void setNotInvoicedRevenue_Acct(int NotInvoicedRevenue_Acct) {
		set_Value("NotInvoicedRevenue_Acct", new Integer(
				NotInvoicedRevenue_Acct));
	}

	/**
	 * Get Not-invoiced Revenue. Account for not invoiced Revenue
	 */
	public int getNotInvoicedRevenue_Acct() {
		Integer ii = (Integer) get_Value("NotInvoicedRevenue_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Payment Discount Expense. Payment Discount Expense Account
	 */
	public void setPayDiscount_Exp_Acct(int PayDiscount_Exp_Acct) {
		set_Value("PayDiscount_Exp_Acct", new Integer(PayDiscount_Exp_Acct));
	}

	/**
	 * Get Payment Discount Expense. Payment Discount Expense Account
	 */
	public int getPayDiscount_Exp_Acct() {
		Integer ii = (Integer) get_Value("PayDiscount_Exp_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Payment Discount Revenue. Payment Discount Revenue Account
	 */
	public void setPayDiscount_Rev_Acct(int PayDiscount_Rev_Acct) {
		set_Value("PayDiscount_Rev_Acct", new Integer(PayDiscount_Rev_Acct));
	}

	/**
	 * Get Payment Discount Revenue. Payment Discount Revenue Account
	 */
	public int getPayDiscount_Rev_Acct() {
		Integer ii = (Integer) get_Value("PayDiscount_Rev_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Process Now */
	public void setProcessing(boolean Processing) {
		set_Value("Processing", new Boolean(Processing));
	}

	/** Get Process Now */
	public boolean isProcessing() {
		Object oo = get_Value("Processing");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Unearned Revenue. Account for unearned revenue
	 */
	public void setUnEarnedRevenue_Acct(int UnEarnedRevenue_Acct) {
		set_Value("UnEarnedRevenue_Acct", new Integer(UnEarnedRevenue_Acct));
	}

	/**
	 * Get Unearned Revenue. Account for unearned revenue
	 */
	public int getUnEarnedRevenue_Acct() {
		Integer ii = (Integer) get_Value("UnEarnedRevenue_Acct");
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

	/**
	 * Set Write-off. Account for Receivables write-off
	 */
	public void setWriteOff_Acct(int WriteOff_Acct) {
		set_Value("WriteOff_Acct", new Integer(WriteOff_Acct));
	}

	/**
	 * Get Write-off. Account for Receivables write-off
	 */
	public int getWriteOff_Acct() {
		Integer ii = (Integer) get_Value("WriteOff_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
