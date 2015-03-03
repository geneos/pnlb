/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): PT. RFID INDONESIA (info@rfid-indonesia.com)_______________.
 *****************************************************************************/
package org.compiere.util;

import java.util.ListResourceBundle;

/**
 *  License Dialog Translation
 *
 *  @author     Halim Englen
 *  @version    $Id: IniRes_in.java,v 1.3 2005/11/14 02:29:40 jjanke Exp $
 */
public class IniRes_in extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Compiere_License",   "Persetujuan Lisensi" },
	{ "Do_you_accept",      "Apakah anda menerima persetujuan lisensi ini ?" },
	{ "No",                 "Tidak" },
	{ "Yes_I_Understand",   "Ya, Saya Mengerti dan Menerima sepenuhnya" },
	{ "license_htm",        "org/compiere/license.htm" },
	{ "License_rejected",   "Lisensi telah ditolak atau habis masa berlakunya" }
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
