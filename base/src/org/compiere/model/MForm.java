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
import org.compiere.util.*;


/**
 *	Form Model
 *
 *  @author Jorg Janke
 *  @version $Id: MForm.java,v 1.5 2005/03/11 20:26:02 jjanke Exp $
 */
public class MForm extends X_AD_Form
{

	/**
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param AD_Form_ID id
	 */
	public MForm (Properties ctx, int AD_Form_ID, String trxName)
	{
		super (ctx, AD_Form_ID, trxName);
	}	//	MForm

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MForm (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MForm

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord)
		{
			int AD_Role_ID = Env.getAD_Role_ID(getCtx());
			MFormAccess pa = new MFormAccess(this, AD_Role_ID);
			pa.save();
		}
		return success;
	}	//	afterSave

}	//	MForm
