/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for ZYN_VIEW
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.2.0 - 2012-04-11 23:02:25.179 */
public class X_ZYN_VIEW extends PO
{
/** Standard Constructor */
public X_ZYN_VIEW (Properties ctx, int ZYN_VIEW_ID, String trxName)
{
super (ctx, ZYN_VIEW_ID, trxName);
/** if (ZYN_VIEW_ID == 0)
{
setISSUM (false);
setISTRANSP (false);
setIsOrderBy (false);
setNEXTLINE (false);
setNEXTPAGE (false);
setName (null);
setORDERVIEW (0);
setVIEWTYPE (false);
setZYN_MODEL_COLUMN_ID (0);
setZYN_MODEL_TABLE_ID (0);
setZYN_REPORT_ID (0);
setZYN_VIEW_ID (0);
}
 */
}
/** Load Constructor */
public X_ZYN_VIEW (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000027 */
public static final int Table_ID=5000031;

/** TableName=ZYN_VIEW */
public static final String Table_Name="ZYN_VIEW";

protected static KeyNamePair Model = new KeyNamePair(5000031,"ZYN_VIEW");

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
StringBuffer sb = new StringBuffer ("X_ZYN_VIEW[").append(get_ID()).append("]");
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
/** Set ISSUM */
public void setISSUM (boolean ISSUM)
{
set_Value ("ISSUM", new Boolean(ISSUM));
}
/** Get ISSUM */
public boolean isSUM() 
{
Object oo = get_Value("ISSUM");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set ISTRANSP */
public void setISTRANSP (boolean ISTRANSP)
{
set_Value ("ISTRANSP", new Boolean(ISTRANSP));
}
/** Get ISTRANSP */
public boolean isTRANSP() 
{
Object oo = get_Value("ISTRANSP");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Order by.
Include in sort order */
public void setIsOrderBy (boolean IsOrderBy)
{
set_Value ("IsOrderBy", new Boolean(IsOrderBy));
}
/** Get Order by.
Include in sort order */
public boolean isOrderBy() 
{
Object oo = get_Value("IsOrderBy");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set LINK_ID */
public void setLINK_ID (int LINK_ID)
{
if (LINK_ID <= 0) set_Value ("LINK_ID", null);
 else 
set_Value ("LINK_ID", new Integer(LINK_ID));
}
/** Get LINK_ID */
public int getLINK_ID() 
{
Integer ii = (Integer)get_Value("LINK_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set NEXTLINE */
public void setNEXTLINE (boolean NEXTLINE)
{
set_Value ("NEXTLINE", new Boolean(NEXTLINE));
}
/** Get NEXTLINE */
public boolean isNEXTLINE() 
{
Object oo = get_Value("NEXTLINE");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set NEXTPAGE */
public void setNEXTPAGE (boolean NEXTPAGE)
{
set_Value ("NEXTPAGE", new Boolean(NEXTPAGE));
}
/** Get NEXTPAGE */
public boolean isNEXTPAGE() 
{
Object oo = get_Value("NEXTPAGE");
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
/** Set ORDERVIEW */
public void setORDERVIEW (int ORDERVIEW)
{
set_Value ("ORDERVIEW", new Integer(ORDERVIEW));
}
/** Get ORDERVIEW */
public int getORDERVIEW() 
{
Integer ii = (Integer)get_Value("ORDERVIEW");
if (ii == null) return 0;
return ii.intValue();
}
/** Set VIEWTYPE */
public void setVIEWTYPE (boolean VIEWTYPE)
{
set_Value ("VIEWTYPE", new Boolean(VIEWTYPE));
}
/** Get VIEWTYPE */
public boolean isVIEWTYPE() 
{
Object oo = get_Value("VIEWTYPE");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set ZYN_MODEL_COLUMN_ID */
public void setZYN_MODEL_COLUMN_ID (int ZYN_MODEL_COLUMN_ID)
{
if (ZYN_MODEL_COLUMN_ID < 1) throw new IllegalArgumentException ("ZYN_MODEL_COLUMN_ID is mandatory.");
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
/** Set ZYN_VIEW_ID */
public void setZYN_VIEW_ID (int ZYN_VIEW_ID)
{
if (ZYN_VIEW_ID < 1) throw new IllegalArgumentException ("ZYN_VIEW_ID is mandatory.");
set_ValueNoCheck ("ZYN_VIEW_ID", new Integer(ZYN_VIEW_ID));
}
/** Get ZYN_VIEW_ID */
public int getZYN_VIEW_ID() 
{
Integer ii = (Integer)get_Value("ZYN_VIEW_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
