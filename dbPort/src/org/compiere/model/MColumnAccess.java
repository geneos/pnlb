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

import java.sql.*;
import java.util.*;

import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Column Access Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MColumnAccess.java,v 1.8 2005/10/08 02:03:34 jjanke Exp $
 */
public class MColumnAccess extends X_AD_Column_Access
{
	/**
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 */
	public MColumnAccess (Properties ctx, int ignored, String trxName)
	{
		super(ctx, 0, trxName);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MColumnAccess

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MColumnAccess(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MColumnAccess

	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MColumnAccess[");
		sb.append("AD_Role_ID=").append(getAD_Role_ID())
			.append(",AD_Table_ID=").append(getAD_Table_ID())
			.append(",AD_Column_ID=").append(getAD_Column_ID())
			.append(",Exclude=").append(isExclude());
		sb.append("]");
		return sb.toString();
	} 	//	toString

	/**
	 * 	Extended String Representation
	 *	@return extended info
	 */
	public String toStringX (Properties ctx)
	{
		String in = Msg.getMsg(ctx, "Include");
		String ex = Msg.getMsg(ctx, "Exclude");
		StringBuffer sb = new StringBuffer();
		sb.append(Msg.translate(ctx, "AD_Table_ID"))
			.append("=").append(getTableName(ctx)).append(", ")
			.append(Msg.translate(ctx, "AD_Column_ID"))
			.append("=").append(getColumnName(ctx))
			.append(" (").append(Msg.translate(ctx, "IsReadOnly")).append("=").append(isReadOnly())
			.append(") - ").append(isExclude() ?  ex : in);
		return sb.toString();
	}	//	toStringX

	/**	TableName			*/
	private String		m_tableName;
	/**	ColumnName			*/
	private String		m_columnName;

	/**
	 * 	Get Table Name
	 *	@param ctx context for translatioin
	 *	@return table name
	 */
	public String getTableName (Properties ctx)
	{
		if (m_tableName == null)
			getColumnName(ctx);
		return m_tableName;
	}	//	getTableName

	/**
	 * 	Get Column Name
	 *	@param ctx context for translatioin
	 *	@return column name
	 */
	public String getColumnName (Properties ctx)
	{
		if (m_columnName == null)
		{
			String sql = "SELECT t.TableName,c.ColumnName, t.AD_Table_ID "
				+ "FROM AD_Table t INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID) "
				+ "WHERE AD_Column_ID=?";
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_TrxName());
				pstmt.setInt(1, getAD_Column_ID());
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
				{
					m_tableName = rs.getString(1);
					m_columnName = rs.getString(2);
					if (rs.getInt(3) != getAD_Table_ID())
						log.log(Level.SEVERE, "AD_Table_ID inconsistent - Access=" + getAD_Table_ID() + " - Table=" + rs.getInt(3));
				}
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, sql, e);
			}
			try
			{
				if (pstmt != null)
					pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				pstmt = null;
			}
			//	Get Clear Text
			String realName = Msg.translate(ctx, m_tableName + "_ID");
			if (!realName.equals(m_tableName + "_ID"))
				m_tableName = realName;
			m_columnName = Msg.translate(ctx, m_columnName);
		}		
		return m_columnName;
	}	//	getColumnName

}	//	MColumnAccess
