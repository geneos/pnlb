/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for PA_Measure
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.937
 */
public class X_PA_Measure extends PO {
	/** Standard Constructor */
	public X_PA_Measure(Properties ctx, int PA_Measure_ID, String trxName) {
		super(ctx, PA_Measure_ID, trxName);
		/**
		 * if (PA_Measure_ID == 0) { setMeasureDataType (null); // T
		 * setMeasureType (null); // M setName (null); setPA_Measure_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_PA_Measure(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=PA_Measure */
	public static final String Table_Name = "PA_Measure";

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
		StringBuffer sb = new StringBuffer("X_PA_Measure[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Calculation Class. Java Class for calculation, implementing Interface
	 * Measure
	 */
	public void setCalculationClass(String CalculationClass) {
		if (CalculationClass != null && CalculationClass.length() > 60) {
			log.warning("Length > 60 - truncated");
			CalculationClass = CalculationClass.substring(0, 59);
		}
		set_Value("CalculationClass", CalculationClass);
	}

	/**
	 * Get Calculation Class. Java Class for calculation, implementing Interface
	 * Measure
	 */
	public String getCalculationClass() {
		return (String) get_Value("CalculationClass");
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
	 * Set Manual Actual. Manually entered actual value
	 */
	public void setManualActual(BigDecimal ManualActual) {
		set_Value("ManualActual", ManualActual);
	}

	/**
	 * Get Manual Actual. Manually entered actual value
	 */
	public BigDecimal getManualActual() {
		BigDecimal bd = (BigDecimal) get_Value("ManualActual");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Note. Note for manual entry
	 */
	public void setManualNote(String ManualNote) {
		if (ManualNote != null && ManualNote.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			ManualNote = ManualNote.substring(0, 1999);
		}
		set_Value("ManualNote", ManualNote);
	}

	/**
	 * Get Note. Note for manual entry
	 */
	public String getManualNote() {
		return (String) get_Value("ManualNote");
	}

	/** MeasureDataType AD_Reference_ID=369 */
	public static final int MEASUREDATATYPE_AD_Reference_ID = 369;

	/** Status Qty/Amount = S */
	public static final String MEASUREDATATYPE_StatusQtyAmount = "S";

	/** Qty/Amount in Time = T */
	public static final String MEASUREDATATYPE_QtyAmountInTime = "T";

	/**
	 * Set Measure Data Type. Type of data - Status or in Time
	 */
	public void setMeasureDataType(String MeasureDataType) {
		if (MeasureDataType == null)
			throw new IllegalArgumentException("MeasureDataType is mandatory");
		if (MeasureDataType.equals("S") || MeasureDataType.equals("T"))
			;
		else
			throw new IllegalArgumentException(
					"MeasureDataType Invalid value - " + MeasureDataType
							+ " - Reference_ID=369 - S - T");
		if (MeasureDataType.length() > 1) {
			log.warning("Length > 1 - truncated");
			MeasureDataType = MeasureDataType.substring(0, 0);
		}
		set_Value("MeasureDataType", MeasureDataType);
	}

	/**
	 * Get Measure Data Type. Type of data - Status or in Time
	 */
	public String getMeasureDataType() {
		return (String) get_Value("MeasureDataType");
	}

	/** MeasureType AD_Reference_ID=231 */
	public static final int MEASURETYPE_AD_Reference_ID = 231;

	/** Achievements = A */
	public static final String MEASURETYPE_Achievements = "A";

	/** Calculated = C */
	public static final String MEASURETYPE_Calculated = "C";

	/** Manual = M */
	public static final String MEASURETYPE_Manual = "M";

	/** Ratio = R */
	public static final String MEASURETYPE_Ratio = "R";

	/** User defined = U */
	public static final String MEASURETYPE_UserDefined = "U";

	/**
	 * Set Measure Type. Determines how the actual performance is derived
	 */
	public void setMeasureType(String MeasureType) {
		if (MeasureType == null)
			throw new IllegalArgumentException("MeasureType is mandatory");
		if (MeasureType.equals("A") || MeasureType.equals("C")
				|| MeasureType.equals("M") || MeasureType.equals("R")
				|| MeasureType.equals("U"))
			;
		else
			throw new IllegalArgumentException("MeasureType Invalid value - "
					+ MeasureType + " - Reference_ID=231 - A - C - M - R - U");
		if (MeasureType.length() > 1) {
			log.warning("Length > 1 - truncated");
			MeasureType = MeasureType.substring(0, 0);
		}
		set_Value("MeasureType", MeasureType);
	}

	/**
	 * Get Measure Type. Determines how the actual performance is derived
	 */
	public String getMeasureType() {
		return (String) get_Value("MeasureType");
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
	 * Set Benchmark. Performance Benchmark
	 */
	public void setPA_Benchmark_ID(int PA_Benchmark_ID) {
		if (PA_Benchmark_ID <= 0)
			set_Value("PA_Benchmark_ID", null);
		else
			set_Value("PA_Benchmark_ID", new Integer(PA_Benchmark_ID));
	}

	/**
	 * Get Benchmark. Performance Benchmark
	 */
	public int getPA_Benchmark_ID() {
		Integer ii = (Integer) get_Value("PA_Benchmark_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Reporting Hierarchy. Optional Reporting Hierarchy - If not selected
	 * the default hierarchy trees are used.
	 */
	public void setPA_Hierarchy_ID(int PA_Hierarchy_ID) {
		if (PA_Hierarchy_ID <= 0)
			set_Value("PA_Hierarchy_ID", null);
		else
			set_Value("PA_Hierarchy_ID", new Integer(PA_Hierarchy_ID));
	}

	/**
	 * Get Reporting Hierarchy. Optional Reporting Hierarchy - If not selected
	 * the default hierarchy trees are used.
	 */
	public int getPA_Hierarchy_ID() {
		Integer ii = (Integer) get_Value("PA_Hierarchy_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Measure Calculation. Calculation method for measuring performance
	 */
	public void setPA_MeasureCalc_ID(int PA_MeasureCalc_ID) {
		if (PA_MeasureCalc_ID <= 0)
			set_Value("PA_MeasureCalc_ID", null);
		else
			set_Value("PA_MeasureCalc_ID", new Integer(PA_MeasureCalc_ID));
	}

	/**
	 * Get Measure Calculation. Calculation method for measuring performance
	 */
	public int getPA_MeasureCalc_ID() {
		Integer ii = (Integer) get_Value("PA_MeasureCalc_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Measure. Concrete Performance Measurement
	 */
	public void setPA_Measure_ID(int PA_Measure_ID) {
		if (PA_Measure_ID < 1)
			throw new IllegalArgumentException("PA_Measure_ID is mandatory.");
		set_ValueNoCheck("PA_Measure_ID", new Integer(PA_Measure_ID));
	}

	/**
	 * Get Measure. Concrete Performance Measurement
	 */
	public int getPA_Measure_ID() {
		Integer ii = (Integer) get_Value("PA_Measure_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Ratio. Performace Ratio
	 */
	public void setPA_Ratio_ID(int PA_Ratio_ID) {
		if (PA_Ratio_ID <= 0)
			set_Value("PA_Ratio_ID", null);
		else
			set_Value("PA_Ratio_ID", new Integer(PA_Ratio_ID));
	}

	/**
	 * Get Ratio. Performace Ratio
	 */
	public int getPA_Ratio_ID() {
		Integer ii = (Integer) get_Value("PA_Ratio_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
