/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for ZYN_MODEL_TABLE
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.2.0 - 2012-04-11 23:02:25.138 */
public class X_ZYN_MODEL_TABLE extends PO
{
/** Standard Constructor */
public X_ZYN_MODEL_TABLE (Properties ctx, int ZYN_MODEL_TABLE_ID, String trxName)
{
super (ctx, ZYN_MODEL_TABLE_ID, trxName);
/** if (ZYN_MODEL_TABLE_ID == 0)
{
setAD_Table_ID (0);
setName (null);
setZYN_MODEL_ID (0);
setZYN_MODEL_TABLE_ID (0);
}
 */
}
/** Load Constructor */
public X_ZYN_MODEL_TABLE (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000022 */
public static final int Table_ID=5000038;

/** TableName=ZYN_MODEL_TABLE */
public static final String Table_Name="ZYN_MODEL_TABLE";

protected static KeyNamePair Model = new KeyNamePair(5000038,"ZYN_MODEL_TABLE");

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
StringBuffer sb = new StringBuffer ("X_ZYN_MODEL_TABLE[").append(get_ID()).append("]");
return sb.toString();
}
/** Set AD_TABLE_NAME */
public void setAD_TABLE_NAME (String AD_TABLE_NAME)
{
if (AD_TABLE_NAME != null && AD_TABLE_NAME.length() > 255)
{
log.warning("Length > 255 - truncated");
AD_TABLE_NAME = AD_TABLE_NAME.substring(0,254);
}
set_Value ("AD_TABLE_NAME", AD_TABLE_NAME);
}
/** Get AD_TABLE_NAME */
public String getAD_TABLE_NAME() 
{
return (String)get_Value("AD_TABLE_NAME");
}
/** Set Table.
Table for the Fields */
public void setAD_Table_ID (int AD_Table_ID)
{
if (AD_Table_ID < 1) throw new IllegalArgumentException ("AD_Table_ID is mandatory.");
set_Value ("AD_Table_ID", new Integer(AD_Table_ID));
}
/** Get Table.
Table for the Fields */
public int getAD_Table_ID() 
{
Integer ii = (Integer)get_Value("AD_Table_ID");
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
/** Set ZYN_MODEL_ID */
public void setZYN_MODEL_ID (int ZYN_MODEL_ID)
{
if (ZYN_MODEL_ID < 1) throw new IllegalArgumentException ("ZYN_MODEL_ID is mandatory.");
set_Value ("ZYN_MODEL_ID", new Integer(ZYN_MODEL_ID));
}
/** Get ZYN_MODEL_ID */
public int getZYN_MODEL_ID() 
{
Integer ii = (Integer)get_Value("ZYN_MODEL_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set ZYN_MODEL_TABLE_ID */
public void setZYN_MODEL_TABLE_ID (int ZYN_MODEL_TABLE_ID)
{
if (ZYN_MODEL_TABLE_ID < 1) throw new IllegalArgumentException ("ZYN_MODEL_TABLE_ID is mandatory.");
set_ValueNoCheck ("ZYN_MODEL_TABLE_ID", new Integer(ZYN_MODEL_TABLE_ID));
}
/** Get ZYN_MODEL_TABLE_ID */
public int getZYN_MODEL_TABLE_ID() 
{
Integer ii = (Integer)get_Value("ZYN_MODEL_TABLE_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
