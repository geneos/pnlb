/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Charge
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.062
 */
public class X_C_Charge extends PO {
	/** Standard Constructor */
	public X_C_Charge(Properties ctx, int C_Charge_ID, String trxName) {
		super(ctx, C_Charge_ID, trxName);
		/**
		 * if (C_Charge_ID == 0) { setC_Charge_ID (0); setC_TaxCategory_ID (0);
		 * setChargeAmt (Env.ZERO); setISTAX (false); setIsSameCurrency (false);
		 * setIsSameTax (false); setIsTaxIncluded (false); // N setName (null); }
		 */
	}

	/** Load Constructor */
	public X_C_Charge(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Charge */
	public static final String Table_Name = "C_Charge";

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
		StringBuffer sb = new StringBuffer("X_C_Charge[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** C_BPartner_ID AD_Reference_ID=192 */
	public static final int C_BPARTNER_ID_AD_Reference_ID = 192;

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
	 * Set Charge. Additional document charges
	 */
	public void setC_Charge_ID(int C_Charge_ID) {
		if (C_Charge_ID < 1)
			throw new IllegalArgumentException("C_Charge_ID is mandatory.");
		set_ValueNoCheck("C_Charge_ID", new Integer(C_Charge_ID));
	}

	/**
	 * Get Charge. Additional document charges
	 */
	public int getC_Charge_ID() {
		Integer ii = (Integer) get_Value("C_Charge_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Tax Category. Tax Category
	 */
	public void setC_TaxCategory_ID(int C_TaxCategory_ID) {
		if (C_TaxCategory_ID < 1)
			throw new IllegalArgumentException("C_TaxCategory_ID is mandatory.");
		set_Value("C_TaxCategory_ID", new Integer(C_TaxCategory_ID));
	}

	/**
	 * Get Tax Category. Tax Category
	 */
	public int getC_TaxCategory_ID() {
		Integer ii = (Integer) get_Value("C_TaxCategory_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Charge amount. Charge Amount
	 */
	public void setChargeAmt(BigDecimal ChargeAmt) {
		if (ChargeAmt == null)
			throw new IllegalArgumentException("ChargeAmt is mandatory.");
		set_Value("ChargeAmt", ChargeAmt);
	}

	/**
	 * Get Charge amount. Charge Amount
	 */
	public BigDecimal getChargeAmt() {
		BigDecimal bd = (BigDecimal) get_Value("ChargeAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Description. Optional short description of the record
	 */
	public void setDescription(String Description) {
		if (Description != null && Description.length() > 255) {
			log.warning("Length > 255 - truncated");
			Description = Description.substring(0, 254);
		}
		set_Value("Description", Description);
	}

	/**
	 * Get Description. Optional short description of the record
	 */
	public String getDescription() {
		return (String) get_Value("Description");
	}

	/** Set ISCIF */
	public void setISCIF(boolean ISCIF) {
		set_Value("ISCIF", new Boolean(ISCIF));
	}

	/** Get ISCIF */
	public boolean isCIF() {
		Object oo = get_Value("ISCIF");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set ISTAX */
	public void setISTAX(boolean ISTAX) {
		set_Value("ISTAX", new Boolean(ISTAX));
	}

	/** Get ISTAX */
	public boolean isTAX() {
		Object oo = get_Value("ISTAX");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Same Currency */
	public void setIsSameCurrency(boolean IsSameCurrency) {
		set_Value("IsSameCurrency", new Boolean(IsSameCurrency));
	}

	/** Get Same Currency */
	public boolean isSameCurrency() {
		Object oo = get_Value("IsSameCurrency");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Same Tax. Use the same tax as the main transaction
	 */
	public void setIsSameTax(boolean IsSameTax) {
		set_Value("IsSameTax", new Boolean(IsSameTax));
	}

	/**
	 * Get Same Tax. Use the same tax as the main transaction
	 */
	public boolean isSameTax() {
		Object oo = get_Value("IsSameTax");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Price includes Tax. Tax is included in the price
	 */
	public void setIsTaxIncluded(boolean IsTaxIncluded) {
		set_Value("IsTaxIncluded", new Boolean(IsTaxIncluded));
	}

	/**
	 * Get Price includes Tax. Tax is included in the price
	 */
	public boolean isTaxIncluded() {
		Object oo = get_Value("IsTaxIncluded");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name == null)
			throw new IllegalArgumentException("Name is mandatory.");
		if (Name.length() > 60) {
			log.warning("Length > 60 - truncated");
			Name = Name.substring(0, 59);
		}
		set_Value("Name", Name);
	}

	/**
	 * Get Name. Alphanumeric identifier of the entity
	 */
	public String getName() {
		return (String) get_Value("Name");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getName());
	}

	/** TAXTYPE AD_Reference_ID=1000058 */
	public static final int TAXTYPE_AD_Reference_ID = 1000058;

	/** Ganancias = GAN */
	public static final String TAXTYPE_Ganancias = "GAN";

	/** Ingresos Brutos = IIB */
	public static final String TAXTYPE_IngresosBrutos = "IIB";

	/** IVA = IVA */
	public static final String TAXTYPE_IVA = "IVA";

	/** Set TAXTYPE */
	public void setTAXTYPE(String TAXTYPE) {
		if (TAXTYPE != null && TAXTYPE.length() > 3) {
			log.warning("Length > 3 - truncated");
			TAXTYPE = TAXTYPE.substring(0, 2);
		}
		set_Value("TAXTYPE", TAXTYPE);
	}

	/** Get TAXTYPE */
	public String getTAXTYPE() {
		return (String) get_Value("TAXTYPE");
	}

	/** Get ESPERCEPCION ADUANERA */
	public boolean ISPERCEPADUANERA() {
		Object oo = get_Value("ISPERCEPADUANERA");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set PERCEPCION ADUANERA */
	public void setISPERCEPADUANERA(boolean ISPERCEPADUANERA) {
		set_Value("ISPERCEPADUANERA", new Boolean(ISPERCEPADUANERA));
	}

	/** Set ESRETENCION BANCARIA */
	public void setISRETBANK(boolean ISRETBANK) {
		set_Value("ISRETBANK", new Boolean(ISRETBANK));
	}

	/** Get ESRETENCION BANCARIA */
	public boolean ISRETBANK() {
		Object oo = get_Value("ISRETBANK");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set ISCREDITOFISCAL */
	public void setISCREDITOFISCAL(boolean ISCREDITOFISCAL) {
		set_Value("ISCREDITOFISCAL", new Boolean(ISCREDITOFISCAL));
	}

	/** Get ISCREDITOFISCAL */
	public boolean ISCREDITOFISCAL() {
		Object oo = get_Value("ISCREDITOFISCAL");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set ISMONTOFIJO */
	public void setISMONTOFIJO(boolean ISMONTOFIJO) {
		set_Value("ISMONTOFIJO", new Boolean(ISMONTOFIJO));
	}

	/** Get ISMONTOFIJO */
	public boolean ISMONTOFIJO() {
		Object oo = get_Value("ISMONTOFIJO");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set ISPORCENTAJE */
	public void setISPORCENTAJE(boolean ISPORCENTAJE) {
		set_Value("ISPORCENTAJE", new Boolean(ISPORCENTAJE));
	}

	/** Get ISPORCENTAJE */
	public boolean ISPORCENTAJE() {
		Object oo = get_Value("ISPORCENTAJE");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set MONTOMAXIMO.
	 */
	public void setMONTOMAXIMO(BigDecimal MONTOMAXIMO) {
		set_ValueNoCheck("MONTOMAXIMO", MONTOMAXIMO);
	}

	/** Get MONTOMAXIMO */
	public BigDecimal getMONTOMAXIMO() {
		BigDecimal bd = (BigDecimal) get_Value("MONTOMAXIMO");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set PORCENTAJE.
	 */
	public void setPORCENTAJE(BigDecimal PORCENTAJE) {
		set_ValueNoCheck("PORCENTAJE", PORCENTAJE);
	}

	/** Get PORCENTAJE */
	public BigDecimal getPORCENTAJE() {
		BigDecimal bd = (BigDecimal) get_Value("PORCENTAJE");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
