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
 *  @author     .
 *  @version    $Id: IniRes_fa.java,v 1.5 2005/03/11 20:34:38 jjanke Exp $
 */
public class IniRes_fa extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Compiere_License",   "\u0642\u0628\u0648\u0644 \u0645\u062c\u0648\u0632 \u0646\u0631\u0645 \u0627\u0641\u0632\u0627\u0631" },
	{ "Do_you_accept",      "\u0622\u064a\u0627 \u0645\u062c\u0648\u0632 \u0646\u0631\u0645 \u0627\u0641\u0632\u0627\u0631 \u0631\u0627 \u0642\u0628\u0648\u0644 \u062f\u0627\u0631\u064a\u062f\u061f" },
	{ "No",                 "\u062e\u064a\u0631" },
	{ "Yes_I_Understand",   "\u0628\u0644\u0647 \u0645\u0646 \u0645\u062c\u0648\u0632 \u0631\u0627 \u0641\u0647\u0645\u064a\u062f\u0647 \u0648 \u0642\u0628\u0648\u0644 \u062f\u0627\u0631\u0645" },
	{ "license_htm",        "org/compiere/license.htm" },
	{ "License_rejected",   "\u0645\u062c\u0648\u0632 \u0645\u0648\u0631\u062f \u0642\u0628\u0648\u0644 \u0648\u0627\u0642\u0639 \u0646\u0634\u062f \u064a\u0627 \u0645\u0646\u0642\u0636\u06cc \u0634\u062f\u0647" }
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
