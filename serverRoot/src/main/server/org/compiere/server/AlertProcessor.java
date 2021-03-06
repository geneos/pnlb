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
package org.compiere.server;

import java.sql.*;
import java.util.logging.*;
import org.compiere.*;
import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Alert Processor
 *	
 *  @author Jorg Janke
 *  @version $Id: AlertProcessor.java,v 1.11 2005/11/12 23:00:29 jjanke Exp $
 */
public class AlertProcessor extends CompiereServer
{
	/**
	 * 	Alert Processor
	 *	@param model model
	 */
	public AlertProcessor (MAlertProcessor model)
	{
		super (model, 180);		//	3 monute delay 
		m_model = model;
		m_client = MClient.get(model.getCtx(), model.getAD_Client_ID());
	}	//	AlertProcessor

	/**	The Concrete Model			*/
	private MAlertProcessor		m_model = null;
	/**	Last Summary				*/
	private StringBuffer 		m_summary = new StringBuffer();
	/**	Last Error Msg				*/
	private StringBuffer 		m_errors = new StringBuffer();
	/** Client onfo					*/
	private MClient 			m_client = null;

	/**
	 * 	Work
	 */
	protected void doWork ()
	{
		m_summary = new StringBuffer();
		m_errors = new StringBuffer();
		//
		int count = 0;
		int countError = 0;
		MAlert[] alerts = m_model.getAlerts(false);
		for (int i = 0; i < alerts.length; i++)
		{
			if (!processAlert(alerts[i]))
				countError++;
			count++;
		}
		//
		String summary = "Total=" + count;
		if (countError > 0)
			summary += ", Not processed=" + countError;
		summary += " - ";
		m_summary.insert(0, summary);
		//
		int no = m_model.deleteLog();
		m_summary.append("Logs deleted=").append(no);
		//
		MAlertProcessorLog pLog = new MAlertProcessorLog(m_model, m_summary.toString());
		pLog.setReference("#" + String.valueOf(p_runCount) 
			+ " - " + TimeUtil.formatElapsed(new Timestamp(p_startWork)));
		pLog.setTextMsg(m_errors.toString());
		pLog.save();
	}	//	doWork

