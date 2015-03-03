/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for GL_Category
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:01.0
 */
public class X_GL_Category extends PO {
	/** Standard Constructor */
	public X_GL_Category(Properties ctx, int GL_Category_ID, String trxName) {
		super(ctx, GL_Category_ID, trxName);
		/**
		 * if (GL_Category_ID == 0) { setCategoryType (null); // M
		 * setGL_Category_ID (0); setIsDefault (false); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_GL_Category(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=GL_Category */
	public static final String Table_Name = "GL_Category";

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
		StringBuffer sb = new StringBuffer("X_GL_Category[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** CategoryType AD_Reference_ID=207 */
	public static final int CATEGORYTYPE_AD_Reference_ID = 207;

	/** Document = D */
	public static final String CATEGORYTYPE_Document = "D";

	/** Import = I */
	public static final String CATEGORYTYPE_Import = "I";

	/** Manual = M */
	public static final String CATEGORYTYPE_Manual = "M";

	/** System generated = S */
	public static final String CATEGORYTYPE_SystemGenerated = "S";

	/**
	 * Set Category Type. Source of the Journal with this category
	 */
	public void setCategoryType(String CategoryType) {
		if (CategoryType == null)
			throw new IllegalArgumentException("CategoryType is mandatory");
		if (CategoryType.equals("D") || CategoryType.equals("I")
				|| CategoryType.equals("M") || CategoryType.equals("S"))
			;
		else
			throw new IllegalArgumentException("CategoryType Invalid value - "
					+ CategoryType + " - Reference_ID=207 - D - I - M - S");
		if (CategoryType.length() > 1) {
			log.warning("Length > 1 - truncated");
			CategoryType = CategoryType.substring(0, 0);
		}
		set_Value("CategoryType", CategoryType);
	}

	/**
	 * Get Category Type. Source of the Journal with this category
	 */
	public String getCategoryType() {
		return (String) get_Value("CategoryType");
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
	 * Set GL Category. General Ledger Category
	 */
	public void setGL_Category_ID(int GL_Category_ID) {
		if (GL_Category_ID < 1)
			throw new IllegalArgumentException("GL_Category_ID is mandatory.");
		set_ValueNoCheck("GL_Category_ID", new Integer(GL_Category_ID));
	}

	/**
	 * Get GL Category. General Ledger Category
	 */
	public int getGL_Category_ID() {
		Integer ii = (Integer) get_Value("GL_Category_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Default. Default value
	 */
	public void setIsDefault(boolean IsDefault) {
		set_Value("IsDefault", new Boolean(IsDefault));
	}

	/**
	 * Get Default. Default value
	 */
	public boolean isDefault() {
		Object oo = get_Value("IsDefault");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
