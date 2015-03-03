/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;

import org.compiere.util.*;
/** Generated Model for ZYN_CATEGORY_TOLERANCE
 *  @author Jorg Janke (generated) 
 *  @version Release 3.3.0.0 - 2013-10-30 15:35:26.239 */
public class X_ZYN_CATEGORY_TOLERANCE extends PO
{
/** Standard Constructor */
public X_ZYN_CATEGORY_TOLERANCE (Properties ctx, int ZYN_CATEGORY_TOLERANCE_ID, String trxName)
{
super (ctx, ZYN_CATEGORY_TOLERANCE_ID, trxName);
/** if (ZYN_CATEGORY_TOLERANCE_ID == 0)
{
setM_Product_Category_ID (0);
setTOLERANCE (Env.ZERO);
setZYN_CATEGORY_TOLERANCE_ID (0);
}
 */
}
/** Load Constructor */
public X_ZYN_CATEGORY_TOLERANCE (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000052 */
public static int Table_ID=5000052;

/** TableName=ZYN_CATEGORY_TOLERANCE */
public static final String Table_Name="ZYN_CATEGORY_TOLERANCE";

protected static KeyNamePair Model = new KeyNamePair(5000052,"ZYN_CATEGORY_TOLERANCE");

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
StringBuffer sb = new StringBuffer ("X_ZYN_CATEGORY_TOLERANCE[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Product Category.
Category of a Product */
public void setM_Product_Category_ID (int M_Product_Category_ID)
{
if (M_Product_Category_ID < 1) throw new IllegalArgumentException ("M_Product_Category_ID is mandatory.");
set_Value ("M_Product_Category_ID", new Integer(M_Product_Category_ID));
}
/** Get Product Category.
Category of a Product */
public int getM_Product_Category_ID() 
{
Integer ii = (Integer)get_Value("M_Product_Category_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set TOLERANCE */
public void setTOLERANCE (BigDecimal TOLERANCE)
{
if (TOLERANCE == null) throw new IllegalArgumentException ("TOLERANCE is mandatory.");
set_Value ("TOLERANCE", TOLERANCE);
}
/** Get TOLERANCE */
public BigDecimal getTOLERANCE() 
{
BigDecimal bd = (BigDecimal)get_Value("TOLERANCE");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set ZYN_CATEGORY_TOLERANCE_ID */
public void setZYN_CATEGORY_TOLERANCE_ID (int ZYN_CATEGORY_TOLERANCE_ID)
{
if (ZYN_CATEGORY_TOLERANCE_ID < 1) throw new IllegalArgumentException ("ZYN_CATEGORY_TOLERANCE_ID is mandatory.");
set_ValueNoCheck ("ZYN_CATEGORY_TOLERANCE_ID", new Integer(ZYN_CATEGORY_TOLERANCE_ID));
}
/** Get ZYN_CATEGORY_TOLERANCE_ID */
public int getZYN_CATEGORY_TOLERANCE_ID() 
{
Integer ii = (Integer)get_Value("ZYN_CATEGORY_TOLERANCE_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
