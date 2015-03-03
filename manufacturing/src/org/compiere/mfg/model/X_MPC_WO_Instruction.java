/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2003 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import java.io.Serializable;
import org.compiere.util.*;
/** Generated Model for MPC_WO_Instruction
 ** @version $Id: X_MPC_WO_Instruction.java,v 1.1 2004/02/27 19:49:00 sklakken Exp $ */
public class X_MPC_WO_Instruction extends PO
{
/** Standard Constructor */
public X_MPC_WO_Instruction (Properties ctx, int MPC_WO_Instruction_ID)
{
super (ctx, MPC_WO_Instruction_ID);
/** if (MPC_WO_Instruction_ID == 0)
{
setC_UOM_ID (0);
setMPC_WO_Instruction_ID (0);
setMPC_WO_Task_ID (0);
setName (null);
setQty (0);
setValue (null);
}
 */
}
/** Load Constructor */
public X_MPC_WO_Instruction (Properties ctx, ResultSet rs)
{
super (ctx, rs);
}
public static final int Table_ID=671;

/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_MPC_WO_Instruction[").append(getID()).append("]");
return sb.toString();
}
/** Set UOM.
Unit of Measure */
public void setC_UOM_ID (int C_UOM_ID)
{
setValue ("C_UOM_ID", new Integer(C_UOM_ID));
}
/** Get UOM.
Unit of Measure */
public int getC_UOM_ID() 
{
Integer ii = (Integer)getValue("C_UOM_ID");
if (ii == null) return 0;
return ii.intValue();
}
public static final int INSTRUCTIONTYPE_AD_Reference_ID=1000000;
/** Attribute = AT */
public static final String INSTRUCTIONTYPE_Attribute = "AT";
/** Picklist = PI */
public static final String INSTRUCTIONTYPE_Picklist = "PI";
/** Manual = MA */
public static final String INSTRUCTIONTYPE_Manual = "MA";
/** Custom = CU */
public static final String INSTRUCTIONTYPE_Custom = "CU";
/** Packing = PA */
public static final String INSTRUCTIONTYPE_Packing = "PA";
/** Set Instruction Type.
Manufacturing Instruction Type */
public void setInstructionType (String InstructionType)
{
if (InstructionType == null || InstructionType.equals("AT") || InstructionType.equals("PI") || InstructionType.equals("MA") || InstructionType.equals("CU") || InstructionType.equals("PA"));
 else throw new IllegalArgumentException ("InstructionType Invalid value - Reference_ID=1000000 - AT - PI - MA - CU - PA");
if (InstructionType != null && InstructionType.length() > 2)
{
log.warn("setInstructionType - length > 2 - truncated");
InstructionType = InstructionType.substring(0,1);
}
setValue ("InstructionType", InstructionType);
}
/** Get Instruction Type.
Manufacturing Instruction Type */
public String getInstructionType() 
{
return (String)getValue("InstructionType");
}
/** Set Instruction.
Manufacturing Work Order Instruction */
void setMPC_WO_Instruction_ID (int MPC_WO_Instruction_ID)
{
setValueNoCheck ("MPC_WO_Instruction_ID", new Integer(MPC_WO_Instruction_ID));
}
/** Get Instruction.
Manufacturing Work Order Instruction */
public int getMPC_WO_Instruction_ID() 
{
Integer ii = (Integer)getValue("MPC_WO_Instruction_ID");
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
/** Set Quantity.
Quantity */
public void setQty (int Qty)
{
setValue ("Qty", new Integer(Qty));
}
/** Get Quantity.
Quantity */
public int getQty() 
{
Integer ii = (Integer)getValue("Qty");
if (ii == null) return 0;
return ii.intValue();
}
public static final int TEXT_AD_Reference_ID=1000000;
/** Attribute = AT */
public static final String TEXT_Attribute = "AT";
/** Picklist = PI */
public static final String TEXT_Picklist = "PI";
/** Manual = MA */
public static final String TEXT_Manual = "MA";
/** Custom = CU */
public static final String TEXT_Custom = "CU";
/** Packing = PA */
public static final String TEXT_Packing = "PA";
/** Set Text.
Info Text */
public void setText (String Text)
{
if (Text == null || Text.equals("AT") || Text.equals("PI") || Text.equals("MA") || Text.equals("CU") || Text.equals("PA"));
 else throw new IllegalArgumentException ("Text Invalid value - Reference_ID=1000000 - AT - PI - MA - CU - PA");
if (Text != null && Text.length() > 2000)
{
log.warn("setText - length > 2000 - truncated");
Text = Text.substring(0,1999);
}
setValue ("Text", Text);
}
/** Get Text.
Info Text */
public String getText() 
{
return (String)getValue("Text");
}
/** Set Search Key.
Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory");
if (Value.length() > 2000)
{
log.warn("setValue - length > 2000 - truncated");
Value = Value.substring(0,1999);
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
