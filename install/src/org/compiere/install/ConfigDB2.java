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
import java.sql.*;

import org.compiere.db.*;
import org.compiere.util.*;

import com.ibm.db2.jcc.*;

/**
 * 	DB/2 Configuration Test
 *	
 *  @author Jorg Janke
 *  @version $Id: ConfigDB2.java,v 1.1 2005/12/31 06:33:50 jjanke Exp $
 */
public class ConfigDB2 extends Config
{
	/**
	 * 	ConfigOracle
	 */
	public ConfigDB2 (ConfigurationData data)
	{
		super (data);
	}	//	ConfigOracle

	/** DB/2 Driver				*/
	private static DB2Driver s_db2Driver = null;
	/** Last Connection			*/
	private Connection			m_con = null;

	/**
	 * 	Init
	 */
	public void init()
	{
		p_data.setDatabasePort(String.valueOf(DB_DB2.DEFAULT_PORT_0));
		p_data.setDatabaseName("compiere");
	}	//	init

	
	/**************************************************************************
	 * 	Test
	 *	@return error message or null if OK
	 */
	public String test()
	{
		//	Database Server
		String server = p_data.getDatabaseServer();
		boolean pass = server != null && server.length() > 0
			&& server.toLowerCase().indexOf("localhost") == -1 
			&& !server.equals("127.0.0.1");
		String error = "Not correct: DB Server = " + server;
		InetAddress databaseServer = null;
		try
		{
			if (pass)
				databaseServer = InetAddress.getByName(server);
		}
		catch (Exception e)
		{
			error += " - " + e.getMessage();
			pass = false;
		}
		signalOK(getPanel().okDatabaseServer, "ErrorDatabaseServer", 
			pass, true, error); 
		log.info("OK: Database Server = " + databaseServer);
		setProperty(ConfigurationData.COMPIERE_DB_SERVER, databaseServer.getHostName());
		setProperty(ConfigurationData.COMPIERE_DB_TYPE, p_data.getDatabaseType());

		//	Database Port
		int databasePort = p_data.getDatabasePort();
		pass = p_data.testPort (databaseServer, databasePort, true);
		error = "DB Server Port = " + databasePort; 
		signalOK(getPanel().okDatabaseServer, "ErrorDatabasePort",
			pass, true, error);
		if (!pass)
			return error;
		log.info("OK: Database Port = " + databasePort);
		setProperty(ConfigurationData.COMPIERE_DB_PORT, String.valueOf(databasePort));


		//	JDBC Database Info
		String databaseName = p_data.getDatabaseName();	//	Service Name
		String systemPassword = p_data.getDatabaseSystemPassword();
		pass = systemPassword != null && systemPassword.length() > 0;
		error = "No Database System Password entered";
		signalOK(getPanel().okDatabaseSystem, "ErrorJDBC",
			pass, true,	error);
		if (!pass)
			return error;
		//
		//	URL (derived)	jdbc:oracle:thin:@//prod1:1521/prod1
		String url = "jdbc:db2://" + databaseServer.getHostName()
			+ ":" + databasePort
			+ "/" + databaseName;
		pass = testJDBC(url, "db2admin", systemPassword);
		error = "Error connecting: " + url 
			+ " - as db2admin/" + systemPassword;
		signalOK(getPanel().okDatabaseSystem, "ErrorJDBC",
			pass, true, error);
		if (!pass)
			return error;
		log.info("OK: Connection = " + url);
		setProperty(ConfigurationData.COMPIERE_DB_URL, url);
		log.info("OK: Database System User " + databaseName);
		setProperty(ConfigurationData.COMPIERE_DB_NAME, databaseName);
		setProperty(ConfigurationData.COMPIERE_DB_SYSTEM, systemPassword);


		//	Database User Info
		String databaseUser = p_data.getDatabaseUser();	//	UID
		String databasePassword = p_data.getDatabasePassword();	//	PWD
		pass = databasePassword != null && databasePassword.length() > 0;
		error = "Invalid Database User Password";
		signalOK(getPanel().okDatabaseUser, "ErrorJDBC",
			pass, true, error); 
		if (!pass)
			return error;
		//	Ignore result as it might not be imported
		pass = testJDBC(url, databaseUser, databasePassword);
		error = "Database imported? Cannot connect to User: " + databaseUser + "/" + databasePassword;
		signalOK(getPanel().okDatabaseUser, "ErrorJDBC",
			pass, false, error);
		if (pass)
		{
			log.info("OK: Database User = " + databaseUser);
			if (m_con != null)
				setProperty(ConfigurationData.COMPIERE_WEBSTORES, getWebStores(m_con));
		}
		else
			log.warning(error);
		setProperty(ConfigurationData.COMPIERE_DB_USER, databaseUser);
		setProperty(ConfigurationData.COMPIERE_DB_PASSWORD, databasePassword);

		if (!p_data.getAppsServerType().equals(ConfigurationData.APPSTYPE_TOMCAT))
		{
			String cmd = "db2 ";
			if (Env.isWindows())
				cmd = "db2cmd -c -w -i db2 ";
			String sqlcmd1 = cmd + "connect to xx";
			String sqlcmd2 = cmd + "-f utils/db2/Test.sql";
			log.config(sqlcmd2);
			pass = testSQL(sqlcmd2);
			error = "Error connecting via: " + sqlcmd2;
			signalOK(getPanel().okDatabaseSQL, "ErrorTNS", 
				pass, true, error);
			if (pass)
				log.info("OK: Database SQL Connection");
		}
		
		m_con = null;
		return null;
	}	//	test
	
	/**
	 * 	Test JDBC Connection to Server
	 * 	@param url connection string
	 *  @param uid user id
	 *  @param pwd password
	 * 	@return true if OK
	 */
	private boolean testJDBC (String url, String uid, String pwd)
	{
		log.fine("Url=" + url + ", UID=" + uid);
		try
		{
			if (s_db2Driver == null)
			{
				s_db2Driver = new DB2Driver();
				DriverManager.registerDriver(s_db2Driver);
			}
			m_con = DriverManager.getConnection(url, uid, pwd);
		}
		catch (Exception e)
		{
			log.severe(e.toString());
			return false;
		}
		return true;
	}	//	testJDBC
	
	/**
	 * 	Test Command Line Connection
	 *  @param sqlcmd sql command line
	 * 	@return true if OK
	 */
	private boolean testSQL (String sqlcmd)
	{
		if (true)
			return true;
		//
		StringBuffer sbOut = new StringBuffer();
		StringBuffer sbErr = new StringBuffer();
		int result = -1;
		try
		{
			Process p = Runtime.getRuntime().exec (sqlcmd);
			InputStream in = p.getInputStream();
			int c;
			while ((c = in.read()) != -1)
			{
				sbOut.append((char)c);
				System.out.print((char)c);
			}
			in.close();
			in = p.getErrorStream();
			while ((c = in.read()) != -1)
				sbErr.append((char)c);
			in.close();
			//	Get result
			try
			{
				Thread.yield();
				result = p.exitValue();
			}
			catch (Exception e)		//	Timing issue on Solaris.
			{
				Thread.sleep(200);	//	.2 sec
				result = p.exitValue();
			}
		}
		catch (Exception ex)
		{
			log.severe(ex.toString());
		}
		log.finer(sbOut.toString());
		if (sbErr.length() > 0)
			log.warning(sbErr.toString());
		return result == 0;
	}	//	testSQL
	
}	//	ConfigDB2
