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
package org.compiere.util;

import java.awt.*;
import java.awt.font.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;

/**
 *  General Utilities
 *
 *  @author     Jorg Janke
 *  @version    $Id: Util.java,v 1.22 2005/12/20 04:21:54 jjanke Exp $
 */
public class Util
{
	/**	Logger			*/
	private static Logger log = Logger.getLogger(Util.class.getName());

	/**
	 *	Replace String values.
	 *  @param value string to be processed
	 *  @param oldPart old part
	 *  @param newPart replacement - can be null or ""
	 *  @return String with replaced values
	 */
	public static String replace (String value, String oldPart, String newPart)
	{
		if (value == null || value.length() == 0
			|| oldPart == null || oldPart.length() == 0)
			return value;
		//
		int oldPartLength = oldPart.length();
		String oldValue = value;
		StringBuffer retValue = new StringBuffer();
		int pos = oldValue.indexOf(oldPart);
		while (pos != -1)
		{
			retValue.append (oldValue.substring(0, pos));
			if (newPart != null && newPart.length() > 0)
				retValue.append(newPart);
			oldValue = oldValue.substring(pos+oldPartLength);
			pos = oldValue.indexOf(oldPart);
		}
		retValue.append(oldValue);
	//	log.fine( "Env.replace - " + value + " - Old=" + oldPart + ", New=" + newPart + ", Result=" + retValue.toString());
		return retValue.toString();
	}	//	replace

	/**
	 * 	Remove CR / LF from String
	 * 	@param in input
	 * 	@return cleaned string
	 */
	public static String removeCRLF (String in)
	{
		char[] inArray = in.toCharArray();
		StringBuffer out = new StringBuffer (inArray.length);
		for (int i = 0; i < inArray.length; i++)
		{
			char c = inArray[i];
			if (c == '\n' || c == '\r')
				;
			else
				out.append(c);
		}
		return out.toString();
	}	//	removeCRLF


	/**
	 * 	Mask HTML content.
	 *  i.e. replace characters with &values;
	 * 	@param content content
	 * 	@return masked content
	 */
	public static String maskHTML (String content)
	{
		if (content == null || content.length() == 0 || content.equals(" "))
			return "&nbsp";
		//
		StringBuffer out = new StringBuffer();
		char[] chars = content.toCharArray();
		for (int i = 0; i < chars.length; i++)
		{
			char c = chars[i];
			switch (c)
			{
				case '<':	out.append("&lt;");			break;
				case '>':	out.append("&gt;");			break;
				case '&':	out.append("&amp;");		break;
				case '"':	out.append("&quot;");		break;
				case '\'':	out.append("&#039;");		break;
				//
				default:
					int ii =  (int)c;
					if (ii > 255)		//	Write Unicode
						out.append("&#").append(ii).append(";");
					else
						out.append(c);
					break;
			}
		}
		return out.toString();
	}	//	maskHTML

	/**
	 * 	Get the number of occurances of countChar in string.
	 * 	@param string String to be searched
	 * 	@param countChar to be counted character
	 * 	@return number of occurances
	 */
	public static int getCount (String string, char countChar)
	{
		if (string == null || string.length() == 0)
			return 0;
		int counter = 0;
		char[] array = string.toCharArray();
		for (int i = 0; i < array.length; i++)
		{
			if (array[i] == countChar)
				counter++;
		}
		return counter;
	}	//	getCount

	/**
	 * 	Is String Empty
	 *	@param str string
	 *	@return true if >= 1 char
	 */
	public static boolean isEmpty (String str)
	{
		return (str == null || str.length() == 0);
	}	//	isEmpty
	
	/**************************************************************************
	 *  Find index of search character in str.
	 *  This ignores content in () and 'texts'
	 *  @param str string
	 *  @param search search character
	 *  @return index or -1 if not found
	 */
	public static int findIndexOf (String str, char search)
	{
		return findIndexOf(str, search, search);
	}   //  findIndexOf

	/**
	 *  Find index of search characters in str.
	 *  This ignores content in () and 'texts'
	 *  @param str string
	 *  @param search1 first search character
	 *  @param search2 second search character (or)
	 *  @return index or -1 if not found
	 */
	public static int findIndexOf (String str, char search1, char search2)
	{
		if (str == null)
			return -1;
		//
		int endIndex = -1;
		int parCount = 0;
		boolean ignoringText = false;
		int size = str.length();
		while (++endIndex < size)
		{
			char c = str.charAt(endIndex);
			if (c == '\'')
				ignoringText = !ignoringText;
			else if (!ignoringText)
			{
				if (parCount == 0 && (c == search1 || c == search2))
					return endIndex;
				else if (c == ')')
						parCount--;
				else if (c == '(')
					parCount++;
			}
		}
		return -1;
	}   //  findIndexOf

