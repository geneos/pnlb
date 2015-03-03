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
 *	Dunning Level Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MDunningLevel.java,v 1.4 2005/03/11 20:25:59 jjanke Exp $
 */
public class MDunningLevel extends X_C_DunningLevel
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_DunningLevel_ID id
	 */
	public MDunningLevel (Properties ctx, int C_DunningLevel_ID, String trxName)
	{
		super (ctx, C_DunningLevel_ID, trxName);
	}	//	MDunningLevel

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MDunningLevel (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MDunningLevel

}	//	MDunningLevel
