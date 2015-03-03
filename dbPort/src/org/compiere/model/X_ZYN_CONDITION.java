/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for ZYN_CONDITION
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.2.0 - 2012-04-11 23:02:25.073 */
public class X_ZYN_CONDITION extends PO
{
/** Standard Constructor */
public X_ZYN_CONDITION (Properties ctx, int ZYN_CONDITION_ID, String trxName)
{
super (ctx, ZYN_CONDITION_ID, trxName);
/** if (ZYN_CONDITION_ID == 0)
{
setZYN_CONDITION_ID (0);
setZYN_REPORT_ID (0);
}
 */
}
/** Load Constructor */
public X_ZYN_CONDITION (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000030 */
public static final int Table_ID=5000029;

/** TableName=ZYN_CONDITION */
public static final String Table_Name="ZYN_CONDITION";

protected static KeyNamePair Model = new KeyNamePair(5000029,"ZYN_CONDITION");

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
StringBuffer sb = new StringBuffer ("X_ZYN_CONDITION[").append(get_ID()).append("]");
return sb.toString();
}
/** Set MAIN */
public void setMAIN (boolean MAIN)
{
set_Value ("MAIN", new Boolean(MAIN));
}
/** Get MAIN */
public boolean isMAIN() 
{
Object oo = get_Value("MAIN");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set NEGATE */
public void setNEGATE (boolean NEGATE)
{
set_Value ("NEGATE", new Boolean(NEGATE));
}
/** Get NEGATE */
public boolean isNEGATE() 
{
Object oo = get_Value("NEGATE");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
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
/** Set Search Key.
Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value != null && Value.length() > 255)
{
log.warning("Length > 255 - truncated");
Value = Value.substring(0,254);
}
set_Value ("Value", Value);
}
/** Get Search Key.
Search key for the record in the format required - must be unique */
public String getValue() 
{
return (String)get_Value("Value");
}
/** Set ZYN_CONDITION_ID */
public void setZYN_CONDITION_ID (int ZYN_CONDITION_ID)
{
if (ZYN_CONDITION_ID < 1) throw new IllegalArgumentException ("ZYN_CONDITION_ID is mandatory.");
set_ValueNoCheck ("ZYN_CONDITION_ID", new Integer(ZYN_CONDITION_ID));
}
/** Get ZYN_CONDITION_ID */
public int getZYN_CONDITION_ID() 
{
Integer ii = (Integer)get_Value("ZYN_CONDITION_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set ZYN_CONDITION_TYPE_ID */
public void setZYN_CONDITION_TYPE_ID (int ZYN_CONDITION_TYPE_ID)
{
if (ZYN_CONDITION_TYPE_ID <= 0) set_Value ("ZYN_CONDITION_TYPE_ID", null);
 else 
set_Value ("ZYN_CONDITION_TYPE_ID", new Integer(ZYN_CONDITION_TYPE_ID));
}
/** Get ZYN_CONDITION_TYPE_ID */
public int getZYN_CONDITION_TYPE_ID() 
{
Integer ii = (Integer)get_Value("ZYN_CONDITION_TYPE_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set ZYN_FILTER_ID */
public void setZYN_FILTER_ID (int ZYN_FILTER_ID)
{
if (ZYN_FILTER_ID <= 0) set_Value ("ZYN_FILTER_ID", null);
 else 
set_Value ("ZYN_FILTER_ID", new Integer(ZYN_FILTER_ID));
}
/** Get ZYN_FILTER_ID */
public int getZYN_FILTER_ID() 
{
Integer ii = (Integer)get_Value("ZYN_FILTER_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set ZYN_MODEL_COLUMN_ID */
public void setZYN_MODEL_COLUMN_ID (int ZYN_MODEL_COLUMN_ID)
{
if (ZYN_MODEL_COLUMN_ID <= 0) set_Value ("ZYN_MODEL_COLUMN_ID", null);
 else 
set_Value ("ZYN_MODEL_COLUMN_ID", new Integer(ZYN_MODEL_COLUMN_ID));
}
/** Get ZYN_MODEL_COLUMN_ID */
public int getZYN_MODEL_COLUMN_ID() 
{
Integer ii = (Integer)get_Value("ZYN_MODEL_COLUMN_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set ZYN_MODEL_TABLE_ID */
public void setZYN_MODEL_TABLE_ID (int ZYN_MODEL_TABLE_ID)
{
if (ZYN_MODEL_TABLE_ID <= 0) set_Value ("ZYN_MODEL_TABLE_ID", null);
 else 
set_Value ("ZYN_MODEL_TABLE_ID", new Integer(ZYN_MODEL_TABLE_ID));
}
/** Get ZYN_MODEL_TABLE_ID */
public int getZYN_MODEL_TABLE_ID() 
{
Integer ii = (Integer)get_Value("ZYN_MODEL_TABLE_ID");
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
