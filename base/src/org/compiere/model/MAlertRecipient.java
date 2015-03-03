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
 *	Alert Recipient
 *	
 *  @author Jorg Janke
 *  @version $Id: MAlertRecipient.java,v 1.6 2005/09/19 04:49:47 jjanke Exp $
 */
public class MAlertRecipient extends X_AD_AlertRecipient
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_AlertRecipient_ID id
	 */
	public MAlertRecipient (Properties ctx, int AD_AlertRecipient_ID, String trxName)
	{
		super (ctx, AD_AlertRecipient_ID, trxName);
	}	//	MAlertRecipient

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MAlertRecipient (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MAlertRecipient

	
	
	/**
	 * 	Get User
	 *	@return	AD_User_ID or -1 if none
	 */
	public int getAD_User_ID ()
	{
		Integer ii = (Integer)get_Value("AD_User_ID");
		if (ii == null) 
			return -1;
		return ii.intValue();
	}	//	getAD_User_ID
	
	
	/**
	 * 	Get Role
	 *	@return AD_Role_ID or -1 if none
	 */
	public int getAD_Role_ID ()
	{
		Integer ii = (Integer)get_Value("AD_Role_ID");
		if (ii == null) 
			return -1;
		return ii.intValue();
	}	//	getAD_Role_ID
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MAlertRecipient[");
		sb.append(get_ID())
			.append(",AD_User_ID=").append(getAD_User_ID())
			.append(",AD_Role_ID=").append(getAD_Role_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MAlertRecipient
