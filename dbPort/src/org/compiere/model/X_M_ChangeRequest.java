/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_ChangeRequest
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.5
 */
public class X_M_ChangeRequest extends PO {
	/** Standard Constructor */
	public X_M_ChangeRequest(Properties ctx, int M_ChangeRequest_ID,
			String trxName) {
		super(ctx, M_ChangeRequest_ID, trxName);
		/**
		 * if (M_ChangeRequest_ID == 0) { setDocumentNo (null); setIsApproved
		 * (false); // N setM_ChangeRequest_ID (0); setName (null); setProcessed
		 * (false); }
		 */
	}

	/** Load Constructor */
	public X_M_ChangeRequest(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_ChangeRequest */
	public static final String Table_Name = "M_ChangeRequest";

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
		StringBuffer sb = new StringBuffer("X_M_ChangeRequest[").append(
				get_ID()).append("]");
		return sb.toString();
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
	 * Set Detail Information. Additional Detail Information
	 */
	public void setDetailInfo(String DetailInfo) {
		if (DetailInfo != null && DetailInfo.length() > 4000) {
			log.warning("Length > 4000 - truncated");
			DetailInfo = DetailInfo.substring(0, 3999);
		}
		set_Value("DetailInfo", DetailInfo);
	}

	/**
	 * Get Detail Information. Additional Detail Information
	 */
	public String getDetailInfo() {
		return (String) get_Value("DetailInfo");
	}

	/**
	 * Set Document No. Document sequence number of the document
	 */
	public void setDocumentNo(String DocumentNo) {
		if (DocumentNo == null)
			throw new IllegalArgumentException("DocumentNo is mandatory.");
		if (DocumentNo.length() > 30) {
			log.warning("Length > 30 - truncated");
			DocumentNo = DocumentNo.substring(0, 29);
		}
		set_Value("DocumentNo", DocumentNo);
	}

	/**
	 * Get Document No. Document sequence number of the document
	 */
	public String getDocumentNo() {
		return (String) get_Value("DocumentNo");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getDocumentNo());
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
	 * Set Approved. Indicates if this document requires approval
	 */
	public void setIsApproved(boolean IsApproved) {
		set_Value("IsApproved", new Boolean(IsApproved));
	}

	/**
	 * Get Approved. Indicates if this document requires approval
	 */
	public boolean isApproved() {
		Object oo = get_Value("IsApproved");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set BOM. Bill of Material
	 */
	public void setM_BOM_ID(int M_BOM_ID) {
		if (M_BOM_ID <= 0)
			set_ValueNoCheck("M_BOM_ID", null);
		else
			set_ValueNoCheck("M_BOM_ID", new Integer(M_BOM_ID));
	}

	/**
	 * Get BOM. Bill of Material
	 */
	public int getM_BOM_ID() {
		Integer ii = (Integer) get_Value("M_BOM_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Change Notice. Bill of Materials (Engineering) Change Notice
	 * (Version)
	 */
	public void setM_ChangeNotice_ID(int M_ChangeNotice_ID) {
		if (M_ChangeNotice_ID <= 0)
			set_ValueNoCheck("M_ChangeNotice_ID", null);
		else
			set_ValueNoCheck("M_ChangeNotice_ID",
					new Integer(M_ChangeNotice_ID));
	}

	/**
	 * Get Change Notice. Bill of Materials (Engineering) Change Notice
	 * (Version)
	 */
	public int getM_ChangeNotice_ID() {
		Integer ii = (Integer) get_Value("M_ChangeNotice_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Change Request. BOM (Engineering) Change Request
	 */
	public void setM_ChangeRequest_ID(int M_ChangeRequest_ID) {
		if (M_ChangeRequest_ID < 1)
			throw new IllegalArgumentException(
					"M_ChangeRequest_ID is mandatory.");
		set_ValueNoCheck("M_ChangeRequest_ID", new Integer(M_ChangeRequest_ID));
	}

	/**
	 * Get Change Request. BOM (Engineering) Change Request
	 */
	public int getM_ChangeRequest_ID() {
		Integer ii = (Integer) get_Value("M_ChangeRequest_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** M_FixChangeNotice_ID AD_Reference_ID=351 */
	public static final int M_FIXCHANGENOTICE_ID_AD_Reference_ID = 351;

	/**
	 * Set Fixed in. Fixed in Change Notice
	 */
	public void setM_FixChangeNotice_ID(int M_FixChangeNotice_ID) {
		if (M_FixChangeNotice_ID <= 0)
			set_ValueNoCheck("M_FixChangeNotice_ID", null);
		else
			set_ValueNoCheck("M_FixChangeNotice_ID", new Integer(
					M_FixChangeNotice_ID));
	}

	/**
	 * Get Fixed in. Fixed in Change Notice
	 */
	public int getM_FixChangeNotice_ID() {
		Integer ii = (Integer) get_Value("M_FixChangeNotice_ID");
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

	/**
	 * Set Processed. The document has been processed
	 */
	public void setProcessed(boolean Processed) {
		set_Value("Processed", new Boolean(Processed));
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
}
