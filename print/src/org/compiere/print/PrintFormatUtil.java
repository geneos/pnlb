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
import org.compiere.util.*;

/**
 *	Print Format Utilities.
 *	- Add Missing Columns for all Print Format
 *	
 *  @author Jorg Janke
 *  @version $Id: PrintFormatUtil.java,v 1.15 2005/11/13 23:40:21 jjanke Exp $
 */
public class PrintFormatUtil
{
	/**
	 * 	Print Format Utility
	 *	@param ctx context
	 */
	public PrintFormatUtil(Properties ctx)
	{
		super();
		m_ctx = ctx;
	}	//	PrintFormatUtil

	/**	Logger					*/
	private CLogger			log = CLogger.getCLogger (getClass());
	/** Context					*/
	private Properties		m_ctx;
	

	/**
	 * 	Add Missing Columns for all Print Format
	 */
	public void addMissingColumns ()
	{
		int total = 0;
		String sql = "SELECT * FROM AD_PrintFormat pf "
			+ "ORDER BY Name";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				total += addMissingColumns(new MPrintFormat (m_ctx, rs, null));
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
		log.info ("Total = " + total);
	}	//	addMissingColumns


	/**
	 * 	Add Missing Columns for Print Format
	 *	@param pf print format
	 *	@return no of columns created
	 */
	public int addMissingColumns (MPrintFormat pf)
	{
		log.config(pf.toString());
		String sql = "SELECT c.AD_Column_ID, c.ColumnName "
			+ "FROM AD_Column c "
			+ "WHERE NOT EXISTS "
				+ "(SELECT * "
				+ "FROM AD_PrintFormatItem pfi"
				+ " INNER JOIN AD_PrintFormat pf ON (pfi.AD_PrintFormat_ID=pf.AD_PrintFormat_ID) "
				+ "WHERE pf.AD_Table_ID=c.AD_Table_ID"
				+ " AND pfi.AD_Column_ID=c.AD_Column_ID"
				+ " AND pfi.AD_PrintFormat_ID=?)"	//	1 
			+ " AND c.AD_Table_ID=? "				//	2
			+ "ORDER BY 1";
		PreparedStatement pstmt = null;
		int counter = 0;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, pf.getAD_PrintFormat_ID());
			pstmt.setInt(2, pf.getAD_Table_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				int AD_Column_ID = rs.getInt(1);
				String ColumnName = rs.getString(2);
				MPrintFormatItem pfi = MPrintFormatItem.createFromColumn (pf, AD_Column_ID, 0);
				if (pfi.get_ID() != 0)
					log.fine("#" + ++counter + " - added " + ColumnName);
				else
					log.warning("Not added: " + ColumnName);
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
		if (counter == 0)
			log.fine("None"
				/**
				+ " - " + sql 
				+ " - AD_PrintFormat_ID=" + pf.getAD_PrintFormat_ID()
				+ " - AD_Table_ID=" + pf.getAD_Table_ID()
				*/
				);
		else
			log.fine("Added=" + counter);
		return counter;
	}	//	addMissingColumns


	/**************************************************************************
	 * 	Main
	 *	@param args arguments
	 */
	public static void main(String[] args)
	{
		org.compiere.Compiere.startupEnvironment(true);
		//
		PrintFormatUtil pfu = new PrintFormatUtil (Env.getCtx());
		pfu.addMissingColumns();
	}	//	main
	
}	//	PrintFormatUtils
