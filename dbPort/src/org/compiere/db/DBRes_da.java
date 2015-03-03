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
 *  @author     Jorg Janke
 *  @version    $Id: DBRes_da.java,v 1.3 2005/11/20 22:40:26 jjanke Exp $
 */
public class DBRes_da extends ListResourceBundle
{
	/** Data        */
	static final Object[][] contents = new String[][]
	{
	{ "CConnectionDialog",  "Compiere forbindelse" },
	{ "Name",               "Navn" },
	{ "AppsHost",           "Programvært" },
	{ "AppsPort",           "Programport" },
	{ "TestApps",           "Test programserver" },
	{ "DBHost",             "Databasevært" },
	{ "DBPort",             "Databaseport" },
	{ "DBName",             "Databasenavn" },
	{ "DBUidPwd",           "Bruger/adgangskode" },
	{ "ViaFirewall",        "via Firewall" },
	{ "FWHost",             "Firewall-vært" },
	{ "FWPort",             "Firewall-port" },
	{ "TestConnection",     "Test database" },
	{ "Type",               "Databasetype" },
	{ "BequeathConnection", "Nedarv forbindelse" },
	{ "Overwrite",          "Overskriv" },
	{ "ConnectionProfile",	"Connection" },
	{ "LAN",		 		"LAN" },
	{ "TerminalServer",		"Terminal Server" },
	{ "VPN",		 		"VPN" },
	{ "WAN", 				"WAN" },
	{ "ConnectionError",    "Fejl i forbindelse" },
	{ "ServerNotActive",    "Server er ikke aktiv" }
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
