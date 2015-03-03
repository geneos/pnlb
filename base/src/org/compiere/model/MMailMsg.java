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

/**
 * 	Wab Store Mail Message Model
 *  @author Jorg Janke
 *  @version $Id: MMailMsg.java,v 1.3 2005/11/13 22:21:21 jjanke Exp $
 */
public class MMailMsg extends X_W_MailMsg
{
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param W_MailMsg_ID id
	 *	@param trxName trx
	 */
	public MMailMsg (Properties ctx, int W_MailMsg_ID, String trxName)
	{
		super (ctx, W_MailMsg_ID, trxName);
		if (W_MailMsg_ID == 0)
		{
		//	setW_Store_ID (0);
		//	setMailMsgType (null);
		//	setName (null);
		//	setSubject (null);
		//	setMessage (null);
		}
	}	//	MMailMsg

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MMailMsg (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MMailMsg
	
	/**
	 * 	Full Constructor
	 *	@param parent store
	 *	@param MailMsgType msg type
	 *	@param Name name
	 *	@param Subject subject
	 *	@param Message message
	 */
	public MMailMsg (MStore parent, String MailMsgType, 
		String Name, String Subject, String Message, String Message2, String Message3)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());;
		setClientOrg(parent);
		setW_Store_ID(parent.getW_Store_ID());
		setMailMsgType (MailMsgType);
		setName (Name);
		setSubject (Subject);
		setMessage (Message);
		setMessage2 (Message2);
		setMessage3 (Message3);
	}	//	MMailMsg
	
}	//	MMailMsg
