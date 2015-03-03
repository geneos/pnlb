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
 * Portions created by Victor Perez are Copyright (C) 1999-2005 e-Evolution,S.C
 * Contributor(s): Victor Perez
 *****************************************************************************/
package org.compiere.process;

import java.sql.*;
import java.math.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Synchronize Column with Database
 *	
 *  @author Victor P�rez, Jorg Janke
 *  @version $Id: ColumnSync.java,v 1.4 2005/09/19 04:49:45 jjanke Exp $
 */
public class ColumnSync extends SvrProcess
{
	/** The Column				*/
	private int			p_AD_Column_ID = 0;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_AD_Column_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
		log.info("C_Column_ID=" + p_AD_Column_ID);
		if (p_AD_Column_ID == 0)
			throw new CompiereUserError("@No@ @AD_Column_ID@");
		M_Column column = new M_Column (getCtx(), p_AD_Column_ID, get_TrxName());
		if (column.get_ID() == 0)
			throw new CompiereUserError("@NotFound@ @AD_Column_ID@ " + p_AD_Column_ID);
		
		M_Table table = M_Table.get(getCtx(), column.getAD_Table_ID());
		if (table.get_ID() == 0)
			throw new CompiereUserError("@NotFound@ @AD_Table_ID@ " + column.getAD_Table_ID());
		
		//	Find Column in Database
		DatabaseMetaData md = DB.getConnectionRO().getMetaData();
		String catalog = DB.getDatabase().getCatalog();
		String schema = DB.getDatabase().getSchema();
		String tableName = table.getTableName();
		if (DB.isOracle())
			tableName = tableName.toUpperCase();
                // begin vpj-cd e-evolution 08/01/2005 PostgreSQL
                if (DB.isPostgreSQL())                
			tableName = tableName.toLowerCase();
                // end vpj-cd e-evolution 08/01/2005 PostgreSQL   
		int noColumns = 0;
		String sql = null;
		//
		ResultSet rs = md.getColumns(catalog, schema, tableName, null);
		while (rs.next())
		{
			noColumns++;
			String columnName = rs.getString ("COLUMN_NAME");
			if (!columnName.equalsIgnoreCase(column.getColumnName()))
				continue;
			
			//	update existing column
			boolean notNull = DatabaseMetaData.columnNoNulls == rs.getInt("NULLABLE");
			sql = column.getSQLModify(table, column.isMandatory() != notNull);
			break;
		}
		rs.close();
		rs = null;
		
		//	No Table
		if (noColumns == 0)
			sql = table.getSQLCreate ();
		//	No existing column
		else if (sql == null)
			sql = column.getSQLAdd(table);
		
		int no = 0;
		if (sql.indexOf(DB.SQLSTATEMENT_SEPARATOR) == -1)
		{
			no = DB.executeUpdate(sql, false, get_TrxName());
			addLog (0, null, new BigDecimal(no), sql);
		}
		else
		{
			String statements[] = sql.split(DB.SQLSTATEMENT_SEPARATOR);
			for (int i = 0; i < statements.length; i++)
			{
				int count = DB.executeUpdate(statements[i], false, get_TrxName());
				addLog (0, null, new BigDecimal(count), statements[i]);
				no += count;
			}
		}

		if (no == -1)
		{
			String msg = "@Error@ ";
			ValueNamePair pp = CLogger.retrieveError();
			if (pp != null)
				msg = pp.getName() + " - ";
			msg += sql;
			throw new CompiereUserError (msg);
		}
		return sql;		
	}	//	doIt

}	//	ColumnSync
