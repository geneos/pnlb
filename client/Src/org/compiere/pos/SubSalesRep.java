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
import javax.swing.border.*;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *	POS Sales Rep Sub Panel
 *	
 *  @author Jorg Janke
 *  @version $Id: SubSalesRep.java,v 1.3 2005/03/11 20:28:22 jjanke Exp $
 */
public class SubSalesRep extends PosSubPanel implements ActionListener
{
	/**
	 * 	Constructor
	 *	@param posPanel POS Panel
	 */
	public SubSalesRep (PosPanel posPanel)
	{
		super (posPanel);
	}	//	PosSubSalesRep
	
	private CLabel f_label = null;
	private CButton f_button = null;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(SubSalesRep.class);
	
	/**
	 * 	Initialize
	 */
	public void init()
	{
		//	Title
		TitledBorder border = new TitledBorder(Msg.translate(Env.getCtx(), "C_POS_ID"));
		setBorder(border);
		
		//	Content
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = INSETS2;
		//	--
		f_label = new CLabel(p_pos.getName(), CLabel.LEADING);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;
		add (f_label, gbc);
		//
		f_button = new CButton (Msg.getMsg(Env.getCtx(), "Logout"));
		f_button.setActionCommand("LogOut");
		f_button.setFocusable(false);
		f_button.addActionListener(this);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		add (f_button, gbc);
	}	//	init

	/**
	 * 	Get Panel Position
	 */
	public GridBagConstraints getGridBagConstraints()
	{
		GridBagConstraints gbc = super.getGridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		return gbc;
	}	//	getGridBagConstraints
	
	/**
	 * 	Dispose - Free Resources
	 */
	public void dispose()
	{
		super.dispose();
	}	//	dispose

	/**
	 * 	Action Listener
	 *	@param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		String action = e.getActionCommand();
		if (action == null || action.length() == 0)
			return;
		log.info( "PosSubSalesRep - actionPerformed: " + action);
		//	Logout
		p_posPanel.dispose();
	}	//	actinPerformed
	
}	//	PosSubSalesRep
