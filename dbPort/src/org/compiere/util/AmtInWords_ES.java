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
 *	Spanish Amount in Words
 *	
 *  @author Jorg Janke - http://www.rgagnon.com/javadetails/java-0426.html
 *  @version $Id: AmtInWords_ES.java,v 1.3 2005/09/16 00:49:04 jjanke Exp $
 */
public class AmtInWords_ES implements AmtInWords
{
	/**
	 * 	AmtInWords_ES
	 */
	public AmtInWords_ES ()
	{
		super ();
	} //	AmtInWords_ES

	private static final String[]	majorNames	= {
		"", 
		" MIL", 
		" MILLON",
		" BILLON", 
		" TRILLON", 
		" CUATRILLON", 
		" QUINTRILLON"
		};

	private static final String[]	tensNames	= { 
		"", 
		" DIEZ", 
		" VEINTE",
		" TREINTA", 
		" CUARENTA", 
		" CINCUENTA", 
		" SESENTA", 
		" SETENTA",
		" OCHENTA", 
		" NOVENTA"
		};

	private static final String[]	numNames	= { 
		"", 
		" UNO", 
		" DOS",
		" TRES", 
		" CUATRO", 
		" CINCO", 
		" SEIS", 
		" SIETE", 
		" OCHO", 
		" NUEVE",
		" DIEZ", 
		" ONCE", 
		" DOCE", 
		" TRECE", 
		" CATORCE", 
		" QUINCE",
		" DIECISEIS", 
		" DIECISIETE", 
		" DIECIOCHO", 
		" DIECINUEVE"
		};

	/**
	 * 	Convert Less Than One Thousand
	 *	@param number
	 *	@return amt
	 */
	private String convertLessThanOneThousand (int number)
	{
		String soFar;
		// Esta dentro de los 1os. diecinueve?? ISCAP
		if (number % 100 < 20)
		{
			soFar = numNames[number % 100];
			number /= 100;
		}
		else
		{
			soFar = numNames[number % 10];
			number /= 10;
			String s = Integer.toString (number);
			if (s.endsWith ("2") && soFar != "")
				soFar = " VEINTI" + soFar.trim ();
			else if (soFar == "")
				soFar = tensNames[number % 10] + soFar;
			else
				soFar = tensNames[number % 10] + " Y" + soFar;
			number /= 10;
		}
		if (number == 0)
			return soFar;
		if (number > 1)
			soFar = "S" + soFar;
		if (number == 1 && soFar != "")
			number = 0;
		return numNames[number] + " CIENTO" + soFar;
	}	//	convertLessThanOneThousand

	/**
	 * 	Convert
	 *	@param number
	 *	@return amt
	 */
	private String convert (int number)
	{
		/* special case */
		if (number == 0)
			return "CERO";
		String prefix = "";
		if (number < 0)
		{
			number = -number;
			prefix = "MENOS";
		}
		String soFar = "";
		int place = 0;
		do
		{
			int n = number % 1000;
			if (n != 0)
			{
				String s = convertLessThanOneThousand (n);
				if (s.startsWith ("CINCO CIENTOS", 1))
				{
					s = s.replaceFirst ("CINCO CIENTOS", "QUINIENTOS");
				}
				if (s.startsWith ("SIETE CIENTOS", 1))
				{
					s = s.replaceFirst ("SIETE CIENTOS", "SETECIENTOS");
				}
				if (s.startsWith ("NUEVE CIENTOS", 1))
				{
					s = s.replaceFirst ("NUEVE CIENTOS", "NOVECIENTOS");
				}
				if (s == " UNO")
				{
					soFar = majorNames[place] + soFar;
				}
				else
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
		int pesos = Integer.parseInt (amount.substring (0, newpos));
		//begin vpj-cd e-evolution 17 Feb 2006
		//sb.append (convert (pesos));
		sb.append (convert (pesos)).append(" PESOS ");
		//end vpj-cd e-evolution 17 Feb 2006
		for (int i = 0; i < oldamt.length (); i++)
		{
			if (pos == i) //	we are done
			{
				String cents = oldamt.substring (i + 1);
				sb.append (' ')
					.append (cents)
				//begin vpj-cd e-evolution 17 Feb 2006	
				//	.append ("/100");
					.append ("/100 M.N.");
				//end vpj-cd e-evolution 17 Feb 2006
				break;
			}
		}
		return sb.toString ();
	}	//	getAmtInWords
	
}	//	AmtInWords_ES
