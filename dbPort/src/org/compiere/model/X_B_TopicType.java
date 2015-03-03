/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for B_TopicType
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.359
 */
public class X_B_TopicType extends PO {
	/** Standard Constructor */
	public X_B_TopicType(Properties ctx, int B_TopicType_ID, String trxName) {
		super(ctx, B_TopicType_ID, trxName);
		/**
		 * if (B_TopicType_ID == 0) { setAuctionType (null); setB_TopicType_ID
		 * (0); setM_PriceList_ID (0); setM_ProductMember_ID (0);
		 * setM_Product_ID (0); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_B_TopicType(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=B_TopicType */
	public static final String Table_Name = "B_TopicType";

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
		StringBuffer sb = new StringBuffer("X_B_TopicType[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** Set Auction Type */
	public void setAuctionType(String AuctionType) {
		if (AuctionType == null)
			throw new IllegalArgumentException("AuctionType is mandatory.");
		if (AuctionType.length() > 1) {
			log.warning("Length > 1 - truncated");
			AuctionType = AuctionType.substring(0, 0);
		}
		set_Value("AuctionType", AuctionType);
	}

	/** Get Auction Type */
	public String getAuctionType() {
		return (String) get_Value("AuctionType");
	}

	/**
	 * Set Topic Type. Auction Topic Type
	 */
	public void setB_TopicType_ID(int B_TopicType_ID) {
		if (B_TopicType_ID < 1)
			throw new IllegalArgumentException("B_TopicType_ID is mandatory.");
		set_ValueNoCheck("B_TopicType_ID", new Integer(B_TopicType_ID));
	}

	/**
	 * Get Topic Type. Auction Topic Type
	 */
	public int getB_TopicType_ID() {
		Integer ii = (Integer) get_Value("B_TopicType_ID");
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

	/** M_ProductMember_ID AD_Reference_ID=162 */
	public static final int M_PRODUCTMEMBER_ID_AD_Reference_ID = 162;

	/**
	 * Set Membership. Product used to deternine the price of the membership for
	 * the topic type
	 */
	public void setM_ProductMember_ID(int M_ProductMember_ID) {
		if (M_ProductMember_ID < 1)
			throw new IllegalArgumentException(
					"M_ProductMember_ID is mandatory.");
		set_Value("M_ProductMember_ID", new Integer(M_ProductMember_ID));
	}

	/**
	 * Get Membership. Product used to deternine the price of the membership for
	 * the topic type
	 */
	public int getM_ProductMember_ID() {
		Integer ii = (Integer) get_Value("M_ProductMember_ID");
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
}
