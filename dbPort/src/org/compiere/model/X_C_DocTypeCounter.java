/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_DocTypeCounter
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.312
 */
public class X_C_DocTypeCounter extends PO {
	/** Standard Constructor */
	public X_C_DocTypeCounter(Properties ctx, int C_DocTypeCounter_ID,
			String trxName) {
		super(ctx, C_DocTypeCounter_ID, trxName);
		/**
		 * if (C_DocTypeCounter_ID == 0) { setC_DocTypeCounter_ID (0);
		 * setC_DocType_ID (0); setCounter_C_DocType_ID (0); setIsCreateCounter
		 * (true); // Y setIsValid (false); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_C_DocTypeCounter(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_DocTypeCounter */
	public static final String Table_Name = "C_DocTypeCounter";

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
		StringBuffer sb = new StringBuffer("X_C_DocTypeCounter[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Counter Document. Counter Document Relationship
	 */
	public void setC_DocTypeCounter_ID(int C_DocTypeCounter_ID) {
		if (C_DocTypeCounter_ID < 1)
			throw new IllegalArgumentException(
					"C_DocTypeCounter_ID is mandatory.");
		set_ValueNoCheck("C_DocTypeCounter_ID",
				new Integer(C_DocTypeCounter_ID));
	}

	/**
	 * Get Counter Document. Counter Document Relationship
	 */
	public int getC_DocTypeCounter_ID() {
		Integer ii = (Integer) get_Value("C_DocTypeCounter_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Document Type. Document type or rules
	 */
	public void setC_DocType_ID(int C_DocType_ID) {
		if (C_DocType_ID < 0)
			throw new IllegalArgumentException("C_DocType_ID is mandatory.");
		set_Value("C_DocType_ID", new Integer(C_DocType_ID));
	}

	/**
	 * Get Document Type. Document type or rules
	 */
	public int getC_DocType_ID() {
		Integer ii = (Integer) get_Value("C_DocType_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Counter_C_DocType_ID AD_Reference_ID=170 */
	public static final int COUNTER_C_DOCTYPE_ID_AD_Reference_ID = 170;

	/**
	 * Set Counter Document Type. Generated Counter Document Type (To)
	 */
	public void setCounter_C_DocType_ID(int Counter_C_DocType_ID) {
		if (Counter_C_DocType_ID < 1)
			throw new IllegalArgumentException(
					"Counter_C_DocType_ID is mandatory.");
		set_Value("Counter_C_DocType_ID", new Integer(Counter_C_DocType_ID));
	}

	/**
	 * Get Counter Document Type. Generated Counter Document Type (To)
	 */
	public int getCounter_C_DocType_ID() {
		Integer ii = (Integer) get_Value("Counter_C_DocType_ID");
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

	/** DocAction AD_Reference_ID=135 */
	public static final int DOCACTION_AD_Reference_ID = 135;

	/** <None> = -- */
	public static final String DOCACTION_None = "--";

	/** Approve = AP */
	public static final String DOCACTION_Approve = "AP";

	/** Close = CL */
	public static final String DOCACTION_Close = "CL";

	/** Complete = CO */
	public static final String DOCACTION_Complete = "CO";

	/** Invalidate = IN */
	public static final String DOCACTION_Invalidate = "IN";

	/** Post = PO */
	public static final String DOCACTION_Post = "PO";

	/** Prepare = PR */
	public static final String DOCACTION_Prepare = "PR";

	/** Reverse - Accrual = RA */
	public static final String DOCACTION_Reverse_Accrual = "RA";

	/** Reverse - Correct = RC */
	public static final String DOCACTION_Reverse_Correct = "RC";

	/** Re-activate = RE */
	public static final String DOCACTION_Re_Activate = "RE";

	/** Reject = RJ */
	public static final String DOCACTION_Reject = "RJ";

	/** Void = VO */
	public static final String DOCACTION_Void = "VO";

	/** Wait Complete = WC */
	public static final String DOCACTION_WaitComplete = "WC";

	/** Unlock = XL */
	public static final String DOCACTION_Unlock = "XL";

	/**
	 * Set Document Action. The targeted status of the document
	 */
	public void setDocAction(String DocAction) {
		if (DocAction != null && DocAction.length() > 2) {
			log.warning("Length > 2 - truncated");
			DocAction = DocAction.substring(0, 1);
		}
		set_Value("DocAction", DocAction);
	}

	/**
	 * Get Document Action. The targeted status of the document
	 */
	public String getDocAction() {
		return (String) get_Value("DocAction");
	}

	/**
	 * Set Create Counter Document. Create Counter Document
	 */
	public void setIsCreateCounter(boolean IsCreateCounter) {
		set_Value("IsCreateCounter", new Boolean(IsCreateCounter));
	}

	/**
	 * Get Create Counter Document. Create Counter Document
	 */
	public boolean isCreateCounter() {
		Object oo = get_Value("IsCreateCounter");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Valid. Element is valid
	 */
	public void setIsValid(boolean IsValid) {
		set_Value("IsValid", new Boolean(IsValid));
	}

	/**
	 * Get Valid. Element is valid
	 */
	public boolean isValid() {
		Object oo = get_Value("IsValid");
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
}
