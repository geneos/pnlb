/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for MPC_Order
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-01-22 12:09:22.312
 */
public class X_MPC_Order extends PO {
	/** Standard Constructor */
	public X_MPC_Order(Properties ctx, int MPC_Order_ID, String trxName) {
		super(ctx, MPC_Order_ID, trxName);
		/**
		 * if (MPC_Order_ID == 0) { setAD_OrgTrx_ID (0); setAD_Workflow_ID (0);
		 * setC_DocTypeTarget_ID (0); // 0 setC_UOM_ID (0); //
		 * 
		 * @UOMConversion@=Y |
		 * @Processed@='Y' setDateOrdered (new
		 *                 Timestamp(System.currentTimeMillis())); //
		 * @#Date@ setDatePromised (new Timestamp(System.currentTimeMillis()));
		 *         setDocAction (null); // -- setDocStatus (null); // DR
		 *         setDocumentNo (null); setIsApproved (false); // N
		 *         setIsPrinted (false); // N setIsSOTrx (false); // N
		 *         setIsSelected (false); // N setLine (0); //
		 * @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM MPC_Order WHERE
		 *             MPC_Order_ID=@MPC_Order_ID@ setMPC_Order_ID (0);
		 *             setMPC_Product_BOM_ID (0); setM_Product_ID (0);
		 *             setM_Warehouse_ID (0); setPriorityRule (null);
		 *             setProcessed (false); // N setQtyDelivered (Env.ZERO); //
		 *             0 setQtyOrdered (Env.ZERO); // 1 setQtyReject (Env.ZERO); //
		 *             0 setQtyScrap (Env.ZERO); // 0 setS_Resource_ID (0);
		 *             setScheduleType (null); setYield (Env.ZERO); // 100 }
		 */
	}

