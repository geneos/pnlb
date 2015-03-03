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
package org.compiere.install;

import java.sql.*;
import org.compiere.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 *	SAX Handler for parsing Translation
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: TranslationHandler.java,v 1.10 2005/11/14 02:10:58 jjanke Exp $
 */
public class TranslationHandler extends DefaultHandler
{
	/**
	 * 	Translation Handler
	 * 	@param AD_Client_ID only certain client if id >= 0
	 */
	public TranslationHandler (int AD_Client_ID)
	{
		m_AD_Client_ID = AD_Client_ID;
	}	//	TranslationHandler

	/**	Client							*/
	private int				m_AD_Client_ID = -1;
	/** Language						*/
	private String			m_AD_Language = null;
	/** Is Base Language				*/
	private boolean			m_isBaseLanguage = false;
	/** Table							*/
	private String			m_TableName = null;
	/** Update SQL						*/
	private String			m_updateSQL = null;
	/** Current ID						*/
	private String			m_curID = null;
	/** Translated Flag					*/
	private String			m_trl = null;
	/** Current ColumnName				*/
	private String			m_curColumnName = null;
	/** Current Value					*/
	private StringBuffer	m_curValue = null;
	/**	SQL								*/
	private StringBuffer	m_sql = null;

	private Timestamp		m_time = new Timestamp(System.currentTimeMillis());
	private int				m_updateCount = 0;

	private static CLogger	log = CLogger.getCLogger(TranslationHandler.class);

	
	/**************************************************************************
	 * 	Receive notification of the start of an element.
	 *
	 * 	@param uri namespace
	 * 	@param localName simple name
	 * 	@param qName qualified name
	 * 	@param attributes attributes
	 * 	@throws org.xml.sax.SAXException
	 */
	public void startElement (String uri, String localName, String qName, Attributes attributes)
		throws org.xml.sax.SAXException
	{
	//	log.fine( "TranslationHandler.startElement", qName);	// + " - " + uri + " - " + localName);
		if (qName.equals(Translation.XML_TAG))
		{
			m_AD_Language = attributes.getValue(Translation.XML_ATTRIBUTE_LANGUAGE);
			m_isBaseLanguage = Language.isBaseLanguage(m_AD_Language);
			m_TableName = attributes.getValue(Translation.XML_ATTRIBUTE_TABLE);
			m_updateSQL = "UPDATE " + m_TableName;
			if (!m_isBaseLanguage)
				m_updateSQL += "_Trl";
			m_updateSQL += " SET ";
			log.fine("AD_Language=" + m_AD_Language + ", Base=" + m_isBaseLanguage + ", TableName=" + m_TableName);
		}
		else if (qName.equals(Translation.XML_ROW_TAG))
		{
			m_curID = attributes.getValue(Translation.XML_ROW_ATTRIBUTE_ID);
			m_trl = attributes.getValue(Translation.XML_ROW_ATTRIBUTE_TRANSLATED);
		//	log.finest( "ID=" + m_curID);
			m_sql = new StringBuffer();
		}
		else if (qName.equals(Translation.XML_VALUE_TAG))
		{
			m_curColumnName = attributes.getValue(Translation.XML_VALUE_ATTRIBUTE_COLUMN);
		//	log.finest( "ColumnName=" + m_curColName);
		}
		else
			log.severe ("UNKNOWN TAG: " + qName);
		m_curValue = new StringBuffer();
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
		m_curValue.append(ch, start, length);
	//	Log.trace(Log.l6_Database+1, "TranslationHandler.characters", m_curValue.toString());
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
	//	Log.trace(Log.l6_Database+1, "TranslationHandler.endElement", qName);
		if (qName.equals(Translation.XML_TAG))
		{
		}
		else if (qName.equals(Translation.XML_ROW_TAG))
		{
			//	Set section
			if (m_sql.length() > 0)
				m_sql.append(",");
			m_sql.append("Updated=").append(DB.TO_DATE(m_time, false));
			if (!m_isBaseLanguage)
			{
				if (m_trl != null 
					&& ("Y".equals(m_trl) || "N".equals(m_trl)))
					m_sql.append(",IsTranslated='").append(m_trl).append("'");
				else
					m_sql.append(",IsTranslated='Y'");
			}
			//	Where section
			m_sql.append(" WHERE ")
				.append(m_TableName).append("_ID=").append(m_curID);
			if (!m_isBaseLanguage)
				m_sql.append(" AND AD_Language='").append(m_AD_Language).append("'");
			if (m_AD_Client_ID >= 0)
				m_sql.append(" AND AD_Client_ID=").append(m_AD_Client_ID);
			//	Update section
			m_sql.insert(0, m_updateSQL);

			//	Execute
			int no = DB.executeUpdate(m_sql.toString(), null);
			if (no == 1)
			{
				if (CLogMgt.isLevelFinest())
					log.fine(m_sql.toString());
				m_updateCount++;
			}
			else if (no == 0)
				log.warning ("Not Found - " + m_sql.toString());
			else
				log.severe ("Update Rows=" + no + " (Should be 1) - " + m_sql.toString());
		}
		else if (qName.equals(Translation.XML_VALUE_TAG))
		{
			if (m_sql.length() > 0)
				m_sql.append(",");
			m_sql.append(m_curColumnName).append("=").append(DB.TO_STRING(m_curValue.toString()));
		}
	}	//	endElement


	/**
	 * 	Get Number of updates
	 * 	@return update count
	 */
	public int getUpdateCount()
	{
		return m_updateCount;
	}	//	getUpdateCount

}	//	TranslationHandler
