/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_Product_Category_Acct
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.468
 */
public class X_M_Product_Category_Acct extends PO {
	/** Standard Constructor */
	public X_M_Product_Category_Acct(Properties ctx,
			int M_Product_Category_Acct_ID, String trxName) {
		super(ctx, M_Product_Category_Acct_ID, trxName);
		/**
		 * if (M_Product_Category_Acct_ID == 0) { setC_AcctSchema_ID (0);
		 * setM_Product_Category_ID (0); setP_Asset_Acct (0); setP_COGS_Acct
		 * (0); setP_CostAdjustment_Acct (0); setP_Expense_Acct (0);
		 * setP_InventoryClearing_Acct (0); setP_InvoicePriceVariance_Acct (0);
		 * setP_PurchasePriceVariance_Acct (0); setP_Revenue_Acct (0);
		 * setP_TradeDiscountGrant_Acct (0); setP_TradeDiscountRec_Acct (0); }
		 */
	}

	/** Load Constructor */
	public X_M_Product_Category_Acct(Properties ctx, ResultSet rs,
			String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_Product_Category_Acct */
	public static final String Table_Name = "M_Product_Category_Acct";

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
		StringBuffer sb = new StringBuffer("X_M_Product_Category_Acct[")
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

	/** CostingLevel AD_Reference_ID=355 */
	public static final int COSTINGLEVEL_AD_Reference_ID = 355;

	/** Batch/Lot = B */
	public static final String COSTINGLEVEL_BatchLot = "B";

	/** Client = C */
	public static final String COSTINGLEVEL_Client = "C";

	/** Organization = O */
	public static final String COSTINGLEVEL_Organization = "O";

	/**
	 * Set Costing Level. The lowest level to accumulate Costing Information
	 */
	public void setCostingLevel(String CostingLevel) {
		if (CostingLevel != null && CostingLevel.length() > 1) {
			log.warning("Length > 1 - truncated");
			CostingLevel = CostingLevel.substring(0, 0);
		}
		set_Value("CostingLevel", CostingLevel);
	}

	/**
	 * Get Costing Level. The lowest level to accumulate Costing Information
	 */
	public String getCostingLevel() {
		return (String) get_Value("CostingLevel");
	}

	/** CostingMethod AD_Reference_ID=122 */
	public static final int COSTINGMETHOD_AD_Reference_ID = 122;

	/** Average PO = A */
	public static final String COSTINGMETHOD_AveragePO = "A";

	/** Fifo = F */
	public static final String COSTINGMETHOD_Fifo = "F";

	/** Average Invoice = I */
	public static final String COSTINGMETHOD_AverageInvoice = "I";

	/** Lifo = L */
	public static final String COSTINGMETHOD_Lifo = "L";

	/** Standard Costing = S */
	public static final String COSTINGMETHOD_StandardCosting = "S";

	/** User Defined = U */
	public static final String COSTINGMETHOD_UserDefined = "U";

	/** Last Invoice = i */
	public static final String COSTINGMETHOD_LastInvoice = "i";

	/** Last PO Price = p */
	public static final String COSTINGMETHOD_LastPOPrice = "p";

	/** _ = x */
	public static final String COSTINGMETHOD__ = "x";

	/**
	 * Set Costing Method. Indicates how Costs will be calculated
	 */
	public void setCostingMethod(String CostingMethod) {
		if (CostingMethod != null && CostingMethod.length() > 1) {
			log.warning("Length > 1 - truncated");
			CostingMethod = CostingMethod.substring(0, 0);
		}
		set_Value("CostingMethod", CostingMethod);
	}

	/**
	 * Get Costing Method. Indicates how Costs will be calculated
	 */
	public String getCostingMethod() {
		return (String) get_Value("CostingMethod");
	}

	/**
	 * Set Product Category. Category of a Product
	 */
	public void setM_Product_Category_ID(int M_Product_Category_ID) {
		if (M_Product_Category_ID < 1)
			throw new IllegalArgumentException(
					"M_Product_Category_ID is mandatory.");
		set_ValueNoCheck("M_Product_Category_ID", new Integer(
				M_Product_Category_ID));
	}

	/**
	 * Get Product Category. Category of a Product
	 */
	public int getM_Product_Category_ID() {
		Integer ii = (Integer) get_Value("M_Product_Category_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Product Asset. Account for Product Asset (Inventory)
	 */
	public void setP_Asset_Acct(int P_Asset_Acct) {
		set_Value("P_Asset_Acct", new Integer(P_Asset_Acct));
	}

	/**
	 * Get Product Asset. Account for Product Asset (Inventory)
	 */
	public int getP_Asset_Acct() {
		Integer ii = (Integer) get_Value("P_Asset_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Product COGS. Account for Cost of Goods Sold
	 */
	public void setP_COGS_Acct(int P_COGS_Acct) {
		set_Value("P_COGS_Acct", new Integer(P_COGS_Acct));
	}

	/**
	 * Get Product COGS. Account for Cost of Goods Sold
	 */
	public int getP_COGS_Acct() {
		Integer ii = (Integer) get_Value("P_COGS_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Cost Adjustment. Product Cost Adjustment Account
	 */
	public void setP_CostAdjustment_Acct(int P_CostAdjustment_Acct) {
		set_Value("P_CostAdjustment_Acct", new Integer(P_CostAdjustment_Acct));
	}

	/**
	 * Get Cost Adjustment. Product Cost Adjustment Account
	 */
	public int getP_CostAdjustment_Acct() {
		Integer ii = (Integer) get_Value("P_CostAdjustment_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Product Expense. Account for Product Expense
	 */
	public void setP_Expense_Acct(int P_Expense_Acct) {
		set_Value("P_Expense_Acct", new Integer(P_Expense_Acct));
	}

	/**
	 * Get Product Expense. Account for Product Expense
	 */
	public int getP_Expense_Acct() {
		Integer ii = (Integer) get_Value("P_Expense_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Inventory Clearing. Product Inventory Clearing Account
	 */
	public void setP_InventoryClearing_Acct(int P_InventoryClearing_Acct) {
		set_Value("P_InventoryClearing_Acct", new Integer(
				P_InventoryClearing_Acct));
	}

	/**
	 * Get Inventory Clearing. Product Inventory Clearing Account
	 */
	public int getP_InventoryClearing_Acct() {
		Integer ii = (Integer) get_Value("P_InventoryClearing_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Invoice Price Variance. Difference between Costs and Invoice Price
	 * (IPV)
	 */
	public void setP_InvoicePriceVariance_Acct(int P_InvoicePriceVariance_Acct) {
		set_Value("P_InvoicePriceVariance_Acct", new Integer(
				P_InvoicePriceVariance_Acct));
	}

	/**
	 * Get Invoice Price Variance. Difference between Costs and Invoice Price
	 * (IPV)
	 */
	public int getP_InvoicePriceVariance_Acct() {
		Integer ii = (Integer) get_Value("P_InvoicePriceVariance_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Purchase Price Variance. Difference between Standard Cost and
	 * Purchase Price (PPV)
	 */
	public void setP_PurchasePriceVariance_Acct(int P_PurchasePriceVariance_Acct) {
		set_Value("P_PurchasePriceVariance_Acct", new Integer(
				P_PurchasePriceVariance_Acct));
	}

	/**
	 * Get Purchase Price Variance. Difference between Standard Cost and
	 * Purchase Price (PPV)
	 */
	public int getP_PurchasePriceVariance_Acct() {
		Integer ii = (Integer) get_Value("P_PurchasePriceVariance_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Product Revenue. Account for Product Revenue (Sales Account)
	 */
	public void setP_Revenue_Acct(int P_Revenue_Acct) {
		set_Value("P_Revenue_Acct", new Integer(P_Revenue_Acct));
	}

	/**
	 * Get Product Revenue. Account for Product Revenue (Sales Account)
	 */
	public int getP_Revenue_Acct() {
		Integer ii = (Integer) get_Value("P_Revenue_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Trade Discount Granted. Trade Discount Granted Account
	 */
	public void setP_TradeDiscountGrant_Acct(int P_TradeDiscountGrant_Acct) {
		set_Value("P_TradeDiscountGrant_Acct", new Integer(
				P_TradeDiscountGrant_Acct));
	}

	/**
	 * Get Trade Discount Granted. Trade Discount Granted Account
	 */
	public int getP_TradeDiscountGrant_Acct() {
		Integer ii = (Integer) get_Value("P_TradeDiscountGrant_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Trade Discount Received. Trade Discount Receivable Account
	 */
	public void setP_TradeDiscountRec_Acct(int P_TradeDiscountRec_Acct) {
		set_Value("P_TradeDiscountRec_Acct", new Integer(
				P_TradeDiscountRec_Acct));
	}

	/**
	 * Get Trade Discount Received. Trade Discount Receivable Account
	 */
	public int getP_TradeDiscountRec_Acct() {
		Integer ii = (Integer) get_Value("P_TradeDiscountRec_Acct");
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
}
