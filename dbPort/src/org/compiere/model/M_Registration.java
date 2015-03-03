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
 *	System Registration Model
 *	
 *  @author Jorg Janke
 *  @version $Id: M_Registration.java,v 1.5 2005/03/11 20:28:38 jjanke Exp $
 */
public class M_Registration extends X_AD_Registration
{
	/**
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param AD_Registration_ID id
	 */
	public M_Registration (Properties ctx, int AD_Registration_ID, String trxName)
	{
		super (ctx, AD_Registration_ID, trxName);
		setAD_Client_ID(0);
		setAD_Org_ID(0);
		setAD_System_ID(0);
	}	//	M_Registration

	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public M_Registration (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	M_Registration

	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true/false
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		MSystem system = MSystem.get(getCtx());
		if (system.getName().equals("?")
			|| system.getUserName().equals("?"))
		{
			log.saveError("Error", "Define System first");
			return false;
		}
		return true;
	}	//	beforeSave
	
	
}	//	M_Registration
