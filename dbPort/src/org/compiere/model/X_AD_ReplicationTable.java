/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_ReplicationTable
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.156
 */
public class X_AD_ReplicationTable extends PO {
	/** Standard Constructor */
	public X_AD_ReplicationTable(Properties ctx, int AD_ReplicationTable_ID,
			String trxName) {
		super(ctx, AD_ReplicationTable_ID, trxName);
		/**
		 * if (AD_ReplicationTable_ID == 0) { setAD_ReplicationStrategy_ID (0);
		 * setAD_ReplicationTable_ID (0); setAD_Table_ID (0); setEntityType
		 * (null); // U setReplicationType (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_ReplicationTable(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_ReplicationTable */
	public static final String Table_Name = "AD_ReplicationTable";

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
		StringBuffer sb = new StringBuffer("X_AD_ReplicationTable[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Replication Strategy. Data Replication Strategy
	 */
	public void setAD_ReplicationStrategy_ID(int AD_ReplicationStrategy_ID) {
		if (AD_ReplicationStrategy_ID < 1)
			throw new IllegalArgumentException(
					"AD_ReplicationStrategy_ID is mandatory.");
		set_ValueNoCheck("AD_ReplicationStrategy_ID", new Integer(
				AD_ReplicationStrategy_ID));
	}

	/**
	 * Get Replication Strategy. Data Replication Strategy
	 */
	public int getAD_ReplicationStrategy_ID() {
		Integer ii = (Integer) get_Value("AD_ReplicationStrategy_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getAD_ReplicationStrategy_ID()));
	}

	/**
	 * Set Replication Table. Data Replication Strategy Table Info
	 */
	public void setAD_ReplicationTable_ID(int AD_ReplicationTable_ID) {
		if (AD_ReplicationTable_ID < 1)
			throw new IllegalArgumentException(
					"AD_ReplicationTable_ID is mandatory.");
		set_ValueNoCheck("AD_ReplicationTable_ID", new Integer(
				AD_ReplicationTable_ID));
	}

	/**
	 * Get Replication Table. Data Replication Strategy Table Info
	 */
	public int getAD_ReplicationTable_ID() {
		Integer ii = (Integer) get_Value("AD_ReplicationTable_ID");
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
		set_ValueNoCheck("AD_Table_ID", new Integer(AD_Table_ID));
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

	/** ReplicationType AD_Reference_ID=126 */
	public static final int REPLICATIONTYPE_AD_Reference_ID = 126;

	/** Local = L */
	public static final String REPLICATIONTYPE_Local = "L";

	/** Merge = M */
	public static final String REPLICATIONTYPE_Merge = "M";

	/** Reference = R */
	public static final String REPLICATIONTYPE_Reference = "R";

	/**
	 * Set Replication Type. Type of Data Replication
	 */
	public void setReplicationType(String ReplicationType) {
		if (ReplicationType == null)
			throw new IllegalArgumentException("ReplicationType is mandatory");
		if (ReplicationType.equals("L") || ReplicationType.equals("M")
				|| ReplicationType.equals("R"))
			;
		else
			throw new IllegalArgumentException(
					"ReplicationType Invalid value - " + ReplicationType
							+ " - Reference_ID=126 - L - M - R");
		if (ReplicationType.length() > 1) {
			log.warning("Length > 1 - truncated");
			ReplicationType = ReplicationType.substring(0, 0);
		}
		set_Value("ReplicationType", ReplicationType);
	}

	/**
	 * Get Replication Type. Type of Data Replication
	 */
	public String getReplicationType() {
		return (String) get_Value("ReplicationType");
	}
}
