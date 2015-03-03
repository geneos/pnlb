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
 * 	@version 	$Id: SetupRes_ml.java,v 1.5 2005/03/11 20:30:21 jjanke Exp $
 */
public class SetupRes_ml extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", "Compiere Server Setup" },
	{ "Ok", 				"Ok" },
	{ "File", 				"File" },
	{ "Exit", 				"Exit" },
	{ "Help", 				"Help" },
	{ "PleaseCheck", 		"Please Check" },
	{ "UnableToConnect",	"Unable get help from Compiere Web Site" },

	{ "CompiereHomeInfo", 	"Compiere Home is the main Folder" },
	{ "CompiereHome", 		"Compiere Home" },
	{ "WebPortInfo", 		"Web (HTML) Port" },
	{ "WebPort", 			"Web Port" },
	{ "AppsServerInfo", 	"Application Server Name" },
	{ "AppsServer", 		"Apps Server" },
	{ "DatabaseTypeInfo", 	"Database Type" },
	{ "DatabaseType", 		"Database Type" },
	{ "DatabaseNameInfo", 	"Database Name " },
	{ "DatabaseName", 		"Database Name (SID)" },
	{ "DatabasePortInfo", 	"Database Listener Port" },
	{ "DatabasePort", 		"Database Port" },
	{ "DatabaseUserInfo", 	"Database Compiere User ID" },
	{ "DatabaseUser", 		"Database User" },
	{ "DatabasePasswordInfo", "Database Compiere User Password" },
	{ "DatabasePassword", 	"Database Password" },
	{ "TNSNameInfo", 		"TNS or Global Database Name" },
	{ "TNSName", 			"TNS Name" },
	{ "SystemPasswordInfo", "System User Password" },
	{ "SystemPassword", 	"System Password" },
	{ "MailServerInfo", 	"Mail Server" },
	{ "MailServer", 		"Mail Server" },
	{ "AdminEMailInfo", 	"Compiere Administrator EMail" },
	{ "AdminEMail", 		"Admin EMail" },
	{ "DatabaseServerInfo", "Database Server Name" },
	{ "DatabaseServer", 	"Database Server" },
	{ "JavaHomeInfo", 		"Java Home Folder" },
	{ "JavaHome", 			"Java Home" },
	{ "JNPPortInfo", 		"Application Server JNP Port" },
	{ "JNPPort", 			"JNP Port" },
	{ "MailUserInfo", 		"Compiere Mail User" },
	{ "MailUser", 			"Mail User" },
	{ "MailPasswordInfo", 	"Compiere Mail User Password" },
	{ "MailPassword", 		"Mail Password" },
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
	{ "TestInfo", 			"Test the Setup" },
	{ "Test", 				"Test" },
	{ "SaveInfo", 			"Save the Setup" },
	{ "Save", 				"Save" },
	{ "HelpInfo", 			"Get Help" },

	{ "ServerError", 		"Server Setup Error" },
	{ "ErrorJavaHome", 		"Error Java Home" },
	{ "ErrorCompiereHome", 	"Error Compiere Home" },
	{ "ErrorAppsServer", 	"Error Apps Server (do not use localhost)" },
	{ "ErrorWebPort", 		"Error Web Port" },
	{ "ErrorJNPPort", 		"Error JNP Port" },
	{ "ErrorDatabaseServer", "Error Database Server (do not use localhost)" },
	{ "ErrorDatabasePort", 	"Error Database Port" },
	{ "ErrorJDBC", 			"Error JDBC Connection" },
	{ "ErrorTNS", 			"Error TNS Connection" },
	{ "ErrorMailServer", 	"Error Mail Server (do not use localhost)" },
	{ "ErrorMail", 			"Error Mail" },
	{ "ErrorSave", 			"Error Sving File" },

	{ "EnvironmentSaved",	"Environment saved\nYou need to re-start the server." }
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