	/**
	 *  Find index of search character in str.
	 *  This ignores content in () and 'texts'
	 *  @param str string
	 *  @param search search character
	 *  @return index or -1 if not found
	 */
	public static int findIndexOf (String str, String search)
	{
		if (str == null || search == null || search.length() == 0)
			return -1;
		//
		int endIndex = -1;
		int parCount = 0;
		boolean ignoringText = false;
		int size = str.length();
		while (++endIndex < size)
		{
			char c = str.charAt(endIndex);
			if (c == '\'')
				ignoringText = !ignoringText;
			else if (!ignoringText)
			{
				if (parCount == 0 && c == search.charAt(0))
				{
					if (str.substring(endIndex).startsWith(search))
						return endIndex;
				}
				else if (c == ')')
						parCount--;
				else if (c == '(')
					parCount++;
			}
		}
		return -1;
	}   //  findIndexOf

	
	/**************************************************************************
	 *  Return Hex String representation of byte b
	 *  @param b byte
	 *  @return Hex
	 */
	static public String toHex (byte b)
	{
		char hexDigit[] = {
			'0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
		};
		char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
		return new String(array);
	}

	/**
	 *  Return Hex String representation of char c
	 *  @param c character
	 *  @return Hex
	 */
	static public String toHex (char c)
	{
		byte hi = (byte) (c >>> 8);
		byte lo = (byte) (c & 0xff);
		return toHex(hi) + toHex(lo);
	}   //  toHex

	
	/**************************************************************************
	 * 	Init Cap Words With Spaces
	 * 	@param in string
	 * 	@return init cap
	 */
	public static String initCap (String in)
	{
		if (in == null || in.length() == 0)
			return in;
		//
		boolean capitalize = true;
		char[] data = in.toCharArray();
		for (int i = 0; i < data.length; i++)
		{
			if (data[i] == ' ' || Character.isWhitespace(data[i]))
				capitalize = true;
			else if (capitalize)
			{
				data[i] = Character.toUpperCase (data[i]);
				capitalize = false;
			}
			else
				data[i] = Character.toLowerCase (data[i]);
		}
		return new String (data);
	}	//	initCap

	
	/**************************************************************************
	 * 	Return a Iterator with only the relevant attributes.
	 *  Fixes implementation in AttributedString, which returns everything
	 * 	@param aString attributed string
	 * 	@param relevantAttributes relevant attributes
	 * 	@return iterator
	 */
	static public AttributedCharacterIterator getIterator (AttributedString aString, 
		AttributedCharacterIterator.Attribute[] relevantAttributes)
	{
		AttributedCharacterIterator iter = aString.getIterator();
		Set set = iter.getAllAttributeKeys();
	//	System.out.println("AllAttributeKeys=" + set);
		if (set.size() == 0)
			return iter;
		//	Check, if there are unwanted attributes
		Set<AttributedCharacterIterator.Attribute> unwanted = new HashSet<AttributedCharacterIterator.Attribute>(iter.getAllAttributeKeys());
		for (int i = 0; i < relevantAttributes.length; i++)
			unwanted.remove(relevantAttributes[i]);
		if (unwanted.size() == 0)
			return iter;

		//	Create new String
		StringBuffer sb = new StringBuffer();
		for (char c = iter.first(); c != AttributedCharacterIterator.DONE; c = iter.next())
			sb.append(c);
		aString = new AttributedString(sb.toString());

		//	copy relevant attributes
		Iterator it = iter.getAllAttributeKeys().iterator();
		while (it.hasNext())
		{
			AttributedCharacterIterator.Attribute att = (AttributedCharacterIterator.Attribute)it.next();
			if (!unwanted.contains(att))
			{
				for (char c = iter.first(); c != AttributedCharacterIterator.DONE; c = iter.next())
				{
					Object value = iter.getAttribute(att);
					if (value != null)
					{
						int start = iter.getRunStart(att);
						int limit = iter.getRunLimit(att);
					//	System.out.println("Attribute=" + att + " Value=" + value + " Start=" + start + " Limit=" + limit);
						aString.addAttribute(att, value, start, limit);
						iter.setIndex(limit);
					}
				}
			}
		//	else
		//		System.out.println("Unwanted: " + att);
		}
		return aString.getIterator();
	}	//	getIterator


