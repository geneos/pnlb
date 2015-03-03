/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for ZYN_TABLE_ROW
 *  @author Jorg Janke (generated) 
 *  @version Release 2.5.3b - 2010-08-24 22:35:24.515 */
public class X_ZYN_TABLE_ROW extends PO
{
/** Standard Constructor */
public X_ZYN_TABLE_ROW (Properties ctx, int ZYN_TABLE_ROW_ID, String trxName)
{
super (ctx, ZYN_TABLE_ROW_ID, trxName);
/** if (ZYN_TABLE_ROW_ID == 0)
{
setAD_Column_ID (0);
setZYN_TABLE_MODEL_ID (0);
setZYN_TABLE_ROW_ID (0);
}
 */
}
/** Load Constructor */
public X_ZYN_TABLE_ROW (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=1000358 */
public static final int Table_ID=1000358;

/** TableName=ZYN_TABLE_ROW */
public static final String Table_Name="ZYN_TABLE_ROW";

protected static KeyNamePair Model = new KeyNamePair(1000358,"ZYN_TABLE_ROW");

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
StringBuffer sb = new StringBuffer ("X_ZYN_TABLE_ROW[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Column.
Column in the table */
public void setAD_Column_ID (int AD_Column_ID)
{
if (AD_Column_ID < 1) throw new IllegalArgumentException ("AD_Column_ID is mandatory.");
set_Value ("AD_Column_ID", new Integer(AD_Column_ID));
}
/** Get Column.
Column in the table */
public int getAD_Column_ID() 
{
Integer ii = (Integer)get_Value("AD_Column_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set ZYN_TABLE_MODEL_ID */
public void setZYN_TABLE_MODEL_ID (int ZYN_TABLE_MODEL_ID)
{
if (ZYN_TABLE_MODEL_ID < 1) throw new IllegalArgumentException ("ZYN_TABLE_MODEL_ID is mandatory.");
set_Value ("ZYN_TABLE_MODEL_ID", new Integer(ZYN_TABLE_MODEL_ID));
}
/** Get ZYN_TABLE_MODEL_ID */
public int getZYN_TABLE_MODEL_ID() 
{
Integer ii = (Integer)get_Value("ZYN_TABLE_MODEL_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set ZYN_TABLE_ROW_ID */
public void setZYN_TABLE_ROW_ID (int ZYN_TABLE_ROW_ID)
{
if (ZYN_TABLE_ROW_ID < 1) throw new IllegalArgumentException ("ZYN_TABLE_ROW_ID is mandatory.");
set_ValueNoCheck ("ZYN_TABLE_ROW_ID", new Integer(ZYN_TABLE_ROW_ID));
}
/** Get ZYN_TABLE_ROW_ID */
public int getZYN_TABLE_ROW_ID() 
{
Integer ii = (Integer)get_Value("ZYN_TABLE_ROW_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
