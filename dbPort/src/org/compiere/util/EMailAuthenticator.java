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
package org.compiere.util;

import java.util.logging.*;
import javax.mail.*;

/**
 *  Email User Authentification
 *
 *  @author Jorg Janke
 *  @version $Id: EMailAuthenticator.java,v 1.1 2005/07/18 03:50:19 jjanke Exp $
 */
public class EMailAuthenticator extends Authenticator
{
	/**
	 * 	Constructor
	 * 	@param username user name
	 * 	@param password user password
	 */
	public EMailAuthenticator (String username, String password)
	{
		m_pass = new PasswordAuthentication (username, password);
		if (username == null || username.length() == 0)
		{
			log.log(Level.SEVERE, "Username is NULL");
			Thread.dumpStack();
		}
		if (password == null || password.length() == 0)
		{
			log.log(Level.SEVERE, "Password is NULL");
			Thread.dumpStack();
		}
	}	//	EMailAuthenticator

	/**	Password		*/
	private PasswordAuthentication 	m_pass = null;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(EMailAuthenticator.class);

	/**
	 *	Ger PasswordAuthentication
	 * 	@return Password Autnetifucation
	 */
	protected PasswordAuthentication getPasswordAuthentication()
	{
		return m_pass;
	}	//	getPasswordAuthentication

	/**
	 * 	Get String representation
	 * 	@return info
	 */
	public String toString()
	{
		if (m_pass == null)
			return "EMailAuthenticator[]";
		if (CLogMgt.isLevelFinest())
			return "EMailAuthenticator[" 
				+ m_pass.getUserName() + "/" + m_pass.getPassword() + "]";
		return "EMailAuthenticator[" 
			+ m_pass.getUserName() + "/************]";
	}	//	toString

}	//	EMailAuthenticator
