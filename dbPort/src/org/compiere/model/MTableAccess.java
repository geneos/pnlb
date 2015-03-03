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
 *	
 *	
 *  @author Jorg Janke
 *  @version $Id: MTableAccess.java,v 1.7 2005/10/26 00:38:17 jjanke Exp $
 */
public class MTableAccess extends X_AD_Table_Access
{
	/**
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 */
	public MTableAccess (Properties ctx, int ignored, String trxName)
	{
		super(ctx, 0, trxName);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MTableAccess

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MTableAccess(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MTableAccess

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MTableAccess[");
		sb.append("AD_Role_ID=").append(getAD_Role_ID())
			.append(",AD_Table_ID=").append(getAD_Table_ID())
			.append(",Exclude=").append(isExclude())
			.append(",Type=").append(getAccessTypeRule());
		if (ACCESSTYPERULE_Accessing.equals(getAccessTypeRule()))
			sb.append("-ReadOnly=").append(isReadOnly());
		else if (ACCESSTYPERULE_Exporting.equals(getAccessTypeRule()))
			sb.append("-CanExport=").append(isCanExport());
		else if (ACCESSTYPERULE_Reporting.equals(getAccessTypeRule()))
			sb.append("-CanReport=").append(isCanReport());
		sb.append("]");
		return sb.toString();
	}	//	toString

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
			.append("=").append(getTableName(ctx));
		if (ACCESSTYPERULE_Accessing.equals(getAccessTypeRule()))
			sb.append(" - ").append(Msg.translate(ctx, "IsReadOnly")).append("=").append(isReadOnly());
		else if (ACCESSTYPERULE_Exporting.equals(getAccessTypeRule()))
			sb.append(" - ").append(Msg.translate(ctx, "IsCanExport")).append("=").append(isCanExport());
		else if (ACCESSTYPERULE_Reporting.equals(getAccessTypeRule()))
			sb.append(" - ").append(Msg.translate(ctx, "IsCanReport")).append("=").append(isCanReport());
		sb.append(" - ").append(isExclude() ?  ex : in);
		return sb.toString();
	}	//	toStringX

	/**	TableName			*/
	private String		m_tableName;

	/**
	 * 	Get Table Name
	 *	@param ctx context
	 *	@return table name
	 */
	public String getTableName (Properties ctx)
	{
		if (m_tableName == null)
		{
			String sql = "SELECT TableName FROM AD_Table WHERE AD_Table_ID=?";
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_TrxName());
				pstmt.setInt(1, getAD_Table_ID());
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
					m_tableName = rs.getString(1);
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "getTableName", e);
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
		}		
		return m_tableName;
	}	//	getTableName

}	//	MTableAccess
