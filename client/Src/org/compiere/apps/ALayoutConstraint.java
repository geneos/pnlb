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
package org.compiere.apps;


/**
 *  Application Layout Constraint to indicate grid position (immutable)
 *
 *  @author Jorg Janke
 *  @version  $Id: ALayoutConstraint.java,v 1.7 2005/03/11 20:28:21 jjanke Exp $
 */
public class ALayoutConstraint implements Comparable
{
	/**
	 *  Layout Constraint to indicate grid position
	 *  @param row row 0..x
	 *  @param col column 0..x
	 */
	public ALayoutConstraint(int row, int col)
	{
		m_row = row;
		m_col = col;
	}  //  ALayoutConstraint

	/**
	 *  Create Next in Row
	 *  @return ALayoutConstraint for additional column in same row
	 */
	public ALayoutConstraint createNext()
	{
		return new ALayoutConstraint(m_row, m_col+1);
	}   //  createNext

	private int m_row;
	private int m_col;

	/**
	 *  Get Row
	 *  @return roe no
	 */
	public int getRow()
	{
		return m_row;
	}   //  getRow

	/**
	 *  Get Column
	 *  @return col no
	 */
	public int getCol()
	{
		return m_col;
	}   //  getCol

	/**
	 * Compares this object with the specified object for order.  Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified object.<p>
	 *
	 * @param   o the Object to be compared.
	 * @return  a negative integer if this object is less than the specified object,
	 *          zero if equal,
	 *          or a positive integer if this object is greater than the specified object.
	 */
	public int compareTo(Object o)
	{
		ALayoutConstraint comp = null;
		if (o instanceof ALayoutConstraint)
			comp = (ALayoutConstraint)o;
		if (comp == null)
			return +111;

		//  Row compare
		int rowComp = m_row - comp.getRow();
		if (rowComp != 0)
			return rowComp;
		//  Column compare
		return m_col - comp.getCol();
	}   //  compareTo

	/**
	 *  Is Object Equal
	 *  @param o
	 *  @return true if equal
	 */
	public boolean equals(Object o)
	{
		if (o instanceof ALayoutConstraint)
			return compareTo(o) == 0;
		return false;
	}   //  equal

	/**
	 *  To String
	 *  @return info
	 */
	public String toString()
	{
		return "ALayoutConstraint [Row=" + m_row + ", Col=" + m_col + "]";
	}   //  toString

}   //  ALayoutConstraint
