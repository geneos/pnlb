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

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Comparator;

/**
 *	Class to Sort Data
 *
 *  @author Jorg Janke
 *  @version  $Id: MSort.java,v 1.5 2005/05/14 05:29:50 jjanke Exp $
 */
public final class MSort implements Comparator, Serializable
{
	/**
	 *	Constructor - Sort Entity
	 *  @param new_index index
	 *  @param new_data  data
	 */
	public MSort (int new_index, Object new_data)
	{
		index = new_index;
		data = new_data;
	}	//	MSort

	/** Direct access index */
	public int 		index;
	/** The data            */
	public Object 	data;

	/** Multiplier          */
	private int		m_multiplier = 1;		//	Asc by default

	/**
	 *	Sort Ascending
	 *  @param ascending if true sort ascending
	 */
	public void setSortAsc (boolean ascending)
	{
		if (ascending)
			m_multiplier = 1;
		else
			m_multiplier = -1;
	}	//	setSortAsc

	
	/**************************************************************************
	 *	Compare Data of two entities
	 *  @param o1 object
	 *  @param o2 object
	 *  @return comparator
	 */
	public int compare(Object o1, Object o2)
	{
		//	Get Objects to compare
		Object cmp1 = null;
		if (o1 instanceof MSort)
			cmp1 = ((MSort)o1).data;
		if (cmp1 instanceof NamePair)
			cmp1 = ((NamePair)cmp1).getName();

		Object cmp2 = o2;
		if (o2 instanceof MSort)
			cmp2 = ((MSort)o2).data;
		if (cmp2 instanceof NamePair)
			cmp2 = ((NamePair)cmp2).getName();

		//	Comparing Null values
		if (cmp1 == null)
			cmp1 = new String("");
		if (cmp2 == null)
			cmp2 = new String("");

		/**
		 *	compare different data types
		 */

		//	String
		if (cmp1 instanceof String && cmp1 instanceof String)
		{
			String s = (String)cmp1;
			return s.compareTo((String)cmp2) * m_multiplier;
		}
		//	Date
		else if (cmp1 instanceof Timestamp && cmp2 instanceof Timestamp)
		{
			Timestamp t = (Timestamp)cmp1;
			return t.compareTo((Timestamp)cmp2) * m_multiplier;
		}
		//	BigDecimal
		else if (cmp1 instanceof BigDecimal && cmp2 instanceof BigDecimal)
		{
			BigDecimal d = (BigDecimal)cmp1;
			return d.compareTo((BigDecimal)cmp2) * m_multiplier;
		}
		//	Integer
		else if (cmp1 instanceof Integer && cmp2 instanceof Integer)
		{
			Integer d = (Integer)cmp1;
			return d.compareTo((Integer)cmp2) * m_multiplier;
		}

		//  String value
		String s = cmp1.toString();
		return s.compareTo(cmp2.toString()) * m_multiplier;
	}	//	compare

	/**
	 *	Equal (based on data, ignores index)
	 *  @param obj object
	 *  @return true if equal
	 */
	public boolean equals (Object obj)
	{
		if (obj instanceof MSort)
		{
			MSort ms = (MSort)obj;
			if (data == ms.data)
				return true;
		}
		return false;
	}	//	equals

	/**
	 * 	String Representation
	 * 	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MSort[");
		sb.append("Index=").append(index).append(",Data=").append(data);
		sb.append("]");
		return sb.toString();
	}	//	toString

}	//	MSort
