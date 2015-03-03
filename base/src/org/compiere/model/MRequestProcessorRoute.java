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
 *	Request Processor Route
 *	
 *  @author Jorg Janke
 *  @version $Id: MRequestProcessorRoute.java,v 1.5 2005/05/17 05:29:53 jjanke Exp $
 */
public class MRequestProcessorRoute extends X_R_RequestProcessor_Route
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param R_RequestProcessor_Route_ID id
	 */
	public MRequestProcessorRoute (Properties ctx, int R_RequestProcessor_Route_ID, String trxName)
	{
		super (ctx, R_RequestProcessor_Route_ID, trxName);
	}	//	MRequestProcessorRoute

	/**
	 * 	Load Constructor
	 *	@param ctx context 
	 *	@param rs result set
	 */
	public MRequestProcessorRoute (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRequestProcessorRoute

}	//	MRequestProcessorRoute
