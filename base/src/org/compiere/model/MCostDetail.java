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
import java.util.logging.*;

import org.compiere.util.*;

/**
 * 	Cost Detail Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MCostDetail.java,v 1.12 2005/12/20 04:21:04 jjanke Exp $
 */

public class MCostDetail extends X_M_CostDetail
{
	/**
	 * 	Create New Order Cost Detail for Purchase Orders.
	 * 	Called from Doc_MatchPO
	 *	@param as accounting schema
	 *	@param AD_Org_ID org
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param C_OrderLine_ID order
	 *	@param M_CostElement_ID optional cost element for Freight
	 *	@param Amt amt total amount
	 *	@param Qty qty
	 *	@param Description optional description
	 */
	
        /* Bision - 17/07/08 - Ibañez Santiago
         * Se agregó esta variable logLevel para poder definir el nivel de log
         * */
        public static int logLevel = 1;
        
        public static boolean createOrder (MAcctSchema as, int AD_Org_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,
		int C_OrderLine_ID, int M_CostElement_ID, 
		BigDecimal Amt, BigDecimal Qty,
		String Description, String trxName)
	{
		/* Bision - 17/07/08 - Ibañez Santiago
                 * Se agregaron logs para testear este método
                 * */
                if (logLevel==1){
                    System.out.println("Clase: MCostDetail - Método: createOrder");
                    System.out.println("Parámetros de entrada:\n MAcctSchema: "+as.toString());
                    System.out.println("AD_Org_ID: "+AD_Org_ID);
                    System.out.println("M_Product_ID: "+M_Product_ID);
                    System.out.println("M_AttributeSetInstance_ID: "+M_AttributeSetInstance_ID);
                    System.out.println("C_OrderLine_ID: "+C_OrderLine_ID);
                    System.out.println("M_CostElement_ID: "+M_CostElement_ID);
                    System.out.println("Amt: "+Amt);
                    System.out.println("Qty: "+Qty);
                    System.out.println("Description"+Description);
                }//fin logs
                //	Delete Unprocessed zero Differences
		String sql = "DELETE M_CostDetail "
			+ "WHERE Processed='N' AND COALESCE(DeltaAmt,0)=0 AND COALESCE(DeltaQty,0)=0"
			+ " AND C_OrderLine_ID=" + C_OrderLine_ID
			+ " AND M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID;
		int no = DB.executeUpdate(sql, trxName);
		if (no != 0)
			s_log.config("Deleted #" + no);
		MCostDetail cd = get (as.getCtx(), "C_OrderLine_ID=? AND M_AttributeSetInstance_ID=?", 
			C_OrderLine_ID, M_AttributeSetInstance_ID, trxName);
		//
		if (cd == null)		//	createNew
		{
			cd = new MCostDetail (as, AD_Org_ID, 
				M_Product_ID, M_AttributeSetInstance_ID, 
				M_CostElement_ID, 
				Amt, Qty, Description, trxName);
			cd.setC_OrderLine_ID (C_OrderLine_ID);
		}
		else
		{
			cd.setDeltaAmt(cd.getAmt().subtract(Amt));
			cd.setDeltaQty(cd.getQty().subtract(Qty));
			if (cd.isDelta())
				cd.setProcessed(false);
			else
				return true;	//	nothing to do
		}
		boolean ok = cd.save();
		if (ok && !cd.isProcessed())
		{
			MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
			if (client.isCostImmediate())
				cd.process();
		}
		s_log.config("(" + ok + ") " + cd);
		return ok;
	}	//	createOrder

