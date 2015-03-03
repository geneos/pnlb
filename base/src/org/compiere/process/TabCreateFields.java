/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.process;

import java.sql.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Create Field from Table Column.
 *	(which do not exist in the Tab yet)
 *	
 *  @author Jorg Janke
 *  @version $Id: TabCreateFields.java,v 1.4 2005/11/21 23:47:54 jjanke Exp $
 */
public class TabCreateFields extends SvrProcess
{
	/**	Tab NUmber				*/
	private int	p_AD_Tab_ID= 0;
	
	/**
	 * 	prepare
	 */
	protected void prepare ()
	{
		p_AD_Tab_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		M_Tab tab = new M_Tab (getCtx(), p_AD_Tab_ID, get_TrxName());
		if (p_AD_Tab_ID == 0 || tab == null || tab.get_ID() == 0)
			throw new CompiereSystemError("@NotFound@: @AD_Tab_ID@ " + p_AD_Tab_ID);
		log.info(tab.toString());
		//
		int count = 0;
		String sql = "SELECT * FROM AD_Column c "
			+ "WHERE NOT EXISTS (SELECT * FROM AD_Field f "
				+ "WHERE c.AD_Column_ID=f.AD_Column_ID"
				+ " AND c.AD_Table_ID=?"	//	#1
				+ " AND f.AD_Tab_ID=?)"		//	#2
			+ " AND AD_Table_ID=?"			//	#3
			+ " AND NOT (Name LIKE 'Created%' OR Name LIKE 'Updated%')"
			+ " AND IsActive='Y' "
			+ "ORDER BY Name";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, tab.getAD_Table_ID());
			pstmt.setInt (2, tab.getAD_Tab_ID());
			pstmt.setInt (3, tab.getAD_Table_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				M_Column column = new M_Column (getCtx(), rs, get_TrxName());
				//
				M_Field field = new M_Field (tab);
				field.setColumn(column);
				if (column.isKey())
					field.setIsDisplayed(false);
				if (field.save())
				{
					addLog(0, null, null, column.getName());
					count++;
				}
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		return "@Created@ #" + count;
	}	//	doIt
	
}	//	TabCreateFields
