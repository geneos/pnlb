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
package org.compiere.model;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import org.apache.ecs.xhtml.*;
import org.compiere.plaf.CompiereColor;
import org.compiere.util.*;


/**
 *	Window Model
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: MWindow.java,v 1.34 2005/09/28 01:34:03 jjanke Exp $
 */
public final class MWindow implements Serializable
{
	/**
	 *	Constructor
	 *  @param vo value object
	 */
	public MWindow (MWindowVO vo)
	{
		m_vo = vo;
		if (loadTabData())
			enableEvents();
	}	//	MWindow

	/** Value Object                */
	private MWindowVO   	m_vo;
	/**	Tabs						*/
	private ArrayList<MTab>	m_tabs = new ArrayList<MTab>();
	/**	Position					*/
	private Rectangle 		m_position = null;

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(MWindow.class);
	
	/**************************************************************************
	 *	Dispose
	 */
	public void dispose()
	{
		log.info("AD_Window_ID=" + m_vo.AD_Window_ID);
		for (int i = 0; i < getTabCount(); i++)
			getTab(i).dispose();
		m_tabs.clear();
		m_tabs = null;
	}	//	dispose

	/**
	 *  Load is complete.
	 *  Return when async load is complete
	 *  Used for performance tests (Base.test())
	 */
	public void loadCompete ()
	{
		//  for all tabs
		for (int i = 0; i < getTabCount(); i++)
			getTab(i).getMTable().loadComplete();
	}   //  loadComplete

	/**
	 *	Get Tab data and create MTab(s)
	 *  @return true if tab loaded
	 */
	private boolean loadTabData()
	{
		log.config("");

		if (m_vo.Tabs == null)
			return false;

		for (int t = 0; t < m_vo.Tabs.size(); t++)
		{
			MTabVO mTabVO = (MTabVO)m_vo.Tabs.get(t);
			if (mTabVO != null)
			{
				MTab mTab = new MTab(mTabVO);
				//	Set Link Column
				if (mTab.getLinkColumnName().length() == 0)
				{
					ArrayList parents = mTab.getParentColumnNames();
					//	No Parent - no link
					if (parents.size() == 0)
						;
					//	Standard case
					else if (parents.size() == 1)
						mTab.setLinkColumnName((String)parents.get(0));
					else
					{
						//	More than one parent.
						//	Search prior tabs for the "right parent"
						//	for all previous tabs
						for (int i = 0; i < m_tabs.size(); i++)
						{
							//	we have a tab
							MTab tab = (MTab)m_tabs.get(i);
							String tabKey = tab.getKeyColumnName();		//	may be ""
							//	look, if one of our parents is the key of that tab
							for (int j = 0; j < parents.size(); j++)
							{
								String parent = (String)parents.get(j);
								if (parent.equals(tabKey))
								{
									mTab.setLinkColumnName(parent);
									break;
								}
								//	The tab could have more than one key, look into their parents
								if (tabKey.equals(""))
									for (int k = 0; k < tab.getParentColumnNames().size(); k++)
										if (parent.equals(tab.getParentColumnNames().get(k)))
										{
											mTab.setLinkColumnName(parent);
											break;
										}
							}	//	for all parents
						}	//	for all previous tabs
					}	//	parents.size > 1
				}	//	set Link column
				mTab.setLinkColumnName(null);	//	overwrites, if AD_Column_ID exists
				//
				m_tabs.add(mTab);
			}
		}	//  for all tabs
		return true;
	}	//	loadTabData

	/**
	 *  Get Window Icon
	 *  @return Icon for Window
	 */
	public Image getImage()
	{
		if (m_vo.AD_Image_ID == 0)
			return null;
		//
		MImage mImage = MImage.get(Env.getCtx(), m_vo.AD_Image_ID);
		return mImage.getImage();
	}   //  getImage

	/**
	 *  Get Window Icon
	 *  @return Icon for Window
	 */
	public Icon getIcon()
	{
		if (m_vo.AD_Image_ID == 0)
			return null;
		//
		MImage mImage = MImage.get(Env.getCtx(), m_vo.AD_Image_ID);
		return mImage.getIcon();
	}   //  getIcon

	/**
	 *  Get Color
	 *  @return CompiereColor or null
	 */
	public CompiereColor getColor()
	{
		if (m_vo.AD_Color_ID == 0)
			return null;
		MColor mc = new MColor(m_vo.ctx,  m_vo.AD_Color_ID, null);
		return mc.getCompiereColor();
	}   //  getColor

	/**
	 * 	SO Trx Window
	 *	@return true if SO Trx
	 */
	public boolean isSOTrx()
	{
		return m_vo.IsSOTrx;
	}	//	isSOTrx
	
	
	/**
	 *  Open and query first Tab (events should be enabled) and get first row.
	 */
	public void query()
	{
		log.info("");
		MTab tab = getTab(0);
		tab.query(false, 0);
		if (tab.getRowCount() > 0)
			tab.navigate(0);
	}   //  open

	/**
	 *  Enable Events - enable data events of tabs (add listeners)
	 */
	private void enableEvents()
	{
		for (int i = 0; i < getTabCount(); i++)
			getTab(i).enableEvents();
	}   //  enableEvents

	/**
	 *	Get number of Tabs
	 *  @return number of tabs
	 */
	public int getTabCount()
	{
		return m_tabs.size();
	}	//	getTabCount

	/**
	 *	Get i-th MTab - null if not valid
	 *  @param i index
	 *  @return MTab
	 */
	public MTab getTab (int i)
	{
		if (i < 0 || i+1 > m_tabs.size())
			return null;
		return (MTab)m_tabs.get(i);
	}	//	getTab

