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

import java.util.*;

/**
 *	Setup Resources
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: SetupRes_da.java,v 1.2 2005/07/31 14:33:20 jpedersen Exp $
 */
public class SetupRes_da extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", "Compiere: Opsætning af server" },
	{ "Ok", 				"OK" },
	{ "File", 				"Fil" },
	{ "Exit", 				"Afslut" },
	{ "Help", 				"Hjælp" },
	{ "PleaseCheck", 		"Kontrollér" },
	{ "UnableToConnect",	"Kan ikke hente hjælp fra Compieres web-sted" },

	{ "CompiereHomeInfo", 	"Compiere Home er hovedmappen" },
	{ "CompiereHome", 		"Compiere Home" },
	{ "WebPortInfo", 		"Web (HTML)-port" },
	{ "WebPort", 			"Web-port" },
	{ "AppsServerInfo", 	"Programserverens navn" },
	{ "AppsServer", 		"Prog.-server" },
	{ "DatabaseTypeInfo", 	"Databasetype" },
	{ "DatabaseType", 		"Databasetype" },
	{ "DatabaseNameInfo", 	"Databasenavn " },
	{ "DatabaseName", 		"Databasenavn (SID)" },
	{ "DatabasePortInfo", 	"Database Listener Port" },
	{ "DatabasePort", 		"Databaseport" },
	{ "DatabaseUserInfo", 	"Database: Bruger-ID til Compiere" },
	{ "DatabaseUser", 		"Database: Bruger" },
	{ "DatabasePasswordInfo", "Database: Brugeradgangskode til Compiere" },
	{ "DatabasePassword", 	"Database: Adgangskode" },
	{ "TNSNameInfo", 		"TNS eller Global Database Name" },
	{ "TNSName", 			"TNS-navn" },
	{ "SystemPasswordInfo", "System: Brugeradgangskode" },
	{ "SystemPassword", 	"System-adgangskode" },
	{ "MailServerInfo", 	"Mail-server" },
	{ "MailServer", 		"Mail-server" },
	{ "AdminEMailInfo", 	"Compiere: Administrators e-mail" },
	{ "AdminEMail", 		"Admin. e-mail" },
	{ "DatabaseServerInfo", "Databaseservers navn" },
	{ "DatabaseServer", 	"Databaseserver" },
	{ "JavaHomeInfo", 		"Java Home-mappe" },
	{ "JavaHome", 			"Java Home" },
	{ "JNPPortInfo", 		"Programservers JNP-port" },
	{ "JNPPort", 			"JNP-port" },
	{ "MailUserInfo", 		"Compiere: Mail-bruger" },
	{ "MailUser", 			"Mail: Bruger" },
	{ "MailPasswordInfo", 	"Compiere: Brugeradgangskode til mail" },
	{ "MailPassword", 		"Adgangskode til mail" },
	{ "KeyStorePassword",		"Key Store Password" },
	{ "KeyStorePasswordInfo",	"Password for SSL Key Store" },
	//
	{ "JavaType",				"Java VM"},
	{ "JavaTypeInfo",			"Java VM Vendor"},
	{ "AppsType",				"Server Type"},
	{ "AppsTypeInfo",			"J2EE Application Server Type"},
	{ "DeployDir",				"Deployment"},
	{ "DeployDirInfo",			"J2EE Deployment Directory"},
	{ "ErrorDeployDir",			"Error Deployment Directory"},
	//
	{ "TestInfo", 			"Afprøv opsætning" },
	{ "Test", 				"Afprøv" },
	{ "SaveInfo", 			"Gem opsætning" },
	{ "Save", 				"Gem" },
	{ "HelpInfo", 			"Hjælp" },

	{ "ServerError", 		"Fejl: Serverops�tning" },
	{ "ErrorJavaHome", 		"Fejl: Java Home" },
	{ "ErrorCompiereHome", 	"Fejl: Compiere Home" },
	{ "ErrorAppsServer", 	"Fejl: Prog.-server (brug ikke localhost)" },
	{ "ErrorWebPort", 		"Fejl: Web-port" },
	{ "ErrorJNPPort", 		"Fejl: JNP-port" },
	{ "ErrorDatabaseServer", "Fejl: Databaseserver (brug ikke localhost)" },
	{ "ErrorDatabasePort", 	"Fejl: Databaseport" },
	{ "ErrorJDBC", 			"Fejl: JDBC-forbindelse" },
	{ "ErrorTNS", 			"Fejl: TNS-forbindelse" },
	{ "ErrorMailServer", 	"Fejl: Mailserver (brug ikke localhost)" },
	{ "ErrorMail", 			"Fejl: Mail" },
	{ "ErrorSave", 			"Fejl: Swing-fil" },

	{ "EnvironmentSaved",	"Miljøet er gemt\nGenstart serveren." },
	{ "RMIoverHTTP", 		"Tunnel Objects via HTTP" },
	{ "RMIoverHTTPInfo", 	"RMI over HTTP allows to go through firewalls" }
	};

	/**
	 * 	Get Contents
	 * 	@return contents
	 */
	public Object[][] getContents()
	{
		return contents;
	}	//	getContents

}	//	SetupRes_da

