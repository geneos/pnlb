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

import java.util.*;

import org.xml.sax.helpers.*;
import org.xml.sax.*;

/**
 *	SAX Handler for parsing PrintData
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: PrintDataHandler.java,v 1.10 2005/11/13 23:40:21 jjanke Exp $
 */
public class PrintDataHandler extends DefaultHandler
{
	/**
	 *	Constructor
	 * 	@param ctx context
	 */
	public PrintDataHandler(Properties ctx)
	{
		m_ctx = ctx;
	}	//	PrintDataHandler

	/**	Context					*/
	private Properties		m_ctx = null;
	/**	Final Structure			*/
	private PrintData		m_pd = null;

	/** Current Active Element Name		*/
	private String			m_curPDEname = null;
	/** Current Active Element Value	*/
	private StringBuffer	m_curPDEvalue = null;
	/**	Current Active Print Data		*/
	private PrintData		m_curPD = null;

	/**
	 * 	Get PrintData
	 * 	@return PrintData
	 */
	public PrintData getPrintData()
	{
		return m_pd;
	}	//	getPrintData

	/*************************************************************************/

	/**
	 * 	Receive notification of the start of an element.
	 *
	 * 	@param uri namespace
	 * 	@param localName simple name
	 * 	@param qName qualified name
	 * 	@param attributes attributes
	 * 	@throws org.xml.sax.SAXException
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes)
		throws org.xml.sax.SAXException
	{
		if (qName.equals(PrintData.XML_TAG))
		{
			String name = attributes.getValue(PrintData.XML_ATTRIBUTE_NAME);
			if (m_pd == null)
			{
				m_pd = new PrintData(m_ctx, name);
				push(m_pd);
			}
			else
			{
				PrintData temp = new PrintData(m_ctx, name);
				m_curPD.addNode(temp);
				push(temp);
			}
		}
		else if (qName.equals(PrintData.XML_ROW_TAG))
		{
			m_curPD.addRow(false, 0);
		}
		else if (qName.equals(PrintDataElement.XML_TAG))
		{
			m_curPDEname = attributes.getValue(PrintDataElement.XML_ATTRIBUTE_NAME);
			m_curPDEvalue = new StringBuffer();
		}
	}	//	startElement

	/**
	 *	Receive notification of character data inside an element.
	 *
	 * 	@param ch buffer
	 * 	@param start start
	 * 	@param length length
	 * 	@throws SAXException
	 */
	public void characters (char ch[], int start, int length)
		throws SAXException
	{
		m_curPDEvalue.append(ch, start, length);
	}	//	characters

	/**
	 *	Receive notification of the end of an element.
	 * 	@param uri namespace
	 * 	@param localName simple name
	 * 	@param qName qualified name
	 * 	@throws SAXException
	 */
	public void endElement (String uri, String localName, String qName)
		throws SAXException
	{
		if (qName.equals(PrintData.XML_TAG))
		{
			pop();
		}
		else if (qName.equals(PrintDataElement.XML_TAG))
		{
			m_curPD.addNode(new PrintDataElement(m_curPDEname, m_curPDEvalue.toString(),0));
		}
	}	//	endElement

	/*************************************************************************/

	/**	Stack						*/
	private ArrayList<PrintData>	m_stack = new ArrayList<PrintData>();

	/**
	 * 	Push new PD on Stack and set m_cutPD
	 * 	@param newPD new PD
	 */
	private void push (PrintData newPD)
	{
		//	add
		m_stack.add(newPD);
		m_curPD = newPD;
	}	//	push

	/**
	 * 	Pop last PD from Stack and set m_cutPD
	 */
	private void pop ()
	{
		//	remove last
		if (m_stack.size() > 0)
			m_stack.remove(m_stack.size()-1);
		//	get previous
		if (m_stack.size() > 0)
			m_curPD = (PrintData)m_stack.get(m_stack.size()-1);
	}	//	pop

}	//	PrintDataHandler
