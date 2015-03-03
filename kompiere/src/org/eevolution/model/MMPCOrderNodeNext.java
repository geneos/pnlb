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

import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.*;

/**
 *	Workflow Node Next - Transition
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: MWFNodeNext.java,v 1.10 2004/05/20 05:57:47 jjanke Exp $
 */
public class MMPCOrderNodeNext extends X_MPC_Order_NodeNext
{
	/**
	 * 	Standard Costructor
	 *	@param ctx context
	 *	@param MPC_Order_NodeNext_ID id
	 */
	public MMPCOrderNodeNext(Properties ctx, int MPC_Order_NodeNext_ID,String trxName)
	{
		super (ctx, MPC_Order_NodeNext_ID,trxName);
		if (MPC_Order_NodeNext_ID == 0)
		{
		//	setMPC_Order_Next_ID (0);
		//	setMPC_Order_Node_ID (0);
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
	public MMPCOrderNodeNext(Properties ctx, ResultSet rs,String trxName)
	{
		super (ctx, rs,trxName);
	}	//	MWFNodeNext

  
        
        
	/**
	 * 	Create new
	 * 	@param ctx context
	 * 	@param MPC_Order_Node_ID Node
	 * 	@param MPC_Order_Next_ID Next
	 * 	@param SeqNo sequence
	 */
	public MMPCOrderNodeNext(Properties ctx, int MPC_Order_Node_ID, int MPC_Order_Next_ID, int SeqNo, String trxName)
	{
		super (ctx, 0,trxName);
		setMPC_Order_Node_ID(MPC_Order_Node_ID);
		setMPC_Order_Next_ID(MPC_Order_Next_ID);
		//
		setEntityType (ENTITYTYPE_UserMaintained);	// U
		setIsStdUserWorkflow (false);
		setSeqNo (SeqNo);
		
		save(get_TrxName());
	}	//	MWFNodeNext

	/** Transition Conditions			*/
        /** no apply for manufacturing
	private MWFNextCondition[] 	m_conditions = null;
         */
	/**	From (Split Eleemnt) is AND		*/
	public Boolean				m_fromSplitAnd = null;
	/**	To (Join Element) is AND		*/
	public Boolean				m_toJoinAnd = null;
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MWFNodeNext[");
		sb.append(getSeqNo())
			.append(":Node=").append(getMPC_Order_Node_ID()).append("->Next=").append(getMPC_Order_Next_ID());
		/*if (m_conditions != null)
			sb.append(",#").append(m_conditions.length);*/
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(",").append(getDescription());
		sb.append ("]");
		return sb.toString ();
	}	//	toString
	
	
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
		if (m_toJoinAnd == null && getMPC_Order_Next_ID() != 0)
		{
			MMPCOrderNode next = MMPCOrderNode.get(getCtx(), getMPC_Order_Next_ID(),"MPC_Order_Node");
			setToJoinAnd(MMPCOrderNode.JOINELEMENT_AND.equals(next.getJoinElement()));
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