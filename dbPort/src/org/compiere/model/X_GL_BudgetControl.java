/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for GL_BudgetControl
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.984
 */
public class X_GL_BudgetControl extends PO {
	/** Standard Constructor */
	public X_GL_BudgetControl(Properties ctx, int GL_BudgetControl_ID,
			String trxName) {
		super(ctx, GL_BudgetControl_ID, trxName);
		/**
		 * if (GL_BudgetControl_ID == 0) { setBudgetControlScope (null);
		 * setC_AcctSchema_ID (0); setCommitmentType (null); // C
		 * setGL_BudgetControl_ID (0); setGL_Budget_ID (0); setIsBeforeApproval
		 * (false); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_GL_BudgetControl(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=GL_BudgetControl */
	public static final String Table_Name = "GL_BudgetControl";

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
		StringBuffer sb = new StringBuffer("X_GL_BudgetControl[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/** BudgetControlScope AD_Reference_ID=361 */
	public static final int BUDGETCONTROLSCOPE_AD_Reference_ID = 361;

	/** Period only = P */
	public static final String BUDGETCONTROLSCOPE_PeriodOnly = "P";

	/** Total = T */
	public static final String BUDGETCONTROLSCOPE_Total = "T";

	/** Year To Date = Y */
	public static final String BUDGETCONTROLSCOPE_YearToDate = "Y";

	/**
	 * Set Control Scope. Scope of the Budget Control
	 */
	public void setBudgetControlScope(String BudgetControlScope) {
		if (BudgetControlScope == null)
			throw new IllegalArgumentException(
					"BudgetControlScope is mandatory");
		if (BudgetControlScope.equals("P") || BudgetControlScope.equals("T")
				|| BudgetControlScope.equals("Y"))
			;
		else
			throw new IllegalArgumentException(
					"BudgetControlScope Invalid value - " + BudgetControlScope
							+ " - Reference_ID=361 - P - T - Y");
		if (BudgetControlScope.length() > 1) {
			log.warning("Length > 1 - truncated");
			BudgetControlScope = BudgetControlScope.substring(0, 0);
		}
		set_Value("BudgetControlScope", BudgetControlScope);
	}

	/**
	 * Get Control Scope. Scope of the Budget Control
	 */
	public String getBudgetControlScope() {
		return (String) get_Value("BudgetControlScope");
	}

	/**
	 * Set Accounting Schema. Rules for accounting
	 */
	public void setC_AcctSchema_ID(int C_AcctSchema_ID) {
		if (C_AcctSchema_ID < 1)
			throw new IllegalArgumentException("C_AcctSchema_ID is mandatory.");
		set_Value("C_AcctSchema_ID", new Integer(C_AcctSchema_ID));
	}

	/**
	 * Get Accounting Schema. Rules for accounting
	 */
	public int getC_AcctSchema_ID() {
		Integer ii = (Integer) get_Value("C_AcctSchema_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** CommitmentType AD_Reference_ID=359 */
	public static final int COMMITMENTTYPE_AD_Reference_ID = 359;

	/** Commitment & Reservation = B */
	public static final String COMMITMENTTYPE_CommitmentReservation = "B";

	/** Commitment only = C */
	public static final String COMMITMENTTYPE_CommitmentOnly = "C";

	/** None = N */
	public static final String COMMITMENTTYPE_None = "N";

	/**
	 * Set Commitment Type. Create Commitment and/or Reservations for Budget
	 * Control
	 */
	public void setCommitmentType(String CommitmentType) {
		if (CommitmentType == null)
			throw new IllegalArgumentException("CommitmentType is mandatory");
		if (CommitmentType.equals("B") || CommitmentType.equals("C")
				|| CommitmentType.equals("N"))
			;
		else
			throw new IllegalArgumentException(
					"CommitmentType Invalid value - " + CommitmentType
							+ " - Reference_ID=359 - B - C - N");
		if (CommitmentType.length() > 1) {
			log.warning("Length > 1 - truncated");
			CommitmentType = CommitmentType.substring(0, 0);
		}
		set_Value("CommitmentType", CommitmentType);
	}

	/**
	 * Get Commitment Type. Create Commitment and/or Reservations for Budget
	 * Control
	 */
	public String getCommitmentType() {
		return (String) get_Value("CommitmentType");
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
	 * Set Budget Control. Budget Control
	 */
	public void setGL_BudgetControl_ID(int GL_BudgetControl_ID) {
		if (GL_BudgetControl_ID < 1)
			throw new IllegalArgumentException(
					"GL_BudgetControl_ID is mandatory.");
		set_ValueNoCheck("GL_BudgetControl_ID",
				new Integer(GL_BudgetControl_ID));
	}

	/**
	 * Get Budget Control. Budget Control
	 */
	public int getGL_BudgetControl_ID() {
		Integer ii = (Integer) get_Value("GL_BudgetControl_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Budget. General Ledger Budget
	 */
	public void setGL_Budget_ID(int GL_Budget_ID) {
		if (GL_Budget_ID < 1)
			throw new IllegalArgumentException("GL_Budget_ID is mandatory.");
		set_Value("GL_Budget_ID", new Integer(GL_Budget_ID));
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
	 * Set Comment/Help. Comment or Hint
	 */
	public void setHelp(String Help) {
		if (Help != null && Help.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Help = Help.substring(0, 1999);
		}
		set_Value("Help", Help);
	}

	/**
	 * Get Comment/Help. Comment or Hint
	 */
	public String getHelp() {
		return (String) get_Value("Help");
	}

	/**
	 * Set Before Approval. The Check is before the (manual) approval
	 */
	public void setIsBeforeApproval(boolean IsBeforeApproval) {
		set_Value("IsBeforeApproval", new Boolean(IsBeforeApproval));
	}

	/**
	 * Get Before Approval. The Check is before the (manual) approval
	 */
	public boolean isBeforeApproval() {
		Object oo = get_Value("IsBeforeApproval");
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
}
