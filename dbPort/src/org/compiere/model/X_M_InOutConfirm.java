/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_InOutConfirm
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.859
 */
public class X_M_InOutConfirm extends PO {
	/** Standard Constructor */
	public X_M_InOutConfirm(Properties ctx, int M_InOutConfirm_ID,
			String trxName) {
		super(ctx, M_InOutConfirm_ID, trxName);
		/**
		 * if (M_InOutConfirm_ID == 0) { setConfirmType (null); setDocAction
		 * (null); // CO setDocStatus (null); // DR setDocumentNo (null);
		 * setIsApproved (false); setIsCancelled (false); setIsInDispute
		 * (false); // N setM_InOutConfirm_ID (0); setM_InOut_ID (0);
		 * setProcessed (false); }
		 */
	}

	/** Load Constructor */
	public X_M_InOutConfirm(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_InOutConfirm */
	public static final String Table_Name = "M_InOutConfirm";

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

	protected BigDecimal accessLevel = new BigDecimal(1);

	/** AccessLevel 1 - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_M_InOutConfirm[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Approval Amount. Document Approval Amount
	 */
	public void setApprovalAmt(BigDecimal ApprovalAmt) {
		set_Value("ApprovalAmt", ApprovalAmt);
	}

	/**
	 * Get Approval Amount. Document Approval Amount
	 */
	public BigDecimal getApprovalAmt() {
		BigDecimal bd = (BigDecimal) get_Value("ApprovalAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Invoice. Invoice Identifier
	 */
	public void setC_Invoice_ID(int C_Invoice_ID) {
		if (C_Invoice_ID <= 0)
			set_Value("C_Invoice_ID", null);
		else
			set_Value("C_Invoice_ID", new Integer(C_Invoice_ID));
	}

	/**
	 * Get Invoice. Invoice Identifier
	 */
	public int getC_Invoice_ID() {
		Integer ii = (Integer) get_Value("C_Invoice_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** ConfirmType AD_Reference_ID=320 */
	public static final int CONFIRMTYPE_AD_Reference_ID = 320;

	/** Drop Ship Confirm = DS */
	public static final String CONFIRMTYPE_DropShipConfirm = "DS";

	/** Pick/QA Confirm = PC */
	public static final String CONFIRMTYPE_PickQAConfirm = "PC";

	/** Ship/Receipt Confirm = SC */
	public static final String CONFIRMTYPE_ShipReceiptConfirm = "SC";

	/** Customer Confirmation = XC */
	public static final String CONFIRMTYPE_CustomerConfirmation = "XC";

	/** Vendor Confirmation = XV */
	public static final String CONFIRMTYPE_VendorConfirmation = "XV";

	/**
	 * Set Confirmation Type. Type of confirmation
	 */
	public void setConfirmType(String ConfirmType) {
		if (ConfirmType == null)
			throw new IllegalArgumentException("ConfirmType is mandatory");
		if (ConfirmType.equals("DS") || ConfirmType.equals("PC")
				|| ConfirmType.equals("SC") || ConfirmType.equals("XC")
				|| ConfirmType.equals("XV"))
			;
		else
			throw new IllegalArgumentException("ConfirmType Invalid value - "
					+ ConfirmType
					+ " - Reference_ID=320 - DS - PC - SC - XC - XV");
		if (ConfirmType.length() > 2) {
			log.warning("Length > 2 - truncated");
			ConfirmType = ConfirmType.substring(0, 1);
		}
		set_Value("ConfirmType", ConfirmType);
	}

	/**
	 * Get Confirmation Type. Type of confirmation
	 */
	public String getConfirmType() {
		return (String) get_Value("ConfirmType");
	}

	/**
	 * Set Confirmation No. Confirmation Number
	 */
	public void setConfirmationNo(String ConfirmationNo) {
		if (ConfirmationNo != null && ConfirmationNo.length() > 20) {
			log.warning("Length > 20 - truncated");
			ConfirmationNo = ConfirmationNo.substring(0, 19);
		}
		set_Value("ConfirmationNo", ConfirmationNo);
	}

	/**
	 * Get Confirmation No. Confirmation Number
	 */
	public String getConfirmationNo() {
		return (String) get_Value("ConfirmationNo");
	}

	/** Set Create Package */
	public void setCreatePackage(String CreatePackage) {
		if (CreatePackage != null && CreatePackage.length() > 1) {
			log.warning("Length > 1 - truncated");
			CreatePackage = CreatePackage.substring(0, 0);
		}
		set_Value("CreatePackage", CreatePackage);
	}

	/** Get Create Package */
	public String getCreatePackage() {
		return (String) get_Value("CreatePackage");
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
		if (DocumentNo.length() > 30) {
			log.warning("Length > 30 - truncated");
			DocumentNo = DocumentNo.substring(0, 29);
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
	 * Set Cancelled. The transaction was cancelled
	 */
	public void setIsCancelled(boolean IsCancelled) {
		set_Value("IsCancelled", new Boolean(IsCancelled));
	}

	/**
	 * Get Cancelled. The transaction was cancelled
	 */
	public boolean isCancelled() {
		Object oo = get_Value("IsCancelled");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set In Dispute. Document is in dispute
	 */
	public void setIsInDispute(boolean IsInDispute) {
		set_Value("IsInDispute", new Boolean(IsInDispute));
	}

	/**
	 * Get In Dispute. Document is in dispute
	 */
	public boolean isInDispute() {
		Object oo = get_Value("IsInDispute");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Ship/Receipt Confirmation. Material Shipment or Receipt Confirmation
	 */
	public void setM_InOutConfirm_ID(int M_InOutConfirm_ID) {
		if (M_InOutConfirm_ID < 1)
			throw new IllegalArgumentException(
					"M_InOutConfirm_ID is mandatory.");
		set_ValueNoCheck("M_InOutConfirm_ID", new Integer(M_InOutConfirm_ID));
	}

	/**
	 * Get Ship/Receipt Confirmation. Material Shipment or Receipt Confirmation
	 */
	public int getM_InOutConfirm_ID() {
		Integer ii = (Integer) get_Value("M_InOutConfirm_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Shipment/Receipt. Material Shipment Document
	 */
	public void setM_InOut_ID(int M_InOut_ID) {
		if (M_InOut_ID < 1)
			throw new IllegalArgumentException("M_InOut_ID is mandatory.");
		set_ValueNoCheck("M_InOut_ID", new Integer(M_InOut_ID));
	}

	/**
	 * Get Shipment/Receipt. Material Shipment Document
	 */
	public int getM_InOut_ID() {
		Integer ii = (Integer) get_Value("M_InOut_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Phys.Inventory. Parameters for a Physical Inventory
	 */
	public void setM_Inventory_ID(int M_Inventory_ID) {
		if (M_Inventory_ID <= 0)
			set_Value("M_Inventory_ID", null);
		else
			set_Value("M_Inventory_ID", new Integer(M_Inventory_ID));
	}

	/**
	 * Get Phys.Inventory. Parameters for a Physical Inventory
	 */
	public int getM_Inventory_ID() {
		Integer ii = (Integer) get_Value("M_Inventory_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
}
