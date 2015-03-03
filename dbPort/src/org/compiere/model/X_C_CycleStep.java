/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_CycleStep
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.234
 */
public class X_C_CycleStep extends PO {
	/** Standard Constructor */
	public X_C_CycleStep(Properties ctx, int C_CycleStep_ID, String trxName) {
		super(ctx, C_CycleStep_ID, trxName);
		/**
		 * if (C_CycleStep_ID == 0) { setC_CycleStep_ID (0); setC_Cycle_ID (0);
		 * setName (null); setRelativeWeight (Env.ZERO); // 1 setSeqNo (0); //
		 * 
		 * @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM C_CycleStep
		 *             WHERE C_Cycle_ID=@C_Cycle_ID@ }
		 */
	}

	/** Load Constructor */
	public X_C_CycleStep(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_CycleStep */
	public static final String Table_Name = "C_CycleStep";

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
		StringBuffer sb = new StringBuffer("X_C_CycleStep[").append(get_ID())
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
	 * Set Project Cycle. Identifier for this Project Reporting Cycle
	 */
	public void setC_Cycle_ID(int C_Cycle_ID) {
		if (C_Cycle_ID < 1)
			throw new IllegalArgumentException("C_Cycle_ID is mandatory.");
		set_ValueNoCheck("C_Cycle_ID", new Integer(C_Cycle_ID));
	}

	/**
	 * Get Project Cycle. Identifier for this Project Reporting Cycle
	 */
	public int getC_Cycle_ID() {
		Integer ii = (Integer) get_Value("C_Cycle_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
	 * Set Relative Weight. Relative weight of this step (0 = ignored)
	 */
	public void setRelativeWeight(BigDecimal RelativeWeight) {
		if (RelativeWeight == null)
			throw new IllegalArgumentException("RelativeWeight is mandatory.");
		set_Value("RelativeWeight", RelativeWeight);
	}

	/**
	 * Get Relative Weight. Relative weight of this step (0 = ignored)
	 */
	public BigDecimal getRelativeWeight() {
		BigDecimal bd = (BigDecimal) get_Value("RelativeWeight");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Sequence. Method of ordering records; lowest number comes first
	 */
	public void setSeqNo(int SeqNo) {
		set_Value("SeqNo", new Integer(SeqNo));
	}

	/**
	 * Get Sequence. Method of ordering records; lowest number comes first
	 */
	public int getSeqNo() {
		Integer ii = (Integer) get_Value("SeqNo");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
