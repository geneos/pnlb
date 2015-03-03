/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_ReportView
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.187
 */
public class X_AD_ReportView extends PO {
	/** Standard Constructor */
	public X_AD_ReportView(Properties ctx, int AD_ReportView_ID, String trxName) {
		super(ctx, AD_ReportView_ID, trxName);
		/**
		 * if (AD_ReportView_ID == 0) { setAD_ReportView_ID (0); setAD_Table_ID
		 * (0); setEntityType (null); // U setName (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_ReportView(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_ReportView */
	public static final String Table_Name = "AD_ReportView";

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

	protected BigDecimal accessLevel = new BigDecimal(4);

	/** AccessLevel 4 - System */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_AD_ReportView[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Report View. View used to generate this report
	 */
	public void setAD_ReportView_ID(int AD_ReportView_ID) {
		if (AD_ReportView_ID < 1)
			throw new IllegalArgumentException("AD_ReportView_ID is mandatory.");
		set_ValueNoCheck("AD_ReportView_ID", new Integer(AD_ReportView_ID));
	}

	/**
	 * Get Report View. View used to generate this report
	 */
	public int getAD_ReportView_ID() {
		Integer ii = (Integer) get_Value("AD_ReportView_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Table. Table for the Fields
	 */
	public void setAD_Table_ID(int AD_Table_ID) {
		if (AD_Table_ID < 1)
			throw new IllegalArgumentException("AD_Table_ID is mandatory.");
		set_Value("AD_Table_ID", new Integer(AD_Table_ID));
	}

	/**
	 * Get Table. Table for the Fields
	 */
	public int getAD_Table_ID() {
		Integer ii = (Integer) get_Value("AD_Table_ID");
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

	/** EntityType AD_Reference_ID=245 */
	public static final int ENTITYTYPE_AD_Reference_ID = 245;

	/** Applications = A */
	public static final String ENTITYTYPE_Applications = "A";

	/** Compiere = C */
	public static final String ENTITYTYPE_Compiere = "C";

	/** Customization = CUST */
	public static final String ENTITYTYPE_Customization = "CUST";

	/** Dictionary = D */
	public static final String ENTITYTYPE_Dictionary = "D";

	/** User maintained = U */
	public static final String ENTITYTYPE_UserMaintained = "U";

	/**
	 * Set Entity Type. Dictionary Entity Type; Determines ownership and
	 * synchronization
	 */
	public void setEntityType(String EntityType) {
		if (EntityType == null)
			throw new IllegalArgumentException("EntityType is mandatory");
		if (EntityType.length() > 4) {
			log.warning("Length > 4 - truncated");
			EntityType = EntityType.substring(0, 3);
		}
		set_Value("EntityType", EntityType);
	}

	/**
	 * Get Entity Type. Dictionary Entity Type; Determines ownership and
	 * synchronization
	 */
	public String getEntityType() {
		return (String) get_Value("EntityType");
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
	 * Set Sql ORDER BY. Fully qualified ORDER BY clause
	 */
	public void setOrderByClause(String OrderByClause) {
		if (OrderByClause != null && OrderByClause.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			OrderByClause = OrderByClause.substring(0, 1999);
		}
		set_Value("OrderByClause", OrderByClause);
	}

	/**
	 * Get Sql ORDER BY. Fully qualified ORDER BY clause
	 */
	public String getOrderByClause() {
		return (String) get_Value("OrderByClause");
	}

	/**
	 * Set Sql WHERE. Fully qualified SQL WHERE clause
	 */
	public void setWhereClause(String WhereClause) {
		if (WhereClause != null && WhereClause.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			WhereClause = WhereClause.substring(0, 1999);
		}
		set_Value("WhereClause", WhereClause);
	}

	/**
	 * Get Sql WHERE. Fully qualified SQL WHERE clause
	 */
	public String getWhereClause() {
		return (String) get_Value("WhereClause");
	}
}
