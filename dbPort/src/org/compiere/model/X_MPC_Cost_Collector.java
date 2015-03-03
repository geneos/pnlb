/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for MPC_Cost_Collector
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:01.718
 */
public class X_MPC_Cost_Collector extends PO {
	/** Standard Constructor */
	public X_MPC_Cost_Collector(Properties ctx, int MPC_Cost_Collector_ID,
			String trxName) {
		super(ctx, MPC_Cost_Collector_ID, trxName);
		/**
		 * if (MPC_Cost_Collector_ID == 0) { setC_DocTypeTarget_ID (0);
		 * setC_DocType_ID (0); setDateAcct (new
		 * Timestamp(System.currentTimeMillis())); //
		 * 
		 * @#Date@ setMPC_Cost_Collector_ID (0); setMPC_Order_ID (0);
		 *         setM_Locator_ID (0); setM_Product_ID (0); setM_Warehouse_ID
		 *         (0); setMovementDate (new
		 *         Timestamp(System.currentTimeMillis())); //
		 * @#Date@ setMovementQty (Env.ZERO); // 0 setPosted (false);
		 *         setProcessed (false); setS_Resource_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_MPC_Cost_Collector(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=MPC_Cost_Collector */
	public static final String Table_Name = "MPC_Cost_Collector";

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
		StringBuffer sb = new StringBuffer("X_MPC_Cost_Collector[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Trx Organization. Performing or initiating organization
	 */
	public void setAD_OrgTrx_ID(int AD_OrgTrx_ID) {
		if (AD_OrgTrx_ID <= 0)
			set_Value("AD_OrgTrx_ID", null);
		else
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
	 * Set User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public void setAD_User_ID(int AD_User_ID) {
		if (AD_User_ID <= 0)
			set_Value("AD_User_ID", null);
		else
			set_Value("AD_User_ID", new Integer(AD_User_ID));
	}

	/**
	 * Get User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public int getAD_User_ID() {
		Integer ii = (Integer) get_Value("AD_User_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** CAUSECODE AD_Reference_ID=1000040 */
	public static final int CAUSECODE_AD_Reference_ID = 1000040;

	/** PH Alto = AAA */
	public static final String CAUSECODE_PHAlto = "AAA";

	/** Set CAUSECODE */
	public void setCAUSECODE(String CAUSECODE) {
		if (CAUSECODE != null && CAUSECODE.length() > 20) {
			log.warning("Length > 20 - truncated");
			CAUSECODE = CAUSECODE.substring(0, 19);
		}
		set_Value("CAUSECODE", CAUSECODE);
	}

	/** Get CAUSECODE */
	public String getCAUSECODE() {
		return (String) get_Value("CAUSECODE");
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
		if (C_DocType_ID < 0)
			throw new IllegalArgumentException("C_DocType_ID is mandatory.");
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
		if (C_UOM_ID <= 0)
			set_Value("C_UOM_ID", null);
		else
			set_Value("C_UOM_ID", new Integer(C_UOM_ID));
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
	 * Set Account Date. Accounting Date
	 */
	public void setDateAcct(Timestamp DateAcct) {
		if (DateAcct == null)
			throw new IllegalArgumentException("DateAcct is mandatory.");
		set_Value("DateAcct", DateAcct);
	}

	/**
	 * Get Account Date. Accounting Date
	 */
	public Timestamp getDateAcct() {
		return (Timestamp) get_Value("DateAcct");
	}

	/**
	 * Set Description. Optional short description of the record
	 */
	public void setDescription(String Description) {
		if (Description != null && Description.length() > 255) {
			log.warning("Length > 255 - truncated");
			Description = Description.substring(0, 254);
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
		if (DocAction != null && DocAction.length() > 2) {
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
		if (DocStatus != null && DocStatus.length() > 2) {
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

	/** Set Duration Real */
	public void setDurationReal(BigDecimal DurationReal) {
		set_Value("DurationReal", DurationReal);
	}

	/** Get Duration Real */
	public BigDecimal getDurationReal() {
		BigDecimal bd = (BigDecimal) get_Value("DurationReal");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** DurationUnit AD_Reference_ID=299 */
	public static final int DURATIONUNIT_AD_Reference_ID = 299;

	/** Day = D */
	public static final String DURATIONUNIT_Day = "D";

	/** Month = M */
	public static final String DURATIONUNIT_Month = "M";

	/** Year = Y */
	public static final String DURATIONUNIT_Year = "Y";

	/** Hour = h */
	public static final String DURATIONUNIT_Hour = "h";

	/** Minute = m */
	public static final String DURATIONUNIT_Minute = "m";

	/** Second = s */
	public static final String DURATIONUNIT_Second = "s";

	/**
	 * Set Duration Unit. Unit of Duration
	 */
	public void setDurationUnit(String DurationUnit) {
		if (DurationUnit != null && DurationUnit.length() > 20) {
			log.warning("Length > 20 - truncated");
			DurationUnit = DurationUnit.substring(0, 19);
		}
		set_Value("DurationUnit", DurationUnit);
	}

	/**
	 * Get Duration Unit. Unit of Duration
	 */
	public String getDurationUnit() {
		return (String) get_Value("DurationUnit");
	}

	/** Set IsBatchTime */
	public void setIsBatchTime(boolean IsBatchTime) {
		set_Value("IsBatchTime", new Boolean(IsBatchTime));
	}

	/** Get IsBatchTime */
	public boolean isBatchTime() {
		Object oo = get_Value("IsBatchTime");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Is Subcontracting. The operation will be made in an external Work
	 * Center
	 */
	public void setIsSubcontracting(boolean IsSubcontracting) {
		set_Value("IsSubcontracting", new Boolean(IsSubcontracting));
	}

	/**
	 * Get Is Subcontracting. The operation will be made in an external Work
	 * Center
	 */
	public boolean isSubcontracting() {
		Object oo = get_Value("IsSubcontracting");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Cost Collector CMPCS. Cost Collector
	 */
	public void setMPC_Cost_Collector_ID(int MPC_Cost_Collector_ID) {
		if (MPC_Cost_Collector_ID < 1)
			throw new IllegalArgumentException(
					"MPC_Cost_Collector_ID is mandatory.");
		set_ValueNoCheck("MPC_Cost_Collector_ID", new Integer(
				MPC_Cost_Collector_ID));
	}

	/**
	 * Get Cost Collector CMPCS. Cost Collector
	 */
	public int getMPC_Cost_Collector_ID() {
		Integer ii = (Integer) get_Value("MPC_Cost_Collector_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Order BOM Line ID */
	public void setMPC_Order_BOMLine_ID(int MPC_Order_BOMLine_ID) {
		if (MPC_Order_BOMLine_ID <= 0)
			set_Value("MPC_Order_BOMLine_ID", null);
		else
			set_Value("MPC_Order_BOMLine_ID", new Integer(MPC_Order_BOMLine_ID));
	}

	/** Get Order BOM Line ID */
	public int getMPC_Order_BOMLine_ID() {
		Integer ii = (Integer) get_Value("MPC_Order_BOMLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** MPC_Order_ID AD_Reference_ID=1000045 */
	public static final int MPC_ORDER_ID_AD_Reference_ID = 1000045;

	/**
	 * Set Manufacturing Order. Manufacturing Order
	 */
	public void setMPC_Order_ID(int MPC_Order_ID) {
		if (MPC_Order_ID < 1)
			throw new IllegalArgumentException("MPC_Order_ID is mandatory.");
		set_Value("MPC_Order_ID", new Integer(MPC_Order_ID));
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

	/** Set Order Node ID */
	public void setMPC_Order_Node_ID(int MPC_Order_Node_ID) {
		if (MPC_Order_Node_ID <= 0)
			set_Value("MPC_Order_Node_ID", null);
		else
			set_Value("MPC_Order_Node_ID", new Integer(MPC_Order_Node_ID));
	}

	/** Get Order Node ID */
	public int getMPC_Order_Node_ID() {
		Integer ii = (Integer) get_Value("MPC_Order_Node_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Order Workflow */
	public void setMPC_Order_Workflow_ID(int MPC_Order_Workflow_ID) {
		if (MPC_Order_Workflow_ID <= 0)
			set_Value("MPC_Order_Workflow_ID", null);
		else
			set_Value("MPC_Order_Workflow_ID", new Integer(
					MPC_Order_Workflow_ID));
	}

	/** Get Order Workflow */
	public int getMPC_Order_Workflow_ID() {
		Integer ii = (Integer) get_Value("MPC_Order_Workflow_ID");
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
	 * Set Locator. Warehouse Locator
	 */
	public void setM_Locator_ID(int M_Locator_ID) {
		if (M_Locator_ID < 1)
			throw new IllegalArgumentException("M_Locator_ID is mandatory.");
		set_Value("M_Locator_ID", new Integer(M_Locator_ID));
	}

	/**
	 * Get Locator. Warehouse Locator
	 */
	public int getM_Locator_ID() {
		Integer ii = (Integer) get_Value("M_Locator_ID");
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
		set_Value("M_Product_ID", new Integer(M_Product_ID));
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getM_Product_ID()));
	}

	/**
	 * Set Warehouse. Storage Warehouse and Service Point
	 */
	public void setM_Warehouse_ID(int M_Warehouse_ID) {
		if (M_Warehouse_ID < 1)
			throw new IllegalArgumentException("M_Warehouse_ID is mandatory.");
		set_Value("M_Warehouse_ID", new Integer(M_Warehouse_ID));
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
	 * Set Movement Date. Date a product was moved in or out of inventory
	 */
	public void setMovementDate(Timestamp MovementDate) {
		if (MovementDate == null)
			throw new IllegalArgumentException("MovementDate is mandatory.");
		set_Value("MovementDate", MovementDate);
	}

	/**
	 * Get Movement Date. Date a product was moved in or out of inventory
	 */
	public Timestamp getMovementDate() {
		return (Timestamp) get_Value("MovementDate");
	}

	/**
	 * Set Movement Quantity. Quantity of a product moved.
	 */
	public void setMovementQty(BigDecimal MovementQty) {
		if (MovementQty == null)
			throw new IllegalArgumentException("MovementQty is mandatory.");
		set_Value("MovementQty", MovementQty);
	}

	/**
	 * Get Movement Quantity. Quantity of a product moved.
	 */
	public BigDecimal getMovementQty() {
		BigDecimal bd = (BigDecimal) get_Value("MovementQty");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** MovementType AD_Reference_ID=189 */
	public static final int MOVEMENTTYPE_AD_Reference_ID = 189;

	/** Customer Returns = C+ */
	public static final String MOVEMENTTYPE_CustomerReturns = "C+";

	/** Customer Shipment = C- */
	public static final String MOVEMENTTYPE_CustomerShipment = "C-";

	/** Inventory In = I+ */
	public static final String MOVEMENTTYPE_InventoryIn = "I+";

	/** Inventory Out = I- */
	public static final String MOVEMENTTYPE_InventoryOut = "I-";

	/** Movement To = M+ */
	public static final String MOVEMENTTYPE_MovementTo = "M+";

	/** Movement From = M- */
	public static final String MOVEMENTTYPE_MovementFrom = "M-";

	/** Production + = P+ */
	public static final String MOVEMENTTYPE_ProductionPlus = "P+";

	/** Production - = P- */
	public static final String MOVEMENTTYPE_Production_ = "P-";

	/** Vendor Receipts = V+ */
	public static final String MOVEMENTTYPE_VendorReceipts = "V+";

	/** Vendor Returns = V- */
	public static final String MOVEMENTTYPE_VendorReturns = "V-";

	/** Work Order + = W+ */
	public static final String MOVEMENTTYPE_WorkOrderPlus = "W+";

	/** Work Order - = W- */
	public static final String MOVEMENTTYPE_WorkOrder_ = "W-";

	/**
	 * Set Movement Type. Method of moving the inventory
	 */
	public void setMovementType(String MovementType) {
		if (MovementType != null && MovementType.length() > 2) {
			log.warning("Length > 2 - truncated");
			MovementType = MovementType.substring(0, 1);
		}
		set_Value("MovementType", MovementType);
	}

	/**
	 * Get Movement Type. Method of moving the inventory
	 */
	public String getMovementType() {
		return (String) get_Value("MovementType");
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

	/** Set Qty Reject */
	public void setQtyReject(BigDecimal QtyReject) {
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
	 * Set Resource. Resource
	 */
	public void setS_Resource_ID(int S_Resource_ID) {
		if (S_Resource_ID < 1)
			throw new IllegalArgumentException("S_Resource_ID is mandatory.");
		set_Value("S_Resource_ID", new Integer(S_Resource_ID));
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
	 * Set Scrapped Quantity. The Quantity scrapped due to QA issues
	 */
	public void setScrappedQty(BigDecimal ScrappedQty) {
		set_Value("ScrappedQty", ScrappedQty);
	}

	/**
	 * Get Scrapped Quantity. The Quantity scrapped due to QA issues
	 */
	public BigDecimal getScrappedQty() {
		BigDecimal bd = (BigDecimal) get_Value("ScrappedQty");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Setup Time Real */
	public void setSetupTimeReal(BigDecimal SetupTimeReal) {
		set_Value("SetupTimeReal", SetupTimeReal);
	}

	/** Get Setup Time Real */
	public BigDecimal getSetupTimeReal() {
		BigDecimal bd = (BigDecimal) get_Value("SetupTimeReal");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

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

	public void setBUNDLE(String b) {
		set_Value("BUNDLE", b);
	}

	public String getBUNDLE() {
		return (String) get_Value("BUNDLE");
	}
}
