/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2003 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import java.io.Serializable;
import org.compiere.util.*;
/** Generated Model for MPC_Facility
 ** @version $Id: X_MPC_Facility.java,v 1.1 2004/02/27 19:49:00 sklakken Exp $ */
public class X_MPC_Facility extends PO
{
/** Standard Constructor */
public X_MPC_Facility (Properties ctx, int MPC_Facility_ID)
{
super (ctx, MPC_Facility_ID);
/** if (MPC_Facility_ID == 0)
{
setC_Location_ID (0);
setMPC_Facility_ID (0);
setName (null);
setValue (null);
}
 */
}
/** Load Constructor */
public X_MPC_Facility (Properties ctx, ResultSet rs)
{
super (ctx, rs);
}
public static final int Table_ID=666;

/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_MPC_Facility[").append(getID()).append("]");
return sb.toString();
}
/** Set Location / Address.
Location or Address */
public void setC_Location_ID (int C_Location_ID)
{
setValue ("C_Location_ID", new Integer(C_Location_ID));
}
/** Get Location / Address.
Location or Address */
public int getC_Location_ID() 
{
Integer ii = (Integer)getValue("C_Location_ID");
if (ii == null) return 0;
return ii.intValue();
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
/** Set Facility.
Production Facility */
void setMPC_Facility_ID (int MPC_Facility_ID)
{
setValueNoCheck ("MPC_Facility_ID", new Integer(MPC_Facility_ID));
}
/** Get Facility.
Production Facility */
public int getMPC_Facility_ID() 
{
Integer ii = (Integer)getValue("MPC_Facility_ID");
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
/** Set Resource.
Resource */
public void setS_Resource_ID (int S_Resource_ID)
{
if (S_Resource_ID == 0) setValue ("S_Resource_ID", null);
 else 
setValue ("S_Resource_ID", new Integer(S_Resource_ID));
}
/** Get Resource.
Resource */
public int getS_Resource_ID() 
{
Integer ii = (Integer)getValue("S_Resource_ID");
if (ii == null) return 0;
return ii.intValue();
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