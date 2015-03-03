/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for MPC_Order_BOMLineMA
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:01.968
 */
public class X_MPC_Order_BOMLineMA extends PO {
	/** Standard Constructor */
	public X_MPC_Order_BOMLineMA(Properties ctx, int MPC_Order_BOMLineMA_ID,
			String trxName) {
		super(ctx, MPC_Order_BOMLineMA_ID, trxName);
		/**
		 * if (MPC_Order_BOMLineMA_ID == 0) { setMPC_Order_BOMLine_ID (0);
		 * setM_AttributeSetInstance_ID (0); setMovementQty (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_MPC_Order_BOMLineMA(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=MPC_Order_BOMLineMA */
	public static final String Table_Name = "MPC_Order_BOMLineMA";

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
		StringBuffer sb = new StringBuffer("X_MPC_Order_BOMLineMA[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/** Set Order BOM Line ID */
	public void setMPC_Order_BOMLine_ID(int MPC_Order_BOMLine_ID) {
		if (MPC_Order_BOMLine_ID < 1)
			throw new IllegalArgumentException(
					"MPC_Order_BOMLine_ID is mandatory.");
		set_Value("MPC_Order_BOMLine_ID", new Integer(MPC_Order_BOMLine_ID));
	}

	/** Get Order BOM Line ID */
	public int getMPC_Order_BOMLine_ID() {
		Integer ii = (Integer) get_Value("MPC_Order_BOMLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Attribute Set Instance. Product Attribute Set Instance
	 */
	public void setM_AttributeSetInstance_ID(int M_AttributeSetInstance_ID) {
		if (M_AttributeSetInstance_ID < 0)
			throw new IllegalArgumentException(
					"M_AttributeSetInstance_ID is mandatory.");
		set_Value("M_AttributeSetInstance_ID", new Integer(
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
