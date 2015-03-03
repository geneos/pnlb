/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for ZYN_MODEL_FK_TYPE
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.2.0 - 2012-04-11 23:02:25.13 */
public class X_ZYN_MODEL_FK_TYPE extends PO
{
/** Standard Constructor */
public X_ZYN_MODEL_FK_TYPE (Properties ctx, int ZYN_MODEL_FK_TYPE_ID, String trxName)
{
super (ctx, ZYN_MODEL_FK_TYPE_ID, trxName);
/** if (ZYN_MODEL_FK_TYPE_ID == 0)
{
setName (null);
setValue (null);
setZYN_MODEL_FK_TYPE_ID (0);
}
 */
}
/** Load Constructor */
public X_ZYN_MODEL_FK_TYPE (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000024 */
public static final int Table_ID=5000030;

/** TableName=ZYN_MODEL_FK_TYPE */
public static final String Table_Name="ZYN_MODEL_FK_TYPE";

protected static KeyNamePair Model = new KeyNamePair(5000030,"ZYN_MODEL_FK_TYPE");

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
StringBuffer sb = new StringBuffer ("X_ZYN_MODEL_FK_TYPE[").append(get_ID()).append("]");
return sb.toString();
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
/** Set Search Key.
Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
if (Value.length() > 255)
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
/** Set ZYN_MODEL_FK_TYPE_ID */
public void setZYN_MODEL_FK_TYPE_ID (int ZYN_MODEL_FK_TYPE_ID)
{
if (ZYN_MODEL_FK_TYPE_ID < 1) throw new IllegalArgumentException ("ZYN_MODEL_FK_TYPE_ID is mandatory.");
set_ValueNoCheck ("ZYN_MODEL_FK_TYPE_ID", new Integer(ZYN_MODEL_FK_TYPE_ID));
}
/** Get ZYN_MODEL_FK_TYPE_ID */
public int getZYN_MODEL_FK_TYPE_ID() 
{
Integer ii = (Integer)get_Value("ZYN_MODEL_FK_TYPE_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
