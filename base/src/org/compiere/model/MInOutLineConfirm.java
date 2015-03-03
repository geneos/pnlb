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
 *	Ship Confirmation Line Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MInOutLineConfirm.java,v 1.17 2005/05/28 21:03:01 jjanke Exp $
 */
public class MInOutLineConfirm extends X_M_InOutLineConfirm
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_InOutLineConfirm_ID id
	 *	@param trxName transaction
	 */
	public MInOutLineConfirm (Properties ctx, int M_InOutLineConfirm_ID, String trxName)
	{
		super (ctx, M_InOutLineConfirm_ID, trxName);
		if (M_InOutLineConfirm_ID == 0)
		{
		//	setM_InOutConfirm_ID (0);
		//	setM_InOutLine_ID (0);
		//	setTargetQty (Env.ZERO);
		//	setConfirmedQty (Env.ZERO);
			setDifferenceQty(Env.ZERO);
			setScrappedQty(Env.ZERO);
			setProcessed (false);
		}
	}	//	MInOutLineConfirm

	/**
	 * 	Load Construvtor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MInOutLineConfirm (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MInOutLineConfirm
	
	/**
	 * 	Parent Construvtor
	 *	@param header parent
	 */
	public MInOutLineConfirm (MInOutConfirm header)
	{
		this (header.getCtx(), 0, header.get_TrxName());
		setClientOrg(header);
		setM_InOutConfirm_ID(header.getM_InOutConfirm_ID());
	}	//	MInOutLineConfirm
	
	/** Ship Line				*/
	private MInOutLine 	m_line = null;
	
	/**
	 * 	Set Shipment Line
	 *	@param line shipment line
	 */
	public void setInOutLine (MInOutLine line)
	{
		setM_InOutLine_ID(line.getM_InOutLine_ID());
		setTargetQty(line.getMovementQty());	//	Confirmations in Storage UOM	
		setConfirmedQty (getTargetQty());		//	suggestion
		m_line = line;
	}	//	setInOutLine

	/**
	 * 	Get Shipment Line
	 *	@return line
	 */
	public MInOutLine getLine()
	{
		if (m_line == null)
			m_line = new MInOutLine (getCtx(), getM_InOutLine_ID(), get_TrxName());
		return m_line;
	}	//	getLine
	
	
	/**
	 * 	Process Confirmation Line.
	 * 	- Update InOut Line
	 *	@return success
	 */
	public boolean processLine (boolean isSOTrx, String confirmType)
	{
		MInOutLine line = getLine();
		
		//	Customer
		if (MInOutConfirm.CONFIRMTYPE_CustomerConfirmation.equals(confirmType))
		{
			line.setConfirmedQty(getConfirmedQty());
		}
		
		//	Drop Ship
		else if (MInOutConfirm.CONFIRMTYPE_DropShipConfirm.equals(confirmType))
		{
			
		}
		
		//	Pick or QA
		else if (MInOutConfirm.CONFIRMTYPE_PickQAConfirm.equals(confirmType))
		{
			line.setTargetQty(getTargetQty());
			line.setMovementQty(getConfirmedQty());	//	Entered NOT changed
			line.setPickedQty(getConfirmedQty());
			//
			line.setScrappedQty(getScrappedQty());
		}
		
		//	Ship or Receipt
		else if (MInOutConfirm.CONFIRMTYPE_ShipReceiptConfirm.equals(confirmType))
		{
			line.setTargetQty(getTargetQty());
			BigDecimal qty = getConfirmedQty();
			if (!isSOTrx)	//	In PO, we have the responsibility for scapped
				qty = qty.add(getScrappedQty());
			line.setMovementQty(qty);				//	Entered NOT changed
			//
			line.setScrappedQty(getScrappedQty());
		}
		//	Vendor
		else if (MInOutConfirm.CONFIRMTYPE_VendorConfirmation.equals(confirmType))
		{
			line.setConfirmedQty(getConfirmedQty());
		}
		
		return line.save(get_TrxName());
	}	//	processConfirmation
	
	/**
	 * 	Is Fully Confirmed
	 *	@return true if Target = Confirmed qty
	 */
	public boolean isFullyConfirmed()
	{
		return getTargetQty().compareTo(getConfirmedQty()) == 0;
	}	//	isFullyConfirmed
	
	
	/**
	 * 	Before Delete - do not delete
	 *	@return false 
	 */
	protected boolean beforeDelete ()
	{
		log.saveError("Error", Msg.getMsg(getCtx(), "CannotDelete"));
		return false;
	}	//	beforeDelete
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Calculate Difference = Target - Confirmed - Scrapped
		BigDecimal difference = getTargetQty();
		difference = difference.subtract(getConfirmedQty());
		difference = difference.subtract(getScrappedQty());
		setDifferenceQty(difference);
		//
		return true;
	}	//	beforeSave
	
}	//	MInOutLineConfirm
