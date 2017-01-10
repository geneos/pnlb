/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_AttributeInstance
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.375
 */
public class X_M_AttributeInstance extends PO {
	/** Standard Constructor */
	public X_M_AttributeInstance(Properties ctx, int M_AttributeInstance_ID,
			String trxName) {
		super(ctx, M_AttributeInstance_ID, trxName);
		/**
		 * if (M_AttributeInstance_ID == 0) { setM_AttributeSetInstance_ID (0);
		 * setM_Attribute_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_M_AttributeInstance(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_AttributeInstance */
	public static final String Table_Name = "M_AttributeInstance";

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
		StringBuffer sb = new StringBuffer("X_M_AttributeInstance[").append(
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
	 * Set Attribute Value. Product Attribute Value
	 */
	public void setM_AttributeValue_ID(int M_AttributeValue_ID) {
		if (M_AttributeValue_ID <= 0)
			set_Value("M_AttributeValue_ID", null);
		else
			set_Value("M_AttributeValue_ID", new Integer(M_AttributeValue_ID));
	}

	/**
	 * Get Attribute Value. Product Attribute Value
	 */
	public int getM_AttributeValue_ID() {
		Integer ii = (Integer) get_Value("M_AttributeValue_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getM_AttributeValue_ID()));
	}

	/**
	 * Set Attribute. Product Attribute
	 */
	public void setM_Attribute_ID(int M_Attribute_ID) {
		if (M_Attribute_ID < 1)
			throw new IllegalArgumentException("M_Attribute_ID is mandatory.");
		set_ValueNoCheck("M_Attribute_ID", new Integer(M_Attribute_ID));
	}

	/**
	 * Get Attribute. Product Attribute
	 */
	public int getM_Attribute_ID() {
		Integer ii = (Integer) get_Value("M_Attribute_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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

	/**
	 * Set Value. Numeric Value
	 */
	public void setValueNumber(BigDecimal ValueNumber) {
		set_Value("ValueNumber", ValueNumber);
	}

	/**
	 * Get Value. Numeric Value
	 */
	public BigDecimal getValueNumber() {
		BigDecimal bd = (BigDecimal) get_Value("ValueNumber");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
        
                       /**
	 * Set Lote Andreani. 
	 */
	public void setLoteAndreani(String LoteAndreani) {
                                if (LoteAndreani != null && LoteAndreani.length() > 40) {
                                        log.warning("Length > 40 - truncated");
                                        LoteAndreani = LoteAndreani.substring(0, 50);
                                }
                                set_Value("LOTE_ANREANI", LoteAndreani);
	}

	/**
	 * Get Lote Andreani. 
	 */
	public String getLoteAndreani() {
		return (String) get_Value("LOTE_ANDREANI");
	}
}
