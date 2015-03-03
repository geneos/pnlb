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

import java.util.*;
import org.compiere.model.*;
import org.compiere.print.*;
import org.compiere.util.*;

/**
 *	Parameter Table
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ParameterElement.java,v 1.8 2005/03/11 20:34:41 jjanke Exp $
 */
public class ParameterElement extends GridElement
{
	/**
	 * 	Parameter Element.
	 *  <pre>
	 *  Parameter fromValue - toValue
	 *  </pre>
	 * 	@param query query
	 *  @param ctx context
	 *  @param tFormat Table Format
	 */
	public ParameterElement(MQuery query, Properties ctx, MPrintTableFormat tFormat)
	{
		super (query.getRestrictionCount(), 4);
		setData (0, 0, Msg.getMsg(ctx, "Parameter") + ":", tFormat.getPageHeader_Font(), tFormat.getPageHeaderFG_Color());
		for (int r = 0; r < query.getRestrictionCount(); r++)
		{
			setData (r, 1, query.getInfoName(r), tFormat.getParameter_Font(), tFormat.getParameter_Color());
			setData (r, 2, query.getInfoOperator(r), tFormat.getParameter_Font(), tFormat.getParameter_Color());
			setData (r, 3, query.getInfoDisplayAll(r), tFormat.getParameter_Font(), tFormat.getParameter_Color());
		}
	}	//	ParameterElement

}	//	ParameterElement

