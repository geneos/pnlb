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
	{ "Defaults",           "M\u1EB7c nhiên" },
	{ "Login",              "\u0110\u0103ng nh\u1EADp" },
	{ "File",               "H\u1EC7 th\u1ED1ng" },
	{ "Exit",               "Thoát" },
	{ "Help",               "Giúp \u0111\u1EE1" },
	{ "About",              "Gi\u1EDBi thi\u1EC7u" },
	{ "Host",               "Máy ch\u1EE7" },
	{ "Database",           "C\u01A1 s\u1EDF d\u1EEF li\u1EC7u" },
	{ "User",               "Tên ng\u01B0\u1EDDi dùng" },
	{ "EnterUser",          "Hãy nh\u1EADp tên ng\u01B0\u1EDDi dùng" },
	{ "Password",           "M\u1EADt kh\u1EA9u" },
	{ "EnterPassword",      "Hãy nh\u1EADp m\u1EADt kh\u1EA9u" },
	{ "Language",           "Ngôn ng\u1EEF" },
	{ "SelectLanguage",     "Hãy ch\u1ECDn ngôn ng\u1EEF" },
	{ "Role",               "Vai trò" },
	{ "Client",             "Công ty" },
	{ "Organization",       "\u0110\u01A1n v\u1ECB" },
	{ "Date",               "Ngày" },
	{ "Warehouse",          "Kho hàng" },
	{ "Printer",            "Máy in" },
	{ "Connected",          "\u0110ã k\u1EBFt n\u1ED1i" },
	{ "NotConnected",       "Ch\u01B0a k\u1EBFt n\u1ED1i \u0111\u01B0\u1EE3c" },
	{ "DatabaseNotFound",   "Không tìm th\u1EA5y CSDL" },
	{ "UserPwdError",       "Ng\u01B0\u1EDDi dùng và m\u1EADt kh\u1EA9u không kh\u1EDBp nhau" },
	{ "RoleNotFound",       "Không tìm th\u1EA5y vai trò này" },
	{ "Authorized",         "\u0110ã \u0111\u01B0\u1EE3c phép" },
	{ "Ok",                 "\u0110\u1ED3ng ý" },
	{ "Cancel",             "H\u1EE7y" },
	{ "VersionConflict",    "X\u1EA3y ra tranh ch\u1EA5p phiên b\u1EA3n:" },
	{ "VersionInfo",        "Thông tin v\u1EC1 phiên b\u1EA3n" },
	{ "PleaseUpgrade",      "Vui lòng nâng c\u1EA5p ch\u01B0\u01A1ng trình" }
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
