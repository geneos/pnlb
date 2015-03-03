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
 *  Model Tab Value Object
 *
 *  @author Jorg Janke
 *  @version  $Id: MTabVO.java,v 1.30 2005/12/09 05:19:09 jjanke Exp $
 */
public class MTabVO implements Evaluatee, Serializable
{
	/**************************************************************************
	 *	Create MTab VO
	 *
	 *  @param wVO value object
	 *  @param TabNo tab no
	 *	@param rs ResultSet from AD_Tab_v
	 *	@param isRO true if window is r/o
	 *  @param onlyCurrentRows if true query is limited to not processed records
	 *  @return TabVO
	 */
	public static MTabVO create (MWindowVO wVO, int TabNo, ResultSet rs, 
		boolean isRO, boolean onlyCurrentRows)
	{
		CLogger.get().config("#" + TabNo);

		MTabVO vo = new MTabVO (wVO.ctx, wVO.WindowNo);
		vo.AD_Window_ID = wVO.AD_Window_ID;
		vo.TabNo = TabNo;
		//
		if (!loadTabDetails(vo, rs))
			return null;

		if (isRO)
		{
			CLogger.get().fine("Tab is ReadOnly");
			vo.IsReadOnly = true;
		}
		vo.onlyCurrentRows = onlyCurrentRows;

		//  Create Fields
		if (vo.IsSortTab)
		{
			vo.Fields = new ArrayList<MFieldVO>();	//	dummy
		}
		else
		{
			createFields (vo);
			if (vo.Fields == null || vo.Fields.size() == 0)
			{
				CLogger.get().log(Level.SEVERE, "No Fields");
				return null;
			}
		}
		return vo;
	}	//	create

