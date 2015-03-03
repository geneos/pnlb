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
package org.compiere.acct;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;

import org.compiere.model.*;
import org.compiere.report.core.*;
import org.compiere.util.*;


/**
 *  Account Viewer State - maintaines State information for the Account Viewer
 *
 *  @author Jorg Janke
 *  @version  $Id: AcctViewerData.java,v 1.27 2005/10/26 00:37:50 jjanke Exp $
 */
class AcctViewerData
{
	/**
	 *  Constructor
	 *	@param ctx context
	 *  @param windowNo window no
	 *  @param ad_Client_ID client
	 * 	@param ad_Table_ID table
	 */
	public AcctViewerData (Properties ctx, int windowNo, int ad_Client_ID, int ad_Table_ID)
	{
		WindowNo = windowNo;
		AD_Client_ID = ad_Client_ID;
		if (AD_Client_ID == 0)
			AD_Client_ID = Env.getContextAsInt(Env.getCtx(), WindowNo, "AD_Client_ID");
		if (AD_Client_ID == 0)
			AD_Client_ID = Env.getContextAsInt(Env.getCtx(), "AD_Client_ID");
		AD_Table_ID = ad_Table_ID;
		//
		ASchemas = MAcctSchema.getClientAcctSchema(ctx, AD_Client_ID);
		ASchema = ASchemas[0];
	}   //  AcctViewerData

	/** Window              */
	public int              WindowNo;
	public int              AD_Client_ID;

	public MAcctSchema[]    ASchemas = null;
	public MAcctSchema      ASchema = null;

	//  Selection Info
	public boolean          documentQuery = false;
	public int              C_AcctSchema_ID = 0;
	public String			PostingType = "";
	public int              AD_Org_ID = 0;
	public Timestamp        DateFrom = null;
	public Timestamp        DateTo = null;

	//  Table Selection Info
	public int              AD_Table_ID;
	public int              Record_ID;

	/** Containing Column and Query     */
	public HashMap<String,String>	whereInfo = new HashMap<String,String>();
	/** Containing TableName and AD_Table_ID    */
	public HashMap<String,Integer>	tableInfo = new HashMap<String,Integer>();

	//  Display Info
	public boolean          displayQty = false;
	public boolean          displaySourceAmt = false;
	public boolean          displayDocumentInfo = false;
	//
	public String           sortBy1 = "";
	public String           sortBy2 = "";
	public String           sortBy3 = "";
	public String           sortBy4 = "";
	//
	public boolean          group1 = false;
	public boolean          group2 = false;
	public boolean          group3 = false;
	public boolean          group4 = false;

	private int				m_leadingColumns = 0;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(AcctViewerData.class);

	/**
	 *  Dispose
	 */
	public void dispose()
	{
		ASchemas = null;
		ASchema = null;
		//
		whereInfo.clear();
		whereInfo = null;
		//
		Env.clearWinContext(WindowNo);
	}   //  dispose

	
	/**************************************************************************
	 *  Fill Accounting Schema
	 *  @param cb JComboBox to be filled
	 */
	protected void fillAcctSchema (JComboBox cb)
	{
		for (int i = 0; i < ASchemas.length; i++)
			cb.addItem(new KeyNamePair(ASchemas[i].getC_AcctSchema_ID(),
				ASchemas[i].getName()));
	}   //  fillAcctSchema

	/**
	 *  Fill Posting Type
	 *  @param cb JComboBox to be filled
	 */
	protected void fillPostingType (JComboBox cb)
	{
		int AD_Reference_ID = 125;
		ValueNamePair[] pt = MRefList.getList(AD_Reference_ID, true);
		for (int i = 0; i < pt.length; i++)
			cb.addItem(pt[i]);
	}   //  fillPostingType

	/**
	 *  Fill Table with
	 *      ValueNamePair (TableName, translatedKeyColumnName)
	 *  and tableInfo with (TableName, AD_Table_ID)
	 *  and select the entry for AD_Table_ID
	 *
	 *  @param cb JComboBox to be filled
	 */
	protected void fillTable (JComboBox cb)
	{
		ValueNamePair select = null;
		//
		String sql = "SELECT AD_Table_ID, TableName FROM AD_Table t "
			+ "WHERE EXISTS (SELECT * FROM AD_Column c"
			+ " WHERE t.AD_Table_ID=c.AD_Table_ID AND c.ColumnName='Posted')"
			+ " AND IsView='N'";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				int id = rs.getInt(1);
				String tableName = rs.getString(2);
				String name = Msg.translate(Env.getCtx(), tableName+"_ID");
				//
				ValueNamePair pp = new ValueNamePair(tableName, name);
				cb.addItem(pp);
				tableInfo.put (tableName, new Integer(id));
				if (id == AD_Table_ID)
					select = pp;
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		if (select != null)
			cb.setSelectedItem(select);
	}   //  fillTable

