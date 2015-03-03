/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2003 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import java.io.Serializable;
import org.compiere.util.*;
/** Generated Model for MPC_WO_Task
 ** @version $Id: X_MPC_WO_Task.java,v 1.1 2004/02/27 19:49:00 sklakken Exp $ */
public class X_MPC_WO_Task extends PO
{
/** Standard Constructor */
public X_MPC_WO_Task (Properties ctx, int MPC_WO_Task_ID)
{
super (ctx, MPC_WO_Task_ID);
/** if (MPC_WO_Task_ID == 0)
{
setMPC_Operation_ID (0);
setMPC_WO_Task_ID (0);
setMPC_WorkOrder_ID (0);
setName (null);
setValue (null);
}
 */
}
/** Load Constructor */
public X_MPC_WO_Task (Properties ctx, ResultSet rs)
{
super (ctx, rs);
}
public static final int Table_ID=672;

/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_MPC_WO_Task[").append(getID()).append("]");
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
/** Set Operation.
Manufacturing Operation */
public void setMPC_Operation_ID (int MPC_Operation_ID)
{
setValue ("MPC_Operation_ID", new Integer(MPC_Operation_ID));
}
/** Get Operation.
Manufacturing Operation */
public int getMPC_Operation_ID() 
{
Integer ii = (Integer)getValue("MPC_Operation_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Task.
Manufacturing Work Order Task */
void setMPC_WO_Task_ID (int MPC_WO_Task_ID)
{
setValueNoCheck ("MPC_WO_Task_ID", new Integer(MPC_WO_Task_ID));
}
/** Get Task.
Manufacturing Work Order Task */
public int getMPC_WO_Task_ID() 
{
Integer ii = (Integer)getValue("MPC_WO_Task_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Work Order.
Manufacturing Work Order */
void setMPC_WorkOrder_ID (int MPC_WorkOrder_ID)
{
setValueNoCheck ("MPC_WorkOrder_ID", new Integer(MPC_WorkOrder_ID));
}
/** Get Work Order.
Manufacturing Work Order */
public int getMPC_WorkOrder_ID() 
{
Integer ii = (Integer)getValue("MPC_WorkOrder_ID");
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
