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
package org.compiere.apps;

import java.awt.*;
import java.awt.print.*;
import java.util.*;
import org.compiere.print.*;
import org.compiere.util.*;

/**
 *	PrintScreen Painter
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: PrintScreenPainter.java,v 1.9 2005/03/11 20:28:21 jjanke Exp $
 */
public class PrintScreenPainter implements Pageable, Printable
{
	/**
	 *  PrintScreen Painter
	 *  @param element Window to print
	 */
	public PrintScreenPainter (Window element)
	{
		m_element = element;
	}	//	PrintScreenPainter

	/**	Element				*/
	private Window		m_element;

	/**
	 * 	Get Number of pages
	 * 	@return 1
	 */
	public int getNumberOfPages()
	{
		return 1;
	}	//	getNumberOfPages

	/**
	 * 	Get Printable
	 * 	@param pageIndex page index
	 * 	@return this
	 * 	@throws java.lang.IndexOutOfBoundsException
	 */
	public Printable getPrintable(int pageIndex) throws java.lang.IndexOutOfBoundsException
	{
		return this;
	}	//	getPrintable

	/**
	 * 	Get Page Format
	 * 	@param pageIndex page index
	 * 	@return Portrait
	 * 	@throws java.lang.IndexOutOfBoundsException
	 */
	public PageFormat getPageFormat(int pageIndex) throws java.lang.IndexOutOfBoundsException
	{
		CPaper paper = new CPaper(false);
		return paper.getPageFormat();
	}	//	getPageFormat

	/**
	 *	Print
	 *  @param graphics graphics
	 *  @param pageFormat page format
	 *  @param pageIndex page index
	 *  @return NO_SUCH_PAGE or PAGE_EXISTS
	 *  @throws PrinterException
	 */
	public int print (Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException
	{
	//	log.config( "PrintScreenPainter.print " + pageIndex, "ClipBounds=" + graphics.getClipBounds());
		if (pageIndex > 0)
			return Printable.NO_SUCH_PAGE;
		//
		Graphics2D g2 = (Graphics2D) graphics;

		//	Start position - top of page
		g2.translate (pageFormat.getImageableX(), pageFormat.getImageableY());

		//	Print Header
		String header = Msg.getMsg(Env.getCtx(), "PrintScreen") + " - "
			+ DisplayType.getDateFormat(DisplayType.DateTime).format(new Date());
		int y = g2.getFontMetrics().getHeight();	//	leading + ascent + descent
		g2.drawString(header, 0, y);
		//	Leave one row free
		g2.translate (0, 2*y);

		double xRatio = pageFormat.getImageableWidth() / m_element.getSize().width;
		double yRatio = (pageFormat.getImageableHeight() - 2*y) / m_element.getSize().height;
		//	Sacle evenly, but don't inflate
		double ratio = Math.min(Math.min(xRatio, yRatio), 1.0);
		g2.scale (ratio, ratio);
		//	Print Element
		m_element.printAll (g2);

		return Printable.PAGE_EXISTS;
	}	//	print

	/*************************************************************************/

	/**
	 *	Static print start
	 *  @param element window
	 */
	public static void printScreen (Window element)
	{
		PrintUtil.print(new PrintScreenPainter(element), null, "PrintScreen", 1, false);
	}	//	printScreen

}	//	PrintScreenPainter
