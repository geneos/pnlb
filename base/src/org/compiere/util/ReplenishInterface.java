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
package org.compiere.util;

import java.math.*;
import org.compiere.model.*;

/**
 * 	Custom Replenishment Interface
 *	
 *  @author Jorg Janke
 *  @version $Id: ReplenishInterface.java,v 1.1 2005/07/23 04:53:36 jjanke Exp $
 */
public interface ReplenishInterface
{

	/**
	 * 	Return the Qty To Order
	 *	@param wh warehouse
	 *	@param replenish temporary replenishment
	 *	@return qty to order
	 */
	public BigDecimal getQtyToOrder (MWarehouse wh, X_T_Replenish replenish);
	
}	//	ReplenishmentInterface
