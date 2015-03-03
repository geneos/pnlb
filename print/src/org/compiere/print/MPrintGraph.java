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
package org.compiere.print;

import java.sql.*;
import java.util.*;

import org.compiere.model.*;

/**
 *	Graph Model
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: MPrintGraph.java,v 1.7 2005/11/13 23:40:21 jjanke Exp $
 */
public class MPrintGraph extends X_AD_PrintGraph
{
	/**
	 *	Standard Constructor
	 *  @param ctx context
	 *  @param AD_PrintGraph_ID graph id
	 *  @param trxName trx
	 */
	public MPrintGraph (Properties ctx, int AD_PrintGraph_ID, String trxName)
	{
		super (ctx, AD_PrintGraph_ID, trxName);
	}	//	MPrintGraph
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MPrintGraph (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MPrintGraph

}	//	MPrintGraph
