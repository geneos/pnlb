/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.compiere.model;

import java.sql.*;
import java.math.BigDecimal;
import java.util.Properties;

import org.compiere.util.KeyNamePair;

/**
 * 
 * @author santiago
 */
public class X_M_LotCtl_Sincr extends PO {

	/** Load Constructor */
	public X_M_LotCtl_Sincr(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** Standard Constructor */
	public X_M_LotCtl_Sincr(Properties ctx, int M_LotCtl_Sincrt_ID,
			String trxName) {
		super(ctx, M_LotCtl_Sincrt_ID, trxName);
	}

	/** TableName=C_CONCILIACIONBANCARIA */
	public static final String Table_Name = "M_LotCtl_Sinc";

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
		StringBuffer sb = new StringBuffer("X_M_LotCtl_Sincr[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * M�todo que retorna si una secuencia de control de lotes est� siendo usada
	 * (no disponible) o no por un procedimiento.
	 * 
	 * @return
	 */
	public boolean isDisponible() {
		Object oo = get_Value("ISDISPONIBLE");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	public void setIsDisponible(boolean value) {
		set_Value("ISDISPONIBLE", new Boolean(value));
	}

	public int getM_LotCtl_ID() {
		Integer ii = (Integer) get_Value("M_LotCtl_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public void setM_LotCtl_ID(int value) {
		set_Value("M_LotCtl_ID", value);
	}

	public int getM_LotCtl_Sincr_ID() {
		Integer ii = (Integer) get_Value("M_LOTCTL_SINCR_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

}
