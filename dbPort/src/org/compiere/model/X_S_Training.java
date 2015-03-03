/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for S_Training
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.828
 */
public class X_S_Training extends PO {
	/** Standard Constructor */
	public X_S_Training(Properties ctx, int S_Training_ID, String trxName) {
		super(ctx, S_Training_ID, trxName);
		/**
		 * if (S_Training_ID == 0) { setC_TaxCategory_ID (0); setC_UOM_ID (0);
		 * setM_Product_Category_ID (0); setName (null); setS_Training_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_S_Training(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=S_Training */
	public static final String Table_Name = "S_Training";

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
		StringBuffer sb = new StringBuffer("X_S_Training[").append(get_ID())
				.append("]");
		return sb.toString();
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
	 * Set UOM. Unit of Measure
	 */
	public void setC_UOM_ID(int C_UOM_ID) {
		if (C_UOM_ID < 1)
			throw new IllegalArgumentException("C_UOM_ID is mandatory.");
		set_Value("C_UOM_ID", new Integer(C_UOM_ID));
	}

	/**
	 * Get UOM. Unit of Measure
	 */
	public int getC_UOM_ID() {
		Integer ii = (Integer) get_Value("C_UOM_ID");
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
	 * Set Description URL. URL for the description
	 */
	public void setDescriptionURL(String DescriptionURL) {
		if (DescriptionURL != null && DescriptionURL.length() > 120) {
			log.warning("Length > 120 - truncated");
			DescriptionURL = DescriptionURL.substring(0, 119);
		}
		set_Value("DescriptionURL", DescriptionURL);
	}

	/**
	 * Get Description URL. URL for the description
	 */
	public String getDescriptionURL() {
		return (String) get_Value("DescriptionURL");
	}

	/**
	 * Set Document Note. Additional information for a Document
	 */
	public void setDocumentNote(String DocumentNote) {
		if (DocumentNote != null && DocumentNote.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			DocumentNote = DocumentNote.substring(0, 1999);
		}
		set_Value("DocumentNote", DocumentNote);
	}

	/**
	 * Get Document Note. Additional information for a Document
	 */
	public String getDocumentNote() {
		return (String) get_Value("DocumentNote");
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
	 * Set Image URL. URL of image
	 */
	public void setImageURL(String ImageURL) {
		if (ImageURL != null && ImageURL.length() > 120) {
			log.warning("Length > 120 - truncated");
			ImageURL = ImageURL.substring(0, 119);
		}
		set_Value("ImageURL", ImageURL);
	}

	/**
	 * Get Image URL. URL of image
	 */
	public String getImageURL() {
		return (String) get_Value("ImageURL");
	}

	/**
	 * Set Product Category. Category of a Product
	 */
	public void setM_Product_Category_ID(int M_Product_Category_ID) {
		if (M_Product_Category_ID < 1)
			throw new IllegalArgumentException(
					"M_Product_Category_ID is mandatory.");
		set_Value("M_Product_Category_ID", new Integer(M_Product_Category_ID));
	}

	/**
	 * Get Product Category. Category of a Product
	 */
	public int getM_Product_Category_ID() {
		Integer ii = (Integer) get_Value("M_Product_Category_ID");
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
	 * Set Training. Repeated Training
	 */
	public void setS_Training_ID(int S_Training_ID) {
		if (S_Training_ID < 1)
			throw new IllegalArgumentException("S_Training_ID is mandatory.");
		set_ValueNoCheck("S_Training_ID", new Integer(S_Training_ID));
	}

	/**
	 * Get Training. Repeated Training
	 */
	public int getS_Training_ID() {
		Integer ii = (Integer) get_Value("S_Training_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
