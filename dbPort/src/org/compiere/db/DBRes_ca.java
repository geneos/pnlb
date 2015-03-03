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
 *  @author Jaume Teixi
 *  @version    $Id: DBRes_ca.java,v 1.6 2005/11/20 22:40:26 jjanke Exp $
 */
public class DBRes_ca extends ListResourceBundle
{
	/** Data        */
	static final Object[][] contents = new String[][]
	{
	{ "CConnectionDialog",  "Connexió Compiere" },
	{ "Name",               "Nom" },
	{ "AppsHost",           "Servidor Aplicació" },
	{ "AppsPort",           "Port Aplicació" },
	{ "TestApps",           "Provar Aplicació" },
	{ "DBHost",             "Servidor Base de Dades" },
	{ "DBPort",             "Port Base de Dades" },
	{ "DBName",             "Nom Base de Dades" },
	{ "DBUidPwd",           "Usuari / Contrasenya" },
	{ "ViaFirewall",        "via Tallafocs" },
	{ "FWHost",             "Servidor Tallafocs" },
	{ "FWPort",             "Port Tallafocs" },
	{ "TestConnection",     "Provar Base de Dades" },
	{ "Type",               "Tipus Base de Dades" },
	{ "BequeathConnection", "Delegar Connexió" },
	{ "Overwrite",          "Sobrescriure" },
	{ "ConnectionProfile",	"Connection" },
	{ "LAN",		 		"LAN" },
	{ "TerminalServer",		"Terminal Server" },
	{ "VPN",		 		"VPN" },
	{ "WAN", 				"WAN" },
	{ "ConnectionError",    "Error Connexió" },
	{ "ServerNotActive",    "Servidor No Actiu" }
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
