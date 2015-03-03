/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_PERCEPCIONES_SIFERE
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:05.109
 */
public class X_T_PERCEPCIONES_SIFERE extends PO {
	/** Standard Constructor */
	public X_T_PERCEPCIONES_SIFERE(Properties ctx,
			int T_PERCEPCIONES_SIFERE_ID, String trxName) {
		super(ctx, T_PERCEPCIONES_SIFERE_ID, trxName);
		/**
		 * if (T_PERCEPCIONES_SIFERE_ID == 0) { }
		 */
	}

	/** Load Constructor */
	public X_T_PERCEPCIONES_SIFERE(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_PERCEPCIONES_SIFERE */
	public static final String Table_Name = "T_PERCEPCIONES_SIFERE";

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
		StringBuffer sb = new StringBuffer("X_T_PERCEPCIONES_SIFERE[").append(
				get_ID()).append("]");
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

	/** Set CODIGO_JURISDICCION */
	public void setCODIGO_JURISDICCION(String CODIGO_JURISDICCION) {
		if (CODIGO_JURISDICCION != null && CODIGO_JURISDICCION.length() > 3) {
			log.warning("Length > 3 - truncated");
			CODIGO_JURISDICCION = CODIGO_JURISDICCION.substring(0, 2);
		}
		set_Value("CODIGO_JURISDICCION", CODIGO_JURISDICCION);
	}

	/** Get CODIGO_JURISDICCION */
	public String getCODIGO_JURISDICCION() {
		return (String) get_Value("CODIGO_JURISDICCION");
	}

	/** Set CONSTANCIA */
	public void setCONSTANCIA(String CONSTANCIA) {
		if (CONSTANCIA != null && CONSTANCIA.length() > 8) {
			log.warning("Length > 8 - truncated");
			CONSTANCIA = CONSTANCIA.substring(0, 7);
		}
		set_Value("CONSTANCIA", CONSTANCIA);
	}

	/** Get CONSTANCIA */
	public String getCONSTANCIA() {
		return (String) get_Value("CONSTANCIA");
	}

	/** Set CUIT */
	public void setCUIT(String CUIT) {
		if (CUIT != null && CUIT.length() > 13) {
			log.warning("Length > 13 - truncated");
			CUIT = CUIT.substring(0, 12);
		}
		set_Value("CUIT", CUIT);
	}

	/** Get CUIT */
	public String getCUIT() {
		return (String) get_Value("CUIT");
	}

	/** Set FECHA */
	public void setFECHA(Timestamp FECHA) {
		set_Value("FECHA", FECHA);
	}

	/** Get FECHA */
	public Timestamp getFECHA() {
		return (Timestamp) get_Value("FECHA");
	}

	/** Set IMPORTE */
	public void setIMPORTE(String IMPORTE) {
		if (IMPORTE != null && IMPORTE.length() > 11) {
			log.warning("Length > 11 - truncated");
			IMPORTE = IMPORTE.substring(0, 10);
		}
		set_Value("IMPORTE", IMPORTE);
	}

	/** Get IMPORTE */
	public String getIMPORTE() {
		return (String) get_Value("IMPORTE");
	}

	/** Set LETRA_COMP */
	public void setLETRA_COMP(String LETRA_COMP) {
		if (LETRA_COMP != null && LETRA_COMP.length() > 1) {
			log.warning("Length > 1 - truncated");
			LETRA_COMP = LETRA_COMP.substring(0, 0);
		}
		set_Value("LETRA_COMP", LETRA_COMP);
	}

	/** Get LETRA_COMP */
	public String getLETRA_COMP() {
		return (String) get_Value("LETRA_COMP");
	}

	/** Set SUCURSAL */
	public void setSUCURSAL(String SUCURSAL) {
		if (SUCURSAL != null && SUCURSAL.length() > 4) {
			log.warning("Length > 4 - truncated");
			SUCURSAL = SUCURSAL.substring(0, 3);
		}
		set_Value("SUCURSAL", SUCURSAL);
	}

	/** Get SUCURSAL */
	public String getSUCURSAL() {
		return (String) get_Value("SUCURSAL");
	}

	/** Set TIPO_COMP */
	public void setTIPO_COMP(String TIPO_COMP) {
		if (TIPO_COMP != null && TIPO_COMP.length() > 1) {
			log.warning("Length > 1 - truncated");
			TIPO_COMP = TIPO_COMP.substring(0, 0);
		}
		set_Value("TIPO_COMP", TIPO_COMP);
	}

	/** Get TIPO_COMP */
	public String getTIPO_COMP() {
		return (String) get_Value("TIPO_COMP");
	}
}
