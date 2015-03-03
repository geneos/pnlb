/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for R_RequestProcessor_Route
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.625
 */
public class X_R_RequestProcessor_Route extends PO {
	/** Standard Constructor */
	public X_R_RequestProcessor_Route(Properties ctx,
			int R_RequestProcessor_Route_ID, String trxName) {
		super(ctx, R_RequestProcessor_Route_ID, trxName);
		/**
		 * if (R_RequestProcessor_Route_ID == 0) { setAD_User_ID (0);
		 * 
		 * setR_RequestProcessor_ID (0); setR_RequestProcessor_Route_ID (0);
		 * setSeqNo (0); }
		 */
	}

	/** Load Constructor */
	public X_R_RequestProcessor_Route(Properties ctx, ResultSet rs,
			String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=R_RequestProcessor_Route */
	public static final String Table_Name = "R_RequestProcessor_Route";

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
		StringBuffer sb = new StringBuffer("X_R_RequestProcessor_Route[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public void setAD_User_ID(int AD_User_ID) {
		if (AD_User_ID < 1)
			throw new IllegalArgumentException("AD_User_ID is mandatory.");
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
	 * Set Keyword. Case insensitive keyword
	 */
	public void setKeyword(String Keyword) {
		if (Keyword != null && Keyword.length() > 60) {
			log.warning("Length > 60 - truncated");
			Keyword = Keyword.substring(0, 59);
		}
		set_Value("Keyword", Keyword);
	}

	/**
	 * Get Keyword. Case insensitive keyword
	 */
	public String getKeyword() {
		return (String) get_Value("Keyword");
	}

	/**
	 * Set Request Processor. Processor for Requests
	 */
	public void setR_RequestProcessor_ID(int R_RequestProcessor_ID) {
		if (R_RequestProcessor_ID < 1)
			throw new IllegalArgumentException(
					"R_RequestProcessor_ID is mandatory.");
		set_ValueNoCheck("R_RequestProcessor_ID", new Integer(
				R_RequestProcessor_ID));
	}

	/**
	 * Get Request Processor. Processor for Requests
	 */
	public int getR_RequestProcessor_ID() {
		Integer ii = (Integer) get_Value("R_RequestProcessor_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Request Routing. Automatic routing of requests
	 */
	public void setR_RequestProcessor_Route_ID(int R_RequestProcessor_Route_ID) {
		if (R_RequestProcessor_Route_ID < 1)
			throw new IllegalArgumentException(
					"R_RequestProcessor_Route_ID is mandatory.");
		set_ValueNoCheck("R_RequestProcessor_Route_ID", new Integer(
				R_RequestProcessor_Route_ID));
	}

	/**
	 * Get Request Routing. Automatic routing of requests
	 */
	public int getR_RequestProcessor_Route_ID() {
		Integer ii = (Integer) get_Value("R_RequestProcessor_Route_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Request Type. Type of request (e.g. Inquiry, Complaint, ..)
	 */
	public void setR_RequestType_ID(int R_RequestType_ID) {
		if (R_RequestType_ID <= 0)
			set_Value("R_RequestType_ID", null);
		else
			set_Value("R_RequestType_ID", new Integer(R_RequestType_ID));
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

	/**
	 * Set Sequence. Method of ordering records; lowest number comes first
	 */
	public void setSeqNo(int SeqNo) {
		set_Value("SeqNo", new Integer(SeqNo));
	}

	/**
	 * Get Sequence. Method of ordering records; lowest number comes first
	 */
	public int getSeqNo() {
		Integer ii = (Integer) get_Value("SeqNo");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getSeqNo()));
	}
}
