/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Withholding
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-01-30 09:48:32.328
 */
public class X_C_Withholding extends PO {
	/** Standard Constructor */
	public X_C_Withholding(Properties ctx, int C_Withholding_ID, String trxName) {
		super(ctx, C_Withholding_ID, trxName);
		/**
		 * if (C_Withholding_ID == 0) { setBeneficiary (0); setC_PaymentTerm_ID
		 * (0); setC_Withholding_ID (0); setIsPaidTo3Party (false);
		 * setIsPercentWithholding (false); setIsTaxProrated (false);
		 * setIsTaxWithholding (false); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_C_Withholding(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Withholding */
	public static final String Table_Name = "C_Withholding";

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
		StringBuffer sb = new StringBuffer("X_C_Withholding[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** Beneficiary AD_Reference_ID=138 */
	public static final int BENEFICIARY_AD_Reference_ID = 138;

	/**
	 * Set Beneficiary. Business Partner to whom payment is made
	 */
	public void setBeneficiary(int Beneficiary) {
		set_Value("Beneficiary", new Integer(Beneficiary));
	}

	/**
	 * Get Beneficiary. Business Partner to whom payment is made
	 */
	public int getBeneficiary() {
		Integer ii = (Integer) get_Value("Beneficiary");
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
	 * Set Withholding. Withholding type defined
	 */
	public void setC_Withholding_ID(int C_Withholding_ID) {
		if (C_Withholding_ID < 1)
			throw new IllegalArgumentException("C_Withholding_ID is mandatory.");
		set_ValueNoCheck("C_Withholding_ID", new Integer(C_Withholding_ID));
	}

	/**
	 * Get Withholding. Withholding type defined
	 */
	public int getC_Withholding_ID() {
		Integer ii = (Integer) get_Value("C_Withholding_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
	 * Set Fix amount. Fix amounted amount to be levied or paid
	 */
	public void setFixAmt(BigDecimal FixAmt) {
		set_Value("FixAmt", FixAmt);
	}

	/**
	 * Get Fix amount. Fix amounted amount to be levied or paid
	 */
	public BigDecimal getFixAmt() {
		BigDecimal bd = (BigDecimal) get_Value("FixAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Paid to third party. Amount paid to someone other than the Business
	 * Partner
	 */
	public void setIsPaidTo3Party(boolean IsPaidTo3Party) {
		set_Value("IsPaidTo3Party", new Boolean(IsPaidTo3Party));
	}

	/**
	 * Get Paid to third party. Amount paid to someone other than the Business
	 * Partner
	 */
	public boolean isPaidTo3Party() {
		Object oo = get_Value("IsPaidTo3Party");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Percent withholding. Withholding amount is a percentage of the
	 * invoice amount
	 */
	public void setIsPercentWithholding(boolean IsPercentWithholding) {
		set_Value("IsPercentWithholding", new Boolean(IsPercentWithholding));
	}

	/**
	 * Get Percent withholding. Withholding amount is a percentage of the
	 * invoice amount
	 */
	public boolean isPercentWithholding() {
		Object oo = get_Value("IsPercentWithholding");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Prorate tax. Tax is Prorated
	 */
	public void setIsTaxProrated(boolean IsTaxProrated) {
		set_Value("IsTaxProrated", new Boolean(IsTaxProrated));
	}

	/**
	 * Get Prorate tax. Tax is Prorated
	 */
	public boolean isTaxProrated() {
		Object oo = get_Value("IsTaxProrated");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Tax withholding. This is a tax related withholding
	 */
	public void setIsTaxWithholding(boolean IsTaxWithholding) {
		set_Value("IsTaxWithholding", new Boolean(IsTaxWithholding));
	}

	/**
	 * Get Tax withholding. This is a tax related withholding
	 */
	public boolean isTaxWithholding() {
		Object oo = get_Value("IsTaxWithholding");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set MINIMOARETENER */
	public void setMINIMOARETENER(BigDecimal MINIMOARETENER) {
		set_Value("MINIMOARETENER", MINIMOARETENER);
	}

	/** Get MINIMOARETENER */
	public BigDecimal getMINIMOARETENER() {
		BigDecimal bd = (BigDecimal) get_Value("MINIMOARETENER");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set MINIMOIMPONIBLE */
	public void setMINIMOIMPONIBLE(BigDecimal MINIMOIMPONIBLE) {
		set_Value("MINIMOIMPONIBLE", MINIMOIMPONIBLE);
	}

	/** Get MINIMOIMPONIBLE */
	public BigDecimal getMINIMOIMPONIBLE() {
		BigDecimal bd = (BigDecimal) get_Value("MINIMOIMPONIBLE");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Max Amount. Maximum Amount in invoice currency
	 */
	public void setMaxAmt(BigDecimal MaxAmt) {
		set_Value("MaxAmt", MaxAmt);
	}

	/**
	 * Get Max Amount. Maximum Amount in invoice currency
	 */
	public BigDecimal getMaxAmt() {
		BigDecimal bd = (BigDecimal) get_Value("MaxAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Min Amount. Minimum Amount in invoice currency
	 */
	public void setMinAmt(BigDecimal MinAmt) {
		set_Value("MinAmt", MinAmt);
	}

	/**
	 * Get Min Amount. Minimum Amount in invoice currency
	 */
	public BigDecimal getMinAmt() {
		BigDecimal bd = (BigDecimal) get_Value("MinAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
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
	 * Set Percent. Percentage
	 */
	public void setPercent(BigDecimal Percent) {
		set_Value("Percent", Percent);
	}

	/**
	 * Get Percent. Percentage
	 */
	public BigDecimal getPercent() {
		BigDecimal bd = (BigDecimal) get_Value("Percent");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set REGIMENGANANCIAS */
	public void setREGIMENGANANCIAS(String REGIMENGANANCIAS) {
		if (REGIMENGANANCIAS.length() > 30) {
			log.warning("Length > 60 - truncated");
			REGIMENGANANCIAS = REGIMENGANANCIAS.substring(0, 29);
		}
		set_Value("REGIMENGANANCIAS", REGIMENGANANCIAS);

	}

	/** Get REGIMENGANANCIAS */
	public String getREGIMENGANANCIAS() {
		return (String) get_Value("Description");
	}

	/**
	 * Set Threshold max. Maximum gross amount for withholding calculation (0=no
	 * limit)
	 */
	public void setThresholdMax(BigDecimal ThresholdMax) {
		set_Value("ThresholdMax", ThresholdMax);
	}

	/**
	 * Get Threshold max. Maximum gross amount for withholding calculation (0=no
	 * limit)
	 */
	public BigDecimal getThresholdMax() {
		BigDecimal bd = (BigDecimal) get_Value("ThresholdMax");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Threshold min. Minimum gross amount for withholding calculation
	 */
	public void setThresholdmin(BigDecimal Thresholdmin) {
		set_Value("Thresholdmin", Thresholdmin);
	}

	/**
	 * Get Threshold min. Minimum gross amount for withholding calculation
	 */
	public BigDecimal getThresholdmin() {
		BigDecimal bd = (BigDecimal) get_Value("Thresholdmin");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
