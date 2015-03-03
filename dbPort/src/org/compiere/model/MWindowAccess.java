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
 *	
 *	
 *  @author Jorg Janke
 *  @version $Id: MWindowAccess.java,v 1.4 2005/03/11 20:28:32 jjanke Exp $
 */
public class MWindowAccess extends X_AD_Window_Access
{

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param ignored -
	 */
	public MWindowAccess (Properties ctx, int ignored, String trxName)
	{
		super(ctx, 0, trxName);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
		else
		{
		//	setAD_Role_ID (0);
		//	setAD_Window_ID (0);
			setIsReadWrite (true);
		}
	}	//	MWindowAccess

	/**
	 * 	MWindowAccess
	 *	@param ctx
	 *	@param rs
	 */
	public MWindowAccess (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MWindowAccess

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param AD_Role_ID role id
	 */
	public MWindowAccess (M_Window parent, int AD_Role_ID)
	{
		super (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setAD_Window_ID(parent.getAD_Window_ID());
		setAD_Role_ID (AD_Role_ID);
	}	//	MWindowAccess

}	//	MWindowAccess
