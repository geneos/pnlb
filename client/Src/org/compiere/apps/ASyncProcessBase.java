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

import org.compiere.process.*;
import org.compiere.util.*;

/**
 *  ASync Process Base Class
 *
 *  @author     Jorg Janke
 *  @version    $Id: ASyncProcessBase.java,v 1.6 2005/03/11 20:28:21 jjanke Exp $
 */
public abstract class ASyncProcessBase implements ASyncProcess
{
	/**
	 *  Constructor
	 *  @param pi process info
	 */
	public ASyncProcessBase(ProcessInfo pi)
	{
		m_pi = pi;
	}   //  ASyncProcessBase

	private ProcessInfo m_pi;
	private boolean     m_isLocked = false;
	private Splash      m_splash;

	/**
	 *  Start ASync Worker
	 */
	void start()
	{
		if (isUILocked())   //  don't start twice
			return;
		ASyncWorker worker = new ASyncWorker (this, m_pi);
		worker.start();     //  calls lockUI, executeASync, unlockUI
	}   //  start

	/**
	 *  Lock User Interface.
	 *  Called from the Worker before processing
	 *  @param pi process info
	 */
	public void lockUI (ProcessInfo pi)
	{
		m_isLocked = true;
		m_splash = new Splash (Msg.getMsg(Env.getCtx(), "Processing"));
		m_splash.toFront();
	}   //  lockUI

	/**
	 *  Unlock User Interface.
	 *  Called from the Worker when processing is done
	 *  @param pi process info
	 */
	public void unlockUI (ProcessInfo pi)
	{
		m_isLocked = false;
		m_splash.dispose();
		m_splash = null;
	}   //  unlockUI

	/**
	 *  Is the UI locked (Internal method)
	 *  @return true, if UI is locked
	 */
	public boolean isUILocked()
	{
		return m_isLocked;
	}   //  isLoacked

	/**
	 *  Method to be executed async
	 *  Called from the Worker
	 *  @param pi process info
	 */
	public abstract void executeASync (ProcessInfo pi);

}   //  ASyncProcessBase
