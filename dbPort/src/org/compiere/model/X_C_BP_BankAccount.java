/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_BP_BankAccount
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.593
 */
public class X_C_BP_BankAccount extends PO {
	/** Standard Constructor */
	public X_C_BP_BankAccount(Properties ctx, int C_BP_BankAccount_ID,
			String trxName) {
		super(ctx, C_BP_BankAccount_ID, trxName);
		/**
		 * if (C_BP_BankAccount_ID == 0) { setA_Name (null);
		 * setC_BP_BankAccount_ID (0); setC_BPartner_ID (0); setIsACH (false); }
		 */
	}

	/** Load Constructor */
	public X_C_BP_BankAccount(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_BP_BankAccount */
	public static final String Table_Name = "C_BP_BankAccount";

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
		StringBuffer sb = new StringBuffer("X_C_BP_BankAccount[").append(
				get_ID()).append("]");
		return sb.toString();
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
		if (A_Name == null)
			throw new IllegalArgumentException("A_Name is mandatory.");
		if (A_Name.length() > 60) {
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

	/** BankAccountType AD_Reference_ID=216 */
	public static final int BANKACCOUNTTYPE_AD_Reference_ID = 216;

	/** Checking = C */
	public static final String BANKACCOUNTTYPE_Checking = "C";

	/** Savings = S */
	public static final String BANKACCOUNTTYPE_Savings = "S";

	/**
	 * Set Bank Account Type. Bank Account Type
	 */
	public void setBankAccountType(String BankAccountType) {
		if (BankAccountType != null && BankAccountType.length() > 1) {
			log.warning("Length > 1 - truncated");
			BankAccountType = BankAccountType.substring(0, 0);
		}
		set_Value("BankAccountType", BankAccountType);
	}

	/**
	 * Get Bank Account Type. Bank Account Type
	 */
	public String getBankAccountType() {
		return (String) get_Value("BankAccountType");
	}

	/**
	 * Set Partner Bank Account. Bank Account of the Business Partner
	 */
	public void setC_BP_BankAccount_ID(int C_BP_BankAccount_ID) {
		if (C_BP_BankAccount_ID < 1)
			throw new IllegalArgumentException(
					"C_BP_BankAccount_ID is mandatory.");
		set_ValueNoCheck("C_BP_BankAccount_ID",
				new Integer(C_BP_BankAccount_ID));
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
	public void setC_BPartner_ID(int C_BPartner_ID) {
		if (C_BPartner_ID < 1)
			throw new IllegalArgumentException("C_BPartner_ID is mandatory.");
		set_ValueNoCheck("C_BPartner_ID", new Integer(C_BPartner_ID));
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
	 * Set Bank. Bank
	 */
	public void setC_Bank_ID(int C_Bank_ID) {
		if (C_Bank_ID <= 0)
			set_Value("C_Bank_ID", null);
		else
			set_Value("C_Bank_ID", new Integer(C_Bank_ID));
	}

	/**
	 * Get Bank. Bank
	 */
	public int getC_Bank_ID() {
		Integer ii = (Integer) get_Value("C_Bank_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getC_Bank_ID()));
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
	 * Set ACH. Automatic Clearing House
	 */
	public void setIsACH(boolean IsACH) {
		set_Value("IsACH", new Boolean(IsACH));
	}

	/**
	 * Get ACH. Automatic Clearing House
	 */
	public boolean isACH() {
		Object oo = get_Value("IsACH");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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

	/**
	 * Set CBU. CBU Number
	 */
	public void setCBU(String CBU) {
		if (CBU != null && CBU.length() > 22) {
			log.warning("Length > 22 - truncated");
			CBU = CBU.substring(0, 21);
		}
		set_Value("CBU", CBU);
	}

	/**
	 * Get CBU. CBU Number
	 */
	public String getCBU() {
		return (String) get_Value("CBU");
	}
}
