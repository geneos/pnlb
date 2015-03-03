/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for K_Synonym
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:01.687
 */
public class X_K_Synonym extends PO {
	/** Standard Constructor */
	public X_K_Synonym(Properties ctx, int K_Synonym_ID, String trxName) {
		super(ctx, K_Synonym_ID, trxName);
		/**
		 * if (K_Synonym_ID == 0) { setAD_Language (null); setK_Synonym_ID (0);
		 * setName (null); setSynonymName (null); }
		 */
	}

	/** Load Constructor */
	public X_K_Synonym(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=K_Synonym */
	public static final String Table_Name = "K_Synonym";

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
		StringBuffer sb = new StringBuffer("X_K_Synonym[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** AD_Language AD_Reference_ID=106 */
	public static final int AD_LANGUAGE_AD_Reference_ID = 106;

	/**
	 * Set Language. Language for this entity
	 */
	public void setAD_Language(String AD_Language) {
		if (AD_Language.length() > 6) {
			log.warning("Length > 6 - truncated");
			AD_Language = AD_Language.substring(0, 5);
		}
		set_Value("AD_Language", AD_Language);
	}

	/**
	 * Get Language. Language for this entity
	 */
	public String getAD_Language() {
		return (String) get_Value("AD_Language");
	}

	/**
	 * Set Knowledge Synonym. Knowlege Keyword Synonym
	 */
	public void setK_Synonym_ID(int K_Synonym_ID) {
		if (K_Synonym_ID < 1)
			throw new IllegalArgumentException("K_Synonym_ID is mandatory.");
		set_ValueNoCheck("K_Synonym_ID", new Integer(K_Synonym_ID));
	}

	/**
	 * Get Knowledge Synonym. Knowlege Keyword Synonym
	 */
	public int getK_Synonym_ID() {
		Integer ii = (Integer) get_Value("K_Synonym_ID");
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
	 * Set Synonym Name. The synonym for the name
	 */
	public void setSynonymName(String SynonymName) {
		if (SynonymName == null)
			throw new IllegalArgumentException("SynonymName is mandatory.");
		if (SynonymName.length() > 60) {
			log.warning("Length > 60 - truncated");
			SynonymName = SynonymName.substring(0, 59);
		}
		set_Value("SynonymName", SynonymName);
	}

	/**
	 * Get Synonym Name. The synonym for the name
	 */
	public String getSynonymName() {
		return (String) get_Value("SynonymName");
	}
}
