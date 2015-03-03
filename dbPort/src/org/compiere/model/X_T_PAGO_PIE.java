/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_PAGO_PIE
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:05.093
 */
public class X_T_PAGO_PIE extends PO {
	/** Standard Constructor */
	public X_T_PAGO_PIE(Properties ctx, int T_PAGO_PIE_ID, String trxName) {
		super(ctx, T_PAGO_PIE_ID, trxName);
		/**
		 * if (T_PAGO_PIE_ID == 0) { }
		 */
	}

	/** Load Constructor */
	public X_T_PAGO_PIE(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_PAGO_PIE */
	public static final String Table_Name = "T_PAGO_PIE";

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
		StringBuffer sb = new StringBuffer("X_T_PAGO_PIE[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Process Instance. Instance of the process
	 */
	public void setAD_PInstance_ID(int AD_PInstance_ID) {
		if (AD_PInstance_ID <= 0)
			set_Value("AD_PInstance_ID", null);
		else
			set_Value("AD_PInstance_ID", new Integer(AD_PInstance_ID));
	}

	/**
	 * Get Process Instance. Instance of the process
	 */
	public int getAD_PInstance_ID() {
		Integer ii = (Integer) get_Value("AD_PInstance_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Payment. Payment identifier
	 */
	public void setC_Payment_ID(int C_Payment_ID) {
		if (C_Payment_ID <= 0)
			set_Value("C_Payment_ID", null);
		else
			set_Value("C_Payment_ID", new Integer(C_Payment_ID));
	}

	/**
	 * Get Payment. Payment identifier
	 */
	public int getC_Payment_ID() {
		Integer ii = (Integer) get_Value("C_Payment_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set RETENCIONGANANCIAS */
	public void setRETENCIONGANANCIAS(BigDecimal RETENCIONGANANCIAS) {
		set_Value("RETENCIONGANANCIAS", RETENCIONGANANCIAS);
	}

	/** Get RETENCIONGANANCIAS */
	public BigDecimal getRETENCIONGANANCIAS() {
		BigDecimal bd = (BigDecimal) get_Value("RETENCIONGANANCIAS");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set RETENCIONIB */
	public void setRETENCIONIB(BigDecimal RETENCIONIB) {
		set_Value("RETENCIONIB", RETENCIONIB);
	}

	/** Get RETENCIONIB */
	public BigDecimal getRETENCIONIB() {
		BigDecimal bd = (BigDecimal) get_Value("RETENCIONIB");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set RETENCIONIVA */
	public void setRETENCIONIVA(BigDecimal RETENCIONIVA) {
		set_Value("RETENCIONIVA", RETENCIONIVA);
	}

	/** Get RETENCIONIVA */
	public BigDecimal getRETENCIONIVA() {
		BigDecimal bd = (BigDecimal) get_Value("RETENCIONIVA");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set RETENCIONUSS */
	public void setRETENCIONUSS(BigDecimal RETENCIONUSS) {
		set_Value("RETENCIONUSS", RETENCIONUSS);
	}

	/** Get RETENCIONUSS */
	public BigDecimal getRETENCIONUSS() {
		BigDecimal bd = (BigDecimal) get_Value("RETENCIONUSS");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set TOTAL */
	public void setTOTAL(BigDecimal TOTAL) {
		set_Value("TOTAL", TOTAL);
	}

	/** Get TOTAL */
	public BigDecimal getTOTAL() {
		BigDecimal bd = (BigDecimal) get_Value("TOTAL");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
