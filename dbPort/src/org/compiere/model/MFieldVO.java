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
 *  Field Model Value Object
 *
 *  @author Jorg Janke
 *  @version  $Id: MFieldVO.java,v 1.17 2005/12/13 00:16:10 jjanke Exp $
 */
public class MFieldVO implements Serializable
{
	/**
	 *  Return the SQL statement used for the MFieldVO.create
	 *  @param ctx context
	 *  @return SQL with or w/o translation and 1 parameter
	 */
	public static String getSQL (Properties ctx)
	{
		//	IsActive is part of View
		String sql = "SELECT * FROM AD_Field_v WHERE AD_Tab_ID=?"
			+ " ORDER BY SeqNo";
		if (!Env.isBaseLanguage(ctx, "AD_Tab"))
			sql = "SELECT * FROM AD_Field_vt WHERE AD_Tab_ID=?"
				+ " AND AD_Language='" + Env.getAD_Language(ctx) + "'"
				+ " ORDER BY SeqNo";
		return sql;
	}   //  getSQL

	/**
	 *  Create Field Value Object
	 *  @param ctx context
	 *  @param WindowNo window
	 *  @param TabNo tab
	 *  @param AD_Window_ID window
	 *  @param readOnly r/o
	 *  @param rs resultset AD_Field_v
	 *  @return MFieldVO
	 */
	public static MFieldVO create (Properties ctx, int WindowNo, 
		int TabNo, int AD_Window_ID, boolean readOnly, ResultSet rs)
	{
		MFieldVO vo = new MFieldVO (ctx, WindowNo, TabNo, AD_Window_ID, readOnly);
		try
		{
			vo.ColumnName = rs.getString("ColumnName");
			if (vo.ColumnName == null)
				return null;

			CLogger.get().fine(vo.ColumnName);

			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 1; i <= rsmd.getColumnCount(); i++)
			{
				String columnName = rsmd.getColumnName (i);
				if (columnName.equalsIgnoreCase("Name"))
					vo.Header = rs.getString (i);
				else if (columnName.equalsIgnoreCase("AD_Reference_ID"))
					vo.displayType = rs.getInt (i);
				else if (columnName.equalsIgnoreCase("AD_Column_ID"))
					vo.AD_Column_ID = rs.getInt (i);
				else if (columnName.equalsIgnoreCase("AD_Table_ID"))
					vo.AD_Table_ID = rs.getInt (i);
				else if (columnName.equalsIgnoreCase("DisplayLength"))
					vo.DisplayLength = rs.getInt (i);
				else if (columnName.equalsIgnoreCase("IsSameLine"))
					vo.IsSameLine = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("IsDisplayed"))
					vo.IsDisplayed = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("DisplayLogic"))
					vo.DisplayLogic = rs.getString (i);
				else if (columnName.equalsIgnoreCase("DefaultValue"))
					vo.DefaultValue = rs.getString (i);
				else if (columnName.equalsIgnoreCase("IsMandatory"))
					vo.IsMandatory = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("IsReadOnly"))
					vo.IsReadOnly = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("IsUpdateable"))
					vo.IsUpdateable = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("IsAlwaysUpdateable"))
					vo.IsAlwaysUpdateable = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("IsHeading"))
					vo.IsHeading = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("IsFieldOnly"))
					vo.IsFieldOnly = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("IsEncryptedField"))
					vo.IsEncryptedField = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("IsEncryptedColumn"))
					vo.IsEncryptedColumn = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("IsSelectionColumn"))
					vo.IsSelectionColumn = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("SortNo"))
					vo.SortNo = rs.getInt (i);
				else if (columnName.equalsIgnoreCase("FieldLength"))
					vo.FieldLength = rs.getInt (i);
				else if (columnName.equalsIgnoreCase("VFormat"))
					vo.VFormat = rs.getString (i);
				else if (columnName.equalsIgnoreCase("ValueMin"))
					vo.ValueMin = rs.getString (i);
				else if (columnName.equalsIgnoreCase("ValueMax"))
					vo.ValueMax = rs.getString (i);
				else if (columnName.equalsIgnoreCase("FieldGroup"))
					vo.FieldGroup = rs.getString (i);
				else if (columnName.equalsIgnoreCase("IsKey"))
					vo.IsKey = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("IsParent"))
					vo.IsParent = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("Description"))
					vo.Description = rs.getString (i);
				else if (columnName.equalsIgnoreCase("Help"))
					vo.Help = rs.getString (i);
				else if (columnName.equalsIgnoreCase("Callout"))
					vo.Callout = rs.getString (i);
				else if (columnName.equalsIgnoreCase("AD_Process_ID"))
					vo.AD_Process_ID = rs.getInt (i);
				else if (columnName.equalsIgnoreCase("ReadOnlyLogic"))
					vo.ReadOnlyLogic = rs.getString (i);
				else if (columnName.equalsIgnoreCase("ObscureType"))
					vo.ObscureType = rs.getString (i);
				//
				else if (columnName.equalsIgnoreCase("AD_Reference_Value_ID"))
					vo.AD_Reference_Value_ID = rs.getInt(i);
				else if (columnName.equalsIgnoreCase("ValidationCode"))
					vo.ValidationCode = rs.getString(i);
				else if (columnName.equalsIgnoreCase("ColumnSQL"))
					vo.ColumnSQL = rs.getString(i);
                                //begin vpj-cd e-evolution 08/09/2005
                                else if (columnName.equalsIgnoreCase("Included_Tab_ID"))
					vo.Included_Tab_ID = rs.getInt(i);
                                //end vpj-cd e-evolution 08/09/2005
			}
			if (vo.Header == null)
				vo.Header = vo.ColumnName;
		}
		catch (SQLException e)
		{
			CLogger.get().log(Level.SEVERE, "create", e);
			return null;
		}
		vo.initFinish();
		return vo;
	}   //  create

	/**
	 *  Init Field for Process Parameter
	 *  @param ctx context
	 *  @param WindowNo window
	 *  @param rs result set AD_Process_Para
	 *  @return MFieldVO
	 */
	public static MFieldVO createParameter (Properties ctx, int WindowNo, ResultSet rs)
	{
		MFieldVO vo = new MFieldVO (ctx, WindowNo, 0, 0, false);
		vo.isProcess = true;
		vo.IsDisplayed = true;
		vo.IsReadOnly = false;
		vo.IsUpdateable = true;

		try
		{
			vo.AD_Table_ID = 0;
			vo.AD_Column_ID = rs.getInt("AD_Process_Para_ID");	//	**
			vo.ColumnName = rs.getString("ColumnName");
			vo.Header = rs.getString("Name");
			vo.Description = rs.getString("Description");
			vo.Help = rs.getString("Help");
			vo.displayType = rs.getInt("AD_Reference_ID");
			vo.IsMandatory = rs.getString("IsMandatory").equals("Y");
			vo.FieldLength = rs.getInt("FieldLength");
			vo.DisplayLength = vo.FieldLength;
			vo.DefaultValue = rs.getString("DefaultValue");
			vo.DefaultValue2 = rs.getString("DefaultValue2");
			vo.VFormat = rs.getString("VFormat");
			vo.ValueMin = rs.getString("ValueMin");
			vo.ValueMax = rs.getString("ValueMax");
			vo.isRange = rs.getString("IsRange").equals("Y");
			//
			vo.AD_Reference_Value_ID = rs.getInt("AD_Reference_Value_ID");
			vo.ValidationCode = rs.getString("ValidationCode");
		}
		catch (SQLException e)
		{
			CLogger.get().log(Level.SEVERE, "createParameter", e);
		}
		//
		vo.initFinish();
		if (vo.DefaultValue2 == null)
			vo.DefaultValue2 = "";
		return vo;
	}   //  createParameter

	/**
	 *  Create range "to" Parameter Field from "from" Parameter Field
	 *  @param voF field value object
	 *  @return to MFieldVO
	 */
	public static MFieldVO createParameter (MFieldVO voF)
	{
		MFieldVO voT = new MFieldVO (voF.ctx, voF.WindowNo, voF.TabNo, voF.AD_Window_ID, voF.tabReadOnly);
		voT.isProcess = true;
		voT.IsDisplayed = true;
		voT.IsReadOnly = false;
		voT.IsUpdateable = true;
		//
		voT.AD_Table_ID = voF.AD_Table_ID;
		voT.AD_Column_ID = voF.AD_Column_ID;    //  AD_Process_Para_ID
		voT.ColumnName = voF.ColumnName;
		voT.Header = voF.Header;
		voT.Description = voF.Description;
		voT.Help = voF.Help;
		voT.displayType = voF.displayType;
		voT.IsMandatory = voF.IsMandatory;
		voT.FieldLength = voF.FieldLength;
		voT.DisplayLength = voF.FieldLength;
		voT.DefaultValue = voF.DefaultValue2;
		voT.VFormat = voF.VFormat;
		voT.ValueMin = voF.ValueMin;
		voT.ValueMax = voF.ValueMax;
		voT.isRange = voF.isRange;
		//
		return voT;
	}   //  createParameter


	/**
	 *  Make a standard field (Created/Updated/By)
	 *  @param ctx context
	 *  @param WindowNo window
	 *  @param TabNo tab
	 *  @param AD_Window_ID window
	 *  @param tabReadOnly rab is r/o
	 *  @param isCreated is Created field
	 *  @param isTimestamp is the timestamp (not by)
	 *  @return MFieldVO
	 */
	public static MFieldVO createStdField (Properties ctx, int WindowNo, int TabNo, int AD_Window_ID, boolean tabReadOnly,
		boolean isCreated, boolean isTimestamp)
	{
		MFieldVO vo = new MFieldVO (ctx, WindowNo, TabNo, AD_Window_ID, tabReadOnly);
		vo.ColumnName = isCreated ? "Created" : "Updated";
		if (!isTimestamp)
			vo.ColumnName += "By";
		vo.displayType = isTimestamp ? DisplayType.DateTime : DisplayType.Table;
		if (!isTimestamp)
			vo.AD_Reference_Value_ID = 110;		//	AD_User Table Reference
		vo.IsDisplayed = false;
		vo.IsMandatory = false;
		vo.IsReadOnly = false;
		vo.IsUpdateable = true;
		vo.initFinish();
		return vo;
	}   //  initStdField

	
	/**************************************************************************
	 *  Private constructor
	 *  @param ctx context
	 *  @param WindowNo window
	 *  @param TabNo tab
	 *  @param AD_Window_ID window
	 *  @param tabReadOnly read only
	 */
	private MFieldVO (Properties ctx, int WindowNo, int TabNo, int AD_Window_ID, boolean tabReadOnly)
	{
		this.ctx = ctx;
		this.WindowNo = WindowNo;
		this.TabNo = TabNo;
		this.AD_Window_ID = AD_Window_ID;
		this.tabReadOnly = tabReadOnly;
	}   //  MFieldVO

	static final long serialVersionUID = 4385061125114436797L;
	
	/** Context                     */
	public Properties   ctx = null;
	/** Window No                   */
	public int          WindowNo;
	/** Tab No                      */
	public int          TabNo;
	/** AD_Winmdow_ID               */
	public int          AD_Window_ID;
	/** Is the Tab Read Only        */
	public boolean      tabReadOnly = false;

	/** Is Process Parameter        */
	public boolean      isProcess = false;
	
	//  Database Fields
	public String       ColumnName = "";
	public String       ColumnSQL;
	public String       Header = "";
	public int          displayType = 0;
	public int          AD_Table_ID = 0;
	public int          AD_Column_ID = 0;
	public int          DisplayLength = 0;
	public boolean      IsSameLine = false;
	public boolean      IsDisplayed = false;
	public String       DisplayLogic = "";
	public String       DefaultValue = "";
	public boolean      IsMandatory = false;
	public boolean      IsReadOnly = false;
	public boolean      IsUpdateable = false;
	public boolean      IsAlwaysUpdateable = false;
	public boolean      IsHeading = false;
	public boolean      IsFieldOnly = false;
	public boolean      IsEncryptedField = false;
	public boolean      IsEncryptedColumn = false;
	public boolean		IsSelectionColumn = false;
	public int          SortNo = 0;
	public int          FieldLength = 0;
	public String       VFormat = "";
	public String       ValueMin = "";
	public String       ValueMax = "";
	public String       FieldGroup = "";
	public boolean      IsKey = false;
	public boolean      IsParent = false;
	public String       Callout = "";
	public int          AD_Process_ID = 0;
	public String       Description = "";
	public String       Help = "";
	public String       ReadOnlyLogic = "";
	public String		ObscureType = null;
	//	Lookup
	public String		ValidationCode = "";
	public int			AD_Reference_Value_ID = 0;

	//  Process Parameter
	public boolean      isRange = false;
	public String       DefaultValue2 = "";

	/** Lookup Value Object     */
	public MLookupInfo  lookupInfo = null;
       //begin vpj-cd e-evolution 08/09/2005
        public int          Included_Tab_ID = 0;
        //begin vpj-cd e-evolution 08/09/2005

	
	/**
	 *  Set Context including contained elements
	 *  @param newCtx new context
	 */
	public void setCtx (Properties newCtx)
	{
		ctx = newCtx;
		if (lookupInfo != null)
			lookupInfo.ctx = newCtx;
	}   //  setCtx

	/**
	 *  Validate Fields and create LookupInfo if required
	 */
	protected void initFinish()
	{
		//  Not null fields
		if (DisplayLogic == null)
			DisplayLogic = "";
		if (DefaultValue == null)
			DefaultValue = "";
		if (FieldGroup == null)
			FieldGroup = "";
		if (Description == null)
			Description = "";
		if (Help == null)
			Help = "";
		if (Callout == null)
			Callout = "";
		if (ReadOnlyLogic == null)
			ReadOnlyLogic = "";


		//  Create Lookup, if not ID
		if (DisplayType.isLookup(displayType))
		{
			try
			{
				lookupInfo = MLookupFactory.getLookupInfo (ctx, WindowNo, AD_Column_ID, displayType,
					Env.getLanguage(ctx), ColumnName, AD_Reference_Value_ID,
					IsParent, ValidationCode);
			}
			catch (Exception e)     //  Cannot create Lookup
			{
				CLogger.get().log(Level.SEVERE, "No LookupInfo for " + ColumnName, e);
				displayType = DisplayType.ID;
			}
		}
	}   //  initFinish

	/**
	 * 	Clone Field
	 *	@param ctx ctx
	 *	@param WindowNo window no
	 *	@param TabNo tab no
	 *	@param AD_Window_ID window id
	 *	@param tabReadOnly r/o
	 *	@return Field or null
	 */
	protected MFieldVO clone(Properties ctx, int WindowNo, int TabNo, int AD_Window_ID, boolean tabReadOnly)
	{
		MFieldVO clone = new MFieldVO(ctx, WindowNo, TabNo, AD_Window_ID, tabReadOnly);
		//
		clone.isProcess = false;
		//  Database Fields
		clone.ColumnName = ColumnName;
		clone.ColumnSQL = ColumnSQL;
		clone.Header = Header;
		clone.displayType = displayType;
		clone.AD_Table_ID = AD_Table_ID;
		clone.AD_Column_ID = AD_Column_ID;
		clone.DisplayLength = DisplayLength;
		clone.IsSameLine = IsSameLine;
		clone.IsDisplayed = IsDisplayed;
		clone.DisplayLogic = DisplayLogic;
		clone.DefaultValue = DefaultValue;
		clone.IsMandatory = IsMandatory;
		clone.IsReadOnly = IsReadOnly;
		clone.IsUpdateable = IsUpdateable;
		clone.IsAlwaysUpdateable = IsAlwaysUpdateable;
		clone.IsHeading = IsHeading;
		clone.IsFieldOnly = IsFieldOnly;
		clone.IsEncryptedField = IsEncryptedField;
		clone.IsEncryptedColumn = IsEncryptedColumn;
		clone.IsSelectionColumn = IsSelectionColumn;
		clone.SortNo = SortNo;
		clone.FieldLength = FieldLength;
		clone.VFormat = VFormat;
		clone.ValueMin = ValueMin;
		clone.ValueMax = ValueMax;
		clone.FieldGroup = FieldGroup;
		clone.IsKey = IsKey;
		clone.IsParent = IsParent;
		clone.Callout = Callout;
		clone.AD_Process_ID = AD_Process_ID;
		clone.Description = Description;
		clone.Help = Help;
		clone.ReadOnlyLogic = ReadOnlyLogic;
		clone.ObscureType = ObscureType;
		//	Lookup
		clone.ValidationCode = ValidationCode;
		clone.AD_Reference_Value_ID = AD_Reference_Value_ID;
		clone.lookupInfo = lookupInfo;

		//  Process Parameter
		clone.isRange = isRange;
		clone.DefaultValue2 = DefaultValue2;

		return clone;
	}	//	clone
	
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MFieldVO[");
		sb.append(AD_Column_ID).append("-").append(ColumnName)
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}   //  MFieldVO
