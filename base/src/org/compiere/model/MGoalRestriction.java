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
 * 	Performance Goal Restriction
 *	
 *  @author Jorg Janke
 *  @version $Id: MGoalRestriction.java,v 1.1 2005/12/27 06:17:56 jjanke Exp $
 */
public class MGoalRestriction extends X_PA_GoalRestriction
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param PA_GoalRestriction_ID id
	 *	@param trxName trx
	 */
	public MGoalRestriction (Properties ctx, int PA_GoalRestriction_ID,
		String trxName)
	{
		super (ctx, PA_GoalRestriction_ID, trxName);
	}	//	MGoalRestriction

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MGoalRestriction (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MGoalRestriction
	
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MGoalRestriction[");
		sb.append (get_ID()).append ("-").append (getName()).append ("]");
		return sb.toString ();
	}	//	toString
}	//	MGoalRestriction
