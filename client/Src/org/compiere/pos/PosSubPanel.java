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

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import org.compiere.apps.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	POS Sub Panel Base Class.
 *	The Panel knows where to position itself in the POS Panel
 *	
 *  @author Jorg Janke
 *  @version $Id: PosSubPanel.java,v 1.5 2005/03/11 20:28:22 jjanke Exp $
 */
public abstract class PosSubPanel extends CPanel 
	implements ActionListener
{
	/**
	 * 	Constructor
	 *	@param posPanel POS Panel
	 */
	public PosSubPanel (PosPanel posPanel)
	{
		super();
		p_posPanel = posPanel;
		p_pos = posPanel.p_pos;
		init();
	}	//	PosSubPanel
	
	/** POS Panel							*/
	protected PosPanel 				p_posPanel = null;
	/**	Underlying POS Model				*/
	protected MPOS					p_pos = null;
	/**	Position of SubPanel in Main		*/
	protected GridBagConstraints	p_position = null;
	/** Context								*/
	protected Properties			p_ctx = Env.getCtx();
	

	/** Button Width = 40			*/
	private static final int	WIDTH = 45;	
	/** Button Height = 40			*/
	private static final int	HEIGHT = 35;	
	/** Inset 1all					*/
	public static Insets 		INSETS1 = new Insets(1,1,1,1);	
	/** Inset 2all					*/
	public static Insets 		INSETS2 = new Insets(2,2,2,2);	
	
	/**
	 * 	Initialize
	 */
	protected abstract void init();
	
	
	/**
	 * 	Get Panel Position
	 */
	protected GridBagConstraints getGridBagConstraints()
	{
		if (p_position == null)
		{
			p_position = new GridBagConstraints();
			p_position.anchor = GridBagConstraints.NORTHWEST;
			p_position.fill = GridBagConstraints.BOTH;
			p_position.weightx = 0.1;
			p_position.weighty = 0.1;
		}
		return p_position;
	}	//	getGridBagConstraints
	
	/**
	 * 	Dispose - Free Resources
	 */
	public void dispose()
	{
		p_pos = null;
	}	//	dispose

	
	/**
	 * 	Create Action Button
	 *	@param action action 
	 *	@return button
	 */
	protected CButton createButtonAction (String action, KeyStroke accelerator)
	{
		AppsAction act = new AppsAction(action, accelerator, false);
		act.setDelegate(this);
		CButton button = (CButton)act.getButton();
		button.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		button.setMinimumSize(getPreferredSize());
		button.setMaximumSize(getPreferredSize());
		button.setFocusable(false);
		return button;
	}	//	getButtonAction
	
	/**
	 * 	Create Standard Button
	 *	@param text text
	 *	@return button
	 */
	protected CButton createButton (String text)
	{
	//	if (text.indexOf("<html>") == -1)
	//		text = "<html><h4>" + text + "</h4></html>";
		CButton button = new CButton(text);
		button.addActionListener(this);
		button.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		button.setMinimumSize(getPreferredSize());
		button.setMaximumSize(getPreferredSize());
		button.setFocusable(false);
		return button;
	}	//	getButton

	/**
	 * 	Action Listener
	 *	@param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
	}	//	actinPerformed

}	//	PosSubPanel
