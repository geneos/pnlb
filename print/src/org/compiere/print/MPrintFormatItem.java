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
package org.compiere.print;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Print Format Item Model.
 * 	Caches Column Name
 *	(Add missing Items with PrintFormatUtil)
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: MPrintFormatItem.java,v 1.47 2005/11/13 23:40:21 jjanke Exp $
 */
public class MPrintFormatItem extends X_AD_PrintFormatItem
{
	/**
	 *	Constructor
	 *  @param ctx context
	 *  @param AD_PrintFormatItem_ID AD_PrintFormatItem_ID
	 */
	public MPrintFormatItem (Properties ctx, int AD_PrintFormatItem_ID, String trxName)
	{
		super (ctx, AD_PrintFormatItem_ID, trxName);
		//	Default Setting
		if (AD_PrintFormatItem_ID == 0)
		{
			setFieldAlignmentType(FIELDALIGNMENTTYPE_Default);
			setLineAlignmentType(LINEALIGNMENTTYPE_None);
			setPrintFormatType(PRINTFORMATTYPE_Text);
			setPrintAreaType(PRINTAREATYPE_Content);
			setShapeType(SHAPETYPE_NormalRectangle);
			//
			setIsCentrallyMaintained(true);
			setIsRelativePosition(true);
			setIsNextLine(false);
			setIsNextPage(false);
			setIsSetNLPosition(false);
			setIsFilledRectangle(false);
			setIsImageField(false);
			setXSpace(0);
			setYSpace(0);
			setXPosition(0);
			setYPosition(0);
			setMaxWidth(0);
			setIsFixedWidth(false);
			setIsHeightOneLine(false);
			setMaxHeight(0);
			setLineWidth(1);
			setArcDiameter(0);
			//
			setIsOrderBy(false);
			setSortNo(0);
			setIsGroupBy(false);
			setIsPageBreak(false);
			setIsSummarized(false);
			setIsAveraged(false);
			setIsCounted(false);
			setIsMinCalc(false);
			setIsMaxCalc(false);
			setIsVarianceCalc(false);
			setIsDeviationCalc(false);
			setIsRunningTotal(false);
			setImageIsAttached(false);
			setIsSuppressNull(false);
		}
	}	//	MPrintFormatItem

