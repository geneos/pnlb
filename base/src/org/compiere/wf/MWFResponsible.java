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
package org.compiere.wf;

import java.sql.*;
import java.util.*;
import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Workflow Resoinsible
 *	
 *  @author Jorg Janke
 *  @version $Id: MWFResponsible.java,v 1.10 2005/11/12 22:59:13 jjanke Exp $
 */
public class MWFResponsible extends X_AD_WF_Responsible
{
	/**
	 * 	Get WF Responsible from Cache
	 *	@param ctx context
	 *	@param AD_WF_Responsible_ID id
	 *	@return MWFResponsible
	 */
	public static MWFResponsible get (Properties ctx, int AD_WF_Responsible_ID)
	{
		Integer key = new Integer (AD_WF_Responsible_ID);
		MWFResponsible retValue = (MWFResponsible) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MWFResponsible (ctx, AD_WF_Responsible_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer,MWFResponsible>	s_cache	= new CCache<Integer,MWFResponsible>("AD_WF_Responsible", 10);

	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_WF_Responsible_ID id
	 */
	public MWFResponsible (Properties ctx, int AD_WF_Responsible_ID, String trxName)
	{
		super (ctx, AD_WF_Responsible_ID, trxName);
	}	//	MWFResponsible

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MWFResponsible (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MWFResponsible
	
	/**
	 * 	Invoker - return true if no user and no role 
	 *	@return true if invoker
	 */
	public boolean isInvoker()
	{
		return getAD_User_ID() == 0 && getAD_Role_ID() == 0;
	}	//	isInvoker
	
	/**
	 * 	Is Role Responsible
	 *	@return true if role
	 */
	public boolean isRole()
	{
		return RESPONSIBLETYPE_Role.equals(getResponsibleType()) 
			&& getAD_Role_ID() != 0;
	}	//	isRole

	/**
	 * 	Is Role Responsible
	 *	@return true if role
	 */
	public MRole getRole()
	{
		if (!isRole())
			return null;
		return MRole.get(getCtx(), getAD_Role_ID());
	}	//	getRole

	/**
	 * 	Is Human Responsible
	 *	@return true if human
	 */
	public boolean isHuman()
	{
		return RESPONSIBLETYPE_Human.equals(getResponsibleType()) 
			&& getAD_User_ID() != 0;
	}	//	isHuman
	
	/**
	 * 	Is Org Responsible
	 *	@return true if Org
	 */
	public boolean isOrganization()
	{
		return RESPONSIBLETYPE_Organization.equals(getResponsibleType()) 
			&& getAD_Org_ID() != 0;
	}	//	isOrg
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return tre if can be saved
	 */
	protected boolean beforeSave (boolean newRecord)
	{
	//	if (RESPONSIBLETYPE_Human.equals(getResponsibleType()) && getAD_User_ID() == 0)
	//		return true;
		if (RESPONSIBLETYPE_Role.equals(getResponsibleType()) 
			&& getAD_Role_ID() == 0
			&& getAD_Client_ID() > 0)
		{
			log.saveError("Error", Msg.parseTranslation(getCtx(), "@RequiredEnter@ @AD_Role_ID@"));
			return false;
		}
		//	User not used
		if (!RESPONSIBLETYPE_Human.equals(getResponsibleType()) && getAD_User_ID() == 0)
			setAD_User_ID(0);
		//	Role not used
		if (!RESPONSIBLETYPE_Role.equals(getResponsibleType()) && getAD_Role_ID() == 0)
			setAD_Role_ID(0);
		return true;
	}	//	beforeSave
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer("MWFResponsible[");
		sb.append (get_ID())
			.append("-").append(getName())
			.append(",Type=").append(getResponsibleType());
		if (getAD_User_ID() != 0)
			sb.append(",AD_User_ID=").append(getAD_User_ID());
		if (getAD_Role_ID() != 0)
			sb.append(",AD_Role_ID=").append(getAD_Role_ID());
		sb.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MWFResponsible
