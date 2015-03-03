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
package org.compiere.wf;

import java.sql.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.*;


/**
 *	Document Workflow Manager
 *	
 *  @author Jorg Janke
 *  @version $Id: DocWorkflowManager.java,v 1.9 2005/11/05 00:43:37 jjanke Exp $
 */
public class DocWorkflowManager implements DocWorkflowMgr
{
	/**
	 * 	Get Document Workflow Manager
	 *	@return mgr
	 */
	public static DocWorkflowManager get()
	{
		if (s_mgr == null)
			s_mgr = new DocWorkflowManager();
		return s_mgr;
	}	//	get

	//	Set PO Workflow Manager
	static {
		PO.setDocWorkflowMgr(get());
	}
	
	/**	Document Workflow Manager		*/
	private static DocWorkflowManager	s_mgr = null;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(DocWorkflowManager.class);
	
	
	/**
	 * 	Doc Workflow Manager
	 */
	private DocWorkflowManager ()
	{
		super ();
		if (s_mgr == null)
			s_mgr = this;
	}	//	DocWorkflowManager

	private int	m_noCalled = 0;
	private int	m_noStarted = 0;
	
	/**
	 * 	Process Document Value Workflow
	 *	@param document document
	 *	@param AD_Table_ID table
	 *	@return true if WF started
	 */
	public boolean process (PO document, int AD_Table_ID)
	{
		m_noCalled++;
		MWorkflow[] wfs = MWorkflow.getDocValue (document.getCtx(), 
			document.getAD_Client_ID(), AD_Table_ID);
		if (wfs == null || wfs.length == 0)
			return false;
		
		boolean started = false;
		for (int i = 0; i < wfs.length; i++)
		{
			MWorkflow wf = wfs[i];
			//	We have a Document Workflow
			String logic = wf.getDocValueLogic();
			if (logic == null || logic.length() == 0)
			{
				log.severe ("Workflow has no Logic - " + wf.getName());
				continue;
			}
		
			//	Re-check: Document must be same Client as workflow
			if (wf.getAD_Client_ID() != document.getAD_Client_ID())
				continue;
		
			//	Check Logic
			boolean sql = logic.startsWith("SQL=");
			if (sql && !testStart(wf, document))
			{
				log.fine("SQL Logic evaluated to false (" + logic + ")");
				continue;
			}
			if (!sql && !Evaluator.evaluateLogic(document, logic))
			{
				log.fine("Logic evaluated to false (" + logic + ")");
				continue;
			}
		
			//	Start Workflow
			log.fine(logic);
			int AD_Process_ID = 305;		//	HARDCODED
			ProcessInfo pi = new ProcessInfo (wf.getName(), AD_Process_ID, 
				AD_Table_ID, document.get_ID());
			pi.setAD_User_ID (Env.getAD_User_ID(document.getCtx()));
			pi.setAD_Client_ID(document.getAD_Client_ID());
			//
			if (wf.start(pi) != null)
			{
				log.config(wf.getName());
				m_noStarted++;
				started = true;
			}
		}
		return started;
	}	//	process

	/**
	 * 	Test Start condition
	 *	@param wf workflow
	 *	@param document document
	 *	@return true if WF should be started
	 */
	private boolean testStart (MWorkflow wf, PO document)
	{
		boolean retValue = false;
		String logic = wf.getDocValueLogic();
		logic = logic.substring(4);		//	"SQL="
		//
		String tableName = document.get_TableName();
		String[] keyColumns = document.get_KeyColumns();
		if (keyColumns.length != 1)
		{
			log.severe("Tables with more then one key column not supported - " 
				+ tableName + " = " + keyColumns.length);
			return false;
		}
		String keyColumn = keyColumns[0];
		StringBuffer sql = new StringBuffer("SELECT ")
			.append(keyColumn).append(" FROM ").append(tableName)
			.append(" WHERE AD_Client_ID=? AND ")		//	#1
				.append(keyColumn).append("=? AND ")	//	#2
			.append(logic)
		//	Duplicate Open Workflow test
			.append(" AND NOT EXISTS (SELECT * FROM AD_WF_Process wfp ")
				.append("WHERE wfp.AD_Table_ID=? AND wfp.Record_ID=")	//	#3
				.append(tableName).append(".").append(keyColumn)
				.append(" AND wfp.AD_Workflow_ID=?")	//	#4
				.append(" AND SUBSTR(wfp.WFState,1,1)='O')");
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), document.get_TrxName());
			pstmt.setInt (1, wf.getAD_Client_ID());
			pstmt.setInt (2, document.get_ID());
			pstmt.setInt (3, document.get_Table_ID());
			pstmt.setInt (4, wf.getAD_Workflow_ID());
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = true;
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, "Logic=" + logic
				+ " - SQL=" + sql.toString(), e);
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
		return retValue;
	}	//	testStart
	
	
	/**
	* 	String Representation
	*	@return info
	*/
	public String toString()
	{
		StringBuffer sb = new StringBuffer("DocWorkflowManager[");
		sb.append("Called=").append(m_noCalled)
			.append(",Stated=").append(m_noStarted)
			.append("]");
		return sb.toString();
	}	//	toString
}	//	DocWorkflowManager
