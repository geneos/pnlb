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
 *	Revenue Recognition Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRevenueRecognition.java,v 1.5 2005/05/17 05:29:52 jjanke Exp $
 */
public class MRevenueRecognition extends X_C_RevenueRecognition
{

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_RevenueRecognition_ID id
	 */
	public MRevenueRecognition (Properties ctx, int C_RevenueRecognition_ID, String trxName)
	{
		super (ctx, C_RevenueRecognition_ID, trxName);
	}	//	MRevenueRecognition

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRevenueRecognition (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRevenueRecognition
	
}	//	MRevenueRecognition
