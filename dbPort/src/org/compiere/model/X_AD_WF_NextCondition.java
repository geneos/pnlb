/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_WF_NextCondition
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.75
 */
public class X_AD_WF_NextCondition extends PO {
	/** Standard Constructor */
	public X_AD_WF_NextCondition(Properties ctx, int AD_WF_NextCondition_ID,
			String trxName) {
		super(ctx, AD_WF_NextCondition_ID, trxName);
		/**
		 * if (AD_WF_NextCondition_ID == 0) { setAD_Column_ID (0);
		 * setAD_WF_NextCondition_ID (0); setAD_WF_NodeNext_ID (0); setAndOr
		 * (null); // O setEntityType (null); // U setOperation (null); setSeqNo
		 * (0); //
		 * 
		 * @SQL=SELECT COALESCE(MAX(SeqNo),0)+10 AS DefaultValue FROM
		 *             AD_WF_NextCondition WHERE
		 *             AD_WF_NodeNext_ID=@AD_WF_NodeNext_ID@ setValue (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_WF_NextCondition(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_WF_NextCondition */
	public static final String Table_Name = "AD_WF_NextCondition";

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
		StringBuffer sb = new StringBuffer("X_AD_WF_NextCondition[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Column. Column in the table
	 */
	public void setAD_Column_ID(int AD_Column_ID) {
		if (AD_Column_ID < 1)
			throw new IllegalArgumentException("AD_Column_ID is mandatory.");
		set_Value("AD_Column_ID", new Integer(AD_Column_ID));
	}

	/**
	 * Get Column. Column in the table
	 */
	public int getAD_Column_ID() {
		Integer ii = (Integer) get_Value("AD_Column_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Transition Condition. Workflow Node Transition Condition
	 */
	public void setAD_WF_NextCondition_ID(int AD_WF_NextCondition_ID) {
		if (AD_WF_NextCondition_ID < 1)
			throw new IllegalArgumentException(
					"AD_WF_NextCondition_ID is mandatory.");
		set_ValueNoCheck("AD_WF_NextCondition_ID", new Integer(
				AD_WF_NextCondition_ID));
	}

	/**
	 * Get Transition Condition. Workflow Node Transition Condition
	 */
	public int getAD_WF_NextCondition_ID() {
		Integer ii = (Integer) get_Value("AD_WF_NextCondition_ID");
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

	/** AndOr AD_Reference_ID=204 */
	public static final int ANDOR_AD_Reference_ID = 204;

	/** And = A */
	public static final String ANDOR_And = "A";

	/** Or = O */
	public static final String ANDOR_Or = "O";

	/**
	 * Set And/Or. Logical operation: AND or OR
	 */
	public void setAndOr(String AndOr) {
		if (AndOr == null)
			throw new IllegalArgumentException("AndOr is mandatory");
		if (AndOr.equals("A") || AndOr.equals("O"))
			;
		else
			throw new IllegalArgumentException("AndOr Invalid value - " + AndOr
					+ " - Reference_ID=204 - A - O");
		if (AndOr.length() > 1) {
			log.warning("Length > 1 - truncated");
			AndOr = AndOr.substring(0, 0);
		}
		set_Value("AndOr", AndOr);
	}

	/**
	 * Get And/Or. Logical operation: AND or OR
	 */
	public String getAndOr() {
		return (String) get_Value("AndOr");
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

	/** Operation AD_Reference_ID=205 */
	public static final int OPERATION_AD_Reference_ID = 205;

	/** != = != */
	public static final String OPERATION_NotEq = "!=";

	/** < = << */
	public static final String OPERATION_Le = "<<";

	/** <= = <= */
	public static final String OPERATION_LeEq = "<=";

	/** = = == */
	public static final String OPERATION_Eq = "==";

	/** >= = >= */
	public static final String OPERATION_GtEq = ">=";

	/** > = >> */
	public static final String OPERATION_Gt = ">>";

	/** |<x>| = AB */
	public static final String OPERATION_X = "AB";

	/** sql = SQ */
	public static final String OPERATION_Sql = "SQ";

	/** ~ = ~~ */
	public static final String OPERATION_Like = "~~";

	/**
	 * Set Operation. Compare Operation
	 */
	public void setOperation(String Operation) {
		if (Operation == null)
			throw new IllegalArgumentException("Operation is mandatory");
		if (Operation.equals("!=") || Operation.equals("<<")
				|| Operation.equals("<=") || Operation.equals("==")
				|| Operation.equals(">=") || Operation.equals(">>")
				|| Operation.equals("AB") || Operation.equals("SQ")
				|| Operation.equals("~~"))
			;
		else
			throw new IllegalArgumentException(
					"Operation Invalid value - "
							+ Operation
							+ " - Reference_ID=205 - != - << - <= - == - >= - >> - AB - SQ - ~~");
		if (Operation.length() > 2) {
			log.warning("Length > 2 - truncated");
			Operation = Operation.substring(0, 1);
		}
		set_Value("Operation", Operation);
	}

	/**
	 * Get Operation. Compare Operation
	 */
	public String getOperation() {
		return (String) get_Value("Operation");
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
	 * Set Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public void setValue(String Value) {
		if (Value == null)
			throw new IllegalArgumentException("Value is mandatory.");
		if (Value.length() > 40) {
			log.warning("Length > 40 - truncated");
			Value = Value.substring(0, 39);
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

	/**
	 * Set Value To. Value To
	 */
	public void setValue2(String Value2) {
		if (Value2 != null && Value2.length() > 40) {
			log.warning("Length > 40 - truncated");
			Value2 = Value2.substring(0, 39);
		}
		set_Value("Value2", Value2);
	}

	/**
	 * Get Value To. Value To
	 */
	public String getValue2() {
		return (String) get_Value("Value2");
	}
}
