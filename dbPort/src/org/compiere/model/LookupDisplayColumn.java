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

import java.io.Serializable;


/**
 *  Lookup Display Column Value Object
 *
 *  @author Jorg Janke
 *  @version $Id: LookupDisplayColumn.java,v 1.4 2005/03/11 20:28:35 jjanke Exp $
 */
public class LookupDisplayColumn implements Serializable
{
	/**
	 *	Lookup Column Value Object
	 * 	@param columnName column name
	 * 	@param isTranslated translated
	 * 	@param ad_Reference_ID display type
	 * 	@param ad_Reference_Value_ID table/list reference id
	 */
	public LookupDisplayColumn(String columnName, boolean isTranslated,
		int ad_Reference_ID, int ad_Reference_Value_ID)
	{
		ColumnName = columnName;
		IsTranslated = isTranslated;
		DisplayType = ad_Reference_ID;
		AD_Reference_ID = ad_Reference_Value_ID;
	}	//

	public String 	ColumnName;
	public boolean 	IsTranslated;
	public int 		DisplayType;
	public int 		AD_Reference_ID;

	/**
	 * 	String Representation
	 * 	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("LookupDisplayColumn[");
		sb.append("ColumnName=").append(ColumnName);
		if (IsTranslated)
			sb.append(",IsTranslated");
		sb.append(",DisplayType=").append(DisplayType);
		if (AD_Reference_ID != 0)
			sb.append(",AD_Reference_ID=").append(AD_Reference_ID);
		sb.append("]");
		return sb.toString();
	}	//	toString

}	//	LookupDisplayColumn
