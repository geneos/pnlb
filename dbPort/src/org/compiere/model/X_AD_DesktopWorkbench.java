/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_DesktopWorkbench
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.359
 */
public class X_AD_DesktopWorkbench extends PO {
	/** Standard Constructor */
	public X_AD_DesktopWorkbench(Properties ctx, int AD_DesktopWorkbench_ID,
			String trxName) {
		super(ctx, AD_DesktopWorkbench_ID, trxName);
		/**
		 * if (AD_DesktopWorkbench_ID == 0) { setAD_DesktopWorkbench_ID (0);
		 * setAD_Desktop_ID (0); setAD_Workbench_ID (0); setSeqNo (0); }
		 */
	}

	/** Load Constructor */
	public X_AD_DesktopWorkbench(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_DesktopWorkbench */
	public static final String Table_Name = "AD_DesktopWorkbench";

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
	
	protected BigDecimal accessLevel = new BigDecimal(4);

	/** AccessLevel 4 - System */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_AD_DesktopWorkbench[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/** Set Desktop Workbench */
	public void setAD_DesktopWorkbench_ID(int AD_DesktopWorkbench_ID) {
		if (AD_DesktopWorkbench_ID < 1)
			throw new IllegalArgumentException(
					"AD_DesktopWorkbench_ID is mandatory.");
		set_ValueNoCheck("AD_DesktopWorkbench_ID", new Integer(
				AD_DesktopWorkbench_ID));
	}

	/** Get Desktop Workbench */
	public int getAD_DesktopWorkbench_ID() {
		Integer ii = (Integer) get_Value("AD_DesktopWorkbench_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Desktop. Collection of Workbenches
	 */
	public void setAD_Desktop_ID(int AD_Desktop_ID) {
		if (AD_Desktop_ID < 1)
			throw new IllegalArgumentException("AD_Desktop_ID is mandatory.");
		set_ValueNoCheck("AD_Desktop_ID", new Integer(AD_Desktop_ID));
	}

	/**
	 * Get Desktop. Collection of Workbenches
	 */
	public int getAD_Desktop_ID() {
		Integer ii = (Integer) get_Value("AD_Desktop_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Workbench. Collection of windows, reports
	 */
	public void setAD_Workbench_ID(int AD_Workbench_ID) {
		if (AD_Workbench_ID < 1)
			throw new IllegalArgumentException("AD_Workbench_ID is mandatory.");
		set_Value("AD_Workbench_ID", new Integer(AD_Workbench_ID));
	}

	/**
	 * Get Workbench. Collection of windows, reports
	 */
	public int getAD_Workbench_ID() {
		Integer ii = (Integer) get_Value("AD_Workbench_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getAD_Workbench_ID()));
	}

	/**
	 * Set Sequence. Method of ordering records; lowest number comes first
	 */
	public void setSeqNo(int SeqNo) {
		set_Value("SeqNo", new Integer(SeqNo));
	}

	/**
	 * Get Sequence. Method of ordering records; lowest number comes first
	 */
	public int getSeqNo() {
		Integer ii = (Integer) get_Value("SeqNo");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
