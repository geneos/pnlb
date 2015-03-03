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
import java.util.*;
import java.util.regex.*;
import org.compiere.db.*;
import oracle.jdbc.*;


/**
 *	Oracle Confguration
 *	
 *  @author Jorg Janke
 *  @version $Id: ConfigOracle.java,v 1.8 2005/12/31 06:33:50 jjanke Exp $
 */
public class ConfigOracle extends Config
{
	/**
	 * 	ConfigOracle
	 */
	public ConfigOracle (ConfigurationData data)
	{
		super (data);
	}	//	ConfigOracle
	
	/**	Oracle Driver			*/
	private static OracleDriver s_oracleDriver = null;
	/** Discoverd TNS			*/
	private String[] 			p_discovered = null;
	/** Last Connection			*/
	private Connection			m_con = null;
	
	/**
	 * 	Init
	 */
	public void init()
	{
		p_data.setDatabasePort(String.valueOf(DB_Oracle.DEFAULT_PORT));
	}	//	init
	
	/**
	 * 	Discover Databases.
	 * 	To be overwritten by database configs
	 *	@param selected selected database
	 *	@return array of databases
	 */
	public String[] discoverDatabases(String selected)
	{
		if (p_discovered != null)
			return p_discovered;
		//
		ArrayList<String> list = new ArrayList<String>();
		//	default value to lowercase or null
		String def = selected;
		if (def != null && def.trim().length() == 0)
			def = null;
		if (def != null)
			list.add(def.toLowerCase());

		//	Search for Oracle Info
		String path = System.getProperty("java.library.path");
		String[] entries = path.split(File.pathSeparator);
		for (int e = 0; e < entries.length; e++)
		{
			String entry = entries[e].toLowerCase();
			if (entry.indexOf("ora") != -1 && entry.endsWith("bin"))
			{
				StringBuffer sb = getTNS_File (entries[e].substring(0, entries[e].length()-4));
				String[] tnsnames = getTNS_Names (sb);
				if (tnsnames != null)
				{
					for (int i = 0; i < tnsnames.length; i++)
					{
						String tns = tnsnames[i];	//	 is lower case
						if (!tns.equals(def))
							list.add(tns);
					}
					break;
				}
			}
		}	//	for all path entries

		p_discovered = new String[list.size()];
		list.toArray(p_discovered);
		return p_discovered;
	}	//	discoverDatabases
	
	/**
	 * 	Get File tnmsnames.ora in StringBuffer
	 * 	@param oraHome ORACLE_HOME
	 * 	@return tnsnames.ora or null
	 */
	private StringBuffer getTNS_File (String oraHome)
	{
		String tnsnames = oraHome + File.separator
			+ "network" + File.separator
			+ "admin" + File.separator
			+ "tnsnames.ora";
		File tnsfile = new File (tnsnames);
		if (!tnsfile.exists())
			return null;

		log.fine(tnsnames);
		StringBuffer sb = new StringBuffer();
		try
		{
			FileReader fr = new FileReader (tnsfile);
			int c;
			while ((c = fr.read()) != -1)
				sb.append((char)c);
		}
		catch (IOException ex)
		{
			log.severe("Error Reading " + tnsnames);
			ex.printStackTrace();
			return null;
		}
		if (sb.length() == 0)
			return null;
		return sb;
	}	//	getTNS_File

	/**
	 * 	Get TNS Names entries.
	 * 	Assumes standard tnsmanes.ora formatting of NetMgr
	 * 	@param tnsnames content of tnsnames.ora
	 * 	@return tns names or null
	 */
	private String[] getTNS_Names (StringBuffer tnsnames)
	{
		if (tnsnames == null)
			return null;

		ArrayList<String> list = new ArrayList<String>();
		Pattern pattern = Pattern.compile("$", Pattern.MULTILINE);
		String[] lines = pattern.split(tnsnames);
		for (int i = 0; i < lines.length; i++)
		{
			String line = lines[i].trim();
			log.finest(i + ": " + line);
			if (false)	//	get TNS Name
			{
				if (line.length() > 0
					&& Character.isLetter(line.charAt(0))	//	no # (
					&& line.indexOf("=") != -1
					&& line.indexOf("EXTPROC_") == -1
					&& line.indexOf("_HTTP") == -1)
				{
					String entry = line.substring(0, line.indexOf('=')).trim().toLowerCase();
					log.fine(entry);
					list.add(entry);
				}
			}
			else	//	search service names
			{
				if (line.length() > 0
					&& line.toUpperCase().indexOf("SERVICE_NAME") != -1)
				{
					String entry = line.substring(line.indexOf('=')+1).trim().toLowerCase();
					int index = entry.indexOf(')');
					if (index != 0)
						entry = entry.substring(0, index).trim();
					log.fine(entry);
					list.add(entry);
				}
				
			}
		}
		//	Convert to Array
		if (list.size() == 0)
			return null;
		String[] retValue = new String[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getTNS_Names
	
	
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
		String url = "jdbc:oracle:thin:@//" + databaseServer.getHostName()
			+ ":" + databasePort
			+ "/" + databaseName;
		pass = testJDBC(url, "system", systemPassword);
		error = "Error connecting: " + url 
			+ " - as system/" + systemPassword;
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

		//	TNS Name Info via sqlplus - if not tomcat 
		if (!p_data.getAppsServerType().equals(ConfigurationData.APPSTYPE_TOMCAT))
		{
			String sqlplus = "sqlplus system/" + systemPassword + "@" + databaseName
				+ " @utils/oracle/Test.sql";
			log.config(sqlplus);
			pass = testSQL(sqlplus);
			error = "Error connecting via: " + sqlplus;
			signalOK(getPanel().okDatabaseSQL, "ErrorTNS", 
				pass, true, error);
			if (pass)
				log.info("OK: Database SQL Connection");
		}
		
		//	OCI Test
		if (System.getProperty("TestOCI", "N").equals("Y"))
		{
			url = "jdbc:oracle:oci8:@" + databaseName;
			pass = testJDBC(url, "system", systemPassword);
			if (pass)
				log.info("OK: Connection = " + url);
			else
				log.warning("Cannot connect via Net8: " + url);
		}
			log.info("OCI Test Skipped");
		
		//
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
			if (s_oracleDriver == null)
			{
				s_oracleDriver = new OracleDriver();
				DriverManager.registerDriver(s_oracleDriver);
			}
			m_con = DriverManager.getConnection(url, uid, pwd);
		}
		catch (UnsatisfiedLinkError ule)
		{
			log.warning("Check [ORACLE_HOME]/jdbc/Readme.txt for (OCI) driver setup");
			log.warning(ule.toString());
		}
		catch (Exception e)
		{
			log.severe(e.toString());
			return false;
		}
		return true;
	}	//	testJDBC

	/**
	 * 	Test TNS Connection
	 *  @param sqlplus sqlplus command line
	 * 	@return true if OK
	 */
	private boolean testSQL (String sqlplus)
	{
		StringBuffer sbOut = new StringBuffer();
		StringBuffer sbErr = new StringBuffer();
		int result = -1;
		try
		{
			Process p = Runtime.getRuntime().exec (sqlplus);
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
			
}	//	ConfigOracle
