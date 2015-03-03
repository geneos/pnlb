/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Invoice
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.437
 */
public class X_C_Invoice extends PO {
	/** Standard Constructor */
	public X_C_Invoice(Properties ctx, int C_Invoice_ID, String trxName) {
		super(ctx, C_Invoice_ID, trxName);
		/**
		 * if (C_Invoice_ID == 0) { setC_BPartner_ID (0);
		 * setC_BPartner_Location_ID (0); setC_Currency_ID (0); //
		 * 
		 * @C_Currency_ID@ setC_DocTypeTarget_ID (0); setC_DocType_ID (0); // 0
		 *                 setC_Invoice_ID (0); setC_PaymentTerm_ID (0);
		 *                 setDateAcct (new
		 *                 Timestamp(System.currentTimeMillis())); //
		 * @#Date@ setDateInvoiced (new Timestamp(System.currentTimeMillis())); //
		 * @#Date@ setDocAction (null); // CO setDocStatus (null); // DR
		 *         setDocumentNo (null); setGrandTotal (Env.ZERO); setIsApproved
		 *         (false); //
		 * @IsApproved@ setIsDiscountPrinted (false); setIsInDispute (false); //
		 *              N setIsPaid (false); setIsPayScheduleValid (false);
		 *              setIsPrinted (false); setIsSOTrx (false); //
		 * @IsSOTrx@ setIsSelfService (false); setIsTaxIncluded (false);
		 *           setIsTransferred (false); setM_PriceList_ID (0);
		 *           setPaymentRule (null); // P setPosted (false); // N
		 *           setProcessed (false); setSendEMail (false); setTotalLines
		 *           (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_C_Invoice(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Invoice */
	public static final String Table_Name = "C_Invoice";

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
		StringBuffer sb = new StringBuffer("X_C_Invoice[").append(get_ID())
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

	// --------------------
	/**
	 * Set Delivery . Identifies a Jurisdicci�n
	 */
	public void setC_Jurisdiccion_ID(int C_Jurisdiccion_ID) {
		if (C_Jurisdiccion_ID < 1)
			throw new IllegalArgumentException(
					"C_Jurisdiccion_ID is mandatory.");
		set_Value("C_JURISDICCION_ID", new Integer(C_Jurisdiccion_ID));
	}

	/**
	 * Get Delivery . Identifies a Jurisdicci�n
	 */
	public int getC_Jurisdiccion_ID() {
		Integer ii = (Integer) get_Value("C_JURISDICCION_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	// --------------------

	/**
	 * Set Delivery . Identifies a Jurisdiccion
	 */
	public void setC_Location_ID(int C_Location_ID) {
		if (C_Location_ID < 1)
			throw new IllegalArgumentException("C_Location_ID is mandatory.");
		set_Value("C_Location_ID", new Integer(C_Location_ID));
	}

	/**
	 * Get Delivery . Identifies a Jurisdiccion
	 */
	public int getC_Location_ID() {
		Integer ii = (Integer) get_Value("C_Location_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Delivery . Identifies a Jurisdiccion
	 */
	public void setBill_Location_ID(int Bill_Location_ID) {
		set_Value("Bill_Location_ID", new Integer(Bill_Location_ID));
	}

	/**
	 * Get Delivery . Identifies a Jurisdiccion
	 */
	public int getBill_Location_ID() {
		Integer ii = (Integer) get_Value("Bill_Location_ID");
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

	/**
	 * Set Cash Journal Line. Cash Journal Line
	 */
	public void setC_CashLine_ID(int C_CashLine_ID) {
		if (C_CashLine_ID <= 0)
			set_Value("C_CashLine_ID", null);
		else
			set_Value("C_CashLine_ID", new Integer(C_CashLine_ID));
	}

	/**
	 * Get Cash Journal Line. Cash Journal Line
	 */
	public int getC_CashLine_ID() {
		Integer ii = (Integer) get_Value("C_CashLine_ID");
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

	/**
	 * Set Currency Type. Currency Conversion Rate Type
	 */
	public void setC_ConversionType_ID(int C_ConversionType_ID) {
		if (C_ConversionType_ID <= 0)
			set_Value("C_ConversionType_ID", null);
		else
			set_Value("C_ConversionType_ID", new Integer(C_ConversionType_ID));
	}

	/**
	 * Get Currency Type. Currency Conversion Rate Type
	 */
	public int getC_ConversionType_ID() {
		Integer ii = (Integer) get_Value("C_ConversionType_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Currency. The Currency for this record
	 */
	public void setC_Currency_ID(int C_Currency_ID) {
		if (C_Currency_ID < 1)
			throw new IllegalArgumentException("C_Currency_ID is mandatory.");
		set_Value("C_Currency_ID", new Integer(C_Currency_ID));
	}

	/**
	 * Get Currency. The Currency for this record
	 */
	public int getC_Currency_ID() {
		Integer ii = (Integer) get_Value("C_Currency_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** C_DocTypeTarget_ID AD_Reference_ID=170 */
	public static final int C_DOCTYPETARGET_ID_AD_Reference_ID = 170;

	/**
	 * Set Target Document Type. Target document type for conversing documents
	 */
	public void setC_DocTypeTarget_ID(int C_DocTypeTarget_ID) {
		if (C_DocTypeTarget_ID < 1)
			throw new IllegalArgumentException(
					"C_DocTypeTarget_ID is mandatory.");
		set_Value("C_DocTypeTarget_ID", new Integer(C_DocTypeTarget_ID));
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
		if (C_Invoice_ID < 1)
			throw new IllegalArgumentException("C_Invoice_ID is mandatory.");
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
	 * Set Payment Term. The terms of Payment (timing, discount)
	 */
	public void setC_PaymentTerm_ID(int C_PaymentTerm_ID) {
		if (C_PaymentTerm_ID < 1)
			throw new IllegalArgumentException("C_PaymentTerm_ID is mandatory.");
		set_Value("C_PaymentTerm_ID", new Integer(C_PaymentTerm_ID));
	}

	/**
	 * Get Payment Term. The terms of Payment (timing, discount)
	 */
	public int getC_PaymentTerm_ID() {
		Integer ii = (Integer) get_Value("C_PaymentTerm_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Payment. Payment identifier
	 */
	public void setC_Payment_ID(int C_Payment_ID) {
		if (C_Payment_ID <= 0)
			set_Value("C_Payment_ID", null);
		else
			set_Value("C_Payment_ID", new Integer(C_Payment_ID));
	}

	/**
	 * Get Payment. Payment identifier
	 */
	public int getC_Payment_ID() {
		Integer ii = (Integer) get_Value("C_Payment_ID");
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
	 * Set Date Invoiced. Date printed on Invoice
	 */
	public void setDateInvoiced(Timestamp DateInvoiced) {
		if (DateInvoiced == null)
			throw new IllegalArgumentException("DateInvoiced is mandatory.");
		set_Value("DateInvoiced", DateInvoiced);
	}

	/**
	 * Get Date Invoiced. Date printed on Invoice
	 */
	public Timestamp getDateInvoiced() {
		return (Timestamp) get_Value("DateInvoiced");
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
	 * Set Grand Total. Total amount of document
	 */
	public void setGrandTotal(BigDecimal GrandTotal) {
		if (GrandTotal == null)
			throw new IllegalArgumentException("GrandTotal is mandatory.");
		set_ValueNoCheck("GrandTotal", GrandTotal);
	}

	/**
	 * Get Grand Total. Total amount of document
	 */
	public BigDecimal getGrandTotal() {
		BigDecimal bd = (BigDecimal) get_Value("GrandTotal");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Approved. Indicates if this document requires approval
	 */
	public void setIsApproved(boolean IsApproved) {
		set_ValueNoCheck("IsApproved", new Boolean(IsApproved));
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
	 * Set Discount Printed. Print Discount on Invoice and Order
	 */
	public void setIsDiscountPrinted(boolean IsDiscountPrinted) {
		set_Value("IsDiscountPrinted", new Boolean(IsDiscountPrinted));
	}

	/**
	 * Get Discount Printed. Print Discount on Invoice and Order
	 */
	public boolean isDiscountPrinted() {
		Object oo = get_Value("IsDiscountPrinted");
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
	 * Set Paid. The document is paid
	 */
	public void setIsPaid(boolean IsPaid) {
		set_Value("IsPaid", new Boolean(IsPaid));
	}

	/**
	 * Get Paid. The document is paid
	 */
	public boolean isPaid() {
		Object oo = get_Value("IsPaid");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Pay Schedule valid. Is the Payment Schedule is valid
	 */
	public void setIsPayScheduleValid(boolean IsPayScheduleValid) {
		set_ValueNoCheck("IsPayScheduleValid", new Boolean(IsPayScheduleValid));
	}

	/**
	 * Get Pay Schedule valid. Is the Payment Schedule is valid
	 */
	public boolean isPayScheduleValid() {
		Object oo = get_Value("IsPayScheduleValid");
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
		set_ValueNoCheck("IsPrinted", new Boolean(IsPrinted));
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
		set_ValueNoCheck("IsSOTrx", new Boolean(IsSOTrx));
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
	 * Set Self-Service. This is a Self-Service entry or this entry can be
	 * changed via Self-Service
	 */
	public void setIsSelfService(boolean IsSelfService) {
		set_Value("IsSelfService", new Boolean(IsSelfService));
	}

	/**
	 * Get Self-Service. This is a Self-Service entry or this entry can be
	 * changed via Self-Service
	 */
	public boolean isSelfService() {
		Object oo = get_Value("IsSelfService");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Price includes Tax. Tax is included in the price
	 */
	public void setIsTaxIncluded(boolean IsTaxIncluded) {
		set_Value("IsTaxIncluded", new Boolean(IsTaxIncluded));
	}

	/**
	 * Get Price includes Tax. Tax is included in the price
	 */
	public boolean isTaxIncluded() {
		Object oo = get_Value("IsTaxIncluded");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Transferred. Transferred to General Ledger (i.e. accounted)
	 */
	public void setIsTransferred(boolean IsTransferred) {
		set_ValueNoCheck("IsTransferred", new Boolean(IsTransferred));
	}

	/**
	 * Get Transferred. Transferred to General Ledger (i.e. accounted)
	 */
	public boolean isTransferred() {
		Object oo = get_Value("IsTransferred");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Price List. Unique identifier of a Price List
	 */
	public void setM_PriceList_ID(int M_PriceList_ID) {
		if (M_PriceList_ID < 1)
			throw new IllegalArgumentException("M_PriceList_ID is mandatory.");
		set_Value("M_PriceList_ID", new Integer(M_PriceList_ID));
	}

	/**
	 * Get Price List. Unique identifier of a Price List
	 */
	public int getM_PriceList_ID() {
		Integer ii = (Integer) get_Value("M_PriceList_ID");
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

	/** PaymentRule AD_Reference_ID=195 */
	public static final int PAYMENTRULE_AD_Reference_ID = 195;

	/** Cash = B */
	public static final String PAYMENTRULE_Cash = "B";

	/** Direct Debit = D */
	public static final String PAYMENTRULE_DirectDebit = "D";

	/** Credit Card = K */
	public static final String PAYMENTRULE_CreditCard = "K";

	/** On Credit = P */
	public static final String PAYMENTRULE_OnCredit = "P";

	/** Check = S */
	public static final String PAYMENTRULE_Check = "S";

	/** Direct Deposit = T */
	public static final String PAYMENTRULE_DirectDeposit = "T";

	/**
	 * Set Payment Rule. How you pay the invoice
	 */
	public void setPaymentRule(String PaymentRule) {
		/*
		 * if (PaymentRule == null) throw new IllegalArgumentException
		 * ("PaymentRule is mandatory"); if (PaymentRule.equals("B") ||
		 * PaymentRule.equals("D") || PaymentRule.equals("K") ||
		 * PaymentRule.equals("P") || PaymentRule.equals("S") ||
		 * PaymentRule.equals("T")); else throw new IllegalArgumentException
		 * ("PaymentRule Invalid value - " + PaymentRule + " - Reference_ID=195 -
		 * B - D - K - P - S - T"); if (PaymentRule.length() > 1) {
		 * log.warning("Length > 1 - truncated"); PaymentRule =
		 * PaymentRule.substring(0,0); }
		 */
		if (PaymentRule == null)
			PaymentRule = new String("");
		set_Value("PaymentRule", PaymentRule);
	}

	/**
	 * Get Payment Rule. How you pay the invoice
	 */
	public String getPaymentRule() {
		return (String) get_Value("PaymentRule");
	}

	/** Set PercepcionIB */
	public void setPercepcionIB(BigDecimal PercepcionIB) {
		set_Value("PercepcionIB", PercepcionIB);
	}

	/** Get PercepcionIB */
	public BigDecimal getPercepcionIB() {
		BigDecimal bd = (BigDecimal) get_Value("PercepcionIB");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set PercepcionIVA */
	public void setPercepcionIVA(BigDecimal PercepcionIVA) {
		set_Value("PercepcionIVA", PercepcionIVA);
	}

	/** Get PercepcionIVA */
	public BigDecimal getPercepcionIVA() {
		BigDecimal bd = (BigDecimal) get_Value("PercepcionIVA");
		if (bd == null)
			return Env.ZERO;
		return bd;
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
		set_ValueNoCheck("Processed", new Boolean(Processed));
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

	/** Set Referenced Invoice */
	public void setRef_Invoice_ID(int Ref_Invoice_ID) {
		if (Ref_Invoice_ID <= 0)
			set_Value("Ref_Invoice_ID", null);
		else
			set_Value("Ref_Invoice_ID", new Integer(Ref_Invoice_ID));
	}

	/** Get Referenced Invoice */
	public int getRef_Invoice_ID() {
		Integer ii = (Integer) get_Value("Ref_Invoice_ID");
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
	 * Set Total Lines. Total of all document lines
	 */
	public void setTotalLines(BigDecimal TotalLines) {
		if (TotalLines == null)
			throw new IllegalArgumentException("TotalLines is mandatory.");
		set_ValueNoCheck("TotalLines", TotalLines);
	}

	/**
	 * Get Total Lines. Total of all document lines
	 */
	public BigDecimal getTotalLines() {
		BigDecimal bd = (BigDecimal) get_Value("TotalLines");
		if (bd == null)
			return Env.ZERO;
		return bd;
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
	 * Get Lugar de Entrega.
	 */
	public int getC_JURISDICCION_ID() {
		Integer ii = (Integer) get_Value("C_JURISDICCION_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Invoice Ref . */
	public void setC_Invoice_Ref(int C_Invoice_ID) {
		set_Value("C_INVOICE_REF", new Integer(C_Invoice_ID));
	}

	/** Get Invoice Ref . */
	public int getC_Invoice_Ref() {
		Integer ii = (Integer) get_Value("C_INVOICE_REF");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Invoice Ref . */
	public void setCotizacion(BigDecimal cotizacion) {
		set_Value("COTIZACION", cotizacion);
	}

	/** Get Invoice Ref . */
	public BigDecimal getCotizacion() {
		BigDecimal ii = (BigDecimal) get_Value("COTIZACION");
		if (ii == null)
			return BigDecimal.ONE;
		return ii;
	}
	
/**
 * 06-01-2011 Camarzana Mariano
 * Agregado por ticket 128 (Trellis), se necesitaban los getters y setters para copiar la causa de 
 * emision al completar la OV y generar la correspondiente factura 
 */	
	/** Set Invoice CausaEmision . */
	public void setCausaEmision(int C_Causa_Emision_ID) {
		set_Value("C_CAUSA_EMISION_ID", new Integer(C_Causa_Emision_ID));
	}

	/** Get Invoice CausaEmision . */
	public int getCausaEmision() {
		Integer ii = (Integer) get_Value("C_CAUSA_EMISION_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}


}
