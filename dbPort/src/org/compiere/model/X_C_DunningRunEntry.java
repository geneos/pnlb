/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_DunningRunEntry
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.375
 */
public class X_C_DunningRunEntry extends PO {
	/** Standard Constructor */
	public X_C_DunningRunEntry(Properties ctx, int C_DunningRunEntry_ID,
			String trxName) {
		super(ctx, C_DunningRunEntry_ID, trxName);
		/**
		 * if (C_DunningRunEntry_ID == 0) { setAmt (Env.ZERO); setC_BPartner_ID
		 * (0); setC_BPartner_Location_ID (0); setC_Currency_ID (0);
		 * setC_DunningRunEntry_ID (0); setC_DunningRun_ID (0); setProcessed
		 * (false); setQty (Env.ZERO); setSalesRep_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_C_DunningRunEntry(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_DunningRunEntry */
	public static final String Table_Name = "C_DunningRunEntry";

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
		StringBuffer sb = new StringBuffer("X_C_DunningRunEntry[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public void setAD_User_ID(int AD_User_ID) {
		if (AD_User_ID <= 0)
			set_Value("AD_User_ID", null);
		else
			set_Value("AD_User_ID", new Integer(AD_User_ID));
	}

	/**
	 * Get User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public int getAD_User_ID() {
		Integer ii = (Integer) get_Value("AD_User_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Amount. Amount
	 */
	public void setAmt(BigDecimal Amt) {
		if (Amt == null)
			throw new IllegalArgumentException("Amt is mandatory.");
		set_Value("Amt", Amt);
	}

	/**
	 * Get Amount. Amount
	 */
	public BigDecimal getAmt() {
		BigDecimal bd = (BigDecimal) get_Value("Amt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Business Partner . Identifies a Business Partner
	 */
	public void setC_BPartner_ID(int C_BPartner_ID) {
		if (C_BPartner_ID < 1)
			throw new IllegalArgumentException("C_BPartner_ID is mandatory.");
		set_Value("C_BPartner_ID", new Integer(C_BPartner_ID));
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
	 * Set Partner Location. Identifies the (ship to) address for this Business
	 * Partner
	 */
	public void setC_BPartner_Location_ID(int C_BPartner_Location_ID) {
		if (C_BPartner_Location_ID < 1)
			throw new IllegalArgumentException(
					"C_BPartner_Location_ID is mandatory.");
		set_Value("C_BPartner_Location_ID", new Integer(C_BPartner_Location_ID));
	}

	/**
	 * Get Partner Location. Identifies the (ship to) address for this Business
	 * Partner
	 */
	public int getC_BPartner_Location_ID() {
		Integer ii = (Integer) get_Value("C_BPartner_Location_ID");
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
	 * Set Dunning Run Entry. Dunning Run Entry
	 */
	public void setC_DunningRunEntry_ID(int C_DunningRunEntry_ID) {
		if (C_DunningRunEntry_ID < 1)
			throw new IllegalArgumentException(
					"C_DunningRunEntry_ID is mandatory.");
		set_ValueNoCheck("C_DunningRunEntry_ID", new Integer(
				C_DunningRunEntry_ID));
	}

	/**
	 * Get Dunning Run Entry. Dunning Run Entry
	 */
	public int getC_DunningRunEntry_ID() {
		Integer ii = (Integer) get_Value("C_DunningRunEntry_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Dunning Run. Dunning Run
	 */
	public void setC_DunningRun_ID(int C_DunningRun_ID) {
		if (C_DunningRun_ID < 1)
			throw new IllegalArgumentException("C_DunningRun_ID is mandatory.");
		set_ValueNoCheck("C_DunningRun_ID", new Integer(C_DunningRun_ID));
	}

	/**
	 * Get Dunning Run. Dunning Run
	 */
	public int getC_DunningRun_ID() {
		Integer ii = (Integer) get_Value("C_DunningRun_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getC_DunningRun_ID()));
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

	/**
	 * Set Quantity. Quantity
	 */
	public void setQty(BigDecimal Qty) {
		if (Qty == null)
			throw new IllegalArgumentException("Qty is mandatory.");
		set_Value("Qty", Qty);
	}

	/**
	 * Get Quantity. Quantity
	 */
	public BigDecimal getQty() {
		BigDecimal bd = (BigDecimal) get_Value("Qty");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** SalesRep_ID AD_Reference_ID=190 */
	public static final int SALESREP_ID_AD_Reference_ID = 190;

	/**
	 * Set Sales Representative. Sales Representative or Company Agent
	 */
	public void setSalesRep_ID(int SalesRep_ID) {
		if (SalesRep_ID < 1)
			throw new IllegalArgumentException("SalesRep_ID is mandatory.");
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
}
