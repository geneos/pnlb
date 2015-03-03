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
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import org.compiere.util.*;

/**
 *  Workbench Model
 *
 *  @author Jorg Janke
 *  @version $Id: MWorkbench.java,v 1.11 2005/11/14 02:10:52 jjanke Exp $
 */
public class MWorkbench implements Serializable
{
	/**
	 *  Workbench Model Constructor
	 */
	public MWorkbench (Properties ctx)
	{
		m_ctx = ctx;
	}   //  MWorkbench

	/**
	 *  No Workbench - Just Frame for Window
	 */
	public MWorkbench (Properties ctx, int AD_Window_ID)
	{
		m_ctx = ctx;
		m_windows.add (new WBWindow(TYPE_WINDOW, AD_Window_ID));
	}   //  MWorkbench

	/** Properties      */
	private Properties  m_ctx;

	/** List of windows */
	private ArrayList<WBWindow>   m_windows = new ArrayList<WBWindow>();

	private int         AD_Workbench_ID = 0;
	private String      Name = "";
	private String      Description = "";
	private String      Help = "";
	private int         AD_Column_ID = 0;
	private int         AD_Image_ID = 0;
	private int         AD_Color_ID = 0;
	private int         PA_Goal_ID = 0;
	private String      ColumnName = "";

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(MWorkbench.class);
	
