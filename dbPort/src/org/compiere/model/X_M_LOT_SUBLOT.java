/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for M_LOT_SUBLOT
 *  @author Jorg Janke (generated) 
 *  @version Release 3.3.0.0 - 2013-07-22 16:07:16.275 */
public class X_M_LOT_SUBLOT extends PO
{
/** Standard Constructor */
public X_M_LOT_SUBLOT (Properties ctx, int M_LOT_SUBLOT_ID, String trxName)
{
super (ctx, M_LOT_SUBLOT_ID, trxName);
/** if (M_LOT_SUBLOT_ID == 0)
{
setM_LOT_SUBLOT_ID (0);
setM_Lot_ID (0);
setM_SUBLOT_ID (0);
setSEQUENCE (null);
}
 */
}
/** Load Constructor */
public X_M_LOT_SUBLOT (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000048 */
public static final int Table_ID=5000048;

/** TableName=M_LOT_SUBLOT */
public static final String Table_Name="M_LOT_SUBLOT";

protected static KeyNamePair Model = new KeyNamePair(5000048,"M_LOT_SUBLOT");

protected BigDecimal accessLevel = new BigDecimal(3);
/** AccessLevel 3 - Client - Org  */
protected int get_AccessLevel()
{
return accessLevel.intValue();
}
/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = initPO(ctx, Table_Name);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_M_LOT_SUBLOT[").append(get_ID()).append("]");
return sb.toString();
}
/** Set M_LOT_SUBLOT_ID */
public void setM_LOT_SUBLOT_ID (int M_LOT_SUBLOT_ID)
{
if (M_LOT_SUBLOT_ID < 1) throw new IllegalArgumentException ("M_LOT_SUBLOT_ID is mandatory.");
set_ValueNoCheck ("M_LOT_SUBLOT_ID", new Integer(M_LOT_SUBLOT_ID));
}
/** Get M_LOT_SUBLOT_ID */
public int getM_LOT_SUBLOT_ID() 
{
Integer ii = (Integer)get_Value("M_LOT_SUBLOT_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Lot.
Product Lot Definition */
public void setM_Lot_ID (int M_Lot_ID)
{
if (M_Lot_ID < 1) throw new IllegalArgumentException ("M_Lot_ID is mandatory.");
set_Value ("M_Lot_ID", new Integer(M_Lot_ID));
}
/** Get Lot.
Product Lot Definition */
public int getM_Lot_ID() 
{
Integer ii = (Integer)get_Value("M_Lot_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set M_SUBLOT_ID */
public void setM_SUBLOT_ID (int M_SUBLOT_ID)
{
if (M_SUBLOT_ID < 1) throw new IllegalArgumentException ("M_SUBLOT_ID is mandatory.");
set_Value ("M_SUBLOT_ID", new Integer(M_SUBLOT_ID));
}
/** Get M_SUBLOT_ID */
public int getM_SUBLOT_ID() 
{
Integer ii = (Integer)get_Value("M_SUBLOT_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set SEQUENCE */
public void setSEQUENCE (String SEQUENCE)
{
if (SEQUENCE == null) throw new IllegalArgumentException ("SEQUENCE is mandatory.");
if (SEQUENCE.length() > 1)
{
log.warning("Length > 1 - truncated");
SEQUENCE = SEQUENCE.substring(0,0);
}
set_Value ("SEQUENCE", SEQUENCE);
}
/** Get SEQUENCE */
public String getSEQUENCE() 
{
return (String)get_Value("SEQUENCE");
}
}