	/**
	 * 	Create New Invoice Cost Detail for AP Invoices.
	 * 	Called from Doc_Invoice - for Invoice Adjustments
	 *	@param as accounting schema
	 *	@param AD_Org_ID org
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param C_InvoiceLine_ID invoice
	 *	@param M_CostElement_ID optional cost element for Freight
	 *	@param Amt amt
	 *	@param Qty qty
	 *	@param Description optional description
	 *	@return true if created
	 */
	public static boolean createInvoice (MAcctSchema as, int AD_Org_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,
		int C_InvoiceLine_ID, int M_CostElement_ID, 
		BigDecimal Amt, BigDecimal Qty,
		String Description, String trxName)
	{
		/* Bision - 17/07/08 - Ibañez Santiago
                 * Se agregaron logs para testear este método
                 * */
                if (logLevel==1){
                    System.out.println("Clase: MCostDetail - Método: createInvoice");
                    System.out.println("Parámetros de entrada:\n MAcctSchema: "+as.toString());
                    System.out.println("AD_Org_ID: "+AD_Org_ID);
                    System.out.println("M_Product_ID: "+M_Product_ID);
                    System.out.println("M_AttributeSetInstance_ID: "+M_AttributeSetInstance_ID);
                    System.out.println("C_InvoiceLine_ID: "+C_InvoiceLine_ID);
                    System.out.println("M_CostElement_ID: "+M_CostElement_ID);
                    System.out.println("Amt: "+Amt);
                    System.out.println("Qty: "+Qty);
                    System.out.println("Description"+Description);
                }//fin logs
                //	Delete Unprocessed zero Differences
		String sql = "DELETE M_CostDetail "
			+ "WHERE Processed='N' AND COALESCE(DeltaAmt,0)=0 AND COALESCE(DeltaQty,0)=0"
			+ " AND C_InvoiceLine_ID=" + C_InvoiceLine_ID
			+ " AND M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID;
		int no = DB.executeUpdate(sql, trxName);
		if (no != 0)
			s_log.config("Deleted #" + no);
		MCostDetail cd = get (as.getCtx(), "C_InvoiceLine_ID=? AND M_AttributeSetInstance_ID=?", 
			C_InvoiceLine_ID, M_AttributeSetInstance_ID, trxName);
		//
		if (cd == null)		//	createNew
		{
			cd = new MCostDetail (as, AD_Org_ID, 
				M_Product_ID, M_AttributeSetInstance_ID, 
				M_CostElement_ID, 
				Amt, Qty, Description, trxName);
			cd.setC_InvoiceLine_ID (C_InvoiceLine_ID);
		}
		else
		{
			cd.setDeltaAmt(cd.getAmt().subtract(Amt));
			cd.setDeltaQty(cd.getQty().subtract(Qty));
			if (cd.isDelta())
				cd.setProcessed(false);
			else
				return true;	//	nothing to do
		}
		boolean ok = cd.save();
		if (ok && !cd.isProcessed())
		{
			MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
			if (client.isCostImmediate())
				cd.process();
		}
		s_log.config("(" + ok + ") " + cd);
		return ok;
	}	//	createInvoice
	
	/**
	 * 	Create New Shipment Cost Detail for SO Shipments.
	 * 	Called from Doc_MInOut - for SO Shipments  
	 *	@param as accounting schema
	 *	@param AD_Org_ID org
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param M_InOutLine_ID shipment
	 *	@param M_CostElement_ID optional cost element for Freight
	 *	@param Amt amt
	 *	@param Qty qty
	 *	@param Description optional description
	 *	@param IsSOTrx sales order
	 */
	public static boolean createShipment (MAcctSchema as, int AD_Org_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,
		int M_InOutLine_ID, int M_CostElement_ID, 
		BigDecimal Amt, BigDecimal Qty,
		String Description, boolean IsSOTrx, String trxName)
	{
		/* Bision - 17/07/08 - Ibañez Santiago
                 * Se agregaron logs para testear este método
                 * */
                if (logLevel==1){
                    System.out.println("Clase: MCostDetail - Método: createShipment");
                    System.out.println("Parámetros de entrada:\n MAcctSchema: "+as.toString());
                    System.out.println("AD_Org_ID: "+AD_Org_ID);
                    System.out.println("M_Product_ID: "+M_Product_ID);
                    System.out.println("M_AttributeSetInstance_ID: "+M_AttributeSetInstance_ID);
                    System.out.println("M_InOutLine_ID: "+M_InOutLine_ID);
                    System.out.println("M_CostElement_ID: "+M_CostElement_ID);
                    System.out.println("Amt: "+Amt);
                    System.out.println("Qty: "+Qty);
                    System.out.println("Description"+Description);
                    System.out.println("IsSOTrx"+IsSOTrx);
                }//fin logs
                //	Delete Unprocessed zero Differences
		String sql = "DELETE M_CostDetail "
			+ "WHERE Processed='N' AND COALESCE(DeltaAmt,0)=0 AND COALESCE(DeltaQty,0)=0"
			+ " AND M_InOutLine_ID=" + M_InOutLine_ID
			+ " AND M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID;
		int no = DB.executeUpdate(sql, trxName);
		if (no != 0)
			s_log.config("Deleted #" + no);
		MCostDetail cd = get (as.getCtx(), "M_InOutLine_ID=? AND M_AttributeSetInstance_ID=?", 
			M_InOutLine_ID, M_AttributeSetInstance_ID, trxName);
		//
		if (cd == null)		//	createNew
		{
			cd = new MCostDetail (as, AD_Org_ID, 
				M_Product_ID, M_AttributeSetInstance_ID, 
				M_CostElement_ID, 
				Amt, Qty, Description, trxName);
			cd.setM_InOutLine_ID(M_InOutLine_ID);
			cd.setIsSOTrx(IsSOTrx);
		}
		else
		{
			cd.setDeltaAmt(cd.getAmt().subtract(Amt));
			cd.setDeltaQty(cd.getQty().subtract(Qty));
			if (cd.isDelta())
				cd.setProcessed(false);
			else
				return true;	//	nothing to do
		}
		boolean ok = cd.save();
		if (ok && !cd.isProcessed())
		{
			MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
			if (client.isCostImmediate())
				cd.process();
		}
		s_log.config("(" + ok + ") " + cd);
		return ok;
	}	//	createShipment

