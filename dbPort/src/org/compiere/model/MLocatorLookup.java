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

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Warehouse Locator Lookup Model.
 * 	(Lookup Model is model.Lookup.java)
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: MLocatorLookup.java,v 1.7 2005/10/26 00:38:17 jjanke Exp $
 */
public final class MLocatorLookup extends Lookup implements Serializable
{
	/**
	 *	Constructor
	 *  @param ctx context
	 *  @param WindowNo window no
	 */
	public MLocatorLookup(Properties ctx, int WindowNo)
	{
		super (DisplayType.TableDir, WindowNo);
		m_ctx = ctx;
		//
		m_loader = new Loader();
		m_loader.start();
	}	//	MLocator

	/**	Context						*/
	private Properties          m_ctx;
	protected int				C_Locator_ID;
	private Loader				m_loader;

	/**	Only Warehouse					*/
	private int					m_only_Warehouse_ID = 0;

	/** Storage of data  MLookups		*/
	private volatile LinkedHashMap<Integer,MLocator> m_lookup = new LinkedHashMap<Integer,MLocator>();
	
	private static int			s_maxRows = 200;	//	how many rows to read

	/**
	 *  Dispose
	 */
	public void dispose()
	{
		log.fine("C_Locator_ID=" + C_Locator_ID);
		if (m_loader != null)
		{
			while (m_loader.isAlive())
				m_loader.interrupt();
		}
		m_loader = null;
		if (m_lookup != null)
			m_lookup.clear();
		m_lookup = null;
		//
		super.dispose();
	}   //  dispose

	/**
	 * 	Set Warehouse restriction
	 *	@param only_Warehouse_ID wahrehouse
	 */
	public void setOnly_Warehouse_ID (int only_Warehouse_ID)
	{
		m_only_Warehouse_ID = only_Warehouse_ID;
	}	//	setOnly_Warehouse_ID

	/**
	 * 	Get Only Wahrehouse
	 *	@return	warehouse
	 */
	public int getOnly_Warehouse_ID()
	{
		return m_only_Warehouse_ID;
	}	//	getOnly_Warehouse_ID

	/**
	 *  Wait until async Load Complete
	 */
	public void loadComplete()
	{
		if (m_loader != null)
		{
			try
			{
				m_loader.join();
			}
			catch (InterruptedException ie)
			{
				log.log(Level.SEVERE, "Join interrupted", ie);
			}
		}
	}   //  loadComplete

	/**
	 *	Get value
	 *  @param key key
	 *  @return value value
	 */
	public NamePair get (Object key)
	{
		if (key == null)
			return null;

		//	try cache
		MLocator loc = (MLocator) m_lookup.get(key);
		if (loc != null)
			return new KeyNamePair (loc.getM_Locator_ID(), loc.toString());

		//	Not found and waiting for loader
		if (m_loader.isAlive())
		{
			log.fine("Waiting for Loader");
			loadComplete();
			//	is most current
			loc = (MLocator) m_lookup.get(key);
		}
		if (loc != null)
			return new KeyNamePair (loc.getM_Locator_ID(), loc.toString());

		//	Try to get it directly
		return getDirect(key, true, null);
	}	//	get

	/**
	 *	Get Display value
	 *  @param value value
	 *  @return String to display
	 */
	public String getDisplay (Object value)
	{
		if (value == null)
			return "";
		//
		NamePair display = get (value);
		if (display == null)
			return "<" + value.toString() + ">";
		return display.toString();
	}	//	getDisplay

	/**
	 *  The Lookup contains the key
	 *  @param key key
	 *  @return true, if lookup contains key
	 */
	public boolean containsKey (Object key)
	{
		return m_lookup.containsKey(key);
	}   //  containsKey

	/**
	 *	Get Data Direct from Table
	 *  @param keyValue integer key value
	 *  @param saveInCache save in cache
	 *  @return Object directly loaded
	 */
	public NamePair getDirect (Object keyValue, boolean saveInCache, String trxName)
	{
		MLocator loc = getMLocator (keyValue, trxName);
		if (loc == null)
			return null;
		//
		int key = loc.getM_Locator_ID();
		if (saveInCache)
			m_lookup.put(new Integer(key), loc);
		NamePair retValue = new KeyNamePair(key, loc.toString());
		return retValue;
	}	//	getDirect

	/**
	 *	Get Data Direct from Table
	 *  @param keyValue integer key value
	 *  @return Object directly loaded
	 */
	public MLocator getMLocator (Object keyValue, String trxName)
	{
	//	log.fine( "MLocatorLookup.getDirect " + keyValue.getClass() + "=" + keyValue);
		int M_Locator_ID = -1;
		try
		{
			M_Locator_ID = Integer.parseInt(keyValue.toString());
		}
		catch (Exception e)
		{}
		if (M_Locator_ID == -1)
		{
			log.log(Level.SEVERE, "Invalid key=" + keyValue);
			return null;
		}
		//
		return new MLocator (m_ctx, M_Locator_ID, trxName);
	}	//	getMLocator

