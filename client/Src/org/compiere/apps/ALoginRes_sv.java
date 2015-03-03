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
 *  Swedish Base Resource Bundle Translation
 *
 * 	@author 	Thomas Dilts
 * 	@version 	$Id: ALoginRes_sv.java,v 1.4 2005/12/19 01:16:53 jjanke Exp $
 */
public final class ALoginRes_sv extends ListResourceBundle
{
	// TODO Run native2ascii to convert to plain ASCII !! 
	
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "Anslutning" },
	{ "Defaults",           "Standardinställningar" },
	{ "Login",              "Compiere inloggning" },
	{ "File",               "Fil" },
	{ "Exit",               "Avsluta" },
	{ "Help",               "Hjälp" },
	{ "About",              "Om" },
	{ "Host",               "Värddatorn" },
	{ "Database",           "Databas" },
	{ "User",               "Användarnamn" },
	{ "EnterUser",          "Ange program användarnamn" },
	{ "Password",           "Lösenord" },
	{ "EnterPassword",      "Ange program lösenord" },
	{ "Language",           "Språk" },
	{ "SelectLanguage",     "Välj ditt språk" },
	{ "Role",               "Roll" },
	{ "Client",             "Klient" },
	{ "Organization",       "Organisation" },
	{ "Date",               "Datum" },
	{ "Warehouse",          "Lager" },
	{ "Printer",            "Skrivare" },
	{ "Connected",          "Anslutad" },
	{ "NotConnected",       "Ej ansluten" },
	{ "DatabaseNotFound",   "Hittade inte databasen" },
	{ "UserPwdError",       "User does not match password" },
	{ "RoleNotFound",       "Hittade inte rollen" },
	{ "Authorized",         "Auktoriserad" },
	{ "Ok",                 "Ok" },
	{ "Cancel",             "Avbryt" },
	{ "VersionConflict",    "Versionskonflikt:" },
	{ "VersionInfo",        "Server <> Klient" },
	{ "PleaseUpgrade",      "Kör updateringsprogram" }
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
