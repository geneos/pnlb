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
 *	Request Callouts
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutRequest.java,v 1.8 2005/11/06 01:17:27 jjanke Exp $
 */
public class CalloutRequest extends CalloutEngine
{
	/**
	 *  Request - Copy Mail Text - <b>Callout</b>
	 *
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @return Error message or ""
	 */
	public String copyMail (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		String colName = mField.getColumnName();
		log.info(colName + "=" + value);
		if (value == null)
			return "";

		Integer R_MailText_ID = (Integer)value;
		String sql = "SELECT MailHeader, MailText FROM R_MailText "
			+ "WHERE R_MailText_ID=?";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, R_MailText_ID.intValue());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				String txt = rs.getString(2);
				txt = Env.parseContext(ctx, WindowNo, txt, false, true);
				mTab.setValue("Result", txt);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		return "";
	}   //  copyText

	
	/**
	 *  Request - Copy Response Text - <b>Callout</b>
	 *
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @return Error message or ""
	 */
	public String copyResponse (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		String colName = mField.getColumnName();
		log.info(colName + "=" + value);
		if (value == null)
			return "";

		Integer R_StandardResponse_ID = (Integer)value;
		String sql = "SELECT Name, ResponseText FROM R_StandardResponse "
			+ "WHERE R_StandardResponse_ID=?";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, R_StandardResponse_ID.intValue());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				String txt = rs.getString(2);
				txt = Env.parseContext(ctx, WindowNo, txt, false, true);
				mTab.setValue("Result", txt);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		return "";
	}   //  copyResponse

}	//	CalloutRequest
