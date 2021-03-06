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
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Persistet Object Info.
 *  Provides structural information
 *
 *  @author Jorg Janke
 *  @version $Id: POInfo.java,v 1.35 2005/10/26 00:38:16 jjanke Exp $
 */
public class POInfo implements Serializable
{
	/** Used by Remote FinReport			*/
	static final long serialVersionUID = -5976719579744948419L;

	/**
	 *  POInfo Factory
	 *  @param ctx context
	 *  @param AD_Table_ID AD_Table_ID
	 *  @return POInfo
	 */
	public static POInfo getPOInfo (Properties ctx, int AD_Table_ID)
	{
		Integer key = new Integer(AD_Table_ID);
		POInfo retValue = (POInfo)s_cache.get(key);
		if (retValue == null)
		{
			retValue = new POInfo(ctx, AD_Table_ID, false);
			if (retValue.getColumnCount() == 0)
				//	May be run before Language verification
				retValue = new POInfo(ctx, AD_Table_ID, true);
			else
				s_cache.put(key, retValue);
		}
		return retValue;
	}   //  getPOInfo

	/** Cache of POInfo     */
	private static CCache<Integer,POInfo>  s_cache = new CCache<Integer,POInfo>("POInfo", 200);
	
	/**************************************************************************
	 *  Create Persistent Info
	 *  @param ctx context
	 *  @param AD_Table_ID AD_ Table_ID
	 * 	@param baseLanguageOnly get in base language
	 */
	private POInfo (Properties ctx, int AD_Table_ID, boolean baseLanguageOnly)
	{
		m_ctx = ctx;
		m_AD_Table_ID = AD_Table_ID;
		boolean baseLanguage = baseLanguageOnly ? true : Env.isBaseLanguage(m_ctx, "AD_Table");
		loadInfo (baseLanguage);
	}   //  PInfo

	/** Context             	*/
	private Properties  m_ctx = null;
	/** Table_ID            	*/
	private int         m_AD_Table_ID = 0;
	/** Table Name          	*/
	private String      m_TableName = null;
	/** Access Level			*/
	private String		m_AccessLevel = M_Table.ACCESSLEVEL_Organization;
	/** Columns             	*/
	private POInfoColumn[]    m_columns = null;
	/** Table has Key Column	*/ 
	private boolean		m_hasKeyColumn = false;
	

	/**
	 *  Load Table/Column Info
	 * 	@param baseLanguage in English
	 */
	private void loadInfo (boolean baseLanguage)
	{
		ArrayList<POInfoColumn> list = new ArrayList<POInfoColumn>(15);
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t.TableName, c.ColumnName,c.AD_Reference_ID,"    //  1..3
			+ "c.IsMandatory,c.IsUpdateable,c.DefaultValue,"                //  4..6
			+ "e.Name,e.Description, c.AD_Column_ID, "						//  7..9
			+ "c.IsKey,c.IsParent, "										//	10..11
			+ "c.AD_Reference_Value_ID, vr.Code, "							//	12..13
			+ "c.FieldLength, c.ValueMin, c.ValueMax, c.IsTranslated, "		//	14..17
			+ "t.AccessLevel, c.ColumnSQL, c.IsEncrypted ");				//	18..20
		sql.append("FROM AD_Table t"
			+ " INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID)"
			+ " LEFT OUTER JOIN AD_Val_Rule vr ON (c.AD_Val_Rule_ID=vr.AD_Val_Rule_ID)"
			+ " INNER JOIN AD_Element");
		if (!baseLanguage)
			sql.append("_Trl");
		sql.append(" e "
			+ " ON (c.AD_Element_ID=e.AD_Element_ID) "
			+ "WHERE t.AD_Table_ID=?"
			+ " AND c.IsActive='Y'");
		if (!baseLanguage)
			sql.append(" AND e.AD_Language='").append(Env.getAD_Language(m_ctx)).append("'");
		//
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, m_AD_Table_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				if (m_TableName == null)
					m_TableName = rs.getString(1);
				String ColumnName = rs.getString(2);
				int AD_Reference_ID = rs.getInt(3);
				boolean IsMandatory = "Y".equals(rs.getString(4));
				boolean IsUpdateable = "Y".equals(rs.getString(5));
				String DefaultLogic = rs.getString(6);
				String Name = rs.getString(7);
				String Description = rs.getString(8);
				int AD_Column_ID = rs.getInt(9);
				boolean IsKey = "Y".equals(rs.getString(10));
				if (IsKey)
					m_hasKeyColumn = true;
				boolean IsParent = "Y".equals(rs.getString(11));
				int AD_Reference_Value_ID = rs.getInt(12);
				String ValidationCode = rs.getString(13);
				int FieldLength = rs.getInt(14);
				String ValueMin = rs.getString(15);
				String ValueMax = rs.getString(16);
				boolean IsTranslated = "Y".equals(rs.getString(17));
				//
				m_AccessLevel = rs.getString(18);
				String ColumnSQL = rs.getString(19);
				boolean IsEncrypted = "Y".equals(rs.getString(20));

