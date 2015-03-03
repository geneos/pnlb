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
import org.compiere.acct.*;
import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Accounting Processor
 *	
 *  @author Jorg Janke
 *  @version $Id: AcctProcessor.java,v 1.12 2005/12/13 00:17:13 jjanke Exp $
 */
public class AcctProcessor extends CompiereServer
{
	/**
	 * 	Accounting Processor
	 *	@param model model 
	 */
	public AcctProcessor (MAcctProcessor model)
	{
		super (model, 30);	//	30 seconds delay
		m_model = model;
		m_client = MClient.get(model.getCtx(), model.getAD_Client_ID());
	}	//	AcctProcessor

	/**	The Concrete Model			*/
	private MAcctProcessor		m_model = null;
	/**	Last Summary				*/
	private StringBuffer 		m_summary = new StringBuffer();
	/** Client onfo					*/
	private MClient 			m_client = null;
	/**	Accounting Schemata			*/
	private MAcctSchema[] 		m_ass = null;
	
	/**
	 * 	Work
	 */
	protected void doWork ()
	{
		System.out.println("Ejecutando el metodo doWork - AcctProcessor");
		m_summary = new StringBuffer();
		//	Get Schemata
		if (m_model.getC_AcctSchema_ID() == 0)
			m_ass = MAcctSchema.getClientAcctSchema(getCtx(), m_model.getAD_Client_ID());
		else	//	only specific accounting schema
			m_ass = new MAcctSchema[] {new MAcctSchema (getCtx(), m_model.getC_AcctSchema_ID(), null)};
		//
		postSession();
		//
		int no = m_model.deleteLog();
		m_summary.append("Logs deleted=").append(no);
		//
		MAcctProcessorLog pLog = new MAcctProcessorLog(m_model, m_summary.toString());
		pLog.setReference("#" + String.valueOf(p_runCount) 
			+ " - " + TimeUtil.formatElapsed(new Timestamp(p_startWork)));
		pLog.save();
	}	//	doWork

	/**
	 * 	Post Session
	 */
	private void postSession()
	{
		for (int i = 0; i < Doc.documentsTableID.length; i++)
		{
			int AD_Table_ID = Doc.documentsTableID[i];
			String TableName = Doc.documentsTableName[i];
			//	Post only special documents
			if (m_model.getAD_Table_ID() != 0 
				&& m_model.getAD_Table_ID() != AD_Table_ID)
				continue;
			//  Select id FROM table
			StringBuffer sql = new StringBuffer ("SELECT * FROM ").append(TableName)
				.append(" WHERE AD_Client_ID=?")
				.append(" AND Processed='Y' AND Posted='N' AND IsActive='Y'");
			sql.append(" ORDER BY Created");
			//
			int count = 0;
			int countError = 0;
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql.toString(), null);
				pstmt.setInt(1, m_model.getAD_Client_ID());
				ResultSet rs = pstmt.executeQuery();
				while (!isInterrupted() && rs.next())
				{
					count++;
					boolean ok = true;
					try
					{
						Doc doc = Doc.get (m_ass, AD_Table_ID, rs, null);
						if (doc == null)
						{
							log.severe(getName() + ": No Doc for " + TableName);
							ok = false;
						}
						else
						{
							String error = doc.post(false, false);   //  post no force/repost
							ok = error == null;
						}
					}
					catch (Exception e)
					{
						log.log(Level.SEVERE, getName() + ": " + TableName, e);
						ok = false;
					}
					if (!ok)
						countError++;
				}
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, sql.toString(), e);
			}
			if (pstmt != null)
			{
				try
				{
					pstmt.close();
				}
				catch (Exception e)
				{
				}
			}
			//
			if (count > 0)
			{
				m_summary.append(TableName).append("=").append(count);
				if (countError > 0)
					m_summary.append("(Errors=").append(countError).append(")");
				m_summary.append(" - ");
				log.finer(getName() + ": " + m_summary.toString());
			}
			else
				log.finer(getName() + ": " + TableName + " - no work");
		}
	}	//	postSession
	
	/**
	 * 	Get Server Info
	 *	@return info
	 */
	public String getServerInfo()
	{
		return "#" + p_runCount + " - Last=" + m_summary.toString();
	}	//	getServerInfo

}	//	AcctProcessor
