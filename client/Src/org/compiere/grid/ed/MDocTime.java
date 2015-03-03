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

import java.util.logging.*;
import javax.swing.text.*;
import org.compiere.util.*;

/**
 *	Time Model.
 *		Validates input for hour or minute field
 *  @see VDate
 *
 *  @author Jorg Janke
 *  @version  $Id: MDocTime.java,v 1.5 2005/03/11 20:28:26 jjanke Exp $
 */
public final class MDocTime extends PlainDocument
{
	/**
	 *	Constructor
	 *  @param isHour Hour field
	 *  @param is12Hour 12 hour format
	 */
	public MDocTime(boolean isHour, boolean is12Hour)
	{
		super();
		m_isHour = isHour;
		m_is12Hour = is12Hour;
	}	//	MDocTime

	private boolean		m_isHour;
	private boolean		m_is12Hour;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(MDocTime.class);

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
	//	log.fine( "MDocTime.insertString - Offset=" + offset
	//		+ ", String=" + string + ", Attr=" + attr	+ ", Text=" + getText() + ", Length=" + getText().length());

		//	manual entry
		//	DBTextDataBinder.updateText sends stuff at once
		if (string != null && string.length() == 1)
		{
			//	ignore if too long
			if (offset > 2)
				return;

			//	is it a digit ?
			if (!Character.isDigit(string.charAt(0)))
			{
				log.config("No Digit=" + string);
				return;
			}

			//	resulting string
			char[] cc = getText().toCharArray();
			cc[offset] = string.charAt(0);
			String result = new String(cc);

			int i = 0;
			try
			{
				i = Integer.parseInt(result.trim());
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, e.toString());
			}
			if (i < 0)
			{
				log.config("Invalid value: " + i);
				return;
			}
			//	Minutes
			if (!m_isHour && i > 59)
			{
				log.config("Invalid minute value: " + i);
				return;
			}
			//	Hour
			if (m_isHour && m_is12Hour && i > 12)
			{
				log.config("Invalid 12 hour value: " + i);
				return;
			}
			if (m_isHour && !m_is12Hour && i > 24)
			{
				log.config("Invalid 24 hour value: " + i);
				return;
			}
			//
		//	super.remove(offset, 1);	//	replace current position
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
	//	log.fine( "MDocTime.remove - Offset=" + offset + ", Length=" + length);

		super.remove(offset, length);
	}	//	deleteString

	/**
	 *	Get Full Text (always two character)
	 *  @return text
	 */
	private String getText()
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			sb.append(getContent().getString(0, getContent().length()-1));		//	cr at end
		}
		catch (Exception e)
		{
		}
		while (sb.length() < 2)
			sb.insert(0, ' ');
		return sb.toString();
	}	//	getString

}	//	MDocTime
