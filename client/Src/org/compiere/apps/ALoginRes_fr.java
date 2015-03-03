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
 *  Login Resource Strings (French)
 *
 *  @author     Jean-Luc SCHEIDEGGER
 *  @version    $Id: ALoginRes_fr.java,v 1.8 2005/12/19 01:16:53 jjanke Exp $
 */
public class ALoginRes_fr extends ListResourceBundle
{
	// TODO Run native2ascii to convert to plain ASCII !! 
	
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",             "Connexion" },
	{ "Defaults",               "Défauts" },
	{ "Login",                  "Login Compiere" },
	{ "File",                   "Fichier" },
	{ "Exit",                   "Sortir" },
	{ "Help",                   "Aide" },
	{ "About",                  "A propos de" },
	{ "Host",                   "Serveur" },
	{ "Database",               "Base de données" },
	{ "User",                   "Utilisateur" },
	{ "EnterUser",              "Entrer votre code utilisateur" },
	{ "Password",               "Mot de passe" },
	{ "EnterPassword",          "Entrer le mot de passe" },
	{ "Language",               "Langue" },
	{ "SelectLanguage",         "Sélectionnez votre langue" },
	{ "Role",                   "Rôle" },
	{ "Client",                 "Société" },
	{ "Organization",           "Département" },
	{ "Date",                   "Date" },
	{ "Warehouse",              "Stock" },
	{ "Printer",                "Imprimante" },
	{ "Connected",              "Connecté" },
	{ "NotConnected",           "Non Connecté" },
	{ "DatabaseNotFound",       "Base de données non trouvée" },
	{ "UserPwdError",           "L'utilisateur n'a pas entré de mot de passe" },
	{ "RoleNotFound",           "Rôle non trouvé" },
	{ "Authorized",             "Autorisé" },
	{ "Ok",                     "Ok" },
	{ "Cancel",                 "Annuler" },
	{ "VersionConflict",        "Conflit de Version:" },
	{ "VersionInfo",            "Serveur <> Client" },
	{ "PleaseUpgrade",          "SVP, mettez à jour le programme" }
	};

	/**
	 *  Get Contents
	 *  @return data
	 */
	public Object[][] getContents()
	{
		return contents;
	}   //  getContents
}   //  ALoginRes_fr
