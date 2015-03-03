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
 *	Menu Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MMenu.java,v 1.7 2005/10/26 00:38:15 jjanke Exp $
 */
public class MMenu extends X_AD_Menu
{

	/**
	 * 	Get menues with where clause
	 *	@param ctx context
	 *	@param whereClause where clause w/o the actual WHERE
	 *	@return MMenu
	 */
	public static MMenu[] get (Properties ctx, String whereClause)
	{
		String sql = "SELECT * FROM AD_Menu";
		if (whereClause != null && whereClause.length() > 0)
			sql += " WHERE " + whereClause;
		ArrayList<MMenu> list = new ArrayList<MMenu>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MMenu (ctx, rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
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
		MMenu[] retValue = new MMenu[list.size()];
		list.toArray (retValue);
		return retValue;
	}	//	get
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MMenu.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Menu_ID id
	 */
	public MMenu (Properties ctx, int AD_Menu_ID, String trxName)
	{
		super (ctx, AD_Menu_ID, trxName);
		if (AD_Menu_ID == 0)
		{
			setEntityType (ENTITYTYPE_UserMaintained);	// U
			setIsReadOnly (false);	// N
			setIsSOTrx (false);
			setIsSummary (false);
		//	setName (null);
		}
	}	//	MMenu

	/**
	 * 	MMenu
	 *	@param ctx
	 *	@param rs
	 */
	public MMenu (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MMenu

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Reset info
		if (isSummary() && getAction() != null)
			setAction(null);
		String action = getAction();
		if (action == null)
			action = "";
		//	Clean up references
		if (getAD_Window_ID() != 0 && !action.equals(ACTION_Window))
			setAD_Window_ID(0);
		if (getAD_Form_ID() != 0 && !action.equals(ACTION_Form))
			setAD_Form_ID(0);
		if (getAD_Workflow_ID() != 0 && !action.equals(ACTION_WorkFlow))
			setAD_Workflow_ID(0);
		if (getAD_Workbench_ID() != 0 && !action.equals(ACTION_Workbench))
			setAD_Workbench_ID(0);
		if (getAD_Task_ID() != 0 && !action.equals(ACTION_Task))
			setAD_Task_ID(0);
		if (getAD_Process_ID() != 0 
			&& !(action.equals(ACTION_Process) || action.equals(ACTION_Report)))
			setAD_Process_ID(0);
		return true;
	}	//	beforeSave
	
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord)
			insert_Tree(MTree_Base.TREETYPE_Menu);
		return success;
	}	//	afterSave

	/**
	 * 	After Delete
	 *	@param success
	 *	@return deleted
	 */
	protected boolean afterDelete (boolean success)
	{
		if (success)
			delete_Tree(MTree_Base.TREETYPE_Menu);
		return success;
	}	//	afterDelete
	
}	//	MMenu
