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
import java.util.*;
import org.compiere.util.*;

/**
 *  MultiMap allows multiple keys with their values.
 *  It accepts null values as keys and values.
 *  (implemented as two array lists)
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: MultiMap.java,v 1.7 2005/10/08 02:02:29 jjanke Exp $
 */
public final class MultiMap<K,V> implements Map<K,V>, Serializable
{
	/**
	 *  Constructor with 10 initial Capacity (same as ArrayList)
	 */
	public MultiMap()
	{
		this(10);
	}   //  MultiMap

	/**
	 *  Constructor
	 */
	public MultiMap(int initialCapacity)
	{
		m_keys = new ArrayList<K>(initialCapacity);
		m_values = new ArrayList<V>(initialCapacity);
	}   //  MultiMap

	private ArrayList<K>	m_keys = null;
	private ArrayList<V>	m_values = null;

	/**
	 *  Return number of elements
	 */
	public int size()
	{
		return m_keys.size();
	}   //  size

	/**
	 *  Is Empty
	 */
	public boolean isEmpty()
	{
		return (m_keys.size() == 0);
	}   //  isEmpty

	/**
	 *  Contains Key
	 */
	public boolean containsKey(Object key)
	{
		return m_keys.contains(key);
	}   //  containsKey

	/**
	 *  Contains Value
	 */
	public boolean containsValue(Object value)
	{
		return m_values.contains(value);
	}   //  containsKey

	/**
	 *  Return ArrayList of Values of Key
	 */
	@SuppressWarnings("unchecked")
	public V get(Object key)
	{
		return (V)getValues(key);
	}   //  get

	/**
	 *  Return ArrayList of Values of Key
	 */
	public ArrayList getValues (Object key)
	{
		ArrayList<V> list = new ArrayList<V>();
		//  We don't have it
		if (!m_keys.contains(key))
			return list;
		//  go through keys
		int size = m_keys.size();
		for (int i = 0; i < size; i++)
		{
			if (m_keys.get(i).equals(key))
				if (!list.contains(m_values.get(i)))
					list.add(m_values.get(i));
		}
		return list;
	}   //  getValues

	/**
	 *  Return ArrayList of Keys with Value
	 */
	public ArrayList getKeys (Object value)
	{
		ArrayList<K> list = new ArrayList<K>();
		//  We don't have it
		if (!m_values.contains(value))
			return list;
		//  go through keys
		int size = m_values.size();
		for (int i = 0; i < size; i++)
		{
			if (m_values.get(i).equals(value))
				if (!list.contains(m_keys.get(i)))
					list.add(m_keys.get(i));
		}
		return list;
	}   //  getKeys

	/**
	 *  Put Key & Value
	 *  @return always null
	 */
	public V put (K key, V value)
	{
		m_keys.add(key);
		m_values.add(value);
		return null;
	}   //  put

	/**
	 *  Remove key
	 */
	public V remove (Object key)
	{
		throw new java.lang.UnsupportedOperationException("Method remove() not implemented.");
	}   //  remove

	/**
	 *  Put all
	 */
	public void putAll(Map t)
	{
		throw new java.lang.UnsupportedOperationException("Method putAll() not implemented.");
	}   //  putAll

	/**
	 *  Clear content
	 */
	public void clear()
	{
		m_keys.clear();
		m_values.clear();
	}   //  clear

	/**
	 *  Return HashSet of Keys
	 */
	public Set<K> keySet()
	{
		HashSet<K> keys = new HashSet<K>(m_keys);
		return keys;
	}   //  keySet

	/**
	 *  Return Collection of values
	 */
	public Collection<V> values()
	{
		return m_values;
	}

	/**
	 *
	 */
	public Set<Map.Entry<K, V>> entrySet()
	{
		throw new java.lang.UnsupportedOperationException("Method entrySet() not implemented.");
	}

	/**
	 *
	 */
	public boolean equals(Object o)
	{
		throw new java.lang.UnsupportedOperationException("Method equals() not implemented.");
	}

	/**************************************************************************
	 *  Returns class name and number of entries
	 */
	public String toString()
	{
		return "MultiMap #" + m_keys.size();
	}

	/**
	 *  dump all keys - values to log
	 */
	public void printToLog()
	{
		CLogger	log = CLogger.getCLogger(getClass());
		log.fine("MultiMap.printToLog");
		int size = m_keys.size();
		for (int i = 0; i < size; i++)
		{
			Object k = m_keys.get(i);
			Object v = m_values.get(i);
			log.finest(k==null ? "null" : k.toString() + "=" + v==null ? "null" : v.toString());
		}
	}   //  printToLog

}   //  MultiMap
