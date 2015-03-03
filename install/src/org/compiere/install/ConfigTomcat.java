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
 * 	Tomcat 5.5.9 Configuration
 *	
 *  @author Jorg Janke
 *  @version $Id: ConfigTomcat.java,v 1.3 2005/09/13 04:27:21 jjanke Exp $
 */
public class ConfigTomcat extends Config
{
	/**
	 * 	ConfigJBoss
	 */
	public ConfigTomcat (ConfigurationData data)
	{
		super (data);
	}	//	ConfigTomcat
	
	/**
	 * 	Initialize
	 */
	public void init()
	{
		p_data.setAppsServerDeployDir(getDeployDir());
		p_data.setAppsServerDeployDir(true);
		//
		p_data.setAppsServerJNPPort("1099");
		p_data.setAppsServerJNPPort(false);
		p_data.setAppsServerWebPort("80");
		p_data.setAppsServerWebPort(true);
		p_data.setAppsServerSSLPort("443");
		p_data.setAppsServerSSLPort(true);
	}	//	init

	/**
	 * 	Get Notes
	 *	@return notes
	 */
	public String getNotes()
	{
		return "Compiere requires Tomcat 5.5.9"
			+ "\nPlease set the Web Port in $CATALINA_HOME//conf//server.xml"
			//	C:\Program Files\Apache Software Foundation\Tomcat 5.5\conf\server.xml
			+ "\n";
	}	//	getNotes
	
	
	/**
	 * 	Get Deployment Dir
	 *	@return deployment dir
	 */
	private String getDeployDir()
	{
		return "C:"
			+ File.separator + "Program Files"
			+ File.separator + "Apache Software Foundation"
			+ File.separator + "Tomcat 5.5"; 
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
		File deploy = new File (p_data.getAppsServerDeployDir());
		pass = deploy.exists();
		error = "CATALINA_HOME Not found: " + deploy;
		signalOK(getPanel().okDeployDir, "ErrorDeployDir", 
			pass, true, error);
		if (!pass)
			return error;
		setProperty(ConfigurationData.COMPIERE_APPS_DEPLOY, p_data.getAppsServerDeployDir());
		log.info("OK: Deploy Directory = " + deploy);

		String baseDir = p_data.getAppsServerDeployDir();
		if (!baseDir.endsWith(File.separator))
			baseDir += File.separator;
		//	Need to have /shared/lib
		String sharedLib = baseDir + "shared" + File.separator + "lib";
		File sharedLibDir = new File (sharedLib);
		pass = sharedLibDir.exists();
		error = "Not found (shared library): " + sharedLib;
		signalOK(getPanel().okDeployDir, "ErrorDeployDir", 
			pass, true, error);
		if (!pass)
			return error;

		//	Need to have /webapps
		String webApps = baseDir + "webapps";
		File webAppsDir = new File (webApps);
		pass = webAppsDir.exists();
		error = "Not found (webapps): " + sharedLib;
		signalOK(getPanel().okDeployDir, "ErrorDeployDir", 
			pass, true, error);
		if (!pass)
			return error;
		//
		return null;
	}	//	test

}	//	ConfigTomcat
