/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_STOCKCONPARTIDA
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:05.25
 */
public class X_T_STOCKCONPARTIDA extends PO {
	/** Standard Constructor */
	public X_T_STOCKCONPARTIDA(Properties ctx, int T_STOCKCONPARTIDA_ID,
			String trxName) {
		super(ctx, T_STOCKCONPARTIDA_ID, trxName);
		/**
		 * if (T_STOCKCONPARTIDA_ID == 0) { setAD_PInstance_ID (0); setC_UOM_ID
		 * (0); setFECHA (new Timestamp(System.currentTimeMillis()));
		 * setM_AttributeSetInstance_ID (0); setM_Product_Category_ID (0);
		 * setM_Product_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_T_STOCKCONPARTIDA(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_STOCKCONPARTIDA */
	public static final String Table_Name = "T_STOCKCONPARTIDA";

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
		StringBuffer sb = new StringBuffer("X_T_STOCKCONPARTIDA[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Process Instance. Instance of the process
	 */
	public void setAD_PInstance_ID(int AD_PInstance_ID) {
		if (AD_PInstance_ID < 1)
			throw new IllegalArgumentException("AD_PInstance_ID is mandatory.");
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

	/**
	 * Set UOM. Unit of Measure
	 */
	public void setC_UOM_ID(int C_UOM_ID) {
		if (C_UOM_ID < 1)
			throw new IllegalArgumentException("C_UOM_ID is mandatory.");
		set_Value("C_UOM_ID", new Integer(C_UOM_ID));
	}

	/**
	 * Get UOM. Unit of Measure
	 */
	public int getC_UOM_ID() {
		Integer ii = (Integer) get_Value("C_UOM_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set FECHA */
	public void setFECHA(Timestamp FECHA) {
		if (FECHA == null)
			throw new IllegalArgumentException("FECHA is mandatory.");
		set_Value("FECHA", FECHA);
	}

	/** Get FECHA */
	public Timestamp getFECHA() {
		return (Timestamp) get_Value("FECHA");
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
	 * Set Product Category. Category of a Product
	 */
	public void setM_Product_Category_ID(int M_Product_Category_ID) {
		if (M_Product_Category_ID < 1)
			throw new IllegalArgumentException(
					"M_Product_Category_ID is mandatory.");
		set_Value("M_Product_Category_ID", new Integer(M_Product_Category_ID));
	}

	/**
	 * Get Product Category. Category of a Product
	 */
	public int getM_Product_Category_ID() {
		Integer ii = (Integer) get_Value("M_Product_Category_ID");
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

	/** Set M_WAREHOUSE_NAME */
	public void setM_WAREHOUSE_NAME(String M_WAREHOUSE_NAME) {
		if (M_WAREHOUSE_NAME != null && M_WAREHOUSE_NAME.length() > 240) {
			log.warning("Length > 240 - truncated");
			M_WAREHOUSE_NAME = M_WAREHOUSE_NAME.substring(0, 239);
		}
		set_Value("M_WAREHOUSE_NAME", M_WAREHOUSE_NAME);
	}

	/** Get M_WAREHOUSE_NAME */
	public String getM_WAREHOUSE_NAME() {
		return (String) get_Value("M_WAREHOUSE_NAME");
	}

	/**
	 * Set Movement Quantity. Quantity of a product moved.
	 */
	public void setMovementQty(BigDecimal MovementQty) {
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

	/**
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name != null && Name.length() > 480) {
			log.warning("Length > 480 - truncated");
			Name = Name.substring(0, 479);
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
	 * Set Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public void setValue(String Value) {
		if (Value != null && Value.length() > 240) {
			log.warning("Length > 240 - truncated");
			Value = Value.substring(0, 239);
		}
		set_Value("Value", Value);
	}

	/**
	 * Get Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public String getValue() {
		return (String) get_Value("Value");
	}
}
