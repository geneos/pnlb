/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_PrintForm
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.718
 */
public class X_AD_PrintForm extends PO {
	/** Standard Constructor */
	public X_AD_PrintForm(Properties ctx, int AD_PrintForm_ID, String trxName) {
		super(ctx, AD_PrintForm_ID, trxName);
		/**
		 * if (AD_PrintForm_ID == 0) { setAD_PrintForm_ID (0); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_PrintForm(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_PrintForm */
	public static final String Table_Name = "AD_PrintForm";

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
		StringBuffer sb = new StringBuffer("X_AD_PrintForm[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Print Form. Form
	 */
	public void setAD_PrintForm_ID(int AD_PrintForm_ID) {
		if (AD_PrintForm_ID < 1)
			throw new IllegalArgumentException("AD_PrintForm_ID is mandatory.");
		set_ValueNoCheck("AD_PrintForm_ID", new Integer(AD_PrintForm_ID));
	}

	/**
	 * Get Print Form. Form
	 */
	public int getAD_PrintForm_ID() {
		Integer ii = (Integer) get_Value("AD_PrintForm_ID");
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

	/** Invoice_MailText_ID AD_Reference_ID=274 */
	public static final int INVOICE_MAILTEXT_ID_AD_Reference_ID = 274;

	/**
	 * Set Invoice Mail Text. Email text used for sending invoices
	 */
	public void setInvoice_MailText_ID(int Invoice_MailText_ID) {
		if (Invoice_MailText_ID <= 0)
			set_Value("Invoice_MailText_ID", null);
		else
			set_Value("Invoice_MailText_ID", new Integer(Invoice_MailText_ID));
	}

	/**
	 * Get Invoice Mail Text. Email text used for sending invoices
	 */
	public int getInvoice_MailText_ID() {
		Integer ii = (Integer) get_Value("Invoice_MailText_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Invoice_PrintFormat_ID AD_Reference_ID=261 */
	public static final int INVOICE_PRINTFORMAT_ID_AD_Reference_ID = 261;

	/**
	 * Set Invoice Print Format. Print Format for printing Invoices
	 */
	public void setInvoice_PrintFormat_ID(int Invoice_PrintFormat_ID) {
		if (Invoice_PrintFormat_ID <= 0)
			set_Value("Invoice_PrintFormat_ID", null);
		else
			set_Value("Invoice_PrintFormat_ID", new Integer(
					Invoice_PrintFormat_ID));
	}

	/**
	 * Get Invoice Print Format. Print Format for printing Invoices
	 */
	public int getInvoice_PrintFormat_ID() {
		Integer ii = (Integer) get_Value("Invoice_PrintFormat_ID");
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

	/** Order_MailText_ID AD_Reference_ID=274 */
	public static final int ORDER_MAILTEXT_ID_AD_Reference_ID = 274;

	/**
	 * Set Order Mail Text. Email text used for sending order acknowledgements
	 * or quotations
	 */
	public void setOrder_MailText_ID(int Order_MailText_ID) {
		if (Order_MailText_ID <= 0)
			set_Value("Order_MailText_ID", null);
		else
			set_Value("Order_MailText_ID", new Integer(Order_MailText_ID));
	}

	/**
	 * Get Order Mail Text. Email text used for sending order acknowledgements
	 * or quotations
	 */
	public int getOrder_MailText_ID() {
		Integer ii = (Integer) get_Value("Order_MailText_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Order_PrintFormat_ID AD_Reference_ID=262 */
	public static final int ORDER_PRINTFORMAT_ID_AD_Reference_ID = 262;

	/**
	 * Set Order Print Format. Print Format for Orders, Quotes, Offers
	 */
	public void setOrder_PrintFormat_ID(int Order_PrintFormat_ID) {
		if (Order_PrintFormat_ID <= 0)
			set_Value("Order_PrintFormat_ID", null);
		else
			set_Value("Order_PrintFormat_ID", new Integer(Order_PrintFormat_ID));
	}

	/**
	 * Get Order Print Format. Print Format for Orders, Quotes, Offers
	 */
	public int getOrder_PrintFormat_ID() {
		Integer ii = (Integer) get_Value("Order_PrintFormat_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Project_MailText_ID AD_Reference_ID=274 */
	public static final int PROJECT_MAILTEXT_ID_AD_Reference_ID = 274;

	/**
	 * Set Project Mail Text. Standard text for Project EMails
	 */
	public void setProject_MailText_ID(int Project_MailText_ID) {
		if (Project_MailText_ID <= 0)
			set_Value("Project_MailText_ID", null);
		else
			set_Value("Project_MailText_ID", new Integer(Project_MailText_ID));
	}

	/**
	 * Get Project Mail Text. Standard text for Project EMails
	 */
	public int getProject_MailText_ID() {
		Integer ii = (Integer) get_Value("Project_MailText_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Project_PrintFormat_ID AD_Reference_ID=259 */
	public static final int PROJECT_PRINTFORMAT_ID_AD_Reference_ID = 259;

	/**
	 * Set Project Print Format. Standard Project Print Format
	 */
	public void setProject_PrintFormat_ID(int Project_PrintFormat_ID) {
		if (Project_PrintFormat_ID <= 0)
			set_Value("Project_PrintFormat_ID", null);
		else
			set_Value("Project_PrintFormat_ID", new Integer(
					Project_PrintFormat_ID));
	}

	/**
	 * Get Project Print Format. Standard Project Print Format
	 */
	public int getProject_PrintFormat_ID() {
		Integer ii = (Integer) get_Value("Project_PrintFormat_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Remittance_MailText_ID AD_Reference_ID=274 */
	public static final int REMITTANCE_MAILTEXT_ID_AD_Reference_ID = 274;

	/**
	 * Set Remittance Mail Text. Email text used for sending payment remittances
	 */
	public void setRemittance_MailText_ID(int Remittance_MailText_ID) {
		if (Remittance_MailText_ID <= 0)
			set_Value("Remittance_MailText_ID", null);
		else
			set_Value("Remittance_MailText_ID", new Integer(
					Remittance_MailText_ID));
	}

	/**
	 * Get Remittance Mail Text. Email text used for sending payment remittances
	 */
	public int getRemittance_MailText_ID() {
		Integer ii = (Integer) get_Value("Remittance_MailText_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Remittance_PrintFormat_ID AD_Reference_ID=268 */
	public static final int REMITTANCE_PRINTFORMAT_ID_AD_Reference_ID = 268;

	/**
	 * Set Remittance Print Format. Print Format for separate Remittances
	 */
	public void setRemittance_PrintFormat_ID(int Remittance_PrintFormat_ID) {
		if (Remittance_PrintFormat_ID <= 0)
			set_Value("Remittance_PrintFormat_ID", null);
		else
			set_Value("Remittance_PrintFormat_ID", new Integer(
					Remittance_PrintFormat_ID));
	}

	/**
	 * Get Remittance Print Format. Print Format for separate Remittances
	 */
	public int getRemittance_PrintFormat_ID() {
		Integer ii = (Integer) get_Value("Remittance_PrintFormat_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Shipment_MailText_ID AD_Reference_ID=274 */
	public static final int SHIPMENT_MAILTEXT_ID_AD_Reference_ID = 274;

	/**
	 * Set Shipment Mail Text. Email text used for sending delivery notes
	 */
	public void setShipment_MailText_ID(int Shipment_MailText_ID) {
		if (Shipment_MailText_ID <= 0)
			set_Value("Shipment_MailText_ID", null);
		else
			set_Value("Shipment_MailText_ID", new Integer(Shipment_MailText_ID));
	}

	/**
	 * Get Shipment Mail Text. Email text used for sending delivery notes
	 */
	public int getShipment_MailText_ID() {
		Integer ii = (Integer) get_Value("Shipment_MailText_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Shipment_PrintFormat_ID AD_Reference_ID=263 */
	public static final int SHIPMENT_PRINTFORMAT_ID_AD_Reference_ID = 263;

	/**
	 * Set Shipment Print Format. Print Format for Shipments, Receipts, Pick
	 * Lists
	 */
	public void setShipment_PrintFormat_ID(int Shipment_PrintFormat_ID) {
		if (Shipment_PrintFormat_ID <= 0)
			set_Value("Shipment_PrintFormat_ID", null);
		else
			set_Value("Shipment_PrintFormat_ID", new Integer(
					Shipment_PrintFormat_ID));
	}

	/**
	 * Get Shipment Print Format. Print Format for Shipments, Receipts, Pick
	 * Lists
	 */
	public int getShipment_PrintFormat_ID() {
		Integer ii = (Integer) get_Value("Shipment_PrintFormat_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
