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
package org.compiere.apps;

import java.applet.*;
import java.awt.*;
import org.compiere.*;
import org.compiere.util.*;


/**
 *	Application Applet
 *	
 *  @author Jorg Janke
 *  @version $Id: AApplet.java,v 1.3 2005/03/11 20:27:59 jjanke Exp $
 */
public class AApplet extends Applet
{

	/**
	 * 	Compiere Application Applet
	 *	@throws java.awt.HeadlessException
	 */
	public AApplet () throws HeadlessException
	{
		super ();
	}	//	AApplet

	
	/**************************************************************************
	 * 	init
	 */
	public void init ()
	{
		super.init ();
		TextArea ta = new TextArea(Compiere.getSummary());
		add (ta);
	}	//	init
	
	/**
	 * 	start
	 */
	public void start ()
	{
		super.start ();
		showStatus(Compiere.getSummary());
		//
		Splash splash = Splash.getSplash();
		Compiere.startup(true);	//	needs to be here for UI
		AMenu menu = new AMenu();
	}	//	start
	
	/**
	 * 	stop
	 */
	public void stop ()
	{
		super.stop ();
	}	//	stop
	
	/**
	 * 	destroy
	 */
	public void destroy ()
	{
		super.destroy ();
		Env.exitEnv(0);
	}	//	destroy
	
}	//	AApplet
