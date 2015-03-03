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
 *	Attachment Note
 *	
 *  @author Jorg Janke
 *  @version $Id: MAttachmentNote.java,v 1.4 2005/03/11 20:28:32 jjanke Exp $
 */
public class MAttachmentNote extends X_AD_AttachmentNote
{
	/** 
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param AD_AttachmentNote_ID id
	 */
	public MAttachmentNote (Properties ctx, int AD_AttachmentNote_ID, String trxName)
	{
		super (ctx, AD_AttachmentNote_ID, trxName);
		/**
		if (AD_AttachmentNote_ID == 0)
		{
			setAD_Attachment_ID (0);
			setAD_User_ID (0);
			setTextMsg (null);
			setTitle (null);
		}
		/**/
	}	//	MAttachmentNote
	
	/** 
	 * 	Load Constructor 
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MAttachmentNote (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MAttachmentNote

	/**
	 * 	Parent Constructor.
	 * 	Sets current user.
	 *	@param attach attachment
	 */
	public MAttachmentNote (MAttachment attach, String Title, String TextMsg)
	{
		this (attach.getCtx(), 0, attach.get_TrxName());
		setClientOrg(attach);
		setAD_Attachment_ID (attach.getAD_Attachment_ID());
		setAD_User_ID(Env.getAD_User_ID(attach.getCtx()));
		setTitle (Title);
		setTextMsg (TextMsg);
	}	//	MAttachmentNote
	
}	//	MAttachmentNote
