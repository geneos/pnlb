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
package org.compiere.tools;

import java.rmi.*;
import java.rmi.registry.*;
//import java.rmi.server.*;
import java.net.*;
import java.io.*;

/**
 *  Test/List RMI Registry
 *
 *  @author Jorg Janke
 *  @version $Id: RMIUtil.java,v 1.3 2005/03/11 20:26:10 jjanke Exp $
 */
public class RMIUtil
{
	public RMIUtil()
	{
	//	testPort();
		try
		{
			System.out.println("Registry ------------------------------------");
			Registry registry = LocateRegistry.getRegistry();
			System.out.println("- " + registry);
			String[] list = registry.list();
			System.out.println("- size=" + list.length);
			for (int i = 0; i < list.length; i++)
			{
				System.out.println("-- " + list[i]);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		try
		{
			System.out.println("Server --------------------------------------");
		//	System.out.println("- " + RemoteServer.getClientHost());
			String[] list = Naming.list ("rmi://localhost:1099");
			System.out.println("- size=" + list.length);
			for (int i = 0; i < list.length; i++)
			{
				System.out.println("-- " + list[i]);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}	//	RMIUtil

	private void testPort()
	{
		try
		{
			System.out.println("Test Port -----------------------------------");
			Socket socket = new Socket ("localhost", 1099);
			System.out.println("- Socket=" + socket);
			//
			InputStream in = socket.getInputStream();
			int i = 0;
			while (i >= 0)
			{
				i = in.read();
				if (i >= 0)
					System.out.println((char)i);
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}

	}

	/**
	 * 	Test
	 *	@param args args
	 */
	public static void main (String[] args)
	{
		new RMIUtil();
	}	//	main

}	//	RMIUtil
