/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.util;


/**
 *	Amount in Words for English
 *	
 *  @author Jorg Janke - http://www.rgagnon.com/javadetails/java-0426.html
 *  @version $Id: AmtInWords_EN.java,v 1.4 2005/12/31 06:33:21 jjanke Exp $
 */
public class AmtInWords_EN implements AmtInWords
{
	/**
	 * 	AmtInWords_EN
	 */
	public AmtInWords_EN ()
	{
		super ();
	}	//	AmtInWords_EN

	/** Thousands plus				*/
	private static final String[]	majorNames	= {
		"", 
		"Thousand-", 
		"Million-",
		"Billion-", 
		"Trillion-", 
		"Quadrillion-",
		"Quintillion-"
	};

	/** Ten to Ninety				*/
	private static final String[]	tensNames	= { 
		"", 
		"Ten", 
		"Twenty",
		"Thirty", 
		"Fourty", 
		"Fifty", 
		"Sixty", 
		"Seventy",
		"Eighty", 
		"Ninety"
	};

	/** numbers to 19				*/
	private static final String[]	numNames	= { 
		"", 
		"One", 
		"Two",
		"Three", 
		"Four", 
		"Five", 
		"Six", 
		"Seven", 
		"Eight", 
		"Nine",
		"Ten", 
		"Eleven", 
		"Twelve", 
		"Thirteen", 
		"Fourteen", 
		"Fifteen",
		"Sixteen", 
		"Seventeen", 
		"Eighteen", 
		"Nineteen"
	};

	/**
	 * 	Convert Less Than One Thousand
	 *	@param number
	 *	@return amt
	 */
	private String convertLessThanOneThousand (int number)
	{
		String soFar;
		//	Below 20
		if (number % 100 < 20)
		{
			soFar = numNames[number % 100];
			number /= 100;
		}
		else
		{
			soFar = numNames[number % 10];
			number /= 10;
			soFar = tensNames[number % 10] + soFar;
			number /= 10;
		}
		if (number == 0)
			return soFar;
		return numNames[number] + "Hundred-" + soFar;
	}	//	convertLessThanOneThousand

	/**
	 * 	Convert
	 *	@param number
	 *	@return amt
	 */
	private String convert (long number)
	{
		/* special case */
		if (number == 0)
		{
			return "Zero";
		}
		String prefix = "";
		if (number < 0)
		{
			number = -number;
			prefix = "Negative ";
		}
		String soFar = "";
		int place = 0;
		do
		{
			long n = number % 1000;
			if (n != 0)
			{
				String s = convertLessThanOneThousand ((int)n);
				soFar = s + majorNames[place] + soFar;
			}
			place++;
			number /= 1000;
		}
		while (number > 0);
		return (prefix + soFar).trim ();
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
		long dollars = Long.parseLong(amount.substring (0, newpos));
		sb.append (convert (dollars));
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

	/**
	 * 	Test Print
	 *	@param amt amount
	 */
	private void print (String amt)
	{
		try
		{
			System.out.println(amt + " = " + getAmtInWords(amt));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}	//	print
	
	/**
	 * 	Test
	 *	@param args ignored
	 */
	public static void main (String[] args)
	{
		AmtInWords_EN aiw = new AmtInWords_EN();
	//	aiw.print (".23");	Error
		aiw.print ("0.23");
		aiw.print ("1.23");
		aiw.print ("12.345");
		aiw.print ("123.45");
		aiw.print ("1234.56");
		aiw.print ("12345.78");
		aiw.print ("123457.89");
		aiw.print ("1,234,578.90");
	}	//	main
	
}	//	AmtInWords_EN
