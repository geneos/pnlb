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

import org.apache.log4j.*;

/**
 *	Log4J Utilities.
 *	Not intended to be called directly
 *	
 *  @author Jorg Janke
 *  @version $Id: CLogMgtLog4J.java,v 1.2 2005/03/11 20:28:40 jjanke Exp $
 */
public class CLogMgtLog4J
{
	/**
	 * 	Initialize Logging
	 * 	@param isClient client
	 */
	protected static void initialize(boolean isClient)
	{
		if (isClient)
		{
			LogManager.resetConfiguration();
			Logger rootLogger = LogManager.getRootLogger();
			rootLogger.setLevel(s_currentLevelLog4J);
		}
	}	//	initialize

	/** Current Lo4J Level	*/
	private static Level	s_currentLevelLog4J = Level.WARN;

	
	/**
	 * 	Enable/Disable Log4J logging
	 *	@param enableLogging false assumed
	 */
	public static void enable (boolean enableLogging)
	{
		Logger rootLogger = LogManager.getRootLogger(); 
		if (enableLogging)
			rootLogger.setLevel(s_currentLevelLog4J);
		else
		{
			Level level = rootLogger.getLevel(); 
			rootLogger.setLevel(Level.OFF);
			s_currentLevelLog4J = level;
		}
	}	//	enableLog4J

	
}	//	ClientLogMgtLog4J
