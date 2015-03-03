/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for ZYN_MOVEMENT_WAREHOUSE
 *  @author Jorg Janke (generated) 
 *  @version Release 3.3.0.0 - 2013-11-27 14:44:18.314 */
public class X_ZYN_MOVEMENT_WAREHOUSE extends PO
{
/** Standard Constructor */
public X_ZYN_MOVEMENT_WAREHOUSE (Properties ctx, int ZYN_MOVEMENT_WAREHOUSE_ID, String trxName)
{
super (ctx, ZYN_MOVEMENT_WAREHOUSE_ID, trxName);
/** if (ZYN_MOVEMENT_WAREHOUSE_ID == 0)
{
setC_DocType_ID (0);
setM_WAREHOUSEFROM_ID (0);
setM_WAREHOUSETO_ID (0);
setZYN_MOVEMENT_WAREHOUSE_ID (0);
}
 */
}
/** Load Constructor */
public X_ZYN_MOVEMENT_WAREHOUSE (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000055 */
public static int Table_ID=5000055;

/** TableName=ZYN_MOVEMENT_WAREHOUSE */
public static final String Table_Name="ZYN_MOVEMENT_WAREHOUSE";

protected static KeyNamePair Model = new KeyNamePair(5000055,"ZYN_MOVEMENT_WAREHOUSE");

protected BigDecimal accessLevel = new BigDecimal(3);
/** AccessLevel 3 - Client - Org  */
protected int get_AccessLevel()
{
return accessLevel.intValue();
}
/** Load Meta Data */
protected POInfo initPO(Properties ctx) {
        POInfo info = initPO(ctx, Table_Name);
        Table_ID = info.getAD_Table_ID();
        Model = new KeyNamePair(Table_ID, Table_Name);
        return info;
}

public String toString()
{
StringBuffer sb = new StringBuffer ("X_ZYN_MOVEMENT_WAREHOUSE[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Document Type.
Document type or rules */
public void setC_DocType_ID (int C_DocType_ID)
{
if (C_DocType_ID < 0) throw new IllegalArgumentException ("C_DocType_ID is mandatory.");
set_Value ("C_DocType_ID", new Integer(C_DocType_ID));
}
/** Get Document Type.
Document type or rules */
public int getC_DocType_ID() 
{
Integer ii = (Integer)get_Value("C_DocType_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set M_WAREHOUSEFROM_ID */
public void setM_WAREHOUSEFROM_ID (int M_WAREHOUSEFROM_ID)
{
if (M_WAREHOUSEFROM_ID < 1) throw new IllegalArgumentException ("M_WAREHOUSEFROM_ID is mandatory.");
set_Value ("M_WAREHOUSEFROM_ID", new Integer(M_WAREHOUSEFROM_ID));
}
/** Get M_WAREHOUSEFROM_ID */
public int getM_WAREHOUSEFROM_ID() 
{
Integer ii = (Integer)get_Value("M_WAREHOUSEFROM_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set M_WAREHOUSETO_ID */
public void setM_WAREHOUSETO_ID (int M_WAREHOUSETO_ID)
{
if (M_WAREHOUSETO_ID < 1) throw new IllegalArgumentException ("M_WAREHOUSETO_ID is mandatory.");
set_Value ("M_WAREHOUSETO_ID", new Integer(M_WAREHOUSETO_ID));
}
/** Get M_WAREHOUSETO_ID */
public int getM_WAREHOUSETO_ID() 
{
Integer ii = (Integer)get_Value("M_WAREHOUSETO_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set ZYN_MOVEMENT_WAREHOUSE_ID */
public void setZYN_MOVEMENT_WAREHOUSE_ID (int ZYN_MOVEMENT_WAREHOUSE_ID)
{
if (ZYN_MOVEMENT_WAREHOUSE_ID < 1) throw new IllegalArgumentException ("ZYN_MOVEMENT_WAREHOUSE_ID is mandatory.");
set_ValueNoCheck ("ZYN_MOVEMENT_WAREHOUSE_ID", new Integer(ZYN_MOVEMENT_WAREHOUSE_ID));
}
/** Get ZYN_MOVEMENT_WAREHOUSE_ID */
public int getZYN_MOVEMENT_WAREHOUSE_ID() 
{
Integer ii = (Integer)get_Value("ZYN_MOVEMENT_WAREHOUSE_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
