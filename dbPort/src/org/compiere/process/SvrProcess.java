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

import java.lang.reflect.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Server Process Template
 *
 *  @author     Jorg Janke
 *  @version    $Id: SvrProcess.java,v 1.39 2005/12/31 06:33:22 jjanke Exp $
 */
public abstract class SvrProcess implements ProcessCall
{
	/**
	 *  Server Process.
	 * 	Note that the class is initiated by startProcess.
	 */
	public SvrProcess()
	{
	//	Env.ZERO.divide(Env.ZERO);
	}   //  SvrProcess

	private Properties  		m_ctx;
	private ProcessInfo			m_pi;

	/**	Logger							*/
	protected CLogger			log = CLogger.getCLogger (getClass());

	/**	Is the Object locked			*/
	private boolean				m_locked = false;
	/**	Loacked Object					*/
	private PO					m_lockedObject = null;
	/** Process Main transaction 		*/
	private Trx 				m_trx;

	/**	Common Error Message			*/
	protected static String 	MSG_SaveErrorRowNotFound = "@SaveErrorRowNotFound@";
	protected static String		MSG_InvalidArguments = "@InvalidArguments@";


	/**
	 *  Start the process.
	 *  Calls the abstract methods <code>process</code>.
	 *  It should only return false, if the function could not be performed
	 *  as this causes the process to abort.
	 *
	 *  @param ctx      Context
	 *  @param pi		Process Info
	 *  @return true if the next process should be performed
	 * 	@see org.compiere.process.ProcessCall#startProcess(Properties, ProcessInfo, Trx)
	 */
	public final boolean startProcess (Properties ctx, ProcessInfo pi, Trx trx)
	{
		//  Preparation
		m_ctx = ctx == null ? Env.getCtx() : ctx;
		m_pi = pi;
		m_trx = trx;
		//***	Trx
		boolean localTrx = m_trx == null;
		if (localTrx)
			m_trx = Trx.get(Trx.createTrxName("SvrProcess"), true);
		//
		/** BISion - 07/11/2008 - Santiago Ibañez
                 * Se encerró la instrucción process() y el if... en un bloque 
                 * try-catch para deshacer las trx si ocurrió alguna excepción
                 * durante el proceso.
                 */
                try{
                    process();
                    if (localTrx)
                    {
			m_trx.commit();
			m_trx.close();
			m_trx = null;
                    }
                }
                catch (Exception ex){
                    //rollback de la trx solo si era local
                    if (localTrx){
                        m_trx.rollback();
                        m_trx.close();
                        return false;
                    }
                }
		//
		
		return !m_pi.isError();
	}   //  startProcess

	
	/**************************************************************************
	 *  Process
	 */
	private void process()
	{
		String msg = null;
		boolean error = false;
		try
		{
			lock();
			prepare();
			msg = doIt();
			System.gc();
		}
		catch (Exception e)
		{
			msg = e.getMessage();
			if (msg == null)
				msg = e.toString();
			if (e.getCause() != null)
				log.log(Level.SEVERE, msg, e.getCause());
			else if (CLogMgt.isLevelFine())
				log.log(Level.WARNING, msg, e);
			else
				log.warning(msg);
			error = true;
		}
		unlock();
		
		//	Parse Variables
		msg = Msg.parseTranslation(m_ctx, msg);
		m_pi.setSummary (msg, error);
		ProcessInfoUtil.saveLogToDB(m_pi);
                /** BISion - 11/11/2008 - Santiago Ibañez
                 * Modificacion realizada para que muestre para cualquier
                 * excepcion informacion en la ventana
                 */
                if (error){
                    throw new RuntimeException(msg);
                }
	}   //  process

	/**
	 *  Prepare - e.g., get Parameters.
	 *  <code>
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("A_Asset_Group_ID"))
				p_A_Asset_Group_ID = para[i].getParameterAsInt();
			else if (name.equals("GuaranteeDate"))
				p_GuaranteeDate = (Timestamp)para[i].getParameter();
			else if (name.equals("AttachAsset"))
				p_AttachAsset = "Y".equals(para[i].getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	 *  </code>
	 */
	abstract protected void prepare();

	/**
	 *  Perform process.
	 *  @return Message (variables are parsed)
	 *  @throws Exception if not successful e.g.
	 *  throw new CompiereUserError ("@FillMandatory@  @C_BankAccount_ID@");
	 */
	abstract protected String doIt() throws Exception;

	/**
	 * 	Commit
	 */
	protected void commit()
	{
		if (m_trx != null)
			m_trx.commit();
	}	//	commit
	
	/**
	 * 	Rollback
	 */
	protected void rollback()
	{
		if (m_trx != null)
			m_trx.rollback();
	}	//	rollback
	
	
	/**************************************************************************
	 * 	Lock Object.
	 * 	Needs to be explicitly called. Unlock is automatic.
	 *	@param po object
	 *	@return true if locked
	 */
	protected boolean lockObject (PO po)
	{
		//	Unlock existing
		if (m_locked || m_lockedObject != null)
			unlockObject();
		//	Nothing to lock			
		if (po == null)
			return false;
		m_lockedObject = po;
		m_locked = m_lockedObject.lock();
		return m_locked;
	}	//	lockObject

	/**
	 * 	Is an object Locked?
	 *	@return true if object locked
	 */
	protected boolean isLocked()
	{
		return m_locked;
	}	//	isLocked

