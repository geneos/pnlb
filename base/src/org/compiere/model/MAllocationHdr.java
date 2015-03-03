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
package org.compiere.model;

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.JOptionPane;
import org.compiere.process.*;
import org.compiere.util.*;

/**
 *  Payment Allocation Model.
 * 	Allocation Trigger update C_BPartner
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: MAllocationHdr.java,v 1.20 2005/12/01 01:55:21 jjanke Exp $
 */
public final class MAllocationHdr extends X_C_AllocationHdr implements DocAction
{
	/**
	 * 	Get Allocations of Payment
	 *	@param ctx context
	 *	@param C_Payment_ID payment
	 *	@return allocations of payment
	 */
	public static MAllocationHdr[] getOfPayment (Properties ctx, int C_Payment_ID, String trxName)
	{
		String sql = "SELECT * FROM C_AllocationHdr h "
			+ "WHERE IsActive='Y'"
			+ " AND EXISTS (SELECT * FROM C_AllocationLine l "
				+ "WHERE h.C_AllocationHdr_ID=l.C_AllocationHdr_ID AND l.C_Payment_ID=?)";
		ArrayList<MAllocationHdr> list = new ArrayList<MAllocationHdr>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			pstmt.setInt(1, C_Payment_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add (new MAllocationHdr(ctx, rs, trxName));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		MAllocationHdr[] retValue = new MAllocationHdr[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getOfPayment

	/**
	 * 	Get Allocations of Invoice
	 *	@param ctx context
	 *	@param C_Invoice_ID payment
	 *	@return allocations of payment
	 */
	public static MAllocationHdr[] getOfInvoice (Properties ctx, int C_Invoice_ID, String trxName)
	{
		String sql = "SELECT * FROM C_AllocationHdr h "
			+ "WHERE IsActive='Y'"
			+ " AND EXISTS (SELECT * FROM C_AllocationLine l "
				+ "WHERE h.C_AllocationHdr_ID=l.C_AllocationHdr_ID AND l.C_Invoice_ID=?)";
		ArrayList<MAllocationHdr> list = new ArrayList<MAllocationHdr>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			pstmt.setInt(1, C_Invoice_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add (new MAllocationHdr(ctx, rs, trxName));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		MAllocationHdr[] retValue = new MAllocationHdr[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getOfInvoice
	
	/**	Logger						*/
	private static CLogger s_log = CLogger.getCLogger(MAllocationHdr.class);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_AllocationHdr_ID id
	 */
	public MAllocationHdr (Properties ctx, int C_AllocationHdr_ID, String trxName)
	{
		super (ctx, C_AllocationHdr_ID, trxName);
		if (C_AllocationHdr_ID == 0)
		{
		//	setDocumentNo (null);
			setDateTrx (new Timestamp(System.currentTimeMillis()));
			setDateAcct (getDateTrx());
			setDocAction (DOCACTION_Complete);	// CO
			setDocStatus (DOCSTATUS_Drafted);	// DR
		//	setC_Currency_ID (0);
			setApprovalAmt (Env.ZERO);
			setIsApproved (false);
			setIsManual (false);
			//
			setPosted (false);
			setProcessed (false);
			setProcessing(false);
		}
	}	//	MAllocation

	/**
	 * 	Mandatory New Constructor
	 *	@param ctx context
	 *	@param IsManual manual trx
	 *	@param DateTrx date (if null today)
	 *	@param C_Currency_ID currency
	 */
	public MAllocationHdr (Properties ctx, boolean IsManual, Timestamp DateTrx, 
		int C_Currency_ID, String description, String trxName)
	{
		this (ctx, 0, trxName);
		setIsManual(IsManual);
		if (DateTrx != null)
		{
			setDateTrx (DateTrx);
			setDateAcct (DateTrx);
		}
		setC_Currency_ID (C_Currency_ID);
		if (description != null)
			setDescription(description);
	}	//  create Allocation

	/** 
	 * 	Load Constructor
	 * 	@param ctx context
	 *	@param rs result set
	 */
	public MAllocationHdr (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MAllocation

	/**	Lines						*/
	private MAllocationLine[]	m_lines = null;
	
	/**
	 * 	Get Lines
	 *	@param requery if true requery
	 *	@return lines
	 */
	public MAllocationLine[] getLines (boolean requery)
	{
		if (m_lines != null && !requery)
			return m_lines;
		//
		String sql = "SELECT * FROM C_AllocationLine WHERE C_AllocationHdr_ID=?";
		ArrayList<MAllocationLine> list = new ArrayList<MAllocationLine>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getC_AllocationHdr_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MAllocationLine line = new MAllocationLine(getCtx(), rs, get_TrxName());
				line.setParent(this);
				list.add (line);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		} catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		} catch (Exception e)
		{
			pstmt = null;
		}
		//
		m_lines = new MAllocationLine[list.size ()];
		list.toArray (m_lines);
		return m_lines;
	}	//	getLines

	/**
	 * 	Set Processed
	 *	@param processed Processed
	 */
	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
		String sql = "UPDATE C_AllocationHdr SET Processed='"
			+ (processed ? "Y" : "N")
			+ "' WHERE C_AllocationHdr_ID=" + getC_AllocationHdr_ID();
		int no = DB.executeUpdate(sql, get_TrxName());
		m_lines = null;
		log.fine(processed + " - #" + no);
	}	//	setProcessed
	
	
	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord
	 *	@return save
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Changed from Not to Active
		if (!newRecord && is_ValueChanged("IsActive") && isActive())
		{
			log.severe ("Cannot Re-Activate deactivated Allocations");
			return false;
		}
		return true;
	}	//	beforeSave

	/**
	 * 	Before Delete.
	 *	@return true if acct was deleted
	 */
	protected boolean beforeDelete ()
	{
		String trxName = get_TrxName();
		if (trxName == null || trxName.length() == 0)
			log.warning ("No transaction");
		if (isPosted())
		{
			if (!MPeriod.isOpen(getCtx(), getDateTrx(), MDocType.DOCBASETYPE_PaymentAllocation))
			{
                                JOptionPane.showMessageDialog(null,"El periodo esta cerrado para "+getDocumentInfo(), "Error al Borrar "+Msg.getElement(getCtx(), "C_AllocationHdr_ID"), JOptionPane.INFORMATION_MESSAGE); 
				log.warning ("Period Closed");
				return false;
			}
			setPosted(false);
			if (MFactAcct.delete (Table_ID, get_ID(), trxName) < 0)
				return false;
		}
		//	Mark as Inactive
		setIsActive(false);		//	updated DB for line delete/process
		String sql = "UPDATE C_AllocationHdr SET IsActive='N' WHERE C_AllocationHdr_ID=?";
		DB.executeUpdate(sql, getC_AllocationHdr_ID(), trxName);
		
		//	Unlink
		getLines(true);
		HashSet<Integer> bps = new HashSet<Integer>();
		for (int i = 0; i < m_lines.length; i++)
		{
			MAllocationLine line = m_lines[i];
			bps.add(new Integer(line.getC_BPartner_ID()));
			if (!line.delete(true, trxName))
				return false;
		}
		updateBP(bps);
		return true;
	}	//	beforeDelete

	/**
	 * 	After Save
	 *	@param newRecord
	 *	@param success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		return success;
	}	//	afterSave
	
	/**************************************************************************
	 * 	Process document
	 *	@param processAction document action
	 *	@return true if performed
	 */
	public boolean processIt (String processAction)
	{
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (processAction, getDocAction());
	}	//	processIt
	
	/**	Process Message 			*/
	private String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	private boolean		m_justPrepared = false;

	/**
	 * 	Unlock Document.
	 * 	@return true if success 
	 */
	public boolean unlockIt()
	{
		log.info(toString());
		setProcessing(false);
		return true;
	}	//	unlockIt
	
	/**
	 * 	Invalidate Document
	 * 	@return true if success 
	 */
	public boolean invalidateIt()
	{
		log.info(toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt
	
	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid) 
	 */
	public String prepareIt()
	{
		log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		//	Std Period open?
		if (!MPeriod.isOpen(getCtx(), getDateAcct(), MDocType.DOCBASETYPE_PaymentAllocation))
		{
			m_processMsg = "@PeriodClosed@";
			return DocAction.STATUS_Invalid;
		}
		getLines(false);
		if (m_lines.length == 0)
		{
			m_processMsg = "@NoLines@";
			return DocAction.STATUS_Invalid;
		}
		//	Add up Amounts & validate
		BigDecimal approval = Env.ZERO;
		for (int i = 0; i < m_lines.length; i++)
		{
			MAllocationLine line = m_lines[i];
			approval = approval.add(line.getWriteOffAmt()).add(line.getDiscountAmt());
			//	Make sure there is BP
			if (line.getC_BPartner_ID() == 0)
			{
				m_processMsg = "No Business Partner";
				return DocAction.STATUS_Invalid;
			}
		}
		setApprovalAmt(approval);
		//
		m_justPrepared = true;
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocAction.STATUS_InProgress;
	}	//	prepareIt
	
	/**
	 * 	Approve Document
	 * 	@return true if success 
	 */
	public boolean  approveIt()
	{
		log.info(toString());
		setIsApproved(true);
		return true;
	}	//	approveIt
	
	/**
	 * 	Reject Approval
	 * 	@return true if success 
	 */
	public boolean rejectIt()
	{
		log.info(toString());
		setIsApproved(false);
		return true;
	}	//	rejectIt
	
	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		//	Re-Check
		if (!m_justPrepared)
		{
			String status = prepareIt();
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}
		//	Implicit Approval
		if (!isApproved())
			approveIt();
		log.info(toString());

		//	Link
		getLines(false);
		HashSet<Integer> bps = new HashSet<Integer>();
		for (int i = 0; i < m_lines.length; i++)
		{
			MAllocationLine line = m_lines[i];
			bps.add(new Integer(line.processIt(false)));	//	not reverse
		}
		updateBP(bps);

		//	User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}

		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}	//	completeIt
	
	/**
	 * 	Void Document.
	 * 	Same as Close.
	 * 	@return true if success 
	 */
	public boolean voidIt()
	{
		log.info(toString());
		boolean retValue = reverseIt();
		setDocAction(DOCACTION_None);
		return retValue;
	}	//	voidIt
	
	/**
	 * 	Close Document.
	 * 	Cancel not delivered Qunatities
	 * 	@return true if success 
	 */
	public boolean closeIt()
	{
		log.info(toString());

		setDocAction(DOCACTION_None);
		return true;
	}	//	closeIt
	
	/**
	 * 	Reverse Correction
	 * 	@return true if success 
	 */
	public boolean reverseCorrectIt()
	{
		log.info(toString());
		boolean retValue = reverseIt();
		setDocAction(DOCACTION_None);
		return retValue;
	}	//	reverseCorrectionIt
	
	/**
	 * 	Reverse Accrual - none
	 * 	@return false 
	 */
	public boolean reverseAccrualIt()
	{
		log.info(toString());
		boolean retValue = reverseIt();
		setDocAction(DOCACTION_None);
		return retValue;
	}	//	reverseAccrualIt
	
	/** 
	 * 	Re-activate
	 * 	@return false 
	 */
	public boolean reActivateIt()
	{
		log.info(toString());
		return false;
	}	//	reActivateIt

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MAllocationHdr[");
		sb.append(get_ID()).append("-").append(getSummary()).append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		return Msg.getElement(getCtx(), "C_AllocationHdr_ID") + " " + getDocumentNo();
	}	//	getDocumentInfo

	/**
	 * 	Create PDF
	 *	@return File or null
	 */
	public File createPDF ()
	{
		try
		{
			File temp = File.createTempFile(get_TableName()+get_ID()+"_", ".pdf");
			return createPDF (temp);
		}
		catch (Exception e)
		{
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	}	//	getPDF

	/**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return file if success
	 */
	public File createPDF (File file)
	{
	//	ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.INVOICE, getC_Invoice_ID());
	//	if (re == null)
			return null;
	//	return re.getPDF(file);
	}	//	createPDF

	/*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	public String getSummary()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getDocumentNo());
		//	: Total Lines = 123.00 (#1)
		sb.append(": ")
			.append(Msg.translate(getCtx(),"ApprovalAmt")).append("=").append(getApprovalAmt())
			.append(" (#").append(getLines(false).length).append(")");
		//	 - Description
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}	//	getSummary
	
	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	public String getProcessMsg()
	{
		return m_processMsg;
	}	//	getProcessMsg
	
	/**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID()
	{
		return getCreatedBy();
	}	//	getDoc_User_ID

	
	/**************************************************************************
	 * 	Reverse Allocation.
	 * 	Period needs to be open
	 *	@return true if reversed
	 */
	private boolean reverseIt() 
	{
		if (!isActive())
			throw new IllegalStateException("Allocation already reversed (not active)");

		//	Can we delete posting
		if (!MPeriod.isOpen(getCtx(), getDateTrx(), MPeriodControl.DOCBASETYPE_PaymentAllocation))
			throw new IllegalStateException("@PeriodClosed@");

		//	Set Inactive
		setIsActive (false);
		setDocumentNo(getDocumentNo()+"^");
		setDocStatus(DOCSTATUS_Reversed);	//	for direct calls
		if (!save() || isActive())
			throw new IllegalStateException("Cannot de-activate allocation");
			
		//	Delete Posting
		String sql = "DELETE FROM Fact_Acct WHERE AD_Table_ID=" + MAllocationHdr.getTableId(MAllocationHdr.Table_Name)
			+ " AND Record_ID=" + getC_AllocationHdr_ID();
		int no = DB.executeUpdate(sql, get_TrxName());
		log.fine("Fact_Acct deleted #" + no);
		
		//	Unlink Invoices
		getLines(true);
		HashSet<Integer> bps = new HashSet<Integer>();
		for (int i = 0; i < m_lines.length; i++)
		{
			MAllocationLine line = m_lines[i];
			line.setIsActive(false);
			line.save();
			bps.add(new Integer(line.processIt(true)));	//	reverse
		}
		updateBP(bps);
		return true;
	}	//	reverse

	
	/**
	 * 	Update Open Balance of BP's
	 *	@param bps list of business partners
	 */
	private void updateBP(HashSet<Integer> bps)
	{
		log.info("#" + bps.size());
		Iterator<Integer> it = bps.iterator();
		while (it.hasNext())
		{
			int C_BPartner_ID = it.next();
			MBPartner bp = new MBPartner(getCtx(), C_BPartner_ID, get_TrxName());
			bp.setTotalOpenBalance();		//	recalculates from scratch
		//	bp.setSOCreditStatus();			//	called automatically
			if (bp.save())
				log.fine(bp.toString());
			else
				log.log(Level.SEVERE, "BP not updated - " + bp);
		}
	}	//	updateBP
	
	public List getContabilidad()
    {
    	List<MFactAcct> list = new ArrayList<MFactAcct>();
    	
    	String sql = "SELECT Fact_Acct_ID "
			+ " FROM Fact_Acct "
			+ " WHERE Record_ID=? AND AD_Table_ID=? ";
		
	    try
	    {
	    	PreparedStatement pstm = DB.prepareStatement(sql, get_TrxName());
	    	pstm.setInt(1,getC_AllocationHdr_ID());
	    	pstm.setInt(2,get_Table_ID());
	    	ResultSet rs = pstm.executeQuery();
		    
		    while (rs.next())
		    {
		    	MFactAcct cont = new MFactAcct(getCtx(),rs.getInt(1),get_TrxName());
		    	list.add(cont);
		    }
	    }
	    catch (SQLException e){}
	    
	    return list;
    }
	
	protected void anularContabilidad()	{
    	//	Acct Reversed (this)
		List fAcct = getContabilidad();
		for (int i = 0; i < fAcct.size(); i++)
			((MFactAcct)fAcct.get(i)).delete(true);
    }
	
}   //  MAllocation
