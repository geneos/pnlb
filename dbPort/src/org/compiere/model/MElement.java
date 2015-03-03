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
 *	Accounting Element Model.
 *	
 *  @author Jorg Janke
 *  @version $Id: MElement.java,v 1.7 2005/11/25 21:58:30 jjanke Exp $
 */
public class MElement extends X_C_Element
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Element_ID id
	 */
	public MElement (Properties ctx, int C_Element_ID, String trxName)
	{
		super(ctx, C_Element_ID, trxName);
		if (C_Element_ID == 0)
		{
		//	setName (null);
		//	setAD_Tree_ID (0);
		//	setElementType (null);	// A
			setIsBalancing (false);
			setIsNaturalAccount (false);
		}
	}	//	MElement

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MElement (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MElement

	/**
	 * 	Full Constructor
	 *	@param client client
	 *	@param Name name
	 *	@param ElementType type
	 *	@param AD_Tree_ID tree
	 */
	public MElement (MClient client, String Name, String ElementType, int AD_Tree_ID)
	{
		this (client.getCtx(), 0, client.get_TrxName());
		setClientOrg(client);
		setName (Name);
		setElementType (ElementType);	// A
		setAD_Tree_ID (AD_Tree_ID);
		setIsNaturalAccount(ELEMENTTYPE_Account.equals(ElementType));
	}	//	MElement

	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (getAD_Org_ID() != 0)
			setAD_Org_ID(0);
		//
		if (ELEMENTTYPE_UserDefined.equals(getElementType()) && isNaturalAccount())
			setIsNaturalAccount(false);
		return true;
	}	//	beforeSave
	
}	//	MElement
