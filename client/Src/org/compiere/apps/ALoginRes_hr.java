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
 * 	@author 	Marko Bubalo
 * 	@version 	$Id: ALoginRes_hr.java,v 1.4 2005/12/19 01:16:52 jjanke Exp $
 */
public final class ALoginRes_hr extends ListResourceBundle
{
	// TODO Run native2ascii to convert to plain ASCII !! 
	
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "Veza" },
	{ "Defaults",           "Uobièajeno" },
	{ "Login",              "Compiere Login" },	
	{ "File",               "Datoteka" },
	{ "Exit",               "Izlaz" },
	{ "Help",               "Pomoæ" },
	{ "About",              "O programu" },
	{ "Host",               "Host" },
	{ "Database",           "Baza podataka" },
	{ "User",               "Korisnik" },
	{ "EnterUser",          "Unos korisnika" },
	{ "Password",           "Lozinka" },
	{ "EnterPassword",      "Unos lozinke" },
	{ "Language",           "Jezika" },
	{ "SelectLanguage",     "Izbor jezika" },
	{ "Role",               "Uloga" },
	{ "Client",             "Klijent" },
	{ "Organization",       "Organizacija" },
	{ "Date",               "Datum" },
	{ "Warehouse",          "Skladište" },
	{ "Printer",            "Pisac" },
	{ "Connected",          "Spojeno" },
	{ "NotConnected",       "Nije spojeno" },
	{ "DatabaseNotFound",   "Baza podataka nije pronadena" },
	{ "UserPwdError",       "Lozinka ne odgovara korisniku" },
	{ "RoleNotFound",       "Uloga nije pronadena" },
	{ "Authorized",         "Autoriziran" },
	{ "Ok",                 "U redu" },
	{ "Cancel",             "Otkazati" },
	{ "VersionConflict",    "Konflikt verzija" },
	{ "VersionInfo",        "Server <> Klijent" },
	{ "PleaseUpgrade",      "Molim pokrenite nadogradnju programa" }
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

