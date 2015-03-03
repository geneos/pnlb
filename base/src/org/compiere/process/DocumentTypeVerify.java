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
import java.util.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Verify Document Types.
 *	- Make sure that there is a DocumentType for all Document Base Types
 *	- Create missing Period Controls for Document Type 
 *	
 *  @author Jorg Janke
 *  @version $Id: DocumentTypeVerify.java,v 1.13 2005/09/29 22:01:55 jjanke Exp $
 */
public class DocumentTypeVerify extends SvrProcess
{
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (DocumentTypeVerify.class);
	
	
	/**
	 *	No Parameters (Nop)
	 *	@see org.compiere.process.SvrProcess#prepare()
	 */
	protected void prepare()
	{
	}	//	prepare

	/**
	 * 	Execute process
	 *	@see org.compiere.process.SvrProcess#doIt()
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
		createDocumentTypes(getCtx(), getAD_Client_ID(), this, get_TrxName());
		createPeriodControls(getCtx(), getAD_Client_ID(), this, get_TrxName());
		return "OK";
	}	//	doIt

	
	/**************************************************************************
	 * 	Create Missing Document Types
	 * 	@param ctx context
	 * 	@param AD_Client_ID client
	 * 	@param sp server process
	 */
	public static void createDocumentTypes(Properties ctx, int AD_Client_ID, SvrProcess sp, String trxName)
	{
		s_log.info("AD_Client_ID=" + AD_Client_ID);
		String sql = "SELECT rl.Value, rl.Name "
			+ "FROM AD_Ref_List rl "
			+ "WHERE rl.AD_Reference_ID=183"
			+ " AND rl.IsActive='Y' AND NOT EXISTS "
			+ " (SELECT * FROM C_DocType dt WHERE dt.AD_Client_ID=? AND rl.Value=dt.DocBaseType)";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			pstmt.setInt(1, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				String name = rs.getString(2);
				String value = rs.getString(1);
				s_log.config(name + "=" + value);
				MDocType dt = new MDocType (ctx, value, name, trxName);
				if (dt.save())
				{
					if (sp != null)
						sp.addLog (0, null, null, name);
					else
						s_log.fine(name);
				}
				else
				{
					if (sp != null)
						sp.addLog (0, null, null, "Not created: " + name);
					else
						s_log.warning("Not created: " + name);
				}
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
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
	}	//	createDocumentTypes
	
	/**
	 * 	Create Period Controls
	 * 	@param ctx context
	 * 	@param AD_Client_ID client
	 * 	@param sp server process
	 */
	public static void createPeriodControls(Properties ctx, int AD_Client_ID, 
		SvrProcess sp, String trxName)
	{
		s_log.info("AD_Client_ID=" + AD_Client_ID);
		String sql = "SELECT p.AD_Client_ID, p.C_Period_ID, dt.DocBaseType "
			+ "FROM C_Period p"
			+ " FULL JOIN C_DocType dt ON (p.AD_Client_ID=dt.AD_Client_ID) "
			+ "WHERE p.AD_Client_ID=?"
			+ " AND NOT EXISTS"
			+ " (SELECT * FROM C_PeriodControl pc WHERE pc.C_Period_ID=p.C_Period_ID AND pc.DocBaseType=dt.DocBaseType)";
		PreparedStatement pstmt = null;
		int counter = 0;
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			pstmt.setInt(1, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				int Client_ID = rs.getInt(1);
				int C_Period_ID = rs.getInt(2);
				String DocBaseType = rs.getString(3);
				s_log.config("AD_Client_ID=" + Client_ID 
					+ ", C_Period_ID=" + C_Period_ID + ", DocBaseType=" + DocBaseType);
				MPeriodControl pc = new MPeriodControl (ctx, Client_ID, 
					C_Period_ID, DocBaseType, trxName);
				if (pc.save())
				{
					counter++;
					s_log.fine(pc.toString());
				}
				else
					s_log.warning("Not saved: " + pc);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
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
		if (sp != null)
			sp.addLog (0, null, new BigDecimal(counter), "@C_PeriodControl_ID@ @Created@");
	}	//	createPeriodControls

}	//	DocumentTypeVerify
