/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for ZYN_REPORT_ACCESS
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.2.0 - 2012-05-12 20:04:48.478 */
public class X_ZYN_REPORT_ACCESS extends PO
{
/** Standard Constructor */
public X_ZYN_REPORT_ACCESS (Properties ctx, int ZYN_REPORT_ACCESS_ID, String trxName)
{
super (ctx, ZYN_REPORT_ACCESS_ID, trxName);
/** if (ZYN_REPORT_ACCESS_ID == 0)
{
setAD_Role_ID (0);
setIsReadWrite (false);
setZYN_REPORT_ID (0);
}
 */
}
/** Load Constructor */
public X_ZYN_REPORT_ACCESS (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000036 */
public static final int Table_ID=5000035;

/** TableName=ZYN_REPORT_ACCESS */
public static final String Table_Name="ZYN_REPORT_ACCESS";

protected static KeyNamePair Model = new KeyNamePair(5000035,"ZYN_REPORT_ACCESS");

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
StringBuffer sb = new StringBuffer ("X_ZYN_REPORT_ACCESS[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Role.
Responsibility Role */
public void setAD_Role_ID (int AD_Role_ID)
{
if (AD_Role_ID < 0) throw new IllegalArgumentException ("AD_Role_ID is mandatory.");
set_Value ("AD_Role_ID", new Integer(AD_Role_ID));
}
/** Get Role.
Responsibility Role */
public int getAD_Role_ID() 
{
Integer ii = (Integer)get_Value("AD_Role_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Read Write.
Field is read / write */
public void setIsReadWrite (boolean IsReadWrite)
{
set_Value ("IsReadWrite", new Boolean(IsReadWrite));
}
/** Get Read Write.
Field is read / write */
public boolean isReadWrite() 
{
Object oo = get_Value("IsReadWrite");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
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
