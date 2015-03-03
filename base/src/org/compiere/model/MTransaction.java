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

import java.math.*;
import java.sql.*;
import java.util.*;
import org.compiere.util.*;

/**
 * 	Material Transaction Model
 *
 *	@author Jorg Janke
 *	@version $Id: MTransaction.java,v 1.9 2005/09/19 04:49:46 jjanke Exp $
 */
public class MTransaction extends X_M_Transaction
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_Transaction_ID id
	 */
	public MTransaction (Properties ctx, int M_Transaction_ID, String trxName)
	{
		super (ctx, M_Transaction_ID, trxName);
		if (M_Transaction_ID == 0)
		{
		//	setM_Transaction_ID (0);		//	PK
		//	setM_Locator_ID (0);
		//	setM_Product_ID (0);
			setMovementDate (new Timestamp(System.currentTimeMillis()));
			setMovementQty (Env.ZERO);
		//	setMovementType (MOVEMENTTYPE_CustomerShipment);
		}
	}	//	MTransaction

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MTransaction (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MTransaction

	/**
	 * 	Detail Constructor
	 *	@param ctx context
	 * 	@param MovementType movement type
	 * 	@param M_Locator_ID locator
	 * 	@param M_Product_ID product
	 * 	@param M_AttributeSetInstance_ID attribute
	 * 	@param MovementQty qty
	 * 	@param MovementDate optional date
	 */
	public MTransaction (Properties ctx, String MovementType, 
		int M_Locator_ID, int M_Product_ID, int M_AttributeSetInstance_ID, 
		BigDecimal MovementQty, Timestamp MovementDate, String trxName)
	{
		super(ctx, 0, trxName);
		setMovementType (MovementType);
		if (M_Locator_ID == 0)
			throw new IllegalArgumentException("No Locator");
		setM_Locator_ID (M_Locator_ID);
		if (M_Product_ID == 0)
			throw new IllegalArgumentException("No Product");
		setM_Product_ID (M_Product_ID);
		setM_AttributeSetInstance_ID (M_AttributeSetInstance_ID);
		//
		if (MovementQty != null)		//	Can be 0
			setMovementQty (MovementQty);
		if (MovementDate == null)
			setMovementDate (new Timestamp(System.currentTimeMillis()));
		else
			setMovementDate(MovementDate);
	}	//	MTransaction

	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MTransaction[");
		sb.append(get_ID()).append(",").append(getMovementType())
			.append(",Qty=").append(getMovementQty())
			.append(",M_Product_ID=").append(getM_Product_ID())
			.append(",ASI=").append(getM_AttributeSetInstance_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MTransaction
