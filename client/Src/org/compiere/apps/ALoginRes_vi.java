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
 *  Vietnamese Resource Bundle
 *
 * 	@author 	Bui Chi Trung
 * 	@version 	$Id: ALoginRes_vi.java,v 1.6 2005/12/19 01:16:53 jjanke Exp $
 */
public final class ALoginRes_vi extends ListResourceBundle
{
	// TODO Run native2ascii to convert everything to plain ASCII !! 
	
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "K\u1EBFt n\u1ED1i" },
	{ "Defaults",           "M\u1EB7c nhi�n" },
	{ "Login",              "\u0110\u0103ng nh\u1EADp" },
	{ "File",               "H\u1EC7 th\u1ED1ng" },
	{ "Exit",               "Tho�t" },
	{ "Help",               "Gi�p \u0111\u1EE1" },
	{ "About",              "Gi\u1EDBi thi\u1EC7u" },
	{ "Host",               "M�y ch\u1EE7" },
	{ "Database",           "C\u01A1 s\u1EDF d\u1EEF li\u1EC7u" },
	{ "User",               "T�n ng\u01B0\u1EDDi d�ng" },
	{ "EnterUser",          "H�y nh\u1EADp t�n ng\u01B0\u1EDDi d�ng" },
	{ "Password",           "M\u1EADt kh\u1EA9u" },
	{ "EnterPassword",      "H�y nh\u1EADp m\u1EADt kh\u1EA9u" },
	{ "Language",           "Ng�n ng\u1EEF" },
	{ "SelectLanguage",     "H�y ch\u1ECDn ng�n ng\u1EEF" },
	{ "Role",               "Vai tr�" },
	{ "Client",             "C�ng ty" },
	{ "Organization",       "\u0110\u01A1n v\u1ECB" },
	{ "Date",               "Ng�y" },
	{ "Warehouse",          "Kho h�ng" },
	{ "Printer",            "M�y in" },
	{ "Connected",          "\u0110� k\u1EBFt n\u1ED1i" },
	{ "NotConnected",       "Ch\u01B0a k\u1EBFt n\u1ED1i \u0111\u01B0\u1EE3c" },
	{ "DatabaseNotFound",   "Kh�ng t�m th\u1EA5y CSDL" },
	{ "UserPwdError",       "Ng\u01B0\u1EDDi d�ng v� m\u1EADt kh\u1EA9u kh�ng kh\u1EDBp nhau" },
	{ "RoleNotFound",       "Kh�ng t�m th\u1EA5y vai tr� n�y" },
	{ "Authorized",         "\u0110� \u0111\u01B0\u1EE3c ph�p" },
	{ "Ok",                 "\u0110\u1ED3ng �" },
	{ "Cancel",             "H\u1EE7y" },
	{ "VersionConflict",    "X\u1EA3y ra tranh ch\u1EA5p phi�n b\u1EA3n:" },
	{ "VersionInfo",        "Th�ng tin v\u1EC1 phi�n b\u1EA3n" },
	{ "PleaseUpgrade",      "Vui l�ng n�ng c\u1EA5p ch\u01B0\u01A1ng tr�nh" }
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
