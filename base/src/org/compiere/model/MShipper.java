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

import javax.swing.JOptionPane;

import org.compiere.util.DB;


/**
 *	Shipper Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MShipper.java,v 1.5 2005/05/17 05:29:53 jjanke Exp $
 */
public class MShipper extends X_M_Shipper
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_Shipper_ID id
	 */
	public MShipper (Properties ctx, int M_Shipper_ID, String trxName)
	{
		super (ctx, M_Shipper_ID, trxName);
	}	//	MShipper

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MShipper (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MShipper
	
	/**
	 * 	Before Save
	 */
	protected boolean beforeSave(boolean newRecord)
	{
		try{
			String sql =  
			" SELECT M_Shipper_ID FROM M_Shipper " +
			" WHERE ISDEFAULT = 'Y' AND AD_ORG_ID = ? AND AD_CLIENT_ID = ? AND ISACTIVE='Y'";
			PreparedStatement pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,getAD_Org_ID());
			pstmt.setInt(2,getAD_Client_ID());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next() && isDefault() && get_ID()!=rs.getInt(1))
			{	
				JOptionPane.showMessageDialog(null, "No pueden existir 2 registros como Predeterminados", "Datos Incorrectos", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
		} catch (SQLException e)
		{	e.printStackTrace();
		  	return false;	}
		
		return true; 
	}	//	beforeSave
	
}	//	MShipper
