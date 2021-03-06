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
import javax.swing.*;
import org.compiere.util.*;

/**
 *	Base Class for MLookup, MLocator.
 *  as well as for MLocation, MAccount (only single value)
 *  Maintains selectable data as NamePairs in ArrayList
 *  The objects itself may be shared by the lookup implementation (ususally HashMap)
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: Lookup.java,v 1.22 2005/09/24 01:53:35 jjanke Exp $
 */
public abstract class Lookup extends AbstractListModel
	implements MutableComboBoxModel, Serializable
{
	/**
	 *  Lookup
	 * 	@param displayType display type
	 * 	@param windowNo window no
	 */
	public Lookup (int displayType, int windowNo)
	{
		m_displayType = displayType;
		m_WindowNo = windowNo;
	}   //  Lookup

	/** The Data List           */
	protected volatile ArrayList<Object>   p_data = new ArrayList<Object>();

	/** The Selected Item       */
	private volatile Object         m_selectedObject;

	/** Temporary Data          */
	private Object[]                m_tempData = null;

	/**	Logger					*/
	protected CLogger				log = CLogger.getCLogger(getClass());

	/**	Display Type			*/
	private int						m_displayType;
	/**	Window No				*/
	private int						m_WindowNo;

	/**
	 * 	Get Display Type
	 *	@return display type
	 */
	public int getDisplayType()
	{
		return m_displayType;
	}	//	getDisplayType

	/**
	 * 	Get Window No
	 *	@return Window No
	 */
	public int getWindowNo()
	{
		return m_WindowNo;
	}	//	getWindowNo

	
	/**************************************************************************
	 * Set the value of the selected item. The selected item may be null.
	 * <p>
	 * @param anObject The combo box value or null for no selection.
	 */
	public void setSelectedItem(Object anObject)
	{
		if ((m_selectedObject != null && !m_selectedObject.equals( anObject ))
			|| m_selectedObject == null && anObject != null)
		{
			if (p_data.contains(anObject) || anObject == null)
			{
				m_selectedObject = anObject;
			//	Log.trace(s_ll, "Lookup.setSelectedItem", anObject);
			}
			else
			{
				m_selectedObject = null;
				log.fine(getColumnName() + ": setSelectedItem - Set to NULL");
			}
		//	if (m_worker == null || !m_worker.isAlive())
			fireContentsChanged(this, -1, -1);
		}
	}   //  setSelectedItem

	/**
	 *  Return previously selected Item
	 *  @return value
	 */
	public Object getSelectedItem()
	{
		return m_selectedObject;
	}   //  getSelectedItem

	/**
	 *  Get Size of Model
	 *  @return size
	 */
	public int getSize()
	{
		return p_data.size();
	}   //  getSize

	/**
	 *  Get Element at Index
	 *  @param index index
	 *  @return value
	 */
	public Object getElementAt (int index)
	{
		return p_data.get(index);
	}   //  getElementAt

	/**
	 * Returns the index-position of the specified object in the list.
	 *
	 * @param anObject object
	 * @return an int representing the index position, where 0 is
	 *         the first position
	 */
	public int getIndexOf (Object anObject)
	{
		return p_data.indexOf(anObject);
	}   //  getIndexOf

	/**
	 *  Add Element at the end
	 *  @param anObject object
	 */
	public void addElement (Object anObject)
	{
		p_data.add(anObject);
		fireIntervalAdded (this, p_data.size()-1, p_data.size()-1);
		if (p_data.size() == 1 && m_selectedObject == null && anObject != null)
			setSelectedItem (anObject);
	}   //  addElement

	/**
	 *  Insert Element At
	 *  @param anObject object
	 *  @param index index
	 */
	public void insertElementAt (Object anObject, int index)
	{
		p_data.add (index, anObject);
		fireIntervalAdded (this, index, index);
	}   //  insertElementAt

	/**
	 *  Remove Item at index
	 *  @param index index
	 */
	public void removeElementAt (int index)
	{
		if (getElementAt(index) == m_selectedObject)
		{
			if (index == 0)
				setSelectedItem (getSize() == 1 ? null : getElementAt( index + 1 ));
			else
				setSelectedItem (getElementAt (index - 1));
		}
		p_data.remove(index);
		fireIntervalRemoved (this, index, index);
	}   //  removeElementAt

	/**
	 *  Remove Item
	 *  @param anObject object
	 */
	public void removeElement (Object anObject)
	{
		int index = p_data.indexOf (anObject);
		if (index != -1)
			removeElementAt(index);
	}   //  removeItem

	/**
	 *  Empties the list.
	 */
	public void removeAllElements()
	{
		if (p_data.size() > 0)
		{
			int firstIndex = 0;
			int lastIndex = p_data.size() - 1;
			p_data.clear();
			m_selectedObject = null;
			fireIntervalRemoved (this, firstIndex, lastIndex);
		}
	}   //  removeAllElements

	
	/**************************************************************************
	 *	Put Value
	 *  @param key key
	 *  @param value value
	 */
	public void put (String key, String value)
	{
		NamePair pp = new ValueNamePair (key, value);
		addElement(pp);
	}	//	put

	/**
	 *	Put Value
	 *  @param key key
	 *  @param value value
	 */
	public void put (int key, String value)
	{
		NamePair pp = new KeyNamePair (key, value);
		addElement(pp);
	}	//	put

	/**
	 *  Fill ComboBox with lookup data (async using Worker).
	 *  - try to maintain selected item
	 *  @param mandatory  has mandatory data only (i.e. no "null" selection)
	 *  @param onlyValidated only validated
	 *  @param onlyActive onlt active
	 *  @param temporary  save current values - restore via fillComboBox (true)
	 */
	public void fillComboBox (boolean mandatory, boolean onlyValidated, boolean onlyActive, boolean temporary)
	{
		long startTime = System.currentTimeMillis();

		//  Save current data
		if (temporary)
		{
			int size = p_data.size();
			m_tempData = new Object[size];
			//  We need to do a deep copy, so store it in Array
			p_data.toArray(m_tempData);
		//	for (int i = 0; i < size; i++)
		//		m_tempData[i] = p_data.get(i);
		}


		Object obj = m_selectedObject;
		p_data.clear();

		//  may cause delay *** The Actual Work ***
		p_data = getData (mandatory, onlyValidated, onlyActive, temporary);

		//  Selected Object changed
		if (obj != m_selectedObject)
		{
			log.finest(getColumnName() + ": fillComboBox - SelectedValue Changed=" + obj + "->" + m_selectedObject);
			obj = m_selectedObject;
		}

		//  if nothing selected & mandatory, select first
		if (obj == null && mandatory  && p_data.size() > 0)
		{
			obj = p_data.get(0);
			m_selectedObject = obj;
			log.finest(getColumnName() + ": fillComboBox - SelectedValue SetToFirst=" + obj);
		//	fireContentsChanged(this, -1, -1);
		}
		fireContentsChanged(this, 0, p_data.size());
		if (p_data.size() == 0)
			log.fine(getColumnName() + ": fillComboBox - #0 - ms=" + String.valueOf(System.currentTimeMillis()-startTime)
				 );
		else
			log.fine(getColumnName() + ": fillComboBox - #" + p_data.size() + " - ms=" + String.valueOf(System.currentTimeMillis()-startTime));
	}   //  fillComboBox

	/**
	 *  Fill ComboBox with old saved data (if exists) or all data available
	 *  @param restore if true, use saved data - else fill it with all data
	 */
	public void fillComboBox (boolean restore)
	{
		if (restore && m_tempData != null)
		{
			Object obj = m_selectedObject;
			p_data.clear();
			//  restore old data
			p_data = new ArrayList<Object>(m_tempData.length);
			for (int i = 0; i < m_tempData.length; i++)
				p_data.add(m_tempData[i]);
			m_tempData = null;

			//  if nothing selected, select first
			if (obj == null && p_data.size() > 0)
				obj = p_data.get(0);
			setSelectedItem(obj);
			fireContentsChanged(this, 0, p_data.size());
			return;
		}
		if (p_data != null)
			fillComboBox(false, false, false, false);
	}   //  fillComboBox

	
	/**************************************************************************
	 *	Get Display of Key Value
	 *  @param key key
	 *  @return String
	 */
	public abstract String getDisplay (Object key);

	/**
	 *	Get Object of Key Value
	 *  @param key key
	 *  @return Object or null
	 */
	public abstract NamePair get (Object key);


	/**
	 *  Fill ComboBox with Data (Value/KeyNamePair)
	 *  @param mandatory  has mandatory data only (i.e. no "null" selection)
	 *  @param onlyValidated only validated
	 *  @param onlyActive only active
	 * 	@param temporary force load for temporary display
	 *  @return ArrayList
	 */
	public abstract ArrayList<Object> getData (boolean mandatory, 
		boolean onlyValidated, boolean onlyActive, boolean temporary);

	/**
	 *	Get underlying fully qualified Table.Column Name.
	 *	Used for VLookup.actionButton (Zoom)
	 *  @return column name
	 */
	public abstract String getColumnName();

	/**
	 *  The Lookup contains the key
	 *  @param key key
	 *  @return true if contains key
	 */
	public abstract boolean containsKey (Object key);

	
	/**************************************************************************
	 *	Refresh Values - default implementation
	 *  @return size
	 */
	public int refresh()
	{
		return 0;
	}	//	refresh

	/**
	 *	Is Validated - default implementation
	 *  @return true if validated
	 */
	public boolean isValidated()
	{
		return true;
	}	//	isValidated

	/**
	 *  Get dynamic Validation SQL (none)
	 *  @return validation
	 */
	public String getValidation()
	{
		return "";
	}   //  getValidation

	/**
	 *  Has Inactive records - default implementation
	 *  @return true if inactive
	 */
	public boolean hasInactive()
	{
		return false;
	}

	/**
	 *	Get Zoom - default implementation
	 *  @return Zoom Window
	 */
	public int getZoom()
	{
		return 0;
	}	//	getZoom

	/**
	 *	Get Zoom - default implementation
	 * 	@param query query
	 *  @return Zoom Window - here 0
	 */
	public int getZoom(MQuery query)
	{
		return 0;
	}	//	getZoom

	/**
	 *	Get Zoom Query String - default implementation
	 *  @return Zoom Query
	 */
	public MQuery getZoomQuery()
	{
		return null;
	}	//	getZoomQuery

	/**
	 *	Get Data Direct from Table.
	 *	Default implementation - does not requery
	 *  @param key key
	 *  @param saveInCache save in cache for r/w
	 * 	@param cacheLocal cache locally for r/o
	 *  @return value
	 */
	public NamePair getDirect (Object key, boolean saveInCache, boolean cacheLocal)
	{
		return get (key);
	}	//	getDirect

	/**
	 *  Dispose - clear items w/o firing events
	 */
	public void dispose()
	{
		if (p_data != null)
			p_data.clear();
		p_data = null;
		m_selectedObject = null;
		m_tempData = null;
	}   //  dispose

	/**
	 *  Wait until async Load Complete
	 */
	public void loadComplete()
	{
	}   //  loadComplete

}	//	Lookup
