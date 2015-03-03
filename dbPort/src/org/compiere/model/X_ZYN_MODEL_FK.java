/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for ZYN_MODEL_FK
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.2.0 - 2012-04-11 23:02:25.121 */
public class X_ZYN_MODEL_FK extends PO
{
/** Standard Constructor */
public X_ZYN_MODEL_FK (Properties ctx, int ZYN_MODEL_FK_ID, String trxName)
{
super (ctx, ZYN_MODEL_FK_ID, trxName);
/** if (ZYN_MODEL_FK_ID == 0)
{
setName (null);
setZYN_MODEL_COLUMN1_ID (0);
setZYN_MODEL_COLUMN2_ID (0);
setZYN_MODEL_FK_ID (0);
setZYN_MODEL_FK_TYPE_ID (0);
setZYN_MODEL_ID (0);
setZYN_MODEL_TABLE1_ID (0);
setZYN_MODEL_TABLE2_ID (0);
}
 */
}
/** Load Constructor */
public X_ZYN_MODEL_FK (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000025 */
public static final int Table_ID=5000027;

/** TableName=ZYN_MODEL_FK */
public static final String Table_Name="ZYN_MODEL_FK";

protected static KeyNamePair Model = new KeyNamePair(5000027,"ZYN_MODEL_FK");

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
StringBuffer sb = new StringBuffer ("X_ZYN_MODEL_FK[").append(get_ID()).append("]");
return sb.toString();
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
/** Set ZYN_MODEL_COLUMN1_ID */
public void setZYN_MODEL_COLUMN1_ID (int ZYN_MODEL_COLUMN1_ID)
{
if (ZYN_MODEL_COLUMN1_ID < 1) throw new IllegalArgumentException ("ZYN_MODEL_COLUMN1_ID is mandatory.");
set_Value ("ZYN_MODEL_COLUMN1_ID", new Integer(ZYN_MODEL_COLUMN1_ID));
}
/** Get ZYN_MODEL_COLUMN1_ID */
public int getZYN_MODEL_COLUMN1_ID() 
{
Integer ii = (Integer)get_Value("ZYN_MODEL_COLUMN1_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set ZYN_MODEL_COLUMN2_ID */
public void setZYN_MODEL_COLUMN2_ID (int ZYN_MODEL_COLUMN2_ID)
{
if (ZYN_MODEL_COLUMN2_ID < 1) throw new IllegalArgumentException ("ZYN_MODEL_COLUMN2_ID is mandatory.");
set_Value ("ZYN_MODEL_COLUMN2_ID", new Integer(ZYN_MODEL_COLUMN2_ID));
}
/** Get ZYN_MODEL_COLUMN2_ID */
public int getZYN_MODEL_COLUMN2_ID() 
{
Integer ii = (Integer)get_Value("ZYN_MODEL_COLUMN2_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set ZYN_MODEL_FK_ID */
public void setZYN_MODEL_FK_ID (int ZYN_MODEL_FK_ID)
{
if (ZYN_MODEL_FK_ID < 1) throw new IllegalArgumentException ("ZYN_MODEL_FK_ID is mandatory.");
set_ValueNoCheck ("ZYN_MODEL_FK_ID", new Integer(ZYN_MODEL_FK_ID));
}
/** Get ZYN_MODEL_FK_ID */
public int getZYN_MODEL_FK_ID() 
{
Integer ii = (Integer)get_Value("ZYN_MODEL_FK_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set ZYN_MODEL_FK_TYPE_ID */
public void setZYN_MODEL_FK_TYPE_ID (int ZYN_MODEL_FK_TYPE_ID)
{
if (ZYN_MODEL_FK_TYPE_ID < 1) throw new IllegalArgumentException ("ZYN_MODEL_FK_TYPE_ID is mandatory.");
set_Value ("ZYN_MODEL_FK_TYPE_ID", new Integer(ZYN_MODEL_FK_TYPE_ID));
}
/** Get ZYN_MODEL_FK_TYPE_ID */
public int getZYN_MODEL_FK_TYPE_ID() 
{
Integer ii = (Integer)get_Value("ZYN_MODEL_FK_TYPE_ID");
if (ii == null) return 0;
return ii.intValue();
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
/** Set ZYN_MODEL_TABLE1_ID */
public void setZYN_MODEL_TABLE1_ID (int ZYN_MODEL_TABLE1_ID)
{
if (ZYN_MODEL_TABLE1_ID < 1) throw new IllegalArgumentException ("ZYN_MODEL_TABLE1_ID is mandatory.");
set_Value ("ZYN_MODEL_TABLE1_ID", new Integer(ZYN_MODEL_TABLE1_ID));
}
/** Get ZYN_MODEL_TABLE1_ID */
public int getZYN_MODEL_TABLE1_ID() 
{
Integer ii = (Integer)get_Value("ZYN_MODEL_TABLE1_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set ZYN_MODEL_TABLE2_ID */
public void setZYN_MODEL_TABLE2_ID (int ZYN_MODEL_TABLE2_ID)
{
if (ZYN_MODEL_TABLE2_ID < 1) throw new IllegalArgumentException ("ZYN_MODEL_TABLE2_ID is mandatory.");
set_Value ("ZYN_MODEL_TABLE2_ID", new Integer(ZYN_MODEL_TABLE2_ID));
}
/** Get ZYN_MODEL_TABLE2_ID */
public int getZYN_MODEL_TABLE2_ID() 
{
Integer ii = (Integer)get_Value("ZYN_MODEL_TABLE2_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
