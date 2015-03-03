/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * BISion 12/12/2008
 * 
 * @author santiago
 * 
 * Incorporado para MCOBRANZARET, verificar que Nro. Documento!=NULL.
 */
public class X_C_COBRANZA_RET extends PO {
	/** Standard Constructor */
	public X_C_COBRANZA_RET(Properties ctx, int C_CobranzaRet_ID, String trxName) {
		super(ctx, C_CobranzaRet_ID, trxName);
	}

	/** Load Constructor */
	public X_C_COBRANZA_RET(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_COBRANZA_RET */
	public static final String Table_Name = "C_COBRANZA_RET";

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
		StringBuffer sb = new StringBuffer("X_C_CobranzaRet[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	public void setC_CobranzaRet_ID(int C_CobranzaRet_ID) {
		if (C_CobranzaRet_ID < 1)
			throw new IllegalArgumentException("C_CobranzaRet_ID is mandatory.");
		set_Value("C_COBRANZA_RET_ID", new Integer(C_CobranzaRet_ID));
	}

	/** Get C_CobranzaRet_ID */
	public int getC_CobranzaRet_ID() {
		Integer ii = (Integer) get_Value("C_COBRANZA_RET_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public int getC_RegRetenRecib_ID() {
		Integer ii = (Integer) get_Value("C_REGRETEN_RECIB_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public void setC_RegRetenRecib_ID(int C_RegRetenRecib_ID) {
		if (C_RegRetenRecib_ID < 1)
			throw new IllegalArgumentException(
					"C_REGRETEN_RECIB_ID is mandatory.");
		set_Value("C_REGRETEN_RECIB_ID", new Integer(C_RegRetenRecib_ID));
	}

	/** Get IMPORTE */
	public BigDecimal getImporte() {
		BigDecimal bd = (BigDecimal) get_Value("IMPORTE");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set IMPORTE */
	public void setImporte(BigDecimal IMPORTE) {
		if (IMPORTE == null)
			throw new IllegalArgumentException("IMPORTE is mandatory.");
		set_Value("IMPORTE", IMPORTE);
	}

	public int getC_Payment_ID() {
		Integer ii = (Integer) get_Value("C_Payment_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public void setC_Payment_ID(int C_Payment_ID) {
		if (C_Payment_ID < 1)
			throw new IllegalArgumentException("C_Payment_ID is mandatory.");
		set_Value("C_Payment_ID", new Integer(C_Payment_ID));
	}

	public Timestamp getDateTrx() {
		return (Timestamp) get_Value("DATETRX");
	}

	public void setDateTrx(Timestamp date) {
		set_Value("DATETRX", date);
	}

	public String getDocumentNo() {
		return (String) get_Value("DocumentNo");
	}

	public void setDocumentNo(String DocumentNo) {
		if (DocumentNo == null)
			throw new IllegalArgumentException("DocumentNo is mandatory.");
		if (DocumentNo.length() > 30) {
			log.warning("Length > 30 - truncated");
			DocumentNo = DocumentNo.substring(0, 29);
		}
		set_ValueNoCheck("DocumentNo", DocumentNo);
	}
}
