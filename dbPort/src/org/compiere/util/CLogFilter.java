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

import java.util.logging.*;


/**
 *	Compiere Log Filter
 *	
 *  @author Jorg Janke
 *  @version $Id: CLogFilter.java,v 1.6 2005/09/24 01:52:07 jjanke Exp $
 */
public class CLogFilter implements Filter
{
	/**
	 * 	Get Filter
	 *	@return singleton
	 */
	public static CLogFilter get()
	{
		if (s_filter == null)
			s_filter = new CLogFilter();
		return s_filter;
	}
	
	/**	Singleton			*/
	private static CLogFilter	s_filter = null;
	
	/**************************************************************************
	 * 	Constructor
	 */
	public CLogFilter ()
	{
	}	//	CLogFilter

	/**
	 * 	Loggable - Don't log core java classes
	 *	@param record log record
	 *	@return true
	 */
	public boolean isLoggable (LogRecord record)
	{
		if (record.getLevel() == Level.SEVERE
			|| record.getLevel() == Level.WARNING)
			return true;
		//
		String loggerName = record.getLoggerName();
		if (loggerName != null)
		{
			if (loggerName.startsWith("sun.")
				|| loggerName.startsWith("java.awt.")
				|| loggerName.startsWith("javax.")
				)
				return false;
		}
		String className = record.getSourceClassName();
		if (className != null)
		{
			if (className.startsWith("sun.")
				|| className.startsWith("java.awt.")
				|| className.startsWith("javax.")
				)
				return false;
		}
		return true;
	}	//	isLoggable
	
}	//	CLogFilter
