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
 *	AD_PrintFormat - Print Format Model.
 *	(Add missing Items with PrintFormatUtil)
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: MPrintFormat.java,v 1.54 2005/11/13 23:40:21 jjanke Exp $
 */
public class MPrintFormat extends X_AD_PrintFormat
{
	/**
	 *	Public Constructor.
	 * 	Use static get methods
	 *  @param ctx context
	 *  @param AD_PrintFormat_ID AD_PrintFormat_ID
	 */
	public MPrintFormat (Properties ctx, int AD_PrintFormat_ID, String trxName)
	{
		super (ctx, AD_PrintFormat_ID, trxName);
		//	Language=[Deutsch,Locale=de_DE,AD_Language=en_US,DatePattern=DD.MM.YYYY,DecimalPoint=false]
		m_language = Language.getLoginLanguage();
		if (AD_PrintFormat_ID == 0)
		{
			setStandardHeaderFooter(true);
			setIsTableBased(true);
			setIsForm(false);
			setIsDefault(false);
		}
		m_items = getItems();
	}	//	MPrintFormat
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MPrintFormat (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
		m_language = Language.getLoginLanguage();
		m_items = getItems();
	}	//	MPrintFormat

	/** Items							*/
	private MPrintFormatItem[]		m_items = null;
	/** Translation View Language		*/
	private String					m_translationViewLanguage = null;
	/**	Language of Report				*/
	private Language 				m_language;
	/** Table Format					*/
	private MPrintTableFormat 		m_tFormat;

	private static CLogger			s_log = CLogger.getCLogger (MPrintFormat.class);

	/**
	 * 	Get Language
	 *  @return language
	 */
	public Language getLanguage()
	{
		return m_language;
	}	//	getLanguage

	/**
	 * 	Set Language
	 *  @param language language
	 */
	public void setLanguage(Language language)
	{
		if (language != null)
		{
			m_language = language;
		//	log.fine("setLanguage - " + language);
		}
		m_translationViewLanguage = null;
	}	//	getLanguage

	/**
	 * 	Get AD_Column_ID of Order Columns
	 * 	@return Array of AD_Column_IDs in Sort Order
	 */
	public int[] getOrderAD_Column_IDs()
	{
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();	//	SortNo - AD_Column_ID
		for (int i = 0; i < m_items.length; i++)
		{
			//	Sort Order and Column must be > 0
			if (m_items[i].getSortNo() != 0 && m_items[i].getAD_Column_ID() != 0)
				map.put(new Integer(m_items[i].getSortNo()), new Integer(m_items[i].getAD_Column_ID()));
		}
		//	Get SortNo and Sort them
		Integer[] keys = new Integer[map.keySet().size()];
		map.keySet().toArray(keys);
		Arrays.sort(keys);

		//	Create AD_Column_ID array
		int[] retValue = new int[keys.length];
		for (int i = 0; i < keys.length; i++)
		{
			Integer value = (Integer)map.get(keys[i]);
			retValue[i] = value.intValue();
		}
		return retValue;
	}	//	getOrderAD_Column_IDs

