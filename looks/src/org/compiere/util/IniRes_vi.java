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
 *  @author     Bui Chi Trung
 *  @version    $Id: IniRes_vi.java,v 1.6 2005/03/11 20:34:38 jjanke Exp $
 */
public class IniRes_vi extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Compiere_License",   "Gi\u1EA5y phép s\u1EED d\u1EE5ng" },
	{ "Do_you_accept",      "B\u1EA1n có ch\u1EA5p nh\u1EADn gi\u1EA5y phép này không?" },
	{ "No",                 "Không" },
	{ "Yes_I_Understand",   "Vâng, tôi hi\u1EC3u và ch\u1EA5p nh\u1EADn gi\u1EA5p phép này" },
	{ "license_htm",        "org/compiere/license.htm" },
	{ "License_rejected",   "Gi\u1EA5y phép b\u1ECB kh\u01B0\u1EDBc t\u1EEB ho\u1EB7c h\u1EBFt h\u1EA1n" }
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