	/**
	 *	Get Window_ID
	 *  @return AD_Window_ID
	 */
	public int getAD_Window_ID()
	{
		return m_vo.AD_Window_ID;
	}	//	getAD_Window_ID

	/**
	 *	Get WindowNo
	 *  @return WindowNo
	 */
	public int getWindowNo()
	{
		return m_vo.WindowNo;
	}	//	getWindowNo

	/**
	 *	Get Name
	 *  @return name
	 */
	public String getName()
	{
		return m_vo.Name;
	}	//	getName

	/**
	 *	Get Description
	 *  @return Description
	 */
	public String getDescription()
	{
		return m_vo.Description;
	}	//	getDescription

	/**
	 *	Get Help
	 *  @return Help
	 */
	public String getHelp()
	{
		return m_vo.Help;
	}	//	getHelp

	/**
	 *	Get Window Type
	 *  @return Window Type see WindowType_*
	 */
	public String getWindowType()
	{
		return m_vo.WindowType;
	}	//	getWindowType

	/**
	 *	Is Transaction Window
	 *  @return true if transaction
	 */
	public boolean isTransaction()
	{
		return m_vo.WindowType.equals(MWindowVO.WINDOWTYPE_TRX);
	}   //	isTransaction

	/**
	 * 	Get Window Size
	 *	@return window size or null if not set
	 */
	public Dimension getWindowSize()
	{
		if (m_vo.WinWidth != 0 && m_vo.WinHeight != 0)
			return new Dimension (m_vo.WinWidth, m_vo.WinHeight);
		return null;
	}	//	getWindowSize

	/**
	 *  To String
	 *  @return String representation
	 */
	public String toString()
	{
		return "MWindow[" + m_vo.WindowNo + "," + m_vo.Name + " (" + m_vo.AD_Window_ID + ")]";
	}   //  toString

	/**
	 * 	Get Help HTML Document
	 * 	@param javaClient true if java client false for browser
	 *	@return help 
	 */
	public WebDoc getHelpDoc (boolean javaClient)
	{
		String title = Msg.getMsg(Env.getCtx(), "Window") + ": " + getName();
		WebDoc doc = null;
		if (javaClient)
		{
			doc = WebDoc.create (false, title, javaClient);
		}
		else	//	HTML
		{
			doc = WebDoc.createPopup (title);
			doc.addPopupClose();
		}
		
	//	body.addElement("&copy;&nbsp;Compiere &nbsp; ");
	//	body.addElement(new a("http://www.compiere.org/help/", "Online Help"));
		td center  = doc.addPopupCenter(false);
		//	Window
		if (getDescription().length() != 0)
			center.addElement(new p().addElement(new i(getDescription())));
		if (getHelp().length() != 0)
			center.addElement(new p().addElement(getHelp()));

		//	Links to Tabs
		int size = getTabCount();
		p p = new p();
		for (int i = 0; i < size; i++)
		{
			MTab tab = getTab(i);
			if (i > 0)
				p.addElement(" - ");
			p.addElement(new a("#Tab"+i).addElement(tab.getName()));
		}
		center.addElement(p)
			.addElement(new p().addElement(WebDoc.NBSP));

		//	For all Tabs
		for (int i = 0; i < size; i++)
		{
			table table = new table("1", "5", "5", "100%", null);
			MTab tab = getTab(i);
			tr tr = new tr()
				.addElement(new th()
					.addElement(new a().setName("Tab" + i)
						.addElement(new h2(Msg.getMsg(Env.getCtx(), "Tab") + ": " + tab.getName()))));
			if (tab.getDescription().length() != 0)
				tr.addElement(new th()
					.addElement(new i(tab.getDescription())));
			else
				tr.addElement(new th()
					.addElement(WebDoc.NBSP));
			table.addElement(tr);
			//	Desciption
			td td = new td().setColSpan(2);
			if (tab.getHelp().length() != 0)
				td.addElement(new p().addElement(tab.getHelp()));
			//	Links to Fields
			p = new p();
			for (int j = 0; j < tab.getFieldCount(); j++)
			{
				MField field = tab.getField(j);
				String hdr = field.getHeader();
				if (hdr != null && hdr.length() > 0)
				{
					if (j > 0)
						p.addElement(" - ");
					p.addElement(new a("#Field" + i + j, hdr));
				}
			}
			td.addElement(p);
			table.addElement(new tr().addElement(td));

			//	For all Fields
			for (int j = 0; j < tab.getFieldCount(); j++)
			{
				MField field = tab.getField(j);
				String hdr = field.getHeader();
				if (hdr != null && hdr.length() > 0)
				{
					td = new td().setColSpan(2)
						.addElement(new a().setName("Field" + i + j)
							.addElement(new h3(Msg.getMsg(Env.getCtx(), "Field") + ": " + hdr))
						);
					if (field.getDescription().length() != 0)
						td.addElement(new i(field.getDescription()));
					//
					if (field.getHelp().length() != 0)
						td.addElement(new p().addElement(field.getHelp()));
					table.addElement(new tr().addElement(td));
				}
			}	//	for all Fields
			
			center.addElement(table);
			center.addElement(new p().addElement(WebDoc.NBSP));
		}	//	for all Tabs
		
		if (!javaClient)
			doc.addPopupClose();
	//	System.out.println(doc.toString());
		return doc;
	}	//	getHelpDoc
	
	
}	//	MWindow
