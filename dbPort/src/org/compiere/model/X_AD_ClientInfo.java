/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_ClientInfo
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.218
 */
public class X_AD_ClientInfo extends PO {
	/** Standard Constructor */
	public X_AD_ClientInfo(Properties ctx, int AD_ClientInfo_ID, String trxName) {
		super(ctx, AD_ClientInfo_ID, trxName);
		/**
		 * if (AD_ClientInfo_ID == 0) { setIsDiscountLineAmt (false); }
		 */
	}

	/** Load Constructor */
	public X_AD_ClientInfo(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_ClientInfo */
	public static final String Table_Name = "AD_ClientInfo";

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
		StringBuffer sb = new StringBuffer("X_AD_ClientInfo[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** AD_Tree_Activity_ID AD_Reference_ID=184 */
	public static final int AD_TREE_ACTIVITY_ID_AD_Reference_ID = 184;

	/**
	 * Set Activity Tree. Tree to determine activity hierarchy
	 */
	public void setAD_Tree_Activity_ID(int AD_Tree_Activity_ID) {
		if (AD_Tree_Activity_ID <= 0)
			set_ValueNoCheck("AD_Tree_Activity_ID", null);
		else
			set_ValueNoCheck("AD_Tree_Activity_ID", new Integer(
					AD_Tree_Activity_ID));
	}

	/**
	 * Get Activity Tree. Tree to determine activity hierarchy
	 */
	public int getAD_Tree_Activity_ID() {
		Integer ii = (Integer) get_Value("AD_Tree_Activity_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_Tree_BPartner_ID AD_Reference_ID=184 */
	public static final int AD_TREE_BPARTNER_ID_AD_Reference_ID = 184;

	/**
	 * Set BPartner Tree. Tree to determine business partner hierarchy
	 */
	public void setAD_Tree_BPartner_ID(int AD_Tree_BPartner_ID) {
		if (AD_Tree_BPartner_ID <= 0)
			set_ValueNoCheck("AD_Tree_BPartner_ID", null);
		else
			set_ValueNoCheck("AD_Tree_BPartner_ID", new Integer(
					AD_Tree_BPartner_ID));
	}

	/**
	 * Get BPartner Tree. Tree to determine business partner hierarchy
	 */
	public int getAD_Tree_BPartner_ID() {
		Integer ii = (Integer) get_Value("AD_Tree_BPartner_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_Tree_Campaign_ID AD_Reference_ID=184 */
	public static final int AD_TREE_CAMPAIGN_ID_AD_Reference_ID = 184;

	/**
	 * Set Campaign Tree. Tree to determine marketing campaign hierarchy
	 */
	public void setAD_Tree_Campaign_ID(int AD_Tree_Campaign_ID) {
		if (AD_Tree_Campaign_ID <= 0)
			set_ValueNoCheck("AD_Tree_Campaign_ID", null);
		else
			set_ValueNoCheck("AD_Tree_Campaign_ID", new Integer(
					AD_Tree_Campaign_ID));
	}

	/**
	 * Get Campaign Tree. Tree to determine marketing campaign hierarchy
	 */
	public int getAD_Tree_Campaign_ID() {
		Integer ii = (Integer) get_Value("AD_Tree_Campaign_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_Tree_Menu_ID AD_Reference_ID=184 */
	public static final int AD_TREE_MENU_ID_AD_Reference_ID = 184;

	/**
	 * Set Menu Tree. Tree of the menu
	 */
	public void setAD_Tree_Menu_ID(int AD_Tree_Menu_ID) {
		if (AD_Tree_Menu_ID <= 0)
			set_ValueNoCheck("AD_Tree_Menu_ID", null);
		else
			set_ValueNoCheck("AD_Tree_Menu_ID", new Integer(AD_Tree_Menu_ID));
	}

	/**
	 * Get Menu Tree. Tree of the menu
	 */
	public int getAD_Tree_Menu_ID() {
		Integer ii = (Integer) get_Value("AD_Tree_Menu_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_Tree_Org_ID AD_Reference_ID=184 */
	public static final int AD_TREE_ORG_ID_AD_Reference_ID = 184;

	/**
	 * Set Organization Tree. Tree to determine organizational hierarchy
	 */
	public void setAD_Tree_Org_ID(int AD_Tree_Org_ID) {
		if (AD_Tree_Org_ID <= 0)
			set_ValueNoCheck("AD_Tree_Org_ID", null);
		else
			set_ValueNoCheck("AD_Tree_Org_ID", new Integer(AD_Tree_Org_ID));
	}

	/**
	 * Get Organization Tree. Tree to determine organizational hierarchy
	 */
	public int getAD_Tree_Org_ID() {
		Integer ii = (Integer) get_Value("AD_Tree_Org_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_Tree_Product_ID AD_Reference_ID=184 */
	public static final int AD_TREE_PRODUCT_ID_AD_Reference_ID = 184;

	/**
	 * Set Product Tree. Tree to determine product hierarchy
	 */
	public void setAD_Tree_Product_ID(int AD_Tree_Product_ID) {
		if (AD_Tree_Product_ID <= 0)
			set_ValueNoCheck("AD_Tree_Product_ID", null);
		else
			set_ValueNoCheck("AD_Tree_Product_ID", new Integer(
					AD_Tree_Product_ID));
	}

	/**
	 * Get Product Tree. Tree to determine product hierarchy
	 */
	public int getAD_Tree_Product_ID() {
		Integer ii = (Integer) get_Value("AD_Tree_Product_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_Tree_Project_ID AD_Reference_ID=184 */
	public static final int AD_TREE_PROJECT_ID_AD_Reference_ID = 184;

	/**
	 * Set Project Tree. Tree to determine project hierarchy
	 */
	public void setAD_Tree_Project_ID(int AD_Tree_Project_ID) {
		if (AD_Tree_Project_ID <= 0)
			set_ValueNoCheck("AD_Tree_Project_ID", null);
		else
			set_ValueNoCheck("AD_Tree_Project_ID", new Integer(
					AD_Tree_Project_ID));
	}

	/**
	 * Get Project Tree. Tree to determine project hierarchy
	 */
	public int getAD_Tree_Project_ID() {
		Integer ii = (Integer) get_Value("AD_Tree_Project_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_Tree_SalesRegion_ID AD_Reference_ID=184 */
	public static final int AD_TREE_SALESREGION_ID_AD_Reference_ID = 184;

	/**
	 * Set Sales Region Tree. Tree to determine sales regional hierarchy
	 */
	public void setAD_Tree_SalesRegion_ID(int AD_Tree_SalesRegion_ID) {
		if (AD_Tree_SalesRegion_ID <= 0)
			set_ValueNoCheck("AD_Tree_SalesRegion_ID", null);
		else
			set_ValueNoCheck("AD_Tree_SalesRegion_ID", new Integer(
					AD_Tree_SalesRegion_ID));
	}

	/**
	 * Get Sales Region Tree. Tree to determine sales regional hierarchy
	 */
	public int getAD_Tree_SalesRegion_ID() {
		Integer ii = (Integer) get_Value("AD_Tree_SalesRegion_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** C_AcctSchema1_ID AD_Reference_ID=136 */
	public static final int C_ACCTSCHEMA1_ID_AD_Reference_ID = 136;

	/**
	 * Set Primary Accounting Schema. Primary rules for accounting
	 */
	public void setC_AcctSchema1_ID(int C_AcctSchema1_ID) {
		if (C_AcctSchema1_ID <= 0)
			set_ValueNoCheck("C_AcctSchema1_ID", null);
		else
			set_ValueNoCheck("C_AcctSchema1_ID", new Integer(C_AcctSchema1_ID));
	}

	/**
	 * Get Primary Accounting Schema. Primary rules for accounting
	 */
	public int getC_AcctSchema1_ID() {
		Integer ii = (Integer) get_Value("C_AcctSchema1_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** C_BPartnerCashTrx_ID AD_Reference_ID=138 */
	public static final int C_BPARTNERCASHTRX_ID_AD_Reference_ID = 138;

	/**
	 * Set Template B.Partner. Business Partner used for creating new Business
	 * Partners on the fly
	 */
	public void setC_BPartnerCashTrx_ID(int C_BPartnerCashTrx_ID) {
		if (C_BPartnerCashTrx_ID <= 0)
			set_Value("C_BPartnerCashTrx_ID", null);
		else
			set_Value("C_BPartnerCashTrx_ID", new Integer(C_BPartnerCashTrx_ID));
	}

	/**
	 * Get Template B.Partner. Business Partner used for creating new Business
	 * Partners on the fly
	 */
	public int getC_BPartnerCashTrx_ID() {
		Integer ii = (Integer) get_Value("C_BPartnerCashTrx_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Calendar. Accounting Calendar Name
	 */
	public void setC_Calendar_ID(int C_Calendar_ID) {
		if (C_Calendar_ID <= 0)
			set_Value("C_Calendar_ID", null);
		else
			set_Value("C_Calendar_ID", new Integer(C_Calendar_ID));
	}

	/**
	 * Get Calendar. Accounting Calendar Name
	 */
	public int getC_Calendar_ID() {
		Integer ii = (Integer) get_Value("C_Calendar_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** C_UOM_Length_ID AD_Reference_ID=114 */
	public static final int C_UOM_LENGTH_ID_AD_Reference_ID = 114;

	/**
	 * Set UOM for Length. Standard Unit of Measure for Length
	 */
	public void setC_UOM_Length_ID(int C_UOM_Length_ID) {
		if (C_UOM_Length_ID <= 0)
			set_Value("C_UOM_Length_ID", null);
		else
			set_Value("C_UOM_Length_ID", new Integer(C_UOM_Length_ID));
	}

	/**
	 * Get UOM for Length. Standard Unit of Measure for Length
	 */
	public int getC_UOM_Length_ID() {
		Integer ii = (Integer) get_Value("C_UOM_Length_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** C_UOM_Time_ID AD_Reference_ID=114 */
	public static final int C_UOM_TIME_ID_AD_Reference_ID = 114;

	/**
	 * Set UOM for Time. Standard Unit of Measure for Time
	 */
	public void setC_UOM_Time_ID(int C_UOM_Time_ID) {
		if (C_UOM_Time_ID <= 0)
			set_Value("C_UOM_Time_ID", null);
		else
			set_Value("C_UOM_Time_ID", new Integer(C_UOM_Time_ID));
	}

	/**
	 * Get UOM for Time. Standard Unit of Measure for Time
	 */
	public int getC_UOM_Time_ID() {
		Integer ii = (Integer) get_Value("C_UOM_Time_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** C_UOM_Volume_ID AD_Reference_ID=114 */
	public static final int C_UOM_VOLUME_ID_AD_Reference_ID = 114;

	/**
	 * Set UOM for Volume. Standard Unit of Measure for Volume
	 */
	public void setC_UOM_Volume_ID(int C_UOM_Volume_ID) {
		if (C_UOM_Volume_ID <= 0)
			set_Value("C_UOM_Volume_ID", null);
		else
			set_Value("C_UOM_Volume_ID", new Integer(C_UOM_Volume_ID));
	}

	/**
	 * Get UOM for Volume. Standard Unit of Measure for Volume
	 */
	public int getC_UOM_Volume_ID() {
		Integer ii = (Integer) get_Value("C_UOM_Volume_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** C_UOM_Weight_ID AD_Reference_ID=114 */
	public static final int C_UOM_WEIGHT_ID_AD_Reference_ID = 114;

	/**
	 * Set UOM for Weight. Standard Unit of Measure for Weight
	 */
	public void setC_UOM_Weight_ID(int C_UOM_Weight_ID) {
		if (C_UOM_Weight_ID <= 0)
			set_Value("C_UOM_Weight_ID", null);
		else
			set_Value("C_UOM_Weight_ID", new Integer(C_UOM_Weight_ID));
	}

	/**
	 * Get UOM for Weight. Standard Unit of Measure for Weight
	 */
	public int getC_UOM_Weight_ID() {
		Integer ii = (Integer) get_Value("C_UOM_Weight_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Discount calculated from Line Amounts. Payment Discount calculation
	 * does not include Taxes and Charges
	 */
	public void setIsDiscountLineAmt(boolean IsDiscountLineAmt) {
		set_Value("IsDiscountLineAmt", new Boolean(IsDiscountLineAmt));
	}

	/**
	 * Get Discount calculated from Line Amounts. Payment Discount calculation
	 * does not include Taxes and Charges
	 */
	public boolean isDiscountLineAmt() {
		Object oo = get_Value("IsDiscountLineAmt");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Days to keep Log. Number of days to keep the log entries
	 */
	public void setKeepLogDays(int KeepLogDays) {
		set_Value("KeepLogDays", new Integer(KeepLogDays));
	}

	/**
	 * Get Days to keep Log. Number of days to keep the log entries
	 */
	public int getKeepLogDays() {
		Integer ii = (Integer) get_Value("KeepLogDays");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** M_ProductFreight_ID AD_Reference_ID=162 */
	public static final int M_PRODUCTFREIGHT_ID_AD_Reference_ID = 162;

	/** Set Product for Freight */
	public void setM_ProductFreight_ID(int M_ProductFreight_ID) {
		if (M_ProductFreight_ID <= 0)
			set_Value("M_ProductFreight_ID", null);
		else
			set_Value("M_ProductFreight_ID", new Integer(M_ProductFreight_ID));
	}

	/** Get Product for Freight */
	public int getM_ProductFreight_ID() {
		Integer ii = (Integer) get_Value("M_ProductFreight_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
