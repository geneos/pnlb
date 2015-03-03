/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for C_ACCTINI
 *  @author Jorg Janke (generated) 
 *  @version Release 2.8.9 - 2012-03-20 14:26:50.021 */
public class X_C_ACCTINI extends PO
{
/** Standard Constructor */
public X_C_ACCTINI (Properties ctx, int C_ACCTINI_ID, String trxName)
{
super (ctx, C_ACCTINI_ID, trxName);
/** if (C_ACCTINI_ID == 0)
{
setC_ACCTINI_ID (0);
setTrxType (false);
}
 */
}
/** Load Constructor */
public X_C_ACCTINI (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000032 */
public static final int Table_ID=5000019;

/** TableName=C_ACCTINI */
public static final String Table_Name="C_ACCTINI";

protected static KeyNamePair Model = new KeyNamePair(5000019,"C_ACCTINI");

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
StringBuffer sb = new StringBuffer ("X_C_ACCTINI[").append(get_ID()).append("]");
return sb.toString();
}
/** Set C_ACCTINI_ID */
public void setC_ACCTINI_ID (int C_ACCTINI_ID)
{
if (C_ACCTINI_ID < 1) throw new IllegalArgumentException ("C_ACCTINI_ID is mandatory.");
set_ValueNoCheck ("C_ACCTINI_ID", new Integer(C_ACCTINI_ID));
}
/** Get C_ACCTINI_ID */
public int getC_ACCTINI_ID() 
{
Integer ii = (Integer)get_Value("C_ACCTINI_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set INIDATE */
public void setINIDATE (Timestamp INIDATE)
{
set_Value ("INIDATE", INIDATE);
}
/** Get INIDATE */
public Timestamp getINIDATE() 
{
return (Timestamp)get_Value("INIDATE");
}
/** Set Transaction Type.
Type of credit card transaction */
public void setTrxType (boolean TrxType)
{
set_Value ("TrxType", new Boolean(TrxType));
}
/** Get Transaction Type.
Type of credit card transaction */
public boolean isTrxType() 
{
Object oo = get_Value("TrxType");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
}
