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
 * 	@author 	Marek Mosiewicz <marek.mosiewicz@jotel.com.pl>
 * 	@version 	$Id: SetupRes_pl.java,v 1.7 2005/03/11 20:30:21 jjanke Exp $
 */
public class SetupRes_pl extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", "Konfiguracja serwera Compiere" },
	{ "Ok", 				"Ok" },
	{ "File", 				"Plik" },
	{ "Exit", 				"Wyj\u015bcie" },
	{ "Help", 				"Pomoc" },
	{ "PleaseCheck", 		"Prosz\u0119 sprawdzi\u0107" },
	{ "UnableToConnect",	"Nie mo\u017cna po\u0142\u0105czy\u0107 si\u0119 ze stron\u0105 Compiere w celu uzyskania pomocy" },

	{ "CompiereHomeInfo", 	"Folder Compiere jest folderem g\u0142\u00f3wnym" },
	{ "CompiereHome", 		"Folder Compiere" },
	{ "WebPortInfo", 		"Web (HTML) Port" },
	{ "WebPort", 			"Web Port" },
	{ "AppsServerInfo", 	"Nazwa serwera aplikacji" },
	{ "AppsServer", 		"Serwer bazy danych" },
	{ "DatabaseTypeInfo", 	"Typ bazy danych" },
	{ "DatabaseType", 		"Typ bazy danych" },
	{ "DatabaseNameInfo", 	"Nazwa bazy danych " },
	{ "DatabaseName", 		"Nazwa bazy danych (SID)" },
	{ "DatabasePortInfo", 	"Port listenera bazy danych" },
	{ "DatabasePort", 		"Port bazy danych" },
	{ "DatabaseUserInfo", 	"U\u017cytkownik Compiere w bazie danych" },
	{ "DatabaseUser", 		"U\u017cytkownik bazy" },
	{ "DatabasePasswordInfo", "Has\u0142o u\u017cytkownika Compiere" },
	{ "DatabasePassword", 	"Has\u0142o u\u017cytkownika" },
	{ "TNSNameInfo", 		"TNS lub Globalna Nazwa Bazy (dla Oracle)" },
	{ "TNSName", 			"Nazwa TNS" },
	{ "SystemPasswordInfo", "Has\u0142o dla u\u017cytkownika System w bazie danych" },
	{ "SystemPassword", 	"Has\u0142o System" },
	{ "MailServerInfo", 	"Serwer pocztowy" },
	{ "MailServer", 		"Serwer pocztowy" },
	{ "AdminEMailInfo", 	"Adres email administartora Compiere" },
	{ "AdminEMail", 		"EMail administ." },
	{ "DatabaseServerInfo", "Nazwa serwera bazy danych" },
	{ "DatabaseServer", 	"Serwer bazy danych" },
	{ "JavaHomeInfo", 		"Folder Javy" },
	{ "JavaHome", 			"Folder Javy" },
	{ "JNPPortInfo", 		"Application Server JNP Port" },
	{ "JNPPort", 			"JNP Port" },
	{ "MailUserInfo", 		"U\u017cytkownik poczty dla cel\u00f3w administracyjnych Compiere" },
	{ "MailUser", 			"U\u017cytkownik poczty" },
	{ "MailPasswordInfo", 	"Has\u0142o dla konta pocztowego Compiere" },
	{ "MailPassword", 		"Has\u0142o poczty" },
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
	{ "TestInfo", 			"Sprawd\u017a ustawienia" },
	{ "Test", 				"Testuj" },
	{ "SaveInfo", 			"Zapisz ustawienia" },
	{ "Save", 				"Zapisz" },
	{ "HelpInfo", 			"Pomoc" },

	{ "ServerError", 		"B\u0142\u0119dne ustawienia" },
	{ "ErrorJavaHome", 		"Niepoprawny folder Javy" },
	{ "ErrorCompiereHome", 	"Nie stwierdzono zainstalowanego systemu Compiere w miescu wskazanym jako Folder Compiere" },
	{ "ErrorAppsServer", 	"Niepoprawny serwer aplikacji (nie mo\u017ce by\u0107 localhost)" },
	{ "ErrorWebPort", 		"Niepoprawny port WWW (by\u0107 mo\u017ce inna aplikacja u\u017cywa ju\u017c tego portu)" },
	{ "ErrorJNPPort", 		"Niepoprawny port JNP (by\u0107 mo\u017ce inna aplikacja u\u017cywa ju\u017c tego portu)" },
	{ "ErrorDatabaseServer", "Niepoprawny serwer bazy (nie mo\u017ce by\u0107 localhost)" },
	{ "ErrorDatabasePort", 	"Niepoprawny port serwer bazy" },
	{ "ErrorJDBC", 			"Wyst\u0105pi\u0142 b\u0142\u0105d przy pr\u00f3bie po\u0142\u0105cznia si\u0119 z baz\u0105 danych" },
	{ "ErrorTNS", 			"Wyst\u0105pi\u0142 b\u0142\u0105d przy pr\u00f3bie po\u0142\u0105cznia si\u0119 z baz\u0105 danych poprzez TNS" },
	{ "ErrorMailServer", 	"Niepoprawny serwer pocztowy (nie mo\u017ce by\u0107 localhost)" },
	{ "ErrorMail", 			"B\u0142\u0105d poczty" },
	{ "ErrorSave", 			"B\u0142\u0105d przy zapisywaniu konfiguracji" },

	{ "EnvironmentSaved",	"Ustawienia zapisany\nMusisz ponownie uruchomi\u0107 serwer." },
	{ "RMIoverHTTP", 		"Tunelowanie RMI over HTTP" },
	{ "RMIoverHTTPInfo", 	"Tunelowanie RMI over HTTP pozwala u\u017cywa\u0107 Compiere przez firewall" }
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
