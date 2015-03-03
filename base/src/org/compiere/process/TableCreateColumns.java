/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.process;

import java.sql.*;
import java.util.logging.*;
import org.compiere.db.*;
import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Create Columns of Table or View
 *	
 *  @author Jorg Janke
 *  @version $Id: TableCreateColumns.java,v 1.7 2005/09/19 04:49:45 jjanke Exp $
 */
public class TableCreateColumns extends SvrProcess
{
	/** Entity Type			*/
	private String	p_EntityType = M_Element.ENTITYTYPE_Customization;
	/** Table				*/
	private int		p_AD_Table_ID = 0;
	/** CheckAllDBTables	*/
	private boolean	p_AllTables = false;
	
	/** Column Count	*/
	private int 	m_count = 0;

	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("EntityType"))
				p_EntityType = (String)para[i].getParameter();
			else if (name.equals("AllTables"))
				p_AllTables = "Y".equals(para[i].getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_AD_Table_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		if (p_AD_Table_ID == 0)
			throw new CompiereSystemError("@NotFound@ @AD_Table_ID@ " + p_AD_Table_ID);
		log.info("EntityType=" + p_EntityType
			+ ", AllTables=" + p_AllTables
			+ ", AD_Table_ID" + p_AD_Table_ID);
		//
		Connection conn = DB.getConnectionRO();
		CompiereDatabase db = DB.getDatabase();
		DatabaseMetaData md = conn.getMetaData();
		String catalog = db.getCatalog();
		String schema = db.getSchema();
		
		if (p_AllTables)
			addTable (md, catalog, schema);
		else
		{
			M_Table table = new M_Table (getCtx(), p_AD_Table_ID, get_TrxName());
			if (table == null || table.get_ID() == 0)
				throw new CompiereSystemError("@NotFound@ @AD_Table_ID@ " + p_AD_Table_ID);
			log.info(table.getTableName() + ", EntityType=" + p_EntityType);
			String tableName = table.getTableName();
			if (DB.isOracle())
				tableName = tableName.toUpperCase();
			ResultSet rs = md.getColumns(catalog, schema, tableName, null);
			addTableColumn(rs, table);
		}
		
		return "#" + m_count;
	}	//	doIt

	/**
	 * 	Add Table
	 *	@param md meta data
	 *	@param catalog catalog
	 *	@param schema schema
	 *	@throws Exception
	 */
	private void addTable (DatabaseMetaData md, String catalog, String schema) throws Exception
	{
		ResultSet rs = md.getTables(catalog, schema, null, null);
		while (rs.next())
		{
			String tableName = rs.getString("TABLE_NAME");
			String tableType = rs.getString("TABLE_TYPE");
			
			//	Try to find
			M_Table table = M_Table.get(getCtx(), tableName);
			//	Create new ?
			if (table == null)
			{
				String tn = tableName.toUpperCase();
				if (tn.startsWith("T_SELECTION")	//	temp table
					|| tn.endsWith("_VT")			//	print trl views
					|| tn.endsWith("_V")			//	views
					|| tn.endsWith("_V1")			//	views
					|| tn.startsWith("A_A")			//	asset tables not yet
					|| tn.startsWith("A_D")			//	asset tables not yet
					|| tn.indexOf("$") != -1		//	oracle system tables
					|| tn.indexOf("EXPLAIN") != -1	//	explain plan
					)
				{
					log.fine("Ignored: " + tableName + " - " + tableType);
					continue;
				}
				//
				log.info(tableName + " - " + tableType);
				
				//	Create New
				table = new M_Table(getCtx(), 0, get_TrxName());
				table.setEntityType (p_EntityType);
				table.setName (tableName);
				table.setTableName (tableName);
				table.setIsView("VIEW".equals(tableType));
				if (!table.save())
					continue;
			}
			//	Check Columns
			if (DB.isOracle())
				tableName = tableName.toUpperCase();
			ResultSet rsC = md.getColumns(catalog, schema, tableName, null);
			addTableColumn(rsC, table);
		}
	}	//	addTable
	
	
	/**
	 * 	Add Table Column
	 *	@param rs result set with meta data
	 *	@param table table
	 *	@throws Exception
	 */
	private void addTableColumn (ResultSet rs, M_Table table) throws Exception
	{
		String tableName = table.getTableName ();
		if (DB.isOracle ())
			tableName = tableName.toUpperCase ();
		//
		while (rs.next ())
		{
			String tn = rs.getString ("TABLE_NAME");
			if (!tableName.equalsIgnoreCase (tn))
				continue;
			String columnName = rs.getString ("COLUMN_NAME");
			M_Column column = table.getColumn (columnName);
			if (column != null)
				continue;
			int dataType = rs.getInt ("DATA_TYPE");
			String typeName = rs.getString ("TYPE_NAME");
			String nullable = rs.getString ("IS_NULLABLE");
			int size = rs.getInt ("COLUMN_SIZE");
			int digits = rs.getInt ("DECIMAL_DIGITS");
			//
			log.config (columnName + " - DataType=" + dataType + " " + typeName
				+ ", Nullable=" + nullable + ", Size=" + size + ", Digits="
				+ digits);
			//
			column = new M_Column (table);
			column.setEntityType (p_EntityType);
			//
			M_Element element = M_Element.get (getCtx (), columnName);
			if (element == null)
			{
				element = new M_Element (getCtx (), columnName, p_EntityType,
					get_TrxName ());
				element.save ();
			}
			column.setColumnName (element.getColumnName ());
			column.setName (element.getName ());
			column.setDescription (element.getDescription ());
			column.setHelp (element.getHelp ());
			column.setAD_Element_ID (element.getAD_Element_ID ());
			//
			column.setIsMandatory ("NO".equals (nullable));
			// Key
			if (columnName.equalsIgnoreCase (tableName + "_ID"))
			{
				column.setIsKey (true);
				column.setAD_Reference_ID (DisplayType.ID);
				column.setIsUpdateable(false);
			}
			// Account
			else if (columnName.toUpperCase ().indexOf ("ACCT") != -1
				&& size == 10)
				column.setAD_Reference_ID (DisplayType.Account);
			// Account
			else if (columnName.equalsIgnoreCase ("C_Location_ID"))
				column.setAD_Reference_ID (DisplayType.Location);
			// Product Attribute
			else if (columnName.equalsIgnoreCase ("M_AttributeSetInstance_ID"))
				column.setAD_Reference_ID (DisplayType.PAttribute);
			// SalesRep_ID (=User)
			else if (columnName.equalsIgnoreCase ("SalesRep_ID"))
			{
				column.setAD_Reference_ID (DisplayType.Table);
				column.setAD_Reference_Value_ID (190);
			}
			// ID
			else if (columnName.endsWith ("_ID"))
				column.setAD_Reference_ID (DisplayType.TableDir);
			// Date
			else if (dataType == Types.DATE || dataType == Types.TIME
				|| dataType == Types.TIMESTAMP
				// || columnName.toUpperCase().indexOf("DATE") != -1
				|| columnName.equalsIgnoreCase ("Created")
				|| columnName.equalsIgnoreCase ("Updated"))
				column.setAD_Reference_ID (DisplayType.DateTime);
			// CreatedBy/UpdatedBy (=User)
			else if (columnName.equalsIgnoreCase ("CreatedBy")
				|| columnName.equalsIgnoreCase ("UpdatedBy"))
			{
				column.setAD_Reference_ID (DisplayType.Table);
				column.setAD_Reference_Value_ID (110);
				column.setIsUpdateable(false);
			}
			// CLOB
			else if (dataType == Types.CLOB)
				column.setAD_Reference_ID (DisplayType.TextLong);
			// BLOB
			else if (dataType == Types.BLOB)
				column.setAD_Reference_ID (DisplayType.Binary);
			// Amount
			else if (columnName.toUpperCase ().indexOf ("AMT") != -1)
				column.setAD_Reference_ID (DisplayType.Amount);
			// Qty
			else if (columnName.toUpperCase ().indexOf ("QTY") != -1)
				column.setAD_Reference_ID (DisplayType.Quantity);
			// Boolean
			else if (size == 1
				&& (columnName.toUpperCase ().startsWith ("IS") || dataType == Types.CHAR))
				column.setAD_Reference_ID (DisplayType.YesNo);
			// List
			else if (size < 4 && dataType == Types.CHAR)
				column.setAD_Reference_ID (DisplayType.List);
			// Name, DocumentNo
			else if (columnName.equalsIgnoreCase ("Name")
				|| columnName.equals ("DocumentNo"))
			{
				column.setAD_Reference_ID (DisplayType.String);
				column.setIsIdentifier (true);
				column.setSeqNo (1);
			}
			// String, Text
			else if (dataType == Types.CHAR || dataType == Types.VARCHAR
				|| typeName.startsWith ("NVAR")
				|| typeName.startsWith ("NCHAR"))
			{
				if (typeName.startsWith("N"))	//	MultiByte	
					size /= 2;
				if (size > 255)
					column.setAD_Reference_ID (DisplayType.Text);
				else
					column.setAD_Reference_ID (DisplayType.String);
			}
			// Number
			else if (dataType == Types.INTEGER || dataType == Types.SMALLINT
				|| dataType == Types.DECIMAL || dataType == Types.NUMERIC)
			{
				if (size == 10)
					column.setAD_Reference_ID (DisplayType.Integer);
				else
					column.setAD_Reference_ID (DisplayType.Number);
			}
			//	??
			else
				column.setAD_Reference_ID (DisplayType.String);
			
			column.setFieldLength (size);
			if (column.isUpdateable()
				&& (table.isView()
					|| columnName.equalsIgnoreCase("AD_Client_ID") 
					|| columnName.equalsIgnoreCase("AD_Org_ID")
					|| columnName.toUpperCase().startsWith("CREATED") 
					|| columnName.toUpperCase().equals("UPDATED") ))
				column.setIsUpdateable(false);

			//	Done
			if (column.save ())
			{
				addLog (0, null, null, table.getTableName() + "." + column.getColumnName());
				m_count++;
			}
		}	//	while columns
		
	}	//	addTableColumn

	
}	//	TableCreateColumns
