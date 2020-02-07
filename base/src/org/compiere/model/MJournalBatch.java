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
import org.compiere.process.*;
import org.compiere.util.*;

/**
 *  Journal Batch Model
 *
 *	@author Jorg Janke
 *	@version $Id: MJournalBatch.java,v 1.18 2005/12/19 01:16:45 jjanke Exp $
 */
public class MJournalBatch extends X_GL_JournalBatch implements DocAction
{
	/**
	 * 	Create new Journal Batch by copying
	 * 	@param ctx context
	 *	@param GL_JournalBatch_ID journal batch
	 * 	@param dateDoc date of the document date
	 *	@return Journal Batch
	 */
	public static MJournalBatch copyFrom (Properties ctx, int GL_JournalBatch_ID, Timestamp dateDoc, String trxName)
	{
		MJournalBatch from = new MJournalBatch (ctx, GL_JournalBatch_ID, trxName);
		if (from.getGL_JournalBatch_ID() == 0)
			throw new IllegalArgumentException ("From Journal Batch not found GL_JournalBatch_ID=" + GL_JournalBatch_ID);
		//
		MJournalBatch to = new MJournalBatch (ctx, 0, trxName);
		PO.copyValues(from, to, from.getAD_Client_ID(), from.getAD_Org_ID());
		to.set_ValueNoCheck ("DocumentNo", null);
		to.setDateAcct(dateDoc);
		to.setDateDoc(dateDoc);
		to.setDocStatus(DOCSTATUS_Drafted);
		to.setDocAction(DOCACTION_Complete);
		to.setIsApproved(false);
		//
		if (!to.save())
			throw new IllegalStateException("Could not create Journal Batch");

		if (to.copyDetailsFrom(from) == 0)
			throw new IllegalStateException("Could not create Journal Batch Details");

		return to;
	}	//	copyFrom
	
	
	/**************************************************************************
	 * 	Standard Construvtore
	 *	@param ctx context
	 *	@param GL_JournalBatch_ID id if 0 - create actual batch
	 */
	public MJournalBatch (Properties ctx, int GL_JournalBatch_ID, String trxName)
	{
		super (ctx, GL_JournalBatch_ID, trxName);
		if (GL_JournalBatch_ID == 0)
		{
		//	setGL_JournalBatch_ID (0);	PK
		//	setDescription (null);
		//	setDocumentNo (null);
		//	setC_DocType_ID (0);
			setPostingType (POSTINGTYPE_Actual);
			setDocAction (DOCACTION_Complete);
			setDocStatus (DOCSTATUS_Drafted);
			setTotalCr (Env.ZERO);
			setTotalDr (Env.ZERO);
			setProcessed (false);
			setProcessing (false);
			setIsApproved(false);
		}
	}	//	MJournalBatch

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MJournalBatch (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MJournalBatch

	/**
	 * 	Copy Constructor.
	 * 	Dos not copy: Dates/Period
	 *	@param original original
	 */
	public MJournalBatch (MJournalBatch original)
	{
		this (original.getCtx(), 0, original.get_TrxName());
		setClientOrg(original);
		setGL_JournalBatch_ID(original.getGL_JournalBatch_ID());
		//
	//	setC_AcctSchema_ID(original.getC_AcctSchema_ID());
	//	setGL_Budget_ID(original.getGL_Budget_ID());
		setGL_Category_ID(original.getGL_Category_ID());
		setPostingType(original.getPostingType());
		setDescription(original.getDescription());
		setC_DocType_ID(original.getC_DocType_ID());
		setControlAmt(original.getControlAmt());
		//
		setC_Currency_ID(original.getC_Currency_ID());
	//	setC_ConversionType_ID(original.getC_ConversionType_ID());
	//	setCurrencyRate(original.getCurrencyRate());
		
	//	setDateDoc(original.getDateDoc());
	//	setDateAcct(original.getDateAcct());
	//	setC_Period_ID(original.getC_Period_ID());
	}	//	MJournal
	
	
	
	/**
	 * 	Overwrite Client/Org if required
	 * 	@param AD_Client_ID client
	 * 	@param AD_Org_ID org
	 */
	public void setClientOrg (int AD_Client_ID, int AD_Org_ID)
	{
		super.setClientOrg(AD_Client_ID, AD_Org_ID);
	}	//	setClientOrg

	/**
	 * 	Set Accounting Date.
	 * 	Set also Period if not set earlier
	 *	@param DateAcct date
	 */
	public void setDateAcct (Timestamp DateAcct)
	{
		super.setDateAcct(DateAcct);
		if (DateAcct == null)
			return;
		if (getC_Period_ID() != 0)
			return;
		int C_Period_ID = MPeriod.getC_Period_ID(getCtx(), DateAcct);
		if (C_Period_ID == 0)
			log.warning("Period not found");
		else
			setC_Period_ID(C_Period_ID);
	}	//	setDateAcct

	/**
	 * 	Get Journal Lines
	 * 	@param requery requery
	 *	@return Array of lines
	 */
	public MJournal[] getJournals (boolean requery)
	{
		ArrayList<MJournal> list = new ArrayList<MJournal>();
		String sql = "SELECT * FROM GL_Journal WHERE GL_JournalBatch_ID=? ORDER BY DocumentNo";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getGL_JournalBatch_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MJournal (getCtx(), rs, get_TrxName()));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, sql, ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		//
		MJournal[] retValue = new MJournal[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getJournals

	/**
	 * 	Copy Journal/Lines from other Journal Batch
	 *	@param jb Journal Batch
	 *	@return number of journals + lines copied
	 */
	public int copyDetailsFrom (MJournalBatch jb)
	{
		if (isProcessed() || jb == null)
			return 0;
		int count = 0;
		int lineCount = 0;
		MJournal[] fromJournals = jb.getJournals(false);
		for (int i = 0; i < fromJournals.length; i++)
		{
			MJournal toJournal = new MJournal (getCtx(), 0, jb.get_TrxName());
			PO.copyValues(fromJournals[i], toJournal, getAD_Client_ID(), getAD_Org_ID());
			toJournal.setGL_JournalBatch_ID(getGL_JournalBatch_ID());
			toJournal.set_ValueNoCheck ("DocumentNo", null);	//	create new
			toJournal.setDateDoc(getDateDoc());		//	dates from this Batch
			toJournal.setDateAcct(getDateAcct());
			toJournal.setDocStatus(MJournal.DOCSTATUS_Drafted);
			toJournal.setDocAction(MJournal.DOCACTION_Complete);
			toJournal.setTotalCr(Env.ZERO);
			toJournal.setTotalDr(Env.ZERO);
			toJournal.setIsApproved(false);
			toJournal.setIsPrinted(false);
			toJournal.setPosted(false);
			toJournal.setProcessed(false);
			if (toJournal.save())
			{
				count++;
				lineCount += toJournal.copyLinesFrom(fromJournals[i], getDateAcct(), 'x');
			}
		}
		if (fromJournals.length != count)
			log.log(Level.SEVERE, "copyDetailsFrom - Line difference - Journals=" + fromJournals.length + " <> Saved=" + count);

		return count + lineCount;
	}	//	copyLinesFrom

	
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
	}	//	process
	
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
		log.info("unlockIt - " + toString());
		setProcessing(false);
		return true;
	}	//	unlockIt
	
