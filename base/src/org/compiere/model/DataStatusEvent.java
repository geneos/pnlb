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

/**
 *  Data Status Event
 *  <p>
 *	Indicates the current Status of the database
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: DataStatusEvent.java,v 1.8 2005/11/25 21:57:26 jjanke Exp $
 */
public final class DataStatusEvent extends EventObject implements Serializable
{
	/**
	 *	Constructor
	 *  @param source source
	 *  @param totalRows total rows
	 *  @param changed changed
	 *  @param autoSave auto save
	 *  @param inserting inserting
	 */
	public DataStatusEvent (Object source, int totalRows, boolean changed, 
		boolean autoSave, boolean inserting)
	{
		super(source);
		m_totalRows = totalRows;
		m_changed = changed;
		m_autoSave = autoSave;
		m_inserting = inserting;
	}	//	DataStatusEvent

	private int				m_totalRows;
	private boolean			m_changed;
	private boolean			m_autoSave;
	private boolean         m_inserting;
	//
	private String			m_AD_Message = null;
	private String          m_info = null;
	private boolean			m_isError = false;
	private boolean			m_isWarning = false;
	private boolean         m_confirmed = false;
	//
	private boolean			m_allLoaded = true;
	private int				m_loadedRows = -1;
	private int				m_currentRow = -1;
	//
	private int				m_changedColumn = 0;
	//
	public Timestamp        Created = null;
	public Integer          CreatedBy = null;
	public Timestamp        Updated = null;
	public Integer          UpdatedBy = null;
	//
	public String           Info = null;
	public int 				AD_Table_ID = 0;
	public Object			Record_ID = null;

	/**
	 *	Set Loaded Info
	 *  @param loadedRows loaded rows
	 */
	public void setLoading (int loadedRows)
	{
		m_allLoaded = false;
		m_loadedRows = loadedRows;
	}	//	setLoaded

	/**
	 *	Is loading
	 *  @return true if loading
	 */
	public boolean isLoading()
	{
		return !m_allLoaded;
	}	//	isLoading

	/**
	 *	Get loaded rows
	 *  @return loaded rows
	 */
	public int getLoadedRows()
	{
		return m_loadedRows;
	}	//	getLoadedRows

	/**
	 *	Set current Row (zero based)
	 *  @param currentRow current row
	 */
	public void setCurrentRow (int currentRow)
	{
		m_currentRow = currentRow;
	}	//	setCurrentRow

	/**
	 *	Get current row (zero based)
	 *  @return current roe
	 */
	public int getCurrentRow()
	{
		return m_currentRow;
	}	//	getCurrentRow

	/**
	 *	Get total rows
	 *  @return total rows
	 */
	public int getTotalRows()
	{
		return m_totalRows;
	}	//	getTotalRows

	/**
	 *	Set Message Info
	 *  @param AD_Message message
	 *  @param info info
	 *  @param isError error
	 */
	public void setInfo (String AD_Message, String info, boolean isError, boolean isWarning)
	{
		m_AD_Message = AD_Message;
		m_info = info;
		m_isError = isError;
		m_isWarning = isWarning;
	}	//	setInfo

	/**
	 *  Set Inserting
	 *  @param inserting inserting
	 */
	public void setInserting (boolean inserting)
	{
		m_inserting = inserting;
	}   //  setInserting

	/**
	 *  Are we inserting
	 *  @return true if inserting
	 */
	public boolean isInserting()
	{
		return m_inserting;
	}   //  isInserting

	/**
	 *	Get Message Info
	 *  @return Message
	 */
	public String getAD_Message()
	{
		return m_AD_Message;
	}	//	getAD_Message

	/**
	 *	Get Message Info
	 *  @return Info
	 */
	public String getInfo()
	{
		return m_info;
	}	//	getInfo

	/**
	 *	Is this an error
	 *  @return true if error
	 */
	public boolean isError()
	{
		return m_isError;
	}	//	isError

	/**
	 *	Is this a warning
	 *  @return true if warning
	 */
	public boolean isWarning()
	{
		return m_isWarning;
	}	//	isWarning

	/**
	 *	String representation of Status.
	 *  @return Examples:	+*1?/20
	 *		1/256->2000
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("DataStatusEvent - ");
		if (m_AD_Message != null)
			sb.append(m_AD_Message);
		if (m_info != null)
			sb.append(" ").append(m_info);
		sb.append(" : ").append(getMessage());
		return sb.toString();
	}	//	toString

	/**
	 *	String representation of Status.
	 *  <pre>
	 *		*1/20 		Change - automatic commit
	 *		?1/20		Change - manual confirm
	 *		 1/56->200	Loading
	 *		 1/20		Normal
	 *     +*1/20       Inserting, changed - automatic commit
	 *  The row number is converted from zero based representation
	 *  </pre>
	 *  @return Status info
	 */
	public String getMessage()
	{
		StringBuffer retValue = new StringBuffer();
		if (m_inserting)
			retValue.append("+");
		retValue.append(m_changed ? (m_autoSave ? "*" : "?") : " ");
		//  current row
		if (m_totalRows == 0)
			retValue.append(m_currentRow);
		else
			retValue.append(m_currentRow+1);
		//  of
		retValue.append("/");
		if (m_allLoaded)
			retValue.append(m_totalRows);
		else
			retValue.append(m_loadedRows).append("->").append(m_totalRows);
		//
		return retValue.toString();
	}	//	getMessage

	/**
	 *	Is Data Changed
	 *  @return true if changed
	 */
	public boolean isChanged()
	{
		return m_changed;
	}	//	isChanged

	/**
	 *	Is First Row - (zero based)
	 *  @return true if first row
	 */
	public boolean isFirstRow()
	{
		if (m_totalRows == 0)
			return true;
		return m_currentRow == 0;
	}	//	isFirstRow

	/**
	 *	Is Last Row - (zero based)
	 *  @return true if last row
	 */
	public boolean isLastRow()
	{
		if (m_totalRows == 0)
			return true;
		return m_currentRow == m_totalRows-1;
	}	//	isLastRow

	//begin vpj-cd e-evolution 30 Ene 2005
	/**
	 *	Is Last Row - (zero based)
	 *  @return true if last row
	 */
	public int  getNewRow()
	{
		return  m_currentRow + 1;
	}	//	isLastRow
	//end vpj-cd e-evolution 30 Ene 2005
	/**
	 *	Set Changed Column
	 *  @param col column
	 */
	public void setChangedColumn (int col)
	{
		m_changedColumn = col;
	}	//	setChangedColumn

	/**
	 *	Get Changed Column
	 *  @return changed column
	 */
	public int getChangedColumn()
	{
		return m_changedColumn;
	}   //	getChangedColumn

	/**
	 *  Set Confirmed toggle
	 *  @param confirmed confirmed
	 */
	public void setConfirmed (boolean confirmed)
	{
		m_confirmed = confirmed;
	}   //  setConfirmed

	/**
	 *  Is Confirmed (e.g. user has seen it)
	 *  @return true if confirmed
	 */
	public boolean isConfirmed()
	{
		return m_confirmed;
	}	//  isConfirmed

}	//	DataStatusEvent
