/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_PrintGraph
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.843
 */
public class X_AD_PrintGraph extends PO {
	/** Standard Constructor */
	public X_AD_PrintGraph(Properties ctx, int AD_PrintGraph_ID, String trxName) {
		super(ctx, AD_PrintGraph_ID, trxName);
		/**
		 * if (AD_PrintGraph_ID == 0) { setAD_PrintFormat_ID (0); // 0
		 * setAD_PrintGraph_ID (0); setData_PrintFormatItem_ID (0);
		 * setDescription_PrintFormatItem_ID (0); setGraphType (null); setName
		 * (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_PrintGraph(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_PrintGraph */
	public static final String Table_Name = "AD_PrintGraph";

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
		StringBuffer sb = new StringBuffer("X_AD_PrintGraph[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Print Format. Data Print Format
	 */
	public void setAD_PrintFormat_ID(int AD_PrintFormat_ID) {
		if (AD_PrintFormat_ID < 1)
			throw new IllegalArgumentException(
					"AD_PrintFormat_ID is mandatory.");
		set_Value("AD_PrintFormat_ID", new Integer(AD_PrintFormat_ID));
	}

	/**
	 * Get Print Format. Data Print Format
	 */
	public int getAD_PrintFormat_ID() {
		Integer ii = (Integer) get_Value("AD_PrintFormat_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Graph. Graph included in Reports
	 */
	public void setAD_PrintGraph_ID(int AD_PrintGraph_ID) {
		if (AD_PrintGraph_ID < 1)
			throw new IllegalArgumentException("AD_PrintGraph_ID is mandatory.");
		set_ValueNoCheck("AD_PrintGraph_ID", new Integer(AD_PrintGraph_ID));
	}

	/**
	 * Get Graph. Graph included in Reports
	 */
	public int getAD_PrintGraph_ID() {
		Integer ii = (Integer) get_Value("AD_PrintGraph_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Data1_PrintFormatItem_ID AD_Reference_ID=264 */
	public static final int DATA1_PRINTFORMATITEM_ID_AD_Reference_ID = 264;

	/**
	 * Set Data Column 2. Data Column for Line Charts
	 */
	public void setData1_PrintFormatItem_ID(int Data1_PrintFormatItem_ID) {
		if (Data1_PrintFormatItem_ID <= 0)
			set_Value("Data1_PrintFormatItem_ID", null);
		else
			set_Value("Data1_PrintFormatItem_ID", new Integer(
					Data1_PrintFormatItem_ID));
	}

	/**
	 * Get Data Column 2. Data Column for Line Charts
	 */
	public int getData1_PrintFormatItem_ID() {
		Integer ii = (Integer) get_Value("Data1_PrintFormatItem_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Data2_PrintFormatItem_ID AD_Reference_ID=264 */
	public static final int DATA2_PRINTFORMATITEM_ID_AD_Reference_ID = 264;

	/**
	 * Set Data Column 3. Data Column for Line Charts
	 */
	public void setData2_PrintFormatItem_ID(int Data2_PrintFormatItem_ID) {
		if (Data2_PrintFormatItem_ID <= 0)
			set_Value("Data2_PrintFormatItem_ID", null);
		else
			set_Value("Data2_PrintFormatItem_ID", new Integer(
					Data2_PrintFormatItem_ID));
	}

	/**
	 * Get Data Column 3. Data Column for Line Charts
	 */
	public int getData2_PrintFormatItem_ID() {
		Integer ii = (Integer) get_Value("Data2_PrintFormatItem_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Data3_PrintFormatItem_ID AD_Reference_ID=264 */
	public static final int DATA3_PRINTFORMATITEM_ID_AD_Reference_ID = 264;

	/**
	 * Set Data Column 4. Data Column for Line Charts
	 */
	public void setData3_PrintFormatItem_ID(int Data3_PrintFormatItem_ID) {
		if (Data3_PrintFormatItem_ID <= 0)
			set_Value("Data3_PrintFormatItem_ID", null);
		else
			set_Value("Data3_PrintFormatItem_ID", new Integer(
					Data3_PrintFormatItem_ID));
	}

	/**
	 * Get Data Column 4. Data Column for Line Charts
	 */
	public int getData3_PrintFormatItem_ID() {
		Integer ii = (Integer) get_Value("Data3_PrintFormatItem_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Data4_PrintFormatItem_ID AD_Reference_ID=264 */
	public static final int DATA4_PRINTFORMATITEM_ID_AD_Reference_ID = 264;

	/**
	 * Set Data Column 5. Data Column for Line Charts
	 */
	public void setData4_PrintFormatItem_ID(int Data4_PrintFormatItem_ID) {
		if (Data4_PrintFormatItem_ID <= 0)
			set_Value("Data4_PrintFormatItem_ID", null);
		else
			set_Value("Data4_PrintFormatItem_ID", new Integer(
					Data4_PrintFormatItem_ID));
	}

	/**
	 * Get Data Column 5. Data Column for Line Charts
	 */
	public int getData4_PrintFormatItem_ID() {
		Integer ii = (Integer) get_Value("Data4_PrintFormatItem_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Data_PrintFormatItem_ID AD_Reference_ID=264 */
	public static final int DATA_PRINTFORMATITEM_ID_AD_Reference_ID = 264;

	/**
	 * Set Data Column. Data Column for Pie and Line Charts
	 */
	public void setData_PrintFormatItem_ID(int Data_PrintFormatItem_ID) {
		if (Data_PrintFormatItem_ID < 1)
			throw new IllegalArgumentException(
					"Data_PrintFormatItem_ID is mandatory.");
		set_Value("Data_PrintFormatItem_ID", new Integer(
				Data_PrintFormatItem_ID));
	}

	/**
	 * Get Data Column. Data Column for Pie and Line Charts
	 */
	public int getData_PrintFormatItem_ID() {
		Integer ii = (Integer) get_Value("Data_PrintFormatItem_ID");
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

	/** Description_PrintFormatItem_ID AD_Reference_ID=264 */
	public static final int DESCRIPTION_PRINTFORMATITEM_ID_AD_Reference_ID = 264;

	/**
	 * Set Description Column. Description Column for Pie/Line/Bar Charts
	 */
	public void setDescription_PrintFormatItem_ID(
			int Description_PrintFormatItem_ID) {
		if (Description_PrintFormatItem_ID < 1)
			throw new IllegalArgumentException(
					"Description_PrintFormatItem_ID is mandatory.");
		set_Value("Description_PrintFormatItem_ID", new Integer(
				Description_PrintFormatItem_ID));
	}

	/**
	 * Get Description Column. Description Column for Pie/Line/Bar Charts
	 */
	public int getDescription_PrintFormatItem_ID() {
		Integer ii = (Integer) get_Value("Description_PrintFormatItem_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** GraphType AD_Reference_ID=265 */
	public static final int GRAPHTYPE_AD_Reference_ID = 265;

	/** Bar Chart = B */
	public static final String GRAPHTYPE_BarChart = "B";

	/** Line Chart = L */
	public static final String GRAPHTYPE_LineChart = "L";

	/** Pie Chart = P */
	public static final String GRAPHTYPE_PieChart = "P";

	/**
	 * Set Graph Type. Type of graph to be painted
	 */
	public void setGraphType(String GraphType) {
		if (GraphType == null)
			throw new IllegalArgumentException("GraphType is mandatory");
		if (GraphType.equals("B") || GraphType.equals("L")
				|| GraphType.equals("P"))
			;
		else
			throw new IllegalArgumentException("GraphType Invalid value - "
					+ GraphType + " - Reference_ID=265 - B - L - P");
		if (GraphType.length() > 1) {
			log.warning("Length > 1 - truncated");
			GraphType = GraphType.substring(0, 0);
		}
		set_Value("GraphType", GraphType);
	}

	/**
	 * Get Graph Type. Type of graph to be painted
	 */
	public String getGraphType() {
		return (String) get_Value("GraphType");
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
