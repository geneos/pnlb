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
 * 	Change Request Model
 *  @author Jorg Janke
 *  @version $Id: MChangeRequest.java,v 1.1 2005/05/21 04:47:16 jjanke Exp $
 */
public class MChangeRequest extends X_M_ChangeRequest
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_ChangeRequest_ID ix
	 *	@param trxName trx
	 */
	public MChangeRequest (Properties ctx, int M_ChangeRequest_ID, String trxName)
	{
		super (ctx, M_ChangeRequest_ID, trxName);
		if (M_ChangeRequest_ID == 0)
		{
		//	setName (null);
			setIsApproved(false);
			setProcessed(false);
		}
	}	//	MChangeRequest

	/**
	 * 	CRM Request Constructor
	 *	@param request request
	 *	@param group request group
	 */
	public MChangeRequest (MRequest request, MGroup group)
	{
		this (request.getCtx(), 0, request.get_TrxName());
		setClientOrg(request);
		setName(Msg.getElement(getCtx(), "R_Request_ID") + ": " + request.getDocumentNo());
		setHelp(request.getSummary());
		//
		setM_BOM_ID(group.getM_BOM_ID());
		setM_ChangeNotice_ID(group.getM_ChangeNotice_ID());
	}	//	MChangeRequest
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MChangeRequest (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MChangeRequest

	/**
	 * 	Get CRM Requests of Change Requests
	 *	@return requests
	 */
	public MRequest[] getRequests()
	{
		String sql = "SELECT * FROM R_Request WHERE M_ChangeRequest_ID=?";
		return null;
	}	//	getRequests
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true/false
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Have at least one
		if (getM_BOM_ID() == 0 && getM_ChangeNotice_ID() == 0)
		{
			log.saveError("Error", Msg.parseTranslation(getCtx(), "@NotFound@: @M_BOM_ID@ / @M_ChangeNotice_ID@"));
			return false;
		}
		
		//	Derive ChangeNotice from BOM if defined
		if (newRecord && getM_BOM_ID() != 0 && getM_ChangeNotice_ID() == 0)
		{
			MBOM bom = new MBOM (getCtx(), getM_BOM_ID(), get_TrxName());
			if (bom.getM_ChangeNotice_ID() != 0)
				setM_BOM_ID(bom.getM_ChangeNotice_ID());
		}
		
		return true;
	}	//	beforeSave
	
}	//	MChangeRequest
