/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Tax
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.625
 */
public class X_C_Tax extends PO {
	/** Standard Constructor */
	public X_C_Tax(Properties ctx, int C_Tax_ID, String trxName) {
		super(ctx, C_Tax_ID, trxName);
		/**
		 * if (C_Tax_ID == 0) { setC_TaxCategory_ID (0); setC_Tax_ID (0);
		 * setIsDefault (false); setIsDocumentLevel (false); setIsSalesTax
		 * (false); // N setIsSummary (false); setIsTaxExempt (false); setName
		 * (null); setRate (Env.ZERO); setRequiresTaxCertificate (false);
		 * setSOPOType (null); // B setValidFrom (new
		 * Timestamp(System.currentTimeMillis())); }
		 */
	}

	/** Load Constructor */
	public X_C_Tax(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Tax */
	public static final String Table_Name = "C_Tax";

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
		StringBuffer sb = new StringBuffer("X_C_Tax[").append(get_ID()).append(
				"]");
		return sb.toString();
	}

	/** C_Country_ID AD_Reference_ID=156 */
	public static final int C_COUNTRY_ID_AD_Reference_ID = 156;

	/**
	 * Set Country. Country
	 */
	public void setC_Country_ID(int C_Country_ID) {
		if (C_Country_ID <= 0)
			set_Value("C_Country_ID", null);
		else
			set_Value("C_Country_ID", new Integer(C_Country_ID));
	}

