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

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Model Window Value Object
 *
 *  @author Jorg Janke
 *  @version  $Id: MWindowVO.java,v 1.15 2005/12/09 05:19:09 jjanke Exp $
 */
public class MWindowVO implements Serializable
{
	/**
	 *  Create Window Value Object
	 *
	 *  @param ctx context
	 *  @param WindowNo window no
	 *  @param AD_Window_ID window id
	 *  @return MWindowVO
	 */
	public static MWindowVO create (Properties ctx, int WindowNo, int AD_Window_ID)
	{
		return create (ctx, WindowNo, AD_Window_ID, 0);
	}   //  create

	/**
	 *  Create Window Value Object
	 *
	 *  @param ctx context
	 *  @param WindowNo window no
	 *  @param AD_Window_ID window id
	 *  @param AD_Menu_ID menu id
	 *  @return MWindowVO
	 */
	public static MWindowVO create (Properties ctx, int WindowNo, int AD_Window_ID, int AD_Menu_ID)
	{
		CLogger.get().config("#" + WindowNo
			+ " - AD_Window_ID=" + AD_Window_ID + "; AD_Menu_ID=" + AD_Menu_ID);
		MWindowVO vo = new MWindowVO (ctx, WindowNo);
		vo.AD_Window_ID = AD_Window_ID;

		//  Get Window_ID if required	- (used by HTML UI)
		if (vo.AD_Window_ID == 0 && AD_Menu_ID != 0)
		{
			String sql = "SELECT AD_Window_ID, IsSOTrx, IsReadOnly FROM AD_Menu "
				+ "WHERE AD_Menu_ID=? AND Action='W'";
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt(1, AD_Menu_ID);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
				{
					vo.AD_Window_ID = rs.getInt(1);
					String IsSOTrx = rs.getString(2);
					Env.setContext(ctx, WindowNo, "IsSOTrx", (IsSOTrx != null && IsSOTrx.equals("Y")));
					//
					String IsReadOnly = rs.getString(3);
					if (IsReadOnly != null && IsReadOnly.equals("Y"))
						vo.IsReadWrite = "Y";
					else
						vo.IsReadWrite = "N";
				}
				rs.close();
				pstmt.close();
			}
			catch (SQLException e)
			{
				CLogger.get().log(Level.SEVERE, "Menu", e);
				return null;
			}
			CLogger.get().config("AD_Window_ID=" + vo.AD_Window_ID);
		}

		//  --  Get Window

		StringBuffer sql = new StringBuffer("SELECT Name,Description,Help,WindowType, "
			+ "AD_Color_ID,AD_Image_ID, a.IsReadWrite, WinHeight,WinWidth, "
			+ "IsSOTrx ");

		if (Env.isBaseLanguage(vo.ctx, "AD_Window"))
			sql.append("FROM AD_Window w, AD_Window_Access a "
				+ "WHERE w.AD_Window_ID=?"
				+ " AND w.AD_Window_ID=a.AD_Window_ID AND a.AD_Role_ID=?"
				+ " AND w.IsActive='Y' AND a.IsActive='Y'");
		else
			sql.append("FROM AD_Window_vt w, AD_Window_Access a "
				+ "WHERE w.AD_Window_ID=?"
				+ " AND w.AD_Window_ID=a.AD_Window_ID AND a.AD_Role_ID=?"
				+ " AND a.IsActive='Y'")
				.append(" AND AD_Language='")
				.append(Env.getAD_Language(vo.ctx)).append("'");