	/**
	 * 	Load Tab Details from rs into vo
	 * 	@param vo Tab value object
	 *	@param rs ResultSet from AD_Tab_v/t
	 * 	@return true if read ok
	 */
	private static boolean loadTabDetails (MTabVO vo, ResultSet rs)
	{
		MRole role = MRole.getDefault(vo.ctx, false);
		boolean showTrl = "Y".equals(Env.getContext(vo.ctx, "#ShowTrl"));
		boolean showAcct = "Y".equals(Env.getContext(vo.ctx, "#ShowAcct"));
		boolean showAdvanced = "Y".equals(Env.getContext(vo.ctx, "#ShowAdvanced"));
	//	CLogger.get().warning("ShowTrl=" + showTrl + ", showAcct=" + showAcct);
		try
		{
			vo.AD_Tab_ID = rs.getInt("AD_Tab_ID");
			Env.setContext(vo.ctx, vo.WindowNo, vo.TabNo, "AD_Tab_ID", String.valueOf(vo.AD_Tab_ID));
			vo.Name = rs.getString("Name");
			Env.setContext(vo.ctx, vo.WindowNo, vo.TabNo, "Name", vo.Name);

			//	Translation Tab	**
			if (rs.getString("IsTranslationTab").equals("Y"))
			{
				//	Document Translation
				vo.TableName = rs.getString("TableName");
				if (!Env.isBaseTranslation(vo.TableName)	//	C_UOM, ...
					&& !Env.isMultiLingualDocument(vo.ctx))
					showTrl = false;
				if (!showTrl)
				{
					CLogger.get().config("TrlTab Not displayed - AD_Tab_ID=" 
						+ vo.AD_Tab_ID + "=" + vo.Name + ", Table=" + vo.TableName
						+ ", BaseTrl=" + Env.isBaseTranslation(vo.TableName)
						+ ", MultiLingual=" + Env.isMultiLingualDocument(vo.ctx));
					return false;
				}
			}
			//	Advanced Tab	**
			if (!showAdvanced && rs.getString("IsAdvancedTab").equals("Y"))
			{
				CLogger.get().config("AdvancedTab Not displayed - AD_Tab_ID=" 
					+ vo.AD_Tab_ID + " " + vo.Name);
				return false;
			}
			//	Accounting Info Tab	**
			if (!showAcct && rs.getString("IsInfoTab").equals("Y"))
			{
				CLogger.get().fine("AcctTab Not displayed - AD_Tab_ID=" 
					+ vo.AD_Tab_ID + " " + vo.Name);
				return false;
			}
			
			//	DisplayLogic
			vo.DisplayLogic = rs.getString("DisplayLogic");
/**			if (vo.DisplayLogic != null && vo.DisplayLogic.length() > 0)
			{
				if (Env.parseContext (vo.ctx, 0, vo.DisplayLogic, false, false).length() > 0
					&& Evaluator.evaluateLogic (vo, vo.DisplayLogic) )
				{
					CLogger.get().config("Tab Not displayed (" + vo.DisplayLogic + ") AD_Tab_ID=" 
						+ vo.AD_Tab_ID + " " + vo.Name);
					return false;
				}
			}
**/			
			//	Access Level
			vo.AccessLevel = rs.getString("AccessLevel");
			if (!role.canView (vo.ctx, vo.AccessLevel))	//	No Access
			{
				CLogger.get().fine("No Role Access - AD_Tab_ID=" + vo.AD_Tab_ID + " " + vo. Name);
				return false;
			}	//	Used by MField.getDefault
			Env.setContext(vo.ctx, vo.WindowNo, vo.TabNo, "AccessLevel", vo.AccessLevel);

			//	Table Access
			vo.AD_Table_ID = rs.getInt("AD_Table_ID");
			Env.setContext(vo.ctx, vo.WindowNo, vo.TabNo, "AD_Table_ID", String.valueOf(vo.AD_Table_ID));
			if (!role.isTableAccess(vo.AD_Table_ID, true))
			{
				CLogger.get().config("No Table Access - AD_Tab_ID=" 
					+ vo.AD_Tab_ID + " " + vo. Name);
				return false;
			}
			if (rs.getString("IsReadOnly").equals("Y"))
				vo.IsReadOnly = true;
			vo.ReadOnlyLogic = rs.getString("ReadOnlyLogic");
			if (rs.getString("IsInsertRecord").equals("N"))
				vo.IsInsertRecord = false;
			
			//
			vo.Description = rs.getString("Description");
			if (vo.Description == null)
				vo.Description = "";
			vo.Help = rs.getString("Help");
			if (vo.Help == null)
				vo.Help = "";

			if (rs.getString("IsSingleRow").equals("Y"))
				vo.IsSingleRow = true;
			if (rs.getString("HasTree").equals("Y"))
				vo.HasTree = true;

			vo.AD_Table_ID = rs.getInt("AD_Table_ID");
			vo.TableName = rs.getString("TableName");
			if (rs.getString("IsView").equals("Y"))
				vo.IsView = true;
			vo.AD_Column_ID = rs.getInt("AD_Column_ID");   //  Primary Parent Column

			if (rs.getString("IsSecurityEnabled").equals("Y"))
				vo.IsSecurityEnabled = true;
			if (rs.getString("IsDeleteable").equals("Y"))
				vo.IsDeleteable = true;
			if (rs.getString("IsHighVolume").equals("Y"))
				vo.IsHighVolume = true;

			vo.CommitWarning = rs.getString("CommitWarning");
			if (vo.CommitWarning == null)
				vo.CommitWarning = "";
			vo.WhereClause = rs.getString("WhereClause");
			if (vo.WhereClause == null)
				vo.WhereClause = "";
			vo.OrderByClause = rs.getString("OrderByClause");
			if (vo.OrderByClause == null)
				vo.OrderByClause = "";

			vo.AD_Process_ID = rs.getInt("AD_Process_ID");
			if (rs.wasNull())
				vo.AD_Process_ID = 0;
			vo.AD_Image_ID = rs.getInt("AD_Image_ID");
			if (rs.wasNull())
				vo.AD_Image_ID = 0;
			vo.Included_Tab_ID = rs.getInt("Included_Tab_ID");
			if (rs.wasNull())
				vo.Included_Tab_ID = 0;
			//
			vo.TabLevel = rs.getInt("TabLevel");
			if (rs.wasNull())
				vo.TabLevel = 0;
			//
			vo.IsSortTab = rs.getString("IsSortTab").equals("Y");
			if (vo.IsSortTab)
			{
				vo.AD_ColumnSortOrder_ID = rs.getInt("AD_ColumnSortOrder_ID");
				vo.AD_ColumnSortYesNo_ID = rs.getInt("AD_ColumnSortYesNo_ID");
			}
			//
			//	Replication Type - set R/O if Reference
			try
			{
				int index = rs.findColumn ("ReplicationType");
				vo.ReplicationType = rs.getString (index);
				if ("R".equals(vo.ReplicationType))
					vo.IsReadOnly = true;
			}
			catch (Exception e)
			{
			}
		}
		catch (SQLException ex)
		{
			CLogger.get().log(Level.SEVERE, "", ex);
			return false;
		}
		
		return true;
	}	//	loadTabDetails


