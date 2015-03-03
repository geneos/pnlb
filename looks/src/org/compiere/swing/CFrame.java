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
import org.compiere.plaf.*;

/**
 * 	Compiere Frame
 *	
 *  @author Jorg Janke
 *  @version $Id: CFrame.java,v 1.2 2005/12/27 06:20:19 jjanke Exp $
 */
public class CFrame extends JFrame
{
	/**
	 * 	CFrame
	 *	@throws HeadlessException
	 */
	public CFrame () throws HeadlessException
	{
		super ();
	}	//	CFrame

	/**
	 * 	CFrame
	 *	@param gc
	 */
	public CFrame (GraphicsConfiguration gc)
	{
		super (gc);
	}	//	CFrame

	/**
	 * 	CFrame
	 *	@param title
	 *	@throws HeadlessException
	 */
	public CFrame (String title) throws HeadlessException
	{
		super (cleanup(title));
	}	//	CFrame

	/**
	 * 	CFrame
	 *	@param title
	 *	@param gc
	 */
	public CFrame (String title, GraphicsConfiguration gc)
	{
		super (cleanup(title), gc);
	}	//	CFrame
	
	/**
	 * 	Frame Init.
	 * 	Install ALT-Pause
	 */
	protected void frameInit ()
	{
		super.frameInit ();
		CompiereColor.setBackground(this);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//
		Container c = getContentPane();
		if (c instanceof JPanel)
		{
			JPanel panel = (JPanel)c;
			panel.getActionMap().put(CDialog.ACTION_DISPOSE, CDialog.s_dialogAction);
			panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(CDialog.s_disposeKeyStroke, CDialog.ACTION_DISPOSE);
		}
	}	//	frameInit
	
	/**
	 * 	Cleanedup Title
	 *	@param title title
	 *	@return title w/o mn
	 */
	private static String cleanup (String title)
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
		return title;
	}	//	getTitle

	/**
	 * 	Set Title
	 *	@param title title
	 */
	public void setTitle(String title)
	{
		super.setTitle(cleanup(title));
	}	//	setTitle
	
}	//	CFrame
