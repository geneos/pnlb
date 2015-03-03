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
 * 	@author 	Gabriele Vivinetto - gabriele.mailing@rvmgroup.it
 * 	@version 	$Id: ALoginRes_it.java,v 1.3 2005/03/11 20:27:58 jjanke Exp $
 */
public final class ALoginRes_it extends ListResourceBundle
{
	static final Object[][] contents = new String[][]{
	//{ "Connection",       "Connection" },
	  { "Connection",       "Connessione" },
	//{ "Defaults",         "Defaults" },
	  { "Defaults",         "Defaults" }, //Need to be checked
	//{ "Login",            "Compiere Login" },
	  { "Login",            "Compiere Login" },
	//{ "File",             "File" },
	  { "File",             "File" },
	//{ "Exit",             "Exit" },
	  { "Exit",             "Esci" },
	//{ "Help",             "Help" },
	  { "Help",             "Aiuto" },
	//{ "About",            "About" },
	  { "About",            "Informazioni" },
	//{ "Host",             "Host" },
	  { "Host",             "Host" },
	//{ "Database",         "Database" },
	  { "Database",         "Database" },
	//{ "User",             "User ID" }, //Need to be checked. Leave "User ID" ?
	  { "User",             "Identificativo Utente" },
	//{ "EnterUser",        "Enter Application User ID" },
	  { "EnterUser",        "Identificativo Utente Applicazione" },
	//{ "Password",         "Password" },
	  { "Password",         "Password" },
	//{ "EnterPassword",    "Enter Application password" },
	  { "EnterPassword",    "Inserimento password Applicazione" },
	//{ "Language",         "Language" },
	  { "Language",         "Linguaggio" },
	//{ "SelectLanguage",   "Select your language" },
	  { "SelectLanguage",   "Selezionate il vostro linguaggio" },
	//{ "Role",             "Role" },
	  { "Role",             "Ruolo" },
	//{ "Client",           "Client" }, //Need to be checked. Everybody agree with the SAP translation ?
	  { "Client",           "Mandante" },
	//{ "Organization",     "Organization" },
	  { "Organization",     "Organizzazione" },
	//{ "Date",             "Date" },
	  { "Date",             "Data" },
	//{ "Warehouse",        "Warehouse" },
	  { "Warehouse",        "Magazzino" },
	//{ "Printer",          "Printer" },
	  { "Printer",          "Stampante" },
	//{ "Connected",        "Connected" },
	  { "Connected",        "Connesso" },
	//{ "NotConnected",     "Not Connected" },
	  { "NotConnected",     "Non Connesso" },
	//{ "DatabaseNotFound", "Database not found" },
	  { "DatabaseNotFound", "Database non trovato" },
	//{ "UserPwdError",     "User does not match password" },
	  { "UserPwdError",     "L'Utente non corrisponde alla password" },
	//{ "RoleNotFound",     "Role not found" },
	  { "RoleNotFound",     "Ruolo non trovato" },
	//{ "Authorized",       "Authorized" },
	  { "Authorized",       "Authorizzato" },
	//{ "Ok",               "Ok" },
	  { "Ok",               "Ok" },
	//{ "Cancel",           "Cancel" },
	  { "Cancel",           "Cancella" },
	//{ "VersionConflict",  "Version Conflict:" },
	  { "VersionConflict",  "Conflitto di Versione:" },
	//{ "VersionInfo",      "Server <> Client" },
	  { "VersionInfo",      "Server <> Client" },
	//{ "PleaseUpgrade",    "Please run the update program" }
	  { "PleaseUpgrade",    "Prego lanciare il programma di update" }
	};
	public Object[][] getContents()
	{
		return contents;
	}
}