	/**
	 * Get Country. Country
	 */
	public int getC_Country_ID() {
		Integer ii = (Integer) get_Value("C_Country_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** C_Region_ID AD_Reference_ID=157 */
	public static final int C_REGION_ID_AD_Reference_ID = 157;

	/**
	 * Set Region. Identifies a geographical Region
	 */
	public void setC_Region_ID(int C_Region_ID) {
		if (C_Region_ID <= 0)
			set_Value("C_Region_ID", null);
		else
			set_Value("C_Region_ID", new Integer(C_Region_ID));
	}

	/**
	 * Get Region. Identifies a geographical Region
	 */
	public int getC_Region_ID() {
		Integer ii = (Integer) get_Value("C_Region_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Tax Category. Tax Category
	 */
	public void setC_TaxCategory_ID(int C_TaxCategory_ID) {
		if (C_TaxCategory_ID < 1)
			throw new IllegalArgumentException("C_TaxCategory_ID is mandatory.");
		set_Value("C_TaxCategory_ID", new Integer(C_TaxCategory_ID));
	}

	/**
	 * Get Tax Category. Tax Category
	 */
	public int getC_TaxCategory_ID() {
		Integer ii = (Integer) get_Value("C_TaxCategory_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Tax. Tax identifier
	 */
	public void setC_Tax_ID(int C_Tax_ID) {
		if (C_Tax_ID < 1)
			throw new IllegalArgumentException("C_Tax_ID is mandatory.");
		set_ValueNoCheck("C_Tax_ID", new Integer(C_Tax_ID));
	}

	/**
	 * Get Tax. Tax identifier
	 */
	public int getC_Tax_ID() {
		Integer ii = (Integer) get_Value("C_Tax_ID");
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
	 * Set Default. Default value
	 */
	public void setIsDefault(boolean IsDefault) {
		set_Value("IsDefault", new Boolean(IsDefault));
	}

	/**
	 * Get Default. Default value
	 */
	public boolean isDefault() {
		Object oo = get_Value("IsDefault");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Document Level. Tax is calculated on document level (rather than line
	 * by line)
	 */
	public void setIsDocumentLevel(boolean IsDocumentLevel) {
		set_Value("IsDocumentLevel", new Boolean(IsDocumentLevel));
	}

	/**
	 * Get Document Level. Tax is calculated on document level (rather than line
	 * by line)
	 */
	public boolean isDocumentLevel() {
		Object oo = get_Value("IsDocumentLevel");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Sales Tax. This is a sales tax (i.e. not a value added tax)
	 */
	public void setIsSalesTax(boolean IsSalesTax) {
		set_Value("IsSalesTax", new Boolean(IsSalesTax));
	}

	/**
	 * Get Sales Tax. This is a sales tax (i.e. not a value added tax)
	 */
	public boolean isSalesTax() {
		Object oo = get_Value("IsSalesTax");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Summary Level. This is a summary entity
	 */
	public void setIsSummary(boolean IsSummary) {
		set_Value("IsSummary", new Boolean(IsSummary));
	}

	/**
	 * Get Summary Level. This is a summary entity
	 */
	public boolean isSummary() {
		Object oo = get_Value("IsSummary");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Tax exempt. Business partner is exempt from tax
	 */
	public void setIsTaxExempt(boolean IsTaxExempt) {
		set_Value("IsTaxExempt", new Boolean(IsTaxExempt));
	}

	/**
	 * Get Tax exempt. Business partner is exempt from tax
	 */
	public boolean isTaxExempt() {
		Object oo = get_Value("IsTaxExempt");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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

	/** Parent_Tax_ID AD_Reference_ID=158 */
	public static final int PARENT_TAX_ID_AD_Reference_ID = 158;

	/**
	 * Set Parent Tax. Parent Tax indicates a tax that is made up of multiple
	 * taxes
	 */
	public void setParent_Tax_ID(int Parent_Tax_ID) {
		if (Parent_Tax_ID <= 0)
			set_Value("Parent_Tax_ID", null);
		else
			set_Value("Parent_Tax_ID", new Integer(Parent_Tax_ID));
	}

	/**
	 * Get Parent Tax. Parent Tax indicates a tax that is made up of multiple
	 * taxes
	 */
	public int getParent_Tax_ID() {
		Integer ii = (Integer) get_Value("Parent_Tax_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Rate. Rate or Tax or Exchange
	 */
	public void setRate(BigDecimal Rate) {
		if (Rate == null)
			throw new IllegalArgumentException("Rate is mandatory.");
		set_Value("Rate", Rate);
	}

	/**
	 * Get Rate. Rate or Tax or Exchange
	 */
	public BigDecimal getRate() {
		BigDecimal bd = (BigDecimal) get_Value("Rate");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Requires Tax Certificate. This tax rate requires the Business Partner
	 * to be tax exempt
	 */
	public void setRequiresTaxCertificate(boolean RequiresTaxCertificate) {
		set_Value("RequiresTaxCertificate", new Boolean(RequiresTaxCertificate));
	}

	/**
	 * Get Requires Tax Certificate. This tax rate requires the Business Partner
	 * to be tax exempt
	 */
	public boolean isRequiresTaxCertificate() {
		Object oo = get_Value("RequiresTaxCertificate");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** SOPOType AD_Reference_ID=287 */
	public static final int SOPOTYPE_AD_Reference_ID = 287;

	/** Both = B */
	public static final String SOPOTYPE_Both = "B";

	/** Purchase Tax = P */
	public static final String SOPOTYPE_PurchaseTax = "P";

	/** Sales Tax = S */
	public static final String SOPOTYPE_SalesTax = "S";

	/**
	 * Set SO/PO Type. Sales Tax applies to sales situations, Purchase Tax to
	 * purchase situations
	 */
	public void setSOPOType(String SOPOType) {
		if (SOPOType == null)
			throw new IllegalArgumentException("SOPOType is mandatory");
		if (SOPOType.equals("B") || SOPOType.equals("P")
				|| SOPOType.equals("S"))
			;
		else
			throw new IllegalArgumentException("SOPOType Invalid value - "
					+ SOPOType + " - Reference_ID=287 - B - P - S");
		if (SOPOType.length() > 1) {
			log.warning("Length > 1 - truncated");
			SOPOType = SOPOType.substring(0, 0);
		}
		set_Value("SOPOType", SOPOType);
	}

	/**
	 * Get SO/PO Type. Sales Tax applies to sales situations, Purchase Tax to
	 * purchase situations
	 */
	public String getSOPOType() {
		return (String) get_Value("SOPOType");
	}

	/**
	 * Set Tax Indicator. Short form for Tax to be printed on documents
	 */
	public void setTaxIndicator(String TaxIndicator) {
		if (TaxIndicator != null && TaxIndicator.length() > 10) {
			log.warning("Length > 10 - truncated");
			TaxIndicator = TaxIndicator.substring(0, 9);
		}
		set_Value("TaxIndicator", TaxIndicator);
	}

	/**
	 * Get Tax Indicator. Short form for Tax to be printed on documents
	 */
	public String getTaxIndicator() {
		return (String) get_Value("TaxIndicator");
	}

	/** To_Country_ID AD_Reference_ID=156 */
	public static final int TO_COUNTRY_ID_AD_Reference_ID = 156;

	/**
	 * Set To. Receiving Country
	 */
	public void setTo_Country_ID(int To_Country_ID) {
		if (To_Country_ID <= 0)
			set_Value("To_Country_ID", null);
		else
			set_Value("To_Country_ID", new Integer(To_Country_ID));
	}

	/**
	 * Get To. Receiving Country
	 */
	public int getTo_Country_ID() {
		Integer ii = (Integer) get_Value("To_Country_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** To_Region_ID AD_Reference_ID=157 */
	public static final int TO_REGION_ID_AD_Reference_ID = 157;

	/**
	 * Set To. Receiving Region
	 */
	public void setTo_Region_ID(int To_Region_ID) {
		if (To_Region_ID <= 0)
			set_Value("To_Region_ID", null);
		else
			set_Value("To_Region_ID", new Integer(To_Region_ID));
	}

	/**
	 * Get To. Receiving Region
	 */
	public int getTo_Region_ID() {
		Integer ii = (Integer) get_Value("To_Region_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Valid from. Valid from including this date (first day)
	 */
	public void setValidFrom(Timestamp ValidFrom) {
		if (ValidFrom == null)
			throw new IllegalArgumentException("ValidFrom is mandatory.");
		set_Value("ValidFrom", ValidFrom);
	}

	/**
	 * Get Valid from. Valid from including this date (first day)
	 */
	public Timestamp getValidFrom() {
		return (Timestamp) get_Value("ValidFrom");
	}
}
