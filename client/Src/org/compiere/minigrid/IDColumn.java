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
package org.compiere.minigrid;

/**
 *  ID Column for MiniGrid allows to select a column and maintains the record ID
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: IDColumn.java,v 1.7 2005/03/11 20:28:31 jjanke Exp $
 */
public class IDColumn
{
	/**
	 *  ID Column constructor
	 *  @param record_ID
	 */
	public IDColumn (int record_ID)
	{
		this(new Integer(record_ID));
	}   //  IDColumn

	/**
	 *  ID Column constructor
	 *  @param record_ID
	 */
	public IDColumn(Integer record_ID)
	{
		super();
		setRecord_ID(record_ID);
		setSelected(false);
	}   //  IDColumn

	/** Is the row selected         */
	private boolean     m_selected = false;
	/** The Record_ID               */
	private Integer     m_record_ID;


	/**
	 *  Set Selection
	 *  @param selected
	 */
	public void setSelected(boolean selected)
	{
		m_selected = selected;
	}
	/**
	 *  Is Selected
	 *  @return true if selected
	 */
	public boolean isSelected()
	{
		return m_selected;
	}

	/**
	 *  Set Record_ID
	 *  @param record_ID
	 */
	public void setRecord_ID(Integer record_ID)
	{
		m_record_ID = record_ID;
	}
	/**
	 * Get Record ID
	 * @return ID
	 */
	public Integer getRecord_ID()
	{
		return m_record_ID;
	}

	/**
	 *  To String
	 *  @return String representation
	 */
	public String toString()
	{
		return "IDColumn - ID=" + m_record_ID + ", Selected=" + m_selected;
	}   //  toString

}   //  IDColumn