	/**
	 * 	Dump a Map (key=value) to out
	 * 	@param map Map
	 */
	static public void dump (Map map)
	{
		System.out.println("Dump Map - size=" + map.size());
		Iterator it = map.keySet().iterator();
		while (it.hasNext())
		{
			Object key = it.next();
			Object value = map.get(key);
			System.out.println(key + "=" + value);
		}
	}	//	dump (Map)

	/**
	 *  Print Action and Input Map for component
	 * 	@param comp  Component with ActionMap
	 */
	public static void printActionInputMap (JComponent comp)
	{
		//	Action Map
		ActionMap am = comp.getActionMap();
		Object[] amKeys = am.allKeys(); //  including Parents
		if (amKeys != null)
		{
			System.out.println("-------------------------");
			System.out.println("ActionMap for Component " + comp.toString());
			for (int i = 0; i < amKeys.length; i++)
			{
				Action a = am.get(amKeys[i]);

				StringBuffer sb = new StringBuffer("- ");
				sb.append(a.getValue(Action.NAME));
				if (a.getValue(Action.ACTION_COMMAND_KEY) != null)
					sb.append(", Cmd=").append(a.getValue(Action.ACTION_COMMAND_KEY));
				if (a.getValue(Action.SHORT_DESCRIPTION) != null)
					sb.append(" - ").append(a.getValue(Action.SHORT_DESCRIPTION));
				System.out.println(sb.toString() + " - " + a);
			}
		}
		/**	Same as below
		KeyStroke[] kStrokes = comp.getRegisteredKeyStrokes();
		if (kStrokes != null)
		{
		System.out.println("-------------------------");
			System.out.println("Registered Key Strokes - " + comp.toString());
			for (int i = 0; i < kStrokes.length; i++)
			{
				System.out.println("- " + kStrokes[i].toString());
			}
		}
		/** Focused				*/
		InputMap im = comp.getInputMap(JComponent.WHEN_FOCUSED);
		KeyStroke[] kStrokes = im.allKeys();
		if (kStrokes != null)
		{
			System.out.println("-------------------------");
			System.out.println("InputMap for Component When Focused - " + comp.toString());
			for (int i = 0; i < kStrokes.length; i++)
			{
				System.out.println("- " + kStrokes[i].toString() + " - "
					+ im.get(kStrokes[i]).toString());
			}
		}
		/** Focused in Window	*/
		im = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		kStrokes = im.allKeys();
		if (kStrokes != null)
		{
			System.out.println("-------------------------");
			System.out.println("InputMap for Component When Focused in Window - " + comp.toString());
			for (int i = 0; i < kStrokes.length; i++)
			{
				System.out.println("- " + kStrokes[i].toString() + " - "
					+ im.get(kStrokes[i]).toString());
			}
		}
		/** Focused when Ancester	*/
		im = comp.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		kStrokes = im.allKeys();
		if (kStrokes != null)
		{
			System.out.println("-------------------------");
			System.out.println("InputMap for Component When Ancestor - " + comp.toString());
			for (int i = 0; i < kStrokes.length; i++)
			{
				System.out.println("- " + kStrokes[i].toString() + " - "
					+ im.get(kStrokes[i]).toString());
			}
		}
		System.out.println("-------------------------");
	}   //  printActionInputMap

	/**
	 * 	Is 8 Bit
	 *	@param str string
	 *	@return true if string contains chars > 255
	 */
	public static boolean is8Bit (String str)
	{
		if (str == null || str.length() == 0)
			return true;
		char[] cc = str.toCharArray();
		for (int i = 0; i < cc.length; i++)
		{
			if (cc[i] > 255)
			{
			//	System.out.println("Not 8 Bit - " + str);
				return false;
			}
		}
		return true;
	}	//	is8Bit
	
	/**
	 * 	Clean Ampersand (used to indicate shortcut) 
	 *	@param in input
	 *	@return cleaned string
	 */
	public static String cleanAmp (String in)
	{
		if (in == null)
			return in;
		int pos = in.indexOf('&');
		if (pos == -1)
			return in;
		//
		if (pos+1 < in.length() && in.charAt(pos+1) != ' ')
			in = in.substring(0, pos) + in.substring(pos+1);
		return in;
	}	//	cleanAmp
	
	
	/**************************************************************************
	 * 	Test
	 * 	@param args args
	 */
	public static void main (String[] args)
	{
		AttributedString aString = new AttributedString ("test test");
		aString.addAttribute(TextAttribute.FOREGROUND, Color.blue);
		aString.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, 2, 4);
		getIterator (aString, new AttributedCharacterIterator.Attribute[] {TextAttribute.UNDERLINE});
	}	//	main

}   //  Util
