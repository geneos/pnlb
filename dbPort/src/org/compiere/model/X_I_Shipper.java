/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for I_Shipper
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:01.593
 */
public class X_I_Shipper extends PO {
	/** Standard Constructor */
	public X_I_Shipper(Properties ctx, int I_Shipper_ID, String trxName) {
		super(ctx, I_Shipper_ID, trxName);
		/**
		 * if (I_Shipper_ID == 0) { }
		 */
	}

	/** Load Constructor */
	public X_I_Shipper(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=I_Shipper */
	public static final String Table_Name = "I_Shipper";

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
		StringBuffer sb = new StringBuffer("X_I_Shipper[").append(get_ID())
				.append("]");
		return sb.toString();
	}
}
