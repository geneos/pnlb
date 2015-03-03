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
 *  @author     ZhaoXing Meng
 *  @version    $Id: IniRes_zh_CN.java,v 1.6 2005/03/11 20:34:37 jjanke Exp $
 */
public class IniRes_zh_CN extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Compiere_License",   "\u6700\u7ec8\u7528\u6237\u8bb8\u53ef\u534f\u8bae" },
	{ "Do_you_accept",      "\u60a8\u540c\u610f\u6b64\u534f\u8bae\u5417\uff1f" },
	{ "No",                 "\u4e0d" },
	{ "Yes_I_Understand",   "\u662f\uff0c\u6211\u540c\u610f\u5e76\u4e14\u63a5\u53d7" },
	{ "license_htm",        "org/compiere/license.htm" },
	{ "License_rejected",   "\u6743\u9650\u88ab\u62d2\u7edd\u6216\u5df2\u8fc7\u671f" }
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
