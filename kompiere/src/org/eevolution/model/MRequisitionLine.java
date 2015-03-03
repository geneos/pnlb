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
package org.eevolution.model;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;
import org.eevolution.model.MMPCMRP;
/**
 *	Requisition Line Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRequisitionLine.java,v 1.12 2005/10/26 00:37:41 jjanke Exp $
 */
public class MRequisitionLine extends org.compiere.model.MRequisitionLine
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_RequisitionLine_ID id
	 */
	public MRequisitionLine (Properties ctx, int M_RequisitionLine_ID, String trxName)
	{
		super (ctx, M_RequisitionLine_ID, trxName);				
	}	//	MRequisitionLine
        
        	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRequisitionLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRequisitionLine


	
	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		
		return super.beforeSave (newRecord);
	}	//	beforeSave
	
	/**
	 * 	After Save.
	 * 	Update Total on Header
	 *	@param newRecord if new record
	 *	@param success save was success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
                //begin e-evolution vpj-cd 10/30/2004
                MMPCMRP.M_RequisitionLine(this,get_TrxName(),false);
                //end e-evolution vpj-cd 10/30/2004
		return super.afterSave (newRecord, success);
	}	//	afterSave

	
	/**
	 * 	After Delete
	 *	@param success
	 *	@return true/false
	 */
	protected boolean afterDelete (boolean success)
	{
		if (!success)
			return success;
                 //begin e-evolution vpj-cd 10/30/2004
                MMPCMRP.M_RequisitionLine(this,get_TrxName(),true);
                //end e-evolution vpj-cd 10/30/2004
		return super.afterDelete(success);
	}	//	afterDelete		
}	//	MRequisitionLine
