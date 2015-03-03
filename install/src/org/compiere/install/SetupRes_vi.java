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
	{ "CompiereServerSetup", "Setup máy ch\u1EE7 C\u0103mpia-\u01A1" },
	{ "Ok", 				"\u0110\u1ED3ng ý" },
	{ "File", 				"T\u1EC7p" },
	{ "Exit", 				"Thoát" },
	{ "Help", 				"Giúp \u0111\u1EE1" },
	{ "PleaseCheck", 		"Vui lòng ki\u1EC3m tra" },
	{ "UnableToConnect",	"Không th\u1EC3 tìm th\u1EA5y h\u1ED7 tr\u1EE3 t\u1EEB trang WEB C\u0103mpia-\u01A1" },

	{ "CompiereHomeInfo", 	"Th\u01B0 m\u1EE5c ch\u1EE9a C\u0103mpia-\u01A1" },
	{ "CompiereHome", 	"Th\u01B0 m\u1EE5c ch\u1EE9a C\u0103mpia-\u01A1" },
	{ "WebPortInfo", 	"C\u1ED5ng Web (HTML)" },
	{ "WebPort", 			"Web C\u1ED5ng" },
	{ "AppsServerInfo", 	"Tên máy ch\u1EE7 ch\u1EA1y \u1EE9ng d\u1EE5ng" },
	{ "AppsServer", 		"Máy ch\u1EE7 các \u1EE9ng d\u1EE5ng" },
	{ "DatabaseTypeInfo", 	"Lo\u1EA1i CSDL" },
	{ "DatabaseType", 		"Lo\u1EA1i CSDL" },
	{ "DatabaseNameInfo", 	"Tên CSDL " },
	{ "DatabaseName", 		"Tên CSDL (SID)" },
	{ "DatabasePortInfo", 	"C\u1ED5ng nghe CSDL" },
	{ "DatabasePort", 		"C\u1ED5ng CSDL" },
	{ "DatabaseUserInfo", 	"Ng\u01B0\u1EDDi dùng CSDL C\u0103mpia-\u01A1" },
	{ "DatabaseUser", 		"Ng\u01B0\u1EDDi dùng CSDL" },
	{ "DatabasePasswordInfo", "M\u1EADt kh\u1EA9u dùng CSDL C\u0103mpia-\u01A1" },
	{ "DatabasePassword", 	"M\u1EADt kh\u1EA9u CSDL" },
	{ "TNSNameInfo", 		"TNS ho\u1EB7c Global Database Name" },
	{ "TNSName", 			"Tên theo TNS" },
	{ "SystemPasswordInfo", "M\u1EADt kh\u1EA9u c\u1EE7a ng\u01B0\u1EDDi dùng H\u1EC7 th\u1ED1ng" },
	{ "SystemPassword", 	"M\u1EADt kh\u1EA9u c\u1EE7a ng\u01B0\u1EDDi dùng H\u1EC7 th\u1ED1ng" },
	{ "MailServerInfo", 	"Máy ch\u1EE7 th\u01B0 \u0111i\u1EC7n t\u1EED" },
	{ "MailServer", 	"Máy ch\u1EE7 th\u01B0 \u0111i\u1EC7n t\u1EED" },
	{ "AdminEMailInfo", 	"Email c\u1EE7a ng\u01B0\u1EDDi qu\u1EA3n tr\u1ECB C\u0103mpia-\u01A1" },
	{ "AdminEMail", 		"Email c\u1EE7a ng\u01B0\u1EDDi qu\u1EA3n tr\u1ECB" },
	{ "DatabaseServerInfo", "Tên máy ch\u1EE7 CSDL" },
	{ "DatabaseServer", 	"Máy ch\u1EE7 CSDL" },
	{ "JavaHomeInfo", 		"Th\u01B0 m\u1EE5c ch\u1EE9a Java" },
	{ "JavaHome", 			"Th\u01B0 m\u1EE5c ch\u1EE9a Java" },
	{ "JNPPortInfo", 		"C\u1ED5ng JNP c\u1EE7a máy ch\u1EE7 \u1EE9ng d\u1EE5ng" },
	{ "JNPPort", 			"C\u1ED5ng JNP" },
	{ "MailUserInfo", 		"Ng\u01B0\u1EDDi dùng th\u01B0 \u0111i\u1EC7n t\u1EED" },
	{ "MailUser", 			"Ng\u01B0\u1EDDi dùng th\u01B0 \u0111i\u1EC7n t\u1EED" },
	{ "MailPasswordInfo", 	"M\u1EADt kh\u1EA9u c\u1EE7a ng\u01B0\u1EDDi dùng th\u01B0 \u0111i\u1EC7n t\u1EED" },
	{ "MailPassword", 		"M\u1EADt kh\u1EA9u c\u1EE7a ng\u01B0\u1EDDi dùng th\u01B0 \u0111i\u1EC7n t\u1EED" },
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
	{ "TestInfo", 	"Th\u1EED nghi\u1EC7m các c\u1EA5u hình này" },
	{ "Test", 				"Th\u1EED nghi\u1EC7m" },
	{ "SaveInfo", 			"L\u01B0u các c\u1EA5u hình này" },
	{ "Save", 				"L\u01B0u" },
	{ "HelpInfo", 			"Nh\u1EADn giúp \u0111\u1EE1" },

	{ "ServerError", 		"SL\u1ED7i khi cài \u0111\u1EB7t ph\u1EA7n m\u1EC1m này \u1EDF máy ch\u1EE7" },
	{ "ErrorJavaHome", 		"Tên th\u01B0 m\u1EE5c ch\u1EE9a Java không \u0111úng" },
	{ "ErrorCompiereHome", 	"Tên th\u01B0 m\u1EE5c ch\u1EE9a C\u0103mpia-\u01A1 không \u0111úng" },
	{ "ErrorAppsServer", 	"L\u1ED7i liên quan \u0111\u1EBFn máy ch\u1EE7 \u1EE9ng d\u1EE5ng (\u0111\u1EEBng dùng localhost)" },
	{ "ErrorWebPort", 		"C\u1ED5ng Web không \u0111úng" },
	{ "ErrorJNPPort", 		"C\u1ED5ng JNP không \u0111úng" },
	{ "ErrorDatabaseServer", "L\u1ED7i liên quan \u0111\u1EBFn máy ch\u1EE7 CSDL (\u0111\u1EEBng dùng localhost)" },
	{ "ErrorDatabasePort", 	"C\u1ED5ng CSDL không \u0111úng" },
	{ "ErrorJDBC", 			"L\u1ED7i liên quan \u0111\u1EBFn k\u1EBFt n\u1ED1i JDBC" },
	{ "ErrorTNS", 			"L\u1ED7i liên quan \u0111\u1EBFn k\u1EBFt n\u1ED1i TNS" },
	{ "ErrorMailServer", 	"L\u1ED7i liên quan \u0111\u1EBFn Mail Server (\u0111\u1EEBng dùng localhost)" },
	{ "ErrorMail", 		"L\u1ED7i liên quan \u0111\u1EBFn Mail" },
	{ "ErrorSave", 			"L\u1ED7i khi l\u01B0u file" },

	{ "EnvironmentSaved",	"Môi tr\u01B0\u1EDDng \u0111ã \u0111\u01B0\u1EE3c l\u01B0u\nB\u1EA1n c\u1EA7n kh\u1EDFi \u0111\u1ED9ng l\u1EA1i Server." },
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
