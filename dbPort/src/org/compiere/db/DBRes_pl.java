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
package org.compiere.db;

import java.util.*;

/**
 *  Connection Resource Strings
 *
 *  @author     Adam Bodurka
 *  @version    $Id: DBRes_pl.java,v 1.8 2005/11/20 22:40:26 jjanke Exp $
 */
public class DBRes_pl extends ListResourceBundle
{
	/** Data        */
	static final Object[][] contents = new String[][]
	{
	{ "CConnectionDialog",  "Po\u0142\u0105czenie z Compiere" },
	{ "Name",               "Nazwa" },
	{ "AppsHost",           "Host Aplikacji" },
	{ "AppsPort",           "Port Aplikacji" },
	{ "TestApps",           "Test Aplikacji" },
	{ "DBHost",             "Host Bazy Danych" },
	{ "DBPort",             "Port Bazy Danych" },
	{ "DBName",             "Nazwa Bazy Danych" },
	{ "DBUidPwd",           "U\u017cytkownik / Has\u0142o" },
	{ "ViaFirewall",        "via Firewall" },
	{ "FWHost",             "Host Firewall-a" },
	{ "FWPort",             "Port Firewall-a" },
	{ "TestConnection",     "Test Bazy Danych" },
	{ "Type",               "Typ Bazy Danych" },
	{ "BequeathConnection", "Zapisuj Po\u0142\u0105czenie" },
	{ "Overwrite",          "Nadpisuj" },
	{ "ConnectionProfile",	"Connection" },
	{ "LAN",		 		"LAN" },
	{ "TerminalServer",		"Terminal Server" },
	{ "VPN",		 		"VPN" },
	{ "WAN", 				"WAN" },
	{ "ConnectionError",    "B\u0142\u0105d po\u0142\u0105czenia" },
	{ "ServerNotActive",    "Serwer nie jest aktywny" }
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
