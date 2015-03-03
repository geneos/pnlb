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
import java.math.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	PO Info Column Info Value Object
 *	
 *  @author Jorg Janke
 *  @version $Id: POInfoColumn.java,v 1.5 2005/07/20 19:28:13 jjanke Exp $
 */
public class POInfoColumn implements Serializable
{
	/** Used by Remote FinReport			*/
	static final long serialVersionUID = -3983585608504631958L;
	
	/**
	 *  Constructor
	 *	@param ad_Column_ID Column ID
	 *	@param columnName Dolumn name
	 *	@param columnSQL virtual column
	 *	@param displayType Display Type
	 *	@param isMandatory Mandatory
	 *	@param isUpdateable Updateable
	 *	@param defaultLogic Default Logic
	 *	@param columnLabel Column Label
	 *	@param columnDescription Column Description
	 *	@param isKey true if key
	 *	@param isParent true if parent
	 *	@param ad_Reference_Value_ID reference value
	 *	@param validationCode sql validation code
	 *	@param fieldLength Field Length
	 * 	@param valueMin minimal value
	 * 	@param valueMax maximal value
	 */
	public POInfoColumn (int ad_Column_ID, String columnName, String columnSQL, int displayType,
		boolean isMandatory, boolean isUpdateable, String defaultLogic,
		String columnLabel, String columnDescription,
		boolean isKey, boolean isParent,
		int ad_Reference_Value_ID, String validationCode,
		int fieldLength, String valueMin, String valueMax,
		boolean isTranslated, boolean isEncrypted)
	{
		AD_Column_ID = ad_Column_ID;
		ColumnName = columnName;
		ColumnSQL = columnSQL;
		DisplayType = displayType;
		if (columnName.equals("AD_Language"))
		{
			DisplayType = org.compiere.util.DisplayType.String;
			ColumnClass = String.class;
		}
		else if (columnName.equals("Posted") 
			|| columnName.equals("Processed")
			|| columnName.equals("Processing"))
		{
			ColumnClass = Boolean.class;
		}
		else if (columnName.equals("Record_ID"))
		{
			DisplayType = org.compiere.util.DisplayType.ID;
			ColumnClass = Integer.class;
		}
		else
			ColumnClass = org.compiere.util.DisplayType.getClass(displayType, true);
		IsMandatory = isMandatory;
		IsUpdateable = isUpdateable;
		DefaultLogic = defaultLogic;
		ColumnLabel = columnLabel;
		ColumnDescription = columnDescription;
		IsKey = isKey;
		IsParent = isParent;
		//
		AD_Reference_Value_ID = ad_Reference_Value_ID;
		ValidationCode = validationCode;
		//
		FieldLength = fieldLength;
		ValueMin = valueMin;
		try
		{
			if (valueMin != null && valueMin.length() > 0)
				ValueMin_BD = new BigDecimal(valueMin);
		}
		catch (Exception ex)
		{
			CLogger.get().log(Level.SEVERE, "ValueMin=" + valueMin, ex);
		}
		ValueMax = valueMax;
		try
		{
			if (valueMax != null && valueMax.length() > 0)
				ValueMax_BD = new BigDecimal(valueMax);
		}
		catch (Exception ex)
		{
			CLogger.get().log(Level.SEVERE, "ValueMax=" + valueMax, ex);
		}
		IsTranslated = isTranslated;
		IsEncrypted = isEncrypted;
	}   //  Column

	public int          AD_Column_ID;
	public String       ColumnName;
	public String       ColumnSQL;
	public int          DisplayType;
	public Class        ColumnClass;
	public boolean      IsMandatory;
	public String       DefaultLogic;
	public boolean      IsUpdateable;
	public String       ColumnLabel;
	public String       ColumnDescription;
	public boolean		IsKey;
	public boolean		IsParent;
	public boolean		IsTranslated;
	public boolean		IsEncrypted;
	//
	public int			AD_Reference_Value_ID;
	public String		ValidationCode;
	//
	public int			FieldLength;
	public String		ValueMin;
	public String		ValueMax;
	public BigDecimal	ValueMin_BD = null;
	public BigDecimal	ValueMax_BD = null;

	/**
	 * 	String representation
	 *  @return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("POInfo.Column[");
		sb.append(ColumnName).append(",ID=").append(AD_Column_ID)
			.append(",DisplayType=").append(DisplayType)
			.append(",ColumnClass=").append(ColumnClass);
		sb.append("]");
		return sb.toString();
	}	//	toString

}	//	POInfoColumn