	/**
	 * 	Unlock Object.
	 * 	Is automatically called at the end of process.
	 *	@return true if unlocked or if there was nothing to unlock
	 */
	protected boolean unlockObject()
	{
		boolean success = true;
		if (m_locked || m_lockedObject != null)
		{
			success = m_lockedObject.unlock();
		}
		m_locked = false;
		m_lockedObject = null;
		return success;
	}	//	unlock


	/**************************************************************************

	/**
	 *  Get Process Info
	 *  @return Process Info
	 */
	public ProcessInfo getProcessInfo()
	{
		return m_pi;
	}   //  getProcessInfo

	/**
	 *  Get Properties
	 *  @return Properties
	 */
	public Properties getCtx()
	{
		return m_ctx;
	}   //  getCtx

	/**
	 *  Get Name/Title
	 *  @return Name
	 */
	protected String getName()
	{
		return m_pi.getTitle();
	}   //  getName

	/**
	 *  Get Process Instance
	 *  @return Process Instance
	 */
	protected int getAD_PInstance_ID()
	{
		return m_pi.getAD_PInstance_ID();
	}   //  getAD_PInstance_ID

	/**
	 *  Get Table_ID
	 *  @return AD_Table_ID
	 */
	protected int getTable_ID()
	{
		return m_pi.getTable_ID();
	}   //  getRecord_ID

	/**
	 *  Get Record_ID
	 *  @return Record_ID
	 */
	protected int getRecord_ID()
	{
		return m_pi.getRecord_ID();
	}   //  getRecord_ID

	/**
	 *  Get AD_User_ID
	 *  @return AD_User_ID of Process owner
	 */
	protected int getAD_User_ID()
	{
		if (m_pi.getAD_User_ID() == null || m_pi.getAD_Client_ID() == null)
		{
			String sql = "SELECT AD_User_ID, AD_Client_ID FROM AD_PInstance WHERE AD_PInstance_ID=?";
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
				pstmt.setInt(1, m_pi.getAD_PInstance_ID());
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
				{
					m_pi.setAD_User_ID (rs.getInt (1));
					m_pi.setAD_Client_ID (rs.getInt(2));
				}
				rs.close();
				pstmt.close();
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sql, e);
			}
		}
		if (m_pi.getAD_User_ID() == null)
			return 0;
		return m_pi.getAD_User_ID().intValue();
	}   //  getAD_User_ID

	/**
	 *  Get AD_User_ID
	 *  @return AD_User_ID of Process owner
	 */
	protected int getAD_Client_ID()
	{
		if (m_pi.getAD_Client_ID() == null)
		{
			getAD_User_ID();	//	sets also Client
			if (m_pi.getAD_Client_ID() == null)
				return 0;
		}
		return m_pi.getAD_Client_ID().intValue();
	}	//	getAD_Client_ID

	
	/**************************************************************************
	 * 	Get Parameter
	 *	@return parameter
	 */
	protected ProcessInfoParameter[] getParameter()
	{
		ProcessInfoParameter[] retValue = m_pi.getParameter();
		if (retValue == null)
		{
			ProcessInfoUtil.setParameterFromDB(m_pi);
			retValue = m_pi.getParameter();
		}
		return retValue;
	}	//	getParameter


	/**************************************************************************
	 *  Add Log Entry
	 *  @param date date or null
	 *  @param id record id or 0
	 *  @param number number or null
	 *  @param msg message or null
	 */
	public void addLog (int id, Timestamp date, BigDecimal number, String msg)
	{
		if (m_pi != null)
			m_pi.addLog(id, date, number, msg);
		log.info(id + " - " + date + " - " + number + " - " + msg);
	}	//	addLog


	/**************************************************************************
	 * 	Execute function
	 * 	@param className class
	 * 	@param methodName method
	 * 	@param args arguments
	 * 	@return result
	 */
	public Object doIt (String className, String methodName, Object args[])
	{
		try
		{
			Class clazz = Class.forName(className);
			Object object = clazz.newInstance();
			Method[] methods = clazz.getMethods();
			for (int i = 0; i < methods.length; i++)
			{
				if (methods[i].getName().equals(methodName))
					return methods[i].invoke(object, args);
			}
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, "doIt", ex);
			throw new RuntimeException(ex);
		}
		return null;
	}	//	doIt

	
	/**************************************************************************
	 *  Lock Process Instance
	 */
	private void lock()
	{
		log.fine("AD_PInstance_ID=" + m_pi.getAD_PInstance_ID());
		DB.executeUpdate("UPDATE AD_PInstance SET IsProcessing='Y' WHERE AD_PInstance_ID=" 
			+ m_pi.getAD_PInstance_ID(), null);		//	outside trx
	}   //  lock

	/**
	 *  Unlock Process Instance.
	 *  Update Process Instance DB and write option return message
	 */
	private void unlock ()
	{
		MPInstance mpi = new MPInstance (getCtx(), m_pi.getAD_PInstance_ID(), null);
		if (mpi.get_ID() == 0)
		{
			log.log(Level.SEVERE, "Did not find PInstance " + m_pi.getAD_PInstance_ID());
			return;
		}
		mpi.setIsProcessing(false);
		mpi.setResult(m_pi.isError());
		mpi.setErrorMsg(m_pi.getSummary());
		mpi.save();
		log.fine(mpi.toString());
	}   //  unlock

	/**
	 * Return the main transaction of the current process.
	 * @return the transaction name
	 */
	protected String get_TrxName()
	{
		if (m_trx != null)
			return m_trx.getTrxName();
		return null;
	}	//	get_TrxName
	
}   //  SvrProcess
