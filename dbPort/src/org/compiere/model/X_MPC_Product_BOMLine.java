/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for MPC_Product_BOMLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.156
 */
public class X_MPC_Product_BOMLine extends PO {
	/** Standard Constructor */
	public X_MPC_Product_BOMLine(Properties ctx, int MPC_Product_BOMLine_ID,
			String trxName) {
		super(ctx, MPC_Product_BOMLine_ID, trxName);
		/**
		 * if (MPC_Product_BOMLine_ID == 0) { setIssueMethod (null); setLine
		 * (0); //
		 * 
		 * @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM
		 *             MPC_Product_BOMLine WHERE
		 *             MPC_Product_BOMLine_ID=@MPC_Product_BOMLine_ID@
		 *             setMPC_Product_BOMLine_ID (0); setMPC_Product_BOM_ID (0);
		 *             setM_Product_ID (0); setValidFrom (new
		 *             Timestamp(System.currentTimeMillis())); //
		 * @#Date@ }
		 */
	}

	/** Load Constructor */
	public X_MPC_Product_BOMLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=MPC_Product_BOMLine */
	public static final String Table_Name = "MPC_Product_BOMLine";

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
		StringBuffer sb = new StringBuffer("X_MPC_Product_BOMLine[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/** Set Assay */
	public void setAssay(BigDecimal Assay) {
		set_Value("Assay", Assay);
	}

	/** Get Assay */
	public BigDecimal getAssay() {
		BigDecimal bd = (BigDecimal) get_Value("Assay");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set BackflushGroup */
	public void setBackflushGroup(String BackflushGroup) {
		if (BackflushGroup != null && BackflushGroup.length() > 20) {
			log.warning("Length > 20 - truncated");
			BackflushGroup = BackflushGroup.substring(0, 19);
		}
		set_Value("BackflushGroup", BackflushGroup);
	}

	/** Get BackflushGroup */
	public String getBackflushGroup() {
		return (String) get_Value("BackflushGroup");
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

	/** ComponentType AD_Reference_ID=1000037 */
	public static final int COMPONENTTYPE_AD_Reference_ID = 1000037;

	/** By Product = BY */
	public static final String COMPONENTTYPE_ByProduct = "BY";

	/** Component = CO */
	public static final String COMPONENTTYPE_Component = "CO";

	/** Phantom = PH */
	public static final String COMPONENTTYPE_Phantom = "PH";

	/** Packing = PK */
	public static final String COMPONENTTYPE_Packing = "PK";

	/** Planning = PL */
	public static final String COMPONENTTYPE_Planning = "PL";

	/** Tools = TL */
	public static final String COMPONENTTYPE_Tools = "TL";

	/** Set Component Type */
	public void setComponentType(String ComponentType) {
		if (ComponentType != null && ComponentType.length() > 2) {
			log.warning("Length > 2 - truncated");
			ComponentType = ComponentType.substring(0, 1);
		}
		set_Value("ComponentType", ComponentType);
	}

	/** Get Component Type */
	public String getComponentType() {
		return (String) get_Value("ComponentType");
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

	/** Set Forecast */
	public void setForecast(BigDecimal Forecast) {
		set_Value("Forecast", Forecast);
	}

	/** Get Forecast */
	public BigDecimal getForecast() {
		BigDecimal bd = (BigDecimal) get_Value("Forecast");
		if (bd == null)
			return Env.ZERO;
		return bd;
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

	/** Set IsCritical */
	public void setIsCritical(boolean IsCritical) {
		set_Value("IsCritical", new Boolean(IsCritical));
	}

	/** Get IsCritical */
	public boolean isCritical() {
		Object oo = get_Value("IsCritical");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsQtyPercentage */
	public void setIsQtyPercentage(boolean IsQtyPercentage) {
		set_Value("IsQtyPercentage", new Boolean(IsQtyPercentage));
	}

	/** Get IsQtyPercentage */
	public boolean isQtyPercentage() {
		Object oo = get_Value("IsQtyPercentage");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** IssueMethod AD_Reference_ID=1000002 */
	public static final int ISSUEMETHOD_AD_Reference_ID = 1000002;

	/** Issue = 0 */
	public static final String ISSUEMETHOD_Issue = "0";

	/** BackFlush = 1 */
	public static final String ISSUEMETHOD_BackFlush = "1";

	/** Set IssueMethod */
	public void setIssueMethod(String IssueMethod) {
		if (IssueMethod == null)
			throw new IllegalArgumentException("IssueMethod is mandatory");
		if (IssueMethod.equals("0") || IssueMethod.equals("1"))
			;
		else
			throw new IllegalArgumentException("IssueMethod Invalid value - "
					+ IssueMethod + " - Reference_ID=1000002 - 0 - 1");
		if (IssueMethod.length() > 1) {
			log.warning("Length > 1 - truncated");
			IssueMethod = IssueMethod.substring(0, 0);
		}
		set_Value("IssueMethod", IssueMethod);
	}

	/** Get IssueMethod */
	public String getIssueMethod() {
		return (String) get_Value("IssueMethod");
	}

	/**
	 * Set Lead Time Offset. Optional Lead Time offest before starting
	 * production
	 */
	public void setLeadTimeOffset(int LeadTimeOffset) {
		set_Value("LeadTimeOffset", new Integer(LeadTimeOffset));
	}

	/**
	 * Get Lead Time Offset. Optional Lead Time offest before starting
	 * production
	 */
	public int getLeadTimeOffset() {
		Integer ii = (Integer) get_Value("LeadTimeOffset");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Line No. Unique line for this document
	 */
	public void setLine(int Line) {
		set_Value("Line", new Integer(Line));
	}

	/**
	 * Get Line No. Unique line for this document
	 */
	public int getLine() {
		Integer ii = (Integer) get_Value("Line");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set MPC_Product_BOMLine_ID */
	public void setMPC_Product_BOMLine_ID(int MPC_Product_BOMLine_ID) {
		if (MPC_Product_BOMLine_ID < 1)
			throw new IllegalArgumentException(
					"MPC_Product_BOMLine_ID is mandatory.");
		set_ValueNoCheck("MPC_Product_BOMLine_ID", new Integer(
				MPC_Product_BOMLine_ID));
	}

	/** Get MPC_Product_BOMLine_ID */
	public int getMPC_Product_BOMLine_ID() {
		Integer ii = (Integer) get_Value("MPC_Product_BOMLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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

	/** M_Product_ID AD_Reference_ID=171 */
	public static final int M_PRODUCT_ID_AD_Reference_ID = 171;

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
	 * Set Qty. Bill of Materials Quantity
	 */
	public void setQtyBOM(BigDecimal QtyBOM) {
		set_Value("QtyBOM", QtyBOM);
	}

	/**
	 * Get Qty. Bill of Materials Quantity
	 */
	public BigDecimal getQtyBOM() {
		BigDecimal bd = (BigDecimal) get_Value("QtyBOM");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Qty % */
	public void setQtyBatch(BigDecimal QtyBatch) {
		set_Value("QtyBatch", QtyBatch);
	}

	/** Get Qty % */
	public BigDecimal getQtyBatch() {
		BigDecimal bd = (BigDecimal) get_Value("QtyBatch");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Scrap */
	public void setScrap(BigDecimal Scrap) {
		set_Value("Scrap", Scrap);
	}

	/** Get Scrap */
	public BigDecimal getScrap() {
		BigDecimal bd = (BigDecimal) get_Value("Scrap");
		if (bd == null)
			return Env.ZERO;
		return bd;
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
}
