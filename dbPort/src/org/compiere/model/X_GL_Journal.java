/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for GL_Journal
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:01.125
 */
public class X_GL_Journal extends PO {
	/** Standard Constructor */
	public X_GL_Journal(Properties ctx, int GL_Journal_ID, String trxName) {
		super(ctx, GL_Journal_ID, trxName);
		/**
		 * if (GL_Journal_ID == 0) { setC_AcctSchema_ID (0); //
		 * 
		 * @$C_AcctSchema_ID@ setC_ConversionType_ID (0); setC_Currency_ID (0); //
		 * @C_Currency_ID@ setC_DocType_ID (0); //
		 * @C_DocType_ID@ setC_Period_ID (0); //
		 * @C_Period_ID@ setCurrencyRate (Env.ZERO); // 1 setDateAcct (new
		 *               Timestamp(System.currentTimeMillis())); //
		 * @DateAcct@ setDateDoc (new Timestamp(System.currentTimeMillis())); //
		 * @DateDoc@ setDescription (null); setDocAction (null); // CO
		 *           setDocStatus (null); // DR setDocumentNo (null);
		 *           setGL_Category_ID (0); //
		 * @GL_Category_ID@ setGL_Journal_ID (0); setIsApproved (true); // Y
		 *                  setIsPrinted (false); // N setPosted (false); // N
		 *                  setPostingType (null); //
		 * @PostingType@ setTotalCr (Env.ZERO); // 0 setTotalDr (Env.ZERO); // 0 }
		 */
	}

