/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_OrgInfo
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.64
 */
public class X_AD_OrgInfo extends PO {
	/** Standard Constructor */
	public X_AD_OrgInfo(Properties ctx, int AD_OrgInfo_ID, String trxName) {
		super(ctx, AD_OrgInfo_ID, trxName);
		/**
		 * if (AD_OrgInfo_ID == 0) { setDUNS (null); setTaxID (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_OrgInfo(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_OrgInfo */
	public static final String Table_Name = "AD_OrgInfo";

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
		StringBuffer sb = new StringBuffer("X_AD_OrgInfo[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Organization Type. Organization Type allows you to categorize your
	 * organizations
	 */
	public void setAD_OrgType_ID(int AD_OrgType_ID) {
		if (AD_OrgType_ID <= 0)
			set_Value("AD_OrgType_ID", null);
		else
			set_Value("AD_OrgType_ID", new Integer(AD_OrgType_ID));
	}

	/**
	 * Get Organization Type. Organization Type allows you to categorize your
	 * organizations
	 */
	public int getAD_OrgType_ID() {
		Integer ii = (Integer) get_Value("AD_OrgType_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set AGENTE */
	public void setAGENTE(boolean AGENTE) {
		set_Value("AGENTE", new Boolean(AGENTE));
	}

	/** Get AGENTE */
	public boolean isAGENTE() {
		Object oo = get_Value("AGENTE");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set AGENTEIVAA */
	public void setAGENTEIVAA(boolean AGENTEIVAA) {
		set_Value("AGENTEIVAA", new Boolean(AGENTEIVAA));
	}

	/** Get AGENTEIVAA */
	public boolean isAGENTEIVAA() {
		Object oo = get_Value("AGENTEIVAA");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set AGENTEPER */
	public void setAGENTEPER(boolean AGENTEPER) {
		set_Value("AGENTEPER", new Boolean(AGENTEPER));
	}

	/** Get AGENTEPER */
	public boolean isAGENTEPER() {
		Object oo = get_Value("AGENTEPER");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Address. Location or Address
	 */
	public void setC_Location_ID(int C_Location_ID) {
		if (C_Location_ID <= 0)
			set_Value("C_Location_ID", null);
		else
			set_Value("C_Location_ID", new Integer(C_Location_ID));
	}

	/**
	 * Get Address. Location or Address
	 */
	public int getC_Location_ID() {
		Integer ii = (Integer) get_Value("C_Location_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set DEBUG */
	public void setDEBUG(boolean DEBUG) {
		set_Value("DEBUG", new Boolean(DEBUG));
	}

	/** Get DEBUG */
	public boolean isDEBUG() {
		Object oo = get_Value("DEBUG");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set D-U-N-S. Dun & Bradstreet Number
	 */
	public void setDUNS(String DUNS) {
		if (DUNS == null)
			throw new IllegalArgumentException("DUNS is mandatory.");
		if (DUNS.length() > 11) {
			log.warning("Length > 11 - truncated");
			DUNS = DUNS.substring(0, 10);
		}
		set_Value("DUNS", DUNS);
	}

	/**
	 * Get D-U-N-S. Dun & Bradstreet Number
	 */
	public String getDUNS() {
		return (String) get_Value("DUNS");
	}

	/**
	 * Set Warehouse. Storage Warehouse and Service Point
	 */
	public void setM_Warehouse_ID(int M_Warehouse_ID) {
		if (M_Warehouse_ID <= 0)
			set_Value("M_Warehouse_ID", null);
		else
			set_Value("M_Warehouse_ID", new Integer(M_Warehouse_ID));
	}

	/**
	 * Get Warehouse. Storage Warehouse and Service Point
	 */
	public int getM_Warehouse_ID() {
		Integer ii = (Integer) get_Value("M_Warehouse_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Goal. Performance Goal
	 */
	public void setPA_Goal_ID(int PA_Goal_ID) {
		if (PA_Goal_ID <= 0)
			set_Value("PA_Goal_ID", null);
		else
			set_Value("PA_Goal_ID", new Integer(PA_Goal_ID));
	}

	/**
	 * Get Goal. Performance Goal
	 */
	public int getPA_Goal_ID() {
		Integer ii = (Integer) get_Value("PA_Goal_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Parent_Org_ID AD_Reference_ID=130 */
	public static final int PARENT_ORG_ID_AD_Reference_ID = 130;

	/**
	 * Set Parent Organization. Parent (superior) Organization
	 */
	public void setParent_Org_ID(int Parent_Org_ID) {
		if (Parent_Org_ID <= 0)
			set_Value("Parent_Org_ID", null);
		else
			set_Value("Parent_Org_ID", new Integer(Parent_Org_ID));
	}

	/**
	 * Get Parent Organization. Parent (superior) Organization
	 */
	public int getParent_Org_ID() {
		Integer ii = (Integer) get_Value("Parent_Org_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Supervisor_ID AD_Reference_ID=286 */
	public static final int SUPERVISOR_ID_AD_Reference_ID = 286;

	/**
	 * Set Supervisor. Supervisor for this user/organization - used for
	 * escalation and approval
	 */
	public void setSupervisor_ID(int Supervisor_ID) {
		if (Supervisor_ID <= 0)
			set_Value("Supervisor_ID", null);
		else
			set_Value("Supervisor_ID", new Integer(Supervisor_ID));
	}

	/**
	 * Get Supervisor. Supervisor for this user/organization - used for
	 * escalation and approval
	 */
	public int getSupervisor_ID() {
		Integer ii = (Integer) get_Value("Supervisor_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Tax ID. Tax Identification
	 */
	public void setTaxID(String TaxID) {
		if (TaxID == null)
			throw new IllegalArgumentException("TaxID is mandatory.");
		if (TaxID.length() > 20) {
			log.warning("Length > 20 - truncated");
			TaxID = TaxID.substring(0, 19);
		}
		set_Value("TaxID", TaxID);
	}

	/**
	 * Get Tax ID. Tax Identification
	 */
	public String getTaxID() {
		return (String) get_Value("TaxID");
	}
}
