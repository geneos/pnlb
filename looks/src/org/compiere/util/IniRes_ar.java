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
package org.compiere.util;

import java.util.ListResourceBundle;

/**
 *  License Dialog Translation
 *
 *  @author     Jorg Janke
 *  @version    $Id: IniRes_ar.java,v 1.1 2005/12/01 20:30:40 jjanke Exp $
 */
public class IniRes_ar extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Compiere_License",   "\u0637\u00a7\u0638\u201e\u0637\u00a7\u0637\u06be\u0638\u2018\u0638\u067e\u0637\u00a7\u0638\u201a\u0638\u0679\u0638\u2018\u0637\u00a9 \u0638\u02c6 \u0637\u00a7\u0638\u201e\u0637\u06be\u0638\u2018\u0637\u00b1\u0637\u00ae\u0638\u0679\u0637\u00b5" },
	{ "Do_you_accept",      "?\u0638\u2021\u0638\u201e \u0637\u06be\u0638\u201a\u0637\u00a8\u0638\u201e \u0638\u2021\u0637\u00b0\u0637\u00a7 \u0637\u00a7\u0638\u201e\u0637\u06be\u0638\u2018\u0637\u00b1\u0637\u00ae\u0638\u0679\u0637\u00b5" },
	{ "No",                          "\u0638\u201e\u0637\u00a7" },
	{ "Yes_I_Understand",       "\u0638\u2020\u0637\u00b9\u0638\u2026, \u0637\u00a3\u0638\u067e\u0638\u2021\u0638\u2026 \u0638\u02c6 \u0637\u00a3\u0638\u201a\u0637\u00a8\u0638\u201e"},
	{ "license_htm",        "org/compiere/license.htm" },
	{ "License_rejected",   "\u0637\u00b1\u0638\u067e\u0637\u00b6 \u0637\u00a7\u0638\u201e\u0637\u00b1\u0638\u2018\u0637\u00ae\u0637\u00b5\u0637\u00a9 \u0637\u00a3\u0638\u02c6 \u0637\u00a7\u0638\u2020\u0637\u06be\u0638\u2021\u0637\u00a7\u0637\u00a6\u0638\u2021\u0637\u00a7" }
	};

	/**
	 *  Get Content
	 *  @return Content
	 */
	public Object[][] getContents()
	{
		return contents;
	}   //  getContent
}   //  IniRes
