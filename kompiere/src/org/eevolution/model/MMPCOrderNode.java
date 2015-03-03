/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2002 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * created by Victor Perez are Copyright (C) e-Evolution,SC. All Rights Reserved.
 * Contributor(s): Victor Perez
 *****************************************************************************/
//package org.compiere.mfg.model;
package org.eevolution.model;

import java.util.*;
import java.sql.*;
import java.awt.Point;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.wf.*;
import org.compiere.util.*;

/**
 *	Workflow Node
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: MMPCOrderNode.java,v 1.15 2004/05/15 06:29:16 jjanke Exp $
 */
public class MMPCOrderNode extends X_MPC_Order_Node
{
	/**
	 * 	Get WF Node from Cache
	 *	@param ctx context
	 *	@param MPC_Order_Node_ID id
	 *	@return MMPCOrderNode
	 */
	public static MMPCOrderNode get (Properties ctx, int MPC_Order_Node_ID,String trxName)
	{
		Integer key = new Integer (MPC_Order_Node_ID);
		MMPCOrderNode retValue = (MMPCOrderNode) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MMPCOrderNode (ctx, MPC_Order_Node_ID,"MPC_Order_Node");
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static CCache	s_cache	= new CCache ("MPC_Order_Node", 50);
	
	
	/**************************************************************************
	 * 	Standard Constructor - save to cache
	 *	@param ctx context
	 *	@param MPC_Order_Node_ID id
	 */
	public MMPCOrderNode(Properties ctx, int MPC_Order_Node_ID,String trxName)
	{
		super (ctx, MPC_Order_Node_ID,trxName);
		if (MPC_Order_Node_ID == 0)
		{
		//	setMPC_Order_Node_ID (0);
		//	setMPC_Order_Workflow_ID (0);
		//	setValue (null);
		//	setName (null);
			setAction (ACTION_WaitSleep);
			setCost (Env.ZERO);
			//setDuration (Env.ZERO);
			setEntityType (ENTITYTYPE_UserMaintained);	// U
			setIsCentrallyMaintained (true);	// Y
			setJoinElement (JOINELEMENT_XOR);	// X
			setLimit (0);
			setSplitElement (SPLITELEMENT_XOR);	// X
			
//			setWaitingTime (0);
			setXPosition (0);
			setYPosition (0);
                        setAD_Column_ID(0);
                        setAD_WF_Responsible_ID(0);
                        //setAD_Imagen_ID(0);
                        setSubflowExecution(SUBFLOWEXECUTION_Synchronously);                   
                        setDocAction(DOCACTION_None);
                        setStartMode(STARTMODE_Manual);                        
                        setFinishMode(FINISHMODE_Manual);
                       // setDurationLimit(0);
                        setPriority(0);
                        //setDuration(Env.ZERO);
                        //setWorkingTime(0);
                        // fjv e-evolution
                        setWaitingTime(0);
                        //setWaitingTime(Env.ZERO);
                        //end e-evolution
		}
		//	Save to Cache
		if (get_ID() != 0)
			s_cache.put (new Integer(getMPC_Order_Node_ID()), this);
	}	//	MMPCOrderNode

        
       
        
	/**
	 * 	Parent Constructor
	 *	@param wf workflow (parent)
	 *	@param Value value
	 *	@param Name name
	 */
	public MMPCOrderNode(MMPCOrderWorkflow wf, String Value, String Name)
	{
		this (wf.getCtx(), 0,Name);
		setClientOrg(wf);
		setMPC_Order_Workflow_ID (wf.getMPC_Order_Workflow_ID());
		setValue (Value);
		setName (Name);
	}	//	MMPCOrderNode
	
	/**
	 * 	Load Constructor - save to cache
	 * 	@param ctx context
	 * 	@param rs result set to load info from
	 */
	public MMPCOrderNode(Properties ctx, ResultSet rs,String trxName)
	{
		super (ctx, rs,trxName);
		loadNext();
		loadTrl();
		//	Save to Cache
		s_cache.put (new Integer(getMPC_Order_Node_ID()), this);
	}	//	MMPCOrderNode

	
	
	/**	Next Modes				*/
	private ArrayList		m_next = new ArrayList();
	/**	Translated Name			*/
	private String			m_name_trl = null;
	/**	Translated Description	*/
	private String			m_description_trl = null;
	/**	Translated Help			*/
	private String			m_help_trl = null;
	/**	Translation Flag		*/
	private boolean			m_translated = false;
	/**	Column					*/
	private M_Column		m_column = null;
	/**	Process Parameters		*/
	//private MMPCOrderNodePara[]	m_paras = null;
	
	/**
	 * 	Load Next
	 */
	private void loadNext()
	{
		String sql = "SELECT * FROM MPC_Order_NodeNext WHERE MPC_Order_Node_ID=? AND IsActive='Y' ORDER BY SeqNo";
		boolean splitAnd = SPLITELEMENT_AND.equals(getSplitElement());
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql);
			pstmt.setInt(1, get_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MMPCOrderNodeNext next = new MMPCOrderNodeNext (getCtx(), rs,"MPC_Order_NodeNext");
				next.setFromSplitAnd(splitAnd);
				m_next.add(next);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE,"loadNext", e);
		}
		log.info("loadNext #" + m_next.size());
	}	//	loadNext

	/**
	 * 	Load Translation
	 */
	private void loadTrl()
	{
		if (Env.isBaseLanguage(getCtx(), "MPC_Order_Workflow") || get_ID() == 0)
			return;
		String sql = "SELECT Name, Description, Help FROM MPC_Order_Node_Trl WHERE MPC_Order_Node_ID=? AND AD_Language=?";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql);
			pstmt.setInt(1, get_ID());
			pstmt.setString(2, Env.getAD_Language(getCtx()));
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				m_name_trl = rs.getString(1);
				m_description_trl = rs.getString(2);
				m_help_trl = rs.getString(3);
				m_translated = true;
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "loadTrl", e);
		}
		log.info("loadTrl " + m_translated);
	}	//	loadTrl

	/**
	 * 	Get Number of Next Nodes
	 * 	@return number of next nodes
	 */
	public int getNextNodeCount()
	{
		return m_next.size();
	}	//	getNextNodeCount

	/**
	 * 	Get the transitions
	 * 	@return array of next nodes
	 */
	public MMPCOrderNodeNext[] getTransitions()
	{
		MMPCOrderNodeNext[] retValue = new MMPCOrderNodeNext [m_next.size()];
		m_next.toArray(retValue);
		return retValue;
	}	//	getNextNodes

	
	/**************************************************************************
	 * 	Get Name
	 * 	@param translated translated
	 * 	@return Name
	 */
	public String getName(boolean translated)
	{
		if (translated && m_translated)
			return m_name_trl;
		return getName();
	}	//	getName

	/**
	 * 	Get Description
	 * 	@param translated translated
	 * 	@return Description
	 */
	public String getDescription(boolean translated)
	{
		if (translated && m_translated)
			return m_description_trl;
		return getDescription();
	}	//	getDescription

	/**
	 * 	Get Help
	 * 	@param translated translated
	 * 	@return Name
	 */
	public String getHelp(boolean translated)
	{
		if (translated && m_translated)
			return m_help_trl;
		return getHelp();
	}	//	getHelp

	/**
	 * 	Set Position
	 * 	@param position point
	 */
	public void setPosition (Point position)
	{
		setPosition(position.x, position.y);
	}	//	setPosition

	/**
	 * 	Set Position
	 * 	@param x x
	 * 	@param y y
	 */
	public void setPosition (int x, int y)
	{
		setXPosition(x);
		setYPosition(y);
	}	//	setPosition

	/**
	 * 	Get Position
	 * 	@return position point
	 */
	public Point getPosition ()
	{
		return new Point (getXPosition(), getYPosition());
	}	//	getPosition

	/**
	 * 	Get Action Info
	 *	@return info
	 */
	public String getActionInfo()
	{
		String action = getAction();
		if (ACTION_AppsProcess.equals(action))
			return "Process:AD_Process_ID=" + getAD_Process_ID();
		else if (ACTION_DocumentAction.equals(action))
			return "DocumentAction=" + getDocAction();
		else if (ACTION_AppsReport.equals(action))
			return "Report:AD_Process_ID=" + getAD_Process_ID();
		else if (ACTION_AppsTask.equals(action))
			return "Task:AD_Task_ID=" + getAD_Task_ID();
		else if (ACTION_SetVariable.equals(action))
			return "SetVariable:AD_Column_ID=" + getAD_Column_ID();
		else if (ACTION_SubWorkflow.equals(action))
			return "Workflow:MPC_Order_Workflow_ID=" + getMPC_Order_Workflow_ID();
		else if (ACTION_UserChoice.equals(action))
			return "UserChoice:AD_Column_ID=" + getAD_Column_ID();
		else if (ACTION_UserWorkbench.equals(action))
			return "Workbench:?";
		else if (ACTION_UserForm.equals(action))
			return "Form:AD_Form_ID=" + getAD_Form_ID();
		else if (ACTION_UserWindow.equals(action))
			return "Window:AD_Window_ID=" + getAD_Window_ID();
		/*else if (ACTION_WaitSleep.equals(action))
			return "Sleep:WaitTime=" + getWaitTime();*/
		return "??";
	}	//	getActionInfo
	
	
	/**
	 * 	Get Attribute Name
	 *	@see org.compiere.model.X_MPC_Order_Node#getAttributeName()
	 *	@return Attribute Name
	 */
	public String getAttributeName ()
	{
		if (getAD_Column_ID() == 0)
			return super.getAttributeName();
		//	We have a column
		String attribute = super.getAttributeName();
		if (attribute != null && attribute.length() > 0)
			return attribute;
		setAttributeName(getColumn().getColumnName());
		return super.getAttributeName ();
	}	//	getAttributeName
	
	
	/**
	 * 	Get Column
	 *	@return column if valid
	 */
	public M_Column getColumn()
	{
		if (getAD_Column_ID() == 0)
			return null;
		if (m_column == null)
			m_column = M_Column.get(getCtx(), getAD_Column_ID());
		return m_column;
	}	//	getColumn
	
	/**
	 * 	Is this an Approval setp?
	 *	@return true if User Approval
	 */
	public boolean isUserApproval()
	{
		if (!ACTION_UserChoice.equals(getAction()))
			return false;
		return getColumn() != null 
			&& "IsApproved".equals(getColumn().getColumnName());
	}	//	isApproval

	/**
	 * 	Is this a User Choice step?
	 *	@return true if User Choice
	 */
	public boolean isUserChoice()
	{
		return ACTION_UserChoice.equals(getAction());
	}	//	isUserChoice
	
	/**
	 * 	Is this a Manual user step?
	 *	@return true if Window/Form/Workbench
	 */
	public boolean isUserManual()
	{
		if (ACTION_UserForm.equals(getAction())
			|| ACTION_UserWindow.equals(getAction())
			|| ACTION_UserWorkbench.equals(getAction()))
			return true;
		return false;
	}	//	isUserManual
	
	/**************************************************************************
	 * 	Get Parameters
	 *	@return array of parameters
	 */
        /** no apply for Manufacture
	public MMPCOrderNodePara[] getParameters()
	{
		if (m_paras == null)
			m_paras = MMPCOrderNodePara.getParameters(getCtx(), getMPC_Order_Node_ID());
		return m_paras;
	}	//	getParameters
         */

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MMPCOrderNode[");
		sb.append(get_ID()).append("-").append(getName())
			.append(",Action=").append(getActionInfo())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	User String Representation
	 *	@return info
	 */
	public String toStringX ()
	{
		StringBuffer sb = new StringBuffer ("MMPCOrderNode[");
		sb.append(getName())
			.append("-").append(getActionInfo());
		return sb.toString ();
	}	//	toStringX
	
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return saved
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		//TranslationTable.save(this, newRecord);
		return true;
	}	//	afterSave
	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return deleted
	 */
	protected boolean afterDelete (boolean success)
	{
		if (!TranslationTable.isActiveLanguages(false))
			return true;
		TranslationTable.delete(this);
		return true;
	}	//	afterDelete

}	//	M_WFNext
