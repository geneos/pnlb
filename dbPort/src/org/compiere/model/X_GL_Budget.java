/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for GL_Budget
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.968
 */
public class X_GL_Budget extends PO {
	/** Standard Constructor */
	public X_GL_Budget(Properties ctx, int GL_Budget_ID, String trxName) {
		super(ctx, GL_Budget_ID, trxName);
		/**
		 * if (GL_Budget_ID == 0) { setGL_Budget_ID (0); setIsPrimary (false);
		 * setName (null); }
		 */
	}

	/** Load Constructor */
	public X_GL_Budget(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=GL_Budget */
	public static final String Table_Name = "GL_Budget";

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
		StringBuffer sb = new StringBuffer("X_GL_Budget[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** BudgetStatus AD_Reference_ID=178 */
	public static final int BUDGETSTATUS_AD_Reference_ID = 178;

	/** Approved = A */
	public static final String BUDGETSTATUS_Approved = "A";

	/** Draft = D */
	public static final String BUDGETSTATUS_Draft = "D";

	/**
	 * Set Budget Status. Indicates the current status of this budget
	 */
	public void setBudgetStatus(String BudgetStatus) {
		if (BudgetStatus != null && BudgetStatus.length() > 1) {
			log.warning("Length > 1 - truncated");
			BudgetStatus = BudgetStatus.substring(0, 0);
		}
		set_Value("BudgetStatus", BudgetStatus);
	}

	/**
	 * Get Budget Status. Indicates the current status of this budget
	 */
	public String getBudgetStatus() {
		return (String) get_Value("BudgetStatus");
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
	 * Set Budget. General Ledger Budget
	 */
	public void setGL_Budget_ID(int GL_Budget_ID) {
		if (GL_Budget_ID < 1)
			throw new IllegalArgumentException("GL_Budget_ID is mandatory.");
		set_ValueNoCheck("GL_Budget_ID", new Integer(GL_Budget_ID));
	}

	/**
	 * Get Budget. General Ledger Budget
	 */
	public int getGL_Budget_ID() {
		Integer ii = (Integer) get_Value("GL_Budget_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
