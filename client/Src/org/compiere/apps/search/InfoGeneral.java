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
package org.compiere.apps.search;

import java.awt.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import org.compiere.apps.*;
import org.compiere.minigrid.*;
import org.compiere.plaf.CompierePLAF;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *	Generic Table Search
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: InfoGeneral.java,v 1.27 2005/12/13 00:15:43 jjanke Exp $
 */
public class InfoGeneral extends Info
{
	/**
	 *	Detail Protected Constructor.
	 *
	 * 	@param frame parent
	 * 	@param modal modal
	 * 	@param WindowNo window no
	 * 	@param value QueryValue
	 * 	@param tableName table name
	 * 	@param keyColumn key column (ignored)
	 * 	@param multiSelection multiple selections
	 * 	@param whereClause where clause
	 */
	protected InfoGeneral (Frame frame, boolean modal, int WindowNo, String value,
		String tableName, String keyColumn,
		boolean multiSelection, String whereClause)
	{
		super (frame, modal, WindowNo, tableName, keyColumn, multiSelection, whereClause);
		log.info(tableName + " - " + keyColumn + " - " + whereClause);
		setTitle(Msg.getMsg(Env.getCtx(), "Info"));
		//
		statInit();
		p_loadedOK = initInfo ();
		//
		int no = p_table.getRowCount();
		setStatusLine(Integer.toString(no) + " " 
			+ Msg.getMsg(Env.getCtx(), "SearchRows_EnterQuery"), false);
		setStatusDB(Integer.toString(no));
		//	Focus
		textField1.setValue(value);
		textField1.requestFocus();
		if (value != null && value.length() > 0)
			executeQuery();
	}	//	InfoGeneral

	/**  String Array of Column Info    */
	private Info_Column[] m_generalLayout;
	/** list of query columns           */
	private ArrayList<String> 	m_queryColumns = new ArrayList<String>();

	//  Static data
	private CLabel label1 = new CLabel();
	private CTextField textField1 = new CTextField(10);
	private CLabel label2 = new CLabel();
	private CTextField textField2 = new CTextField(10);
	private CLabel label3 = new CLabel();
	private CTextField textField3 = new CTextField(10);
	private CLabel label4 = new CLabel();
	private CTextField textField4 = new CTextField(10);

	/**
	 *	Static Setup - add fields to parameterPanel (GridLayout)
	 */
	private void statInit()
	{
		label1.setLabelFor(textField1);
		label1.setText("Label1");
		label1.setHorizontalAlignment(JLabel.LEADING);
		textField1.setBackground(CompierePLAF.getInfoBackground());
		label2.setLabelFor(textField2);
		label2.setText("Label2");
		label2.setHorizontalAlignment(JLabel.LEADING);
		textField2.setBackground(CompierePLAF.getInfoBackground());
		label3.setLabelFor(textField3);
		label3.setText("Label3");
		label3.setHorizontalAlignment(JLabel.LEADING);
		textField3.setBackground(CompierePLAF.getInfoBackground());
		label4.setLabelFor(textField4);
		label4.setText("Label4");
		label4.setHorizontalAlignment(JLabel.LEADING);
		textField4.setBackground(CompierePLAF.getInfoBackground());
		//
		parameterPanel.setLayout(new ALayout());
		parameterPanel.add(label1, new ALayoutConstraint(0,0));
		parameterPanel.add(label2, null);
		parameterPanel.add(label3, null);
		parameterPanel.add(label4, null);
		//
		parameterPanel.add(textField1, new ALayoutConstraint(1,0));
		parameterPanel.add(textField2, null);
		parameterPanel.add(textField3, null);
		parameterPanel.add(textField4, null);
	}	//	statInit

