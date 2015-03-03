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
import org.compiere.process.*;
import org.compiere.util.*;


/**
 *	Workflow Process
 *	
 *  @author Jorg Janke
 *  @version $Id: MWFProcess.java,v 1.27 2005/11/05 23:27:40 jjanke Exp $
 */
public class MWFProcess extends X_AD_WF_Process
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_WF_Process_ID process
	 */
	public MWFProcess (Properties ctx, int AD_WF_Process_ID, String trxName)
	{
		super (ctx, AD_WF_Process_ID, trxName);
		if (AD_WF_Process_ID == 0)
			throw new IllegalArgumentException ("Cannot create new WF Process directly");
		m_state = new StateEngine (getWFState());
	}	//	MWFProcess

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MWFProcess (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
		m_state = new StateEngine (getWFState());
	}	//	MWFProcess
	
	/**
	 * 	New Constructor
	 *	@param wf workflow
	 *	@param pi Process Info (Record_ID)
	 */
	public MWFProcess (MWorkflow wf, ProcessInfo pi) throws Exception
	{
		super (wf.getCtx(), 0, wf.get_TrxName());
		if (!TimeUtil.isValid(wf.getValidFrom(), wf.getValidTo()))
			throw new IllegalStateException("Workflow not valid");
		m_wf = wf;
		m_pi = pi;
		setAD_Workflow_ID (wf.getAD_Workflow_ID());
		setPriority(wf.getPriority());
		//	Document
		setAD_Table_ID(wf.getAD_Table_ID());
		setRecord_ID(pi.getRecord_ID());
		setTextMsg(getPO());
		//	Responsible/User
		if (wf.getAD_WF_Responsible_ID() == 0)
			setAD_WF_Responsible_ID();
		else
			setAD_WF_Responsible_ID(wf.getAD_WF_Responsible_ID());
		setUser_ID(pi.getAD_User_ID());		//	user starting
		//
		super.setWFState (WFSTATE_NotStarted);
		m_state = new StateEngine (getWFState());
		setProcessed (false);
	}	//	MWFProcess

	/**	State Machine				*/
	private StateEngine			m_state = null;
	/**	Activities					*/
	private MWFActivity[] 		m_activities = null;
	/**	Workflow					*/
	private MWorkflow			m_wf = null;
	/**	Process Info				*/
	private ProcessInfo			m_pi = null;
	/**	Persistent Object			*/
	private PO					m_po = null;
	/** Message from Activity		*/
	private String				m_processMsg = null;
	
	/**
	 * 	Get active Activities of Process
	 *	@param requery if true requery
	 *	@return array of activities
	 */
	public MWFActivity[] getActivities (boolean requery, boolean onlyActive)
	{
		if (!requery && m_activities != null)
			return m_activities;
		//
		ArrayList<MWFActivity> list = new ArrayList<MWFActivity>();
		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM AD_WF_Activity WHERE AD_WF_Process_ID=?";
		if (onlyActive)
			sql += " AND Processed='N'";
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getAD_WF_Process_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MWFActivity(getCtx(), rs, get_TrxName()));
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
		m_activities = new MWFActivity[list.size ()];
		list.toArray (m_activities);
		return m_activities;
	}	//	getActivities
	
	/**
	 * 	Get State
	 *	@return state
	 */
	public StateEngine getState()
	{
		return m_state;
	}	//	getState
	
	/**
	 * 	Get Action Options
	 *	@return array of valid actions
	 */
	public String[] getActionOptions()
	{
		return m_state.getActionOptions();
	}	//	getActionOptions
	
	/**
	 * 	Set Process State and update Actions
	 *	@param WFState
	 */
	public void setWFState (String WFState)
	{
		if (m_state == null)
			m_state = new StateEngine (getWFState());
		if (m_state.isClosed())
			return;
		if (getWFState().equals(WFState))
			return;
		//
		if (m_state.isValidNewState(WFState))
		{
			log.fine(WFState); 
			super.setWFState (WFState);
			m_state = new StateEngine (getWFState());
			if (m_state.isClosed())
				setProcessed(true);
			save();
			//	Force close to all Activities
			if (m_state.isClosed())
			{
				MWFActivity[] activities = getActivities(true, true);	//	requery only active
				for (int i = 0; i < activities.length; i++)
				{
					if (!activities[i].isClosed())
					{
						activities[i].setTextMsg("Process:" + WFState);
						activities[i].setWFState(WFState);
					}
					if (!activities[i].isProcessed())
						activities[i].setProcessed(true);
					activities[i].save();
				}
			}	//	closed
		}
		else	
			log.log(Level.SEVERE, "Ignored Invalid Transformation - New=" + WFState 
				+ ", Current=" + getWFState());
	}	//	setWFState

	
	/**************************************************************************
	 * 	Check Status of Activities
	 * 	- update Process if required
	 * 	- start new activity
	 */
	public void checkActivities()
	{
		log.info("(" + getAD_Workflow_ID() + ") - " + getWFState());
		if (m_state.isClosed())
			return;
		//
		MWFActivity[] activities = getActivities (true, true);	//	requery active
		String closedState = null;
		boolean suspended = false;
		boolean running = false;
		for (int i = 0; i < activities.length; i++)
		{
			MWFActivity activity = activities[i];
			StateEngine activityState = activity.getState(); 
			
			//	Completed - Start Next
			if (activityState.isCompleted())
			{
				if (startNext (activity, activities))
					continue;		
			}
			//
			String activityWFState = activity.getWFState();
			if (activityState.isClosed())
			{
				//	eliminate from active processed
				activity.setProcessed(true);
				activity.save();
				//
				if (closedState == null)
					closedState = activityWFState;
				else if (!closedState.equals(activityState))
				{
					//	Overwrite if terminated
					if (WFSTATE_Terminated.equals(activityState))
						closedState = activityWFState;
					//	Overwrite if activity aborted and no other terminated
					else if (WFSTATE_Aborted.equals(activityState) && !WFSTATE_Terminated.equals(closedState))
						closedState = activityWFState;
				}
			}
			else	//	not closed
			{
				closedState = null;		//	all need to be closed
				if (activityState.isSuspended())
					suspended = true;
				if (activityState.isRunning())
					running = true;
			}
		}	//	for all activities
		if (activities.length == 0)
		{
			setTextMsg("No Active Processed found");
			closedState = WFSTATE_Terminated;
		}
		if (closedState != null)
			setWFState(closedState);
		else if (suspended)
			setWFState(WFSTATE_Suspended);
		else if (running)
			setWFState(WFSTATE_Running);
	}	//	checkActivities


	/**
	 * 	Start Next Activity
	 *	@param last last activity
	 *	@param activities all activities
	 *	@return true if there is a next activity
	 */
	private boolean startNext (MWFActivity last, MWFActivity[] activities)
	{
		log.fine("Last=" + last);
		//	transitions from the last processed node
		MWFNodeNext[] transitions = getWorkflow().getNodeNexts(
			last.getAD_WF_Node_ID(), last.getPO_AD_Client_ID());
		if (transitions == null || transitions.length == 0)
			return false;	//	done
		
		//	We need to wait for last activity
		if (MWFNode.JOINELEMENT_AND.equals(last.getNode().getJoinElement()))
		{
			//	get previous nodes
			//	check if all have closed activities
			//	return false for all but the last
		}
		//	eliminate from active processed
		last.setProcessed(true);
		last.save();

		//	Start next activity
		String split = last.getNode().getSplitElement();
		for (int i = 0; i < transitions.length; i++)
		{
			//	Is this a valid transition?
			if (!transitions[i].isValidFor(last))
				continue;
			
			//	Start new Activity
			MWFActivity activity = new MWFActivity (this, transitions[i].getAD_WF_Next_ID());
			new Thread(activity).start();
			
			//	only the first valid if XOR
			if (MWFNode.SPLITELEMENT_XOR.equals(split))
				return true;
		}	//	for all transitions
		return true;
	}	//	startNext

	
	/**************************************************************************
	 * 	Set Workflow Responsible.
	 * 	Searches for a Invoker.
	 */
	public void setAD_WF_Responsible_ID ()
	{
		int AD_WF_Responsible_ID = DB.getSQLValue(null,
			MRole.getDefault(getCtx(), false).addAccessSQL(	
			"SELECT AD_WF_Responsible_ID FROM AD_WF_Responsible "
			+ "WHERE ResponsibleType='H' AND COALESCE(AD_User_ID,0)=0 "
			+ "ORDER BY AD_Client_ID DESC", 
			"AD_WF_Responsible", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO));
		setAD_WF_Responsible_ID (AD_WF_Responsible_ID);
	}	//	setAD_WF_Responsible_ID

	/**
	 * 	Set User from 
	 * 	- (1) Responsible
	 *  - (2) Document Sales Rep
	 *  - (3) Document UpdatedBy
	 * 	- (4) Process invoker
	 * 	@param User_ID process invoker
	 */
	private void setUser_ID (Integer User_ID)
	{
		//	Responsible
		MWFResponsible resp = MWFResponsible.get(getCtx(), getAD_WF_Responsible_ID());
		//	(1) User - Directly responsible
		int AD_User_ID = resp.getAD_User_ID();
		
		//	Invoker - get Sales Rep or last updater of Document
		if (AD_User_ID == 0 && resp.isInvoker())
		{
			getPO();
			//	(2) Doc Owner
			if (m_po != null && m_po instanceof DocAction)
			{
				DocAction da = (DocAction)m_po;
				AD_User_ID = da.getDoc_User_ID();
			}
			//	(2) Sales Rep
			if (AD_User_ID == 0 && m_po != null && m_po.get_ColumnIndex("SalesRep_ID") != -1)
			{
				Object sr = m_po.get_Value("SalesRep_ID");
				if (sr != null && sr instanceof Integer)
					AD_User_ID = ((Integer)sr).intValue();
			}
			//	(3) UpdatedBy
			if (AD_User_ID == 0 && m_po != null)
				AD_User_ID = m_po.getUpdatedBy();
		}
		
		//	(4) Process Owner
		if (AD_User_ID == 0 && User_ID != null)
			AD_User_ID = User_ID.intValue();
		//	Fallback 
		if (AD_User_ID == 0)
			AD_User_ID = Env.getAD_User_ID(getCtx());
		//
		setAD_User_ID(AD_User_ID);
	}	//	setUser_ID
	
	/**
	 * 	Get Workflow
	 *	@return workflow
	 */
	private MWorkflow getWorkflow()
	{
		if (m_wf == null)
			m_wf = MWorkflow.get (getCtx(), getAD_Workflow_ID());
		if (m_wf.get_ID() == 0)
			throw new IllegalStateException("Not found - AD_Workflow_ID=" + getAD_Workflow_ID());
		return m_wf;
	}	//	getWorkflow
	
	
	/**************************************************************************
	 * 	Perform Action
	 *	@param action StateEngine.ACTION_*
	 *	@return true if valid
	 */
	public boolean perform (String action)
	{
		if (!m_state.isValidAction(action))
		{
			log.log(Level.SEVERE, "Ignored Invalid Transformation - Action=" + action 
				+ ", CurrentState=" + getWFState());
			return false;
		}
		log.fine(action); 
		//	Action is Valid
		if (StateEngine.ACTION_Start.equals(action))
			return startWork();
		//	Set new State
		setWFState (m_state.getNewStateIfAction(action));
		return true;
	}	//	perform
	
	/**
	 * 	Start WF Execution async
	 *	@return true if success
	 */
	public boolean startWork()
	{
		if (!m_state.isValidAction(StateEngine.ACTION_Start))
		{
			log.warning("State=" + getWFState() + " - cannot start");
			return false;
		}
		int AD_WF_Node_ID = getWorkflow().getAD_WF_Node_ID();
		log.fine("AD_WF_Node_ID=" + AD_WF_Node_ID);
		setWFState(WFSTATE_Running);
		try
		{
			//	Start first Activity with first Node
			MWFActivity activity = new MWFActivity (this, AD_WF_Node_ID);
			new Thread(activity).start();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "AD_WF_Node_ID=" + AD_WF_Node_ID, e);
			setTextMsg(e.toString());
			setWFState(StateEngine.STATE_Terminated);
			return false;
		}
		return true;
	}	//	performStart
	

	/**************************************************************************
	 * 	Get Persistent Object
	 *	@return po
	 */
	public PO getPO()
	{
		if (m_po != null)
			return m_po;
		if (getRecord_ID() == 0)
			return null;
		
		M_Table table = M_Table.get (getCtx(), getAD_Table_ID());
		m_po = table.getPO(getRecord_ID(), get_TrxName());
		return m_po;
	}	//	getPO

	/**
	 * 	Set Text Msg (add to existing)
	 *	@param po base object
	 */
	public void setTextMsg (PO po)
	{
		if (po != null && po instanceof DocAction)
			setTextMsg(((DocAction)po).getSummary());
	}	//	setTextMsg	

	/**
	 * 	Set Text Msg (add to existing)
	 *	@param TextMsg msg
	 */
	public void setTextMsg (String TextMsg)
	{
		String oldText = getTextMsg();
		if (oldText == null || oldText.length() == 0)
			super.setTextMsg (TextMsg);
		else if (TextMsg != null && TextMsg.length() > 0)
			super.setTextMsg (oldText + "\n - " + TextMsg);
	}	//	setTextMsg	


	/**
	 * 	Set Runtime (Error) Message
	 *	@param msg message
	 */
	public void setProcessMsg (String msg)
	{
		m_processMsg = msg;
		if (msg != null && msg.length() > 0)
			setTextMsg(msg);
	}	//	setProcessMsg
	
	/**
	 * 	Get Runtime (Error) Message
	 *	@return msg
	 */
	public String getProcessMsg()
	{
		return m_processMsg;
	}	//	getProcessMsg
	
}	//	MWFProcess
