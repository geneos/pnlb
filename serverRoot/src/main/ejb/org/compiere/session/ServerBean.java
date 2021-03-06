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
package org.compiere.session;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.ejb.*;
import javax.sql.*;

import org.compiere.*;
import org.compiere.acct.*;
import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.*;
import org.compiere.wf.*;

/**
 * 	Compiere Server Bean.
 *
 *  @ejb.bean name="compiere/Server"
 *		display-name="Compiere Server Session Bean"
 *		type="Stateless"
 *		view-type="both"
 *      transaction-type="Bean"
 *      jndi-name="compiere/Server"
 *      local-jndi-name="compiere/ServerLocal"
 *
 *  @ejb.ejb-ref ejb-name="compiere/Server"
 *  	view-type="both"
 *		ref-name="compiere/Server"
 *  @ejb.ejb-ref ejb-name="compiere/Server"
 *  	view-type="local"
 *		ref-name="compiere/ServerLocal"
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ServerBean.java,v 1.43 2005/12/13 00:17:14 jjanke Exp $
 */
public class ServerBean implements SessionBean
{
	/**	Context				*/
	private SessionContext 	m_Context;
	/**	Logger				*/
	private static CLogger log = CLogger.getCLogger(ServerBean.class);
	//
	private static int		s_no = 0;
	private int				m_no = 0;
	//
	private int				m_windowCount = 0;
	private int				m_postCount = 0;
	private int				m_processCount = 0;
	private int				m_workflowCount = 0;
	private int				m_paymentCount = 0;
	private int				m_nextSeqCount = 0;
	private int				m_stmt_rowSetCount = 0;
	private int				m_stmt_updateCount = 0;
	private int				m_cacheResetCount = 0;
	private int				m_updateLOBCount = 0;

	/**
	 *  Get and create Window Model Value Object
	 *  @ejb.interface-method view-type="both"
	 *
	 *  @param ctx   Environment Properties
	 *  @param WindowNo  number of this window
	 *  @param AD_Window_ID  the internal number of the window, if not 0, AD_Menu_ID is ignored
	 *  @param AD_Menu_ID ine internal menu number, used when AD_Window_ID is 0
	 *  @return initialized Window Model
	 */
	public MWindowVO getWindowVO (Properties ctx, int WindowNo, int AD_Window_ID, int AD_Menu_ID)
	{
		log.info ("getWindowVO[" + m_no + "] Window=" + AD_Window_ID);
	//	log.fine(ctx);
		MWindowVO vo = MWindowVO.create(ctx, WindowNo, AD_Window_ID, AD_Menu_ID);
		m_windowCount++;
		return vo;
	}	//	getWindowVO


	/**
	 *  Post Immediate
	 *  @ejb.interface-method view-type="both"
	 *
	 *	@param	ctx Client Context
	 *  @param  AD_Client_ID    Client ID of Document
	 *  @param  AD_Table_ID     Table ID of Document
	 *  @param  Record_ID       Record ID of this document
	 *  @param  force           force posting
	 *  @return null, if success or error message
	 */
	public String postImmediate (Properties ctx, 
		int AD_Client_ID, int AD_Table_ID, int Record_ID, boolean force, String trxName)
	{
		log.info ("[" + m_no + "] Table=" + AD_Table_ID + ", Record=" + Record_ID);
		m_postCount++;
		MAcctSchema[] ass = MAcctSchema.getClientAcctSchema(ctx, AD_Client_ID);
		return Doc.postImmediate(ass, AD_Table_ID, Record_ID, force, trxName);
	}	//	postImmediate

	/*************************************************************************
	 *  Get Prepared Statement ResultSet
	 *  @ejb.interface-method view-type="both"
	 *
	 *  @param info Result info
	 *  @return RowSet
	 *  @throws NotSerializableException
	 */
	public RowSet pstmt_getRowSet (CStatementVO info) 
		throws NotSerializableException
	{
		log.finer("[" + m_no + "]");
		m_stmt_rowSetCount++;
		CPreparedStatement pstmt = new CPreparedStatement(info);
		return pstmt.remote_getRowSet();
	}	//	pstmt_getRowSet

	/**
	 *  Get Statement ResultSet
	 *  @ejb.interface-method view-type="both"
	 *
	 *  @param info Result info
	 *  @return RowSet
	 */
	public RowSet stmt_getRowSet (CStatementVO info)
	{
		log.finer("[" + m_no + "]");
		m_stmt_rowSetCount++;
		CStatement stmt = new CStatement(info);
		return stmt.remote_getRowSet();
	}	//	stmt_getRowSet

