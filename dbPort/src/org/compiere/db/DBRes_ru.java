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
 *  @author     Vyacheslav Pedak
 *  @version    $Id: DBRes_ru.java,v 1.6 2005/11/20 22:40:26 jjanke Exp $
 */
public class DBRes_ru extends ListResourceBundle
{
	/** Data        */
	static final Object[][] contents = new String[][]
	{
	{ "CConnectionDialog",  "\u0421\u043e\u0435\u0434\u0438\u043d\u0435\u043d\u0438\u0435 \u0441 Compiere" },
	{ "Name",               "\u0418\u043c\u044f" },
	{ "AppsHost",           "\u0421\u0435\u0440\u0432\u0435\u0440 \u043f\u0440\u0438\u043b\u043e\u0436\u0435\u043d\u0438\u044f" },
	{ "AppsPort",           "\u041f\u043e\u0440\u0442" },
	{ "TestApps",           "\u0422\u0435\u0441\u0442\u043e\u0432\u043e\u0435 \u043f\u0440\u0438\u043b\u043e\u0436\u0435\u043d\u0438\u0435" },
	{ "DBHost",             "\u0421\u0435\u0440\u0432\u0435\u0440 \u0431\u0430\u0437\u044b \u0434\u0430\u043d\u043d\u044b\u0445" },
	{ "DBPort",             "\u041f\u043e\u0440\u0442 \u0441\u0435\u0440\u0432\u0435\u0440\u0430 \u0431\u0430\u0437\u044b \u0434\u0430\u043d\u043d\u044b\u0445" },
	{ "DBName",             "\u0418\u043c\u044f \u0431\u0430\u0437\u044b \u0434\u0430\u043d\u043d\u044b\u0445" },
	{ "DBUidPwd",           "\u041f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u0435\u043b\u044c / \u041f\u0430\u0440\u043e\u043b\u044c" },
	{ "ViaFirewall",        "\u0447\u0435\u0440\u0435\u0437 Firewall" },
	{ "FWHost",             "\u0421\u0435\u0440\u0432\u0435\u0440 Firewall" },
	{ "FWPort",             "\u041f\u043e\u0440\u0442 \u0441\u0435\u0440\u0432\u0435\u0440\u0430 Firewall" },
	{ "TestConnection",     "\u0422\u0435\u0441\u0442\u043e\u0432\u0430\u044f \u0431\u0430\u0437\u0430 \u0434\u0430\u043d\u043d\u044b\u0445" },
	{ "Type",               "\u0422\u0438\u043f \u0431\u0430\u0437\u044b \u0434\u0430\u043d\u043d\u044b\u0445" },
	{ "BequeathConnection", "Bequeath \u0441\u043e\u0435\u0434\u0438\u043d\u0435\u043d\u0438\u0435" },
	{ "Overwrite",          "\u041f\u0435\u0440\u0435\u043f\u0438\u0441\u0430\u0442\u044c" },
	{ "ConnectionProfile",	"Connection" },
	{ "LAN",		 		"LAN" },
	{ "TerminalServer",		"Terminal Server" },
	{ "VPN",		 		"VPN" },
	{ "WAN", 				"WAN" },
	{ "ConnectionError",    "\u041e\u0448\u0438\u0431\u043a\u0430 \u0441\u043e\u0435\u0434\u0438\u043d\u0435\u043d\u0438\u044f" },
	{ "ServerNotActive",    "\u0421\u0435\u0440\u0432\u0435\u0440 \u043d\u0435 \u0430\u043a\u0442\u0438\u0432\u0435\u043d" }
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