	/**
	 *  Init Workbench
	 */
	public boolean initWorkbench (int ad_Workbench_ID)
	{
		AD_Workbench_ID = ad_Workbench_ID;
		//  Get WB info
		String sql = null;
		if (Env.isBaseLanguage(m_ctx, "AD_Workbench"))
			sql = "SELECT w.Name,w.Description,w.Help,"                         //  1..3
				+ " w.AD_Column_ID,w.AD_Image_ID,w.AD_Color_ID,w.PA_Goal_ID,"   //  4..7
				+ " c.ColumnName "                                              //  8
				+ "FROM AD_Workbench w, AD_Column c "
				+ "WHERE w.AD_Workbench_ID=?"                   //  #1
				+ " AND w.IsActive='Y'"
				+ " AND w.AD_Column_ID=c.AD_Column_ID";
		else
			sql = "SELECT t.Name,t.Description,t.Help,"
				+ " w.AD_Column_ID,w.AD_Image_ID,w.AD_Color_ID,w.PA_Goal_ID,"
				+ " c.ColumnName "
				+ "FROM AD_Workbench w, AD_Workbench_Trl t, AD_Column c "
				+ "WHERE w.AD_Workbench_ID=?"                   //  #1
				+ " AND w.IsActive='Y'"
				+ " AND w.AD_Workbench_ID=t.AD_Workbench_ID"
				+ " AND t.AD_Language='" + Env.getAD_Language(m_ctx) + "'"
				+ " AND w.AD_Column_ID=c.AD_Column_ID";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Workbench_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				Name = rs.getString(1);
				Description = rs.getString(2);
				if (Description == null)
					Description = "";
				Help = rs.getString(3);
				if (Help == null)
					Help = "";
				//
				AD_Column_ID = rs.getInt(4);
				AD_Image_ID = rs.getInt(5);
				AD_Color_ID = rs.getInt(6);
				PA_Goal_ID = rs.getInt(7);
				ColumnName = rs.getString(8);
			}
			else
				AD_Workbench_ID = 0;
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}

		if (AD_Workbench_ID == 0)
			return false;
		return initWorkbenchWindows();
	}   //  initWorkbench

	/**
	 *  String Representation
	 */
	public String toString()
	{
		return "MWorkbench ID=" + AD_Workbench_ID + " " + Name
			+ ", windows=" + m_windows.size() + ", LinkColumn=" + ColumnName;
	}   //  toString

	/**
	 *  Dispose
	 */
	public void dispose()
	{
		for (int i = 0; i < m_windows.size(); i++)
		{
			dispose(i);
		}
		m_windows.clear();
		m_windows = null;
	}   //  dispose

	/**
	 *  Get Workbench Query
	 *  ColumnName=@#ColumnName@
	 */
	public MQuery getQuery()
	{
		return MQuery.getEqualQuery(ColumnName, "@#" + ColumnName + "@");
	}   //  getQuery

	/*************************************************************************/

	public int getAD_Workbench_ID()
	{
		return AD_Workbench_ID;
	}
	public String getName()
	{
		return Name;
	}
	public String getDescription()
	{
		return Description;
	}
	public String getHelp()
	{
		return Help;
	}
	public int getAD_Column_ID()
	{
		return AD_Column_ID;
	}
	public int getAD_Image_ID()
	{
		return AD_Image_ID;
	}
	public int getAD_Color_ID()
	{
		return AD_Color_ID;
	}
	public int getPA_Goal_ID()
	{
		return PA_Goal_ID;
	}

	/*************************************************************************/

	/** Window          */
	public static final int     TYPE_WINDOW = 1;
	/** Form            */
	public static final int     TYPE_FORM = 2;
	/** Process         */
	public static final int     TYPE_PROCESS = 3;
	/** Task            */
	public static final int     TYPE_TASK = 4;

	/**
	 *  Init Workbench Windows
	 */
	private boolean initWorkbenchWindows()
	{
		String sql = "SELECT AD_Window_ID, AD_Form_ID, AD_Process_ID, AD_Task_ID "
			+ "FROM AD_WorkbenchWindow "
			+ "WHERE AD_Workbench_ID=? AND IsActive='Y'"
			+ "ORDER BY SeqNo";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Workbench_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				int AD_Window_ID = rs.getInt(1);
				int AD_Form_ID = rs.getInt(2);
				int AD_Process_ID = rs.getInt(3);
				int AD_Task_ID = rs.getInt(4);
				//
				if (AD_Window_ID > 0)
					m_windows.add (new WBWindow(TYPE_WINDOW, AD_Window_ID));
				else if (AD_Form_ID > 0)
					m_windows.add (new WBWindow(TYPE_FORM, AD_Form_ID));
				else if (AD_Process_ID > 0)
					m_windows.add (new WBWindow(TYPE_PROCESS, AD_Process_ID));
				else if (AD_Task_ID > 0)
					m_windows.add (new WBWindow(TYPE_TASK, AD_Task_ID));
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return false;
		}
		return true;
	}   //  initWorkbenchWindows

	/**
	 *  Get Window Count
	 */
	public int getWindowCount()
	{
		return m_windows.size();
	}   //  getWindowCount

	/**
	 *  Get Window Type of Window
	 *  @return -1 if not valid
	 */
	public int getWindowType (int index)
	{
		if (index < 0 || index > m_windows.size())
			return -1;
		WBWindow win = (WBWindow)m_windows.get(index);
		return win.Type;
	}   //  getWindowType

	/**
	 *  Get ID for Window
	 *  @return -1 if not valid
	 */
	public int getWindowID (int index)
	{
		if (index < 0 || index > m_windows.size())
			return -1;
		WBWindow win = (WBWindow)m_windows.get(index);
		return win.ID;
	}   //  getWindowID

	
	/**************************************************************************
	 *  Set Window Model of Window
	 */
	public void setMWindow (int index, MWindow mw)
	{
		if (index < 0 || index > m_windows.size())
			throw new IllegalArgumentException ("Index invalid: " + index);
		WBWindow win = (WBWindow)m_windows.get(index);
		if (win.Type != TYPE_WINDOW)
			throw new IllegalArgumentException ("Not a MWindow: " + index);
		win.mWindow = mw;
	}   //  setMWindow

	/**
	 *  Get Window Model of Window
	 */
	public MWindow getMWindow (int index)
	{
		if (index < 0 || index > m_windows.size())
			throw new IllegalArgumentException ("Index invalid: " + index);
		WBWindow win = (WBWindow)m_windows.get(index);
		if (win.Type != TYPE_WINDOW)
			throw new IllegalArgumentException ("Not a MWindow: " + index);
		return win.mWindow;
	}   //  getMWindow

	/**
	 *  Get Name of Window
	 *  @return Window Name or null if not set
	 */
	public String getName (int index)
	{
		if (index < 0 || index > m_windows.size())
			throw new IllegalArgumentException ("Index invalid: " + index);
		WBWindow win = (WBWindow)m_windows.get(index);
		if (win.mWindow != null && win.Type == TYPE_WINDOW)
			return win.mWindow.getName();
		return null;
	}   //  getName

	/**
	 *  Get Description of Window
	 *  @return Window Description or null if not set
	 */
	public String getDescription (int index)
	{
		if (index < 0 || index > m_windows.size())
			throw new IllegalArgumentException ("Index invalid: " + index);
		WBWindow win = (WBWindow)m_windows.get(index);
		if (win.mWindow != null && win.Type == TYPE_WINDOW)
			return win.mWindow.getDescription();
		return null;
	}   //  getDescription

	/**
	 *  Get Help of Window
	 *  @return Window Help or null if not set
	 */
	public String getHelp (int index)
	{
		if (index < 0 || index > m_windows.size())
			throw new IllegalArgumentException ("Index invalid: " + index);
		WBWindow win = (WBWindow)m_windows.get(index);
		if (win.mWindow != null && win.Type == TYPE_WINDOW)
			return win.mWindow.getHelp();
		return null;
	}   //  getHelp

	/**
	 *  Get Icon of Window
	 *  @return Window Icon or null if not set
	 */
	public Icon getIcon (int index)
	{
		if (index < 0 || index > m_windows.size())
			throw new IllegalArgumentException ("Index invalid: " + index);
		WBWindow win = (WBWindow)m_windows.get(index);
		if (win.mWindow != null && win.Type == TYPE_WINDOW)
			return win.mWindow.getIcon();
		return null;
	}   //  getIcon

	/**
	 *  Get Image Icon of Window
	 *  @return Window Icon or null if not set
	 */
	public Image getImage (int index)
	{
		if (index < 0 || index > m_windows.size())
			throw new IllegalArgumentException ("Index invalid: " + index);
		WBWindow win = (WBWindow)m_windows.get(index);
		if (win.mWindow != null && win.Type == TYPE_WINDOW)
			return win.mWindow.getImage();
		return null;
	}   //  getImage

	/**
	 *  Get AD_Color_ID of Window
	 *  @return Window Color or Workbench color if not set
	 */
	public int getAD_Color_ID (int index)
	{
		if (index < 0 || index > m_windows.size())
			throw new IllegalArgumentException ("Index invalid: " + index);
		WBWindow win = (WBWindow)m_windows.get(index);
		int retValue = -1;
	//	if (win.mWindow != null && win.Type == TYPE_WINDOW)
	//		return win.mWindow.getAD_Color_ID();
		if (retValue == -1)
			return getAD_Color_ID();
		return retValue;
	}   //  getAD_Color_ID

	/**
	 *  Set WindowNo of Window
	 */
	public void setWindowNo (int index, int windowNo)
	{
		if (index < 0 || index > m_windows.size())
			throw new IllegalArgumentException ("Index invalid: " + index);
		WBWindow win = (WBWindow)m_windows.get(index);
		win.WindowNo = windowNo;
	}   //  getWindowNo

	/**
	 *  Get WindowNo of Window
	 *  @return WindowNo of Window if previously set, otherwise -1;
	 */
	public int getWindowNo (int index)
	{
		if (index < 0 || index > m_windows.size())
			throw new IllegalArgumentException ("Index invalid: " + index);
		WBWindow win = (WBWindow)m_windows.get(index);
		return win.WindowNo;
	}   //  getWindowNo

	/**
	 *  Dispose of Window
	 */
	public void dispose (int index)
	{
		if (index < 0 || index > m_windows.size())
			throw new IllegalArgumentException ("Index invalid: " + index);
		WBWindow win = (WBWindow)m_windows.get(index);
		if (win.mWindow != null)
			win.mWindow.dispose();
		win.mWindow = null;
	}   //  dispose

	/**
	 * 	Get Window Size
	 *	@return window size or null if not set
	 */
	public Dimension getWindowSize()
	{
		return null;
	}	//	getWindowSize
	
	
	/**************************************************************************
	 *  Window Type
	 */
	class WBWindow
	{
		public WBWindow (int type, int id)
		{
			Type = type;
			ID = id;
		}
		public int      Type = 0;
		public int      ID = 0;
		public int      WindowNo = -1;
		//
		public MWindow  mWindow = null;
	//	public MFrame   mFrame = null;
	//	public MProcess mProcess = null;
	}   //  WBWindow
	
}   //  Workbench
