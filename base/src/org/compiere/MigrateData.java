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
package org.compiere;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;
import org.compiere.model.*;
import org.compiere.print.*;

/**
 * 	Migrate Data
 *  @author Jorg Janke
 *  @version $Id: MigrateData.java,v 1.3 2005/11/14 02:28:49 jjanke Exp $
 */
public class MigrateData
{
	/**
	 * 	Migrate Data
	 */
	public MigrateData ()
	{
		release252c();
		
		//	Update existing Print Format
		PrintFormatUtil pfu = new PrintFormatUtil (Env.getCtx());
		pfu.addMissingColumns();
	}	//	MigrateData
	
	/**	Logger	*/
	private static CLogger	log	= CLogger.getCLogger (MigrateData.class);
	
	/**
	 * 	Release 252c
	 */
	private void release252c()
	{
		String sql = "SELECT COUNT(*) FROM M_ProductDownload";
		int no = DB.getSQLValue(null, sql);
		if (no > 0)
		{
			log.finer("No Need - Downloads #" + no);
			return;
		}
		//
		int count = 0;
		sql = "SELECT AD_Client_ID, AD_Org_ID, M_Product_ID, Name, DownloadURL "
			+ "FROM M_Product "
			+ "WHERE DownloadURL IS NOT NULL";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				int AD_Client_ID = rs.getInt(1);
				int AD_Org_ID = rs.getInt(2);
				int M_Product_ID = rs.getInt(3);
				String Name = rs.getString(4);
				String DownloadURL = rs.getString(5);
				//
				Properties ctx = new Properties (Env.getCtx());
				Env.setContext(ctx, "#AD_Client_ID", AD_Client_ID);
				Env.setContext(ctx, "AD_Client_ID", AD_Client_ID);
				Env.setContext(ctx, "#AD_Org_ID", AD_Org_ID);
				Env.setContext(ctx, "AD_Org_ID", AD_Org_ID);
				MProductDownload pdl = new MProductDownload(ctx, 0, null);
				pdl.setM_Product_ID(M_Product_ID);
				pdl.setName(Name);
				pdl.setDownloadURL(DownloadURL);
				if (pdl.save())
				{
					count++;
					String sqlUpdate = "UPDATE M_Product SET DownloadURL = NULL WHERE M_Product_ID=" + M_Product_ID;
					int updated = DB.executeUpdate(sqlUpdate, null);
					if (updated != 1)
						log.warning("Product not updated");
				}
				else
					log.warning("Product Download not created M_Product_ID=" + M_Product_ID);
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
		log.info("#" + count);
	}	//	release252c
	
	
	/**
	 * 	Migrate Data
	 *	@param args ignored
	 */
	public static void main (String[] args)
	{
		Compiere.startup(true);
		new MigrateData();
	}	//	main
	
}	//	MigrateData