	/**
	 * 	Create New Order Cost Detail for Physical Inventory.
	 * 	Called from Doc_Inventory
	 *	@param as accounting schema
	 *	@param AD_Org_ID org
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param M_InventoryLine_ID order
	 *	@param M_CostElement_ID optional cost element
	 *	@param Amt amt total amount
	 *	@param Qty qty
	 *	@param Description optional description
	 */
	public static boolean createInventory (MAcctSchema as, int AD_Org_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,
		int M_InventoryLine_ID, int M_CostElement_ID, 
		BigDecimal Amt, BigDecimal Qty,
		String Description, String trxName)
	{
		/* Bision - 17/07/08 - Ibañez Santiago
                 * Se agregaron logs para testear este método
                 * */
                if (logLevel==1){
                    System.out.println("Clase: MCostDetail - Método: createShipment");
                    System.out.println("Parámetros de entrada:\n MAcctSchema: "+as.toString());
                    System.out.println("AD_Org_ID: "+AD_Org_ID);
                    System.out.println("M_Product_ID: "+M_Product_ID);
                    System.out.println("M_AttributeSetInstance_ID: "+M_AttributeSetInstance_ID);
                    System.out.println("M_InventoryLine_ID: "+M_InventoryLine_ID);
                    System.out.println("M_CostElement_ID: "+M_CostElement_ID);
                    System.out.println("Amt: "+Amt);
                    System.out.println("Qty: "+Qty);
                    System.out.println("Description"+Description);
                }//fin logs
                //	Delete Unprocessed zero Differences
		String sql = "DELETE M_CostDetail "
			+ "WHERE Processed='N' AND COALESCE(DeltaAmt,0)=0 AND COALESCE(DeltaQty,0)=0"
			+ " AND M_InventoryLine_ID=" + M_InventoryLine_ID
			+ " AND M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID;
		int no = DB.executeUpdate(sql, trxName);
		if (no != 0)
			s_log.config("Deleted #" + no);
		MCostDetail cd = get (as.getCtx(), "M_InventoryLine_ID=? AND M_AttributeSetInstance_ID=?", 
			M_InventoryLine_ID, M_AttributeSetInstance_ID, trxName);
		//
		if (cd == null)		//	createNew
		{
			cd = new MCostDetail (as, AD_Org_ID, 
				M_Product_ID, M_AttributeSetInstance_ID, 
				M_CostElement_ID, 
				Amt, Qty, Description, trxName);
			cd.setM_InventoryLine_ID(M_InventoryLine_ID);
		}
		else
		{
			cd.setDeltaAmt(cd.getAmt().subtract(Amt));
			cd.setDeltaQty(cd.getQty().subtract(Qty));
			if (cd.isDelta())
				cd.setProcessed(false);
			else
				return true;	//	nothing to do
		}
		boolean ok = cd.save();
		if (ok && !cd.isProcessed())
		{
			MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
			if (client.isCostImmediate())
				cd.process();
		}
		s_log.config("(" + ok + ") " + cd);
		return ok;
	}	//	createInventory
	
