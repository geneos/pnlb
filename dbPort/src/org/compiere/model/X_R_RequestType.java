/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for R_RequestType
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.64
 */
public class X_R_RequestType extends PO {
	/** Standard Constructor */
	public X_R_RequestType(Properties ctx, int R_RequestType_ID, String trxName) {
		super(ctx, R_RequestType_ID, trxName);
		/**
		 * if (R_RequestType_ID == 0) { setConfidentialType (null); // C
		 * setDueDateTolerance (0); // 7 setIsAutoChangeRequest (false);
		 * setIsConfidentialInfo (false); // N setIsDefault (false); // N
		 * setIsEMailWhenDue (false); setIsEMailWhenOverdue (false);
		 * setIsSelfService (true); // Y setName (null); setR_RequestType_ID
		 * (0); }
		 */
	}

	/** Load Constructor */
	public X_R_RequestType(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=R_RequestType */
	public static final String Table_Name = "R_RequestType";

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
		StringBuffer sb = new StringBuffer("X_R_RequestType[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Auto Due Date Days. Automatic Due Date Days
	 */
	public void setAutoDueDateDays(int AutoDueDateDays) {
		set_Value("AutoDueDateDays", new Integer(AutoDueDateDays));
	}

	/**
	 * Get Auto Due Date Days. Automatic Due Date Days
	 */
	public int getAutoDueDateDays() {
		Integer ii = (Integer) get_Value("AutoDueDateDays");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** ConfidentialType AD_Reference_ID=340 */
	public static final int CONFIDENTIALTYPE_AD_Reference_ID = 340;

	/** Public Information = A */
	public static final String CONFIDENTIALTYPE_PublicInformation = "A";

	/** Customer Confidential = C */
	public static final String CONFIDENTIALTYPE_CustomerConfidential = "C";

	/** Internal = I */
	public static final String CONFIDENTIALTYPE_Internal = "I";

	/** Private Information = P */
	public static final String CONFIDENTIALTYPE_PrivateInformation = "P";

	/**
	 * Set Confidentiality. Type of Confidentiality
	 */
	public void setConfidentialType(String ConfidentialType) {
		if (ConfidentialType == null)
			throw new IllegalArgumentException("ConfidentialType is mandatory");
		if (ConfidentialType.equals("A") || ConfidentialType.equals("C")
				|| ConfidentialType.equals("I") || ConfidentialType.equals("P"))
			;
		else
			throw new IllegalArgumentException(
					"ConfidentialType Invalid value - " + ConfidentialType
							+ " - Reference_ID=340 - A - C - I - P");
		if (ConfidentialType.length() > 1) {
			log.warning("Length > 1 - truncated");
			ConfidentialType = ConfidentialType.substring(0, 0);
		}
		set_Value("ConfidentialType", ConfidentialType);
	}

	/**
	 * Get Confidentiality. Type of Confidentiality
	 */
	public String getConfidentialType() {
		return (String) get_Value("ConfidentialType");
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
	 * Set Due Date Tolerance. Tolerance in days between the Date Next Action
	 * and the date the request is regarded as overdue
	 */
	public void setDueDateTolerance(int DueDateTolerance) {
		set_Value("DueDateTolerance", new Integer(DueDateTolerance));
	}

	/**
	 * Get Due Date Tolerance. Tolerance in days between the Date Next Action
	 * and the date the request is regarded as overdue
	 */
	public int getDueDateTolerance() {
		Integer ii = (Integer) get_Value("DueDateTolerance");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Create Change Request. Automatically create BOM (Engineering) Change
	 * Request
	 */
	public void setIsAutoChangeRequest(boolean IsAutoChangeRequest) {
		set_Value("IsAutoChangeRequest", new Boolean(IsAutoChangeRequest));
	}

	/**
	 * Get Create Change Request. Automatically create BOM (Engineering) Change
	 * Request
	 */
	public boolean isAutoChangeRequest() {
		Object oo = get_Value("IsAutoChangeRequest");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Confidential Info. Can enter confidential information
	 */
	public void setIsConfidentialInfo(boolean IsConfidentialInfo) {
		set_Value("IsConfidentialInfo", new Boolean(IsConfidentialInfo));
	}

	/**
	 * Get Confidential Info. Can enter confidential information
	 */
	public boolean isConfidentialInfo() {
		Object oo = get_Value("IsConfidentialInfo");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Default. Default value
	 */
	public void setIsDefault(boolean IsDefault) {
		set_Value("IsDefault", new Boolean(IsDefault));
	}

	/**
	 * Get Default. Default value
	 */
	public boolean isDefault() {
		Object oo = get_Value("IsDefault");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set EMail when Due. Send EMail when Request becomes due
	 */
	public void setIsEMailWhenDue(boolean IsEMailWhenDue) {
		set_Value("IsEMailWhenDue", new Boolean(IsEMailWhenDue));
	}

	/**
	 * Get EMail when Due. Send EMail when Request becomes due
	 */
	public boolean isEMailWhenDue() {
		Object oo = get_Value("IsEMailWhenDue");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set EMail when Overdue. Send EMail when Request becomes overdue
	 */
	public void setIsEMailWhenOverdue(boolean IsEMailWhenOverdue) {
		set_Value("IsEMailWhenOverdue", new Boolean(IsEMailWhenOverdue));
	}

	/**
	 * Get EMail when Overdue. Send EMail when Request becomes overdue
	 */
	public boolean isEMailWhenOverdue() {
		Object oo = get_Value("IsEMailWhenOverdue");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Invoiced. Is this invoiced?
	 */
	public void setIsInvoiced(boolean IsInvoiced) {
		set_Value("IsInvoiced", new Boolean(IsInvoiced));
	}

	/**
	 * Get Invoiced. Is this invoiced?
	 */
	public boolean isInvoiced() {
		Object oo = get_Value("IsInvoiced");
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

	/**
	 * Set Request Type. Type of request (e.g. Inquiry, Complaint, ..)
	 */
	public void setR_RequestType_ID(int R_RequestType_ID) {
		if (R_RequestType_ID < 1)
			throw new IllegalArgumentException("R_RequestType_ID is mandatory.");
		set_ValueNoCheck("R_RequestType_ID", new Integer(R_RequestType_ID));
	}

	/**
	 * Get Request Type. Type of request (e.g. Inquiry, Complaint, ..)
	 */
	public int getR_RequestType_ID() {
		Integer ii = (Integer) get_Value("R_RequestType_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
