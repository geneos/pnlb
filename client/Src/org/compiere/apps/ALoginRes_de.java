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
 *  German Resource Bundle
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ALoginRes_de.java,v 1.9 2005/12/19 01:16:52 jjanke Exp $
 */
public final class ALoginRes_de extends ListResourceBundle
{
	// TODO Run native2ascii to convert to plain ASCII !! 
	
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",     "Verbindung" },
	{ "Defaults",       "Standard Werte" },
	{ "Login",          "Compiere Anmeldung" },
	{ "File",           "Datei" },
	{ "Exit",           "Beenden" },
	{ "Help",           "Hilfe" },
	{ "About",          "Über" },
	{ "Host",           "Rechner" },
	{ "Database",       "Datenbank" },
	{ "User",           "Benutzer" },
	{ "EnterUser",      "Answendungs-Benutzer eingeben" },
	{ "Password",       "Kennwort" },
	{ "EnterPassword",  "Anwendungs-Kennwort eingeben" },
	{ "Language",       "Sprache" },
	{ "SelectLanguage", "Anwendungs-Sprache auswählen" },
	{ "Role",           "Rolle" },
	{ "Client",         "Mandant" },
	{ "Organization",   "Organisation" },
	{ "Date",           "Datum" },
	{ "Warehouse",      "Lager" },
	{ "Printer",        "Drucker" },
	{ "Connected",      "Verbunden" },
	{ "NotConnected",   "Nicht verbunden" },
	{ "DatabaseNotFound", "Datenbank nicht gefunden" },
	{ "UserPwdError",   "Benutzer und Kennwort stimmen nicht überein" },
	{ "RoleNotFound",   "Rolle nicht gefunden/komplett" },
	{ "Authorized",     "Authorisiert" },
	{ "Ok",             "Ok" },
	{ "Cancel",         "Abbruch" },
	{ "VersionConflict", "Versions Konflikt:" },
	{ "VersionInfo",    "Server <> Arbeitsstation" },
	{ "PleaseUpgrade",  "Bitte das Aktualisierung-Programm (update) starten" }
	};

	/**
	 *  Get Contents
	 *  @return context
	 */
	public Object[][] getContents()
	{
		return contents;
	}   //  getContents
}   //  LoginRes_de
