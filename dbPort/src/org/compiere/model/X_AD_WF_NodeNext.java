/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_WF_NodeNext
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.875
 */
public class X_AD_WF_NodeNext extends PO {
	/** Standard Constructor */
	public X_AD_WF_NodeNext(Properties ctx, int AD_WF_NodeNext_ID,
			String trxName) {
		super(ctx, AD_WF_NodeNext_ID, trxName);
		/**
		 * if (AD_WF_NodeNext_ID == 0) { setAD_WF_Next_ID (0);
		 * setAD_WF_NodeNext_ID (0); setAD_WF_Node_ID (0); setEntityType (null); //
		 * U setIsStdUserWorkflow (false); setSeqNo (0); // 10 setTRANSITIONTYPE
		 * (false); }
		 */
	}

	/** Load Constructor */
	public X_AD_WF_NodeNext(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_WF_NodeNext */
	public static final String Table_Name = "AD_WF_NodeNext";

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
		StringBuffer sb = new StringBuffer("X_AD_WF_NodeNext[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/** AD_WF_Next_ID AD_Reference_ID=109 */
	public static final int AD_WF_NEXT_ID_AD_Reference_ID = 109;

	/**
	 * Set Next Node. Next Node in workflow
	 */
	public void setAD_WF_Next_ID(int AD_WF_Next_ID) {
		if (AD_WF_Next_ID < 1)
			throw new IllegalArgumentException("AD_WF_Next_ID is mandatory.");
		set_Value("AD_WF_Next_ID", new Integer(AD_WF_Next_ID));
	}

	/**
	 * Get Next Node. Next Node in workflow
	 */
	public int getAD_WF_Next_ID() {
		Integer ii = (Integer) get_Value("AD_WF_Next_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Node Transition. Workflow Node Transition
	 */
	public void setAD_WF_NodeNext_ID(int AD_WF_NodeNext_ID) {
		if (AD_WF_NodeNext_ID < 1)
			throw new IllegalArgumentException(
					"AD_WF_NodeNext_ID is mandatory.");
		set_ValueNoCheck("AD_WF_NodeNext_ID", new Integer(AD_WF_NodeNext_ID));
	}

	/**
	 * Get Node Transition. Workflow Node Transition
	 */
	public int getAD_WF_NodeNext_ID() {
		Integer ii = (Integer) get_Value("AD_WF_NodeNext_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Node. Workflow Node (activity), step or process
	 */
	public void setAD_WF_Node_ID(int AD_WF_Node_ID) {
		if (AD_WF_Node_ID < 1)
			throw new IllegalArgumentException("AD_WF_Node_ID is mandatory.");
		set_ValueNoCheck("AD_WF_Node_ID", new Integer(AD_WF_Node_ID));
	}

	/**
	 * Get Node. Workflow Node (activity), step or process
	 */
	public int getAD_WF_Node_ID() {
		Integer ii = (Integer) get_Value("AD_WF_Node_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getAD_WF_Node_ID()));
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
	 * Set Std User Workflow. Standard Manual User Approval Workflow
	 */
	public void setIsStdUserWorkflow(boolean IsStdUserWorkflow) {
		set_Value("IsStdUserWorkflow", new Boolean(IsStdUserWorkflow));
	}

	/**
	 * Get Std User Workflow. Standard Manual User Approval Workflow
	 */
	public boolean isStdUserWorkflow() {
		Object oo = get_Value("IsStdUserWorkflow");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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

	/** Set TRANSITIONTYPE */
	public void setTRANSITIONTYPE(boolean TRANSITIONTYPE) {
		set_Value("TRANSITIONTYPE", new Boolean(TRANSITIONTYPE));
	}

	/** Get TRANSITIONTYPE */
	public boolean isTRANSITIONTYPE() {
		Object oo = get_Value("TRANSITIONTYPE");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Transition Code. Code resulting in TRUE of FALSE
	 */
	public void setTransitionCode(String TransitionCode) {
		if (TransitionCode != null && TransitionCode.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			TransitionCode = TransitionCode.substring(0, 1999);
		}
		set_Value("TransitionCode", TransitionCode);
	}

	/**
	 * Get Transition Code. Code resulting in TRUE of FALSE
	 */
	public String getTransitionCode() {
		return (String) get_Value("TransitionCode");
	}
}
