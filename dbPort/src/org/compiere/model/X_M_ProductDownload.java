/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_ProductDownload
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.406
 */
public class X_M_ProductDownload extends PO {
	/** Standard Constructor */
	public X_M_ProductDownload(Properties ctx, int M_ProductDownload_ID,
			String trxName) {
		super(ctx, M_ProductDownload_ID, trxName);
		/**
		 * if (M_ProductDownload_ID == 0) { setDownloadURL (null);
		 * setM_ProductDownload_ID (0); setM_Product_ID (0); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_M_ProductDownload(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_ProductDownload */
	public static final String Table_Name = "M_ProductDownload";

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
		StringBuffer sb = new StringBuffer("X_M_ProductDownload[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Download URL. URL of the Download files
	 */
	public void setDownloadURL(String DownloadURL) {
		if (DownloadURL == null)
			throw new IllegalArgumentException("DownloadURL is mandatory.");
		if (DownloadURL.length() > 120) {
			log.warning("Length > 120 - truncated");
			DownloadURL = DownloadURL.substring(0, 119);
		}
		set_Value("DownloadURL", DownloadURL);
	}

	/**
	 * Get Download URL. URL of the Download files
	 */
	public String getDownloadURL() {
		return (String) get_Value("DownloadURL");
	}

	/**
	 * Set Product Download. Product downloads
	 */
	public void setM_ProductDownload_ID(int M_ProductDownload_ID) {
		if (M_ProductDownload_ID < 1)
			throw new IllegalArgumentException(
					"M_ProductDownload_ID is mandatory.");
		set_ValueNoCheck("M_ProductDownload_ID", new Integer(
				M_ProductDownload_ID));
	}

	/**
	 * Get Product Download. Product downloads
	 */
	public int getM_ProductDownload_ID() {
		Integer ii = (Integer) get_Value("M_ProductDownload_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Product. Product, Service, Item
	 */
	public void setM_Product_ID(int M_Product_ID) {
		if (M_Product_ID < 1)
			throw new IllegalArgumentException("M_Product_ID is mandatory.");
		set_ValueNoCheck("M_Product_ID", new Integer(M_Product_ID));
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
}
