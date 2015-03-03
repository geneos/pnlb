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
package org.compiere.plaf;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.*;


public class CompiereTextAreaUI extends BasicTextAreaUI
{
    /**
     *	Creates a UI for a JTextArea.
     * 	Tab is the normal focus traversal key - to enter Tab enter Ctrl-Tab 
     *
     *	@param ta a text area
     *	@return the UI
     */
    public static ComponentUI createUI(JComponent ta) 
    {
        return new CompiereTextAreaUI(ta);
    }
    
    /**
     * 	Constructor
     *	@param ta text area
     */
    public CompiereTextAreaUI (JComponent ta)
    {
    	if (ta instanceof JTextComponent)
    		m_editor = (JTextComponent)ta;
    }	//	CompiereTextAreaUI
    
    /**	The Editor				*/
    private JTextComponent 		m_editor = null;
    /** Tab Stroke				*/
    private static KeyStroke	s_stroke = KeyStroke.getKeyStroke (KeyEvent.VK_TAB, InputEvent.CTRL_MASK);
    /** Tab Action				*/
    private static Action		s_action = new DefaultEditorKit.InsertTabAction();
    
    /**
     * 	Create Keymap
     *	@return key Map
     */
    protected Keymap createKeymap ()
    {
    	Keymap map = super.createKeymap ();
    	map.addActionForKeyStroke(s_stroke, s_action);
    	return map;
    }	//	createKeyMap
    
    /**
     * 	Property Change
     *	@param evt event
     */
    protected void propertyChange (PropertyChangeEvent evt)
    {
    	String name = evt.getPropertyName();
    	if ("editable".equals(name))
    		updateFocusTraversalKeysX();
   	    else
   	    	super.propertyChange (evt);
    }	//	propertyChange
    
    /**
     * 	UpdateFocusTraversalKeysX
     */
	void updateFocusTraversalKeysX ()
	{
		if (m_editor == null)
			return;
		//
		EditorKit editorKit = getEditorKit (m_editor);
		if (editorKit != null && editorKit instanceof DefaultEditorKit)
		{
			Set<AWTKeyStroke> storedForwardTraversalKeys = m_editor.getFocusTraversalKeys (KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
			Set<AWTKeyStroke> storedBackwardTraversalKeys = m_editor.getFocusTraversalKeys (KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);
			Set<AWTKeyStroke> forwardTraversalKeys = new HashSet<AWTKeyStroke>(storedForwardTraversalKeys);
			Set<AWTKeyStroke> backwardTraversalKeys = new HashSet<AWTKeyStroke>(storedBackwardTraversalKeys);
			//
			forwardTraversalKeys.add (KeyStroke.getKeyStroke (KeyEvent.VK_TAB, 0));
			forwardTraversalKeys.remove(s_stroke);
			backwardTraversalKeys.add (KeyStroke.getKeyStroke (KeyEvent.VK_TAB, InputEvent.SHIFT_MASK));
			//
			LookAndFeel.installProperty (m_editor, "focusTraversalKeysForward",	forwardTraversalKeys);
			LookAndFeel.installProperty (m_editor, "focusTraversalKeysBackward", backwardTraversalKeys);
		}
	}	//	updateFocusTraversalKeysX
    
}	//	CompiereTextAreaUI
