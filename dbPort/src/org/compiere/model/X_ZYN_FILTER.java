/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for ZYN_FILTER
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.2.0 - 2012-04-11 23:02:25.089 */
public class X_ZYN_FILTER extends PO
{
/** Standard Constructor */
public X_ZYN_FILTER (Properties ctx, int ZYN_FILTER_ID, String trxName)
{
super (ctx, ZYN_FILTER_ID, trxName);
/** if (ZYN_FILTER_ID == 0)
{
setZYN_FILTER_ID (0);
setZYN_REPORT_ID (0);
}
 */
}
/** Load Constructor */
public X_ZYN_FILTER (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000031 */
public static final int Table_ID=5000025;

/** TableName=ZYN_FILTER */
public static final String Table_Name="ZYN_FILTER";

protected static KeyNamePair Model = new KeyNamePair(5000025,"ZYN_FILTER");

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
StringBuffer sb = new StringBuffer ("X_ZYN_FILTER[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Name.
Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name != null && Name.length() > 510)
{
log.warning("Length > 510 - truncated");
Name = Name.substring(0,509);
}
set_Value ("Name", Name);
}
/** Get Name.
Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getName());
}
/** Set ZYN_CONDITION1_ID */
public void setZYN_CONDITION1_ID (int ZYN_CONDITION1_ID)
{
if (ZYN_CONDITION1_ID <= 0) set_Value ("ZYN_CONDITION1_ID", null);
 else 
set_Value ("ZYN_CONDITION1_ID", new Integer(ZYN_CONDITION1_ID));
}
/** Get ZYN_CONDITION1_ID */
public int getZYN_CONDITION1_ID() 
{
Integer ii = (Integer)get_Value("ZYN_CONDITION1_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set ZYN_CONDITION2_ID */
public void setZYN_CONDITION2_ID (int ZYN_CONDITION2_ID)
{
if (ZYN_CONDITION2_ID <= 0) set_Value ("ZYN_CONDITION2_ID", null);
 else 
set_Value ("ZYN_CONDITION2_ID", new Integer(ZYN_CONDITION2_ID));
}
/** Get ZYN_CONDITION2_ID */
public int getZYN_CONDITION2_ID() 
{
Integer ii = (Integer)get_Value("ZYN_CONDITION2_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set ZYN_FILTER_ID */
public void setZYN_FILTER_ID (int ZYN_FILTER_ID)
{
if (ZYN_FILTER_ID < 1) throw new IllegalArgumentException ("ZYN_FILTER_ID is mandatory.");
set_ValueNoCheck ("ZYN_FILTER_ID", new Integer(ZYN_FILTER_ID));
}
/** Get ZYN_FILTER_ID */
public int getZYN_FILTER_ID() 
{
Integer ii = (Integer)get_Value("ZYN_FILTER_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set ZYN_FILTER_TYPE_ID */
public void setZYN_FILTER_TYPE_ID (int ZYN_FILTER_TYPE_ID)
{
if (ZYN_FILTER_TYPE_ID <= 0) set_Value ("ZYN_FILTER_TYPE_ID", null);
 else 
set_Value ("ZYN_FILTER_TYPE_ID", new Integer(ZYN_FILTER_TYPE_ID));
}
/** Get ZYN_FILTER_TYPE_ID */
public int getZYN_FILTER_TYPE_ID() 
{
Integer ii = (Integer)get_Value("ZYN_FILTER_TYPE_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set ZYN_REPORT_ID */
public void setZYN_REPORT_ID (int ZYN_REPORT_ID)
{
if (ZYN_REPORT_ID < 1) throw new IllegalArgumentException ("ZYN_REPORT_ID is mandatory.");
set_Value ("ZYN_REPORT_ID", new Integer(ZYN_REPORT_ID));
}
/** Get ZYN_REPORT_ID */
public int getZYN_REPORT_ID() 
{
Integer ii = (Integer)get_Value("ZYN_REPORT_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
