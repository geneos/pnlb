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

import java.awt.*;
import java.awt.event.*;

/**
 *	Convenience Class to tunnel Events.
 *
 *  Forwards Window Events like windowClosed to the WindowState listener.
 *  Usually, a WindowStateListener gets only state events (minimized,..)
 *  This allows implementing a single method (windowStateChanged)
 *  to receive potentially all window events.
 *  <p>
 *  Implemented:
 * 	e.getID() == WindowEvent.WINDOW_CLOSED
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: AWindowListener.java,v 1.4 2005/03/11 20:27:58 jjanke Exp $
 */
public class AWindowListener extends WindowAdapter
{
	/**
	 *	Constructor
	 *
	 * 	@param win Window
	 * 	@param l Listener
	 */
	public AWindowListener (Window win, WindowStateListener l)
	{
		m_window = win;
		m_listener = l;
		win.addWindowListener(this);
	}	//	AWindowListener

	/**	The Window					*/
	private Window 				m_window;
	/** The Listener				*/
	private WindowStateListener m_listener;

	/**
	 * 	Invoked when a window has been closed.
	 *  Forwarded.
	 *  @param e event to be forwarded
	 */
	public void windowClosed(WindowEvent e)
	{
		m_listener.windowStateChanged(e);
	}	//	windowClosed

}	//	AWindowListenr
