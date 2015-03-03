/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Regim_Reten_Percep_Recib
 * 
 * @author BISION - Mat�as Maenza
 * @version 1.0
 * @version Release 2.5.3b - 2008-04-04 14:06:00.359
 */
public class X_C_Regim_Reten_Percep_Recib extends PO {
	/** Standard Constructor */
	public X_C_Regim_Reten_Percep_Recib(Properties ctx,
			int C_Regim_Reten_Percep_Recib_ID, String trxName) {
		super(ctx, C_Regim_Reten_Percep_Recib_ID, trxName);
		/**
		 * if (C_Regim_Reten_Percep_Recib_ID == 0) {
		 * setC_REGIM_RETEN_PERCEP_RECIB_ID (0); setValue (null); }
		 */
	}

	/** Load Constructor */
	public X_C_Regim_Reten_Percep_Recib(Properties ctx, ResultSet rs,
			String trxName) {
		super(ctx, rs, trxName);
	}

	/** AD_Table_ID=1000239 */

	/** TableName=C_Regim_Reten_Percep_Recib */
	public static final String Table_Name = "C_Regim_Reten_Percep_Recib";

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
		StringBuffer sb = new StringBuffer("X_C_Regim_Reten_Percep_Recib[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/** Set CODIGO_REGIMEN */
	public void setCODIGO_REGIMEN(String CODIGO_REGIMEN) {
		if (CODIGO_REGIMEN != null && CODIGO_REGIMEN.length() > 6) {
			log.warning("Length > 6 - truncated");
			CODIGO_REGIMEN = CODIGO_REGIMEN.substring(0, 5);
		}
		set_Value("CODIGO_REGIMEN", CODIGO_REGIMEN);
	}

	/** Get CODIGO_REGIMEN */
	public String getCODIGO_REGIMEN() {
		return (String) get_Value("CODIGO_REGIMEN");
	}

	/** Set C_REGIM_RETEN_PERCEP_RECIB_ID */
	public void setC_REGIM_RETEN_PERCEP_RECIB_ID(
			int C_REGIM_RETEN_PERCEP_RECIB_ID) {
		if (C_REGIM_RETEN_PERCEP_RECIB_ID < 1)
			throw new IllegalArgumentException(
					"C_REGIM_RETEN_PERCEP_RECIB_ID is mandatory.");
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

	/** Set DESCRIPCION */
	public void setDESCRIPCION(String DESCRIPCION) {
		if (DESCRIPCION != null && DESCRIPCION.length() > 30) {
			log.warning("Length > 30 - truncated");
			DESCRIPCION = DESCRIPCION.substring(0, 29);
		}
		set_Value("DESCRIPCION", DESCRIPCION);
	}

	/** Get DESCRIPCION */
	public String getDESCRIPCION() {
		return (String) get_Value("DESCRIPCION");
	}

	/** Set ESPERCEPCION */
	public void setESPERCEPCION(boolean ESPERCEPCION) {
		set_Value("ESPERCEPCION", new Boolean(ESPERCEPCION));
	}

	/** Get ESPERCEPCION */
	public boolean ESPERCEPCION() {
		Object oo = get_Value("ESPERCEPCION");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set ESRETENCION */
	public void setESRETENCION(boolean ESRETENCION) {
		set_Value("ESRETENCION", new Boolean(ESRETENCION));
	}

	/** Get ESRETENCION */
	public boolean ESRETENCION() {
		Object oo = get_Value("ESRETENCION");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** TIPO_PERCEP AD_Reference_ID=1000058 */
	public static final int TIPO_PERCEP_AD_Reference_ID = 1000058;

	/** Ganancias = GAN */
	public static final String TIPO_PERCEP_Ganancias = "GAN";

	/** Ingresos Brutos = IIB */
	public static final String TIPO_PERCEP_IngresosBrutos = "IIB";

	/** IVA = IVA */
	public static final String TIPO_PERCEP_IVA = "IVA";

	/** Set TIPO_PERCEP */
	public void setTIPO_PERCEP(String TIPO_PERCEP) {
		if (TIPO_PERCEP != null && TIPO_PERCEP.length() > 3) {
			log.warning("Length > 3 - truncated");
			TIPO_PERCEP = TIPO_PERCEP.substring(0, 2);
		}
		set_Value("TIPO_PERCEP", TIPO_PERCEP);
	}

	/** Get TIPO_PERCEP */
	public String getTIPO_PERCEP() {
		return (String) get_Value("TIPO_PERCEP");
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
		if (TIPO_RET != null && TIPO_RET.length() > 1) {
			log.warning("Length > 1 - truncated");
			TIPO_RET = TIPO_RET.substring(0, 0);
		}
		set_Value("TIPO_RET", TIPO_RET);
	}

	/** Get TIPO_RET */
	public String getTIPO_RET() {
		return (String) get_Value("TIPO_RET");
	}

	/** T_CODIGOJURISDICCION_ID AD_Reference_ID=1000064 */
	public static final int T_CODIGOJURISDICCION_ID_AD_Reference_ID = 1000064;

	/** 901 = 901 */
	public static final String T_CODIGOJURISDICCION_ID_901 = "901";

	/** 902 = 902 */
	public static final String T_CODIGOJURISDICCION_ID_902 = "902";

	/** 903 = 903 */
	public static final String T_CODIGOJURISDICCION_ID_903 = "903";

	/** 904 = 904 */
	public static final String T_CODIGOJURISDICCION_ID_904 = "904";

	/** 905 = 905 */
	public static final String T_CODIGOJURISDICCION_ID_905 = "905";

	/** 906 = 906 */
	public static final String T_CODIGOJURISDICCION_ID_906 = "906";

	/** 907 = 907 */
	public static final String T_CODIGOJURISDICCION_ID_907 = "907";

	/** 908 = 908 */
	public static final String T_CODIGOJURISDICCION_ID_908 = "908";

	/** 909 = 909 */
	public static final String T_CODIGOJURISDICCION_ID_909 = "909";

	/** 910 = 910 */
	public static final String T_CODIGOJURISDICCION_ID_910 = "910";

	/** 911 = 911 */
	public static final String T_CODIGOJURISDICCION_ID_911 = "911";

	/** 912 = 912 */
	public static final String T_CODIGOJURISDICCION_ID_912 = "912";

	/** 913 = 913 */
	public static final String T_CODIGOJURISDICCION_ID_913 = "913";

	/** 914 = 914 */
	public static final String T_CODIGOJURISDICCION_ID_914 = "914";

	/** 915 = 915 */
	public static final String T_CODIGOJURISDICCION_ID_915 = "915";

	/** 916 = 916 */
	public static final String T_CODIGOJURISDICCION_ID_916 = "916";

	/** 917 = 917 */
	public static final String T_CODIGOJURISDICCION_ID_917 = "917";

	/** 918 = 918 */
	public static final String T_CODIGOJURISDICCION_ID_918 = "918";

	/** 919 = 919 */
	public static final String T_CODIGOJURISDICCION_ID_919 = "919";

	/** 920 = 920 */
	public static final String T_CODIGOJURISDICCION_ID_920 = "920";

	/** 921 = 921 */
	public static final String T_CODIGOJURISDICCION_ID_921 = "921";

	/** 922 = 922 */
	public static final String T_CODIGOJURISDICCION_ID_922 = "922";

	/** 923 = 923 */
	public static final String T_CODIGOJURISDICCION_ID_923 = "923";

	/** 924 = 924 */
	public static final String T_CODIGOJURISDICCION_ID_924 = "924";

	/** Set T_CODIGOJURISDICCION_ID */
	public void setT_CODIGOJURISDICCION_ID(String T_CODIGOJURISDICCION_ID) {
		if (T_CODIGOJURISDICCION_ID != null
				&& T_CODIGOJURISDICCION_ID.length() > 4) {
			log.warning("Length > 4 - truncated");
			T_CODIGOJURISDICCION_ID = T_CODIGOJURISDICCION_ID.substring(0, 3);
		}
		set_Value("T_CODIGOJURISDICCION_ID", T_CODIGOJURISDICCION_ID);
	}

	/** Get T_CODIGOJURISDICCION_ID */
	public String getT_CODIGOJURISDICCION_ID() {
		return (String) get_Value("T_CODIGOJURISDICCION_ID");
	}

	/**
	 * Set Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public void setValue(String Value) {
		if (Value == null)
			throw new IllegalArgumentException("Value is mandatory.");
		if (Value.length() > 40) {
			log.warning("Length > 40 - truncated");
			Value = Value.substring(0, 39);
		}
		set_Value("Value", Value);
	}

	/**
	 * Get Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public String getValue() {
		return (String) get_Value("Value");
	}

	/** Set NRO_REGIM */
	public void setNRO_REGIM(int NRO_REGIM) {
		if (NRO_REGIM < 1 || NRO_REGIM > 999)
			throw new IllegalArgumentException(
					"Numero de Regimen debe ser de tres digitos");
		set_ValueNoCheck("NRO_REGIM", new Integer(NRO_REGIM));
	}

	/** Get NRO_REGIM */
	public int getNRO_REGIM() {
		Integer ii = (Integer) get_Value("NRO_REGIM");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

}
