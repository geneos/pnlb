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
package org.compiere.process;

import java.util.logging.*;
import java.sql.*;
import java.math.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Update existing Inventory Count List with current Book value
 *	
 *  @author Jorg Janke
 *  @version $Id: InventoryCountUpdate.java,v 1.7 2005/09/19 04:49:45 jjanke Exp $
 */
public class InventoryCountUpdate extends SvrProcess
{
	/** Physical Inventory		*/
	private int	p_M_Inventory_ID = 0;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_M_Inventory_ID = getRecord_ID();
	}	//	prepare

	
	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		log.info("M_Inventory_ID=" + p_M_Inventory_ID);
		MInventory inventory = new MInventory (getCtx(), p_M_Inventory_ID, get_TrxName());
		if (inventory.get_ID() == 0)
			throw new CompiereSystemError ("Not found: M_Inventory_ID=" + p_M_Inventory_ID);

		//	Multiple Lines for one item
		String sql = "UPDATE M_InventoryLine SET IsActive='N' "
			+ "WHERE M_Inventory_ID=" + p_M_Inventory_ID
			+ " AND (M_Product_ID, M_Locator_ID, M_AttributeSetInstance_ID) IN "
				+ "(SELECT M_Product_ID, M_Locator_ID, M_AttributeSetInstance_ID "
				+ "FROM M_InventoryLine "
				+ "WHERE M_Inventory_ID=" + p_M_Inventory_ID
				+ " GROUP BY M_Product_ID, M_Locator_ID, M_AttributeSetInstance_ID "
				+ "HAVING COUNT(*) > 1)";
		int multiple = DB.executeUpdate(sql, get_TrxName());
		log.info("Multiple=" + multiple);

		int delMA = MInventoryLineMA.deleteInventoryMA(p_M_Inventory_ID, get_TrxName());
		log.info("DeletedMA=" + delMA);

		//	ASI
                                // TODO: Contemplar tambien storages inexistentes, para esos casos poner las qty en 0
                                // TODO: Tener en cuenta de actualizar las cantidades en mano al completar el Inventory para que las cantidades sean las que corresponden.
		sql = "UPDATE M_InventoryLine l "
			+ "SET (QtyBook,QtyCount) = "
				+ "(SELECT QtyOnHand,QtyOnHand FROM M_Storage s "
				+ "WHERE s.M_Product_ID=l.M_Product_ID AND s.M_Locator_ID=l.M_Locator_ID"
				+ " AND s.M_AttributeSetInstance_ID=l.M_AttributeSetInstance_ID),"
			+ " Updated=SysDate,"
			+ " UpdatedBy=" + getAD_User_ID()
			//
			+ " WHERE M_Inventory_ID=" + p_M_Inventory_ID
			+ " AND EXISTS (SELECT * FROM M_Storage s "
				+ "WHERE s.M_Product_ID=l.M_Product_ID AND s.M_Locator_ID=l.M_Locator_ID"
				+ " AND s.M_AttributeSetInstance_ID=l.M_AttributeSetInstance_ID)";
		int no = DB.executeUpdate(sql, get_TrxName());
		log.info("Update with ASI=" + no);

		//	No ASI
		int noMA = updateWithMA();
		
		if (multiple > 0)
			return "@M_InventoryLine_ID@ - #" + (no + noMA) + " --> @InventoryProductMultiple@";
		
		return "@M_InventoryLine_ID@ - #" + no;
	}	//	doIt

	/**
	 * 	Update Inventory Lines With Material Allocation
	 *	@return no updated
	 */
	private int updateWithMA()
	{
		int no = 0;
		//
		String sql = "SELECT * FROM M_InventoryLine WHERE M_Inventory_ID=? AND M_AttributeSetInstance_ID=0";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, p_M_Inventory_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MInventoryLine il = new MInventoryLine (getCtx(), rs, get_TrxName());
				BigDecimal onHand = Env.ZERO;
				MStorage[] storages = MStorage.getAll(getCtx(), il.getM_Product_ID(), il.getM_Locator_ID(), get_TrxName());
				MInventoryLineMA ma = null;
				for (int i = 0; i < storages.length; i++)
				{
					MStorage storage = storages[i];
					if (storage.getQtyOnHand().signum() == 0)
						continue;
					onHand = onHand.add(storage.getQtyOnHand());
					//	No ASI
					if (storage.getM_AttributeSetInstance_ID() == 0 
						&& storages.length == 1)
						continue;
					//	Save ASI
					ma = new MInventoryLineMA (il, 
						storage.getM_AttributeSetInstance_ID(), storage.getQtyOnHand());
					if (!ma.save())
						;
				}
				il.setQtyBook(onHand);
				il.setQtyCount(onHand);
				if (il.save())
					no++;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		//
		log.info("#" + no);
		return no;
	}	//	updateWithMA
	
	
}	//	InventoryCountUpdate
