/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_BankStatementLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.921
 */
public class X_C_BankStatementLine extends PO {
	/** Standard Constructor */
	public X_C_BankStatementLine(Properties ctx, int C_BankStatementLine_ID,
			String trxName) {
		super(ctx, C_BankStatementLine_ID, trxName);
		/**
		 * if (C_BankStatementLine_ID == 0) { setC_BankStatementLine_ID (0);
		 * setC_BankStatement_ID (0); setC_Charge_ID (0); setC_Currency_ID (0); //
		 * 
		 * @SQL=SELECT C_Currency_ID FROM C_BankAccount WHERE
		 *             C_BankAccount_ID=@C_BankAccount_ID@ setChargeAmt
		 *             (Env.ZERO); setDateAcct (new
		 *             Timestamp(System.currentTimeMillis())); //
		 * @StatementDate@ setInterestAmt (Env.ZERO); setIsManual (true); // Y
		 *                 setIsReversal (false); setLine (0); //
		 * @SQL=SELECT COALESCE(MAX(Line),0)+10 FROM C_BankStatementLine WHERE
		 *             C_BankStatement_ID=@C_BankStatement_ID@ setProcessed
		 *             (false); setStatementLineDate (new
		 *             Timestamp(System.currentTimeMillis())); //
		 * @StatementLineDate@ setStmtAmt (Env.ZERO); setTrxAmt (Env.ZERO);
		 *                     setValutaDate (new
		 *                     Timestamp(System.currentTimeMillis())); //
		 * @StatementDate@ }
		 */
	}

