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

import java.util.*;
import org.compiere.util.*;

/**
 *  MLookup Data Cache.
 *  - not synchronized on purpose -
 *  Called from MLookup.
 *  Only caches multiple use for a single window!
 *  @author Jorg Janke
 *  @version  $Id: MLookupCache.java,v 1.9 2005/10/20 04:51:35 jjanke Exp $
 */
public class MLookupCache
{
	/** Static Logger					*/
	private static CLogger 		s_log = CLogger.getCLogger(MLookupCache.class);
	/** Static Lookup data with MLookupInfo -> HashMap  */
	private static CCache<String,HashMap> s_loadedLookups = new CCache<String,HashMap>("MLookupCache", 50);
	
	/**
	 *  MLookup Loader starts loading - ignore for now
	 *
	 *  @param info MLookupInfo
	 */
	protected static void loadStart (MLookupInfo info)
	{
	}   //  loadStart

	/**
	 *  MLookup Loader ends loading, so add it to cache
	 *
	 *  @param info
	 *  @param lookup
	 */
	protected static void loadEnd (MLookupInfo info, HashMap lookup)
	{
		if (info.IsValidated && lookup.size() > 0)
			s_loadedLookups.put(getKey(info), lookup);
	}   //  loadEnd

	/**
	 * 	Get Storage Key
	 *	@param info lookup info
	 *	@return key
	 */
	private static String getKey (MLookupInfo info)
	{
		if (info == null)
			return String.valueOf(System.currentTimeMillis());
		//
		StringBuffer sb = new StringBuffer();
		sb.append(info.WindowNo).append(":")
		//	.append(info.Column_ID)
			.append(info.KeyColumn)
			.append(info.AD_Reference_Value_ID)
			.append(info.Query)
			.append(info.ValidationCode);
		//	does not include ctx
		return sb.toString();
	}	//	getKey


	/**
	 *  Load from Cache if applicable
	 *  Called from MLookup constructor
	 *
	 * @param info  MLookupInfo to search
	 * @param lookupTarget Target HashMap
	 * @return true, if lookup found
	 */
	protected static boolean loadFromCache (MLookupInfo info, HashMap<Object,Object> lookupTarget)
	{
		String key = getKey(info);
		HashMap cache = (HashMap)s_loadedLookups.get(key);
		if (cache == null)
			return false;
		//  Nothing cached
		if (cache.size() == 0)
		{
			s_loadedLookups.remove(key);
			return false;
		}

		//  Copy Asynchronously to speed things up
	//	if (cache.size() > ?) copyAsync

		//  copy cache
		//  we can use iterator, as the lookup loading is complete (i.e. no additional entries)
		Iterator iterator = cache.keySet().iterator();
		while (iterator.hasNext())
		{
			Object cacheKey = iterator.next();
			Object cacheData = cache.get(cacheKey);
			lookupTarget.put(cacheKey, cacheData);
		}

		s_log.fine("#" + lookupTarget.size());
		return true;
	}   //  loadFromCache

	/**
	 *	Clear Static Lookup Cache for Window
	 *  @param WindowNo WindowNo of Cache entries to delete
	 */
	public static void cacheReset (int WindowNo)
	{
		String key = String.valueOf(WindowNo) + ":";
		int startNo = s_loadedLookups.size();
		//  find keys of Lookups to delete
		ArrayList<String> toDelete = new ArrayList<String>();
		Iterator iterator = s_loadedLookups.keySet().iterator();
		while (iterator.hasNext())
		{
			String info = (String)iterator.next();
			if (info != null && info.startsWith(key))
				toDelete.add(info);
		}

		//  Do the actual delete
		for (int i = 0; i < toDelete.size(); i++)
			s_loadedLookups.remove(toDelete.get(i));
		int endNo = s_loadedLookups.size();
		s_log.fine("WindowNo=" + WindowNo
			+ " - " + startNo + " -> " + endNo);
	}	//	cacheReset

	
	/**************************************************************************
	 *  Private constructor
	 */
	private MLookupCache()
	{
	}   //  MLookupCache

}   //  MLookupCache
