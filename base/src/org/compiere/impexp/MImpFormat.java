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
package org.compiere.impexp;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Import Format Model 
 *	
 *  @author Jorg Janke
 *  @version $Id: MImpFormat.java,v 1.8 2005/11/25 21:57:27 jjanke Exp $
 */
public class MImpFormat extends X_AD_ImpFormat
{

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_ImpFormat_ID id
	 */
	public MImpFormat (Properties ctx, int AD_ImpFormat_ID, String trxName)
	{
		super (ctx, AD_ImpFormat_ID, trxName);
	}	//	MImpFormat

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MImpFormat (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MImpFormat
	
	/**
	 * 	Get (all) Rows
	 *	@return array of Rows
	 */
	public MImpFormatRow[] getRows()
	{
		ArrayList<MImpFormatRow> list = new ArrayList<MImpFormatRow>();
		String sql = "SELECT * FROM AD_ImpFormat_Row "
			+ "WHERE AD_ImpFormat_ID=? "
			+ "ORDER BY SeqNo";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getAD_ImpFormat_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MImpFormatRow (getCtx(), rs, get_TrxName()));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getRows", e);
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
		MImpFormatRow[] retValue = new MImpFormatRow[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getRows
	
}	//	MImpFormat