	/**
	 * 	Process Alert
	 *	@param alert alert
	 *	@return true if processed
	 */
	private boolean processAlert (MAlert alert)
	{
		if (!alert.isValid())
			return false;
		log.info("" + alert);

		StringBuffer message = new StringBuffer(alert.getAlertMessage())
			.append(Env.NL);
		//
		boolean valid = true;
		boolean processed = false;
		MAlertRule[] rules = alert.getRules(false);
		for (int i = 0; i < rules.length; i++)
		{
			if (i > 0)
				message.append(Env.NL).append("================================").append(Env.NL);
			String trxName = null;		//	assume r/o
			
			MAlertRule rule = rules[i];
			if (!rule.isValid())
				continue;
			log.fine("" + rule);
			
			//	Pre
			String sql = rule.getPreProcessing();
			if (sql != null && sql.length() > 0)
			{
				int no = DB.executeUpdate(sql, false, trxName);
				if (no == -1)
				{
					ValueNamePair error = CLogger.retrieveError();
					rule.setErrorMsg("Pre=" + error.getName());
					m_errors.append("Pre=" + error.getName());
					rule.setIsValid(false);
					rule.save();
					valid = false;
					break;
				}
			}	//	Pre
			
			//	The processing
			sql = rule.getSql();
			if (alert.isEnforceRoleSecurity()
				|| alert.isEnforceClientSecurity())
			{
				int AD_Role_ID = alert.getFirstAD_Role_ID();
				if (AD_Role_ID == -1)
					AD_Role_ID = alert.getFirstUserAD_Role_ID();
				if (AD_Role_ID != -1)
				{
					MRole role = MRole.get(getCtx(), AD_Role_ID);
					sql = role.addAccessSQL(sql, null, true, false);
				}
			}
			
			try
			{
				String text = listSqlSelect(sql, trxName);
				if (text != null && text.length() > 0)
				{
					message.append(text);
					processed = true;
				}
			}
			catch (Exception e)
			{
				rule.setErrorMsg("Select=" + e.getLocalizedMessage());
				m_errors.append("Select=" + e.getLocalizedMessage());
				rule.setIsValid(false);
				rule.save();
				valid = false;
				break;
			}

			//	Post
			sql = rule.getPostProcessing();
			if (sql != null && sql.length() > 0)
			{
				int no = DB.executeUpdate(sql, false, trxName);
				if (no == -1)
				{
					ValueNamePair error = CLogger.retrieveError();
					rule.setErrorMsg("Post=" + error.getName());
					m_errors.append("Post=" + error.getName());
					rule.setIsValid(false);
					rule.save();
					valid = false;
					break;
				}
			}	//	Post
			
			/**	Trx				*/
			if (trxName != null)
			{
				Trx trx = Trx.get(trxName, false);
				if (trx != null)
				{
					trx.commit();
					trx.close();
				}
			}
		}	//	 for all rules
		
		//	Update header if error
		if (!valid)
		{
			alert.setIsValid(false);
			alert.save();
			return false;
		}
		
		//	Nothing to report
		if (!processed)
		{
			m_summary.append(alert.getName()).append("=No Result - ");
			return true;
		}
		
		//	Send Message
		int countMail = 0;
		MAlertRecipient[] recipients = alert.getRecipients(false);
		for (int i = 0; i < recipients.length; i++)
		{
			MAlertRecipient recipient = recipients[i];
			if (recipient.getAD_User_ID() >= 0)		//	System == 0
				if (m_client.sendEMail(recipient.getAD_User_ID(), 
					alert.getAlertSubject(), message.toString(), null))
					countMail++;
			if (recipient.getAD_Role_ID() >= 0)		//	SystemAdministrator == 0
			{
				MUserRoles[] urs = MUserRoles.getOfRole(getCtx(), recipient.getAD_Role_ID());
				for (int j = 0; j < urs.length; j++)
				{
					MUserRoles ur = urs[j];
					if (m_client.sendEMail (ur.getAD_User_ID(), 
						alert.getAlertSubject(), message.toString(), null))
						countMail++;
				}
			}
		}
		
		m_summary.append(alert.getName()).append(" (EMails=").append(countMail).append(") - ");
		return valid;
	}	//	processAlert
	
	/**
	 * 	List Sql Select
	 *	@param sql sql select
	 *	@return list of rows & values
	 *	@throws Exception
	 */
	private String listSqlSelect (String sql, String trxName) throws Exception
	{
		StringBuffer result = new StringBuffer();
		PreparedStatement pstmt = null;
		Exception error = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			ResultSetMetaData meta = rs.getMetaData();
			while (rs.next ())
			{
				result.append("------------------").append(Env.NL);
				for (int col = 1; col <= meta.getColumnCount(); col++)
				{
					result.append(meta.getColumnLabel(col)).append(" = ");
					result.append(rs.getString(col));
					result.append(Env.NL);
				}	//	for all columns
			}
			if (result.length() == 0)
				log.fine("No rows selected");
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
			error = e;
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		
		//	Error occured
		if (error != null)
			throw new Exception ("(" + sql + ") " + Env.NL 
				+ error.getLocalizedMessage());
		
		return result.toString();
	}	//	listSqlSelect
	
	
	
	/**
	 * 	Get Server Info
	 *	@return info
	 */
	public String getServerInfo()
	{
		return "#" + p_runCount + " - Last=" + m_summary.toString();
	}	//	getServerInfo

	
	/***************************************************************************
	 * 	Test
	 *	@param args ignored
	 */
	public static void main (String[] args)
	{
		Compiere.startup(true);
		MAlertProcessor model = new MAlertProcessor (Env.getCtx(), 100, null);
		AlertProcessor ap = new AlertProcessor(model);
		ap.start();
		
		
	}	//	main
	
}	//	AlertProcessor