	/**
	 * 	Invalidate Document
	 * 	@return true if success 
	 */
	public boolean invalidateIt()
	{
		log.info("invalidateIt - " + toString());
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
		// Comentar por que la ventana no tiene mas tipo de documento.
		/*
		 * MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		 * if (!MPeriod.isOpen(getCtx(), getDateAcct(), dt.getDocBaseType()))
		 * {
		 *	 m_processMsg = "@PeriodClosed@";
		 *	 return DocAction.STATUS_Invalid;
		 * }
		*/
		
		//	Add up Amounts & prepare them
		MJournal[] journals = getJournals(false);
		if (journals.length == 0)
		{
			m_processMsg = "@NoLines@";
			return DocAction.STATUS_Invalid;
		}
		
		BigDecimal TotalDr = Env.ZERO;
		BigDecimal TotalCr = Env.ZERO;		
		for (int i = 0; i < journals.length; i++)
		{
			MJournal journal = journals[i];
			if (!journal.isActive())
				continue;
			//	Prepare if not closed
			if (DOCSTATUS_Closed.equals(journal.getDocStatus())
				|| DOCSTATUS_Voided.equals(journal.getDocStatus())
				|| DOCSTATUS_Reversed.equals(journal.getDocStatus())
				|| DOCSTATUS_Completed.equals(journal.getDocStatus()))
				;
			else
			{
				String status = journal.prepareIt();
				if (!DocAction.STATUS_InProgress.equals(status))
				{
					journal.setDocStatus(status);
					journal.save();
					m_processMsg = journal.getProcessMsg();
					return status;
				}
				journal.setDocStatus(DOCSTATUS_InProgress);
				journal.save();
			}
			//
			TotalDr = TotalDr.add(journal.getTotalDr());
			TotalCr = TotalCr.add(journal.getTotalCr());
		}
		setTotalDr(TotalDr);
		setTotalCr(TotalCr);
		
		//	Control Amount
		/*if (Env.ZERO.compareTo(getControlAmt()) != 0
			&& getControlAmt().compareTo(getTotalDr()) != 0)
		{
			m_processMsg = "@ControlAmtError@";
			return DocAction.STATUS_Invalid;
		}
		*/
		//	Add up Amounts
		m_justPrepared = true;
		return DocAction.STATUS_InProgress;
	}	//	prepareIt
	
