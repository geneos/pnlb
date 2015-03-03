/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_ProjectType
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.312
 */
public class X_C_ProjectType extends PO {
	/** Standard Constructor */
	public X_C_ProjectType(Properties ctx, int C_ProjectType_ID, String trxName) {
		super(ctx, C_ProjectType_ID, trxName);
		/**
		 * if (C_ProjectType_ID == 0) { setC_ProjectType_ID (0); setName (null);
		 * setProjectCategory (null); // N }
		 */
	}

	/** Load Constructor */
	public X_C_ProjectType(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_ProjectType */
	public static final String Table_Name = "C_ProjectType";

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

	protected BigDecimal accessLevel = new BigDecimal(3);

	/** AccessLevel 3 - Client - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_C_ProjectType[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Project Type. Type of the project
	 */
	public void setC_ProjectType_ID(int C_ProjectType_ID) {
		if (C_ProjectType_ID < 1)
			throw new IllegalArgumentException("C_ProjectType_ID is mandatory.");
		set_ValueNoCheck("C_ProjectType_ID", new Integer(C_ProjectType_ID));
	}

	/**
	 * Get Project Type. Type of the project
	 */
	public int getC_ProjectType_ID() {
		Integer ii = (Integer) get_Value("C_ProjectType_ID");
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

	/** ProjectCategory AD_Reference_ID=288 */
	public static final int PROJECTCATEGORY_AD_Reference_ID = 288;

	/** Asset Project = A */
	public static final String PROJECTCATEGORY_AssetProject = "A";

	/** General = N */
	public static final String PROJECTCATEGORY_General = "N";

	/** Service (Charge) Project = S */
	public static final String PROJECTCATEGORY_ServiceChargeProject = "S";

	/** Work Order (Job) = W */
	public static final String PROJECTCATEGORY_WorkOrderJob = "W";

	/**
	 * Set Project Category. Project Category
	 */
	public void setProjectCategory(String ProjectCategory) {
		if (ProjectCategory == null)
			throw new IllegalArgumentException("ProjectCategory is mandatory");
		if (ProjectCategory.equals("A") || ProjectCategory.equals("N")
				|| ProjectCategory.equals("S") || ProjectCategory.equals("W"))
			;
		else
			throw new IllegalArgumentException(
					"ProjectCategory Invalid value - " + ProjectCategory
							+ " - Reference_ID=288 - A - N - S - W");
		if (ProjectCategory.length() > 1) {
			log.warning("Length > 1 - truncated");
			ProjectCategory = ProjectCategory.substring(0, 0);
		}
		set_ValueNoCheck("ProjectCategory", ProjectCategory);
	}

	/**
	 * Get Project Category. Project Category
	 */
	public String getProjectCategory() {
		return (String) get_Value("ProjectCategory");
	}
}
