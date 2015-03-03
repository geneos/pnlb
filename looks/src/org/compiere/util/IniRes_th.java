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
 *  License Dialog Translation (Thai)
 *
 *  @author     Sureeraya Limpaibul
 *  @version    $Id: IniRes_th.java,v 1.6 2005/03/11 20:34:38 jjanke Exp $
 */
public class IniRes_th extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Compiere_License",   "\u0e02\u0e49\u0e2d\u0e15\u0e01\u0e25\u0e07\u0e17\u0e32\u0e07\u0e25\u0e34\u0e02\u0e2a\u0e34\u0e17\u0e18\u0e34\u0e4c" },
	{ "Do_you_accept",      "\u0e17\u0e48\u0e32\u0e19\u0e44\u0e14\u0e49\u0e22\u0e2d\u0e21\u0e23\u0e31\u0e1a\u0e25\u0e34\u0e02\u0e2a\u0e34\u0e17\u0e18\u0e34\u0e4c\u0e2b\u0e23\u0e37\u0e2d\u0e44\u0e21\u0e48" },
	{ "No",                 "\u0e44\u0e21\u0e48\u0e22\u0e2d\u0e21\u0e23\u0e31\u0e1a" },
	{ "Yes_I_Understand",   "\u0e22\u0e2d\u0e21\u0e23\u0e31\u0e1a, \u0e02\u0e49\u0e32\u0e1e\u0e40\u0e08\u0e49\u0e32\u0e21\u0e35\u0e04\u0e27\u0e32\u0e21\u0e40\u0e02\u0e49\u0e32\u0e43\u0e08\u0e41\u0e25\u0e30\u0e22\u0e2d\u0e21\u0e23\u0e31\u0e1a\u0e15\u0e32\u0e21\u0e02\u0e49\u0e2d\u0e15\u0e01\u0e25\u0e07\u0e17\u0e32\u0e07\u0e25\u0e34\u0e02\u0e2a\u0e34\u0e17\u0e18\u0e34\u0e4c\u0e19\u0e35\u0e49" },
	{ "license_htm",        "org/compiere/license.htm" },
	{ "License_rejected",   "\u0e25\u0e34\u0e02\u0e2a\u0e34\u0e17\u0e18\u0e34\u0e4c" }
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
