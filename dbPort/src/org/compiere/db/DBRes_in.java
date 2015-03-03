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
 * Contributor(s): PT. RFID INDONESIA (info@rfid-indonesia.com)_______________.
 *****************************************************************************/
package org.compiere.db;

import java.util.*;

/**
 *  Connection Resource Strings
 *
 *  @author     Halim Englen
 *  @version    $Id: DBRes_in.java,v 1.3 2005/11/20 22:40:26 jjanke Exp $
 */
public class DBRes_in extends ListResourceBundle
{
	/** Data        */
	static final Object[][] contents = new String[][]{
	{ "CConnectionDialog", 	"Koneksi Ke Compiere" },
	{ "Name", 				"Nama" },
	{ "AppsHost", 			"Pusat Aplikasi" },
	{ "AppsPort", 			"Port Aplikasi" },
	{ "TestApps", 			"Uji Server Aplikasi" },
	{ "DBHost", 			"Pusat Database" },
	{ "DBPort", 			"Port Database" },
	{ "DBName", 			"Nama Database" },
	{ "DBUidPwd", 			"ID Pengguna / Kata Sandi" },
	{ "ViaFirewall", 		"lewat Firewall" },
	{ "FWHost", 			"Pusat Firewall" },
	{ "FWPort", 			"Port Firewall" },
	{ "TestConnection", 	"Uji Koneksi" },
	{ "Type", 				"Tipe Database" },
	{ "BequeathConnection", "Koneksi Warisan" },
	{ "Overwrite", 			"Timpakan" },
	{ "ConnectionProfile",	"Connection" },
	{ "LAN",		 		"LAN" },
	{ "TerminalServer",		"Terminal Server" },
	{ "VPN",		 		"VPN" },
	{ "WAN", 				"WAN" },
	{ "ConnectionError", 	"Kesalahan Koneksi" },
	{ "ServerNotActive", 	"Server tidak aktif" }
	};

	/**
	 * Get Contsnts
	 * @return contents
	 */
	public Object[][] getContents()
	{
		return contents;
	}   //  getContent
}   //  Res
