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

/**
 *	(String) Value Name Pair
 *
 *  @author     Jorg Janke
 *  @version    $Id: ValueNamePair.java,v 1.5 2005/03/11 20:34:37 jjanke Exp $
 */
public final class ValueNamePair extends NamePair
{
	/**
	 *	Construct KeyValue Pair
	 *  @param value value
	 *  @param name string representation
	 */
	public ValueNamePair(String value, String name)
	{
		super(name);
		m_value = value;
		if (m_value == null)
			m_value = "";
	}   //  ValueNamePair

	/** The Value       */
	private String m_value = null;

	/**
	 *	Get Value
	 *  @return Value
	 */
	public String getValue()
	{
		return m_value;
	}	//	getValue

	/**
	 *	Get ID
	 *  @return Value
	 */
	public String getID()
	{
		return m_value;
	}	//	getID

	/**
	 *	Equals
	 *  @param obj Object
	 *  @return true, if equal
	 */
	public boolean equals(Object obj)
	{
		if (obj instanceof ValueNamePair)
		{
			ValueNamePair pp = (ValueNamePair)obj;
			if (pp.getName() != null && pp.getValue() != null &&
				pp.getName().equals(getName()) && pp.getValue().equals(m_value))
				return true;
			return false;
		}
		return false;
	}	//	equals

	/**
	 *  Return Hashcode of value
	 *  @return hascode
	 */
	public int hashCode()
	{
		return m_value.hashCode();
	}   //  hashCode

}	//	KeyValuePair

