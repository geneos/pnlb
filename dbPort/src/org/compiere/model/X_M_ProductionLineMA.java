/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_ProductionLineMA
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.531
 */
public class X_M_ProductionLineMA extends PO {
	/** Standard Constructor */
	public X_M_ProductionLineMA(Properties ctx, int M_ProductionLineMA_ID,
			String trxName) {
		super(ctx, M_ProductionLineMA_ID, trxName);
		/**
		 * if (M_ProductionLineMA_ID == 0) { setM_AttributeSetInstance_ID (0);
		 * setM_ProductionLine_ID (0); setMovementQty (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_M_ProductionLineMA(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_ProductionLineMA */
	public static final String Table_Name = "M_ProductionLineMA";

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
		StringBuffer sb = new StringBuffer("X_M_ProductionLineMA[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Attribute Set Instance. Product Attribute Set Instance
	 */
	public void setM_AttributeSetInstance_ID(int M_AttributeSetInstance_ID) {
		if (M_AttributeSetInstance_ID < 0)
			throw new IllegalArgumentException(
					"M_AttributeSetInstance_ID is mandatory.");
		set_ValueNoCheck("M_AttributeSetInstance_ID", new Integer(
				M_AttributeSetInstance_ID));
	}

	/**
	 * Get Attribute Set Instance. Product Attribute Set Instance
	 */
	public int getM_AttributeSetInstance_ID() {
		Integer ii = (Integer) get_Value("M_AttributeSetInstance_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Production Line. Document Line representing a production
	 */
	public void setM_ProductionLine_ID(int M_ProductionLine_ID) {
		if (M_ProductionLine_ID < 1)
			throw new IllegalArgumentException(
					"M_ProductionLine_ID is mandatory.");
		set_ValueNoCheck("M_ProductionLine_ID",
				new Integer(M_ProductionLine_ID));
	}

	/**
	 * Get Production Line. Document Line representing a production
	 */
	public int getM_ProductionLine_ID() {
		Integer ii = (Integer) get_Value("M_ProductionLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getM_ProductionLine_ID()));
	}

	/**
	 * Set Movement Quantity. Quantity of a product moved.
	 */
	public void setMovementQty(BigDecimal MovementQty) {
		if (MovementQty == null)
			throw new IllegalArgumentException("MovementQty is mandatory.");
		set_Value("MovementQty", MovementQty);
	}

	/**
	 * Get Movement Quantity. Quantity of a product moved.
	 */
	public BigDecimal getMovementQty() {
		BigDecimal bd = (BigDecimal) get_Value("MovementQty");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
