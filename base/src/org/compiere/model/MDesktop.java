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
 *  Desktop Model
 *
 *  @author Jorg Janke
 *  @version $Id: MDesktop.java,v 1.10 2005/11/06 01:17:27 jjanke Exp $
 */
public class MDesktop
{
	/**
	 *  Desktop Model
	 */
	public MDesktop(Properties ctx)
	{
		m_ctx = ctx;
	}   //  MDesktop

	/** Properties      */
	private Properties  m_ctx;

	/** List of workbenches */
	private ArrayList<Integer>   m_workbenches = new ArrayList<Integer>();

	private int         AD_Desktop_ID;
	private String      Name;
	private String      Description;
	private String      Help;
	private int         AD_Column_ID;
	private int         AD_Image_ID;
	private int         AD_Color_ID;
	private int         PA_Goal_ID;

	/**	Logger			*/
	private CLogger	log = CLogger.getCLogger(getClass());
	
	/**
	 *  Init Desktop
	 */
	public boolean initDesktop (int ad_Desktop_ID)
	{
		AD_Desktop_ID = ad_Desktop_ID;
		//  Get WB info
		String sql = null;
		if (Env.isBaseLanguage(m_ctx, "AD_Desktop"))
			sql = "SELECT Name,Description,Help,"                       //  1..3
				+ " AD_Column_ID,AD_Image_ID,AD_Color_ID,PA_Goal_ID "  //   4..7
				+ "FROM AD_Desktop "
				+ "WHERE AD_Desktop_ID=? AND IsActive='Y'";
		else
			sql = "SELECT t.Name,t.Description,t.Help,"
				+ " w.AD_Column_ID,w.AD_Image_ID,w.AD_Color_ID,w.PA_Goal_ID "
				+ "FROM AD_Desktop w, AD_Desktop_Trl t "
				+ "WHERE w.AD_Desktop_ID=? AND w.IsActive='Y'"
				+ " AND w.AD_Desktop_ID=t.AD_Desktop_ID"
				+ " AND t.AD_Language='" + Env.getAD_Language(m_ctx) + "'";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Desktop_ID);
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
			}
			else
				AD_Desktop_ID = 0;
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}

		if (AD_Desktop_ID == 0)
			return false;
		return initDesktopWorkbenches();
	}   //  initDesktop

	/**
	 *  String Representation
	 */
	public String toString()
	{
		return "MDesktop ID=" + AD_Desktop_ID + " " + Name;
	}

	/*************************************************************************/

	public int getAD_Desktop_ID()
	{
		return AD_Desktop_ID;
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

	/**
	 *  Init Workbench Windows
	 */
	private boolean initDesktopWorkbenches()
	{
		String sql = "SELECT AD_Workbench_ID "
			+ "FROM AD_DesktopWorkbench "
			+ "WHERE AD_Desktop_ID=? AND IsActive='Y' "
			+ "ORDER BY SeqNo";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Desktop_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				int AD_Workbench_ID = rs.getInt(1);
				m_workbenches.add (new Integer(AD_Workbench_ID));
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "MWorkbench.initDesktopWorkbenches", e);
			return false;
		}
		return true;
	}   //  initDesktopWorkbenches

	/**
	 *  Get Window Count
	 */
	public int getWindowCount()
	{
		return m_workbenches.size();
	}   //  getWindowCount

	/**
	 *  Get AD_Workbench_ID of index
	 *  @return -1 if not valid
	 */
	public int getAD_Workbench_ID (int index)
	{
		if (index < 0 || index > m_workbenches.size())
			return -1;
		Integer id = (Integer)m_workbenches.get(index);
		return id.intValue();
	}   //  getAD_Workbench_ID

}   //  MDesktop