	/**
	 * 	Create New Order Cost Detail for Movements.
	 * 	Called from Doc_Movement
	 *	@param as accounting schema
	 *	@param AD_Org_ID org
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param M_MovementLine_ID movement
	 *	@param M_CostElement_ID optional cost element for Freight
	 *	@param Amt amt total amount
	 *	@param Qty qty
	 *	@param from if true the from (reduction)
	 *	@param Description optional description
	 */
	public static boolean createMovement (MAcctSchema as, int AD_Org_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,
		int M_MovementLine_ID, int M_CostElement_ID, 
		BigDecimal Amt, BigDecimal Qty, boolean from,
		String Description, String trxName)
	{
		/* Bision - 17/07/08 - Ibañez Santiago
                 * Se agregaron logs para testear este método
                 * */
                if (logLevel==1){
                    System.out.println("Clase: MCostDetail - Método: createShipment");
                    System.out.println("Parámetros de entrada:\n MAcctSchema: "+as.toString());
                    System.out.println("AD_Org_ID: "+AD_Org_ID);
                    System.out.println("M_Product_ID: "+M_Product_ID);
                    System.out.println("M_AttributeSetInstance_ID: "+M_AttributeSetInstance_ID);
                    System.out.println("M_MovementLine_ID: "+M_MovementLine_ID);
                    System.out.println("M_CostElement_ID: "+M_CostElement_ID);
                    System.out.println("Amt: "+Amt);
                    System.out.println("Qty: "+Qty);
                    System.out.println("Description"+Description);
                }//fin logs
                //	Delete Unprocessed zero Differences
		String sql = "DELETE M_CostDetail "
			+ "WHERE Processed='N' AND COALESCE(DeltaAmt,0)=0 AND COALESCE(DeltaQty,0)=0"
			+ " AND M_MovementLine_ID=" + M_MovementLine_ID 
			+ " AND IsSOTrx=" + (from ? "'Y'" : "'N'")
			+ " AND M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID;
		int no = DB.executeUpdate(sql, trxName);
		if (no != 0)
			s_log.config("Deleted #" + no);
		MCostDetail cd = get (as.getCtx(), "M_MovementLine_ID=? AND M_AttributeSetInstance_ID=? AND IsSOTrx=" 
			+ (from ? "'Y'" : "'N'"), 
			M_MovementLine_ID, M_AttributeSetInstance_ID, trxName);
		//
		if (cd == null)		//	createNew
		{
			cd = new MCostDetail (as, AD_Org_ID, 
				M_Product_ID, M_AttributeSetInstance_ID, 
				M_CostElement_ID, 
				Amt, Qty, Description, trxName);
			cd.setM_MovementLine_ID (M_MovementLine_ID);
			cd.setIsSOTrx(from);
		}
		else
		{
			cd.setDeltaAmt(cd.getAmt().subtract(Amt));
			cd.setDeltaQty(cd.getQty().subtract(Qty));
			if (cd.isDelta())
				cd.setProcessed(false);
			else
				return true;	//	nothing to do
		}
		boolean ok = cd.save();
		if (ok && !cd.isProcessed())
		{
			MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
			if (client.isCostImmediate())
				cd.process();
		}
		s_log.config("(" + ok + ") " + cd);
		return ok;
	}	//	createMovement

