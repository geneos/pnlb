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
package org.compiere.grid;

import java.util.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Manual Lookup (Model)- loaded by the put method
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: XLookup.java,v 1.10 2005/11/14 02:10:58 jjanke Exp $
 */
public class XLookup extends Lookup
{
	/**
	 *	Manual Lookup
	 * 	@param keyColumn key Column
	 */
	public XLookup(String keyColumn)
	{
		super(DisplayType.TableDir, 0);
		m_keyColumn = keyColumn;
	}	//	XLookup


	/** Key Column - as identifier      */
	private String		m_keyColumn;

	/**
	 *	Get Display String of key value
	 * 	@param key key
	 * 	@return display
	 */
	public String getDisplay (Object key)
	{
		//  linear seatch in m_data
		for (int i = 0; i < p_data.size(); i++)
		{
			Object oo = p_data.get(i);
			if (oo != null && oo instanceof NamePair)
			{
				NamePair pp = (NamePair)oo;
				if (pp.getID().equals(key))
					return pp.getName();
			}
		}
		return "<" + key + ">";
	}	//	getDisplay

	/**
	 *  The Lookup contains the key
	 * 	@param key key
	 * 	@return true if contains key
	 */
	public boolean containsKey (Object key)
	{
		//  linear seatch in p_data
		for (int i = 0; i < p_data.size(); i++)
		{
			Object oo = p_data.get(i);
			if (oo != null && oo instanceof NamePair)
			{
				NamePair pp = (NamePair)oo;
				if (pp.getID().equals(key))
					return true;
			}
		}
		return false;
	}   //  containsKey

	/**
	 *	Get Object of Key Value
	 *  @param key key
	 *  @return Object or null
	 */
	public NamePair get (Object key)
	{
		//  linear seatch in m_data
		for (int i = 0; i < p_data.size(); i++)
		{
			Object oo = p_data.get(i);
			if (oo != null && oo instanceof NamePair)
			{
				NamePair pp = (NamePair)oo;
				if (pp.getID().equals(key))
					return pp;
			}
		}
		return null;
	}	//	get


	/**
	 *	Return data as sorted Array
	 * 	@param mandatory mandatory
	 * 	@param onlyValidated only validated
	 * 	@param onlyActive only active
	 * 	@param temporary force load for temporary display
	 * 	@return list of data
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Object> getData (boolean mandatory, 
		boolean onlyValidated, boolean onlyActive, boolean temporary)
	{
		ArrayList<Object> list = new ArrayList<Object>(p_data);
		
		//	Sort Data
		if (m_keyColumn.endsWith("_ID"))
		{
			KeyNamePair p = new KeyNamePair (-1, "");
			if (!mandatory)
				list.add (p);
			Collections.sort (list, p);
		}
		else
		{
			ValueNamePair p = new ValueNamePair (null, "");
			if (!mandatory)
				list.add (p);
			Collections.sort (list, p);
		}
		return list;
	}	//	getArray

	/**
	 *	Refresh Values (nop)
	 * 	@return number of cache
	 */
	public int refresh()
	{
		return p_data.size();
	}	//	refresh

	/**
	 *	Get underlying fully qualified Table.Column Name
	 * 	@return column name
	 */
	public String getColumnName()
	{
		return m_keyColumn;
	}   //  getColumnName

}	//	XLookup
