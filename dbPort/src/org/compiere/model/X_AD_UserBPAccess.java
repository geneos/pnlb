/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_UserBPAccess
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.562
 */
public class X_AD_UserBPAccess extends PO {
	/** Standard Constructor */
	public X_AD_UserBPAccess(Properties ctx, int AD_UserBPAccess_ID,
			String trxName) {
		super(ctx, AD_UserBPAccess_ID, trxName);
		/**
		 * if (AD_UserBPAccess_ID == 0) { setAD_UserBPAccess_ID (0);
		 * setAD_User_ID (0); setBPAccessType (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_UserBPAccess(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_UserBPAccess */
	public static final String Table_Name = "AD_UserBPAccess";

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
		StringBuffer sb = new StringBuffer("X_AD_UserBPAccess[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set User BP Access. User/concat access to Business Partner information
	 * and resources
	 */
	public void setAD_UserBPAccess_ID(int AD_UserBPAccess_ID) {
		if (AD_UserBPAccess_ID < 1)
			throw new IllegalArgumentException(
					"AD_UserBPAccess_ID is mandatory.");
		set_ValueNoCheck("AD_UserBPAccess_ID", new Integer(AD_UserBPAccess_ID));
	}

	/**
	 * Get User BP Access. User/concat access to Business Partner information
	 * and resources
	 */
	public int getAD_UserBPAccess_ID() {
		Integer ii = (Integer) get_Value("AD_UserBPAccess_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public void setAD_User_ID(int AD_User_ID) {
		if (AD_User_ID < 1)
			throw new IllegalArgumentException("AD_User_ID is mandatory.");
		set_Value("AD_User_ID", new Integer(AD_User_ID));
	}

	/**
	 * Get User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public int getAD_User_ID() {
		Integer ii = (Integer) get_Value("AD_User_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** BPAccessType AD_Reference_ID=358 */
	public static final int BPACCESSTYPE_AD_Reference_ID = 358;

	/** Assets, Download = A */
	public static final String BPACCESSTYPE_AssetsDownload = "A";

	/** Business Documents = B */
	public static final String BPACCESSTYPE_BusinessDocuments = "B";

	/** Requests = R */
	public static final String BPACCESSTYPE_Requests = "R";

	/**
	 * Set Access Type. Type of Access of the user/contact to Business Partner
	 * information and resources
	 */
	public void setBPAccessType(String BPAccessType) {
		if (BPAccessType == null)
			throw new IllegalArgumentException("BPAccessType is mandatory");
		if (BPAccessType.equals("A") || BPAccessType.equals("B")
				|| BPAccessType.equals("R"))
			;
		else
			throw new IllegalArgumentException("BPAccessType Invalid value - "
					+ BPAccessType + " - Reference_ID=358 - A - B - R");
		if (BPAccessType.length() > 1) {
			log.warning("Length > 1 - truncated");
			BPAccessType = BPAccessType.substring(0, 0);
		}
		set_Value("BPAccessType", BPAccessType);
	}

	/**
	 * Get Access Type. Type of Access of the user/contact to Business Partner
	 * information and resources
	 */
	public String getBPAccessType() {
		return (String) get_Value("BPAccessType");
	}

	/** DocBaseType AD_Reference_ID=183 */
	public static final int DOCBASETYPE_AD_Reference_ID = 183;

	/** AP Credit Memo = APC */
	public static final String DOCBASETYPE_APCreditMemo = "APC";

	/** AP Invoice = API */
	public static final String DOCBASETYPE_APInvoice = "API";

	/** AP Payment = APP */
	public static final String DOCBASETYPE_APPayment = "APP";

	/** AR Credit Memo = ARC */
	public static final String DOCBASETYPE_ARCreditMemo = "ARC";

	/** AR Pro Forma Invoice = ARF */
	public static final String DOCBASETYPE_ARProFormaInvoice = "ARF";

	/** AR Invoice = ARI */
	public static final String DOCBASETYPE_ARInvoice = "ARI";

	/** AR Receipt = ARR */
	public static final String DOCBASETYPE_ARReceipt = "ARR";

	/** Payment Allocation = CMA */
	public static final String DOCBASETYPE_PaymentAllocation = "CMA";

	/** Bank Statement = CMB */
	public static final String DOCBASETYPE_BankStatement = "CMB";

	/** Cash Journal = CMC */
	public static final String DOCBASETYPE_CashJournal = "CMC";

	/** GL Document = GLD */
	public static final String DOCBASETYPE_GLDocument = "GLD";

	/** GL Journal = GLJ */
	public static final String DOCBASETYPE_GLJournal = "GLJ";

	/** Material Physical Inventory = MMI */
	public static final String DOCBASETYPE_MaterialPhysicalInventory = "MMI";

	/** Material Movement = MMM */
	public static final String DOCBASETYPE_MaterialMovement = "MMM";

	/** Material Production = MMP */
	public static final String DOCBASETYPE_MaterialProduction = "MMP";

	/** Material Receipt = MMR */
	public static final String DOCBASETYPE_MaterialReceipt = "MMR";

	/** Material Delivery = MMS */
	public static final String DOCBASETYPE_MaterialDelivery = "MMS";

	/** Manufacturing Operation Activity = MOA */
	public static final String DOCBASETYPE_ManufacturingOperationActivity = "MOA";

	/** Maintenance Order = MOF */
	public static final String DOCBASETYPE_MaintenanceOrder = "MOF";

	/** Manufacturing Order Issue = MOI */
	public static final String DOCBASETYPE_ManufacturingOrderIssue = "MOI";

	/** Manufacturing Order Method Variation = MOM */
	public static final String DOCBASETYPE_ManufacturingOrderMethodVariation = "MOM";

	/** Manufacturing Order = MOP */
	public static final String DOCBASETYPE_ManufacturingOrder = "MOP";

	/** Manufacturing Order Receipt = MOR */
	public static final String DOCBASETYPE_ManufacturingOrderReceipt = "MOR";

	/** Manufacturing Order Use Variation = MOU */
	public static final String DOCBASETYPE_ManufacturingOrderUseVariation = "MOU";

	/** Manufacturing Order Rate Variation = MOV */
	public static final String DOCBASETYPE_ManufacturingOrderRateVariation = "MOV";

	/** Quality Order = MQO */
	public static final String DOCBASETYPE_QualityOrder = "MQO";

	/** Match Invoice = MXI */
	public static final String DOCBASETYPE_MatchInvoice = "MXI";

	/** Match PO = MXP */
	public static final String DOCBASETYPE_MatchPO = "MXP";

	/** Project Issue = PJI */
	public static final String DOCBASETYPE_ProjectIssue = "PJI";

	/** Purchase Order = POO */
	public static final String DOCBASETYPE_PurchaseOrder = "POO";

	/** Purchase Requisition = POR */
	public static final String DOCBASETYPE_PurchaseRequisition = "POR";

	/** Sales Order = SOO */
	public static final String DOCBASETYPE_SalesOrder = "SOO";

	/**
	 * Set Document BaseType. Logical type of document
	 */
	public void setDocBaseType(String DocBaseType) {
		if (DocBaseType != null && DocBaseType.length() > 10) {
			log.warning("Length > 10 - truncated");
			DocBaseType = DocBaseType.substring(0, 9);
		}
		set_Value("DocBaseType", DocBaseType);
	}

	/**
	 * Get Document BaseType. Logical type of document
	 */
	public String getDocBaseType() {
		return (String) get_Value("DocBaseType");
	}

	/**
	 * Set Request Type. Type of request (e.g. Inquiry, Complaint, ..)
	 */
	public void setR_RequestType_ID(int R_RequestType_ID) {
		if (R_RequestType_ID <= 0)
			set_Value("R_RequestType_ID", null);
		else
			set_Value("R_RequestType_ID", new Integer(R_RequestType_ID));
	}

	/**
	 * Get Request Type. Type of request (e.g. Inquiry, Complaint, ..)
	 */
	public int getR_RequestType_ID() {
		Integer ii = (Integer) get_Value("R_RequestType_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
