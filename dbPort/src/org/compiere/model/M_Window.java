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

import java.awt.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Window Model
 *	
 *  @author Jorg Janke
 *  @version $Id: M_Window.java,v 1.11 2005/11/21 23:48:44 jjanke Exp $
 */
public class M_Window extends X_AD_Window
{
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (M_Window.class);
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Window_ID
	 */
	public M_Window (Properties ctx, int AD_Window_ID, String trxName)
	{
		super (ctx, AD_Window_ID, trxName);
		if (AD_Window_ID == 0)
		{
			setWindowType (WINDOWTYPE_Maintain);	// M
			setEntityType (ENTITYTYPE_UserMaintained);	// U
			setIsBetaFunctionality (false);
			setIsDefault (false);
			setIsSOTrx (true);	// Y
		}	}	//	M_Window

	/**
	 * 	Koad Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public M_Window (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	M_Window
	
	/**
	 * 	Set Window Size
	 *	@param size size
	 */
	public void setWindowSize (Dimension size)
	{
		if (size != null)
		{
			setWinWidth(size.width);
			setWinHeight(size.height);
		}
	}	//	setWindowSize
	
	/**	The Lines						*/
	private M_Tab[]		m_tabs	= null;

	/**
	 * 	Get Fields
	 *	@param reload reload data
	 *	@return array of lines
	 */
	public M_Tab[] getTabs (boolean reload, String trxName)
	{
		if (m_tabs != null && !reload)
			return m_tabs;
		String sql = "SELECT * FROM AD_Tab WHERE AD_Window_ID=? ORDER BY SeqNo";
		ArrayList<M_Tab> list = new ArrayList<M_Tab>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, getAD_Window_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new M_Tab (getCtx(), rs, trxName));
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
		//
		m_tabs = new M_Tab[list.size ()];
		list.toArray (m_tabs);
		return m_tabs;
	}	//	getFields

	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord)	//	Add to all automatic roles
		{
			MRole[] roles = MRole.getOf(getCtx(), "IsManual='N'");
			for (int i = 0; i < roles.length; i++)
			{
				MWindowAccess wa = new MWindowAccess(this, roles[i].getAD_Role_ID());
				wa.save();
			}
		}
		//	Menu/Workflow
		else if (is_ValueChanged("IsActive") || is_ValueChanged("Name") 
			|| is_ValueChanged("Description") || is_ValueChanged("Help"))
		{
			MMenu[] menues = MMenu.get(getCtx(), "AD_Window_ID=" + getAD_Window_ID());
			for (int i = 0; i < menues.length; i++)
			{
				menues[i].setName(getName());
				menues[i].setDescription(getDescription());
				menues[i].setIsActive(isActive());
				menues[i].save();
			}
			//
			X_AD_WF_Node[] nodes = getWFNodes(getCtx(), "AD_Window_ID=" + getAD_Window_ID());
			for (int i = 0; i < nodes.length; i++)
			{
				boolean changed = false;
				if (nodes[i].isActive() != isActive())
				{
					nodes[i].setIsActive(isActive());
					changed = true;
				}
				if (nodes[i].isCentrallyMaintained())
				{
					nodes[i].setName(getName());
					nodes[i].setDescription(getDescription());
					nodes[i].setHelp(getHelp());
					changed = true;
				}
				if (changed)
					nodes[i].save();
			}
		}
		return success;
	}	//	afterSave

	
	/**
	 * 	Get workflow nodes with where clause.
	 * 	Is here as MWFNode is in base
	 *	@param ctx context
	 *	@param whereClause where clause w/o the actual WHERE
	 *	@return nodes
	 */
	public static X_AD_WF_Node[] getWFNodes (Properties ctx, String whereClause)
	{
		String sql = "SELECT * FROM AD_WF_Node";
		if (whereClause != null && whereClause.length() > 0)
			sql += " WHERE " + whereClause;
		ArrayList<X_AD_WF_Node> list = new ArrayList<X_AD_WF_Node>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new X_AD_WF_Node (ctx, rs, null));
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
		X_AD_WF_Node[] retValue = new X_AD_WF_Node[list.size()];
		list.toArray (retValue);
		return retValue;
	}	//	getWFNode
	
}	//	M_Window
