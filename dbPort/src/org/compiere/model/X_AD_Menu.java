/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Menu
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.593
 */
public class X_AD_Menu extends PO {
	/** Standard Constructor */
	public X_AD_Menu(Properties ctx, int AD_Menu_ID, String trxName) {
		super(ctx, AD_Menu_ID, trxName);
		/**
		 * if (AD_Menu_ID == 0) { setAD_Menu_ID (0); setEntityType (null); // U
		 * setIsReadOnly (false); // N setIsSOTrx (false); setIsSummary (false);
		 * setName (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_Menu(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Menu */
	public static final String Table_Name = "AD_Menu";

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
		StringBuffer sb = new StringBuffer("X_AD_Menu[").append(get_ID())
				.append("]");
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
	 * Set Menu. Identifies a Menu
	 */
	public void setAD_Menu_ID(int AD_Menu_ID) {
		if (AD_Menu_ID < 1)
			throw new IllegalArgumentException("AD_Menu_ID is mandatory.");
		set_ValueNoCheck("AD_Menu_ID", new Integer(AD_Menu_ID));
	}

	/**
	 * Get Menu. Identifies a Menu
	 */
	public int getAD_Menu_ID() {
		Integer ii = (Integer) get_Value("AD_Menu_ID");
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

	/**
	 * Set Workbench. Collection of windows, reports
	 */
	public void setAD_Workbench_ID(int AD_Workbench_ID) {
		if (AD_Workbench_ID <= 0)
			set_Value("AD_Workbench_ID", null);
		else
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

	/**
	 * Set Workflow. Workflow or combination of tasks
	 */
	public void setAD_Workflow_ID(int AD_Workflow_ID) {
		if (AD_Workflow_ID <= 0)
			set_Value("AD_Workflow_ID", null);
		else
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

	/** Action AD_Reference_ID=104 */
	public static final int ACTION_AD_Reference_ID = 104;

	/** Workbench = B */
	public static final String ACTION_Workbench = "B";

	/** WorkFlow = F */
	public static final String ACTION_WorkFlow = "F";

	/** Process = P */
	public static final String ACTION_Process = "P";

	/** Report = R */
	public static final String ACTION_Report = "R";

	/** Task = T */
	public static final String ACTION_Task = "T";

	/** Window = W */
	public static final String ACTION_Window = "W";

	/** Form = X */
	public static final String ACTION_Form = "X";

	/**
	 * Set Action. Indicates the Action to be performed
	 */
	public void setAction(String Action) {
		if (Action != null && Action.length() > 1) {
			log.warning("Length > 1 - truncated");
			Action = Action.substring(0, 0);
		}
		set_Value("Action", Action);
	}

	/**
	 * Get Action. Indicates the Action to be performed
	 */
	public String getAction() {
		return (String) get_Value("Action");
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
	 * Set Read Only. Field is read only
	 */
	public void setIsReadOnly(boolean IsReadOnly) {
		set_Value("IsReadOnly", new Boolean(IsReadOnly));
	}

	/**
	 * Get Read Only. Field is read only
	 */
	public boolean isReadOnly() {
		Object oo = get_Value("IsReadOnly");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Sales Transaction. This is a Sales Transaction
	 */
	public void setIsSOTrx(boolean IsSOTrx) {
		set_Value("IsSOTrx", new Boolean(IsSOTrx));
	}

	/**
	 * Get Sales Transaction. This is a Sales Transaction
	 */
	public boolean isSOTrx() {
		Object oo = get_Value("IsSOTrx");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Summary Level. This is a summary entity
	 */
	public void setIsSummary(boolean IsSummary) {
		set_Value("IsSummary", new Boolean(IsSummary));
	}

	/**
	 * Get Summary Level. This is a summary entity
	 */
	public boolean isSummary() {
		Object oo = get_Value("IsSummary");
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
