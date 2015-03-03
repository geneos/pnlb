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
 * 	@version 	$Id: ALoginRes_fa.java,v 1.3 2005/03/11 20:27:58 jjanke Exp $
 */
public final class ALoginRes_fa extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "\u0627\u062a\u0635\u0627\u0644" },
	{ "Defaults",           "\u0645\u0642\u0627\u062f\u064a\u0631 \u067e\u064a\u0634 \u0641\u0631\u0636" },
	{ "Login",              "\u0648\u0631\u0648\u062f \u0628\u0647 \u0633\u064a\u0633\u062a\u0645" },
	{ "File",               "\u0641\u0627\u064a\u0644" },
	{ "Exit",               "\u062e\u0631\u0648\u062c" },
	{ "Help",               "\u0631\u0627\u0647\u0646\u0645\u0627\u0626\u06cc" },
	{ "About",              "\u062f\u0631\u0628\u0627\u0631\u0647" },
	{ "Host",               "\u0633\u064a\u0633\u062a\u0645 \u0645\u064a\u0632\u0628\u0627\u0646" },
	{ "Database",           "\u0628\u0627\u0646\u06a9 \u0627\u0637\u0644\u0627\u0639\u0627\u062a" },
	{ "User",               "\u0645\u0634\u062e\u0635\u0647 \u0627\u0633\u062a\u0641\u0627\u062f\u0647 \u06a9\u0646\u0646\u062f\u0647" },
	{ "EnterUser",          "\u0645\u0634\u062e\u0635\u0647 \u0627\u0633\u062a\u0641\u0627\u062f\u0647 \u06a9\u0646\u0646\u062f\u0647 \u0631\u0627 \u0648\u0627\u0631\u062f \u06a9\u0646\u064a\u062f" },
	{ "Password",           "\u06a9\u0644\u0645\u0647 \u0639\u0628\u0648\u0631" },
	{ "EnterPassword",      "\u06a9\u0644\u0645\u0647 \u0639\u0628\u0648\u0631 \u0631\u0627 \u0648\u0627\u0631\u062f \u06a9\u0646\u064a\u062f" },
	{ "Language",           "\u0632\u0628\u0627\u0646" },
	{ "SelectLanguage",     "\u0632\u0628\u0627\u0646 \u0631\u0627 \u0627\u0646\u062a\u062e\u0627\u0628 \u06a9\u0646\u064a\u062f" },
	{ "Role",               "\u0646\u0642\u0634" },
	{ "Client",             "\u0645\u0634\u062a\u0631\u06cc" },
	{ "Organization",       "\u0633\u0627\u0632\u0645\u0627\u0646" },
	{ "Date",               "\u062a\u0627\u0631\u064a\u062e" },
	{ "Warehouse",          "\u0627\u0646\u0628\u0627\u0631 \u06a9\u0627\u0644\u0627" },
	{ "Printer",            "\u0686\u0627\u067e\u06af\u0631" },
	{ "Connected",          "\u0645\u062a\u0635\u0644 \u0634\u062f\u0647" },
	{ "NotConnected",       "\u0645\u062a\u0635\u0644 \u0646\u0634\u062f\u0647" },
	{ "DatabaseNotFound",   "\u0628\u0627\u0646\u06a9 \u0627\u0637\u0644\u0627\u0639\u0627\u062a \u067e\u064a\u062f\u0627 \u0646\u0634\u062f" },
	{ "UserPwdError",       "\u0645\u0634\u062e\u0635\u0647 \u0627\u0633\u062a\u0641\u0627\u062f\u0647 \u06a9\u0646\u0646\u062f\u0647 \u0648 \u06a9\u0644\u0645\u0647 \u0639\u0628\u0648\u0631 \u0628\u0627 \u0647\u0645 \u062a\u0637\u0627\u0628\u0642 \u0646\u062f\u0627\u0631\u0646\u062f" },
	{ "RoleNotFound",       "\u0646\u0642\u0634\u06cc \u067e\u064a\u062f\u0627 \u0646\u0634\u062f" },
	{ "Authorized",         "\u0645\u062c\u0648\u0632 \u062f\u0627\u0631\u062f" },
	{ "Ok",                 "\u062a\u0635\u0648\u064a\u0628" },
	{ "Cancel",             "\u0644\u063a\u0648" },
	{ "VersionConflict",    "\u0646\u0633\u062e\u0647 \u0647\u0627 \u0646\u0627\u0633\u0627\u0632\u06af\u0627\u0631\u0627\u0646\u062f" },
	{ "VersionInfo",        "\u0633\u0631\u0648\u0631 <> \u0645\u0634\u062a\u0631\u06cc" },
	{ "PleaseUpgrade",      "\u0644\u0637\u0641\u0627 \u0628\u0631\u0646\u0627\u0645\u0647 \u0645\u0631\u0628\u0648\u0637 \u0628\u0647 \u062a\u0635\u064a\u062d \u0646\u0633\u062e\u0647 \u0628\u0631\u0646\u0627\u0645\u0647 \u0631\u0627 \u0627\u062c\u0631\u0627 \u06a9\u0646\u064a\u062f" }
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
