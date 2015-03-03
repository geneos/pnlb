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
 *	POS Function Key Layout
 *	
 *  @author Jorg Janke
 *  @version $Id: MPOSKeyLayout.java,v 1.7 2005/10/14 00:44:31 jjanke Exp $
 */
public class MPOSKeyLayout extends X_C_POSKeyLayout
{
	/**
	 * 	Get POS Key Layout from Cache
	 *	@param ctx context
	 *	@param C_POSKeyLayout_ID id
	 *	@return MPOSKeyLayout
	 */
	public static MPOSKeyLayout get (Properties ctx, int C_POSKeyLayout_ID)
	{
		Integer key = new Integer (C_POSKeyLayout_ID);
		MPOSKeyLayout retValue = (MPOSKeyLayout) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MPOSKeyLayout (ctx, C_POSKeyLayout_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer,MPOSKeyLayout> s_cache = new CCache<Integer,MPOSKeyLayout>("C_POSKeyLayout", 3);

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_POSKeyLayout_ID id
	 */
	public MPOSKeyLayout (Properties ctx, int C_POSKeyLayout_ID, String trxName)
	{
		super (ctx, C_POSKeyLayout_ID, trxName);
	}	//	MPOSKeyLayout

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MPOSKeyLayout (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MPOSKeyLayout

	/**	Keys				*/
	private MPOSKey[]	m_keys = null;
	
	/**
	 * 	Get Keys
	 *	@param requery requery
	 *	@return keys
	 */
	public MPOSKey[] getKeys (boolean requery)
	{
		if (m_keys != null && !requery)
			return m_keys;
		
		ArrayList<MPOSKey> list = new ArrayList<MPOSKey>();
		String sql = "SELECT * FROM C_POSKey WHERE C_POSKeyLayout_ID=? ORDER BY SeqNo";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getC_POSKeyLayout_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MPOSKey(getCtx(), rs, get_TrxName()));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
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
		
		m_keys = new MPOSKey[list.size ()];
		list.toArray (m_keys);
		return m_keys;
	}	//	getKeys
	
	/**
	 * 	Get Number of Keys
	 *	@return keys
	 */
	public int getNoOfKeys()
	{
		return getKeys(false).length;
	}	//	getNoOfKeys
	
}	//	MPOSKeyLayout

