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
 *  @author     Matjaž Godec
 *  @version    $Id: DBRes_sl.java,v 1.4 2005/11/20 22:40:26 jjanke Exp $
 */
public class DBRes_sl extends ListResourceBundle
{
	/** Data        */
	static final Object[][] contents = new String[][]{
	{ "CConnectionDialog", 	"Compiere povezava" },
	{ "Name", 				"Ime" },
	{ "AppsHost", 			"Programski strežnik" },
	{ "AppsPort", 			"Vrata programskega strežnika" },
	{ "TestApps", 			"Test programskega strežnika" },
	{ "DBHost", 			"Strežnik baze podatkov" },
	{ "DBPort", 			"Vrata baze podatkov" },
	{ "DBName", 			"Ime baze podatkov" },
	{ "DBUidPwd", 			"Uporabnik / geslo" },
	{ "ViaFirewall", 		"Skozi požarni zid" },
	{ "FWHost", 			"Požarni zid" },
	{ "FWPort", 			"Vrata požarnega zidu" },
	{ "TestConnection", 	"Testiranje baze podatkov" },
	{ "Type", 				"Tip baze podatkov" },
	{ "BequeathConnection", "Bequeath Connection" },
	{ "Overwrite", 			"Prepiši" },
	{ "ConnectionProfile",	"Connection" },
	{ "LAN",		 		"LAN" },
	{ "TerminalServer",		"Terminal Server" },
	{ "VPN",		 		"VPN" },
	{ "WAN", 				"WAN" },
	{ "ConnectionError", 	"Napaka na povezavi" },
	{ "ServerNotActive", 	"Strežnik ni aktiven" }
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

