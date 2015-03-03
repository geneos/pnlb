/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_BOMLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.875
 */
public class X_T_BOMLine extends PO {
	/** Standard Constructor */
	public X_T_BOMLine(Properties ctx, int T_BOMLine_ID, String trxName) {
		super(ctx, T_BOMLine_ID, trxName);
		/**
		 * if (T_BOMLine_ID == 0) { setT_BOMLine_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_T_BOMLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_BOMLine */
	public static final String Table_Name = "T_BOMLine";

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

	protected BigDecimal accessLevel = new BigDecimal(7);

	/** AccessLevel 7 - System - Client - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_T_BOMLine[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Process Instance. Instance of the process
	 */
	public void setAD_PInstance_ID(int AD_PInstance_ID) {
		if (AD_PInstance_ID <= 0)
			set_Value("AD_PInstance_ID", null);
		else
			set_Value("AD_PInstance_ID", new Integer(AD_PInstance_ID));
	}

	/**
	 * Get Process Instance. Instance of the process
	 */
	public int getAD_PInstance_ID() {
		Integer ii = (Integer) get_Value("AD_PInstance_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Level no */
	public void setLevelNo(int LevelNo) {
		set_Value("LevelNo", new Integer(LevelNo));
	}

	/** Get Level no */
	public int getLevelNo() {
		Integer ii = (Integer) get_Value("LevelNo");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Levels */
	public void setLevels(String Levels) {
		if (Levels != null && Levels.length() > 250) {
			log.warning("Length > 250 - truncated");
			Levels = Levels.substring(0, 249);
		}
		set_Value("Levels", Levels);
	}

	/** Get Levels */
	public String getLevels() {
		return (String) get_Value("Levels");
	}

	/** Set MPC_Product_BOMLine_ID */
	public void setMPC_Product_BOMLine_ID(int MPC_Product_BOMLine_ID) {
		if (MPC_Product_BOMLine_ID <= 0)
			set_Value("MPC_Product_BOMLine_ID", null);
		else
			set_Value("MPC_Product_BOMLine_ID", new Integer(
					MPC_Product_BOMLine_ID));
	}

	/** Get MPC_Product_BOMLine_ID */
	public int getMPC_Product_BOMLine_ID() {
		Integer ii = (Integer) get_Value("MPC_Product_BOMLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set BOM & Formula */
	public void setMPC_Product_BOM_ID(int MPC_Product_BOM_ID) {
		if (MPC_Product_BOM_ID <= 0)
			set_Value("MPC_Product_BOM_ID", null);
		else
			set_Value("MPC_Product_BOM_ID", new Integer(MPC_Product_BOM_ID));
	}

	/** Get BOM & Formula */
	public int getMPC_Product_BOM_ID() {
		Integer ii = (Integer) get_Value("MPC_Product_BOM_ID");
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

	/** Set T_BOMLine_ID */
	public void setT_BOMLine_ID(int T_BOMLine_ID) {
		if (T_BOMLine_ID < 1)
			throw new IllegalArgumentException("T_BOMLine_ID is mandatory.");
		set_ValueNoCheck("T_BOMLine_ID", new Integer(T_BOMLine_ID));
	}

	/** Get T_BOMLine_ID */
	public int getT_BOMLine_ID() {
		Integer ii = (Integer) get_Value("T_BOMLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
