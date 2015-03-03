/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_PeriodControl
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.187
 */
public class X_C_PeriodControl extends PO {
	/** Standard Constructor */
	public X_C_PeriodControl(Properties ctx, int C_PeriodControl_ID,
			String trxName) {
		super(ctx, C_PeriodControl_ID, trxName);
		/**
		 * if (C_PeriodControl_ID == 0) { setC_PeriodControl_ID (0);
		 * setC_Period_ID (0); setDocBaseType (null); setPeriodAction (null); //
		 * N }
		 */
	}

	/** Load Constructor */
	public X_C_PeriodControl(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_PeriodControl */
	public static final String Table_Name = "C_PeriodControl";

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
		StringBuffer sb = new StringBuffer("X_C_PeriodControl[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/** Set Period Control */
	public void setC_PeriodControl_ID(int C_PeriodControl_ID) {
		if (C_PeriodControl_ID < 1)
			throw new IllegalArgumentException(
					"C_PeriodControl_ID is mandatory.");
		set_ValueNoCheck("C_PeriodControl_ID", new Integer(C_PeriodControl_ID));
	}

	/** Get Period Control */
	public int getC_PeriodControl_ID() {
		Integer ii = (Integer) get_Value("C_PeriodControl_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getC_PeriodControl_ID()));
	}

	/**
	 * Set Period. Period of the Calendar
	 */
	public void setC_Period_ID(int C_Period_ID) {
		if (C_Period_ID < 1)
			throw new IllegalArgumentException("C_Period_ID is mandatory.");
		set_ValueNoCheck("C_Period_ID", new Integer(C_Period_ID));
	}

	/**
	 * Get Period. Period of the Calendar
	 */
	public int getC_Period_ID() {
		Integer ii = (Integer) get_Value("C_Period_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
		if (DocBaseType == null)
			throw new IllegalArgumentException("DocBaseType is mandatory");
		if (DocBaseType.length() > 3) {
			log.warning("Length > 3 - truncated");
			DocBaseType = DocBaseType.substring(0, 2);
		}
		set_ValueNoCheck("DocBaseType", DocBaseType);
	}

	/**
	 * Get Document BaseType. Logical type of document
	 */
	public String getDocBaseType() {
		return (String) get_Value("DocBaseType");
	}

	/** PeriodAction AD_Reference_ID=176 */
	public static final int PERIODACTION_AD_Reference_ID = 176;

	/** Close Period = C */
	public static final String PERIODACTION_ClosePeriod = "C";

	/** <No Action> = N */
	public static final String PERIODACTION_NoAction = "N";

	/** Open Period = O */
	public static final String PERIODACTION_OpenPeriod = "O";

	/** Permanently Close Period = P */
	public static final String PERIODACTION_PermanentlyClosePeriod = "P";

	/**
	 * Set Period Action. Action taken for this period
	 */
	public void setPeriodAction(String PeriodAction) {
		if (PeriodAction == null)
			throw new IllegalArgumentException("PeriodAction is mandatory");
		if (PeriodAction.equals("C") || PeriodAction.equals("N")
				|| PeriodAction.equals("O") || PeriodAction.equals("P"))
			;
		else
			throw new IllegalArgumentException("PeriodAction Invalid value - "
					+ PeriodAction + " - Reference_ID=176 - C - N - O - P");
		if (PeriodAction.length() > 1) {
			log.warning("Length > 1 - truncated");
			PeriodAction = PeriodAction.substring(0, 0);
		}
		set_Value("PeriodAction", PeriodAction);
	}

	/**
	 * Get Period Action. Action taken for this period
	 */
	public String getPeriodAction() {
		return (String) get_Value("PeriodAction");
	}

	/** PeriodStatus AD_Reference_ID=177 */
	public static final int PERIODSTATUS_AD_Reference_ID = 177;

	/** Closed = C */
	public static final String PERIODSTATUS_Closed = "C";

	/** Never opened = N */
	public static final String PERIODSTATUS_NeverOpened = "N";

	/** Open = O */
	public static final String PERIODSTATUS_Open = "O";

	/** Permanently closed = P */
	public static final String PERIODSTATUS_PermanentlyClosed = "P";

	/**
	 * Set Period Status. Current state of this period
	 */
	public void setPeriodStatus(String PeriodStatus) {
		if (PeriodStatus != null && PeriodStatus.length() > 1) {
			log.warning("Length > 1 - truncated");
			PeriodStatus = PeriodStatus.substring(0, 0);
		}
		set_ValueNoCheck("PeriodStatus", PeriodStatus);
	}

	/**
	 * Get Period Status. Current state of this period
	 */
	public String getPeriodStatus() {
		return (String) get_Value("PeriodStatus");
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
}
