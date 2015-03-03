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

import java.net.*;
import java.util.*;
import javax.naming.*;

import org.compiere.interfaces.*;

/**
 *	Apps Server Connection Test
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ConnectTest.java,v 1.4 2005/10/08 02:03:38 jjanke Exp $
 */
public class ConnectTest
{
	/**
	 * 	Connection Test Constructor
	 * 	@param serverName server name or IP
	 */
	public ConnectTest (String serverName)
	{
		System.out.println("ConnectTest: " + serverName);
		System.out.println();
		//
		Hashtable<String,String> env = new Hashtable<String,String>();
		env.put(InitialContext.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		env.put(InitialContext.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
		env.put(InitialContext.PROVIDER_URL, serverName);
	//	env.put(InitialContext.SECURITY_PROTOCOL, "");				//	"ssl"
	//	env.put(InitialContext.SECURITY_AUTHENTICATION, "none");	//	"none", "simple", "strong"
	//	env.put(InitialContext.SECURITY_PRINCIPAL, "");
	//	env.put(InitialContext.SECURITY_CREDENTIALS, "");

		//	Get Context
		System.out.println ("Creating context ...");
		System.out.println ("  " + env);
		InitialContext context = null;
		try
		{
			context = new InitialContext(env);
		}
		catch (Exception e)
		{
			System.err.println("ERROR: Could not create context: " + e);
			return;
		}

		testJNP (serverName, context);
		testEJB (serverName, context);

	}	//	ConnectTest

	/**
	 * 	Test JNP
	 * 	@param serverName server name
	 *  @param context context
	 */
	private void testJNP (String serverName, InitialContext context)
	{
		//	Connect to MBean
		System.out.println();
		System.out.println ("Connecting to MBean ...");
		/**
		try
		{
			String connectorName = "jmx:" + serverName + ":rmi";
			RMIAdaptor server = (RMIAdaptor) context.lookup (connectorName);
			System.out.println("- have Server");
			System.out.println("- Default Domain=" + server.getDefaultDomain());
			System.out.println("- MBeanCount = " + server.getMBeanCount());

	//		ObjectName serviceName = new ObjectName ("Compiere:service=CompiereCtrl");
	//		System.out.println("- " + serviceName + " is registered=" + server.isRegistered(serviceName));

	//		System.out.println("  - CompiereSummary= "
	//				+ server.getAttribute(serviceName, "CompiereSummary"));

			Object[] params = {};
			String[] signature = {};
		}
		catch (Exception e)
		{
			System.err.println("ERROR: Could not contact MBean: " + e);
			return;
		}
		**/

		//	List Context
		System.out.println();
		System.out.println(" Examining context ....");
		try
		{
			System.out.println("  Namespace=" + context.getNameInNamespace());
			System.out.println("  Environment=" + context.getEnvironment());
			System.out.println("  Context '/':");
			NamingEnumeration ne = context.list("/");
			while (ne.hasMore())
				System.out.println("  - " + ne.nextElement());
			//
			System.out.println("  Context 'ejb':");
			ne = context.list("ejb");
			while (ne.hasMore())
				System.out.println("  - " + ne.nextElement());
			//
			System.out.println("  Context 'ejb/compiere':");
			ne = context.list("ejb/compiere");
			while (ne.hasMore())
				System.out.println("  - " + ne.nextElement());
		}
		catch (Exception e)
		{
			System.err.println("ERROR: Could not examine context: " + e);
			return;
		}
	}	//	testJNP

	/**
	 * 	Test EJB
	 * 	@param serverName server name
	 *  @param context context
	 */
	private void testEJB (String serverName, InitialContext context)
	{
		System.out.println();
		System.out.println ("Connecting to EJB server ...");
		try
		{
			System.out.println("  Name=" + StatusHome.JNDI_NAME);
			StatusHome staHome = (StatusHome)context.lookup (StatusHome.JNDI_NAME);
			System.out.println("  .. home created");
			Status sta = staHome.create();
			System.out.println("  .. bean created");
			System.out.println("  ServerVersion=" + sta.getMainVersion() + " " + sta.getDateVersion());
			sta.remove();
			System.out.println("  .. bean removed");
		}
		catch (Exception e)
		{
			System.err.println("ERROR: Could not connect: " + e);
			return;
		}

		System.out.println();
		System.out.println("SUCCESS !!");
	}	//	testEJB


	/**************************************************************************
	 * 	Start Method
	 *  @param args serverName
	 */
	public static void main(String[] args)
	{
		String serverName = null;
		if (args.length > 0)
			serverName = args[0];
		if (serverName == null || serverName.length() == 0)
		{
			try
			{
				serverName = InetAddress.getLocalHost().getHostName();
			}
			catch (UnknownHostException ex)
			{
				ex.printStackTrace();
			}
		}


		//	Start
		ConnectTest ct = new ConnectTest (serverName);
	}	//	main

}	//	ConnectionTest
