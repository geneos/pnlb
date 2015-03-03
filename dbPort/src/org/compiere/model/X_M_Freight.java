/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_Freight
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.765
 */
public class X_M_Freight extends PO {
	/** Standard Constructor */
	public X_M_Freight(Properties ctx, int M_Freight_ID, String trxName) {
		super(ctx, M_Freight_ID, trxName);
		/**
		 * if (M_Freight_ID == 0) { setC_Currency_ID (0); setFreightAmt
		 * (Env.ZERO); setM_FreightCategory_ID (0); setM_Freight_ID (0);
		 * setM_Shipper_ID (0); setValidFrom (new
		 * Timestamp(System.currentTimeMillis())); }
		 */
	}

	/** Load Constructor */
	public X_M_Freight(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_Freight */
	public static final String Table_Name = "M_Freight";

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
		StringBuffer sb = new StringBuffer("X_M_Freight[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Country. Country
	 */
	public void setC_Country_ID(int C_Country_ID) {
		if (C_Country_ID <= 0)
			set_Value("C_Country_ID", null);
		else
			set_Value("C_Country_ID", new Integer(C_Country_ID));
	}

	/**
	 * Get Country. Country
	 */
	public int getC_Country_ID() {
		Integer ii = (Integer) get_Value("C_Country_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Currency. The Currency for this record
	 */
	public void setC_Currency_ID(int C_Currency_ID) {
		if (C_Currency_ID < 1)
			throw new IllegalArgumentException("C_Currency_ID is mandatory.");
		set_Value("C_Currency_ID", new Integer(C_Currency_ID));
	}

	/**
	 * Get Currency. The Currency for this record
	 */
	public int getC_Currency_ID() {
		Integer ii = (Integer) get_Value("C_Currency_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Region. Identifies a geographical Region
	 */
	public void setC_Region_ID(int C_Region_ID) {
		if (C_Region_ID <= 0)
			set_Value("C_Region_ID", null);
		else
			set_Value("C_Region_ID", new Integer(C_Region_ID));
	}

	/**
	 * Get Region. Identifies a geographical Region
	 */
	public int getC_Region_ID() {
		Integer ii = (Integer) get_Value("C_Region_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Freight Amount. Freight Amount
	 */
	public void setFreightAmt(BigDecimal FreightAmt) {
		if (FreightAmt == null)
			throw new IllegalArgumentException("FreightAmt is mandatory.");
		set_Value("FreightAmt", FreightAmt);
	}

	/**
	 * Get Freight Amount. Freight Amount
	 */
	public BigDecimal getFreightAmt() {
		BigDecimal bd = (BigDecimal) get_Value("FreightAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Freight Category. Category of the Freight
	 */
	public void setM_FreightCategory_ID(int M_FreightCategory_ID) {
		if (M_FreightCategory_ID < 1)
			throw new IllegalArgumentException(
					"M_FreightCategory_ID is mandatory.");
		set_Value("M_FreightCategory_ID", new Integer(M_FreightCategory_ID));
	}

	/**
	 * Get Freight Category. Category of the Freight
	 */
	public int getM_FreightCategory_ID() {
		Integer ii = (Integer) get_Value("M_FreightCategory_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Freight. Freight Rate
	 */
	public void setM_Freight_ID(int M_Freight_ID) {
		if (M_Freight_ID < 1)
			throw new IllegalArgumentException("M_Freight_ID is mandatory.");
		set_ValueNoCheck("M_Freight_ID", new Integer(M_Freight_ID));
	}

	/**
	 * Get Freight. Freight Rate
	 */
	public int getM_Freight_ID() {
		Integer ii = (Integer) get_Value("M_Freight_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Shipper. Method or manner of product delivery
	 */
	public void setM_Shipper_ID(int M_Shipper_ID) {
		if (M_Shipper_ID < 1)
			throw new IllegalArgumentException("M_Shipper_ID is mandatory.");
		set_ValueNoCheck("M_Shipper_ID", new Integer(M_Shipper_ID));
	}

	/**
	 * Get Shipper. Method or manner of product delivery
	 */
	public int getM_Shipper_ID() {
		Integer ii = (Integer) get_Value("M_Shipper_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getM_Shipper_ID()));
	}

	/** To_Country_ID AD_Reference_ID=156 */
	public static final int TO_COUNTRY_ID_AD_Reference_ID = 156;

	/**
	 * Set To. Receiving Country
	 */
	public void setTo_Country_ID(int To_Country_ID) {
		if (To_Country_ID <= 0)
			set_Value("To_Country_ID", null);
		else
			set_Value("To_Country_ID", new Integer(To_Country_ID));
	}

	/**
	 * Get To. Receiving Country
	 */
	public int getTo_Country_ID() {
		Integer ii = (Integer) get_Value("To_Country_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** To_Region_ID AD_Reference_ID=157 */
	public static final int TO_REGION_ID_AD_Reference_ID = 157;

	/**
	 * Set To. Receiving Region
	 */
	public void setTo_Region_ID(int To_Region_ID) {
		if (To_Region_ID <= 0)
			set_Value("To_Region_ID", null);
		else
			set_Value("To_Region_ID", new Integer(To_Region_ID));
	}

	/**
	 * Get To. Receiving Region
	 */
	public int getTo_Region_ID() {
		Integer ii = (Integer) get_Value("To_Region_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Valid from. Valid from including this date (first day)
	 */
	public void setValidFrom(Timestamp ValidFrom) {
		if (ValidFrom == null)
			throw new IllegalArgumentException("ValidFrom is mandatory.");
		set_Value("ValidFrom", ValidFrom);
	}

	/**
	 * Get Valid from. Valid from including this date (first day)
	 */
	public Timestamp getValidFrom() {
		return (Timestamp) get_Value("ValidFrom");
	}
}
