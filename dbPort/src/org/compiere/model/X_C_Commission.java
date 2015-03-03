/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Commission
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.093
 */
public class X_C_Commission extends PO {
	/** Standard Constructor */
	public X_C_Commission(Properties ctx, int C_Commission_ID, String trxName) {
		super(ctx, C_Commission_ID, trxName);
		/**
		 * if (C_Commission_ID == 0) { setC_BPartner_ID (0); setC_Charge_ID (0);
		 * setC_Commission_ID (0); setC_Currency_ID (0); setDocBasisType (null); //
		 * I setFrequencyType (null); // M setListDetails (false); setName
		 * (null); }
		 */
	}

	/** Load Constructor */
	public X_C_Commission(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Commission */
	public static final String Table_Name = "C_Commission";

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
		StringBuffer sb = new StringBuffer("X_C_Commission[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** C_BPartner_ID AD_Reference_ID=232 */
	public static final int C_BPARTNER_ID_AD_Reference_ID = 232;

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
	 * Set Commission. Commission
	 */
	public void setC_Commission_ID(int C_Commission_ID) {
		if (C_Commission_ID < 1)
			throw new IllegalArgumentException("C_Commission_ID is mandatory.");
		set_ValueNoCheck("C_Commission_ID", new Integer(C_Commission_ID));
	}

	/**
	 * Get Commission. Commission
	 */
	public int getC_Commission_ID() {
		Integer ii = (Integer) get_Value("C_Commission_ID");
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
	 * Set Date last run. Date the process was last run.
	 */
	public void setDateLastRun(Timestamp DateLastRun) {
		set_ValueNoCheck("DateLastRun", DateLastRun);
	}

	/**
	 * Get Date last run. Date the process was last run.
	 */
	public Timestamp getDateLastRun() {
		return (Timestamp) get_Value("DateLastRun");
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

	/** DocBasisType AD_Reference_ID=224 */
	public static final int DOCBASISTYPE_AD_Reference_ID = 224;

	/** Invoice = I */
	public static final String DOCBASISTYPE_Invoice = "I";

	/** Order = O */
	public static final String DOCBASISTYPE_Order = "O";

	/** Receipt = R */
	public static final String DOCBASISTYPE_Receipt = "R";

	/**
	 * Set Calculation Basis. Basis for the calculation the commission
	 */
	public void setDocBasisType(String DocBasisType) {
		if (DocBasisType == null)
			throw new IllegalArgumentException("DocBasisType is mandatory");
		if (DocBasisType.equals("I") || DocBasisType.equals("O")
				|| DocBasisType.equals("R"))
			;
		else
			throw new IllegalArgumentException("DocBasisType Invalid value - "
					+ DocBasisType + " - Reference_ID=224 - I - O - R");
		if (DocBasisType.length() > 1) {
			log.warning("Length > 1 - truncated");
			DocBasisType = DocBasisType.substring(0, 0);
		}
		set_Value("DocBasisType", DocBasisType);
	}

	/**
	 * Get Calculation Basis. Basis for the calculation the commission
	 */
	public String getDocBasisType() {
		return (String) get_Value("DocBasisType");
	}

	/** FrequencyType AD_Reference_ID=225 */
	public static final int FREQUENCYTYPE_AD_Reference_ID = 225;

	/** Monthly = M */
	public static final String FREQUENCYTYPE_Monthly = "M";

	/** Quarterly = Q */
	public static final String FREQUENCYTYPE_Quarterly = "Q";

	/** Weekly = W */
	public static final String FREQUENCYTYPE_Weekly = "W";

	/** Yearly = Y */
	public static final String FREQUENCYTYPE_Yearly = "Y";

	/**
	 * Set Frequency Type. Frequency of event
	 */
	public void setFrequencyType(String FrequencyType) {
		if (FrequencyType == null)
			throw new IllegalArgumentException("FrequencyType is mandatory");
		if (FrequencyType.equals("M") || FrequencyType.equals("Q")
				|| FrequencyType.equals("W") || FrequencyType.equals("Y"))
			;
		else
			throw new IllegalArgumentException("FrequencyType Invalid value - "
					+ FrequencyType + " - Reference_ID=225 - M - Q - W - Y");
		if (FrequencyType.length() > 1) {
			log.warning("Length > 1 - truncated");
			FrequencyType = FrequencyType.substring(0, 0);
		}
		set_Value("FrequencyType", FrequencyType);
	}

	/**
	 * Get Frequency Type. Frequency of event
	 */
	public String getFrequencyType() {
		return (String) get_Value("FrequencyType");
	}

	/**
	 * Set List Details. List document details
	 */
	public void setListDetails(boolean ListDetails) {
		set_Value("ListDetails", new Boolean(ListDetails));
	}

	/**
	 * Get List Details. List document details
	 */
	public boolean isListDetails() {
		Object oo = get_Value("ListDetails");
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
}
