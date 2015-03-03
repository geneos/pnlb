/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_CashAnalysis
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.937
 */
public class X_T_CashAnalysis extends PO {
	/** Standard Constructor */
	public X_T_CashAnalysis(Properties ctx, int T_CashAnalysis_ID,
			String trxName) {
		super(ctx, T_CashAnalysis_ID, trxName);
		/**
		 * if (T_CashAnalysis_ID == 0) { setAD_OrgTrx_ID (0);
		 * setT_CashAnalysis_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_T_CashAnalysis(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_CashAnalysis */
	public static final String Table_Name = "T_CashAnalysis";

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

	protected BigDecimal accessLevel = new BigDecimal(4);

	/** AccessLevel 4 - System */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_T_CashAnalysis[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/** AD_OrgTrx_ID AD_Reference_ID=130 */
	public static final int AD_ORGTRX_ID_AD_Reference_ID = 130;

	/**
	 * Set Trx Organization. Performing or initiating organization
	 */
	public void setAD_OrgTrx_ID(int AD_OrgTrx_ID) {
		if (AD_OrgTrx_ID < 1)
			throw new IllegalArgumentException("AD_OrgTrx_ID is mandatory.");
		set_Value("AD_OrgTrx_ID", new Integer(AD_OrgTrx_ID));
	}

	/**
	 * Get Trx Organization. Performing or initiating organization
	 */
	public int getAD_OrgTrx_ID() {
		Integer ii = (Integer) get_Value("AD_OrgTrx_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Business Partner . Identifies a Business Partner
	 */
	public void setC_BPartner_ID(int C_BPartner_ID) {
		if (C_BPartner_ID <= 0)
			set_Value("C_BPartner_ID", null);
		else
			set_Value("C_BPartner_ID", new Integer(C_BPartner_ID));
	}

	/**
	 * Get Business Partner . Identifies a Business Partner
	 */
	public int getC_BPartner_ID() {
		Integer ii = (Integer) get_Value("C_BPartner_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Currency. The Currency for this record
	 */
	public void setC_Currency_ID(int C_Currency_ID) {
		if (C_Currency_ID <= 0)
			set_Value("C_Currency_ID", null);
		else
			set_Value("C_Currency_ID", new Integer(C_Currency_ID));
	}

	/**
	 * Get Currency. The Currency for this record
	 */
	public int getC_Currency_ID() {
		Integer ii = (Integer) get_Value("C_Currency_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Sales Region. Sales coverage region
	 */
	public void setC_SalesRegion_ID(int C_SalesRegion_ID) {
		if (C_SalesRegion_ID <= 0)
			set_Value("C_SalesRegion_ID", null);
		else
			set_Value("C_SalesRegion_ID", new Integer(C_SalesRegion_ID));
	}

	/**
	 * Get Sales Region. Sales coverage region
	 */
	public int getC_SalesRegion_ID() {
		Integer ii = (Integer) get_Value("C_SalesRegion_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Col1 */
	public void setCol1(BigDecimal Col1) {
		set_Value("Col1", Col1);
	}

	/** Get Col1 */
	public BigDecimal getCol1() {
		BigDecimal bd = (BigDecimal) get_Value("Col1");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Col2 */
	public void setCol2(BigDecimal Col2) {
		set_Value("Col2", Col2);
	}

	/** Get Col2 */
	public BigDecimal getCol2() {
		BigDecimal bd = (BigDecimal) get_Value("Col2");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Col3 */
	public void setCol3(BigDecimal Col3) {
		set_Value("Col3", Col3);
	}

	/** Get Col3 */
	public BigDecimal getCol3() {
		BigDecimal bd = (BigDecimal) get_Value("Col3");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Col4 */
	public void setCol4(BigDecimal Col4) {
		set_Value("Col4", Col4);
	}

	/** Get Col4 */
	public BigDecimal getCol4() {
		BigDecimal bd = (BigDecimal) get_Value("Col4");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Col5 */
	public void setCol5(BigDecimal Col5) {
		set_Value("Col5", Col5);
	}

	/** Get Col5 */
	public BigDecimal getCol5() {
		BigDecimal bd = (BigDecimal) get_Value("Col5");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set T_CashAnalysis_ID */
	public void setT_CashAnalysis_ID(int T_CashAnalysis_ID) {
		if (T_CashAnalysis_ID < 1)
			throw new IllegalArgumentException(
					"T_CashAnalysis_ID is mandatory.");
		set_ValueNoCheck("T_CashAnalysis_ID", new Integer(T_CashAnalysis_ID));
	}

	/** Get T_CashAnalysis_ID */
	public int getT_CashAnalysis_ID() {
		Integer ii = (Integer) get_Value("T_CashAnalysis_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
