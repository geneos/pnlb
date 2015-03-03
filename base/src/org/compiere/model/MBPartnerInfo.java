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
 *	Business Partner Info Model for Query
 *	
 *  @author Jorg Janke
 *  @version $Id: MBPartnerInfo.java,v 1.7 2005/11/06 01:17:27 jjanke Exp $
 */
public class MBPartnerInfo extends X_RV_BPartner
{
	/**
	 * 	Find BPartners
	 *	@param ctx context
	 *	@param Value Business Partner Value
	 *	@param Name Business Partner Name
	 *	@param Contact Contact/User Name
	 *	@param EMail Contact/User EMail
	 *	@return array if of info
	 */
	public static MBPartnerInfo[] find (Properties ctx, 
		String Value, String Name, String Contact, String EMail, String Phone, String City)
	{
		StringBuffer sql = new StringBuffer ("SELECT * FROM RV_BPartner WHERE IsActive='Y'");
		StringBuffer sb = new StringBuffer();
		Value = getFindParameter (Value);
		if (Value != null)
			sb.append("UPPER(Value) LIKE ?");
		Name = getFindParameter (Name);
		if (Name != null)
		{
			if (sb.length() > 0)
				sb.append(" OR ");
			sb.append("UPPER(Name) LIKE ?");
		}
		Contact = getFindParameter (Contact);
		if (Contact != null)
		{
			if (sb.length() > 0)
				sb.append(" OR ");
			sb.append("UPPER(Contact) LIKE ?");
		}
		EMail = getFindParameter (EMail);
		if (EMail != null)
		{
			if (sb.length() > 0)
				sb.append(" OR ");
			sb.append("UPPER(EMail) LIKE ?");
		}
		Phone = getFindParameter (Phone);
		if (Phone != null)
		{
			if (sb.length() > 0)
				sb.append(" OR ");
			sb.append("UPPER(Phone) LIKE ?");
		}
		City = getFindParameter (City);
		if (City != null)
		{
			if (sb.length() > 0)
				sb.append(" OR ");
			sb.append("UPPER(City) LIKE ?");
		}
		if (sb.length() > 0)
			sql.append(" AND (").append(sb).append(")");
		sql.append(" ORDER BY Value");
		//
		String finalSQL = MRole.getDefault().addAccessSQL(sql.toString(), 
			"RV_BPartner", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		ArrayList<MBPartnerInfo> list = new ArrayList<MBPartnerInfo>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(finalSQL, null);
			int index = 1;
			if (Value != null)
				pstmt.setString(index++, Value);
			if (Name != null)
				pstmt.setString(index++, Name);
			if (Contact != null)
				pstmt.setString(index++, Contact);
			if (EMail != null)
				pstmt.setString(index++, EMail);
			if (Phone != null)
				pstmt.setString(index++, Phone);
			if (City != null)
				pstmt.setString(index++, City);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MBPartnerInfo (ctx, rs, null));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "find - " + finalSQL, e);
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
		//	Return
		MBPartnerInfo[] retValue = new MBPartnerInfo[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	find
	
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MBPartnerInfo.class);
	
	/**************************************************************************
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MBPartnerInfo (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MBPartnerInfo

}	//	MBPartnerInfo