				POInfoColumn col = new POInfoColumn (
					AD_Column_ID, ColumnName, ColumnSQL, AD_Reference_ID,
					IsMandatory, IsUpdateable,
					DefaultLogic, Name, Description,
					IsKey, IsParent,
					AD_Reference_Value_ID, ValidationCode,
					FieldLength, ValueMin, ValueMax,
					IsTranslated, IsEncrypted);
				list.add(col);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			CLogger.get().log(Level.SEVERE, sql.toString(), e);
		}
		//  convert to array
		m_columns = new POInfoColumn[list.size()];
		list.toArray(m_columns);
	}   //  loadInfo

	/**
	 *  String representation
	 *  @return String Representation
	 */
	public String toString()
	{
		return "POInfo[" + getTableName() + ",AD_Table_ID=" + getAD_Table_ID() + "]";
	}   //  toString

	/**
	 *  String representation for index
	 * 	@param index column index
	 *  @return String Representation
	 */
	public String toString (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return "POInfo[" + getTableName() + "-(InvalidColumnIndex=" + index + ")]";
		return "POInfo[" + getTableName() + "-" + m_columns[index].toString() + "]";
	}   //  toString

	/**
	 *  Get Table Name
	 *  @return Table Name
	 */
	public String getTableName()
	{
		return m_TableName;
	}   //  getTableName

	/**
	 *  Get AD_Table_ID
	 *  @return AD_Table_ID
	 */
	public int getAD_Table_ID()
	{
		return m_AD_Table_ID;
	}   //  getAD_Table_ID

	/**
	 * 	Table has a Key Column
	 *	@return true if has a key column
	 */
	public boolean hasKeyColumn()
	{
		return m_hasKeyColumn;
	}	//	hasKeyColumn

	/**
	 * 	Get Table Access Level
	 *	@return M_Table.ACCESS..
	 */
	public String getAccessLevel()
	{
		return m_AccessLevel;
	}	//	getAccessLevel
	
	/**************************************************************************
	 *  Get ColumnCount
	 *  @return column count
	 */
	public int getColumnCount()
	{
		return m_columns.length;
	}   //  getColumnCount

	/**
	 *  Get Column Index
	 *  @param ColumnName column name
	 *  @return index of column with ColumnName or -1 if not found
	 */
	public int getColumnIndex (String ColumnName)
	{
		for (int i = 0; i < m_columns.length; i++)
		{
			if (ColumnName.equals(m_columns[i].ColumnName))
				return i;
		}
		return -1;
	}   //  getColumnIndex

	/**
	 *  Get Column Index
	 *  @param AD_Column_ID column
	 *  @return index of column with ColumnName or -1 if not found
	 */
	public int getColumnIndex (int AD_Column_ID)
	{
		for (int i = 0; i < m_columns.length; i++)
		{
			if (AD_Column_ID == m_columns[i].AD_Column_ID)
				return i;
		}
		return -1;
	}   //  getColumnIndex

	/**
	 *  Get Column
	 *  @param index index
	 *  @return column
	 */
	protected POInfoColumn getColumn (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return null;
		return m_columns[index];
	}   //  getColumn

	/**
	 *  Get Column Name
	 *  @param index index
	 *  @return ColumnName column name
	 */
	public String getColumnName (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return null;
		return m_columns[index].ColumnName;
	}   //  getColumnName

	/**
	 *  Get Column SQL or Column Name
	 *  @param index index
	 *  @return ColumnSQL column sql or name
	 */
	public String getColumnSQL (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return null;
		if (m_columns[index].ColumnSQL != null && m_columns[index].ColumnSQL.length() > 0)
			return m_columns[index].ColumnSQL + " AS " + m_columns[index].ColumnName;
		return m_columns[index].ColumnName;
	}   //  getColumnSQL

	/**
	 *  Is Column Virtal?
	 *  @param index index
	 *  @return true if column is virtual
	 */
	public boolean isVirtualColumn (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return true;
		return m_columns[index].ColumnSQL != null 
			&& m_columns[index].ColumnSQL.length() > 0;
	}   //  isVirtualColumn

	/**
	 *  Get Column Label
	 *  @param index index
	 *  @return column label
	 */
	public String getColumnLabel (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return null;
		return m_columns[index].ColumnLabel;
	}   //  getColumnLabel

	/**
	 *  Get Column Description
	 *  @param index index
	 *  @returncolumn description
	 */
	public String getColumnDescription (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return null;
		return m_columns[index].ColumnDescription;
	}   //  getColumnDescription

	/**
	 *  Get Column Class
	 *  @param index index
	 *  @return Class
	 */
	public Class getColumnClass (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return null;
		return m_columns[index].ColumnClass;
	}   //  getColumnClass

	/**
	 *  Get Column Display Type
	 *  @param index index
	 *  @return DisplayType
	 */
	public int getColumnDisplayType (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return DisplayType.String;
		return m_columns[index].DisplayType;
	}   //  getColumnDisplayType

	/**
	 *  Get Column Default Logic
	 *  @param index index
	 *  @return Default Logic
	 */
	public String getDefaultLogic (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return null;
		return m_columns[index].DefaultLogic;
	}   //  getDefaultLogic

	/**
	 *  Is Column Mandatory
	 *  @param index index
	 *  @return true if column mandatory
	 */
	public boolean isColumnMandatory (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return false;
		return m_columns[index].IsMandatory;
	}   //  isMandatory

	/**
	 *  Is Column Updateable
	 *  @param index index
	 *  @return true if column updateable
	 */
	public boolean isColumnUpdateable (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return false;
		return m_columns[index].IsUpdateable;
	}   //  isUpdateable

	/**
	 *  Set Column Updateable
	 *  @param index index
	 *  @param updateable column updateable
	 */
	public void setColumnUpdateable (int index, boolean updateable)
	{
		if (index < 0 || index >= m_columns.length)
			return;
		m_columns[index].IsUpdateable = updateable;
	}	//	setColumnUpdateable

	/**
	 * 	Set all columns updateable
	 * 	@param updateable updateable
	 */
	public void setUpdateable (boolean updateable)
	{
		for (int i = 0; i < m_columns.length; i++)
			m_columns[i].IsUpdateable = updateable;
	}	//	setUpdateable

	/**
	 *  Is Lookup Column
	 *  @param index index
	 *  @return true if it is a lookup column
	 */
	public boolean isColumnLookup (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return false;
		return DisplayType.isLookup(m_columns[index].DisplayType);
	}   //  isColumnLookup

	/**
	 *  Get Lookup
	 *  @param index index
	 *  @return Lookup
	 */
	public Lookup getColumnLookup (int index)
	{
		if (!isColumnLookup(index))
			return null;
		//
		int WindowNo = 0;
		//  List, Table, TableDir
		Lookup lookup = null;
		try
		{
			lookup = MLookupFactory.get (m_ctx, WindowNo,
				m_columns[index].AD_Column_ID, m_columns[index].DisplayType,
				Env.getLanguage(m_ctx), m_columns[index].ColumnName,
				m_columns[index].AD_Reference_Value_ID,
				m_columns[index].IsParent, m_columns[index].ValidationCode);
		}
		catch (Exception e)
		{
			lookup = null;          //  cannot create Lookup
		}
		return lookup;
		/** @todo other lookup types */
	}   //  getColumnLookup

	/**
	 *  Is Column Key
	 *  @param index index
	 *  @return true if column is the key
	 */
	public boolean isKey (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return false;
		return m_columns[index].IsKey;
	}   //  isKey

	/**
	 *  Is Column Parent
	 *  @param index index
	 *  @return true if column is a Parent
	 */
	public boolean isColumnParent (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return false;
		return m_columns[index].IsParent;
	}   //  isColumnParent

	/**
	 *  Is Column Translated
	 *  @param index index
	 *  @return true if column is translated
	 */
	public boolean isColumnTranslated (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return false;
		return m_columns[index].IsTranslated;
	}   //  isColumnTranslated

	/**
	 *  Is Table Translated
	 *  @return true if table is translated
	 */
	public boolean isTranslated ()
	{
		for (int i = 0; i < m_columns.length; i++)
		{
			if (m_columns[i].IsTranslated)
				return true;
		}
		return false;
	}   //  isTranslated

	/**
	 *  Is Column (data) Encrypted
	 *  @param index index
	 *  @return true if column is encrypted
	 */
	public boolean isEncrypted (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return false;
		return m_columns[index].IsEncrypted;
	}   //  isEncrypted

	/**
	 *  Get Column FieldLength
	 *  @param index index
	 *  @return field length
	 */
	public int getFieldLength (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return 0;
		return m_columns[index].FieldLength;
	}   //  getFieldLength

	/**
	 *  Validate Content
	 *  @param index index
	 * 	@param value new Value
	 *  @return null if all valid otherwise error message
	 */
	public String validate (int index, Object value)
	{
		if (index < 0 || index >= m_columns.length)
			return "RangeError";
		//	Mandatory (i.e. not null
		if (m_columns[index].IsMandatory && value == null)
		{
			return "IsMandatory";
		}
		if (value == null)
			return null;
		
		//	Length ignored

		//
		if (m_columns[index].ValueMin != null)
		{
			BigDecimal value_BD = null;
			try
			{
				if (m_columns[index].ValueMin_BD != null)
					value_BD = new BigDecimal(value.toString());
			}
			catch (Exception ex){}
			//	Both are Numeric
			if (m_columns[index].ValueMin_BD != null && value_BD != null)
			{	//	error: 1 - 0 => 1  -  OK: 1 - 1 => 0 & 1 - 10 => -1
				int comp = m_columns[index].ValueMin_BD.compareTo(value_BD);
				if (comp > 0)
				{
					return "MinValue=" + m_columns[index].ValueMin_BD + "(" + m_columns[index].ValueMin + ")"
					  + " - compared with Numeric Value=" + value_BD + "(" + value + ")"
					  + " - results in " + comp;
				}
			}
			else	//	String
			{
				int comp = m_columns[index].ValueMin.compareTo(value.toString());
				if (comp > 0)
				{
					return "MinValue=" + m_columns[index].ValueMin
					  + " - compared with String Value=" + value
					  + " - results in " + comp;
				}
			}
		}
		if (m_columns[index].ValueMax != null)
		{
			BigDecimal value_BD = null;
			try
			{
				if (m_columns[index].ValueMax_BD != null)
					value_BD = new BigDecimal(value.toString());
			}
			catch (Exception ex){}
			//	Both are Numeric
			if (m_columns[index].ValueMax_BD != null && value_BD != null)
			{	//	error 12 - 20 => -1  -  OK: 12 - 12 => 0 & 12 - 10 => 1
				int comp = m_columns[index].ValueMax_BD.compareTo(value_BD);
				if (comp < 0)
				{
					return "MaxValue=" + m_columns[index].ValueMax_BD + "(" + m_columns[index].ValueMax + ")"
					  + " - compared with Numeric Value=" + value_BD + "(" + value + ")"
					  + " - results in " + comp;
				}
			}
			else	//	String
			{
				int comp = m_columns[index].ValueMax.compareTo(value.toString());
				if (comp < 0)
				{
					return "MaxValue=" + m_columns[index].ValueMax
					  + " - compared with String Value=" + value
					  + " - results in " + comp;
				}
			}
		}
		return null;
	}   //  validate

}   //  POInfo
