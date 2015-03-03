/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2003 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import java.io.Serializable;
import org.compiere.util.*;
/** Generated Model for MPC_WorkOrder
 ** @version $Id: X_MPC_WorkOrder.java,v 1.1 2004/02/27 19:49:00 sklakken Exp $ */
public class X_MPC_WorkOrder extends PO
{
/** Standard Constructor */
public X_MPC_WorkOrder (Properties ctx, int MPC_WorkOrder_ID)
{
super (ctx, MPC_WorkOrder_ID);
/** if (MPC_WorkOrder_ID == 0)
{
setC_OrderLine_ID (0);
setC_UOM_ID (0);
setDocAction (null);
setDocStatus (null);	// DR
setDocumentNo (null);
setIsApproved (false);	// @IsApproved@
setIsPrinted (false);
setIsSelected (false);
setMPC_WorkOrder_ID (0);
setM_Product_ID (0);
setProcessed (false);
}
 */
}
/** Load Constructor */
public X_MPC_WorkOrder (Properties ctx, ResultSet rs)
{
super (ctx, rs);
}
public static final int Table_ID=670;

/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_MPC_WorkOrder[").append(getID()).append("]");
return sb.toString();
}
/** Set Actual End Date.
Actual End Date */
public void setActualEndDate (Timestamp ActualEndDate)
{
setValue ("ActualEndDate", ActualEndDate);
}
/** Get Actual End Date.
Actual End Date */
public Timestamp getActualEndDate() 
{
return (Timestamp)getValue("ActualEndDate");
}
/** Set Actual Start Date.
Actual Start Date */
public void setActualStartDate (Timestamp ActualStartDate)
{
setValue ("ActualStartDate", ActualStartDate);
}
/** Get Actual Start Date.
Actual Start Date */
public Timestamp getActualStartDate() 
{
return (Timestamp)getValue("ActualStartDate");
}
/** Set Sales Order Line.
Sales Order Line */
void setC_OrderLine_ID (int C_OrderLine_ID)
{
setValueNoCheck ("C_OrderLine_ID", new Integer(C_OrderLine_ID));
}
/** Get Sales Order Line.
Sales Order Line */
public int getC_OrderLine_ID() 
{
Integer ii = (Integer)getValue("C_OrderLine_ID");
if (ii == null) return 0;
return ii.intValue();
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
/** Set Date Confirmed.
Date Confirmed */
public void setDateConfirmed (Timestamp DateConfirmed)
{
setValue ("DateConfirmed", DateConfirmed);
}
/** Get Date Confirmed.
Date Confirmed */
public Timestamp getDateConfirmed() 
{
return (Timestamp)getValue("DateConfirmed");
}
/** Set Date Promised.
Date Order was promised */
public void setDatePromised (Timestamp DatePromised)
{
setValue ("DatePromised", DatePromised);
}
/** Get Date Promised.
Date Order was promised */
public Timestamp getDatePromised() 
{
return (Timestamp)getValue("DatePromised");
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
/** Set Document Action.
The targeted status of the document */
public void setDocAction (String DocAction)
{
if (DocAction == null) throw new IllegalArgumentException ("DocAction is mandatory");
if (DocAction.length() > 2)
{
log.warn("setDocAction - length > 2 - truncated");
DocAction = DocAction.substring(0,1);
}
setValue ("DocAction", DocAction);
}
/** Get Document Action.
The targeted status of the document */
public String getDocAction() 
{
return (String)getValue("DocAction");
}
public static final int DOCSTATUS_AD_Reference_ID=131;
/** Drafted = DR */
public static final String DOCSTATUS_Drafted = "DR";
/** Completed = CO */
public static final String DOCSTATUS_Completed = "CO";
/** Approved = AP */
public static final String DOCSTATUS_Approved = "AP";
/** Changed = CH */
public static final String DOCSTATUS_Changed = "CH";
/** Not Approved = NA */
public static final String DOCSTATUS_NotApproved = "NA";
/** Transfer Error = TE */
public static final String DOCSTATUS_TransferError = "TE";
/** Printed = PR */
public static final String DOCSTATUS_Printed = "PR";
/** Transferred = TR */
public static final String DOCSTATUS_Transferred = "TR";
/** Voided = VO */
public static final String DOCSTATUS_Voided = "VO";
/** Inactive = IN */
public static final String DOCSTATUS_Inactive = "IN";
/** Posting Error = PE */
public static final String DOCSTATUS_PostingError = "PE";
/** Posted = PO */
public static final String DOCSTATUS_Posted = "PO";
/** Reversed = RE */
public static final String DOCSTATUS_Reversed = "RE";
/** Closed = CL */
public static final String DOCSTATUS_Closed = "CL";
/** Unknown = ?? */
public static final String DOCSTATUS_Unknown = "??";
/** Being Processed = XX */
public static final String DOCSTATUS_BeingProcessed = "XX";
/** In Progress = IP */
public static final String DOCSTATUS_InProgress = "IP";
/** Waiting Payment = WP */
public static final String DOCSTATUS_WaitingPayment = "WP";
/** Set Document Status.
The current status of the document */
void setDocStatus (String DocStatus)
{
if (DocStatus.equals("DR") || DocStatus.equals("CO") || DocStatus.equals("AP") || DocStatus.equals("CH") || DocStatus.equals("NA") || DocStatus.equals("TE") || DocStatus.equals("PR") || DocStatus.equals("TR") || DocStatus.equals("VO") || DocStatus.equals("IN") || DocStatus.equals("PE") || DocStatus.equals("PO") || DocStatus.equals("RE") || DocStatus.equals("CL") || DocStatus.equals("??") || DocStatus.equals("XX") || DocStatus.equals("IP") || DocStatus.equals("WP"));
 else throw new IllegalArgumentException ("DocStatus Invalid value - Reference_ID=131 - DR - CO - AP - CH - NA - TE - PR - TR - VO - IN - PE - PO - RE - CL - ?? - XX - IP - WP");
if (DocStatus == null) throw new IllegalArgumentException ("DocStatus is mandatory");
if (DocStatus.length() > 2)
{
log.warn("setDocStatus - length > 2 - truncated");
DocStatus = DocStatus.substring(0,1);
}
setValueNoCheck ("DocStatus", DocStatus);
}
/** Get Document Status.
The current status of the document */
public String getDocStatus() 
{
return (String)getValue("DocStatus");
}
/** Set Document No.
Document sequence number of the document */
public void setDocumentNo (String DocumentNo)
{
if (DocumentNo == null) throw new IllegalArgumentException ("DocumentNo is mandatory");
if (DocumentNo.length() > 30)
{
log.warn("setDocumentNo - length > 30 - truncated");
DocumentNo = DocumentNo.substring(0,29);
}
setValue ("DocumentNo", DocumentNo);
}
/** Get Document No.
Document sequence number of the document */
public String getDocumentNo() 
{
return (String)getValue("DocumentNo");
}
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(getID(), getDocumentNo());
}
/** Set Approved.
Indicates if this document requires approval */
public void setIsApproved (boolean IsApproved)
{
setValue ("IsApproved", new Boolean(IsApproved));
}
/** Get Approved.
Indicates if this document requires approval */
public boolean isApproved() 
{
Object oo = getValue("IsApproved");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Printed.
Indicates if this document / line is printed */
public void setIsPrinted (boolean IsPrinted)
{
setValue ("IsPrinted", new Boolean(IsPrinted));
}
/** Get Printed.
Indicates if this document / line is printed */
public boolean isPrinted() 
{
Object oo = getValue("IsPrinted");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Selected */
public void setIsSelected (boolean IsSelected)
{
setValue ("IsSelected", new Boolean(IsSelected));
}
/** Get Selected */
public boolean isSelected() 
{
Object oo = getValue("IsSelected");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
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
/** Set Processed.
The document has been processed */
void setProcessed (boolean Processed)
{
setValueNoCheck ("Processed", new Boolean(Processed));
}
/** Get Processed.
The document has been processed */
public boolean isProcessed() 
{
Object oo = getValue("Processed");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Process Now */
void setProcessing (boolean Processing)
{
setValueNoCheck ("Processing", new Boolean(Processing));
}
/** Get Process Now */
public boolean isProcessing() 
{
Object oo = getValue("Processing");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Scheduled End Date.
Scheduled End Date */
public void setScheduledEndDate (Timestamp ScheduledEndDate)
{
setValue ("ScheduledEndDate", ScheduledEndDate);
}
/** Get Scheduled End Date.
Scheduled End Date */
public Timestamp getScheduledEndDate() 
{
return (Timestamp)getValue("ScheduledEndDate");
}
/** Set Scheduled Start Date.
Scheduled Start Date */
public void setScheduledStartDate (Timestamp ScheduledStartDate)
{
setValue ("ScheduledStartDate", ScheduledStartDate);
}
/** Get Scheduled Start Date.
Scheduled Start Date */
public Timestamp getScheduledStartDate() 
{
return (Timestamp)getValue("ScheduledStartDate");
}
}
