/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for C_CREDITCARD
 *  @author Jorg Janke (generated) 
 *  @version Release 2.8.9 - 2011-11-04 00:25:19.948 */
public class X_C_CREDITCARD extends PO
{
/** Standard Constructor */
public X_C_CREDITCARD (Properties ctx, int C_CREDITCARD_ID, String trxName)
{
super (ctx, C_CREDITCARD_ID, trxName);
/** if (C_CREDITCARD_ID == 0)
{
setC_Bank_ID (0);
setC_CREDITCARD_ID (0);
}
 */
}
/** Load Constructor */
public X_C_CREDITCARD (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000007 */
public static final int Table_ID=5000012;

/** TableName=C_CREDITCARD */
public static final String Table_Name="C_CREDITCARD";

protected static KeyNamePair Model = new KeyNamePair(5000012,"C_CREDITCARD");

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
StringBuffer sb = new StringBuffer ("X_C_CREDITCARD[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Bank.
Bank */
public void setC_Bank_ID (int C_Bank_ID)
{
if (C_Bank_ID < 1) throw new IllegalArgumentException ("C_Bank_ID is mandatory.");
set_Value ("C_Bank_ID", new Integer(C_Bank_ID));
}
/** Get Bank.
Bank */
public int getC_Bank_ID() 
{
Integer ii = (Integer)get_Value("C_Bank_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set C_CREDITCARD_ID */
public void setC_CREDITCARD_ID (int C_CREDITCARD_ID)
{
if (C_CREDITCARD_ID < 1) throw new IllegalArgumentException ("C_CREDITCARD_ID is mandatory.");
set_ValueNoCheck ("C_CREDITCARD_ID", new Integer(C_CREDITCARD_ID));
}
/** Get C_CREDITCARD_ID */
public int getC_CREDITCARD_ID() 
{
Integer ii = (Integer)get_Value("C_CREDITCARD_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Name.
Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name != null && Name.length() > 510)
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
}
