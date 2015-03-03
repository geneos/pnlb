/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_CyclePhase
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.234
 */
public class X_C_CyclePhase extends PO {
	/** Standard Constructor */
	public X_C_CyclePhase(Properties ctx, int C_CyclePhase_ID, String trxName) {
		super(ctx, C_CyclePhase_ID, trxName);
		/**
		 * if (C_CyclePhase_ID == 0) { setC_CycleStep_ID (0); setC_Phase_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_C_CyclePhase(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_CyclePhase */
	public static final String Table_Name = "C_CyclePhase";

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
		StringBuffer sb = new StringBuffer("X_C_CyclePhase[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Cycle Step. The step for this Cycle
	 */
	public void setC_CycleStep_ID(int C_CycleStep_ID) {
		if (C_CycleStep_ID < 1)
			throw new IllegalArgumentException("C_CycleStep_ID is mandatory.");
		set_ValueNoCheck("C_CycleStep_ID", new Integer(C_CycleStep_ID));
	}

	/**
	 * Get Cycle Step. The step for this Cycle
	 */
	public int getC_CycleStep_ID() {
		Integer ii = (Integer) get_Value("C_CycleStep_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Standard Phase. Standard Phase of the Project Type
	 */
	public void setC_Phase_ID(int C_Phase_ID) {
		if (C_Phase_ID < 1)
			throw new IllegalArgumentException("C_Phase_ID is mandatory.");
		set_ValueNoCheck("C_Phase_ID", new Integer(C_Phase_ID));
	}

	/**
	 * Get Standard Phase. Standard Phase of the Project Type
	 */
	public int getC_Phase_ID() {
		Integer ii = (Integer) get_Value("C_Phase_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
