/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_RMALine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.578
 */
public class X_M_RMALine extends PO {
	/** Standard Constructor */
	public X_M_RMALine(Properties ctx, int M_RMALine_ID, String trxName) {
		super(ctx, M_RMALine_ID, trxName);
		/**
		 * if (M_RMALine_ID == 0) { setM_InOutLine_ID (0); setM_RMALine_ID (0);
		 * setM_RMA_ID (0); setProcessed (false); setQty (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_M_RMALine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_RMALine */
	public static final String Table_Name = "M_RMALine";

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

	protected BigDecimal accessLevel = new BigDecimal(1);

	/** AccessLevel 1 - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_M_RMALine[").append(get_ID())
				.append("]");
		return sb.toString();
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
	 * Set Shipment/Receipt Line. Line on Shipment or Receipt document
	 */
	public void setM_InOutLine_ID(int M_InOutLine_ID) {
		if (M_InOutLine_ID < 1)
			throw new IllegalArgumentException("M_InOutLine_ID is mandatory.");
		set_Value("M_InOutLine_ID", new Integer(M_InOutLine_ID));
	}

	/**
	 * Get Shipment/Receipt Line. Line on Shipment or Receipt document
	 */
	public int getM_InOutLine_ID() {
		Integer ii = (Integer) get_Value("M_InOutLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set RMA Line. Return Material Authorization Line
	 */
	public void setM_RMALine_ID(int M_RMALine_ID) {
		if (M_RMALine_ID < 1)
			throw new IllegalArgumentException("M_RMALine_ID is mandatory.");
		set_ValueNoCheck("M_RMALine_ID", new Integer(M_RMALine_ID));
	}

	/**
	 * Get RMA Line. Return Material Authorization Line
	 */
	public int getM_RMALine_ID() {
		Integer ii = (Integer) get_Value("M_RMALine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set RMA. Return Material Authorization
	 */
	public void setM_RMA_ID(int M_RMA_ID) {
		if (M_RMA_ID < 1)
			throw new IllegalArgumentException("M_RMA_ID is mandatory.");
		set_ValueNoCheck("M_RMA_ID", new Integer(M_RMA_ID));
	}

	/**
	 * Get RMA. Return Material Authorization
	 */
	public int getM_RMA_ID() {
		Integer ii = (Integer) get_Value("M_RMA_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getM_RMA_ID()));
	}

	/**
	 * Set Processed. The document has been processed
	 */
	public void setProcessed(boolean Processed) {
		set_Value("Processed", new Boolean(Processed));
	}

	/**
	 * Get Processed. The document has been processed
	 */
	public boolean isProcessed() {
		Object oo = get_Value("Processed");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
}
