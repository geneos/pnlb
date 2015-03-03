/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for R_StandardResponse
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.687
 */
public class X_R_StandardResponse extends PO {
	/** Standard Constructor */
	public X_R_StandardResponse(Properties ctx, int R_StandardResponse_ID,
			String trxName) {
		super(ctx, R_StandardResponse_ID, trxName);
		/**
		 * if (R_StandardResponse_ID == 0) { setName (null);
		 * setR_StandardResponse_ID (0); setResponseText (null); }
		 */
	}

	/** Load Constructor */
	public X_R_StandardResponse(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=R_StandardResponse */
	public static final String Table_Name = "R_StandardResponse";

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
		StringBuffer sb = new StringBuffer("X_R_StandardResponse[").append(
				get_ID()).append("]");
		return sb.toString();
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
	 * Set Standard Response. Request Standard Response
	 */
	public void setR_StandardResponse_ID(int R_StandardResponse_ID) {
		if (R_StandardResponse_ID < 1)
			throw new IllegalArgumentException(
					"R_StandardResponse_ID is mandatory.");
		set_ValueNoCheck("R_StandardResponse_ID", new Integer(
				R_StandardResponse_ID));
	}

	/**
	 * Get Standard Response. Request Standard Response
	 */
	public int getR_StandardResponse_ID() {
		Integer ii = (Integer) get_Value("R_StandardResponse_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Response Text. Request Response Text
	 */
	public void setResponseText(String ResponseText) {
		if (ResponseText == null)
			throw new IllegalArgumentException("ResponseText is mandatory.");
		if (ResponseText.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			ResponseText = ResponseText.substring(0, 1999);
		}
		set_Value("ResponseText", ResponseText);
	}

	/**
	 * Get Response Text. Request Response Text
	 */
	public String getResponseText() {
		return (String) get_Value("ResponseText");
	}
}
