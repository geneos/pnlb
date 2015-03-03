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
 * 	Project Type Model
 *
 *	@author Jorg Janke
 *	@version $Id: MProjectType.java,v 1.7 2005/10/14 00:44:31 jjanke Exp $
 */
public class MProjectType extends X_C_ProjectType
{
	/**
	 * 	Standrad Constructor
	 *	@param ctx context
	 *	@param C_ProjectType_ID id
	 *	@param trxName trx
	 */
	public MProjectType (Properties ctx, int C_ProjectType_ID, String trxName)
	{
		super (ctx, C_ProjectType_ID, trxName);
		/**
		if (C_ProjectType_ID == 0)
		{
			setC_ProjectType_ID (0);
			setName (null);
		}
		**/
	}	//	MProjectType

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MProjectType (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MProjectType

	/**
	 * 	String Representation
	 *	@return	info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MProjectType[").append(get_ID())
			.append("-").append(getName())
			.append("]");
		return sb.toString();
	}	//	toString

	
	/**************************************************************************
	 * 	Get Project Type Phases
	 *	@return Array of phases
	 */
	public MProjectTypePhase[] getPhases()
	{
		ArrayList<MProjectTypePhase> list = new ArrayList<MProjectTypePhase>();
		String sql = "SELECT * FROM C_Phase WHERE C_ProjectType_ID=? ORDER BY SeqNo";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getC_ProjectType_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MProjectTypePhase (getCtx(), rs, get_TrxName()));
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
		MProjectTypePhase[] retValue = new MProjectTypePhase[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getPhases


}	//	MProjectType
