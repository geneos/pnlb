/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_SalesRegion
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.531
 */
public class X_C_SalesRegion extends PO {
	/** Standard Constructor */
	public X_C_SalesRegion(Properties ctx, int C_SalesRegion_ID, String trxName) {
		super(ctx, C_SalesRegion_ID, trxName);
		/**
		 * if (C_SalesRegion_ID == 0) { setC_SalesRegion_ID (0); setIsDefault
		 * (false); setIsSummary (false); setName (null); setValue (null); }
		 */
	}

	/** Load Constructor */
	public X_C_SalesRegion(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_SalesRegion */
	public static final String Table_Name = "C_SalesRegion";

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
		StringBuffer sb = new StringBuffer("X_C_SalesRegion[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** Set COEFICIENTEIB */
	public void setCOEFICIENTEIB(BigDecimal COEFICIENTEIB) {
		set_Value("COEFICIENTEIB", COEFICIENTEIB);
	}

	/** Get COEFICIENTEIB */
	public BigDecimal getCOEFICIENTEIB() {
		BigDecimal bd = (BigDecimal) get_Value("COEFICIENTEIB");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Sales Region. Sales coverage region
	 */
	public void setC_SalesRegion_ID(int C_SalesRegion_ID) {
		if (C_SalesRegion_ID < 1)
			throw new IllegalArgumentException("C_SalesRegion_ID is mandatory.");
		set_ValueNoCheck("C_SalesRegion_ID", new Integer(C_SalesRegion_ID));
	}

	/**
	 * Get Sales Region. Sales coverage region
	 */
	public int getC_SalesRegion_ID() {
		Integer ii = (Integer) get_Value("C_SalesRegion_ID");
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
	 * Set Default. Default value
	 */
	public void setIsDefault(boolean IsDefault) {
		set_Value("IsDefault", new Boolean(IsDefault));
	}

	/**
	 * Get Default. Default value
	 */
	public boolean isDefault() {
		Object oo = get_Value("IsDefault");
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

	/** Set LIMITEMINIMOIB */
	public void setLIMITEMINIMOIB(BigDecimal LIMITEMINIMOIB) {
		set_Value("LIMITEMINIMOIB", LIMITEMINIMOIB);
	}

	/** Get LIMITEMINIMOIB */
	public BigDecimal getLIMITEMINIMOIB() {
		BigDecimal bd = (BigDecimal) get_Value("LIMITEMINIMOIB");
		if (bd == null)
			return Env.ZERO;
		return bd;
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

	/** SalesRep_ID AD_Reference_ID=190 */
	public static final int SALESREP_ID_AD_Reference_ID = 190;

	/**
	 * Set Sales Representative. Sales Representative or Company Agent
	 */
	public void setSalesRep_ID(int SalesRep_ID) {
		if (SalesRep_ID <= 0)
			set_Value("SalesRep_ID", null);
		else
			set_Value("SalesRep_ID", new Integer(SalesRep_ID));
	}

	/**
	 * Get Sales Representative. Sales Representative or Company Agent
	 */
	public int getSalesRep_ID() {
		Integer ii = (Integer) get_Value("SalesRep_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** T_CODIGOJURISDICCION_ID AD_Reference_ID=1000062 */
	public static final int T_CODIGOJURISDICCION_ID_AD_Reference_ID = 1000062;

	/** Set T_CODIGOJURISDICCION_ID */
	public void setT_CODIGOJURISDICCION_ID(int T_CODIGOJURISDICCION_ID) {
		if (T_CODIGOJURISDICCION_ID <= 0)
			set_Value("T_CODIGOJURISDICCION_ID", null);
		else
			set_Value("T_CODIGOJURISDICCION_ID", new Integer(
					T_CODIGOJURISDICCION_ID));
	}

	/** Get T_CODIGOJURISDICCION_ID */
	public int getT_CODIGOJURISDICCION_ID() {
		Integer ii = (Integer) get_Value("T_CODIGOJURISDICCION_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public void setValue(String Value) {
		if (Value == null)
			throw new IllegalArgumentException("Value is mandatory.");
		if (Value.length() > 40) {
			log.warning("Length > 40 - truncated");
			Value = Value.substring(0, 39);
		}
		set_Value("Value", Value);
	}

	/**
	 * Get Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public String getValue() {
		return (String) get_Value("Value");
	}
}
