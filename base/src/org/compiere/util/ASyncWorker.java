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
import javax.swing.*;
import org.compiere.process.*;

/**
 *  ASync Worker for starting methods in classes implementing ASyncProcess
 *
 *  @author     Jorg Janke
 *  @version    $Id: ASyncWorker.java,v 1.6 2005/03/11 20:26:09 jjanke Exp $
 */
public class ASyncWorker extends Thread
{
	/**
	 *  Execute method Synchronously
	 *  @param parent parent
	 *  @param pi process info
	 *  @return result
	 */
	public static ProcessInfo executeSync (ASyncProcess parent, ProcessInfo pi)
	{
		ASyncWorker worker = new ASyncWorker (parent, pi);
		worker.start();
		try
		{
			worker.join();
		}
		catch (InterruptedException e)
		{
			log.log(Level.SEVERE, "executeSync", e);
		}
		return worker.getResult();
	}   //  executeSync

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ASyncWorker.class);
	
	/**
	 *  Constructor
	 *  @param parent Parent Process
	 *  @param pi process info
	 */
	public ASyncWorker (ASyncProcess parent, ProcessInfo pi)
	{
		m_parent = parent;
		m_pi = pi;
	}   //  ASuncWorker

	private ProcessInfo     m_pi;
	private ASyncProcess    m_parent;

	/**
	 *  The Worker Method
	 */
	public void run()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				m_parent.lockUI(m_pi);
			}
		});

		//
		m_parent.executeASync(m_pi);
		//

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				m_parent.unlockUI (m_pi);
			}
		});
	}   //  run

	/**
	 *  Get Result (usually not used as result is returned via unlockUI
	 *  @return result
	 */
	public ProcessInfo getResult()
	{
		return m_pi;
	}   //  getResult

}   //  ASyncWorker
