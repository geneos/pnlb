/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for Fact_Acct_Balance
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.953
 */
public class X_Fact_Acct_Balance extends PO {
	/** Standard Constructor */
	public X_Fact_Acct_Balance(Properties ctx, int Fact_Acct_Balance_ID,
			String trxName) {
		super(ctx, Fact_Acct_Balance_ID, trxName);
		/**
		 * if (Fact_Acct_Balance_ID == 0) { setAccount_ID (0); setAmtAcctCr
		 * (Env.ZERO); setAmtAcctDr (Env.ZERO); setC_AcctSchema_ID (0);
		 * setDateAcct (new Timestamp(System.currentTimeMillis()));
		 * setPostingType (null); setQty (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_Fact_Acct_Balance(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=Fact_Acct_Balance */
	public static final String Table_Name = "Fact_Acct_Balance";

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
		StringBuffer sb = new StringBuffer("X_Fact_Acct_Balance[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/** AD_OrgTrx_ID AD_Reference_ID=276 */
	public static final int AD_ORGTRX_ID_AD_Reference_ID = 276;

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

	/** Account_ID AD_Reference_ID=182 */
	public static final int ACCOUNT_ID_AD_Reference_ID = 182;

	/**
	 * Set Account. Account used
	 */
	public void setAccount_ID(int Account_ID) {
		if (Account_ID < 1)
			throw new IllegalArgumentException("Account_ID is mandatory.");
		set_Value("Account_ID", new Integer(Account_ID));
	}

	/**
	 * Get Account. Account used
	 */
	public int getAccount_ID() {
		Integer ii = (Integer) get_Value("Account_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Accounted Credit. Accounted Credit Amount
	 */
	public void setAmtAcctCr(BigDecimal AmtAcctCr) {
		if (AmtAcctCr == null)
			throw new IllegalArgumentException("AmtAcctCr is mandatory.");
		set_Value("AmtAcctCr", AmtAcctCr);
	}

	/**
	 * Get Accounted Credit. Accounted Credit Amount
	 */
	public BigDecimal getAmtAcctCr() {
		BigDecimal bd = (BigDecimal) get_Value("AmtAcctCr");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Accounted Debit. Accounted Debit Amount
	 */
	public void setAmtAcctDr(BigDecimal AmtAcctDr) {
		if (AmtAcctDr == null)
			throw new IllegalArgumentException("AmtAcctDr is mandatory.");
		set_Value("AmtAcctDr", AmtAcctDr);
	}

	/**
	 * Get Accounted Debit. Accounted Debit Amount
	 */
	public BigDecimal getAmtAcctDr() {
		BigDecimal bd = (BigDecimal) get_Value("AmtAcctDr");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Accounting Schema. Rules for accounting
	 */
	public void setC_AcctSchema_ID(int C_AcctSchema_ID) {
		if (C_AcctSchema_ID < 1)
			throw new IllegalArgumentException("C_AcctSchema_ID is mandatory.");
		set_Value("C_AcctSchema_ID", new Integer(C_AcctSchema_ID));
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getC_AcctSchema_ID()));
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

	/** C_LocFrom_ID AD_Reference_ID=133 */
	public static final int C_LOCFROM_ID_AD_Reference_ID = 133;

	/**
	 * Set Location From. Location that inventory was moved from
	 */
	public void setC_LocFrom_ID(int C_LocFrom_ID) {
		if (C_LocFrom_ID <= 0)
			set_Value("C_LocFrom_ID", null);
		else
			set_Value("C_LocFrom_ID", new Integer(C_LocFrom_ID));
	}

	/**
	 * Get Location From. Location that inventory was moved from
	 */
	public int getC_LocFrom_ID() {
		Integer ii = (Integer) get_Value("C_LocFrom_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** C_LocTo_ID AD_Reference_ID=133 */
	public static final int C_LOCTO_ID_AD_Reference_ID = 133;

	/**
	 * Set Location To. Location that inventory was moved to
	 */
	public void setC_LocTo_ID(int C_LocTo_ID) {
		if (C_LocTo_ID <= 0)
			set_Value("C_LocTo_ID", null);
		else
			set_Value("C_LocTo_ID", new Integer(C_LocTo_ID));
	}

	/**
	 * Get Location To. Location that inventory was moved to
	 */
	public int getC_LocTo_ID() {
		Integer ii = (Integer) get_Value("C_LocTo_ID");
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
	 * Set Sales Region. Sales coverage region
	 */
	public void setC_SalesRegion_ID(int C_SalesRegion_ID) {
		if (C_SalesRegion_ID <= 0)
			set_Value("C_SalesRegion_ID", null);
		else
			set_Value("C_SalesRegion_ID", new Integer(C_SalesRegion_ID));
	}

	/**
	 * Get Sales Region. Sales coverage region
	 */
	public int getC_SalesRegion_ID() {
		Integer ii = (Integer) get_Value("C_SalesRegion_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Sub Account. Sub account for Element Value
	 */
	public void setC_SubAcct_ID(int C_SubAcct_ID) {
		if (C_SubAcct_ID <= 0)
			set_ValueNoCheck("C_SubAcct_ID", null);
		else
			set_ValueNoCheck("C_SubAcct_ID", new Integer(C_SubAcct_ID));
	}

	/**
	 * Get Sub Account. Sub account for Element Value
	 */
	public int getC_SubAcct_ID() {
		Integer ii = (Integer) get_Value("C_SubAcct_ID");
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
	 * Set Product. Product, Service, Item
	 */
	public void setM_Product_ID(int M_Product_ID) {
		if (M_Product_ID <= 0)
			set_Value("M_Product_ID", null);
		else
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
	 * Set Quantity. Quantity
	 */
	public void setQty(BigDecimal Qty) {
		if (Qty == null)
			throw new IllegalArgumentException("Qty is mandatory.");
		set_Value("Qty", Qty);
	}

	/**
	 * Get Quantity. Quantity
	 */
	public BigDecimal getQty() {
		BigDecimal bd = (BigDecimal) get_Value("Qty");
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
	 * Set User Element 1. User defined accounting Element
	 */
	public void setUserElement1_ID(int UserElement1_ID) {
		if (UserElement1_ID <= 0)
			set_Value("UserElement1_ID", null);
		else
			set_Value("UserElement1_ID", new Integer(UserElement1_ID));
	}

	/**
	 * Get User Element 1. User defined accounting Element
	 */
	public int getUserElement1_ID() {
		Integer ii = (Integer) get_Value("UserElement1_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set User Element 2. User defined accounting Element
	 */
	public void setUserElement2_ID(int UserElement2_ID) {
		if (UserElement2_ID <= 0)
			set_Value("UserElement2_ID", null);
		else
			set_Value("UserElement2_ID", new Integer(UserElement2_ID));
	}

	/**
	 * Get User Element 2. User defined accounting Element
	 */
	public int getUserElement2_ID() {
		Integer ii = (Integer) get_Value("UserElement2_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
