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
 * 	Project Type Phase Model
 *
 *	@author Jorg Janke
 *	@version $Id: MProjectTypePhase.java,v 1.7 2005/10/14 00:44:31 jjanke Exp $
 */
public class MProjectTypePhase extends X_C_Phase
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Phase_ID id
	 *	@param trxName trx
	 */
	public MProjectTypePhase (Properties ctx, int C_Phase_ID, String trxName)
	{
		super (ctx, C_Phase_ID, trxName);
		if (C_Phase_ID == 0)
		{
		//	setC_Phase_ID (0);			//	PK
		//	setC_ProjectType_ID (0);	//	Parent
		//	setName (null);
			setSeqNo (0);
			setStandardQty (Env.ZERO);
		}
	}	//	MProjectTypePhase

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName
	 */
	public MProjectTypePhase (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MProjectTypePhase

	/**
	 * 	Get Project Type Phases
	 *	@return Array of phases
	 */
	public MProjectTypeTask[] getTasks()
	{
		ArrayList<MProjectTypeTask> list = new ArrayList<MProjectTypeTask>();
		String sql = "SELECT * FROM C_Task WHERE C_Phase_ID=? ORDER BY SeqNo";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getC_Phase_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MProjectTypeTask (getCtx(), rs, get_TrxName()));
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
		MProjectTypeTask[] retValue = new MProjectTypeTask[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getPhases

}	//	MProjectTypePhase
