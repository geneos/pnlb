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

import org.compiere.model.*;
import org.compiere.process.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Workflow Node Next - Transition
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: MWFNodeNext.java,v 1.17 2005/09/29 22:01:56 jjanke Exp $
 */
public class MWFNodeNext extends X_AD_WF_NodeNext
{
	/**
	 * 	Standard Costructor
	 *	@param ctx context
	 *	@param AD_WF_NodeNext_ID id
	 */
	public MWFNodeNext (Properties ctx, int AD_WF_NodeNext_ID, String trxName)
	{
		super (ctx, AD_WF_NodeNext_ID, trxName);
		if (AD_WF_NodeNext_ID == 0)
		{
		//	setAD_WF_Next_ID (0);
		//	setAD_WF_Node_ID (0);
			setEntityType (ENTITYTYPE_UserMaintained);	// U
			setIsStdUserWorkflow (false);
			setSeqNo (10);	// 10
		}
	}	//	MWFNodeNext
	
	/**
	 * 	Default Constructor
	 * 	@param ctx context
	 * 	@param rs result set to load info from
	 */
	public MWFNodeNext (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MWFNodeNext

	/**
	 * 	Parent constructor
	 * 	@param parent patent
	 * 	@param AD_WF_Next_ID Next
	 */
	public MWFNodeNext (MWFNode parent, int AD_WF_Next_ID)
	{
		this(parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setAD_WF_Node_ID(parent.getAD_WF_Node_ID());
		setAD_WF_Next_ID(AD_WF_Next_ID);
	}	//	MWFNodeNext

	/** Transition Conditions			*/
	private MWFNextCondition[] 	m_conditions = null;
	/**	From (Split Eleemnt) is AND		*/
	public Boolean				m_fromSplitAnd = null;
	/**	To (Join Element) is AND		*/
	public Boolean				m_toJoinAnd = null;
	
	/**
	 * 	Set Client Org
	 *	@param AD_Client_ID client
	 *	@param AD_Org_ID org
	 */
	public void setClientOrg (int AD_Client_ID, int AD_Org_ID)
	{
		super.setClientOrg (AD_Client_ID, AD_Org_ID);
	}	//	setClientOrg
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MWFNodeNext[");
		sb.append(getSeqNo())
			.append(":Node=").append(getAD_WF_Node_ID()).append("->Next=").append(getAD_WF_Next_ID());
		if (m_conditions != null)
			sb.append(",#").append(m_conditions.length);
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(",").append(getDescription());
		sb.append ("]");
		return sb.toString ();
	}	//	toString
	
	
	/*************************************************************************
	 * 	Get Conditions
	 * 	@param requery true if requery
	 *	@return Array of Conditions
	 */
	public MWFNextCondition[] getConditions(boolean requery)
	{
		if (!requery && m_conditions != null)
			return m_conditions;
		//
		ArrayList<MWFNextCondition> list = new ArrayList<MWFNextCondition>();
		String sql = "SELECT * FROM AD_WF_NextCondition WHERE AD_WF_NodeNext_ID=? AND IsActive='Y' ORDER BY SeqNo";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getAD_WF_NodeNext_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MWFNextCondition(getCtx(), rs, get_TrxName()));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
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
		m_conditions = new MWFNextCondition[list.size()];
		list.toArray (m_conditions);
		return m_conditions;
	}	//	getConditions

	/**
	 * 	Unconditional
	 *	@return true if no conditions
	 */
	public boolean isUnconditional()
	{
		return !isStdUserWorkflow() && getConditions(false).length == 0;
	}	//	isUnconditional
	
	/**
	 * 	Is this a Valid Transition For ..
	 *	@return true if valid
	 */
	public boolean isValidFor (MWFActivity activity)
	{
		if (isStdUserWorkflow())
		{
			PO po = activity.getPO();
			if (po instanceof DocAction)
			{
				DocAction da = (DocAction)po;
				String docStatus = da.getDocStatus();
				String docAction = da.getDocAction();
				if (!DocAction.ACTION_Complete.equals(docAction)
					|| DocAction.STATUS_Completed.equals(docStatus)
					|| DocAction.STATUS_WaitingConfirmation.equals(docStatus)
					|| DocAction.STATUS_WaitingPayment.equals(docStatus)
					|| DocAction.STATUS_Voided.equals(docStatus)
					|| DocAction.STATUS_Closed.equals(docStatus)
					|| DocAction.STATUS_Reversed.equals(docStatus) )
					/*
					|| DocAction.ACTION_Complete.equals(docAction)	
					|| DocAction.ACTION_ReActivate.equals(docAction)	
					|| DocAction.ACTION_None.equals(docAction)
					|| DocAction.ACTION_Post.equals(docAction)
					|| DocAction.ACTION_Unlock.equals(docAction)
					|| DocAction.ACTION_Invalidate.equals(docAction)	) */
				{
					log.fine("isValidFor =NO= StdUserWF - Status=" + docStatus + " - Action=" + docAction);
					return false;
				}
			}
		}
		//	No Conditions
		if (getConditions(false).length == 0)
		{
			log.fine("isValidFor #0 " + toString());
			return true;
		}
		//
		boolean ok = true;
		for (int i = 0; i < m_conditions.length; i++)
		{
			//	First condition always AND
			if (i == 0 && m_conditions[i].isOr())
				m_conditions[i].setAndOr(MWFNextCondition.ANDOR_And);
				
			//	we have an OR condition 
			if (m_conditions[i].isOr() && i > 0)
			{
				//	with existing True condition
				if (ok)
				{
					log.fine("isValidFor #" + i + "(true) " + toString());
					return true;
				}
				ok = true;	//	reset
			}
			
			ok = m_conditions[i].evaluate(activity);
			
		}	//	for all conditions
		log.fine("isValidFor (" + ok + ") " + toString());
		return ok;
	}	//	isValidFor
	
	
	/**
	 * 	Split Element is AND
	 * 	@return Returns the from Split And.
	 */
	public boolean isFromSplitAnd()
	{
		if (m_fromSplitAnd != null)
			return m_fromSplitAnd.booleanValue();
		return false;
	}	//	getFromSplitAnd

	/**
	 * 	Split Element is AND.
	 * 	Set by MWFNode.loadNodes
	 *	@param fromSplitAnd The from Split And
	 */
	public void setFromSplitAnd (boolean fromSplitAnd)
	{
		m_fromSplitAnd = new Boolean(fromSplitAnd);
	}	//	setFromSplitAnd

	/**
	 * 	Join Element is AND
	 *	@return Returns the to Join And.
	 */
	public boolean isToJoinAnd ()
	{
		if (m_toJoinAnd == null && getAD_WF_Next_ID() != 0)
		{
			MWFNode next = MWFNode.get(getCtx(), getAD_WF_Next_ID());
			setToJoinAnd(MWFNode.JOINELEMENT_AND.equals(next.getJoinElement()));
		}
		if (m_toJoinAnd != null)
			return m_toJoinAnd.booleanValue();
		return false;
	}	//	getToJoinAnd

	/**
	 * 	Join Element is AND.
	 *	@param toJoinAnd The to Join And to set.
	 */
	private void setToJoinAnd (boolean toJoinAnd)
	{
		m_toJoinAnd = new Boolean(toJoinAnd);
	}	//	setToJoinAnd

}	//	MWFNodeNext
