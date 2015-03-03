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
package org.compiere.process;

import org.compiere.model.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Reset Password
 *	
 *  @author Jorg Janke
 *  @version $Id: UserPassword.java,v 1.9 2005/10/28 22:30:38 jjanke Exp $
 */
public class UserPassword extends SvrProcess
{
	private int			p_AD_User_ID = -1;
	private String 		p_OldPassword = null;
	private String 		p_NewPassword = null;
	private String		p_NewEMail = null;
	private String		p_NewEMailUser = null;
	private String		p_NewEMailUserPW = null;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("AD_User_ID"))
				p_AD_User_ID = para[i].getParameterAsInt();
			else if (name.equals("OldPassword"))
				p_OldPassword = (String)para[i].getParameter();
			else if (name.equals("NewPassword"))
				p_NewPassword = (String)para[i].getParameter();
			else if (name.equals("NewEMail"))
				p_NewEMail = (String)para[i].getParameter();
			else if (name.equals("NewEMailUser"))
				p_NewEMailUser = (String)para[i].getParameter();
			else if (name.equals("NewEMailUserPW"))
				p_NewEMailUserPW = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		log.info ("AD_User_ID=" + p_AD_User_ID + " from " + getAD_User_ID());
		
		MUser user = MUser.get(getCtx(), p_AD_User_ID);
		MUser operator = MUser.get(getCtx(), getAD_User_ID());
		log.fine("User=" + user + ", Operator=" + operator);
		
		
		
		//	Do we need a password ?
		if (Util.isEmpty(p_OldPassword))		//	Password required
		{
			if (p_AD_User_ID == 0			//	change of System
				|| p_AD_User_ID == 100		//	change of SuperUser
				|| !operator.isAdministrator())
				throw new IllegalArgumentException("@OldPasswordMandatory@");
		}

		//	is entered Password correct ?
		else if (!p_OldPassword.equals(user.getPassword()))
			throw new IllegalArgumentException("@OldPasswordNoMatch@");
		
		//	Change Super User
		if (p_AD_User_ID == 0)
		{
			String sql = "UPDATE AD_User SET Updated=SysDate, UpdatedBy=" + getAD_User_ID();
			if (!Util.isEmpty(p_NewPassword))
				sql += ", Password=" + DB.TO_STRING(p_NewPassword);
			if (!Util.isEmpty(p_NewEMail))
				sql += ", Email=" + DB.TO_STRING(p_NewEMail);
			if (!Util.isEmpty(p_NewEMailUser))
				sql += ", EmailUser=" + DB.TO_STRING(p_NewEMailUser);
			if (!Util.isEmpty(p_NewEMailUserPW))
				sql += ", EmailUserPW=" + DB.TO_STRING(p_NewEMailUserPW);
			sql += " WHERE AD_User_ID=0";
			if (DB.executeUpdate(sql, get_TrxName()) == 1)
				return "OK";
			else 
				return "@Error@";
		}
		else
		{
			if (!Util.isEmpty(p_NewPassword))
				user.setPassword(p_NewPassword);
			if (!Util.isEmpty(p_NewEMail))
				user.setEMail(p_NewEMail);
			if (!Util.isEmpty(p_NewEMailUser))
				user.setEMailUser(p_NewEMailUser);
			if (!Util.isEmpty(p_NewEMailUserPW))
				user.setEMailUserPW(p_NewEMailUserPW);
			//
			if (user.save())
				return "OK";
			else 
				return "@Error@";
		}
	}	//	doIt

}	//	UserPassword

