/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2003 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import java.io.Serializable;
import org.compiere.util.*;
/** Generated Model for MPC_Routing
 ** @version $Id: X_MPC_Routing.java,v 1.1 2004/02/27 19:49:00 sklakken Exp $ */
public class X_MPC_Routing extends PO
{
/** Standard Constructor */
public X_MPC_Routing (Properties ctx, int MPC_Routing_ID)
{
super (ctx, MPC_Routing_ID);
/** if (MPC_Routing_ID == 0)
{
setIsDefaultRouting (true);	// Y
setMPC_Routing_ID (0);	// Y
setM_Product_ID (0);
setM_Warehouse_ID (0);
setName (null);
setValue (null);
}
 */
}
/** Load Constructor */
public X_MPC_Routing (Properties ctx, ResultSet rs)
{
super (ctx, rs);
}
public static final int Table_ID=663;

/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_MPC_Routing[").append(getID()).append("]");
return sb.toString();
}
/** Set Description.
Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 255)
{
log.warn("setDescription - length > 255 - truncated");
Description = Description.substring(0,254);
}
setValue ("Description", Description);
}
/** Get Description.
Optional short description of the record */
public String getDescription() 
{
return (String)getValue("Description");
}
/** Set Default Routing.
Product Default Routing */
public void setIsDefaultRouting (boolean IsDefaultRouting)
{
setValue ("IsDefaultRouting", new Boolean(IsDefaultRouting));
}
/** Get Default Routing.
Product Default Routing */
public boolean isDefaultRouting() 
{
Object oo = getValue("IsDefaultRouting");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Routing.
Manufacturing Product Routing */
void setMPC_Routing_ID (int MPC_Routing_ID)
{
setValueNoCheck ("MPC_Routing_ID", new Integer(MPC_Routing_ID));
}
/** Get Routing.
Manufacturing Product Routing */
public int getMPC_Routing_ID() 
{
Integer ii = (Integer)getValue("MPC_Routing_ID");
if (ii == null) return 0;
return ii.intValue();
}
public static final int M_PRODUCT_ID_AD_Reference_ID=171;
/** Set Product.
Product, Service, Item */
public void setM_Product_ID (int M_Product_ID)
{
setValue ("M_Product_ID", new Integer(M_Product_ID));
}
/** Get Product.
Product, Service, Item */
public int getM_Product_ID() 
{
Integer ii = (Integer)getValue("M_Product_ID");
if (ii == null) return 0;
return ii.intValue();
}
public static final int M_WAREHOUSE_ID_AD_Reference_ID=197;
/** Set Warehouse / Service Point.
Storage Warehouse and Service Point */
public void setM_Warehouse_ID (int M_Warehouse_ID)
{
setValue ("M_Warehouse_ID", new Integer(M_Warehouse_ID));
}
/** Get Warehouse / Service Point.
Storage Warehouse and Service Point */
public int getM_Warehouse_ID() 
{
Integer ii = (Integer)getValue("M_Warehouse_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Name.
Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory");
if (Name.length() > 60)
{
log.warn("setName - length > 60 - truncated");
Name = Name.substring(0,59);
}
setValue ("Name", Name);
}
/** Get Name.
Alphanumeric identifier of the entity */
public String getName() 
{
return (String)getValue("Name");
}
/** Set Search Key.
Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory");
if (Value.length() > 40)
{
log.warn("setValue - length > 40 - truncated");
Value = Value.substring(0,39);
}
setValue ("Value", Value);
}
/** Get Search Key.
Search key for the record in the format required - must be unique */
public String getValue() 
{
return (String)getValue("Value");
}
}
