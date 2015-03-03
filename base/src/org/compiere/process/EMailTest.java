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

import java.io.*;

import org.compiere.util.*;
import org.compiere.model.*;

/**
 *	Client EMail Test
 *	
 *  @author Jorg Janke
 *  @version $Id: EMailTest.java,v 1.1 2005/07/18 03:47:43 jjanke Exp $
 */
public class EMailTest extends SvrProcess
{
	/** Client Parameter			*/
	protected int	p_AD_Client_ID = 0;
	
	/**
	 * 	Get Parameters
	 */
	protected void prepare ()
	{
		p_AD_Client_ID = getRecord_ID();
		if (p_AD_Client_ID == 0)
			p_AD_Client_ID = Env.getAD_Client_ID(getCtx());
	}	//	prepare

	/**
	 * 	Process - Test EMail
	 *	@return info
	 */
	protected String doIt () throws Exception
	{
		MClient client = MClient.get (getCtx(), p_AD_Client_ID);
		log.info(client.toString());
		
		//	 Test Client Mail
		String clientTest = client.testEMail();
		addLog(0, null, null, client.getName() + ": " + clientTest);
		
		//	Test Client DocumentDir
		if (!Ini.isClient())
		{
			String documentDir = client.getDocumentDir();
			if (documentDir == null || documentDir.length() == 0)
				documentDir = ".";
			File file = new File (documentDir);
			if (file.exists() && file.isDirectory())
				addLog(0, null, null, "Found Directory: " + client.getDocumentDir());
			else
				addLog(0, null, null, "Not Found Directory: " + client.getDocumentDir());
		}

		MStore[] wstores = MStore.getOfClient(client);
		for (int i = 0; i < wstores.length; i++)
		{
			MStore store = wstores[i];
			String test = store.testEMail();
			addLog(0, null, null, store.getName() + ": " + test);
		}
		
		return clientTest;
	}	//	doIt
	
}	//	EMailTest
