/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for PA_Benchmark
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.812
 */
public class X_PA_Benchmark extends PO {
	/** Standard Constructor */
	public X_PA_Benchmark(Properties ctx, int PA_Benchmark_ID, String trxName) {
		super(ctx, PA_Benchmark_ID, trxName);
		/**
		 * if (PA_Benchmark_ID == 0) { setAccumulationType (null); setName
		 * (null); setPA_Benchmark_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_PA_Benchmark(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=PA_Benchmark */
	public static final String Table_Name = "PA_Benchmark";

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
		StringBuffer sb = new StringBuffer("X_PA_Benchmark[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** AccumulationType AD_Reference_ID=370 */
	public static final int ACCUMULATIONTYPE_AD_Reference_ID = 370;

	/** Average = A */
	public static final String ACCUMULATIONTYPE_Average = "A";

	/** Sum = S */
	public static final String ACCUMULATIONTYPE_Sum = "S";

	/**
	 * Set Accumulation Type. How to accumulate data on time axis
	 */
	public void setAccumulationType(String AccumulationType) {
		if (AccumulationType == null)
			throw new IllegalArgumentException("AccumulationType is mandatory");
		if (AccumulationType.equals("A") || AccumulationType.equals("S"))
			;
		else
			throw new IllegalArgumentException(
					"AccumulationType Invalid value - " + AccumulationType
							+ " - Reference_ID=370 - A - S");
		if (AccumulationType.length() > 1) {
			log.warning("Length > 1 - truncated");
			AccumulationType = AccumulationType.substring(0, 0);
		}
		set_Value("AccumulationType", AccumulationType);
	}

	/**
	 * Get Accumulation Type. How to accumulate data on time axis
	 */
	public String getAccumulationType() {
		return (String) get_Value("AccumulationType");
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
	 * Set Comment/Help. Comment or Hint
	 */
	public void setHelp(String Help) {
		if (Help != null && Help.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Help = Help.substring(0, 1999);
		}
		set_Value("Help", Help);
	}

	/**
	 * Get Comment/Help. Comment or Hint
	 */
	public String getHelp() {
		return (String) get_Value("Help");
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
