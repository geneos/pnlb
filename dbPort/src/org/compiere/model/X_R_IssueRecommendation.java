/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for R_IssueRecommendation
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.437
 */
public class X_R_IssueRecommendation extends PO {
	/** Standard Constructor */
	public X_R_IssueRecommendation(Properties ctx,
			int R_IssueRecommendation_ID, String trxName) {
		super(ctx, R_IssueRecommendation_ID, trxName);
		/**
		 * if (R_IssueRecommendation_ID == 0) { setName (null);
		 * setR_IssueRecommendation_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_R_IssueRecommendation(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=R_IssueRecommendation */
	public static final String Table_Name = "R_IssueRecommendation";

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
		StringBuffer sb = new StringBuffer("X_R_IssueRecommendation[").append(
				get_ID()).append("]");
		return sb.toString();
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
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name == null)
			throw new IllegalArgumentException("Name is mandatory.");
		if (Name.length() > 120) {
			log.warning("Length > 120 - truncated");
			Name = Name.substring(0, 119);
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
	 * Set Issue Recommendation. Recommendations how to fix an Issue
	 */
	public void setR_IssueRecommendation_ID(int R_IssueRecommendation_ID) {
		if (R_IssueRecommendation_ID < 1)
			throw new IllegalArgumentException(
					"R_IssueRecommendation_ID is mandatory.");
		set_ValueNoCheck("R_IssueRecommendation_ID", new Integer(
				R_IssueRecommendation_ID));
	}

	/**
	 * Get Issue Recommendation. Recommendations how to fix an Issue
	 */
	public int getR_IssueRecommendation_ID() {
		Integer ii = (Integer) get_Value("R_IssueRecommendation_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
