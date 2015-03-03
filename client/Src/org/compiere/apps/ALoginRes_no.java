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
 *  Norwegian Base Resource Bundle Translation
 *
 * 	@author 	Olaf Slazak Løken
 * 	@version 	$Id: ALoginRes_no.java,v 1.4 2005/12/19 01:16:52 jjanke Exp $
 */
public final class ALoginRes_no extends ListResourceBundle
{
	// TODO Run native2ascii to convert to plain ASCII !! 
	
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "Forbindelse" },
	{ "Defaults",           "Vanlige" },
	{ "Login",              "Compiere Loginn" },
	{ "File",               "Fil" },
	{ "Exit",               "Avslutt" },
	{ "Help",               "Hjelp" },
	{ "About",              "Om" },
	{ "Host",               "Maskin" },
	{ "Database",           "Database" },
	{ "User",               "Bruker ID" },
	{ "EnterUser",          "Skriv  Applikasjon Bruker ID" },
	{ "Password",           "Passord" },
	{ "EnterPassword",      "Skriv Applikasjon Passordet" },
	{ "Language",           "Språk" },
	{ "SelectLanguage",     "Velg ønsket Språk" },
	{ "Role",               "Rolle" },
	{ "Client",             "Klient" },
	{ "Organization",       "Organisasjon" },
	{ "Date",               "Dato" },
	{ "Warehouse",          "Varehus" },
	{ "Printer",            "Skriver" },
	{ "Connected",          "Oppkoblett" },
	{ "NotConnected",       "Ikke Oppkoblet" },
	{ "DatabaseNotFound",   "Database ikke funnet" },
	{ "UserPwdError",       "Bruker passer ikke til passordet" },
	{ "RoleNotFound",       "Role not found/complete" },
	{ "Authorized",         "Autorisert" },
	{ "Ok",                 "Ok" },
	{ "Cancel",             "Avbryt" },
	{ "VersionConflict",    "Versions Konflikt:" },
	{ "VersionInfo",        "Server <> Klient" },
	{ "PleaseUpgrade",      "Vennligst kjør oppdaterings programet" }
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
