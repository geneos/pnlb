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
 * 	@author 	Matjaž Godec
 * 	@version 	$Id: ALoginRes_sl.java,v 1.4 2005/12/19 01:16:53 jjanke Exp $
 */
public final class ALoginRes_sl extends ListResourceBundle
{
	// TODO Run native2ascii to convert to plain ASCII !! 
	
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "Povezava" },
	{ "Defaults",           "Privzete vrednosti" },
	{ "Login",              "Prijava" },	
	{ "File",               "Datoteka" },
	{ "Exit",               "Izhod" },
	{ "Help",               "Pomo�?" },
	{ "About",              "O programu" },
	{ "Host",               "Strežnik" },
	{ "Database",           "Baza podatkov" },
	{ "User",               "Uporabnik" },
	{ "EnterUser",          "Vpiši uporabnika" },
	{ "Password",           "Geslo" },
	{ "EnterPassword",      "Vpiši geslo" },
	{ "Language",           "Jezik" },
	{ "SelectLanguage",     "Izbira jezika" },
	{ "Role",               "Vloga" },
	{ "Client",             "Podjetje" },
	{ "Organization",       "Organizacija" },
	{ "Date",               "Datum" },
	{ "Warehouse",          "Skladiš�?e" },
	{ "Printer",            "Tiskalnik" },
	{ "Connected",          "Povezano" },
	{ "NotConnected",       "Ni povezano" },
	{ "DatabaseNotFound",   "Ne najdem baze podatkov" },
	{ "UserPwdError",       "Geslo ni pravilno" },
	{ "RoleNotFound",       "Ne najdem izbrane vloge" },
	{ "Authorized",         "Avtoriziran" },
	{ "Ok",                 "V redu" },
	{ "Cancel",             "Prekli�?i" },
	{ "VersionConflict",    "Konflikt verzij" },
	{ "VersionInfo",        "Strežnik <> Odjemalec" },
	{ "PleaseUpgrade",      "Prosim nadgradite program" }
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

