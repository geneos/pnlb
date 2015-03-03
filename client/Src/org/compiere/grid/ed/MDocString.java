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

import javax.swing.event.*;
import javax.swing.text.*;
import org.compiere.util.*;

/**
 *	String Input Verification.
 *  <pre>
 *	-	Length is set to length of VFormat or FieldLength if no format defined
 *	Control Characters
 *			(Space) any character
 *		_	Space (fixed character)
 *
 *		l	any Letter a..Z NO space
 *		L	any Letter a..Z NO space converted to upper case
 *		o	any Letter a..Z or space
 *		O	any Letter a..Z or space converted to upper case
 *
 *		0	Digits 0..9 NO space
 *      9   Digits 0..9 or space
 *
 *		a	any Letters & Digits NO space
 *		A	any Letters & Digits NO space converted to upper case
 *		c	any Letters & Digits or space
 *		C	any Letters & Digits or space converted to upper case
 *  </pre>
 *  @see VString
 * 	@author 	Jorg Janke
 * 	@version 	$Id: MDocString.java,v 1.7 2005/03/11 20:28:25 jjanke Exp $
 */
public final class MDocString extends PlainDocument implements CaretListener
{
	/**
	 *	Constructor
	 *  @param VFormat
	 *  @param fieldLength
	 *  @param tc
	 */
	public MDocString(String VFormat, int fieldLength, JTextComponent tc)
	{
		super();
		m_fieldLength = fieldLength;
		setFormat (VFormat);
		m_tc = tc;
		if (tc != null)
			m_tc.addCaretListener(this);
	}	//	MDocNumber

	private String 			m_VFormat;		//	Value Format String
	private String 			m_mask;			//	Fixed Elements
	private int				m_fieldLength;
	private int				m_maxLength;
	private JTextComponent 	m_tc;
	private int				m_lastDot = 0;	//	last dot position
	//
	private static final char SPACE = ' ';
	private static final char SPACE_IND = '_';
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(MDocString.class);

	
	/**
	 *	Set Format
	 *  @param VFormat
	 */
	public final void setFormat (String VFormat)
	{
	//	log.config( "MDocString.setFormat - " + VFormat);
		//
		m_VFormat = VFormat;
		if (m_VFormat == null)
			m_VFormat = "";
		m_mask = m_VFormat;
		//	Set Length
		m_maxLength = m_fieldLength;
		if (m_VFormat.length() == 0)
			return;
		if (m_maxLength > m_VFormat.length())
			m_maxLength = m_VFormat.length();

		//	Create Mask
		m_mask = m_mask.replace('c', SPACE);		//	c	any Letter or Digit or space
		m_mask = m_mask.replace('C', SPACE);		//	C	any Letter or Digit or space converted to upper case
		m_mask = m_mask.replace('a', SPACE);		//	a	any Letters or Digits NO space
		m_mask = m_mask.replace('A', SPACE);		//	A	any Letters or Digits NO space converted to upper case
		m_mask = m_mask.replace('l', SPACE);		//	l	any Letter a..Z NO space
		m_mask = m_mask.replace('L', SPACE);		//	L	any Letter a..Z NO space converted to upper case
		m_mask = m_mask.replace('o', SPACE);		//	o	any Letter a..Z or space
		m_mask = m_mask.replace('O', SPACE);		//	O	any Letter a..Z or space converted to upper case
		m_mask = m_mask.replace('0', SPACE);		//	0	Digits 0..9 NO space
		m_mask = m_mask.replace('9', SPACE);		//	9	Digits 0..9 or space

		//	Check Caret
		if (m_tc == null || m_tc.getCaret() instanceof VOvrCaret)
			return;
		else
			m_tc.setCaret(new VOvrCaret());
	}	//	setFormat

