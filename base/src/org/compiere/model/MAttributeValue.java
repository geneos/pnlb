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

import java.util.*;
import java.sql.*;

/**
 *  Product Attribute Value
 *
 *	@author Jorg Janke
 *	@version $Id: MAttributeValue.java,v 1.5 2005/03/11 20:26:00 jjanke Exp $
 */
public class MAttributeValue extends X_M_AttributeValue
{
	/**
	 * 	Constructor
	 *	@param ctx context
	 *	@param M_AttributeValue_ID id
	 */
	public MAttributeValue (Properties ctx, int M_AttributeValue_ID, String trxName)
	{
		super (ctx, M_AttributeValue_ID, trxName);
		/** if (M_AttributeValue_ID == 0)
		{
		setM_AttributeValue_ID (0);
		setM_Attribute_ID (0);
		setName (null);
		setValue (null);
		}
		**/
	}	//	MAttributeValue

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MAttributeValue (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MAttributeValue

	/**
	 *	String Representation
	 * 	@return info
	 */
	public String toString()
	{
		return getName();
	}	//	toString

}	//	MAttributeValue
