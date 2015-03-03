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

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.JOptionPane;
import org.compiere.util.*;

/**
 *	Percepciones Callouts.
 *	
 *  @author Daniel Gini - BISion
 *  @version $Id: CalloutJurisdiccion.java,v 1.2 2008/11/10 12:42:23
 */
public class CalloutPercepcion extends CalloutEngine
{
	/**
	 *	Percepciones - Jurisdiccion.
	 * 		- updates Codigo from Jurisdiccion
	 * 	
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 */
	public String jurisdiccion (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		Integer C_Jurisdiccion_Id = (Integer)value;
		
		if (C_Jurisdiccion_Id == null || C_Jurisdiccion_Id.intValue() == 0)
			return "";
		
		String sql = "SELECT Codigo FROM C_Jurisdiccion WHERE C_Jurisdiccion_Id=?";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_Jurisdiccion_Id.intValue());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				mTab.setValue ("CODIGO", rs.getInt(1));
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return e.getLocalizedMessage();
		}
		
		return "";
	}	//	jurisdiccion
      
}	//	CalloutPercepcion

