/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_INTERFACE_SICORE
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.953
 */
public class X_T_INTERFACE_SICORE extends PO {
	/** Standard Constructor */
	public X_T_INTERFACE_SICORE(Properties ctx, int T_INTERFACE_SICORE_ID,
			String trxName) {
		super(ctx, T_INTERFACE_SICORE_ID, trxName);
		/**
		 * if (T_INTERFACE_SICORE_ID == 0) { }
		 */
	}

	/** Load Constructor */
	public X_T_INTERFACE_SICORE(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_INTERFACE_SICORE */
	public static final String Table_Name = "T_INTERFACE_SICORE";

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
		StringBuffer sb = new StringBuffer("X_T_INTERFACE_SICORE[").append(
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

	/** Set BASE_CALCULO */
	public void setBASE_CALCULO(String BASE_CALCULO) {
		if (BASE_CALCULO != null && BASE_CALCULO.length() > 22) {
			log.warning("Length > 22 - truncated");
			BASE_CALCULO = BASE_CALCULO.substring(0, 21);
		}
		set_Value("BASE_CALCULO", BASE_CALCULO);
	}

	/** Get BASE_CALCULO */
	public String getBASE_CALCULO() {
		return (String) get_Value("BASE_CALCULO");
	}

	/** Set CODE_COMP */
	public void setCODE_COMP(String CODE_COMP) {
		if (CODE_COMP != null && CODE_COMP.length() > 2) {
			log.warning("Length > 2 - truncated");
			CODE_COMP = CODE_COMP.substring(0, 1);
		}
		set_Value("CODE_COMP", CODE_COMP);
	}

	/** Get CODE_COMP */
	public String getCODE_COMP() {
		return (String) get_Value("CODE_COMP");
	}

	/** Set CODE_CONDICION */
	public void setCODE_CONDICION(String CODE_CONDICION) {
		if (CODE_CONDICION != null && CODE_CONDICION.length() > 2) {
			log.warning("Length > 2 - truncated");
			CODE_CONDICION = CODE_CONDICION.substring(0, 1);
		}
		set_Value("CODE_CONDICION", CODE_CONDICION);
	}

	/** Get CODE_CONDICION */
	public String getCODE_CONDICION() {
		return (String) get_Value("CODE_CONDICION");
	}

	/** Set CODE_IMPUESTO */
	public void setCODE_IMPUESTO(String CODE_IMPUESTO) {
		if (CODE_IMPUESTO != null && CODE_IMPUESTO.length() > 3) {
			log.warning("Length > 3 - truncated");
			CODE_IMPUESTO = CODE_IMPUESTO.substring(0, 2);
		}
		set_Value("CODE_IMPUESTO", CODE_IMPUESTO);
	}

	/** Get CODE_IMPUESTO */
	public String getCODE_IMPUESTO() {
		return (String) get_Value("CODE_IMPUESTO");
	}

	/** Set CODE_OP */
	public void setCODE_OP(String CODE_OP) {
		if (CODE_OP != null && CODE_OP.length() > 1) {
			log.warning("Length > 1 - truncated");
			CODE_OP = CODE_OP.substring(0, 0);
		}
		set_Value("CODE_OP", CODE_OP);
	}

	/** Get CODE_OP */
	public String getCODE_OP() {
		return (String) get_Value("CODE_OP");
	}

	/** Set CODE_REGIMEN */
	public void setCODE_REGIMEN(String CODE_REGIMEN) {
		if (CODE_REGIMEN != null && CODE_REGIMEN.length() > 3) {
			log.warning("Length > 3 - truncated");
			CODE_REGIMEN = CODE_REGIMEN.substring(0, 2);
		}
		set_Value("CODE_REGIMEN", CODE_REGIMEN);
	}

	/** Get CODE_REGIMEN */
	public String getCODE_REGIMEN() {
		return (String) get_Value("CODE_REGIMEN");
	}

	/** Set FECHA_EMISION_BOLETIN */
	public void setFECHA_EMISION_BOLETIN(String FECHA_EMISION_BOLETIN) {
		if (FECHA_EMISION_BOLETIN != null && FECHA_EMISION_BOLETIN.length() > 7) {
			log.warning("Length > 7 - truncated");
			FECHA_EMISION_BOLETIN = FECHA_EMISION_BOLETIN.substring(0, 6);
		}
		set_Value("FECHA_EMISION_BOLETIN", FECHA_EMISION_BOLETIN);
	}

	/** Get FECHA_EMISION_BOLETIN */
	public String getFECHA_EMISION_BOLETIN() {
		return (String) get_Value("FECHA_EMISION_BOLETIN");
	}

	/** Set FECHA_EMISION_COMP */
	public void setFECHA_EMISION_COMP(Timestamp FECHA_EMISION_COMP) {
		set_Value("FECHA_EMISION_COMP", FECHA_EMISION_COMP);
	}

	/** Get FECHA_EMISION_COMP */
	public Timestamp getFECHA_EMISION_COMP() {
		return (Timestamp) get_Value("FECHA_EMISION_COMP");
	}

	/** Set FECHA_EMISION_RET */
	public void setFECHA_EMISION_RET(Timestamp FECHA_EMISION_RET) {
		set_Value("FECHA_EMISION_RET", FECHA_EMISION_RET);
	}

	/** Get FECHA_EMISION_RET */
	public Timestamp getFECHA_EMISION_RET() {
		return (Timestamp) get_Value("FECHA_EMISION_RET");
	}

	/** Set IMPORTE_COMP */
	public void setIMPORTE_COMP(String IMPORTE_COMP) {
		if (IMPORTE_COMP != null && IMPORTE_COMP.length() > 22) {
			log.warning("Length > 22 - truncated");
			IMPORTE_COMP = IMPORTE_COMP.substring(0, 21);
		}
		set_Value("IMPORTE_COMP", IMPORTE_COMP);
	}

	/** Get IMPORTE_COMP */
	public String getIMPORTE_COMP() {
		return (String) get_Value("IMPORTE_COMP");
	}

	/** Set IMPORTE_RETENCION */
	public void setIMPORTE_RETENCION(String IMPORTE_RETENCION) {
		if (IMPORTE_RETENCION != null && IMPORTE_RETENCION.length() > 22) {
			log.warning("Length > 22 - truncated");
			IMPORTE_RETENCION = IMPORTE_RETENCION.substring(0, 21);
		}
		set_Value("IMPORTE_RETENCION", IMPORTE_RETENCION);
	}

	/** Get IMPORTE_RETENCION */
	public String getIMPORTE_RETENCION() {
		return (String) get_Value("IMPORTE_RETENCION");
	}

	/** Set NRO_CERTIFICADO */
	public void setNRO_CERTIFICADO(String NRO_CERTIFICADO) {
		if (NRO_CERTIFICADO != null && NRO_CERTIFICADO.length() > 22) {
			log.warning("Length > 22 - truncated");
			NRO_CERTIFICADO = NRO_CERTIFICADO.substring(0, 21);
		}
		set_Value("NRO_CERTIFICADO", NRO_CERTIFICADO);
	}

	/** Get NRO_CERTIFICADO */
	public String getNRO_CERTIFICADO() {
		return (String) get_Value("NRO_CERTIFICADO");
	}

	/** Set NRO_COMPROBANTE */
	public void setNRO_COMPROBANTE(String NRO_COMPROBANTE) {
		if (NRO_COMPROBANTE != null && NRO_COMPROBANTE.length() > 16) {
			log.warning("Length > 16 - truncated");
			NRO_COMPROBANTE = NRO_COMPROBANTE.substring(0, 15);
		}
		set_Value("NRO_COMPROBANTE", NRO_COMPROBANTE);
	}

	/** Get NRO_COMPROBANTE */
	public String getNRO_COMPROBANTE() {
		return (String) get_Value("NRO_COMPROBANTE");
	}

	/** Set NRO_DOC_RET */
	public void setNRO_DOC_RET(String NRO_DOC_RET) {
		if (NRO_DOC_RET != null && NRO_DOC_RET.length() > 20) {
			log.warning("Length > 20 - truncated");
			NRO_DOC_RET = NRO_DOC_RET.substring(0, 19);
		}
		set_Value("NRO_DOC_RET", NRO_DOC_RET);
	}

	/** Get NRO_DOC_RET */
	public String getNRO_DOC_RET() {
		return (String) get_Value("NRO_DOC_RET");
	}

	/** Set PORCENTAJE */
	public void setPORCENTAJE(String PORCENTAJE) {
		if (PORCENTAJE != null && PORCENTAJE.length() > 22) {
			log.warning("Length > 22 - truncated");
			PORCENTAJE = PORCENTAJE.substring(0, 21);
		}
		set_Value("PORCENTAJE", PORCENTAJE);
	}

	/** Get PORCENTAJE */
	public String getPORCENTAJE() {
		return (String) get_Value("PORCENTAJE");
	}

	/** Set TIPO_DOC_RET */
	public void setTIPO_DOC_RET(BigDecimal TIPO_DOC_RET) {
		set_Value("TIPO_DOC_RET", TIPO_DOC_RET);
	}

	/** Get TIPO_DOC_RET */
	public BigDecimal getTIPO_DOC_RET() {
		BigDecimal bd = (BigDecimal) get_Value("TIPO_DOC_RET");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
