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

import javax.swing.JOptionPane;

import org.compiere.util.*;

/**
 *	Percepciones Callouts.
 *	
 *  @author Daniel Gini - BISion
 *  
 */
public class CalloutImpuesto extends CalloutEngine
{
	/**
	 * 	
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 */
	public String impuesto (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		Integer TabNo = Env.getContextAsInt(ctx, WindowNo, "TabNo");
		Integer C_INVOICEPERCEP_ID = Env.getContextAsInt(ctx, WindowNo, TabNo, "C_INVOICEPERCEP_ID");
		MINVOICEPERCEP mi = new MINVOICEPERCEP(ctx,C_INVOICEPERCEP_ID,null);

		
		System.out.print(mi.getIMPUESTO());
		
		Integer C_RegPercep_Recib_Id = (Integer)value;
		
		if (C_RegPercep_Recib_Id == null || C_RegPercep_Recib_Id.intValue() == 0)
			return "";
		
		try
		{
			String sql = "SELECT Impuesto FROM C_RegPercep_Recib WHERE C_RegPercep_Recib_Id=?";
		
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_RegPercep_Recib_Id.intValue());
			ResultSet rs = pstmt.executeQuery();
		
			if (rs.next())
			{
				MINVOICEPERCEP mip = new MINVOICEPERCEP(ctx,C_INVOICEPERCEP_ID,null);
				mip.setIMPUESTO(rs.getString(1));

				mTab.setValue(mTab.getField("IMPUESTO"), rs.getString(1));
				
				mip.save();
			}
			
		}
		catch (Exception e)	{
			
		}
		
		return "";
	}	//	CalloutPercepcion
	
		/*String sql2 = "UPDATE C_INVOICEPERCEP SET IMPUESTO = (SELECT Impuesto FROM C_RegPercep_Recib WHERE C_RegPercep_Recib_Id= '" + C_RegPercep_Recib_Id.intValue() + "') WHERE AD_TAB_ID = " + mTab.getAD_Tab_ID() + " AND NAME = 'ESADUANERA'";
		DB.executeUpdate(sql2, null);
		ba.setDisplayed(true);

		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_RegPercep_Recib_Id.intValue());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				MField ad = mTab.getField("ESADUANERA");
				MField ba = mTab.getField("ESBANCARIA");
				
				if (rs.getString(1).equals("Ingresos Brutos"))
				{
					ad.setDisplayed(true);
					String sql2 = "UPDATE AD_FIELD SET ISDISPLAYED = 'Y' WHERE AD_TAB_ID = " + mTab.getAD_Tab_ID() + " AND NAME = 'ESADUANERA'";
					DB.executeUpdate(sql2, null);
					ba.setDisplayed(true);
					JOptionPane.showMessageDialog(null,"Entro por el SI","Info", JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					ad.setDisplayed(false);
					ad.setDisplayed(true);
					String sql2 = "UPDATE AD_FIELD SET ISDISPLAYED = 'N' WHERE AD_TAB_ID = " + mTab.getAD_Tab_ID() + " AND NAME = 'ESADUANERA'";
					DB.executeUpdate(sql2, null);
					DB.commit(false, null);
					JOptionPane.showMessageDialog(null,"Entro por el NO","Info", JOptionPane.INFORMATION_MESSAGE);
					ba.setDisplayed(false);
				}
				
				ad.refreshLookup();
				ba.refreshLookup();
				
				
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
	}	//	jurisdiccion*/
      
}	//	CalloutPercepcion

