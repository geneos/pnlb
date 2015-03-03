/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_BPartner_Location
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.828
 */
public class X_C_BPartner_Jurisdiccion extends PO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Standard Constructor */
	public X_C_BPartner_Jurisdiccion(Properties ctx,
			int C_BPartner_Jurisdiccion_ID, String trxName) {
		super(ctx, C_BPartner_Jurisdiccion_ID, trxName);

	}

	/** Load Constructor */
	public X_C_BPartner_Jurisdiccion(Properties ctx, ResultSet rs,
			String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_BPartner_Location */
	public static final String Table_Name = "C_BPartner_Jurisdiccion";

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
		StringBuffer sb = new StringBuffer("X_C_BPartner_Jurisdiccion[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Business Partner . Identifies a Business Partner
	 */
	public void setC_BPartner_ID(int C_BPartner_ID) {
		if (C_BPartner_ID < 1)
			throw new IllegalArgumentException("C_BPartner_ID is mandatory.");
		set_ValueNoCheck("C_BPartner_ID", new Integer(C_BPartner_ID));
	}

	/**
	 * Get Business Partner . Identifies a Business Partner
	 */
	public int getC_BPartner_ID() {
		Integer ii = (Integer) get_Value("C_BPartner_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Jurisdiccion. Identifies the (ship to) address for this Business
	 * Partner
	 */
	public void setC_BPartner_Jurisdiccion_ID(int C_BPartner_Jurisdiccion_ID) {
		if (C_BPartner_Jurisdiccion_ID < 1)
			throw new IllegalArgumentException(
					"C_BPartner_Jurisdiccion_ID is mandatory.");
		set_ValueNoCheck("C_BPartner_Location_ID", new Integer(
				C_BPartner_Jurisdiccion_ID));
	}

	/**
	 * Get Jurisdiccion. Identifies the (ship to) address for this Business
	 * Partner
	 */
	public int getC_BPartner_Jurisdiccion_ID() {
		Integer ii = (Integer) get_Value("C_BPartner_Jurisdiccion_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
