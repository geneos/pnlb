/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_COBRANZA_CABECERA
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.89
 */
public class X_T_COBRANZA_CABECERA extends PO {
	/** Standard Constructor */
	public X_T_COBRANZA_CABECERA(Properties ctx, int T_COBRANZA_CABECERA_ID,
			String trxName) {
		super(ctx, T_COBRANZA_CABECERA_ID, trxName);
		/**
		 * if (T_COBRANZA_CABECERA_ID == 0) { }
		 */
	}

	/** Load Constructor */
	public X_T_COBRANZA_CABECERA(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_COBRANZA_CABECERA */
	public static final String Table_Name = "T_COBRANZA_CABECERA";

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
		StringBuffer sb = new StringBuffer("X_T_COBRANZA_CABECERA[").append(
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

	/** Set CPCUI */
	public void setCPCUI(String CPCUI) {
		if (CPCUI != null && CPCUI.length() > 100) {
			log.warning("Length > 100 - truncated");
			CPCUI = CPCUI.substring(0, 99);
		}
		set_Value("CPCUI", CPCUI);
	}

	/** Get CPCUI */
	public String getCPCUI() {
		return (String) get_Value("CPCUI");
	}

	/** Set CPCUIPROV */
	public void setCPCUIPROV(String CPCUIPROV) {
		if (CPCUIPROV != null && CPCUIPROV.length() > 100) {
			log.warning("Length > 100 - truncated");
			CPCUIPROV = CPCUIPROV.substring(0, 99);
		}
		set_Value("CPCUIPROV", CPCUIPROV);
	}

	/** Get CPCUIPROV */
	public String getCPCUIPROV() {
		return (String) get_Value("CPCUIPROV");
	}

	/** Set CUIT */
	public void setCUIT(String CUIT) {
		if (CUIT != null && CUIT.length() > 40) {
			log.warning("Length > 40 - truncated");
			CUIT = CUIT.substring(0, 39);
		}
		set_Value("CUIT", CUIT);
	}

	/** Get CUIT */
	public String getCUIT() {
		return (String) get_Value("CUIT");
	}

	/** Set CUITPROV */
	public void setCUITPROV(String CUITPROV) {
		if (CUITPROV != null && CUITPROV.length() > 40) {
			log.warning("Length > 40 - truncated");
			CUITPROV = CUITPROV.substring(0, 39);
		}
		set_Value("CUITPROV", CUITPROV);
	}

	/** Get CUITPROV */
	public String getCUITPROV() {
		return (String) get_Value("CUITPROV");
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

	/** Set DIR */
	public void setDIR(String DIR) {
		if (DIR != null && DIR.length() > 60) {
			log.warning("Length > 60 - truncated");
			DIR = DIR.substring(0, 59);
		}
		set_Value("DIR", DIR);
	}

	/** Get DIR */
	public String getDIR() {
		return (String) get_Value("DIR");
	}

	/** Set DIRPROV */
	public void setDIRPROV(String DIRPROV) {
		if (DIRPROV != null && DIRPROV.length() > 100) {
			log.warning("Length > 100 - truncated");
			DIRPROV = DIRPROV.substring(0, 99);
		}
		set_Value("DIRPROV", DIRPROV);
	}

	/** Get DIRPROV */
	public String getDIRPROV() {
		return (String) get_Value("DIRPROV");
	}

	/** Set FECHA */
	public void setFECHA(String FECHA) {
		if (FECHA != null && FECHA.length() > 7) {
			log.warning("Length > 7 - truncated");
			FECHA = FECHA.substring(0, 6);
		}
		set_Value("FECHA", FECHA);
	}

	/** Get FECHA */
	public String getFECHA() {
		return (String) get_Value("FECHA");
	}

	/** Set IDPROV */
	public void setIDPROV(String IDPROV) {
		if (IDPROV != null && IDPROV.length() > 60) {
			log.warning("Length > 60 - truncated");
			IDPROV = IDPROV.substring(0, 59);
		}
		set_Value("IDPROV", IDPROV);
	}

	/** Get IDPROV */
	public String getIDPROV() {
		return (String) get_Value("IDPROV");
	}

	/** Set IIBB */
	public void setIIBB(String IIBB) {
		if (IIBB != null && IIBB.length() > 30) {
			log.warning("Length > 30 - truncated");
			IIBB = IIBB.substring(0, 29);
		}
		set_Value("IIBB", IIBB);
	}

	/** Get IIBB */
	public String getIIBB() {
		return (String) get_Value("IIBB");
	}

	/** Set LEYENDA1 */
	public void setLEYENDA1(String LEYENDA1) {
		if (LEYENDA1 != null && LEYENDA1.length() > 255) {
			log.warning("Length > 255 - truncated");
			LEYENDA1 = LEYENDA1.substring(0, 254);
		}
		set_Value("LEYENDA1", LEYENDA1);
	}

	/** Get LEYENDA1 */
	public String getLEYENDA1() {
		return (String) get_Value("LEYENDA1");
	}

	/** Set LEYENDA3 */
	public void setLEYENDA3(String LEYENDA3) {
		if (LEYENDA3 != null && LEYENDA3.length() > 1000) {
			log.warning("Length > 1000 - truncated");
			LEYENDA3 = LEYENDA3.substring(0, 999);
		}
		set_Value("LEYENDA3", LEYENDA3);
	}

	/** Get LEYENDA3 */
	public String getLEYENDA3() {
		return (String) get_Value("LEYENDA3");
	}

	/** Set NOMBRE */
	public void setNOMBRE(String NOMBRE) {
		if (NOMBRE != null && NOMBRE.length() > 70) {
			log.warning("Length > 70 - truncated");
			NOMBRE = NOMBRE.substring(0, 69);
		}
		set_Value("NOMBRE", NOMBRE);
	}

	/** Get NOMBRE */
	public String getNOMBRE() {
		return (String) get_Value("NOMBRE");
	}

	/** Set NRO */
	public void setNRO(String NRO) {
		if (NRO != null && NRO.length() > 30) {
			log.warning("Length > 30 - truncated");
			NRO = NRO.substring(0, 29);
		}
		set_Value("NRO", NRO);
	}

	/** Get NRO */
	public String getNRO() {
		return (String) get_Value("NRO");
	}

	/** Set PAIS */
	public void setPAIS(String PAIS) {
		if (PAIS != null && PAIS.length() > 255) {
			log.warning("Length > 255 - truncated");
			PAIS = PAIS.substring(0, 254);
		}
		set_Value("PAIS", PAIS);
	}

	/** Get PAIS */
	public String getPAIS() {
		return (String) get_Value("PAIS");
	}

	/** Set PAISPR */
	public void setPAISPR(String PAISPR) {
		if (PAISPR != null && PAISPR.length() > 370) {
			log.warning("Length > 370 - truncated");
			PAISPR = PAISPR.substring(0, 369);
		}
		set_Value("PAISPR", PAISPR);
	}

	/** Get PAISPR */
	public String getPAISPR() {
		return (String) get_Value("PAISPR");
	}

	/** Set PROVEEDOR */
	public void setPROVEEDOR(String PROVEEDOR) {
		if (PROVEEDOR != null && PROVEEDOR.length() > 100) {
			log.warning("Length > 100 - truncated");
			PROVEEDOR = PROVEEDOR.substring(0, 99);
		}
		set_Value("PROVEEDOR", PROVEEDOR);
	}

	/** Get PROVEEDOR */
	public String getPROVEEDOR() {
		return (String) get_Value("PROVEEDOR");
	}

	/** Set leyenda2 */
	public void setleyenda2(String leyenda2) {
		if (leyenda2 != null && leyenda2.length() > 2500) {
			log.warning("Length > 2500 - truncated");
			leyenda2 = leyenda2.substring(0, 2499);
		}
		set_Value("leyenda2", leyenda2);
	}

	/** Get leyenda2 */
	public String getleyenda2() {
		return (String) get_Value("leyenda2");
	}
}