	/**
	 * 	Approve Document
	 * 	@return true if success 
	 */
	public boolean  approveIt()
	{
		log.info("approveIt - " + toString());
		setIsApproved(true);
		return true;
	}	//	approveIt
	
	/**
	 * 	Reject Approval
	 * 	@return true if success 
	 */
	public boolean rejectIt()
	{
		log.info("rejectIt - " + toString());
		setIsApproved(false);
		return true;
	}	//	rejectIt
	
	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		log.info("completeIt - " + toString());
		//	Re-Check
		if (!m_justPrepared)
		{
			String status = prepareIt();
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}
		//	Implicit Approval
		approveIt();

		// Verifica que Asientos Balanceados.
                /*
                 *  Zynnia 05/12/2012
                 *  JF
                 *  Corrección de la consulta para eliminar la ambiguedad del where
                 * 
                 */
		String sql = "SELECT 1 "
				   + "FROM GL_JournalLine jl "
				   + "INNER JOIN GL_Journal j ON (jl.GL_Journal_ID=j.GL_Journal_ID) "
				   + "INNER JOIN GL_JournalBatch jb ON (jb.GL_JournalBatch_ID=j.GL_JournalBatch_ID) "
				   + "WHERE jb.GL_JournalBatch_ID = ? "
				   + "HAVING SUM(jl.AmtSourceDr)=SUM(jl.AmtSourceCr)";
		PreparedStatement ps = DB.prepareStatement(sql,null);
		try {
			ps.setInt(1, getGL_JournalBatch_ID());
			ResultSet rs = ps.executeQuery();
			if (!rs.next())
			{
				m_processMsg = "La sumatoria de los importes en el campo débito debe ser igual a los importes en el campo crédito.";
				return DocAction.STATUS_Invalid;
			}
		} catch (SQLException e) {
			m_processMsg = "La sumatoria de los importes en el campo débito debe ser igual a los importes en el campo crédito.";
			return DocAction.STATUS_Invalid;
		}
		
		int period_id = MPeriod.get(Env.getCtx(),getDateAcct()).getC_Period_ID();
		setC_Period_ID(period_id);
		
