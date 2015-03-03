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

import java.util.*;
import java.awt.*;

/**
 *  Collection of Components ordered based on ALayoutConstraint
 *
 *  @author Jorg Janke
 *  @version  $Id: ALayoutCollection.java,v 1.6 2005/11/14 02:10:58 jjanke Exp $
 */
class ALayoutCollection extends HashMap<Object,Object>
{
	/**
	 *  Create Collection
	 */
	public ALayoutCollection()
	{
		super();
	}   //  ALayoutCollection

	/**
	 *  Add a Component.
	 *  If constraint is null, it is added to the last row as additional column
	 *  @param constraint
	 *  @param component
	 *  @return component
	 *  @throws IllegalArgumentException if component is not a Component
	 */
	public Object put (Object constraint, Object component)
	{
		if (!(component instanceof Component))
			throw new IllegalArgumentException ("ALayoutCollection can only add Component values");

		if (constraint != null
				&& !containsKey(constraint)
				&& constraint instanceof ALayoutConstraint)
		{
		//	Log.trace(this,Log.l6_Database, "ALayoutCollection.put", constraint.toString());
			return super.put (constraint, component);
		}

		//  We need to create constraint
		if (super.size() == 0)
		{
		//	Log.trace(this,Log.l6_Database, "ALayoutCollection.put - first");
			return super.put(new ALayoutConstraint(0,0), component);
		}

		//  Add to end of list
		int row = getMaxRow();
		if (row == -1)
			row = 0;
		int col = getMaxCol(row) + 1;
		ALayoutConstraint next = new ALayoutConstraint(row, col);
	//	Log.trace(this,Log.l6_Database, "ALayoutCollection.put - addEnd", next.toString());
		return super.put(next, component);
	}   //  put

	/**
	 *  Get Maximum Row Number
	 *  @return max row no - or -1 if no row
	 */
	public int getMaxRow ()
	{
		int maxRow = -1;
		//
		Iterator i = keySet().iterator();
		while (i.hasNext())
		{
			ALayoutConstraint c = (ALayoutConstraint)i.next();
			maxRow = Math.max(maxRow, c.getRow());
		}
		return maxRow;
	}   //  getMaxRow

	/**
	 *  Get Maximum Column Number
	 *  @return max col no - or -1 if no column
	 */
	public int getMaxCol ()
	{
		int maxCol = -1;
		//
		Iterator i = keySet().iterator();
		while (i.hasNext())
		{
			ALayoutConstraint c = (ALayoutConstraint)i.next();
			maxCol = Math.max(maxCol, c.getCol());
		}
		return maxCol;
	}   //  getMaxCol

	/**
	 *  Get Maximum Column Number for Row
	 *  @param row
	 *  @return max col no for row - or -1 if no col in row
	 */
	public int getMaxCol (int row)
	{
		int maxCol = -1;
		//
		Iterator i = keySet().iterator();
		while (i.hasNext())
		{
			ALayoutConstraint c = (ALayoutConstraint)i.next();
			if (c.getRow() == row)
				maxCol = Math.max(maxCol, c.getCol());
		}
		return maxCol;
	}   //  getMaxCol

}   //  ALayoutCollection
