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
 *  @author     ZhaoXing Meng
 *  @version    $Id: DBRes_zh_CN.java,v 1.7 2005/11/20 22:40:26 jjanke Exp $
 */
public class DBRes_zh_CN extends ListResourceBundle
{
	/** Data        */
	static final Object[][] contents = new String[][]
	{
	{ "CConnectionDialog",  "Compiere \u8fde\u673a" },
	{ "Name",               "\u540d\u79f0" },
	{ "AppsHost",           "\u5e94\u7528\u670d\u52a1\u5668\u4e3b\u673a" },
	{ "AppsPort",           "\u5e94\u7528\u670d\u52a1\u5668\u7aef\u53e3" },
	{ "TestApps",           "\u6d4b\u8bd5\u5e94\u7528\u670d\u52a1\u5668" },
	{ "DBHost",             "\u6570\u636e\u5e93\u4e3b\u673a" },
	{ "DBPort",             "\u6570\u636e\u5e93\u7aef\u53e3" },
	{ "DBName",             "\u6570\u636e\u5e93\u540d" },
	{ "DBUidPwd",           "\u7528\u6237\u53f7 / \u53e3\u4ee4" },
	{ "ViaFirewall",        "\u901a\u8fc7\u9632\u706b\u5899" },
	{ "FWHost",             "\u9632\u706b\u5899\u4e3b\u673a" },
	{ "FWPort",             "\u9632\u706b\u5899\u7aef\u53e3" },
	{ "TestConnection",     "\u6d4b\u8bd5\u6570\u636e\u5e93" },
	{ "Type",               "\u6570\u636e\u5e93\u7c7b\u578b" },
	{ "BequeathConnection", "\u9057\u7559\u8fde\u7ebf" },
	{ "Overwrite",          "\u8986\u5199" },
	{ "ConnectionProfile",	"Connection" },
	{ "LAN",		 		"LAN" },
	{ "TerminalServer",		"Terminal Server" },
	{ "VPN",		 		"VPN" },
	{ "WAN", 				"WAN" },
	{ "ConnectionError",    "\u8fde\u673a\u9519\u8bef" },
	{ "ServerNotActive",    "\u670d\u52a1\u5668\u6ca1\u53cd\u5e94" }
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
