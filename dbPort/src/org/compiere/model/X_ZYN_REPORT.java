/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for ZYN_REPORT
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.2.0 - 2012-04-11 23:24:27.375 */
public class X_ZYN_REPORT extends PO
{
/** Standard Constructor */
public X_ZYN_REPORT (Properties ctx, int ZYN_REPORT_ID, String trxName)
{
super (ctx, ZYN_REPORT_ID, trxName);
/** if (ZYN_REPORT_ID == 0)
{
setName (null);
setPAGEVIEW (false);
setPRINTDATE (false);
setPRINTDESCRIPTION (false);
setPRINTPAGENUMBER (false);
setPRINTPARAM (false);
setREPORTTYPE (null);
setZYN_MODEL_ID (0);
setZYN_REPORT_ID (0);
}
 */
}
/** Load Constructor */
public X_ZYN_REPORT (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000026 */
public static final int Table_ID=5000036;

/** TableName=ZYN_REPORT */
public static final String Table_Name="ZYN_REPORT";

protected static KeyNamePair Model = new KeyNamePair(5000036,"ZYN_REPORT");

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
StringBuffer sb = new StringBuffer ("X_ZYN_REPORT[").append(get_ID()).append("]");
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
/** Set PAGEVIEW */
public void setPAGEVIEW (boolean PAGEVIEW)
{
set_Value ("PAGEVIEW", new Boolean(PAGEVIEW));
}
/** Get PAGEVIEW */
public boolean isPAGEVIEW() 
{
Object oo = get_Value("PAGEVIEW");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set PRINTDATE */
public void setPRINTDATE (boolean PRINTDATE)
{
set_Value ("PRINTDATE", new Boolean(PRINTDATE));
}
/** Get PRINTDATE */
public boolean isPRINTDATE() 
{
Object oo = get_Value("PRINTDATE");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set PRINTDESCRIPTION */
public void setPRINTDESCRIPTION (boolean PRINTDESCRIPTION)
{
set_Value ("PRINTDESCRIPTION", new Boolean(PRINTDESCRIPTION));
}
/** Get PRINTDESCRIPTION */
public boolean isPRINTDESCRIPTION() 
{
Object oo = get_Value("PRINTDESCRIPTION");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set PRINTPAGENUMBER */
public void setPRINTPAGENUMBER (boolean PRINTPAGENUMBER)
{
set_Value ("PRINTPAGENUMBER", new Boolean(PRINTPAGENUMBER));
}
/** Get PRINTPAGENUMBER */
public boolean isPRINTPAGENUMBER() 
{
Object oo = get_Value("PRINTPAGENUMBER");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set PRINTPARAM */
public void setPRINTPARAM (boolean PRINTPARAM)
{
set_Value ("PRINTPARAM", new Boolean(PRINTPARAM));
}
/** Get PRINTPARAM */
public boolean isPRINTPARAM() 
{
Object oo = get_Value("PRINTPARAM");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set REPORTTYPE */
public void setREPORTTYPE (String REPORTTYPE)
{
if (REPORTTYPE == null) throw new IllegalArgumentException ("REPORTTYPE is mandatory.");
if (REPORTTYPE.length() > 1)
{
log.warning("Length > 1 - truncated");
REPORTTYPE = REPORTTYPE.substring(0,0);
}
set_Value ("REPORTTYPE", REPORTTYPE);
}
/** Get REPORTTYPE */
public String getREPORTTYPE() 
{
return (String)get_Value("REPORTTYPE");
}
/** Set TEXTFOOTER */
public void setTEXTFOOTER (String TEXTFOOTER)
{
if (TEXTFOOTER != null && TEXTFOOTER.length() > 255)
{
log.warning("Length > 255 - truncated");
TEXTFOOTER = TEXTFOOTER.substring(0,254);
}
set_Value ("TEXTFOOTER", TEXTFOOTER);
}
/** Get TEXTFOOTER */
public String getTEXTFOOTER() 
{
return (String)get_Value("TEXTFOOTER");
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
/** Set ZYN_REPORT_ID */
public void setZYN_REPORT_ID (int ZYN_REPORT_ID)
{
if (ZYN_REPORT_ID < 1) throw new IllegalArgumentException ("ZYN_REPORT_ID is mandatory.");
set_ValueNoCheck ("ZYN_REPORT_ID", new Integer(ZYN_REPORT_ID));
}
/** Get ZYN_REPORT_ID */
public int getZYN_REPORT_ID() 
{
Integer ii = (Integer)get_Value("ZYN_REPORT_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
