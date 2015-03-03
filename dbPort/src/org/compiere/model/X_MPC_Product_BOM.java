/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for MPC_Product_BOM
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.14
 */
public class X_MPC_Product_BOM extends PO {
	/** Standard Constructor */
	public X_MPC_Product_BOM(Properties ctx, int MPC_Product_BOM_ID,
			String trxName) {
		super(ctx, MPC_Product_BOM_ID, trxName);
		/**
		 * if (MPC_Product_BOM_ID == 0) { setMPC_Product_BOM_ID (0);
		 * setM_Product_ID (0); setName (null); setValidFrom (new
		 * Timestamp(System.currentTimeMillis())); setValue (null); }
		 */
	}

	/** Load Constructor */
	public X_MPC_Product_BOM(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=MPC_Product_BOM */
	public static final String Table_Name = "MPC_Product_BOM";

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
		StringBuffer sb = new StringBuffer("X_MPC_Product_BOM[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/** BOMType AD_Reference_ID=347 */
	public static final int BOMTYPE_AD_Reference_ID = 347;

	/** Current Active = A */
	public static final String BOMTYPE_CurrentActive = "A";

	/** Future = F */
	public static final String BOMTYPE_Future = "F";

	/** Maintenance = M */
	public static final String BOMTYPE_Maintenance = "M";

	/** Make-To-Order = O */
	public static final String BOMTYPE_Make_To_Order = "O";

	/** Previous = P */
	public static final String BOMTYPE_Previous = "P";

	/** Repair = R */
	public static final String BOMTYPE_Repair = "R";

	/** Previous, Spare = S */
	public static final String BOMTYPE_PreviousSpare = "S";

	/**
	 * Set BOM Type. Type of BOM
	 */
	public void setBOMType(String BOMType) {
		if (BOMType != null && BOMType.length() > 1) {
			log.warning("Length > 1 - truncated");
			BOMType = BOMType.substring(0, 0);
		}
		set_Value("BOMType", BOMType);
	}

	/**
	 * Get BOM Type. Type of BOM
	 */
	public String getBOMType() {
		return (String) get_Value("BOMType");
	}

	/** BOMUse AD_Reference_ID=348 */
	public static final int BOMUSE_AD_Reference_ID = 348;

	/** Master = A */
	public static final String BOMUSE_Master = "A";

	/** Engineering = E */
	public static final String BOMUSE_Engineering = "E";

	/** Manufacturing = M */
	public static final String BOMUSE_Manufacturing = "M";

	/** Planning = P */
	public static final String BOMUSE_Planning = "P";

	/** Quality = Q */
	public static final String BOMUSE_Quality = "Q";

	/**
	 * Set BOM Use. The use of the Bill of Material
	 */
	public void setBOMUse(String BOMUse) {
		if (BOMUse != null && BOMUse.length() > 1) {
			log.warning("Length > 1 - truncated");
			BOMUse = BOMUse.substring(0, 0);
		}
		set_Value("BOMUse", BOMUse);
	}

	/**
	 * Get BOM Use. The use of the Bill of Material
	 */
	public String getBOMUse() {
		return (String) get_Value("BOMUse");
	}

	/**
	 * Set UOM. Unit of Measure
	 */
	public void setC_UOM_ID(int C_UOM_ID) {
		if (C_UOM_ID <= 0)
			set_Value("C_UOM_ID", null);
		else
			set_Value("C_UOM_ID", new Integer(C_UOM_ID));
	}

	/**
	 * Get UOM. Unit of Measure
	 */
	public int getC_UOM_ID() {
		Integer ii = (Integer) get_Value("C_UOM_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Copy From. Copy From Record
	 */
	public void setCopyFrom(String CopyFrom) {
		if (CopyFrom != null && CopyFrom.length() > 1) {
			log.warning("Length > 1 - truncated");
			CopyFrom = CopyFrom.substring(0, 0);
		}
		set_Value("CopyFrom", CopyFrom);
	}

	/**
	 * Get Copy From. Copy From Record
	 */
	public String getCopyFrom() {
		return (String) get_Value("CopyFrom");
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
	 * Set Document No. Document sequence number of the document
	 */
	public void setDocumentNo(String DocumentNo) {
		if (DocumentNo != null && DocumentNo.length() > 22) {
			log.warning("Length > 22 - truncated");
			DocumentNo = DocumentNo.substring(0, 21);
		}
		set_Value("DocumentNo", DocumentNo);
	}

	/**
	 * Get Document No. Document sequence number of the document
	 */
	public String getDocumentNo() {
		return (String) get_Value("DocumentNo");
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

	/** Set BOM & Formula */
	public void setMPC_Product_BOM_ID(int MPC_Product_BOM_ID) {
		if (MPC_Product_BOM_ID < 1)
			throw new IllegalArgumentException(
					"MPC_Product_BOM_ID is mandatory.");
		set_ValueNoCheck("MPC_Product_BOM_ID", new Integer(MPC_Product_BOM_ID));
	}

	/** Get BOM & Formula */
	public int getMPC_Product_BOM_ID() {
		Integer ii = (Integer) get_Value("MPC_Product_BOM_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Attribute Set Instance. Product Attribute Set Instance
	 */
	public void setM_AttributeSetInstance_ID(int M_AttributeSetInstance_ID) {
		if (M_AttributeSetInstance_ID <= 0)
			set_Value("M_AttributeSetInstance_ID", null);
		else
			set_Value("M_AttributeSetInstance_ID", new Integer(
					M_AttributeSetInstance_ID));
	}

	/**
	 * Get Attribute Set Instance. Product Attribute Set Instance
	 */
	public int getM_AttributeSetInstance_ID() {
		Integer ii = (Integer) get_Value("M_AttributeSetInstance_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Change Notice. Bill of Materials (Engineering) Change Notice
	 * (Version)
	 */
	public void setM_ChangeNotice_ID(int M_ChangeNotice_ID) {
		if (M_ChangeNotice_ID <= 0)
			set_Value("M_ChangeNotice_ID", null);
		else
			set_Value("M_ChangeNotice_ID", new Integer(M_ChangeNotice_ID));
	}

	/**
	 * Get Change Notice. Bill of Materials (Engineering) Change Notice
	 * (Version)
	 */
	public int getM_ChangeNotice_ID() {
		Integer ii = (Integer) get_Value("M_ChangeNotice_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Product. Product, Service, Item
	 */
	public void setM_Product_ID(int M_Product_ID) {
		if (M_Product_ID < 1)
			throw new IllegalArgumentException("M_Product_ID is mandatory.");
		set_Value("M_Product_ID", new Integer(M_Product_ID));
	}

	/**
	 * Get Product. Product, Service, Item
	 */
	public int getM_Product_ID() {
		Integer ii = (Integer) get_Value("M_Product_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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

	/** Set Revision */
	public void setRevision(String Revision) {
		if (Revision != null && Revision.length() > 10) {
			log.warning("Length > 10 - truncated");
			Revision = Revision.substring(0, 9);
		}
		set_Value("Revision", Revision);
	}

	/** Get Revision */
	public String getRevision() {
		return (String) get_Value("Revision");
	}

	/**
	 * Set Valid from. Valid from including this date (first day)
	 */
	public void setValidFrom(Timestamp ValidFrom) {
		if (ValidFrom == null)
			throw new IllegalArgumentException("ValidFrom is mandatory.");
		set_Value("ValidFrom", ValidFrom);
	}

	/**
	 * Get Valid from. Valid from including this date (first day)
	 */
	public Timestamp getValidFrom() {
		return (Timestamp) get_Value("ValidFrom");
	}

	/**
	 * Set Valid to. Valid to including this date (last day)
	 */
	public void setValidTo(Timestamp ValidTo) {
		set_Value("ValidTo", ValidTo);
	}

	/**
	 * Get Valid to. Valid to including this date (last day)
	 */
	public Timestamp getValidTo() {
		return (Timestamp) get_Value("ValidTo");
	}

	/**
	 * Set Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public void setValue(String Value) {
		if (Value == null)
			throw new IllegalArgumentException("Value is mandatory.");
		if (Value.length() > 80) {
			log.warning("Length > 80 - truncated");
			Value = Value.substring(0, 79);
		}
		set_Value("Value", Value);
	}

	/**
	 * Get Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public String getValue() {
		return (String) get_Value("Value");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getValue());
	}
}
