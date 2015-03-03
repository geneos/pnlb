/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for PA_BenchmarkData
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.828
 */
public class X_PA_BenchmarkData extends PO {
	/** Standard Constructor */
	public X_PA_BenchmarkData(Properties ctx, int PA_BenchmarkData_ID,
			String trxName) {
		super(ctx, PA_BenchmarkData_ID, trxName);
		/**
		 * if (PA_BenchmarkData_ID == 0) { setBenchmarkDate (new
		 * Timestamp(System.currentTimeMillis())); setBenchmarkValue (Env.ZERO);
		 * setName (null); setPA_BenchmarkData_ID (0); setPA_Benchmark_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_PA_BenchmarkData(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=PA_BenchmarkData */
	public static final String Table_Name = "PA_BenchmarkData";

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
		StringBuffer sb = new StringBuffer("X_PA_BenchmarkData[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Date. Benchmark Date
	 */
	public void setBenchmarkDate(Timestamp BenchmarkDate) {
		if (BenchmarkDate == null)
			throw new IllegalArgumentException("BenchmarkDate is mandatory.");
		set_Value("BenchmarkDate", BenchmarkDate);
	}

	/**
	 * Get Date. Benchmark Date
	 */
	public Timestamp getBenchmarkDate() {
		return (Timestamp) get_Value("BenchmarkDate");
	}

	/**
	 * Set Value. Benchmark Value
	 */
	public void setBenchmarkValue(BigDecimal BenchmarkValue) {
		if (BenchmarkValue == null)
			throw new IllegalArgumentException("BenchmarkValue is mandatory.");
		set_Value("BenchmarkValue", BenchmarkValue);
	}

	/**
	 * Get Value. Benchmark Value
	 */
	public BigDecimal getBenchmarkValue() {
		BigDecimal bd = (BigDecimal) get_Value("BenchmarkValue");
		if (bd == null)
			return Env.ZERO;
		return bd;
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
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name == null)
			throw new IllegalArgumentException("Name is mandatory.");
		if (Name.length() > 120) {
			log.warning("Length > 120 - truncated");
			Name = Name.substring(0, 119);
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
	 * Set Benchmark Data. Performance Benchmark Data Point
	 */
	public void setPA_BenchmarkData_ID(int PA_BenchmarkData_ID) {
		if (PA_BenchmarkData_ID < 1)
			throw new IllegalArgumentException(
					"PA_BenchmarkData_ID is mandatory.");
		set_ValueNoCheck("PA_BenchmarkData_ID",
				new Integer(PA_BenchmarkData_ID));
	}

	/**
	 * Get Benchmark Data. Performance Benchmark Data Point
	 */
	public int getPA_BenchmarkData_ID() {
		Integer ii = (Integer) get_Value("PA_BenchmarkData_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Benchmark. Performance Benchmark
	 */
	public void setPA_Benchmark_ID(int PA_Benchmark_ID) {
		if (PA_Benchmark_ID < 1)
			throw new IllegalArgumentException("PA_Benchmark_ID is mandatory.");
		set_ValueNoCheck("PA_Benchmark_ID", new Integer(PA_Benchmark_ID));
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
}