	/**
	 *	General Init
	 *	@return true, if success
	 */
	private boolean initInfo ()
	{
		if (!initInfoTable())
			return false;

		//  prepare table
		StringBuffer where = new StringBuffer("IsActive='Y'");
		if (p_whereClause.length() > 0)
			where.append(" AND ").append(p_whereClause);
		prepareTable(m_generalLayout,
			p_tableName,
			where.toString(),
			"2");

		//	Set & enable Fields
		label1.setText(Msg.translate(Env.getCtx(), m_queryColumns.get(0).toString()));
		textField1.addActionListener(this);
		if (m_queryColumns.size() > 1)
		{
			label2.setText(Msg.translate(Env.getCtx(), m_queryColumns.get(1).toString()));
			textField2.addActionListener(this);
		}
		else
		{
			label2.setVisible(false);
			textField2.setVisible(false);
		}
		if (m_queryColumns.size() > 2)
		{
			label3.setText(Msg.translate(Env.getCtx(), m_queryColumns.get(2).toString()));
			textField3.addActionListener(this);
		}
		else
		{
			label3.setVisible(false);
			textField3.setVisible(false);
		}
		if (m_queryColumns.size() > 3)
		{
			label4.setText(Msg.translate(Env.getCtx(), m_queryColumns.get(3).toString()));
			textField4.addActionListener(this);
		}
		else
		{
			label4.setVisible(false);
			textField4.setVisible(false);
		}
		return true;
	}	//	initInfo


	/**
	 *	Init info with Table.
	 *	- find QueryColumns (Value, Name, ..)
	 *	- build gridController & columsn
	 *  @return true if success
	 */
	private boolean initInfoTable ()
	{
		//	Get Query Columns -------------------------------------------------
		String sql = "SELECT c.ColumnName, t.AD_Table_ID, t.TableName "
			+ "FROM AD_Table t"
			+ " INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID)"
			+ "WHERE c.AD_Reference_ID=10"
			+ " AND t.TableName=?"	//	#1
			//	Displayed in Window
			+ " AND EXISTS (SELECT * FROM AD_Field f "
				+ "WHERE f.AD_Column_ID=c.AD_Column_ID"
				+ " AND f.IsDisplayed='Y' AND f.IsEncrypted='N' AND f.ObscureType IS NULL) "
			//
			+ "ORDER BY c.IsIdentifier DESC, c.SeqNo";
		int AD_Table_ID = 0;
		String tableName = null;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setString(1, p_tableName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				m_queryColumns.add(rs.getString(1));
				if (AD_Table_ID == 0)
				{
					AD_Table_ID = rs.getInt(2);
					tableName = rs.getString(3);
				}
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return false;
		}
		//	Miminum check
		if (m_queryColumns.size() == 0)
		{
			log.log(Level.SEVERE, "No query columns found");
			return false;
		}
		log.finest("Table " + tableName + ", ID=" + AD_Table_ID 
			+ ", QueryColumns #" + m_queryColumns.size());
		//	Only 4 Query Columns
		while (m_queryColumns.size() > 4)
			m_queryColumns.remove(m_queryColumns.size()-1);
		//  Set Title
		String title = Msg.translate(Env.getCtx(), tableName + "_ID");  //  best bet
		if (title.endsWith("_ID"))
			title = Msg.translate(Env.getCtx(), tableName);             //  second best bet
		setTitle(getTitle() + " " + title);


		//	Get Display Columns -----------------------------------------------
		ArrayList<Info_Column> list = new ArrayList<Info_Column>();
		sql = "SELECT c.ColumnName, c.AD_Reference_ID, c.IsKey, f.IsDisplayed, c.AD_Reference_Value_ID "
			+ "FROM AD_Column c"
			+ " INNER JOIN AD_Table t ON (c.AD_Table_ID=t.AD_Table_ID)"
			+ " INNER JOIN AD_Tab tab ON (t.AD_Window_ID=tab.AD_Window_ID)"
			+ " INNER JOIN AD_Field f ON (tab.AD_Tab_ID=f.AD_Tab_ID AND f.AD_Column_ID=c.AD_Column_ID) "
			+ "WHERE t.AD_Table_ID=? "
			+ " AND (c.IsKey='Y' OR "
			//	+ " (f.IsDisplayed='Y' AND f.IsEncrypted='N' AND f.ObscureType IS NULL)) "
				+ " (f.IsEncrypted='N' AND f.ObscureType IS NULL)) "
			+ "ORDER BY c.IsKey DESC, f.SeqNo";
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Table_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				String columnName = rs.getString(1);
				int displayType = rs.getInt(2);
				boolean isKey = rs.getString(3).equals("Y");
				boolean isDisplayed = rs.getString(4).equals("Y");
				int AD_Reference_Value_ID = rs.getInt(5);
				//  Default
				StringBuffer colSql = new StringBuffer(columnName);
				Class colClass = null;
				//
				if (isKey)
					colClass = IDColumn.class;
				else if (!isDisplayed)
					;
				else if (displayType == DisplayType.YesNo)
					colClass = Boolean.class;
				else if (displayType == DisplayType.Amount)
					colClass = BigDecimal.class;
				else if (displayType == DisplayType.Number || displayType == DisplayType.Quantity)
					colClass = Double.class;
				else if (displayType == DisplayType.Integer)
					colClass = Integer.class;
				else if (displayType == DisplayType.String || displayType == DisplayType.Text || displayType == DisplayType.Memo)
					colClass = String.class;
				else if (DisplayType.isDate(displayType))
					colClass = Timestamp.class;
				//  ignore Binary, Button, ID, RowID
			//	else if (displayType == DisplayType.Account)
			//	else if (displayType == DisplayType.Location)
			//	else if (displayType == DisplayType.Locator)
				else if (displayType == DisplayType.List)
				{
					if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
						colSql = new StringBuffer("(SELECT l.Name FROM AD_Ref_List l WHERE l.AD_Reference_ID=")
							.append(AD_Reference_Value_ID).append(" AND l.Value='").append(columnName)
							.append("') AS ").append(columnName);
					else
						colSql = new StringBuffer("(SELECT t.Name FROM AD_Ref_List l, AD_Ref_List_Trl t "
							+ "WHERE l.AD_Ref_List_ID=t.AD_Ref_List_ID AND l.AD_Reference_ID=")
							.append(AD_Reference_Value_ID).append(" AND l.Value='").append(columnName)
							.append("' AND t.AD_Language='").append(Env.getAD_Language(Env.getCtx()))
							.append("') AS ").append(columnName);
					colClass = String.class;
				}
			//	else if (displayType == DisplayType.Table)
//				else if (displayType == DisplayType.TableDir || displayType == DisplayType.Search)
//                                {
//                                    int lengt = columnName.length();
//                                    String TableName = columnName.substring(0,(lengt-3));
//                                    colClass = String.class;
//                                    Integer ids = new Integer(colClass.toString());
//                                    colSql = new StringBuffer("(SELECT Name FROM " +TableName 
//							+ " WHERE ")
//							.append(columnName);
//                                    
//                                    
//                                }
				if (colClass != null)
				{
					list.add(new Info_Column(Msg.translate(Env.getCtx(), columnName), colSql.toString(), colClass));
					log.finest("Added Column=" + columnName);
				}
				else
					log.finest("Not Added Column=" + columnName);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return false;
		}
		if (list.size() == 0)
		{
			ADialog.error(p_WindowNo, this, "Error", "No Info Columns");
			log.log(Level.SEVERE, "No Info for AD_Table_ID=" + AD_Table_ID + " - " + sql);
			return false;
		}
		log.finest("InfoColumns #" + list.size()); 

