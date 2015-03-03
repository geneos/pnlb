/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_ForecastLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.75
 */
public class X_M_ForecastLine extends PO {
	/** Standard Constructor */
	public X_M_ForecastLine(Properties ctx, int M_ForecastLine_ID,
			String trxName) {
		super(ctx, M_ForecastLine_ID, trxName);
		/**
		 * if (M_ForecastLine_ID == 0) { setC_Period_ID (0);
		 * setM_ForecastLine_ID (0); setM_Forecast_ID (0); setM_Product_ID (0);
		 * setQty (Env.ZERO); setQtyCalculated (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_M_ForecastLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_ForecastLine */
	public static final String Table_Name = "M_ForecastLine";

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
		StringBuffer sb = new StringBuffer("X_M_ForecastLine[")
				.append(get_ID()).append("]");
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
	 * Set Date Promised. Date Order was promised
	 */
	public void setDatePromised(Timestamp DatePromised) {
		set_Value("DatePromised", DatePromised);
	}

	/**
	 * Get Date Promised. Date Order was promised
	 */
	public Timestamp getDatePromised() {
		return (Timestamp) get_Value("DatePromised");
	}

	/**
	 * Set Forecast Line. Forecast Line
	 */
	public void setM_ForecastLine_ID(int M_ForecastLine_ID) {
		if (M_ForecastLine_ID < 1)
			throw new IllegalArgumentException(
					"M_ForecastLine_ID is mandatory.");
		set_ValueNoCheck("M_ForecastLine_ID", new Integer(M_ForecastLine_ID));
	}

	/**
	 * Get Forecast Line. Forecast Line
	 */
	public int getM_ForecastLine_ID() {
		Integer ii = (Integer) get_Value("M_ForecastLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Forecast. Material Forecast
	 */
	public void setM_Forecast_ID(int M_Forecast_ID) {
		if (M_Forecast_ID < 1)
			throw new IllegalArgumentException("M_Forecast_ID is mandatory.");
		set_ValueNoCheck("M_Forecast_ID", new Integer(M_Forecast_ID));
	}

	/**
	 * Get Forecast. Material Forecast
	 */
	public int getM_Forecast_ID() {
		Integer ii = (Integer) get_Value("M_Forecast_ID");
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
	 * Set Warehouse. Storage Warehouse and Service Point
	 */
	public void setM_Warehouse_ID(int M_Warehouse_ID) {
		if (M_Warehouse_ID <= 0)
			set_Value("M_Warehouse_ID", null);
		else
			set_Value("M_Warehouse_ID", new Integer(M_Warehouse_ID));
	}

	/**
	 * Get Warehouse. Storage Warehouse and Service Point
	 */
	public int getM_Warehouse_ID() {
		Integer ii = (Integer) get_Value("M_Warehouse_ID");
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
