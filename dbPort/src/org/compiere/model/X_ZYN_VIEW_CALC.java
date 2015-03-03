/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for ZYN_VIEW_CALC
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.2.0 - 2012-04-11 23:29:11.93 */
public class X_ZYN_VIEW_CALC extends PO
{
/** Standard Constructor */
public X_ZYN_VIEW_CALC (Properties ctx, int ZYN_VIEW_CALC_ID, String trxName)
{
super (ctx, ZYN_VIEW_CALC_ID, trxName);
/** if (ZYN_VIEW_CALC_ID == 0)
{
setISSUM (false);
setISTRANSP (false);
setIsOrderBy (false);
setName (null);
setOPCALC (null);
setORDERVIEW (0);
setZYN_REPORT_ID (0);
setZYN_VIEW1_ID (0);
setZYN_VIEW_CALC_ID (0);
}
 */
}
/** Load Constructor */
public X_ZYN_VIEW_CALC (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000028 */
public static final int Table_ID=5000026;

/** TableName=ZYN_VIEW_CALC */
public static final String Table_Name="ZYN_VIEW_CALC";

protected static KeyNamePair Model = new KeyNamePair(5000026,"ZYN_VIEW_CALC");

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
StringBuffer sb = new StringBuffer ("X_ZYN_VIEW_CALC[").append(get_ID()).append("]");
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
/** Set OPCALC */
public void setOPCALC (String OPCALC)
{
if (OPCALC == null) throw new IllegalArgumentException ("OPCALC is mandatory.");
if (OPCALC.length() > 1)
{
log.warning("Length > 1 - truncated");
OPCALC = OPCALC.substring(0,0);
}
set_Value ("OPCALC", OPCALC);
}
/** Get OPCALC */
public String getOPCALC() 
{
return (String)get_Value("OPCALC");
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
/** Set VALUECALC */
public void setVALUECALC (int VALUECALC)
{
set_Value ("VALUECALC", new Integer(VALUECALC));
}
/** Get VALUECALC */
public int getVALUECALC() 
{
Integer ii = (Integer)get_Value("VALUECALC");
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
/** Set ZYN_VIEW1_ID */
public void setZYN_VIEW1_ID (int ZYN_VIEW1_ID)
{
if (ZYN_VIEW1_ID < 1) throw new IllegalArgumentException ("ZYN_VIEW1_ID is mandatory.");
set_Value ("ZYN_VIEW1_ID", new Integer(ZYN_VIEW1_ID));
}
/** Get ZYN_VIEW1_ID */
public int getZYN_VIEW1_ID() 
{
Integer ii = (Integer)get_Value("ZYN_VIEW1_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set ZYN_VIEW2_ID */
public void setZYN_VIEW2_ID (int ZYN_VIEW2_ID)
{
if (ZYN_VIEW2_ID <= 0) set_Value ("ZYN_VIEW2_ID", null);
 else 
set_Value ("ZYN_VIEW2_ID", new Integer(ZYN_VIEW2_ID));
}
/** Get ZYN_VIEW2_ID */
public int getZYN_VIEW2_ID() 
{
Integer ii = (Integer)get_Value("ZYN_VIEW2_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set ZYN_VIEW_CALC_ID */
public void setZYN_VIEW_CALC_ID (int ZYN_VIEW_CALC_ID)
{
if (ZYN_VIEW_CALC_ID < 1) throw new IllegalArgumentException ("ZYN_VIEW_CALC_ID is mandatory.");
set_ValueNoCheck ("ZYN_VIEW_CALC_ID", new Integer(ZYN_VIEW_CALC_ID));
}
/** Get ZYN_VIEW_CALC_ID */
public int getZYN_VIEW_CALC_ID() 
{
Integer ii = (Integer)get_Value("ZYN_VIEW_CALC_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