	/**
	 *	Constructor
	 *  @param ctx context
	 *  @param rs ResultSet
	 */
	public MPrintFormatItem (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MPrintFormatItem

	/**	Locally cached column name			*/
	private String 					m_columnName = null;
	/** Locally cached label translations			*/
	private HashMap<String,String>	m_translationLabel;
	/** Locally cached suffix translations			*/
	private HashMap<String,String>	m_translationSuffix;

	private static CLogger		s_log = CLogger.getCLogger (MPrintFormatItem.class);

	
	/**************************************************************************
	 *	Get print name with language
	 * 	@param language language - ignored if IsMultiLingualDocument not 'Y'
	 * 	@return print name
	 */
	public String getPrintName (Language language)
	{
		if (language == null || Env.isBaseLanguage(language, "AD_PrintFormatItem"))
			return getPrintName();
		loadTranslations();
		String retValue = (String)m_translationLabel.get(language.getAD_Language());
		if (retValue == null || retValue.length() == 0)
			return getPrintName();
		return retValue;
	}	//	getPrintName

	/**
	 * 	Load Translations
	 */
	private void loadTranslations()
	{
		if (m_translationLabel == null)
		{
			m_translationLabel = new HashMap<String,String>();
			m_translationSuffix = new HashMap<String,String>();
			String sql = "SELECT AD_Language, PrintName, PrintNameSuffix FROM AD_PrintFormatItem_Trl WHERE AD_PrintFormatItem_ID=?";
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt(1, get_ID());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
					m_translationLabel.put (rs.getString (1), rs.getString (2));
					m_translationSuffix.put (rs.getString (1), rs.getString (3));
				}
				rs.close();
				pstmt.close();
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, "loadTrl", e);
			}
		}
	}	//	loadTranslations

	/**
	 *	Get print name suffix with language
	 * 	@param language language - ignored if IsMultiLingualDocument not 'Y'
	 * 	@return print name suffix
	 */
	public String getPrintNameSuffix (Language language)
	{
		if (language == null || Env.isBaseLanguage(language, "AD_PrintFormatItem"))
			return getPrintNameSuffix();
		loadTranslations();
		String retValue = (String)m_translationSuffix.get(language.getAD_Language());
		if (retValue == null || retValue.length() == 0)
			return getPrintNameSuffix();
		return retValue;
	}	//	getPrintNameSuffix


	public boolean isTypeField()
	{
		return getPrintFormatType().equals(PRINTFORMATTYPE_Field);
	}
	public boolean isTypeText()
	{
		return getPrintFormatType().equals(PRINTFORMATTYPE_Text);
	}
	public boolean isTypePrintFormat()
	{
		return getPrintFormatType().equals(PRINTFORMATTYPE_PrintFormat);
	}
	public boolean isTypeImage()
	{
		return getPrintFormatType().equals(PRINTFORMATTYPE_Image);
	}
	public boolean isTypeBox()
	{
		return getPrintFormatType().equals(PRINTFORMATTYPE_Line)
			|| getPrintFormatType().equals(PRINTFORMATTYPE_Rectangle);
	}

	public boolean isFieldCenter()
	{
		return getFieldAlignmentType().equals(FIELDALIGNMENTTYPE_Center);
	}
	public boolean isFieldAlignLeading()
	{
		return getFieldAlignmentType().equals(FIELDALIGNMENTTYPE_LeadingLeft);
	}
	public boolean isFieldAlignTrailing()
	{
		return getFieldAlignmentType().equals(FIELDALIGNMENTTYPE_TrailingRight);
	}
	public boolean isFieldAlignBlock()
	{
		return getFieldAlignmentType().equals(FIELDALIGNMENTTYPE_Block);
	}
	public boolean isFieldAlignDefault()
	{
		return getFieldAlignmentType().equals(FIELDALIGNMENTTYPE_Default);
	}

	public boolean isLineAlignCenter()
	{
		return getLineAlignmentType().equals(LINEALIGNMENTTYPE_Center);
	}
	public boolean isLineAlignLeading()
	{
		return getLineAlignmentType().equals(LINEALIGNMENTTYPE_LeadingLeft);
	}
	public boolean isLineAlignTrailing()
	{
		return getLineAlignmentType().equals(LINEALIGNMENTTYPE_TrailingRight);
	}

	public boolean isHeader()
	{
		return getPrintAreaType().equals(PRINTAREATYPE_Header);
	}
	public boolean isContent()
	{
		return getPrintAreaType().equals(PRINTAREATYPE_Content);
	}
	public boolean isFooter()
	{
		return getPrintAreaType().equals(PRINTAREATYPE_Footer);
	}

	
	/**************************************************************************
	 * 	String representation
	 * 	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MPrintFormatItem[");
		sb.append("ID=").append(get_ID())
			.append(",Name=").append(getName())
			.append(",Print=").append(getPrintName())
			.append(", Seq=").append(getSeqNo())
			.append(",Sort=").append(getSortNo())
			.append(", Area=").append(getPrintAreaType())
			.append(", MaxWidth=").append(getMaxWidth())
			.append(",MaxHeight=").append(getMaxHeight())
			.append(",OneLine=").append(isHeightOneLine())
			.append(", Relative=").append(isRelativePosition());
		if (isRelativePosition())
			sb.append(",X=").append(getXSpace()).append(",Y=").append(getYSpace())
				.append(",LineAlign=").append(getLineAlignmentType())
				.append(",NewLine=").append(isNextLine())
				.append(",NewPage=").append(isPageBreak());
		else
			sb.append(",X=").append(getXPosition()).append(",Y=").append(getYPosition());
		sb.append(",FieldAlign=").append(getFieldAlignmentType());
		//
		sb.append(", Type=").append(getPrintFormatType());
		if (isTypeText())
			;
		else if (isTypeField())
			sb.append(",AD_Column_ID=").append(getAD_Column_ID());
		else if (isTypePrintFormat())
			sb.append(",AD_PrintFormatChild_ID=").append(getAD_PrintFormatChild_ID())
				.append(",AD_Column_ID=").append(getAD_Column_ID());
		else if (isTypeImage())
			sb.append(",ImageIsAttached=").append(isImageIsAttached()).append(",ImageURL=").append(getImageURL());
		//
		sb.append(", Printed=").append(isPrinted())
			.append(",SeqNo=").append(getSeqNo())
			.append(",OrderBy=").append(isOrderBy())
			.append(",SortNo=").append(getSortNo())
			.append(",Summarized=").append(isSummarized());
		sb.append("]");
		return sb.toString();
	}	//	toString


	/*************************************************************************/


	/**	Lookup Map of AD_Column_ID for ColumnName	*/
	private static CCache<Integer,String>	s_columns = new CCache<Integer,String>("AD_PrintFormatItem", 200);

	/**
	 * 	Get ColumnName from AD_Column_ID
	 *  @return ColumnName
	 */
	public String getColumnName()
	{
		if (m_columnName == null)	//	Get Column Name from AD_Column not index
			m_columnName = getColumnName (new Integer(getAD_Column_ID()));
		return m_columnName;
	}	//	getColumnName

	/**
	 * 	Get Column Name from AD_Column_ID.
	 *  Be careful not to confuse it with PO method getAD_Column_ID (index)
	 * 	@param AD_Column_ID column
	 * 	@return Column Name
	 */
	private static String getColumnName (Integer AD_Column_ID)
	{
		if (AD_Column_ID == null || AD_Column_ID.intValue() == 0)
			return null;
		//
		String retValue = (String)s_columns.get(AD_Column_ID);
		if (retValue == null)
		{
			String sql = "SELECT ColumnName FROM AD_Column WHERE AD_Column_ID=?";
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt(1, AD_Column_ID.intValue());
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
				{
					retValue = rs.getString(1);
					s_columns.put(AD_Column_ID, retValue);
				}
				else
					s_log.log(Level.SEVERE, "Not found AD_Column_ID=" + AD_Column_ID);
				rs.close();
				pstmt.close();
			}
			catch (SQLException e)
			{
				s_log.log(Level.SEVERE, "AD_Column_ID=" + AD_Column_ID, e);
			}
		}
		return retValue;
	}	//	getColumnName

	
	/**************************************************************************
	 * 	Create Print Format Item from Column
	 *  @param format parent
	 * 	@param AD_Column_ID column
	 *  @param seqNo sequence of display if 0 it is not printed
	 * 	@return Print Format Item
	 */
	public static MPrintFormatItem createFromColumn (MPrintFormat format, int AD_Column_ID, int seqNo)
	{
		MPrintFormatItem pfi = new MPrintFormatItem (format.getCtx(), 0, null);
		pfi.setAD_PrintFormat_ID (format.getAD_PrintFormat_ID());
		pfi.setClientOrg(format);
		pfi.setAD_Column_ID(AD_Column_ID);
		pfi.setPrintFormatType(PRINTFORMATTYPE_Field);

		//	translation is dome by trigger
		String sql = "SELECT c.ColumnName,e.Name,e.PrintName, "		//	1..3
			+ "c.AD_Reference_ID,c.IsKey,c.SeqNo "					//	4..6
			+ "FROM AD_Column c, AD_Element e "
			+ "WHERE c.AD_Column_ID=?"
			+ " AND c.AD_Element_ID=e.AD_Element_ID";
		//	translate base entry if single language - trigger copies to trl tables
		Language language = format.getLanguage();
		boolean trl = !Env.isMultiLingualDocument(format.getCtx()) && !language.isBaseLanguage();
		if (trl)
			sql = "SELECT c.ColumnName,e.Name,e.PrintName, "		//	1..3
				+ "c.AD_Reference_ID,c.IsKey,c.SeqNo "				//	4..6
				+ "FROM AD_Column c, AD_Element_Trl e "
				+ "WHERE c.AD_Column_ID=?"
				+ " AND c.AD_Element_ID=e.AD_Element_ID"
				+ " AND e.AD_Language=?";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Column_ID);
			if (trl)
				pstmt.setString(2, language.getAD_Language());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				String ColumnName = rs.getString(1);
				pfi.setName(rs.getString(2));
				pfi.setPrintName(rs.getString(3));
				int displayType = rs.getInt(4);
				if (DisplayType.isNumeric(displayType))
					pfi.setFieldAlignmentType(FIELDALIGNMENTTYPE_TrailingRight);
				else if (displayType == DisplayType.Text || displayType == DisplayType.Memo )
					pfi.setFieldAlignmentType(FIELDALIGNMENTTYPE_Block);
				else
					pfi.setFieldAlignmentType(FIELDALIGNMENTTYPE_LeadingLeft);
				boolean isKey = "Y".equals(rs.getString(5));
				//
				if (isKey
					|| ColumnName.startsWith("Created") || ColumnName.startsWith("Updated")
					|| ColumnName.equals("AD_Client_ID") || ColumnName.equals("AD_Org_ID")
					|| ColumnName.equals("IsActive")
					|| displayType == DisplayType.Button || displayType == DisplayType.Binary
					|| displayType == DisplayType.ID || displayType == DisplayType.Image
					|| displayType == DisplayType.RowID
					|| seqNo == 0 )
				{
					pfi.setIsPrinted(false);
					pfi.setSeqNo(0);
				}
				else
				{
					pfi.setIsPrinted(true);
					pfi.setSeqNo(seqNo);
				}
				int idSeqNo = rs.getInt(6);	//	IsIdentifier SortNo
				if (idSeqNo > 0)
				{
					pfi.setIsOrderBy(true);
					pfi.setSortNo(idSeqNo);
				}
			}
			else
				s_log.log(Level.SEVERE, "Not Found AD_Column_ID=" + AD_Column_ID
					+ " Trl=" + trl + " " + language.getAD_Language());
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		if (!pfi.save())
			return null;
	//	pfi.dump();
		return pfi;
	}	//	createFromColumn

	/**
	 * 	Copy existing Definition To Client
	 * 	@param To_Client_ID to client
	 *  @param AD_PrintFormat_ID parent print format
	 * 	@return print format item
	 */
	public MPrintFormatItem copyToClient (int To_Client_ID, int AD_PrintFormat_ID)
	{
		MPrintFormatItem to = new MPrintFormatItem (p_ctx, 0, null);
		MPrintFormatItem.copyValues(this, to);
		to.setClientOrg(To_Client_ID, 0);
		to.setAD_PrintFormat_ID(AD_PrintFormat_ID);
		to.save();
		return to;
	}	//	copyToClient

	
	
	/**
	 * 	Before Save
	 *	@param newRecord
	 *	@return true if ok
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Order
		if (!isOrderBy())
		{
			setSortNo(0);
			setIsGroupBy(false);
			setIsPageBreak(false);
		}
		//	Rel Position
		if (isRelativePosition())
		{
			setXPosition(0);
			setYPosition(0);
		}
		else
		{
			setXSpace(0);
			setYSpace(0);
		}
		//	Image
		if (isImageField())
		{
			setImageIsAttached(false);
			setImageURL(null);
		}
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
		//	Set Translation from Element
		if (newRecord 
		//	&& MClient.get(getCtx()).isMultiLingualDocument()
			&& getPrintName() != null && getPrintName().length() > 0)
		{
			String sql = "UPDATE AD_PrintFormatItem_Trl trl "
				+ "SET PrintName = (SELECT e.PrintName "
					+ "FROM AD_Element_Trl e, AD_Column c "
					+ "WHERE e.AD_Language=trl.AD_Language"
					+ " AND e.AD_Element_ID=c.AD_Element_ID"
					+ " AND c.AD_Column_ID=" + getAD_Column_ID() + ") "
				+ "WHERE AD_PrintFormatItem_ID = " + get_ID()
				+ " AND EXISTS (SELECT * "
					+ "FROM AD_Element_Trl e, AD_Column c "
					+ "WHERE e.AD_Language=trl.AD_Language"
					+ " AND e.AD_Element_ID=c.AD_Element_ID"
					+ " AND c.AD_Column_ID=" + getAD_Column_ID()
					+ " AND trl.AD_PrintFormatItem_ID = " + get_ID() + ")"
				+ " AND EXISTS (SELECT * FROM AD_Client "
					+ "WHERE AD_Client_ID=trl.AD_Client_ID AND IsMultiLingualDocument='Y')";
			int no = DB.executeUpdate(sql, get_TrxName());
			log.fine("translations updated #" + no);
		}

		return success;
	}	//	afterSave
	
}	//	MPrintFormatItem