		//  Convert ArrayList to Array
		m_generalLayout = new Info_Column[list.size()];
		list.toArray(m_generalLayout);
		return true;
	}	//	initInfoTable


	/**************************************************************************
	 *	Construct SQL Where Clause and define parameters.
	 *  (setParameters needs to set parameters)
	 *  Includes first AND
	 * 	@return where clause
	 */
	String getSQLWhere()
	{
		StringBuffer sql = new StringBuffer();
		addSQLWhere (sql, 0, textField1.getText().toUpperCase());
		addSQLWhere (sql, 1, textField2.getText().toUpperCase());
		addSQLWhere (sql, 2, textField3.getText().toUpperCase());
		addSQLWhere (sql, 3, textField4.getText().toUpperCase());
		return sql.toString();
	}	//	getSQLWhere

	/**
	 *	Add directly Query as Strings
	 * 	@param sql sql buffer
	 * 	@param index index
	 * 	@param value value
	 */
	private void addSQLWhere(StringBuffer sql, int index, String value)
	{
		if (!(value.equals("") || value.equals("%")) && index < m_queryColumns.size())
		{
			sql.append(" AND UPPER(").append(m_queryColumns.get(index).toString()).append(") LIKE '");
			sql.append(value);
			if (value.endsWith("%"))
				sql.append("'");
			else
				sql.append("%'");
		}
	}	//	addSQLWhere

	/**
	 *  Set Parameters for Query.
	 *  (as defined in getSQLWhere)
	 * 	@param pstmt statement
	 *  @param forCount for counting records
	 *  @throws SQLException
	 */
	void setParameters(PreparedStatement pstmt, boolean forCount) throws SQLException
	{
		int index = 1;
	}   //  setParameters

}	//	InfoGeneral
