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
 *	Inventory Movement Confirmation Line
 *	
 *  @author Jorg Janke
 *  @version $Id: MMovementLineConfirm.java,v 1.7 2005/05/17 05:29:53 jjanke Exp $
 */
public class MMovementLineConfirm extends X_M_MovementLineConfirm
{

	/**
	 * 	Standard Constructor
	 *	@param ctx ctx
	 *	@param M_MovementLineConfirm_ID id
	 */
	public MMovementLineConfirm (Properties ctx, int M_MovementLineConfirm_ID, String trxName)
	{
		super (ctx, M_MovementLineConfirm_ID, trxName);
		if (M_MovementLineConfirm_ID == 0)
		{
		//	setM_MovementConfirm_ID (0);	Parent
		//	setM_MovementLine_ID (0);
			setConfirmedQty (Env.ZERO);
			setDifferenceQty (Env.ZERO);
			setScrappedQty (Env.ZERO);
			setTargetQty (Env.ZERO);
			setProcessed (false);
		}	}	//	M_MovementLineConfirm

	/**
	 * 	M_MovementLineConfirm
	 *	@param ctx
	 *	@param rs
	 */
	public MMovementLineConfirm (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	M_MovementLineConfirm

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 */
	public MMovementLineConfirm (MMovementConfirm parent)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setM_MovementConfirm_ID(parent.getM_MovementConfirm_ID());
	}	//	MMovementLineConfirm

	/**	Movement Line			*/
	private MMovementLine 	m_line = null;
	
	/**
	 * 	Set Movement Line
	 *	@param line line
	 */
	public void setMovementLine (MMovementLine line)
	{
		setM_MovementLine_ID(line.getM_MovementLine_ID());
		setTargetQty(line.getMovementQty());
		setConfirmedQty(getTargetQty());	//	suggestion
		m_line = line;
	}	//	setMovementLine
	
	/**
	 * 	Get Movement Line
	 *	@return line
	 */
	public MMovementLine getLine()
	{
		if (m_line == null)
			m_line = new MMovementLine (getCtx(), getM_MovementLine_ID(), get_TrxName());
		return m_line;
	}	//	getLine
	
	
	/**
	 * 	Process Confirmation Line.
	 * 	- Update Movement Line
	 *	@return success
	 */
	public boolean processLine ()
	{
		MMovementLine line = getLine();
		
		line.setTargetQty(getTargetQty());
		line.setMovementQty(getConfirmedQty());
		line.setConfirmedQty(getConfirmedQty());
		line.setScrappedQty(getScrappedQty());
		
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

	
}	//	M_MovementLineConfirm
