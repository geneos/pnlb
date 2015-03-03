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
 *	Expense Type Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MExpenseType.java,v 1.6 2005/05/17 05:29:53 jjanke Exp $
 */
public class MExpenseType extends X_S_ExpenseType
{

	/**
	 * 	Default Constructor
	 *	@param ctx
	 *	@param S_ExpenseType_ID
	 */
	public MExpenseType (Properties ctx, int S_ExpenseType_ID, String trxName)
	{
		super (ctx, S_ExpenseType_ID, trxName);
	}	//	MExpenseType

	/**
	 * 	MExpenseType
	 *	@param ctx
	 *	@param rs
	 */
	public MExpenseType (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MExpenseType
	
	/** Cached Product			*/
	private MProduct	m_product = null;
	
	/**
	 * 	Get Product
	 *	@return product
	 */
	public MProduct getProduct()
	{
		if (m_product == null)
		{
			MProduct[] products = MProduct.get(getCtx(), "S_ExpenseType_ID=" + getS_ExpenseType_ID(), get_TrxName());
			if (products.length > 0)
				m_product = products[0];
		}
		return m_product;
	}	//	getProduct
	
	
	/**
	 * 	beforeSave
	 *	@see org.compiere.model.PO#beforeSave(boolean)
	 *	@param newRecord
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (newRecord)
		{
			if (getValue() == null || getValue().length() == 0)
				setValue(getName());
			m_product = new MProduct(this);
			return m_product.save(get_TrxName());
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
		if (!success)
			return success;
				
		MProduct prod = getProduct();
		if (prod.setExpenseType(this))
			prod.save(get_TrxName());
		
		return success;
	}	//	afterSave
	
	
}	//	MExpenseType
