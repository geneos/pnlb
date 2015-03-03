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
package org.compiere.grid.ed;

import java.text.*;
import javax.swing.text.*;

import org.compiere.util.*;

/**
 *  Number Document Model.
 *  Locale independent editing of numbers by removing thousands
 *  and treating ., as decimal separator. Final formatting in VNumber.setValue
 *
 *  @see VNumber
 * 	@author 	Jorg Janke
 * 	@version 	$Id: MDocNumber.java,v 1.8 2005/11/14 02:10:57 jjanke Exp $
 */
public final class MDocNumber extends PlainDocument
{
	/**
	 *	Constructor
	 *  @param displayType
	 *  @param format
	 *  @param tc
	 *  @param title
	 */
	public MDocNumber(int displayType, DecimalFormat format,
		JTextComponent tc, String title)
	{
		super();
		if (format == null || tc == null || title == null)
			throw new IllegalArgumentException("Invalid argument");
		//
		m_displayType = displayType;
		m_format = format;
		m_tc = tc;
		m_title = title;
		//
		DecimalFormatSymbols sym = m_format.getDecimalFormatSymbols();
		m_decimalSeparator = sym.getDecimalSeparator();
		m_groupingSeparator = sym.getGroupingSeparator();
		m_minusSign = sym.getMinusSign();
	//	log.finest("Decimal=" + m_decimalSeparator + "(" + (int)m_decimalSeparator
	//		+ ") - Group=" + m_groupingSeparator + "(" + (int)m_groupingSeparator +")");
	}	//	MDocNumber

	/** DisplayType used            */
	private int    			        m_displayType = 0;
	/** Number Format               */
	private DecimalFormat	        m_format = null;
	/** The 'owning' component      */
	private JTextComponent	        m_tc = null;
	/** Title for calculator        */
	private String			        m_title = null;
	/** Decimal Separator			*/
	private char					m_decimalSeparator = '.';
	/** Grouping Separator			*/
	private char					m_groupingSeparator = ',';
	/** Minus Sign					*/
	private char					m_minusSign = '-';
	/**	Logger	*/
	private static CLogger 			log = CLogger.getCLogger (MDocNumber.class);
	
	/**************************************************************************
	 *	Insert String
	 *  @param origOffset
	 *  @param string
	 *  @param attr
	 *  @throws BadLocationException
	 */
	public void insertString(int origOffset, String string, AttributeSet attr)
		throws BadLocationException
	{
	//	log.log(Level.ALL, "Orig=" + origOffset + " String=" + string + " Length=" + string.length());
		if (origOffset < 0 || string == null)
			throw new IllegalArgumentException("Invalid argument");

		int offset = origOffset;
		int length = string.length();
		//	From DataBinder (assuming correct format)
		if (length != 1)
		{
			super.insertString(offset, string, attr);
			return;
		}

		/**
		 *	Manual Entry
		 */
		String content = getText();
		//	remove all Thousands
		if (content.indexOf(m_groupingSeparator) != -1)
		{
			StringBuffer result = new StringBuffer();
			for (int i = 0; i < content.length(); i++)
			{
				if (content.charAt(i) == m_groupingSeparator)
				{
					if (i < offset)
						offset--;
				}
				else
					result.append(content.charAt(i));
			}
			super.remove(0, content.length());
			super.insertString(0, result.toString(), attr);
			//
			m_tc.setCaretPosition(offset);
		//	ADebug.trace(ADebug.l6_Database, "Clear Thousands (" + m_format.toPattern() + ")" + content + " -> " + result.toString());
			content = result.toString();
		}	//	remove Thousands

		/**********************************************************************
		 *	Check Character entered
		 */
		char c = string.charAt(0);
		if (Character.isDigit(c))   // c >= '0' && c <= '9')
		{
		//	ADebug.trace(ADebug.l6_Database, "Digit=" + c);
			super.insertString(offset, string, attr);
			return;
		}

		//	Plus - remove minus sign
		if (c == '+')
		{
		//	ADebug.trace(ADebug.l6_Database, "Plus=" + c);
			//	only positive numbers
			if (m_displayType == DisplayType.Integer)
				return;
			if (content.charAt(0) == '-')
				super.remove(0, 1);
		}

		//	Toggle Minus - put minus on start of string
		else if (c == '-' || c == m_minusSign)
		{
		//	ADebug.trace(ADebug.l6_Database, "Minus=" + c);
			//	no minus possible
			if (m_displayType == DisplayType.Integer)
				return;
			//	remove or add
			if (content.length() > 0 && content.charAt(0) == '-')
				super.remove(0, 1);
			else
				super.insertString(0, "-", attr);
		}

		//	Decimal - remove other decimals
		//	Thousand - treat as Decimal
		else if (c == m_decimalSeparator || c == m_groupingSeparator || c == '.' || c == ',')
		{
		//	log.info("Decimal=" + c + " (ds=" + m_decimalSeparator + "; gs=" + m_groupingSeparator + ")");
			//  no decimals on integers
			if (m_displayType == DisplayType.Integer)
				return;
			int pos = content.indexOf(m_decimalSeparator);

			//	put decimal in
			String decimal = String.valueOf(m_decimalSeparator);
			super.insertString(offset, decimal, attr);

			//	remove other decimals
			if (pos != 0)
			{
				content = getText();
				StringBuffer result = new StringBuffer();
				int correction = 0;
				for (int i = 0; i < content.length(); i++)
				{
					if (content.charAt(i) == m_decimalSeparator)
					{
						if (i == offset)
							result.append(content.charAt(i));
						else if (i < offset)
							correction++;
					}
					else
						result.append(content.charAt(i));
				}
				super.remove(0, content.length());
				super.insertString(0, result.toString(), attr);
				m_tc.setCaretPosition(offset-correction+1);
			}	//	remove other decimals
		}	//	decimal or thousand

		//	something else
		else
		{
			log.fine("Input=" + c + " (" + (int)c + ")");
			String result = VNumber.startCalculator(m_tc, getText(),
				m_format, m_displayType, m_title);
			super.remove(0, content.length());
			super.insertString(0, result, attr);
		}
	}	//	insertString

	
	/**************************************************************************
	 *	Delete String
	 *  @param origOffset
	 *  @param length
	 *  @throws BadLocationException
	 */
	public void remove (int origOffset, int length)
		throws BadLocationException
	{
	//	ADebug.trace(ADebug.l5_DData, "MDocNumber.remove - Offset=" + offset + " Length=" + length);
		if (origOffset < 0 || length < 0)
			throw new IllegalArgumentException("MDocNumber.remove - invalid argument");

		int offset = origOffset;
		if (length != 1)
		{
			super.remove(offset, length);
			return;
		}
		/**
		 *	Manual Entry
		 */
		String content = getText();
		//	remove all Thousands
		if (content.indexOf(m_groupingSeparator) != -1)
		{
			StringBuffer result = new StringBuffer();
			for (int i = 0; i < content.length(); i++)
			{
				if (content.charAt(i) == m_groupingSeparator && i != origOffset)
				{
					if (i < offset)
						offset--;
				}
				else
					result.append(content.charAt(i));
			}
			super.remove(0, content.length());
			super.insertString(0, result.toString(), null);
			m_tc.setCaretPosition(offset);
		}	//	remove Thousands
		super.remove(offset, length);
	}	//	remove

	/**
	 *	Get Full Text
	 *  @return text
	 */
	private String getText()
	{
		Content c = getContent();
		String str = "";
		try
		{
			str = c.getString(0, c.length()-1);		//	cr at end
		}
		catch (Exception e)
		{}
		return str;
	}	//	getString

}	//	MDocNumber
