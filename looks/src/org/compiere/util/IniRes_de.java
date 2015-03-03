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
 *  License Dialog Translation (German)
 *
 *  @author     Jorg Janke
 *  @version    $Id: IniRes_de.java,v 1.7 2005/03/11 20:34:37 jjanke Exp $
 */
public class IniRes_de extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Compiere_License",   "Lizenz Vereinbarung" },
	{ "Do_you_accept",      "Akzeptieren Sie die Lizenz Vereinbarung ?" },
	{ "No",                 "Nein" },
	{ "Yes_I_Understand",   "Ja, ich verstehe und akzeptiere" },
	{ "license_htm",        "org/compiere/license.htm" },
	{ "License_rejected",   "Lizenz abgelehnt oder abgelaufen" }
	};

	/**
	 *  Get Content
	 *  @return Content
	 */
	public Object[][] getContents()
	{
		return contents;
	}   //  getContent
}   //  IniRes_de