	/** Load Constructor */
	public X_MPC_Order(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=MPC_Order */
	public static final String Table_Name = "MPC_Order";

	/** AD_Table_ID */
	public int Table_ID;

	protected KeyNamePair Model;

	/** Load Meta Data */
	protected POInfo initPO(Properties ctx) {
		POInfo info = initPO(ctx, Table_Name);
		Table_ID = info.getAD_Table_ID();
		Model = new KeyNamePair(Table_ID, Table_Name);
		return info;
	}

	protected BigDecimal accessLevel = new BigDecimal(3);

	/** AccessLevel 3 - Client - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_MPC_Order[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** AD_OrgTrx_ID AD_Reference_ID=130 */
	public static final int AD_ORGTRX_ID_AD_Reference_ID = 130;

	/**
	 * Set Trx Organization. Performing or initiating organization
	 */
	public void setAD_OrgTrx_ID(int AD_OrgTrx_ID) {
		if (AD_OrgTrx_ID < 1)
			throw new IllegalArgumentException("AD_OrgTrx_ID is mandatory.");
		set_Value("AD_OrgTrx_ID", new Integer(AD_OrgTrx_ID));
	}

	/**
	 * Get Trx Organization. Performing or initiating organization
	 */
	public int getAD_OrgTrx_ID() {
		Integer ii = (Integer) get_Value("AD_OrgTrx_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Workflow. Workflow or combination of tasks
	 */
	public void setAD_Workflow_ID(int AD_Workflow_ID) {
		if (AD_Workflow_ID < 1)
			throw new IllegalArgumentException("AD_Workflow_ID is mandatory.");
		set_ValueNoCheck("AD_Workflow_ID", new Integer(AD_Workflow_ID));
	}

	/**
	 * Get Workflow. Workflow or combination of tasks
	 */
	public int getAD_Workflow_ID() {
		Integer ii = (Integer) get_Value("AD_Workflow_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Assay */
	public void setAssay(BigDecimal Assay) {
		set_Value("Assay", Assay);
	}

	/** Get Assay */
	public BigDecimal getAssay() {
		BigDecimal bd = (BigDecimal) get_Value("Assay");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Attr_Order_Rel_ID */
	public void setAttr_Order_Rel_ID(String Attr_Order_Rel_ID) {
		throw new IllegalArgumentException(
				"Attr_Order_Rel_ID is virtual column");
	}

	/** Get Attr_Order_Rel_ID */
	public String getAttr_Order_Rel_ID() {
		return (String) get_Value("Attr_Order_Rel_ID");
	}

	/** Set Attr_Order_Rel_Lot_ID */
	public void setAttr_Order_Rel_Lot(String Attr_Order_Rel_Lot) {
		throw new IllegalArgumentException(
				"Attr_Order_Rel_Lot is virtual column");
	}

	/** Get Attr_Order_Rel_Lot_ID */
	public String getAttr_Order_Rel_Lot() {
		return (String) get_Value("Attr_Order_Rel_Lot");
	}

	/**
	 * Set Activity. Business Activity
	 */
	public void setC_Activity_ID(int C_Activity_ID) {
		if (C_Activity_ID <= 0)
			set_Value("C_Activity_ID", null);
		else
			set_Value("C_Activity_ID", new Integer(C_Activity_ID));
	}

	/**
	 * Get Activity. Business Activity
	 */
	public int getC_Activity_ID() {
		Integer ii = (Integer) get_Value("C_Activity_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Campaign. Marketing Campaign
	 */
	public void setC_Campaign_ID(int C_Campaign_ID) {
		if (C_Campaign_ID <= 0)
			set_Value("C_Campaign_ID", null);
		else
			set_Value("C_Campaign_ID", new Integer(C_Campaign_ID));
	}

	/**
	 * Get Campaign. Marketing Campaign
	 */
	public int getC_Campaign_ID() {
		Integer ii = (Integer) get_Value("C_Campaign_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** C_DocTypeTarget_ID AD_Reference_ID=1000012 */
	public static final int C_DOCTYPETARGET_ID_AD_Reference_ID = 1000012;

	/**
	 * Set Target Document Type. Target document type for conversing documents
	 */
	public void setC_DocTypeTarget_ID(int C_DocTypeTarget_ID) {
		if (C_DocTypeTarget_ID < 1)
			throw new IllegalArgumentException(
					"C_DocTypeTarget_ID is mandatory.");
		set_ValueNoCheck("C_DocTypeTarget_ID", new Integer(C_DocTypeTarget_ID));
	}

	/**
	 * Get Target Document Type. Target document type for conversing documents
	 */
	public int getC_DocTypeTarget_ID() {
		Integer ii = (Integer) get_Value("C_DocTypeTarget_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** C_DocType_ID AD_Reference_ID=1000012 */
	public static final int C_DOCTYPE_ID_AD_Reference_ID = 1000012;

	/**
	 * Set Document Type. Document type or rules
	 */
	public void setC_DocType_ID(int C_DocType_ID) {
		if (C_DocType_ID <= 0)
			set_Value("C_DocType_ID", null);
		else
			set_Value("C_DocType_ID", new Integer(C_DocType_ID));
	}

	/**
	 * Get Document Type. Document type or rules
	 */
	public int getC_DocType_ID() {
		Integer ii = (Integer) get_Value("C_DocType_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Sales Order Line. Sales Order Line
	 */
	public void setC_OrderLine_ID(int C_OrderLine_ID) {
		if (C_OrderLine_ID <= 0)
			set_Value("C_OrderLine_ID", null);
		else
			set_Value("C_OrderLine_ID", new Integer(C_OrderLine_ID));
	}

	/**
	 * Get Sales Order Line. Sales Order Line
	 */
	public int getC_OrderLine_ID() {
		Integer ii = (Integer) get_Value("C_OrderLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Project. Financial Project
	 */
	public void setC_Project_ID(int C_Project_ID) {
		if (C_Project_ID <= 0)
			set_Value("C_Project_ID", null);
		else
			set_Value("C_Project_ID", new Integer(C_Project_ID));
	}

	/**
	 * Get Project. Financial Project
	 */
	public int getC_Project_ID() {
		Integer ii = (Integer) get_Value("C_Project_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set UOM. Unit of Measure
	 */
	public void setC_UOM_ID(int C_UOM_ID) {
		if (C_UOM_ID < 1)
			throw new IllegalArgumentException("C_UOM_ID is mandatory.");
		set_ValueNoCheck("C_UOM_ID", new Integer(C_UOM_ID));
	}

	/**
	 * Get UOM. Unit of Measure
	 */
	public int getC_UOM_ID() {
		Integer ii = (Integer) get_Value("C_UOM_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Copy From. Copy From Record
	 */
	public void setCopyFrom(String CopyFrom) {
		if (CopyFrom != null && CopyFrom.length() > 1) {
			log.warning("Length > 1 - truncated");
			CopyFrom = CopyFrom.substring(0, 0);
		}
		set_Value("CopyFrom", CopyFrom);
	}

	/**
	 * Get Copy From. Copy From Record
	 */
	public String getCopyFrom() {
		return (String) get_Value("CopyFrom");
	}

	/**
	 * Set Date Confirm. Date when the planned order was Confirmed from MRP.
	 */
	public void setDateConfirm(Timestamp DateConfirm) {
		set_ValueNoCheck("DateConfirm", DateConfirm);
	}

	/**
	 * Get Date Confirm. Date when the planned order was Confirmed from MRP.
	 */
	public Timestamp getDateConfirm() {
		return (Timestamp) get_Value("DateConfirm");
	}

	/**
	 * Set Date Delivered. Date when the product was delivered
	 */
	public void setDateDelivered(Timestamp DateDelivered) {
		set_ValueNoCheck("DateDelivered", DateDelivered);
	}

	/**
	 * Get Date Delivered. Date when the product was delivered
	 */
	public Timestamp getDateDelivered() {
		return (Timestamp) get_Value("DateDelivered");
	}

	/**
	 * Set Finish Date. Finish or (planned) completion date
	 */
	public void setDateFinish(Timestamp DateFinish) {
		set_ValueNoCheck("DateFinish", DateFinish);
	}

	/**
	 * Get Finish Date. Finish or (planned) completion date
	 */
	public Timestamp getDateFinish() {
		return (Timestamp) get_Value("DateFinish");
	}

	/**
	 * Set Finish Date Scheduled. Last date of the schedule.
	 */
	public void setDateFinishSchedule(Timestamp DateFinishSchedule) {
		set_Value("DateFinishSchedule", DateFinishSchedule);
	}

	/**
	 * Get Finish Date Scheduled. Last date of the schedule.
	 */
	public Timestamp getDateFinishSchedule() {
		return (Timestamp) get_Value("DateFinishSchedule");
	}

	/**
	 * Set Date Ordered. Date of Order
	 */
	public void setDateOrdered(Timestamp DateOrdered) {
		if (DateOrdered == null)
			throw new IllegalArgumentException("DateOrdered is mandatory.");
		set_Value("DateOrdered", DateOrdered);
	}

	/**
	 * Get Date Ordered. Date of Order
	 */
	public Timestamp getDateOrdered() {
		return (Timestamp) get_Value("DateOrdered");
	}

	/**
	 * Set Date Promised. Date Order was promised
	 */
	public void setDatePromised(Timestamp DatePromised) {
		if (DatePromised == null)
			throw new IllegalArgumentException("DatePromised is mandatory.");
		set_Value("DatePromised", DatePromised);
	}

	/**
	 * Get Date Promised. Date Order was promised
	 */
	public Timestamp getDatePromised() {
		return (Timestamp) get_Value("DatePromised");
	}

	/**
	 * Set Start Date. Starting Date
	 */
	public void setDateStart(Timestamp DateStart) {
		set_ValueNoCheck("DateStart", DateStart);
	}

	/**
	 * Get Start Date. Starting Date
	 */
	public Timestamp getDateStart() {
		return (Timestamp) get_Value("DateStart");
	}

	/**
	 * Set Starting Date Scheduled. Starting Date Scheduled
	 */
	public void setDateStartSchedule(Timestamp DateStartSchedule) {
		set_Value("DateStartSchedule", DateStartSchedule);
	}

	/**
	 * Get Starting Date Scheduled. Starting Date Scheduled
	 */
	public Timestamp getDateStartSchedule() {
		return (Timestamp) get_Value("DateStartSchedule");
	}

	/**
	 * Set Description. Optional short description of the record
	 */
	public void setDescription(String Description) {
		if (Description != null && Description.length() > 510) {
			log.warning("Length > 510 - truncated");
			Description = Description.substring(0, 509);
		}
		set_Value("Description", Description);
	}

	/**
	 * Get Description. Optional short description of the record
	 */
	public String getDescription() {
		return (String) get_Value("Description");
	}

	/** DocAction AD_Reference_ID=135 */
	public static final int DOCACTION_AD_Reference_ID = 135;

	/** <None> = -- */
	public static final String DOCACTION_None = "--";

	/** Approve = AP */
	public static final String DOCACTION_Approve = "AP";

	/** Close = CL */
	public static final String DOCACTION_Close = "CL";

	/** Complete = CO */
	public static final String DOCACTION_Complete = "CO";

	/** Invalidate = IN */
	public static final String DOCACTION_Invalidate = "IN";

	/** Post = PO */
	public static final String DOCACTION_Post = "PO";

	/** Prepare = PR */
	public static final String DOCACTION_Prepare = "PR";

	/** Reverse - Accrual = RA */
	public static final String DOCACTION_Reverse_Accrual = "RA";

	/** Reverse - Correct = RC */
	public static final String DOCACTION_Reverse_Correct = "RC";

	/** Re-activate = RE */
	public static final String DOCACTION_Re_Activate = "RE";

	/** Reject = RJ */
	public static final String DOCACTION_Reject = "RJ";

	/** Void = VO */
	public static final String DOCACTION_Void = "VO";

	/** Wait Complete = WC */
	public static final String DOCACTION_WaitComplete = "WC";

	/** Unlock = XL */
	public static final String DOCACTION_Unlock = "XL";

	/**
	 * Set Document Action. The targeted status of the document
	 */
	public void setDocAction(String DocAction) {
		if (DocAction == null)
			throw new IllegalArgumentException("DocAction is mandatory");
		if (DocAction.equals("--") || DocAction.equals("AP")
				|| DocAction.equals("CL") || DocAction.equals("CO")
				|| DocAction.equals("IN") || DocAction.equals("PO")
				|| DocAction.equals("PR") || DocAction.equals("RA")
				|| DocAction.equals("RC") || DocAction.equals("RE")
				|| DocAction.equals("RJ") || DocAction.equals("VO")
				|| DocAction.equals("WC") || DocAction.equals("XL"))
			;
		else
			throw new IllegalArgumentException(
					"DocAction Invalid value - "
							+ DocAction
							+ " - Reference_ID=135 - -- - AP - CL - CO - IN - PO - PR - RA - RC - RE - RJ - VO - WC - XL");
		if (DocAction.length() > 2) {
			log.warning("Length > 2 - truncated");
			DocAction = DocAction.substring(0, 1);
		}
		set_Value("DocAction", DocAction);
	}

	/**
	 * Get Document Action. The targeted status of the document
	 */
	public String getDocAction() {
		return (String) get_Value("DocAction");
	}

	/** DocStatus AD_Reference_ID=131 */
	public static final int DOCSTATUS_AD_Reference_ID = 131;

	/** Unknown = ?? */
	public static final String DOCSTATUS_Unknown = "??";

	/** Approved = AP */
	public static final String DOCSTATUS_Approved = "AP";

	/** Closed = CL */
	public static final String DOCSTATUS_Closed = "CL";

	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";

	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";

	/** Invalid = IN */
	public static final String DOCSTATUS_Invalid = "IN";

	/** In Progress = IP */
	public static final String DOCSTATUS_InProgress = "IP";

	/** Not Approved = NA */
	public static final String DOCSTATUS_NotApproved = "NA";

	/** Reversed = RE */
	public static final String DOCSTATUS_Reversed = "RE";

	/** Voided = VO */
	public static final String DOCSTATUS_Voided = "VO";

	/** Waiting Confirmation = WC */
	public static final String DOCSTATUS_WaitingConfirmation = "WC";

	/** Waiting Payment = WP */
	public static final String DOCSTATUS_WaitingPayment = "WP";

	/**
	 * Set Document Status. The current status of the document
	 */
	public void setDocStatus(String DocStatus) {
		if (DocStatus == null)
			throw new IllegalArgumentException("DocStatus is mandatory");
		if (DocStatus.equals("??") || DocStatus.equals("AP")
				|| DocStatus.equals("CL") || DocStatus.equals("CO")
				|| DocStatus.equals("DR") || DocStatus.equals("IN")
				|| DocStatus.equals("IP") || DocStatus.equals("NA")
				|| DocStatus.equals("RE") || DocStatus.equals("VO")
				|| DocStatus.equals("WC") || DocStatus.equals("WP"))
			;
		else
			throw new IllegalArgumentException(
					"DocStatus Invalid value - "
							+ DocStatus
							+ " - Reference_ID=131 - ?? - AP - CL - CO - DR - IN - IP - NA - RE - VO - WC - WP");
		if (DocStatus.length() > 2) {
			log.warning("Length > 2 - truncated");
			DocStatus = DocStatus.substring(0, 1);
		}
		set_Value("DocStatus", DocStatus);
	}

	/**
	 * Get Document Status. The current status of the document
	 */
	public String getDocStatus() {
		return (String) get_Value("DocStatus");
	}

	/**
	 * Set Document No. Document sequence number of the document
	 */
	public void setDocumentNo(String DocumentNo) {
		if (DocumentNo == null)
			throw new IllegalArgumentException("DocumentNo is mandatory.");
		if (DocumentNo.length() > 60) {
			log.warning("Length > 60 - truncated");
			DocumentNo = DocumentNo.substring(0, 59);
		}
		set_Value("DocumentNo", DocumentNo);
	}

	/**
	 * Get Document No. Document sequence number of the document
	 */
	public String getDocumentNo() {
		return (String) get_Value("DocumentNo");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getDocumentNo());
	}

	/** Set Float After */
	public void setFloatAfter(BigDecimal FloatAfter) {
		set_Value("FloatAfter", FloatAfter);
	}

	/** Get Float After */
	public BigDecimal getFloatAfter() {
		BigDecimal bd = (BigDecimal) get_Value("FloatAfter");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Float Before */
	public void setFloatBefored(BigDecimal FloatBefored) {
		set_Value("FloatBefored", FloatBefored);
	}

	/** Get Float Before */
	public BigDecimal getFloatBefored() {
		BigDecimal bd = (BigDecimal) get_Value("FloatBefored");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Approved. Indicates if this document requires approval
	 */
	public void setIsApproved(boolean IsApproved) {
		set_Value("IsApproved", new Boolean(IsApproved));
	}

	/**
	 * Get Approved. Indicates if this document requires approval
	 */
	public boolean isApproved() {
		Object oo = get_Value("IsApproved");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Printed. Indicates if this document / line is printed
	 */
	public void setIsPrinted(boolean IsPrinted) {
		set_Value("IsPrinted", new Boolean(IsPrinted));
	}

	/**
	 * Get Printed. Indicates if this document / line is printed
	 */
	public boolean isPrinted() {
		Object oo = get_Value("IsPrinted");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsQtyPercentage */
	public void setIsQtyPercentage(boolean IsQtyPercentage) {
		set_Value("IsQtyPercentage", new Boolean(IsQtyPercentage));
	}

	/** Get IsQtyPercentage */
	public boolean isQtyPercentage() {
		Object oo = get_Value("IsQtyPercentage");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Sales Transaction. This is a Sales Transaction
	 */
	public void setIsSOTrx(boolean IsSOTrx) {
		set_Value("IsSOTrx", new Boolean(IsSOTrx));
	}

	/**
	 * Get Sales Transaction. This is a Sales Transaction
	 */
	public boolean isSOTrx() {
		Object oo = get_Value("IsSOTrx");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Selected */
	public void setIsSelected(boolean IsSelected) {
		set_Value("IsSelected", new Boolean(IsSelected));
	}

	/** Get Selected */
	public boolean isSelected() {
		Object oo = get_Value("IsSelected");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Line No. Unique line for this document
	 */
	public void setLine(int Line) {
		set_Value("Line", new Integer(Line));
	}

	/**
	 * Get Line No. Unique line for this document
	 */
	public int getLine() {
		Integer ii = (Integer) get_Value("Line");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Lot No. Lot number (alphanumeric)
	 */
	public void setLot(String Lot) {
		if (Lot != null && Lot.length() > 20) {
			log.warning("Length > 20 - truncated");
			Lot = Lot.substring(0, 19);
		}
		set_Value("Lot", Lot);
	}

	/**
	 * Get Lot No. Lot number (alphanumeric)
	 */
	public String getLot() {
		return (String) get_Value("Lot");
	}

	/** Set Lote */
	public void setLote(String Lote) {
		throw new IllegalArgumentException("Lote is virtual column");
	}

	/** Get Lote */
	public String getLote() {
		return (String) get_Value("Lote");
	}

	/** MPC_Order_ID AD_Reference_ID=1000045 */
	public static final int MPC_ORDER_ID_AD_Reference_ID = 1000045;

	/**
	 * Set Manufacturing Order. Manufacturing Order
	 */
	public void setMPC_Order_ID(int MPC_Order_ID) {
		if (MPC_Order_ID < 1)
			throw new IllegalArgumentException("MPC_Order_ID is mandatory.");
		set_ValueNoCheck("MPC_Order_ID", new Integer(MPC_Order_ID));
	}

	/**
	 * Get Manufacturing Order. Manufacturing Order
	 */
	public int getMPC_Order_ID() {
		Integer ii = (Integer) get_Value("MPC_Order_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set BOM & Formula */
	public void setMPC_Product_BOM_ID(int MPC_Product_BOM_ID) {
		if (MPC_Product_BOM_ID < 1)
			throw new IllegalArgumentException(
					"MPC_Product_BOM_ID is mandatory.");
		set_ValueNoCheck("MPC_Product_BOM_ID", new Integer(MPC_Product_BOM_ID));
	}

	/** Get BOM & Formula */
	public int getMPC_Product_BOM_ID() {
		Integer ii = (Integer) get_Value("MPC_Product_BOM_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Attribute Set Instance. Product Attribute Set Instance
	 */
	public void setM_AttributeSetInstance_ID(int M_AttributeSetInstance_ID) {
		if (M_AttributeSetInstance_ID <= 0)
			set_Value("M_AttributeSetInstance_ID", null);
		else
			set_Value("M_AttributeSetInstance_ID", new Integer(
					M_AttributeSetInstance_ID));
	}

	/**
	 * Get Attribute Set Instance. Product Attribute Set Instance
	 */
	public int getM_AttributeSetInstance_ID() {
		Integer ii = (Integer) get_Value("M_AttributeSetInstance_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Product. Product, Service, Item
	 */
	public void setM_Product_ID(int M_Product_ID) {
		if (M_Product_ID < 1)
			throw new IllegalArgumentException("M_Product_ID is mandatory.");
		set_ValueNoCheck("M_Product_ID", new Integer(M_Product_ID));
	}

	/**
	 * Get Product. Product, Service, Item
	 */
	public int getM_Product_ID() {
		Integer ii = (Integer) get_Value("M_Product_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Warehouse. Storage Warehouse and Service Point
	 */
	public void setM_Warehouse_ID(int M_Warehouse_ID) {
		if (M_Warehouse_ID < 1)
			throw new IllegalArgumentException("M_Warehouse_ID is mandatory.");
		set_ValueNoCheck("M_Warehouse_ID", new Integer(M_Warehouse_ID));
	}

	/**
	 * Get Warehouse. Storage Warehouse and Service Point
	 */
	public int getM_Warehouse_ID() {
		Integer ii = (Integer) get_Value("M_Warehouse_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Order Type. Order type: discrete, reprocessing, expenses, repetitive
	 */
	public void setOrderType(String OrderType) {
		if (OrderType != null && OrderType.length() > 1) {
			log.warning("Length > 1 - truncated");
			OrderType = OrderType.substring(0, 0);
		}
		set_Value("OrderType", OrderType);
	}

	/**
	 * Get Order Type. Order type: discrete, reprocessing, expenses, repetitive
	 */
	public String getOrderType() {
		return (String) get_Value("OrderType");
	}

	/** Planner_ID AD_Reference_ID=286 */
	public static final int PLANNER_ID_AD_Reference_ID = 286;

	/**
	 * Set Planner . ID of the person responsible of planning the product.
	 */
	public void setPlanner_ID(int Planner_ID) {
		if (Planner_ID <= 0)
			set_Value("Planner_ID", null);
		else
			set_Value("Planner_ID", new Integer(Planner_ID));
	}

	/**
	 * Get Planner . ID of the person responsible of planning the product.
	 */
	public int getPlanner_ID() {
		Integer ii = (Integer) get_Value("Planner_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Posted. Posting status
	 */
	public void setPosted(boolean Posted) {
		set_Value("Posted", new Boolean(Posted));
	}

	/**
	 * Get Posted. Posting status
	 */
	public boolean isPosted() {
		Object oo = get_Value("Posted");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** PriorityRule AD_Reference_ID=154 */
	public static final int PRIORITYRULE_AD_Reference_ID = 154;

	/** Urgent = 1 */
	public static final String PRIORITYRULE_Urgent = "1";

	/** High = 3 */
	public static final String PRIORITYRULE_High = "3";

	/** Medium = 5 */
	public static final String PRIORITYRULE_Medium = "5";

	/** Low = 7 */
	public static final String PRIORITYRULE_Low = "7";

	/** Minor = 9 */
	public static final String PRIORITYRULE_Minor = "9";

	/**
	 * Set Priority. Priority of a document
	 */
	public void setPriorityRule(String PriorityRule) {
		if (PriorityRule == null)
			throw new IllegalArgumentException("PriorityRule is mandatory");
		if (PriorityRule.equals("1") || PriorityRule.equals("3")
				|| PriorityRule.equals("5") || PriorityRule.equals("7")
				|| PriorityRule.equals("9"))
			;
		else
			throw new IllegalArgumentException("PriorityRule Invalid value - "
					+ PriorityRule + " - Reference_ID=154 - 1 - 3 - 5 - 7 - 9");
		if (PriorityRule.length() > 1) {
			log.warning("Length > 1 - truncated");
			PriorityRule = PriorityRule.substring(0, 0);
		}
		set_Value("PriorityRule", PriorityRule);
	}

	/**
	 * Get Priority. Priority of a document
	 */
	public String getPriorityRule() {
		return (String) get_Value("PriorityRule");
	}

	/**
	 * Set Processed. The document has been processed
	 */
	public void setProcessed(boolean Processed) {
		set_Value("Processed", new Boolean(Processed));
	}

	/**
	 * Get Processed. The document has been processed
	 */
	public boolean isProcessed() {
		Object oo = get_Value("Processed");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Process Now */
	public void setProcessing(boolean Processing) {
		set_Value("Processing", new Boolean(Processing));
	}

	/** Get Process Now */
	public boolean isProcessing() {
		Object oo = get_Value("Processing");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Qty Batch Size */
	public void setQtyBatchSize(BigDecimal QtyBatchSize) {
		set_ValueNoCheck("QtyBatchSize", QtyBatchSize);
	}

	/** Get Qty Batch Size */
	public BigDecimal getQtyBatchSize() {
		BigDecimal bd = (BigDecimal) get_Value("QtyBatchSize");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Qty Batchs */
	public void setQtyBatchs(BigDecimal QtyBatchs) {
		set_ValueNoCheck("QtyBatchs", QtyBatchs);
	}

	/** Get Qty Batchs */
	public BigDecimal getQtyBatchs() {
		BigDecimal bd = (BigDecimal) get_Value("QtyBatchs");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Delivered Quantity. Delivered Quantity
	 */
	public void setQtyDelivered(BigDecimal QtyDelivered) {
		if (QtyDelivered == null)
			throw new IllegalArgumentException("QtyDelivered is mandatory.");
		set_Value("QtyDelivered", QtyDelivered);
	}

	/**
	 * Get Delivered Quantity. Delivered Quantity
	 */
	public BigDecimal getQtyDelivered() {
		BigDecimal bd = (BigDecimal) get_Value("QtyDelivered");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Quantity. The Quantity Entered is based on the selected UoM
	 */
	public void setQtyEntered(BigDecimal QtyEntered) {
		set_Value("QtyEntered", QtyEntered);
	}

	/**
	 * Get Quantity. The Quantity Entered is based on the selected UoM
	 */
	public BigDecimal getQtyEntered() {
		BigDecimal bd = (BigDecimal) get_Value("QtyEntered");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Ordered Quantity. Ordered Quantity
	 */
	public void setQtyOrdered(BigDecimal QtyOrdered) {
		if (QtyOrdered == null)
			throw new IllegalArgumentException("QtyOrdered is mandatory.");
		set_ValueNoCheck("QtyOrdered", QtyOrdered);
	}

	/**
	 * Get Ordered Quantity. Ordered Quantity
	 */
	public BigDecimal getQtyOrdered() {
		BigDecimal bd = (BigDecimal) get_Value("QtyOrdered");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Qty Reject */
	public void setQtyReject(BigDecimal QtyReject) {
		if (QtyReject == null)
			throw new IllegalArgumentException("QtyReject is mandatory.");
		set_Value("QtyReject", QtyReject);
	}

	/** Get Qty Reject */
	public BigDecimal getQtyReject() {
		BigDecimal bd = (BigDecimal) get_Value("QtyReject");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Reserved Quantity. Reserved Quantity
	 */
	public void setQtyReserved(BigDecimal QtyReserved) {
		set_Value("QtyReserved", QtyReserved);
	}

	/**
	 * Get Reserved Quantity. Reserved Quantity
	 */
	public BigDecimal getQtyReserved() {
		BigDecimal bd = (BigDecimal) get_Value("QtyReserved");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Qty Scrap */
	public void setQtyScrap(BigDecimal QtyScrap) {
		if (QtyScrap == null)
			throw new IllegalArgumentException("QtyScrap is mandatory.");
		set_Value("QtyScrap", QtyScrap);
	}

	/** Get Qty Scrap */
	public BigDecimal getQtyScrap() {
		BigDecimal bd = (BigDecimal) get_Value("QtyScrap");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Resource. Resource
	 */
	public void setS_Resource_ID(int S_Resource_ID) {
		if (S_Resource_ID < 1)
			throw new IllegalArgumentException("S_Resource_ID is mandatory.");
		set_ValueNoCheck("S_Resource_ID", new Integer(S_Resource_ID));
	}

	/**
	 * Get Resource. Resource
	 */
	public int getS_Resource_ID() {
		Integer ii = (Integer) get_Value("S_Resource_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Schedule Type. Type of schedule
	 */
	public void setScheduleType(String ScheduleType) {
		if (ScheduleType == null)
			throw new IllegalArgumentException("ScheduleType is mandatory.");
		if (ScheduleType.length() > 1) {
			log.warning("Length > 1 - truncated");
			ScheduleType = ScheduleType.substring(0, 0);
		}
		set_Value("ScheduleType", ScheduleType);
	}

	/**
	 * Get Schedule Type. Type of schedule
	 */
	public String getScheduleType() {
		return (String) get_Value("ScheduleType");
	}

	/**
	 * Set Serial No. Product Serial Number
	 */
	public void setSerNo(String SerNo) {
		if (SerNo != null && SerNo.length() > 20) {
			log.warning("Length > 20 - truncated");
			SerNo = SerNo.substring(0, 19);
		}
		set_Value("SerNo", SerNo);
	}

	/**
	 * Get Serial No. Product Serial Number
	 */
	public String getSerNo() {
		return (String) get_Value("SerNo");
	}

	/** User1_ID AD_Reference_ID=134 */
	public static final int USER1_ID_AD_Reference_ID = 134;

	/**
	 * Set User List 1. User defined list element #1
	 */
	public void setUser1_ID(int User1_ID) {
		if (User1_ID <= 0)
			set_Value("User1_ID", null);
		else
			set_Value("User1_ID", new Integer(User1_ID));
	}

	/**
	 * Get User List 1. User defined list element #1
	 */
	public int getUser1_ID() {
		Integer ii = (Integer) get_Value("User1_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** User2_ID AD_Reference_ID=137 */
	public static final int USER2_ID_AD_Reference_ID = 137;

	/**
	 * Set User List 2. User defined list element #2
	 */
	public void setUser2_ID(int User2_ID) {
		if (User2_ID <= 0)
			set_Value("User2_ID", null);
		else
			set_Value("User2_ID", new Integer(User2_ID));
	}

	/**
	 * Get User List 2. User defined list element #2
	 */
	public int getUser2_ID() {
		Integer ii = (Integer) get_Value("User2_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Value */
	public void setValue(String Value) {
		throw new IllegalArgumentException("Value is virtual column");
	}

	/** Get Value */
	public String getValue() {
		return (String) get_Value("Value");
	}

	/** Set Value2 */
	public void setValue2(String Value2) {
		throw new IllegalArgumentException("Value2 is virtual column");
	}

	/** Get Value2 */
	public String getValue2() {
		return (String) get_Value("Value2");
	}

	/** Set Yield */
	public void setYield(BigDecimal Yield) {
		if (Yield == null)
			throw new IllegalArgumentException("Yield is mandatory.");
		set_Value("Yield", Yield);
	}

	/** Get Yield */
	public BigDecimal getYield() {
		BigDecimal bd = (BigDecimal) get_Value("Yield");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * BISion - 22/10/2009 - Santiago Ibañez COM-PAN-REQ-02.002.01
	 */
	public void setBUNDLE(String bundle) {
		set_Value("BUNDLE", bundle);
	}

	public String getBUNDLE() {
		return (String) get_Value("BUNDLE");
	}
        
        /**
	 * Set AttributeSetInstanceDesc. Optional short description of the M_AttributeSetInstance
	 */
	public void setAttributeSetInstanceDesc(String AttributeSetInstanceDesc) {
		if (AttributeSetInstanceDesc != null && AttributeSetInstanceDesc.length() > 50) {
			log.warning("Length > 50 - truncated");
			AttributeSetInstanceDesc = AttributeSetInstanceDesc.substring(0, 49);
		}
		set_Value("AttributeSetInstanceDesc", AttributeSetInstanceDesc);
	}

	/**
	 * Get AttributeSetInstanceDesc. Optional short description of the M_AttributeSetInstance
	 */
	public String getAttributeSetInstanceDesc() {
		return (String) get_Value("AttributeSetInstanceDesc");
	}

}
