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
 *	RMA Line Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRMALine.java,v 1.6 2005/03/11 20:26:03 jjanke Exp $
 */
public class MRMALine extends X_M_RMALine
{

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_RMALine_ID id
	 */
	public MRMALine (Properties ctx, int M_RMALine_ID, String trxName)
	{
		super (ctx, M_RMALine_ID, trxName);
		if (M_RMALine_ID == 0)
		{
			setQty(Env.ONE);
		}
	}	//	MRMALine

	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRMALine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRMALine
	
	/**	Shipment Line			*/
	private MInOutLine m_ioLine = null;
	
	
	/**
	 * 	Set M_InOutLine_ID
	 *	@param M_InOutLine_ID
	 */
	public void setM_InOutLine_ID (int M_InOutLine_ID)
	{
		super.setM_InOutLine_ID (M_InOutLine_ID);
		m_ioLine = null;
	}	//	setM_InOutLine_ID
	
	/**
	 * 	Get Ship Line
	 *	@return ship line
	 */
	public MInOutLine getShipLine()
	{
		if (m_ioLine == null && getM_InOutLine_ID() != 0)
			m_ioLine = new MInOutLine (getCtx(), getM_InOutLine_ID(), get_TrxName());
		return m_ioLine;
	}	//	getShipLine
	
	/**
	 * 	Get Total Amt
	 *	@return amt
	 */
	public BigDecimal getAmt()
	{
		BigDecimal amt = Env.ZERO;
		getShipLine();
		if (m_ioLine != null)
		{
			if (m_ioLine.getC_OrderLine_ID() != 0)
			{
				MOrderLine ol = new MOrderLine (getCtx(), m_ioLine.getC_OrderLine_ID(), get_TrxName());
				amt = ol.getPriceActual();
			}
		}
		//
		return amt.multiply(getQty());
	}	//	getAmt
	
	
}	//	MRMALine
