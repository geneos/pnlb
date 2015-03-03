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

import java.awt.*;

import javax.swing.*;

import org.compiere.plaf.CompiereColor;
import org.compiere.plaf.CompierePLAF;
import org.compiere.plaf.CompierePanelUI;


/**
 *	Compiere Srcoll Pane.
 *	
 *  @author Jorg Janke
 *  @version $Id: CScrollPane.java,v 1.4 2005/10/08 02:03:48 jjanke Exp $
 */
public class CScrollPane extends JScrollPane
{

	/**
	 * 	Compiere ScollPane
	 */
	public CScrollPane ()
	{
		this (null, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}	//	CScollPane

	/**
	 * 	Compiere ScollPane
	 *	@param vsbPolicy vertical policy
	 *	@param hsbPolicy horizontal policy
	 */
	public CScrollPane (int vsbPolicy, int hsbPolicy)
	{
		this (null, vsbPolicy, hsbPolicy);
	}	//	CScollPane

	/**
	 * 	Compiere ScollPane
	 *	@param view view
	 */
	public CScrollPane (Component view)
	{
		this (view, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}	//	CScollPane

	/**
	 * 	Compiere ScollPane
	 *	@param view view
	 *	@param vsbPolicy vertical policy
	 *	@param hsbPolicy horizontal policy
	 */
	public CScrollPane (Component view, int vsbPolicy, int hsbPolicy)
	{
		super (view, vsbPolicy, hsbPolicy);
		setBackgroundColor(null);
		setOpaque(false);
		getViewport().setOpaque(false);
	}	//	CScollPane
	
	
	/**
	 *  Set Background
	 *  @param bg CompiereColor for Background, if null set standard background
	 */
	public void setBackgroundColor (CompiereColor bg)
	{
		if (bg == null)
			bg = CompierePanelUI.getDefaultBackground();
		putClientProperty(CompierePLAF.BACKGROUND, bg);
	//	super.setBackground(bg.getFlatColor());
	//	getViewport().putClientProperty(CompierePLAF.BACKGROUND, bg);
	//	getViewport().setBackground(bg.getFlatColor());
	//	getViewport().setOpaque(true);
	}   //  setBackground
	
}	//	CScollPane
