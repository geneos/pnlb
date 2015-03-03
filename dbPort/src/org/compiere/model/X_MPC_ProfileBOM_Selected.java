/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for MPC_ProfileBOM_Selected
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.296
 */
public class X_MPC_ProfileBOM_Selected extends PO {
	/** Standard Constructor */
	public X_MPC_ProfileBOM_Selected(Properties ctx,
			int MPC_ProfileBOM_Selected_ID, String trxName) {
		super(ctx, MPC_ProfileBOM_Selected_ID, trxName);
		/**
		 * if (MPC_ProfileBOM_Selected_ID == 0) { setMPC_ProfileBOM_ID (0);
		 * setMPC_ProfileBOM_Selected_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_MPC_ProfileBOM_Selected(Properties ctx, ResultSet rs,
			String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=MPC_ProfileBOM_Selected */
	public static final String Table_Name = "MPC_ProfileBOM_Selected";

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
		StringBuffer sb = new StringBuffer("X_MPC_ProfileBOM_Selected[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set UOM. Unit of Measure
	 */
	public void setC_UOM_ID(int C_UOM_ID) {
		if (C_UOM_ID <= 0)
			set_Value("C_UOM_ID", null);
		else
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

	/**
	 * Set Generate To. Generate To
	 */
	public void setGenerateTo(String GenerateTo) {
		if (GenerateTo != null && GenerateTo.length() > 1) {
			log.warning("Length > 1 - truncated");
			GenerateTo = GenerateTo.substring(0, 0);
		}
		set_Value("GenerateTo", GenerateTo);
	}

	/**
	 * Get Generate To. Generate To
	 */
	public String getGenerateTo() {
		return (String) get_Value("GenerateTo");
	}

	/** Set IsAlias */
	public void setIsAlias(boolean IsAlias) {
		set_Value("IsAlias", new Boolean(IsAlias));
	}

	/** Get IsAlias */
	public boolean isAlias() {
		Object oo = get_Value("IsAlias");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsCritical */
	public void setIsCritical(boolean IsCritical) {
		set_Value("IsCritical", new Boolean(IsCritical));
	}

	/** Get IsCritical */
	public boolean isCritical() {
		Object oo = get_Value("IsCritical");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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

	/**
	 * Set Line No. Unique line for this document
	 */
	public void setLine(int Line) {
		set_Value("Line", new Integer(Line));
	}

	/**
	 * Get Line No. Unique line for this document
	 */
	public int getLine() {
		Integer ii = (Integer) get_Value("Line");
		if (ii == null)
			return 0;
		return ii.intValue();
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

	/** Set MPC_ProfileBOM_Selected_ID */
	public void setMPC_ProfileBOM_Selected_ID(int MPC_ProfileBOM_Selected_ID) {
		if (MPC_ProfileBOM_Selected_ID < 1)
			throw new IllegalArgumentException(
					"MPC_ProfileBOM_Selected_ID is mandatory.");
		set_ValueNoCheck("MPC_ProfileBOM_Selected_ID", new Integer(
				MPC_ProfileBOM_Selected_ID));
	}

	/** Get MPC_ProfileBOM_Selected_ID */
	public int getMPC_ProfileBOM_Selected_ID() {
		Integer ii = (Integer) get_Value("MPC_ProfileBOM_Selected_ID");
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

	/** Planteamiento AD_Reference_ID=1000030 */
	public static final int PLANTEAMIENTO_AD_Reference_ID = 1000030;

	/** Set Planteamiento */
	public void setPlanteamiento(int Planteamiento) {
		set_Value("Planteamiento", new Integer(Planteamiento));
	}

	/** Get Planteamiento */
	public int getPlanteamiento() {
		Integer ii = (Integer) get_Value("Planteamiento");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set List Price. List Price
	 */
	public void setPriceList(BigDecimal PriceList) {
		set_Value("PriceList", PriceList);
	}

	/**
	 * Get List Price. List Price
	 */
	public BigDecimal getPriceList() {
		BigDecimal bd = (BigDecimal) get_Value("PriceList");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Process */
	public void setProcess(String Process) {
		if (Process != null && Process.length() > 1) {
			log.warning("Length > 1 - truncated");
			Process = Process.substring(0, 0);
		}
		set_Value("Process", Process);
	}

	/** Get Process */
	public String getProcess() {
		return (String) get_Value("Process");
	}

	/**
	 * Set Quantity. Quantity
	 */
	public void setQty(BigDecimal Qty) {
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

	/**
	 * Set Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public void setValue(String Value) {
		if (Value != null && Value.length() > 80) {
			log.warning("Length > 80 - truncated");
			Value = Value.substring(0, 79);
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
