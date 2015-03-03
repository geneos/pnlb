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
package org.compiere.model;


/**
 *	Obscure Strings (e.g. Credit Card Numbers).
 *	Obscure Type defined in AD_Field
 *	
 *  @author Jorg Janke
 *  @version $Id: Obscure.java,v 1.4 2005/03/11 20:28:38 jjanke Exp $
 */
public class Obscure extends Object
{
	/**
	 *	Obscure clear value.
	 * 	Obscure Digits but last 4
	 *	@param clearValue clear value
	 *	@return obscured value
	 */	
	public static String obscure (String clearValue)
	{
		Obscure ob = new Obscure (clearValue);
		return ob.getObscuredValue();
	}	//	obscure

	/**
	 *	Obscure clear value
	 *	@param clearValue clear value
	 *	@param obscureType Obscure Type
	 *	@return obscured value
	 */	
	public static String obscure (String clearValue, String obscureType)
	{
		Obscure ob = new Obscure (clearValue, obscureType);
		return ob.getObscuredValue();
	}	//	obscure


	/**************************************************************************
	 * 	Obscure
	 */
	public Obscure ()
	{
	}	//	Obscure

	/**
	 * 	Obscure.
	 * 	Obscure Digits but last 4
	 *	@param clearValue clear value
	 */
	public Obscure (String clearValue)
	{
		setClearValue(clearValue);
	}	//	Obscure

	/**
	 * 	Obscure
	 *	@param clearValue clear value
	 *	@param obscureType Obscure Type
	 */
	public Obscure (String clearValue, String obscureType)
	{
		setClearValue(clearValue);
		setType(obscureType);
	}	//	Obscure
	
	/** Obscure Digits but last 4 = 904 (default) */
	public static final String OBSCURETYPE_ObscureDigitsButLast4 = "904";
	/** Obscure Digits but first/last 4 = 944 */
	public static final String OBSCURETYPE_ObscureDigitsButFirstLast4 = "944";
	/** Obscure AlphaNumeric but first/last 4 = A44 */
	public static final String OBSCURETYPE_ObscureAlphaNumericButFirstLast4 = "A44";
	/** Obscure AlphaNumeric but last 4 = A04 */
	public static final String OBSCURETYPE_ObscureAlphaNumericButLast4 = "A04";

	/** Obscure Type			*/
	private String 	m_type = OBSCURETYPE_ObscureDigitsButLast4;
	/**	Clear Value				*/
	private String 	m_clearValue;
	/** Obscrure Value			*/
	private String 	m_obscuredValue;

	/**
	 *	Set Type
	 *	@param obscureType Obscure Type
	 */
	public void setType (String obscureType)
	{
		if (obscureType == null || obscureType.equals("904") || obscureType.equals("944") || obscureType.equals("A44") || obscureType.equals("A04"))
		{
			m_type = obscureType;
			m_obscuredValue = null;
			return;
		}
		throw new IllegalArgumentException ("ObscureType Invalid value - Reference_ID=291 - 904 - 944 - A44 - A04");
	}	//	setType

	/**
	 * 	Get Obscure Type
	 *	@return type
	 */
	public String getType ()
	{
		return m_type;
	}	//	getType

	/**
	 *	Get Clear Value
	 * 	@return Returns the clear Value.
	 */
	public String getClearValue ()
	{
		return m_clearValue;
	}	//	getClearValue

	/**
	 *	Set Clear Value
	 *	@param clearValue The clearValue to set.
	 */
	public void setClearValue (String clearValue)
	{
		m_clearValue = clearValue;
		m_obscuredValue = null;
	}	//	setClearValue

	/**
	 *	Get Obscured Value
	 *	@param clearValue The clearValue to set.
	 *	@return Returns the obscuredValue.
	 */
	public String getObscuredValue (String clearValue)
	{
		setClearValue(clearValue);
		return getObscuredValue();
	}	//	getObscuredValue

	/**
	 *	Get Obscured Value
	 *	@return Returns the obscuredValue.
	 */
	public String getObscuredValue ()
	{
		if (m_obscuredValue != null)
			return m_obscuredValue;
		if (m_clearValue == null || m_clearValue.length() == 0)
			return m_clearValue;	
		//
		boolean alpha = m_type.charAt(0) == 'A';
		int clearStart = Integer.parseInt(m_type.substring(1,2));
		int clearEnd = Integer.parseInt(m_type.substring(2));
		//
		char[] chars = m_clearValue.toCharArray();
		int length = chars.length;
		StringBuffer sb = new StringBuffer(length);
		for (int i = 0; i < length; i++)
		{
			char c = chars[i];
			if (i < clearStart)
				sb.append(c);
			else if (i >= length-clearEnd)
				sb.append(c);
			else
			{
				if (!alpha && !Character.isDigit(c))
					sb.append(c);
				else
					sb.append('*');
			}				
		}
		m_obscuredValue = sb.toString();
		return m_obscuredValue;
	}	//	getObscuredValue


	/**************************************************************************
	 *	test
	 */
	public static void main (String[] args)
	{
		System.out.println (Obscure.obscure("1a2b3c4d5e6f7g8h9"));
	}	//	main

	
}	//	Obscrure
