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

import java.sql.*;
import java.util.*;


/**
 *	Alert Rule Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MAlertRule.java,v 1.6 2005/09/19 04:49:48 jjanke Exp $
 */
public class MAlertRule extends X_AD_AlertRule
{
	/**
	 * 	Standatd Constructor
	 *	@param ctx context
	 *	@param AD_AlertRule_ID id
	 */
	public MAlertRule (Properties ctx, int AD_AlertRule_ID, String trxName)
	{
		super (ctx, AD_AlertRule_ID, trxName);
	}	//	MAlertRule

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MAlertRule (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MAlertRule
	
	/**
	 * 	Get Sql
	 *	@return sql
	 */
	public String getSql()
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ").append(getSelectClause())
			.append(" FROM ").append(getFromClause());
		if (getWhereClause() != null && getWhereClause().length() > 0)
			sql.append(" WHERE ").append(getWhereClause());
		if (getOtherClause() != null && getOtherClause().length() > 0)
			sql.append(" ").append(getOtherClause());
		return sql.toString();
	}	//	getSql
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (newRecord)
			setIsValid(true);
		if (isValid())
			setErrorMsg(null);
		return true;
	}	//	beforeSave

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MAlertRule[");
		sb.append(get_ID())
			.append("-").append(getName())
			.append(",Valid=").append(isValid())
			.append(",").append(getSql());
		sb.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MAlertRule
