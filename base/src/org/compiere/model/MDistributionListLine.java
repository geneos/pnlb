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
 *	Distribution List Line
 *	
 *  @author Jorg Janke
 *  @version $Id: MDistributionListLine.java,v 1.4 2005/03/11 20:26:00 jjanke Exp $
 */
public class MDistributionListLine extends X_M_DistributionListLine
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_DistributionListLine_ID id
	 */
	public MDistributionListLine (Properties ctx, int M_DistributionListLine_ID, String trxName)
	{
		super (ctx, M_DistributionListLine_ID, trxName);
	}	//	MDistributionListLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MDistributionListLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MDistributionListLine
	
	
	/**
	 * 	Get Min Qty
	 *	@return min Qty or 0
	 */
	public BigDecimal getMinQty ()
	{
		BigDecimal minQty = super.getMinQty ();
		if (minQty == null)
			return Env.ZERO;
		return minQty;
	}	//	getMinQty
	
	
	/**
	 * 	Get Ratio
	 *	@return ratio or 0
	 */
	public BigDecimal getRatio ()
	{
		BigDecimal ratio = super.getRatio();
		if (ratio == null)
			return Env.ZERO;
		return ratio;
	}	//	getRatio
	
}	//	MDistributionListLine