	/**
	 * 	Create New Order Cost Detail for Production.
	 * 	Called from Doc_Production
	 *	@param as accounting schema
	 *	@param AD_Org_ID org
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param M_ProductionLine_ID production line
	 *	@param M_CostElement_ID optional cost element
	 *	@param Amt amt total amount
	 *	@param Qty qty
	 *	@param Description optional description
	 */
	public static boolean createProduction (MAcctSchema as, int AD_Org_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,
		int M_ProductionLine_ID, int M_CostElement_ID, 
		BigDecimal Amt, BigDecimal Qty,
		String Description, String trxName)
	{
		/* Bision - 17/07/08 - Iba�ez Santiago
                 * Se agregaron logs para testear este m�todo
                 * */
                if (logLevel==1){
                    System.out.println("Clase: MCostDetail - M�todo: createShipment");
                    System.out.println("Par�metros de entrada:\n MAcctSchema: "+as.toString());
                    System.out.println("AD_Org_ID: "+AD_Org_ID);
                    System.out.println("M_Product_ID: "+M_Product_ID);
                    System.out.println("M_AttributeSetInstance_ID: "+M_AttributeSetInstance_ID);
                    System.out.println("M_ProductionLine_ID: "+M_ProductionLine_ID);
                    System.out.println("M_CostElement_ID: "+M_CostElement_ID);
                    System.out.println("Amt: "+Amt);
                    System.out.println("Qty: "+Qty);
                    System.out.println("Description"+Description);
                }//fin logs
                //	Delete Unprocessed zero Differences
		String sql = "DELETE M_CostDetail "
			+ "WHERE Processed='N' AND COALESCE(DeltaAmt,0)=0 AND COALESCE(DeltaQty,0)=0"
			+ " AND M_ProductionLine_ID=" + M_ProductionLine_ID
			+ " AND M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID;
		int no = DB.executeUpdate(sql, trxName);
		if (no != 0)
			s_log.config("Deleted #" + no);
		MCostDetail cd = get (as.getCtx(), "M_ProductionLine_ID=? AND M_AttributeSetInstance_ID=?", 
			M_ProductionLine_ID, M_AttributeSetInstance_ID, trxName);
		//
		if (cd == null)		//	createNew
		{
			cd = new MCostDetail (as, AD_Org_ID, 
				M_Product_ID, M_AttributeSetInstance_ID, 
				M_CostElement_ID, 
				Amt, Qty, Description, trxName);
			cd.setM_ProductionLine_ID(M_ProductionLine_ID);
		}
		else
		{
			cd.setDeltaAmt(cd.getAmt().subtract(Amt));
			cd.setDeltaQty(cd.getQty().subtract(Qty));
			if (cd.isDelta())
				cd.setProcessed(false);
			else
				return true;	//	nothing to do
		}
		boolean ok = cd.save();
		if (ok && !cd.isProcessed())
		{
			MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
			if (client.isCostImmediate())
				cd.process();
		}
		s_log.config("(" + ok + ") " + cd);
		return ok;
	}	//	createProduction
	
	
	/**************************************************************************
	 * 	Get Cost Detail
	 *	@param ctx context
	 *	@param whereClause where clause
	 *	@param ID 1st parameter
	 *	@param ID2 2nd parameter
	 *	@param trxName trx
	 *	@return cost detail
	 */
	private static MCostDetail get (Properties ctx, String whereClause, 
		int ID, int ID2, String trxName)
	{
		String sql = "SELECT * FROM M_CostDetail WHERE " + whereClause;
		MCostDetail retValue = null;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, ID);
			pstmt.setInt (2, ID2);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MCostDetail (ctx, rs, trxName);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql + " - " + ID, e);
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
		return retValue;
	}	//	get
	
	/**
	 * 	Process Cost Details for product
	 *	@param product product
	 *	@param trxName
	 */
	public static boolean processProduct (MProduct product, String trxName)
	{
		String sql = "SELECT * FROM M_CostDetail "
			+ "WHERE M_Product_ID=?"
			+ " AND Processed='N' "
			+ "ORDER BY C_AcctSchema_ID, M_CostElement_ID, AD_Org_ID, M_AttributeSetInstance_ID, Created";
		int counterOK = 0;
		int counterError = 0;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, product.getM_Product_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MCostDetail cd = new MCostDetail(product.getCtx(), rs, trxName);
				if (cd.process())	//	saves
					counterOK++;
				else
					counterError++;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
			counterError++;
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
		s_log.config("OK=" + counterOK + ", Errors=" + counterError);
		return counterError == 0;
	}	//	processProduct
	
	/**	Logger	*/
	private static CLogger 	s_log = CLogger.getCLogger (MCostDetail.class);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_CostDetail_ID id
	 *	@param trxName trx
	 */
	public MCostDetail (Properties ctx, int M_CostDetail_ID, String trxName)
	{
		super (ctx, M_CostDetail_ID, trxName);
		if (M_CostDetail_ID == 0)
		{
		//	setC_AcctSchema_ID (0);
		//	setM_Product_ID (0);
			setM_AttributeSetInstance_ID (0);
		//	setC_OrderLine_ID (0);
		//	setM_InOutLine_ID(0);
		//	setC_InvoiceLine_ID (0);
			setProcessed (false);
			setAmt (Env.ZERO);
			setQty (Env.ZERO);
			setIsSOTrx (false);
			setDeltaAmt (Env.ZERO);
			setDeltaQty (Env.ZERO);
		}	
	}	//	MCostDetail

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MCostDetail (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MCostDetail

	/**
	 * 	New Constructor
	 *	@param as accounting schema
	 *	@param AD_Org_ID org
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param M_CostElement_ID optional cost element for Freight
	 *	@param Amt amt
	 *	@param Qty qty
	 *	@param Description optional description
	 */
	public MCostDetail (MAcctSchema as, int AD_Org_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,
		int M_CostElement_ID, BigDecimal Amt, BigDecimal Qty,
		String Description, String trxName)
	{
		this (as.getCtx(), 0, trxName);
		setClientOrg(as.getAD_Client_ID(), AD_Org_ID);
		setC_AcctSchema_ID (as.getC_AcctSchema_ID());
		setM_Product_ID (M_Product_ID);
		setM_AttributeSetInstance_ID (M_AttributeSetInstance_ID);
		//
		setM_CostElement_ID(M_CostElement_ID);
		//
		setAmt (Amt);
		setQty (Qty);
		setDescription(Description);
	}	//	MCostDetail
	
	/**
	 * 	Set Amt
	 *	@param Amt amt
	 */
	public void setAmt (BigDecimal Amt)
	{
		if (isProcessed())
			throw new IllegalStateException("Cannot change Amt - processed");
		if (Amt == null)
			super.setAmt (Env.ZERO);
		else
			super.setAmt (Amt);
	}	//	setAmt
	
	/**
	 * 	Set Qty
	 *	@param Qty qty
	 */
	public void setQty (BigDecimal Qty)
	{
		if (isProcessed())
			throw new IllegalStateException("Cannot change Qty - processed");
		if (Qty == null)
			super.setQty (Env.ZERO);
		else
			super.setQty (Qty);
	}	//	setQty

	/**
	 * 	Is Order
	 *	@return true if order line
	 */
	public boolean isOrder()
	{
		return getC_OrderLine_ID() != 0;
	}	//	isOrder

	/**
	 * 	Is Invoice
	 *	@return true if invoice line
	 */
	public boolean isInvoice()
	{
		return getC_InvoiceLine_ID() != 0;
	}	//	isInvoice

	/**
	 * 	Is Shipment
	 *	@return true if sales order shipment
	 */
	public boolean isShipment()
	{
		return isSOTrx() && getM_InOutLine_ID() != 0;
	}	//	isShipment
	
	/**
	 * 	Is this a Delta Record (previously processed)?
	 *	@return true if delta is not null
	 */
	public boolean isDelta()
	{
		return !(getDeltaAmt().signum() == 0 
			&& getDeltaQty().signum() == 0);
	}	//	isDelta
	
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return true
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		return true;
	}	//	afterSave
	
	/**
	 * 	Before Delete
	 *	@return false if processed
	 */
	protected boolean beforeDelete ()
	{
		return !isProcessed();
	}	//	beforeDelete
	
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MCostDetail[");
		sb.append (get_ID());
		if (getC_OrderLine_ID() != 0)
			sb.append (",C_OrderLine_ID=").append (getC_OrderLine_ID());
		if (getM_InOutLine_ID() != 0)
			sb.append (",M_InOutLine_ID=").append (getM_InOutLine_ID());
		if (getC_InvoiceLine_ID() != 0)
			sb.append (",C_InvoiceLine_ID=").append (getC_InvoiceLine_ID());
		if (getC_ProjectIssue_ID() != 0)
			sb.append (",C_ProjectIssue_ID=").append (getC_ProjectIssue_ID());
		if (getM_MovementLine_ID() != 0)
			sb.append (",M_MovementLine_ID=").append (getM_MovementLine_ID());
		if (getM_InventoryLine_ID() != 0)
			sb.append (",M_InventoryLine_ID=").append (getM_InventoryLine_ID());
		if (getM_ProductionLine_ID() != 0)
			sb.append (",M_ProductionLine_ID=").append (getM_ProductionLine_ID());
		sb.append(",Amt=").append(getAmt())
			.append(",Qty=").append(getQty());
		if (isDelta())
			sb.append(",DeltaAmt=").append(getDeltaAmt())
				.append(",DeltaQty=").append(getDeltaQty());
		sb.append ("]");
		return sb.toString ();
	}	//	toString
	
	
	/**************************************************************************
	 * 	Process Cost Detail Record.
	 * 	The record is saved if processed.
	 *	@return true if processed
	 */
	public synchronized boolean process()
	{
		if (isProcessed())
		{
			log.info("already processed");
			return true;
		}
		boolean ok = false;

		//	get costing level for product
		MAcctSchema as = new MAcctSchema (getCtx(), getC_AcctSchema_ID(), null);
		String CostingLevel = as.getCostingLevel();
		MProduct product = MProduct.get(getCtx(), getM_Product_ID());
		MProductCategoryAcct pca = MProductCategoryAcct.get (getCtx(),
			product.getM_Product_Category_ID(), getC_AcctSchema_ID(), null);	
		if (pca.getCostingLevel() != null)
			CostingLevel = pca.getCostingLevel();
		//	Org Element
		int Org_ID = getAD_Org_ID();
		int M_ASI_ID = getM_AttributeSetInstance_ID();
		if (MAcctSchema.COSTINGLEVEL_Client.equals(CostingLevel))
		{
			Org_ID = 0;
			M_ASI_ID = 0;
		}
		else if (MAcctSchema.COSTINGLEVEL_Organization.equals(CostingLevel))
			M_ASI_ID = 0;
		else if (MAcctSchema.COSTINGLEVEL_BatchLot.equals(CostingLevel))
			Org_ID = 0;

		//	Create Material Cost elements
		if (getM_CostElement_ID() == 0)
		{
			MCostElement[] ces = MCostElement.getCostingMethods(this);
			for (int i = 0; i < ces.length; i++)
			{
				MCostElement ce = ces[i];
				ok = process (as, product, ce, Org_ID, M_ASI_ID);
				if (!ok)
					break;
			}
		}	//	Material Cost elements
		else
		{
			MCostElement ce = MCostElement.get(getCtx(), getM_CostElement_ID());
			ok = process (as, product, ce, Org_ID, M_ASI_ID);
		}
		
		//	Save it
		if (ok)
		{
			setDeltaAmt(null);
			setDeltaQty(null);
			setProcessed(true);
			ok = save();
		}
		log.info(ok + " - " + toString());
		return ok;
	}	//	process
	
	/**
	 * 	Process cost detail for cost record
	 *	@param as accounting schema
	 *	@param product product
	 *	@param ce cost element
	 *	@param Org_ID org - corrected for costing level
	 *	@param M_ASI_ID - asi corrected for costing level
	 *	@return true if cost ok
	 */
	private boolean process (MAcctSchema as, MProduct product, MCostElement ce, 
		int Org_ID, int M_ASI_ID)
	{
		MCost cost = MCost.get(product, M_ASI_ID, as, 
			Org_ID, ce.getM_CostElement_ID());
		if (cost == null)
			cost = new MCost(product, M_ASI_ID, 
				as, Org_ID, ce.getM_CostElement_ID());

		BigDecimal qty = getQty();
		BigDecimal amt = getAmt();
		int precision = as.getCostingPrecision();
		BigDecimal price = amt;
		if (qty.signum() != 0)
			price = amt.divide(qty, precision, BigDecimal.ROUND_HALF_UP);
		
		/** All Costing Methods
		if (ce.isAverageInvoice())
		else if (ce.isAveragePO())
		else if (ce.isFifo())
		else if (ce.isLifo())
		else if (ce.isLastInvoice())
		else if (ce.isLastPOPrice())
		else if (ce.isStandardCosting())
		else if (ce.isUserDefined())
		else if (!ce.isCostingMethod())
		**/
		
		//	*** Purchase Order Detail Record ***
		if (getC_OrderLine_ID() != 0)
		{
			if (ce.isAveragePO())
			{
				cost.setWeightedAverage(amt, qty);
				log.finer("PO - AveragePO - " + cost);
			}
			else if (ce.isLastPOPrice())
			{
				if (qty.signum() != 0)
					cost.setCurrentCostPrice(price);
				else
				{
					BigDecimal cCosts = cost.getCurrentCostPrice().add(amt);
					cost.setCurrentCostPrice(cCosts);
				}
				cost.add(amt, qty);
				log.finer("PO - LastPO - " + cost);
			}
			else if (ce.isUserDefined())
			{
				//	Interface
				log.finer("PO - UserDef - " + cost);
			}
			else if (!ce.isCostingMethod())
			{
				log.finer("PO - " + ce + " - " + cost);
			}
		//	else
		//		log.warning("PO - " + ce + " - " + cost);
		}
		
		//	*** AP Invoice Detail Record ***
		else if (getC_InvoiceLine_ID() != 0)
		{
			if (ce.isAverageInvoice())
			{
				cost.setWeightedAverage(amt, qty);
				log.finer("Inv - AverageInv - " + cost);
			}
			else if (ce.isFifo()
				|| ce.isLifo())
			{
				//	Real ASI - costing level Org
				MCostQueue cq = MCostQueue.get(product, getM_AttributeSetInstance_ID(), 
					as, Org_ID, ce.getM_CostElement_ID(), get_TrxName());
				cq.setCosts(amt, qty, precision);
				cq.save();
				//	Get Costs - costing level Org/ASI
				MCostQueue[] cQueue = MCostQueue.getQueue(product, M_ASI_ID, 
					as, Org_ID, ce, get_TrxName());
				if (cQueue != null && cQueue.length > 0)
					cost.setCurrentCostPrice(cQueue[0].getCurrentCostPrice());
				cost.add(amt, qty);
				log.finer("Inv - FiFo/LiFo - " + cost);
			}
			else if (ce.isLastInvoice())
			{
				if (qty.signum() != 0)
					cost.setCurrentCostPrice(price);
				else
				{
					BigDecimal cCosts = cost.getCurrentCostPrice().add(amt);
					cost.setCurrentCostPrice(cCosts);
				}
				cost.add(amt, qty);
				log.finer("Inv - LastInv - " + cost);
			}
			else if (ce.isStandardCosting())
			{
				if (cost.getCurrentCostPrice().signum() == 0)
				{
					cost.setCurrentCostPrice(price);
					//	seed initial price
					if (cost.getCurrentCostPrice().signum() == 0 
						&& cost.get_ID() == 0)
						cost.setCurrentCostPrice(
							MCost.getSeedCosts(product, M_ASI_ID, 
								as, Org_ID, ce.getCostingMethod(), getC_OrderLine_ID()));
				}
				cost.add(amt, qty);
				log.finer("Inv - Standard - " + cost);
			}
			else if (ce.isUserDefined())
			{
				//	Interface
				cost.add(amt, qty);
				log.finer("Inv - UserDef - " + cost);
			}
			else if (!ce.isCostingMethod())		//	Cost Adjustments
			{
				BigDecimal cCosts = cost.getCurrentCostPrice().add(amt);
				cost.setCurrentCostPrice(cCosts);
				cost.add(amt, qty);
				log.finer("Inv - none - " + cost);
			}
		//	else
		//		log.warning("Inv - " + ce + " - " + cost);
		}
		
		//	*** Qty Adjustment Detail Record ***
		else if (getM_InOutLine_ID() != 0 		//	AR Shipment Detail Record  
			|| getM_MovementLine_ID() != 0 
			|| getM_InventoryLine_ID() != 0
			|| getM_ProductionLine_ID() != 0
			|| getC_ProjectIssue_ID() != 0)
		{
			boolean addition = qty.signum() > 0;
			//
			if (ce.isAverageInvoice())
			{
				if (addition)
					cost.setWeightedAverage(amt, qty);
				else
					cost.setCurrentQty(cost.getCurrentQty().add(qty));
				log.finer("QtyAdjust - AverageInv - " + cost);
			}
			else if (ce.isAveragePO())
			{
				if (addition)
					cost.setWeightedAverage(amt, qty);
				else
					cost.setCurrentQty(cost.getCurrentQty().add(qty));
				log.finer("QtyAdjust - AveragePO - " + cost);
			}
			else if (ce.isFifo() || ce.isLifo())
			{
				if (addition)
				{
					//	Real ASI - costing level Org
					MCostQueue cq = MCostQueue.get(product, getM_AttributeSetInstance_ID(), 
						as, Org_ID, ce.getM_CostElement_ID(), get_TrxName());
					cq.setCosts(amt, qty, precision);
					cq.save();
				}
				else
				{
					//	Adjust Queue - costing level Org/ASI
					MCostQueue.adjustQty(product, M_ASI_ID, 
						as, Org_ID, ce, qty.negate(), get_TrxName());
				}
				//	Get Costs - costing level Org/ASI
				MCostQueue[] cQueue = MCostQueue.getQueue(product, M_ASI_ID, 
					as, Org_ID, ce, get_TrxName());
				if (cQueue != null && cQueue.length > 0)
					cost.setCurrentCostPrice(cQueue[0].getCurrentCostPrice());
				cost.setCurrentQty(cost.getCurrentQty().add(qty));
				log.finer("QtyAdjust - FiFo/Lifo - " + cost);
			}
			else if (ce.isLastInvoice())
			{
				cost.setCurrentQty(cost.getCurrentQty().add(qty));
				log.finer("QtyAdjust - LastInv - " + cost);
			}
			else if (ce.isLastPOPrice())
			{
				cost.setCurrentQty(cost.getCurrentQty().add(qty));
				log.finer("QtyAdjust - LastPO - " + cost);
			}
			else if (ce.isStandardCosting())
			{
				if (addition)
				{
					cost.add(amt, qty);
					//	Initial
					if (cost.getCurrentCostPrice().signum() == 0 
						&& cost.get_ID() == 0)
						cost.setCurrentCostPrice(price);
				}
				else
					cost.setCurrentQty(cost.getCurrentQty().add(qty));
				log.finer("QtyAdjust - Standard - " + cost);
			}
			else if (ce.isUserDefined())
			{
				//	Interface
				if (addition)
					cost.add(amt, qty);
				else
					cost.setCurrentQty(cost.getCurrentQty().add(qty));
				log.finer("QtyAdjust - UserDef - " + cost);
			}
			else if (!ce.isCostingMethod())
			{
			//	Should not happen
				log.finer("QtyAdjust - ?none? - " + cost);
			}
			else
				log.warning("QtyAdjust - " + ce + " - " + cost);
		}
		else	//	unknown or no id
		{
			log.warning("Unknown Type: " + toString());
			return false;
		}
		
		return cost.save();
	}	//	process
	
}	//	MCostDetail
