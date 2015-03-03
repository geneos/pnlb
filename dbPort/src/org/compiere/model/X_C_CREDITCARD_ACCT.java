/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for C_CREDITCARD_ACCT
 *  @author Jorg Janke (generated) 
 *  @version Release 2.8.9 - 2011-11-04 00:25:19.951 */
public class X_C_CREDITCARD_ACCT extends PO
{
/** Standard Constructor */
public X_C_CREDITCARD_ACCT (Properties ctx, int C_CREDITCARD_ACCT_ID, String trxName)
{
super (ctx, C_CREDITCARD_ACCT_ID, trxName);
/** if (C_CREDITCARD_ACCT_ID == 0)
{
setC_AcctSchema_ID (0);
setC_CREDITCARD_ID (0);
setC_TAR_ACCT (0);
}
 */
}
/** Load Constructor */
public X_C_CREDITCARD_ACCT (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000008 */
public static final int Table_ID=5000013;

/** TableName=C_CREDITCARD_ACCT */
public static final String Table_Name="C_CREDITCARD_ACCT";

protected static KeyNamePair Model = new KeyNamePair(5000013,"C_CREDITCARD_ACCT");

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
StringBuffer sb = new StringBuffer ("X_C_CREDITCARD_ACCT[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Accounting Schema.
Rules for accounting */
public void setC_AcctSchema_ID (int C_AcctSchema_ID)
{
if (C_AcctSchema_ID < 1) throw new IllegalArgumentException ("C_AcctSchema_ID is mandatory.");
set_Value ("C_AcctSchema_ID", new Integer(C_AcctSchema_ID));
}
/** Get Accounting Schema.
Rules for accounting */
public int getC_AcctSchema_ID() 
{
Integer ii = (Integer)get_Value("C_AcctSchema_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set C_CREDITCARD_ID */
public void setC_CREDITCARD_ID (int C_CREDITCARD_ID)
{
if (C_CREDITCARD_ID < 1) throw new IllegalArgumentException ("C_CREDITCARD_ID is mandatory.");
set_Value ("C_CREDITCARD_ID", new Integer(C_CREDITCARD_ID));
}
/** Get C_CREDITCARD_ID */
public int getC_CREDITCARD_ID() 
{
Integer ii = (Integer)get_Value("C_CREDITCARD_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set C_TAR_ACCT */
public void setC_TAR_ACCT (int C_TAR_ACCT)
{
set_Value ("C_TAR_ACCT", new Integer(C_TAR_ACCT));
}
/** Get C_TAR_ACCT */
public int getC_TAR_ACCT() 
{
Integer ii = (Integer)get_Value("C_TAR_ACCT");
if (ii == null) return 0;
return ii.intValue();
}
}
