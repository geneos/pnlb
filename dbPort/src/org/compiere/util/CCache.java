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

import java.beans.*;
import java.util.*;

/**
 *  Compiere Cache.
 *
 *  @author Jorg Janke
 *  @version $Id: CCache.java,v 1.18 2005/10/28 01:00:19 jjanke Exp $
 */
public class CCache<K,V> extends HashMap<K,V> implements CacheInterface
{
	/**
	 * 	Compiere Cache - expires after 2 hours
	 * 	@param name name of the cache
	 * 	@param initialCapacity initial capacity
	 */
	public CCache (String name, int initialCapacity)
	{
		this (name, initialCapacity, 120);
	}	//	CCache


	/**
	 * 	Compiere Cache
	 * 	@param name (table) name of the cache
	 * 	@param initialCapacity initial capacity
	 * 	@param expireMinutes expire after minutes (0=no expire)
	 */
	public CCache (String name, int initialCapacity, int expireMinutes)
	{
		super(initialCapacity);
		m_name = name;
		setExpireMinutes(expireMinutes);
		CacheMgt.get().register(this);
	}	//	CCache

	/**	Name						*/
	private String				m_name = null;
	/** Expire after minutes		*/
	private int					m_expire = 0;
	/** Time						*/ 
	private volatile long		m_timeExp = 0;
	/**	Just reset - not used		*/
	private boolean				m_justReset = true;
	
	/** Vetoable Change Support			*/
	private VetoableChangeSupport	m_changeSupport = null;
	/** Vetoable Change Support	Name	*/
	private static String		PROPERTYNAME = "cache"; 
	
	/**
	 * 	Get (table) Name
	 *	@return name
	 */
	public String getName()
	{
		return m_name;
	}	//	getName

	/**
	 * 	Set Expire Minutes and start it
	 *	@param expireMinutes minutes or 0
	 */
	public void setExpireMinutes (int expireMinutes)
	{
		if (expireMinutes > 0)
		{
			m_expire = expireMinutes;
			long addMS = 60000L * m_expire;
			m_timeExp = System.currentTimeMillis() + addMS;
		}
		else
		{
			m_expire = 0;
			m_timeExp = 0;
		}
	}	//	setExpireMinutes

	/**
	 * 	Get Expire Minutes
	 *	@return expire minutes
	 */
	public int getExpireMinutes()
	{
		return m_expire;
	}	//	getExpireMinutes

	/**
	 * 	Cache was reset
	 *	@return true if reset
	 */
	public boolean isReset()
	{
		return m_justReset;
	}	//	isReset

	/**
	 * 	Resets the Reset flag
	 */
	public void setUsed()
	{
		m_justReset = false;
	}	//	setUsed
	
	/**
	 *	Reset Cache
	 * 	@return number of items cleared
	 *	@see org.compiere.util.CacheInterface#reset()
	 */
	public int reset()
	{
		int no = super.size();
		clear();
		return no;
	}	//	reset

	/**
	 * 	Expire Cache if enabled
	 */	
	private void expire()
	{
		if (m_expire != 0 && m_timeExp < System.currentTimeMillis())
		{
		//	System.out.println ("------------ Expired: " + getName() + " --------------------");
			reset();
		}
	}	//	expire

	/**
	 * 	String Representation
	 * 	@return info
	 */
	public String toString()
	{
		return "CCache[" + m_name 
			+ ",Exp=" + getExpireMinutes()  
			+ ", #" + super.size() + "]";
	}	//	toString

	/**
	 * 	Clear cache and calculate new expiry time
	 *	@see java.util.Map#clear()
	 */
	public void clear()
	{
		if (m_changeSupport != null)
		{
			try
			{
				m_changeSupport.fireVetoableChange(PROPERTYNAME, super.size(), 0);
			}
			catch (Exception e)
			{
				System.out.println ("CCache.clear - " + e);
				return;
			}
		}
		//	Clear
		super.clear();
		if (m_expire != 0)
		{
			long addMS = 60000L * m_expire;
			m_timeExp = System.currentTimeMillis() + addMS;
		}
		m_justReset = true;
	}	//	clear

	
	/**
	 *	@see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key)
	{
		expire();
		return super.containsKey(key);
	}	//	containsKey

	/**
	 *	@see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value)
	{
		expire();
		return super.containsValue(value);
	}	//	containsValue

	/**
	 *	@see java.util.Map#entrySet()
	 */
	public Set<Map.Entry<K,V>> entrySet()
	{
		expire();
		return super.entrySet();
	}	//	entrySet

	/**
	 *	@see java.util.Map#get(java.lang.Object)
	 */
	public V get(Object key)
	{
		expire();
		return super.get(key);
	}	//	get

	/**
	 * 	Put value
	 *	@param key key
	 *	@param value value
	 *	@return previous value
	 */
	public V put (K key, V value)
	{
		expire();
		m_justReset = false;
		return super.put (key, value);
	}	// put

	/**
	 * 	Put All
	 *	@param m map
	 */
	public void putAll (Map<? extends K, ? extends V> m)
	{
		expire();
		m_justReset = false;
		super.putAll (m);
	}	//	putAll
	
	/**
	 *	@see java.util.Map#isEmpty()
	 */
	public boolean isEmpty()
	{
		expire();
		return super.isEmpty();
	}	// isEmpty

	/**
	 *	@see java.util.Map#keySet()
	 */
	public Set<K> keySet()
	{
		expire();
		return super.keySet();
	}	//	keySet

	/**
	 *	@see java.util.Map#size()
	 */
	public int size()
	{
		expire();
		return super.size();
	}	//	size

	/**
	 * 	Get Size w/o Expire
	 *	@see java.util.Map#size()
	 */
	public int sizeNoExpire()
	{
		return super.size();
	}	//	size

	/**
	 *	@see java.util.Map#values()
	 */
	public Collection<V> values()
	{
		expire();
		return super.values();
	}	//	values

	
	/**
	 * 	Add Vetoable Change Listener
	 *	@param listener listner
	 */
	public void addVetoableChangeListener (VetoableChangeListener listener)
	{
		if (m_changeSupport == null)
			m_changeSupport = new VetoableChangeSupport (this);
		if (listener != null)
			m_changeSupport.addVetoableChangeListener(listener);
	}	//	addVetoableChangeListener

	/**
	 * 	Remove Vetoable Change Listener
	 *	@param listener listener
	 */
    public void removeVetoableChangeListener (VetoableChangeListener listener) 
    {
		if (m_changeSupport != null && listener != null)
			m_changeSupport.removeVetoableChangeListener(listener);
    }	//	removeVetoableChangeListener
	
}	//	CCache
