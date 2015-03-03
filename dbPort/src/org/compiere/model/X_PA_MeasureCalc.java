/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for PA_MeasureCalc
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.953
 */
public class X_PA_MeasureCalc extends PO {
	/** Standard Constructor */
	public X_PA_MeasureCalc(Properties ctx, int PA_MeasureCalc_ID,
			String trxName) {
		super(ctx, PA_MeasureCalc_ID, trxName);
		/**
		 * if (PA_MeasureCalc_ID == 0) { setAD_Table_ID (0); setDateColumn
		 * (null); // x.Date setEntityType (null); // U setKeyColumn (null);
		 * setName (null); setOrgColumn (null); // x.AD_Org_ID
		 * setPA_MeasureCalc_ID (0); setSelectClause (null); // SELECT ... FROM
		 * ... setWhereClause (null); // WHERE ... }
		 */
	}

	/** Load Constructor */
	public X_PA_MeasureCalc(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=PA_MeasureCalc */
	public static final String Table_Name = "PA_MeasureCalc";

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
		StringBuffer sb = new StringBuffer("X_PA_MeasureCalc[")
				.append(get_ID()).append("]");
		return sb.toString();
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
	 * Set B.Partner Column. Fully qualified Business Partner key column
	 * (C_BPartner_ID)
	 */
	public void setBPartnerColumn(String BPartnerColumn) {
		if (BPartnerColumn != null && BPartnerColumn.length() > 60) {
			log.warning("Length > 60 - truncated");
			BPartnerColumn = BPartnerColumn.substring(0, 59);
		}
		set_Value("BPartnerColumn", BPartnerColumn);
	}

	/**
	 * Get B.Partner Column. Fully qualified Business Partner key column
	 * (C_BPartner_ID)
	 */
	public String getBPartnerColumn() {
		return (String) get_Value("BPartnerColumn");
	}

	/**
	 * Set Date Column. Fully qualified date column
	 */
	public void setDateColumn(String DateColumn) {
		if (DateColumn == null)
			throw new IllegalArgumentException("DateColumn is mandatory.");
		if (DateColumn.length() > 60) {
			log.warning("Length > 60 - truncated");
			DateColumn = DateColumn.substring(0, 59);
		}
		set_Value("DateColumn", DateColumn);
	}

	/**
	 * Get Date Column. Fully qualified date column
	 */
	public String getDateColumn() {
		return (String) get_Value("DateColumn");
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
	 * Set Key Column. Key Column for Table
	 */
	public void setKeyColumn(String KeyColumn) {
		if (KeyColumn == null)
			throw new IllegalArgumentException("KeyColumn is mandatory.");
		if (KeyColumn.length() > 60) {
			log.warning("Length > 60 - truncated");
			KeyColumn = KeyColumn.substring(0, 59);
		}
		set_Value("KeyColumn", KeyColumn);
	}

	/**
	 * Get Key Column. Key Column for Table
	 */
	public String getKeyColumn() {
		return (String) get_Value("KeyColumn");
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
	 * Set Org Column. Fully qualified Organization column (AD_Org_ID)
	 */
	public void setOrgColumn(String OrgColumn) {
		if (OrgColumn == null)
			throw new IllegalArgumentException("OrgColumn is mandatory.");
		if (OrgColumn.length() > 60) {
			log.warning("Length > 60 - truncated");
			OrgColumn = OrgColumn.substring(0, 59);
		}
		set_Value("OrgColumn", OrgColumn);
	}

	/**
	 * Get Org Column. Fully qualified Organization column (AD_Org_ID)
	 */
	public String getOrgColumn() {
		return (String) get_Value("OrgColumn");
	}

	/**
	 * Set Measure Calculation. Calculation method for measuring performance
	 */
	public void setPA_MeasureCalc_ID(int PA_MeasureCalc_ID) {
		if (PA_MeasureCalc_ID < 1)
			throw new IllegalArgumentException(
					"PA_MeasureCalc_ID is mandatory.");
		set_ValueNoCheck("PA_MeasureCalc_ID", new Integer(PA_MeasureCalc_ID));
	}

	/**
	 * Get Measure Calculation. Calculation method for measuring performance
	 */
	public int getPA_MeasureCalc_ID() {
		Integer ii = (Integer) get_Value("PA_MeasureCalc_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Product Column. Fully qualified Product column (M_Product_ID)
	 */
	public void setProductColumn(String ProductColumn) {
		if (ProductColumn != null && ProductColumn.length() > 60) {
			log.warning("Length > 60 - truncated");
			ProductColumn = ProductColumn.substring(0, 59);
		}
		set_Value("ProductColumn", ProductColumn);
	}

	/**
	 * Get Product Column. Fully qualified Product column (M_Product_ID)
	 */
	public String getProductColumn() {
		return (String) get_Value("ProductColumn");
	}

	/**
	 * Set Sql SELECT. SQL SELECT clause
	 */
	public void setSelectClause(String SelectClause) {
		if (SelectClause == null)
			throw new IllegalArgumentException("SelectClause is mandatory.");
		if (SelectClause.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			SelectClause = SelectClause.substring(0, 1999);
		}
		set_Value("SelectClause", SelectClause);
	}

	/**
	 * Get Sql SELECT. SQL SELECT clause
	 */
	public String getSelectClause() {
		return (String) get_Value("SelectClause");
	}

	/**
	 * Set Sql WHERE. Fully qualified SQL WHERE clause
	 */
	public void setWhereClause(String WhereClause) {
		if (WhereClause == null)
			throw new IllegalArgumentException("WhereClause is mandatory.");
		if (WhereClause.length() > 2000) {
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
