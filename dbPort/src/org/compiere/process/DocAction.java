/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.process;

import java.util.*;
import java.io.*;
import java.math.*;
import org.compiere.util.*;

/**
 *	Document Action Interface
 *	
 *  @author Jorg Janke
 *  @version $Id: DocAction.java,v 1.16 2005/11/12 22:59:45 jjanke Exp $
 */
public interface DocAction
{
	/** Complete = CO */
	public static final String ACTION_Complete = "CO";
	/** Wait Complete = WC */
	public static final String ACTION_WaitComplete = "WC";
	/** Approve = AP */
	public static final String ACTION_Approve = "AP";
	/** Reject = RJ */
	public static final String ACTION_Reject = "RJ";
	/** Post = PO */
	public static final String ACTION_Post = "PO";
	/** Void = VO */
	public static final String ACTION_Void = "VO";
	/** Close = CL */
	public static final String ACTION_Close = "CL";
	/** Reverse - Correct = RC */
	public static final String ACTION_Reverse_Correct = "RC";
	/** Reverse - Accrual = RA */
	public static final String ACTION_Reverse_Accrual = "RA";
	/** ReActivate = RE */
	public static final String ACTION_ReActivate = "RE";
	/** <None> = -- */
	public static final String ACTION_None = "--";
	/** Prepare = PR */
	public static final String ACTION_Prepare = "PR";
	/** Unlock = XL */
	public static final String ACTION_Unlock = "XL";
	/** Invalidate = IN */
	public static final String ACTION_Invalidate = "IN";
	/** ReOpen = OP */
	public static final String ACTION_ReOpen = "OP";

	/** Drafted = DR */
	public static final String STATUS_Drafted = "DR";
	/** Completed = CO */
	public static final String STATUS_Completed = "CO";
	/** Approved = AP */
	public static final String STATUS_Approved = "AP";
	/** Invalid = IN */
	public static final String STATUS_Invalid = "IN";
	/** Not Approved = NA */
	public static final String STATUS_NotApproved = "NA";
	/** Voided = VO */
	public static final String STATUS_Voided = "VO";
	/** Reversed = RE */
	public static final String STATUS_Reversed = "RE";
	/** Closed = CL */
	public static final String STATUS_Closed = "CL";
	/** Unknown = ?? */
	public static final String STATUS_Unknown = "??";
	/** In Progress = IP */
	public static final String STATUS_InProgress = "IP";
	/** Waiting Payment = WP */
	public static final String STATUS_WaitingPayment = "WP";
	/** Waiting Confirmation = WC */
	public static final String STATUS_WaitingConfirmation = "WC";


	/**
	 * 	Set Doc Status
	 *	@param newStatus new Status
	 */
	public void setDocStatus (String newStatus);

	/**
	 * 	Get Doc Status
	 *	@return Document Status
	 */
	public String getDocStatus();
	
	
	/*************************************************************************
	 * 	Process document
	 *	@param action document action
	 *	@return true if performed
	 */
	public boolean processIt (String action) throws Exception;
	
	/**
	 * 	Unlock Document.
	 * 	@return true if success 
	 */
	public boolean unlockIt();
	/**
	 * 	Invalidate Document
	 * 	@return true if success 
	 */
	public boolean invalidateIt();
	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid) 
	 */
	public String prepareIt();
	/**
	 * 	Approve Document
	 * 	@return true if success 
	 */
	public boolean  approveIt();
	/**
	 * 	Reject Approval
	 * 	@return true if success 
	 */
	public boolean rejectIt();
	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt();
	/**
	 * 	Void Document
	 * 	@return true if success 
	 */
	public boolean voidIt();
	/**
	 * 	Close Document
	 * 	@return true if success 
	 */
	public boolean closeIt();
	/**
	 * 	Reverse Correction
	 * 	@return true if success 
	 */
	public boolean reverseCorrectIt();
	/**
	 * 	Reverse Accrual
	 * 	@return true if success 
	 */
	public boolean reverseAccrualIt();
	/** 
	 * 	Re-activate
	 * 	@return true if success 
	 */
	public boolean reActivateIt();

	/**************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	public String getSummary();

	/**
	 * 	Get Document No
	 *	@return Document No
	 */
	public String getDocumentNo();

	/**
	 * 	Get Document Info
	 *	@return Type and Document No
	 */
	public String getDocumentInfo();

	/**
	 * 	Create PDF
	 *	@return file
	 */
	public File createPDF ();
	
	/**
	 * 	Get Process Message
	 *	@return clear text message
	 */
	public String getProcessMsg ();
	
	/**
	 * 	Get Document Owner
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID();
	
	/**
	 * 	Get Document Currency
	 *	@return C_Currency_ID
	 */
	public int getC_Currency_ID();

	/**
	 * 	Get Document Approval Amount
	 *	@return amount
	 */
	public BigDecimal getApprovalAmt();

	/**
	 * 	Get Document Client
	 *	@return AD_Client_ID
	 */
	public int getAD_Client_ID();

	/**
	 * 	Get Document Organization
	 *	@return AD_Org_ID
	 */
	public int getAD_Org_ID();

	/**
	 * 	Get Doc Action
	 *	@return Document Action
	 */
	public String getDocAction();

	/**
	 * 	Save Document
	 *	@return true if saved
	 */
	public boolean save();
	
	/**
	 * 	Get Context
	 *	@return context
	 */
	public Properties getCtx();
	
	/**
	 * 	Get ID of record
	 *	@return ID
	 */
	public int get_ID();
	
	/**
	 * 	Get AD_Table_ID
	 *	@return AD_Table_ID
	 */
	public int get_Table_ID();
	
	/**
	 * 	Get Logger
	 *	@return logger
	 */
	public CLogger get_Logger();

	/**
	 * 	Get Transaction
	 *	@return trx name
	 */
	public String get_TrxName();

}	//	DocAction
