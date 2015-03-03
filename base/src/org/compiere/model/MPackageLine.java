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
 *	Package Line Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MPackageLine.java,v 1.4 2005/03/11 20:25:59 jjanke Exp $
 */
public class MPackageLine extends X_M_PackageLine
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_PackageLine_ID id
	 */
	public MPackageLine (Properties ctx, int M_PackageLine_ID, String trxName)
	{
		super (ctx, M_PackageLine_ID, trxName);
		if (M_PackageLine_ID == 0)
		{
		//	setM_Package_ID (0);
		//	setM_InOutLine_ID (0);
			setQty (Env.ZERO);
		}
	}	//	MPackageLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MPackageLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MPackageLine
	
	/**
	 * 	Parent Constructor
	 *	@param parent header
	 */
	public MPackageLine (MPackage parent)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setM_Package_ID(parent.getM_Package_ID());
	}	//	MPackageLine
	
	/**
	 * 	Set Shipment Line
	 *	@param line line
	 */
	public void setInOutLine (MInOutLine line)
	{
		setM_InOutLine_ID (line.getM_InOutLine_ID());
		setQty (line.getMovementQty());
	}	//	setInOutLine
	
}	//	MPackageLine
