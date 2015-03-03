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
import org.compiere.util.*;


/**
 *	Resource Unavailable
 *	
 *  @author Jorg Janke
 *  @version $Id: MResourceUnAvailable.java,v 1.6 2005/07/21 16:43:51 jjanke Exp $
 */
public class MResourceUnAvailable extends X_S_ResourceUnAvailable
{

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param S_ResourceUnAvailable_ID id
	 *	@param trxName trx 
	 */
	public MResourceUnAvailable (Properties ctx, int S_ResourceUnAvailable_ID, String trxName)
	{
		super (ctx, S_ResourceUnAvailable_ID, trxName);
	}	//	MResourceUnAvailable

	/**
	 * 	MResourceUnAvailable
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MResourceUnAvailable (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MResourceUnAvailable
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (getDateTo() == null)
			setDateTo(getDateFrom());
                // Bugfix Gunther Hoppe, 06.10.2005 add vpj-cd e-evolution
                // Begin		
		// if (getDateTo().before(getDateFrom()))
                // End
		if (getDateFrom().after(getDateTo()))
		{
			log.saveError("Error", Msg.parseTranslation(getCtx(), "@DateTo@ > @DateFrom@"));
			return false;
		}
		return true;
	}	//	beforeSave
	
}	//	MResourceUnAvailable
