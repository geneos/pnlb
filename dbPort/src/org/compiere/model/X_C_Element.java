/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Element
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.39
 */
public class X_C_Element extends PO {
	/** Standard Constructor */
	public X_C_Element(Properties ctx, int C_Element_ID, String trxName) {
		super(ctx, C_Element_ID, trxName);
		/**
		 * if (C_Element_ID == 0) { setAD_Tree_ID (0); setC_Element_ID (0);
		 * setElementType (null); // A setIsBalancing (false);
		 * setIsNaturalAccount (false); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_C_Element(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Element */
	public static final String Table_Name = "C_Element";

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
		StringBuffer sb = new StringBuffer("X_C_Element[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Tree. Identifies a Tree
	 */
	public void setAD_Tree_ID(int AD_Tree_ID) {
		if (AD_Tree_ID < 1)
			throw new IllegalArgumentException("AD_Tree_ID is mandatory.");
		set_ValueNoCheck("AD_Tree_ID", new Integer(AD_Tree_ID));
	}

	/**
	 * Get Tree. Identifies a Tree
	 */
	public int getAD_Tree_ID() {
		Integer ii = (Integer) get_Value("AD_Tree_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Element. Accounting Element
	 */
	public void setC_Element_ID(int C_Element_ID) {
		if (C_Element_ID < 1)
			throw new IllegalArgumentException("C_Element_ID is mandatory.");
		set_ValueNoCheck("C_Element_ID", new Integer(C_Element_ID));
	}

	/**
	 * Get Element. Accounting Element
	 */
	public int getC_Element_ID() {
		Integer ii = (Integer) get_Value("C_Element_ID");
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

	/** ElementType AD_Reference_ID=116 */
	public static final int ELEMENTTYPE_AD_Reference_ID = 116;

	/** Account = A */
	public static final String ELEMENTTYPE_Account = "A";

	/** User defined = U */
	public static final String ELEMENTTYPE_UserDefined = "U";

	/**
	 * Set Type. Element Type (account or user defined)
	 */
	public void setElementType(String ElementType) {
		if (ElementType == null)
			throw new IllegalArgumentException("ElementType is mandatory");
		if (ElementType.equals("A") || ElementType.equals("U"))
			;
		else
			throw new IllegalArgumentException("ElementType Invalid value - "
					+ ElementType + " - Reference_ID=116 - A - U");
		if (ElementType.length() > 1) {
			log.warning("Length > 1 - truncated");
			ElementType = ElementType.substring(0, 0);
		}
		set_ValueNoCheck("ElementType", ElementType);
	}

	/**
	 * Get Type. Element Type (account or user defined)
	 */
	public String getElementType() {
		return (String) get_Value("ElementType");
	}

	/**
	 * Set Balancing. All transactions within an element value must balance
	 * (e.g. cost centers)
	 */
	public void setIsBalancing(boolean IsBalancing) {
		set_Value("IsBalancing", new Boolean(IsBalancing));
	}

	/**
	 * Get Balancing. All transactions within an element value must balance
	 * (e.g. cost centers)
	 */
	public boolean isBalancing() {
		Object oo = get_Value("IsBalancing");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Natural Account. The primary natural account
	 */
	public void setIsNaturalAccount(boolean IsNaturalAccount) {
		set_Value("IsNaturalAccount", new Boolean(IsNaturalAccount));
	}

	/**
	 * Get Natural Account. The primary natural account
	 */
	public boolean isNaturalAccount() {
		Object oo = get_Value("IsNaturalAccount");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
	 * Set Value Format. Format of the value; Can contain fixed format elements,
	 * Variables: "_lLoOaAcCa09"
	 */
	public void setVFormat(String VFormat) {
		if (VFormat != null && VFormat.length() > 40) {
			log.warning("Length > 40 - truncated");
			VFormat = VFormat.substring(0, 39);
		}
		set_Value("VFormat", VFormat);
	}

	/**
	 * Get Value Format. Format of the value; Can contain fixed format elements,
	 * Variables: "_lLoOaAcCa09"
	 */
	public String getVFormat() {
		return (String) get_Value("VFormat");
	}
}
