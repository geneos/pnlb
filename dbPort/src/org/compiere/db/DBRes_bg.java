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
 *  @author     Plamen Niikolov
 *  @version    $Id: DBRes_bg.java,v 1.7 2005/11/20 22:40:26 jjanke Exp $
 */
public class DBRes_bg extends ListResourceBundle
{
	/** Data        */
	static final Object[][] contents = new String[][]{
	{ "CConnectionDialog",  "\u0412\u0440\u044a\u0437\u043a\u0430" },
	{ "Name",               "\u0418\u043c\u0435" },
	{ "AppsHost",           "\u0421\u044a\u0440\u0432\u0435\u0440 \u043d\u0430 \u043f\u0440\u0438\u043b\u043e\u0436\u0435\u043d\u0438\u0435\u0442\u043e" },
	{ "AppsPort",           "\u041f\u043e\u0440\u0442 \u043d\u0430 \u043f\u0440\u0438\u043b\u043e\u0436\u0435\u043d\u0438\u0435\u0442\u043e" },
	{ "TestApps",           "\u0422\u0435\u0441\u0442 \u043d\u0430 \u043f\u0440\u0438\u043b\u043e\u0436\u0435\u043d\u0438\u0435\u0442\u043e" },
	{ "DBHost",             "\u0421\u044a\u0440\u0432\u0435\u0440 \u043d\u0430 \u0431\u0430\u0437\u0430\u0442\u0430 \u0434\u0430\u043d\u043d\u0438" },
	{ "DBPort",             "\u041f\u043e\u0440\u0442 \u043d\u0430 \u0431\u0430\u0437\u0430\u0442\u0430 \u0434\u0430\u043d\u043d\u0438" },
	{ "DBName",             "\u0418\u043c\u0435 \u043d\u0430 \u0431\u0430\u0437\u0430\u0442\u0430 \u0434\u0430\u043d\u043d\u0438" },
	{ "DBUidPwd",           "\u041f\u043e\u0442\u0440\u0435\u0431\u0438\u0442\u0435\u043b / \u041f\u0430\u0440\u043e\u043b\u0430" },
	{ "ViaFirewall",        "\u0417\u0430\u0434 \u0437\u0430\u0449\u0438\u0442\u043d\u0430 \u0441\u0442\u0435\u043d\u0430" },
	{ "FWHost",             "\u0421\u044a\u0440\u0432\u0435\u0440 \u043d\u0430 \u0437\u0430\u0449\u0438\u0442\u043d\u0430\u0442\u0430 \u0441\u0442\u0435\u043d\u0430" },
	{ "FWPort",             "\u041f\u043e\u0440\u0442 \u043d\u0430 \u0437\u0430\u0449\u0438\u0442\u043d\u0430\u0442\u0430 \u0441\u0442\u0435\u043d\u0430" },
	{ "TestConnection",     "\u0422\u0435\u0441\u0442 \u043d\u0430 \u0431\u0430\u0437\u0430\u0442\u0430 \u0434\u0430\u043d\u043d\u0438" },
	{ "Type",               "\u0412\u0438\u0434 \u043d\u0430 \u0431\u0430\u0437\u0430\u0442\u0430 \u0434\u0430\u043d\u043d\u0438" },
	{ "BequeathConnection", "\u041b\u043e\u043a\u0430\u043b\u043d\u0430 \u0432\u0440\u044a\u0437\u043a\u0430" },
	{ "Overwrite",          "\u0417\u0430\u043c\u044f\u043d\u0430" },
	{ "ConnectionProfile",	"Connection" },
	{ "LAN",		 		"LAN" },
	{ "TerminalServer",		"Terminal Server" },
	{ "VPN",		 		"VPN" },
	{ "WAN", 				"WAN" },
	{ "ConnectionError",    "\u0413\u0440\u0435\u0448\u043a\u0430 \u043f\u0440\u0438 \u0441\u0432\u044a\u0440\u0437\u0432\u0430\u043d\u0435" },
	{ "ServerNotActive",    "\u0421\u044a\u0440\u0432\u0435\u0440\u044a\u0442 \u043d\u0435 \u0435 \u0430\u043a\u0442\u0438\u0432\u0435\u043d" }
	};

	/**
	 * Get Contsnts
	 * @return contents
	 */
	public Object[][] getContents()
	{
		return contents;
	}
}   //  Res
