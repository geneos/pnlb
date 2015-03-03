/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_BankStatement
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.89
 */
@SuppressWarnings("serial")
public class X_C_CONCILIACIONBANCARIA extends PO {
	/** Standard Constructor */
	public X_C_CONCILIACIONBANCARIA(Properties ctx,
			int C_ConciliacionBancaria_ID, String trxName) {
		super(ctx, C_ConciliacionBancaria_ID, trxName);
	}

	/** Load Constructor */
	public X_C_CONCILIACIONBANCARIA(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_CONCILIACIONBANCARIA */
	public static final String Table_Name = "C_CONCILIACIONBANCARIA";
	
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

	/** Load Meta Data */
	/*
	 * protected POInfo initPO (Properties ctx, String tableName) { try {
	 * PreparedStatement pstm = DB.prepareStatement("Select AD_TABLE_ID FROM
	 * AD_TABLE WHERE TABLENAME = '" + tableName + "'", get_TrxName());
	 * ResultSet rs = pstm.executeQuery();
	 * 
	 * if (rs.next()) Table_ID = rs.getInt(1); } catch (Exception e){} Model =
	 * new KeyNamePair(Table_ID,Table_Name);
	 * 
	 * POInfo poi = POInfo.getPOInfo (ctx, Table_ID); return poi; }
	 */

	public String toString() {
		StringBuffer sb = new StringBuffer("X_C_CONCILIACIONBANCARIA[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Beginning Balance. Balance prior to any transactions
	 */
	public void setSaldoInicial(BigDecimal AmountInicial) {
		set_Value("AMOUNTINICIAL", AmountInicial);
	}

	/**
	 * Get Beginning Balance. Balance prior to any transactions
	 */
	public BigDecimal getSaldoInicial() {
		BigDecimal bd = (BigDecimal) get_Value("AMOUNTINICIAL");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Bank Account. Account at the Bank
	 */
	public void setC_BankAccount_ID(int C_BankAccount_ID) {
		if (C_BankAccount_ID < 1)
			throw new IllegalArgumentException("C_BankAccount_ID is mandatory.");
		set_Value("C_BankAccount_ID", new Integer(C_BankAccount_ID));
	}

	/**
	 * Get Bank Account. Account at the Bank
	 */
	public int getC_BankAccount_ID() {
		Integer ii = (Integer) get_Value("C_BankAccount_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Bank Conciliacion.
	 */
	public void setC_ConciliacionBancaria_ID(int C_ConciliacionBancaria_ID) {
		if (C_ConciliacionBancaria_ID < 1)
			throw new IllegalArgumentException(
					"C_ConciliacionBancaria_ID is mandatory.");
		set_ValueNoCheck("C_CONCILIACIONBANCARIA_ID", new Integer(
				C_ConciliacionBancaria_ID));
	}

	/**
	 * Get Bank Conciliacion.
	 */
	public int getC_ConciliacionBancaria_ID() {
		Integer ii = (Integer) get_Value("C_CONCILIACIONBANCARIA_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Create lines from. Process which will generate a new document lines
	 * based on an existing document
	 */
	public void setCreateFrom(String CreateFrom) {
		if (CreateFrom != null && CreateFrom.length() > 1) {
			log.warning("Length > 1 - truncated");
			CreateFrom = CreateFrom.substring(0, 0);
		}
		set_Value("CreateFrom", CreateFrom);
	}

	/**
	 * Get Create lines from. Process which will generate a new document lines
	 * based on an existing document
	 */
	public String getCreateFrom() {
		return (String) get_Value("CreateFrom");
	}

	/**
	 * Set Description. Optional short description of the record
	 */
	public void setDescription(String Description) {
		if (Description != null && Description.length() > 255) {
			log.warning("Length > 255 - truncated");
			Description = Description.substring(0, 254);
		}
		set_Value("DESCRIPCION", Description);
	}

	/**
	 * Get Description. Optional short description of the record
	 */
	public String getDescription() {
		return (String) get_Value("DESCRIPCION");
	}

	/**
	 * Set Conciliacion. Optional short description of the record
	 */
	public void setConciliacion(String Conciliacion) {
		if (Conciliacion != null && Conciliacion.length() > 255) {
			log.warning("Length > 255 - truncated");
			Conciliacion = Conciliacion.substring(0, 254);
		}
		set_Value("CONCILIACION", Conciliacion);
	}

	/**
	 * Get Conciliacion. Optional short description of the record
	 */
	public String getConciliacion() {
		return (String) get_Value("CONCILIACION");
	}

	/**
	 * Set Beginning Balance. Balance prior to any transactions
	 */
	public void setSaldoCierre(BigDecimal AmountCierre) {
		set_Value("AMOUNTCIERRE", AmountCierre);
	}

	/**
	 * Get Beginning Balance. Balance prior to any transactions
	 */
	public BigDecimal getSaldoCierre() {
		BigDecimal bd = (BigDecimal) get_Value("AMOUNTCIERRE");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Beginning Balance. Balance prior to any transactions
	 */
	public void setSaldoConciliado(BigDecimal AmountConciliado) {
		set_Value("AMOUNTCONCILIADO", AmountConciliado);
	}

	/**
	 * Get Beginning Balance. Balance prior to any transactions
	 */
	public BigDecimal getSaldoConciliado() {
		BigDecimal bd = (BigDecimal) get_Value("AMOUNTCONCILIADO");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Beginning Balance. Balance prior to any transactions
	 */
	public void setSaldoAConciliar(BigDecimal AmountAConciliar) {
		set_Value("AMOUNTACONCILIAR", AmountAConciliar);
	}

	/**
	 * Get Beginning Balance. Balance prior to any transactions
	 */
	public BigDecimal getSaldoAConciliar() {
		BigDecimal bd = (BigDecimal) get_Value("AMOUNTACONCILIAR");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Beginning Balance. Balance prior to any transactions
	 */
	public void setSaldoPendiente(BigDecimal AmountPendiente) {
		set_Value("AMOUNTPENDIENTE", AmountPendiente);
	}

	/**
	 * Get Beginning Balance. Balance prior to any transactions
	 */
	public BigDecimal getSaldoPendiente() {
		BigDecimal bd = (BigDecimal) get_Value("AMOUNTPENDIENTE");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Beginning Balance. Balance prior to any transactions
	 */
	public void setSaldoContable(BigDecimal AmountRegContable) {
		set_Value("AMOUNTREGCONTABLE", AmountRegContable);
	}

	/**
	 * Get Beginning Balance. Balance prior to any transactions
	 */
	public BigDecimal getSaldoContable() {
		BigDecimal bd = (BigDecimal) get_Value("AMOUNTREGCONTABLE");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set To Date. Electronic Funds Transfer To Date
	 */
	public void setToDate(Timestamp ToDate) {
		set_Value("TODATE", ToDate);
	}

	/**
	 * Get To Date. Electronic Funds Transfer To Date
	 */
	public Timestamp getToDate() {
		return (Timestamp) get_Value("TODATE");
	}

	/**
	 * Set From Date. Electronic Funds Transfer From Date
	 */
	public void setFromDate(Timestamp FromDate) {
		set_Value("FROMDATE", FromDate);
	}

	/**
	 * Get From Date. Electronic Funds Transfer From Date
	 */
	public Timestamp getFromDate() {
		return (Timestamp) get_Value("FROMDATE");
	}

	/** DocAction AD_Reference_ID=135 */
	public static final int DOCACTION_AD_Reference_ID = 135;

	/** <None> = -- */
	public static final String DOCACTION_None = "--";

	/** Approve = AP */
	public static final String DOCACTION_Approve = "AP";

	/** Close = CL */
	public static final String DOCACTION_Close = "CL";

	/** Complete = CO */
	public static final String DOCACTION_Complete = "CO";

	/** Invalidate = IN */
	public static final String DOCACTION_Invalidate = "IN";

	/** Post = PO */
	public static final String DOCACTION_Post = "PO";

	/** Prepare = PR */
	public static final String DOCACTION_Prepare = "PR";

	/** Reverse - Accrual = RA */
	public static final String DOCACTION_Reverse_Accrual = "RA";

	/** Reverse - Correct = RC */
	public static final String DOCACTION_Reverse_Correct = "RC";

	/** Re-activate = RE */
	public static final String DOCACTION_Re_Activate = "RE";

	/** Reject = RJ */
	public static final String DOCACTION_Reject = "RJ";

	/** Void = VO */
	public static final String DOCACTION_Void = "VO";

	/** Wait Complete = WC */
	public static final String DOCACTION_WaitComplete = "WC";

	/** Unlock = XL */
	public static final String DOCACTION_Unlock = "XL";

	/**
	 * Set Document Action. The targeted status of the document
	 */
	public void setDocAction(String DocAction) {
		if (DocAction == null)
			throw new IllegalArgumentException("DocAction is mandatory");
		if (DocAction.equals("--") || DocAction.equals("AP")
				|| DocAction.equals("CL") || DocAction.equals("CO")
				|| DocAction.equals("IN") || DocAction.equals("PO")
				|| DocAction.equals("PR") || DocAction.equals("RA")
				|| DocAction.equals("RC") || DocAction.equals("RE")
				|| DocAction.equals("RJ") || DocAction.equals("VO")
				|| DocAction.equals("WC") || DocAction.equals("XL"))
			;
		else
			throw new IllegalArgumentException(
					"DocAction Invalid value - "
							+ DocAction
							+ " - Reference_ID=135 - -- - AP - CL - CO - IN - PO - PR - RA - RC - RE - RJ - VO - WC - XL");
		if (DocAction.length() > 2) {
			log.warning("Length > 2 - truncated");
			DocAction = DocAction.substring(0, 1);
		}
		set_Value("DocAction", DocAction);
	}

	/**
	 * Get Document Action. The targeted status of the document
	 */
	public String getDocAction() {
		return (String) get_Value("DocAction");
	}

	/** DocStatus AD_Reference_ID=131 */
	public static final int DOCSTATUS_AD_Reference_ID = 131;

	/** Unknown = ?? */
	public static final String DOCSTATUS_Unknown = "??";

	/** Approved = AP */
	public static final String DOCSTATUS_Approved = "AP";

	/** Closed = CL */
	public static final String DOCSTATUS_Closed = "CL";

	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";

	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";

	/** Invalid = IN */
	public static final String DOCSTATUS_Invalid = "IN";

	/** In Progress = IP */
	public static final String DOCSTATUS_InProgress = "IP";

	/** Not Approved = NA */
	public static final String DOCSTATUS_NotApproved = "NA";

	/** Reversed = RE */
	public static final String DOCSTATUS_Reversed = "RE";

	/** Voided = VO */
	public static final String DOCSTATUS_Voided = "VO";

	/** Waiting Confirmation = WC */
	public static final String DOCSTATUS_WaitingConfirmation = "WC";

	/** Waiting Payment = WP */
	public static final String DOCSTATUS_WaitingPayment = "WP";

	/**
	 * Set Document Status. The current status of the document
	 */
	public void setDocStatus(String DocStatus) {
		if (DocStatus == null)
			throw new IllegalArgumentException("DocStatus is mandatory");
		if (DocStatus.equals("??") || DocStatus.equals("AP")
				|| DocStatus.equals("CL") || DocStatus.equals("CO")
				|| DocStatus.equals("DR") || DocStatus.equals("IN")
				|| DocStatus.equals("IP") || DocStatus.equals("NA")
				|| DocStatus.equals("RE") || DocStatus.equals("VO")
				|| DocStatus.equals("WC") || DocStatus.equals("WP"))
			;
		else
			throw new IllegalArgumentException(
					"DocStatus Invalid value - "
							+ DocStatus
							+ " - Reference_ID=131 - ?? - AP - CL - CO - DR - IN - IP - NA - RE - VO - WC - WP");
		if (DocStatus.length() > 2) {
			log.warning("Length > 2 - truncated");
			DocStatus = DocStatus.substring(0, 1);
		}
		set_Value("DocStatus", DocStatus);
	}

	/**
	 * Get Document Status. The current status of the document
	 */
	public String getDocStatus() {
		return (String) get_Value("DocStatus");
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
	 * Set Posted. Posting status
	 */
	public void setPosted(boolean Posted) {
		set_Value("Posted", new Boolean(Posted));
	}

	/**
	 * Get Posted. Posting status
	 */
	public boolean isPosted() {
		Object oo = get_Value("Posted");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
