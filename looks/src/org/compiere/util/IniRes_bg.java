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
 *  @author     Plamen Niikolov
 *  @version    $Id: IniRes_bg.java,v 1.6 2005/03/11 20:34:37 jjanke Exp $
 */
public class IniRes_bg extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Compiere_License",   "\u041b\u0438\u0446\u0435\u043d\u0437\u043d\u043e \u0441\u043f\u043e\u0440\u0430\u0437\u0443\u043c\u0435\u043d\u0438\u0435" },
	{ "Do_you_accept",      "\u0421\u044a\u0433\u043b\u0430\u0441\u043d\u0438 \u043b\u0438 \u0441\u0442\u0435 \u0441 \u043b\u0438\u0446\u0435\u043d\u0437\u043d\u043e\u0442\u043e \u0441\u043f\u043e\u0440\u0430\u0437\u0443\u043c\u0435\u043d\u0438\u0435 ?" },
	{ "No",                 "\u041d\u0435" },
	{ "Yes_I_Understand",   "\u0414\u0430, \u0440\u0430\u0431\u0438\u0440\u0430\u043c \u0433\u043e \u0438 \u0433\u043e \u043f\u0440\u0438\u0435\u043c\u0430\u043c" },
	{ "license_htm",        "org/compiere/license.htm" },
	{ "License_rejected",   "\u041b\u0438\u0446\u0435\u043d\u0437\u0430 \u0435 \u043e\u0442\u043a\u0430\u0437\u0430\u043d \u0438\u043b\u0438 \u0441\u0432\u044a\u0440\u0448\u0438\u043b" }
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
