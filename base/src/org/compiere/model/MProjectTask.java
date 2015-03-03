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
import org.compiere.util.*;

/**
 * 	Project Phase Task Model
 *
 *	@author Jorg Janke
 *	@version $Id: MProjectTask.java,v 1.6 2005/03/11 20:26:05 jjanke Exp $
 */
public class MProjectTask extends X_C_ProjectTask
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_ProjectTask_ID id
	 */
	public MProjectTask (Properties ctx, int C_ProjectTask_ID, String trxName)
	{
		super (ctx, C_ProjectTask_ID, trxName);
		if (C_ProjectTask_ID == 0)
		{
		//	setC_ProjectTask_ID (0);	//	PK
		//	setC_ProjectPhase_ID (0);	//	Parent
		//	setC_Task_ID (0);			//	FK
			setSeqNo (0);
		//	setName (null);
			setQty (Env.ZERO);
		}
	}	//	MProjectTask

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MProjectTask (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MProjectTask

	/**
	 * 	Parent Constructor
	 * 	@param phase parent
	 */
	public MProjectTask (MProjectPhase phase)
	{
		this (phase.getCtx(), 0, phase.get_TrxName());
		setClientOrg(phase);
		setC_ProjectPhase_ID(phase.getC_ProjectPhase_ID());
	}	//	MProjectTask

	/**
	 * 	Copy Constructor
	 *	@param phase parent
	 *	@param task type copy
	 */
	public MProjectTask (MProjectPhase phase, MProjectTypeTask task)
	{
		this (phase);
		//
		setC_Task_ID (task.getC_Task_ID());			//	FK
		setSeqNo (task.getSeqNo());
		setName (task.getName());
		setDescription(task.getDescription());
		setHelp(task.getHelp());
		if (task.getM_Product_ID() != 0)
			setM_Product_ID(task.getM_Product_ID());
		setQty(task.getStandardQty());
	}	//	MProjectTask

}	//	MProjectTask
