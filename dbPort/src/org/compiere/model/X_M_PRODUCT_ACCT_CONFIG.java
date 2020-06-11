/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for M_PRODUCT_ACCT_CONFIG
 *  @author Jorg Janke (generated) 
 *  @version Release 3.3.0.0 - 2020-06-03 14:19:26.846 */
public class X_M_PRODUCT_ACCT_CONFIG extends PO
{
/** Standard Constructor */
public X_M_PRODUCT_ACCT_CONFIG (Properties ctx, int M_PRODUCT_ACCT_CONFIG_ID, String trxName)
{
super (ctx, M_PRODUCT_ACCT_CONFIG_ID, trxName);
/** if (M_PRODUCT_ACCT_CONFIG_ID == 0)
{
setC_AcctSchema_ID (0);
setM_PRODUCT_ACCT_CONFIG_ID (0);
setP_Asset_Acct (0);
setP_COGS_Acct (0);
setP_Expense_Acct (0);
setP_InvoicePriceVariance_Acct (0);
setP_PurchasePriceVariance_Acct (0);
setP_Revenue_Acct (0);
setP_TradeDiscountGrant_Acct (0);
setP_TradeDiscountRec_Acct (0);
setValue (null);
}
 */
}
/** Load Constructor */
public X_M_PRODUCT_ACCT_CONFIG (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000078 */
public static final int Table_ID=5000078;

/** TableName=M_PRODUCT_ACCT_CONFIG */
public static final String Table_Name="M_PRODUCT_ACCT_CONFIG";

protected static KeyNamePair Model = new KeyNamePair(5000078,"M_PRODUCT_ACCT_CONFIG");

protected BigDecimal accessLevel = new BigDecimal(3);
/** AccessLevel 3 - Client - Org  */
protected int get_AccessLevel()
{
return accessLevel.intValue();
}
/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_M_PRODUCT_ACCT_CONFIG[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Accounting Schema.
Rules for accounting */
public void setC_AcctSchema_ID (int C_AcctSchema_ID)
{
if (C_AcctSchema_ID < 1) throw new IllegalArgumentException ("C_AcctSchema_ID is mandatory.");
set_Value ("C_AcctSchema_ID", new Integer(C_AcctSchema_ID));
}
/** Get Accounting Schema.
Rules for accounting */
public int getC_AcctSchema_ID() 
{
Integer ii = (Integer)get_Value("C_AcctSchema_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Description.
Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 255)
{
log.warning("Length > 255 - truncated");
Description = Description.substring(0,254);
}
set_Value ("Description", Description);
}
/** Get Description.
Optional short description of the record */
public String getDescription() 
{
return (String)get_Value("Description");
}
/** Set M_PRODUCT_ACCT_CONFIG_ID */
public void setM_PRODUCT_ACCT_CONFIG_ID (int M_PRODUCT_ACCT_CONFIG_ID)
{
if (M_PRODUCT_ACCT_CONFIG_ID < 1) throw new IllegalArgumentException ("M_PRODUCT_ACCT_CONFIG_ID is mandatory.");
set_ValueNoCheck ("M_PRODUCT_ACCT_CONFIG_ID", new Integer(M_PRODUCT_ACCT_CONFIG_ID));
}
/** Get M_PRODUCT_ACCT_CONFIG_ID */
public int getM_PRODUCT_ACCT_CONFIG_ID() 
{
Integer ii = (Integer)get_Value("M_PRODUCT_ACCT_CONFIG_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Product Asset.
Account for Product Asset (Inventory) */
public void setP_Asset_Acct (int P_Asset_Acct)
{
set_Value ("P_Asset_Acct", new Integer(P_Asset_Acct));
}
/** Get Product Asset.
Account for Product Asset (Inventory) */
public int getP_Asset_Acct() 
{
Integer ii = (Integer)get_Value("P_Asset_Acct");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Product COGS.
Account for Cost of Goods Sold */
public void setP_COGS_Acct (int P_COGS_Acct)
{
set_Value ("P_COGS_Acct", new Integer(P_COGS_Acct));
}
/** Get Product COGS.
Account for Cost of Goods Sold */
public int getP_COGS_Acct() 
{
Integer ii = (Integer)get_Value("P_COGS_Acct");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Cost Adjustment.
Product Cost Adjustment Account */
public void setP_CostAdjustment_Acct (int P_CostAdjustment_Acct)
{
set_Value ("P_CostAdjustment_Acct", new Integer(P_CostAdjustment_Acct));
}
/** Get Cost Adjustment.
Product Cost Adjustment Account */
public int getP_CostAdjustment_Acct() 
{
Integer ii = (Integer)get_Value("P_CostAdjustment_Acct");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Product Expense.
Account for Product Expense */
public void setP_Expense_Acct (int P_Expense_Acct)
{
set_Value ("P_Expense_Acct", new Integer(P_Expense_Acct));
}
/** Get Product Expense.
Account for Product Expense */
public int getP_Expense_Acct() 
{
Integer ii = (Integer)get_Value("P_Expense_Acct");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Inventory Clearing.
Product Inventory Clearing Account */
public void setP_InventoryClearing_Acct (int P_InventoryClearing_Acct)
{
set_Value ("P_InventoryClearing_Acct", new Integer(P_InventoryClearing_Acct));
}
/** Get Inventory Clearing.
Product Inventory Clearing Account */
public int getP_InventoryClearing_Acct() 
{
Integer ii = (Integer)get_Value("P_InventoryClearing_Acct");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Invoice Price Variance.
Difference between Costs and Invoice Price (IPV) */
public void setP_InvoicePriceVariance_Acct (int P_InvoicePriceVariance_Acct)
{
set_Value ("P_InvoicePriceVariance_Acct", new Integer(P_InvoicePriceVariance_Acct));
}
/** Get Invoice Price Variance.
Difference between Costs and Invoice Price (IPV) */
public int getP_InvoicePriceVariance_Acct() 
{
Integer ii = (Integer)get_Value("P_InvoicePriceVariance_Acct");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Purchase Price Variance.
Difference between Standard Cost and Purchase Price (PPV) */
public void setP_PurchasePriceVariance_Acct (int P_PurchasePriceVariance_Acct)
{
set_Value ("P_PurchasePriceVariance_Acct", new Integer(P_PurchasePriceVariance_Acct));
}
/** Get Purchase Price Variance.
Difference between Standard Cost and Purchase Price (PPV) */
public int getP_PurchasePriceVariance_Acct() 
{
Integer ii = (Integer)get_Value("P_PurchasePriceVariance_Acct");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Product Revenue.
Account for Product Revenue (Sales Account) */
public void setP_Revenue_Acct (int P_Revenue_Acct)
{
set_Value ("P_Revenue_Acct", new Integer(P_Revenue_Acct));
}
/** Get Product Revenue.
Account for Product Revenue (Sales Account) */
public int getP_Revenue_Acct() 
{
Integer ii = (Integer)get_Value("P_Revenue_Acct");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Trade Discount Granted.
Trade Discount Granted Account */
public void setP_TradeDiscountGrant_Acct (int P_TradeDiscountGrant_Acct)
{
set_Value ("P_TradeDiscountGrant_Acct", new Integer(P_TradeDiscountGrant_Acct));
}
/** Get Trade Discount Granted.
Trade Discount Granted Account */
public int getP_TradeDiscountGrant_Acct() 
{
Integer ii = (Integer)get_Value("P_TradeDiscountGrant_Acct");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Trade Discount Received.
Trade Discount Receivable Account */
public void setP_TradeDiscountRec_Acct (int P_TradeDiscountRec_Acct)
{
set_Value ("P_TradeDiscountRec_Acct", new Integer(P_TradeDiscountRec_Acct));
}
/** Get Trade Discount Received.
Trade Discount Receivable Account */
public int getP_TradeDiscountRec_Acct() 
{
Integer ii = (Integer)get_Value("P_TradeDiscountRec_Acct");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Search Key.
Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
if (Value.length() > 40)
{
log.warning("Length > 40 - truncated");
Value = Value.substring(0,39);
}
set_Value ("Value", Value);
}
/** Get Search Key.
Search key for the record in the format required - must be unique */
public String getValue() 
{
return (String)get_Value("Value");
}
}
