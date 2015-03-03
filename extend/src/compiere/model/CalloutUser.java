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
package compiere.model;

import java.sql.*;
import java.util.*;
import org.compiere.model.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	User Callout Example.
 *
 *  @author Jorg Janke
 *  @version  $Id: CalloutUser.java,v 1.4 2005/12/23 01:45:47 jjanke Exp $
 */
public class CalloutUser extends CalloutEngine
{
	/**
	 *	JustAnExample.
	 *	The string in the Callout field is: 
	 *  <code>com.compiere.custom.CalloutEngine.justAnExample</code> 
	 *
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @param oldValue The old value
	 *	@return error message or "" if OK
	 */
	public String justAnExample (Properties ctx, int WindowNo,
		MTab mTab, MField mField, Object value, Object oldValue)
	{
		log.info("JustAnExample");
		return "";
	}	//	justAnExample

	
	
	
	
	
	
	
	
	
	
	

	/**************************************************************************
	 *	Frie Value - convert to standardized Name
	 *
	 * @param value Name
	 * @return Name
	 */
	public String Frie_Name (String value)
	{
		if (value == null || value.length() == 0)
			return "";
		//
		String retValue = value;
		String SQL = "SELECT FRIE_Name(?) FROM DUAL";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			pstmt.setString(1, value);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				retValue = rs.getString(1);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, SQL, e);
		}
		return retValue;
	}	//	Frie_Name


	/**
	 *	Frie Value - convert Name to Value
	 *
	 *  @param value Name
	 *  @return Value of Name
	 */
	public String Frie_Value (String value)
	{
		if (value == null || value.length() == 0)
			return "";
		//
		String retValue = value;
		String SQL = "SELECT FRIE_Value(FRIE_Name(?)) FROM DUAL";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			pstmt.setString(1, value);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				retValue = rs.getString(1);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, SQL, e);
		}
		return retValue;
	}	//	Frie_Value


	/**
	 *	Frie Status - convert to Status.
	 *
	 *  @param value value
	 *  @return Status
	 */
	public String Frie_Status (String value)
	{
		String retValue = "N";		//	default
		if (value != null && value.equals("A"))		//	Auslaufartikel
			retValue = "Y";			//
		return retValue;
	}	//	Frie_Status

}	//	CalloutUser
