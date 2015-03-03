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
public class X_C_JURISDICCION extends PO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Standard Constructor */
	public X_C_JURISDICCION(Properties ctx, int C_Jurisdiccion_ID,
			String trxName) {
		super(ctx, C_Jurisdiccion_ID, trxName);
	}

	/** Load Constructor */
	public X_C_JURISDICCION(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_JURISDICCION */
	public static final String Table_Name = "C_JURISDICCION";

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
		StringBuffer sb = new StringBuffer("X_C_JURISDICCION[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	public void setC_Jurisdiccion_ID(int C_Jurisdiccion_ID) {
		if (C_Jurisdiccion_ID < 1)
			throw new IllegalArgumentException(
					"C_Jurisdiccion_ID is mandatory.");
		set_Value("C_JURISDICCION_ID", new Integer(C_Jurisdiccion_ID));
	}

	/** Get C_Jurisdiccion_ID */
	public int getC_Jurisdiccion_ID() {
		Integer ii = (Integer) get_Value("C_JURISDICCION_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Get NAME JURISDICCION */
	public String getName() {
		return (String) get_Value("Name");
	}

}
