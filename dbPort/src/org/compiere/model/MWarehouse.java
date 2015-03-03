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
 *	Warehouse Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MWarehouse.java,v 1.10 2005/11/09 17:39:18 jjanke Exp $
 */
public class MWarehouse extends X_M_Warehouse
{
	/**
	 * 	Get from Cache
	 *	@param ctx context
	 *	@param M_Warehouse_ID id
	 *	@return warehouse
	 */
	public static MWarehouse get (Properties ctx, int M_Warehouse_ID)
	{
		Integer key = new Integer(M_Warehouse_ID);
		MWarehouse retValue = (MWarehouse)s_cache.get(key);
		if (retValue != null)
			return retValue;
		//
		retValue = new MWarehouse (ctx, M_Warehouse_ID, null);
		s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get Warehouses for Org
	 *	@param ctx context
	 *	@param AD_Org_ID id
	 *	@return warehouse
	 */
	public static MWarehouse[] getForOrg (Properties ctx, int AD_Org_ID)
	{
		ArrayList<MWarehouse> list = new ArrayList<MWarehouse>();
		String sql = "SELECT * FROM M_Warehouse WHERE AD_Org_ID=? ORDER BY Created";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, AD_Org_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MWarehouse (ctx, rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
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
		MWarehouse[] retValue = new MWarehouse[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	get
	
	
	/**	Cache					*/
	private static CCache<Integer,MWarehouse> s_cache = new CCache<Integer,MWarehouse>("M_Warehouse", 5);
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MWarehouse.class);
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_Warehouse_ID id
	 */
	public MWarehouse (Properties ctx, int M_Warehouse_ID, String trxName)
	{
		super(ctx, M_Warehouse_ID, trxName);
		if (M_Warehouse_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
		//	setC_Location_ID (0);
			setSeparator ("*");	// *
		}
	}	//	MWarehouse

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MWarehouse (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MWarehouse

	/**
	 * 	Organization Constructor
	 *	@param org parent
	 */
	public MWarehouse (MOrg org)
	{
		this (org.getCtx(), 0, org.get_TrxName());
		setClientOrg(org);
		setValue (org.getValue());
		setName (org.getName());
		if (org.getInfo() != null)
			setC_Location_ID (org.getInfo().getC_Location_ID());
	}	//	MWarehouse

	/**	Warehouse Locators				*/
	private MLocator[]	m_locators = null;
	
	/**
	 * 	Get Locators
	 *	@param reload if true reload
	 *	@return array of locators
	 */
	public MLocator[] getLocators(boolean reload)
	{
		if (!reload && m_locators != null)
			return m_locators;
		//
		String sql = "SELECT * FROM M_Locator WHERE M_Warehouse_ID=? ORDER BY X,Y,Z";
		ArrayList<MLocator> list = new ArrayList<MLocator>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, getM_Warehouse_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MLocator (getCtx(), rs, null));
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
		//
		m_locators = new MLocator[list.size()];
		list.toArray (m_locators);
		return m_locators;
	}	//	getLocators
	
	/**
	 * 	Get Default Locator
	 *	@return (first) default locator
	 */
	public MLocator getDefaultLocator()
	{
		MLocator[] locators = getLocators(false);
		for (int i = 0; i < locators.length; i++)
		{
			if (locators[i].isDefault() && locators[i].isActive())
				return locators[i];
		}
		//	No Default - first one
		if (locators.length > 0)
		{
			log.warning("No default locator for " + getName());
			return locators[0];
		}
		//	No Locator - create one
		MLocator loc = new MLocator (this, "Standard");
		loc.setIsDefault(true);
		loc.save();
		log.info("Created default locator for " + getName());
		return loc;
	}	//	getLocators
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord & success)
			insert_Accounting("M_Warehouse_Acct", "C_AcctSchema_Default", null);
		
		return success;
	}	//	afterSave

	/**
	 * 	Before Delete
	 *	@return true
	 */
	protected boolean beforeDelete ()
	{
		return delete_Accounting("M_Warehouse_Acct"); 
	}	//	beforeDelete

}	//	MWarehouse
