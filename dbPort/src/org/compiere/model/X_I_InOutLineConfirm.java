/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for I_InOutLineConfirm
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:01.359
 */
public class X_I_InOutLineConfirm extends PO {
	/** Standard Constructor */
	public X_I_InOutLineConfirm(Properties ctx, int I_InOutLineConfirm_ID,
			String trxName) {
		super(ctx, I_InOutLineConfirm_ID, trxName);
		/**
		 * if (I_InOutLineConfirm_ID == 0) { setConfirmationNo (null);
		 * setConfirmedQty (Env.ZERO); setDifferenceQty (Env.ZERO);
		 * setI_InOutLineConfirm_ID (0); setI_IsImported (false);
		 * setM_InOutLineConfirm_ID (0); setScrappedQty (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_I_InOutLineConfirm(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=I_InOutLineConfirm */
	public static final String Table_Name = "I_InOutLineConfirm";

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
		StringBuffer sb = new StringBuffer("X_I_InOutLineConfirm[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Confirmation No. Confirmation Number
	 */
	public void setConfirmationNo(String ConfirmationNo) {
		if (ConfirmationNo == null)
			throw new IllegalArgumentException("ConfirmationNo is mandatory.");
		if (ConfirmationNo.length() > 20) {
			log.warning("Length > 20 - truncated");
			ConfirmationNo = ConfirmationNo.substring(0, 19);
		}
		set_Value("ConfirmationNo", ConfirmationNo);
	}

	/**
	 * Get Confirmation No. Confirmation Number
	 */
	public String getConfirmationNo() {
		return (String) get_Value("ConfirmationNo");
	}

	/**
	 * Set Confirmed Quantity. Confirmation of a received quantity
	 */
	public void setConfirmedQty(BigDecimal ConfirmedQty) {
		if (ConfirmedQty == null)
			throw new IllegalArgumentException("ConfirmedQty is mandatory.");
		set_Value("ConfirmedQty", ConfirmedQty);
	}

	/**
	 * Get Confirmed Quantity. Confirmation of a received quantity
	 */
	public BigDecimal getConfirmedQty() {
		BigDecimal bd = (BigDecimal) get_Value("ConfirmedQty");
		if (bd == null)
			return Env.ZERO;
		return bd;
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
	 * Set Difference. Difference Quantity
	 */
	public void setDifferenceQty(BigDecimal DifferenceQty) {
		if (DifferenceQty == null)
			throw new IllegalArgumentException("DifferenceQty is mandatory.");
		set_Value("DifferenceQty", DifferenceQty);
	}

	/**
	 * Get Difference. Difference Quantity
	 */
	public BigDecimal getDifferenceQty() {
		BigDecimal bd = (BigDecimal) get_Value("DifferenceQty");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Import Error Message. Messages generated from import process
	 */
	public void setI_ErrorMsg(String I_ErrorMsg) {
		if (I_ErrorMsg != null && I_ErrorMsg.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			I_ErrorMsg = I_ErrorMsg.substring(0, 1999);
		}
		set_Value("I_ErrorMsg", I_ErrorMsg);
	}

	/**
	 * Get Import Error Message. Messages generated from import process
	 */
	public String getI_ErrorMsg() {
		return (String) get_Value("I_ErrorMsg");
	}

	/**
	 * Set Ship/Receipt Confirmation Import Line. Material Shipment or Receipt
	 * Confirmation Import Line
	 */
	public void setI_InOutLineConfirm_ID(int I_InOutLineConfirm_ID) {
		if (I_InOutLineConfirm_ID < 1)
			throw new IllegalArgumentException(
					"I_InOutLineConfirm_ID is mandatory.");
		set_ValueNoCheck("I_InOutLineConfirm_ID", new Integer(
				I_InOutLineConfirm_ID));
	}

	/**
	 * Get Ship/Receipt Confirmation Import Line. Material Shipment or Receipt
	 * Confirmation Import Line
	 */
	public int getI_InOutLineConfirm_ID() {
		Integer ii = (Integer) get_Value("I_InOutLineConfirm_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getI_InOutLineConfirm_ID()));
	}

	/**
	 * Set Imported. Has this import been processed
	 */
	public void setI_IsImported(boolean I_IsImported) {
		set_Value("I_IsImported", new Boolean(I_IsImported));
	}

	/**
	 * Get Imported. Has this import been processed
	 */
	public boolean isI_IsImported() {
		Object oo = get_Value("I_IsImported");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Ship/Receipt Confirmation Line. Material Shipment or Receipt
	 * Confirmation Line
	 */
	public void setM_InOutLineConfirm_ID(int M_InOutLineConfirm_ID) {
		if (M_InOutLineConfirm_ID < 1)
			throw new IllegalArgumentException(
					"M_InOutLineConfirm_ID is mandatory.");
		set_Value("M_InOutLineConfirm_ID", new Integer(M_InOutLineConfirm_ID));
	}

	/**
	 * Get Ship/Receipt Confirmation Line. Material Shipment or Receipt
	 * Confirmation Line
	 */
	public int getM_InOutLineConfirm_ID() {
		Integer ii = (Integer) get_Value("M_InOutLineConfirm_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Processed. The document has been processed
	 */
	public void setProcessed(boolean Processed) {
		set_Value("Processed", new Boolean(Processed));
	}

	/**
	 * Get Processed. The document has been processed
	 */
	public boolean isProcessed() {
		Object oo = get_Value("Processed");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Process Now */
	public void setProcessing(boolean Processing) {
		set_Value("Processing", new Boolean(Processing));
	}

	/** Get Process Now */
	public boolean isProcessing() {
		Object oo = get_Value("Processing");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Scrapped Quantity. The Quantity scrapped due to QA issues
	 */
	public void setScrappedQty(BigDecimal ScrappedQty) {
		if (ScrappedQty == null)
			throw new IllegalArgumentException("ScrappedQty is mandatory.");
		set_Value("ScrappedQty", ScrappedQty);
	}

	/**
	 * Get Scrapped Quantity. The Quantity scrapped due to QA issues
	 */
	public BigDecimal getScrappedQty() {
		BigDecimal bd = (BigDecimal) get_Value("ScrappedQty");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
