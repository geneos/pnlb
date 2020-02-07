/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Payment
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-01-30 09:48:31.843
 */
public class X_C_Payment extends PO {
	/** Standard Constructor */
	public X_C_Payment(Properties ctx, int C_Payment_ID, String trxName) {
		super(ctx, C_Payment_ID, trxName);
		/**
		 * if (C_Payment_ID == 0) { setC_BPartner_ID (0); setC_BankAccount_ID
		 * (0); setC_Currency_ID (0); setC_DocType_ID (0); setC_Payment_ID (0);
		 * setDateAcct (new Timestamp(System.currentTimeMillis())); //
		 * 
		 * @#Date@ setDateTrx (new Timestamp(System.currentTimeMillis())); //
		 * @#Date@ setDocAction (null); // CO setDocStatus (null); // DR
		 *         setDocumentNo (null); setIsAllocated (false); setIsApproved
		 *         (false); // N setIsDelayedCapture (false); setIsOnline
		 *         (false); setIsOverUnderPayment (false); // N setIsPrepayment
		 *         (false); setIsReceipt (false); setIsReconciled (false);
		 *         setIsSelfService (false); setPayAmt (Env.ZERO); // 0
		 *         setPosted (false); // N setProcessed (false); setTenderType
		 *         (null); // K setTrxType (null); // S }
		 */
	}

