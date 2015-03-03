/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_RfQLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.453
 */
public class X_C_RfQLine extends PO {
	/** Standard Constructor */
	public X_C_RfQLine(Properties ctx, int C_RfQLine_ID, String trxName) {
		super(ctx, C_RfQLine_ID, trxName);
		/**
		 * if (C_RfQLine_ID == 0) { setC_RfQLine_ID (0); setC_RfQ_ID (0);
		 * setLine (0); //
		 * 
		 * @SQL=SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM C_RfQLine
		 *             WHERE C_RfQ_ID=@C_RfQ_ID@ setM_AttributeSetInstance_ID
		 *             (0); }
		 */
	}

	/** Load Constructor */
	public X_C_RfQLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_RfQLine */
	public static final String Table_Name = "C_RfQLine";

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

	protected BigDecimal accessLevel = new BigDecimal(1);

	/** AccessLevel 1 - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_C_RfQLine[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set RfQ Line. Request for Quotation Line
	 */
	public void setC_RfQLine_ID(int C_RfQLine_ID) {
		if (C_RfQLine_ID < 1)
			throw new IllegalArgumentException("C_RfQLine_ID is mandatory.");
		set_ValueNoCheck("C_RfQLine_ID", new Integer(C_RfQLine_ID));
	}

	/**
	 * Get RfQ Line. Request for Quotation Line
	 */
	public int getC_RfQLine_ID() {
		Integer ii = (Integer) get_Value("C_RfQLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set RfQ. Request for Quotation
	 */
	public void setC_RfQ_ID(int C_RfQ_ID) {
		if (C_RfQ_ID < 1)
			throw new IllegalArgumentException("C_RfQ_ID is mandatory.");
		set_ValueNoCheck("C_RfQ_ID", new Integer(C_RfQ_ID));
	}

	/**
	 * Get RfQ. Request for Quotation
	 */
	public int getC_RfQ_ID() {
		Integer ii = (Integer) get_Value("C_RfQ_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getC_RfQ_ID()));
	}

	/**
	 * Set Work Complete. Date when work is (planned to be) complete
	 */
	public void setDateWorkComplete(Timestamp DateWorkComplete) {
		set_Value("DateWorkComplete", DateWorkComplete);
	}

	/**
	 * Get Work Complete. Date when work is (planned to be) complete
	 */
	public Timestamp getDateWorkComplete() {
		return (Timestamp) get_Value("DateWorkComplete");
	}

	/**
	 * Set Work Start. Date when work is (planned to be) started
	 */
	public void setDateWorkStart(Timestamp DateWorkStart) {
		set_Value("DateWorkStart", DateWorkStart);
	}

	/**
	 * Get Work Start. Date when work is (planned to be) started
	 */
	public Timestamp getDateWorkStart() {
		return (Timestamp) get_Value("DateWorkStart");
	}

	/**
	 * Set Delivery Days. Number of Days (planned) until Delivery
	 */
	public void setDeliveryDays(int DeliveryDays) {
		set_Value("DeliveryDays", new Integer(DeliveryDays));
	}

	/**
	 * Get Delivery Days. Number of Days (planned) until Delivery
	 */
	public int getDeliveryDays() {
		Integer ii = (Integer) get_Value("DeliveryDays");
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
	 * Set Line No. Unique line for this document
	 */
	public void setLine(int Line) {
		set_Value("Line", new Integer(Line));
	}

	/**
	 * Get Line No. Unique line for this document
	 */
	public int getLine() {
		Integer ii = (Integer) get_Value("Line");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Attribute Set Instance. Product Attribute Set Instance
	 */
	public void setM_AttributeSetInstance_ID(int M_AttributeSetInstance_ID) {
		if (M_AttributeSetInstance_ID < 0)
			throw new IllegalArgumentException(
					"M_AttributeSetInstance_ID is mandatory.");
		set_Value("M_AttributeSetInstance_ID", new Integer(
				M_AttributeSetInstance_ID));
	}

	/**
	 * Get Attribute Set Instance. Product Attribute Set Instance
	 */
	public int getM_AttributeSetInstance_ID() {
		Integer ii = (Integer) get_Value("M_AttributeSetInstance_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Product. Product, Service, Item
	 */
	public void setM_Product_ID(int M_Product_ID) {
		if (M_Product_ID <= 0)
			set_Value("M_Product_ID", null);
		else
			set_Value("M_Product_ID", new Integer(M_Product_ID));
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