	/**
	 *  Execute Update
	 *  @ejb.interface-method view-type="both"
	 *
	 *  @param info Result info
	 *  @return row count
	 */
	public int stmt_executeUpdate (CStatementVO info)
	{
		log.finer("[" + m_no + "]");
		m_stmt_updateCount++;
		if (info.getParameterCount() == 0)
		{
			CStatement stmt = new CStatement(info);
			return stmt.remote_executeUpdate();
		}
		CPreparedStatement pstmt = new CPreparedStatement(info);
		return pstmt.remote_executeUpdate();
	}	//	stmt_executeUpdate

	/*************************************************************************
	 *	Get next number for Key column = 0 is Error.
	 *  @ejb.interface-method view-type="both"
	 *
	 *  @param AD_Client_ID client
	 *  @param TableName table name
	 * 	@param trxName optional Transaction Name
	 *  @return next no
	 */
	public int getNextID (int AD_Client_ID, String TableName, String trxName)
	{
		int retValue = MSequence.getNextID (AD_Client_ID, TableName, trxName);
		log.finer("[" + m_no + "] " + TableName + " = " + retValue);
		m_nextSeqCount++;
		return retValue;
	}	//	getNextID

	/**
	 * 	Get Document No from table
	 *  @ejb.interface-method view-type="both"
	 * 
	 *	@param AD_Client_ID client
	 *	@param TableName table name
	 * 	@param trxName optional Transaction Name
	 *	@return document no or null
	 */
	public String getDocumentNo (int AD_Client_ID, String TableName, String trxName)
	{
		m_nextSeqCount++;
		String dn = MSequence.getDocumentNo (AD_Client_ID, TableName, trxName);
		if (dn == null)		//	try again
			dn = MSequence.getDocumentNo (AD_Client_ID, TableName, trxName);
		return dn;
	}	//	GetDocumentNo
	
	/**
	 * 	Get Document No based on Document Type
	 *  @ejb.interface-method view-type="both"
	 * 
	 *	@param C_DocType_ID document type
	 * 	@param trxName optional Transaction Name
	 *	@return document no or null
	 */
	public String getDocumentNo (int C_DocType_ID, String trxName)
	{
		m_nextSeqCount++;
		String dn = MSequence.getDocumentNo (C_DocType_ID, trxName);
		if (dn == null)		//	try again
			dn = MSequence.getDocumentNo (C_DocType_ID, trxName);
		return dn;
	}	//	getDocumentNo


	/*************************************************************************
	 *  Process Remote
	 *  @ejb.interface-method view-type="both"
	 *
	 *  @param ctx Context
	 *  @param pi Process Info
	 *  @return resulting Process Info
	 */
	public ProcessInfo process (Properties ctx, ProcessInfo pi)
	{
		String className = pi.getClassName();
		log.info(className + " - " + pi);
		m_processCount++;
		//	Get Class
		Class clazz = null;
		try
		{
			clazz = Class.forName (className);
		}
		catch (ClassNotFoundException ex)
		{
			log.log(Level.WARNING, className, ex);
			pi.setSummary ("ClassNotFound", true);
			return pi;
		}
		//	Get Process
		SvrProcess process = null;
		try
		{
			process = (SvrProcess)clazz.newInstance ();
		}
		catch (Exception ex)
		{
			log.log(Level.WARNING, "Instance for " + className, ex);
			pi.setSummary ("InstanceError", true);
			return pi;
		}
		//	Start Process
		Trx trx = Trx.get(Trx.createTrxName("ServerPrc"), true);
		try
		{
			boolean ok = process.startProcess (ctx, pi, trx);
			pi = process.getProcessInfo();
			trx.commit();
			trx.close();
		}
		catch (Exception ex1)
		{
			trx.rollback();
			trx.close();
			pi.setSummary ("ProcessError", true);
			return pi;
		}
		return pi;
	}	//	process


	/*************************************************************************
	 *  Run Workflow (and wait) on Server
	 *  @ejb.interface-method view-type="both"
	 *
	 *  @param ctx Context
	 *  @param pi Process Info
	 *  @return process info
	 */
	public ProcessInfo workflow (Properties ctx, ProcessInfo pi, int AD_Workflow_ID)
	{
		log.info ("[" + m_no + "] " + AD_Workflow_ID);
		m_workflowCount++;
		MWorkflow wf = MWorkflow.get (ctx, AD_Workflow_ID);
                //begin vpj-cd 28/03/2006 
		//MWFProcess wfProcess = wf.startWait(pi);	//	may return null
                MWFProcess wfProcess = null; 
                if (pi.isBatch()) 
                       wfProcess = wf.start(pi);               //      may return null 
                else 
                       wfProcess = wf.startWait(pi);   //      may return null 
                //end vpj-cd 28/03/2006

		log.fine(pi.toString());
		return pi;
	}	//	workflow

