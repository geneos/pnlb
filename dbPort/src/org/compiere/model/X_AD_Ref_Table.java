/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Ref_Table
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.062
 */
public class X_AD_Ref_Table extends PO {
	/** Standard Constructor */
	public X_AD_Ref_Table(Properties ctx, int AD_Ref_Table_ID, String trxName) {
		super(ctx, AD_Ref_Table_ID, trxName);
		/**
		 * if (AD_Ref_Table_ID == 0) { setAD_Display (0); setAD_Key (0);
		 * setAD_Reference_ID (0); setAD_Table_ID (0); setEntityType (null); //
		 * U setIsValueDisplayed (false); }
		 */
	}

	/** Load Constructor */
	public X_AD_Ref_Table(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Ref_Table */
	public static final String Table_Name = "AD_Ref_Table";

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
		StringBuffer sb = new StringBuffer("X_AD_Ref_Table[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** AD_Display AD_Reference_ID=3 */
	public static final int AD_DISPLAY_AD_Reference_ID = 3;

	/**
	 * Set Display column. Column that will display
	 */
	public void setAD_Display(int AD_Display) {
		set_Value("AD_Display", new Integer(AD_Display));
	}

	/**
	 * Get Display column. Column that will display
	 */
	public int getAD_Display() {
		Integer ii = (Integer) get_Value("AD_Display");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_Key AD_Reference_ID=3 */
	public static final int AD_KEY_AD_Reference_ID = 3;

	/**
	 * Set Key column. Unique identifier of a record
	 */
	public void setAD_Key(int AD_Key) {
		set_Value("AD_Key", new Integer(AD_Key));
	}

	/**
	 * Get Key column. Unique identifier of a record
	 */
	public int getAD_Key() {
		Integer ii = (Integer) get_Value("AD_Key");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Reference. System Reference (Pick List)
	 */
	public void setAD_Reference_ID(int AD_Reference_ID) {
		if (AD_Reference_ID < 1)
			throw new IllegalArgumentException("AD_Reference_ID is mandatory.");
		set_ValueNoCheck("AD_Reference_ID", new Integer(AD_Reference_ID));
	}

	/**
	 * Get Reference. System Reference (Pick List)
	 */
	public int getAD_Reference_ID() {
		Integer ii = (Integer) get_Value("AD_Reference_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getAD_Reference_ID()));
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
	 * Set Display Value. Displays Value column with the Display column
	 */
	public void setIsValueDisplayed(boolean IsValueDisplayed) {
		set_Value("IsValueDisplayed", new Boolean(IsValueDisplayed));
	}

	/**
	 * Get Display Value. Displays Value column with the Display column
	 */
	public boolean isValueDisplayed() {
		Object oo = get_Value("IsValueDisplayed");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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