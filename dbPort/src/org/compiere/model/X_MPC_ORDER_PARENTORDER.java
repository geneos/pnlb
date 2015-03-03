/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for MPC_ORDER_PARENTORDER
 *  @author Jorg Janke (generated) 
 *  @version Release 3.3.0.0 - 2013-07-16 15:26:51.208 */
public class X_MPC_ORDER_PARENTORDER extends PO
{
/** Standard Constructor */
public X_MPC_ORDER_PARENTORDER (Properties ctx, int MPC_ORDER_PARENTORDER_ID, String trxName)
{
super (ctx, MPC_ORDER_PARENTORDER_ID, trxName);
/** if (MPC_ORDER_PARENTORDER_ID == 0)
{
setMPC_ORDER_PARENTORDER_ID (0);
setMPC_Order_ID (0);
setMPC_PARENTORDER_ID (0);
}
 */
}
/** Load Constructor */
public X_MPC_ORDER_PARENTORDER (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000047 */
public static final int Table_ID=5000047;

/** TableName=MPC_ORDER_PARENTORDER */
public static final String Table_Name="MPC_ORDER_PARENTORDER";

protected static KeyNamePair Model = new KeyNamePair(5000047,"MPC_ORDER_PARENTORDER");

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
StringBuffer sb = new StringBuffer ("X_MPC_ORDER_PARENTORDER[").append(get_ID()).append("]");
return sb.toString();
}
/** Set MPC_ORDER_PARENTORDER_ID */
public void setMPC_ORDER_PARENTORDER_ID (int MPC_ORDER_PARENTORDER_ID)
{
if (MPC_ORDER_PARENTORDER_ID < 1) throw new IllegalArgumentException ("MPC_ORDER_PARENTORDER_ID is mandatory.");
set_ValueNoCheck ("MPC_ORDER_PARENTORDER_ID", new Integer(MPC_ORDER_PARENTORDER_ID));
}
/** Get MPC_ORDER_PARENTORDER_ID */
public int getMPC_ORDER_PARENTORDER_ID() 
{
Integer ii = (Integer)get_Value("MPC_ORDER_PARENTORDER_ID");
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
/** Set MPC_PARENTORDER_ID */
public void setMPC_PARENTORDER_ID (int MPC_PARENTORDER_ID)
{
if (MPC_PARENTORDER_ID < 1) throw new IllegalArgumentException ("MPC_PARENTORDER_ID is mandatory.");
set_Value ("MPC_PARENTORDER_ID", new Integer(MPC_PARENTORDER_ID));
}
/** Get MPC_PARENTORDER_ID */
public int getMPC_PARENTORDER_ID() 
{
Integer ii = (Integer)get_Value("MPC_PARENTORDER_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
