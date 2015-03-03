/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for PA_ColorSchema
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.843
 */
public class X_PA_ColorSchema extends PO {
	/** Standard Constructor */
	public X_PA_ColorSchema(Properties ctx, int PA_ColorSchema_ID,
			String trxName) {
		super(ctx, PA_ColorSchema_ID, trxName);
		/**
		 * if (PA_ColorSchema_ID == 0) { setAD_PrintColor1_ID (0);
		 * setAD_PrintColor2_ID (0); setMark1Percent (0); setMark2Percent (0);
		 * setName (null); setPA_ColorSchema_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_PA_ColorSchema(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=PA_ColorSchema */
	public static final String Table_Name = "PA_ColorSchema";

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

	protected BigDecimal accessLevel = new BigDecimal(6);

	/** AccessLevel 6 - System - Client */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_PA_ColorSchema[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/** AD_PrintColor1_ID AD_Reference_ID=266 */
	public static final int AD_PRINTCOLOR1_ID_AD_Reference_ID = 266;

	/**
	 * Set Color 1. First color used
	 */
	public void setAD_PrintColor1_ID(int AD_PrintColor1_ID) {
		if (AD_PrintColor1_ID < 1)
			throw new IllegalArgumentException(
					"AD_PrintColor1_ID is mandatory.");
		set_Value("AD_PrintColor1_ID", new Integer(AD_PrintColor1_ID));
	}

	/**
	 * Get Color 1. First color used
	 */
	public int getAD_PrintColor1_ID() {
		Integer ii = (Integer) get_Value("AD_PrintColor1_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_PrintColor2_ID AD_Reference_ID=266 */
	public static final int AD_PRINTCOLOR2_ID_AD_Reference_ID = 266;

	/**
	 * Set Color 2. Second color used
	 */
	public void setAD_PrintColor2_ID(int AD_PrintColor2_ID) {
		if (AD_PrintColor2_ID < 1)
			throw new IllegalArgumentException(
					"AD_PrintColor2_ID is mandatory.");
		set_Value("AD_PrintColor2_ID", new Integer(AD_PrintColor2_ID));
	}

	/**
	 * Get Color 2. Second color used
	 */
	public int getAD_PrintColor2_ID() {
		Integer ii = (Integer) get_Value("AD_PrintColor2_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_PrintColor3_ID AD_Reference_ID=266 */
	public static final int AD_PRINTCOLOR3_ID_AD_Reference_ID = 266;

	/**
	 * Set Color 3. Third color used
	 */
	public void setAD_PrintColor3_ID(int AD_PrintColor3_ID) {
		if (AD_PrintColor3_ID <= 0)
			set_Value("AD_PrintColor3_ID", null);
		else
			set_Value("AD_PrintColor3_ID", new Integer(AD_PrintColor3_ID));
	}

	/**
	 * Get Color 3. Third color used
	 */
	public int getAD_PrintColor3_ID() {
		Integer ii = (Integer) get_Value("AD_PrintColor3_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_PrintColor4_ID AD_Reference_ID=266 */
	public static final int AD_PRINTCOLOR4_ID_AD_Reference_ID = 266;

	/**
	 * Set Color 4. Forth color used
	 */
	public void setAD_PrintColor4_ID(int AD_PrintColor4_ID) {
		if (AD_PrintColor4_ID <= 0)
			set_Value("AD_PrintColor4_ID", null);
		else
			set_Value("AD_PrintColor4_ID", new Integer(AD_PrintColor4_ID));
	}

	/**
	 * Get Color 4. Forth color used
	 */
	public int getAD_PrintColor4_ID() {
		Integer ii = (Integer) get_Value("AD_PrintColor4_ID");
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
	 * Set Mark 1 Percent. Percentage up to this color is used
	 */
	public void setMark1Percent(int Mark1Percent) {
		set_Value("Mark1Percent", new Integer(Mark1Percent));
	}

	/**
	 * Get Mark 1 Percent. Percentage up to this color is used
	 */
	public int getMark1Percent() {
		Integer ii = (Integer) get_Value("Mark1Percent");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Mark 2 Percent. Percentage up to this color is used
	 */
	public void setMark2Percent(int Mark2Percent) {
		set_Value("Mark2Percent", new Integer(Mark2Percent));
	}

	/**
	 * Get Mark 2 Percent. Percentage up to this color is used
	 */
	public int getMark2Percent() {
		Integer ii = (Integer) get_Value("Mark2Percent");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Mark 3 Percent. Percentage up to this color is used
	 */
	public void setMark3Percent(int Mark3Percent) {
		set_Value("Mark3Percent", new Integer(Mark3Percent));
	}

	/**
	 * Get Mark 3 Percent. Percentage up to this color is used
	 */
	public int getMark3Percent() {
		Integer ii = (Integer) get_Value("Mark3Percent");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Mark 4 Percent. Percentage up to this color is used
	 */
	public void setMark4Percent(int Mark4Percent) {
		set_Value("Mark4Percent", new Integer(Mark4Percent));
	}

	/**
	 * Get Mark 4 Percent. Percentage up to this color is used
	 */
	public int getMark4Percent() {
		Integer ii = (Integer) get_Value("Mark4Percent");
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
		if (Name.length() > 120) {
			log.warning("Length > 120 - truncated");
			Name = Name.substring(0, 119);
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
	 * Set Color Schema. Performance Color Schema
	 */
	public void setPA_ColorSchema_ID(int PA_ColorSchema_ID) {
		if (PA_ColorSchema_ID < 1)
			throw new IllegalArgumentException(
					"PA_ColorSchema_ID is mandatory.");
		set_ValueNoCheck("PA_ColorSchema_ID", new Integer(PA_ColorSchema_ID));
	}

	/**
	 * Get Color Schema. Performance Color Schema
	 */
	public int getPA_ColorSchema_ID() {
		Integer ii = (Integer) get_Value("PA_ColorSchema_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
