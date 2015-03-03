/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_RevenueRecognition_Run
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.421
 */
public class X_C_RevenueRecognition_Run extends PO {
	/** Standard Constructor */
	public X_C_RevenueRecognition_Run(Properties ctx,
			int C_RevenueRecognition_Run_ID, String trxName) {
		super(ctx, C_RevenueRecognition_Run_ID, trxName);
		/**
		 * if (C_RevenueRecognition_Run_ID == 0) {
		 * setC_RevenueRecognition_Plan_ID (0); setC_RevenueRecognition_Run_ID
		 * (0); setGL_Journal_ID (0); setRecognizedAmt (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_C_RevenueRecognition_Run(Properties ctx, ResultSet rs,
			String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_RevenueRecognition_Run */
	public static final String Table_Name = "C_RevenueRecognition_Run";

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

	protected BigDecimal accessLevel = new BigDecimal(1);

	/** AccessLevel 1 - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_C_RevenueRecognition_Run[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Revenue Recognition Plan. Plan for recognizing or recording revenue
	 */
	public void setC_RevenueRecognition_Plan_ID(int C_RevenueRecognition_Plan_ID) {
		if (C_RevenueRecognition_Plan_ID < 1)
			throw new IllegalArgumentException(
					"C_RevenueRecognition_Plan_ID is mandatory.");
		set_ValueNoCheck("C_RevenueRecognition_Plan_ID", new Integer(
				C_RevenueRecognition_Plan_ID));
	}

	/**
	 * Get Revenue Recognition Plan. Plan for recognizing or recording revenue
	 */
	public int getC_RevenueRecognition_Plan_ID() {
		Integer ii = (Integer) get_Value("C_RevenueRecognition_Plan_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getC_RevenueRecognition_Plan_ID()));
	}

	/**
	 * Set Revenue Recognition Run. Revenue Recognition Run or Process
	 */
	public void setC_RevenueRecognition_Run_ID(int C_RevenueRecognition_Run_ID) {
		if (C_RevenueRecognition_Run_ID < 1)
			throw new IllegalArgumentException(
					"C_RevenueRecognition_Run_ID is mandatory.");
		set_ValueNoCheck("C_RevenueRecognition_Run_ID", new Integer(
				C_RevenueRecognition_Run_ID));
	}

	/**
	 * Get Revenue Recognition Run. Revenue Recognition Run or Process
	 */
	public int getC_RevenueRecognition_Run_ID() {
		Integer ii = (Integer) get_Value("C_RevenueRecognition_Run_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Journal. General Ledger Journal
	 */
	public void setGL_Journal_ID(int GL_Journal_ID) {
		if (GL_Journal_ID < 1)
			throw new IllegalArgumentException("GL_Journal_ID is mandatory.");
		set_ValueNoCheck("GL_Journal_ID", new Integer(GL_Journal_ID));
	}

	/**
	 * Get Journal. General Ledger Journal
	 */
	public int getGL_Journal_ID() {
		Integer ii = (Integer) get_Value("GL_Journal_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Recognized Amount */
	public void setRecognizedAmt(BigDecimal RecognizedAmt) {
		if (RecognizedAmt == null)
			throw new IllegalArgumentException("RecognizedAmt is mandatory.");
		set_ValueNoCheck("RecognizedAmt", RecognizedAmt);
	}

	/** Get Recognized Amount */
	public BigDecimal getRecognizedAmt() {
		BigDecimal bd = (BigDecimal) get_Value("RecognizedAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
