/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_InOut
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-01-22 12:09:23.0
 */
public class X_M_InOut extends PO {
	/** Standard Constructor */
	public X_M_InOut(Properties ctx, int M_InOut_ID, String trxName) {
		super(ctx, M_InOut_ID, trxName);
		/**
		 * if (M_InOut_ID == 0) { setC_BPartner_ID (0);
		 * setC_BPartner_Location_ID (0); setC_DocType_ID (0); setDateAcct (new
		 * Timestamp(System.currentTimeMillis())); //
		 * 
		 * @#Date@ setDeliveryRule (null); // A setDeliveryViaRule (null); // P
		 *         setDocAction (null); // CO setDocStatus (null); // DR
		 *         setDocumentNo (null); setFreightCostRule (null); // I
		 *         setIsApproved (false); setIsInDispute (false); setIsInTransit
		 *         (false); setIsPrinted (false); setIsSOTrx (false); //
		 * @IsSOTrx@ setM_InOut_ID (0); setM_Warehouse_ID (0); setMovementDate
		 *           (new Timestamp(System.currentTimeMillis())); //
		 * @#Date@ setMovementType (null); setPosted (false); setPriorityRule
		 *         (null); // 5 setProcessed (false); setSendEMail (false); }
		 */
	}

	/** Load Constructor */
	public X_M_InOut(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_InOut */
	public static final String Table_Name = "M_InOut";

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
		StringBuffer sb = new StringBuffer("X_M_InOut[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** AD_OrgTrx_ID AD_Reference_ID=130 */
	public static final int AD_ORGTRX_ID_AD_Reference_ID = 130;

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
	 * Set Business Partner . Identifies a Business Partner
	 */
	public void setC_BPartner_ID(int C_BPartner_ID) {
		if (C_BPartner_ID < 1)
			throw new IllegalArgumentException("C_BPartner_ID is mandatory.");
		set_Value("C_BPartner_ID", new Integer(C_BPartner_ID));
	}

	/**
	 * Get Business Partner . Identifies a Business Partner
	 */
	public int getC_BPartner_ID() {
		Integer ii = (Integer) get_Value("C_BPartner_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Partner Location. Identifies the (ship to) address for this Business
	 * Partner
	 */
	public void setC_BPartner_Location_ID(int C_BPartner_Location_ID) {
		if (C_BPartner_Location_ID < 1)
			throw new IllegalArgumentException(
					"C_BPartner_Location_ID is mandatory.");
		set_Value("C_BPartner_Location_ID", new Integer(C_BPartner_Location_ID));
	}

	/**
	 * Get Partner Location. Identifies the (ship to) address for this Business
	 * Partner
	 */
	public int getC_BPartner_Location_ID() {
		Integer ii = (Integer) get_Value("C_BPartner_Location_ID");
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

	/** C_Charge_ID AD_Reference_ID=200 */
	public static final int C_CHARGE_ID_AD_Reference_ID = 200;

	/**
	 * Set Charge. Additional document charges
	 */
	public void setC_Charge_ID(int C_Charge_ID) {
		if (C_Charge_ID <= 0)
			set_Value("C_Charge_ID", null);
		else
			set_Value("C_Charge_ID", new Integer(C_Charge_ID));
	}

	/**
	 * Get Charge. Additional document charges
	 */
	public int getC_Charge_ID() {
		Integer ii = (Integer) get_Value("C_Charge_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** C_DocType_ID AD_Reference_ID=170 */
	public static final int C_DOCTYPE_ID_AD_Reference_ID = 170;

	/**
	 * Set Document Type. Document type or rules
	 */
	public void setC_DocType_ID(int C_DocType_ID) {
		if (C_DocType_ID < 0)
			throw new IllegalArgumentException("C_DocType_ID is mandatory.");
		set_ValueNoCheck("C_DocType_ID", new Integer(C_DocType_ID));
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
	 * Set Invoice. Invoice Identifier
	 */
	public void setC_Invoice_ID(int C_Invoice_ID) {
		if (C_Invoice_ID <= 0)
			set_ValueNoCheck("C_Invoice_ID", null);
		else
			set_ValueNoCheck("C_Invoice_ID", new Integer(C_Invoice_ID));
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

	/**
	 * Set Delivery . Identifies a Jurisdicci�n
	 */
	public void setLocation_Bill_ID(int Location_Bill_ID) {
		set_Value("LOCATION_BILL_ID", new Integer(Location_Bill_ID));
	}

	/**
	 * Set Delivery . Identifies a Jurisdicci�n
	 */
	public void setC_Location_ID(int C_Location_ID) {
		if (C_Location_ID < 1)
			throw new IllegalArgumentException("C_Location_ID is mandatory.");
		set_Value("C_Location_ID", new Integer(C_Location_ID));
	}

	/**
	 * Set Invoice Location. Business Partner Location for invoicing
	 */
	public void setBill_Location_ID(int Bill_Location_ID) {
		if (Bill_Location_ID <= 0)
			set_Value("Bill_Location_ID", null);
		else
			set_Value("Bill_Location_ID", new Integer(Bill_Location_ID));
	}

	/**
	 * Set Order. Order
	 */
	public void setC_Order_ID(int C_Order_ID) {
		if (C_Order_ID <= 0)
			set_ValueNoCheck("C_Order_ID", null);
		else
			set_ValueNoCheck("C_Order_ID", new Integer(C_Order_ID));
	}

	/**
	 * Get Order. Order
	 */
	public int getC_Order_ID() {
		Integer ii = (Integer) get_Value("C_Order_ID");
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
	 * Set Charge amount. Charge Amount
	 */
	public void setChargeAmt(BigDecimal ChargeAmt) {
		set_Value("ChargeAmt", ChargeAmt);
	}

	/**
	 * Get Charge amount. Charge Amount
	 */
	public BigDecimal getChargeAmt() {
		BigDecimal bd = (BigDecimal) get_Value("ChargeAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Create Confirm */
	public void setCreateConfirm(String CreateConfirm) {
		if (CreateConfirm != null && CreateConfirm.length() > 1) {
			log.warning("Length > 1 - truncated");
			CreateConfirm = CreateConfirm.substring(0, 0);
		}
		set_Value("CreateConfirm", CreateConfirm);
	}

	/** Get Create Confirm */
	public String getCreateConfirm() {
		return (String) get_Value("CreateConfirm");
	}

	/**
	 * Set Create lines from. Process which will generate a new document lines
	 * based on an existing document
	 */
	public void setCreateFrom(String CreateFrom) {
		if (CreateFrom != null && CreateFrom.length() > 1) {
			log.warning("Length > 1 - truncated");
			CreateFrom = CreateFrom.substring(0, 0);
		}
		set_Value("CreateFrom", CreateFrom);
	}

	/**
	 * Get Create lines from. Process which will generate a new document lines
	 * based on an existing document
	 */
	public String getCreateFrom() {
		return (String) get_Value("CreateFrom");
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
	 * Set Date Ordered. Date of Order
	 */
	public void setDateOrdered(Timestamp DateOrdered) {
		set_ValueNoCheck("DateOrdered", DateOrdered);
	}

	/**
	 * Get Date Ordered. Date of Order
	 */
	public Timestamp getDateOrdered() {
		return (Timestamp) get_Value("DateOrdered");
	}

	/**
	 * Set Date printed. Date the document was printed.
	 */
	public void setDatePrinted(Timestamp DatePrinted) {
		set_Value("DatePrinted", DatePrinted);
	}

	/**
	 * Get Date printed. Date the document was printed.
	 */
	public Timestamp getDatePrinted() {
		return (Timestamp) get_Value("DatePrinted");
	}

	/**
	 * Set Date received. Date a product was received
	 */
	public void setDateReceived(Timestamp DateReceived) {
		set_Value("DateReceived", DateReceived);
	}

	/**
	 * Get Date received. Date a product was received
	 */
	public Timestamp getDateReceived() {
		return (Timestamp) get_Value("DateReceived");
	}

	/** DeliveryRule AD_Reference_ID=151 */
	public static final int DELIVERYRULE_AD_Reference_ID = 151;

	/** Availability = A */
	public static final String DELIVERYRULE_Availability = "A";

	/** Force = F */
	public static final String DELIVERYRULE_Force = "F";

	/** Complete Line = L */
	public static final String DELIVERYRULE_CompleteLine = "L";

	/** Manual = M */
	public static final String DELIVERYRULE_Manual = "M";

	/** Complete Order = O */
	public static final String DELIVERYRULE_CompleteOrder = "O";

	/** After Receipt = R */
	public static final String DELIVERYRULE_AfterReceipt = "R";

	/**
	 * Set Delivery Rule. Defines the timing of Delivery
	 */
	public void setDeliveryRule(String DeliveryRule) {
		if (DeliveryRule == null)
			throw new IllegalArgumentException("DeliveryRule is mandatory");
		if (DeliveryRule.equals("A") || DeliveryRule.equals("F")
				|| DeliveryRule.equals("L") || DeliveryRule.equals("M")
				|| DeliveryRule.equals("O") || DeliveryRule.equals("R"))
			;
		else
			throw new IllegalArgumentException("DeliveryRule Invalid value - "
					+ DeliveryRule
					+ " - Reference_ID=151 - A - F - L - M - O - R");
		if (DeliveryRule.length() > 1) {
			log.warning("Length > 1 - truncated");
			DeliveryRule = DeliveryRule.substring(0, 0);
		}
		set_Value("DeliveryRule", DeliveryRule);
	}

	/**
	 * Get Delivery Rule. Defines the timing of Delivery
	 */
	public String getDeliveryRule() {
		return (String) get_Value("DeliveryRule");
	}

	/** DeliveryViaRule AD_Reference_ID=152 */
	public static final int DELIVERYVIARULE_AD_Reference_ID = 152;

	/** Delivery = D */
	public static final String DELIVERYVIARULE_Delivery = "D";

	/** Pickup = P */
	public static final String DELIVERYVIARULE_Pickup = "P";

	/** Shipper = S */
	public static final String DELIVERYVIARULE_Shipper = "S";

	/**
	 * Set Delivery Via. How the order will be delivered
	 */
	public void setDeliveryViaRule(String DeliveryViaRule) {
		if (DeliveryViaRule == null)
			throw new IllegalArgumentException("DeliveryViaRule is mandatory");
		if (DeliveryViaRule.equals("D") || DeliveryViaRule.equals("P")
				|| DeliveryViaRule.equals("S"))
			;
		else
			throw new IllegalArgumentException(
					"DeliveryViaRule Invalid value - " + DeliveryViaRule
							+ " - Reference_ID=152 - D - P - S");
		if (DeliveryViaRule.length() > 1) {
			log.warning("Length > 1 - truncated");
			DeliveryViaRule = DeliveryViaRule.substring(0, 0);
		}
		set_Value("DeliveryViaRule", DeliveryViaRule);
	}

	/**
	 * Get Delivery Via. How the order will be delivered
	 */
	public String getDeliveryViaRule() {
		return (String) get_Value("DeliveryViaRule");
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
		set_ValueNoCheck("DocumentNo", DocumentNo);
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
	 * Set Freight Amount. Freight Amount
	 */
	public void setFreightAmt(BigDecimal FreightAmt) {
		set_Value("FreightAmt", FreightAmt);
	}

	/**
	 * Get Freight Amount. Freight Amount
	 */
	public BigDecimal getFreightAmt() {
		BigDecimal bd = (BigDecimal) get_Value("FreightAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** FreightCostRule AD_Reference_ID=153 */
	public static final int FREIGHTCOSTRULE_AD_Reference_ID = 153;

	/** Calculated = C */
	public static final String FREIGHTCOSTRULE_Calculated = "C";

	/** Fix price = F */
	public static final String FREIGHTCOSTRULE_FixPrice = "F";

	/** Freight included = I */
	public static final String FREIGHTCOSTRULE_FreightIncluded = "I";

	/** Line = L */
	public static final String FREIGHTCOSTRULE_Line = "L";

	/**
	 * Set Freight Cost Rule. Method for charging Freight
	 */
	public void setFreightCostRule(String FreightCostRule) {
		if (FreightCostRule == null)
			throw new IllegalArgumentException("FreightCostRule is mandatory");
		if (FreightCostRule.equals("C") || FreightCostRule.equals("F")
				|| FreightCostRule.equals("I") || FreightCostRule.equals("L"))
			;
		else
			throw new IllegalArgumentException(
					"FreightCostRule Invalid value - " + FreightCostRule
							+ " - Reference_ID=153 - C - F - I - L");
		if (FreightCostRule.length() > 1) {
			log.warning("Length > 1 - truncated");
			FreightCostRule = FreightCostRule.substring(0, 0);
		}
		set_Value("FreightCostRule", FreightCostRule);
	}

	/**
	 * Get Freight Cost Rule. Method for charging Freight
	 */
	public String getFreightCostRule() {
		return (String) get_Value("FreightCostRule");
	}

	/**
	 * Set Generate To. Generate To
	 */
	public void setGenerateTo(String GenerateTo) {
		if (GenerateTo != null && GenerateTo.length() > 1) {
			log.warning("Length > 1 - truncated");
			GenerateTo = GenerateTo.substring(0, 0);
		}
		set_Value("GenerateTo", GenerateTo);
	}

	/**
	 * Get Generate To. Generate To
	 */
	public String getGenerateTo() {
		return (String) get_Value("GenerateTo");
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
	 * Set In Transit. Movement is in transit
	 */
	public void setIsInTransit(boolean IsInTransit) {
		set_Value("IsInTransit", new Boolean(IsInTransit));
	}

	/**
	 * Get In Transit. Movement is in transit
	 */
	public boolean isInTransit() {
		Object oo = get_Value("IsInTransit");
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
	 * Set Shipper. Method or manner of product delivery
	 */
	public void setM_Shipper_ID(int M_Shipper_ID) {
		if (M_Shipper_ID <= 0)
			set_Value("M_Shipper_ID", null);
		else
			set_Value("M_Shipper_ID", new Integer(M_Shipper_ID));
	}

	/**
	 * Get Shipper. Method or manner of product delivery
	 */
	public int getM_Shipper_ID() {
		Integer ii = (Integer) get_Value("M_Shipper_ID");
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
		if (MovementType == null)
			throw new IllegalArgumentException("MovementType is mandatory");
		if (MovementType.equals("C+") || MovementType.equals("C-")
				|| MovementType.equals("I+") || MovementType.equals("I-")
				|| MovementType.equals("M+") || MovementType.equals("M-")
				|| MovementType.equals("P+") || MovementType.equals("P-")
				|| MovementType.equals("V+") || MovementType.equals("V-")
				|| MovementType.equals("W+") || MovementType.equals("W-"))
			;
		else
			throw new IllegalArgumentException(
					"MovementType Invalid value - "
							+ MovementType
							+ " - Reference_ID=189 - C+ - C- - I+ - I- - M+ - M- - P+ - P- - V+ - V- - W+ - W-");
		if (MovementType.length() > 2) {
			log.warning("Length > 2 - truncated");
			MovementType = MovementType.substring(0, 1);
		}
		set_ValueNoCheck("MovementType", MovementType);
	}

	/**
	 * Get Movement Type. Method of moving the inventory
	 */
	public String getMovementType() {
		return (String) get_Value("MovementType");
	}

	/**
	 * Set No Packages. Number of packages shipped
	 */
	public void setNoPackages(int NoPackages) {
		set_Value("NoPackages", new Integer(NoPackages));
	}

	/**
	 * Get No Packages. Number of packages shipped
	 */
	public int getNoPackages() {
		Integer ii = (Integer) get_Value("NoPackages");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Order Reference. Transaction Reference Number (Sales Order, Purchase
	 * Order) of your Business Partner
	 */
	public void setPOReference(String POReference) {
		if (POReference != null && POReference.length() > 20) {
			log.warning("Length > 20 - truncated");
			POReference = POReference.substring(0, 19);
		}
		set_Value("POReference", POReference);
	}

	/**
	 * Get Order Reference. Transaction Reference Number (Sales Order, Purchase
	 * Order) of your Business Partner
	 */
	public String getPOReference() {
		return (String) get_Value("POReference");
	}

	/**
	 * Set Pick Date. Date/Time when picked for Shipment
	 */
	public void setPickDate(Timestamp PickDate) {
		set_Value("PickDate", PickDate);
	}

	/**
	 * Get Pick Date. Date/Time when picked for Shipment
	 */
	public Timestamp getPickDate() {
		return (Timestamp) get_Value("PickDate");
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

	/** Set Referenced Shipment */
	public void setRef_InOut_ID(int Ref_InOut_ID) {
		if (Ref_InOut_ID <= 0)
			set_Value("Ref_InOut_ID", null);
		else
			set_Value("Ref_InOut_ID", new Integer(Ref_InOut_ID));
	}

	/** Get Referenced Shipment */
	public int getRef_InOut_ID() {
		Integer ii = (Integer) get_Value("Ref_InOut_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** SalesRep_ID AD_Reference_ID=190 */
	public static final int SALESREP_ID_AD_Reference_ID = 190;

	/**
	 * Set Sales Representative. Sales Representative or Company Agent
	 */
	public void setSalesRep_ID(int SalesRep_ID) {
		if (SalesRep_ID <= 0)
			set_Value("SalesRep_ID", null);
		else
			set_Value("SalesRep_ID", new Integer(SalesRep_ID));
	}

	/**
	 * Get Sales Representative. Sales Representative or Company Agent
	 */
	public int getSalesRep_ID() {
		Integer ii = (Integer) get_Value("SalesRep_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Send EMail. Enable sending Document EMail
	 */
	public void setSendEMail(boolean SendEMail) {
		set_Value("SendEMail", new Boolean(SendEMail));
	}

	/**
	 * Get Send EMail. Enable sending Document EMail
	 */
	public boolean isSendEMail() {
		Object oo = get_Value("SendEMail");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Ship Date. Shipment Date/Time
	 */
	public void setShipDate(Timestamp ShipDate) {
		set_Value("ShipDate", ShipDate);
	}

	/**
	 * Get Ship Date. Shipment Date/Time
	 */
	public Timestamp getShipDate() {
		return (Timestamp) get_Value("ShipDate");
	}

	/**
	 * Set Tracking No. Number to track the shipment
	 */
	public void setTrackingNo(String TrackingNo) {
		if (TrackingNo != null && TrackingNo.length() > 60) {
			log.warning("Length > 60 - truncated");
			TrackingNo = TrackingNo.substring(0, 59);
		}
		set_Value("TrackingNo", TrackingNo);
	}

	/**
	 * Get Tracking No. Number to track the shipment
	 */
	public String getTrackingNo() {
		return (String) get_Value("TrackingNo");
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

	/**
	 * BISion - 28/05/2009 - Santiago Ibañez Agregado para verificar si se debe
	 * chequear el umbral o no
	 * 
	 * @param checkumbral
	 */

	public void setCHECKUMBRAL(boolean checkumbral) {
		set_Value("CHECKUMBRAL", new Boolean(checkumbral));
	}

	/**
	 * BISION - 28/05/2009 - Santiago Ibañez Agregado para setear si debe
	 * chequear el umbral o no
	 * 
	 * @return
	 */
	public boolean checkUmbral() {
		Object oo = get_Value("CHECKUMBRAL");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * BISion - 18/06/2009 - Santiago Ibañez Agregado para obtener el valor
	 * declarado
	 * 
	 * @return
	 */
	public BigDecimal getVALORDECLARADO() {
		BigDecimal ii = (BigDecimal) get_Value("VALORDECLARADO");
		if (ii == null)
			return BigDecimal.ZERO;
		return ii;
	}

	/**
	 * BISion - 18/06/2009 - Santiago Ibañez Agregado para setear el valor
	 * declarado
	 * 
	 * @param valorizacion
	 */
	public void setVALORDECLARADO(BigDecimal valorizacion) {
		set_Value("VALORDECLARADO", valorizacion);
	}

	/** Get Referenced Manufacturing Order */
	public int getMPC_Order_ID() {
		Integer ii = (Integer) get_Value("MPC_Order_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Get Referenced Movement */
	public int getM_Movement_ID() {
		Integer ii = (Integer) get_Value("M_Movement_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public void setMPC_Order_ID(int MPC_Order_ID) {
		if (MPC_Order_ID <= 0)
			set_ValueNoCheck("MPC_Order_ID", null);
		else
			set_ValueNoCheck("MPC_Order_ID", new Integer(MPC_Order_ID));
	}

	public void setM_Movement_ID(int M_Movement_ID) {
		if (M_Movement_ID <= 0)
			set_ValueNoCheck("M_Movement_ID", null);
		else
			set_ValueNoCheck("M_Movement_ID", new Integer(M_Movement_ID));
	}
}
