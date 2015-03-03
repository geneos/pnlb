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
package org.compiere.sqlj;

import java.sql.*;


/**
 *	SQLJ Business Partner related Functions
 *	
 *  @author Jorg Janke
 *  @version $Id: BPartner.java,v 1.2 2005/03/11 20:35:02 jjanke Exp $
 */
public class BPartner
{
	/**
	 * 	Return first Remit Location of BPartner.
	 * 	C_BPartner_RemitLocation - bpartnerRemitLocation
	 *	@param p_C_BPartner_ID business partner
	 *	@return remit to location
	 */
	public static int remitLocation (int p_C_BPartner_ID)
		throws SQLException
	{
		int C_Location_ID = 0;
		String sql = "SELECT IsRemitTo, C_Location_ID "
			+ "FROM C_BPartner_Location "
			+ "WHERE C_BPartner_ID=? "
			+ "ORDER BY IsRemitTo DESC";
		PreparedStatement pstmt = Compiere.prepareStatement(sql);
		pstmt.setInt(1, p_C_BPartner_ID);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
			C_Location_ID = rs.getInt(2);
		rs.close();
		pstmt.close();
		//
		return C_Location_ID;
	}	//	remitLocation
	
}	//	BPartner
