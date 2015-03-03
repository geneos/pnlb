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
package org.compiere.util;

import java.rmi.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.db.*;
import org.compiere.interfaces.*;

/**
 *  Compiere Cache Manangement
 *
 *  @author Jorg Janke
 *  @version $Id: CacheMgt.java,v 1.17 2005/12/09 05:19:09 jjanke Exp $
 */
public class CacheMgt
{
	/**
	 * 	Get Cache Management
	 * 	@return Cache Mgr
	 */
	public static CacheMgt get()
	{
		if (s_cache == null)
			s_cache = new CacheMgt();
		return s_cache;
	}	//	get

	/**	Singleton					*/
	private static CacheMgt		s_cache = null;

	/**
	 *	Private Constructor
	 */
	private CacheMgt()
	{
	}	//	CacheMgt

	/**	List of Instances				*/
	private ArrayList<CacheInterface>	m_instances = new ArrayList<CacheInterface>();
	/** List of Table Names				*/
	private ArrayList<String>	m_tableNames = new ArrayList<String>();
	/** Logger							*/
	private static CLogger		log = CLogger.getCLogger(CacheMgt.class);

	
	/**************************************************************************
	 * 	Register Cache Instance
	 *	@param instance Cache
	 *	@return true if added
	 */
	@SuppressWarnings("unchecked")
	public synchronized boolean register (CacheInterface instance)
	{
		if (instance == null)
			return false;
		if (instance instanceof CCache)
		{
			String tableName = ((CCache)instance).getName(); 
			m_tableNames.add(tableName);
		}
		return m_instances.add (instance);
	}	//	register

	/**
	 * 	Un-Register Cache Instance
	 *	@param instance Cache
	 *	@return true if removed
	 */
	public boolean unregister (CacheInterface instance)
	{
		if (instance == null)
			return false;
		boolean found = false;
		//	Could be included multiple times
		for (int i = m_instances.size()-1; i >= 0; i--)
		{
			CacheInterface stored = (CacheInterface)m_instances.get(i);
			if (instance.equals(stored))
			{
				m_instances.remove(i);
				found = true;
			}
		}
		return found;
	}	//	unregister

	/**************************************************************************
	 * 	Reset All registered Cache
	 * 	@return number of deleted cache entries
	 */
	public int reset()
	{
		int counter = 0;
		int total = 0;
		for (int i = 0; i < m_instances.size(); i++)
		{
			CacheInterface stored = (CacheInterface)m_instances.get(i);
			if (stored != null && stored.size() > 0)
			{
				log.fine(stored.toString());
				total += stored.reset();
				counter++;
			}
		}
		log.info("#" + counter + " (" + total + ")");
		return total;
	}	//	reset

	/**
	 * 	Reset registered Cache
	 * 	@param tableName table name
	 * 	@return number of deleted cache entries
	 */
	public int reset (String tableName)
	{
		return reset (tableName, 0);
	}	//	reset
	
	/**
	 * 	Reset registered Cache
	 * 	@param tableName table name
	 * 	@param Record_ID record if applicable or 0 for all
	 * 	@return number of deleted cache entries
	 */
	@SuppressWarnings("unchecked")
	public int reset (String tableName, int Record_ID)
	{
		if (tableName == null)
			return reset();
	//	if (tableName.endsWith("Set"))
	//		tableName = tableName.substring(0, tableName.length()-3);
		if (!m_tableNames.contains(tableName))
			return 0;
		//
		int counter = 0;
		int total = 0;
		for (int i = 0; i < m_instances.size(); i++)
		{
			CacheInterface stored = (CacheInterface)m_instances.get(i);
			if (stored != null && stored instanceof CCache)
			{
				CCache cc = (CCache)stored;
				if (cc.getName().startsWith(tableName))		//	reset lines/dependent too
				{
				//	if (Record_ID == 0)
					{
						log.fine("(all) - " + stored);
						total += stored.reset();
						counter++;
					}
				}
			}
		}
		log.info(tableName + ": #" + counter + " (" + total + ")");
		//	Update Server
		if (DB.isRemoteObjects())
		{
			Server server = CConnection.get().getServer();
			try
			{
				if (server != null)
				{	//	See ServerBean
					int serverTotal = server.cacheReset(tableName, 0); 
					if (CLogMgt.isLevelFinest())
						log.fine("Server => " + serverTotal);
				}
			}
			catch (RemoteException ex)
			{
				log.log(Level.SEVERE, "AppsServer error", ex);
			}
		}
		return total;
	}	//	reset
	
	/**
	 * 	Total Cached Elements
	 *	@return count
	 */
	@SuppressWarnings("unchecked")
	public int getElementCount()
	{
		int total = 0;
		for (int i = 0; i < m_instances.size(); i++)
		{
			CacheInterface stored = (CacheInterface)m_instances.get(i);
			if (stored != null && stored.size() > 0)
			{
				log.fine(stored.toString());
				if (stored instanceof CCache)
					total += ((CCache)stored).sizeNoExpire();
				else
					total += stored.size();
			}
		}
		return total;
	}	//	getElementCount
	
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("CacheMgt[");
		sb.append("Instances=")
			.append(m_instances.size())
			.append("]");
		return sb.toString ();
	}	//	toString
	
	/**
	 * 	Extended String Representation
	 *	@return info
	 */
	public String toStringX ()
	{
		StringBuffer sb = new StringBuffer ("CacheMgt[");
		sb.append("Instances=")
			.append(m_instances.size())
			.append(", Elements=")
			.append(getElementCount())
			.append("]");
		return sb.toString ();
	}	//	toString

}	//	CCache
