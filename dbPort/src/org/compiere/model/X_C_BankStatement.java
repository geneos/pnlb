/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_BankStatement
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.89
 */
public class X_C_BankStatement extends PO {
	/** Standard Constructor */
	public X_C_BankStatement(Properties ctx, int C_BankStatement_ID,
			String trxName) {
		super(ctx, C_BankStatement_ID, trxName);
		/**
		 * if (C_BankStatement_ID == 0) { setC_BankAccount_ID (0);
		 * setC_BankStatement_ID (0); setDocAction (null); // CO setDocStatus
		 * (null); // DR setEndingBalance (Env.ZERO); setIsApproved (false); //
		 * N setIsManual (true); // Y setName (null); //
		 * 
		 * @#Date@ setPosted (false); // N setProcessed (false);
		 *         setStatementDate (new Timestamp(System.currentTimeMillis())); //
		 * @Date@ }
		 */
	}

	/** Load Constructor */
	public X_C_BankStatement(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_BankStatement */
	public static final String Table_Name = "C_BankStatement";

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
		StringBuffer sb = new StringBuffer("X_C_BankStatement[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Beginning Balance. Balance prior to any transactions
	 */
	public void setBeginningBalance(BigDecimal BeginningBalance) {
		set_Value("BeginningBalance", BeginningBalance);
	}

	/**
	 * Get Beginning Balance. Balance prior to any transactions
	 */
	public BigDecimal getBeginningBalance() {
		BigDecimal bd = (BigDecimal) get_Value("BeginningBalance");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Bank Account. Account at the Bank
	 */
	public void setC_BankAccount_ID(int C_BankAccount_ID) {
		if (C_BankAccount_ID < 1)
			throw new IllegalArgumentException("C_BankAccount_ID is mandatory.");
		set_Value("C_BankAccount_ID", new Integer(C_BankAccount_ID));
	}

	/**
	 * Get Bank Account. Account at the Bank
	 */
	public int getC_BankAccount_ID() {
		Integer ii = (Integer) get_Value("C_BankAccount_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Bank Statement. Bank Statement of account
	 */
	public void setC_BankStatement_ID(int C_BankStatement_ID) {
		if (C_BankStatement_ID < 1)
			throw new IllegalArgumentException(
					"C_BankStatement_ID is mandatory.");
		set_ValueNoCheck("C_BankStatement_ID", new Integer(C_BankStatement_ID));
	}

	/**
	 * Get Bank Statement. Bank Statement of account
	 */
	public int getC_BankStatement_ID() {
		Integer ii = (Integer) get_Value("C_BankStatement_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
	 * Set EFT Statement Date. Electronic Funds Transfer Statement Date
	 */
	public void setEftStatementDate(Timestamp EftStatementDate) {
		set_Value("EftStatementDate", EftStatementDate);
	}

	/**
	 * Get EFT Statement Date. Electronic Funds Transfer Statement Date
	 */
	public Timestamp getEftStatementDate() {
		return (Timestamp) get_Value("EftStatementDate");
	}

	/**
	 * Set EFT Statement Reference. Electronic Funds Transfer Statement
	 * Reference
	 */
	public void setEftStatementReference(String EftStatementReference) {
		if (EftStatementReference != null
				&& EftStatementReference.length() > 60) {
			log.warning("Length > 60 - truncated");
			EftStatementReference = EftStatementReference.substring(0, 59);
		}
		set_Value("EftStatementReference", EftStatementReference);
	}

	/**
	 * Get EFT Statement Reference. Electronic Funds Transfer Statement
	 * Reference
	 */
	public String getEftStatementReference() {
		return (String) get_Value("EftStatementReference");
	}

	/**
	 * Set Ending balance. Ending or closing balance
	 */
	public void setEndingBalance(BigDecimal EndingBalance) {
		if (EndingBalance == null)
			throw new IllegalArgumentException("EndingBalance is mandatory.");
		set_Value("EndingBalance", EndingBalance);
	}

	/**
	 * Get Ending balance. Ending or closing balance
	 */
	public BigDecimal getEndingBalance() {
		BigDecimal bd = (BigDecimal) get_Value("EndingBalance");
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
	 * Set Manual. This is a manual process
	 */
	public void setIsManual(boolean IsManual) {
		set_Value("IsManual", new Boolean(IsManual));
	}

	/**
	 * Get Manual. This is a manual process
	 */
	public boolean isManual() {
		Object oo = get_Value("IsManual");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Match Statement */
	public void setMatchStatement(String MatchStatement) {
		if (MatchStatement != null && MatchStatement.length() > 1) {
			log.warning("Length > 1 - truncated");
			MatchStatement = MatchStatement.substring(0, 0);
		}
		set_Value("MatchStatement", MatchStatement);
	}

	/** Get Match Statement */
	public String getMatchStatement() {
		return (String) get_Value("MatchStatement");
	}

	/**
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name == null)
			throw new IllegalArgumentException("Name is mandatory.");
		if (Name.length() > 60) {
			log.warning("Length > 60 - truncated");
			Name = Name.substring(0, 59);
		}
		set_Value("Name", Name);
	}

	/**
	 * Get Name. Alphanumeric identifier of the entity
	 */
	public String getName() {
		return (String) get_Value("Name");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getName());
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

	/**
	 * Set Statement date. Date of the statement
	 */
	public void setStatementDate(Timestamp StatementDate) {
		if (StatementDate == null)
			throw new IllegalArgumentException("StatementDate is mandatory.");
		set_Value("StatementDate", StatementDate);
	}

	/**
	 * Get Statement date. Date of the statement
	 */
	public Timestamp getStatementDate() {
		return (Timestamp) get_Value("StatementDate");
	}

	/**
	 * Set Statement difference. Difference between statement ending balance and
	 * actual ending balance
	 */
	public void setStatementDifference(BigDecimal StatementDifference) {
		set_Value("StatementDifference", StatementDifference);
	}

	/**
	 * Get Statement difference. Difference between statement ending balance and
	 * actual ending balance
	 */
	public BigDecimal getStatementDifference() {
		BigDecimal bd = (BigDecimal) get_Value("StatementDifference");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
