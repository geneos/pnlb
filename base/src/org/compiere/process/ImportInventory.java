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

import java.math.*;
import java.sql.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Import Physical Inventory fom I_Inventory
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ImportInventory.java,v 1.13 2005/10/26 00:37:42 jjanke Exp $
 */
public class ImportInventory extends SvrProcess
{
	/**	Client to be imported to		*/
	private int				p_AD_Client_ID = 0;
	/**	Organization to be imported to	*/
	private int				p_AD_Org_ID = 0;
	/**	Location to be imported to		*/
	private int				p_M_Locator_ID = 0;
	/**	Default Date					*/
	private Timestamp		p_MovementDate = null;
	/**	Delete old Imported				*/
	private boolean			p_DeleteOldImported = false;

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
			else if (name.equals("AD_Client_ID"))
				p_AD_Client_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("AD_Org_ID"))
				p_AD_Org_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("M_Locator_ID"))
				p_M_Locator_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("MovementDate"))
				p_MovementDate = (Timestamp)para[i].getParameter();
			else if (name.equals("DeleteOldImported"))
				p_DeleteOldImported = "Y".equals(para[i].getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare


	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception
	 */
	protected String doIt() throws java.lang.Exception
	{
		log.info("M_Locator_ID=" + p_M_Locator_ID + ",MovementDate=" + p_MovementDate);
		//
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + p_AD_Client_ID;

		//	****	Prepare	****

		//	Delete Old Imported
		if (p_DeleteOldImported)
		{
			sql = new StringBuffer ("DELETE I_Inventory "
				  + "WHERE I_IsImported='Y'").append (clientCheck);
			no = DB.executeUpdate (sql.toString (), get_TrxName());
			log.fine("Delete Old Impored =" + no);
		}

		//	Set Client, Org, Location, IsActive, Created/Updated
		sql = new StringBuffer ("UPDATE I_Inventory "
			  + "SET AD_Client_ID = COALESCE (AD_Client_ID,").append (p_AD_Client_ID).append ("),"
			  + " AD_Org_ID = COALESCE (AD_Org_ID,").append (p_AD_Org_ID).append ("),");
		if (p_MovementDate != null)
			sql.append(" MovementDate = COALESCE (MovementDate,").append (DB.TO_DATE(p_MovementDate)).append ("),");
		sql.append(" IsActive = COALESCE (IsActive, 'Y'),"
			  + " Created = COALESCE (Created, SysDate),"
			  + " CreatedBy = COALESCE (CreatedBy, 0),"
			  + " Updated = COALESCE (Updated, SysDate),"
			  + " UpdatedBy = COALESCE (UpdatedBy, 0),"
			  + " I_ErrorMsg = NULL,"
			  + " M_Warehouse_ID = NULL,"	//	reset
			  + " I_IsImported = 'N' "
			  + "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdate (sql.toString (), get_TrxName());
		log.info ("Reset=" + no);

		sql = new StringBuffer ("UPDATE I_Inventory o "
			+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Org, '"
			+ "WHERE (AD_Org_ID IS NULL OR AD_Org_ID=0"
			+ " OR EXISTS (SELECT * FROM AD_Org oo WHERE o.AD_Org_ID=oo.AD_Org_ID AND (oo.IsSummary='Y' OR oo.IsActive='N')))"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate (sql.toString (), get_TrxName());
		if (no != 0)
			log.warning ("Invalid Org=" + no);


		//	Location
		sql = new StringBuffer ("UPDATE I_Inventory i "
			+ "SET M_Locator_ID=(SELECT M_Locator_ID FROM M_Locator l"
			+ " WHERE i.LocatorValue=l.Value AND i.AD_Client_ID=l.AD_Client_ID AND ROWNUM=1) "
			+ "WHERE M_Locator_ID IS NULL AND LocatorValue IS NOT NULL"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate (sql.toString (), get_TrxName());
		log.fine("Set Locator from Value =" + no);
		sql = new StringBuffer ("UPDATE I_Inventory i "
			+ "SET M_Locator_ID=(SELECT M_Locator_ID FROM M_Locator l"
			+ " WHERE i.X=l.X AND i.Y=l.Y AND i.Z=l.Z AND i.AD_Client_ID=l.AD_Client_ID AND ROWNUM=1) "
			+ "WHERE M_Locator_ID IS NULL AND X IS NOT NULL AND Y IS NOT NULL AND Z IS NOT NULL"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate (sql.toString (), get_TrxName());
		log.fine("Set Locator from X,Y,Z =" + no);
		if (p_M_Locator_ID != 0)
		{
			sql = new StringBuffer ("UPDATE I_Inventory "
				+ "SET M_Locator_ID = ").append (p_M_Locator_ID).append (
				" WHERE M_Locator_ID IS NULL"
				+ " AND I_IsImported<>'Y'").append (clientCheck);
			no = DB.executeUpdate (sql.toString (), get_TrxName());
			log.fine("Set Locator from Parameter=" + no);
		}
		sql = new StringBuffer ("UPDATE I_Inventory "
			+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=No Location, ' "
			+ "WHERE M_Locator_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate (sql.toString (), get_TrxName());
		if (no != 0)
			log.warning ("No Location=" + no);


		//	Set M_Warehouse_ID
		sql = new StringBuffer ("UPDATE I_Inventory i "
			+ "SET M_Warehouse_ID=(SELECT M_Warehouse_ID FROM M_Locator l WHERE i.M_Locator_ID=l.M_Locator_ID) "
			+ "WHERE M_Locator_ID IS NOT NULL"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate (sql.toString (), get_TrxName());
		log.fine("Set Warehouse from Locator =" + no);
		sql = new StringBuffer ("UPDATE I_Inventory "
			+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=No Warehouse, ' "
			+ "WHERE M_Warehouse_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate (sql.toString (), get_TrxName());
		if (no != 0)
			log.warning ("No Warehouse=" + no);


		//	Product
		sql = new StringBuffer ("UPDATE I_Inventory i "
			  + "SET M_Product_ID=(SELECT M_Product_ID FROM M_Product p"
			  + " WHERE i.Value=p.Value AND i.AD_Client_ID=p.AD_Client_ID AND ROWNUM=1) "
			  + "WHERE M_Product_ID IS NULL AND Value IS NOT NULL"
			  + " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate (sql.toString (), get_TrxName());
		log.fine("Set Product from Value=" + no);
		sql = new StringBuffer ("UPDATE I_Inventory i "
			  + "SET M_Product_ID=(SELECT M_Product_ID FROM M_Product p"
			  + " WHERE i.UPC=p.UPC AND i.AD_Client_ID=p.AD_Client_ID AND ROWNUM=1) "
			  + "WHERE M_Product_ID IS NULL AND UPC IS NOT NULL"
			  + " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate (sql.toString (), get_TrxName());
		log.fine("Set Product from UPC=" + no);
		sql = new StringBuffer ("UPDATE I_Inventory "
			+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=No Product, ' "
			+ "WHERE M_Product_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate (sql.toString (), get_TrxName());
		if (no != 0)
			log.warning ("No Product=" + no);

		//	No QtyCount
		sql = new StringBuffer ("UPDATE I_Inventory "
			+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=No Qty Count, ' "
			+ "WHERE QtyCount IS NULL"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate (sql.toString (), get_TrxName());
		if (no != 0)
			log.warning ("No QtyCount=" + no);

		commit();
		
		/*********************************************************************/

		MInventory inventory = null;

		int noInsert = 0;
		int noInsertLine = 0;

		//	Go through Inventory Records
		sql = new StringBuffer ("SELECT * FROM I_Inventory "
			+ "WHERE I_IsImported='N'").append (clientCheck)
			.append(" ORDER BY M_Warehouse_ID, TRUNC(MovementDate), I_Inventory_ID");
		try
		{
			PreparedStatement pstmt = DB.prepareStatement (sql.toString (), get_TrxName());
			ResultSet rs = pstmt.executeQuery ();
			//
			int x_M_Warehouse_ID = -1;
			Timestamp x_MovementDate = null;
			while (rs.next())
			{
				X_I_Inventory imp = new X_I_Inventory (getCtx (), rs, get_TrxName());
				Timestamp MovementDate = TimeUtil.getDay(imp.getMovementDate());

				if (inventory == null
					|| imp.getM_Warehouse_ID() != x_M_Warehouse_ID
					|| !MovementDate.equals(x_MovementDate))
				{
					inventory = new MInventory (getCtx(), 0, get_TrxName());
					inventory.setClientOrg(imp.getAD_Client_ID(), imp.getAD_Org_ID());
					inventory.setDescription("I " + imp.getM_Warehouse_ID() + " " + MovementDate);
					inventory.setM_Warehouse_ID(imp.getM_Warehouse_ID());
					inventory.setMovementDate(MovementDate);
					//
					if (!inventory.save())
					{
						log.log(Level.SEVERE, "Inventory not saved");
						break;
					}
					x_M_Warehouse_ID = imp.getM_Warehouse_ID();
					x_MovementDate = MovementDate;
					noInsert++;
				}

				//	Line
				int M_AttributeSetInstance_ID = 0;
				if (imp.getLot() != null || imp.getSerNo() != null)
				{
					MProduct product = MProduct.get(getCtx(), imp.getM_Product_ID());
					if (product.isInstanceAttribute())
					{
						MAttributeSet mas = product.getAttributeSet();
						MAttributeSetInstance masi = new MAttributeSetInstance(getCtx(), 0, mas.getM_AttributeSet_ID(), get_TrxName());
						if (mas.isLot() && imp.getLot() != null)
							masi.setLot(imp.getLot(), imp.getM_Product_ID());
						if (mas.isSerNo() && imp.getSerNo() != null)
							masi.setSerNo(imp.getSerNo());
						masi.setDescription();
						masi.save();
						M_AttributeSetInstance_ID = masi.getM_AttributeSetInstance_ID();
					}
				}
				MInventoryLine line = new MInventoryLine (inventory, 
					imp.getM_Locator_ID(), imp.getM_Product_ID(), M_AttributeSetInstance_ID,
					imp.getQtyBook(), imp.getQtyCount());
				if (line.save())
				{
					imp.setI_IsImported(true);
					imp.setM_Inventory_ID(line.getM_Inventory_ID());
					imp.setM_InventoryLine_ID(line.getM_InventoryLine_ID());
					imp.setProcessed(true);
					if (imp.save())
						noInsertLine++;
				}
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}

		//	Set Error to indicator to not imported
		sql = new StringBuffer ("UPDATE I_Inventory "
			+ "SET I_IsImported='N', Updated=SysDate "
			+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		addLog (0, null, new BigDecimal (no), "@Errors@");
		//
		addLog (0, null, new BigDecimal (noInsert), "@M_Inventory_ID@: @Inserted@");
		addLog (0, null, new BigDecimal (noInsertLine), "@M_InventoryLine_ID@: @Inserted@");
		return "";
	}	//	doIt

}	//	ImportInventory