	/**************************************************************************
	 *  Create Tab Fields
	 *  @param mTabVO tab value object
	 *  @return true if fields were created
	 */
	private static boolean createFields (MTabVO mTabVO)
	{
		mTabVO.Fields = new ArrayList<MFieldVO>();

		String sql = MFieldVO.getSQL(mTabVO.ctx);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, mTabVO.AD_Tab_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MFieldVO voF = MFieldVO.create (mTabVO.ctx, mTabVO.WindowNo, mTabVO.TabNo, mTabVO.AD_Window_ID, mTabVO.IsReadOnly, rs);
				if (voF != null)
					mTabVO.Fields.add(voF);
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			CLogger.get().log(Level.SEVERE, "createFields", e);
			return false;
		}
		return mTabVO.Fields.size() != 0;
	}   //  createFields

	/**
	 *  Return the SQL statement used for the MTabVO.create
	 *  @param ctx context
	 *  @return SQL SELECT String
	 */
	protected static String getSQL (Properties ctx)
	{
		//  View only returns IsActive='Y'
		String sql = "SELECT * FROM AD_Tab_v WHERE AD_Window_ID=?"
			+ " ORDER BY SeqNo";
		if (!Env.isBaseLanguage(ctx, "AD_Window"))
			sql = "SELECT * FROM AD_Tab_vt WHERE AD_Window_ID=?"
				+ " AND AD_Language='" + Env.getAD_Language(ctx) + "'"
				+ " ORDER BY SeqNo";
		return sql;
	}   //  getSQL
	
	
	/**************************************************************************
	 *  Private constructor - must use Factory
	 */
	private MTabVO(Properties ctx, int WindowNo)
	{
		this.ctx = ctx;
		this.WindowNo = WindowNo;
	}   //  MTabVO

	static final long serialVersionUID = 9160212869277319305L;
	
	/** Context - replicated    */
	public  Properties      ctx;
	/** Window No - replicated  */
	public  int				WindowNo;
	/** AD Window - replicated  */
	public  int             AD_Window_ID;

	/** Tab No (not AD_Tab_ID) 0.. */
	public  int				TabNo;

	//  Database Fields

	public	int		    AD_Tab_ID;
	public	String	    Name = "";
	public	String	    Description = "";
	public	String	    Help = "";
	public	boolean	    IsSingleRow = false;
	public  boolean     IsReadOnly = false;
	public 	boolean		IsInsertRecord = true;
	public  boolean	    HasTree = false;
	public  int		    AD_Table_ID;
	/** Primary Parent Column   */
	public  int		    AD_Column_ID = 0;
	public  String	    TableName;
	public  boolean     IsView = false;
	public  String	    AccessLevel;
	public  boolean	    IsSecurityEnabled = false;
	public  boolean	    IsDeleteable = false;
	public  boolean     IsHighVolume = false;
	public	int		    AD_Process_ID = 0;
	public  String	    CommitWarning;
	public  String	    WhereClause;
	public  String      OrderByClause;
	public  String      ReadOnlyLogic;
	public  String      DisplayLogic;
	public  int         TabLevel = 0;
	public int          AD_Image_ID = 0;
	public int          Included_Tab_ID = 0;
	public String		ReplicationType = "L";

	//
	public boolean		IsSortTab = false;
	public int			AD_ColumnSortOrder_ID = 0;
	public int			AD_ColumnSortYesNo_ID = 0;

	//  Derived
	public  boolean     onlyCurrentRows = true;
	//	Only
	public int			onlyCurrentDays = 0;

	/** Fields contain MFieldVO entities    */
	public ArrayList<MFieldVO>	Fields = null;

	
	/**
	 *  Set Context including contained elements
	 *  @param newCtx new context
	 */
	public void setCtx (Properties newCtx)
	{
		ctx = newCtx;
		for (int i = 0; i < Fields.size() ; i++)
		{
			MFieldVO field = (MFieldVO)Fields.get(i);
			field.setCtx(newCtx);
		}
	}   //  setCtx
	
	/**
	 * 	Get Variable Value (Evaluatee)
	 *	@param variableName name
	 *	@return value
	 */
	public String get_ValueAsString (String variableName)
	{
		return Env.getContext (ctx, WindowNo, variableName, false);	// not just window
	}	//	get_ValueAsString

	/**
	 * 	Clone
	 * 	@param ctx context
	 * 	@param WindowNo no
	 *	@return MTabVO or null
	 */
	protected MTabVO clone(Properties ctx, int WindowNo)
	{
		MTabVO clone = new MTabVO(ctx, WindowNo);
		clone.AD_Window_ID = AD_Window_ID;
		clone.TabNo = TabNo;
		//
		clone.AD_Tab_ID = AD_Tab_ID;
		clone.Name = Name;
		clone.Description = Description;
		clone.Help = Help;
		clone.IsSingleRow = IsSingleRow;
		clone.IsReadOnly = IsReadOnly;
		clone.IsInsertRecord = IsInsertRecord;
		clone.HasTree = HasTree;
		clone.AD_Table_ID = AD_Table_ID;
		clone.AD_Column_ID = AD_Column_ID;
		clone.TableName = TableName;
		clone.IsView = IsView;
		clone.AccessLevel = AccessLevel;
		clone.IsSecurityEnabled = IsSecurityEnabled;
		clone.IsDeleteable = IsDeleteable;
		clone.IsHighVolume = IsHighVolume;
		clone.AD_Process_ID = AD_Process_ID;
		clone.CommitWarning = CommitWarning;
		clone.WhereClause = WhereClause;
		clone.OrderByClause = OrderByClause;
		clone.ReadOnlyLogic = ReadOnlyLogic;
		clone.DisplayLogic = DisplayLogic;
		clone.TabLevel = TabLevel;
		clone.AD_Image_ID = AD_Image_ID;
		clone.Included_Tab_ID = Included_Tab_ID;
		clone.ReplicationType = ReplicationType;
		//
		clone.IsSortTab = false;
		clone.AD_ColumnSortOrder_ID = 0;
		clone.AD_ColumnSortYesNo_ID = 0;
		//  Derived
		clone.onlyCurrentRows = true;
		clone.onlyCurrentDays = 0;

		clone.Fields = new ArrayList<MFieldVO>();
		for (int i = 0; i < Fields.size(); i++)
		{
			MFieldVO field = Fields.get(i);
			field = field.clone(ctx, WindowNo, TabNo, AD_Window_ID, IsReadOnly);
			if (field == null)
				return null;
			clone.Fields.add(field);
		}
		
		return clone;
	}	//	clone

}   //  MTabVO