	/**
	 * 	Get AD_Column_IDs of columns in Report
	 * 	@return Array of AD_Column_ID
	 */
	public int[] getAD_Column_IDs()
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < m_items.length; i++)
		{
			if (m_items[i].getAD_Column_ID() != 0 && m_items[i].isPrinted())
				list.add(new Integer(m_items[i].getAD_Column_ID()));
		}
		//	Convert
		int[] retValue = new int[list.size()];
		for (int i = 0; i < list.size(); i++)
			retValue[i] = ((Integer)list.get(i)).intValue();
		return retValue;
	}	//	getAD_Column_IDs

	/**
	 * 	Set Items
	 * 	@param items items
	 */
	private void setItems (MPrintFormatItem[] items)
	{
		if (items != null)
			m_items = items;
	}	//	setItems

	/**
	 * 	Get active Items
	 * 	@return items
	 */
	private MPrintFormatItem[] getItems()
	{
		ArrayList<MPrintFormatItem> list = new ArrayList<MPrintFormatItem>();
		String sql = "SELECT * FROM AD_PrintFormatItem pfi "
			+ "WHERE pfi.AD_PrintFormat_ID=? AND pfi.IsActive='Y'"
			//	Display restrictions - Passwords, etc.
			+ " AND NOT EXISTS (SELECT * FROM AD_Field f "
				+ "WHERE pfi.AD_Column_ID=f.AD_Column_ID"
				+ " AND (f.IsEncrypted='Y' OR f.ObscureType IS NOT NULL))" 
			+ "ORDER BY SeqNo";
		MRole role = MRole.getDefault(getCtx(), false);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, get_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MPrintFormatItem pfi = new MPrintFormatItem(p_ctx, rs, get_TrxName());
				if (role.isColumnAccess(getAD_Table_ID(), pfi.getAD_Column_ID(), true))
					list.add (pfi);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		//
		MPrintFormatItem[] retValue = new MPrintFormatItem[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getItems

	/**
	 * 	Get Item Count
	 * 	@return number of items or -1 if items not defined
	 */
	public int getItemCount()
	{
		if (m_items == null)
			return -1;
		return m_items.length;
	}	//	getItemCount

	/**
	 * 	Get Print Format Item
	 * 	@param index index
	 * 	@return Print Format Item
	 */
	public MPrintFormatItem getItem (int index)
	{
		if (index < 0 || index >= m_items.length)
			throw new ArrayIndexOutOfBoundsException("Index=" + index + " - Length=" + m_items.length);
		return m_items[index];
	}	//	getItem

	/**
	 * 	Set the translation of the Format Items to the original
	 */
	public void setTranslation()
	{
		StringBuffer sb = new StringBuffer ("UPDATE AD_PrintFormatItem_Trl t"
			+ " SET (PrintName, PrintNameSuffix)="
			+ " (SELECT PrintName, PrintNameSuffix FROM AD_PrintFormatItem i WHERE i.AD_PrintFormatItem_ID=t.AD_PrintFormatItem_ID) "
			+ "WHERE AD_PrintFormatItem_ID IN"
			+ " (SELECT AD_PrintFormatItem_ID FROM AD_PrintFormatItem WHERE AD_PrintFormat_ID=").append(get_ID()).append(")");
		int no = DB.executeUpdate(sb.toString(), get_TrxName());
		log.fine("setTranslation #" + no);
	}	//	setTranslation

	
	/**************************************************************************
	 * 	Set Standard Header
	 *	@param standardHeaderFooter true if std header
	 */
	public void setStandardHeaderFooter (boolean standardHeaderFooter)
	{
		super.setIsStandardHeaderFooter(standardHeaderFooter);
		if (standardHeaderFooter)
		{
			setFooterMargin(0);
			setHeaderMargin(0);
		}
	}	//	setSatndardHeaderFooter

	/**
	 * 	Set Table based.
	 * 	Reset Form
	 * 	@param tableBased true if table based
	 */
	public void setIsTableBased (boolean tableBased)
	{
		super.setIsTableBased (tableBased);
		if (tableBased)
			super.setIsForm(false);
	}	//	setIsTableBased

	
	/**************************************************************************
	 * 	Set Translation View Language.
	 * 	@param language language (checked for base language)
	 */
	public void setTranslationLanguage (Language language)
	{
		if (language == null || language.isBaseLanguage())
		{
			log.info("Ignored - " + language);
			m_translationViewLanguage = null;
		}
		else
		{
			log.info("Language=" + language.getAD_Language());
			m_translationViewLanguage = language.getAD_Language();
			m_language = language;
		}
	}	//	setTranslationLanguage

	/**
	 *  Get Translation View use
	 *	@return true if a translation view is used
	 */
	public boolean isTranslationView()
	{
		return m_translationViewLanguage != null;
	}	//	isTranslationView

	/**
	 *	Update the Query to access the Translation View.
	 *  Can be called multiple times, adds only if not set already
	 *  @param query query to be updated
	 */
	public void setTranslationViewQuery (MQuery query)
	{
		//	Set Table Name and add add restriction, if a view and language set
		if (m_translationViewLanguage != null && query != null && query.getTableName().toUpperCase().endsWith("_V"))
		{
			query.setTableName(query.getTableName() + "t");
			query.addRestriction("AD_Language", MQuery.EQUAL, m_translationViewLanguage);
		}
	}	//	setTranslationViewQuery

	
	/**************************************************************************
	 * 	Get Optional TableFormat
	 * 	@param AD_PrintTableFormat_ID table format
	 */
	public void setAD_PrintTableFormat_ID (int AD_PrintTableFormat_ID)
	{
		super.setAD_PrintTableFormat_ID(AD_PrintTableFormat_ID);
		m_tFormat = MPrintTableFormat.get (getCtx(), AD_PrintTableFormat_ID, getAD_PrintFont_ID());
	}	//	getAD_PrintTableFormat_ID

	/**
	 * 	Get Table Format
	 * 	@return Table Format
	 */
	public MPrintTableFormat getTableFormat()
	{
		if (m_tFormat == null)
			m_tFormat = MPrintTableFormat.get(getCtx(), getAD_PrintTableFormat_ID(), getAD_PrintFont_ID());
		return m_tFormat;
	}	//	getTableFormat

	/**
	 * 	Sting Representation
	 * 	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MPrintFormat[ID=").append(get_ID())
			.append(",Name=").append(getName())
			.append(",Language=").append(getLanguage())
			.append(",Items=").append(getItemCount())
			.append("]");
		return sb.toString();
	}	//	toString

	
	/**************************************************************************
	 *  Load Special data (images, ..).
	 *  To be extended by sub-classes
	 *  @param rs result set
	 *  @param index zero based index
	 *  @return value value
	 *  @throws SQLException
	 */
	protected Object loadSpecial (ResultSet rs, int index) throws SQLException
	{
		//	CreateCopy
	//	log.config( "MPrintFormat.loadSpecial", p_info.getColumnName(index));
		return null;
	}   //  loadSpecial

	/**
	 *  Save Special Data.
	 *  To be extended by sub-classes
	 *  @param value value
	 *  @param index index
	 *  @return SQL code for INSERT VALUES clause
	 */
	protected String saveNewSpecial (Object value, int index)
	{
		//	CreateCopy
	//	String colName = p_info.getColumnName(index);
	//	String colClass = p_info.getColumnClass(index).toString();
	//	String colValue = value == null ? "null" : value.getClass().toString();
	//	log.log(Level.SEVERE, "PO.saveNewSpecial - Unknown class for column " + colName + " (" + colClass + ") - Value=" + colValue);
		if (value == null)
			return "NULL";
		return value.toString();
	}   //  saveNewSpecial


	/**************************************************************************
	 * 	Create MPrintFormat for Table
	 *  @param ctx context
	 * 	@param AD_Table_ID table
	 * 	@return print format
	 */
	static public MPrintFormat createFromTable (Properties ctx, int AD_Table_ID)
	{
		return createFromTable(ctx, AD_Table_ID, 0);
	}	//	createFromTable

	/**
	 * 	Create MPrintFormat for Table
	 *  @param ctx context
	 * 	@param AD_Table_ID table
	 *  @param AD_PrintFormat_ID 0 or existing PrintFormat
	 * 	@return print format
	 */
	static public MPrintFormat createFromTable (Properties ctx, 
		int AD_Table_ID, int AD_PrintFormat_ID)
	{
		int AD_Client_ID = Env.getAD_Client_ID(ctx);
		s_log.info ("AD_Table_ID=" + AD_Table_ID + " - AD_Client_ID=" + AD_Client_ID);

		MPrintFormat pf = new MPrintFormat(ctx, AD_PrintFormat_ID, null);
		pf.setAD_Table_ID (AD_Table_ID);

		//	Get Info
		String sql = "SELECT TableName,"		//	1
			+ " (SELECT COUNT(*) FROM AD_PrintFormat x WHERE x.AD_Table_ID=t.AD_Table_ID AND x.AD_Client_ID=c.AD_Client_ID) AS Count,"
			+ " COALESCE (cpc.AD_PrintColor_ID, pc.AD_PrintColor_ID) AS AD_PrintColor_ID,"	//	3
			+ " COALESCE (cpf.AD_PrintFont_ID, pf.AD_PrintFont_ID) AS AD_PrintFont_ID,"
			+ " COALESCE (cpp.AD_PrintPaper_ID, pp.AD_PrintPaper_ID) AS AD_PrintPaper_ID "
			+ "FROM AD_Table t, AD_Client c"
			+ " LEFT OUTER JOIN AD_PrintColor cpc ON (cpc.AD_Client_ID=c.AD_Client_ID AND cpc.IsDefault='Y')"
			+ " LEFT OUTER JOIN AD_PrintFont cpf ON (cpf.AD_Client_ID=c.AD_Client_ID AND cpf.IsDefault='Y')"
			+ " LEFT OUTER JOIN AD_PrintPaper cpp ON (cpp.AD_Client_ID=c.AD_Client_ID AND cpp.IsDefault='Y'),"
			+ " AD_PrintColor pc, AD_PrintFont pf, AD_PrintPaper pp "
			+ "WHERE t.AD_Table_ID=? AND c.AD_Client_ID=?"		//	#1/2
			+ " AND pc.IsDefault='Y' AND pf.IsDefault='Y' AND pp.IsDefault='Y'";
		boolean error = true;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Table_ID);
			pstmt.setInt(2, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				//	Name
				String TableName = rs.getString(1);
				String ColumnName = TableName + "_ID";
				String s = ColumnName;
				if (!ColumnName.equals("T_Report_ID"))
				{
					s = Msg.translate (ctx, ColumnName);
					if (ColumnName.equals (s)) //	not found
						s = Msg.translate (ctx, TableName);
				}
				int count = rs.getInt(2);
				if (count > 0)
					s += "_" + (count+1);
				pf.setName(s);
				//
				pf.setAD_PrintColor_ID(rs.getInt(3));
				pf.setAD_PrintFont_ID(rs.getInt(4));
				pf.setAD_PrintPaper_ID(rs.getInt(5));
				//
				error = false;
			}
			else
				s_log.log(Level.SEVERE, "No info found " + AD_Table_ID);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		if (error)
			return null;

		//	Save & complete
		if (!pf.save())
			return null;
	//	pf.dump();
		pf.setItems (createItems(ctx, pf));
		//
		return pf;
	}	//	createFromTable

	/**
	 * 	Create MPrintFormat for ReportView
	 *  @param ctx context
	 * 	@param AD_ReportView_ID ReportView
	 *  @param ReportName - optional Report Name
	 * 	@return print format
	 */
	static public MPrintFormat createFromReportView (Properties ctx, int AD_ReportView_ID, String ReportName)
	{
		int AD_Client_ID = Env.getAD_Client_ID(ctx);
		s_log.info ("AD_ReportView_ID=" + AD_ReportView_ID + " - AD_Client_ID=" + AD_Client_ID + " - " + ReportName);

		MPrintFormat pf = new MPrintFormat(ctx, 0, null);
		pf.setAD_ReportView_ID (AD_ReportView_ID);

		//	Get Info
		String sql = "SELECT t.TableName,"
			+ " (SELECT COUNT(*) FROM AD_PrintFormat x WHERE x.AD_ReportView_ID=rv.AD_ReportView_ID AND x.AD_Client_ID=c.AD_Client_ID) AS Count,"
			+ " COALESCE (cpc.AD_PrintColor_ID, pc.AD_PrintColor_ID) AS AD_PrintColor_ID,"
			+ " COALESCE (cpf.AD_PrintFont_ID, pf.AD_PrintFont_ID) AS AD_PrintFont_ID,"
			+ " COALESCE (cpp.AD_PrintPaper_ID, pp.AD_PrintPaper_ID) AS AD_PrintPaper_ID,"
			+ " t.AD_Table_ID "
			+ "FROM AD_ReportView rv"
			+ " INNER JOIN AD_Table t ON (rv.AD_Table_ID=t.AD_Table_ID),"
			+ " AD_Client c"
			+ " LEFT OUTER JOIN AD_PrintColor cpc ON (cpc.AD_Client_ID=c.AD_Client_ID AND cpc.IsDefault='Y')"
			+ " LEFT OUTER JOIN AD_PrintFont cpf ON (cpf.AD_Client_ID=c.AD_Client_ID AND cpf.IsDefault='Y')"
			+ " LEFT OUTER JOIN AD_PrintPaper cpp ON (cpp.AD_Client_ID=c.AD_Client_ID AND cpp.IsDefault='Y'),"
			+ " AD_PrintColor pc, AD_PrintFont pf, AD_PrintPaper pp "
			+ "WHERE rv.AD_ReportView_ID=? AND c.AD_Client_ID=?"
			+ " AND pc.IsDefault='Y' AND pf.IsDefault='Y' AND pp.IsDefault='Y'";
		boolean error = true;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_ReportView_ID);
			pstmt.setInt(2, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				//	Name
				String name = ReportName;
				if (name == null || name.length() == 0)
					name = rs.getString(1);		//	TableName
				int count = rs.getInt(2);
				if (count > 0)
					name += "_" + count;
				pf.setName(name);
				//
				pf.setAD_PrintColor_ID(rs.getInt(3));
				pf.setAD_PrintFont_ID(rs.getInt(4));
				pf.setAD_PrintPaper_ID(rs.getInt(5));
				//
				pf.setAD_Table_ID (rs.getInt(6));
				error = false;
			}
			else
				s_log.log(Level.SEVERE, "Not found: AD_ReportView_ID=" + AD_ReportView_ID);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		if (error)
			return null;

		//	Save & complete
		if (!pf.save())
			return null;
	//	pf.dump();
		pf.setItems (createItems(ctx, pf));
		//
		return pf;
	}	//	createFromReportView


	/**
	 * 	Create Items.
	 *  Using the display order of Fields in some Tab
	 *  @param ctx context
	 *  @param format print format
	 * 	@return items
	 */
	static private MPrintFormatItem[] createItems (Properties ctx, MPrintFormat format)
	{
		s_log.fine ("From window Tab ...");
		ArrayList<MPrintFormatItem> list = new ArrayList<MPrintFormatItem>();
		//	Get Column List from Tab
		String sql = "SELECT AD_Column_ID " //, Name, IsDisplayed, SeqNo
			+ "FROM AD_Field "
			+ "WHERE AD_Tab_ID=(SELECT AD_Tab_ID FROM AD_Tab WHERE AD_Table_ID=? AND ROWNUM=1)"
			+ " AND IsEncrypted='N' AND ObscureType IS NULL "
			+ "ORDER BY COALESCE(IsDisplayed,'N') DESC, SortNo, SeqNo, Name";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, format.get_TrxName());
			pstmt.setInt(1, format.getAD_Table_ID());
			ResultSet rs = pstmt.executeQuery();
			int seqNo = 1;
			while (rs.next())
			{
				MPrintFormatItem pfi = MPrintFormatItem.createFromColumn (format, rs.getInt(1), seqNo++);
				if (pfi != null)
				{
					list.add (pfi);
					s_log.finest("Tab: " + pfi);
				}
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, "(tab) - " + sql, e);
		}
		//	No Tab found for Table
		if (list.size() == 0)
		{
			s_log.fine("From Table ...");
			sql = "SELECT AD_Column_ID "
				+ "FROM AD_Column "
				+ "WHERE AD_Table_ID=? "
				+ "ORDER BY IsIdentifier DESC, SeqNo, Name";
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(sql, format.get_TrxName());
				pstmt.setInt(1, format.getAD_Table_ID());
				ResultSet rs = pstmt.executeQuery();
				int seqNo = 1;
				while (rs.next())
				{
					MPrintFormatItem pfi = MPrintFormatItem.createFromColumn (format, rs.getInt(1), seqNo++);
					if (pfi != null)
					{
						list.add (pfi);
						s_log.finest("Table: " + pfi);
					}
				}
				rs.close();
				pstmt.close();
			}
			catch (SQLException e)
			{
				s_log.log(Level.SEVERE, "(table) - " + sql, e);
			}			
		}
		
		//
		MPrintFormatItem[] retValue = new MPrintFormatItem[list.size()];
		list.toArray(retValue);
		s_log.info(format + " - #" + retValue.length);
		return retValue;
	}	//	createItems

	/**
	 * 	Copy Items
	 *  @param fromFormat from print format
	 *  @param toFormat to print format (client, id)
	 * 	@return items
	 */
	static private MPrintFormatItem[] copyItems (MPrintFormat fromFormat, MPrintFormat toFormat)
	{
		s_log.info("From=" + fromFormat);
		ArrayList<MPrintFormatItem> list = new ArrayList<MPrintFormatItem>();

		MPrintFormatItem[] items = fromFormat.getItems();
		for (int i = 0; i < items.length; i++)
		{
			MPrintFormatItem pfi = items[i].copyToClient (toFormat.getAD_Client_ID(), toFormat.get_ID());
			if (pfi != null)
				list.add (pfi);
		}
		//
		MPrintFormatItem[] retValue = new MPrintFormatItem[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	copyItems

	
	/**************************************************************************
	 * 	Copy existing Definition To Client
	 * 	@param ctx context
	 * 	@param from_AD_PrintFormat_ID format
	 * 	@param to_AD_PrintFormat_ID format
	 * 	@return print format
	 */
	public static MPrintFormat copy (Properties ctx, 
		int from_AD_PrintFormat_ID, int to_AD_PrintFormat_ID)
	{
		return copy (ctx, from_AD_PrintFormat_ID, to_AD_PrintFormat_ID, -1);
	}	//	copy

	/**
	 * 	Copy existing Definition To Client
	 * 	@param ctx context
	 * 	@param AD_PrintFormat_ID format
	 * 	@param to_Client_ID to client
	 * 	@return print format
	 */
	public static MPrintFormat copyToClient (Properties ctx, 
		int AD_PrintFormat_ID, int to_Client_ID)
	{
		return copy (ctx, AD_PrintFormat_ID, 0, to_Client_ID);
	}	//	copy

	/**
	 * 	Copy existing Definition To Client
	 * 	@param ctx context
	 * 	@param from_AD_PrintFormat_ID format
	 *  @param to_AD_PrintFormat_ID to format or 0 for new
	 * 	@param to_Client_ID to client (ignored, if to_AD_PrintFormat_ID <> 0)
	 * 	@return print format
	 */
	private static MPrintFormat copy (Properties ctx, int from_AD_PrintFormat_ID,
		int to_AD_PrintFormat_ID, int to_Client_ID)
	{
		s_log.info ("From AD_PrintFormat_ID=" + from_AD_PrintFormat_ID
			+ ", To AD_PrintFormat_ID=" + to_AD_PrintFormat_ID 
			+ ", To Client_ID=" + to_Client_ID);
		if (from_AD_PrintFormat_ID == 0)
			throw new IllegalArgumentException ("From_AD_PrintFormat_ID is 0");
		//
		MPrintFormat from = new MPrintFormat(ctx, from_AD_PrintFormat_ID, null);
		MPrintFormat to = new MPrintFormat (ctx, to_AD_PrintFormat_ID, null);		//	could be 0
		MPrintFormat.copyValues (from, to);
		//	New
		if (to_AD_PrintFormat_ID == 0)
		{
			if (to_Client_ID < 0)
				to_Client_ID = Env.getAD_Client_ID(ctx);
			to.setClientOrg (to_Client_ID, 0);
		}
		//	Set Name - Remove TEMPLATE - add copy
		to.setName(Util.replace(to.getName(), "TEMPLATE", String.valueOf(to_Client_ID)));
		to.setName(to.getName() 
			+ " " + Msg.getMsg(ctx, "Copy")		 
			+ " " + to.hashCode());		//	unique name
		//
		to.save();

		//	Copy Items
		to.setItems(copyItems(from,to));
		return to;
	}	//	copyToClient

	/*************************************************************************/

	/** Cached Formats						*/
	static private CCache<Integer,MPrintFormat> s_formats = new CCache<Integer,MPrintFormat>("AD_PrintFormat", 30);

	/**
	 * 	Get Format
	 * 	@param ctx context
	 * 	@param AD_PrintFormat_ID id
	 *  @param readFromDisk refresh from disk
	 * 	@return Format
	 */
	static public MPrintFormat get (Properties ctx, int AD_PrintFormat_ID, boolean readFromDisk)
	{
		Integer key = new Integer(AD_PrintFormat_ID);
		MPrintFormat pf = null;
		if (!readFromDisk)
			pf = (MPrintFormat)s_formats.get(key);
		if (pf == null)
		{
			pf = new MPrintFormat (ctx, AD_PrintFormat_ID, null);
			s_formats.put(key, pf);
		}
		return pf;
	}	//	get

	/**
	 * 	Get (default) Printformat for Report View or Table
	 *	@param ctx context
	 *	@param AD_ReportView_ID id or 0
	 *	@param AD_Table_ID id or 0
	 *	@return first print format found or null
	 */
	static public MPrintFormat get (Properties ctx, int AD_ReportView_ID, int AD_Table_ID)
	{
		MPrintFormat retValue = null;
		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM AD_PrintFormat WHERE ";
		if (AD_ReportView_ID > 0)
			sql += "AD_ReportView_ID=?";
		else
			sql += "AD_Table_ID=?";
		sql += " ORDER BY IsDefault DESC"; 
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, AD_ReportView_ID > 0 ? AD_ReportView_ID : AD_Table_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MPrintFormat (ctx, rs, null);
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
		return retValue;
	}	//	get

	/**
	 * 	Delete Format from Cache
	 * 	@param AD_PrintFormat_ID id
	 */
	static public void deleteFromCache (int AD_PrintFormat_ID)
	{
		Integer key = new Integer(AD_PrintFormat_ID);
		s_formats.put(key, null);
	}	//	deleteFromCache

	
	/**************************************************************************
	 * 	Test
	 * 	@param args arga
	 */
	static public void main (String[] args)
	{
		org.compiere.Compiere.startup(true);
		/**
		MPrintFormat.createFromTable(Env.getCtx(), 496);	//	Order
		MPrintFormat.createFromTable(Env.getCtx(), 497);
		MPrintFormat.createFromTable(Env.getCtx(), 516);	//	Invoice
		MPrintFormat.createFromTable(Env.getCtx(), 495);
		MPrintFormat.createFromTable(Env.getCtx(), 500);	//	Shipment
		MPrintFormat.createFromTable(Env.getCtx(), 501);

		MPrintFormat.createFromTable(Env.getCtx(), 498);	//	Check
		MPrintFormat.createFromTable(Env.getCtx(), 499);
		MPrintFormat.createFromTable(Env.getCtx(), 498);	//	Remittance
		**/
	}	//	main


}	//	MPrintFormat