	/**
	 *	Insert String
	 *  @param offset
	 *  @param string
	 *  @param attr
	 *  @throws BadLocationException
	 */
	public void insertString(int offset, String string, AttributeSet attr)
		throws BadLocationException
	{
		//	Max Length
		if (offset >= m_maxLength)
			return;
		//	Do we have a Format or inserted not manually (assuming correct Format)
		if (m_VFormat.length() == 0 || string.length() != 1)
		{
			super.insertString(offset, string, attr);
			return;
		}

		//	Formating required
	//	Log.trace(this,Log.l5_DData, "MDocString.insertString - Offset=" + offset
	//		+ ", String=" + string + ", MaxLength=" + m_maxLength + ", Format=" + m_VFormat + ", Mask=" + m_mask
	//		+ ", Text=" + getText() + ", Length=" + getText().length());

		String text = getText();

		//	Apply Mask, if not target length
		if (m_VFormat.length() != text.length())
		{
			char[] result = m_mask.toCharArray();
			for (int i = 0; i < result.length; i++)
			{
				if (result[i] == SPACE && text.length() > i)
					result[i] = text.charAt(i);
				else
					if (result[i] == SPACE_IND)
						result[i] = SPACE;
					else
			/*	 	
			 * 		BISion - 30-SEP-2008 - Daniel Gini
			 *	 
			 *		Si el texto ingresado, es menor a la longitud del VFormat, se debe completar los
			 *	lugares restantes. Para el caso en que el formato no acepte nulos ('0', 'a', 'A', 'l' ó 'L')
			 *	se completa con 0's (ceros).  
			*/
						if (m_VFormat.charAt(i) == '0' || m_VFormat.charAt(i) == 'a' || m_VFormat.charAt(i) == 'A' || m_VFormat.charAt(i) == 'l' || m_VFormat.charAt(i) == 'L')
							result[i] = '0';
			}
			//
			super.remove(0, text.length());
			super.insertString(0, new String(result), attr);
			m_tc.setCaretPosition(offset);
			text = getText();
		}

		//	positioned before a mask character - jump over it
		if (offset+1 < text.length() && m_mask.charAt(offset+1) != SPACE)
			if (offset+2 < getText().length())
				m_tc.setCaretPosition(offset+2);

		//	positioned at the mask character
		if (m_mask.charAt(offset) != SPACE)
		{
			if (offset+1 == m_mask.length())
				return;
			offset++;
			m_tc.setCaretPosition(offset+1);
		}

		//	Conversion
		char c = string.charAt(0);
		char cmd = m_VFormat.charAt(offset);
		log.fine( "char=" + c + ", cmd=" + cmd);
		switch (cmd)
		{
			case 'c':		//	c	any Letter or Digits or space
				if (Character.isLetter(c) || Character.isDigit(c) || Character.isSpaceChar(c))
					;
				else
					return;
				break;
			//
			case 'C':		//	C	any Letter or Digits or space converted to upper case
				if (Character.isLetter(c) || Character.isDigit(c) || Character.isSpaceChar(c))
					string = string.toUpperCase();
				else
					return;
				break;
			//
			case 'a':		//	a	any Letter or Digits NO space
				if (Character.isLetter(c) || Character.isDigit(c))
					;
				else
					return;
				break;
			//
			case 'A':		//	A	any Letter or Digits NO space converted to upper case
				if (Character.isLetter(c) || Character.isDigit(c))
					string = string.toUpperCase();
				else
					return;
				break;
			//  ----
			case 'l':		//	l	any Letter a..Z NO space
				if (!Character.isLetter(c))
					return;
				break;
			//
			case 'L':		//	L	any Letter a..Z NO space converted to upper case
				if (!Character.isLetter(c))
					return;
				string = string.toUpperCase();
				break;
			//
			case 'o':		//	o	any Letter a..Z or space
				if (Character.isLetter(c) || Character.isSpaceChar(c))
					;
				else
					return;
				break;
			//
			case 'O':		//	O	any Letter a..Z or space converted to upper case
				if (Character.isLetter(c) || Character.isSpaceChar(c))
					string = string.toUpperCase();
				else
					return;
				break;
			//  ----
			case '9':		//	9	Digits 0..9 or space
				if (Character.isDigit(c) || Character.isSpaceChar(c))
					;
				else
					return;
				break;
			//
			case '0':		//	0	Digits 0..9 NO space
				if (!Character.isDigit(c))
					return;
				break;
			//
			case SPACE:		//	any character
				break;
			//
			default:		//	other
				return;
		}	//	switch

		super.remove(offset, 1);	//	replace current position
		super.insertString(offset, string, attr);
	}	//	insertString

	/**
	 *	Delete String
	 *  @param offset
	 *  @param length
	 *  @throws BadLocationException
	 */
	public void remove (int offset, int length)
		throws BadLocationException
	{
		//	No format or non manual entry
		if (m_VFormat.length() == 0 || length != 1)
		{
			super.remove(offset, length);
			return;
		}

	//	log.fine( "MDocString.remove - Offset=" + offset + " Length=" + length);

		//	begin of string
		if (offset == 0)
		{
			//	empty the field
			if (length == m_mask.length())
				super.remove(offset, length);
			return;
		}

		//	one position behind delimiter
		if (offset-1 >= 0 && m_mask.charAt(offset-1) != SPACE)
		{
			if (offset-2 >= 0)
				m_tc.setCaretPosition(offset-2);
			else
				return;
		}
		else
			m_tc.setCaretPosition(offset-1);
	}	//	deleteString

	/**
	 *	Get Full Text
	 *  @return text
	 */
	private String getText()
	{
		String str = "";
		try
		{
			str = getContent().getString(0, getContent().length()-1);		//	cr at end
		}
		catch (Exception e)
		{
			str = "";
		}
		return str;
	}	//	getString

	/*************************************************************************/

	/**
	 *	Caret Listener
	 *  @param e
	 */
	public void caretUpdate(CaretEvent e)
	{
		//	No Format
		if (m_VFormat.length() == 0
		//  Format error - all fixed characters
			|| m_VFormat.equals(m_mask))
			return;
		//	Selection
		if (e.getDot() != e.getMark())
		{
			m_lastDot = e.getDot();
			return;
		}
		//
	//	log.fine( "MDocString.caretUpdate -" + m_VFormat + "-" + m_mask + "- dot=" + e.getDot() + ", mark=" + e.getMark());

		//	Is the current position a fixed character?
		if (e.getDot()+1 > m_mask.length() || m_mask.charAt(e.getDot()) == SPACE)
		{
			m_lastDot = e.getDot();
			return;
		}

		//	Direction?
		int newDot = -1;
		if (m_lastDot > e.getDot())			//	<-
			newDot = e.getDot() - 1;
		else								//	-> (or same)
			newDot = e.getDot() + 1;
		//
		if (e.getDot() == 0)						//	first
			newDot = 1;
		else if (e.getDot() == m_mask.length()-1)	//	last
			newDot = e.getDot() - 1;
		//
	//	log.fine( "OnFixedChar=" + m_mask.charAt(e.getDot()) + ", newDot=" + newDot + ", last=" + m_lastDot);

		m_lastDot = e.getDot();
		if (newDot >= 0 && newDot < getText().length())
			m_tc.setCaretPosition(newDot);
	}	//	caretUpdate

}	//	MDocString
