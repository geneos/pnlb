/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for W_Store
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:05.406
 */
public class X_W_Store extends PO {
	/** Standard Constructor */
	public X_W_Store(Properties ctx, int W_Store_ID, String trxName) {
		super(ctx, W_Store_ID, trxName);
		/**
		 * if (W_Store_ID == 0) { setC_PaymentTerm_ID (0); setIsDefault (false);
		 * setIsMenuAssets (true); // Y setIsMenuContact (true); // Y
		 * setIsMenuInterests (true); // Y setIsMenuInvoices (true); // Y
		 * setIsMenuOrders (true); // Y setIsMenuPayments (true); // Y
		 * setIsMenuRegistrations (true); // Y setIsMenuRequests (true); // Y
		 * setIsMenuRfQs (true); // Y setIsMenuShipments (true); // Y
		 * setM_PriceList_ID (0); setM_Warehouse_ID (0); setName (null);
		 * setSalesRep_ID (0); setURL (null); setW_Store_ID (0); setWebContext
		 * (null); }
		 */
	}

	/** Load Constructor */
	public X_W_Store(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=W_Store */
	public static final String Table_Name = "W_Store";

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
		StringBuffer sb = new StringBuffer("X_W_Store[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Payment Term. The terms of Payment (timing, discount)
	 */
	public void setC_PaymentTerm_ID(int C_PaymentTerm_ID) {
		if (C_PaymentTerm_ID < 1)
			throw new IllegalArgumentException("C_PaymentTerm_ID is mandatory.");
		set_Value("C_PaymentTerm_ID", new Integer(C_PaymentTerm_ID));
	}

	/**
	 * Get Payment Term. The terms of Payment (timing, discount)
	 */
	public int getC_PaymentTerm_ID() {
		Integer ii = (Integer) get_Value("C_PaymentTerm_ID");
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
	 * Set EMail Footer. Footer added to EMails
	 */
	public void setEMailFooter(String EMailFooter) {
		if (EMailFooter != null && EMailFooter.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			EMailFooter = EMailFooter.substring(0, 1999);
		}
		set_Value("EMailFooter", EMailFooter);
	}

	/**
	 * Get EMail Footer. Footer added to EMails
	 */
	public String getEMailFooter() {
		return (String) get_Value("EMailFooter");
	}

	/**
	 * Set EMail Header. Header added to EMails
	 */
	public void setEMailHeader(String EMailHeader) {
		if (EMailHeader != null && EMailHeader.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			EMailHeader = EMailHeader.substring(0, 1999);
		}
		set_Value("EMailHeader", EMailHeader);
	}

	/**
	 * Get EMail Header. Header added to EMails
	 */
	public String getEMailHeader() {
		return (String) get_Value("EMailHeader");
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
	 * Set Default. Default value
	 */
	public void setIsDefault(boolean IsDefault) {
		set_Value("IsDefault", new Boolean(IsDefault));
	}

	/**
	 * Get Default. Default value
	 */
	public boolean isDefault() {
		Object oo = get_Value("IsDefault");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Menu Assets. Show Menu Assets
	 */
	public void setIsMenuAssets(boolean IsMenuAssets) {
		set_Value("IsMenuAssets", new Boolean(IsMenuAssets));
	}

	/**
	 * Get Menu Assets. Show Menu Assets
	 */
	public boolean isMenuAssets() {
		Object oo = get_Value("IsMenuAssets");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Menu Contact. Show Menu Contact
	 */
	public void setIsMenuContact(boolean IsMenuContact) {
		set_Value("IsMenuContact", new Boolean(IsMenuContact));
	}

	/**
	 * Get Menu Contact. Show Menu Contact
	 */
	public boolean isMenuContact() {
		Object oo = get_Value("IsMenuContact");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Menu Interests. Show Menu Interests
	 */
	public void setIsMenuInterests(boolean IsMenuInterests) {
		set_Value("IsMenuInterests", new Boolean(IsMenuInterests));
	}

	/**
	 * Get Menu Interests. Show Menu Interests
	 */
	public boolean isMenuInterests() {
		Object oo = get_Value("IsMenuInterests");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Menu Invoices. Show Menu Invoices
	 */
	public void setIsMenuInvoices(boolean IsMenuInvoices) {
		set_Value("IsMenuInvoices", new Boolean(IsMenuInvoices));
	}

	/**
	 * Get Menu Invoices. Show Menu Invoices
	 */
	public boolean isMenuInvoices() {
		Object oo = get_Value("IsMenuInvoices");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Menu Orders. Show Menu Orders
	 */
	public void setIsMenuOrders(boolean IsMenuOrders) {
		set_Value("IsMenuOrders", new Boolean(IsMenuOrders));
	}

	/**
	 * Get Menu Orders. Show Menu Orders
	 */
	public boolean isMenuOrders() {
		Object oo = get_Value("IsMenuOrders");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Menu Payments. Show Menu Payments
	 */
	public void setIsMenuPayments(boolean IsMenuPayments) {
		set_Value("IsMenuPayments", new Boolean(IsMenuPayments));
	}

	/**
	 * Get Menu Payments. Show Menu Payments
	 */
	public boolean isMenuPayments() {
		Object oo = get_Value("IsMenuPayments");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Menu Registrations. Show Menu Registrations
	 */
	public void setIsMenuRegistrations(boolean IsMenuRegistrations) {
		set_Value("IsMenuRegistrations", new Boolean(IsMenuRegistrations));
	}

	/**
	 * Get Menu Registrations. Show Menu Registrations
	 */
	public boolean isMenuRegistrations() {
		Object oo = get_Value("IsMenuRegistrations");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Menu Requests. Show Menu Requests
	 */
	public void setIsMenuRequests(boolean IsMenuRequests) {
		set_Value("IsMenuRequests", new Boolean(IsMenuRequests));
	}

	/**
	 * Get Menu Requests. Show Menu Requests
	 */
	public boolean isMenuRequests() {
		Object oo = get_Value("IsMenuRequests");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Menu RfQs. Show Menu RfQs
	 */
	public void setIsMenuRfQs(boolean IsMenuRfQs) {
		set_Value("IsMenuRfQs", new Boolean(IsMenuRfQs));
	}

	/**
	 * Get Menu RfQs. Show Menu RfQs
	 */
	public boolean isMenuRfQs() {
		Object oo = get_Value("IsMenuRfQs");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Menu Shipments. Show Menu Shipments
	 */
	public void setIsMenuShipments(boolean IsMenuShipments) {
		set_Value("IsMenuShipments", new Boolean(IsMenuShipments));
	}

	/**
	 * Get Menu Shipments. Show Menu Shipments
	 */
	public boolean isMenuShipments() {
		Object oo = get_Value("IsMenuShipments");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Price List. Unique identifier of a Price List
	 */
	public void setM_PriceList_ID(int M_PriceList_ID) {
		if (M_PriceList_ID < 1)
			throw new IllegalArgumentException("M_PriceList_ID is mandatory.");
		set_Value("M_PriceList_ID", new Integer(M_PriceList_ID));
	}

	/**
	 * Get Price List. Unique identifier of a Price List
	 */
	public int getM_PriceList_ID() {
		Integer ii = (Integer) get_Value("M_PriceList_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Warehouse. Storage Warehouse and Service Point
	 */
	public void setM_Warehouse_ID(int M_Warehouse_ID) {
		if (M_Warehouse_ID < 1)
			throw new IllegalArgumentException("M_Warehouse_ID is mandatory.");
		set_Value("M_Warehouse_ID", new Integer(M_Warehouse_ID));
	}

	/**
	 * Get Warehouse. Storage Warehouse and Service Point
	 */
	public int getM_Warehouse_ID() {
		Integer ii = (Integer) get_Value("M_Warehouse_ID");
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

	/** SalesRep_ID AD_Reference_ID=190 */
	public static final int SALESREP_ID_AD_Reference_ID = 190;

	/**
	 * Set Sales Representative. Sales Representative or Company Agent
	 */
	public void setSalesRep_ID(int SalesRep_ID) {
		if (SalesRep_ID < 1)
			throw new IllegalArgumentException("SalesRep_ID is mandatory.");
		set_Value("SalesRep_ID", new Integer(SalesRep_ID));
	}

	/**
	 * Get Sales Representative. Sales Representative or Company Agent
	 */
	public int getSalesRep_ID() {
		Integer ii = (Integer) get_Value("SalesRep_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set URL. Full URL address - e.g. http://www.compiere.org
	 */
	public void setURL(String URL) {
		if (URL == null)
			throw new IllegalArgumentException("URL is mandatory.");
		if (URL.length() > 120) {
			log.warning("Length > 120 - truncated");
			URL = URL.substring(0, 119);
		}
		set_Value("URL", URL);
	}

	/**
	 * Get URL. Full URL address - e.g. http://www.compiere.org
	 */
	public String getURL() {
		return (String) get_Value("URL");
	}

	/**
	 * Set Web Store EMail. EMail address used as the sender (From)
	 */
	public void setWStoreEMail(String WStoreEMail) {
		if (WStoreEMail != null && WStoreEMail.length() > 60) {
			log.warning("Length > 60 - truncated");
			WStoreEMail = WStoreEMail.substring(0, 59);
		}
		set_Value("WStoreEMail", WStoreEMail);
	}

	/**
	 * Get Web Store EMail. EMail address used as the sender (From)
	 */
	public String getWStoreEMail() {
		return (String) get_Value("WStoreEMail");
	}

	/**
	 * Set WebStore User. User ID of the Web Store EMail address
	 */
	public void setWStoreUser(String WStoreUser) {
		if (WStoreUser != null && WStoreUser.length() > 60) {
			log.warning("Length > 60 - truncated");
			WStoreUser = WStoreUser.substring(0, 59);
		}
		set_Value("WStoreUser", WStoreUser);
	}

	/**
	 * Get WebStore User. User ID of the Web Store EMail address
	 */
	public String getWStoreUser() {
		return (String) get_Value("WStoreUser");
	}

	/**
	 * Set WebStore Password. Password of the Web Store EMail address
	 */
	public void setWStoreUserPW(String WStoreUserPW) {
		if (WStoreUserPW != null && WStoreUserPW.length() > 20) {
			log.warning("Length > 20 - truncated");
			WStoreUserPW = WStoreUserPW.substring(0, 19);
		}
		set_Value("WStoreUserPW", WStoreUserPW);
	}

	/**
	 * Get WebStore Password. Password of the Web Store EMail address
	 */
	public String getWStoreUserPW() {
		return (String) get_Value("WStoreUserPW");
	}

	/**
	 * Set Web Store. A Web Store of the Client
	 */
	public void setW_Store_ID(int W_Store_ID) {
		if (W_Store_ID < 1)
			throw new IllegalArgumentException("W_Store_ID is mandatory.");
		set_ValueNoCheck("W_Store_ID", new Integer(W_Store_ID));
	}

	/**
	 * Get Web Store. A Web Store of the Client
	 */
	public int getW_Store_ID() {
		Integer ii = (Integer) get_Value("W_Store_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Web Context. Web Server Context - e.g. /wstore
	 */
	public void setWebContext(String WebContext) {
		if (WebContext == null)
			throw new IllegalArgumentException("WebContext is mandatory.");
		if (WebContext.length() > 20) {
			log.warning("Length > 20 - truncated");
			WebContext = WebContext.substring(0, 19);
		}
		set_Value("WebContext", WebContext);
	}

	/**
	 * Get Web Context. Web Server Context - e.g. /wstore
	 */
	public String getWebContext() {
		return (String) get_Value("WebContext");
	}

	/**
	 * Set Web Store Info. Web Store Header Information
	 */
	public void setWebInfo(String WebInfo) {
		if (WebInfo != null && WebInfo.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			WebInfo = WebInfo.substring(0, 1999);
		}
		set_Value("WebInfo", WebInfo);
	}

	/**
	 * Get Web Store Info. Web Store Header Information
	 */
	public String getWebInfo() {
		return (String) get_Value("WebInfo");
	}

	/**
	 * Set Web Order EMail. EMail address to receive notifications when web
	 * orders were processed
	 */
	public void setWebOrderEMail(String WebOrderEMail) {
		if (WebOrderEMail != null && WebOrderEMail.length() > 60) {
			log.warning("Length > 60 - truncated");
			WebOrderEMail = WebOrderEMail.substring(0, 59);
		}
		set_Value("WebOrderEMail", WebOrderEMail);
	}

	/**
	 * Get Web Order EMail. EMail address to receive notifications when web
	 * orders were processed
	 */
	public String getWebOrderEMail() {
		return (String) get_Value("WebOrderEMail");
	}

	/**
	 * Set Web Parameter 1. Web Site Parameter 1 (default: header image)
	 */
	public void setWebParam1(String WebParam1) {
		if (WebParam1 != null && WebParam1.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			WebParam1 = WebParam1.substring(0, 1999);
		}
		set_Value("WebParam1", WebParam1);
	}

	/**
	 * Get Web Parameter 1. Web Site Parameter 1 (default: header image)
	 */
	public String getWebParam1() {
		return (String) get_Value("WebParam1");
	}

	/**
	 * Set Web Parameter 2. Web Site Parameter 2 (default index page)
	 */
	public void setWebParam2(String WebParam2) {
		if (WebParam2 != null && WebParam2.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			WebParam2 = WebParam2.substring(0, 1999);
		}
		set_Value("WebParam2", WebParam2);
	}

	/**
	 * Get Web Parameter 2. Web Site Parameter 2 (default index page)
	 */
	public String getWebParam2() {
		return (String) get_Value("WebParam2");
	}

	/**
	 * Set Web Parameter 3. Web Site Parameter 3 (default left - menu)
	 */
	public void setWebParam3(String WebParam3) {
		if (WebParam3 != null && WebParam3.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			WebParam3 = WebParam3.substring(0, 1999);
		}
		set_Value("WebParam3", WebParam3);
	}

	/**
	 * Get Web Parameter 3. Web Site Parameter 3 (default left - menu)
	 */
	public String getWebParam3() {
		return (String) get_Value("WebParam3");
	}

	/**
	 * Set Web Parameter 4. Web Site Parameter 4 (default footer left)
	 */
	public void setWebParam4(String WebParam4) {
		if (WebParam4 != null && WebParam4.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			WebParam4 = WebParam4.substring(0, 1999);
		}
		set_Value("WebParam4", WebParam4);
	}

	/**
	 * Get Web Parameter 4. Web Site Parameter 4 (default footer left)
	 */
	public String getWebParam4() {
		return (String) get_Value("WebParam4");
	}

	/**
	 * Set Web Parameter 5. Web Site Parameter 5 (default footer center)
	 */
	public void setWebParam5(String WebParam5) {
		if (WebParam5 != null && WebParam5.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			WebParam5 = WebParam5.substring(0, 1999);
		}
		set_Value("WebParam5", WebParam5);
	}

	/**
	 * Get Web Parameter 5. Web Site Parameter 5 (default footer center)
	 */
	public String getWebParam5() {
		return (String) get_Value("WebParam5");
	}

	/**
	 * Set Web Parameter 6. Web Site Parameter 6 (default footer right)
	 */
	public void setWebParam6(String WebParam6) {
		if (WebParam6 != null && WebParam6.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			WebParam6 = WebParam6.substring(0, 1999);
		}
		set_Value("WebParam6", WebParam6);
	}

	/**
	 * Get Web Parameter 6. Web Site Parameter 6 (default footer right)
	 */
	public String getWebParam6() {
		return (String) get_Value("WebParam6");
	}
}
