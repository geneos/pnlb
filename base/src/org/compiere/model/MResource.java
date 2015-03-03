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
 *	Resource Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MResource.java,v 1.6 2005/05/17 05:29:53 jjanke Exp $
 */
public class MResource extends X_S_Resource
{

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param S_Resource_ID id
	 */
	public MResource (Properties ctx, int S_Resource_ID, String trxName)
	{
		super (ctx, S_Resource_ID, trxName);
	}	//	MResource

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MResource (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MResource
	
	
	/** Cached Resource Type	*/
	private MResourceType	m_resourceType = null;
	/** Cached Product			*/
	private MProduct		m_product = null;
	
	
	/**
	 * 	Get cached Resource Type
	 *	@return Resource Type
	 */
	public MResourceType getResourceType()
	{
		if (m_resourceType == null && getS_ResourceType_ID() != 0)
			m_resourceType = new MResourceType (getCtx(), getS_ResourceType_ID(), get_TrxName());
		return m_resourceType;
	}	//	getResourceType
	
	/**
	 * 	Get Product
	 *	@return product
	 */
	public MProduct getProduct()
	{
		if (m_product == null)
		{
			MProduct[] products = MProduct.get(getCtx(), "S_Resource_ID=" + getS_Resource_ID(), get_TrxName());
			if (products.length > 0)
				m_product = products[0];
		}
		return m_product;
	}	//	getProduct
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true if it can be saved
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (newRecord)
		{
			if (getValue() == null || getValue().length() == 0)
				setValue(getName());
			m_product = new MProduct(this, getResourceType());
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
		if (prod.setResource(this))
			prod.save(get_TrxName());
		
		return success;
	}	//	afterSave
	
}	//	MResource
