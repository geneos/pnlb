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
 *  Connection Resource Strings (French)
 *
 *  @author     Jean-Luc SCHEIDEGGER
 *  @version    $Id: DBRes_fr.java,v 1.7 2005/11/20 22:40:26 jjanke Exp $
 */
public class DBRes_fr extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "CConnectionDialog",      "Connexion Compiere" },
	{ "Name",                   "Nom" },
	{ "AppsHost",               "Hote d'Application" },
	{ "AppsPort",               "Port de l'Application" },
	{ "TestApps",               "Application de Test" },
	{ "DBHost",                 "Hote Base de Données" },
	{ "DBPort",                 "Port Base de Données" },
	{ "DBName",                 "Nom Base de Données" },
	{ "DBUidPwd",               "Utilisateur / Mot de Passe" },
	{ "ViaFirewall",            "via Firewall" },
	{ "FWHost",                 "Hote Firewall" },
	{ "FWPort",                 "Port Firewall" },
	{ "TestConnection",         "Test Base de Données" },
	{ "Type",                   "Type Base de Données" },
	{ "BequeathConnection",     "Connexion dédiée" },
	{ "Overwrite",              "Ecraser" },
	{ "ConnectionProfile",	"Connection" },
	{ "LAN",		 		"LAN" },
	{ "TerminalServer",		"Terminal Server" },
	{ "VPN",		 		"VPN" },
	{ "WAN", 				"WAN" },
	{ "ConnectionError",        "Erreur Connexion" },
	{ "ServerNotActive",        "Serveur Non Actif" }
	};

	/**
	 * Get Contsnts
	 * @return contents
	 */
	public Object[][] getContents()
	{
		return contents;
	}   //  getContents
}   //  DBRes_fr
