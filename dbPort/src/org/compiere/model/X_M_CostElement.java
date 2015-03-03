/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_CostElement
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.546
 */
public class X_M_CostElement extends PO {
	/** Standard Constructor */
	public X_M_CostElement(Properties ctx, int M_CostElement_ID, String trxName) {
		super(ctx, M_CostElement_ID, trxName);
		/**
		 * if (M_CostElement_ID == 0) { setCostElementType (null);
		 * setIsCalculated (false); setM_CostElement_ID (0); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_M_CostElement(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_CostElement */
	public static final String Table_Name = "M_CostElement";

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

	protected BigDecimal accessLevel = new BigDecimal(2);

	/** AccessLevel 2 - Client */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_M_CostElement[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** CostElementType AD_Reference_ID=338 */
	public static final int COSTELEMENTTYPE_AD_Reference_ID = 338;

	/** Burden (M.Overhead) = B */
	public static final String COSTELEMENTTYPE_BurdenMOverhead = "B";

	/** Material = M */
	public static final String COSTELEMENTTYPE_Material = "M";

	/** Overhead = O */
	public static final String COSTELEMENTTYPE_Overhead = "O";

	/** Resource = R */
	public static final String COSTELEMENTTYPE_Resource = "R";

	/** Outside Processing = X */
	public static final String COSTELEMENTTYPE_OutsideProcessing = "X";

	/**
	 * Set Cost Element Type. Type of Cost Element
	 */
	public void setCostElementType(String CostElementType) {
		if (CostElementType == null)
			throw new IllegalArgumentException("CostElementType is mandatory");
		if (CostElementType.equals("B") || CostElementType.equals("M")
				|| CostElementType.equals("O") || CostElementType.equals("R")
				|| CostElementType.equals("X"))
			;
		else
			throw new IllegalArgumentException(
					"CostElementType Invalid value - " + CostElementType
							+ " - Reference_ID=338 - B - M - O - R - X");
		if (CostElementType.length() > 1) {
			log.warning("Length > 1 - truncated");
			CostElementType = CostElementType.substring(0, 0);
		}
		set_Value("CostElementType", CostElementType);
	}

	/**
	 * Get Cost Element Type. Type of Cost Element
	 */
	public String getCostElementType() {
		return (String) get_Value("CostElementType");
	}

	/** CostingMethod AD_Reference_ID=122 */
	public static final int COSTINGMETHOD_AD_Reference_ID = 122;

	/** Average PO = A */
	public static final String COSTINGMETHOD_AveragePO = "A";

	/** Fifo = F */
	public static final String COSTINGMETHOD_Fifo = "F";

	/** Average Invoice = I */
	public static final String COSTINGMETHOD_AverageInvoice = "I";

	/** Lifo = L */
	public static final String COSTINGMETHOD_Lifo = "L";

	/** Standard Costing = S */
	public static final String COSTINGMETHOD_StandardCosting = "S";

	/** User Defined = U */
	public static final String COSTINGMETHOD_UserDefined = "U";

	/** Last Invoice = i */
	public static final String COSTINGMETHOD_LastInvoice = "i";

	/** Last PO Price = p */
	public static final String COSTINGMETHOD_LastPOPrice = "p";

	/** _ = x */
	public static final String COSTINGMETHOD__ = "x";

	/**
	 * Set Costing Method. Indicates how Costs will be calculated
	 */
	public void setCostingMethod(String CostingMethod) {
		if (CostingMethod != null && CostingMethod.length() > 1) {
			log.warning("Length > 1 - truncated");
			CostingMethod = CostingMethod.substring(0, 0);
		}
		set_Value("CostingMethod", CostingMethod);
	}

	/**
	 * Get Costing Method. Indicates how Costs will be calculated
	 */
	public String getCostingMethod() {
		return (String) get_Value("CostingMethod");
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
	 * Set Calculated. The value is calculated by the system
	 */
	public void setIsCalculated(boolean IsCalculated) {
		set_Value("IsCalculated", new Boolean(IsCalculated));
	}

	/**
	 * Get Calculated. The value is calculated by the system
	 */
	public boolean isCalculated() {
		Object oo = get_Value("IsCalculated");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Cost Element. Product Cost Element
	 */
	public void setM_CostElement_ID(int M_CostElement_ID) {
		if (M_CostElement_ID < 1)
			throw new IllegalArgumentException("M_CostElement_ID is mandatory.");
		set_ValueNoCheck("M_CostElement_ID", new Integer(M_CostElement_ID));
	}

	/**
	 * Get Cost Element. Product Cost Element
	 */
	public int getM_CostElement_ID() {
		Integer ii = (Integer) get_Value("M_CostElement_ID");
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getName());
	}
}
