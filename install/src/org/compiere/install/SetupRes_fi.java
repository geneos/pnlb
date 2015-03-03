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
 *	Setup Resources for Finnish language
 *
 * 	@author 	Petteri Soininen (petteri.soininen@netorek.fi)
 * 	@version 	$Id: SetupRes_fi.java,v 1.5 2005/03/11 20:30:21 jjanke Exp $
 */
public class SetupRes_fi extends ListResourceBundle
{
	/**	
    * Translation Info
    */
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", 	"Compiere-palvelimen Asetukset" },
	{ "Ok", 					"Hyväksy" },
	{ "File", 					"Tiedosto" },
	{ "Exit", 					"Poistu" },
	{ "Help", 					"Help" },
	{ "PleaseCheck", 			"Ole hyvä ja valitse" },
	{ "UnableToConnect", 		"Yhteydenotto Compieren Web-Help:in ei onnistu" },
	//
	{ "CompiereHomeInfo", 		"Compiere Home on pääkansio" },
	{ "CompiereHome", 			"Compiere Home" },
	{ "WebPortInfo", 			"Web (HTML) Portti" },
	{ "WebPort", 				"Web Portti" },
	{ "AppsServerInfo", 		"Sovelluspalvelimen Nimi" },
	{ "AppsServer", 			"Sovelluspalvelin" },
	{ "DatabaseTypeInfo", 		"Tietokantatyyppi" },
	{ "DatabaseType", 			"Tietokantatyyppi" },
	{ "DatabaseNameInfo", 		"Tietokannan Nimi" },
	{ "DatabaseName", 			"Tietokannan Nimi (SID)" },
	{ "DatabasePortInfo", 		"Tietokannan kuuntelijaportti" },
	{ "DatabasePort", 			"Tietokantaportti" },
	{ "DatabaseUserInfo", 		"Tietokannan Compiere-käyttäjätunnus" },
	{ "DatabaseUser", 			"Tietokannan käyttäjätunnus" },
	{ "DatabasePasswordInfo", 	"Tietokannan Compiere-salasana" },
	{ "DatabasePassword", 		"Tietokannan salasana" },
	{ "TNSNameInfo", 			"TNS tai Globaali Tietokannan Nimi" },
	{ "TNSName", 				"TNS Nimi" },
	{ "SystemPasswordInfo", 	"Järjestelmäsalasana" },
	{ "SystemPassword", 		"Järjestelmäsalasana" },
	{ "MailServerInfo", 		"Sähköpostipalvelin" },
	{ "MailServer", 			"Sähköpostipalvelin" },
	{ "AdminEMailInfo", 		"Compiere-ylläpitäjän Sähköposti" },
	{ "AdminEMail", 			"Ylläpitäjän Sähköposti" },
	{ "DatabaseServerInfo", 	"Tietokantapalvelimen Nimi" },
	{ "DatabaseServer", 		"Tietokantapalvelin" },
	{ "JavaHomeInfo", 			"Java-kotihakemisto" },
	{ "JavaHome", 				"Java-koti" },
	{ "JNPPortInfo", 			"Sovelluspalvelimen JNP-portti" },
	{ "JNPPort", 				"JNP-portti" },
	{ "MailUserInfo", 			"Compiere-sähköpostikäyttäjä" },
	{ "MailUser", 				"Sähköpostikäyttäjä" },
	{ "MailPasswordInfo", 		"Compiere-sähköpostisalasana" },
	{ "MailPassword", 			"Sähköpostisalasana" },
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
	{ "TestInfo", 				"Testaa Asetukset" },
	{ "Test", 					"Testaa" },
	{ "SaveInfo", 				"Tallenna Asetukset" },
	{ "Save", 					"Tallenna" },
	{ "HelpInfo", 				"Hae Apua" },
	//
	{ "ServerError", 			"Palvelimen Asetusvirhe" },
	{ "ErrorJavaHome", 			"Java-kotivirhe" },
	{ "ErrorCompiereHome", 		"Compiere-kotivirhe" },
	{ "ErrorAppsServer", 		"Sovelluspalvelinvirhe (älä käytä paikallisverkkoasemaa)" },
	{ "ErrorWebPort", 			"Web-porttivirhe" },
	{ "ErrorJNPPort", 			"JNP-porttivirhe" },
	{ "ErrorDatabaseServer", 	"Tietokantapalvelinvirhe (älä käytä paikallisverkkoasemaa)" },
	{ "ErrorDatabasePort", 		"Tietokantaporttivirhe" },
	{ "ErrorJDBC", 				"JDBC-yhteysvirhe" },
	{ "ErrorTNS", 				"TNS-yhteysvirhe" },
	{ "ErrorMailServer", 		"Sähköpostipalvelinvirhe (älä käytä paikallisverkkoasemaa)" },
	{ "ErrorMail", 				"Sähköpostivirhe" },
	{ "ErrorSave", 				"Tiedostontallennusvirhe" },

	{ "EnvironmentSaved", 		"Ympäristö tallennettu/Palvelin täytyy käynnistää uudelleen." },

	{ "RMIoverHTTP", 			"Tunneloi objektit HTTP kautta" },
	{ "RMIoverHTTPInfo", 		"RMI HTTP:n yli mahdollistaa palomuurien läpäisyn" }
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
