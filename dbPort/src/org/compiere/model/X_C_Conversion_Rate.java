/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Conversion_Rate
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.171
 */
public class X_C_Conversion_Rate extends PO {
	/** Standard Constructor */
	public X_C_Conversion_Rate(Properties ctx, int C_Conversion_Rate_ID,
			String trxName) {
		super(ctx, C_Conversion_Rate_ID, trxName);
		/**
		 * if (C_Conversion_Rate_ID == 0) { setC_ConversionType_ID (0);
		 * setC_Conversion_Rate_ID (0); setC_Currency_ID (0);
		 * setC_Currency_ID_To (0); setDivideRate (Env.ZERO); setMultiplyRate
		 * (Env.ZERO); setValidFrom (new Timestamp(System.currentTimeMillis())); }
		 */
	}

	/** Load Constructor */
	public X_C_Conversion_Rate(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Conversion_Rate */
	public static final String Table_Name = "C_Conversion_Rate";

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

	protected BigDecimal accessLevel = new BigDecimal(6);

	/** AccessLevel 6 - System - Client */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_C_Conversion_Rate[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Currency Type. Currency Conversion Rate Type
	 */
	public void setC_ConversionType_ID(int C_ConversionType_ID) {
		if (C_ConversionType_ID < 1)
			throw new IllegalArgumentException(
					"C_ConversionType_ID is mandatory.");
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
	 * Set Conversion Rate. Rate used for converting currencies
	 */
	public void setC_Conversion_Rate_ID(int C_Conversion_Rate_ID) {
		if (C_Conversion_Rate_ID < 1)
			throw new IllegalArgumentException(
					"C_Conversion_Rate_ID is mandatory.");
		set_ValueNoCheck("C_Conversion_Rate_ID", new Integer(
				C_Conversion_Rate_ID));
	}

	/**
	 * Get Conversion Rate. Rate used for converting currencies
	 */
	public int getC_Conversion_Rate_ID() {
		Integer ii = (Integer) get_Value("C_Conversion_Rate_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getC_Conversion_Rate_ID()));
	}

	/** C_Currency_ID AD_Reference_ID=112 */
	public static final int C_CURRENCY_ID_AD_Reference_ID = 112;

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

	/** C_Currency_ID_To AD_Reference_ID=112 */
	public static final int C_CURRENCY_ID_TO_AD_Reference_ID = 112;

	/**
	 * Set Currency To. Target currency
	 */
	public void setC_Currency_ID_To(int C_Currency_ID_To) {
		set_Value("C_Currency_ID_To", new Integer(C_Currency_ID_To));
	}

	/**
	 * Get Currency To. Target currency
	 */
	public int getC_Currency_ID_To() {
		Integer ii = (Integer) get_Value("C_Currency_ID_To");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Divide Rate. To convert Source number to Target number, the Source is
	 * divided
	 */
	public void setDivideRate(BigDecimal DivideRate) {
		if (DivideRate == null)
			throw new IllegalArgumentException("DivideRate is mandatory.");
		set_Value("DivideRate", DivideRate);
	}

	/**
	 * Get Divide Rate. To convert Source number to Target number, the Source is
	 * divided
	 */
	public BigDecimal getDivideRate() {
		BigDecimal bd = (BigDecimal) get_Value("DivideRate");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Multiply Rate. Rate to multiple the source by to calculate the
	 * target.
	 */
	public void setMultiplyRate(BigDecimal MultiplyRate) {
		if (MultiplyRate == null)
			throw new IllegalArgumentException("MultiplyRate is mandatory.");
		set_Value("MultiplyRate", MultiplyRate);
	}

	/**
	 * Get Multiply Rate. Rate to multiple the source by to calculate the
	 * target.
	 */
	public BigDecimal getMultiplyRate() {
		BigDecimal bd = (BigDecimal) get_Value("MultiplyRate");
		if (bd == null)
			return Env.ZERO;
		return bd;
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

	/**
	 * Set Valid to. Valid to including this date (last day)
	 */
	public void setValidTo(Timestamp ValidTo) {
		set_Value("ValidTo", ValidTo);
	}

	/**
	 * Get Valid to. Valid to including this date (last day)
	 */
	public Timestamp getValidTo() {
		return (Timestamp) get_Value("ValidTo");
	}
}
