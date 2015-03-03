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
 *	Commission Line Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MCommissionLine.java,v 1.5 2005/03/11 20:26:05 jjanke Exp $
 */
public class MCommissionLine extends X_C_CommissionLine
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_CommissionLine_ID id
	 */
	public MCommissionLine(Properties ctx, int C_CommissionLine_ID, String trxName)
	{
		super(ctx, C_CommissionLine_ID, trxName);
		if (C_CommissionLine_ID == 0)
		{
		//	setC_Commission_ID (0);
			setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM C_CommissionLine WHERE C_Commission_ID=@C_Commission_ID@
			setAmtMultiplier (Env.ZERO);
			setAmtSubtract (Env.ZERO);
			setCommissionOrders (false);
			setIsPositiveOnly (false);
			setQtyMultiplier (Env.ZERO);
			setQtySubtract (Env.ZERO);
		}
	}	//	MCommissionLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MCommissionLine(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MCommissionLine

	
	
}	//	MCommissionLine
