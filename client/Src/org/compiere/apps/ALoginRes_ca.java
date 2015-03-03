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
 * 	@translator  	Jaume Teixi	
 * 	@version 	$Id: ALoginRes_ca.java,v 1.4 2005/12/19 01:16:52 jjanke Exp $
 */
public final class ALoginRes_ca extends ListResourceBundle
{
	// TODO Run native2ascii to convert to plain ASCII !! 
	
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "Connexió" },
	{ "Defaults",           "Predeterminats" },
	{ "Login",              "Accés Compiere" },
	{ "File",               "Fitxer" },
	{ "Exit",               "Sortir" },
	{ "Help",               "Ajuda" },
	{ "About",              "Referent" },
	{ "Host",               "Servidor" },
	{ "Database",           "Base de Dades" },
	{ "User",               "ID Usuari" },
	{ "EnterUser",          "Entrar ID Usuari Aplicació" },
	{ "Password",           "Contrasenya" },
	{ "EnterPassword",      "Entrar Contrasenya Usuari Aplicació" },
	{ "Language",           "Idioma" },
	{ "SelectLanguage",     "Seleccioneu el Vostre Idioma" },
	{ "Role",               "Rol" },
	{ "Client",             "Client" },
	{ "Organization",       "Organització" },
	{ "Date",               "Data" },
	{ "Warehouse",          "Magatzem" },
	{ "Printer",            "Impressora" },
	{ "Connected",          "Connectat" },
	{ "NotConnected",       "No Connectat" },
	{ "DatabaseNotFound",   "No s'ha trobat la Base de Dades" },
	{ "UserPwdError",       "No coincidèix l'Usuari i la Contrasenya" },
	{ "RoleNotFound",       "Rol no trobat/completat" },
	{ "Authorized",         "Autoritzat" },
	{ "Ok",                 "D'Acord" },
	{ "Cancel",             "Cancel.lar" },
	{ "VersionConflict",    "Conflicte Versions:" },
	{ "VersionInfo",        "Servidor <> Client" },
	{ "PleaseUpgrade",      "Sisplau Actualitzeu el Programa" }
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
