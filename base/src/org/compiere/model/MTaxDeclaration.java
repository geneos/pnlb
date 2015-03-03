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
 * 	Tax Declaration Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MTaxDeclaration.java,v 1.1 2005/10/19 17:15:27 jjanke Exp $
 */
public class MTaxDeclaration extends X_C_TaxDeclaration
{
	/**
	 * 	Standard Constructors
	 *	@param ctx context
	 *	@param C_TaxDeclaration_ID ic
	 *	@param trxName trx
	 */
	public MTaxDeclaration (Properties ctx, int C_TaxDeclaration_ID, String trxName)
	{
		super (ctx, C_TaxDeclaration_ID, trxName);
	}	//	MTaxDeclaration

	/**
	 * 	Load Constructor
	 *	@param ctx context 
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MTaxDeclaration (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MTaxDeclaration
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (is_ValueChanged("DateFrom"))
			setDateFrom(TimeUtil.getDay(getDateFrom()));
		if (is_ValueChanged("DateTo"))
			setDateTo(TimeUtil.getDay(getDateTo()));
		return true;
	}	//	beforeSave
	
}	//	MTaxDeclaration
