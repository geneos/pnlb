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
package org.compiere.process;

import java.math.*;
import java.sql.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Document Translation Sync 
 *	
 *  @author Jorg Janke
 *  @version $Id: TranslationDocSync.java,v 1.3 2005/10/26 00:37:42 jjanke Exp $
 */
public class TranslationDocSync extends SvrProcess
{
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
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception
	 */
	protected String doIt() throws Exception
	{
		MClient client = MClient.get(getCtx());
		if (client.isMultiLingualDocument())
			throw new CompiereUserError("@AD_Client_ID@: @IsMultiLingualDocument@");
		//
		log.info("" + client);
		String sql = "SELECT * FROM AD_Table "
			+ "WHERE TableName LIKE '%_Trl' AND TableName NOT LIKE 'AD%' "
			+ "ORDER BY TableName";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				processTable (new M_Table(getCtx(), rs, null), client.getAD_Client_ID());
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
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
		
		return "OK";
	}	//	doIt
	
	/**
	 * 	Process Translation Table
	 *	@param table table
	 */
	private void processTable (M_Table table, int AD_Client_ID)
	{
		StringBuffer sql = new StringBuffer();
		M_Column[] columns = table.getColumns(false);
		for (int i = 0; i < columns.length; i++)
		{
			M_Column column = columns[i];
			if (column.getAD_Reference_ID() == DisplayType.String
				|| column.getAD_Reference_ID() == DisplayType.Text)
			{
				String columnName = column.getColumnName();
				if (sql.length() != 0)
					sql.append(",");
				sql.append(columnName);
			}
		}
		String baseTable = table.getTableName();
		baseTable = baseTable.substring(0, baseTable.length()-4);
		
		log.config(baseTable + ": " + sql);
		String columnNames = sql.toString();
		
		sql = new StringBuffer();
		sql.append("UPDATE ").append(table.getTableName()).append(" t SET (")
			.append(columnNames).append(") = (SELECT ").append(columnNames)
			.append(" FROM ").append(baseTable).append(" b WHERE t.")
			.append(baseTable).append("_ID=b.").append(baseTable).append("_ID) WHERE AD_Client_ID=")
			.append(AD_Client_ID);
		
		int no = DB.executeUpdate(sql.toString(), get_TrxName());
		addLog(0, null, new BigDecimal(no), baseTable);
	}	//	processTable
	
}	//	TranslationDocSync