	/** Load Constructor */
	public X_C_Payment(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Payment */
	public static final String Table_Name = "C_Payment";

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
		StringBuffer sb = new StringBuffer("X_C_Payment[").append(get_ID())
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
	 * Set Account City. City or the Credit Card or Account Holder
	 */
	public void setA_City(String A_City) {
		if (A_City != null && A_City.length() > 60) {
			log.warning("Length > 60 - truncated");
			A_City = A_City.substring(0, 59);
		}
		set_Value("A_City", A_City);
	}

	/**
	 * Get Account City. City or the Credit Card or Account Holder
	 */
	public String getA_City() {
		return (String) get_Value("A_City");
	}

	/**
	 * Set Account Country. Country
	 */
	public void setA_Country(String A_Country) {
		if (A_Country != null && A_Country.length() > 40) {
			log.warning("Length > 40 - truncated");
			A_Country = A_Country.substring(0, 39);
		}
		set_Value("A_Country", A_Country);
	}

	/**
	 * Get Account Country. Country
	 */
	public String getA_Country() {
		return (String) get_Value("A_Country");
	}

	/**
	 * Set Account EMail. Email Address
	 */
	public void setA_EMail(String A_EMail) {
		if (A_EMail != null && A_EMail.length() > 60) {
			log.warning("Length > 60 - truncated");
			A_EMail = A_EMail.substring(0, 59);
		}
		set_Value("A_EMail", A_EMail);
	}

	/**
	 * Get Account EMail. Email Address
	 */
	public String getA_EMail() {
		return (String) get_Value("A_EMail");
	}

	/**
	 * Set Driver License. Payment Identification - Driver License
	 */
	public void setA_Ident_DL(String A_Ident_DL) {
		if (A_Ident_DL != null && A_Ident_DL.length() > 20) {
			log.warning("Length > 20 - truncated");
			A_Ident_DL = A_Ident_DL.substring(0, 19);
		}
		set_Value("A_Ident_DL", A_Ident_DL);
	}

	/**
	 * Get Driver License. Payment Identification - Driver License
	 */
	public String getA_Ident_DL() {
		return (String) get_Value("A_Ident_DL");
	}

	/**
	 * Set Social Security No. Payment Identification - Social Security No
	 */
	public void setA_Ident_SSN(String A_Ident_SSN) {
		if (A_Ident_SSN != null && A_Ident_SSN.length() > 20) {
			log.warning("Length > 20 - truncated");
			A_Ident_SSN = A_Ident_SSN.substring(0, 19);
		}
		set_Value("A_Ident_SSN", A_Ident_SSN);
	}

	/**
	 * Get Social Security No. Payment Identification - Social Security No
	 */
	public String getA_Ident_SSN() {
		return (String) get_Value("A_Ident_SSN");
	}

	/**
	 * Set Account Name. Name on Credit Card or Account holder
	 */
	public void setA_Name(String A_Name) {
		if (A_Name != null && A_Name.length() > 60) {
			log.warning("Length > 60 - truncated");
			A_Name = A_Name.substring(0, 59);
		}
		set_Value("A_Name", A_Name);
	}

	/**
	 * Get Account Name. Name on Credit Card or Account holder
	 */
	public String getA_Name() {
		return (String) get_Value("A_Name");
	}

	/**
	 * Set Account State. State of the Credit Card or Account holder
	 */
	public void setA_State(String A_State) {
		if (A_State != null && A_State.length() > 40) {
			log.warning("Length > 40 - truncated");
			A_State = A_State.substring(0, 39);
		}
		set_Value("A_State", A_State);
	}

	/**
	 * Get Account State. State of the Credit Card or Account holder
	 */
	public String getA_State() {
		return (String) get_Value("A_State");
	}

	/**
	 * Set Account Street. Street address of the Credit Card or Account holder
	 */
	public void setA_Street(String A_Street) {
		if (A_Street != null && A_Street.length() > 60) {
			log.warning("Length > 60 - truncated");
			A_Street = A_Street.substring(0, 59);
		}
		set_Value("A_Street", A_Street);
	}

	/**
	 * Get Account Street. Street address of the Credit Card or Account holder
	 */
	public String getA_Street() {
		return (String) get_Value("A_Street");
	}

	/**
	 * Set Account Zip/Postal. Zip Code of the Credit Card or Account Holder
	 */
	public void setA_Zip(String A_Zip) {
		if (A_Zip != null && A_Zip.length() > 20) {
			log.warning("Length > 20 - truncated");
			A_Zip = A_Zip.substring(0, 19);
		}
		set_Value("A_Zip", A_Zip);
	}

	/**
	 * Get Account Zip/Postal. Zip Code of the Credit Card or Account Holder
	 */
	public String getA_Zip() {
		return (String) get_Value("A_Zip");
	}

	/**
	 * Set Account No. Account Number
	 */
	public void setAccountNo(String AccountNo) {
		if (AccountNo != null && AccountNo.length() > 20) {
			log.warning("Length > 20 - truncated");
			AccountNo = AccountNo.substring(0, 19);
		}
		set_Value("AccountNo", AccountNo);
	}

	/**
	 * Get Account No. Account Number
	 */
	public String getAccountNo() {
		return (String) get_Value("AccountNo");
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
	 * Set Partner Bank Account. Bank Account of the Business Partner
	 */
	public void setC_BP_BankAccount_ID(int C_BP_BankAccount_ID) {
		if (C_BP_BankAccount_ID <= 0)
			set_Value("C_BP_BankAccount_ID", null);
		else
			set_Value("C_BP_BankAccount_ID", new Integer(C_BP_BankAccount_ID));
	}

	/**
	 * Get Partner Bank Account. Bank Account of the Business Partner
	 */
	public int getC_BP_BankAccount_ID() {
		Integer ii = (Integer) get_Value("C_BP_BankAccount_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
        
                /**
	 * Set Business Partner . Identifies a Business Partner
	 */
	public void setC_BPartner_Location_ID(int C_BPartner_Location_ID) {
		if (C_BPartner_Location_ID < 1)
			throw new IllegalArgumentException("C_BPartner_Location_ID is mandatory.");
		set_Value("C_BPartner_Location_ID", new Integer(C_BPartner_Location_ID));
	}
        
          
	public void setC_JURISDICCION_ID(int C_JURISDICCION_ID){
		set_Value("C_JURISDICCION_ID", new Integer(C_JURISDICCION_ID));
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
	 * Set Order. Order
	 */
	public void setC_Order_ID(int C_Order_ID) {
		if (C_Order_ID <= 0)
			set_Value("C_Order_ID", null);
		else
			set_Value("C_Order_ID", new Integer(C_Order_ID));
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
	 * Set Payment Batch. Payment batch for EFT
	 */
	public void setC_PaymentBatch_ID(int C_PaymentBatch_ID) {
		if (C_PaymentBatch_ID <= 0)
			set_Value("C_PaymentBatch_ID", null);
		else
			set_Value("C_PaymentBatch_ID", new Integer(C_PaymentBatch_ID));
	}

	/**
	 * Get Payment Batch. Payment batch for EFT
	 */
	public int getC_PaymentBatch_ID() {
		Integer ii = (Integer) get_Value("C_PaymentBatch_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Payment. Payment identifier
	 */
	public void setC_Payment_ID(int C_Payment_ID) {
		if (C_Payment_ID < 1)
			throw new IllegalArgumentException("C_Payment_ID is mandatory.");
		set_ValueNoCheck("C_Payment_ID", new Integer(C_Payment_ID));
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

	/** Set C_REGIMENGANANCIAS_V_ID */

	/*
	 * Vit4B 25/03/2008 Modificacion para cambiar el campo de int a String
	 */

	public void setC_REGIMENGANANCIAS_V_ID(String C_REGIMENGANANCIAS_V_ID) {

		if (C_REGIMENGANANCIAS_V_ID != null
				&& C_REGIMENGANANCIAS_V_ID.length() > 30) {
			log.warning("Length > 30 - truncated");
			C_REGIMENGANANCIAS_V_ID = C_REGIMENGANANCIAS_V_ID.substring(0, 29);
		}
		set_Value("C_REGIMENGANANCIAS_V_ID", C_REGIMENGANANCIAS_V_ID);

	}

	/** Get C_REGIMENGANANCIAS_V_ID */
	public String getC_REGIMENGANANCIAS_V_ID() {
		return (String) get_Value("C_REGIMENGANANCIAS_V_ID");
	}

	/**
	 * Set Charge amount. Charge Amount
	 */
	public void setChargeAmt(int ChargeAmt) {
		set_Value("ChargeAmt", new Integer(ChargeAmt));
	}

	/**
	 * Get Charge amount. Charge Amount
	 */
	public int getChargeAmt() {
		Integer ii = (Integer) get_Value("ChargeAmt");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Check No. Check Number
	 */
	public void setCheckNo(String CheckNo) {
		if (CheckNo != null && CheckNo.length() > 20) {
			log.warning("Length > 20 - truncated");
			CheckNo = CheckNo.substring(0, 19);
		}
		set_Value("CheckNo", CheckNo);
	}

	/**
	 * Get Check No. Check Number
	 */
	public String getCheckNo() {
		return (String) get_Value("CheckNo");
	}

	/**
	 * Set Exp. Month. Expiry Month
	 */
	public void setCreditCardExpMM(int CreditCardExpMM) {
		set_Value("CreditCardExpMM", new Integer(CreditCardExpMM));
	}

	/**
	 * Get Exp. Month. Expiry Month
	 */
	public int getCreditCardExpMM() {
		Integer ii = (Integer) get_Value("CreditCardExpMM");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Exp. Year. Expiry Year
	 */
	public void setCreditCardExpYY(int CreditCardExpYY) {
		set_Value("CreditCardExpYY", new Integer(CreditCardExpYY));
	}

	/**
	 * Get Exp. Year. Expiry Year
	 */
	public int getCreditCardExpYY() {
		Integer ii = (Integer) get_Value("CreditCardExpYY");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Number. Credit Card Number
	 */
	public void setCreditCardNumber(String CreditCardNumber) {
		if (CreditCardNumber != null && CreditCardNumber.length() > 20) {
			log.warning("Length > 20 - truncated");
			CreditCardNumber = CreditCardNumber.substring(0, 19);
		}
		set_Value("CreditCardNumber", CreditCardNumber);
	}

	/**
	 * Get Number. Credit Card Number
	 */
	public String getCreditCardNumber() {
		return (String) get_Value("CreditCardNumber");
	}

	/** CreditCardType AD_Reference_ID=149 */
	public static final int CREDITCARDTYPE_AD_Reference_ID = 149;

	/** Amex = A */
	public static final String CREDITCARDTYPE_Amex = "A";

	/** ATM = C */
	public static final String CREDITCARDTYPE_ATM = "C";

	/** Diners = D */
	public static final String CREDITCARDTYPE_Diners = "D";

	/** MasterCard = M */
	public static final String CREDITCARDTYPE_MasterCard = "M";

	/** Discover = N */
	public static final String CREDITCARDTYPE_Discover = "N";

	/** Purchase Card = P */
	public static final String CREDITCARDTYPE_PurchaseCard = "P";

	/** Visa = V */
	public static final String CREDITCARDTYPE_Visa = "V";

	/**
	 * Set Credit Card. Credit Card (Visa, MC, AmEx)
	 */
	public void setCreditCardType(String CreditCardType) {
		if (CreditCardType != null && CreditCardType.length() > 1) {
			log.warning("Length > 1 - truncated");
			CreditCardType = CreditCardType.substring(0, 0);
		}
		set_Value("CreditCardType", CreditCardType);
	}

	/**
	 * Get Credit Card. Credit Card (Visa, MC, AmEx)
	 */
	public String getCreditCardType() {
		return (String) get_Value("CreditCardType");
	}

	/**
	 * Set Verification Code. Credit Card Verification code on credit card
	 */
	public void setCreditCardVV(String CreditCardVV) {
		if (CreditCardVV != null && CreditCardVV.length() > 4) {
			log.warning("Length > 4 - truncated");
			CreditCardVV = CreditCardVV.substring(0, 3);
		}
		set_Value("CreditCardVV", CreditCardVV);
	}

	/**
	 * Get Verification Code. Credit Card Verification code on credit card
	 */
	public String getCreditCardVV() {
		return (String) get_Value("CreditCardVV");
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
	 * Set Transaction Date. Transaction Date
	 */
	public void setDateTrx(Timestamp DateTrx) {
		if (DateTrx == null)
			throw new IllegalArgumentException("DateTrx is mandatory.");
		set_Value("DateTrx", DateTrx);
	}

	/**
	 * Get Transaction Date. Transaction Date
	 */
	public Timestamp getDateTrx() {
		return (Timestamp) get_Value("DateTrx");
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
	 * Set Discount Amount. Calculated amount of discount
	 */
	public void setDiscountAmt(BigDecimal DiscountAmt) {
		set_Value("DiscountAmt", DiscountAmt);
	}

	/**
	 * Get Discount Amount. Calculated amount of discount
	 */
	public BigDecimal getDiscountAmt() {
		BigDecimal bd = (BigDecimal) get_Value("DiscountAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
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
	 * Set Allocated. Indicates if the payment has been allocated
	 */
	public void setIsAllocated(boolean IsAllocated) {
		set_Value("IsAllocated", new Boolean(IsAllocated));
	}

	/**
	 * Get Allocated. Indicates if the payment has been allocated
	 */
	public boolean isAllocated() {
		Object oo = get_Value("IsAllocated");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
	 * Set Delayed Capture. Charge after Shipment
	 */
	public void setIsDelayedCapture(boolean IsDelayedCapture) {
		set_Value("IsDelayedCapture", new Boolean(IsDelayedCapture));
	}

	/**
	 * Get Delayed Capture. Charge after Shipment
	 */
	public boolean isDelayedCapture() {
		Object oo = get_Value("IsDelayedCapture");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Online Access. Can be accessed online
	 */
	public void setIsOnline(boolean IsOnline) {
		set_Value("IsOnline", new Boolean(IsOnline));
	}

	/**
	 * Get Online Access. Can be accessed online
	 */
	public boolean isOnline() {
		Object oo = get_Value("IsOnline");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Over/Under Payment. Over-Payment (unallocated) or Under-Payment
	 * (partial payment)
	 */
	public void setIsOverUnderPayment(boolean IsOverUnderPayment) {
		set_Value("IsOverUnderPayment", new Boolean(IsOverUnderPayment));
	}

	/**
	 * Get Over/Under Payment. Over-Payment (unallocated) or Under-Payment
	 * (partial payment)
	 */
	public boolean isOverUnderPayment() {
		Object oo = get_Value("IsOverUnderPayment");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Prepayment. The Payment/Receipt is a Prepayment
	 */
	public void setIsPrepayment(boolean IsPrepayment) {
		set_Value("IsPrepayment", new Boolean(IsPrepayment));
	}

	/**
	 * Get Prepayment. The Payment/Receipt is a Prepayment
	 */
	public boolean isPrepayment() {
		Object oo = get_Value("IsPrepayment");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

/**
 * 08-11-2010 Camarzana Mariano
 * Agregado para poder seleccionar si quiere hacer el calculo de las retenciones o no (por diferencias entre Compiere y Bejerman)
 */
	/**
	 * Set Retenciones. This is a sales transaction (receipt)
	 */
	public void setIsRetenciones(boolean IsReceipt) {
		set_Value("ISRETENCION", new Boolean(IsReceipt));
	}

	/**
	 * Get Retenciones. This is a sales transaction (receipt)
	 */
	public boolean isRetenciones() {
		Object oo = get_Value("ISRETENCION");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}	
	
	
	/**
	 * Set Receipt. This is a sales transaction (receipt)
	 */
	public void setIsReceipt(boolean IsReceipt) {
		set_Value("IsReceipt", new Boolean(IsReceipt));
	}

	/**
	 * Get Receipt. This is a sales transaction (receipt)
	 */
	public boolean isReceipt() {
		Object oo = get_Value("IsReceipt");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Reconciled. Payment is reconciled with bank statement
	 */
	public void setIsReconciled(boolean IsReconciled) {
		set_Value("IsReconciled", new Boolean(IsReconciled));
	}

	/**
	 * Get Reconciled. Payment is reconciled with bank statement
	 */
	public boolean isReconciled() {
		Object oo = get_Value("IsReconciled");
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
	 * Set Micr. Combination of routing no, account and check no
	 */
	public void setMicr(String Micr) {
		if (Micr != null && Micr.length() > 20) {
			log.warning("Length > 20 - truncated");
			Micr = Micr.substring(0, 19);
		}
		set_Value("Micr", Micr);
	}

	/**
	 * Get Micr. Combination of routing no, account and check no
	 */
	public String getMicr() {
		return (String) get_Value("Micr");
	}

	/**
	 * Set Online Processing. This payment can be processed online
	 */
	public void setOProcessing(String OProcessing) {
		if (OProcessing != null && OProcessing.length() > 1) {
			log.warning("Length > 1 - truncated");
			OProcessing = OProcessing.substring(0, 0);
		}
		set_Value("OProcessing", OProcessing);
	}

	/**
	 * Get Online Processing. This payment can be processed online
	 */
	public String getOProcessing() {
		return (String) get_Value("OProcessing");
	}

	/**
	 * Set Original Transaction ID. Original Transaction ID
	 */
	public void setOrig_TrxID(String Orig_TrxID) {
		if (Orig_TrxID != null && Orig_TrxID.length() > 20) {
			log.warning("Length > 20 - truncated");
			Orig_TrxID = Orig_TrxID.substring(0, 19);
		}
		set_Value("Orig_TrxID", Orig_TrxID);
	}

	/**
	 * Get Original Transaction ID. Original Transaction ID
	 */
	public String getOrig_TrxID() {
		return (String) get_Value("Orig_TrxID");
	}

	/**
	 * Set Over/Under Payment. Over-Payment (unallocated) or Under-Payment
	 * (partial payment) Amount
	 */
	public void setOverUnderAmt(BigDecimal OverUnderAmt) {
		set_Value("OverUnderAmt", OverUnderAmt);
	}

	/**
	 * Get Over/Under Payment. Over-Payment (unallocated) or Under-Payment
	 * (partial payment) Amount
	 */
	public BigDecimal getOverUnderAmt() {
		BigDecimal bd = (BigDecimal) get_Value("OverUnderAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set PAYNET */
	public void setPAYNET(BigDecimal PAYNET) {
		set_Value("PAYNET", PAYNET);
	}

	/** Get PAYNET */
	public BigDecimal getPAYNET() {
		BigDecimal bd = (BigDecimal) get_Value("PAYNET");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set PO Number. Purchase Order Number
	 */
	public void setPONum(String PONum) {
		if (PONum != null && PONum.length() > 60) {
			log.warning("Length > 60 - truncated");
			PONum = PONum.substring(0, 59);
		}
		set_Value("PONum", PONum);
	}

	/**
	 * Get PO Number. Purchase Order Number
	 */
	public String getPONum() {
		return (String) get_Value("PONum");
	}

	/**
	 * Set Payment amount. Amount being paid
	 */
	public void setPayAmt(BigDecimal PayAmt) {
		if (PayAmt == null)
			throw new IllegalArgumentException("PayAmt is mandatory.");
		set_Value("PayAmt", PayAmt);
	}

	/**
	 * Get Payment amount. Amount being paid
	 */
	public BigDecimal getPayAmt() {
		BigDecimal bd = (BigDecimal) get_Value("PayAmt");
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

	/** Set REGIMENGANANCIAS */
	public void setREGIMENGANANCIAS(int REGIMENGANANCIAS) {
		set_Value("REGIMENGANANCIAS", new Integer(REGIMENGANANCIAS));
	}

	/** Get REGIMENGANANCIAS */
	public int getREGIMENGANANCIAS() {
		Integer ii = (Integer) get_Value("REGIMENGANANCIAS");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Authorization Code. Authorization Code returned
	 */
	public void setR_AuthCode(String R_AuthCode) {
		if (R_AuthCode != null && R_AuthCode.length() > 20) {
			log.warning("Length > 20 - truncated");
			R_AuthCode = R_AuthCode.substring(0, 19);
		}
		set_ValueNoCheck("R_AuthCode", R_AuthCode);
	}

	/**
	 * Get Authorization Code. Authorization Code returned
	 */
	public String getR_AuthCode() {
		return (String) get_Value("R_AuthCode");
	}

	/**
	 * Set Authorization Code (DC). Authorization Code Delayed Capture returned
	 */
	public void setR_AuthCode_DC(String R_AuthCode_DC) {
		if (R_AuthCode_DC != null && R_AuthCode_DC.length() > 20) {
			log.warning("Length > 20 - truncated");
			R_AuthCode_DC = R_AuthCode_DC.substring(0, 19);
		}
		set_ValueNoCheck("R_AuthCode_DC", R_AuthCode_DC);
	}

	/**
	 * Get Authorization Code (DC). Authorization Code Delayed Capture returned
	 */
	public String getR_AuthCode_DC() {
		return (String) get_Value("R_AuthCode_DC");
	}

	/** R_AvsAddr AD_Reference_ID=213 */
	public static final int R_AVSADDR_AD_Reference_ID = 213;

	/** No Match = N */
	public static final String R_AVSADDR_NoMatch = "N";

	/** Unavailable = X */
	public static final String R_AVSADDR_Unavailable = "X";

	/** Match = Y */
	public static final String R_AVSADDR_Match = "Y";

	/**
	 * Set Address verified. This address has been verified
	 */
	public void setR_AvsAddr(String R_AvsAddr) {
		if (R_AvsAddr != null && R_AvsAddr.length() > 1) {
			log.warning("Length > 1 - truncated");
			R_AvsAddr = R_AvsAddr.substring(0, 0);
		}
		set_ValueNoCheck("R_AvsAddr", R_AvsAddr);
	}

	/**
	 * Get Address verified. This address has been verified
	 */
	public String getR_AvsAddr() {
		return (String) get_Value("R_AvsAddr");
	}

	/** R_AvsZip AD_Reference_ID=213 */
	public static final int R_AVSZIP_AD_Reference_ID = 213;

	/** No Match = N */
	public static final String R_AVSZIP_NoMatch = "N";

	/** Unavailable = X */
	public static final String R_AVSZIP_Unavailable = "X";

	/** Match = Y */
	public static final String R_AVSZIP_Match = "Y";

	/**
	 * Set Zip verified. The Zip Code has been verified
	 */
	public void setR_AvsZip(String R_AvsZip) {
		if (R_AvsZip != null && R_AvsZip.length() > 1) {
			log.warning("Length > 1 - truncated");
			R_AvsZip = R_AvsZip.substring(0, 0);
		}
		set_ValueNoCheck("R_AvsZip", R_AvsZip);
	}

	/**
	 * Get Zip verified. The Zip Code has been verified
	 */
	public String getR_AvsZip() {
		return (String) get_Value("R_AvsZip");
	}

	/**
	 * Set CVV Match. Credit Card Verification Code Match
	 */
	public void setR_CVV2Match(boolean R_CVV2Match) {
		set_ValueNoCheck("R_CVV2Match", new Boolean(R_CVV2Match));
	}

	/**
	 * Get CVV Match. Credit Card Verification Code Match
	 */
	public boolean isR_CVV2Match() {
		Object oo = get_Value("R_CVV2Match");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Info. Response info
	 */
	public void setR_Info(String R_Info) {
		if (R_Info != null && R_Info.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			R_Info = R_Info.substring(0, 1999);
		}
		set_ValueNoCheck("R_Info", R_Info);
	}

	/**
	 * Get Info. Response info
	 */
	public String getR_Info() {
		return (String) get_Value("R_Info");
	}

	/**
	 * Set Reference. Payment reference
	 */
	public void setR_PnRef(String R_PnRef) {
		if (R_PnRef != null && R_PnRef.length() > 20) {
			log.warning("Length > 20 - truncated");
			R_PnRef = R_PnRef.substring(0, 19);
		}
		set_ValueNoCheck("R_PnRef", R_PnRef);
	}

	/**
	 * Get Reference. Payment reference
	 */
	public String getR_PnRef() {
		return (String) get_Value("R_PnRef");
	}

	/**
	 * Set Reference (DC). Payment Reference Delayed Capture
	 */
	public void setR_PnRef_DC(String R_PnRef_DC) {
		if (R_PnRef_DC != null && R_PnRef_DC.length() > 20) {
			log.warning("Length > 20 - truncated");
			R_PnRef_DC = R_PnRef_DC.substring(0, 19);
		}
		set_ValueNoCheck("R_PnRef_DC", R_PnRef_DC);
	}

	/**
	 * Get Reference (DC). Payment Reference Delayed Capture
	 */
	public String getR_PnRef_DC() {
		return (String) get_Value("R_PnRef_DC");
	}

	/**
	 * Set Response Message. Response message
	 */
	public void setR_RespMsg(String R_RespMsg) {
		if (R_RespMsg != null && R_RespMsg.length() > 60) {
			log.warning("Length > 60 - truncated");
			R_RespMsg = R_RespMsg.substring(0, 59);
		}
		set_ValueNoCheck("R_RespMsg", R_RespMsg);
	}

	/**
	 * Get Response Message. Response message
	 */
	public String getR_RespMsg() {
		return (String) get_Value("R_RespMsg");
	}

	/**
	 * Set Result. Result of transmission
	 */
	public void setR_Result(String R_Result) {
		if (R_Result != null && R_Result.length() > 20) {
			log.warning("Length > 20 - truncated");
			R_Result = R_Result.substring(0, 19);
		}
		set_ValueNoCheck("R_Result", R_Result);
	}

	/**
	 * Get Result. Result of transmission
	 */
	public String getR_Result() {
		return (String) get_Value("R_Result");
	}

	/** Ref_Payment_ID AD_Reference_ID=343 */
	public static final int REF_PAYMENT_ID_AD_Reference_ID = 343;

	/** Set Referenced Payment */
	public void setRef_Payment_ID(int Ref_Payment_ID) {
		if (Ref_Payment_ID <= 0)
			set_ValueNoCheck("Ref_Payment_ID", null);
		else
			set_ValueNoCheck("Ref_Payment_ID", new Integer(Ref_Payment_ID));
	}

	/** Get Referenced Payment */
	public int getRef_Payment_ID() {
		Integer ii = (Integer) get_Value("Ref_Payment_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set RetencionGanHono */
	public void setRetencionGanHono(BigDecimal RetencionGanHono) {
		set_Value("RetencionGanHono", RetencionGanHono);
	}

	/** Get RetencionGanHono */
	public BigDecimal getRetencionGanHono() {
		BigDecimal bd = (BigDecimal) get_Value("RetencionGanHono");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set RetencionGanancias */
	public void setRetencionGanancias(BigDecimal RetencionGanancias) {
		set_Value("RetencionGanancias", RetencionGanancias);
	}

	/** Get RetencionGanancias */
	public BigDecimal getRetencionGanancias() {
		BigDecimal bd = (BigDecimal) get_Value("RetencionGanancias");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set RetencionIB */
	public void setRetencionIB(BigDecimal RetencionIB) {
		set_Value("RetencionIB", RetencionIB);
	}

	/** Get RetencionIB */
	public BigDecimal getRetencionIB() {
		BigDecimal bd = (BigDecimal) get_Value("RetencionIB");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set RetencionIVA */
	public void setRetencionIVA(BigDecimal RetencionIVA) {
		set_Value("RetencionIVA", RetencionIVA);
	}

	/** Get RetencionIVA */
	public BigDecimal getRetencionIVA() {
		BigDecimal bd = (BigDecimal) get_Value("RetencionIVA");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set RetencionSUSS */
	public void setRetencionSUSS(BigDecimal RetencionSUSS) {
		set_Value("RetencionSUSS", RetencionSUSS);
	}

	/** Get RetencionSUSS */
	public BigDecimal getRetencionSUSS() {
		BigDecimal bd = (BigDecimal) get_Value("RetencionSUSS");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Routing No. Bank Routing Number
	 */
	public void setRoutingNo(String RoutingNo) {
		if (RoutingNo != null && RoutingNo.length() > 20) {
			log.warning("Length > 20 - truncated");
			RoutingNo = RoutingNo.substring(0, 19);
		}
		set_Value("RoutingNo", RoutingNo);
	}

	/**
	 * Get Routing No. Bank Routing Number
	 */
	public String getRoutingNo() {
		return (String) get_Value("RoutingNo");
	}

	/** Set SUMRETENCIONIB */
	public void setSUMRETENCIONIB(BigDecimal SUMRETENCIONIB) {
		set_Value("SUMRETENCIONIB", SUMRETENCIONIB);
	}

	/** Get SUMRETENCIONIB */
	public BigDecimal getSUMRETENCIONIB() {
		BigDecimal bd = (BigDecimal) get_Value("SUMRETENCIONIB");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set SUMRETENCIONSUSS */
	public void setSUMRETENCIONSUSS(BigDecimal SUMRETENCIONSUSS) {
		set_Value("SUMRETENCIONSUSS", SUMRETENCIONSUSS);
	}

	/** Get SUMRETENCIONSUSS */
	public BigDecimal getSUMRETENCIONSUSS() {
		BigDecimal bd = (BigDecimal) get_Value("SUMRETENCIONSUSS");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set SumAnt */
	public void setSumAnt(BigDecimal SumAnt) {
		set_Value("SumAnt", SumAnt);
	}

	/** Get SumAnt */
	public BigDecimal getSumAnt() {
		BigDecimal bd = (BigDecimal) get_Value("SumAnt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set SumRetencionGanancias */
	public void setSumRetencionGanancias(BigDecimal SumRetencionGanancias) {
		set_Value("SumRetencionGanancias", SumRetencionGanancias);
	}

	/** Get SumRetencionGanancias */
	public BigDecimal getSumRetencionGanancias() {
		BigDecimal bd = (BigDecimal) get_Value("SumRetencionGanancias");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set SumRetencionIVA */
	public void setSumRetencionIVA(BigDecimal SumRetencionIVA) {
		set_Value("SumRetencionIVA", SumRetencionIVA);
	}

	/** Get SumRetencionIVA */
	public BigDecimal getSumRetencionIVA() {
		BigDecimal bd = (BigDecimal) get_Value("SumRetencionIVA");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Swipe. Track 1 and 2 of the Credit Card
	 */
	public void setSwipe(String Swipe) {
		if (Swipe != null && Swipe.length() > 80) {
			log.warning("Length > 80 - truncated");
			Swipe = Swipe.substring(0, 79);
		}
		set_ValueNoCheck("Swipe", Swipe);
	}

	/**
	 * Get Swipe. Track 1 and 2 of the Credit Card
	 */
	public String getSwipe() {
		return (String) get_Value("Swipe");
	}

	/**
	 * Set Tax Amount. Tax Amount for a document
	 */
	public void setTaxAmt(BigDecimal TaxAmt) {
		set_Value("TaxAmt", TaxAmt);
	}

	/**
	 * Get Tax Amount. Tax Amount for a document
	 */
	public BigDecimal getTaxAmt() {
		BigDecimal bd = (BigDecimal) get_Value("TaxAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** TenderType AD_Reference_ID=214 */
	public static final int TENDERTYPE_AD_Reference_ID = 214;

	/** Direct Deposit = A */
	public static final String TENDERTYPE_DirectDeposit = "A";

	/** Credit Card = C */
	public static final String TENDERTYPE_CreditCard = "C";

	/** Direct Debit = D */
	public static final String TENDERTYPE_DirectDebit = "D";

	/** Check = K */
	public static final String TENDERTYPE_Check = "K";

	/**
	 * Set Tender type. Method of Payment
	 */
	public void setTenderType(String TenderType) {
		if (TenderType == null)
			throw new IllegalArgumentException("TenderType is mandatory");
		if (TenderType.equals("A") || TenderType.equals("C")
				|| TenderType.equals("D") || TenderType.equals("K"))
			;
		else
			throw new IllegalArgumentException("TenderType Invalid value - "
					+ TenderType + " - Reference_ID=214 - A - C - D - K");
		if (TenderType.length() > 1) {
			log.warning("Length > 1 - truncated");
			TenderType = TenderType.substring(0, 0);
		}
		set_Value("TenderType", TenderType);
	}

	/**
	 * Get Tender type. Method of Payment
	 */
	public String getTenderType() {
		return (String) get_Value("TenderType");
	}

	/** TrxType AD_Reference_ID=215 */
	public static final int TRXTYPE_AD_Reference_ID = 215;

	/** Authorization = A */
	public static final String TRXTYPE_Authorization = "A";

	/** Credit (Payment) = C */
	public static final String TRXTYPE_CreditPayment = "C";

	/** Delayed Capture = D */
	public static final String TRXTYPE_DelayedCapture = "D";

	/** Voice Authorization = F */
	public static final String TRXTYPE_VoiceAuthorization = "F";

	/** Sales = S */
	public static final String TRXTYPE_Sales = "S";

	/** Void = V */
	public static final String TRXTYPE_Void = "V";

	/**
	 * Set Transaction Type. Type of credit card transaction
	 */
	public void setTrxType(String TrxType) {
		if (TrxType == null)
			throw new IllegalArgumentException("TrxType is mandatory");
		if (TrxType.equals("A") || TrxType.equals("C") || TrxType.equals("D")
				|| TrxType.equals("F") || TrxType.equals("S")
				|| TrxType.equals("V"))
			;
		else
			throw new IllegalArgumentException("TrxType Invalid value - "
					+ TrxType + " - Reference_ID=215 - A - C - D - F - S - V");
		if (TrxType.length() > 1) {
			log.warning("Length > 1 - truncated");
			TrxType = TrxType.substring(0, 0);
		}
		set_Value("TrxType", TrxType);
	}

	/**
	 * Get Transaction Type. Type of credit card transaction
	 */
	public String getTrxType() {
		return (String) get_Value("TrxType");
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
	 * Set Voice authorization code. Voice Authorization Code from credit card
	 * company
	 */
	public void setVoiceAuthCode(String VoiceAuthCode) {
		if (VoiceAuthCode != null && VoiceAuthCode.length() > 20) {
			log.warning("Length > 20 - truncated");
			VoiceAuthCode = VoiceAuthCode.substring(0, 19);
		}
		set_Value("VoiceAuthCode", VoiceAuthCode);
	}

	/**
	 * Get Voice authorization code. Voice Authorization Code from credit card
	 * company
	 */
	public String getVoiceAuthCode() {
		return (String) get_Value("VoiceAuthCode");
	}

	/**
	 * Set Write-off Amount. Amount to write-off
	 */
	public void setWriteOffAmt(BigDecimal WriteOffAmt) {
		set_Value("WriteOffAmt", WriteOffAmt);
	}

	/**
	 * Get Write-off Amount. Amount to write-off
	 */
	public BigDecimal getWriteOffAmt() {
		BigDecimal bd = (BigDecimal) get_Value("WriteOffAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
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

	/**
	 * Set Foreing Currency.
	 */
	public void setIsForeingCurrency(boolean IsForeingCurrency) {
		set_Value("FOREINGCURR", new Boolean(IsForeingCurrency));
	}

	/**
	 * Get Foreing Currency.
	 */
	public boolean isForeingCurrency() {
		Object oo = get_Value("FOREINGCURR");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Cotizacion */
	public void setCotizacion(BigDecimal Cotizacion) {
		set_Value("COTIZACION", Cotizacion);
	}

	/** Get Cotizacion */
	public BigDecimal getCotizacion() {
		BigDecimal bd = (BigDecimal) get_Value("COTIZACION");
		if (bd == null)
			return BigDecimal.ZERO;
		return bd;
	}

	/**
	 * Get Vendedor/Cobrador.
	 */
	public int getVendedorCobrador() {
		Integer ii = (Integer) get_Value("COBRADOR");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
	
	
	/**
	 * 23/11/2010 Camarzana Mariano - Metodos agregados para setear el cobrador automaticamente
	 */
	/** Set Cobrador */
	public void setCobrador(int cobrador) {
		if (cobrador <= 0)
			set_Value("COBRADOR", null);
		else
			set_Value("COBRADOR", new Integer(cobrador));
	}

	/** Get Cobrador */
	public int getCobrador() {
		Integer ii = (Integer) get_Value("COBRADOR");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

}
