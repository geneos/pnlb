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


public class MRefTable extends X_AD_Ref_Table
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Reference_ID id
	 *	@param trxName trx
	 */
	public MRefTable (Properties ctx, int AD_Reference_ID, String trxName)
	{
		super (ctx, AD_Reference_ID, trxName);
		if (AD_Reference_ID == 0)
		{
		//	setAD_Table_ID (0);
		//	setAD_Display (0);
		//	setAD_Key (0);
			setEntityType (ENTITYTYPE_UserMaintained);	// U
			setIsValueDisplayed (false);
		}
	}	//	MRefTable

	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MRefTable (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MRefTable

}	//	MRefTable
