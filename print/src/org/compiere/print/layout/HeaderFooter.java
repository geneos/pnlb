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
package org.compiere.print.layout;

import java.awt.*;
import java.util.*;
import org.compiere.model.*;

/**
 *	Header Footer
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: HeaderFooter.java,v 1.10 2005/11/13 23:40:21 jjanke Exp $
 */
public class HeaderFooter
{
	/**
	 *	Standard Constructor
	 *  @param ctx context
	 */
	public HeaderFooter (Properties ctx)
	{
		m_ctx = ctx;
	}	//	HeaderFooter

	/**	Context						*/
	private Properties 		m_ctx;

	/**	Header/Footer content			*/
	private ArrayList<PrintElement>	m_elements = new ArrayList<PrintElement>();
	/** Header/Footer content as Array	*/
	private PrintElement[] 	m_pe = null;

	/**
	 * 	Add Print Element to Page
	 * 	@param element print element
	 */
	public void addElement (PrintElement element)
	{
		if (element != null)
			m_elements.add(element);
		m_pe = null;
	}	//	addElement

	/**
	 * 	Get Elements
	 *	@return array of elements
	 */
	public PrintElement[] getElements()
	{
		if (m_pe == null)
		{
			m_pe = new PrintElement[m_elements.size()];
			m_elements.toArray(m_pe);
		}
		return m_pe;
	}	//	getElements

	/**
	 * 	Paint Page Header/Footer on Graphics in Bounds
	 *
	 * 	@param g2D graphics
	 * 	@param bounds page bounds
	 *  @param isView true if online view (IDs are links)
	 */
	public void paint (Graphics2D g2D, Rectangle bounds, boolean isView)
	{
		Point pageStart = new Point(bounds.getLocation());
		getElements();
		for (int i = 0; i < m_pe.length; i++)
			m_pe[i].paint(g2D, 0, pageStart, m_ctx, isView);
	}	//	paint

	/**
	 * 	Get DrillDown value
	 * 	@param relativePoint relative Point
	 * 	@return if found NamePait or null
	 */
	public MQuery getDrillDown (Point relativePoint)
	{
		MQuery retValue = null;
		for (int i = 0; i < m_elements.size() && retValue == null; i++)
		{
			PrintElement element = (PrintElement)m_elements.get(i);
			retValue = element.getDrillDown (relativePoint, 1);
		}
		return retValue;
	}	//	getDrillDown

}	//	HeaderFooter
