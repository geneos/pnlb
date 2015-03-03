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
 * 	@author 	Stefan Christians 
 * 	@version 	$Id: SetupRes_ja.java,v 1.4 2005/03/11 20:30:21 jjanke Exp $
 */
public class SetupRes_ja extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", 	"\u30b3\u30f3\u30d4\u30a8\u30fc\u30ec \u30b5\u30fc\u30d0 \u8a2d\u5b9a" },
	{ "Ok", 					"Ok" },
	{ "File", 					"\u30d5\u30a1\u30a4\u30eb" },
	{ "Exit", 					"\u7d42\u4e86" },
	{ "Help", 					"\u30d8\u30eb\u30d7" },
	{ "PleaseCheck", 			"\u78ba\u304b\u3081\u3066\u4e0b\u3055\u3044" },
	{ "UnableToConnect", 		"\u30b3\u30f3\u30d4\u30a8\u30fc\u30ec\u306e\u30db\u30fc\u30e0\u30da\u30fc\u30b8\u306b\u63a5\u7d9a\u304c\u3067\u304d\u306a\u3044" },
	//
	{ "CompiereHomeInfo", 		"\u30b3\u30f3\u30d4\u30a8\u30fc\u30ec\u306e\u30db\u30fc\u30e0\u30d5\u30a9\u30eb\u30c0" },
	{ "CompiereHome", 			"\u30b3\u30f3\u30d4\u30a8\u30fc\u30ec\u306e\u30db\u30fc\u30e0" },
	{ "WebPortInfo", 			"\u30a6\u30a8\u30d6 (HTML) \u30dd\u30fc\u30c8" },
	{ "WebPort", 				"\u30a6\u30a8\u30d6 \u30dd\u30fc\u30c8" },
	{ "AppsServerInfo", 		"\u30a2\u30d7\u30ea\u30b1\u30fc\u30b7\u30e7\u30f3\u30fb\u30b5\u30fc\u30d0\u540d" },
	{ "AppsServer", 			"\u30a2\u30d7\u30ea\u30b1\u30fc\u30b7\u30e7\u30f3\u30fb\u30b5\u30fc\u30d0" },
	{ "DatabaseTypeInfo", 		"\u30c7\u30fc\u30bf\u30d9\u30fc\u30b9" },
	{ "DatabaseType", 			"\u30c7\u30fc\u30bf\u30d9\u30fc\u30b9" },
	{ "DatabaseNameInfo", 		"\u30c7\u30fc\u30bf\u30d9\u30fc\u30b9\u540d" },
	{ "DatabaseName", 			"\u30c7\u30fc\u30bf\u30d9\u30fc\u30b9\u540d (SID)" },
	{ "DatabasePortInfo", 		"\u30c7\u30fc\u30bf\u30d9\u30fc\u30b9 \u30ea\u30bb\u30ca\u30fc \u30dd\u30fc\u30c8" },
	{ "DatabasePort", 			"\u30c7\u30fc\u30bf\u30d9\u30fc\u30b9 \u30dd\u30fc\u30c8" },
	{ "DatabaseUserInfo", 		"\u30b3\u30f3\u30d4\u30a8\u30fc\u30ec\u306e\u30c7\u30fc\u30bf\u30d9\u30fc\u30b9\u30e6\u30fc\u30b6\u540d" },
	{ "DatabaseUser", 			"\u30c7\u30fc\u30bf\u30d9\u30fc\u30b9\u30e6\u30fc\u30b6\u540d" },
	{ "DatabasePasswordInfo", 	"\u30b3\u30f3\u30d4\u30a8\u30fc\u30ec\u306e\u30c7\u30fc\u30bf\u30d9\u30fc\u30b9\u30d1\u30b9\u30ef\u30fc\u30c9" },
	{ "DatabasePassword", 		"\u30c7\u30fc\u30bf\u30d9\u30fc\u30b9\u30d1\u30b9\u30ef\u30fc\u30c9" },
	{ "TNSNameInfo", 			"\u30b0\u30ed\u30fc\u30d0\u30eb\u30c7\u30fc\u30bf\u30d9\u30fc\u30b9\u540d (TNS)" },
	{ "TNSName", 				"TNS" },
	{ "SystemPasswordInfo", 	"\u30c7\u30fc\u30bf\u30d9\u30fc\u30b9\u306e\u30b7\u30b9\u30c6\u30e0\u30e6\u30fc\u30b6\u306e\u30d1\u30b9\u30ef\u30fc\u30c9" },
	{ "SystemPassword", 		"\u30b7\u30b9\u30c6\u30e0\u30d1\u30b9\u30ef\u30fc\u30c9" },
	{ "MailServerInfo", 		"\u30e1\u30fc\u30eb\u30fb\u30b5\u30fc\u30d0" },
	{ "MailServer", 			"\u30e1\u30fc\u30eb\u30fb\u30b5\u30fc\u30d0" },
	{ "AdminEMailInfo", 		"\u30a2\u30c9\u30df\u30cb\u30b9\u30c8\u30ec\u30fc\u30c8\u306e\u30e1\u30fc\u30eb\u30a2\u30c9\u30ec\u30b9" },
	{ "AdminEMail", 			"\u30e1\u30fc\u30eb" },
	{ "DatabaseServerInfo", 	"\u30c7\u30fc\u30bf\u30d9\u30fc\u30b9\u30fb\u30b5\u30fc\u30d0\u540d" },
	{ "DatabaseServer", 		"\u30c7\u30fc\u30bf\u30d9\u30fc\u30b9\u30fb\u30b5\u30fc\u30d0\u540d" },
	{ "JavaHomeInfo", 			"Java\u306e\u30db\u30fc\u30e0\u30d5\u30a9\u30eb\u30c0" },
	{ "JavaHome", 				"Java\u306e\u30db\u30fc\u30e0" },
	{ "JNPPortInfo", 			"\u30a2\u30d7\u30ea\u30b1\u30fc\u30b7\u30e7\u30f3\u30fb\u30b5\u30fc\u30d0\u306eJNP \u30dd\u30fc\u30c8" },
	{ "JNPPort", 				"JNP \u30dd\u30fc\u30c8" },
	{ "MailUserInfo", 			"\u30b3\u30f3\u30d4\u30a8\u30fc\u30ec\u306e\u30e1\u30fc\u30eb\u30e6\u30fc\u30b6\u540d" },
	{ "MailUser", 				"\u30e1\u30fc\u30eb\u30e6\u30fc\u30b6" },
	{ "MailPasswordInfo", 		"\u30b3\u30f3\u30d4\u30a8\u30fc\u30ec\u306e\u30e1\u30fc\u30eb\u30d1\u30b9\u30ef\u30fc\u30c9" },
	{ "MailPassword", 			"\u30e1\u30fc\u30eb\u30d1\u30b9\u30ef\u30fc\u30c9" },
	//
	{ "JavaType",				"Java VM"},
	{ "JavaTypeInfo",			"Java VM Vendor"},
	{ "AppsType",				"Server Type"},
	{ "AppsTypeInfo",			"J2EE Application Server Type"},
	{ "DeployDir",				"Deployment"},
	{ "DeployDirInfo",			"J2EE Deployment Directory"},
	{ "ErrorDeployDir",			"Error Deployment Directory"},
	//
	{ "TestInfo", 				"\u8a2d\u5b9a\u306e\u30c6\u30b9\u30c8" },
	{ "Test", 					"\u30c6\u30b9\u30c8" },
	{ "SaveInfo", 				"\u8a2d\u5b9a\u306e\u4fdd\u5b58" },
	{ "Save", 					"\u4fdd\u5b58" },
	{ "HelpInfo", 				"\u30d8\u30eb\u30d7" },
	//
	{ "ServerError", 			"\u30b5\u30fc\u30d0\u8a2d\u5b9a\u30a8\u30e9\u30fc" },
	{ "ErrorJavaHome", 			"Java\u306e\u30db\u30fc\u30e0\u30a8\u30e9\u30fc" },
	{ "ErrorCompiereHome", 		"\u30b3\u30f3\u30d4\u30a8\u30fc\u30ec\u306e\u30db\u30fc\u30e0\u30a8\u30e9\u30fc" },
	{ "ErrorAppsServer", 		"\u30a2\u30d7\u30ea\u30b1\u30fc\u30b7\u30e7\u30f3\u30fb\u30b5\u30fc\u30d0\u306e\u30a8\u30e9\u30fc\uff1alocalhost" },
	{ "ErrorWebPort", 			"\u30a6\u30a8\u30d6\u30dd\u30fc\u30c8\u306e\u30a8\u30e9\u30fc" },
	{ "ErrorJNPPort", 			"JNP\u30dd\u30fc\u30c8\u306e\u30a8\u30e9\u30fc" },
	{ "ErrorDatabaseServer", 	"\u30c7\u30fc\u30bf\u30d9\u30fc\u30b9\u30fb\u30b5\u30fc\u30d0\u306e\u30a8\u30e9\u30fc\uff1alocalhost" },
	{ "ErrorDatabasePort", 		"\u30c7\u30fc\u30bf\u30d9\u30fc\u30b9\u30dd\u30fc\u30c8\u306e\u30a8\u30e9\u30fc" },
	{ "ErrorJDBC", 				"JDBC\u63a5\u7d9a\u306e\u30a8\u30e9\u30fc" },
	{ "ErrorTNS", 				"TNS\u63a5\u7d9a\u306e\u30a8\u30e9\u30fc" },
	{ "ErrorMailServer", 		"\u30e1\u30fc\u30eb\u30fb\u30b5\u30fc\u30d0\u306e\u30a8\u30e9\u30fc\uff1alocalhost" },
	{ "ErrorMail", 				"\u30e1\u30fc\u30eb\u306e\u30a8\u30e9\u30fc" },
	{ "ErrorSave", 				"\u4fdd\u5b58\u306e\u30a8\u30e9\u30fc" },

	{ "EnvironmentSaved", 		"\u8a2d\u5b9a\u3092\u4fdd\u5b58\u3057\u307e\u3057\u305f\u3002\n\u30a2\u30d7\u30ea\u30b1\u30fc\u30b7\u30e7\u30f3\u30fb\u30b5\u30fc\u30d0\u3092\u958b\u3044\u3066\u4e0b\u3055\u3044\u3002" }

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
