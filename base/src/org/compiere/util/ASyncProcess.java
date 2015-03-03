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

import org.compiere.process.*;

/**
 *  Async Process Interface.
 *  <p>
 *  The Process implements the methods.
 *  The Worker is started like
 *  <code> MyWorker.start()</code>
 *  <p>
 *  The worker's run method basically executes
 *  <pre>
 *      process.lockUI(pi);
 *      process.executeAsync(pi);
 *      process.unlockUI(pi);
 *  </pre>
 *  The isUILocked() method is used internally (not called by worker).
 *
 *  @author     Jorg Janke
 *  @version    $Id: ASyncProcess.java,v 1.5 2005/03/11 20:26:08 jjanke Exp $
 */
public interface ASyncProcess
{
	/**
	 *  Lock User Interface.
	 *  Called from the Worker before processing
	 *  @param pi process info
	 */
	public void lockUI (ProcessInfo pi);

	/**
	 *  Unlock User Interface.
	 *  Called from the Worker when processing is done
	 *  @param pi result of execute ASync call
	 */
	public void unlockUI (ProcessInfo pi);

	/**
	 *  Is the UI locked (Internal method)
	 *  @return true, if UI is locked
	 */
	boolean isUILocked();

	/**
	 *  Method to be executed async.
	 *  Called from the Worker
	 *  @param pi ProcessInfo
	 */
	public void executeASync (ProcessInfo pi);

}   //  ASyncProcess
