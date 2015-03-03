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
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ALoginRes_ml.java,v 1.3 2005/03/11 20:27:59 jjanke Exp $
 */
public final class ALoginRes_ml extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "Hubungan" },
	{ "Defaults",           "Defaults" },
	{ "Login",              "Compiere Login" },
	{ "File",               "Fail" },
	{ "Exit",               "Keluar" },
	{ "Help",               "Tolong" },
	{ "About",              "Tentang" },
	{ "Host",               "Host" },
	{ "Database",           "Pangkalan Data" },
	{ "User",               "ID Pengguna" },
	{ "EnterUser",          "Masukkan ID Pengguna" },
	{ "Password",           "Kata Laluan" },
	{ "EnterPassword",      "Masukkan Kata Laluan Applikasi" },
	{ "Language",           "Bahasa" },
	{ "SelectLanguage",     "Pilih Bahasa Anda" },
	{ "Role",               "Role" },
	{ "Client",             "Pengguna" },
	{ "Organization",       "Organisasi" },
	{ "Date",               "Tarikh" },
	{ "Warehouse",          "Warehouse" },
	{ "Printer",            "Printer" },
	{ "Connected",          "Telah dihubungi" },
	{ "NotConnected",       "Tiday dapat dihubungi" },
	{ "DatabaseNotFound",   "Pangkalan Data tidak dijumpai" },
	{ "UserPwdError",       "Pengguna tidak padan dengan kata laluan" },
	{ "RoleNotFound",       "Role not found/complete" },
	{ "Authorized",         "Authorized" },
	{ "Ok",                 "Ok" },
	{ "Cancel",             "Batal" },
	{ "VersionConflict",    "Bertentangan versi:" },
	{ "VersionInfo",        "Pelayan <> Pengguna" },
	{ "PleaseUpgrade",      "Please run the update program" }
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
