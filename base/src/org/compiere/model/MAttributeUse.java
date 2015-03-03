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
 *	Attribute Use Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MAttributeUse.java,v 1.5 2005/03/11 20:26:04 jjanke Exp $
 */
public class MAttributeUse extends X_M_AttributeUse
{
	/**
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 */
	public MAttributeUse (Properties ctx, int ignored, String trxName)
	{
		super (ctx, ignored, trxName);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MAttributeUse

	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MAttributeUse (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MAttributeUse

	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	also used for afterDelete
		String sql = "UPDATE M_AttributeSet mas"
			+ " SET IsInstanceAttribute='Y' "
			+ "WHERE M_AttributeSet_ID=" + getM_AttributeSet_ID()
			+ " AND IsInstanceAttribute='N'"
			+ " AND (IsSerNo='Y' OR IsLot='Y' OR IsGuaranteeDate='Y'"
				+ " OR EXISTS (SELECT * FROM M_AttributeUse mau"
					+ " INNER JOIN M_Attribute ma ON (mau.M_Attribute_ID=ma.M_Attribute_ID) "
					+ "WHERE mau.M_AttributeSet_ID=mas.M_AttributeSet_ID"
					+ " AND mau.IsActive='Y' AND ma.IsActive='Y'"
					+ " AND ma.IsInstanceAttribute='Y')"
					+ ")";
		int no = DB.executeUpdate(sql, get_TrxName());
		if (no != 0)
			log.fine("afterSave - Set Instance Attribute");
		//
		sql = "UPDATE M_AttributeSet mas"
			+ " SET IsInstanceAttribute='N' "
			+ "WHERE M_AttributeSet_ID=" + getM_AttributeSet_ID()
			+ " AND IsInstanceAttribute='Y'"
			+ "	AND IsSerNo='N' AND IsLot='N' AND IsGuaranteeDate='N'"
			+ " AND NOT EXISTS (SELECT * FROM M_AttributeUse mau"
				+ " INNER JOIN M_Attribute ma ON (mau.M_Attribute_ID=ma.M_Attribute_ID) "
				+ "WHERE mau.M_AttributeSet_ID=mas.M_AttributeSet_ID"
				+ " AND mau.IsActive='Y' AND ma.IsActive='Y'"
				+ " AND ma.IsInstanceAttribute='Y')";
		no = DB.executeUpdate(sql, get_TrxName());
		if (no != 0)
			log.fine("afterSave - Reset Instance Attribute");
		
		return success;
	}	//	afterSave
	
	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterDelete (boolean success)
	{
		afterSave(false, success);
		return success;
	}	//	afterDelete
	
}	//	MAttributeUse
