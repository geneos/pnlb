/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_DemandDetail
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.593
 */
public class X_M_DemandDetail extends PO {
	/** Standard Constructor */
	public X_M_DemandDetail(Properties ctx, int M_DemandDetail_ID,
			String trxName) {
		super(ctx, M_DemandDetail_ID, trxName);
		/**
		 * if (M_DemandDetail_ID == 0) { setM_DemandDetail_ID (0);
		 * setM_DemandLine_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_M_DemandDetail(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_DemandDetail */
	public static final String Table_Name = "M_DemandDetail";

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
		StringBuffer sb = new StringBuffer("X_M_DemandDetail[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Sales Order Line. Sales Order Line
	 */
	public void setC_OrderLine_ID(int C_OrderLine_ID) {
		if (C_OrderLine_ID <= 0)
			set_Value("C_OrderLine_ID", null);
		else
			set_Value("C_OrderLine_ID", new Integer(C_OrderLine_ID));
	}

	/**
	 * Get Sales Order Line. Sales Order Line
	 */
	public int getC_OrderLine_ID() {
		Integer ii = (Integer) get_Value("C_OrderLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Demand Detail. Material Demand Line Source Detail
	 */
	public void setM_DemandDetail_ID(int M_DemandDetail_ID) {
		if (M_DemandDetail_ID < 1)
			throw new IllegalArgumentException(
					"M_DemandDetail_ID is mandatory.");
		set_ValueNoCheck("M_DemandDetail_ID", new Integer(M_DemandDetail_ID));
	}

	/**
	 * Get Demand Detail. Material Demand Line Source Detail
	 */
	public int getM_DemandDetail_ID() {
		Integer ii = (Integer) get_Value("M_DemandDetail_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getM_DemandDetail_ID()));
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
	 * Set Forecast Line. Forecast Line
	 */
	public void setM_ForecastLine_ID(int M_ForecastLine_ID) {
		if (M_ForecastLine_ID <= 0)
			set_Value("M_ForecastLine_ID", null);
		else
			set_Value("M_ForecastLine_ID", new Integer(M_ForecastLine_ID));
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
	 * Set Requisition Line. Material Requisition Line
	 */
	public void setM_RequisitionLine_ID(int M_RequisitionLine_ID) {
		if (M_RequisitionLine_ID <= 0)
			set_Value("M_RequisitionLine_ID", null);
		else
			set_Value("M_RequisitionLine_ID", new Integer(M_RequisitionLine_ID));
	}

	/**
	 * Get Requisition Line. Material Requisition Line
	 */
	public int getM_RequisitionLine_ID() {
		Integer ii = (Integer) get_Value("M_RequisitionLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
