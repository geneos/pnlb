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
 *	Vietnamese Setup Resources
 *
 * 	@author 	Bui Chi Trung
 * 	@version 	$Id: SetupRes_vi.java,v 1.6 2005/03/11 20:30:21 jjanke Exp $
 */
public class SetupRes_vi extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", "Setup m�y ch\u1EE7 C\u0103mpia-\u01A1" },
	{ "Ok", 				"\u0110\u1ED3ng �" },
	{ "File", 				"T\u1EC7p" },
	{ "Exit", 				"Tho�t" },
	{ "Help", 				"Gi�p \u0111\u1EE1" },
	{ "PleaseCheck", 		"Vui l�ng ki\u1EC3m tra" },
	{ "UnableToConnect",	"Kh�ng th\u1EC3 t�m th\u1EA5y h\u1ED7 tr\u1EE3 t\u1EEB trang WEB C\u0103mpia-\u01A1" },

	{ "CompiereHomeInfo", 	"Th\u01B0 m\u1EE5c ch\u1EE9a C\u0103mpia-\u01A1" },
	{ "CompiereHome", 	"Th\u01B0 m\u1EE5c ch\u1EE9a C\u0103mpia-\u01A1" },
	{ "WebPortInfo", 	"C\u1ED5ng Web (HTML)" },
	{ "WebPort", 			"Web C\u1ED5ng" },
	{ "AppsServerInfo", 	"T�n m�y ch\u1EE7 ch\u1EA1y \u1EE9ng d\u1EE5ng" },
	{ "AppsServer", 		"M�y ch\u1EE7 c�c \u1EE9ng d\u1EE5ng" },
	{ "DatabaseTypeInfo", 	"Lo\u1EA1i CSDL" },
	{ "DatabaseType", 		"Lo\u1EA1i CSDL" },
	{ "DatabaseNameInfo", 	"T�n CSDL " },
	{ "DatabaseName", 		"T�n CSDL (SID)" },
	{ "DatabasePortInfo", 	"C\u1ED5ng nghe CSDL" },
	{ "DatabasePort", 		"C\u1ED5ng CSDL" },
	{ "DatabaseUserInfo", 	"Ng\u01B0\u1EDDi d�ng CSDL C\u0103mpia-\u01A1" },
	{ "DatabaseUser", 		"Ng\u01B0\u1EDDi d�ng CSDL" },
	{ "DatabasePasswordInfo", "M\u1EADt kh\u1EA9u d�ng CSDL C\u0103mpia-\u01A1" },
	{ "DatabasePassword", 	"M\u1EADt kh\u1EA9u CSDL" },
	{ "TNSNameInfo", 		"TNS ho\u1EB7c Global Database Name" },
	{ "TNSName", 			"T�n theo TNS" },
	{ "SystemPasswordInfo", "M\u1EADt kh\u1EA9u c\u1EE7a ng\u01B0\u1EDDi d�ng H\u1EC7 th\u1ED1ng" },
	{ "SystemPassword", 	"M\u1EADt kh\u1EA9u c\u1EE7a ng\u01B0\u1EDDi d�ng H\u1EC7 th\u1ED1ng" },
	{ "MailServerInfo", 	"M�y ch\u1EE7 th\u01B0 \u0111i\u1EC7n t\u1EED" },
	{ "MailServer", 	"M�y ch\u1EE7 th\u01B0 \u0111i\u1EC7n t\u1EED" },
	{ "AdminEMailInfo", 	"Email c\u1EE7a ng\u01B0\u1EDDi qu\u1EA3n tr\u1ECB C\u0103mpia-\u01A1" },
	{ "AdminEMail", 		"Email c\u1EE7a ng\u01B0\u1EDDi qu\u1EA3n tr\u1ECB" },
	{ "DatabaseServerInfo", "T�n m�y ch\u1EE7 CSDL" },
	{ "DatabaseServer", 	"M�y ch\u1EE7 CSDL" },
	{ "JavaHomeInfo", 		"Th\u01B0 m\u1EE5c ch\u1EE9a Java" },
	{ "JavaHome", 			"Th\u01B0 m\u1EE5c ch\u1EE9a Java" },
	{ "JNPPortInfo", 		"C\u1ED5ng JNP c\u1EE7a m�y ch\u1EE7 \u1EE9ng d\u1EE5ng" },
	{ "JNPPort", 			"C\u1ED5ng JNP" },
	{ "MailUserInfo", 		"Ng\u01B0\u1EDDi d�ng th\u01B0 \u0111i\u1EC7n t\u1EED" },
	{ "MailUser", 			"Ng\u01B0\u1EDDi d�ng th\u01B0 \u0111i\u1EC7n t\u1EED" },
	{ "MailPasswordInfo", 	"M\u1EADt kh\u1EA9u c\u1EE7a ng\u01B0\u1EDDi d�ng th\u01B0 \u0111i\u1EC7n t\u1EED" },
	{ "MailPassword", 		"M\u1EADt kh\u1EA9u c\u1EE7a ng\u01B0\u1EDDi d�ng th\u01B0 \u0111i\u1EC7n t\u1EED" },
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
	{ "TestInfo", 	"Th\u1EED nghi\u1EC7m c�c c\u1EA5u h�nh n�y" },
	{ "Test", 				"Th\u1EED nghi\u1EC7m" },
	{ "SaveInfo", 			"L\u01B0u c�c c\u1EA5u h�nh n�y" },
	{ "Save", 				"L\u01B0u" },
	{ "HelpInfo", 			"Nh\u1EADn gi�p \u0111\u1EE1" },

	{ "ServerError", 		"SL\u1ED7i khi c�i \u0111\u1EB7t ph\u1EA7n m\u1EC1m n�y \u1EDF m�y ch\u1EE7" },
	{ "ErrorJavaHome", 		"T�n th\u01B0 m\u1EE5c ch\u1EE9a Java kh�ng \u0111�ng" },
	{ "ErrorCompiereHome", 	"T�n th\u01B0 m\u1EE5c ch\u1EE9a C\u0103mpia-\u01A1 kh�ng \u0111�ng" },
	{ "ErrorAppsServer", 	"L\u1ED7i li�n quan \u0111\u1EBFn m�y ch\u1EE7 \u1EE9ng d\u1EE5ng (\u0111\u1EEBng d�ng localhost)" },
	{ "ErrorWebPort", 		"C\u1ED5ng Web kh�ng \u0111�ng" },
	{ "ErrorJNPPort", 		"C\u1ED5ng JNP kh�ng \u0111�ng" },
	{ "ErrorDatabaseServer", "L\u1ED7i li�n quan \u0111\u1EBFn m�y ch\u1EE7 CSDL (\u0111\u1EEBng d�ng localhost)" },
	{ "ErrorDatabasePort", 	"C\u1ED5ng CSDL kh�ng \u0111�ng" },
	{ "ErrorJDBC", 			"L\u1ED7i li�n quan \u0111\u1EBFn k\u1EBFt n\u1ED1i JDBC" },
	{ "ErrorTNS", 			"L\u1ED7i li�n quan \u0111\u1EBFn k\u1EBFt n\u1ED1i TNS" },
	{ "ErrorMailServer", 	"L\u1ED7i li�n quan \u0111\u1EBFn Mail Server (\u0111\u1EEBng d�ng localhost)" },
	{ "ErrorMail", 		"L\u1ED7i li�n quan \u0111\u1EBFn Mail" },
	{ "ErrorSave", 			"L\u1ED7i khi l\u01B0u file" },

	{ "EnvironmentSaved",	"M�i tr\u01B0\u1EDDng \u0111� \u0111\u01B0\u1EE3c l\u01B0u\nB\u1EA1n c\u1EA7n kh\u1EDFi \u0111\u1ED9ng l\u1EA1i Server." },
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

}	//	SetupRes_vi
