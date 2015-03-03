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
 * 	Request Update Model
 *  @author Jorg Janke
 *  @version $Id: MRequestUpdate.java,v 1.3 2005/07/18 03:47:42 jjanke Exp $
 */
public class MRequestUpdate extends X_R_RequestUpdate
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param R_RequestUpdate_ID id
	 *	@param trxName trx
	 */
	public MRequestUpdate (Properties ctx, int R_RequestUpdate_ID,
		String trxName)
	{
		super (ctx, R_RequestUpdate_ID, trxName);
	}	//	MRequestUpdate

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MRequestUpdate (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MRequestUpdate

	/**
	 * 	Parent Constructor
	 *	@param parent request
	 */
	public MRequestUpdate (MRequest parent)
	{
		super (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setR_Request_ID (parent.getR_Request_ID());
		//
		setStartTime(parent.getStartTime());
		setEndTime(parent.getEndTime());
		setResult(parent.getResult());
		setQtySpent(parent.getQtySpent());
		setQtyInvoiced(parent.getQtyInvoiced());
		setM_ProductSpent_ID(parent.getM_ProductSpent_ID());
		setConfidentialTypeEntry(parent.getConfidentialTypeEntry());
	}	//	MRequestUpdate
	
	/**
	 * 	Do we have new info
	 *	@return true if new info
	 */
	public boolean isNewInfo()
	{
		return getResult() != null;
	}	//	isNewInfo
	
	/**
	 * 	Get Name of creator
	 *	@return name
	 */
	public String getCreatedByName()
	{
		MUser user = MUser.get(getCtx(), getCreatedBy());
		return user.getName();
	}	//	getCreatedByName

	/**
	 * 	Get Confidential Entry Text (for jsp)
	 *	@return text
	 */
	public String getConfidentialEntryText()
	{
		return MRefList.getListName(getCtx(), CONFIDENTIALTYPEENTRY_AD_Reference_ID, getConfidentialTypeEntry());
	}	//	getConfidentialTextEntry

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (getConfidentialTypeEntry() == null)
			setConfidentialTypeEntry(CONFIDENTIALTYPEENTRY_PublicInformation);
		return true;
	}	//	beforeSave
	
}	//	MRequestUpdate
