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
 *  Reference List Value
 *
 *  @author Jorg Janke
 *  @version $Id: MRefList.java,v 1.10 2005/12/27 06:20:04 jjanke Exp $
 */
public class MRefList extends X_AD_Ref_List
{
	/**
	 * 	Get Reference List 
	 *	@param ctx context
	 *	@param AD_Reference_ID reference
	 *	@param Value value
	 *	@return List or null
	 */
	public static MRefList get (Properties ctx, int AD_Reference_ID, String Value, String trxName)
	{
		MRefList retValue = null;
		String sql = "SELECT * FROM AD_Ref_List "
			+ "WHERE AD_Reference_ID=? AND Value=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, AD_Reference_ID);
			pstmt.setString (2, Value);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MRefList (ctx, rs, trxName);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			s_log.log(Level.SEVERE, sql, ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;

		return retValue;
	}	//	get

	/**
	 * 	Get Reference List Value Name (cached)
	 *	@param ctx context
	 *	@param AD_Reference_ID reference
	 *	@param Value value
	 *	@return List or null
	 */
	public static String getListName (Properties ctx, int AD_Reference_ID, String Value)
	{
		String AD_Language = Env.getAD_Language(ctx);
		String key = AD_Language + "_" + AD_Reference_ID + "_" + Value;
		String retValue = (String)s_cache.get(key);
		if (retValue != null)
			return retValue;

		boolean isBaseLanguage = Env.isBaseLanguage(AD_Language, "AD_Ref_List");
		String sql = isBaseLanguage ?
			"SELECT Name FROM AD_Ref_List "
			+ "WHERE AD_Reference_ID=? AND Value=?" :
			"SELECT t.Name FROM AD_Ref_List_Trl t"
			+ " INNER JOIN AD_Ref_List r ON (r.AD_Ref_List_ID=t.AD_Ref_List_ID) "
			+ "WHERE r.AD_Reference_ID=? AND r.Value=? AND t.AD_Language=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, AD_Reference_ID);
			pstmt.setString(2, Value);
			if (!isBaseLanguage)
				pstmt.setString(3, AD_Language);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = rs.getString(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			s_log.log(Level.SEVERE, sql + " - " + key, ex);
		}
		try
		{
			if (pstmt != null)
			   pstmt.close ();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;

		//	Save into Cache
		if (retValue == null)
		{
			retValue = "";
			s_log.warning("getListName - Not found " + key);
		}
		s_cache.put(key, retValue);
		//
		return retValue;
	}	//	getListName


	/**
	 * 	Get Reference List
	 *	@param AD_Reference_ID reference
	 *	@param optional if true add "",""
	 *	@return List or null
	 */
	public static ValueNamePair[] getList (int AD_Reference_ID, boolean optional)
	{
		String sql = "SELECT Value, Name FROM AD_Ref_List "
			+ "WHERE AD_Reference_ID=? AND IsActive='Y' ORDER BY 1";
		PreparedStatement pstmt = null;
		ArrayList<ValueNamePair> list = new ArrayList<ValueNamePair>();
		if (optional)
			list.add(new ValueNamePair("", ""));
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Reference_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new ValueNamePair(rs.getString(1), rs.getString(2)));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		ValueNamePair[] retValue = new ValueNamePair[list.size()];
		list.toArray(retValue);
		return retValue;		
	}	//	getList


	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (MRefList.class);
	/** Value Cache						*/
	private static CCache<String,String> s_cache = new CCache<String,String>("AD_Ref_List", 20);


	/**************************************************************************
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param AD_Ref_List_ID id
	 */
	public MRefList (Properties ctx, int AD_Ref_List_ID, String trxName)
	{
		super (ctx, AD_Ref_List_ID, trxName);
		if (AD_Ref_List_ID == 0)
		{
		//	setAD_Reference_ID (0);
		//	setAD_Ref_List_ID (0);
			setEntityType (ENTITYTYPE_UserMaintained);	// U
		//	setName (null);
		//	setValue (null);
		}
	}	//	MRef_List

	/**
	 * 	Load Contructor
	 *	@param ctx context
	 *	@param rs result
	 */
	public MRefList (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRef_List

	/**
	 *	String Representation
	 * 	@return Name
	 */
	public String toString()
	{
		return getName();
	}	//	toString


}	//	MRef_List
