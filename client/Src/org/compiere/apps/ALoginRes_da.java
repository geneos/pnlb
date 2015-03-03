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
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ALoginRes_da.java,v 1.3 2005/12/19 01:16:52 jjanke Exp $
 */
public final class ALoginRes_da extends ListResourceBundle
{
	// TODO Run native2ascii to convert to plain ASCII !! 
	
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "Forbindelse" },
	{ "Defaults",           "Basis" },
	{ "Login",              "Compiere: Log på" },
	{ "File",               "Fil" },
	{ "Exit",               "Afslut" },
	{ "Help",               "Hjælp" },
	{ "About",              "Om" },
	{ "Host",               "Vært" },
	{ "Database",           "Database" },
	{ "User",               "Bruger-ID" },
	{ "EnterUser",          "Angiv bruger-ID til program" },
	{ "Password",           "Adgangskode" },
	{ "EnterPassword",      "Angiv adgangskode til program" },
	{ "Language",           "Sprog" },
	{ "SelectLanguage",     "Vælg sprog" },
	{ "Role",               "Rolle" },
	{ "Client",             "Firma" },
	{ "Organization",       "Organisation" },
	{ "Date",               "Dato" },
	{ "Warehouse",          "Lager" },
	{ "Printer",            "Printer" },
	{ "Connected",          "Forbindelse OK" },
	{ "NotConnected",       "Ingen forbindelse" },
	{ "DatabaseNotFound",   "Database blev ikke fundet" },
	{ "UserPwdError",       "Forkert bruger til adgangskode" },
	{ "RoleNotFound",       "Rolle blev ikke fundet/afsluttet" },
	{ "Authorized",         "Tilladelse OK" },
	{ "Ok",                 "OK" },
	{ "Cancel",             "Annullér" },
	{ "VersionConflict",    "Konflikt:" },
	{ "VersionInfo",        "Server <> Klient" },
	{ "PleaseUpgrade",      "Kør opdateringsprogram" }
	};

	/**
	 *  Get Contents
	 *  @return context
	 */
	public Object[][] getContents()
	{
		return contents;
	}   //  getContents
}   //  ALoginRes_da
 //  ALoginRes-da
