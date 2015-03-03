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
 *  @author     Alessandro Riolo 
 *  @version    $Id: DBRes_it.java,v 1.8 2005/11/20 22:40:26 jjanke Exp $
 */
public class DBRes_it extends ListResourceBundle
{
	/** Data        */
	static final Object[][] contents = new String[][]{
	  { "CConnectionDialog",  "Connessione a Compiere" },
	  { "Name",               "Nome" },
	  { "AppsHost",           "Host dell'Applicativo" },
	  { "AppsPort",           "Porta dell'Applicativo" }, 
	  { "TestApps",           "Applicazione di Test" },
	  { "DBHost",             "Host del Database" },
	  { "DBPort",             "Porta del Database" },
	  { "DBName",             "Nome del Database" },
	  { "DBUidPwd",           "Utente / Password" },
	  { "ViaFirewall",        "via Firewall" },
	  { "FWHost",             "Host del Firewall" },
	  { "FWPort",             "Porta del Firewall" },
	  { "TestConnection",     "Database di Test" },
	  { "Type",               "Tipo di Database" },
	  { "BequeathConnection", "Connessione Dedicata" },
	  { "Overwrite",          "Sovrascri" }, 
		{ "ConnectionProfile",	"Connection" },
		{ "LAN",		 		"LAN" },
		{ "TerminalServer",		"Terminal Server" },
		{ "VPN",		 		"VPN" },
		{ "WAN", 				"WAN" },
	  { "ConnectionError",    "Errore di Connessione" },
	  { "ServerNotActive",    "Server non Attivo" }};

	/**
	 * Get Contsnts
	 * @return contents
	 */
	public Object[][] getContents()
	{
		return contents;
	}
}   //  Res
