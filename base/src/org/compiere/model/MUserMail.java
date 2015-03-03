/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.model;

import java.sql.*;
import java.util.*;

import org.compiere.util.*;

/**
 * 	User Mail Model
 *  @author Jorg Janke
 *  @version $Id: MUserMail.java,v 1.4 2005/09/24 01:52:52 jjanke Exp $
 */
public class MUserMail extends X_AD_UserMail
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_UserMail_ID id
	 *	@param trxName trx
	 */
	public MUserMail (Properties ctx, int AD_UserMail_ID, String trxName)
	{
		super (ctx, AD_UserMail_ID, trxName);
	}	//	MUserMail

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MUserMail (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MUserMail
	
	/**
	 * 	User Mail
	 *	@param parent Request Mail Text
	 *	@param AD_User_ID recipient user
	 *	@param mail email
	 */
	public MUserMail (MMailText parent, int AD_User_ID, EMail mail)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setAD_User_ID(AD_User_ID);
		setR_MailText_ID(parent.getR_MailText_ID());
		//
		if (mail.isSentOK())
			setMessageID(mail.getMessageID());
		else
		{
			setMessageID(mail.getSentMsg());
			setIsDelivered(ISDELIVERED_No);
		}
	}	//	MUserMail
	
	/**
	 * 	Parent Constructor
	 *	@param parent Mail message
	 *	@param AD_User_ID recipient user
	 *	@param mail email
	 */
	public MUserMail (MMailMsg parent, int AD_User_ID, EMail mail)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setAD_User_ID(AD_User_ID);
		setW_MailMsg_ID(parent.getW_MailMsg_ID());
		//
		if (mail.isSentOK())
			setMessageID(mail.getMessageID());
		else
		{
			setMessageID(mail.getSentMsg());
			setIsDelivered(ISDELIVERED_No);
		}
	}	//	MUserMail

	/**
	 * 	New User Mail (no trx)
	 *	@param po persistent object
	 *	@param AD_User_ID recipient user
	 *	@param mail email
	 */
	public MUserMail (PO po, int AD_User_ID, EMail mail)
	{
		this (po.getCtx(), 0, null);
		setClientOrg(po);
		setAD_User_ID(AD_User_ID);
		setSubject(mail.getSubject());
		setMailText(mail.getMessageCRLF());
		//
		if (mail.isSentOK())
			setMessageID(mail.getMessageID());
		else
		{
			setMessageID(mail.getSentMsg());
			setIsDelivered(ISDELIVERED_No);
		}
	}	//	MUserMail
	
	
	/**
	 * 	Is it Delivered
	 *	@return true if yes
	 */
	public boolean isDelivered()
	{
		String s = getIsDelivered();
		return s != null 
			&& ISDELIVERED_Yes.equals(s);
	}	//	isDelivered

	/**
	 * 	Is it not Delivered
	 *	@return true if null or no
	 */
	public boolean isDeliveredNo()
	{
		String s = getIsDelivered();
		return s == null 
			|| ISDELIVERED_No.equals(s);
	}	//	isDelivered

	/**
	 * 	Is Delivered unknown
	 *	@return true if null
	 */
	public boolean isDeliveredUnknown()
	{
		String s = getIsDelivered();
		return s == null;
	}	//	isDeliveredUnknown

}	//	MUserMail
