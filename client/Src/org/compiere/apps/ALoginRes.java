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
 *  Base Resource Bundle.
 *  If you translate it, make sure that you convert the file to ASCII via
 *  native2ascii 
 *  http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/native2ascii.html
 *  The non ASCII characters need to be converted to unicode - e.g. \u00ab
 *  This makes it less readable in the source, but viewable for everyone
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ALoginRes.java,v 1.10 2005/12/19 01:16:53 jjanke Exp $
 */
public final class ALoginRes extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "Connection" },
	{ "Defaults",           "Defaults" },
	{ "Login",              "Compiere Login" },
	{ "File",               "&File" },
	{ "Exit",               "Exit" },
	{ "Help",               "&Help" },
	{ "About",              "About" },
	{ "Host",               "&Server" },
	{ "Database",           "Database" },
	{ "User",               "&User ID" },
	{ "EnterUser",          "Enter Application User ID" },
	{ "Password",           "&Password" },
	{ "EnterPassword",      "Enter Application Password" },
	{ "Language",           "&Language" },
	{ "SelectLanguage",     "Select your language" },
	{ "Role",               "&Role" },
	{ "Client",             "&Client" },
	{ "Organization",       "&Organization" },
	{ "Date",               "&Date" },
	{ "Warehouse",          "&Warehouse" },
	{ "Printer",            "Prin&ter" },
	{ "Connected",          "Connected" },
	{ "NotConnected",       "Not Connected" },
	{ "DatabaseNotFound",   "Database not found" },
	{ "UserPwdError",       "User does not match password" },
	{ "RoleNotFound",       "Role not found/complete" },
	{ "Authorized",         "Authorized" },
	{ "Ok",                 "&Ok" },
	{ "Cancel",             "&Cancel" },
	{ "VersionConflict",    "Version Conflict:" },
	{ "VersionInfo",        "Server <> Client" },
	{ "PleaseUpgrade",      "Please download new Version from Server" }
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
