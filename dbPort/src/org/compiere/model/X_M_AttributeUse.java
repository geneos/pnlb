/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_AttributeUse
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.437
 */
public class X_M_AttributeUse extends PO {
	/** Standard Constructor */
	public X_M_AttributeUse(Properties ctx, int M_AttributeUse_ID,
			String trxName) {
		super(ctx, M_AttributeUse_ID, trxName);
		/**
		 * if (M_AttributeUse_ID == 0) { setM_AttributeSet_ID (0);
		 * setM_Attribute_ID (0); setSeqNo (0); //
		 * 
		 * @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM M_AttributeUse
		 *             WHERE M_AttributeSet_ID=@M_AttributeSet_ID@ }
		 */
	}

	/** Load Constructor */
	public X_M_AttributeUse(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_AttributeUse */
	public static final String Table_Name = "M_AttributeUse";

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
		StringBuffer sb = new StringBuffer("X_M_AttributeUse[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Attribute Set. Product Attribute Set
	 */
	public void setM_AttributeSet_ID(int M_AttributeSet_ID) {
		if (M_AttributeSet_ID < 0)
			throw new IllegalArgumentException(
					"M_AttributeSet_ID is mandatory.");
		set_ValueNoCheck("M_AttributeSet_ID", new Integer(M_AttributeSet_ID));
	}

	/**
	 * Get Attribute Set. Product Attribute Set
	 */
	public int getM_AttributeSet_ID() {
		Integer ii = (Integer) get_Value("M_AttributeSet_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getM_AttributeSet_ID()));
	}

	/**
	 * Set Attribute. Product Attribute
	 */
	public void setM_Attribute_ID(int M_Attribute_ID) {
		if (M_Attribute_ID < 1)
			throw new IllegalArgumentException("M_Attribute_ID is mandatory.");
		set_ValueNoCheck("M_Attribute_ID", new Integer(M_Attribute_ID));
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
}
