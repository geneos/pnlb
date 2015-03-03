/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for QM_Specification
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.156
 */
public class X_QM_Specification extends PO {
	/** Standard Constructor */
	public X_QM_Specification(Properties ctx, int QM_Specification_ID,
			String trxName) {
		super(ctx, QM_Specification_ID, trxName);
		/**
		 * if (QM_Specification_ID == 0) { setAD_Workflow_ID (0);
		 * setMPC_Product_BOM_ID (0); setM_AttributeSet_ID (0); setM_Product_ID
		 * (0); setQM_Specification_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_QM_Specification(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=QM_Specification */
	public static final String Table_Name = "QM_Specification";

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
		StringBuffer sb = new StringBuffer("X_QM_Specification[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Workflow. Workflow or combination of tasks
	 */
	public void setAD_Workflow_ID(int AD_Workflow_ID) {
		if (AD_Workflow_ID < 1)
			throw new IllegalArgumentException("AD_Workflow_ID is mandatory.");
		set_Value("AD_Workflow_ID", new Integer(AD_Workflow_ID));
	}

	/**
	 * Get Workflow. Workflow or combination of tasks
	 */
	public int getAD_Workflow_ID() {
		Integer ii = (Integer) get_Value("AD_Workflow_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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

	/** Set BOM & Formula */
	public void setMPC_Product_BOM_ID(int MPC_Product_BOM_ID) {
		if (MPC_Product_BOM_ID < 1)
			throw new IllegalArgumentException(
					"MPC_Product_BOM_ID is mandatory.");
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
	 * Set Attribute Set. Product Attribute Set
	 */
	public void setM_AttributeSet_ID(int M_AttributeSet_ID) {
		if (M_AttributeSet_ID < 0)
			throw new IllegalArgumentException(
					"M_AttributeSet_ID is mandatory.");
		set_Value("M_AttributeSet_ID", new Integer(M_AttributeSet_ID));
	}

	/**
	 * Get Attribute Set. Product Attribute Set
	 */
	public int getM_AttributeSet_ID() {
		Integer ii = (Integer) get_Value("M_AttributeSet_ID");
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

	/**
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name != null && Name.length() > 60) {
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

	/** Set QM_Specification_ID */
	public void setQM_Specification_ID(int QM_Specification_ID) {
		if (QM_Specification_ID < 1)
			throw new IllegalArgumentException(
					"QM_Specification_ID is mandatory.");
		set_ValueNoCheck("QM_Specification_ID",
				new Integer(QM_Specification_ID));
	}

	/** Get QM_Specification_ID */
	public int getQM_Specification_ID() {
		Integer ii = (Integer) get_Value("QM_Specification_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Valid from. Valid from including this date (first day)
	 */
	public void setValidFrom(Timestamp ValidFrom) {
		set_Value("ValidFrom", ValidFrom);
	}

	/**
	 * Get Valid from. Valid from including this date (first day)
	 */
	public Timestamp getValidFrom() {
		return (Timestamp) get_Value("ValidFrom");
	}

	/**
	 * Set Valid to. Valid to including this date (last day)
	 */
	public void setValidTo(Timestamp ValidTo) {
		set_Value("ValidTo", ValidTo);
	}

	/**
	 * Get Valid to. Valid to including this date (last day)
	 */
	public Timestamp getValidTo() {
		return (Timestamp) get_Value("ValidTo");
	}

	/**
	 * Set Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public void setValue(String Value) {
		if (Value != null && Value.length() > 40) {
			log.warning("Length > 40 - truncated");
			Value = Value.substring(0, 39);
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