		int AD_Role_ID = Env.getContextAsInt(vo.ctx, "#AD_Role_ID");
		try
		{
			//	create statement
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, vo.AD_Window_ID);
			pstmt.setInt(2, AD_Role_ID);
			// 	get data
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				vo.Name = rs.getString(1);
				vo.Description = rs.getString(2);
				if (vo.Description == null)
					vo.Description = "";
				vo.Help = rs.getString(3);
				if (vo.Help == null)
					vo.Help = "";
				vo.WindowType = rs.getString(4);
				//
				vo.AD_Color_ID = rs.getInt(5);
				vo.AD_Image_ID = rs.getInt(6);
				vo.IsReadWrite = rs.getString(7);
				//
				vo.WinHeight = rs.getInt(8);
				vo.WinWidth = rs.getInt(9);
				//
				vo.IsSOTrx = "Y".equals(rs.getString(10));
			}
			else
				vo = null;
			rs.close();
			pstmt.close();
		}
		catch (SQLException ex)
		{
			CLogger.get().log(Level.SEVERE, sql.toString(), ex);
			return null;
		}
		//	Not found
		if (vo == null)
		{
			CLogger.get().log(Level.SEVERE, "No Window - AD_Window_ID=" + AD_Window_ID
				+ ", AD_Role_ID=" + AD_Role_ID + " - " + sql);
			CLogger.get().saveError("AccessTableNoView", "(Not found)");
			return null;
		}
		//	Read Write
		if (vo.IsReadWrite == null)
		{
			CLogger.get().saveError("AccessTableNoView", "(found)");
			return null;
		}

		//  Create Tabs
		createTabs (vo);
		if (vo.Tabs == null || vo.Tabs.size() == 0)
			return null;

		return vo;
	}   //  create

	/**
	 *  Create Window Tabs
	 *  @param mWindowVO Window Value Object
	 *  @return true if tabs were created
	 */
	private static boolean createTabs (MWindowVO mWindowVO)
	{
		mWindowVO.Tabs = new ArrayList<MTabVO>();

		String sql = MTabVO.getSQL(mWindowVO.ctx);
		int TabNo = 0;
		int AD_Table_ID = 0;
		try
		{
			//	create statement
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, mWindowVO.AD_Window_ID);
			ResultSet rs = pstmt.executeQuery();
			boolean firstTab = true;
			while (rs.next())
			{
				if (AD_Table_ID == 0)
					AD_Table_ID = rs.getInt("AD_Table_ID");
				//  Create TabVO
				MTabVO mTabVO = MTabVO.create(mWindowVO, TabNo, rs,
					mWindowVO.WindowType.equals(WINDOWTYPE_QUERY),  //  isRO
					mWindowVO.WindowType.equals(WINDOWTYPE_TRX));   //  onlyCurrentRows
				if (mTabVO == null && firstTab)
					break;		//	don't continue if first tab is null
				if (mTabVO != null)
				{
					if (!mTabVO.IsReadOnly && "N".equals(mWindowVO.IsReadWrite))
						mTabVO.IsReadOnly = true;
					mWindowVO.Tabs.add(mTabVO);
					TabNo++;        //  must be same as mWindow.getTab(x)
					firstTab = false;
				}
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			CLogger.get().log(Level.SEVERE, "createTabs", e);
			return false;
		}

		//  No Tabs
		if (TabNo == 0 || mWindowVO.Tabs.size() == 0)
		{
			CLogger.get().log(Level.SEVERE, "No Tabs - AD_Window_ID=" 
				+ mWindowVO.AD_Window_ID + " - " + sql);
			return false;
		}

		//	Put base table of window in ctx (for VDocAction)
		Env.setContext(mWindowVO.ctx, mWindowVO.WindowNo, "BaseTable_ID", AD_Table_ID);
		return true;
	}   //  createTabs

	
	/**************************************************************************
	 *  Private Constructor
	 *  @param ctx context
	 *  @param WindowNo window no
	 */
	private MWindowVO (Properties ctx, int WindowNo)
	{
		this.ctx = ctx;
		this.WindowNo = WindowNo;
	}   //  MWindowVO

	static final long serialVersionUID = 3802628212531678981L;

	/** Properties      */
	public Properties   ctx;

	public int 		    WindowNo;

	//	Database fields
	public	int			AD_Window_ID = 0;
	public	String		Name = "";
	public	String		Description = "";
	public	String		Help = "";
	public	String		WindowType = "";
	public int          AD_Image_ID = 0;
	public int          AD_Color_ID = 0;
	public String		IsReadWrite = null;
	public int			WinWidth = 0;
	public int			WinHeight = 0;
	public boolean		IsSOTrx = false;

	/** Tabs contains MTabVO elements   */
	public ArrayList<MTabVO>	Tabs = null;

	public static final String	WINDOWTYPE_QUERY = "Q";
	public static final String	WINDOWTYPE_TRX = "T";
	public static final String	WINDOWTYPE_MMAINTAIN = "M";

	/**
	 *  Set Context including contained elements
	 *  @param newCtx context
	 */
	public void setCtx (Properties newCtx)
	{
		ctx = newCtx;
		for (int i = 0; i < Tabs.size() ; i++)
		{
			MTabVO tab = (MTabVO)Tabs.get(i);
			tab.setCtx(newCtx);
		}
	}   //  setCtx

	/**
	 * 	Clone
	 * 	@param WindowNo no
	 *	@return WindowVO
	 */
	public MWindowVO clone (int WindowNo)
	{
		MWindowVO clone = null;
		try
		{
			clone = new MWindowVO((Properties)ctx.clone(), WindowNo);
			clone.AD_Window_ID = AD_Window_ID;
			clone.Name = Name;
			clone.Description = Description;
			clone.Help = Help;
			clone.WindowType = WindowType;
			clone.AD_Image_ID = AD_Image_ID;
			clone.AD_Color_ID = AD_Color_ID;
			clone.IsReadWrite = IsReadWrite;
			clone.WinWidth = WinWidth;
			clone.WinHeight = WinHeight;
			clone.IsSOTrx = IsSOTrx;
			//
			clone.Tabs = new ArrayList<MTabVO>();
			for (int i = 0; i < Tabs.size(); i++)
			{
				MTabVO tab = Tabs.get(i);
				tab = tab.clone(clone.ctx, WindowNo);
				if (tab == null)
					return null;
				clone.Tabs.add(tab);
			}
		}
		catch (Exception e)
		{
			clone = null;
		}
		return clone;
	}	//	clone

}   //  MWindowVO

