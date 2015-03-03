/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for PA_Achievement
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.812
 */
public class X_PA_Achievement extends PO {
	/** Standard Constructor */
	public X_PA_Achievement(Properties ctx, int PA_Achievement_ID,
			String trxName) {
		super(ctx, PA_Achievement_ID, trxName);
		/**
		 * if (PA_Achievement_ID == 0) { setIsAchieved (false); setManualActual
		 * (Env.ZERO); setName (null); setPA_Achievement_ID (0);
		 * setPA_Measure_ID (0); setSeqNo (0); }
		 */
	}

	/** Load Constructor */
	public X_PA_Achievement(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=PA_Achievement */
	public static final String Table_Name = "PA_Achievement";

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
		StringBuffer sb = new StringBuffer("X_PA_Achievement[")
				.append(get_ID()).append("]");
		return sb.toString();
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
	 * Set Achieved. The goal is achieved
	 */
	public void setIsAchieved(boolean IsAchieved) {
		set_Value("IsAchieved", new Boolean(IsAchieved));
	}

	/**
	 * Get Achieved. The goal is achieved
	 */
	public boolean isAchieved() {
		Object oo = get_Value("IsAchieved");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Manual Actual. Manually entered actual value
	 */
	public void setManualActual(BigDecimal ManualActual) {
		if (ManualActual == null)
			throw new IllegalArgumentException("ManualActual is mandatory.");
		set_Value("ManualActual", ManualActual);
	}

	/**
	 * Get Manual Actual. Manually entered actual value
	 */
	public BigDecimal getManualActual() {
		BigDecimal bd = (BigDecimal) get_Value("ManualActual");
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

	/**
	 * Set Note. Optional additional user defined information
	 */
	public void setNote(String Note) {
		if (Note != null && Note.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Note = Note.substring(0, 1999);
		}
		set_Value("Note", Note);
	}

	/**
	 * Get Note. Optional additional user defined information
	 */
	public String getNote() {
		return (String) get_Value("Note");
	}

	/**
	 * Set Achievement. Performance Achievement
	 */
	public void setPA_Achievement_ID(int PA_Achievement_ID) {
		if (PA_Achievement_ID < 1)
			throw new IllegalArgumentException(
					"PA_Achievement_ID is mandatory.");
		set_ValueNoCheck("PA_Achievement_ID", new Integer(PA_Achievement_ID));
	}

	/**
	 * Get Achievement. Performance Achievement
	 */
	public int getPA_Achievement_ID() {
		Integer ii = (Integer) get_Value("PA_Achievement_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Measure. Concrete Performance Measurement
	 */
	public void setPA_Measure_ID(int PA_Measure_ID) {
		if (PA_Measure_ID < 1)
			throw new IllegalArgumentException("PA_Measure_ID is mandatory.");
		set_ValueNoCheck("PA_Measure_ID", new Integer(PA_Measure_ID));
	}

	/**
	 * Get Measure. Concrete Performance Measurement
	 */
	public int getPA_Measure_ID() {
		Integer ii = (Integer) get_Value("PA_Measure_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
