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
 * 	@version 	$Id: SetupRes_ar.java,v 1.1 2005/12/01 21:01:41 jjanke Exp $
 */
public class SetupRes_ar extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", 	"Compiere Server Setup" },
	{ "Ok", 					"\u0637\u00a7\u0638\u201e\u0638\u2026\u0638\u02c6\u0637\u00a7\u0638\u067e\u0638\u201a\u0637\u00a9"},
	{ "File", 					"\u0637\u00a7\u0638\u201e\u0638\u2026\u0638\u201e\u0638\u067e\u0638\u2018"},
	{ "Exit", 					"\u0637\u00ae\u0637\u00b1\u0638\u02c6\u0637\u00ac"},
	{ "Help", 					"\u0638\u2026\u0637\u00b3\u0637\u00a7\u0637\u00b9\u0637\u00af\u0637\u00a9"},
	{ "PleaseCheck", 			 "\u0638\u2026\u0638\u2020 \u0638\u067e\u0637\u00b6\u0638\u201e\u0638\u0192 \u0637\u00a7\u0638\u067e\u0637\u00ad\u0637\u00b5 \u0638\u2026\u0638\u0688\u0637\u00ac\u0637\u00af\u0638\u2018\u0638\u0698\u0637\u00af\u0638\u2039\u0637\u00a7"},
	{ "UnableToConnect", 		"\u0638\u2026\u0638\u2020 \u0637\u061b\u0638\u0679\u0637\u00b1 \u0637\u00a7\u0638\u201e\u0638\u2026\u0638\u0688\u0638\u2026\u0638\u2019\u0638\u0192\u0638\u06af\u0638\u2020 \u0637\u00a7\u0638\u201e\u0637\u00ad\u0638\u0688\u0637\u00b5\u0638\u02c6\u0638\u201e \u0637\u00b9\u0638\u201e\u0638\u2030 \u0638\u2026\u0638\u0688\u0637\u00b3\u0637\u00a7\u0637\u00b9\u0637\u00af\u0637\u00a9 \u0637\u00b9\u0638\u2020 \u0637\u00b7\u0637\u00b1\u0638\u0679\u0638\u201a \u0638\u2026\u0638\u02c6\u0638\u201a\u0637\u00b9 \u0637\u00a7\u0638\u201e\u0637\u00a5\u0638\u2020\u0637\u06be\u0637\u00b1\u0638\u2020\u0637\u06be" },
	//
	{ "CompiereHomeInfo", 		"Compiere Home is the main Folder" },
	{ "CompiereHome", 			"Compiere Home" },
	{ "WebPortInfo", 			"Web (HTML) Port" },
	{ "WebPort", 				"Web Port" },
	{ "AppsServerInfo", 		"Application Server Name" },
	{ "AppsServer", 			"Application Server" },
	{ "DatabaseTypeInfo", 	   	        "\u0638\u2020\u0638\u02c6\u0637\u00b9 \u0638\u201a\u0637\u00a7\u0637\u00b9\u0637\u00af\u0637\u00a9 \u0637\u00a7\u0638\u201e\u0637\u00a8\u0638\u0679\u0637\u00a7\u0638\u2020\u0637\u00a7\u0637\u06be" },
	{ "DatabaseType", 			"\u0638\u2020\u0638\u02c6\u0637\u00b9 \u0638\u201a\u0637\u00a7\u0637\u00b9\u0637\u00af\u0637\u00a9 \u0637\u00a7\u0638\u201e\u0637\u00a8\u0638\u0679\u0637\u00a7\u0638\u2020\u0637\u00a7\u0637\u06be" },
	{ "DatabaseNameInfo", 		         "\u0637\u00a7\u0637\u00b3\u0638\u2026 \u0638\u201a\u0637\u00a7\u0637\u00b9\u0637\u00af\u0637\u00a9 \u0637\u00a7\u0638\u201e\u0637\u00a8\u0638\u0679\u0637\u00a7\u0638\u2020\u0637\u00a7\u0637\u06be"},
	{ "DatabaseName", 		        "\u0637\u00a7\u0637\u00b3\u0638\u2026 \u0638\u201a\u0637\u00a7\u0637\u00b9\u0637\u00af\u0637\u00a9 \u0637\u00a7\u0638\u201e\u0637\u00a8\u0638\u0679\u0637\u00a7\u0638\u2020\u0637\u00a7\u0637\u06be" },
	{ "DatabasePortInfo", 		"Database Listener Port" },
	{ "DatabasePort", 			"Database Port" },
	{ "DatabaseUserInfo", 		"Database Compiere User ID" },
	{ "DatabaseUser", 			"\u0638\u201a\u0637\u00a7\u0637\u00b9\u0637\u00af\u0637\u00a9 \u0637\u00a8\u0638\u0679\u0637\u00a7\u0638\u2020\u0637\u00a7\u0637\u06be \u0637\u00a7\u0638\u201e\u0638\u2026\u0637\u00b3\u0637\u06be\u0637\u00ae\u0637\u00af\u0638\u2026" },
	{ "DatabasePasswordInfo", 	"Database Compiere User Password" },
	{ "DatabasePassword", 	   	        "\u0638\u0192\u0638\u201e\u0638\u2026\u0637\u00a9 \u0638\u2026\u0637\u00b1\u0638\u02c6\u0637\u00b1 \u0638\u201a\u0637\u00a7\u0637\u00b9\u0637\u00af\u0637\u00a9 \u0637\u00a7\u0638\u201e\u0637\u00a8\u0638\u0679\u0637\u00a7\u0638\u2020\u0637\u00a7\u0637\u06be"},
	{ "TNSNameInfo", 			  "\u0637\u00a5\u0638\u0192\u0637\u06be\u0637\u00b4\u0637\u00a7\u0638\u067e \u0638\u201a\u0637\u00a7\u0637\u00b9\u0637\u00af\u0637\u00a9 \u0637\u00a7\u0638\u201e\u0637\u00a8\u0638\u0679\u0637\u00a7\u0638\u2020\u0637\u00a7\u0637\u06be"},
	{ "TNSName", 				"\u0637\u00a7\u0638\u201e\u0637\u00a8\u0637\u00ad\u0637\u00ab \u0637\u00b9\u0638\u2020 \u0638\u201a\u0637\u00a7\u0637\u00b9\u0637\u00af\u0637\u00a9 \u0637\u00a7\u0638\u201e\u0637\u00a8\u0638\u0679\u0637\u00a7\u0638\u2020\u0637\u00a7\u0637\u06be"},
	{ "SystemPasswordInfo", 	"\u0638\u0192\u0638\u201e\u0638\u2026\u0637\u00a9 \u0638\u2026\u0637\u00b1\u0638\u02c6\u0637\u00b1 \u0637\u00a7\u0638\u201e\u0638\u2026\u0637\u00b3\u0637\u06be\u0637\u00ae\u0637\u00af\u0638\u2026 \u0638\u201e\u0637\u00af\u0638\u2018\u0638\u0688\u0637\u00ae\u0638\u02c6\u0638\u201e \u0637\u00a5\u0638\u201e\u0638\u2030 \u0637\u00a7\u0638\u201e\u0638\u2020\u0638\u2018\u0637\u00b8\u0637\u00a7\u0638\u2026" },
	{ "SystemPassword", 		            "\u0638\u0192\u0638\u201e\u0638\u2026\u0637\u00a9 \u0637\u00b3\u0637\u00b1\u0638\u2018 \u0637\u00a7\u0638\u201e\u0638\u2020\u0638\u2018\u0637\u00b8\u0637\u00a7\u0638\u2026"},
	{ "MailServerInfo", 		             "\u0637\u00a7\u0638\u201e\u0637\u00a8\u0637\u00b1\u0638\u0679\u0637\u00af \u0637\u00a7\u0638\u201e\u0637\u00b1\u0638\u0698\u0637\u00a6\u0638\u06af\u0638\u0679\u0637\u00b3\u0638\u06af\u0638\u0679"},
	{ "MailServer", 		            "\u0637\u00a7\u0638\u201e\u0637\u00a8\u0637\u00b1\u0638\u0679\u0637\u00af \u0637\u00a7\u0638\u201e\u0637\u00b1\u0638\u0698\u0637\u00a6\u0638\u06af\u0638\u0679\u0637\u00b3\u0638\u06af\u0638\u0679" },
	{ "AdminEMailInfo", 		"Compiere Administrator EMail" },
	{ "AdminEMail", 			"\u0637\u00a7\u0638\u201e\u0637\u00a8\u0637\u00b1\u0638\u0679\u0637\u00af \u0637\u00a7\u0638\u201e\u0637\u00a5\u0638\u201e\u0638\u0192\u0637\u06be\u0637\u00b1\u0638\u02c6\u0638\u2020\u0638\u0679\u0638\u2018 \u0637\u00a7\u0638\u201e\u0637\u00b1\u0638\u0698\u0637\u00a6\u0638\u06af\u0638\u0679\u0637\u00b3\u0638\u06af\u0638\u0679" },
	{ "DatabaseServerInfo", 	        "\u0637\u00a7\u0637\u00b3\u0638\u2026 \u0638\u201a\u0637\u00a7\u0637\u00b9\u0637\u00af\u0637\u00a9 \u0637\u00a7\u0638\u201e\u0637\u00a8\u0638\u0679\u0637\u00a7\u0638\u2020\u0637\u00a7\u0637\u06be \u0637\u00a7\u0638\u201e\u0637\u00b1\u0638\u0698\u0637\u00a6\u0638\u06af\u0638\u0679\u0637\u00b3\u0638\u06af\u0638\u0679\u0638\u2018\u0637\u00a9"},
	{ "DatabaseServer", 		          "\u0638\u201a\u0637\u00a7\u0637\u00b9\u0637\u00af\u0637\u00a9 \u0637\u00a7\u0638\u201e\u0637\u00a8\u0638\u0679\u0637\u00a7\u0638\u2020\u0637\u00a7\u0637\u06be \u0637\u00a7\u0638\u201e\u0637\u00b1\u0638\u0698\u0637\u00a6\u0638\u06af\u0638\u0679\u0637\u00b3\u0638\u06af\u0638\u0679\u0638\u2018\u0637\u00a9" },
	{ "JavaHomeInfo", 			"Java Home Folder" },
	{ "JavaHome", 				"Java Home" },
	{ "JNPPortInfo", 			"Application Server JNP Port" },
	{ "JNPPort", 				"JNP Port" },
	{ "MailUserInfo", 			"Compiere Mail User" },
	{ "MailUser", 				     " \u0637\u00a8\u0637\u00b1\u0638\u0679\u0637\u00af \u0637\u00a7\u0638\u201e\u0638\u2026\u0637\u00b3\u0637\u06be\u0637\u00ae\u0637\u00af\u0638\u2026" },
	{ "MailPasswordInfo", 		"Compiere Mail User Password" },
	{ "MailPassword", 			"\u0638\u0192\u0638\u201e\u0638\u2026\u0637\u00a9 \u0637\u00b3\u0637\u00b1\u0638\u2018 \u0637\u00a7\u0638\u201e\u0637\u00a8\u0637\u00b1\u0638\u0679\u0637\u00af" },
	{ "KeyStorePassword",		"KeyStore Password" },
	{ "KeyStorePasswordInfo",	"Password for SSL Key Store" },
	//
	{ "JavaType",				"Java VM"},
	{ "JavaTypeInfo",			"Java VM Vendor"},
	{ "AppsType",				"Server Type"},
	{ "AppsTypeInfo",			"J2EE Application Server Type"},
	{ "DeployDir",				"\u0637\u00a7\u0638\u201e\u0638\u2020\u0638\u2018\u0637\u00b4\u0637\u00b1"},
	{ "DeployDirInfo",			"J2EE Deployment Directory"},
	{ "ErrorDeployDir",			 "\u0637\u00ae\u0637\u00b7\u0637\u00a5 \u0638\u067e\u0638\u0679 \u0637\u00af\u0638\u201e\u0638\u0679\u0638\u201e \u0637\u00a7\u0638\u201e\u0638\u2020\u0637\u00b4\u0637\u00b1"},
	//
	{ "TestInfo", 				"\u0637\u00a7\u0637\u00ae\u0637\u06be\u0637\u00a8\u0637\u00b1 \u0637\u00a7\u0638\u201e\u0638\u2020\u0638\u2018\u0637\u00b8\u0637\u00a7\u0638\u2026" },
	{ "Test", 					"\u0637\u00a7\u0637\u00ae\u0637\u06be\u0637\u00a8\u0637\u00a7\u0637\u00b1" },
	{ "SaveInfo", 				     "\u0637\u00ad\u0638\u0698\u0638\u067e\u0638\u2019\u0637\u00b6 \u0637\u00a7\u0638\u201e\u0638\u2020\u0638\u2018\u0637\u00b8\u0637\u00a7\u0638\u2026" },
	{ "Save", 					"\u0637\u00ad\u0638\u0698\u0638\u067e\u0638\u2019\u0637\u00b6" },
	{ "HelpInfo", 				"\u0637\u00a7\u0637\u00ad\u0637\u00b5\u0638\u201e \u0637\u00b9\u0638\u201e\u0638\u2030 \u0637\u00a7\u0638\u201e\u0638\u2026\u0637\u00b3\u0637\u00a7\u0637\u00b9\u0637\u00af\u0637\u00a9" },
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
	{ "ErrorMail", 				"\u0637\u00ae\u0637\u00b7\u0637\u00a5 \u0638\u067e\u0638\u0679 \u0637\u00a7\u0638\u201e\u0637\u00a8\u0637\u00b1\u0638\u0679\u0637\u00af" },
	{ "ErrorSave", 				"\u0637\u00ae\u0637\u00b7\u0637\u00a5 \u0638\u067e\u0638\u0679 \u0637\u00ad\u0638\u0698\u0638\u067e\u0638\u2019\u0637\u00b6 \u0637\u00a7\u0638\u201e\u0638\u2026\u0638\u201e\u0638\u067e\u0638\u2018 " },

	{ "EnvironmentSaved", 		"Environment file saved .... starting Deployment\n"
		+ "You can re-start the Application Server after program completes.\n"
		+ "Please check Trace for errors.\n" }

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
