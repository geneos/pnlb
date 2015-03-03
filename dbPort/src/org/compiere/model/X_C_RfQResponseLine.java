/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_RfQResponseLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.5
 */
public class X_C_RfQResponseLine extends PO {
	/** Standard Constructor */
	public X_C_RfQResponseLine(Properties ctx, int C_RfQResponseLine_ID,
			String trxName) {
		super(ctx, C_RfQResponseLine_ID, trxName);
		/**
		 * if (C_RfQResponseLine_ID == 0) { setC_RfQLine_ID (0);
		 * setC_RfQResponseLine_ID (0); setC_RfQResponse_ID (0);
		 * setIsSelectedWinner (false); setIsSelfService (false); }
		 */
	}

	/** Load Constructor */
	public X_C_RfQResponseLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_RfQResponseLine */
	public static final String Table_Name = "C_RfQResponseLine";

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
		StringBuffer sb = new StringBuffer("X_C_RfQResponseLine[").append(
				get_ID()).append("]");
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
	 * Set RfQ Response Line. Request for Quotation Response Line
	 */
	public void setC_RfQResponseLine_ID(int C_RfQResponseLine_ID) {
		if (C_RfQResponseLine_ID < 1)
			throw new IllegalArgumentException(
					"C_RfQResponseLine_ID is mandatory.");
		set_ValueNoCheck("C_RfQResponseLine_ID", new Integer(
				C_RfQResponseLine_ID));
	}

	/**
	 * Get RfQ Response Line. Request for Quotation Response Line
	 */
	public int getC_RfQResponseLine_ID() {
		Integer ii = (Integer) get_Value("C_RfQResponseLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set RfQ Response. Request for Quotation Response from a potential Vendor
	 */
	public void setC_RfQResponse_ID(int C_RfQResponse_ID) {
		if (C_RfQResponse_ID < 1)
			throw new IllegalArgumentException("C_RfQResponse_ID is mandatory.");
		set_ValueNoCheck("C_RfQResponse_ID", new Integer(C_RfQResponse_ID));
	}

	/**
	 * Get RfQ Response. Request for Quotation Response from a potential Vendor
	 */
	public int getC_RfQResponse_ID() {
		Integer ii = (Integer) get_Value("C_RfQResponse_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
	 * Set Selected Winner. The resonse is the selected winner
	 */
	public void setIsSelectedWinner(boolean IsSelectedWinner) {
		set_Value("IsSelectedWinner", new Boolean(IsSelectedWinner));
	}

	/**
	 * Get Selected Winner. The resonse is the selected winner
	 */
	public boolean isSelectedWinner() {
		Object oo = get_Value("IsSelectedWinner");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
}
