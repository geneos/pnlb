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

import java.math.*;
import java.sql.*;
import java.util.*;


/**
 *	Resource Assignment Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MResourceAssignment.java,v 1.6 2005/09/19 04:49:47 jjanke Exp $
 */
public class MResourceAssignment extends X_S_ResourceAssignment
{
	/**
	 * 	Stnadard Constructor
	 *	@param ctx
	 *	@param S_ResourceAssignment_ID
	 */
	public MResourceAssignment (Properties ctx, int S_ResourceAssignment_ID, String trxName)
	{
		super (ctx, S_ResourceAssignment_ID, trxName);
		p_info.setUpdateable(true);		//	default table is not updateable
		//	Default values
		if (S_ResourceAssignment_ID == 0)
		{
			setAssignDateFrom(new Timestamp(System.currentTimeMillis()));
			setQty(new BigDecimal(1.0));
			setName(".");
			setIsConfirmed(false);
		}
	}	//	MResourceAssignment

	/**
	 * 	Load Contsructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MResourceAssignment (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MResourceAssignment
	
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return true
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		/*
		v_Description := :new.Name;
	IF (:new.Description IS NOT NULL AND LENGTH(:new.Description) > 0) THEN
		v_Description := v_Description || ' (' || :new.Description || ')';			
	END IF;
	
	-- Update Expense Line
	UPDATE S_TimeExpenseLine
	  SET  Description = v_Description,
		Qty = :new.Qty
	WHERE S_ResourceAssignment_ID = :new.S_ResourceAssignment_ID
	  AND (Description <> v_Description OR Qty <> :new.Qty);
	  
	-- Update Order Line
	UPDATE C_OrderLine
	  SET  Description = v_Description,
		QtyOrdered = :new.Qty
	WHERE S_ResourceAssignment_ID = :new.S_ResourceAssignment_ID
	  AND (Description <> v_Description OR QtyOrdered <> :new.Qty);

	-- Update Invoice Line
	UPDATE C_InvoiceLine
	  SET  Description = v_Description,
		QtyInvoiced = :new.Qty
	WHERE S_ResourceAssignment_ID = :new.S_ResourceAssignment_ID
	  AND (Description <> v_Description OR QtyInvoiced <> :new.Qty);
	  */
		return success;
	}	//	afterSave
	
	/**
	 *  String Representation
	 *  @return string
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MResourceAssignment[ID=");
		sb.append(get_ID())
			.append(",S_Resource_ID=").append(getS_Resource_ID())
			.append(",From=").append(getAssignDateFrom())
			.append(",To=").append(getAssignDateTo())
			.append(",Qty=").append(getQty())
			.append("]");
		return sb.toString();
	}   //  toString

	/**
	 * 	Before Delete
	 *	@return true if not confirmed
	 */
	protected boolean beforeDelete ()
	{
		//	 allow to delete, when not confirmed
		if (isConfirmed())
			return false;
		
		return true;
	}	//	beforeDelete
	
}	//	MResourceAssignment
