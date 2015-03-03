/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for A_RegistrationProduct
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.265
 */
public class X_A_RegistrationProduct extends PO {
	/** Standard Constructor */
	public X_A_RegistrationProduct(Properties ctx,
			int A_RegistrationProduct_ID, String trxName) {
		super(ctx, A_RegistrationProduct_ID, trxName);
		/**
		 * if (A_RegistrationProduct_ID == 0) { setA_RegistrationAttribute_ID
		 * (0); setM_Product_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_A_RegistrationProduct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=A_RegistrationProduct */
	public static final String Table_Name = "A_RegistrationProduct";

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
		StringBuffer sb = new StringBuffer("X_A_RegistrationProduct[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Registration Attribute. Asset Registration Attribute
	 */
	public void setA_RegistrationAttribute_ID(int A_RegistrationAttribute_ID) {
		if (A_RegistrationAttribute_ID < 1)
			throw new IllegalArgumentException(
					"A_RegistrationAttribute_ID is mandatory.");
		set_ValueNoCheck("A_RegistrationAttribute_ID", new Integer(
				A_RegistrationAttribute_ID));
	}

	/**
	 * Get Registration Attribute. Asset Registration Attribute
	 */
	public int getA_RegistrationAttribute_ID() {
		Integer ii = (Integer) get_Value("A_RegistrationAttribute_ID");
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
	 * Set Product. Product, Service, Item
	 */
	public void setM_Product_ID(int M_Product_ID) {
		if (M_Product_ID < 1)
			throw new IllegalArgumentException("M_Product_ID is mandatory.");
		set_ValueNoCheck("M_Product_ID", new Integer(M_Product_ID));
	}

	/**
	 * Get Product. Product, Service, Item
	 */
	public int getM_Product_ID() {
		Integer ii = (Integer) get_Value("M_Product_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