	/**
	 *  Fill Org
	 *
	 *  @param cb JComboBox to be filled
	 */
	protected void fillOrg (JComboBox cb)
	{
		KeyNamePair pp = new KeyNamePair(0, "");
		cb.addItem(pp);
		String sql = "SELECT AD_Org_ID, Name FROM AD_Org WHERE AD_Client_ID=? ORDER BY Value";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				cb.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}   //  fillOrg

	/**
	 *  Get Button Text
	 *
	 *  @param tableName table
	 *  @param columnName column
	 *  @param selectSQL sql
	 *  @return Text on button
	 */
	protected String getButtonText (String tableName, String columnName, String selectSQL)
	{
		//  SELECT (<embedded>) FROM tableName avd WHERE avd.<selectSQL>
		StringBuffer sql = new StringBuffer ("SELECT (");
		Language language = Env.getLanguage(Env.getCtx());
		sql.append(MLookupFactory.getLookup_TableDirEmbed(language, columnName, "avd"))
			.append(") FROM ").append(tableName).append(" avd WHERE avd.").append(selectSQL);
		String retValue = "<" + selectSQL + ">";
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql.toString());
			if (rs.next())
				retValue = rs.getString(1);
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		return retValue;
	}   //  getButtonText

	/**************************************************************************

	/**
	 *  Create Query and submit
	 *  @return Report Model
	 */
	protected RModel query()
	{
		//  Set Where Clause
		StringBuffer whereClause = new StringBuffer();
		//  Add Organization
		if (C_AcctSchema_ID != 0)
			whereClause.append(RModel.TABLE_ALIAS)
				.append(".C_AcctSchema_ID=").append(C_AcctSchema_ID);

		//	Posting Type Selected
		if (PostingType != null && PostingType.length() > 0)
		{
			if (whereClause.length() > 0)
				whereClause.append(" AND ");
			whereClause.append(RModel.TABLE_ALIAS)
				.append(".PostingType='").append(PostingType).append("'");
		}
		
		//
		if (documentQuery)
		{
			if (whereClause.length() > 0)
				whereClause.append(" AND ");
			whereClause.append(RModel.TABLE_ALIAS).append(".AD_Table_ID=").append(AD_Table_ID)
				.append(" AND ").append(RModel.TABLE_ALIAS).append(".Record_ID=").append(Record_ID);
		}
		else
		{
			//  get values (Queries)
			Iterator it = whereInfo.values().iterator();
			while (it.hasNext())
			{
				String where = (String)it.next();
				if (where != null && where.length() > 0)    //  add only if not empty
				{
					if (whereClause.length() > 0)
						whereClause.append(" AND ");
					whereClause.append(RModel.TABLE_ALIAS).append(".").append(where);
				}
			}
			if (DateFrom != null || DateTo != null)
			{
				if (whereClause.length() > 0)
					whereClause.append(" AND ");
				if (DateFrom != null && DateTo != null)
					whereClause.append("TRUNC(").append(RModel.TABLE_ALIAS).append(".DateAcct) BETWEEN ")
						.append(DB.TO_DATE(DateFrom)).append(" AND ").append(DB.TO_DATE(DateTo));
				else if (DateFrom != null)
					whereClause.append("TRUNC(").append(RModel.TABLE_ALIAS).append(".DateAcct) >= ")
						.append(DB.TO_DATE(DateFrom));
				else    //  DateTo != null
					whereClause.append("TRUNC(").append(RModel.TABLE_ALIAS).append(".DateAcct) <= ")
						.append(DB.TO_DATE(DateTo));
			}
			//  Add Organization
			if (AD_Org_ID != 0)
			{
				if (whereClause.length() > 0)
					whereClause.append(" AND ");
				whereClause.append(RModel.TABLE_ALIAS).append(".AD_Org_ID=").append(AD_Org_ID);
			}
		}

		//  Set Order By Clause
		StringBuffer orderClause = new StringBuffer();
		if (sortBy1.length() > 0)
			orderClause.append(RModel.TABLE_ALIAS).append(".").append(sortBy1);
		if (sortBy2.length() > 0)
		{
			if (orderClause.length() > 0)
				orderClause.append(",");
			orderClause.append(RModel.TABLE_ALIAS).append(".").append(sortBy2);
		}
		if (sortBy3.length() > 0)
		{
			if (orderClause.length() > 0)
				orderClause.append(",");
			orderClause.append(RModel.TABLE_ALIAS).append(".").append(sortBy3);
		}
		if (sortBy4.length() > 0)
		{
			if (orderClause.length() > 0)
				orderClause.append(",");
			orderClause.append(RModel.TABLE_ALIAS).append(".").append(sortBy4);
		}
		if (orderClause.length() == 0)
			orderClause.append(RModel.TABLE_ALIAS).append(".Fact_Acct_ID");

		RModel rm = getRModel();

		//  Groups
		if (group1 && sortBy1.length() > 0)
			rm.setGroup(sortBy1);
		if (group2 && sortBy2.length() > 0)
			rm.setGroup(sortBy2);
		if (group3 && sortBy3.length() > 0)
			rm.setGroup(sortBy3);
		if (group4 && sortBy4.length() > 0)
			rm.setGroup(sortBy4);

		//  Totals
		rm.setFunction("AmtAcctDr", RModel.FUNCTION_SUM);
		rm.setFunction("AmtAcctCr", RModel.FUNCTION_SUM);

		rm.query (Env.getCtx(), whereClause.toString(), orderClause.toString());

		return rm;
	}   //  query

	/**
	 *  Create Report Model (Columns)
	 *  @return Report Model
	 */
	private RModel getRModel()
	{
		Properties ctx = Env.getCtx();
		RModel rm = new RModel("Fact_Acct");
		//  Add Key (Lookups)
		ArrayList keys = createKeyColumns();
		int max = m_leadingColumns;
		if (max == 0)
			max = keys.size();
		for (int i = 0; i < max; i++)
		{
			String column = (String)keys.get(i);
			if (column != null && column.startsWith("Date"))
				rm.addColumn(new RColumn(ctx, column, DisplayType.Date));
			else if (column != null && column.endsWith("_ID"))
				rm.addColumn(new RColumn(ctx, column, DisplayType.TableDir));
		}
		//  Main Info
		rm.addColumn(new RColumn(ctx, "AmtAcctDr", DisplayType.Amount));
		rm.addColumn(new RColumn(ctx, "AmtAcctCr", DisplayType.Amount));
		if (displaySourceAmt)
		{
			if (!keys.contains("DateTrx"))
				rm.addColumn(new RColumn(ctx, "DateTrx", DisplayType.Date));
			rm.addColumn(new RColumn(ctx, "C_Currency_ID", DisplayType.TableDir));
			rm.addColumn(new RColumn(ctx, "AmtSourceDr", DisplayType.Amount));
			rm.addColumn(new RColumn(ctx, "AmtSourceCr", DisplayType.Amount));
			rm.addColumn(new RColumn(ctx, "Rate", DisplayType.Amount,
				"CASE WHEN (AmtSourceDr + AmtSourceCr) = 0 THEN 0"
				+ " ELSE (AmtAcctDr + AmtAcctCr) / (AmtSourceDr + AmtSourceCr) END"));
		}
		//	Remaining Keys
		for (int i = max; i < keys.size(); i++)
		{
			String column = (String)keys.get(i);
			if (column != null && column.startsWith("Date"))
				rm.addColumn(new RColumn(ctx, column, DisplayType.Date));
			else if (column != null && column.endsWith("_ID"))
				rm.addColumn(new RColumn(ctx, column, DisplayType.TableDir));
		}
		//	Info
		if (!keys.contains("DateAcct"))
			rm.addColumn(new RColumn(ctx, "DateAcct", DisplayType.Date));
		if (!keys.contains("C_Period_ID"))
			rm.addColumn(new RColumn(ctx, "C_Period_ID", DisplayType.TableDir));
		if (displayQty)
		{
			rm.addColumn(new RColumn(ctx, "C_UOM_ID", DisplayType.TableDir));
			rm.addColumn(new RColumn(ctx, "Qty", DisplayType.Quantity));
		}
		if (displayDocumentInfo)
		{
			rm.addColumn(new RColumn(ctx, "AD_Table_ID", DisplayType.TableDir));
			rm.addColumn(new RColumn(ctx, "Record_ID", DisplayType.ID));
			rm.addColumn(new RColumn(ctx, "Description", DisplayType.String));
		}
		if (PostingType == null || PostingType.length() == 0)
			rm.addColumn(new RColumn(ctx, "PostingType", DisplayType.List, 
				MFactAcct.POSTINGTYPE_AD_Reference_ID));
		return rm;
	}   //  createRModel

	/**
	 *  Create the key columns in sequence
	 *  @return List of Key Columns
	 */
	private ArrayList createKeyColumns()
	{
		ArrayList<String> columns = new ArrayList<String>();
		m_leadingColumns = 0;
		//  Sorting Fields
		columns.add(sortBy1);               //  may add ""
		if (!columns.contains(sortBy2))
			columns.add(sortBy2);
		if (!columns.contains(sortBy3))
			columns.add(sortBy3);
		if (!columns.contains(sortBy4))
			columns.add(sortBy4);

		//  Add Account Segments
		MAcctSchemaElement[] elements = ASchema.getAcctSchemaElements();
		for (int i = 0; i < elements.length; i++)
		{
			if (m_leadingColumns == 0 && columns.contains("AD_Org_ID") && columns.contains("Account_ID"))
				m_leadingColumns = columns.size();
			//
			MAcctSchemaElement ase = elements[i];
			String columnName = ase.getColumnName();
			if (!columns.contains(columnName))
				columns.add(columnName);
		}
		if (m_leadingColumns == 0 && columns.contains("AD_Org_ID") && columns.contains("Account_ID"))
			m_leadingColumns = columns.size();
		return columns;
	}   //  createKeyColumns

}   //  AcctViewerData
