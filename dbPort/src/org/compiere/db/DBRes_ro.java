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
 *  @version    $Id: DBRes_ro.java,v 1.7 2005/11/20 22:40:26 jjanke Exp $
 */
public class DBRes_ro extends ListResourceBundle
{
	/** Data        */
	static final Object[][] contents = new String[][]{
	{ "CConnectionDialog", 	"Conexiune" },
	{ "Name", 				"Nume" },
	{ "AppsHost", 			"Server de aplica\u0163ie" },
	{ "AppsPort", 			"Port de aplica\u0163ie" },
	{ "TestApps", 			"Testare a serverului de aplica\u0163ie" },
	{ "DBHost", 			"Server de baz\u0103 de date" },
	{ "DBPort", 			"Port de baz\u0103 de date" },
	{ "DBName", 			"Numele bazei de date" },
	{ "DBUidPwd", 			"Utilizator / parol\u0103" },
	{ "ViaFirewall", 		"Prin firewall" },
	{ "FWHost", 			"Gazd\u0103 de firewall" },
	{ "FWPort", 			"Port de firewall" },
	{ "TestConnection", 	"Testare a bazei de date" },
	{ "Type", 				"Tip al bazei de date" },
	{ "BequeathConnection", "Cedare de conexiune" },
	{ "Overwrite", 			"Suprascriere" },
	{ "ConnectionProfile",	"Connection" },
	{ "LAN",		 		"LAN" },
	{ "TerminalServer",		"Terminal Server" },
	{ "VPN",		 		"VPN" },
	{ "WAN", 				"WAN" },
	{ "ConnectionError", 	"Eroare de conexiune" },
	{ "ServerNotActive", 	"Serverul este inactiv" }
	};

	/**
	 * Get Contents
	 * @return contents
	 */
	public Object[][] getContents()
	{
		return contents;
	}   //  getContent
}   //  Res
