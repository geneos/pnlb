/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for ZYN_USER_VERSION
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.2.0 - 2012-04-11 23:02:25.168 */
public class X_ZYN_USER_VERSION extends PO
{
/** Standard Constructor */
public X_ZYN_USER_VERSION (Properties ctx, int ZYN_USER_VERSION_ID, String trxName)
{
super (ctx, ZYN_USER_VERSION_ID, trxName);
/** if (ZYN_USER_VERSION_ID == 0)
{
setIsTranslated (false);
setUSER_ADDRESS (null);
setZYN_USER_VERSION_ID (0);
}
 */
}
/** Load Constructor */
public X_ZYN_USER_VERSION (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000007 */
public static final int Table_ID=5000007;

/** TableName=ZYN_USER_VERSION */
public static final String Table_Name="ZYN_USER_VERSION";

protected static KeyNamePair Model = new KeyNamePair(5000007,"ZYN_USER_VERSION");

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
StringBuffer sb = new StringBuffer ("X_ZYN_USER_VERSION[").append(get_ID()).append("]");
return sb.toString();
}
/** Set CLIENT_VERSION */
public void setCLIENT_VERSION (String CLIENT_VERSION)
{
if (CLIENT_VERSION != null && CLIENT_VERSION.length() > 20)
{
log.warning("Length > 20 - truncated");
CLIENT_VERSION = CLIENT_VERSION.substring(0,19);
}
set_Value ("CLIENT_VERSION", CLIENT_VERSION);
}
/** Get CLIENT_VERSION */
public String getCLIENT_VERSION() 
{
return (String)get_Value("CLIENT_VERSION");
}
/** Set HOSTNAME */
public void setHOSTNAME (String HOSTNAME)
{
if (HOSTNAME != null && HOSTNAME.length() > 800)
{
log.warning("Length > 800 - truncated");
HOSTNAME = HOSTNAME.substring(0,799);
}
set_Value ("HOSTNAME", HOSTNAME);
}
/** Get HOSTNAME */
public String getHOSTNAME() 
{
return (String)get_Value("HOSTNAME");
}
/** Set Translated.
This column is translated */
public void setIsTranslated (boolean IsTranslated)
{
set_Value ("IsTranslated", new Boolean(IsTranslated));
}
/** Get Translated.
This column is translated */
public boolean isTranslated() 
{
Object oo = get_Value("IsTranslated");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set LAST_CHECK */
public void setLAST_CHECK (Timestamp LAST_CHECK)
{
set_Value ("LAST_CHECK", LAST_CHECK);
}
/** Get LAST_CHECK */
public Timestamp getLAST_CHECK() 
{
return (Timestamp)get_Value("LAST_CHECK");
}
/** Set USER_ADDRESS */
public void setUSER_ADDRESS (String USER_ADDRESS)
{
if (USER_ADDRESS == null) throw new IllegalArgumentException ("USER_ADDRESS is mandatory.");
if (USER_ADDRESS.length() > 50)
{
log.warning("Length > 50 - truncated");
USER_ADDRESS = USER_ADDRESS.substring(0,49);
}
set_Value ("USER_ADDRESS", USER_ADDRESS);
}
/** Get USER_ADDRESS */
public String getUSER_ADDRESS() 
{
return (String)get_Value("USER_ADDRESS");
}
/** Set ZYN_USER_VERSION_ID */
public void setZYN_USER_VERSION_ID (int ZYN_USER_VERSION_ID)
{
if (ZYN_USER_VERSION_ID < 1) throw new IllegalArgumentException ("ZYN_USER_VERSION_ID is mandatory.");
set_ValueNoCheck ("ZYN_USER_VERSION_ID", new Integer(ZYN_USER_VERSION_ID));
}
/** Get ZYN_USER_VERSION_ID */
public int getZYN_USER_VERSION_ID() 
{
Integer ii = (Integer)get_Value("ZYN_USER_VERSION_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
