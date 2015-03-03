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
 *  @author     Bui Chi Trung
 *  @version    $Id: DBRes_vi.java,v 1.7 2005/11/20 22:40:26 jjanke Exp $
 */
public class DBRes_vi extends ListResourceBundle
{
	/** Data        */
	static final Object[][] contents = new String[][]
	{
	{ "CConnectionDialog",  "K\u1EBFt n\u1ED1i" },
	{ "Name",               "Tên" },
	{ "AppsHost",           "Máy ch\u1EE7 \u1EE9ng d\u1EE5ng" },
	{ "AppsPort",           "C\u1ED5ng \u1EE9ng d\u1EE5ng" },
	{ "TestApps",           "Th\u1EED nghi\u1EC7m \u1EE9ng d\u1EE5ng" },
	{ "DBHost",             "Máy ch\u1EE7 CSDL" },
	{ "DBPort",             "C\u1ED5ng CSDL" },
	{ "DBName",             "Tên CSDL" },
	{ "DBUidPwd",           "Ng\u01B0\u1EDDi dùng / M\u1EADt kh\u1EA9u" },
	{ "ViaFirewall",        "Qua b\u1EE9c t\u01B0\u1EDDng l\u1EEDa" },
	{ "FWHost",             "Máy ch\u1EE7 b\u1EE9c t\u01B0\u1EDDng l\u1EEDa" },
	{ "FWPort",             "C\u1ED5ng vào b\u1EE9c t\u01B0\u1EDDng l\u1EEDa" },
	{ "TestConnection",     "Ki\u1EC3m tra CSDL" },
	{ "Type",               "Lo\u1EA1i CSDL" },
	{ "BequeathConnection", "Truy\u1EC1n l\u1EA1i k\u1EBFt n\u1ED1i" },
	{ "Overwrite",          "Ghi \u0111è" },
	{ "ConnectionProfile",	"Connection" },
	{ "LAN",		 		"LAN" },
	{ "TerminalServer",		"Terminal Server" },
	{ "VPN",		 		"VPN" },
	{ "WAN", 				"WAN" },
	{ "ConnectionError",    "L\u1ED7i k\u1EBFt n\u1ED1i" },
	{ "ServerNotActive",    "Máy ch\u1EE7 hi\u1EC7n không ho\u1EA1t \u0111\u1ED9ng" }
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
