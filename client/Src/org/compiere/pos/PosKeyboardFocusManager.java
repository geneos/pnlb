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
package org.compiere.pos;

import java.awt.event.*;
import java.util.*;

import org.compiere.apps.*;
import org.compiere.util.*;


/**
 *	POS Keyboard Focus Manager
 *	
 *  @author Jorg Janke
 *  @version $Id: PosKeyboardFocusManager.java,v 1.6 2005/12/05 02:36:08 jjanke Exp $
 */
public class PosKeyboardFocusManager extends AKeyboardFocusManager
	implements ActionListener
{
	/**
	 * 	PosKeyboardFocusManager
	 */
	public PosKeyboardFocusManager ()
	{
		super ();
	}	//	PosKeyboardFocusManager

	/** FirstIn First Out List			*/
	private LinkedList<KeyEvent>	m_fifo = new LinkedList<KeyEvent>();
	/**	Last Key Type					*/
	private long					m_lastWhen = 0;
	/** Timer							*/
	private javax.swing.Timer		m_timer = null;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(PosKeyboardFocusManager.class);
	
	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_timer != null)
			m_timer.stop();
		m_timer = null;
		if (m_fifo != null)
			m_fifo.clear();
		m_fifo = null;
	}	//	dispose

	
	/**
	 * 	Start Timer
	 */
	public void start()
	{
		//	Unqueue time - 200 ms
		int delay = 200;
		log.fine("" + delay); 
		if (m_timer == null)
			m_timer = new javax.swing.Timer (delay, this);
		if (!m_timer.isRunning())
			m_timer.start();
	}	//	start
	
	/**
	 * 	Stop Timer
	 */
	public void stop()
	{
		log.fine("" + m_timer); 
		if (m_timer != null)
			m_timer.stop();
	}	//	stop
	
	
	/**************************************************************************
	 * 	Dispatch Key Event - queue
	 *	@param event event
	 *	@return true
	 */
	public boolean dispatchKeyEvent (KeyEvent event)
	{
		if (event.getID()==KeyEvent.KEY_PRESSED)
		{
			//	Keyboard Repeat: 485 - then 31
		//	log.fine( "PosKeyboardFocusManager.dispatchKeyEvent - " 
		//		+ event.getWhen() + " - " + (event.getWhen() - m_lastWhen));
			m_lastWhen = event.getWhen();
		}
		if (m_timer == null)
			super.dispatchKeyEvent (event);
		else
			m_fifo.add(event);
		return true;
	}	//	displatchEvent
	
	
	/**
	 * 	Action Performed - unqueue
	 *	@param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		if (m_timer == null)
			return;
	//	log.fine( "actionPerformed - " + m_fifo.size()); 
		while (m_fifo.size() > 0)
		{
			KeyEvent event = (KeyEvent)m_fifo.removeFirst();
			super.dispatchKeyEvent (event);
		}
	}	//	actionPerformed
	
}	//	PosKeyboardFocusManager
