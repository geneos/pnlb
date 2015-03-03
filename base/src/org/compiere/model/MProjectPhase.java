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
package org.compiere.model;

import java.sql.*;
import java.util.*;

import java.util.logging.*;
import org.compiere.util.*;

/**
 * 	Project Phase Model
 *
 *	@author Jorg Janke
 *	@version $Id: MProjectPhase.java,v 1.9 2005/10/14 00:44:31 jjanke Exp $
 */
public class MProjectPhase extends X_C_ProjectPhase
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_ProjectPhase_ID id
	 */
	public MProjectPhase (Properties ctx, int C_ProjectPhase_ID, String trxName)
	{
		super (ctx, C_ProjectPhase_ID, trxName);
		if (C_ProjectPhase_ID == 0)
		{
		//	setC_ProjectPhase_ID (0);	//	PK
		//	setC_Project_ID (0);		//	Parent
		//	setC_Phase_ID (0);			//	FK
			setCommittedAmt (Env.ZERO);
			setIsCommitCeiling (false);
			setIsComplete (false);
			setSeqNo (0);
		//	setName (null);
			setQty (Env.ZERO);
		}
	}	//	MProjectPhase

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MProjectPhase (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MProjectPhase

	/**
	 * 	Parent Constructor
	 *	@param project parent
	 */
	public MProjectPhase (MProject project)
	{
		this (project.getCtx(), 0, project.get_TrxName());
		setClientOrg(project);
		setC_Project_ID(project.getC_Project_ID());
	}	//	MProjectPhase

	/**
	 * 	Copy Constructor
	 *	@param project parent
	 *	@param phase copy
	 */
	public MProjectPhase (MProject project, MProjectTypePhase phase)
	{
		this (project);
		//
		setC_Phase_ID (phase.getC_Phase_ID());			//	FK
		setName (phase.getName());
		setSeqNo (phase.getSeqNo());
		setDescription(phase.getDescription());
		setHelp(phase.getHelp());
		if (phase.getM_Product_ID() != 0)
			setM_Product_ID(phase.getM_Product_ID());
		setQty(phase.getStandardQty());
	}	//	MProjectPhase

	/**
	 * 	Get Project Phase Tasks.
	 *	@return Array of tasks
	 */
	public MProjectTask[] getTasks()
	{
		ArrayList<MProjectTask> list = new ArrayList<MProjectTask>();
		String sql = "SELECT * FROM C_ProjectTask WHERE C_ProjectPhase_ID=? ORDER BY SeqNo";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getC_ProjectPhase_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MProjectTask (getCtx(), rs, get_TrxName()));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, sql, ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		//
		MProjectTask[] retValue = new MProjectTask[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getTasks


	/**
	 * 	Copy Tasks from other Phase
	 *	@param fromPhase from phase
	 *	@return number of tasks copied
	 */
	public int copyTasksFrom (MProjectPhase fromPhase)
	{
		if (fromPhase == null)
			return 0;
		int count = 0;
		//
		MProjectTask[] myTasks = getTasks();
		MProjectTask[] fromTasks = fromPhase.getTasks();
		//	Copy Project Tasks
		for (int i = 0; i < fromTasks.length; i++)
		{
			//	Check if Task already exists
			int C_Task_ID = fromTasks[i].getC_Task_ID();
			boolean exists = false;
			if (C_Task_ID == 0)
				exists = false;
			else
			{
				for (int ii = 0; ii < myTasks.length; ii++)
				{
					if (myTasks[ii].getC_Task_ID() == C_Task_ID)
					{
						exists = true;
						break;
					}
				}
			}
			//	Phase exist
			if (exists)
				log.info("Task already exists here, ignored - " + fromTasks[i]);
			else
			{
				MProjectTask toTask = new MProjectTask (getCtx (), 0, get_TrxName());
				PO.copyValues (fromTasks[i], toTask, getAD_Client_ID (), getAD_Org_ID ());
				toTask.setC_ProjectPhase_ID (getC_ProjectPhase_ID ());
				if (toTask.save ())
					count++;
			}
		}
		if (fromTasks.length != count)
			log.warning("Count difference - ProjectPhase=" + fromTasks.length + " <> Saved=" + count);

		return count;
	}	//	copyTasksFrom

	/**
	 * 	Copy Tasks from other Phase
	 *	@param fromPhase from phase
	 *	@return number of tasks copied
	 */
	public int copyTasksFrom (MProjectTypePhase fromPhase)
	{
		if (fromPhase == null)
			return 0;
		int count = 0;
		//	Copy Type Tasks
		MProjectTypeTask[] fromTasks = fromPhase.getTasks();
		for (int i = 0; i < fromTasks.length; i++)
		{
			MProjectTask toTask = new MProjectTask (this, fromTasks[i]);
			if (toTask.save())
				count++;
		}
		log.fine("#" + count + " - " + fromPhase);
		if (fromTasks.length != count)
			log.log(Level.SEVERE, "Count difference - TypePhase=" + fromTasks.length + " <> Saved=" + count);

		return count;
	}	//	copyTasksFrom

}	//	MProjectPhase
