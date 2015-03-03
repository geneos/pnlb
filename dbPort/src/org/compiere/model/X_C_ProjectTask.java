/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_ProjectTask
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.296
 */
public class X_C_ProjectTask extends PO {
	/** Standard Constructor */
	public X_C_ProjectTask(Properties ctx, int C_ProjectTask_ID, String trxName) {
		super(ctx, C_ProjectTask_ID, trxName);
		/**
		 * if (C_ProjectTask_ID == 0) { setC_ProjectPhase_ID (0);
		 * setC_ProjectTask_ID (0); setName (null); setSeqNo (0); //
		 * 
		 * @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM C_ProjectTask
		 *             WHERE C_ProjectPhase_ID=@C_ProjectPhase_ID@ }
		 */
	}

	/** Load Constructor */
	public X_C_ProjectTask(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_ProjectTask */
	public static final String Table_Name = "C_ProjectTask";

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
		StringBuffer sb = new StringBuffer("X_C_ProjectTask[").append(get_ID())
				.append("]");
		return sb.toString();
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
	 * Set Project Task. Actual Project Task in a Phase
	 */
	public void setC_ProjectTask_ID(int C_ProjectTask_ID) {
		if (C_ProjectTask_ID < 1)
			throw new IllegalArgumentException("C_ProjectTask_ID is mandatory.");
		set_ValueNoCheck("C_ProjectTask_ID", new Integer(C_ProjectTask_ID));
	}

	/**
	 * Get Project Task. Actual Project Task in a Phase
	 */
	public int getC_ProjectTask_ID() {
		Integer ii = (Integer) get_Value("C_ProjectTask_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Standard Task. Standard Project Type Task
	 */
	public void setC_Task_ID(int C_Task_ID) {
		if (C_Task_ID <= 0)
			set_ValueNoCheck("C_Task_ID", null);
		else
			set_ValueNoCheck("C_Task_ID", new Integer(C_Task_ID));
	}

	/**
	 * Get Standard Task. Standard Project Type Task
	 */
	public int getC_Task_ID() {
		Integer ii = (Integer) get_Value("C_Task_ID");
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
}
