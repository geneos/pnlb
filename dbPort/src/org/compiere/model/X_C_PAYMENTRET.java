/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_PAYMENTRET
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.875
 */
public class X_C_PAYMENTRET extends PO {
	/** Standard Constructor */
	public X_C_PAYMENTRET(Properties ctx, int C_PAYMENTRET_ID, String trxName) {
		super(ctx, C_PAYMENTRET_ID, trxName);
		/**
		 * if (C_PAYMENTRET_ID == 0) { setC_DocType_ID (0); setC_PAYMENTRET_ID
		 * (0); setDateTrx (new Timestamp(System.currentTimeMillis()));
		 * setDocumentNo (null); setTIPO_RET (null); }
		 */
	}

	/** Load Constructor */
	public X_C_PAYMENTRET(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_PAYMENTRET */
	public static final String Table_Name = "C_PAYMENTRET";

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
		StringBuffer sb = new StringBuffer("X_C_PAYMENTRET[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Document Type. Document type or rules
	 */
	public void setC_DocType_ID(int C_DocType_ID) {
		if (C_DocType_ID < 0)
			throw new IllegalArgumentException("C_DocType_ID is mandatory.");
		set_ValueNoCheck("C_DocType_ID", new Integer(C_DocType_ID));
	}

	/**
	 * Get Document Type. Document type or rules
	 */
	public int getC_DocType_ID() {
		Integer ii = (Integer) get_Value("C_DocType_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set C_PAYMENTRET_ID */
	public void setC_PAYMENTRET_ID(int C_PAYMENTRET_ID) {
		if (C_PAYMENTRET_ID < 1)
			throw new IllegalArgumentException("C_PAYMENTRET_ID is mandatory.");
		set_ValueNoCheck("C_PAYMENTRET_ID", new Integer(C_PAYMENTRET_ID));
	}

	/** Get C_PAYMENTRET_ID */
	public int getC_PAYMENTRET_ID() {
		Integer ii = (Integer) get_Value("C_PAYMENTRET_ID");
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

	/** C_REGIM_RETEN_PERCEP_RECIB_ID AD_Reference_ID=1000066 */
	public static final int C_REGIM_RETEN_PERCEP_RECIB_ID_AD_Reference_ID = 1000066;

	/** Set C_REGIM_RETEN_PERCEP_RECIB_ID */
	public void setC_REGIM_RETEN_PERCEP_RECIB_ID(
			int C_REGIM_RETEN_PERCEP_RECIB_ID) {
		if (C_REGIM_RETEN_PERCEP_RECIB_ID <= 0)
			set_ValueNoCheck("C_REGIM_RETEN_PERCEP_RECIB_ID", null);
		else
			set_ValueNoCheck("C_REGIM_RETEN_PERCEP_RECIB_ID", new Integer(
					C_REGIM_RETEN_PERCEP_RECIB_ID));
	}

	/** Get C_REGIM_RETEN_PERCEP_RECIB_ID */
	public int getC_REGIM_RETEN_PERCEP_RECIB_ID() {
		Integer ii = (Integer) get_Value("C_REGIM_RETEN_PERCEP_RECIB_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Transaction Date. Transaction Date
	 */
	public void setDateTrx(Timestamp DateTrx) {
		if (DateTrx == null)
			throw new IllegalArgumentException("DateTrx is mandatory.");
		set_Value("DateTrx", DateTrx);
	}

	/**
	 * Get Transaction Date. Transaction Date
	 */
	public Timestamp getDateTrx() {
		return (Timestamp) get_Value("DateTrx");
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
		set_Value("DocumentNo", DocumentNo);
	}

	/**
	 * Get Document No. Document sequence number of the document
	 */
	public String getDocumentNo() {
		return (String) get_Value("DocumentNo");
	}

	/** Set ALICUOTA */
	public void setALICUOTA(BigDecimal ALICUOTA) {
		set_Value("ALICUOTA", ALICUOTA);
	}

	/** Set IMPORTE */
	public void setIMPORTE(BigDecimal IMPORTE) {
		set_Value("IMPORTE", IMPORTE);
	}

	/** Get IMPORTE */
	public BigDecimal getIMPORTE() {
		BigDecimal bd = (BigDecimal) get_Value("IMPORTE");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** TIPO_OPERACION AD_Reference_ID=1000059 */
	public static final int TIPO_OPERACION_AD_Reference_ID = 1000059;

	/** Cobranza = C */
	public static final String TIPO_OPERACION_Cobranza = "C";

	/** Pago = P */
	public static final String TIPO_OPERACION_Pago = "P";

	/** Set TIPO_OPERACION */
	public void setTIPO_OPERACION(String TIPO_OPERACION) {
		if (TIPO_OPERACION != null && TIPO_OPERACION.length() > 1) {
			log.warning("Length > 1 - truncated");
			TIPO_OPERACION = TIPO_OPERACION.substring(0, 0);
		}
		set_Value("TIPO_OPERACION", TIPO_OPERACION);
	}

	/** Get TIPO_OPERACION */
	public String getTIPO_OPERACION() {
		return (String) get_Value("TIPO_OPERACION");
	}

	/** TIPO_RET AD_Reference_ID=1000060 */
	public static final int TIPO_RET_AD_Reference_ID = 1000060;

	/** IB = B */
	public static final String TIPO_RET_IB = "B";

	/** Ganancias = G */
	public static final String TIPO_RET_Ganancias = "G";

	/** IVA = I */
	public static final String TIPO_RET_IVA = "I";

	/** SUSS = S */
	public static final String TIPO_RET_SUSS = "S";

	/** Set TIPO_RET */
	public void setTIPO_RET(String TIPO_RET) {
		if (TIPO_RET == null)
			throw new IllegalArgumentException("TIPO_RET is mandatory");
		if (TIPO_RET.equals("B") || TIPO_RET.equals("G")
				|| TIPO_RET.equals("I") || TIPO_RET.equals("S"))
			;
		else
			throw new IllegalArgumentException("TIPO_RET Invalid value - "
					+ TIPO_RET + " - Reference_ID=1000060 - B - G - I - S");
		if (TIPO_RET.length() > 1) {
			log.warning("Length > 1 - truncated");
			TIPO_RET = TIPO_RET.substring(0, 0);
		}
		set_Value("TIPO_RET", TIPO_RET);
	}

	/** Get TIPO_RET */
	public String getTIPO_RET() {
		return (String) get_Value("TIPO_RET");
	}
}
