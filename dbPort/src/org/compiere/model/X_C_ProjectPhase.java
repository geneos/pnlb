/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_ProjectPhase
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.296
 */
public class X_C_ProjectPhase extends PO {
	/** Standard Constructor */
	public X_C_ProjectPhase(Properties ctx, int C_ProjectPhase_ID,
			String trxName) {
		super(ctx, C_ProjectPhase_ID, trxName);
		/**
		 * if (C_ProjectPhase_ID == 0) { setC_ProjectPhase_ID (0);
		 * setC_Project_ID (0); setCommittedAmt (Env.ZERO); setIsCommitCeiling
		 * (false); setIsComplete (false); setName (null); setSeqNo (0); //
		 * 
		 * @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM C_ProjectPhase
		 *             WHERE C_Project_ID=@C_Project_ID@ }
		 */
	}

	/** Load Constructor */
	public X_C_ProjectPhase(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_ProjectPhase */
	public static final String Table_Name = "C_ProjectPhase";

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
		StringBuffer sb = new StringBuffer("X_C_ProjectPhase[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Order. Order
	 */
	public void setC_Order_ID(int C_Order_ID) {
		if (C_Order_ID <= 0)
			set_ValueNoCheck("C_Order_ID", null);
		else
			set_ValueNoCheck("C_Order_ID", new Integer(C_Order_ID));
	}

	/**
	 * Get Order. Order
	 */
	public int getC_Order_ID() {
		Integer ii = (Integer) get_Value("C_Order_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Standard Phase. Standard Phase of the Project Type
	 */
	public void setC_Phase_ID(int C_Phase_ID) {
		if (C_Phase_ID <= 0)
			set_ValueNoCheck("C_Phase_ID", null);
		else
			set_ValueNoCheck("C_Phase_ID", new Integer(C_Phase_ID));
	}

	/**
	 * Get Standard Phase. Standard Phase of the Project Type
	 */
	public int getC_Phase_ID() {
		Integer ii = (Integer) get_Value("C_Phase_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Project Phase. Phase of a Project
	 */
	public void setC_ProjectPhase_ID(int C_ProjectPhase_ID) {
		if (C_ProjectPhase_ID < 1)
			throw new IllegalArgumentException(
					"C_ProjectPhase_ID is mandatory.");
		set_ValueNoCheck("C_ProjectPhase_ID", new Integer(C_ProjectPhase_ID));
	}

	/**
	 * Get Project Phase. Phase of a Project
	 */
	public int getC_ProjectPhase_ID() {
		Integer ii = (Integer) get_Value("C_ProjectPhase_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Project. Financial Project
	 */
	public void setC_Project_ID(int C_Project_ID) {
		if (C_Project_ID < 1)
			throw new IllegalArgumentException("C_Project_ID is mandatory.");
		set_ValueNoCheck("C_Project_ID", new Integer(C_Project_ID));
	}

	/**
	 * Get Project. Financial Project
	 */
	public int getC_Project_ID() {
		Integer ii = (Integer) get_Value("C_Project_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Committed Amount. The (legal) commitment amount
	 */
	public void setCommittedAmt(BigDecimal CommittedAmt) {
		if (CommittedAmt == null)
			throw new IllegalArgumentException("CommittedAmt is mandatory.");
		set_Value("CommittedAmt", CommittedAmt);
	}

	/**
	 * Get Committed Amount. The (legal) commitment amount
	 */
	public BigDecimal getCommittedAmt() {
		BigDecimal bd = (BigDecimal) get_Value("CommittedAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
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
	 * Set End Date. Last effective date (inclusive)
	 */
	public void setEndDate(Timestamp EndDate) {
		set_Value("EndDate", EndDate);
	}

	/**
	 * Get End Date. Last effective date (inclusive)
	 */
	public Timestamp getEndDate() {
		return (Timestamp) get_Value("EndDate");
	}

	/**
	 * Set Generate Order. Generate Order
	 */
	public void setGenerateOrder(String GenerateOrder) {
		if (GenerateOrder != null && GenerateOrder.length() > 1) {
			log.warning("Length > 1 - truncated");
			GenerateOrder = GenerateOrder.substring(0, 0);
		}
		set_Value("GenerateOrder", GenerateOrder);
	}

	/**
	 * Get Generate Order. Generate Order
	 */
	public String getGenerateOrder() {
		return (String) get_Value("GenerateOrder");
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
	 * Set Commitment is Ceiling. The commitment amount/quantity is the
	 * chargeable ceiling
	 */
	public void setIsCommitCeiling(boolean IsCommitCeiling) {
		set_Value("IsCommitCeiling", new Boolean(IsCommitCeiling));
	}

	/**
	 * Get Commitment is Ceiling. The commitment amount/quantity is the
	 * chargeable ceiling
	 */
	public boolean isCommitCeiling() {
		Object oo = get_Value("IsCommitCeiling");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Complete. It is complete
	 */
	public void setIsComplete(boolean IsComplete) {
		set_Value("IsComplete", new Boolean(IsComplete));
	}

	/**
	 * Get Complete. It is complete
	 */
	public boolean isComplete() {
		Object oo = get_Value("IsComplete");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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

	/**
	 * Set Unit Price. Actual Price
	 */
	public void setPriceActual(BigDecimal PriceActual) {
		set_Value("PriceActual", PriceActual);
	}

	/**
	 * Get Unit Price. Actual Price
	 */
	public BigDecimal getPriceActual() {
		BigDecimal bd = (BigDecimal) get_Value("PriceActual");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Quantity. Quantity
	 */
	public void setQty(BigDecimal Qty) {
		set_Value("Qty", Qty);
	}

	/**
	 * Get Quantity. Quantity
	 */
	public BigDecimal getQty() {
		BigDecimal bd = (BigDecimal) get_Value("Qty");
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getSeqNo()));
	}

	/**
	 * Set Start Date. First effective day (inclusive)
	 */
	public void setStartDate(Timestamp StartDate) {
		set_Value("StartDate", StartDate);
	}

	/**
	 * Get Start Date. First effective day (inclusive)
	 */
	public Timestamp getStartDate() {
		return (Timestamp) get_Value("StartDate");
	}
}
