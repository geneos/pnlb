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
 * 	Compiere Check Box Menu Item
 *	
 *  @author Jorg Janke
 *  @version $Id: CCheckBoxMenuItem.java,v 1.1 2005/12/09 05:19:58 jjanke Exp $
 */
public class CCheckBoxMenuItem extends JCheckBoxMenuItem
{

	public CCheckBoxMenuItem ()
	{
		super ();
	}	//	CCheckBoxMenuItem

	public CCheckBoxMenuItem (Icon icon)
	{
		super (icon);
	}	//	CCheckBoxMenuItem

	public CCheckBoxMenuItem (String text)
	{
		super (text);
	}	//	CCheckBoxMenuItem

	public CCheckBoxMenuItem (Action a)
	{
		super (a);
	}	//	CCheckBoxMenuItem

	public CCheckBoxMenuItem (String text, Icon icon)
	{
		super (text, icon);
	}	//	CCheckBoxMenuItem

	public CCheckBoxMenuItem (String text, boolean b)
	{
		super (text, b);
	}	//	CCheckBoxMenuItem

	public CCheckBoxMenuItem (String text, Icon icon, boolean b)
	{
		super (text, icon, b);
	}	//	CCheckBoxMenuItem
	
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

}	//	CCheckBoxMenuItem
