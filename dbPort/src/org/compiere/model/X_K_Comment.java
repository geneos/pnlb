/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for K_Comment
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:01.656
 */
public class X_K_Comment extends PO {
	/** Standard Constructor */
	public X_K_Comment(Properties ctx, int K_Comment_ID, String trxName) {
		super(ctx, K_Comment_ID, trxName);
		/**
		 * if (K_Comment_ID == 0) { setIsPublic (true); // Y setK_Comment_ID
		 * (0); setK_Entry_ID (0); setRating (0); setTextMsg (null); }
		 */
	}

	/** Load Constructor */
	public X_K_Comment(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=K_Comment */
	public static final String Table_Name = "K_Comment";

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
		StringBuffer sb = new StringBuffer("X_K_Comment[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Session. User Session Online or Web
	 */
	public void setAD_Session_ID(int AD_Session_ID) {
		if (AD_Session_ID <= 0)
			set_ValueNoCheck("AD_Session_ID", null);
		else
			set_ValueNoCheck("AD_Session_ID", new Integer(AD_Session_ID));
	}

	/**
	 * Get Session. User Session Online or Web
	 */
	public int getAD_Session_ID() {
		Integer ii = (Integer) get_Value("AD_Session_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Public. Public can read entry
	 */
	public void setIsPublic(boolean IsPublic) {
		set_Value("IsPublic", new Boolean(IsPublic));
	}

	/**
	 * Get Public. Public can read entry
	 */
	public boolean isPublic() {
		Object oo = get_Value("IsPublic");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Entry Comment. Knowledge Entry Comment
	 */
	public void setK_Comment_ID(int K_Comment_ID) {
		if (K_Comment_ID < 1)
			throw new IllegalArgumentException("K_Comment_ID is mandatory.");
		set_ValueNoCheck("K_Comment_ID", new Integer(K_Comment_ID));
	}

	/**
	 * Get Entry Comment. Knowledge Entry Comment
	 */
	public int getK_Comment_ID() {
		Integer ii = (Integer) get_Value("K_Comment_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getK_Comment_ID()));
	}

	/**
	 * Set Entry. Knowledge Entry
	 */
	public void setK_Entry_ID(int K_Entry_ID) {
		if (K_Entry_ID < 1)
			throw new IllegalArgumentException("K_Entry_ID is mandatory.");
		set_ValueNoCheck("K_Entry_ID", new Integer(K_Entry_ID));
	}

	/**
	 * Get Entry. Knowledge Entry
	 */
	public int getK_Entry_ID() {
		Integer ii = (Integer) get_Value("K_Entry_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Rating. Classification or Importance
	 */
	public void setRating(int Rating) {
		set_Value("Rating", new Integer(Rating));
	}

	/**
	 * Get Rating. Classification or Importance
	 */
	public int getRating() {
		Integer ii = (Integer) get_Value("Rating");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Text Message. Text Message
	 */
	public void setTextMsg(String TextMsg) {
		if (TextMsg == null)
			throw new IllegalArgumentException("TextMsg is mandatory.");
		if (TextMsg.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			TextMsg = TextMsg.substring(0, 1999);
		}
		set_Value("TextMsg", TextMsg);
	}

	/**
	 * Get Text Message. Text Message
	 */
	public String getTextMsg() {
		return (String) get_Value("TextMsg");
	}
}
