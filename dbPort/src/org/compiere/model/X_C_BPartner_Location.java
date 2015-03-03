/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_BPartner_Location
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.828
 */
public class X_C_BPartner_Location extends PO {
	/** Standard Constructor */
	public X_C_BPartner_Location(Properties ctx, int C_BPartner_Location_ID,
			String trxName) {
		super(ctx, C_BPartner_Location_ID, trxName);
		/**
		 * if (C_BPartner_Location_ID == 0) { setC_BPartner_ID (0);
		 * setC_BPartner_Location_ID (0); setC_Location_ID (0); setIsBillTo
		 * (true); // Y setIsPayFrom (true); // Y setIsRemitTo (true); // Y
		 * setIsShipTo (true); // Y setName (null); // . }
		 */
	}

	/** Load Constructor */
	public X_C_BPartner_Location(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_BPartner_Location */
	public static final String Table_Name = "C_BPartner_Location";

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
		StringBuffer sb = new StringBuffer("X_C_BPartner_Location[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Business Partner . Identifies a Business Partner
	 */
	public void setC_BPartner_ID(int C_BPartner_ID) {
		if (C_BPartner_ID < 1)
			throw new IllegalArgumentException("C_BPartner_ID is mandatory.");
		set_ValueNoCheck("C_BPartner_ID", new Integer(C_BPartner_ID));
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
	 * Set Partner Location. Identifies the (ship to) address for this Business
	 * Partner
	 */
	public void setC_BPartner_Location_ID(int C_BPartner_Location_ID) {
		if (C_BPartner_Location_ID < 1)
			throw new IllegalArgumentException(
					"C_BPartner_Location_ID is mandatory.");
		set_ValueNoCheck("C_BPartner_Location_ID", new Integer(
				C_BPartner_Location_ID));
	}

	/**
	 * Get Partner Location. Identifies the (ship to) address for this Business
	 * Partner
	 */
	public int getC_BPartner_Location_ID() {
		Integer ii = (Integer) get_Value("C_BPartner_Location_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Address. Location or Address
	 */
	public void setC_Location_ID(int C_Location_ID) {
		if (C_Location_ID < 1)
			throw new IllegalArgumentException("C_Location_ID is mandatory.");
		set_Value("C_Location_ID", new Integer(C_Location_ID));
	}

	/**
	 * Get Address. Location or Address
	 */
	public int getC_Location_ID() {
		Integer ii = (Integer) get_Value("C_Location_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Sales Region. Sales coverage region
	 */
	public void setC_SalesRegion_ID(int C_SalesRegion_ID) {
		if (C_SalesRegion_ID <= 0)
			set_Value("C_SalesRegion_ID", null);
		else
			set_Value("C_SalesRegion_ID", new Integer(C_SalesRegion_ID));
	}

	/**
	 * Get Sales Region. Sales coverage region
	 */
	public int getC_SalesRegion_ID() {
		Integer ii = (Integer) get_Value("C_SalesRegion_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Fax. Facsimile number
	 */
	public void setFax(String Fax) {
		if (Fax != null && Fax.length() > 40) {
			log.warning("Length > 40 - truncated");
			Fax = Fax.substring(0, 39);
		}
		set_Value("Fax", Fax);
	}

	/**
	 * Get Fax. Facsimile number
	 */
	public String getFax() {
		return (String) get_Value("Fax");
	}

	/**
	 * Set ISDN. ISDN or modem line
	 */
	public void setISDN(String ISDN) {
		if (ISDN != null && ISDN.length() > 40) {
			log.warning("Length > 40 - truncated");
			ISDN = ISDN.substring(0, 39);
		}
		set_Value("ISDN", ISDN);
	}

	/**
	 * Get ISDN. ISDN or modem line
	 */
	public String getISDN() {
		return (String) get_Value("ISDN");
	}

	/**
	 * Set Invoice Address. Business Partner Invoice/Bill Address
	 */
	public void setIsBillTo(boolean IsBillTo) {
		set_Value("IsBillTo", new Boolean(IsBillTo));
	}

	/**
	 * Get Invoice Address. Business Partner Invoice/Bill Address
	 */
	public boolean isBillTo() {
		Object oo = get_Value("IsBillTo");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Pay-From Address. Business Partner pays from that address and we'll
	 * send dunning letters there
	 */
	public void setIsPayFrom(boolean IsPayFrom) {
		set_Value("IsPayFrom", new Boolean(IsPayFrom));
	}

	/**
	 * Get Pay-From Address. Business Partner pays from that address and we'll
	 * send dunning letters there
	 */
	public boolean isPayFrom() {
		Object oo = get_Value("IsPayFrom");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Remit-To Address. Business Partner payment address
	 */
	public void setIsRemitTo(boolean IsRemitTo) {
		set_Value("IsRemitTo", new Boolean(IsRemitTo));
	}

	/**
	 * Get Remit-To Address. Business Partner payment address
	 */
	public boolean isRemitTo() {
		Object oo = get_Value("IsRemitTo");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Ship Address. Business Partner Shipment Address
	 */
	public void setIsShipTo(boolean IsShipTo) {
		set_Value("IsShipTo", new Boolean(IsShipTo));
	}

	/**
	 * Get Ship Address. Business Partner Shipment Address
	 */
	public boolean isShipTo() {
		Object oo = get_Value("IsShipTo");
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

	/**
	 * Set Phone. Identifies a telephone number
	 */
	public void setPhone(String Phone) {
		if (Phone != null && Phone.length() > 40) {
			log.warning("Length > 40 - truncated");
			Phone = Phone.substring(0, 39);
		}
		set_Value("Phone", Phone);
	}

	/**
	 * Get Phone. Identifies a telephone number
	 */
	public String getPhone() {
		return (String) get_Value("Phone");
	}

	/**
	 * Set 2nd Phone. Identifies an alternate telephone number.
	 */
	public void setPhone2(String Phone2) {
		if (Phone2 != null && Phone2.length() > 40) {
			log.warning("Length > 40 - truncated");
			Phone2 = Phone2.substring(0, 39);
		}
		set_Value("Phone2", Phone2);
	}

	/**
	 * Get 2nd Phone. Identifies an alternate telephone number.
	 */
	public String getPhone2() {
		return (String) get_Value("Phone2");
	}
}