	/**
	 *  Online Payment from Server
	 *  @ejb.interface-method view-type="both"
	 *	Called from MPayment processOnline
	 *  @param ctx Context
	 *  @param C_Payment_ID payment
	 *  @param C_PaymentProcessor_ID processor
	 *  @return true if approved
	 */
	public boolean paymentOnline (Properties ctx, int C_Payment_ID, int C_PaymentProcessor_ID, String trxName)
	{
		MPayment payment = new MPayment (ctx, C_Payment_ID, trxName);
		MPaymentProcessor mpp = new MPaymentProcessor (ctx, C_PaymentProcessor_ID, null);
		log.info ("[" + m_no + "] " + payment + " - " + mpp);
		m_paymentCount++;
		boolean approved = false;
		try
		{
			PaymentProcessor pp = PaymentProcessor.create(mpp, payment);
			if (pp == null)
				payment.setErrorMessage("No Payment Processor");
			else
			{
				approved = pp.processCC ();
				if (approved)
					payment.setErrorMessage(null);
				else
					payment.setErrorMessage("From " +  payment.getCreditCardName() 
						+ ": " + payment.getR_RespMsg());
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			payment.setErrorMessage("Payment Processor Error");
		}
		payment.save();	
		return approved;
	}	//	paymentOnline

	/**
	 *  Create EMail from Server (Request User)
	 *  @ejb.interface-method view-type="both"
	 *  @param ctx Context
	 *  @param AD_Client_ID client 
	 *	@param to recipient email address
	 *	@param subject subject
	 *	@param message message
	 *  @return EMail
	 */
	public EMail createEMail (Properties ctx, int AD_Client_ID, 
		String to, String subject, String message)
	{
		MClient client = MClient.get(ctx, AD_Client_ID);
		return client.createEMail(to, subject, message);
	}	//	createEMail

	/**
	 *  Create EMail from Server (Request User)
	 *  @ejb.interface-method view-type="both"
	 *  @param ctx Context
	 *  @param AD_Client_ID client 
	 *	@param to recipient email address
	 *	@param subject subject
	 *	@param message message
	 *  @return EMail
	 */
	public EMail createEMail (Properties ctx, int AD_Client_ID,
		int AD_User_ID,
		String to, String subject, String message)
	{
		MClient client = MClient.get(ctx, AD_Client_ID);
		MUser from = new MUser (ctx, AD_User_ID, null);
		return client.createEMail(from, to, subject, message);
	}	//	createEMail

	
	/**
	 *  Create EMail from Server (Request User)
	 *  @ejb.interface-method view-type="both"
	 *  @param AD_Task_ID task 
	 *  @return execution trace
	 */
	public String executeTask (int AD_Task_ID)
	{
		MTask task = new MTask (Env.getCtx(), AD_Task_ID, null);	//	Server Context
		return task.execute();
	}	//	executeTask
	
	
	/**
	 *  Cash Reset
	 *  @ejb.interface-method view-type="both"
	 *
	 *  @param tableName table name
	 *  @param Record_ID record or 0 for all
	 * 	@return number of records reset
	 */
	public int cacheReset (String tableName, int Record_ID)
	{
		log.config(tableName + " - " + Record_ID);
		m_cacheResetCount++;
		return CacheMgt.get().reset(tableName, Record_ID);
	}	//	cacheReset
	
	/**
	 *  LOB update
	 *  @ejb.interface-method view-type="both"
	 *
	 *  @param sql table name
	 *  @param displayType display type (i.e. BLOB/CLOB)
	 * 	@param value the data
	 * 	@return true if updated
	 */
	public boolean updateLOB (String sql, int displayType, Object value)
	{
		if (sql == null || value == null)
		{
			log.fine("No sql or data");
			return false;
		}
		log.fine(sql);
		m_updateLOBCount++;
		boolean success = true;
		Connection con = DB.createConnection(false, Connection.TRANSACTION_READ_COMMITTED);
		PreparedStatement pstmt = null;
		try
		{
			pstmt = con.prepareStatement(sql);
			if (displayType == DisplayType.TextLong)
				pstmt.setString(1, (String)value);
			else
				pstmt.setBytes(1, (byte[])value);
			int no = pstmt.executeUpdate();
			//
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.FINE, sql, e);
			success = false;
		}
		//	Close Statement
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
		
		//	Success - commit local trx
		if (success)
		{
			try
			{
				con.commit();
				con.close();
				con = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "commit" , e);
				success = false;
			}
		}
		//	Error - roll back
		if (!success)
		{
			log.severe ("rollback");
			try
			{
				con.rollback();
				con.close();
				con = null;
			}
			catch (Exception ee)
			{
				log.log(Level.SEVERE, "rollback" , ee);
			}
		}
		
