/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Sequence_Audit
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.312
 */
public class X_AD_Sequence_Audit extends PO {
	/** Standard Constructor */
	public X_AD_Sequence_Audit(Properties ctx, int AD_Sequence_Audit_ID,
			String trxName) {
		super(ctx, AD_Sequence_Audit_ID, trxName);
		/**
		 * if (AD_Sequence_Audit_ID == 0) { setAD_Sequence_ID (0);
		 * setAD_Table_ID (0); setDocumentNo (null); setRecord_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_AD_Sequence_Audit(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Sequence_Audit */
	public static final String Table_Name = "AD_Sequence_Audit";

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
		StringBuffer sb = new StringBuffer("X_AD_Sequence_Audit[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Sequence. Document Sequence
	 */
	public void setAD_Sequence_ID(int AD_Sequence_ID) {
		if (AD_Sequence_ID < 1)
			throw new IllegalArgumentException("AD_Sequence_ID is mandatory.");
		set_ValueNoCheck("AD_Sequence_ID", new Integer(AD_Sequence_ID));
	}

	/**
	 * Get Sequence. Document Sequence
	 */
	public int getAD_Sequence_ID() {
		Integer ii = (Integer) get_Value("AD_Sequence_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Table. Table for the Fields
	 */
	public void setAD_Table_ID(int AD_Table_ID) {
		if (AD_Table_ID < 1)
			throw new IllegalArgumentException("AD_Table_ID is mandatory.");
		set_ValueNoCheck("AD_Table_ID", new Integer(AD_Table_ID));
	}

	/**
	 * Get Table. Table for the Fields
	 */
	public int getAD_Table_ID() {
		Integer ii = (Integer) get_Value("AD_Table_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Document No. Document sequence number of the document
	 */
	public void setDocumentNo(String DocumentNo) {
		if (DocumentNo == null)
			throw new IllegalArgumentException("DocumentNo is mandatory.");
		if (DocumentNo.length() > 30) {
			log.warning("Length > 30 - truncated");
			DocumentNo = DocumentNo.substring(0, 29);
		}
		set_ValueNoCheck("DocumentNo", DocumentNo);
	}

	/**
	 * Get Document No. Document sequence number of the document
	 */
	public String getDocumentNo() {
		return (String) get_Value("DocumentNo");
	}

	/**
	 * Set Record ID. Direct internal record ID
	 */
	public void setRecord_ID(int Record_ID) {
		if (Record_ID < 0)
			throw new IllegalArgumentException("Record_ID is mandatory.");
		set_ValueNoCheck("Record_ID", new Integer(Record_ID));
	}

	/**
	 * Get Record ID. Direct internal record ID
	 */
	public int getRecord_ID() {
		Integer ii = (Integer) get_Value("Record_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
