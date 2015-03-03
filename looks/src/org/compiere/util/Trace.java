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

import java.util.*;
import java.util.logging.*;

/**
 *  Trace Information
 *
 *  @author     Jorg Janke
 *  @version    $Id: Trace.java,v 1.17 2005/10/08 02:03:47 jjanke Exp $
 */
public class Trace
{
	/**
	 * Get Caller Array
	 *
	 * @param caller Optional Thowable/Exception
	 * @param maxNestLevel maximum call nesting level - 0 is all
	 * @return Array of class.method(file:line)
	 */
	public static String[] getCallerClasses (Throwable caller, int maxNestLevel)
	{
		int nestLevel = maxNestLevel;
		if (nestLevel < 1)
			nestLevel = 99;
		//
		ArrayList<String> list = new ArrayList<String>();
		Throwable t = caller;
		if (t == null)
			t = new Throwable();

		StackTraceElement[] elements = t.getStackTrace();
		for (int i = 0; i < elements.length && list.size() <= maxNestLevel; i++)
		{
			String className = elements[i].getClassName();
		//	System.out.println(list.size() + ": " + className);
			if (!(className.startsWith("org.compiere.util.Trace")
				|| className.startsWith("java.lang.Throwable")))
				list.add(className);
		}

		String[] retValue = new String[list.size()];
		list.toArray(retValue);
		return retValue;
	}   //  getCallerClasses

	/**
	 *  Get Caller with nest Level
	 *  @param nestLevel Nesting Level - 0=calling method, 1=previous, ..
	 *  @return class name and line info of nesting level or "" if not exist
	 */
	public static String getCallerClass (int nestLevel)
	{
		String[] array = getCallerClasses (null, nestLevel);
		if (array.length < nestLevel)
			return "";
		return array[nestLevel];
	}   //  getCallerClass

	/**
	 * 	Is the caller Called From the class mentioned
	 *	@param className calling class
	 *	@return the caller was called from className
	 */
	public static boolean isCalledFrom (String className)
	{
		if (className == null || className.length() == 0)
			return false;
		return getCallerClass(1).indexOf(className) != -1;
	}	//	isCalledFrom

	/**
	 *  Print Stack Tace Info (raw) compiereOnly - first9only
	 */
	public static void printStack()
	{
		printStack (true, true);
	}	//	printStack
	
	/**
	 *  Print Stack Tace Info (raw)
	 */
	public static void printStack (boolean compiereOnly, boolean first9only)
	{
		Throwable t = new Throwable();
	//	t.printStackTrace();
		int counter = 0;
		StackTraceElement[] elements = t.getStackTrace();
		for (int i = 1; i < elements.length; i++)
		{
			if (elements[i].getClassName().indexOf("util.Trace") != -1)
				continue;
			if (!compiereOnly
				|| (compiereOnly && elements[i].getClassName().startsWith("org.compiere"))
				)
			{
				Logger.global.fine(i + ": " + elements[i]);
				if (first9only && ++counter > 8)
					break;
			}
		}	
	}   //  printStack
	
}   //  Trace
