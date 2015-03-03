/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for ZYN_DOCTYPE_ROL_ACT
 *  @author Jorg Janke (generated) 
 *  @version Release 3.3.0.0 - 2013-11-26 17:24:59.469 */
public class X_ZYN_DOCTYPE_ROL_ACT extends PO
{
/** Standard Constructor */
public X_ZYN_DOCTYPE_ROL_ACT (Properties ctx, int ZYN_DOCTYPE_ROL_ACT_ID, String trxName)
{
super (ctx, ZYN_DOCTYPE_ROL_ACT_ID, trxName);
/** if (ZYN_DOCTYPE_ROL_ACT_ID == 0)
{
setAD_Role_ID (0);
setC_DocType_ID (0);
setDocAction (null);
setZYN_DOCTYPE_ROL_ACT_ID (0);
}
 */
}
/** Load Constructor */
public X_ZYN_DOCTYPE_ROL_ACT (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000053 */
public static int Table_ID;

/** TableName=ZYN_DOCTYPE_ROL_ACT */
public static final String Table_Name="ZYN_DOCTYPE_ROL_ACT";

protected static KeyNamePair Model = new KeyNamePair(5000053,"ZYN_DOCTYPE_ROL_ACT");

protected BigDecimal accessLevel = new BigDecimal(3);
/** AccessLevel 3 - Client - Org  */
protected int get_AccessLevel()
{
return accessLevel.intValue();
}
/** Load Meta Data */
protected POInfo initPO(Properties ctx) {
        POInfo info = initPO(ctx, Table_Name);
        Table_ID = info.getAD_Table_ID();
        Model = new KeyNamePair(Table_ID, Table_Name);
        return info;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_ZYN_DOCTYPE_ROL_ACT[").append(get_ID()).append("]");
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
/** Set Document Type.
Document type or rules */
public void setC_DocType_ID (int C_DocType_ID)
{
if (C_DocType_ID < 0) throw new IllegalArgumentException ("C_DocType_ID is mandatory.");
set_Value ("C_DocType_ID", new Integer(C_DocType_ID));
}
/** Get Document Type.
Document type or rules */
public int getC_DocType_ID() 
{
Integer ii = (Integer)get_Value("C_DocType_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Document Action.
The targeted status of the document */
public void setDocAction (String DocAction)
{
if (DocAction == null) throw new IllegalArgumentException ("DocAction is mandatory.");
if (DocAction.length() > 2)
{
log.warning("Length > 2 - truncated");
DocAction = DocAction.substring(0,1);
}
set_Value ("DocAction", DocAction);
}
/** Get Document Action.
The targeted status of the document */
public String getDocAction() 
{
return (String)get_Value("DocAction");
}
/** Set ZYN_DOCTYPE_ROL_ACT_ID */
public void setZYN_DOCTYPE_ROL_ACT_ID (int ZYN_DOCTYPE_ROL_ACT_ID)
{
if (ZYN_DOCTYPE_ROL_ACT_ID < 1) throw new IllegalArgumentException ("ZYN_DOCTYPE_ROL_ACT_ID is mandatory.");
set_ValueNoCheck ("ZYN_DOCTYPE_ROL_ACT_ID", new Integer(ZYN_DOCTYPE_ROL_ACT_ID));
}
/** Get ZYN_DOCTYPE_ROL_ACT_ID */
public int getZYN_DOCTYPE_ROL_ACT_ID() 
{
Integer ii = (Integer)get_Value("ZYN_DOCTYPE_ROL_ACT_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
