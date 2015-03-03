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
package org.compiere.apps;

import java.util.*;

/**
 *  Login Resource Bundle (Thai).
 *  native2ascii -encoding MS874 ALoginRes_th.java ALoginRes_th.java.txt
 *
 * 	@author 	Sureeraya Limpaibul
 * 	@version 	$Id: ALoginRes_th.java,v 1.5 2005/05/11 06:21:03 sureeraya Exp $
 */
public final class ALoginRes_th extends ListResourceBundle
{
	static final Object[][] contents = new String[][]
	{
	{ "Connection", "\u0e01\u0e32\u0e23\u0e40\u0e0a\u0e37\u0e48\u0e2d\u0e21\u0e15\u0e48\u0e2d" },
	{ "Defaults", "\u0e04\u0e48\u0e32\u0e42\u0e14\u0e22\u0e1b\u0e23\u0e34\u0e22\u0e32\u0e22" },
	{ "Login", "Compiere Login" },
	{ "File", "\u0e41\u0e1f\u0e49\u0e21\u0e02\u0e49\u0e2d\u0e21\u0e39\u0e25" },
	{ "Exit", "\u0e2d\u0e2d\u0e01\u0e08\u0e32\u0e01\u0e23\u0e30\u0e1a\u0e1a" },
	{ "Help", "\u0e23\u0e30\u0e1a\u0e1a\u0e0a\u0e48\u0e27\u0e22\u0e40\u0e2b\u0e25\u0e37\u0e2d" },
	{ "About", "\u0e40\u0e01\u0e35\u0e48\u0e22\u0e27\u0e01\u0e31\u0e1a\u0e42\u0e1b\u0e23\u0e41\u0e01\u0e23\u0e21" },
	{ "Host", "\u0e42\u0e2e\u0e2a" },
	{ "Database", "\u0e23\u0e30\u0e1a\u0e1a\u0e10\u0e32\u0e19\u0e02\u0e49\u0e2d\u0e21\u0e39\u0e25" },
	{ "User", "\u0e23\u0e2b\u0e31\u0e2a\u0e1c\u0e39\u0e49\u0e43\u0e0a\u0e49" },
	{ "EnterUser", "\u0e01\u0e23\u0e38\u0e13\u0e32\u0e43\u0e2a\u0e48\u0e23\u0e2b\u0e31\u0e2a\u0e1c\u0e39\u0e49\u0e43\u0e0a\u0e49" },
	{ "Password", "\u0e23\u0e2b\u0e31\u0e2a\u0e1c\u0e48\u0e32\u0e19" },
	{ "EnterPassword", "\u0e01\u0e23\u0e38\u0e13\u0e32\u0e43\u0e2a\u0e48\u0e23\u0e2b\u0e31\u0e2a\u0e1c\u0e48\u0e32\u0e19" },
	{ "Language", "\u0e20\u0e32\u0e29\u0e32" },
	{ "SelectLanguage", "\u0e42\u0e1b\u0e23\u0e14\u0e40\u0e25\u0e37\u0e2d\u0e01\u0e20\u0e32\u0e29\u0e32" },
	{ "Role", "\u0e1a\u0e17\u0e1a\u0e32\u0e17/\u0e2b\u0e19\u0e49\u0e32\u0e17\u0e35\u0e48" },
	{ "Client", "\u0e1a\u0e23\u0e34\u0e29\u0e31\u0e17" },
	{ "Organization", "\u0e2a\u0e32\u0e02\u0e32" },
	{ "Date", "\u0e27\u0e31\u0e19\u0e17\u0e35\u0e48" },
	{ "Warehouse", "\u0e04\u0e25\u0e31\u0e07\u0e2a\u0e34\u0e19\u0e04\u0e49\u0e32" },
	{ "Printer", "\u0e40\u0e04\u0e23\u0e37\u0e48\u0e2d\u0e07\u0e1e\u0e34\u0e21\u0e1e\u0e4c" },
	{ "Connected", "\u0e40\u0e0a\u0e37\u0e48\u0e2d\u0e21\u0e15\u0e48\u0e2d\u0e41\u0e25\u0e49\u0e27" },
	{ "NotConnected", "\u0e44\u0e21\u0e48\u0e2a\u0e32\u0e21\u0e32\u0e23\u0e16\u0e40\u0e0a\u0e37\u0e48\u0e2d\u0e21\u0e15\u0e48\u0e2d\u0e44\u0e14\u0e49" },
	{ "DatabaseNotFound", "\u0e44\u0e21\u0e48\u0e1e\u0e1a\u0e10\u0e32\u0e19\u0e02\u0e49\u0e2d\u0e21\u0e39\u0e25" },
	{ "UserPwdError", "\u0e23\u0e2b\u0e31\u0e2a\u0e1c\u0e39\u0e49\u0e43\u0e0a\u0e49\u0e44\u0e21\u0e48\u0e15\u0e23\u0e07\u0e01\u0e31\u0e1a\u0e23\u0e2b\u0e31\u0e2a\u0e1c\u0e48\u0e32\u0e19" },
	{ "RoleNotFound", "\u0e44\u0e21\u0e48\u0e1e\u0e1a\u0e1a\u0e17\u0e1a\u0e32\u0e17/\u0e2b\u0e19\u0e49\u0e32\u0e17\u0e35\u0e48\u0e19\u0e35\u0e49" },
	{ "Authorized", "\u0e2d\u0e19\u0e38\u0e21\u0e31\u0e15\u0e34\u0e41\u0e25\u0e49\u0e27" },
	{ "Ok", "\u0e15\u0e01\u0e25\u0e07" },
	{ "Cancel", "\u0e22\u0e01\u0e40\u0e25\u0e34\u0e01" },
	{ "VersionConflict", "\u0e40\u0e27\u0e2d\u0e23\u0e4c\u0e0a\u0e31\u0e48\u0e19\u0e44\u0e21\u0e48\u0e15\u0e23\u0e07\u0e01\u0e31\u0e19:" },
	{ "VersionInfo", "\u0e40\u0e0b\u0e34\u0e23\u0e4c\u0e1f\u0e40\u0e27\u0e2d\u0e23\u0e4c <> \u0e44\u0e04\u0e25\u0e41\u0e2d\u0e19\u0e17\u0e4c" },
	{ "PleaseUpgrade", "\u0e01\u0e23\u0e38\u0e13\u0e32\u0e23\u0e31\u0e19\u0e2d\u0e31\u0e1e\u0e40\u0e14\u0e17\u0e42\u0e1b\u0e23\u0e41\u0e01\u0e23\u0e21" }
	};

	/**
	 *  Get Contents
	 *  @return context
	 */
	public Object[][] getContents()
	{
		return contents;
	}   //  getContents
}   //  ALoginRes_Th
