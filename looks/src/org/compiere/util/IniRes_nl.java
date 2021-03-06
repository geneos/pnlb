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
 *  @author     Eldir Tomassen
 *  @version    $Id: IniRes_nl.java,v 1.6 2005/03/11 20:34:37 jjanke Exp $
 */
public class IniRes_nl extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Compiere_License",   "Licentieovereenkomst" },
	{ "Do_you_accept",      "Accepteert u de licentieovereenkomst" },
	{ "No",                 "Nee" },
	{ "Yes_I_Understand",   "Ja, Ik heb de licentieovereenkomst gelezen en ga hiermee accoord." },
	{ "license_htm",        "org/compiere/license.htm" },
	{ "License_rejected",   "Licentieovereenkomst afgewezen of verlopen." }
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
