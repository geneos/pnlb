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
 *	Swedish Setup Resource Translation
 *
 * 	@author 	Thomas Dilts
 * 	@version 	$Id: SetupRes_sv.java,v 1.6 2005/03/11 20:30:21 jjanke Exp $
 */
public class SetupRes_sv extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", "Compiere server installationsprogram" },
	{ "Ok", 				"Ok" },
	{ "File", 				"Fil" },
	{ "Exit", 				"Avsluta" },
	{ "Help", 				"Hj�lp" },
	{ "PleaseCheck", 		"Kolla" },
	{ "UnableToConnect",	"Kan inte f� hj�lp fr�n Compiere Web Site" },

	{ "CompiereHomeInfo", 	"Compiere hem �r huvudkatalog" },
	{ "CompiereHome", 		"Compiere hem" },
	{ "WebPortInfo", 		"Web (HTML) port" },
	{ "WebPort", 			"Web port" },
	{ "AppsServerInfo", 	"Program server name" },
	{ "AppsServer", 		"Program server" },
	{ "DatabaseTypeInfo", 	"Databastyp" },
	{ "DatabaseType", 		"Databastyp" },
	{ "DatabaseNameInfo", 	"Databas namn " },
	{ "DatabaseName", 		"Databas namn (SID)" },
	{ "DatabasePortInfo", 	"Databas avlyssningsport" },
	{ "DatabasePort", 		"Databas port" },
	{ "DatabaseUserInfo", 	"Databas Compiere anv�ndarnamn" },
	{ "DatabaseUser", 		"Databas anv�ndarnamn" },
	{ "DatabasePasswordInfo", "Databas Compiere anv�ndare l�senord" },
	{ "DatabasePassword", 	"Databas l�senord" },
	{ "TNSNameInfo", 		"TNS eller global databas namn" },
	{ "TNSName", 			"TNS namn" },
	{ "SystemPasswordInfo", "System anv�ndare l�senord" },
	{ "SystemPassword", 	"System l�senord" },
	{ "MailServerInfo", 	"Post server" },
	{ "MailServer", 		"Post server" },
	{ "AdminEMailInfo", 	"Compiere administrat�r e-post" },
	{ "AdminEMail", 		"Admin e-post" },
	{ "DatabaseServerInfo", "Databas server namn" },
	{ "DatabaseServer", 	"Databas server" },
	{ "JavaHomeInfo", 		"Java hemkatalog" },
	{ "JavaHome", 			"Java hem" },
	{ "JNPPortInfo", 		"Program server JNP port" },
	{ "JNPPort", 			"JNP port" },
	{ "MailUserInfo", 		"Compiere post anv�ndare" },
	{ "MailUser", 			"Post anv�ndare" },
	{ "MailPasswordInfo", 	"Compiere post anv�ndare l�senord" },
	{ "MailPassword", 		"Post l�senord" },
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
	{ "TestInfo", 			"Testa inst�llningar" },
	{ "Test", 				"Testa" },
	{ "SaveInfo", 			"Spara inst�llningar" },
	{ "Save", 				"Spara" },
	{ "HelpInfo", 			"Hj�lp" },

	{ "ServerError", 		"Server inst�llningsfel" },
	{ "ErrorJavaHome", 		"Fel Java hem" },
	{ "ErrorCompiereHome", 	"Fel Compiere hem" },
	{ "ErrorAppsServer", 	"Fel program server (anv�nd ej localhost)" },
	{ "ErrorWebPort", 		"Fel web port" },
	{ "ErrorJNPPort", 		"Fel JNP port" },
	{ "ErrorDatabaseServer", "Fel databas server (anv�nd ej localhost)" },
	{ "ErrorDatabasePort", 	"Fel databas port" },
	{ "ErrorJDBC", 			"Fel JDBC anslutning" },
	{ "ErrorTNS", 			"Fel TNS anslutning" },
	{ "ErrorMailServer", 	"Fel post server (anv�nd ej localhost)" },
	{ "ErrorMail", 			"Fel post" },
	{ "ErrorSave", 			"Fel swing fil" },

	{ "EnvironmentSaved",	"Inst�llningar sparad\nDu m�ste starta om servern." },
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
