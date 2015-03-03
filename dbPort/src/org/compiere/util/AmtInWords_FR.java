/*******************************************************************************
 * The contents of this file are subject to the Compiere License Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License. The
 * Original Code is Compiere ERP & CRM Business Solution The Initial Developer
 * of the Original Code is Jorg Janke and ComPiere, Inc. Portions created by
 * Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts created by ComPiere
 * are Copyright (C) ComPiere, Inc.; All Rights Reserved. Contributor(s):
 * ______________________________________.
 ******************************************************************************/
package org.compiere.util;

/**
 * Amount in Words for French
 * 
 * @author Jorg Janke - http://www.rgagnon.com/javadetails/java-0426.html
 * @version $Id: AmtInWords_FR.java,v 1.1 2005/04/19 04:43:31 jjanke Exp $
 */
public class AmtInWords_FR implements AmtInWords
{

	/**
	 * AmtInWords_FR
	 */
	public AmtInWords_FR ()
	{
		super ();
	}	// AmtInWords_FR

	private static final String[]	majorNames	= {
		"", 
		" mille", 
		" million",
		" milliard", 
		" trillion", 
		" quadrillion", 
		" quintillion"
		};

	private static final String[]	tensNames	= { 
		"", 
		" dix", 
		" vingt",
		" trente", 
		" quarante", 
		" cinquante", 
		" soixante", 
		" soixante-dix",
		" quatre-vingt", 
		" quatre-vingt-dix"	
		};

	private static final String[]	numNames	= { 
		"", 
		" un", 
		" deux",
		" trois", 
		" quatre", 
		" cinq", 
		" six", 
		" sept", 
		" huit", 
		" neuf",
		" dix", 
		" onze", 
		" douze", 
		" treize", 
		" quatorze", 
		" quinze", 
		" seize",
		" dix-sept", 
		" dix-huit", 
		" dix-neuf"	
		};

	/**
	 * Convert Less Than One Thousand
	 * @param number number
	 * @return string
	 */
	private String convertLessThanOneThousand (int number)
	{
		String soFar;
		if (number % 100 < 20)
		{
			// 19 et moins
			soFar = numNames[number % 100];
			number /= 100;
		}
		else
		{
			// 9 et moins
			soFar = numNames[number % 10];
			number /= 10;
			// 90, 80, ... 20
			soFar = tensNames[number % 10] + soFar;
			number /= 10;
		}
		// reste les centaines
		// y'en a pas
		if (number == 0)
			return soFar;
		if (number == 1)
			// on ne retourne "un cent xxxx" mais "cent xxxx"
			return " cent" + soFar;
		else
			return numNames[number] + " cent" + soFar;
	}	//	convertLessThanOneThousand

	/**
	 * Convert
	 * @param number number
	 * @return string
	 */
	private String convert (int number)
	{
		if (number == 0)
			return "zero";
		String prefix = "";
		if (number < 0)
		{
			number = -number;
			prefix = "moins";
		}
		String soFar = "";
		int place = 0;
		boolean pluralPossible = true;
		boolean pluralForm = false;
		do
		{
			int n = number % 1000;
			// par tranche de 1000
			if (n != 0)
			{
				String s = convertLessThanOneThousand (n);
				if (s.trim ().equals ("un") && place == 1)
				{
					// on donne pas le un pour mille
					soFar = majorNames[place] + soFar;
				}
				else
				{
					if (place == 0)
					{
						if (s.trim ().endsWith ("cent")
							&& !s.trim ().startsWith ("cent"))
						{
							// nnn200 ... nnn900 avec "s"
							pluralForm = true;
						}
						else
						{
							// pas de "s" jamais
							pluralPossible = false;
						}
					}
					if (place > 0 && pluralPossible)
					{
						if (!s.trim ().startsWith ("un"))
						{
							// avec "s"
							pluralForm = true;
						}
						else
						{
							// jamis de "s"
							pluralPossible = false;
						}
					}
					soFar = s + majorNames[place] + soFar;
				}
			}
			place++;
			number /= 1000;
		}
		while (number > 0);
		String result = (prefix + soFar).trim ();
		return (pluralForm ? result + "s" : result);
	}	//	convert
	
	/**************************************************************************
	 * 	Get Amount in Words
	 * 	@param amount numeric amount (352.80)
	 * 	@return amount in words (three*five*two 80/100)
	 */
	public String getAmtInWords (String amount) throws Exception
	{
		if (amount == null)
			return amount;
		//
		StringBuffer sb = new StringBuffer ();
		int pos = amount.lastIndexOf ('.');
		int pos2 = amount.lastIndexOf (',');
		if (pos2 > pos)
			pos = pos2;
		String oldamt = amount;
		amount = amount.replaceAll (",", "");
		int newpos = amount.lastIndexOf ('.');
		int pesos = Integer.parseInt (amount.substring (0, newpos));
		sb.append (convert (pesos));
		for (int i = 0; i < oldamt.length (); i++)
		{
			if (pos == i) //	we are done
			{
				String cents = oldamt.substring (i + 1);
				sb.append (' ').append (cents).append ("/100");
				break;
			}
		}
		return sb.toString ();
	}	//	getAmtInWords

}	// AmtInWords_FR
