/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for RV_Operation_Activity
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.328
 */
public class X_RV_Operation_Activity extends PO {
	/** Standard Constructor */
	public X_RV_Operation_Activity(Properties ctx,
			int RV_Operation_Activity_ID, String trxName) {
		super(ctx, RV_Operation_Activity_ID, trxName);
		/**
		 * if (RV_Operation_Activity_ID == 0) { setMPC_Order_ID (0); setValue
		 * (null); }
		 */
	}

	/** Load Constructor */
	public X_RV_Operation_Activity(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=RV_Operation_Activity */
	public static final String Table_Name = "RV_Operation_Activity";

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

	protected BigDecimal accessLevel = new BigDecimal(4);

	/** AccessLevel 4 - System */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_RV_Operation_Activity[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Finish Date Scheduled. Last date of the schedule.
	 */
	public void setDateFinishSchedule(Timestamp DateFinishSchedule) {
		set_Value("DateFinishSchedule", DateFinishSchedule);
	}

	/**
	 * Get Finish Date Scheduled. Last date of the schedule.
	 */
	public Timestamp getDateFinishSchedule() {
		return (Timestamp) get_Value("DateFinishSchedule");
	}

	/**
	 * Set Starting Date Scheduled. Starting Date Scheduled
	 */
	public void setDateStartSchedule(Timestamp DateStartSchedule) {
		set_Value("DateStartSchedule", DateStartSchedule);
	}

	/**
	 * Get Starting Date Scheduled. Starting Date Scheduled
	 */
	public Timestamp getDateStartSchedule() {
		return (Timestamp) get_Value("DateStartSchedule");
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
		if (DocStatus != null && DocStatus.length() > 2) {
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
	 * Set Duration. Normal Duration in Duration Unit
	 */
	public void setDuration(BigDecimal Duration) {
		set_Value("Duration", Duration);
	}

	/**
	 * Get Duration. Normal Duration in Duration Unit
	 */
	public BigDecimal getDuration() {
		BigDecimal bd = (BigDecimal) get_Value("Duration");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Duration Real */
	public void setDurationReal(BigDecimal DurationReal) {
		set_Value("DurationReal", DurationReal);
	}

	/** Get Duration Real */
	public BigDecimal getDurationReal() {
		BigDecimal bd = (BigDecimal) get_Value("DurationReal");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Duration Requiered */
	public void setDurationRequiered(BigDecimal DurationRequiered) {
		set_Value("DurationRequiered", DurationRequiered);
	}

	/** Get Duration Requiered */
	public BigDecimal getDurationRequiered() {
		BigDecimal bd = (BigDecimal) get_Value("DurationRequiered");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** MPC_Order_ID AD_Reference_ID=1000045 */
	public static final int MPC_ORDER_ID_AD_Reference_ID = 1000045;

	/**
	 * Set Manufacturing Order. Manufacturing Order
	 */
	public void setMPC_Order_ID(int MPC_Order_ID) {
		if (MPC_Order_ID < 1)
			throw new IllegalArgumentException("MPC_Order_ID is mandatory.");
		set_Value("MPC_Order_ID", new Integer(MPC_Order_ID));
	}

	/**
	 * Get Manufacturing Order. Manufacturing Order
	 */
	public int getMPC_Order_ID() {
		Integer ii = (Integer) get_Value("MPC_Order_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Delivered Quantity. Delivered Quantity
	 */
	public void setQtyDelivered(BigDecimal QtyDelivered) {
		set_Value("QtyDelivered", QtyDelivered);
	}

	/**
	 * Get Delivered Quantity. Delivered Quantity
	 */
	public BigDecimal getQtyDelivered() {
		BigDecimal bd = (BigDecimal) get_Value("QtyDelivered");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Qty Reject */
	public void setQtyReject(BigDecimal QtyReject) {
		set_Value("QtyReject", QtyReject);
	}

	/** Get Qty Reject */
	public BigDecimal getQtyReject() {
		BigDecimal bd = (BigDecimal) get_Value("QtyReject");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Qty Scrap */
	public void setQtyScrap(BigDecimal QtyScrap) {
		set_Value("QtyScrap", QtyScrap);
	}

	/** Get Qty Scrap */
	public BigDecimal getQtyScrap() {
		BigDecimal bd = (BigDecimal) get_Value("QtyScrap");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Resource. Resource
	 */
	public void setS_Resource_ID(int S_Resource_ID) {
		if (S_Resource_ID <= 0)
			set_Value("S_Resource_ID", null);
		else
			set_Value("S_Resource_ID", new Integer(S_Resource_ID));
	}

	/**
	 * Get Resource. Resource
	 */
	public int getS_Resource_ID() {
		Integer ii = (Integer) get_Value("S_Resource_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public void setValue(String Value) {
		if (Value == null)
			throw new IllegalArgumentException("Value is mandatory.");
		if (Value.length() > 40) {
			log.warning("Length > 40 - truncated");
			Value = Value.substring(0, 39);
		}
		set_Value("Value", Value);
	}

	/**
	 * Get Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public String getValue() {
		return (String) get_Value("Value");
	}
}
