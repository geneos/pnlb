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
package org.compiere.apps;

import java.util.*;

/**
 *  Base Resource Bundle
 *
 * 	@author 	Stefan Christians 
 * 	@version 	$Id: ALoginRes_ja.java,v 1.3 2005/03/11 20:27:58 jjanke Exp $
 */
public final class ALoginRes_ja extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "\u63a5\u7d9a" },
	{ "Defaults",           "\u30c7\u30d5\u30a9\u30eb\u30c8" },
	{ "Login",              "\u30b3\u30f3\u30d4\u30a7\u30fc\u30ec \u30ed\u30b0\u30a4\u30f3" },
	{ "File",               "\u30d5\u30a1\u30a4\u30eb" },
	{ "Exit",               "\u7d42\u4e86" },
	{ "Help",               "\u30d8\u30eb\u30d7" },
	{ "About",              "\u60c5\u5831" },
	{ "Host",               "\u30b5\u30fc\u30d0" },
	{ "Database",           "\u30c7\u30fc\u30bf\u30d9\u30fc\u30b9" },
	{ "User",               "\u30e6\u30fc\u30b6\u30fc" },
	{ "EnterUser",          "\u30a2\u30d7\u30ea\u30b1\u30fc\u30b7\u30e7\u30f3\u306e\u30e6\u30fc\u30b6\u540d\u304a\u5165\u529b\u3057\u3066\u4e0b\u3055\u3044" },
	{ "Password",           "\u30d1\u30b9\u30ef\u30fc\u30c9" },
	{ "EnterPassword",      "\u30a2\u30d7\u30ea\u30b1\u30fc\u30b7\u30e7\u30f3\u306e\u30d1\u30b9\u30ef\u30fc\u30c9\u3092\u5165\u529b\u3057\u3066\u4e0b\u3055\u3044" },
	{ "Language",           "\u8a00\u8a9e" },
	{ "SelectLanguage",     "\u8a00\u8a9e\u3092\u9078\u629e\u3057\u3066\u4e0b\u3055\u3044" },
	{ "Role",               "\u5f79\u76ee" },
	{ "Client",             "\u4f1a\u793e" },
	{ "Organization",       "\u90e8\u8ab2" },
	{ "Date",               "\u65e5\u4ed8" },
	{ "Warehouse",          "\u5009\u5eab" },
	{ "Printer",            "\u30d7\u30ea\u30f3\u30bf" },
	{ "Connected",          "\u63a5\u7d9a\u6709\u308a" },
	{ "NotConnected",       "\u7121\u63a5\u7d9a" },
	{ "DatabaseNotFound",   "\u30c7\u30fc\u30bf\u30d9\u30fc\u30b9\u3092\u898b\u4ed8\u3051\u3089\u306a\u3044" },
	{ "UserPwdError",       "\u30e6\u30fc\u30b6\u540d\u3068\u30d1\u30b9\u30ef\u30fc\u30c9\u304c\u5408\u308f\u306a\u3044" },
	{ "RoleNotFound",       "\u5f79\u540d\u304c\u6709\u308a\u307e\u305b\u3093" },
	{ "Authorized",         "\u691c\u5b9a\u6e08\u307f" },
	{ "Ok",                 "OK" },
	{ "Cancel",             "\u30ad\u30e3\u30f3\u30bb\u30eb" },
	{ "VersionConflict",    "\u30d0\u30fc\u30b8\u30e7\u30f3\u304c\u5408\u308f\u306a\u3044:" },
	{ "VersionInfo",        "\u30b5\u30fc\u30d0 <> \u30af\u30e9\u30a4\u30a2\u30f3\u30c8" },
	{ "PleaseUpgrade",      "\u30d0\u30fc\u30b8\u30e7\u30f3\u30a2\u30c3\u30d7\u3057\u3066\u4e0b\u3055\u3044" }
	};

	/**
	 *  Get Contents
	 *  @return context
	 */
	public Object[][] getContents()
	{
		return contents;
	}   //  getContents
}   //  ALoginRes
