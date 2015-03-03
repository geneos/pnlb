/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for ZYN_MODEL_COLUMN
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.2.0 - 2012-04-11 23:02:25.114 */
public class X_ZYN_MODEL_COLUMN extends PO
{
/** Standard Constructor */
public X_ZYN_MODEL_COLUMN (Properties ctx, int ZYN_MODEL_COLUMN_ID, String trxName)
{
super (ctx, ZYN_MODEL_COLUMN_ID, trxName);
/** if (ZYN_MODEL_COLUMN_ID == 0)
{
setAD_Column_ID (0);
setISPARAMETER (false);
setName (null);
setZYN_MODEL_COLUMN_ID (0);
setZYN_MODEL_TABLE_ID (0);
}
 */
}
/** Load Constructor */
public X_ZYN_MODEL_COLUMN (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000023 */
public static final int Table_ID=5000037;

/** TableName=ZYN_MODEL_COLUMN */
public static final String Table_Name="ZYN_MODEL_COLUMN";

protected static KeyNamePair Model = new KeyNamePair(5000037,"ZYN_MODEL_COLUMN");

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
StringBuffer sb = new StringBuffer ("X_ZYN_MODEL_COLUMN[").append(get_ID()).append("]");
return sb.toString();
}
/** Set AD_COLUMN_NAME */
public void setAD_COLUMN_NAME (String AD_COLUMN_NAME)
{
if (AD_COLUMN_NAME != null && AD_COLUMN_NAME.length() > 255)
{
log.warning("Length > 255 - truncated");
AD_COLUMN_NAME = AD_COLUMN_NAME.substring(0,254);
}
set_Value ("AD_COLUMN_NAME", AD_COLUMN_NAME);
}
/** Get AD_COLUMN_NAME */
public String getAD_COLUMN_NAME() 
{
return (String)get_Value("AD_COLUMN_NAME");
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
/** Set ISPARAMETER */
public void setISPARAMETER (boolean ISPARAMETER)
{
set_Value ("ISPARAMETER", new Boolean(ISPARAMETER));
}
/** Get ISPARAMETER */
public boolean isPARAMETER() 
{
Object oo = get_Value("ISPARAMETER");
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
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 510)
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
/** Set ZYN_MODEL_COLUMN_ID */
public void setZYN_MODEL_COLUMN_ID (int ZYN_MODEL_COLUMN_ID)
{
if (ZYN_MODEL_COLUMN_ID < 1) throw new IllegalArgumentException ("ZYN_MODEL_COLUMN_ID is mandatory.");
set_ValueNoCheck ("ZYN_MODEL_COLUMN_ID", new Integer(ZYN_MODEL_COLUMN_ID));
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
if (ZYN_MODEL_TABLE_ID < 1) throw new IllegalArgumentException ("ZYN_MODEL_TABLE_ID is mandatory.");
set_Value ("ZYN_MODEL_TABLE_ID", new Integer(ZYN_MODEL_TABLE_ID));
}
/** Get ZYN_MODEL_TABLE_ID */
public int getZYN_MODEL_TABLE_ID() 
{
Integer ii = (Integer)get_Value("ZYN_MODEL_TABLE_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set ZYN_PARAMETER_REF_ID */
public void setZYN_PARAMETER_REF_ID (int ZYN_PARAMETER_REF_ID)
{
if (ZYN_PARAMETER_REF_ID <= 0) set_Value ("ZYN_PARAMETER_REF_ID", null);
 else 
set_Value ("ZYN_PARAMETER_REF_ID", new Integer(ZYN_PARAMETER_REF_ID));
}
/** Get ZYN_PARAMETER_REF_ID */
public int getZYN_PARAMETER_REF_ID() 
{
Integer ii = (Integer)get_Value("ZYN_PARAMETER_REF_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set ZYN_PARAMETER_TYPE_ID */
public void setZYN_PARAMETER_TYPE_ID (int ZYN_PARAMETER_TYPE_ID)
{
if (ZYN_PARAMETER_TYPE_ID <= 0) set_Value ("ZYN_PARAMETER_TYPE_ID", null);
 else 
set_Value ("ZYN_PARAMETER_TYPE_ID", new Integer(ZYN_PARAMETER_TYPE_ID));
}
/** Get ZYN_PARAMETER_TYPE_ID */
public int getZYN_PARAMETER_TYPE_ID() 
{
Integer ii = (Integer)get_Value("ZYN_PARAMETER_TYPE_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
