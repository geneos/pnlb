/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for RV_MPC_Product_BOMLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.2d - 2005-06-09 21:09:42.923
 */
public class X_RV_MPC_Product_BOMLine extends PO {
	/** Standard Constructor */
	public X_RV_MPC_Product_BOMLine(Properties ctx,
			int RV_MPC_Product_BOMLine_ID, String trxName) {
		super(ctx, RV_MPC_Product_BOMLine_ID, trxName);
		/**
		 * if (RV_MPC_Product_BOMLine_ID == 0) { setLine (0); setQtyBatch
		 * (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_RV_MPC_Product_BOMLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=RV_MPC_Product_BOMLine */
	public static final String Table_Name = "RV_MPC_Product_BOMLine";

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

	protected static BigDecimal accessLevel = new BigDecimal(7);

	/** AccessLevel 7 - System - Client - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_RV_MPC_Product_BOMLine[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Process Instance. Instance of the process
	 */
	public void setAD_PInstance_ID(int AD_PInstance_ID) {
		if (AD_PInstance_ID <= 0)
			set_Value("AD_PInstance_ID", null);
		else
			set_Value("AD_PInstance_ID", new Integer(AD_PInstance_ID));
	}

	/**
	 * Get Process Instance. Instance of the process
	 */
	public int getAD_PInstance_ID() {
		Integer ii = (Integer) get_Value("AD_PInstance_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
		if (ComponentType == null || ComponentType.equals("BY")
				|| ComponentType.equals("CO") || ComponentType.equals("PH")
				|| ComponentType.equals("PK") || ComponentType.equals("PL")
				|| ComponentType.equals("TL"))
			;
		else
			throw new IllegalArgumentException(
					"ComponentType Invalid value - Reference_ID=1000037 - BY - CO - PH - PK - PL - TL");
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
		if (Description != null && Description.length() > 510) {
			log.warning("Length > 510 - truncated");
			Description = Description.substring(0, 509);
		}
		set_Value("Description", Description);
	}

	/**
	 * Get Description. Optional short description of the record
	 */
	public String getDescription() {
		return (String) get_Value("Description");
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

	public static final int ISSUEMETHOD_AD_Reference_ID = 1000002;

	/** Issue = 0 */
	public static final String ISSUEMETHOD_Issue = "0";

	/** BackFlush = 1 */
	public static final String ISSUEMETHOD_BackFlush = "1";

	/** Set IssueMethod */
	public void setIssueMethod(String IssueMethod) {
		if (IssueMethod == null || IssueMethod.equals("0")
				|| IssueMethod.equals("1"))
			;
		else
			throw new IllegalArgumentException(
					"IssueMethod Invalid value - Reference_ID=1000002 - 0 - 1");
		if (IssueMethod != null && IssueMethod.length() > 1) {
			log.warning("Length > 1 - truncated");
			IssueMethod = IssueMethod.substring(0, 0);
		}
		set_Value("IssueMethod", IssueMethod);
	}

	/** Get IssueMethod */
	public String getIssueMethod() {
		return (String) get_Value("IssueMethod");
	}

	/** Set Level no */
	public void setLevelNo(int LevelNo) {
		set_Value("LevelNo", new Integer(LevelNo));
	}

	/** Get Level no */
	public int getLevelNo() {
		Integer ii = (Integer) get_Value("LevelNo");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Levels */
	public void setLevels(String Levels) {
		if (Levels != null && Levels.length() > 250) {
			log.warning("Length > 250 - truncated");
			Levels = Levels.substring(0, 249);
		}
		set_Value("Levels", Levels);
	}

	/** Get Levels */
	public String getLevels() {
		return (String) get_Value("Levels");
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
		if (MPC_Product_BOMLine_ID <= 0)
			set_Value("MPC_Product_BOMLine_ID", null);
		else
			set_Value("MPC_Product_BOMLine_ID", new Integer(
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
		if (MPC_Product_BOM_ID <= 0)
			set_Value("MPC_Product_BOM_ID", null);
		else
			set_Value("MPC_Product_BOM_ID", new Integer(MPC_Product_BOM_ID));
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
	 * Set Product. Product, Service, Item
	 */
	public void setM_Product_ID(int M_Product_ID) {
		if (M_Product_ID <= 0)
			set_Value("M_Product_ID", null);
		else
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
		if (QtyBatch == null)
			throw new IllegalArgumentException("QtyBatch is mandatory");
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

	/**
	 * Set Valid from. Valid from including this date (first day)
	 */
	public void setValidFrom(Timestamp ValidFrom) {
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
