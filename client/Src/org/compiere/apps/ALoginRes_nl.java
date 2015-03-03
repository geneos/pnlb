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
 *  Base Resource Bundle
 *
 * 	@author 	Eldir Tomassen
 * 	@version 	$Id: ALoginRes_nl.java,v 1.5 2005/03/11 20:27:58 jjanke Exp $
 */
public final class ALoginRes_nl extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "Verbinding" },
	{ "Defaults",           "Standaard" },
	{ "Login",              "Aanmelden bij Compiere" },
	{ "File",               "Bestand" },
	{ "Exit",               "Afsluiten" },
	{ "Help",               "Help" },
	{ "About",              "Info" },
	{ "Host",               "Server" },
	{ "Database",           "Database" },
	{ "User",               "Gebruikersnaam" },
	{ "EnterUser",          "Voer uw gebruikersnaam in" },
	{ "Password",           "Wachtwoord" },
	{ "EnterPassword",      "Voer uw wachtwoord in" },
	{ "Language",           "Taal" },
	{ "SelectLanguage",     "Selecteer uw taal" },
	{ "Role",               "Rol" },
	{ "Client",             "Client" },
	{ "Organization",       "Organisatie" },
	{ "Date",               "Datum" },
	{ "Warehouse",          "Magazijn" },
	{ "Printer",            "Printer" },
	{ "Connected",          "Verbonden" },
	{ "NotConnected",       "Niet verbonden" },
	{ "DatabaseNotFound",   "Database niet gevonden" },
	{ "UserPwdError",       "Foute gebruikersnaam of wachtwoord" },
	{ "RoleNotFound",       "Rol niet gevonden of incompleet" },
	{ "Authorized",         "Geautoriseerd" },
	{ "Ok",                 "Ok" },
	{ "Cancel",             "Annuleren" },
	{ "VersionConflict",    "Versie Conflict:" },
	{ "VersionInfo",        "Server <> Client" },
	{ "PleaseUpgrade",      "Uw Compiere installatie dient te worden bijgewerkt." }
	};

	/**
	 *  Get Contents
	 *  @return context
	 */
	public Object[][] getContents()
	{
		return contents;
	}   //  getContents
}   //  ALoginRes
