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
 *  @author     Stefan Christians 
 *  @version    $Id: IniRes_ja.java,v 1.3 2005/03/11 20:34:37 jjanke Exp $
 */
public class IniRes_ja extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Compiere_License",   "\u30e9\u30a4\u30bb\u30f3\u30b9" },
	{ "Do_you_accept",      "\u4ed4\u306e\u30e9\u30a4\u30bb\u30f3\u30b9\u306b\u8cdb\u6210\u3057\u307e\u3059\u304b\uff1f" },
	{ "No",                 "\u3044\u3048" },
	{ "Yes_I_Understand",   "\u306f\u3044" },
	{ "license_htm",        "org/compiere/license.htm" },
	{ "License_rejected",   "\u7121\u52b9\u306e\u30e9\u30a4\u30bb\u30f3\u30b9" }
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
