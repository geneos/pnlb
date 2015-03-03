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
package org.compiere.install;

import java.io.*;
import java.net.*;


/**
 *	JBoss 4.0.2 Apps Server Configuration
 *	
 *  @author Jorg Janke
 *  @version $Id: ConfigJBoss.java,v 1.3 2005/09/07 04:47:23 jjanke Exp $
 */
public class ConfigJBoss extends Config
{

	/**
	 * 	ConfigJBoss
	 */
	public ConfigJBoss (ConfigurationData data)
	{
		super (data);
	}	//	ConfigJBoss
	
	/**
	 * 	Initialize
	 */
	public void init()
	{
		p_data.setAppsServerDeployDir(getDeployDir());
		p_data.setAppsServerDeployDir(false);
		//
		p_data.setAppsServerJNPPort("1099");
		p_data.setAppsServerJNPPort(true);
		p_data.setAppsServerWebPort("80");
		p_data.setAppsServerWebPort(true);
		p_data.setAppsServerSSLPort("443");
		p_data.setAppsServerSSLPort(true);
	}	//	init

	/**
	 * 	Get Deployment Dir
	 *	@return deployment dir
	 */
	private String getDeployDir()
	{
		return p_data.getCompiereHome()
			+ File.separator + "jboss"
			+ File.separator + "server"
			+ File.separator + "compiere" 
			+ File.separator + "deploy";
	}	//	getDeployDir
	
	/**
	 * 	Test
	 *	@return error message or null if OK
	 */
	public String test()
	{
		//	AppsServer
		String server = p_data.getAppsServer();
		boolean pass = server != null && server.length() > 0
			&& server.toLowerCase().indexOf("localhost") == -1
			&& !server.equals("127.0.0.1");
		InetAddress appsServer = null;
		String error = "Not correct: AppsServer = " + server; 
		try
		{
			if (pass)
				appsServer = InetAddress.getByName(server);
		}
		catch (Exception e)
		{
			error += " - " + e.getMessage();
			pass = false;
		}
		signalOK(getPanel().okAppsServer, "ErrorAppsServer",
			pass, true, error); 
		if (!pass)
			return error;
		log.info("OK: AppsServer = " + appsServer);
		setProperty(ConfigurationData.COMPIERE_APPS_SERVER, appsServer.getHostName());
		setProperty(ConfigurationData.COMPIERE_APPS_TYPE, p_data.getAppsServerType());

		//	Deployment Dir
		p_data.setAppsServerDeployDir(getDeployDir());
		File deploy = new File (p_data.getAppsServerDeployDir());
		pass = deploy.exists();
		error = "Not found: " + deploy;
		signalOK(getPanel().okDeployDir, "ErrorDeployDir", 
			pass, true, error);
		if (!pass)
			return error;
		setProperty(ConfigurationData.COMPIERE_APPS_DEPLOY, p_data.getAppsServerDeployDir());
		log.info("OK: Deploy Directory = " + deploy);
		
		//	JNP Port
		int JNPPort = p_data.getAppsServerJNPPort();
		pass = !p_data.testPort (appsServer, JNPPort, false) 
			&& p_data.testServerPort(JNPPort);
		error = "Not correct: JNP Port = " + JNPPort;
		signalOK(getPanel().okJNPPort, "ErrorJNPPort", 
			pass, true, error);
		if (!pass)
			return error;
		log.info("OK: JNPPort = " + JNPPort);
		setProperty(ConfigurationData.COMPIERE_JNP_PORT, String.valueOf(JNPPort));

		//	Web Port
		int WebPort = p_data.getAppsServerWebPort();
		pass = !p_data.testPort ("http", appsServer.getHostName(), WebPort, "/") 
			&& p_data.testServerPort(WebPort);
		error = "Not correct: Web Port = " + WebPort;
		signalOK(getPanel().okWebPort, "ErrorWebPort",
			pass, true, error); 
		if (!pass)
			return error;
		log.info("OK: Web Port = " + WebPort);
		setProperty(ConfigurationData.COMPIERE_WEB_PORT, String.valueOf(WebPort));
		
		//	SSL Port
		int sslPort = p_data.getAppsServerSSLPort();
		pass = !p_data.testPort ("https", appsServer.getHostName(), sslPort, "/") 
			&& p_data.testServerPort(sslPort);
		error = "Not correct: SSL Port = " + sslPort;
		signalOK(getPanel().okSSLPort, "ErrorWebPort",
			pass, true, error); 
		if (!pass)
			return error;
		log.info("OK: SSL Port = " + sslPort);
		setProperty(ConfigurationData.COMPIERE_SSL_PORT, String.valueOf(sslPort));
		//
		return null;
	}	//	test
	
}	//	ConfigJBoss
