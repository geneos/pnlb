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

import java.sql.*;
import java.util.*;


/**
 *	Field Model
 *	
 *  @author Jorg Janke
 *  @version $Id: M_Field.java,v 1.7 2005/09/16 03:54:02 jjanke Exp $
 */
public class M_Field extends X_AD_Field
{

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Field_ID id
	 */
	public M_Field (Properties ctx, int AD_Field_ID, String trxName)
	{
		super (ctx, AD_Field_ID, trxName);
		if (AD_Field_ID == 0)
		{
		//	setAD_Tab_ID (0);	//	parent
		//	setAD_Column_ID (0);
		//	setName (null);
			setEntityType (ENTITYTYPE_UserMaintained);	// U
			setIsCentrallyMaintained (true);	// Y
			setIsDisplayed (true);	// Y
			setIsEncrypted (false);
			setIsFieldOnly (false);
			setIsHeading (false);
			setIsReadOnly (false);
			setIsSameLine (false);
		//	setObscureType(OBSCURETYPE_ObscureDigitsButLast4);
		}	
	}	//	M_Field

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public M_Field (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	M_Field

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 */
	public M_Field (M_Tab parent)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setAD_Tab_ID(parent.getAD_Tab_ID());
	}	//	M_Field
	
	/**
	 * 	Copy Constructor
	 *	@param parent parent
	 *	@param from copy from
	 */
	public M_Field (M_Tab parent, M_Field from)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		copyValues(from, this);
		setClientOrg(parent);
		setAD_Tab_ID(parent.getAD_Tab_ID());
		setEntityType(parent.getEntityType());
	}	//	M_Field
	
	/**
	 * 	Set Column Values
	 *	@param column column
	 */
	public void setColumn (M_Column column)
	{
		setAD_Column_ID (column.getAD_Column_ID());
		setName (column.getName());
		setDescription(column.getDescription());
		setHelp(column.getHelp());
		setDisplayLength(column.getFieldLength());
		setEntityType(column.getEntityType());
	}	//	setColumn
	
}	//	M_Field
