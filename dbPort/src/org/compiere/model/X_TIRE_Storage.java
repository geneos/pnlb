/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for TIRE_Storage
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.843
 */
public class X_TIRE_Storage extends PO {
	/** Standard Constructor */
	public X_TIRE_Storage(Properties ctx, int TIRE_Storage_ID, String trxName) {
		super(ctx, TIRE_Storage_ID, trxName);
		/**
		 * if (TIRE_Storage_ID == 0) { setDateReceived (new
		 * Timestamp(System.currentTimeMillis())); //
		 * 
		 * @#Date@ setIsReturned (false); setIsStored (false); setName (null);
		 *         setTIRE_Storage_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_TIRE_Storage(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=TIRE_Storage */
	public static final String Table_Name = "TIRE_Storage";

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
		StringBuffer sb = new StringBuffer("X_TIRE_Storage[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public void setAD_User_ID(int AD_User_ID) {
		if (AD_User_ID <= 0)
			set_Value("AD_User_ID", null);
		else
			set_Value("AD_User_ID", new Integer(AD_User_ID));
	}

	/**
	 * Get User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public int getAD_User_ID() {
		Integer ii = (Integer) get_Value("AD_User_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

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
	 * Set Date received. Date a product was received
	 */
	public void setDateReceived(Timestamp DateReceived) {
		if (DateReceived == null)
			throw new IllegalArgumentException("DateReceived is mandatory.");
		set_Value("DateReceived", DateReceived);
	}

	/**
	 * Get Date received. Date a product was received
	 */
	public Timestamp getDateReceived() {
		return (Timestamp) get_Value("DateReceived");
	}

	/**
	 * Set Date returned. Date a product was returned
	 */
	public void setDateReturned(Timestamp DateReturned) {
		set_Value("DateReturned", DateReturned);
	}

	/**
	 * Get Date returned. Date a product was returned
	 */
	public Timestamp getDateReturned() {
		return (Timestamp) get_Value("DateReturned");
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

	/** Set Returned */
	public void setIsReturned(boolean IsReturned) {
		set_Value("IsReturned", new Boolean(IsReturned));
	}

	/** Get Returned */
	public boolean isReturned() {
		Object oo = get_Value("IsReturned");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Moved to storage */
	public void setIsStored(boolean IsStored) {
		set_Value("IsStored", new Boolean(IsStored));
	}

	/** Get Moved to storage */
	public boolean isStored() {
		Object oo = get_Value("IsStored");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Locator. Warehouse Locator
	 */
	public void setM_Locator_ID(int M_Locator_ID) {
		if (M_Locator_ID <= 0)
			set_Value("M_Locator_ID", null);
		else
			set_Value("M_Locator_ID", new Integer(M_Locator_ID));
	}

	/**
	 * Get Locator. Warehouse Locator
	 */
	public int getM_Locator_ID() {
		Integer ii = (Integer) get_Value("M_Locator_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
	 * Set Registration. Vehicle registration
	 */
	public void setRegistration(String Registration) {
		if (Registration != null && Registration.length() > 20) {
			log.warning("Length > 20 - truncated");
			Registration = Registration.substring(0, 19);
		}
		set_Value("Registration", Registration);
	}

	/**
	 * Get Registration. Vehicle registration
	 */
	public String getRegistration() {
		return (String) get_Value("Registration");
	}

	/** Set Remark */
	public void setRemark(String Remark) {
		if (Remark != null && Remark.length() > 60) {
			log.warning("Length > 60 - truncated");
			Remark = Remark.substring(0, 59);
		}
		set_Value("Remark", Remark);
	}

	/** Get Remark */
	public String getRemark() {
		return (String) get_Value("Remark");
	}

	/**
	 * Set Rim. Stored rim
	 */
	public void setRim(String Rim) {
		if (Rim != null && Rim.length() > 20) {
			log.warning("Length > 20 - truncated");
			Rim = Rim.substring(0, 19);
		}
		set_Value("Rim", Rim);
	}

	/**
	 * Get Rim. Stored rim
	 */
	public String getRim() {
		return (String) get_Value("Rim");
	}

	/** Set Rim Back */
	public void setRim_B(String Rim_B) {
		if (Rim_B != null && Rim_B.length() > 20) {
			log.warning("Length > 20 - truncated");
			Rim_B = Rim_B.substring(0, 19);
		}
		set_Value("Rim_B", Rim_B);
	}

	/** Get Rim Back */
	public String getRim_B() {
		return (String) get_Value("Rim_B");
	}

	/** Set Tire Storage */
	public void setTIRE_Storage_ID(int TIRE_Storage_ID) {
		if (TIRE_Storage_ID < 1)
			throw new IllegalArgumentException("TIRE_Storage_ID is mandatory.");
		set_ValueNoCheck("TIRE_Storage_ID", new Integer(TIRE_Storage_ID));
	}

	/** Get Tire Storage */
	public int getTIRE_Storage_ID() {
		Integer ii = (Integer) get_Value("TIRE_Storage_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Tire Quality */
	public void setTireQuality(String TireQuality) {
		if (TireQuality != null && TireQuality.length() > 20) {
			log.warning("Length > 20 - truncated");
			TireQuality = TireQuality.substring(0, 19);
		}
		set_Value("TireQuality", TireQuality);
	}

	/** Get Tire Quality */
	public String getTireQuality() {
		return (String) get_Value("TireQuality");
	}

	/** Set Tire Quality Back */
	public void setTireQuality_B(String TireQuality_B) {
		if (TireQuality_B != null && TireQuality_B.length() > 20) {
			log.warning("Length > 20 - truncated");
			TireQuality_B = TireQuality_B.substring(0, 19);
		}
		set_Value("TireQuality_B", TireQuality_B);
	}

	/** Get Tire Quality Back */
	public String getTireQuality_B() {
		return (String) get_Value("TireQuality_B");
	}

	/** Set Tire size (L/R) */
	public void setTireSize(String TireSize) {
		if (TireSize != null && TireSize.length() > 20) {
			log.warning("Length > 20 - truncated");
			TireSize = TireSize.substring(0, 19);
		}
		set_Value("TireSize", TireSize);
	}

	/** Get Tire size (L/R) */
	public String getTireSize() {
		return (String) get_Value("TireSize");
	}

	/** Set Tire size Back */
	public void setTireSize_B(String TireSize_B) {
		if (TireSize_B != null && TireSize_B.length() > 20) {
			log.warning("Length > 20 - truncated");
			TireSize_B = TireSize_B.substring(0, 19);
		}
		set_Value("TireSize_B", TireSize_B);
	}

	/** Get Tire size Back */
	public String getTireSize_B() {
		return (String) get_Value("TireSize_B");
	}

	/** Set Tire type */
	public void setTireType(String TireType) {
		if (TireType != null && TireType.length() > 20) {
			log.warning("Length > 20 - truncated");
			TireType = TireType.substring(0, 19);
		}
		set_Value("TireType", TireType);
	}

	/** Get Tire type */
	public String getTireType() {
		return (String) get_Value("TireType");
	}

	/** Set Tire type Back */
	public void setTireType_B(String TireType_B) {
		if (TireType_B != null && TireType_B.length() > 20) {
			log.warning("Length > 20 - truncated");
			TireType_B = TireType_B.substring(0, 19);
		}
		set_Value("TireType_B", TireType_B);
	}

	/** Get Tire type Back */
	public String getTireType_B() {
		return (String) get_Value("TireType_B");
	}

	/** Set Vehicle */
	public void setVehicle(String Vehicle) {
		if (Vehicle != null && Vehicle.length() > 20) {
			log.warning("Length > 20 - truncated");
			Vehicle = Vehicle.substring(0, 19);
		}
		set_Value("Vehicle", Vehicle);
	}

	/** Get Vehicle */
	public String getVehicle() {
		return (String) get_Value("Vehicle");
	}
}
