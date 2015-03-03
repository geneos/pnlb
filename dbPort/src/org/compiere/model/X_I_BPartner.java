/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for I_BPartner
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:01.203
 */
public class X_I_BPartner extends PO {
	/** Standard Constructor */
	public X_I_BPartner(Properties ctx, int I_BPartner_ID, String trxName) {
		super(ctx, I_BPartner_ID, trxName);
		/**
		 * if (I_BPartner_ID == 0) { setI_BPartner_ID (0); setI_IsImported
		 * (false); // N }
		 */
	}

	/** Load Constructor */
	public X_I_BPartner(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=I_BPartner */
	public static final String Table_Name = "I_BPartner";

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

	protected BigDecimal accessLevel = new BigDecimal(2);

	/** AccessLevel 2 - Client */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_I_BPartner[").append(get_ID())
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
	 * Set Address 1. Address line 1 for this location
	 */
	public void setAddress1(String Address1) {
		if (Address1 != null && Address1.length() > 60) {
			log.warning("Length > 60 - truncated");
			Address1 = Address1.substring(0, 59);
		}
		set_Value("Address1", Address1);
	}

	/**
	 * Get Address 1. Address line 1 for this location
	 */
	public String getAddress1() {
		return (String) get_Value("Address1");
	}

	/**
	 * Set Address 2. Address line 2 for this location
	 */
	public void setAddress2(String Address2) {
		if (Address2 != null && Address2.length() > 60) {
			log.warning("Length > 60 - truncated");
			Address2 = Address2.substring(0, 59);
		}
		set_Value("Address2", Address2);
	}

	/**
	 * Get Address 2. Address line 2 for this location
	 */
	public String getAddress2() {
		return (String) get_Value("Address2");
	}

	/**
	 * Set BP Contact Greeting. Greeting for Business Partner Contact
	 */
	public void setBPContactGreeting(String BPContactGreeting) {
		if (BPContactGreeting != null && BPContactGreeting.length() > 60) {
			log.warning("Length > 60 - truncated");
			BPContactGreeting = BPContactGreeting.substring(0, 59);
		}
		set_Value("BPContactGreeting", BPContactGreeting);
	}

	/**
	 * Get BP Contact Greeting. Greeting for Business Partner Contact
	 */
	public String getBPContactGreeting() {
		return (String) get_Value("BPContactGreeting");
	}

	/**
	 * Set Birthday. Birthday or Anniversary day
	 */
	public void setBirthday(Timestamp Birthday) {
		set_Value("Birthday", Birthday);
	}

	/**
	 * Get Birthday. Birthday or Anniversary day
	 */
	public Timestamp getBirthday() {
		return (Timestamp) get_Value("Birthday");
	}

	/**
	 * Set Business Partner Group. Business Partner Group
	 */
	public void setC_BP_Group_ID(int C_BP_Group_ID) {
		if (C_BP_Group_ID <= 0)
			set_Value("C_BP_Group_ID", null);
		else
			set_Value("C_BP_Group_ID", new Integer(C_BP_Group_ID));
	}

	/**
	 * Get Business Partner Group. Business Partner Group
	 */
	public int getC_BP_Group_ID() {
		Integer ii = (Integer) get_Value("C_BP_Group_ID");
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
	 * Set Partner Location. Identifies the (ship to) address for this Business
	 * Partner
	 */
	public void setC_BPartner_Location_ID(int C_BPartner_Location_ID) {
		if (C_BPartner_Location_ID <= 0)
			set_Value("C_BPartner_Location_ID", null);
		else
			set_Value("C_BPartner_Location_ID", new Integer(
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
	 * Set Country. Country
	 */
	public void setC_Country_ID(int C_Country_ID) {
		if (C_Country_ID <= 0)
			set_Value("C_Country_ID", null);
		else
			set_Value("C_Country_ID", new Integer(C_Country_ID));
	}

	/**
	 * Get Country. Country
	 */
	public int getC_Country_ID() {
		Integer ii = (Integer) get_Value("C_Country_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Greeting. Greeting to print on correspondence
	 */
	public void setC_Greeting_ID(int C_Greeting_ID) {
		if (C_Greeting_ID <= 0)
			set_Value("C_Greeting_ID", null);
		else
			set_Value("C_Greeting_ID", new Integer(C_Greeting_ID));
	}

	/**
	 * Get Greeting. Greeting to print on correspondence
	 */
	public int getC_Greeting_ID() {
		Integer ii = (Integer) get_Value("C_Greeting_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Region. Identifies a geographical Region
	 */
	public void setC_Region_ID(int C_Region_ID) {
		if (C_Region_ID <= 0)
			set_Value("C_Region_ID", null);
		else
			set_Value("C_Region_ID", new Integer(C_Region_ID));
	}

	/**
	 * Get Region. Identifies a geographical Region
	 */
	public int getC_Region_ID() {
		Integer ii = (Integer) get_Value("C_Region_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set City. Identifies a City
	 */
	public void setCity(String City) {
		if (City != null && City.length() > 60) {
			log.warning("Length > 60 - truncated");
			City = City.substring(0, 59);
		}
		set_Value("City", City);
	}

	/**
	 * Get City. Identifies a City
	 */
	public String getCity() {
		return (String) get_Value("City");
	}

	/**
	 * Set Comments. Comments or additional information
	 */
	public void setComments(String Comments) {
		if (Comments != null && Comments.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Comments = Comments.substring(0, 1999);
		}
		set_Value("Comments", Comments);
	}

	/**
	 * Get Comments. Comments or additional information
	 */
	public String getComments() {
		return (String) get_Value("Comments");
	}

	/**
	 * Set Contact Description. Description of Contact
	 */
	public void setContactDescription(String ContactDescription) {
		if (ContactDescription != null && ContactDescription.length() > 255) {
			log.warning("Length > 255 - truncated");
			ContactDescription = ContactDescription.substring(0, 254);
		}
		set_Value("ContactDescription", ContactDescription);
	}

	/**
	 * Get Contact Description. Description of Contact
	 */
	public String getContactDescription() {
		return (String) get_Value("ContactDescription");
	}

	/**
	 * Set Contact Name. Business Partner Contact Name
	 */
	public void setContactName(String ContactName) {
		if (ContactName != null && ContactName.length() > 60) {
			log.warning("Length > 60 - truncated");
			ContactName = ContactName.substring(0, 59);
		}
		set_Value("ContactName", ContactName);
	}

	/**
	 * Get Contact Name. Business Partner Contact Name
	 */
	public String getContactName() {
		return (String) get_Value("ContactName");
	}

	/**
	 * Set ISO Country Code. Upper-case two-letter alphanumeric ISO Country code
	 * according to ISO 3166-1 -
	 * http://www.chemie.fu-berlin.de/diverse/doc/ISO_3166.html
	 */
	public void setCountryCode(String CountryCode) {
		if (CountryCode != null && CountryCode.length() > 2) {
			log.warning("Length > 2 - truncated");
			CountryCode = CountryCode.substring(0, 1);
		}
		set_Value("CountryCode", CountryCode);
	}

	/**
	 * Get ISO Country Code. Upper-case two-letter alphanumeric ISO Country code
	 * according to ISO 3166-1 -
	 * http://www.chemie.fu-berlin.de/diverse/doc/ISO_3166.html
	 */
	public String getCountryCode() {
		return (String) get_Value("CountryCode");
	}

	/**
	 * Set D-U-N-S. Dun & Bradstreet Number
	 */
	public void setDUNS(String DUNS) {
		if (DUNS != null && DUNS.length() > 11) {
			log.warning("Length > 11 - truncated");
			DUNS = DUNS.substring(0, 10);
		}
		set_Value("DUNS", DUNS);
	}

	/**
	 * Get D-U-N-S. Dun & Bradstreet Number
	 */
	public String getDUNS() {
		return (String) get_Value("DUNS");
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
	 * Set EMail Address. Electronic Mail Address
	 */
	public void setEMail(String EMail) {
		if (EMail != null && EMail.length() > 60) {
			log.warning("Length > 60 - truncated");
			EMail = EMail.substring(0, 59);
		}
		set_Value("EMail", EMail);
	}

	/**
	 * Get EMail Address. Electronic Mail Address
	 */
	public String getEMail() {
		return (String) get_Value("EMail");
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
	 * Set Group Key. Business Partner Group Key
	 */
	public void setGroupValue(String GroupValue) {
		if (GroupValue != null && GroupValue.length() > 40) {
			log.warning("Length > 40 - truncated");
			GroupValue = GroupValue.substring(0, 39);
		}
		set_Value("GroupValue", GroupValue);
	}

	/**
	 * Get Group Key. Business Partner Group Key
	 */
	public String getGroupValue() {
		return (String) get_Value("GroupValue");
	}

	/** Set Import Business Partner */
	public void setI_BPartner_ID(int I_BPartner_ID) {
		if (I_BPartner_ID < 1)
			throw new IllegalArgumentException("I_BPartner_ID is mandatory.");
		set_ValueNoCheck("I_BPartner_ID", new Integer(I_BPartner_ID));
	}

	/** Get Import Business Partner */
	public int getI_BPartner_ID() {
		Integer ii = (Integer) get_Value("I_BPartner_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Import Error Message. Messages generated from import process
	 */
	public void setI_ErrorMsg(String I_ErrorMsg) {
		if (I_ErrorMsg != null && I_ErrorMsg.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			I_ErrorMsg = I_ErrorMsg.substring(0, 1999);
		}
		set_Value("I_ErrorMsg", I_ErrorMsg);
	}

	/**
	 * Get Import Error Message. Messages generated from import process
	 */
	public String getI_ErrorMsg() {
		return (String) get_Value("I_ErrorMsg");
	}

	/**
	 * Set Imported. Has this import been processed
	 */
	public void setI_IsImported(boolean I_IsImported) {
		set_Value("I_IsImported", new Boolean(I_IsImported));
	}

	/**
	 * Get Imported. Has this import been processed
	 */
	public boolean isI_IsImported() {
		Object oo = get_Value("I_IsImported");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set NAICS/SIC. Standard Industry Code or its successor NAIC -
	 * http://www.osha.gov/oshstats/sicser.html
	 */
	public void setNAICS(String NAICS) {
		if (NAICS != null && NAICS.length() > 6) {
			log.warning("Length > 6 - truncated");
			NAICS = NAICS.substring(0, 5);
		}
		set_Value("NAICS", NAICS);
	}

	/**
	 * Get NAICS/SIC. Standard Industry Code or its successor NAIC -
	 * http://www.osha.gov/oshstats/sicser.html
	 */
	public String getNAICS() {
		return (String) get_Value("NAICS");
	}

	/**
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name != null && Name.length() > 60) {
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

	/**
	 * Set Name 2. Additional Name
	 */
	public void setName2(String Name2) {
		if (Name2 != null && Name2.length() > 60) {
			log.warning("Length > 60 - truncated");
			Name2 = Name2.substring(0, 59);
		}
		set_Value("Name2", Name2);
	}

	/**
	 * Get Name 2. Additional Name
	 */
	public String getName2() {
		return (String) get_Value("Name2");
	}

	/**
	 * Set Password. Password of any length (case sensitive)
	 */
	public void setPassword(String Password) {
		if (Password != null && Password.length() > 20) {
			log.warning("Length > 20 - truncated");
			Password = Password.substring(0, 19);
		}
		set_Value("Password", Password);
	}

	/**
	 * Get Password. Password of any length (case sensitive)
	 */
	public String getPassword() {
		return (String) get_Value("Password");
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

	/**
	 * Set ZIP. Postal code
	 */
	public void setPostal(String Postal) {
		if (Postal != null && Postal.length() > 10) {
			log.warning("Length > 10 - truncated");
			Postal = Postal.substring(0, 9);
		}
		set_Value("Postal", Postal);
	}

	/**
	 * Get ZIP. Postal code
	 */
	public String getPostal() {
		return (String) get_Value("Postal");
	}

	/**
	 * Set -. Additional ZIP or Postal code
	 */
	public void setPostal_Add(String Postal_Add) {
		if (Postal_Add != null && Postal_Add.length() > 10) {
			log.warning("Length > 10 - truncated");
			Postal_Add = Postal_Add.substring(0, 9);
		}
		set_Value("Postal_Add", Postal_Add);
	}

	/**
	 * Get -. Additional ZIP or Postal code
	 */
	public String getPostal_Add() {
		return (String) get_Value("Postal_Add");
	}

	/**
	 * Set Processed. The document has been processed
	 */
	public void setProcessed(boolean Processed) {
		set_ValueNoCheck("Processed", new Boolean(Processed));
	}

	/**
	 * Get Processed. The document has been processed
	 */
	public boolean isProcessed() {
		Object oo = get_Value("Processed");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
	 * Set Region. Name of the Region
	 */
	public void setRegionName(String RegionName) {
		if (RegionName != null && RegionName.length() > 60) {
			log.warning("Length > 60 - truncated");
			RegionName = RegionName.substring(0, 59);
		}
		set_Value("RegionName", RegionName);
	}

	/**
	 * Get Region. Name of the Region
	 */
	public String getRegionName() {
		return (String) get_Value("RegionName");
	}

	/**
	 * Set Tax ID. Tax Identification
	 */
	public void setTaxID(String TaxID) {
		if (TaxID != null && TaxID.length() > 20) {
			log.warning("Length > 20 - truncated");
			TaxID = TaxID.substring(0, 19);
		}
		set_Value("TaxID", TaxID);
	}

	/**
	 * Get Tax ID. Tax Identification
	 */
	public String getTaxID() {
		return (String) get_Value("TaxID");
	}

	/**
	 * Set Title. Name this entity is referred to as
	 */
	public void setTitle(String Title) {
		if (Title != null && Title.length() > 40) {
			log.warning("Length > 40 - truncated");
			Title = Title.substring(0, 39);
		}
		set_Value("Title", Title);
	}

	/**
	 * Get Title. Name this entity is referred to as
	 */
	public String getTitle() {
		return (String) get_Value("Title");
	}

	/**
	 * Set Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public void setValue(String Value) {
		if (Value != null && Value.length() > 40) {
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getValue());
	}
}
