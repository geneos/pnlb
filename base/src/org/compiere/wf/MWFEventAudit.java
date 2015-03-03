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
import java.util.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Workflow Event Audit
 *	
 *  @author Jorg Janke
 *  @version $Id: MWFEventAudit.java,v 1.10 2005/11/06 01:17:27 jjanke Exp $
 */
public class MWFEventAudit extends X_AD_WF_EventAudit
{
	/**
	 * 	Get Event Audit for node
	 *	@param ctx context
	 *	@param AD_WF_Process_ID process
	 *	@param AD_WF_Node_ID optional node
	 *	@return event audit or null
	 */
	public static MWFEventAudit[] get (Properties ctx, int AD_WF_Process_ID, int AD_WF_Node_ID)
	{
		ArrayList<MWFEventAudit> list = new ArrayList<MWFEventAudit>();
		String sql = "SELECT * FROM AD_WF_EventAudit "
			+ "WHERE AD_WF_Process_ID=?";
		if (AD_WF_Node_ID > 0)
			sql += " AND AD_WF_Node_ID=?";
		sql += " ORDER BY AD_WF_EventAudit_ID";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, AD_WF_Process_ID);
			if (AD_WF_Node_ID > 0)
				pstmt.setInt (2, AD_WF_Node_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MWFEventAudit (ctx, rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "get", e);
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
		//
		MWFEventAudit[] retValue = new MWFEventAudit[list.size()];
		list.toArray (retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get Event Audit for node
	 *	@param ctx context
	 *	@param AD_WF_Process_ID process
	 *	@return event audit or null
	 */
	public static MWFEventAudit[] get (Properties ctx, int AD_WF_Process_ID)
	{
		return get(ctx, AD_WF_Process_ID, 0);
	}	//	get
	
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MWFEventAudit.class);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param AD_WF_EventAudit_ID id
	 */
	public MWFEventAudit (Properties ctx, int AD_WF_EventAudit_ID, String trxName)
	{
		super (ctx, AD_WF_EventAudit_ID, trxName);
	}	//	MWFEventAudit

	/**
	 * 	Load Cosntructors
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MWFEventAudit (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MWFEventAudit

	/**
	 * 	Activity Constructor
	 *	@param activity activity
	 */
	public MWFEventAudit (MWFActivity activity)
	{
		super (activity.getCtx(), 0, activity.get_TrxName());
		setAD_WF_Process_ID (activity.getAD_WF_Process_ID());
		setAD_WF_Node_ID (activity.getAD_WF_Node_ID());
		setAD_Table_ID (activity.getAD_Table_ID());
		setRecord_ID (activity.getRecord_ID());
		//
		setAD_WF_Responsible_ID (activity.getAD_WF_Responsible_ID());
		setAD_User_ID(activity.getAD_User_ID());
		//
		setWFState (activity.getWFState());
		setEventType (EVENTTYPE_ProcessCreated);
		setElapsedTimeMS (Env.ZERO);
		//
		MWFNode node = activity.getNode();
		if (node != null && node.get_ID() != 0)
		{
			String action = node.getAction();
			if (MWFNode.ACTION_SetVariable.equals(action)
				|| MWFNode.ACTION_UserChoice.equals(action))
			{
				setAttributeName(node.getAttributeName());
				setOldValue(String.valueOf(activity.getAttributeValue()));
				if (MWFNode.ACTION_SetVariable.equals(action))
					setNewValue(node.getAttributeValue());
			}
		}
	}	//	MWFEventAudit
	
	/**
	 * 	Get Node Name
	 *	@return node name
	 */
	public String getNodeName()
	{
		MWFNode node = MWFNode.get(getCtx(), getAD_WF_Node_ID());
		if (node.get_ID() == 0)
			return "?";
		return node.getName(true);
	}	//	getNodeName
	
	
}	//	MWFEventAudit
