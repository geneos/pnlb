/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for MPC_ORDER_QTYRESERVED
 *  @author Jorg Janke (generated) 
 *  @version Release 3.3.0.0 - 2013-07-27 10:13:57.306 */
public class X_MPC_ORDER_QTYRESERVED extends PO
{
/** Standard Constructor */
public X_MPC_ORDER_QTYRESERVED (Properties ctx, int MPC_ORDER_QTYRESERVED_ID, String trxName)
{
super (ctx, MPC_ORDER_QTYRESERVED_ID, trxName);
/** if (MPC_ORDER_QTYRESERVED_ID == 0)
{
setMPC_Order_BOMLine_ID (0);
setMPC_Order_ID (0);
setM_AttributeSetInstance_ID (0);
setM_Locator_ID (0);
setM_Product_ID (0);
setQty (Env.ZERO);
}
 */
}
/** Load Constructor */
public X_MPC_ORDER_QTYRESERVED (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000049 */
public static final int Table_ID=5000049;

/** TableName=MPC_ORDER_QTYRESERVED */
public static final String Table_Name="MPC_ORDER_QTYRESERVED";

protected static KeyNamePair Model = new KeyNamePair(5000049,"MPC_ORDER_QTYRESERVED");

protected BigDecimal accessLevel = new BigDecimal(3);
/** AccessLevel 3 - Client - Org  */
protected int get_AccessLevel()
{
return accessLevel.intValue();
}
/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = initPO(ctx, Table_Name);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_MPC_ORDER_QTYRESERVED[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Order BOM Line ID */
public void setMPC_Order_BOMLine_ID (int MPC_Order_BOMLine_ID)
{
if (MPC_Order_BOMLine_ID < 1) throw new IllegalArgumentException ("MPC_Order_BOMLine_ID is mandatory.");
set_Value ("MPC_Order_BOMLine_ID", new Integer(MPC_Order_BOMLine_ID));
}
/** Get Order BOM Line ID */
public int getMPC_Order_BOMLine_ID() 
{
Integer ii = (Integer)get_Value("MPC_Order_BOMLine_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Manufacturing Order.
Manufacturing Order */
public void setMPC_Order_ID (int MPC_Order_ID)
{
if (MPC_Order_ID < 1) throw new IllegalArgumentException ("MPC_Order_ID is mandatory.");
set_Value ("MPC_Order_ID", new Integer(MPC_Order_ID));
}
/** Get Manufacturing Order.
Manufacturing Order */
public int getMPC_Order_ID() 
{
Integer ii = (Integer)get_Value("MPC_Order_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Attribute Set Instance.
Product Attribute Set Instance */
public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
{
if (M_AttributeSetInstance_ID < 0) throw new IllegalArgumentException ("M_AttributeSetInstance_ID is mandatory.");
set_Value ("M_AttributeSetInstance_ID", new Integer(M_AttributeSetInstance_ID));
}
/** Get Attribute Set Instance.
Product Attribute Set Instance */
public int getM_AttributeSetInstance_ID() 
{
Integer ii = (Integer)get_Value("M_AttributeSetInstance_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Locator.
Warehouse Locator */
public void setM_Locator_ID (int M_Locator_ID)
{
if (M_Locator_ID < 1) throw new IllegalArgumentException ("M_Locator_ID is mandatory.");
set_Value ("M_Locator_ID", new Integer(M_Locator_ID));
}
/** Get Locator.
Warehouse Locator */
public int getM_Locator_ID() 
{
Integer ii = (Integer)get_Value("M_Locator_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Product.
Product, Service, Item */
public void setM_Product_ID (int M_Product_ID)
{
if (M_Product_ID < 1) throw new IllegalArgumentException ("M_Product_ID is mandatory.");
set_Value ("M_Product_ID", new Integer(M_Product_ID));
}
/** Get Product.
Product, Service, Item */
public int getM_Product_ID() 
{
Integer ii = (Integer)get_Value("M_Product_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Total Quantity.
Total Quantity */
public void setTotalQty (BigDecimal TotalQty)
{
if (TotalQty == null) throw new IllegalArgumentException ("Total qty is mandatory.");
set_Value ("TotalQty", TotalQty);
}
/** Get Total Quantity.
Total Quantity */
public BigDecimal getTotalQty() 
{
BigDecimal bd = (BigDecimal)get_Value("TotalQty");
if (bd == null) return Env.ZERO;
return bd;
}

/** Set Remaining Quantity.
Total Quantity */
public void setRemainingQty (BigDecimal RemainingQty)
{
if (RemainingQty == null) throw new IllegalArgumentException ("RemainingQty is mandatory.");
set_Value ("REMAININGQTY", RemainingQty);
}
/** Get Remaining Quantity.
Total Quantity */
public BigDecimal getRemainingQty() 
{
BigDecimal bd = (BigDecimal)get_Value("REMAININGQTY");
if (bd == null) return Env.ZERO;
return bd;
}

/** Set Supply Order.
Quantity */
public void setUseOrder (BigDecimal UseOrder)
{
if (UseOrder.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException ("Use Order is mandatory.");
set_Value ("USEORDER", UseOrder);
}
/** Get Supply Order.
Quantity */
public BigDecimal getUseOrder() 
{
BigDecimal bd = (BigDecimal)get_Value("USEORDER");
if (bd == null) return Env.ZERO;
return bd;
}
}
