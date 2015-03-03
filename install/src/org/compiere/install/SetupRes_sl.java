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
 * 	@author 	Matjaž Godec
 * 	@version 	$Id: SetupRes_sl.java,v 1.4 2005/03/11 20:30:23 jjanke Exp $
 */
public class SetupRes_sl extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", 	"Namestavitve Compiere strežnika" },
	{ "Ok", 			"V redu" },
	{ "File", 			"Datoteka" },
	{ "Exit", 			"Izhod" },
	{ "Help", 			"Pomo�?" },
	{ "PleaseCheck", 		"Prosim preverite" },
	{ "UnableToConnect", 		"Napaka pri povezavi na Compiere web pomo�?" },
	//
	{ "CompiereHomeInfo", 		"Compiere Home je glavni imenik" },
	{ "CompiereHome", 		"Compiere Home" },
	{ "WebPortInfo", 		"Web (HTML) vrata" },
	{ "WebPort", 			"Web vrata" },
	{ "AppsServerInfo", 		"Ime programskega strežnika" },
	{ "AppsServer", 		"Programski strežnik" },
	{ "DatabaseTypeInfo", 		"Tip baze podatkov" },
	{ "DatabaseType", 		"Tip baze podatakov" },
	{ "DatabaseNameInfo", 		"Ime baze podatkov " },
	{ "DatabaseName", 		"Ime baze (SID)" },
	{ "DatabasePortInfo", 		"Vrata Listener programa" },
	{ "DatabasePort", 		"Vrata baze podatkov" },
	{ "DatabaseUserInfo", 		"Uporabniško ime Compiere baze podatkov" },
	{ "DatabaseUser", 		"Uporabnik baze podatkov" },
	{ "DatabasePasswordInfo", 	"Geslo uporabnika baze podatkov" },
	{ "DatabasePassword", 		"Geslo baze podatkov" },
	{ "TNSNameInfo", 		"TNS ali globalno ime baze podatkov" },
	{ "TNSName", 			"TNS Ime" },
	{ "SystemPasswordInfo", 	"Geslo uporabnika System" },
	{ "SystemPassword", 		"System geslo" },
	{ "MailServerInfo", 		"Strežnik elektronske pošte" },
	{ "MailServer", 		"Strežnik elektronske pošte" },
	{ "AdminEMailInfo", 		"Elektronski naslov Compiere Skrbnika" },
	{ "AdminEMail", 		"Elektronski naslov Skrbnika" },
	{ "DatabaseServerInfo", 	"Ime strežnika baze podatkov" },
	{ "DatabaseServer", 		"Strežnik baze podatkov" },
	{ "JavaHomeInfo", 		"Doma�? imenik Jave" },
	{ "JavaHome", 			"Java imenik" },
	{ "JNPPortInfo", 		"JNP vrata programskega strežnika" },
	{ "JNPPort", 			"JNP vrata" },
	{ "MailUserInfo", 		"Uporabnik elektronske pošte za Compiere" },
	{ "MailUser", 			"Uporabnik elektronske pošte" },
	{ "MailPasswordInfo", 		"Geslo uporabnika elektronske pošte Compiere" },
	{ "MailPassword", 		"Geslo uporabnika elektronske pošte" },
	{ "KeyStorePassword",		"Geslo shrambe klju�?ev" },
	{ "KeyStorePasswordInfo",	"Geslo za shrambo SSL klju�?ev" },
	//
	{ "JavaType",				"Java VM"},
	{ "JavaTypeInfo",			"Java VM Vendor"},
	{ "AppsType",				"Server Type"},
	{ "AppsTypeInfo",			"J2EE Application Server Type"},
	{ "DeployDir",				"Deployment"},
	{ "DeployDirInfo",			"J2EE Deployment Directory"},
	{ "ErrorDeployDir",			"Error Deployment Directory"},
	//
	{ "TestInfo", 			"Test informacije" },
	{ "Test", 			"Test" },
	{ "SaveInfo", 			"Shrani informacije" },
	{ "Save", 			"Shrani" },
	{ "HelpInfo", 			"Pomo�?" },
	//
	{ "ServerError", 		"Napaka v nastavitvah programskega strežnika" },
	{ "ErrorJavaHome", 		"Error napa�?en doma�? imenik Java" },
	{ "ErrorCompiereHome", 		"Error napa�?en Compiere Home imenik" },
	{ "ErrorAppsServer", 		"Error programski strežnik (ne uporabljaj imena localhost)" },
	{ "ErrorWebPort", 		"Error napa�?na Web vrata" },
	{ "ErrorJNPPort", 		"Error napa�?na JNP vrata" },
	{ "ErrorDatabaseServer", 	"Error strežnik baze podatkov (ne uporabljaj imena localhost)" },
	{ "ErrorDatabasePort", 		"Error napa�?na vrata baze podatkov" },
	{ "ErrorJDBC", 			"Error napaka v JDBC povezavi" },
	{ "ErrorTNS", 			"Error napaka v TNS povezavi" },
	{ "ErrorMailServer", 		"Error strežnik elektronske pošte (ne uporabljaj imena localhost)" },
	{ "ErrorMail", 			"Error napaka elektronska pošta" },
	{ "ErrorSave", 			"Error napaka pri shranjevanju datoteke" },

	{ "EnvironmentSaved", 		"Nastavitve so shranjene\nSedaj lahko poženete programski strežnik." }

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
