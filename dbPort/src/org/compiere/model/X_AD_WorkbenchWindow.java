/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_WorkbenchWindow
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.984
 */
public class X_AD_WorkbenchWindow extends PO {
	/** Standard Constructor */
	public X_AD_WorkbenchWindow(Properties ctx, int AD_WorkbenchWindow_ID,
			String trxName) {
		super(ctx, AD_WorkbenchWindow_ID, trxName);
		/**
		 * if (AD_WorkbenchWindow_ID == 0) { setAD_WorkbenchWindow_ID (0);
		 * setAD_Workbench_ID (0); setEntityType (null); // U setIsPrimary
		 * (false); setSeqNo (0); }
		 */
	}

	/** Load Constructor */
	public X_AD_WorkbenchWindow(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_WorkbenchWindow */
	public static final String Table_Name = "AD_WorkbenchWindow";

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
		StringBuffer sb = new StringBuffer("X_AD_WorkbenchWindow[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Special Form. Special Form
	 */
	public void setAD_Form_ID(int AD_Form_ID) {
		if (AD_Form_ID <= 0)
			set_Value("AD_Form_ID", null);
		else
			set_Value("AD_Form_ID", new Integer(AD_Form_ID));
	}

	/**
	 * Get Special Form. Special Form
	 */
	public int getAD_Form_ID() {
		Integer ii = (Integer) get_Value("AD_Form_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Process. Process or Report
	 */
	public void setAD_Process_ID(int AD_Process_ID) {
		if (AD_Process_ID <= 0)
			set_Value("AD_Process_ID", null);
		else
			set_Value("AD_Process_ID", new Integer(AD_Process_ID));
	}

	/**
	 * Get Process. Process or Report
	 */
	public int getAD_Process_ID() {
		Integer ii = (Integer) get_Value("AD_Process_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set OS Task. Operation System Task
	 */
	public void setAD_Task_ID(int AD_Task_ID) {
		if (AD_Task_ID <= 0)
			set_Value("AD_Task_ID", null);
		else
			set_Value("AD_Task_ID", new Integer(AD_Task_ID));
	}

	/**
	 * Get OS Task. Operation System Task
	 */
	public int getAD_Task_ID() {
		Integer ii = (Integer) get_Value("AD_Task_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Window. Data entry or display window
	 */
	public void setAD_Window_ID(int AD_Window_ID) {
		if (AD_Window_ID <= 0)
			set_Value("AD_Window_ID", null);
		else
			set_Value("AD_Window_ID", new Integer(AD_Window_ID));
	}

	/**
	 * Get Window. Data entry or display window
	 */
	public int getAD_Window_ID() {
		Integer ii = (Integer) get_Value("AD_Window_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Workbench Window */
	public void setAD_WorkbenchWindow_ID(int AD_WorkbenchWindow_ID) {
		if (AD_WorkbenchWindow_ID < 1)
			throw new IllegalArgumentException(
					"AD_WorkbenchWindow_ID is mandatory.");
		set_ValueNoCheck("AD_WorkbenchWindow_ID", new Integer(
				AD_WorkbenchWindow_ID));
	}

	/** Get Workbench Window */
	public int getAD_WorkbenchWindow_ID() {
		Integer ii = (Integer) get_Value("AD_WorkbenchWindow_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getAD_WorkbenchWindow_ID()));
	}

	/**
	 * Set Workbench. Collection of windows, reports
	 */
	public void setAD_Workbench_ID(int AD_Workbench_ID) {
		if (AD_Workbench_ID < 1)
			throw new IllegalArgumentException("AD_Workbench_ID is mandatory.");
		set_ValueNoCheck("AD_Workbench_ID", new Integer(AD_Workbench_ID));
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

	/** EntityType AD_Reference_ID=245 */
	public static final int ENTITYTYPE_AD_Reference_ID = 245;

	/** Applications = A */
	public static final String ENTITYTYPE_Applications = "A";

	/** Compiere = C */
	public static final String ENTITYTYPE_Compiere = "C";

	/** Customization = CUST */
	public static final String ENTITYTYPE_Customization = "CUST";

	/** Dictionary = D */
	public static final String ENTITYTYPE_Dictionary = "D";

	/** User maintained = U */
	public static final String ENTITYTYPE_UserMaintained = "U";

	/**
	 * Set Entity Type. Dictionary Entity Type; Determines ownership and
	 * synchronization
	 */
	public void setEntityType(String EntityType) {
		if (EntityType == null)
			throw new IllegalArgumentException("EntityType is mandatory");
		if (EntityType.length() > 4) {
			log.warning("Length > 4 - truncated");
			EntityType = EntityType.substring(0, 3);
		}
		set_Value("EntityType", EntityType);
	}

	/**
	 * Get Entity Type. Dictionary Entity Type; Determines ownership and
	 * synchronization
	 */
	public String getEntityType() {
		return (String) get_Value("EntityType");
	}

	/**
	 * Set Primary. Indicates if this is the primary budget
	 */
	public void setIsPrimary(boolean IsPrimary) {
		set_Value("IsPrimary", new Boolean(IsPrimary));
	}

	/**
	 * Get Primary. Indicates if this is the primary budget
	 */
	public boolean isPrimary() {
		Object oo = get_Value("IsPrimary");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
