/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for T_ZADCHANGELOG
 *  @author Jorg Janke (generated) 
 *  @version Release 2.8.9 - 2011-10-12 20:39:59.159 */
public class X_T_ZADCHANGELOG extends PO
{
/** Standard Constructor */
public X_T_ZADCHANGELOG (Properties ctx, int T_ZADCHANGELOG_ID, String trxName)
{
super (ctx, T_ZADCHANGELOG_ID, trxName);
/** if (T_ZADCHANGELOG_ID == 0)
{
setAD_PInstance_ID (0);
setAD_Table_ID (0);
setAD_User_ID (0);
setT_ZADCHANGELOG_ID (0);
}
 */
}
/** Load Constructor */
public X_T_ZADCHANGELOG (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000027 */
public static final int Table_ID=5000027;

/** TableName=T_ZADCHANGELOG */
public static final String Table_Name="T_ZADCHANGELOG";

protected static KeyNamePair Model = new KeyNamePair(5000027,"T_ZADCHANGELOG");

protected BigDecimal accessLevel = new BigDecimal(4);
/** AccessLevel 4 - System  */
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
StringBuffer sb = new StringBuffer ("X_T_ZADCHANGELOG[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Process Instance.
Instance of the process */
public void setAD_PInstance_ID (int AD_PInstance_ID)
{
if (AD_PInstance_ID < 1) throw new IllegalArgumentException ("AD_PInstance_ID is mandatory.");
set_Value ("AD_PInstance_ID", new Integer(AD_PInstance_ID));
}
/** Get Process Instance.
Instance of the process */
public int getAD_PInstance_ID() 
{
Integer ii = (Integer)get_Value("AD_PInstance_ID");
if (ii == null) return 0;
return ii.intValue();
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
/** Set User/Contact.
User within the system - Internal or Business Partner Contact */
public void setAD_User_ID (int AD_User_ID)
{
if (AD_User_ID < 1) throw new IllegalArgumentException ("AD_User_ID is mandatory.");
set_Value ("AD_User_ID", new Integer(AD_User_ID));
}
/** Get User/Contact.
User within the system - Internal or Business Partner Contact */
public int getAD_User_ID() 
{
Integer ii = (Integer)get_Value("AD_User_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set DB Column Name.
Name of the column in the database */
public void setColumnName (String ColumnName)
{
if (ColumnName != null && ColumnName.length() > 60)
{
log.warning("Length > 60 - truncated");
ColumnName = ColumnName.substring(0,59);
}
set_Value ("ColumnName", ColumnName);
}
/** Get DB Column Name.
Name of the column in the database */
public String getColumnName() 
{
return (String)get_Value("ColumnName");
}
/** Set DATECREATED */
public void setDATECREATED (Timestamp DATECREATED)
{
set_Value ("DATECREATED", DATECREATED);
}
/** Get DATECREATED */
public Timestamp getDATECREATED() 
{
return (Timestamp)get_Value("DATECREATED");
}
/** Set Name.
Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name != null && Name.length() > 120)
{
log.warning("Length > 120 - truncated");
Name = Name.substring(0,119);
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
/** Set New Value.
New field value */
public void setNewValue (String NewValue)
{
if (NewValue != null && NewValue.length() > 60)
{
log.warning("Length > 60 - truncated");
NewValue = NewValue.substring(0,59);
}
set_Value ("NewValue", NewValue);
}
/** Get New Value.
New field value */
public String getNewValue() 
{
return (String)get_Value("NewValue");
}
/** Set Old Value.
The old file data */
public void setOldValue (String OldValue)
{
if (OldValue != null && OldValue.length() > 60)
{
log.warning("Length > 60 - truncated");
OldValue = OldValue.substring(0,59);
}
set_Value ("OldValue", OldValue);
}
/** Get Old Value.
The old file data */
public String getOldValue() 
{
return (String)get_Value("OldValue");
}
/** Set RECORD */
public void setRECORD (String RECORD)
{
if (RECORD != null && RECORD.length() > 60)
{
log.warning("Length > 60 - truncated");
RECORD = RECORD.substring(0,59);
}
set_Value ("RECORD", RECORD);
}
/** Get RECORD */
public String getRECORD() 
{
return (String)get_Value("RECORD");
}
/** Set T_ZADCHANGELOG_ID */
public void setT_ZADCHANGELOG_ID (int T_ZADCHANGELOG_ID)
{
if (T_ZADCHANGELOG_ID < 1) throw new IllegalArgumentException ("T_ZADCHANGELOG_ID is mandatory.");
set_ValueNoCheck ("T_ZADCHANGELOG_ID", new Integer(T_ZADCHANGELOG_ID));
}
/** Get T_ZADCHANGELOG_ID */
public int getT_ZADCHANGELOG_ID() 
{
Integer ii = (Integer)get_Value("T_ZADCHANGELOG_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set DB Table Name.
Name of the table in the database */
public void setTableName (String TableName)
{
if (TableName != null && TableName.length() > 60)
{
log.warning("Length > 60 - truncated");
TableName = TableName.substring(0,59);
}
set_Value ("TableName", TableName);
}
/** Get DB Table Name.
Name of the table in the database */
public String getTableName() 
{
return (String)get_Value("TableName");
}
}
