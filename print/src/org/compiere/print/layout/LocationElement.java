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
package org.compiere.print.layout;

import java.awt.*;
import java.util.*;
import java.util.regex.*;

import org.compiere.model.*;

/**
 *	Location/Address Element.
 *  Prints Addresses
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: LocationElement.java,v 1.11 2005/03/11 20:34:41 jjanke Exp $
 */
public class LocationElement extends GridElement
{
	/**
	 *	Constructor
	 * 	@param ctx context
	 * 	@param C_Location_ID location
	 * 	@param font font
	 * 	@param color color
	 */
	public LocationElement(Properties ctx, int C_Location_ID, Font font, Paint color)
	{
		super(10,1);		//	max
		setGap(0,0);
		MLocation ml = MLocation.get (ctx, C_Location_ID, null);
	//	log.fine("C_Location_ID=" + C_Location_ID);
		if (ml != null)
		{
			int index = 0;
			if (ml.isAddressLinesReverse())
			{
				setData(index++, 0, ml.getCountry(true), font, color);
				String[] lines = Pattern.compile("$", Pattern.MULTILINE).split(ml.getCityRegionPostal());
				for (int i = 0; i < lines.length; i++)
					setData(index++, 0, lines[i], font, color);
				if (ml.getAddress4() != null && ml.getAddress4().length() > 0)
					setData(index++, 0, ml.getAddress4(), font, color);
				if (ml.getAddress3() != null && ml.getAddress3().length() > 0)
					setData(index++, 0, ml.getAddress3(), font, color);
				if (ml.getAddress2() != null && ml.getAddress2().length() > 0)
					setData(index++, 0, ml.getAddress2(), font, color);
				if (ml.getAddress1() != null && ml.getAddress1().length() > 0)
					setData(index++, 0, ml.getAddress1(), font, color);
			}
			else
			{
				if (ml.getAddress1() != null && ml.getAddress1().length() > 0)
					setData(index++, 0, ml.getAddress1(), font, color);
				if (ml.getAddress2() != null && ml.getAddress2().length() > 0)
					setData(index++, 0, ml.getAddress2(), font, color);
				if (ml.getAddress3() != null && ml.getAddress3().length() > 0)
					setData(index++, 0, ml.getAddress3(), font, color);
				if (ml.getAddress4() != null && ml.getAddress4().length() > 0)
					setData(index++, 0, ml.getAddress4(), font, color);
				String[] lines = Pattern.compile("$", Pattern.MULTILINE).split(ml.getCityRegionPostal());
				for (int i = 0; i < lines.length; i++)
					setData(index++, 0, lines[i], font, color);
				setData(index++, 0, ml.getCountry(true), font, color);
			}
		}
	}	//	LocationElement
	
}	//	LocationElement
