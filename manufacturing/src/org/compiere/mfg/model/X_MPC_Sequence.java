/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2003 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import java.io.Serializable;
import org.compiere.util.*;
/** Generated Model for MPC_Sequence
 ** @version $Id: X_MPC_Sequence.java,v 1.1 2004/02/27 19:49:00 sklakken Exp $ */
public class X_MPC_Sequence extends PO
{
/** Standard Constructor */
public X_MPC_Sequence (Properties ctx, int MPC_Sequence_ID)
{
super (ctx, MPC_Sequence_ID);
/** if (MPC_Sequence_ID == 0)
{
setMPC_Operation_ID (0);
setMPC_Routing_ID (0);
setMPC_Sequence_ID (0);
setMPC_WorkCenter_ID (0);
setMfgSequence (0);
setName (null);
setValue (null);
}
 */
}
/** Load Constructor */
public X_MPC_Sequence (Properties ctx, ResultSet rs)
{
super (ctx, rs);
}
public static final int Table_ID=665;

/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_MPC_Sequence[").append(getID()).append("]");
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
/** Set Routing.
Manufacturing Product Routing */
public void setMPC_Routing_ID (int MPC_Routing_ID)
{
setValue ("MPC_Routing_ID", new Integer(MPC_Routing_ID));
}
/** Get Routing.
Manufacturing Product Routing */
public int getMPC_Routing_ID() 
{
Integer ii = (Integer)getValue("MPC_Routing_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Sequence.
Sequence Step In the Manufacturing Process */
void setMPC_Sequence_ID (int MPC_Sequence_ID)
{
setValueNoCheck ("MPC_Sequence_ID", new Integer(MPC_Sequence_ID));
}
/** Get Sequence.
Sequence Step In the Manufacturing Process */
public int getMPC_Sequence_ID() 
{
Integer ii = (Integer)getValue("MPC_Sequence_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Work Center.
Manufacturing Work Cell */
public void setMPC_WorkCenter_ID (int MPC_WorkCenter_ID)
{
setValue ("MPC_WorkCenter_ID", new Integer(MPC_WorkCenter_ID));
}
/** Get Work Center.
Manufacturing Work Cell */
public int getMPC_WorkCenter_ID() 
{
Integer ii = (Integer)getValue("MPC_WorkCenter_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Sequence.
Sequence number of a manufacturing process step */
public void setMfgSequence (int MfgSequence)
{
setValue ("MfgSequence", new Integer(MfgSequence));
}
/** Get Sequence.
Sequence number of a manufacturing process step */
public int getMfgSequence() 
{
Integer ii = (Integer)getValue("MfgSequence");
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
