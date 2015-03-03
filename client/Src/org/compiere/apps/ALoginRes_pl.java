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
 *  @author     Adam Bodurka
 *  @version    $Id: ALoginRes_pl.java,v 1.5 2005/03/11 20:28:21 jjanke Exp $
 */
public final class ALoginRes_pl extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "Po\u0142\u0105czenie" },
	{ "Defaults",           "Domy\u015blne" },
	{ "Login",              "Logowanie" },
	{ "File",               "Plik" },
	{ "Exit",               "Zako\u0144cz" },
	{ "Help",               "Pomoc" },
	{ "About",              "O aplikacji" },
	{ "Host",               "Host" },
	{ "Database",           "Baza danych" },
	{ "User",               "U\u017cytkownik" },
	{ "EnterUser",          "Wprowad\u017a Identyfikator U\u017cytkownika Aplikacji" },
	{ "Password",           "Has\u0142o" },
	{ "EnterPassword",      "Wprowad\u017a Has\u0142o Aplikacji" },
	{ "Language",           "J\u0119zyk" },
	{ "SelectLanguage",     "Wybierz j\u0119zyk" },
	{ "Role",               "Funkcja" },
	{ "Client",             "Klient" },
	{ "Organization",       "Organizacja" },
	{ "Date",               "Data" },
	{ "Warehouse",          "Magazyn" },
	{ "Printer",            "Drukarka" },
	{ "Connected",          "Po\u0142\u0105czony" },
	{ "NotConnected",       "Nie Po\u0142\u0105czony" },
	{ "DatabaseNotFound",   "Nie znaleziono bazy danych" },
	{ "UserPwdError",       "Has\u0142o nie odpowiada U\u017cytkownikowi" },
	{ "RoleNotFound",       "Nie znaleziono zasady" },
	{ "Authorized",         "Autoryzowany" },
	{ "Ok",                 "Ok" },
	{ "Cancel",             "Anuluj" },
	{ "VersionConflict",    "Konflikt wersji:" },
	{ "VersionInfo",        "Serwer <> Klienta" },
	{ "PleaseUpgrade",      "Uruchom now\u0105 wersj\u0119 programu !" }
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
