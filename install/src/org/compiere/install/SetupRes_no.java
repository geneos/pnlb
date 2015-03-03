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
 *	Norwegian Setup Resource Translation
 *
 * 	@author 	Olaf Slazak Løken
 * 	@version 	$Id: SetupRes_no.java,v 1.6 2005/03/11 20:30:21 jjanke Exp $
 */
public class SetupRes_no extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", "Compiere Server Oppsett" },
	{ "Ok", 				"Ok" },
	{ "File", 				"Fil" },
	{ "Exit", 				"Avslutt" },
	{ "Help", 				"Hjelp" },
	{ "PleaseCheck", 		"Vennligst Sjekk" },
	{ "UnableToConnect",	"Umulig å hente hjelp fra Compiere Web Side" },

	{ "CompiereHomeInfo", 	"Compiere Hjem er i hoved Mappen" },
	{ "CompiereHome", 		"Compiere Hjem" },
	{ "WebPortInfo", 		"Web (HTML) Port" },
	{ "WebPort", 			"Web Port" },
	{ "AppsServerInfo", 	"Applikasion Server Navn" },
	{ "AppsServer", 		"App. Server" },
	{ "DatabaseTypeInfo", 	"Database Type" },
	{ "DatabaseType", 		"Database Type" },
	{ "DatabaseNameInfo", 	"Database Navn " },
	{ "DatabaseName", 		"Database Navn (SID)" },
	{ "DatabasePortInfo", 	"Database Listener Port" },
	{ "DatabasePort", 		"Database Port" },
	{ "DatabaseUserInfo", 	"Database Compiere Bruker ID" },
	{ "DatabaseUser", 		"Database Bruker" },
	{ "DatabasePasswordInfo", "Database Compiere Bruker Passord" },
	{ "DatabasePassword", 	"Database Passord" },
	{ "TNSNameInfo", 		"TNS eller Global Database Navn" },
	{ "TNSName", 			"TNS Navn" },
	{ "SystemPasswordInfo", "System Bruker Passord" },
	{ "SystemPassword", 	"System Passord" },
	{ "MailServerInfo", 	"Epost Server" },
	{ "MailServer", 		"Epost Server" },
	{ "AdminEMailInfo", 	"Compiere Administrator EPost" },
	{ "AdminEMail", 		"Admin EPost" },
	{ "DatabaseServerInfo", "Database Server Navn" },
	{ "DatabaseServer", 	"Database Server" },
	{ "JavaHomeInfo", 		"Java Hjem Katalog" },
	{ "JavaHome", 			"Java Hjem" },
	{ "JNPPortInfo", 		"Aplikasions Server JNP Port" },
	{ "JNPPort", 			"JNP Port" },
	{ "MailUserInfo", 		"Compiere EPost User" },
	{ "MailUser", 			"EPost User" },
	{ "MailPasswordInfo", 	"Compiere EPost Bruker Passord" },
	{ "MailPassword", 		"EPost Passord" },
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
	{ "TestInfo", 			"Test Oppsettet" },
	{ "Test", 				"Test" },
	{ "SaveInfo", 			"Lagre Oppsett" },
	{ "Save", 				"Lagre" },
	{ "HelpInfo", 			"Hent Hjelp" },

	{ "ServerError", 		"Server Oppsett Feil" },
	{ "ErrorJavaHome", 		"Feil Java Hjem" },
	{ "ErrorCompiereHome", 	"Feil Compiere Hjem" },
	{ "ErrorAppsServer", 	"Feil App. Server (ikke bruk localhost)" },
	{ "ErrorWebPort", 		"Feil Web Port" },
	{ "ErrorJNPPort", 		"Feil JNP Port" },
	{ "ErrorDatabaseServer", "Feil Database Server (ikke bruk localhost)" },
	{ "ErrorDatabasePort", 	"Feil Database Port" },
	{ "ErrorJDBC", 			"Feil ved JDBC Oppkobling" },
	{ "ErrorTNS", 			"Feil ved TNS Oppkobling" },
	{ "ErrorMailServer", 	"Feil EPost Server (ikke bruk localhost)" },
	{ "ErrorMail", 			"Feil EPost" },
	{ "ErrorSave", 			"Feil Sving Fil" },

	{ "EnvironmentSaved",	"Oppsett lagret\nDu trenger å restarte serveren." },
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

}	//	SerupRes