	/** Load Constructor */
	public X_C_BankStatementLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_BankStatementLine */
	public static final String Table_Name = "C_BankStatementLine";

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
		StringBuffer sb = new StringBuffer("X_C_BankStatementLine[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Business Partner . Identifies a Business Partner
	 */
	public void setC_BPartner_ID(int C_BPartner_ID) {
		if (C_BPartner_ID <= 0)
			set_Value("C_BPartner_ID", null);
		else
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
	 * Set Bank statement line. Line on a statement from this Bank
	 */
	public void setC_BankStatementLine_ID(int C_BankStatementLine_ID) {
		if (C_BankStatementLine_ID < 1)
			throw new IllegalArgumentException(
					"C_BankStatementLine_ID is mandatory.");
		set_ValueNoCheck("C_BankStatementLine_ID", new Integer(
				C_BankStatementLine_ID));
	}

	/**
	 * Get Bank statement line. Line on a statement from this Bank
	 */
	public int getC_BankStatementLine_ID() {
		Integer ii = (Integer) get_Value("C_BankStatementLine_ID");
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
	 * Set Charge. Additional document charges
	 */
	public void setC_Charge_ID(int C_Charge_ID) {
		if (C_Charge_ID < 1)
			throw new IllegalArgumentException("C_Charge_ID is mandatory.");
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
	 * Set Charge amount. Charge Amount
	 */
	public void setChargeAmt(BigDecimal ChargeAmt) {
		if (ChargeAmt == null)
			throw new IllegalArgumentException("ChargeAmt is mandatory.");
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

	/** Set Create Payment */
	public void setCreatePayment(String CreatePayment) {
		if (CreatePayment != null && CreatePayment.length() > 1) {
			log.warning("Length > 1 - truncated");
			CreatePayment = CreatePayment.substring(0, 0);
		}
		set_Value("CreatePayment", CreatePayment);
	}

	/** Get Create Payment */
	public String getCreatePayment() {
		return (String) get_Value("CreatePayment");
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

	/**
	 * Set EFT Amount. Electronic Funds Transfer Amount
	 */
	public void setEftAmt(BigDecimal EftAmt) {
		set_Value("EftAmt", EftAmt);
	}

	/**
	 * Get EFT Amount. Electronic Funds Transfer Amount
	 */
	public BigDecimal getEftAmt() {
		BigDecimal bd = (BigDecimal) get_Value("EftAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set EFT Check No. Electronic Funds Transfer Check No
	 */
	public void setEftCheckNo(String EftCheckNo) {
		if (EftCheckNo != null && EftCheckNo.length() > 20) {
			log.warning("Length > 20 - truncated");
			EftCheckNo = EftCheckNo.substring(0, 19);
		}
		set_Value("EftCheckNo", EftCheckNo);
	}

	/**
	 * Get EFT Check No. Electronic Funds Transfer Check No
	 */
	public String getEftCheckNo() {
		return (String) get_Value("EftCheckNo");
	}

	/**
	 * Set EFT Currency. Electronic Funds Transfer Currency
	 */
	public void setEftCurrency(String EftCurrency) {
		if (EftCurrency != null && EftCurrency.length() > 20) {
			log.warning("Length > 20 - truncated");
			EftCurrency = EftCurrency.substring(0, 19);
		}
		set_Value("EftCurrency", EftCurrency);
	}

	/**
	 * Get EFT Currency. Electronic Funds Transfer Currency
	 */
	public String getEftCurrency() {
		return (String) get_Value("EftCurrency");
	}

	/**
	 * Set EFT Memo. Electronic Funds Transfer Memo
	 */
	public void setEftMemo(String EftMemo) {
		if (EftMemo != null && EftMemo.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			EftMemo = EftMemo.substring(0, 1999);
		}
		set_Value("EftMemo", EftMemo);
	}

	/**
	 * Get EFT Memo. Electronic Funds Transfer Memo
	 */
	public String getEftMemo() {
		return (String) get_Value("EftMemo");
	}

	/**
	 * Set EFT Payee. Electronic Funds Transfer Payee information
	 */
	public void setEftPayee(String EftPayee) {
		if (EftPayee != null && EftPayee.length() > 255) {
			log.warning("Length > 255 - truncated");
			EftPayee = EftPayee.substring(0, 254);
		}
		set_Value("EftPayee", EftPayee);
	}

	/**
	 * Get EFT Payee. Electronic Funds Transfer Payee information
	 */
	public String getEftPayee() {
		return (String) get_Value("EftPayee");
	}

	/**
	 * Set EFT Payee Account. Electronic Funds Transfer Payyee Account
	 * Information
	 */
	public void setEftPayeeAccount(String EftPayeeAccount) {
		if (EftPayeeAccount != null && EftPayeeAccount.length() > 40) {
			log.warning("Length > 40 - truncated");
			EftPayeeAccount = EftPayeeAccount.substring(0, 39);
		}
		set_Value("EftPayeeAccount", EftPayeeAccount);
	}

	/**
	 * Get EFT Payee Account. Electronic Funds Transfer Payyee Account
	 * Information
	 */
	public String getEftPayeeAccount() {
		return (String) get_Value("EftPayeeAccount");
	}

	/**
	 * Set EFT Reference. Electronic Funds Transfer Reference
	 */
	public void setEftReference(String EftReference) {
		if (EftReference != null && EftReference.length() > 60) {
			log.warning("Length > 60 - truncated");
			EftReference = EftReference.substring(0, 59);
		}
		set_Value("EftReference", EftReference);
	}

	/**
	 * Get EFT Reference. Electronic Funds Transfer Reference
	 */
	public String getEftReference() {
		return (String) get_Value("EftReference");
	}

	/**
	 * Set EFT Statement Line Date. Electronic Funds Transfer Statement Line
	 * Date
	 */
	public void setEftStatementLineDate(Timestamp EftStatementLineDate) {
		set_Value("EftStatementLineDate", EftStatementLineDate);
	}

	/**
	 * Get EFT Statement Line Date. Electronic Funds Transfer Statement Line
	 * Date
	 */
	public Timestamp getEftStatementLineDate() {
		return (Timestamp) get_Value("EftStatementLineDate");
	}

	/**
	 * Set EFT Trx ID. Electronic Funds Transfer Transaction ID
	 */
	public void setEftTrxID(String EftTrxID) {
		if (EftTrxID != null && EftTrxID.length() > 40) {
			log.warning("Length > 40 - truncated");
			EftTrxID = EftTrxID.substring(0, 39);
		}
		set_Value("EftTrxID", EftTrxID);
	}

	/**
	 * Get EFT Trx ID. Electronic Funds Transfer Transaction ID
	 */
	public String getEftTrxID() {
		return (String) get_Value("EftTrxID");
	}

	/**
	 * Set EFT Trx Type. Electronic Funds Transfer Transaction Type
	 */
	public void setEftTrxType(String EftTrxType) {
		if (EftTrxType != null && EftTrxType.length() > 20) {
			log.warning("Length > 20 - truncated");
			EftTrxType = EftTrxType.substring(0, 19);
		}
		set_Value("EftTrxType", EftTrxType);
	}

	/**
	 * Get EFT Trx Type. Electronic Funds Transfer Transaction Type
	 */
	public String getEftTrxType() {
		return (String) get_Value("EftTrxType");
	}

	/**
	 * Set EFT Effective Date. Electronic Funds Transfer Valuta (effective) Date
	 */
	public void setEftValutaDate(Timestamp EftValutaDate) {
		set_Value("EftValutaDate", EftValutaDate);
	}

	/**
	 * Get EFT Effective Date. Electronic Funds Transfer Valuta (effective) Date
	 */
	public Timestamp getEftValutaDate() {
		return (Timestamp) get_Value("EftValutaDate");
	}

	/**
	 * Set Interest Amount. Interest Amount
	 */
	public void setInterestAmt(BigDecimal InterestAmt) {
		if (InterestAmt == null)
			throw new IllegalArgumentException("InterestAmt is mandatory.");
		set_Value("InterestAmt", InterestAmt);
	}

	/**
	 * Get Interest Amount. Interest Amount
	 */
	public BigDecimal getInterestAmt() {
		BigDecimal bd = (BigDecimal) get_Value("InterestAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
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

	/**
	 * Set Reversal. This is a reversing transaction
	 */
	public void setIsReversal(boolean IsReversal) {
		set_Value("IsReversal", new Boolean(IsReversal));
	}

	/**
	 * Get Reversal. This is a reversing transaction
	 */
	public boolean isReversal() {
		Object oo = get_Value("IsReversal");
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getLine()));
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
	 * Set Memo. Memo Text
	 */
	public void setMemo(String Memo) {
		if (Memo != null && Memo.length() > 255) {
			log.warning("Length > 255 - truncated");
			Memo = Memo.substring(0, 254);
		}
		set_Value("Memo", Memo);
	}

	/**
	 * Get Memo. Memo Text
	 */
	public String getMemo() {
		return (String) get_Value("Memo");
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

	/**
	 * Set Reference No. Your customer or vendor number at the Business
	 * Partner's site
	 */
	public void setReferenceNo(String ReferenceNo) {
		if (ReferenceNo != null && ReferenceNo.length() > 40) {
			log.warning("Length > 40 - truncated");
			ReferenceNo = ReferenceNo.substring(0, 39);
		}
		set_Value("ReferenceNo", ReferenceNo);
	}

	/**
	 * Get Reference No. Your customer or vendor number at the Business
	 * Partner's site
	 */
	public String getReferenceNo() {
		return (String) get_Value("ReferenceNo");
	}

	/**
	 * Set Statement Line Date. Date of the Statement Line
	 */
	public void setStatementLineDate(Timestamp StatementLineDate) {
		if (StatementLineDate == null)
			throw new IllegalArgumentException(
					"StatementLineDate is mandatory.");
		set_Value("StatementLineDate", StatementLineDate);
	}

	/**
	 * Get Statement Line Date. Date of the Statement Line
	 */
	public Timestamp getStatementLineDate() {
		return (Timestamp) get_Value("StatementLineDate");
	}

	/**
	 * Set Statement amount. Statement Amount
	 */
	public void setStmtAmt(BigDecimal StmtAmt) {
		if (StmtAmt == null)
			throw new IllegalArgumentException("StmtAmt is mandatory.");
		set_Value("StmtAmt", StmtAmt);
	}

	/**
	 * Get Statement amount. Statement Amount
	 */
	public BigDecimal getStmtAmt() {
		BigDecimal bd = (BigDecimal) get_Value("StmtAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Transaction Amount. Amount of a transaction
	 */
	public void setTrxAmt(BigDecimal TrxAmt) {
		if (TrxAmt == null)
			throw new IllegalArgumentException("TrxAmt is mandatory.");
		set_Value("TrxAmt", TrxAmt);
	}

	/**
	 * Get Transaction Amount. Amount of a transaction
	 */
	public BigDecimal getTrxAmt() {
		BigDecimal bd = (BigDecimal) get_Value("TrxAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Effective date. Date when money is available
	 */
	public void setValutaDate(Timestamp ValutaDate) {
		if (ValutaDate == null)
			throw new IllegalArgumentException("ValutaDate is mandatory.");
		set_Value("ValutaDate", ValutaDate);
	}

	/**
	 * Get Effective date. Date when money is available
	 */
	public Timestamp getValutaDate() {
		return (Timestamp) get_Value("ValutaDate");
	}
}
