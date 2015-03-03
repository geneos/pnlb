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
	{ "Ok", 					"Hyv�ksy" },
	{ "File", 					"Tiedosto" },
	{ "Exit", 					"Poistu" },
	{ "Help", 					"Help" },
	{ "PleaseCheck", 			"Ole hyv� ja valitse" },
	{ "UnableToConnect", 		"Yhteydenotto Compieren Web-Help:in ei onnistu" },
	//
	{ "CompiereHomeInfo", 		"Compiere Home on p��kansio" },
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
	{ "DatabaseUserInfo", 		"Tietokannan Compiere-k�ytt�j�tunnus" },
	{ "DatabaseUser", 			"Tietokannan k�ytt�j�tunnus" },
	{ "DatabasePasswordInfo", 	"Tietokannan Compiere-salasana" },
	{ "DatabasePassword", 		"Tietokannan salasana" },
	{ "TNSNameInfo", 			"TNS tai Globaali Tietokannan Nimi" },
	{ "TNSName", 				"TNS Nimi" },
	{ "SystemPasswordInfo", 	"J�rjestelm�salasana" },
	{ "SystemPassword", 		"J�rjestelm�salasana" },
	{ "MailServerInfo", 		"S�hk�postipalvelin" },
	{ "MailServer", 			"S�hk�postipalvelin" },
	{ "AdminEMailInfo", 		"Compiere-yll�pit�j�n S�hk�posti" },
	{ "AdminEMail", 			"Yll�pit�j�n S�hk�posti" },
	{ "DatabaseServerInfo", 	"Tietokantapalvelimen Nimi" },
	{ "DatabaseServer", 		"Tietokantapalvelin" },
	{ "JavaHomeInfo", 			"Java-kotihakemisto" },
	{ "JavaHome", 				"Java-koti" },
	{ "JNPPortInfo", 			"Sovelluspalvelimen JNP-portti" },
	{ "JNPPort", 				"JNP-portti" },
	{ "MailUserInfo", 			"Compiere-s�hk�postik�ytt�j�" },
	{ "MailUser", 				"S�hk�postik�ytt�j�" },
	{ "MailPasswordInfo", 		"Compiere-s�hk�postisalasana" },
	{ "MailPassword", 			"S�hk�postisalasana" },
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
	{ "ErrorAppsServer", 		"Sovelluspalvelinvirhe (�l� k�yt� paikallisverkkoasemaa)" },
	{ "ErrorWebPort", 			"Web-porttivirhe" },
	{ "ErrorJNPPort", 			"JNP-porttivirhe" },
	{ "ErrorDatabaseServer", 	"Tietokantapalvelinvirhe (�l� k�yt� paikallisverkkoasemaa)" },
	{ "ErrorDatabasePort", 		"Tietokantaporttivirhe" },
	{ "ErrorJDBC", 				"JDBC-yhteysvirhe" },
	{ "ErrorTNS", 				"TNS-yhteysvirhe" },
	{ "ErrorMailServer", 		"S�hk�postipalvelinvirhe (�l� k�yt� paikallisverkkoasemaa)" },
	{ "ErrorMail", 				"S�hk�postivirhe" },
	{ "ErrorSave", 				"Tiedostontallennusvirhe" },

	{ "EnvironmentSaved", 		"Ymp�rist� tallennettu/Palvelin t�ytyy k�ynnist�� uudelleen." },

	{ "RMIoverHTTP", 			"Tunneloi objektit HTTP kautta" },
	{ "RMIoverHTTPInfo", 		"RMI HTTP:n yli mahdollistaa palomuurien l�p�isyn" }
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
