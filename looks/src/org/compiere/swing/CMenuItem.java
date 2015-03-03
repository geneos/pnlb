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

/**
 * 	Compiere Menu Item
 *	
 *  @author Jorg Janke
 *  @version $Id: CMenuItem.java,v 1.1 2005/12/09 05:19:58 jjanke Exp $
 */
public class CMenuItem extends JMenuItem
{

	public CMenuItem ()
	{
		super ();
	}	//	CMenuItem

	public CMenuItem (Icon icon)
	{
		super (icon);
	}	//	CMenuItem

	public CMenuItem (String text)
	{
		super (text);
	}	//	CMenuItem

	public CMenuItem (Action a)
	{
		super (a);
	}	//	CMenuItem

	public CMenuItem (String text, Icon icon)
	{
		super (text, icon);
	}	//	CMenuItem

	public CMenuItem (String text, int mnemonic)
	{
		super (text, mnemonic);
	}	//	CMenuItem
	
	/**
	 * 	Set Text
	 *	@param text text
	 */
	public void setText (String text)
	{
		if (text == null)
		{
			super.setText(text);
			return;
		}
		int pos = text.indexOf("&");
		if (pos != -1 && text.length() > pos)	//	We have a nemonic - creates ALT-_
		{
			int mnemonic = text.toUpperCase().charAt(pos+1);
			if (mnemonic != ' ')
			{
				setMnemonic(mnemonic);
				text = text.substring(0, pos) + text.substring(pos+1);
			}
		}
		super.setText (text);
		if (getName() == null)
			setName (text);
	}	//	setText
	
}	//	CMenuItem
