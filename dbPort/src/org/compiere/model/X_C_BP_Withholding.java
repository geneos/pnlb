/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_BP_Withholding
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.718
 */
public class X_C_BP_Withholding extends PO {
	/** Standard Constructor */
	public X_C_BP_Withholding(Properties ctx, int C_BP_Withholding_ID,
			String trxName) {
		super(ctx, C_BP_Withholding_ID, trxName);
		/**
		 * if (C_BP_Withholding_ID == 0) { setC_BPartner_ID (0);
		 * setC_Withholding_ID (0); setIsMandatoryWithholding (false);
		 * setIsTemporaryExempt (false); }
		 */
	}

	/** Load Constructor */
	public X_C_BP_Withholding(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_BP_Withholding */
	public static final String Table_Name = "C_BP_Withholding";

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
		StringBuffer sb = new StringBuffer("X_C_BP_Withholding[").append(
				get_ID()).append("]");
		return sb.toString();
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
	 * Set Exempt reason. Reason for not withholding
	 */
	public void setExemptReason(String ExemptReason) {
		if (ExemptReason != null && ExemptReason.length() > 20) {
			log.warning("Length > 20 - truncated");
			ExemptReason = ExemptReason.substring(0, 19);
		}
		set_Value("ExemptReason", ExemptReason);
	}

	/**
	 * Get Exempt reason. Reason for not withholding
	 */
	public String getExemptReason() {
		return (String) get_Value("ExemptReason");
	}

	/**
	 * Set Mandatory Withholding. Monies must be withheld
	 */
	public void setIsMandatoryWithholding(boolean IsMandatoryWithholding) {
		set_Value("IsMandatoryWithholding", new Boolean(IsMandatoryWithholding));
	}

	/**
	 * Get Mandatory Withholding. Monies must be withheld
	 */
	public boolean isMandatoryWithholding() {
		Object oo = get_Value("IsMandatoryWithholding");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Temporary exempt. Temporarily do not withhold taxes
	 */
	public void setIsTemporaryExempt(boolean IsTemporaryExempt) {
		set_Value("IsTemporaryExempt", new Boolean(IsTemporaryExempt));
	}

	/**
	 * Get Temporary exempt. Temporarily do not withhold taxes
	 */
	public boolean isTemporaryExempt() {
		Object oo = get_Value("IsTemporaryExempt");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}
}
