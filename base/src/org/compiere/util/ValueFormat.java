package org.compiere.util;

import java.sql.Timestamp;
import java.util.Date;

	/*	 	
	 * 	INICIO - BISion - 30-SEP-2008 - Daniel Gini
	 */

public class ValueFormat {

	
	private static boolean isLetter(char c, boolean upcase){
		if (upcase)
			return (c >= 'A' && c <= 'Z');
		return (c >= 'a' && c <= 'z');
	}

	private static boolean isDigit(char c){
		return (c >= '0' && c <= '9');
	}

	private static boolean isSpace(char c){
		return (c==' ');
	}

	private static boolean validate(char vf, char st)
	{
		switch (vf) {
		
		case 'c': {	if (isLetter(st,false) || isDigit(st) || isSpace(st)) return true;}
		case 'C': {	if (isLetter(st,true) || isDigit(st) || isSpace(st)) return true;}
		case 'a': {	if (isLetter(st,false) || isDigit(st)) return true;}
		case 'A': {	if (isLetter(st,true) || isDigit(st)) return true;}
		case 'l': {	if (isLetter(st,false)) return true;}
		case 'L': {	if (isLetter(st,true)) return true;}
		case 'o': {	if (isLetter(st,false) || isSpace(st)) return true;}
		case 'O': {	if (isLetter(st,true) || isSpace(st)) return true;}
		case '0': {	if (isDigit(st)) return true;}
		case '9': {	if (isDigit(st) || isSpace(st)) return true;}
		
		default: {	if (vf==st) return true;}

		}
		
		return false;
	}	//validate


	public static boolean validFormat (String string, String VFormat)
	{

		if (string.length() != VFormat.length())
			return false;
		
		char[] value = string.toCharArray();
		char[] format = VFormat.toCharArray();

		for (int i = 0; i < format.length; i++)		{
			if (!validate(format[i],value[i]))
				return false;
		}
		
		return true;
	}	//validFormat
	
	/**
	 *  Devuelve un String con la fecha en un formato listo para consultar con jdcb
	 *  	-- ddmmyyyy --
	 *  
	 *  @param Timestamp
	 *  @return String
	 */
	public static String getFechaFormateada(Timestamp ts)
	{
		String Date = "";
		if ((Integer.toString(ts.getDate()).length()==1) && (Integer.toString(ts.getMonth()+1).length()==1)) 
		{
			Date = '0' + Integer.toString(ts.getDate()) + '0' + Integer.toString(ts.getMonth()+1) + Integer.toString(ts.getYear()+1900);	
		}
		else
			if ((Integer.toString(ts.getDate()).length()==1) && (Integer.toString(ts.getMonth()+1).length()>1)) 
			{
				Date = '0' + Integer.toString(ts.getDate()) + Integer.toString(ts.getMonth()+1) + Integer.toString(ts.getYear()+1900);	
			}
			else
				if ((Integer.toString(ts.getDate()).length()>1) && (Integer.toString(ts.getMonth()+1).length()==1)) 
				{
					Date = Integer.toString(ts.getDate()) + '0' + Integer.toString(ts.getMonth()+1) + Integer.toString(ts.getYear()+1900);	
				}
				else
				{
					Date = Integer.toString(ts.getDate()) + Integer.toString(ts.getMonth()+1) + Integer.toString(ts.getYear()+1900);	
				}
		
		return Date;
	}
	
	public static String getFechaARG(Date date)
	{
		String Date = "";
		if ((Integer.toString(date.getDate()).length()==1) && (Integer.toString(date.getMonth()+1).length()==1)) 
		{
			Date = "0" + Integer.toString(date.getDate()) + "/0" + Integer.toString(date.getMonth()+1) + "/" + Integer.toString(date.getYear()+1900);	
		}
		else
			if ((Integer.toString(date.getDate()).length()==1) && (Integer.toString(date.getMonth()+1).length()>1)) 
			{
				Date =  "0" + Integer.toString(date.getDate()) + "/" + Integer.toString(date.getMonth()+1) + "/" + Integer.toString(date.getYear()+1900);	
			}
			else
				if ((Integer.toString(date.getDate()).length()>1) && (Integer.toString(date.getMonth()+1).length()==1)) 
				{
					Date = Integer.toString(date.getDate()) + "/0" + Integer.toString(date.getMonth()+1) + "/" + Integer.toString(date.getYear()+1900);	
				}
				else
				{
					Date = Integer.toString(date.getDate()) + "/" + Integer.toString(date.getMonth()+1) + "/" + Integer.toString(date.getYear()+1900);	
				}
		
		return Date;
	}
	
}
	
	/*	 	
	 * 	FIN - BISion - 30-SEP-2008 - Daniel Gini
	 */