/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_Attribute
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.359
 */
public class X_M_Attribute extends PO {
	/** Standard Constructor */
	public X_M_Attribute(Properties ctx, int M_Attribute_ID, String trxName) {
		super(ctx, M_Attribute_ID, trxName);
		/**
		 * if (M_Attribute_ID == 0) { setAttributeValueType (null); // S
		 * setIsInstanceAttribute (false); setIsMandatory (false);
		 * setM_Attribute_ID (0); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_M_Attribute(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_Attribute */
	public static final String Table_Name = "M_Attribute";

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
		StringBuffer sb = new StringBuffer("X_M_Attribute[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** AttributeValueType AD_Reference_ID=326 */
	public static final int ATTRIBUTEVALUETYPE_AD_Reference_ID = 326;

	/** List = L */
	public static final String ATTRIBUTEVALUETYPE_List = "L";

	/** Number = N */
	public static final String ATTRIBUTEVALUETYPE_Number = "N";

	/** String (max 40) = S */
	public static final String ATTRIBUTEVALUETYPE_StringMax40 = "S";

	/**
	 * Set Attribute Value Type. Type of Attribute Value
	 */
	public void setAttributeValueType(String AttributeValueType) {
		if (AttributeValueType == null)
			throw new IllegalArgumentException(
					"AttributeValueType is mandatory");
		if (AttributeValueType.equals("L") || AttributeValueType.equals("N")
				|| AttributeValueType.equals("S"))
			;
		else
			throw new IllegalArgumentException(
					"AttributeValueType Invalid value - " + AttributeValueType
							+ " - Reference_ID=326 - L - N - S");
		if (AttributeValueType.length() > 1) {
			log.warning("Length > 1 - truncated");
			AttributeValueType = AttributeValueType.substring(0, 0);
		}
		set_Value("AttributeValueType", AttributeValueType);
	}

	/**
	 * Get Attribute Value Type. Type of Attribute Value
	 */
	public String getAttributeValueType() {
		return (String) get_Value("AttributeValueType");
	}

	/**
	 * Set UOM. Unit of Measure
	 */
	public void setC_UOM_ID(int C_UOM_ID) {
		if (C_UOM_ID <= 0)
			set_Value("C_UOM_ID", null);
		else
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
	 * Set Instance Attribute. The product attribute is specific to the instance
	 * (like Serial No, Lot or Guarantee Date)
	 */
	public void setIsInstanceAttribute(boolean IsInstanceAttribute) {
		set_Value("IsInstanceAttribute", new Boolean(IsInstanceAttribute));
	}

	/**
	 * Get Instance Attribute. The product attribute is specific to the instance
	 * (like Serial No, Lot or Guarantee Date)
	 */
	public boolean isInstanceAttribute() {
		Object oo = get_Value("IsInstanceAttribute");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Mandatory. Data entry is required in this column
	 */
	public void setIsMandatory(boolean IsMandatory) {
		set_Value("IsMandatory", new Boolean(IsMandatory));
	}

	/**
	 * Get Mandatory. Data entry is required in this column
	 */
	public boolean isMandatory() {
		Object oo = get_Value("IsMandatory");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Attribute Search. Common Search Attribute
	 */
	public void setM_AttributeSearch_ID(int M_AttributeSearch_ID) {
		if (M_AttributeSearch_ID <= 0)
			set_Value("M_AttributeSearch_ID", null);
		else
			set_Value("M_AttributeSearch_ID", new Integer(M_AttributeSearch_ID));
	}

	/**
	 * Get Attribute Search. Common Search Attribute
	 */
	public int getM_AttributeSearch_ID() {
		Integer ii = (Integer) get_Value("M_AttributeSearch_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name == null)
			throw new IllegalArgumentException("Name is mandatory.");
		if (Name.length() > 60) {
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getName());
	}
}