		//	Add up Amounts & complete them
		MJournal[] journals = getJournals(true);
		BigDecimal TotalDr = Env.ZERO;
		BigDecimal TotalCr = Env.ZERO;		
		for (int i = 0; i < journals.length; i++)
		{
			MJournal journal = journals[i];
			if (!journal.isActive())
			{
				journal.setProcessed(true);
				journal.setDocStatus(DOCSTATUS_Voided);
				journal.setDocAction(DOCACTION_None);
				journal.save();
				continue;
			}
			//	Complete if not closed
			if (DOCSTATUS_Closed.equals(journal.getDocStatus())
				|| DOCSTATUS_Voided.equals(journal.getDocStatus())
				|| DOCSTATUS_Reversed.equals(journal.getDocStatus())
				|| DOCSTATUS_Completed.equals(journal.getDocStatus()))
				;
			else
			{
				String status = journal.completeIt();
				if (!DocAction.STATUS_Completed.equals(status))
				{
					journal.setDocStatus(status);
					journal.save();
					m_processMsg = journal.getProcessMsg();
					return status;
				}
				journal.setDocStatus(DOCSTATUS_Completed);
				journal.setC_Period_ID(period_id);
				journal.save();
			}
			//
			
			TotalDr = TotalDr.add(journal.getTotalDr());
			TotalCr = TotalCr.add(journal.getTotalCr());
		}
		setTotalDr(TotalDr);
		setTotalCr(TotalCr);
		//	User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}
		//
		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}	//	completeIt
	
	/**
	 * 	Void Document.
	 * 	@return false 
	 */
	public boolean voidIt()
	{
		log.info("voidIt - " + toString());
		return false;
	}	//	voidIt
	
	/**
	 * 	Close Document.
	 * 	@return true if success 
	 */
	public boolean closeIt()
	{
		log.info("closeIt - " + toString());
		MJournal[] journals = getJournals(true);
		for (int i = 0; i < journals.length; i++)
		{
			MJournal journal = journals[i];
			if (!journal.isActive() && !journal.isProcessed())
			{
				journal.setProcessed(true);
				journal.setDocStatus(DOCSTATUS_Voided);
				journal.setDocAction(DOCACTION_None);
				journal.save();
				continue;
			}
			if (DOCSTATUS_Drafted.equals(journal.getDocStatus())
				|| DOCSTATUS_InProgress.equals(journal.getDocStatus())
				|| DOCSTATUS_Invalid.equals(journal.getDocStatus()))
			{
				m_processMsg = "Journal not Completed: " + journal.getSummary();
				return false;
			}
			
			//	Close if not closed
			if (DOCSTATUS_Closed.equals(journal.getDocStatus())
				|| DOCSTATUS_Voided.equals(journal.getDocStatus())
				|| DOCSTATUS_Reversed.equals(journal.getDocStatus()))
				;
			else
			{
				if (!journal.closeIt())
				{
					m_processMsg = "Cannot close: " + journal.getSummary();
					return false;
				}
				journal.save();
			}
		}
		return true;
	}	//	closeIt
	
	/**
	 * 	Reverse Correction.
	 * 	As if nothing happened - same date
	 * 	@return true if success 
	 */
	public boolean reverseCorrectIt()
	{
		log.info("reverseCorrectIt - " + toString());
		MJournal[] journals = getJournals(true);
		//	check prerequisites
		for (int i = 0; i < journals.length; i++)
		{
			MJournal journal = journals[i];
			if (!journal.isActive())
				continue;
			//	All need to be closed/Completed
			if (DOCSTATUS_Completed.equals(journal.getDocStatus()))
				;
			else
			{
				m_processMsg = "All Journals need to be Compleded: " + journal.getSummary();
				return false;
			}
		}
		
		//	Reverse it
		MJournalBatch reverse = new MJournalBatch (this);
		reverse.setDateDoc(getDateDoc());
		reverse.setC_Period_ID(getC_Period_ID());
		reverse.setDateAcct(getDateAcct());
		//	Reverse indicator
		String description = reverse.getDescription();
		if (description == null)
			description = "** " + getDocumentNo() + " **";
		else
			description += " ** " + getDocumentNo() + " **";
		reverse.setDescription(description);
		reverse.save();
		//
		
		//	Reverse Journals
		for (int i = 0; i < journals.length; i++)
		{
			MJournal journal = journals[i];
			if (!journal.isActive())
				continue;
			if (journal.reverseCorrectIt(reverse.getGL_JournalBatch_ID()) == null)
			{
				m_processMsg = "Could not reverse " + journal;
				return false;
			}
			journal.save();
		}
                
                                
		setDocAction(DOCACTION_None);
                                reverse.setProcessed(true);
                                reverse.setDocAction(DOCACTION_None);
                                reverse.setDocStatus(DocAction.STATUS_Reversed);
                                reverse.save();
                        return true;
	}	//	reverseCorrectionIt
	
	/**
	 * 	Reverse Accrual.
	 * 	Flip Dr/Cr - Use Today's date
	 * 	@return true if success 
	 */
	public boolean reverseAccrualIt()
	{
		log.info("reverseAccrualIt - " + toString());
		MJournal[] journals = getJournals(true);
		//	check prerequisites
		for (int i = 0; i < journals.length; i++)
		{
			MJournal journal = journals[i];
			if (!journal.isActive())
				continue;
			//	All need to be closed/Completed
			if (DOCSTATUS_Completed.equals(journal.getDocStatus()))
				;
			else
			{
				m_processMsg = "All Journals need to be Compleded: " + journal.getSummary();
				return false;
			}
		}
		//	Reverse it
		MJournalBatch reverse = new MJournalBatch (this);
		reverse.setC_Period_ID(0);
		reverse.setDateDoc(new Timestamp(System.currentTimeMillis()));
		reverse.setDateAcct(reverse.getDateDoc());
		//	Reverse indicator
		String description = reverse.getDescription();
		if (description == null)
			description = "** " + getDocumentNo() + " **";
		else
			description += " ** " + getDocumentNo() + " **";
		reverse.setDescription(description);
		reverse.save();
		
		//	Reverse Journals
		for (int i = 0; i < journals.length; i++)
		{
			MJournal journal = journals[i];
			if (!journal.isActive())
				continue;
			if (journal.reverseAccrualIt(reverse.getGL_JournalBatch_ID()) == null)
			{
				m_processMsg = "Could not reverse " + journal;
				return false;
			}
			journal.save();
		}
                                
		setDocAction(DOCACTION_None);
                                reverse.setProcessed(true);
                                reverse.setDocAction(DOCACTION_None);
                                reverse.setDocStatus(DocAction.STATUS_Reversed);
                                reverse.save();
		return true;
	}	//	reverseAccrualIt
	
	/** 
	 * 	Re-activate - same as reverse correct
	 * 	@return true if success
         *      Zynnia 10/05/2012
         *      Maria Jesus Martin
         *      Anexar funcionalidad de reactivar Diario CG
	 */
	public boolean reActivateIt()
	{
		log.info("reActivateIt - " + toString());
                /*
	//	setProcessed(false);
		if (reverseCorrectIt()){
                    return true;
                }
		return false;*/
                String sql = "SELECT GL_Journal_ID,posted "
                        + "FROM GL_Journal "
                        + "WHERE GL_JournalBatch_ID = ?";
                try {
                    PreparedStatement pstm = DB.prepareStatement(sql, get_TrxName());
                    pstm.setInt(1, getGL_JournalBatch_ID());
                    ResultSet rs = pstm.executeQuery();
                    while (rs.next()){
                        if (rs.getString(2).equals("Y")){
                            String sql1 = "SELECT Fact_Acct_ID"
                                    + "FROM Fact_Acct"
                                    + "WHERE record_ID = " + rs.getInt(1) + "ad_table_id = 224";
                            PreparedStatement pstm1 = DB.prepareStatement(sql1, get_TrxName());
                            ResultSet rs1;
                            rs1 = pstm1.executeQuery();
                            while (rs1.next()){
                                DB.executeUpdate("DELETE FROM Fact_Acct WHERE Fact_Acct_ID = " + rs1.getInt(1), get_TrxName());
                            }
                            rs1.close();
                            pstm1.close();
                        }
                        String updateJournalLine="UPDATE GL_JournalLine SET processed = 'N' where GL_Journal_ID = "+rs.getInt(1);
                        DB.executeUpdate(updateJournalLine, get_TrxName());
                        String updateJournal= "UPDATE GL_Journal SET posted = 'N',processing = 'N',processed = 'N',"
                                + "DocStatus = 'DR',DocAction = 'CO' "
                                + "WHERE GL_Journal_ID = "+ rs.getInt(1);
                        DB.executeUpdate(updateJournal, get_TrxName());
                        rs.close();
			pstm.close();   
                    }
                 
                } catch (SQLException ex) {      
                }
                this.setDocStatus("DR");
                this.setDocAction("CO");
                this.setProcessed(false);
                this.setProcessing(false);
                this.setPostingType("A");
                this.setIsApproved(false);
                return true;
	}	//	reActivateIt
	

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
		.append(Msg.translate(getCtx(),"TotalDr")).append("=").append(getTotalDr())
		.append(" ")
		.append(Msg.translate(getCtx(),"TotalCR")).append("=").append(getTotalCr())
		.append(" (#").append(getJournals(false).length).append(")");
		//	 - Description
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}	//	getSummary

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MJournalBatch[");
		sb.append(get_ID()).append(",").append(getDescription())
			.append(",DR=").append(getTotalDr())
			.append(",CR=").append(getTotalCr())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		return dt.getName() + " " + getDocumentNo();
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
	 *	@return AD_User_ID (Created By)
	 */
	public int getDoc_User_ID()
	{
		return getCreatedBy();
	}	//	getDoc_User_ID

	/**
	 * 	Get Document Approval Amount
	 *	@return DR amount
	 */
	public BigDecimal getApprovalAmt()
	{
		return getTotalDr();
	}	//	getApprovalAmt
	
/*	protected boolean beforeSave (boolean newRecord)
	{
		if (newRecord)
		{
			MSequence seq = MSequence.get(Env.getCtx(), "GL Journal Batch");
			setDocumentNo(Integer.toString(seq.getCurrentNext()));
			seq.save();
		}
		
		return true;
	}	//	afterSave
*/	
}	//	MJournalBatch
