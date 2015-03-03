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
package org.compiere.grid;

import java.awt.*;
import javax.swing.*;
import org.compiere.apps.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *  Tabbed Pane - either Workbench or Window Tab
 *
 *  @author Jorg Janke
 *  @version $Id: VTabbedPane.java,v 1.15 2005/12/13 00:15:43 jjanke Exp $
 */
public class VTabbedPane extends CTabbedPane
{
	/**
	 *  Constructor
	 *  @param isWorkbench is this a workbench tab (tabs on the left side)
	 */
	public VTabbedPane (boolean isWorkbench)
	{
		super();
		setTabLayoutPolicy (JTabbedPane.SCROLL_TAB_LAYOUT);
		setWorkbench (isWorkbench);
		setFocusable(false);
	}   //  VTabbedPane

	/** Workbench 				*/
	private boolean m_workbenchTab = false;

	/**
	 *  toString
	 *  @return info
	 */
	public String toString()
	{
		return (m_workbenchTab ? "WorkbenchTab" : "WindowTab")
			+ " - selected " + getSelectedIndex() + " of " + getTabCount();
	}   //  toString

	/**
	 *  Set Workbench - or Window
	 *  @param isWorkbench
	 */
	public void setWorkbench (boolean isWorkbench)
	{
		m_workbenchTab = isWorkbench;
		if (m_workbenchTab)
			super.setTabPlacement(JTabbedPane.BOTTOM);
		else
			super.setTabPlacement(Language.getLoginLanguage().isLeftToRight() 
				? JTabbedPane.LEFT : JTabbedPane.RIGHT);
	}   //  setWorkbench

	/**
	 *  Tab is Workbench (not Window)
	 *  @return true if Workbench
	 */
	public boolean isWorkbench()
	{
		return m_workbenchTab;
	}   //  toString

	/**
	 *  Set Tab Placement.
	 *  Do not use - set via setWorkBench
	 *  @param notUsed
	 */
	public void setTabPlacement (int notUsed)
	{
		new java.lang.IllegalAccessError("Do not use VTabbedPane.setTabPlacement directly");
	}   //  setTabPlacement

	/**
	 *  Dispose all contained VTabbedPanes and GridControllers
	 *  @param aPanel
	 */
	public void dispose (APanel aPanel)
	{
		Component[] comp = getComponents();
		for (int i = 0; i < comp.length; i++)
		{
			if (comp[i] instanceof VTabbedPane)
			{
				VTabbedPane tp = (VTabbedPane)comp[i];
				tp.removeChangeListener(aPanel);
				tp.dispose(aPanel);
			}
			else if (comp[i] instanceof GridController)
			{
				GridController gc = (GridController)comp[i];
				gc.addDataStatusListener(aPanel);
				gc.dispose();
			}
		}
		removeAll();
	}   //  dispose

	
	/**
	 * 	Set Selected Index.
	 * 	Register/Unregister Mnemonics
	 *	@param index index
	 */
	public void setSelectedIndex (int index)
	{
		Component newC = getComponentAt(index);
		GridController newGC = null;
		if (newC instanceof GridController)
			newGC = (GridController)newC;
		int oldIndex = getSelectedIndex();
		if (newGC != null && oldIndex >= 0 && index != oldIndex)
		{
			Component oldC = getComponentAt(oldIndex);
			if (oldC != null && oldC instanceof GridController)
			{
				GridController oldGC = (GridController)oldC;
				if (newGC.getTabLevel() > oldGC.getTabLevel()+1)
				{
					//	Search for right tab
					for (int i = index-1; i >=0; i--)
					{
						Component rightC = getComponentAt(i);
						GridController rightGC = null;
						if (rightC instanceof GridController)
						{
							rightGC = (GridController)rightC;
							if (rightGC.getTabLevel() == oldGC.getTabLevel()+1)
							{
								ADialog.warn(0, this, "TabSwitchJumpGo", rightGC.getTitle());
								return;
							}
						}
					}
					ADialog.warn(0, this, "TabSwitchJump");
					return;
				}
				oldGC.setMnemonics(false);
			}
		}
		//	Switch
		super.setSelectedIndex (index);
		if (newGC != null)
			newGC.setMnemonics(true);
	}	//	setSelectedIndex

}   //  VTabbdPane
