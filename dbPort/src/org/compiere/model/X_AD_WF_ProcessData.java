/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_WF_ProcessData
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.906
 */
public class X_AD_WF_ProcessData extends PO {
	/** Standard Constructor */
	public X_AD_WF_ProcessData(Properties ctx, int AD_WF_ProcessData_ID,
			String trxName) {
		super(ctx, AD_WF_ProcessData_ID, trxName);
		/**
		 * if (AD_WF_ProcessData_ID == 0) { setAD_WF_ProcessData_ID (0);
		 * setAD_WF_Process_ID (0); setAttributeName (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_WF_ProcessData(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_WF_ProcessData */
	public static final String Table_Name = "AD_WF_ProcessData";

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

	protected BigDecimal accessLevel = new BigDecimal(7);

	/** AccessLevel 7 - System - Client - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_AD_WF_ProcessData[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Workflow Process Data. Workflow Process Context
	 */
	public void setAD_WF_ProcessData_ID(int AD_WF_ProcessData_ID) {
		if (AD_WF_ProcessData_ID < 1)
			throw new IllegalArgumentException(
					"AD_WF_ProcessData_ID is mandatory.");
		set_ValueNoCheck("AD_WF_ProcessData_ID", new Integer(
				AD_WF_ProcessData_ID));
	}

	/**
	 * Get Workflow Process Data. Workflow Process Context
	 */
	public int getAD_WF_ProcessData_ID() {
		Integer ii = (Integer) get_Value("AD_WF_ProcessData_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Workflow Process. Actual Workflow Process Instance
	 */
	public void setAD_WF_Process_ID(int AD_WF_Process_ID) {
		if (AD_WF_Process_ID < 1)
			throw new IllegalArgumentException("AD_WF_Process_ID is mandatory.");
		set_ValueNoCheck("AD_WF_Process_ID", new Integer(AD_WF_Process_ID));
	}

	/**
	 * Get Workflow Process. Actual Workflow Process Instance
	 */
	public int getAD_WF_Process_ID() {
		Integer ii = (Integer) get_Value("AD_WF_Process_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getAD_WF_Process_ID()));
	}

	/**
	 * Set Attribute Name. Name of the Attribute
	 */
	public void setAttributeName(String AttributeName) {
		if (AttributeName == null)
			throw new IllegalArgumentException("AttributeName is mandatory.");
		if (AttributeName.length() > 60) {
			log.warning("Length > 60 - truncated");
			AttributeName = AttributeName.substring(0, 59);
		}
		set_Value("AttributeName", AttributeName);
	}

	/**
	 * Get Attribute Name. Name of the Attribute
	 */
	public String getAttributeName() {
		return (String) get_Value("AttributeName");
	}

	/**
	 * Set Attribute Value. Value of the Attribute
	 */
	public void setAttributeValue(String AttributeValue) {
		if (AttributeValue != null && AttributeValue.length() > 60) {
			log.warning("Length > 60 - truncated");
			AttributeValue = AttributeValue.substring(0, 59);
		}
		set_Value("AttributeValue", AttributeValue);
	}

	/**
	 * Get Attribute Value. Value of the Attribute
	 */
	public String getAttributeValue() {
		return (String) get_Value("AttributeValue");
	}
}
