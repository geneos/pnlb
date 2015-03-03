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
 *	Discount Schema Line (Price List) Model 
 *	
 *  @author Jorg Janke
 *  @version $Id: MDiscountSchemaLine.java,v 1.4 2005/03/11 20:26:03 jjanke Exp $
 */
public class MDiscountSchemaLine extends X_M_DiscountSchemaLine
{

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_DiscountSchemaLine_ID id
	 */
	public MDiscountSchemaLine (Properties ctx, int M_DiscountSchemaLine_ID, String trxName)
	{
		super (ctx, M_DiscountSchemaLine_ID, trxName);
	}	//	MDiscountSchemaLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MDiscountSchemaLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MDiscountSchemaLine

}	//	MDiscountSchemaLine