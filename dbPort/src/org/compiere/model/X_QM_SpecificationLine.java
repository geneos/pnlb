/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for QM_SpecificationLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.171
 */
public class X_QM_SpecificationLine extends PO {
	/** Standard Constructor */
	public X_QM_SpecificationLine(Properties ctx, int QM_SpecificationLine_ID,
			String trxName) {
		super(ctx, QM_SpecificationLine_ID, trxName);
		/**
		 * if (QM_SpecificationLine_ID == 0) { setAndOr (null);
		 * setM_Attribute_ID (0); setOperation (null);
		 * setQM_SpecificationLine_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_QM_SpecificationLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=QM_SpecificationLine */
	public static final String Table_Name = "QM_SpecificationLine";

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
		StringBuffer sb = new StringBuffer("X_QM_SpecificationLine[").append(
				get_ID()).append("]");
		return sb.toString();
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

	/**
	 * Set Attribute. Product Attribute
	 */
	public void setM_Attribute_ID(int M_Attribute_ID) {
		if (M_Attribute_ID < 1)
			throw new IllegalArgumentException("M_Attribute_ID is mandatory.");
		set_Value("M_Attribute_ID", new Integer(M_Attribute_ID));
	}

	/**
	 * Get Attribute. Product Attribute
	 */
	public int getM_Attribute_ID() {
		Integer ii = (Integer) get_Value("M_Attribute_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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

	/** Set QM_SpecificationLine_ID */
	public void setQM_SpecificationLine_ID(int QM_SpecificationLine_ID) {
		if (QM_SpecificationLine_ID < 1)
			throw new IllegalArgumentException(
					"QM_SpecificationLine_ID is mandatory.");
		set_ValueNoCheck("QM_SpecificationLine_ID", new Integer(
				QM_SpecificationLine_ID));
	}

	/** Get QM_SpecificationLine_ID */
	public int getQM_SpecificationLine_ID() {
		Integer ii = (Integer) get_Value("QM_SpecificationLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set QM_Specification_ID */
	public void setQM_Specification_ID(int QM_Specification_ID) {
		if (QM_Specification_ID <= 0)
			set_Value("QM_Specification_ID", null);
		else
			set_Value("QM_Specification_ID", new Integer(QM_Specification_ID));
	}

	/** Get QM_Specification_ID */
	public int getQM_Specification_ID() {
		Integer ii = (Integer) get_Value("QM_Specification_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
	public void setValidFrom(String ValidFrom) {
		if (ValidFrom != null && ValidFrom.length() > 22) {
			log.warning("Length > 22 - truncated");
			ValidFrom = ValidFrom.substring(0, 21);
		}
		set_Value("ValidFrom", ValidFrom);
	}

	/**
	 * Get Valid from. Valid from including this date (first day)
	 */
	public String getValidFrom() {
		return (String) get_Value("ValidFrom");
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
		if (Value != null && Value.length() > 40) {
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
}
