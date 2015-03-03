/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for MPC_Cost_Element_Acct
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:01.781
 */
public class X_MPC_Cost_Element_Acct extends PO {
	/** Standard Constructor */
	public X_MPC_Cost_Element_Acct(Properties ctx,
			int MPC_Cost_Element_Acct_ID, String trxName) {
		super(ctx, MPC_Cost_Element_Acct_ID, trxName);
		/**
		 * if (MPC_Cost_Element_Acct_ID == 0) { setCE_AbsorptionCost_Acct (0);
		 * setCE_Cogs_Acct (0); setCE_VariationMethod_Acct (0);
		 * setCE_VariationRate_Acct (0); setCE_VariationUse_Acct (0);
		 * setC_AcctSchema_ID (0); setMPC_Cost_Element_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_MPC_Cost_Element_Acct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=MPC_Cost_Element_Acct */
	public static final String Table_Name = "MPC_Cost_Element_Acct";

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
		StringBuffer sb = new StringBuffer("X_MPC_Cost_Element_Acct[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/** Set Absorption Cost CMPCS */
	public void setCE_AbsorptionCost_Acct(int CE_AbsorptionCost_Acct) {
		set_Value("CE_AbsorptionCost_Acct", new Integer(CE_AbsorptionCost_Acct));
	}

	/** Get Absorption Cost CMPCS */
	public int getCE_AbsorptionCost_Acct() {
		Integer ii = (Integer) get_Value("CE_AbsorptionCost_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Acctount Good Cost CMPCS */
	public void setCE_Cogs_Acct(int CE_Cogs_Acct) {
		set_Value("CE_Cogs_Acct", new Integer(CE_Cogs_Acct));
	}

	/** Get Acctount Good Cost CMPCS */
	public int getCE_Cogs_Acct() {
		Integer ii = (Integer) get_Value("CE_Cogs_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Variation in Method */
	public void setCE_VariationMethod_Acct(int CE_VariationMethod_Acct) {
		set_Value("CE_VariationMethod_Acct", new Integer(
				CE_VariationMethod_Acct));
	}

	/** Get Variation in Method */
	public int getCE_VariationMethod_Acct() {
		Integer ii = (Integer) get_Value("CE_VariationMethod_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Variation in Rate */
	public void setCE_VariationRate_Acct(int CE_VariationRate_Acct) {
		set_Value("CE_VariationRate_Acct", new Integer(CE_VariationRate_Acct));
	}

	/** Get Variation in Rate */
	public int getCE_VariationRate_Acct() {
		Integer ii = (Integer) get_Value("CE_VariationRate_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Variation Use */
	public void setCE_VariationUse_Acct(int CE_VariationUse_Acct) {
		set_Value("CE_VariationUse_Acct", new Integer(CE_VariationUse_Acct));
	}

	/** Get Variation Use */
	public int getCE_VariationUse_Acct() {
		Integer ii = (Integer) get_Value("CE_VariationUse_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
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

	/**
	 * Set Cost Element CMPCS. ID of the cost element(an element of a cost type)
	 */
	public void setMPC_Cost_Element_ID(int MPC_Cost_Element_ID) {
		if (MPC_Cost_Element_ID < 1)
			throw new IllegalArgumentException(
					"MPC_Cost_Element_ID is mandatory.");
		set_Value("MPC_Cost_Element_ID", new Integer(MPC_Cost_Element_ID));
	}

	/**
	 * Get Cost Element CMPCS. ID of the cost element(an element of a cost type)
	 */
	public int getMPC_Cost_Element_ID() {
		Integer ii = (Integer) get_Value("MPC_Cost_Element_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
