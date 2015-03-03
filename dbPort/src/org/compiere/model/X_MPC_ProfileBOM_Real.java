/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for MPC_ProfileBOM_Real
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.296
 */
public class X_MPC_ProfileBOM_Real extends PO {
	/** Standard Constructor */
	public X_MPC_ProfileBOM_Real(Properties ctx, int MPC_ProfileBOM_Real_ID,
			String trxName) {
		super(ctx, MPC_ProfileBOM_Real_ID, trxName);
		/**
		 * if (MPC_ProfileBOM_Real_ID == 0) { setMPC_ProfileBOM_ID (0);
		 * setMPC_ProfileBOM_Real_ID (0); setM_Attribute_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_MPC_ProfileBOM_Real(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=MPC_ProfileBOM_Real */
	public static final String Table_Name = "MPC_ProfileBOM_Real";

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
		StringBuffer sb = new StringBuffer("X_MPC_ProfileBOM_Real[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Printed. Indicates if this document / line is printed
	 */
	public void setIsPrinted(boolean IsPrinted) {
		set_Value("IsPrinted", new Boolean(IsPrinted));
	}

	/**
	 * Get Printed. Indicates if this document / line is printed
	 */
	public boolean isPrinted() {
		Object oo = get_Value("IsPrinted");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsPrintedMax */
	public void setIsPrintedMax(boolean IsPrintedMax) {
		set_Value("IsPrintedMax", new Boolean(IsPrintedMax));
	}

	/** Get IsPrintedMax */
	public boolean isPrintedMax() {
		Object oo = get_Value("IsPrintedMax");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set MPC_ProfileBOM_ID */
	public void setMPC_ProfileBOM_ID(int MPC_ProfileBOM_ID) {
		if (MPC_ProfileBOM_ID < 1)
			throw new IllegalArgumentException(
					"MPC_ProfileBOM_ID is mandatory.");
		set_ValueNoCheck("MPC_ProfileBOM_ID", new Integer(MPC_ProfileBOM_ID));
	}

	/** Get MPC_ProfileBOM_ID */
	public int getMPC_ProfileBOM_ID() {
		Integer ii = (Integer) get_Value("MPC_ProfileBOM_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set MPC_ProfileBOM_Real_ID */
	public void setMPC_ProfileBOM_Real_ID(int MPC_ProfileBOM_Real_ID) {
		if (MPC_ProfileBOM_Real_ID < 1)
			throw new IllegalArgumentException(
					"MPC_ProfileBOM_Real_ID is mandatory.");
		set_ValueNoCheck("MPC_ProfileBOM_Real_ID", new Integer(
				MPC_ProfileBOM_Real_ID));
	}

	/** Get MPC_ProfileBOM_Real_ID */
	public int getMPC_ProfileBOM_Real_ID() {
		Integer ii = (Integer) get_Value("MPC_ProfileBOM_Real_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Attribute. Product Attribute
	 */
	public void setM_Attribute_ID(int M_Attribute_ID) {
		if (M_Attribute_ID < 1)
			throw new IllegalArgumentException("M_Attribute_ID is mandatory.");
		set_Value("M_Attribute_ID", new Integer(M_Attribute_ID));
	}

	/**
	 * Get Attribute. Product Attribute
	 */
	public int getM_Attribute_ID() {
		Integer ii = (Integer) get_Value("M_Attribute_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Maximum */
	public void setMaximum(BigDecimal Maximum) {
		set_Value("Maximum", Maximum);
	}

	/** Get Maximum */
	public BigDecimal getMaximum() {
		BigDecimal bd = (BigDecimal) get_Value("Maximum");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Minimum */
	public void setMinimum(BigDecimal Minimum) {
		set_Value("Minimum", Minimum);
	}

	/** Get Minimum */
	public BigDecimal getMinimum() {
		BigDecimal bd = (BigDecimal) get_Value("Minimum");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Realnut */
	public void setRealnut(BigDecimal Realnut) {
		set_Value("Realnut", Realnut);
	}

	/** Get Realnut */
	public BigDecimal getRealnut() {
		BigDecimal bd = (BigDecimal) get_Value("Realnut");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public void setValue(String Value) {
		if (Value != null && Value.length() > 40) {
			log.warning("Length > 40 - truncated");
			Value = Value.substring(0, 39);
		}
		set_Value("Value", Value);
	}

	/**
	 * Get Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public String getValue() {
		return (String) get_Value("Value");
	}
}
