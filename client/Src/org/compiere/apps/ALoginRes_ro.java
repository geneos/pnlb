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
 * 	@version 	$Id: ALoginRes_ro.java,v 1.7 2005/03/11 20:28:21 jjanke Exp $
 */
public final class ALoginRes_ro extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "Conexiune" },
	{ "Defaults",           "Valori implicite" },
	{ "Login",              "Autentificare" },
	{ "File",               "Aplica\u0163ie" },
	{ "Exit",               "Ie\u015fire" },
	{ "Help",               "Ajutor" },
	{ "About",              "Despre..." },
	{ "Host",               "Server" },
	{ "Database",           "Baz\u0103 de date" },
	{ "User",               "Utilizator" },
	{ "EnterUser",          "Introduce\u0163i identificatorul utilizatorului" },
	{ "Password",           "Parol\u0103" },
	{ "EnterPassword",      "Introduce\u0163i parola" },
	{ "Language",           "Limb\u0103" },
	{ "SelectLanguage",     "Alege\u0163i limba dumneavoastr\u0103" },
	{ "Role",               "Rol" },
	{ "Client",             "Titular" },
	{ "Organization",       "Organiza\u0163ie" },
	{ "Date",               "Dat\u0103" },
	{ "Warehouse",          "Depozit" },
	{ "Printer",            "Imprimant\u0103" },
	{ "Connected",          "Conectat" },
	{ "NotConnected",       "Neconectat" },
	{ "DatabaseNotFound",   "Baza de date nu a fost g\u0103sit\u0103" },
	{ "UserPwdError",       "Parola nu se potrive\u015fte cu utilizatorul" },
	{ "RoleNotFound",       "Rolul nu a fost g\u0103sit sau este incomplet" },
	{ "Authorized",         "Autorizat" },
	{ "Ok",                 "OK" },
	{ "Cancel",             "Anulare" },
	{ "VersionConflict",    "Conflict de versiune:" },
	{ "VersionInfo",        "server <> client" },
	{ "PleaseUpgrade",      "Rula\u0163i programul de actualizare" }
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
