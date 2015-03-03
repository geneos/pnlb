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
package org.eevolution.model;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;
import org.compiere.model.*;

/**
 *	Invoice Callouts	
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutInvoice.java,v 1.27 2005/11/25 21:57:24 jjanke Exp $
 */
public class CalloutInvoice extends CalloutEngine
{

	/**
	 *	Invoice Header - DocType.
	 *		- PaymentRule
	 *		- temporary Document
	 *  Context:
	 *  	- DocSubTypeSO
	 *		- HasCharges
	 *	- (re-sets Business Partner info of required)
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 */
	public String documentNo (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		//String  DocumentNo = (String)value;
		//if (DocumentNo == null)
		//	return "";
		 Integer C_Order_ID = (Integer)mTab.getValue("C_Order_ID");
		 if (C_Order_ID == null)
			 return "";
			 
			String sql = "SELECT d.DocSubTypeSO , o.DocumentNo FROM C_Order o INNER JOIN C_DocType d ON (d.C_DocType_ID=o.C_DocType_ID) WHERE o.C_Order_ID=?";
			System.out.println("sql"+sql);
			
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_Order_ID.intValue());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				String DocSubTypeSO = rs.getString(1);
				if (DocSubTypeSO.equals(MOrder.DocSubTypeSO_POS)
						|| DocSubTypeSO.equals(MOrder.DocSubTypeSO_Prepay)
						|| DocSubTypeSO.equals(MOrder.DocSubTypeSO_OnCredit))
				{
					mTab.setValue("DocumentNo",rs.getString(2));
					System.out.println("----------------------------------------------"+rs.getString(2));
				}		
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
	}	//	documentNo
}	//	CalloutInvoice
