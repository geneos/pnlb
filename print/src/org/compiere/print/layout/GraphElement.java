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
import java.awt.geom.*;
import java.util.*;
import org.compiere.print.*;

/**
 *	Graphic Element
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: GraphElement.java,v 1.4 2005/03/11 20:34:41 jjanke Exp $
 */
public class GraphElement extends PrintElement
{
	/**
	 *	Constructor
	 *  @param pg graph model
	 */
	public GraphElement(MPrintGraph pg)
	{
	}	//	GraphElement

	/**
	 * 	Layout and Calculate Size
	 * 	Set p_width & p_height
	 * 	@return true if calculated
	 */
	protected boolean calculateSize()
	{
		return false;
	}	//	calcluateSize

	/**
	 * 	Paint/Print.
	 * 	@param g2D Graphics
	 *  @param pageNo page number for multi page support (0 = header/footer)
	 *  @param pageStart top left Location of page
	 *  @param ctx context
	 *  @param isView true if online view (IDs are links)
	 */
	public void paint (Graphics2D g2D, int pageNo, Point2D pageStart, Properties ctx, boolean isView)
	{

	}	//	paint

}	//	GraphElement

