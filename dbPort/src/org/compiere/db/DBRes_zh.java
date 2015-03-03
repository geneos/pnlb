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
 *  @author     kirinlin
 *  @version    $Id: DBRes_zh.java,v 1.8 2005/11/20 22:40:26 jjanke Exp $
 */
public class DBRes_zh extends ListResourceBundle
{
	/** Data        */
	static final Object[][] contents = new String[][]
	{
	{ "CConnectionDialog",  "Compiere \u9023\u7dda" },
	{ "Name",               "\u540d\u7a31" },
	{ "AppsHost",           "\u61c9\u7528\u7a0b\u5f0f\u4e3b\u6a5f" },
	{ "AppsPort",           "\u61c9\u7528\u7a0b\u5f0f\u57e0" },
	{ "TestApps",           "\u6e2c\u8a66" },
	{ "DBHost",             "\u8cc7\u6599\u5eab\u4e3b\u6a5f" },
	{ "DBPort",             "\u8cc7\u6599\u5eab\u9023\u63a5\u57e0" },
	{ "DBName",             "\u8cc7\u6599\u5eab\u540d\u7a31" },
	{ "DBUidPwd",           "\u5e33\u865f / \u5bc6\u78bc" },
	{ "ViaFirewall",        "\u7d93\u904e\u9632\u706b\u7246" },
	{ "FWHost",             "\u9632\u706b\u7246\u4e3b\u6a5f" },
	{ "FWPort",             "\u9632\u706b\u7246\u57e0" },
	{ "TestConnection",     "\u6e2c\u8a66\u8cc7\u6599\u5eab" },
	{ "Type",               "\u8cc7\u6599\u5eab\u7a2e\u985e" },
	{ "BequeathConnection", "\u907a\u7559\u9023\u7dda" },
	{ "Overwrite",          "\u8986\u5beb" },
	{ "ConnectionProfile",	"Connection" },
	{ "LAN",		 		"LAN" },
	{ "TerminalServer",		"Terminal Server" },
	{ "VPN",		 		"VPN" },
	{ "WAN", 				"WAN" },
	{ "ConnectionError",    "\u9023\u7dda\u932f\u8aa4" },
	{ "ServerNotActive",    "\u4f3a\u670d\u5668\u672a\u52d5\u4f5c" }
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
