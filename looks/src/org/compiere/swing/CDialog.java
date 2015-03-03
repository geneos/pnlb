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
package org.compiere.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.compiere.plaf.CompiereColor;

/**
 *	Conveniance Dialog Class.
 *	Compiere Background + Dispose on Close  
 *  Implementing empty Action and Mouse Listener
 *	
 *  @author Jorg Janke
 *  @version $Id: CDialog.java,v 1.7 2005/12/09 05:19:58 jjanke Exp $
 */
public class CDialog extends JDialog 
	implements ActionListener, MouseListener
{
	
	public CDialog() throws HeadlessException 
	{
		this((Frame)null, false);
	}

	public CDialog(Frame owner) throws HeadlessException 
	{
		this (owner, false);
	}

	public CDialog(Frame owner, boolean modal) throws HeadlessException 
	{
		this (owner, null, modal);
	}

	public CDialog(Frame owner, String title) throws HeadlessException 
	{
		this (owner, title, false);     
	}

	public CDialog(Frame owner, String title, boolean modal) throws HeadlessException 
	{
		super(owner, title, modal);
	}

	public CDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) 
	{
		super(owner, title, modal, gc);
	}

	public CDialog(Dialog owner) throws HeadlessException 
	{
		this (owner, false);
	}

	public CDialog(Dialog owner, boolean modal) throws HeadlessException 
	{
		this(owner, null, modal);
	}

	public CDialog(Dialog owner, String title) throws HeadlessException 
	{
		this(owner, title, false);     
	}

	public CDialog(Dialog owner, String title, boolean modal) throws HeadlessException 
	{
		super(owner, title, modal);
	}
	public CDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) throws HeadlessException 
    {
		super(owner, title, modal, gc);
	}

	/**
	 * 	Initialize.
	 * 	Install ALT-Pause
	 */
	protected void dialogInit()
	{
		super.dialogInit();
		CompiereColor.setBackground(this);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle(getTitle());	//	remove Mn
		//
		Container c = getContentPane();
		if (c instanceof JPanel)
		{
			JPanel panel = (JPanel)c;
			panel.getActionMap().put(ACTION_DISPOSE, s_dialogAction);
			panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(s_disposeKeyStroke, ACTION_DISPOSE);
		}
	}	//	init

	
	/**************************************************************************
	 *	@see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 *	@param e
	 */
	public void actionPerformed(ActionEvent e)
	{
	}

	/**
	 *	@see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 *	@param e
	 */
	public void mouseClicked(MouseEvent e)
	{
	}

	/**
	 *	@see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 *	@param e
	 */
	public void mouseEntered(MouseEvent e)
	{
	}

	/**
	 *	@see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 *	@param e
	 */
	public void mouseExited(MouseEvent e)
	{
	}

	/**
	 *	@see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 *	@param e
	 */
	public void mousePressed(MouseEvent e)
	{
	}

	/**
	 *	@see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 *	@param e
	 */
	public void mouseReleased(MouseEvent e)
	{
	}

	/**
	 * 	Set Title
	 *	@param title title
	 */
	public void setTitle(String title)
	{
		if (title != null)
		{
			int pos = title.indexOf("&");
			if (pos != -1 && title.length() > pos)	//	We have a nemonic
			{
				int mnemonic = title.toUpperCase().charAt(pos+1);
				if (mnemonic != ' ')
					title = title.substring(0, pos) + title.substring(pos+1);
			}
		}
		super.setTitle(title);
	}	//	setTitle

	/** Dispose Action Name				*/
	protected static String			ACTION_DISPOSE = "CDialogDispose";
	/**	Action							*/
	protected static DialogAction	s_dialogAction = new DialogAction(ACTION_DISPOSE);
	/** ALT-EXCAPE						*/
	protected static KeyStroke		s_disposeKeyStroke = 
		KeyStroke.getKeyStroke(KeyEvent.VK_PAUSE, InputEvent.ALT_MASK);
	
	/**
	 * 	Compiere Dialog Action
	 *	
	 *  @author Jorg Janke
	 *  @version $Id: CDialog.java,v 1.7 2005/12/09 05:19:58 jjanke Exp $
	 */
	static class DialogAction extends AbstractAction
	{
		DialogAction (String actionName)
		{
			super(actionName);
			putValue(AbstractAction.ACTION_COMMAND_KEY, actionName);
		}	//	DialogAction
		
		/**
		 * 	Action Listener
		 *	@param e event
		 */
		public void actionPerformed (ActionEvent e)
		{
			if (ACTION_DISPOSE.equals(e.getActionCommand()))
			{
				Object source = e.getSource();
				while (source != null)
				{
					if (source instanceof Window)
					{
						((Window)source).dispose();
						return;
					}
					if (source instanceof Container)
						source = ((Container)source).getParent();
					else
						source = null;
				}
			}
			else
				System.out.println("Action: " + e);
		}	//	actionPerformed
	}	//	DialogAction
	
}	//	CDialog
