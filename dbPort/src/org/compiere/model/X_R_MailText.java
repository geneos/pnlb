/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for R_MailText
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.468
 */
public class X_R_MailText extends PO {
	/** Standard Constructor */
	public X_R_MailText(Properties ctx, int R_MailText_ID, String trxName) {
		super(ctx, R_MailText_ID, trxName);
		/**
		 * if (R_MailText_ID == 0) { setIsHtml (false); setMailText (null);
		 * setName (null); setR_MailText_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_R_MailText(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=R_MailText */
	public static final String Table_Name = "R_MailText";

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

	protected BigDecimal accessLevel = new BigDecimal(7);

	/** AccessLevel 7 - System - Client - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_R_MailText[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set HTML. Text has HTML tags
	 */
	public void setIsHtml(boolean IsHtml) {
		set_Value("IsHtml", new Boolean(IsHtml));
	}

	/**
	 * Get HTML. Text has HTML tags
	 */
	public boolean isHtml() {
		Object oo = get_Value("IsHtml");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Subject. Mail Header (Subject)
	 */
	public void setMailHeader(String MailHeader) {
		if (MailHeader != null && MailHeader.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			MailHeader = MailHeader.substring(0, 1999);
		}
		set_Value("MailHeader", MailHeader);
	}

	/**
	 * Get Subject. Mail Header (Subject)
	 */
	public String getMailHeader() {
		return (String) get_Value("MailHeader");
	}

	/**
	 * Set Mail Text. Text used for Mail message
	 */
	public void setMailText(String MailText) {
		if (MailText == null)
			throw new IllegalArgumentException("MailText is mandatory.");
		if (MailText.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			MailText = MailText.substring(0, 1999);
		}
		set_Value("MailText", MailText);
	}

	/**
	 * Get Mail Text. Text used for Mail message
	 */
	public String getMailText() {
		return (String) get_Value("MailText");
	}

	/**
	 * Set Mail Text 2. Optional second text part used for Mail message
	 */
	public void setMailText2(String MailText2) {
		if (MailText2 != null && MailText2.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			MailText2 = MailText2.substring(0, 1999);
		}
		set_Value("MailText2", MailText2);
	}

	/**
	 * Get Mail Text 2. Optional second text part used for Mail message
	 */
	public String getMailText2() {
		return (String) get_Value("MailText2");
	}

	/**
	 * Set Mail Text 3. Optional third text part used for Mail message
	 */
	public void setMailText3(String MailText3) {
		if (MailText3 != null && MailText3.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			MailText3 = MailText3.substring(0, 1999);
		}
		set_Value("MailText3", MailText3);
	}

	/**
	 * Get Mail Text 3. Optional third text part used for Mail message
	 */
	public String getMailText3() {
		return (String) get_Value("MailText3");
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
	 * Set Mail Template. Text templates for mailings
	 */
	public void setR_MailText_ID(int R_MailText_ID) {
		if (R_MailText_ID < 1)
			throw new IllegalArgumentException("R_MailText_ID is mandatory.");
		set_ValueNoCheck("R_MailText_ID", new Integer(R_MailText_ID));
	}

	/**
	 * Get Mail Template. Text templates for mailings
	 */
	public int getR_MailText_ID() {
		Integer ii = (Integer) get_Value("R_MailText_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
