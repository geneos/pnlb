/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_UOM_Conversion
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.796
 */
public class X_C_UOM_Conversion extends PO {
	/** Standard Constructor */
	public X_C_UOM_Conversion(Properties ctx, int C_UOM_Conversion_ID,
			String trxName) {
		super(ctx, C_UOM_Conversion_ID, trxName);
		/**
		 * if (C_UOM_Conversion_ID == 0) { setC_UOM_Conversion_ID (0);
		 * setC_UOM_ID (0); setC_UOM_To_ID (0); setDivideRate (Env.ZERO);
		 * setMultiplyRate (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_C_UOM_Conversion(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_UOM_Conversion */
	public static final String Table_Name = "C_UOM_Conversion";

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
		StringBuffer sb = new StringBuffer("X_C_UOM_Conversion[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set UOM Conversion. Unit of Measure Conversion
	 */
	public void setC_UOM_Conversion_ID(int C_UOM_Conversion_ID) {
		if (C_UOM_Conversion_ID < 1)
			throw new IllegalArgumentException(
					"C_UOM_Conversion_ID is mandatory.");
		set_ValueNoCheck("C_UOM_Conversion_ID",
				new Integer(C_UOM_Conversion_ID));
	}

	/**
	 * Get UOM Conversion. Unit of Measure Conversion
	 */
	public int getC_UOM_Conversion_ID() {
		Integer ii = (Integer) get_Value("C_UOM_Conversion_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getC_UOM_Conversion_ID()));
	}

	/** C_UOM_ID AD_Reference_ID=114 */
	public static final int C_UOM_ID_AD_Reference_ID = 114;

	/**
	 * Set UOM. Unit of Measure
	 */
	public void setC_UOM_ID(int C_UOM_ID) {
		if (C_UOM_ID < 1)
			throw new IllegalArgumentException("C_UOM_ID is mandatory.");
		set_Value("C_UOM_ID", new Integer(C_UOM_ID));
	}

	/**
	 * Get UOM. Unit of Measure
	 */
	public int getC_UOM_ID() {
		Integer ii = (Integer) get_Value("C_UOM_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** C_UOM_To_ID AD_Reference_ID=114 */
	public static final int C_UOM_TO_ID_AD_Reference_ID = 114;

	/**
	 * Set UoM To. Target or destination Unit of Measure
	 */
	public void setC_UOM_To_ID(int C_UOM_To_ID) {
		if (C_UOM_To_ID < 1)
			throw new IllegalArgumentException("C_UOM_To_ID is mandatory.");
		set_Value("C_UOM_To_ID", new Integer(C_UOM_To_ID));
	}

	/**
	 * Get UoM To. Target or destination Unit of Measure
	 */
	public int getC_UOM_To_ID() {
		Integer ii = (Integer) get_Value("C_UOM_To_ID");
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
}
