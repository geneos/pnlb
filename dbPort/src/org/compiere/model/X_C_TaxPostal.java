/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_TaxPostal
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.765
 */
public class X_C_TaxPostal extends PO {
	/** Standard Constructor */
	public X_C_TaxPostal(Properties ctx, int C_TaxPostal_ID, String trxName) {
		super(ctx, C_TaxPostal_ID, trxName);
		/**
		 * if (C_TaxPostal_ID == 0) { setC_TaxPostal_ID (0); setC_Tax_ID (0);
		 * setPostal (null); }
		 */
	}

	/** Load Constructor */
	public X_C_TaxPostal(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_TaxPostal */
	public static final String Table_Name = "C_TaxPostal";

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
		StringBuffer sb = new StringBuffer("X_C_TaxPostal[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Tax ZIP. Tax Postal/ZIP
	 */
	public void setC_TaxPostal_ID(int C_TaxPostal_ID) {
		if (C_TaxPostal_ID < 1)
			throw new IllegalArgumentException("C_TaxPostal_ID is mandatory.");
		set_ValueNoCheck("C_TaxPostal_ID", new Integer(C_TaxPostal_ID));
	}

	/**
	 * Get Tax ZIP. Tax Postal/ZIP
	 */
	public int getC_TaxPostal_ID() {
		Integer ii = (Integer) get_Value("C_TaxPostal_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Tax. Tax identifier
	 */
	public void setC_Tax_ID(int C_Tax_ID) {
		if (C_Tax_ID < 1)
			throw new IllegalArgumentException("C_Tax_ID is mandatory.");
		set_ValueNoCheck("C_Tax_ID", new Integer(C_Tax_ID));
	}

	/**
	 * Get Tax. Tax identifier
	 */
	public int getC_Tax_ID() {
		Integer ii = (Integer) get_Value("C_Tax_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set ZIP. Postal code
	 */
	public void setPostal(String Postal) {
		if (Postal == null)
			throw new IllegalArgumentException("Postal is mandatory.");
		if (Postal.length() > 10) {
			log.warning("Length > 10 - truncated");
			Postal = Postal.substring(0, 9);
		}
		set_Value("Postal", Postal);
	}

	/**
	 * Get ZIP. Postal code
	 */
	public String getPostal() {
		return (String) get_Value("Postal");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getPostal());
	}

	/**
	 * Set ZIP To. Postal code to
	 */
	public void setPostal_To(String Postal_To) {
		if (Postal_To != null && Postal_To.length() > 10) {
			log.warning("Length > 10 - truncated");
			Postal_To = Postal_To.substring(0, 9);
		}
		set_Value("Postal_To", Postal_To);
	}

	/**
	 * Get ZIP To. Postal code to
	 */
	public String getPostal_To() {
		return (String) get_Value("Postal_To");
	}
}