	/** Load Constructor */
	public X_GL_Journal(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=GL_Journal */
	public static final String Table_Name = "GL_Journal";

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
		StringBuffer sb = new StringBuffer("X_GL_Journal[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Accounting Schema. Rules for accounting
	 */
	public void setC_AcctSchema_ID(int C_AcctSchema_ID) {
		if (C_AcctSchema_ID < 1)
			throw new IllegalArgumentException("C_AcctSchema_ID is mandatory.");
		set_ValueNoCheck("C_AcctSchema_ID", new Integer(C_AcctSchema_ID));
	}

	/**
	 * Get Accounting Schema. Rules for accounting
	 */
	public int getC_AcctSchema_ID() {
		Integer ii = (Integer) get_Value("C_AcctSchema_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Currency Type. Currency Conversion Rate Type
	 */
	public void setC_ConversionType_ID(int C_ConversionType_ID) {
		if (C_ConversionType_ID < 1)
			throw new IllegalArgumentException(
					"C_ConversionType_ID is mandatory.");
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

	/** C_Period_ID AD_Reference_ID=275 */
	public static final int C_PERIOD_ID_AD_Reference_ID = 275;

	/**
	 * Set Period. Period of the Calendar
	 */
	public void setC_Period_ID(int C_Period_ID) {
		if (C_Period_ID < 1)
			throw new IllegalArgumentException("C_Period_ID is mandatory.");
		set_Value("C_Period_ID", new Integer(C_Period_ID));
	}

	/**
	 * Get Period. Period of the Calendar
	 */
	public int getC_Period_ID() {
		Integer ii = (Integer) get_Value("C_Period_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Control Amount. If not zero, the Debit amount of the document must be
	 * equal this amount
	 */
	public void setControlAmt(BigDecimal ControlAmt) {
		set_Value("ControlAmt", ControlAmt);
	}

	/**
	 * Get Control Amount. If not zero, the Debit amount of the document must be
	 * equal this amount
	 */
	public BigDecimal getControlAmt() {
		BigDecimal bd = (BigDecimal) get_Value("ControlAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Rate. Currency Conversion Rate
	 */
	public void setCurrencyRate(BigDecimal CurrencyRate) {
		if (CurrencyRate == null)
			throw new IllegalArgumentException("CurrencyRate is mandatory.");
		set_Value("CurrencyRate", CurrencyRate);
	}

	/**
	 * Get Rate. Currency Conversion Rate
	 */
	public BigDecimal getCurrencyRate() {
		BigDecimal bd = (BigDecimal) get_Value("CurrencyRate");
		if (bd == null)
			return Env.ZERO;
		return bd;
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
	 * Set Document Date. Date of the Document
	 */
	public void setDateDoc(Timestamp DateDoc) {
		if (DateDoc == null)
			throw new IllegalArgumentException("DateDoc is mandatory.");
		set_Value("DateDoc", DateDoc);
	}

	/**
	 * Get Document Date. Date of the Document
	 */
	public Timestamp getDateDoc() {
		return (Timestamp) get_Value("DateDoc");
	}

	/**
	 * Set Description. Optional short description of the record
	 */
	public void setDescription(String Description) {
		if (Description == null)
			throw new IllegalArgumentException("Description is mandatory.");
		if (Description.length() > 255) {
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
	 * Set Budget. General Ledger Budget
	 */
	public void setGL_Budget_ID(int GL_Budget_ID) {
		if (GL_Budget_ID <= 0)
			set_Value("GL_Budget_ID", null);
		else
			set_Value("GL_Budget_ID", new Integer(GL_Budget_ID));
	}

	/**
	 * Get Budget. General Ledger Budget
	 */
	public int getGL_Budget_ID() {
		Integer ii = (Integer) get_Value("GL_Budget_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set GL Category. General Ledger Category
	 */
	public void setGL_Category_ID(int GL_Category_ID) {
		if (GL_Category_ID < 1)
			throw new IllegalArgumentException("GL_Category_ID is mandatory.");
		set_Value("GL_Category_ID", new Integer(GL_Category_ID));
	}

	/**
	 * Get GL Category. General Ledger Category
	 */
	public int getGL_Category_ID() {
		Integer ii = (Integer) get_Value("GL_Category_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Journal Batch. General Ledger Journal Batch
	 */
	public void setGL_JournalBatch_ID(int GL_JournalBatch_ID) {
		if (GL_JournalBatch_ID <= 0)
			set_ValueNoCheck("GL_JournalBatch_ID", null);
		else
			set_ValueNoCheck("GL_JournalBatch_ID", new Integer(
					GL_JournalBatch_ID));
	}

	/**
	 * Get Journal Batch. General Ledger Journal Batch
	 */
	public int getGL_JournalBatch_ID() {
		Integer ii = (Integer) get_Value("GL_JournalBatch_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Journal. General Ledger Journal
	 */
	public void setGL_Journal_ID(int GL_Journal_ID) {
		if (GL_Journal_ID < 1)
			throw new IllegalArgumentException("GL_Journal_ID is mandatory.");
		set_ValueNoCheck("GL_Journal_ID", new Integer(GL_Journal_ID));
	}

	/**
	 * Get Journal. General Ledger Journal
	 */
	public int getGL_Journal_ID() {
		Integer ii = (Integer) get_Value("GL_Journal_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
	 * Set Posted. Posting status
	 */
	public void setPosted(boolean Posted) {
		set_ValueNoCheck("Posted", new Boolean(Posted));
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

	/** PostingType AD_Reference_ID=125 */
	public static final int POSTINGTYPE_AD_Reference_ID = 125;

	/** Actual = A */
	public static final String POSTINGTYPE_Actual = "A";

	/** Budget = B */
	public static final String POSTINGTYPE_Budget = "B";

	/** Commitment = E */
	public static final String POSTINGTYPE_Commitment = "E";

	/** Reservation = R */
	public static final String POSTINGTYPE_Reservation = "R";

	/** Statistical = S */
	public static final String POSTINGTYPE_Statistical = "S";

	/**
	 * Set PostingType. The type of posted amount for the transaction
	 */
	public void setPostingType(String PostingType) {
		if (PostingType == null)
			throw new IllegalArgumentException("PostingType is mandatory");
		if (PostingType.equals("A") || PostingType.equals("B")
				|| PostingType.equals("E") || PostingType.equals("R")
				|| PostingType.equals("S"))
			;
		else
			throw new IllegalArgumentException("PostingType Invalid value - "
					+ PostingType + " - Reference_ID=125 - A - B - E - R - S");
		if (PostingType.length() > 1) {
			log.warning("Length > 1 - truncated");
			PostingType = PostingType.substring(0, 0);
		}
		set_Value("PostingType", PostingType);
	}

	/**
	 * Get PostingType. The type of posted amount for the transaction
	 */
	public String getPostingType() {
		return (String) get_Value("PostingType");
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

	/**
	 * Set Total Credit. Total Credit in document currency
	 */
	public void setTotalCr(BigDecimal TotalCr) {
		if (TotalCr == null)
			throw new IllegalArgumentException("TotalCr is mandatory.");
		set_ValueNoCheck("TotalCr", TotalCr);
	}

	/**
	 * Get Total Credit. Total Credit in document currency
	 */
	public BigDecimal getTotalCr() {
		BigDecimal bd = (BigDecimal) get_Value("TotalCr");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Total Debit. Total debit in document currency
	 */
	public void setTotalDr(BigDecimal TotalDr) {
		if (TotalDr == null)
			throw new IllegalArgumentException("TotalDr is mandatory.");
		set_ValueNoCheck("TotalDr", TotalDr);
	}

	/**
	 * Get Total Debit. Total debit in document currency
	 */
	public BigDecimal getTotalDr() {
		BigDecimal bd = (BigDecimal) get_Value("TotalDr");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
