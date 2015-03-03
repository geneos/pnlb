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
 *  @author     Vyacheslav Pedak
 *  @version    $Id: IniRes_ru.java,v 1.5 2005/03/11 20:34:38 jjanke Exp $
 */
public class IniRes_ru extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Compiere_License",   "\u041b\u0438\u0446\u0435\u043d\u0437\u0438\u043e\u043d\u043d\u043e\u0435 \u0441\u043e\u0433\u043b\u0430\u0448\u0435\u043d\u0438\u0435" },
	{ "Do_you_accept",      "\u0412\u044b \u043f\u0440\u0438\u043d\u0438\u043c\u0430\u0435\u0442\u0435 \u043b\u0438\u0446\u0435\u043d\u0437\u0438\u043e\u043d\u043d\u043e\u0435 \u0441\u043e\u0433\u043b\u0430\u0448\u0435\u043d\u0438\u0435?" },
	{ "No",                 "\u041d\u0435\u0442" },
	{ "Yes_I_Understand",   "\u0414\u0430, \u042f \u043f\u0440\u0438\u043d\u0438\u043c\u0430\u044e" },
	{ "license_htm",        "org/compiere/license.htm" },
	{ "License_rejected",   "\u041b\u0438\u0446\u0435\u043d\u0437\u0438\u044f \u043d\u0435 \u043f\u0440\u0438\u043d\u044f\u0442\u0430 \u0438\u043b\u0438 \u0438\u0441\u0442\u0435\u043a \u0441\u0440\u043e\u043a \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u044f" }
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
