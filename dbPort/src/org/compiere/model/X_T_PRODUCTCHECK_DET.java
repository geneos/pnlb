/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for T_PRODUCTCHECK_DET
 *  @author Jorg Janke (generated) 
 *  @version Release 3.3.0.0 - 2012-10-10 14:32:09.102 */
public class X_T_PRODUCTCHECK_DET extends PO
{
/** Standard Constructor */
public X_T_PRODUCTCHECK_DET (Properties ctx, int T_PRODUCTCHECK_DET_ID, String trxName)
{
super (ctx, T_PRODUCTCHECK_DET_ID, trxName);
/** if (T_PRODUCTCHECK_DET_ID == 0)
{
setM_Product_ID (0);
setM_Warehouse_ID (0);
setT_PRODUCTCHECK_DET_ID (0);
}
 */
}
/** Load Constructor */
public X_T_PRODUCTCHECK_DET (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000042 */
public static final int Table_ID=5000042;

/** TableName=T_PRODUCTCHECK_DET */
public static final String Table_Name="T_PRODUCTCHECK_DET";

protected static KeyNamePair Model = new KeyNamePair(5000042,"T_PRODUCTCHECK_DET");

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
StringBuffer sb = new StringBuffer ("X_T_PRODUCTCHECK_DET[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Process Instance.
Instance of the process */
public void setAD_PInstance_ID (int AD_PInstance_ID)
{
if (AD_PInstance_ID <= 0) set_Value ("AD_PInstance_ID", null);
 else 
set_Value ("AD_PInstance_ID", new Integer(AD_PInstance_ID));
}
/** Get Process Instance.
Instance of the process */
public int getAD_PInstance_ID() 
{
Integer ii = (Integer)get_Value("AD_PInstance_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Product.
Product, Service, Item */
public void setM_Product_ID (int M_Product_ID)
{
if (M_Product_ID < 1) throw new IllegalArgumentException ("M_Product_ID is mandatory.");
set_Value ("M_Product_ID", new Integer(M_Product_ID));
}
/** Get Product.
Product, Service, Item */
public int getM_Product_ID() 
{
Integer ii = (Integer)get_Value("M_Product_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Warehouse.
Storage Warehouse and Service Point */
public void setM_Warehouse_ID (int M_Warehouse_ID)
{
if (M_Warehouse_ID < 1) throw new IllegalArgumentException ("M_Warehouse_ID is mandatory.");
set_Value ("M_Warehouse_ID", new Integer(M_Warehouse_ID));
}
/** Get Warehouse.
Storage Warehouse and Service Point */
public int getM_Warehouse_ID() 
{
Integer ii = (Integer)get_Value("M_Warehouse_ID");
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
/** Set QTYANDREANI */
public void setQTYANDREANI (BigDecimal QTYANDREANI)
{
set_Value ("QTYANDREANI", QTYANDREANI);
}
/** Get QTYANDREANI */
public BigDecimal getQTYANDREANI() 
{
BigDecimal bd = (BigDecimal)get_Value("QTYANDREANI");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set QTYANDREANICUARENTENA */
public void setQTYANDREANICUARENTENA (BigDecimal QTYANDREANICUARENTENA)
{
set_Value ("QTYANDREANICUARENTENA", QTYANDREANICUARENTENA);
}
/** Get QTYANDREANICUARENTENA */
public BigDecimal getQTYANDREANICUARENTENA() 
{
BigDecimal bd = (BigDecimal)get_Value("QTYANDREANICUARENTENA");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set QTYCUARENTENA */
public void setQTYCUARENTENA (BigDecimal QTYCUARENTENA)
{
set_Value ("QTYCUARENTENA", QTYCUARENTENA);
}
/** Get QTYCUARENTENA */
public BigDecimal getQTYCUARENTENA() 
{
BigDecimal bd = (BigDecimal)get_Value("QTYCUARENTENA");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set QTYTERCEROSAPROBADO */
public void setQTYTERCEROSAPROBADO (BigDecimal QTYTERCEROSAPROBADO)
{
set_Value ("QTYTERCEROSAPROBADO", QTYTERCEROSAPROBADO);
}
/** Get QTYTERCEROSAPROBADO */
public BigDecimal getQTYTERCEROSAPROBADO() 
{
BigDecimal bd = (BigDecimal)get_Value("QTYTERCEROSAPROBADO");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set QUANTITY */
public void setQUANTITY (BigDecimal QUANTITY)
{
set_Value ("QUANTITY", QUANTITY);
}
/** Get QUANTITY */
public BigDecimal getQUANTITY() 
{
BigDecimal bd = (BigDecimal)get_Value("QUANTITY");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Available Quantity.
Available Quantity (On Hand - Reserved) */
public void setQtyAvailable (BigDecimal QtyAvailable)
{
set_Value ("QtyAvailable", QtyAvailable);
}
/** Get Available Quantity.
Available Quantity (On Hand - Reserved) */
public BigDecimal getQtyAvailable() 
{
BigDecimal bd = (BigDecimal)get_Value("QtyAvailable");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set On Hand Quantity.
On Hand Quantity */
public void setQtyOnHand (BigDecimal QtyOnHand)
{
set_Value ("QtyOnHand", QtyOnHand);
}
/** Get On Hand Quantity.
On Hand Quantity */
public BigDecimal getQtyOnHand() 
{
BigDecimal bd = (BigDecimal)get_Value("QtyOnHand");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Qty Requiered */
public void setQtyRequiered (BigDecimal QtyRequiered)
{
set_Value ("QtyRequiered", QtyRequiered);
}
/** Get Qty Requiered */
public BigDecimal getQtyRequiered() 
{
BigDecimal bd = (BigDecimal)get_Value("QtyRequiered");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Reserved Quantity.
Reserved Quantity */
public void setQtyReserved (BigDecimal QtyReserved)
{
set_Value ("QtyReserved", QtyReserved);
}
/** Get Reserved Quantity.
Reserved Quantity */
public BigDecimal getQtyReserved() 
{
BigDecimal bd = (BigDecimal)get_Value("QtyReserved");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set T_PRODUCTCHECK_DET_ID */
public void setT_PRODUCTCHECK_DET_ID (int T_PRODUCTCHECK_DET_ID)
{
if (T_PRODUCTCHECK_DET_ID < 1) throw new IllegalArgumentException ("T_PRODUCTCHECK_DET_ID is mandatory.");
set_ValueNoCheck ("T_PRODUCTCHECK_DET_ID", new Integer(T_PRODUCTCHECK_DET_ID));
}
/** Get T_PRODUCTCHECK_DET_ID */
public int getT_PRODUCTCHECK_DET_ID() 
{
Integer ii = (Integer)get_Value("T_PRODUCTCHECK_DET_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