	/**
	 * @return  a string representation of the object.
	 */
	public String toString()
	{
		return "MLocatorLookup[Size=" + m_lookup.size() + "]";
	}	//	toString


	/**
	 * 	Is Locator with key valid (Warehouse)
	 *	@param key key
	 *	@return true if valid
	 */
	public boolean isValid (Object key)
	{
		if (key == null)
			return true;
		//	try cache
		MLocator loc = (MLocator) m_lookup.get(key);
		if (loc == null)
			loc = getMLocator(key, null);
		return isValid(loc);
	}	//	isValid

	/**
	 * 	Is Locator with key valid (Warehouse)
	 *	@param locator locator
	 *	@return true if valid
	 */
	public boolean isValid (MLocator locator)
	{
		if (locator == null || m_only_Warehouse_ID == 0)
			return true;
		return m_only_Warehouse_ID == locator.getM_Warehouse_ID();
	}	//	isValid


	/**************************************************************************
	 *	Loader
	 */
	class Loader extends Thread implements Serializable
	{
		public Loader()
		{
			super("MLocatorLookup");
		}	//	Loader

		/**
		 *	Load Lookup
		 */
		public void run()
		{
		//	log.config( "MLocatorLookup Loader.run " + m_AD_Column_ID);
			//	Set Info
			StringBuffer sql = new StringBuffer("SELECT * FROM M_Locator ")
				.append(" WHERE IsActive='Y'");
			if (m_only_Warehouse_ID != 0)
				sql.append("AND M_Warehouse_ID=").append(m_only_Warehouse_ID);
			String finalSql = MRole.getDefault(m_ctx, false).addAccessSQL(
				sql.toString(), "M_Locator", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
			if (isInterrupted())
			{
				log.log(Level.SEVERE, "Interrupted");
				return;
			}

			//	Reset
			m_lookup.clear();
			int rows = 0;
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(finalSql, null);
				ResultSet rs = pstmt.executeQuery();

				//	Get first 100 rows
				while (rs.next() && rows++ < s_maxRows)
				{
					MLocator loc = new MLocator(m_ctx, rs, null);
					int M_Locator_ID = loc.getM_Locator_ID();
					m_lookup.put(new Integer(M_Locator_ID), loc);
				}
				rs.close();
				pstmt.close();
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sql.toString(), e);
			}
			log.fine("Complete #" + m_lookup.size());
		}	//	run
	}	//	Loader

	/**
	 *	Return info as ArrayList containing Locator, waits for the loader to finish
	 *  @return Collection of lookup values
	 */
	public Collection getData ()
	{
		if (m_loader.isAlive())
		{
			log.fine("Waiting for Loader");
			try
			{
				m_loader.join();
			}
			catch (InterruptedException ie)
			{
				log.severe ("Join interrupted - " + ie.getMessage());
			}
		}
		return m_lookup.values();
	}	//	getData

	/**
	 *	Return data as sorted ArrayList
	 *  @param mandatory mandatory
	 *  @param onlyValidated only validated
	 *  @param onlyActive only active
	 * 	@param temporary force load for temporary display
	 *  @return ArrayList of lookup values
	 */
	public ArrayList<Object> getData (boolean mandatory, boolean onlyValidated, boolean onlyActive, boolean temporary)
	{
		//	create list
		Collection collection = getData();
		ArrayList<Object> list = new ArrayList<Object>(collection.size());
		Iterator it = collection.iterator();
		while (it.hasNext())
		{
			MLocator loc = (MLocator)it.next();
			if (isValid(loc))				//	only valid warehouses
				list.add(loc);
		}

		/**	Sort Data
		MLocator l = new MLocator (m_ctx, 0);
		if (!mandatory)
			list.add (l);
		Collections.sort (list, l);
		**/
		return list;
	}	//	getArray


	/**
	 *	Refresh Values
	 *  @return new size of lookup
	 */
	public int refresh()
	{
		log.fine("start");
		m_loader = new Loader();
		m_loader.start();
		try
		{
			m_loader.join();
		}
		catch (InterruptedException ie)
		{
		}
		log.info("#" + m_lookup.size());
		return m_lookup.size();
	}	//	refresh

	/**
	 *	Get underlying fully qualified Table.Column Name
	 *  @return Table.ColumnName
	 */
	public String getColumnName()
	{
		return "M_Locator.M_Locator_ID";
	}	//	getColumnName

}	//	MLocatorLookup
