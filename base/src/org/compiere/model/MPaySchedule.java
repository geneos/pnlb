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
 *	Payment Term Schedule Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MPaySchedule.java,v 1.6 2005/04/27 05:20:58 jjanke Exp $
 */
public class MPaySchedule extends X_C_PaySchedule
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_PaySchedule_ID id
	 */
	public MPaySchedule(Properties ctx, int C_PaySchedule_ID, String trxName)
	{
		super(ctx, C_PaySchedule_ID, trxName);
		if (C_PaySchedule_ID == 0)
		{
		//	setC_PaymentTerm_ID (0);	//	Parent
			setPercentage (Env.ZERO);
			setDiscount (Env.ZERO);
			setDiscountDays (0);
			setGraceDays (0);
			setNetDays (0);
			setIsValid (false);
		}	
	}	//	MPaySchedule

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MPaySchedule(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MPaySchedule

	/**	Parent					*/
	public MPaymentTerm		m_parent = null;
	
	/**
	 * @return Returns the parent.
	 */
	public MPaymentTerm getParent ()
	{
		if (m_parent == null)
			m_parent = new MPaymentTerm (getCtx(), getC_PaymentTerm_ID(), get_TrxName());
		return m_parent;
	}	//	getParent
	
	/**
	 * @param parent The parent to set.
	 */
	public void setParent (MPaymentTerm parent)
	{
		m_parent = parent;
	}	//	setParent
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (is_ValueChanged("Percentage"))
		{
			log.fine("beforeSave");
			setIsValid(false);
		}
		return true;
	}	//	beforeSave

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord || is_ValueChanged("Percentage"))
		{
			log.fine("afterSave");
			getParent();
			m_parent.validate();
			m_parent.save();
		}
		return success;
	}	//	afterSave
	
}	//	MPaySchedule
