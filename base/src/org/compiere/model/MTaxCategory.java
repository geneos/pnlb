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
 * 	Tax Category Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MTaxCategory.java,v 1.1 2005/07/18 03:47:42 jjanke Exp $
 */
public class MTaxCategory extends X_C_TaxCategory
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_TaxCategory_ID id
	 *	@param trxName trx
	 */
	public MTaxCategory (Properties ctx, int C_TaxCategory_ID, String trxName)
	{
		super (ctx, C_TaxCategory_ID, trxName);
		if (C_TaxCategory_ID == 0)
		{
		//	setName (null);
			setIsDefault (false);
		}
	}	//	MTaxCategory

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs resukt set
	 *	@param trxName trx
	 */
	public MTaxCategory (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MTaxCategory

}	//	MTaxCategory
