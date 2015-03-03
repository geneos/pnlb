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
 * 	@translator 	Jaume Teixi
 * 	@version 	$Id: SetupRes_ca.java,v 1.6 2005/03/11 20:30:21 jjanke Exp $
 */
public class SetupRes_ca extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", "Configuració Servidor Compiere" },
	{ "Ok", 				"D'Acord" },
	{ "File", 				"Fitxer" },
	{ "Exit", 				"Sortir" },
	{ "Help", 				"Ajuda" },
	{ "PleaseCheck", 		"Sisplau Comproveu" },
	{ "UnableToConnect",	"No s'ha pogut obtenir l'ajuda de la web del Compiere" },

	{ "CompiereHomeInfo", 	"Compiere Home és la Carpeta Principal" },
	{ "CompiereHome", 		"Compiere Home" },
	{ "WebPortInfo", 		"Web (HTML) Port" },
	{ "WebPort", 			"Web Port" },
	{ "AppsServerInfo", 	"Nom Servidor Aplicació" },
	{ "AppsServer", 		"Servidor Aplicació" },
	{ "DatabaseTypeInfo", 	"Tipus Base de Dades" },
	{ "DatabaseType", 		"Tipus Base de Dades" },
	{ "DatabaseNameInfo", 	"Nom Base de Dades" },
	{ "DatabaseName", 		"Nom Base de Dades (SID)" },
	{ "DatabasePortInfo", 	"Port Listener Base de Dades" },
	{ "DatabasePort", 		"Port Base de Dades" },
	{ "DatabaseUserInfo", 	"ID Usuari Compiere Base de Dades" },
	{ "DatabaseUser", 		"Usuari Base de Dades" },
	{ "DatabasePasswordInfo", "Contrasenya Usuari Compiere Base de Dades" },
	{ "DatabasePassword", 	"Contrasenya Base de Dades" },
	{ "TNSNameInfo", 		"TNS o Nom Global Base de Dades" },
	{ "TNSName", 			"Nom TNS" },
	{ "SystemPasswordInfo", "Contrasenya Usuari System" },
	{ "SystemPassword", 	"Contrasenya System" },
	{ "MailServerInfo", 	"Servidor Correu" },
	{ "MailServer", 		"Servidor Correu" },
	{ "AdminEMailInfo", 	"Email Administrador Compiere" },
	{ "AdminEMail", 		"Email Admin" },
	{ "DatabaseServerInfo", "Nom Servidor Base de Dades" },
	{ "DatabaseServer", 	"Servidor Base de Dades" },
	{ "JavaHomeInfo", 		"Carpeta Java Home" },
	{ "JavaHome", 			"Java Home" },
	{ "JNPPortInfo", 		"Port JNP Servidor Aplicació" },
	{ "JNPPort", 			"Port JNP" },
	{ "MailUserInfo", 		"Usuari Correu Compiere" },
	{ "MailUser", 			"Usuari Correu" },
	{ "MailPasswordInfo", 	"Contrasenya Usuari Correu Compiere" },
	{ "MailPassword", 		"Contrasenya Correu" },
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
	{ "TestInfo", 			"Provar Configuració" },
	{ "Test", 				"Provar" },
	{ "SaveInfo", 			"Guardar Configuració" },
	{ "Save", 				"Guardar" },
	{ "HelpInfo", 			"Obtenir Ajuda" },

	{ "ServerError", 		"Error Configuració Servidor" },
	{ "ErrorJavaHome", 		"Error Java Home" },
	{ "ErrorCompiereHome", 	"Error Compiere Home" },
	{ "ErrorAppsServer", 	"Error Servidor Aplicació (no emprar localhost)" },
	{ "ErrorWebPort", 		"Error Port Web" },
	{ "ErrorJNPPort", 		"Error Port JNP" },
	{ "ErrorDatabaseServer", "Error Servidor Base de Dades (no emprar localhost)" },
	{ "ErrorDatabasePort", 	"Error Port Base de Dades" },
	{ "ErrorJDBC", 			"Error Connexió JDBC" },
	{ "ErrorTNS", 			"Error Connexió TNS" },
	{ "ErrorMailServer", 	"Error Servidor Correu (no emprar localhost)" },
	{ "ErrorMail", 			"Error Correu" },
	{ "ErrorSave", 			"Error Guardant Fitxer" },

	{ "EnvironmentSaved",	"Entorn Guardat\nCal reiniciar el servidor." },
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