		//	Clean Connection
		try
		{
			if (con != null)
				con.close();
			con = null;
		}
		catch (Exception e)
		{
			con = null;
		}
		return success;
	}	//	updateLOB

	
	/**************************************************************************
	 * 	Describes the instance and its content for debugging purpose
	 *  @ejb.interface-method view-type="both"
	 * 	@return Debugging information about the instance and its content
	 */
	public String getStatus()
	{
		StringBuffer sb = new StringBuffer("ServerBean[");
		sb.append(m_no)
			.append("-Window=").append(m_windowCount)
			.append(",Post=").append(m_postCount)
			.append(",Process=").append(m_processCount)
			.append(",NextSeq=").append(m_nextSeqCount)
			.append(",Workflow=").append(m_workflowCount)
			.append(",Payment=").append(m_paymentCount)
			.append(",RowSet=").append(m_stmt_rowSetCount)
			.append(",Update=").append(m_stmt_updateCount)
			.append(",CacheReset=").append(m_cacheResetCount)
			.append(",UpdateLob=").append(m_updateLOBCount)
			.append("]");
		return sb.toString();
	}	//	getStatus


	/**
	 * 	String Representation
	 * 	@return info
	 */
	public String toString()
	{
		return getStatus();
	}	//	toString

	
	/**************************************************************************
	 * 	Create the Session Bean
	 * 	@throws EJBException, CreateException
	 *  @ejb.create-method view-type="both"
	 */
	public void ejbCreate() throws EJBException, CreateException
	{
		m_no = ++s_no;
		try
		{
			Compiere.startup(false);
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, "ejbCreate", ex);
		//	throw new CreateException ();
		}
		log.info ("#" + getStatus());
	}	//	ejbCreate


	// -------------------------------------------------------------------------
	// Framework Callbacks
	// -------------------------------------------------------------------------

	/**
	 * Method setSessionContext
	 * @param aContext SessionContext
	 * @throws EJBException
	 * @see javax.ejb.SessionBean#setSessionContext(SessionContext)
	 */
	public void setSessionContext (SessionContext aContext) throws EJBException
	{
		m_Context = aContext;
	}	//	setSessionContext

	/**
	 * Method ejbActivate
	 * @throws EJBException
	 * @see javax.ejb.SessionBean#ejbActivate()
	 */
	public void ejbActivate() throws EJBException
	{
		if (log == null)
			log = CLogger.getCLogger(getClass());
		log.info ("ejbActivate " + getStatus());
	}	//	ejbActivate

	/**
	 * Method ejbPassivate
	 * @throws EJBException
	 * @see javax.ejb.SessionBean#ejbPassivate()
	 */
	public void ejbPassivate() throws EJBException
	{
		log.info ("ejbPassivate " + getStatus());
	}	//	ejbPassivate

	/**
	 * Method ejbRemove
	 * @throws EJBException
	 * @see javax.ejb.SessionBean#ejbRemove()
	 */
	public void ejbRemove() throws EJBException
	{
		log.info ("ejbRemove " + getStatus());
	}	//	ejbRemove


	/**************************************************************************
	 * 	Dump SerialVersionUID of class 
	 *	@param clazz class
	 */
	protected static void dumpSVUID (Class clazz)
	{
		String s = clazz.getName() 
			+ " ==\nstatic final long serialVersionUID = "
			+ java.io.ObjectStreamClass.lookup(clazz).getSerialVersionUID()
			+ "L;\n";
		System.out.println (s);
	}	//	dumpSVUID

	/**
	 * 	Print UID of used classes.
	 * 	R2.5.1h		

org.compiere.process.ProcessInfo ==
static final long serialVersionUID = -1993220053515488725L;

org.compiere.util.CStatementVO ==
static final long serialVersionUID = -3393389471515956399L;

org.compiere.model.MQuery ==
static final long serialVersionUID = 1511402030597166113L;

org.compiere.model.POInfo ==
static final long serialVersionUID = -5976719579744948419L;

org.compiere.model.POInfoColumn ==
static final long serialVersionUID = -3983585608504631958L;

org.compiere.model.MWindowVO ==
static final long serialVersionUID = 3802628212531678981L;

org.compiere.model.MTabVO ==
static final long serialVersionUID = 9160212869277319305L;

org.compiere.model.MFieldVO ==
static final long serialVersionUID = 4385061125114436797L;

org.compiere.model.MLookupInfo ==
static final long serialVersionUID = -7958664359250070233L;

	 *
	 *	 *	@param args ignored
	 */
	public static void main (String[] args)
	{
		dumpSVUID(ProcessInfo.class);
		dumpSVUID(CStatementVO.class);
		dumpSVUID(MQuery.class);
		dumpSVUID(POInfo.class);
		dumpSVUID(POInfoColumn.class);
		dumpSVUID(MWindowVO.class);
		dumpSVUID(MTabVO.class);
		dumpSVUID(MFieldVO.class);
		dumpSVUID(MLookupInfo.class);
	}	//	main

}	//	ServerBean
