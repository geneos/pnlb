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
package org.compiere.print;

import java.awt.*;
import org.compiere.model.*;
import org.compiere.print.layout.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	View Panel
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: View.java,v 1.20 2005/03/11 20:34:40 jjanke Exp $
 */
public class View extends CPanel
{
	/**
	 *	Print Preview
	 *  @param layout Layout
	 */
	public View (LayoutEngine layout)
	{
		m_layout = layout;
	}	//	View

	/**	Layout to be Printed		*/
	private LayoutEngine 			m_layout;


	/**	Zoom Level						*/
	private int						m_zoomLevel = 0;
	/** Zoom Options					*/
	public static final String[]	ZOOM_OPTIONS = new String[]
		{"100%", "75%", "50%"};
	/**	Margin around paper				*/
	public static int				MARGIN = 5;
	/** Margin Background Color			*/
	private static Color			COLOR_BACKGROUND = Color.lightGray;

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(View.class);

	/*************************************************************************/

	/**
	 * 	Minimum Size
	 * 	@return Max Page Size
	 */
	public Dimension getMinimumSize()
	{
		return getMaximumSize();
	}	//	getMinimumSize

	/**
	 * 	Minimum Size
	 * 	@return Max Page Size
	 */
	public Dimension getMaximumSize()
	{
		return new Dimension (getPaperWidth()+(2*MARGIN),
			(getPaperHeight()+MARGIN)*getPageCount()+MARGIN);
	}	//	getMaximumSize

	/**
	 * 	Preferred Size
	 * 	@return Max Page Size
	 */
	public Dimension getPreferredSize()
	{
		return getMaximumSize();
	}	//	getPreferredSize

	/**
	 * 	Is Archivable
	 *	@return true if archivable
	 */
	public boolean isArchivable()
	{
		return ArchiveEngine.isValid(m_layout);
	}	//	IsArchivable

	/**
	 * 	Paint Component
	 * 	@param g Graphics
	 */
	public void paintComponent (Graphics g)
	{
	//	log.fine( "View.paintComponent", g.getClip());
		Graphics2D g2D = (Graphics2D)g;
		Rectangle bounds = g2D.getClipBounds();
		//
		g2D.setColor(COLOR_BACKGROUND);
		g2D.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

		//	for all pages
		for (int page = 0; page < m_layout.getPages().size(); page++)
		{
			Rectangle pageRectangle = getRectangleOfPage(page+1);
			if (bounds.intersects(pageRectangle))
			{
				Page p = (Page)m_layout.getPages().get(page);
				p.paint (g2D, pageRectangle, true, false);		//	sets context
				m_layout.getHeaderFooter().paint(g2D, pageRectangle, true);
			}	//	paint page
		}	//	for all pages
	}	//	paintComponent


	/**************************************************************************
	 * 	Set Zoom Level
	 * 	@param level zoom level
	 */
	public void setZoomLevel(int level)
	{
		m_zoomLevel = level;
	}	//	setZoomLevel

	/**
	 * 	Set Zoom Level
	 * 	@param levelString zoom level string
	 */
	public void setZoomLevel(String levelString)
	{
		for (int i = 0; i < ZOOM_OPTIONS.length; i++)
		{
			if (ZOOM_OPTIONS[i].equals(levelString))
			{
				m_zoomLevel = i;
				break;
			}
		}
	}	//	setZoomLevel

	/**
	 * 	Get Zoom Level
	 * 	@return zoom level
	 */
	public int getZoomLevel()
	{
		return m_zoomLevel;
	}	//	getZoomLevel

	/**
	 * 	Get Rectange of Page
	 * 	@param pageNo page no
	 * 	@return rectangle
	 */
	public Rectangle getRectangleOfPage(int pageNo)
	{
		int y = MARGIN + ((pageNo-1) * (getPaperHeight() + MARGIN));
		return new Rectangle (MARGIN, y, getPaperWidth(), getPaperHeight());
	}	//	getRectangleOfPage


	/**
	 * 	Get Page at Point
	 * 	@param p Point
	 * 	@return page as float to determine also position on page
	 */
	public float getPageNoAt (Point p)
	{
		float y = p.y;
		float pageHeight = getPaperHeight() + MARGIN;
		return 1f + (y/pageHeight);
	}	//	getPageAt

	/**
	 * 	Get Page Count
	 * 	@return page count
	 */
	public int getPageCount()
	{
		return m_layout.getPages().size();
	}	//	getPageCount

	/**
	 * 	Get Page Info for Multi-Page tables
	 * 	@param pageNo page
	 * 	@return info e.g. (1,1)
	 */
	public String getPageInfo(int pageNo)
	{
		return m_layout.getPageInfo(pageNo);
	}	//	getPageInfo

	/**
	 * 	Get Max Page Info for Multi-Page tables
	 * 	@return info e.g. (3,2)
	 */
	public String getPageInfoMax()
	{
		return m_layout.getPageInfoMax();
	}	//	getPageInfo

	/**
	 * 	Get Paper
	 * 	@return paper
	 */
	public CPaper getPaper()
	{
		return m_layout.getPaper();
	}	//	getPaper

	/**
	 * 	Get Paper Height
	 * 	@return paper height
	 */
	public int getPaperHeight()
	{
		return (int)m_layout.getPaper().getHeight(true);
	}	//	getPaperHeight

	/**
	 * 	Get Paper Height
	 * 	@return paper height
	 */
	public int getPaperWidth()
	{
		return (int)m_layout.getPaper().getWidth(true);
	}	//	getPaperHeight

	/**
	 * 	Get Drill Down
	 *  @param absolutePoint point
	 *  @return Drill Down
	 */
	public MQuery getDrillDown (Point absolutePoint)
	{
		int pageNo = (int)getPageNoAt(absolutePoint);
		Rectangle pageRectangle = getRectangleOfPage(pageNo);
		Point relativePoint = new Point (absolutePoint.x-pageRectangle.x,
			absolutePoint.y-pageRectangle.y);
		Page page = (Page)m_layout.getPages().get(pageNo-1);
		//
		log.config("Relative=" + relativePoint + ", " + page);
	//	log.config("AbsolutePoint=" + absolutePoint + ", PageNo=" + pageNo + ", pageRectangle=" + pageRectangle);
		MQuery retValue = page.getDrillDown (relativePoint);
		if (retValue == null)
			retValue = m_layout.getHeaderFooter().getDrillDown (relativePoint);
		return retValue;
	}	//	getDrillDown

	/**
	 * 	Get Drill Across
	 *  @param absolutePoint point
	 *  @return Drill Across
	 */
	public MQuery getDrillAcross (Point absolutePoint)
	{
		int pageNo = (int)getPageNoAt(absolutePoint);
		Rectangle pageRectangle = getRectangleOfPage(pageNo);
		Point relativePoint = new Point (absolutePoint.x-pageRectangle.x,
			absolutePoint.y-pageRectangle.y);
		Page page = (Page)m_layout.getPages().get(pageNo-1);
		//
		log.config("Relative=" + relativePoint + ", " + page);
	//	log.config("AbsolutePoint=" + absolutePoint + ", PageNo=" + pageNo + ", pageRectangle=" + pageRectangle);
		return page.getDrillAcross (relativePoint);
	}	//	getDrillAcross


}	//	View
