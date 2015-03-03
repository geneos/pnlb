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
import org.compiere.model.*;
import org.compiere.print.*;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *	POS Function Key Sub Panel
 *	
 *  @author Jorg Janke
 *  @version $Id: SubFunctionKeys.java,v 1.4 2005/09/19 04:48:58 jjanke Exp $
 */
public class SubFunctionKeys extends PosSubPanel implements ActionListener
{
	/**
	 * 	Constructor
	 *	@param posPanel POS Panel
	 */
	public SubFunctionKeys (PosPanel posPanel)
	{
		super (posPanel);
	}	//	PosSubFunctionKeys
	
	/**	Keys				*/
	private MPOSKey[] 	m_keys;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(SubFunctionKeys.class);
	
	/**
	 * 	Initialize
	 */
	public void init()
	{
		//	Title
		TitledBorder border = new TitledBorder(Msg.translate(Env.getCtx(), "C_POSKeyLayout_ID"));
		setBorder(border);
		
		int C_POSKeyLayout_ID = p_pos.getC_POSKeyLayout_ID();
		if (C_POSKeyLayout_ID == 0)
			return;
		MPOSKeyLayout fKeys = MPOSKeyLayout.get(Env.getCtx(), C_POSKeyLayout_ID);
		if (fKeys.get_ID() == 0)
			return;
		
		int COLUMNS = 3;	//	Min Columns
		int ROWS = 3;		//	Min Rows
		m_keys = fKeys.getKeys(false);
		int noKeys = m_keys.length;
		int rows = Math.max (((noKeys-1) / COLUMNS) + 1, ROWS);
		int cols = ((noKeys-1) % COLUMNS) + 1;
		log.fine( "PosSubFunctionKeys.init - NoKeys=" + noKeys 
			+ " - Rows=" + rows + ", Cols=" + cols);
		//	Content
		CPanel content = new CPanel (new GridLayout(Math.max(rows, 3), Math.max(cols, 3)));
		for (int i = 0; i < m_keys.length; i++)
		{
			MPOSKey key = m_keys[i];
			StringBuffer buttonHTML = new StringBuffer("<html><p>");
			if (key.getAD_PrintColor_ID() != 0)
			{
				MPrintColor color = MPrintColor.get(Env.getCtx(), key.getAD_PrintColor_ID());
				buttonHTML
					.append("<font color=#")
					.append(color.getRRGGBB())
					.append(">")
					.append(key.getName())
					.append("</font>");
			}
			else
				buttonHTML.append(key.getName());
			buttonHTML.append("</p></html>");
			log.fine( "#" + i + " - " + buttonHTML); 
			CButton button = new CButton(buttonHTML.toString());
			button.setMargin(INSETS1);
			button.setFocusable(false);
			button.setActionCommand(String.valueOf(key.getC_POSKey_ID()));
			button.addActionListener(this);
			content.add (button);
		}
		for (int i = m_keys.length; i < rows*COLUMNS; i++)
		{
			CButton button = new CButton("");
			button.setFocusable(false);
			content.add (button);
		}
		content.setPreferredSize(new Dimension(cols*70, rows*50));
		add (content);
	}	//	init

	/**
	 * 	Get Panel Position
	 */
	public GridBagConstraints getGridBagConstraints()
	{
		GridBagConstraints gbc = super.getGridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
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
		if (action == null || action.length() == 0 || m_keys == null)
			return;
		log.info( "PosSubFunctionKeys - actionPerformed: " + action);
		try
		{
			int C_POSKey_ID = Integer.parseInt(action);
			for (int i = 0; i < m_keys.length; i++)
			{
				MPOSKey key = m_keys[i];
				if (key.getC_POSKey_ID() == C_POSKey_ID)
				{
					p_posPanel.f_product.setM_Product_ID(key.getM_Product_ID());
					p_posPanel.f_product.setPrice();
					p_posPanel.f_curLine.setQty(key.getQty());
					p_posPanel.f_curLine.saveLine();
					return;
				}
			}
		}
		catch (Exception ex)
		{
		}
	}	//	actinPerformed
	
}	//	PosSubFunctionKeys
