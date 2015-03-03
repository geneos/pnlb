/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_DemandLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.593
 */
public class X_M_DemandLine extends PO {
	/** Standard Constructor */
	public X_M_DemandLine(Properties ctx, int M_DemandLine_ID, String trxName) {
		super(ctx, M_DemandLine_ID, trxName);
		/**
		 * if (M_DemandLine_ID == 0) { setC_Period_ID (0); setM_DemandLine_ID
		 * (0); setM_Demand_ID (0); setM_Product_ID (0); setQty (Env.ZERO);
		 * setQtyCalculated (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_M_DemandLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_DemandLine */
	public static final String Table_Name = "M_DemandLine";

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

	protected BigDecimal accessLevel = new BigDecimal(2);

	/** AccessLevel 2 - Client */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_M_DemandLine[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Period. Period of the Calendar
	 */
	public void setC_Period_ID(int C_Period_ID) {
		if (C_Period_ID < 1)
			throw new IllegalArgumentException("C_Period_ID is mandatory.");
		set_ValueNoCheck("C_Period_ID", new Integer(C_Period_ID));
	}

	/**
	 * Get Period. Period of the Calendar
	 */
	public int getC_Period_ID() {
		Integer ii = (Integer) get_Value("C_Period_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getC_Period_ID()));
	}

	/**
	 * Set Demand Line. Material Demand Line
	 */
	public void setM_DemandLine_ID(int M_DemandLine_ID) {
		if (M_DemandLine_ID < 1)
			throw new IllegalArgumentException("M_DemandLine_ID is mandatory.");
		set_ValueNoCheck("M_DemandLine_ID", new Integer(M_DemandLine_ID));
	}

	/**
	 * Get Demand Line. Material Demand Line
	 */
	public int getM_DemandLine_ID() {
		Integer ii = (Integer) get_Value("M_DemandLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Demand. Material Demand
	 */
	public void setM_Demand_ID(int M_Demand_ID) {
		if (M_Demand_ID < 1)
			throw new IllegalArgumentException("M_Demand_ID is mandatory.");
		set_ValueNoCheck("M_Demand_ID", new Integer(M_Demand_ID));
	}

	/**
	 * Get Demand. Material Demand
	 */
	public int getM_Demand_ID() {
		Integer ii = (Integer) get_Value("M_Demand_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Product. Product, Service, Item
	 */
	public void setM_Product_ID(int M_Product_ID) {
		if (M_Product_ID < 1)
			throw new IllegalArgumentException("M_Product_ID is mandatory.");
		set_ValueNoCheck("M_Product_ID", new Integer(M_Product_ID));
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
	 * Set Quantity. Quantity
	 */
	public void setQty(BigDecimal Qty) {
		if (Qty == null)
			throw new IllegalArgumentException("Qty is mandatory.");
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
	 * Set Calculated Quantity. Calculated Quantity
	 */
	public void setQtyCalculated(BigDecimal QtyCalculated) {
		if (QtyCalculated == null)
			throw new IllegalArgumentException("QtyCalculated is mandatory.");
		set_Value("QtyCalculated", QtyCalculated);
	}

	/**
	 * Get Calculated Quantity. Calculated Quantity
	 */
	public BigDecimal getQtyCalculated() {
		BigDecimal bd = (BigDecimal) get_Value("QtyCalculated");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
