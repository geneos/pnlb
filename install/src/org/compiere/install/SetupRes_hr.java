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
 * 	@author 	Marko Bubalo
 * 	@version 	$Id: SetupRes_hr.java,v 1.6 2005/03/11 20:30:21 jjanke Exp $
 */
public class SetupRes_hr extends ListResourceBundle
{
	/**	Translation Info	*/
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", 	"Postavke Compiere servera" },
	{ "Ok", 					"U redu" },
	{ "File", 					"Datoteka" },
	{ "Exit", 					"Izlaz" },
	{ "Help", 					"Pomoæ" },
	{ "PleaseCheck", 			"Molim provjeriti" },
	{ "UnableToConnect", 		"Greška u spajanju na Compiere web pomoæ" },
	//
	{ "CompiereHomeInfo", 		"Compiere Home je glavni direktorij" },
	{ "CompiereHome", 			"Compiere Home" },
	{ "WebPortInfo", 			"Web (HTML) Port" },
	{ "WebPort", 				"Web Port" },
	{ "AppsServerInfo", 		"Naziv servera aplikacije" },
	{ "AppsServer", 			"Server aplikacije" },
	{ "DatabaseTypeInfo", 		"Tip baze podataka" },
	{ "DatabaseType", 			"Tip baze podataka" },
	{ "DatabaseNameInfo", 		"Naziv baze " },
	{ "DatabaseName", 			"Naziv baze (SID)" },
	{ "DatabasePortInfo", 		"Database Listener Port" },
	{ "DatabasePort", 			"Port baze" },
	{ "DatabaseUserInfo", 		"Database Compiere User ID" },
	{ "DatabaseUser", 			"Korisnik baze" },
	{ "DatabasePasswordInfo", 	"Lozinka korisnika baze" },
	{ "DatabasePassword", 		"Lozinka baze" },
	{ "TNSNameInfo", 			"TNS or Global Database Name" },
	{ "TNSName", 				"TNS Name" },
	{ "SystemPasswordInfo", 	"Lozinka korisnika Sytem" },
	{ "SystemPassword", 		"System lozinka" },
	{ "MailServerInfo", 		"Mail Server" },
	{ "MailServer", 			"Mail Server" },
	{ "AdminEMailInfo", 		"Compiere Administrator EMail" },
	{ "AdminEMail", 			"Admin EMail" },
	{ "DatabaseServerInfo", 	"Naziv servera baze" },
	{ "DatabaseServer", 		"Server baze" },
	{ "JavaHomeInfo", 			"Java Home Folder" },
	{ "JavaHome", 				"Java Home" },
	{ "JNPPortInfo", 			"Application Server JNP Port" },
	{ "JNPPort", 				"JNP Port" },
	{ "MailUserInfo", 			"Compiere Mail korisnik" },
	{ "MailUser", 				"Mail korisnik" },
	{ "MailPasswordInfo", 		"Compiere Mail lozinka korisnika" },
	{ "MailPassword", 			"Mail lozinka" },
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
	{ "TestInfo", 				"Test postavki" },
	{ "Test", 					"Test" },
	{ "SaveInfo", 				"Saèuvati postavke" },
	{ "Save", 					"Saèuvati" },
	{ "HelpInfo", 				"Pomoæ" },
	//
	{ "ServerError", 			"Server Setup Error" },
	{ "ErrorJavaHome", 			"Error Java Home" },
	{ "ErrorCompiereHome", 		"Error Compiere Home" },
	{ "ErrorAppsServer", 		"Error Apps Server (do not use localhost)" },
	{ "ErrorWebPort", 			"Error Web Port" },
	{ "ErrorJNPPort", 			"Error JNP Port" },
	{ "ErrorDatabaseServer", 	"Error Database Server (do not use localhost)" },
	{ "ErrorDatabasePort", 		"Error Database Port" },
	{ "ErrorJDBC", 				"Error JDBC Connection" },
	{ "ErrorTNS", 				"Error TNS Connection" },
	{ "ErrorMailServer", 		"Error Mail Server (do not use localhost)" },
	{ "ErrorMail", 				"Error Mail" },
	{ "ErrorSave", 				"Error Sving File" },

	{ "EnvironmentSaved", 		"Environment saved\nYou can now start the Application Server." }

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
