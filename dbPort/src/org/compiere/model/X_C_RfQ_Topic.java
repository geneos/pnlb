/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_RfQ_Topic
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.515
 */
public class X_C_RfQ_Topic extends PO {
	/** Standard Constructor */
	public X_C_RfQ_Topic(Properties ctx, int C_RfQ_Topic_ID, String trxName) {
		super(ctx, C_RfQ_Topic_ID, trxName);
		/**
		 * if (C_RfQ_Topic_ID == 0) { setC_RfQ_Topic_ID (0); setIsSelfService
		 * (false); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_C_RfQ_Topic(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_RfQ_Topic */
	public static final String Table_Name = "C_RfQ_Topic";

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
		StringBuffer sb = new StringBuffer("X_C_RfQ_Topic[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Print Format. Data Print Format
	 */
	public void setAD_PrintFormat_ID(int AD_PrintFormat_ID) {
		if (AD_PrintFormat_ID <= 0)
			set_Value("AD_PrintFormat_ID", null);
		else
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
	 * Set RfQ Topic. Topic for Request for Quotations
	 */
	public void setC_RfQ_Topic_ID(int C_RfQ_Topic_ID) {
		if (C_RfQ_Topic_ID < 1)
			throw new IllegalArgumentException("C_RfQ_Topic_ID is mandatory.");
		set_ValueNoCheck("C_RfQ_Topic_ID", new Integer(C_RfQ_Topic_ID));
	}

	/**
	 * Get RfQ Topic. Topic for Request for Quotations
	 */
	public int getC_RfQ_Topic_ID() {
		Integer ii = (Integer) get_Value("C_RfQ_Topic_ID");
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
	 * Set Self-Service. This is a Self-Service entry or this entry can be
	 * changed via Self-Service
	 */
	public void setIsSelfService(boolean IsSelfService) {
		set_Value("IsSelfService", new Boolean(IsSelfService));
	}

	/**
	 * Get Self-Service. This is a Self-Service entry or this entry can be
	 * changed via Self-Service
	 */
	public boolean isSelfService() {
		Object oo = get_Value("IsSelfService");
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
