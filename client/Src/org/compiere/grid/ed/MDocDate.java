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

import java.sql.*;
import java.text.*;
import java.util.Date;
import javax.swing.event.*;
import javax.swing.text.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Date Model.
 *		Validates input based on date pattern
 *  @see VDate
 *
 *  @author Jorg Janke
 *  @version  $Id: MDocDate.java,v 1.11 2005/03/11 20:28:25 jjanke Exp $
 */
public final class MDocDate extends PlainDocument implements CaretListener
{
	/**
	 *	Constructor
	 *  @param displayType display type
	 *  @param format format
	 *  @param tc text component
	 *  @param title title
	 */
	public MDocDate (int displayType, SimpleDateFormat format,
		JTextComponent tc, String title)
	{
		super();
		m_displayType = displayType;
		m_tc = tc;
		m_tc.addCaretListener(this);
		//
		m_format = format;
		if (m_format == null)
			m_format = new SimpleDateFormat();
		m_format.setLenient(false);

		//	Mark delimiters as '^' in Pattern
		char[] pattern = m_format.toPattern().toCharArray();
		for (int i = 0; i < pattern.length; i++)
		{
			//	do we have a delimiter?
			if ("Mdy".indexOf(pattern[i]) == -1)
				pattern[i] = DELIMITER;
		}
		m_mask = new String(pattern);
		//
		m_title = title;
		if (m_title == null)
			m_title = "";
	}	//	MDocDate

	private JTextComponent 		m_tc;
	private SimpleDateFormat	m_format;
	private String				m_mask;
	private static final char	DELIMITER = '^';
	//	for Calendar
	private String				m_title;
	private int					m_displayType;
	private int					m_lastDot = 0;		//	last dot position
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(MDocDate.class);
	
	/**
	 *	Insert String
	 *  @param offset offset
	 *  @param string string
	 *  @param attr attributes
	 *  @throws BadLocationException
	 */
	public void insertString (int offset, String string, AttributeSet attr)
		throws BadLocationException
	{
	//	log.fine( "MDocDate.insertString - Offset=" + offset
	//		+ " String=" + string + " Attr=" + attr
	//		+ " Text=" + getText() + " Length=" + getText().length());

		//	manual entry
		//	DBTextDataBinder.updateText sends stuff at once - length=8
		if (string != null && string.length() == 1)
		{
			//	ignore if too long
			if (offset >= m_mask.length())
				return;

			//	is it an empty field?
			int length = getText().length();
			if (offset == 0 && length == 0)
			{
				Date today = new Date(System.currentTimeMillis());
				String dateStr = m_format.format(today);
				super.insertString(0, string + dateStr.substring(1), attr);
				m_tc.setCaretPosition(1);
				return;
			}

			//	is it a digit ?
			try
			{
				Integer.parseInt(string);
			}
			catch (Exception pe)
			{
				startDateDialog();
				return;
			}

			//	try to get date in field, if invalid, get today's
			/*try
			{
				char[] cc = getText().toCharArray();
				cc[offset] = string.charAt(0);
				m_format.parse(new String(cc));
			}
			catch (ParseException pe)
			{
				startDateDialog();
				return;
			}*/

			//	positioned before the delimiter - jump over delimiter
			if (offset != m_mask.length()-1 && m_mask.charAt(offset+1) == DELIMITER)
				m_tc.setCaretPosition(offset+2);

			//	positioned at the delimiter
			if (m_mask.charAt(offset) == DELIMITER)
			{
				offset++;
				m_tc.setCaretPosition(offset+1);
			}
			super.remove(offset, 1);	//	replace current position
		}

		//	Set new character
		super.insertString(offset, string, attr);
	}	//	insertString

	/**
	 *	Delete String
	 *  @param offset offset
	 *  @param length length
	 *  @throws BadLocationException
	 */
	public void remove (int offset, int length)
		throws BadLocationException
	{
	//	log.fine( "MDocDate.remove - Offset=" + offset
	//		+ " Length=" + length);

		//	begin of string
		if (offset == 0 || length == 0)
		{
			//	empty the field
			if (length == m_mask.length() || length == 0)
				super.remove(offset, length);
			return;
		}

		//	one position behind delimiter
		if (offset-1 >= 0 && offset-1 < m_mask.length()
			&& m_mask.charAt(offset-1) == DELIMITER)
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
	 *	Caret Listener
	 *  @param e event
	 */
	public void caretUpdate(CaretEvent e)
	{
		//	Selection
		if (e.getDot() != e.getMark())
		{
			m_lastDot = e.getDot();
			return;
		}
		//
	//	log.fine( "MDocDate.caretUpdate - Dot=" + e.getDot()
	//		+ ", Mark=" + e.getMark());

		//	Is the current position a fixed character?
		if (e.getDot()+1 > m_mask.length() || m_mask.charAt(e.getDot()) != DELIMITER)
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
		if (e.getDot() == 0)						//	first
			newDot = 1;
		else if (e.getDot() == m_mask.length()-1)	//	last
			newDot = e.getDot() - 1;
		//
	//	log.fine( "OnFixedChar=" + m_mask.charAt(e.getDot())
	//		+ ", newDot=" + newDot + ", last=" + m_lastDot);
		//
		m_lastDot = e.getDot();
		if (newDot >= 0 && newDot < getText().length())
			m_tc.setCaretPosition(newDot);
	}	//	caretUpdate

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

	/**
	 *	Call Calendar Dialog
	 */
	private void startDateDialog()
	{
		log.config( "MDocDate.startDateDialog");

		//	Date Dialog
		String result = getText();
		Timestamp ts = null;
		try
		{
			ts = new Timestamp(m_format.parse(result).getTime());
		}
		catch (Exception pe)
		{
			ts = new Timestamp(System.currentTimeMillis());
		}
		ts = VDate.startCalendar(m_tc, ts, m_format, m_displayType, m_title);
		result = m_format.format(ts);

		//	move to field
		try
		{
			super.remove(0, getText().length());
			super.insertString(0, result, null);
		}
		catch (BadLocationException ble)
		{
			log.log(Level.SEVERE, "MDocDate.startDateDialog", ble);
		}
	}	//	startDateDialog

}	//	MDocDate
