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
package org.compiere.db;

import javax.naming.*;
import org.compiere.interfaces.*;


public class TestEJB
{

	public TestEJB ()
	{
		CConnection cc = CConnection.get(null);
		cc.setAppsHost("dev1");
		InitialContext ic = cc.getInitialContext(false);
		/**/
		try
		{
			System.out.println(ic.getEnvironment());
			System.out.println("----------------");
			NamingEnumeration ne = ic.list("");
			while (ne.hasMore())
			{
				System.out.println(ne.next());
			}
		}
		catch (Exception e)
		{
			System.err.println("..");
			e.printStackTrace();
			System.exit(1);
		}
		/**/
		
		//
		try
		{
			StatusHome statusHome = (StatusHome)ic.lookup ("Status");
			Status status = statusHome.create ();
			//
		}
		catch (CommunicationException ce)	//	not a "real" error
		{
			System.err.println("=ce=");
			ce.printStackTrace();
		}
		catch (Exception e)
		{
			System.err.println("=e=");
			e.printStackTrace();
		}
	}

	/**
	 * 	main
	 *	@param args
	 */
	public static void main (String[] args)
	{
		new TestEJB();
	}
}
