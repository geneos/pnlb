/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): PT. RFID INDONESIA (info@rfid-indonesia.com)______________.
 *****************************************************************************/
package org.compiere.apps;

import java.util.*;

/**
 *  Base Resource Bundle
 *
 * 	@author 	Halim Englen (halim@rfid-indonesia.com)
 * 	@version 	$Id: ALoginRes_in.java,v 1.4 2005/12/19 01:16:52 jjanke Exp $
 */
public final class ALoginRes_in extends ListResourceBundle
{
	// TODO Run native2ascii to convert to plain ASCII !! 
	
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "Koneksi" },
	{ "Defaults",           "Konfigurasi Dasar" },
	{ "Login",              "Login Compiere" },
	{ "File",               "File" },
	{ "Exit",               "Keluar" },
	{ "Help",               "Tolong" },
	{ "About",              "Tentang" },
	{ "Host",               "Pusat" },
	{ "Database",           "Database" },
	{ "User",               "ID Pengguna" },
	{ "EnterUser",          "Masukkan ID pengguna" },
	{ "Password",           "Kata Sandi" },
	{ "EnterPassword",      "Masukkan kata sandi applikasi" },
	{ "Language",           "Pilihan Bahasa" },
	{ "SelectLanguage",     "Pilihlah bahasa yang disukai" },
	{ "Role",               "Jabatan" },
	{ "Client",             "Klien" },
	{ "Organization",       "Organisasi" },
	{ "Date",               "Tanggal" },
	{ "Warehouse",          "Gudang" },
	{ "Printer",            "Pencetak" },
	{ "Connected",          "Sistem telah terkoneksi" },
	{ "NotConnected",       "Sistem tidak terkoneksi!" },
	{ "DatabaseNotFound",   "Database tidak ditemukan!" },
	{ "UserPwdError",       "Nama ID pengguna anda tidak sesuai dengan kata sandi" },
	{ "RoleNotFound",       "Jabatan tidak ditemukan" },
	{ "Authorized",         "Anda telah diotorisasi" },
	{ "Ok",                 "Ok" },
	{ "Cancel",             "Batal" },
	{ "VersionConflict",    "Konflik Versi" },
	{ "VersionInfo",        "Info Versi" },
	{ "PleaseUpgrade",      "Mohon hubungi partner Compiere anda untuk upgrade" }
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
