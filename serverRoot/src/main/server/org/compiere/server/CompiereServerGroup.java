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
package org.compiere.server;

import org.compiere.util.*;

/**
 *	Compiere Server Group
 *	
 *  @author Jorg Janke
 *  @version $Id: CompiereServerGroup.java,v 1.4 2005/09/24 01:51:06 jjanke Exp $
 */
public class CompiereServerGroup extends ThreadGroup
{
	/**
	 * 	Get Compiere Server Group
	 *	@return Server Group
	 */
	public static CompiereServerGroup get()
	{
		if (s_group == null || s_group.isDestroyed())
			s_group = new CompiereServerGroup(); 
		return s_group;
	}	//	get
	
	/** Group */
	private static CompiereServerGroup	s_group	= null;
	
	/**
	 * 	CompiereServerGroup
	 */
	private CompiereServerGroup ()
	{
		super ("CompiereServers");
		setDaemon(true);
		setMaxPriority(Thread.MAX_PRIORITY);
		log.info(getName() + " - Parent=" + getParent());
	}	//	CompiereServerGroup

	/**	Logger			*/
	protected CLogger	log = CLogger.getCLogger(getClass());
	
	/**
	 * 	Uncaught Exception
	 *	@param t thread
	 *	@param e exception
	 */
	public void uncaughtException (Thread t, Throwable e)
	{
		log.info ("uncaughtException = " + e.toString());
		super.uncaughtException (t, e);
	}	//	uncaughtException
	
	/**
	 * 	String Representation
	 *	@return name
	 */
	public String toString ()
	{
		return getName();
	}	//	toString

	/**
	 * 	Dump Info
	 */
	public void dump ()
	{
		log.fine(getName() + (isDestroyed() ? " (destroyed)" : ""));
		log.fine("- Parent=" + getParent());
		Thread[] list = new Thread[activeCount()];
		log.fine("- Count=" + enumerate(list, true));
		for (int i = 0; i < list.length; i++)
			log.fine("-- " + list[i]);
	}	//	dump

}	//	CompiereServerGroup
