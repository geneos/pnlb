/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_AcctSchema
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.421
 */
public class X_C_AcctSchema extends PO {
	/** Standard Constructor */
	public X_C_AcctSchema(Properties ctx, int C_AcctSchema_ID, String trxName) {
		super(ctx, C_AcctSchema_ID, trxName);
		/**
		 * if (C_AcctSchema_ID == 0) { setAutoPeriodControl (false);
		 * setC_AcctSchema_ID (0); setC_Currency_ID (0); setCommitmentType
		 * (null); // N setCostingLevel (null); // C setCostingMethod (null); //
		 * S setGAAP (null); setHasAlias (false); setHasCombination (false);
		 * setIsAccrual (true); // Y setIsAdjustCOGS (false);
		 * setIsDiscountCorrectsTax (false); setIsExplicitCostAdjustment
		 * (false); // N setIsPostServices (false); // N
		 * setIsTradeDiscountPosted (false); setM_CostType_ID (0); setName
		 * (null); setSeparator (null); // - }
		 */
	}

	/** Load Constructor */
	public X_C_AcctSchema(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_AcctSchema */
	public static final String Table_Name = "C_AcctSchema";

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

	protected BigDecimal accessLevel = new BigDecimal(2);

	/** AccessLevel 2 - Client */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_C_AcctSchema[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** AD_OrgOnly_ID AD_Reference_ID=322 */
	public static final int AD_ORGONLY_ID_AD_Reference_ID = 322;

	/**
	 * Set Only Organization. Create posting entries only for this organization
	 */
	public void setAD_OrgOnly_ID(int AD_OrgOnly_ID) {
		if (AD_OrgOnly_ID <= 0)
			set_Value("AD_OrgOnly_ID", null);
		else
			set_Value("AD_OrgOnly_ID", new Integer(AD_OrgOnly_ID));
	}

	/**
	 * Get Only Organization. Create posting entries only for this organization
	 */
	public int getAD_OrgOnly_ID() {
		Integer ii = (Integer) get_Value("AD_OrgOnly_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Automatic Period Control. If selected, the periods are automatically
	 * opened and closed
	 */
	public void setAutoPeriodControl(boolean AutoPeriodControl) {
		set_Value("AutoPeriodControl", new Boolean(AutoPeriodControl));
	}

	/**
	 * Get Automatic Period Control. If selected, the periods are automatically
	 * opened and closed
	 */
	public boolean isAutoPeriodControl() {
		Object oo = get_Value("AutoPeriodControl");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
	 * Set Period. Period of the Calendar
	 */
	public void setC_Period_ID(int C_Period_ID) {
		if (C_Period_ID <= 0)
			set_ValueNoCheck("C_Period_ID", null);
		else
			set_ValueNoCheck("C_Period_ID", new Integer(C_Period_ID));
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

	/** CommitmentType AD_Reference_ID=359 */
	public static final int COMMITMENTTYPE_AD_Reference_ID = 359;

	/** Commitment & Reservation = B */
	public static final String COMMITMENTTYPE_CommitmentReservation = "B";

	/** Commitment only = C */
	public static final String COMMITMENTTYPE_CommitmentOnly = "C";

	/** None = N */
	public static final String COMMITMENTTYPE_None = "N";

	/**
	 * Set Commitment Type. Create Commitment and/or Reservations for Budget
	 * Control
	 */
	public void setCommitmentType(String CommitmentType) {
		if (CommitmentType == null)
			throw new IllegalArgumentException("CommitmentType is mandatory");
		if (CommitmentType.equals("B") || CommitmentType.equals("C")
				|| CommitmentType.equals("N"))
			;
		else
			throw new IllegalArgumentException(
					"CommitmentType Invalid value - " + CommitmentType
							+ " - Reference_ID=359 - B - C - N");
		if (CommitmentType.length() > 1) {
			log.warning("Length > 1 - truncated");
			CommitmentType = CommitmentType.substring(0, 0);
		}
		set_Value("CommitmentType", CommitmentType);
	}

	/**
	 * Get Commitment Type. Create Commitment and/or Reservations for Budget
	 * Control
	 */
	public String getCommitmentType() {
		return (String) get_Value("CommitmentType");
	}

	/** CostingLevel AD_Reference_ID=355 */
	public static final int COSTINGLEVEL_AD_Reference_ID = 355;

	/** Batch/Lot = B */
	public static final String COSTINGLEVEL_BatchLot = "B";

	/** Client = C */
	public static final String COSTINGLEVEL_Client = "C";

	/** Organization = O */
	public static final String COSTINGLEVEL_Organization = "O";

	/**
	 * Set Costing Level. The lowest level to accumulate Costing Information
	 */
	public void setCostingLevel(String CostingLevel) {
		if (CostingLevel == null)
			throw new IllegalArgumentException("CostingLevel is mandatory");
		if (CostingLevel.equals("B") || CostingLevel.equals("C")
				|| CostingLevel.equals("O"))
			;
		else
			throw new IllegalArgumentException("CostingLevel Invalid value - "
					+ CostingLevel + " - Reference_ID=355 - B - C - O");
		if (CostingLevel.length() > 1) {
			log.warning("Length > 1 - truncated");
			CostingLevel = CostingLevel.substring(0, 0);
		}
		set_Value("CostingLevel", CostingLevel);
	}

	/**
	 * Get Costing Level. The lowest level to accumulate Costing Information
	 */
	public String getCostingLevel() {
		return (String) get_Value("CostingLevel");
	}

	/** CostingMethod AD_Reference_ID=122 */
	public static final int COSTINGMETHOD_AD_Reference_ID = 122;

	/** Average PO = A */
	public static final String COSTINGMETHOD_AveragePO = "A";

	/** Fifo = F */
	public static final String COSTINGMETHOD_Fifo = "F";

	/** Average Invoice = I */
	public static final String COSTINGMETHOD_AverageInvoice = "I";

	/** Lifo = L */
	public static final String COSTINGMETHOD_Lifo = "L";

	/** Standard Costing = S */
	public static final String COSTINGMETHOD_StandardCosting = "S";

	/** User Defined = U */
	public static final String COSTINGMETHOD_UserDefined = "U";

	/** Last Invoice = i */
	public static final String COSTINGMETHOD_LastInvoice = "i";

	/** Last PO Price = p */
	public static final String COSTINGMETHOD_LastPOPrice = "p";

	/** _ = x */
	public static final String COSTINGMETHOD__ = "x";

	/**
	 * Set Costing Method. Indicates how Costs will be calculated
	 */
	public void setCostingMethod(String CostingMethod) {
		if (CostingMethod == null)
			throw new IllegalArgumentException("CostingMethod is mandatory");
		if (CostingMethod.equals("A") || CostingMethod.equals("F")
				|| CostingMethod.equals("I") || CostingMethod.equals("L")
				|| CostingMethod.equals("S") || CostingMethod.equals("U")
				|| CostingMethod.equals("i") || CostingMethod.equals("p")
				|| CostingMethod.equals("x"))
			;
		else
			throw new IllegalArgumentException("CostingMethod Invalid value - "
					+ CostingMethod
					+ " - Reference_ID=122 - A - F - I - L - S - U - i - p - x");
		if (CostingMethod.length() > 1) {
			log.warning("Length > 1 - truncated");
			CostingMethod = CostingMethod.substring(0, 0);
		}
		set_Value("CostingMethod", CostingMethod);
	}

	/**
	 * Get Costing Method. Indicates how Costs will be calculated
	 */
	public String getCostingMethod() {
		return (String) get_Value("CostingMethod");
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

	/** GAAP AD_Reference_ID=123 */
	public static final int GAAP_AD_Reference_ID = 123;

	/** German HGB = DE */
	public static final String GAAP_GermanHGB = "DE";

	/** French Accounting Standard = FR */
	public static final String GAAP_FrenchAccountingStandard = "FR";

	/** International GAAP = UN */
	public static final String GAAP_InternationalGAAP = "UN";

	/** US GAAP = US */
	public static final String GAAP_USGAAP = "US";

	/** Custom Accounting Rules = XX */
	public static final String GAAP_CustomAccountingRules = "XX";

	/**
	 * Set GAAP. Generally Accepted Accounting Principles
	 */
	public void setGAAP(String GAAP) {
		if (GAAP == null)
			throw new IllegalArgumentException("GAAP is mandatory");
		if (GAAP.equals("DE") || GAAP.equals("FR") || GAAP.equals("UN")
				|| GAAP.equals("US") || GAAP.equals("XX"))
			;
		else
			throw new IllegalArgumentException("GAAP Invalid value - " + GAAP
					+ " - Reference_ID=123 - DE - FR - UN - US - XX");
		if (GAAP.length() > 2) {
			log.warning("Length > 2 - truncated");
			GAAP = GAAP.substring(0, 1);
		}
		set_Value("GAAP", GAAP);
	}

	/**
	 * Get GAAP. Generally Accepted Accounting Principles
	 */
	public String getGAAP() {
		return (String) get_Value("GAAP");
	}

	/**
	 * Set Use Account Alias. Ability to select (partial) account combinations
	 * by an Alias
	 */
	public void setHasAlias(boolean HasAlias) {
		set_Value("HasAlias", new Boolean(HasAlias));
	}

	/**
	 * Get Use Account Alias. Ability to select (partial) account combinations
	 * by an Alias
	 */
	public boolean isHasAlias() {
		Object oo = get_Value("HasAlias");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Use Account Combination Control. Combination of account elements are
	 * checked
	 */
	public void setHasCombination(boolean HasCombination) {
		set_Value("HasCombination", new Boolean(HasCombination));
	}

	/**
	 * Get Use Account Combination Control. Combination of account elements are
	 * checked
	 */
	public boolean isHasCombination() {
		Object oo = get_Value("HasCombination");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Accrual. Indicates if Accrual or Cash Based accounting will be used
	 */
	public void setIsAccrual(boolean IsAccrual) {
		set_Value("IsAccrual", new Boolean(IsAccrual));
	}

	/**
	 * Get Accrual. Indicates if Accrual or Cash Based accounting will be used
	 */
	public boolean isAccrual() {
		Object oo = get_Value("IsAccrual");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Adjust COGS. Adjust Cost of Good Sold
	 */
	public void setIsAdjustCOGS(boolean IsAdjustCOGS) {
		set_Value("IsAdjustCOGS", new Boolean(IsAdjustCOGS));
	}

	/**
	 * Get Adjust COGS. Adjust Cost of Good Sold
	 */
	public boolean isAdjustCOGS() {
		Object oo = get_Value("IsAdjustCOGS");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Correct tax for Discounts/Charges. Correct the tax for payment
	 * discount and charges
	 */
	public void setIsDiscountCorrectsTax(boolean IsDiscountCorrectsTax) {
		set_Value("IsDiscountCorrectsTax", new Boolean(IsDiscountCorrectsTax));
	}

	/**
	 * Get Correct tax for Discounts/Charges. Correct the tax for payment
	 * discount and charges
	 */
	public boolean isDiscountCorrectsTax() {
		Object oo = get_Value("IsDiscountCorrectsTax");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Explicit Cost Adjustment. Post the cost adjustment explicitly
	 */
	public void setIsExplicitCostAdjustment(boolean IsExplicitCostAdjustment) {
		set_Value("IsExplicitCostAdjustment", new Boolean(
				IsExplicitCostAdjustment));
	}

	/**
	 * Get Explicit Cost Adjustment. Post the cost adjustment explicitly
	 */
	public boolean isExplicitCostAdjustment() {
		Object oo = get_Value("IsExplicitCostAdjustment");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Post Services Separately. Differentiate between Services and Product
	 * Receivable/Payables
	 */
	public void setIsPostServices(boolean IsPostServices) {
		set_Value("IsPostServices", new Boolean(IsPostServices));
	}

	/**
	 * Get Post Services Separately. Differentiate between Services and Product
	 * Receivable/Payables
	 */
	public boolean isPostServices() {
		Object oo = get_Value("IsPostServices");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Post Trade Discount. Generate postings for trade discounts
	 */
	public void setIsTradeDiscountPosted(boolean IsTradeDiscountPosted) {
		set_Value("IsTradeDiscountPosted", new Boolean(IsTradeDiscountPosted));
	}

	/**
	 * Get Post Trade Discount. Generate postings for trade discounts
	 */
	public boolean isTradeDiscountPosted() {
		Object oo = get_Value("IsTradeDiscountPosted");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Cost Type. Type of Cost (e.g. Current, Plan, Future)
	 */
	public void setM_CostType_ID(int M_CostType_ID) {
		if (M_CostType_ID < 1)
			throw new IllegalArgumentException("M_CostType_ID is mandatory.");
		set_Value("M_CostType_ID", new Integer(M_CostType_ID));
	}

	/**
	 * Get Cost Type. Type of Cost (e.g. Current, Plan, Future)
	 */
	public int getM_CostType_ID() {
		Integer ii = (Integer) get_Value("M_CostType_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
	 * Set Future Days. Number of days to be able to post to a future date
	 * (based on system date)
	 */
	public void setPeriod_OpenFuture(int Period_OpenFuture) {
		set_Value("Period_OpenFuture", new Integer(Period_OpenFuture));
	}

	/**
	 * Get Future Days. Number of days to be able to post to a future date
	 * (based on system date)
	 */
	public int getPeriod_OpenFuture() {
		Integer ii = (Integer) get_Value("Period_OpenFuture");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set History Days. Number of days to be able to post in the past (based on
	 * system date)
	 */
	public void setPeriod_OpenHistory(int Period_OpenHistory) {
		set_Value("Period_OpenHistory", new Integer(Period_OpenHistory));
	}

	/**
	 * Get History Days. Number of days to be able to post in the past (based on
	 * system date)
	 */
	public int getPeriod_OpenHistory() {
		Integer ii = (Integer) get_Value("Period_OpenHistory");
		if (ii == null)
			return 0;
		return ii.intValue();
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
	 * Set Element Separator. Element Separator
	 */
	public void setSeparator(String Separator) {
		if (Separator == null)
			throw new IllegalArgumentException("Separator is mandatory.");
		if (Separator.length() > 1) {
			log.warning("Length > 1 - truncated");
			Separator = Separator.substring(0, 0);
		}
		set_Value("Separator", Separator);
	}

	/**
	 * Get Element Separator. Element Separator
	 */
	public String getSeparator() {
		return (String) get_Value("Separator");
	}
}
