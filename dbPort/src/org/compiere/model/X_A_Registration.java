/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for A_Registration
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.234
 */
public class X_A_Registration extends PO {
	/** Standard Constructor */
	public X_A_Registration(Properties ctx, int A_Registration_ID,
			String trxName) {
		super(ctx, A_Registration_ID, trxName);
		/**
		 * if (A_Registration_ID == 0) { setA_Registration_ID (0);
		 * setAssetServiceDate (new Timestamp(System.currentTimeMillis()));
		 * setIsAllowPublish (false); setIsInProduction (false); setIsRegistered
		 * (false); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_A_Registration(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=A_Registration */
	public static final String Table_Name = "A_Registration";

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
		StringBuffer sb = new StringBuffer("X_A_Registration[")
				.append(get_ID()).append("]");
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
	 * Set Asset. Asset used internally or by customers
	 */
	public void setA_Asset_ID(int A_Asset_ID) {
		if (A_Asset_ID <= 0)
			set_Value("A_Asset_ID", null);
		else
			set_Value("A_Asset_ID", new Integer(A_Asset_ID));
	}

	/**
	 * Get Asset. Asset used internally or by customers
	 */
	public int getA_Asset_ID() {
		Integer ii = (Integer) get_Value("A_Asset_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Registration. User Asset Registration
	 */
	public void setA_Registration_ID(int A_Registration_ID) {
		if (A_Registration_ID < 1)
			throw new IllegalArgumentException(
					"A_Registration_ID is mandatory.");
		set_ValueNoCheck("A_Registration_ID", new Integer(A_Registration_ID));
	}

	/**
	 * Get Registration. User Asset Registration
	 */
	public int getA_Registration_ID() {
		Integer ii = (Integer) get_Value("A_Registration_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set In Service Date. Date when Asset was put into service
	 */
	public void setAssetServiceDate(Timestamp AssetServiceDate) {
		if (AssetServiceDate == null)
			throw new IllegalArgumentException("AssetServiceDate is mandatory.");
		set_ValueNoCheck("AssetServiceDate", AssetServiceDate);
	}

	/**
	 * Get In Service Date. Date when Asset was put into service
	 */
	public Timestamp getAssetServiceDate() {
		return (Timestamp) get_Value("AssetServiceDate");
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

	/**
	 * Set Comment/Help. Comment or Hint
	 */
	public void setHelp(String Help) {
		if (Help != null && Help.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Help = Help.substring(0, 1999);
		}
		set_Value("Help", Help);
	}

	/**
	 * Get Comment/Help. Comment or Hint
	 */
	public String getHelp() {
		return (String) get_Value("Help");
	}

	/**
	 * Set Allowed to be Published. You allow to publish the information, not
	 * just statistical summary info
	 */
	public void setIsAllowPublish(boolean IsAllowPublish) {
		set_Value("IsAllowPublish", new Boolean(IsAllowPublish));
	}

	/**
	 * Get Allowed to be Published. You allow to publish the information, not
	 * just statistical summary info
	 */
	public boolean isAllowPublish() {
		Object oo = get_Value("IsAllowPublish");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set In Production. The system is in production
	 */
	public void setIsInProduction(boolean IsInProduction) {
		set_Value("IsInProduction", new Boolean(IsInProduction));
	}

	/**
	 * Get In Production. The system is in production
	 */
	public boolean isInProduction() {
		Object oo = get_Value("IsInProduction");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Registered. The application is registered.
	 */
	public void setIsRegistered(boolean IsRegistered) {
		set_Value("IsRegistered", new Boolean(IsRegistered));
	}

	/**
	 * Get Registered. The application is registered.
	 */
	public boolean isRegistered() {
		Object oo = get_Value("IsRegistered");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Product. Product, Service, Item
	 */
	public void setM_Product_ID(int M_Product_ID) {
		if (M_Product_ID <= 0)
			set_Value("M_Product_ID", null);
		else
			set_Value("M_Product_ID", new Integer(M_Product_ID));
	}

	/**
	 * Get Product. Product, Service, Item
	 */
	public int getM_Product_ID() {
		Integer ii = (Integer) get_Value("M_Product_ID");
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
	 * Set Note. Optional additional user defined information
	 */
	public void setNote(String Note) {
		if (Note != null && Note.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Note = Note.substring(0, 1999);
		}
		set_Value("Note", Note);
	}

	/**
	 * Get Note. Optional additional user defined information
	 */
	public String getNote() {
		return (String) get_Value("Note");
	}

	/** Set Process Now */
	public void setProcessing(boolean Processing) {
		set_Value("Processing", new Boolean(Processing));
	}

	/** Get Process Now */
	public boolean isProcessing() {
		Object oo = get_Value("Processing");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Remote Addr. Remote Address
	 */
	public void setRemote_Addr(String Remote_Addr) {
		if (Remote_Addr != null && Remote_Addr.length() > 60) {
			log.warning("Length > 60 - truncated");
			Remote_Addr = Remote_Addr.substring(0, 59);
		}
		set_Value("Remote_Addr", Remote_Addr);
	}

	/**
	 * Get Remote Addr. Remote Address
	 */
	public String getRemote_Addr() {
		return (String) get_Value("Remote_Addr");
	}

	/**
	 * Set Remote Host. Remote host Info
	 */
	public void setRemote_Host(String Remote_Host) {
		if (Remote_Host != null && Remote_Host.length() > 120) {
			log.warning("Length > 120 - truncated");
			Remote_Host = Remote_Host.substring(0, 119);
		}
		set_Value("Remote_Host", Remote_Host);
	}

	/**
	 * Get Remote Host. Remote host Info
	 */
	public String getRemote_Host() {
		return (String) get_Value("Remote_Host");
	}
}
