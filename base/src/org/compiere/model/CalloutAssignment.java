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
import org.compiere.util.*;


/**
 *	Resource Assignment Callout
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutAssignment.java,v 1.4 2005/11/06 01:17:27 jjanke Exp $
 */
public class CalloutAssignment extends CalloutEngine
{

	/**
	 *	Assignment_Product.
	 *		- called from S_ResourceAssignment_ID
	 *		- sets M_Product_ID, Description
	 *			- Qty.. 
	 */
	public String product (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		//	get value
		int S_ResourceAssignment_ID = ((Integer)value).intValue();
		if (S_ResourceAssignment_ID == 0)
			return "";
		setCalloutActive(true);

		int M_Product_ID = 0;
		String Name = null;
		String Description = null;
		BigDecimal Qty = null;
		String sql = "SELECT p.M_Product_ID, ra.Name, ra.Description, ra.Qty "
			+ "FROM S_ResourceAssignment ra"
			+ " INNER JOIN M_Product p ON (p.S_Resource_ID=ra.S_Resource_ID) "
			+ "WHERE ra.S_ResourceAssignment_ID=?";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, S_ResourceAssignment_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				M_Product_ID = rs.getInt (1);
				Name = rs.getString(2);
				Description = rs.getString(3);
				Qty = rs.getBigDecimal(4);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "product", e);
		}

		log.fine("S_ResourceAssignment_ID=" + S_ResourceAssignment_ID + " - M_Product_ID=" + M_Product_ID);
		if (M_Product_ID != 0)
		{
			mTab.setValue ("M_Product_ID", new Integer (M_Product_ID));
			if (Description != null)
				Name += " (" + Description + ")";
			if (!".".equals(Name))
				mTab.setValue("Description", Name);
			//
			String variable = "Qty";	//	TimeExpenseLine
			if (mTab.getTableName().startsWith("C_Order"))
				variable = "QtyOrdered";
			else if (mTab.getTableName().startsWith("C_Invoice"))
				variable = "QtyInvoiced";
			if (Qty != null)
				mTab.setValue(variable, Qty);
		}
		setCalloutActive(false);
		return "";
	}	//	product

}	//	CalloutAssignment
