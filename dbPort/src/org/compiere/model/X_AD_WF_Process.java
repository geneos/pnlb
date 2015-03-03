/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_WF_Process
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.89
 */
public class X_AD_WF_Process extends PO {
	/** Standard Constructor */
	public X_AD_WF_Process(Properties ctx, int AD_WF_Process_ID, String trxName) {
		super(ctx, AD_WF_Process_ID, trxName);
		/**
		 * if (AD_WF_Process_ID == 0) { setAD_Table_ID (0); setAD_WF_Process_ID
		 * (0); setAD_WF_Responsible_ID (0); setAD_Workflow_ID (0); setProcessed
		 * (false); setRecord_ID (0); setWFState (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_WF_Process(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_WF_Process */
	public static final String Table_Name = "AD_WF_Process";

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

	protected BigDecimal accessLevel = new BigDecimal(7);

	/** AccessLevel 7 - System - Client - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_AD_WF_Process[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Message. System Message
	 */
	public void setAD_Message_ID(int AD_Message_ID) {
		if (AD_Message_ID <= 0)
			set_Value("AD_Message_ID", null);
		else
			set_Value("AD_Message_ID", new Integer(AD_Message_ID));
	}

	/**
	 * Get Message. System Message
	 */
	public int getAD_Message_ID() {
		Integer ii = (Integer) get_Value("AD_Message_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Table. Table for the Fields
	 */
	public void setAD_Table_ID(int AD_Table_ID) {
		if (AD_Table_ID < 1)
			throw new IllegalArgumentException("AD_Table_ID is mandatory.");
		set_Value("AD_Table_ID", new Integer(AD_Table_ID));
	}

	/**
	 * Get Table. Table for the Fields
	 */
	public int getAD_Table_ID() {
		Integer ii = (Integer) get_Value("AD_Table_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_User_ID AD_Reference_ID=286 */
	public static final int AD_USER_ID_AD_Reference_ID = 286;

	/**
	 * Set User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public void setAD_User_ID(int AD_User_ID) {
		if (AD_User_ID <= 0)
			set_Value("AD_User_ID", null);
		else
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

	/**
	 * Set Workflow Process. Actual Workflow Process Instance
	 */
	public void setAD_WF_Process_ID(int AD_WF_Process_ID) {
		if (AD_WF_Process_ID < 1)
			throw new IllegalArgumentException("AD_WF_Process_ID is mandatory.");
		set_ValueNoCheck("AD_WF_Process_ID", new Integer(AD_WF_Process_ID));
	}

	/**
	 * Get Workflow Process. Actual Workflow Process Instance
	 */
	public int getAD_WF_Process_ID() {
		Integer ii = (Integer) get_Value("AD_WF_Process_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Workflow Responsible. Responsible for Workflow Execution
	 */
	public void setAD_WF_Responsible_ID(int AD_WF_Responsible_ID) {
		if (AD_WF_Responsible_ID < 1)
			throw new IllegalArgumentException(
					"AD_WF_Responsible_ID is mandatory.");
		set_Value("AD_WF_Responsible_ID", new Integer(AD_WF_Responsible_ID));
	}

	/**
	 * Get Workflow Responsible. Responsible for Workflow Execution
	 */
	public int getAD_WF_Responsible_ID() {
		Integer ii = (Integer) get_Value("AD_WF_Responsible_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Workflow. Workflow or combination of tasks
	 */
	public void setAD_Workflow_ID(int AD_Workflow_ID) {
		if (AD_Workflow_ID < 1)
			throw new IllegalArgumentException("AD_Workflow_ID is mandatory.");
		set_Value("AD_Workflow_ID", new Integer(AD_Workflow_ID));
	}

	/**
	 * Get Workflow. Workflow or combination of tasks
	 */
	public int getAD_Workflow_ID() {
		Integer ii = (Integer) get_Value("AD_Workflow_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getAD_Workflow_ID()));
	}

	/**
	 * Set Priority. Indicates if this request is of a high, medium or low
	 * priority.
	 */
	public void setPriority(int Priority) {
		set_Value("Priority", new Integer(Priority));
	}

	/**
	 * Get Priority. Indicates if this request is of a high, medium or low
	 * priority.
	 */
	public int getPriority() {
		Integer ii = (Integer) get_Value("Priority");
		if (ii == null)
			return 0;
		return ii.intValue();
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

	/**
	 * Set Record ID. Direct internal record ID
	 */
	public void setRecord_ID(int Record_ID) {
		if (Record_ID < 0)
			throw new IllegalArgumentException("Record_ID is mandatory.");
		set_Value("Record_ID", new Integer(Record_ID));
	}

	/**
	 * Get Record ID. Direct internal record ID
	 */
	public int getRecord_ID() {
		Integer ii = (Integer) get_Value("Record_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Text Message. Text Message
	 */
	public void setTextMsg(String TextMsg) {
		if (TextMsg != null && TextMsg.length() > 2000) {
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

	/** WFState AD_Reference_ID=305 */
	public static final int WFSTATE_AD_Reference_ID = 305;

	/** Aborted = CA */
	public static final String WFSTATE_Aborted = "CA";

	/** Completed = CC */
	public static final String WFSTATE_Completed = "CC";

	/** Terminated = CT */
	public static final String WFSTATE_Terminated = "CT";

	/** Not Started = ON */
	public static final String WFSTATE_NotStarted = "ON";

	/** Running = OR */
	public static final String WFSTATE_Running = "OR";

	/** Suspended = OS */
	public static final String WFSTATE_Suspended = "OS";

	/**
	 * Set Workflow State. State of the execution of the workflow
	 */
	public void setWFState(String WFState) {
		if (WFState == null)
			throw new IllegalArgumentException("WFState is mandatory");
		if (WFState.equals("CA") || WFState.equals("CC")
				|| WFState.equals("CT") || WFState.equals("ON")
				|| WFState.equals("OR") || WFState.equals("OS"))
			;
		else
			throw new IllegalArgumentException("WFState Invalid value - "
					+ WFState
					+ " - Reference_ID=305 - CA - CC - CT - ON - OR - OS");
		if (WFState.length() > 2) {
			log.warning("Length > 2 - truncated");
			WFState = WFState.substring(0, 1);
		}
		set_Value("WFState", WFState);
	}

	/**
	 * Get Workflow State. State of the execution of the workflow
	 */
	public String getWFState() {
		return (String) get_Value("WFState");
	}
}
