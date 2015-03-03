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
 *  Connection Resource Strings (Thai)
 *
 *  @author     Sureeraya Limpaibul
 *  @version    $Id: DBRes_th.java,v 1.7 2005/11/20 22:40:26 jjanke Exp $
 */
public class DBRes_th extends ListResourceBundle
{
	/** Data        */
	static final Object[][] contents = new String[][]{
	{ "CConnectionDialog",  "Compiere Connection" },
	{ "Name",               "\u0e0a\u0e37\u0e48\u0e2d" },
	{ "AppsHost",           "\u0e41\u0e2d\u0e47\u0e1e\u0e1e\u0e25\u0e34\u0e40\u0e04\u0e0a\u0e31\u0e48\u0e19 \u0e42\u0e2e\u0e2a" },
	{ "AppsPort",           "\u0e41\u0e2d\u0e47\u0e1e\u0e1e\u0e25\u0e34\u0e40\u0e04\u0e0a\u0e31\u0e48\u0e19 \u0e1e\u0e2d\u0e23\u0e4c\u0e15" },
	{ "TestApps",           "\u0e17\u0e14\u0e2a\u0e2d\u0e1a\u0e41\u0e2d\u0e47\u0e1e\u0e1e\u0e25\u0e34\u0e40\u0e04\u0e0a\u0e31\u0e48\u0e19" },
	{ "DBHost",             "\u0e42\u0e2e\u0e2a\u0e02\u0e2d\u0e07\u0e10\u0e32\u0e19\u0e02\u0e49\u0e2d\u0e21\u0e39\u0e25" },
	{ "DBPort",             "\u0e1e\u0e2d\u0e23\u0e4c\u0e15\u0e02\u0e2d\u0e07\u0e10\u0e32\u0e19\u0e02\u0e49\u0e2d\u0e21\u0e39\u0e25" },
	{ "DBName",             "\u0e0a\u0e37\u0e48\u0e2d\u0e10\u0e32\u0e19\u0e02\u0e49\u0e2d\u0e21\u0e39\u0e25" },
	{ "DBUidPwd",           "\u0e0a\u0e37\u0e48\u0e2d\u0e1c\u0e39\u0e49\u0e43\u0e0a\u0e49 / \u0e23\u0e2b\u0e31\u0e2a\u0e1c\u0e48\u0e32\u0e19" },
	{ "ViaFirewall",        "\u0e1c\u0e48\u0e32\u0e19\u0e44\u0e1f\u0e23\u0e27\u0e2d\u0e25" },
	{ "FWHost",             "\u0e44\u0e1f\u0e23\u0e27\u0e2d\u0e25 \u0e42\u0e2e\u0e2a" },
	{ "FWPort",             "\u0e1e\u0e2d\u0e23\u0e4c\u0e15\u0e44\u0e1f\u0e23\u0e27\u0e2d\u0e25" },
	{ "TestConnection",     "\u0e17\u0e14\u0e2a\u0e2d\u0e1a\u0e10\u0e32\u0e19\u0e02\u0e49\u0e2d\u0e21\u0e39\u0e25" },
	{ "Type",               "\u0e1b\u0e23\u0e30\u0e40\u0e20\u0e17\u0e10\u0e32\u0e19\u0e02\u0e49\u0e2d\u0e21\u0e39\u0e25" },
	{ "BequeathConnection", "Bequeath Connection" },
	{ "Overwrite",          "\u0e1a\u0e31\u0e19\u0e17\u0e36\u0e01\u0e17\u0e31\u0e1a" },
	{ "ConnectionProfile",	"Connection" },
	{ "LAN",		 		"LAN" },
	{ "TerminalServer",		"Terminal Server" },
	{ "VPN",		 		"VPN" },
	{ "WAN", 				"WAN" },
	{ "ConnectionError",    "\u0e01\u0e32\u0e23\u0e40\u0e0a\u0e37\u0e48\u0e2d\u0e21\u0e15\u0e48\u0e2d\u0e1c\u0e34\u0e14\u0e1e\u0e25\u0e32\u0e14" },
	{ "ServerNotActive",    "\u0e40\u0e0a\u0e34\u0e23\u0e4c\u0e1f\u0e40\u0e27\u0e2d\u0e23\u0e4c\u0e44\u0e21\u0e48\u0e41\u0e2d\u0e47\u0e04\u0e17\u0e35\u0e1f" }};

	/**
	 * Get Contsnts
	 * @return contents
	 */
	public Object[][] getContents()
	{
		return contents;
	}
}   //  Res
