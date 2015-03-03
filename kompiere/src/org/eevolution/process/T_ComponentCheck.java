/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Portions created by Carlos Ruiz are Copyright (C) 2005 QSS Ltda.
 * Add e-Evolution by Perez Juarez
 * Contributor(s): Carlos Ruiz (globalqss)
 *****************************************************************************/
package org.eevolution.process;


import java.sql.*;
import java.math.*;
import java.util.logging.*;

import org.compiere.util.*;
import org.compiere.process.*;

/**
 * Title:	Inventory Valuation Temporary Table
 *	
 *  @author Carlos Ruiz (globalqss)
 *  @version $Id: T_InventoryValue_Create.java,v 1.0 2005/09/21 20:29:00 globalqss Exp $
 */
public class T_ComponentCheck extends SvrProcess
{

	/** The Parameters		*/
	private int p_MPC_Order_ID;
	
	/** The Record						*/
	private int		p_Record_ID = 0;
	/** The Instance					*/
	private int     p_PInstance_ID;
	
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
			else if (name.equals("MPC_Order_ID"))
				p_MPC_Order_ID = para[i].getParameterAsInt();
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_Record_ID = getRecord_ID();
		p_PInstance_ID = getAD_PInstance_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
		String sqlupd;
		String sqlins;
		int cntu = 0;
		int cnti = 0;

		log.info("Inventory Valuation Temporary Table");
                
                
                log.info("Delete MO");
                String sqldel ="delete from T_ComponentCheck where MPC_Order_ID="+p_MPC_Order_ID;
                cntu = DB.executeUpdate(sqldel,null); 
                System.out.println("*****  Created=" +cntu);
		// Clear
		//	v_ResultStr := 'ClearTable';
		//	DELETE T_InventoryValue WHERE M_Warehouse_ID=p_M_Warehouse_ID;
		//	COMMIT;		
		
		// Insert Products
		sqlins = "INSERT INTO T_ComponentCheck "
		       + "(AD_Client_ID,AD_Org_ID, AD_PInstance_ID, MPC_Order_ID,MPC_Order_BOM_ID,MPC_Order_BOMLine_ID,M_Product_ID,Name, Value,C_UOM_ID  ,QtyOnhand , QtyRequiered "
                        + ",QtyReserved,QtyAvailable ,M_Warehouse_ID  ,QtyBom  ,QtyBatch ,M_Locator_ID ,m_attributesetinstance_id,x ,y ,z) ";
		      // + "SELECT AD_Client_ID,AD_Org_ID," + p_PInstance_ID + "," + p_MPC_Order_ID + ",M_Product_ID "
		       //+ "FROM M_Storage "
		       //+ "WHERE IsStocked='Y'";
//		cnti = DB.executeUpdate(sqlins);
//		if (cnti == 0) {
//			return "@Created@ = 0";
//		}
//		if (cnti < 0) {
//			raiseError("InsertStockedProducts:ERROR", sqlins);
//		}
                int q_M_Product_ID=0;
                int q_M_Warehouse_ID=0;
                int q_MPC_Order_ID=0;
                BigDecimal req1=Env.ZERO;
                BigDecimal requp=Env.ZERO;
		StringBuffer sqlobl = new StringBuffer("Select obl.M_Product_ID, o.M_Warehouse_ID, o.MPC_Order_ID, obl.MPC_Order_BOM_ID, obl.MPC_Order_BOMLine_ID, obl.QtyRequiered,BOMQtyReserved(obl.M_Product_ID,obl.M_Warehouse_ID,0),BOMQtyAvailable(obl.M_Product_ID,obl.M_Warehouse_ID,0),obl.QtyBom ,obl.isQtyPercentage, obl.QtyBatch, p.Value,p.Name, p.C_UOM_ID, obl.AD_Client_ID, obl.AD_Org_ID  from MPC_Order_BOMLine obl INNER Join MPC_Order o ON(o.MPC_Order_ID=obl.MPC_Order_ID) Inner Join M_Product p ON(p.M_Product_ID=obl.M_Product_ID) where obl.MPC_Order_ID=" +p_MPC_Order_ID);
                System.out.println("***** Imprime primer sql" +sqlobl.toString());
                try
                    {
                        
                            PreparedStatement pstmtobl = DB.prepareStatement(sqlobl.toString(),null);
                            //pstmt.setInt(1, AD_Client_ID);
                            ResultSet rsobl = pstmtobl.executeQuery();
                            //
                            while (rsobl.next())
                            {
                                System.out.println("***** entra primer sql" +rsobl.getInt(1));
                                q_M_Product_ID = rsobl.getInt(1);
                                q_M_Warehouse_ID = rsobl.getInt(2);
                                q_MPC_Order_ID=rsobl.getInt(3);
                                req1=rsobl.getBigDecimal(6);
                                int count1=0;
                                StringBuffer sql = new StringBuffer("SELECT s.AD_Client_ID, s.AD_Org_ID,s.M_Product_ID , s.QtyOnHand, s.Updated, p.Name ,p.Value, masi.Description, l.Value, w.Value, w.M_warehouse_ID, p.C_UOM_ID,s.M_Locator_ID, s.M_AttributeSetInstance_ID, l.x,l.y,l.z ");
                             sql.append("  FROM M_Storage s ");
                             sql.append(" INNER JOIN M_Product p ON (s.M_Product_ID = p.M_Product_ID) ");
                             sql.append(" INNER JOIN M_AttributeSetInstance masi ON (masi.M_AttributeSetInstance_ID = s.M_AttributeSetInstance_ID) ");
                             sql.append(" INNER JOIN M_Warehouse w ON (w.M_Warehouse_ID = " +q_M_Warehouse_ID +") ");
                             sql.append(" Inner Join M_Locator l ON(l.M_Warehouse_ID=w.M_Warehouse_ID and s.M_Locator_ID=l.M_Locator_ID) ");
                             sql.append(" WHERE s.M_Product_ID = " +q_M_Product_ID + " and s.QtyOnHand <> 0 Order by masi.Updated " );
                
		log.log(Level.INFO, "TComponentCheck.executeQuery - SQL", sql.toString());                
		//  reset table
		//  Execute
		
                
			PreparedStatement pstmt1 = DB.prepareStatement(sql.toString(),null);
			//pstmt.setInt(1, AD_Client_ID);
			ResultSet rs1 = pstmt1.executeQuery();
			//
			while (rs1.next())
			{
                             if (req1.compareTo(rs1.getBigDecimal(4)) <0)
                            {
                                 if (req1.compareTo(Env.ZERO)<=0)
                                     requp=Env.ZERO;
                                 else
                                     requp=req1;
                                req1=req1.subtract(rs1.getBigDecimal(4));
                            }
                            else
                            {
                                
                                requp=rs1.getBigDecimal(4);
                                req1=req1.subtract(rs1.getBigDecimal(4));
                            }
                             
                             
                              System.out.println("*****  req1=" +req1);
                            String sqlins2="";
                            sqlins2 = sqlins +"Values("+rs1.getInt(1) +"," +rs1.getInt(2) +"," +p_PInstance_ID +"," +q_MPC_Order_ID +"," +rsobl.getInt(4) +"," +rsobl.getInt(5) +"," +q_M_Product_ID +",'" +rs1.getString(6) +"','" +rs1.getString(7) +"',"
                            +rs1.getInt(12) +"," +rs1.getBigDecimal(4) +"," +requp +"," +rsobl.getBigDecimal(7) +"," +rsobl.getBigDecimal(8) +"," +q_M_Warehouse_ID +"," +rsobl.getBigDecimal(9) +"," +rsobl.getBigDecimal(11) +"," +rs1.getInt(13) +"," +rs1.getInt(14) +"," +rs1.getString(15) +"," +rs1.getString(16) +"," +rs1.getString(17)+")";
                            
                            System.out.println("***** inserta lineas " +sqlins2 );
                            cnti = DB.executeUpdate(sqlins2,null);
                       
                            count1++;
                            System.out.println("*****  Created=" +cnti);
                            
                            
                        }
                        rs1.close();
                        pstmt1.close();
                                
                        if (count1==0)
                        {
                            
                        String sqlins2="";
                            sqlins2 = sqlins +"Values("+rsobl.getInt(15) +"," +rsobl.getInt(16) +"," +p_PInstance_ID +"," +q_MPC_Order_ID +"," +rsobl.getInt(4) +"," +rsobl.getInt(5) +"," +q_M_Product_ID +",'" +rsobl.getString(13) +"','" +rsobl.getString(12) +"',"
                            +rsobl.getInt(14) +",0," +rsobl.getBigDecimal(6) +"," +rsobl.getBigDecimal(7) +"," +rsobl.getBigDecimal(8) +"," +q_M_Warehouse_ID +"," +rsobl.getBigDecimal(9) +"," +rsobl.getBigDecimal(11) +",0,0,'" +"','" +"','"  +"')";
                            
                            System.out.println("***** inserta lineas " +sqlins2 );
                            cnti = DB.executeUpdate(sqlins2,null);
                        }
                        else
                        {
                            
                                if (req1.compareTo(Env.ZERO)>0)
                                {
                                   String sqlins2="";
                            sqlins2 = sqlins +"Values("+rsobl.getInt(15) +"," +rsobl.getInt(16) +"," +p_PInstance_ID +"," +q_MPC_Order_ID +"," +rsobl.getInt(4) +"," +rsobl.getInt(5) +"," +q_M_Product_ID +",'" +rsobl.getString(13) +"','" +rsobl.getString(12) +"',"
                            +rsobl.getInt(14) +",0," +req1 +"," +rsobl.getBigDecimal(7) +"," +rsobl.getBigDecimal(8) +"," +q_M_Warehouse_ID +"," +rsobl.getBigDecimal(9) +"," +rsobl.getBigDecimal(11) +",0,0,'" +"','" +"','"  +"')";
                            
                            System.out.println("***** inserta lineas " +sqlins2 );
                            cnti = DB.executeUpdate(sqlins2,null);
                                }
                            
                        }
                        
                        
                            }
                            rsobl.close();
                            pstmtobl.close();
                    }
                    catch(SQLException obl)
                    {
                    }
                int prod =0;
                StringBuffer sqlt = new StringBuffer("Select M_Product_ID, QtyRequiered, QtyOnhand, QtyAvailable,QtyReserved FROM T_ComponentCheck where AD_Instance_ID="+p_PInstance_ID);
                try
                {
                    PreparedStatement pstmtt = DB.prepareStatement(sqlt.toString(),null);
			//pstmt.setInt(1, AD_Client_ID);
			ResultSet rst = pstmtt.executeQuery();
                    while (rst.next())
                    {
                        
                        //req
               
                            
                             
                    }
                        rst.close();
                            pstmtt.close();
                    }
                    catch(SQLException tex)
                    {
                    }
		// Update Constants
        // En Oracle SET DateValue = TRUNC(?) + 0.9993, equivale a sumar 23:59 a la fecha
//		p_DateValue.setHours(23);
//		p_DateValue.setMinutes(59);
//		p_DateValue.setSeconds(0);
//		sqlupd = "UPDATE T_ComponentCheck "
//		       + "SET DateValue = ?, "
//		       +     "M_PriceList_Version_ID = ? , "
//		       +     "C_Currency_ID = ? "
//		       + "WHERE M_Warehouse_ID = ?";
//		PreparedStatement pstmt = DB.prepareStatement(sqlupd, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE, get_TrxName());
////		pstmt.setTimestamp(1, p_DateValue);
////		pstmt.setInt(2, p_M_PriceList_Version_ID);
////		pstmt.setInt(3, p_C_Currency_ID);
////		pstmt.setInt(4, p_M_Warehouse_ID);
//		cntu = pstmt.executeUpdate();
//		if (cntu < 0) {
//			raiseError("UpdateConstants:ERROR", sqlupd);
//		}
//		
//		// Get current QtyOnHand
//		sqlupd = "UPDATE T_InventoryValue "
//	           + "SET QtyOnHand = (SELECT SUM(QtyOnHand) FROM M_Storage s, M_Locator l "
//	           + "WHERE T_InventoryValue.M_Product_ID=s.M_Product_ID "
//	           + "AND l.M_Locator_ID=s.M_Locator_ID "
//	           + "AND l.M_Warehouse_ID=T_InventoryValue.M_Warehouse_ID) "
//	           + "WHERE T_InventoryValue.M_Warehouse_ID = " + p_M_Warehouse_ID;		
//		cntu = DB.executeUpdate(sqlupd);
//		if (cntu < 0) {
//			raiseError("GetQtyOnHand:ERROR", sqlupd);
//		}
//		
//		// Adjust for Valuation Date
//		sqlupd = "UPDATE T_InventoryValue " 
//	           + "SET QtyOnHand = "
//	           + "(SELECT T_InventoryValue.QtyOnHand - NVL(SUM(t.MovementQty), 0) " 
//	           + "FROM M_Transaction t, M_Locator l "
//	           + "WHERE t.M_Product_ID=T_InventoryValue.M_Product_ID " 
//	           // + "AND t.M_AttributeSetInstance_ID=T_InventoryValue.M_AttributeSetInstance_ID "
//	           + "AND t.MovementDate > T_InventoryValue.DateValue "
//	           + "AND t.M_Locator_ID=l.M_Locator_ID "
//	           + "AND l.M_Warehouse_ID=T_InventoryValue.M_Warehouse_ID) "
//	           + "WHERE	T_InventoryValue.M_Warehouse_ID = " + p_M_Warehouse_ID;		
//		cntu = DB.executeUpdate(sqlupd);
//		if (cntu < 0) {
//			raiseError("AdjustQtyOnHand:ERROR", sqlupd);
//		}
//		
//		// Delete Records w/o OnHand Qty
//		sqlupd = "DELETE T_InventoryValue " 
//	           + "WHERE QtyOnHand=0 "
//	           + "OR QtyOnHand IS NULL";		
//		cntu = DB.executeUpdate(sqlupd);
//		if (cntu < 0) {
//			raiseError("DeleteZeroQtyOnHand:ERROR", sqlupd);
//		}
//		
//		// Update Prices
//		sqlupd = "UPDATE T_InventoryValue "
//	           + "SET PricePO = "
//	           + "(SELECT currencyConvert (po.PriceList,po.C_Currency_ID,T_InventoryValue.C_Currency_ID,T_InventoryValue.DateValue, null, T_InventoryValue.AD_Client_ID, T_InventoryValue.AD_Org_ID) "
//	           + "FROM M_Product_PO po WHERE po.M_Product_ID=T_InventoryValue.M_Product_ID "
//	           + "AND po.IsCurrentVendor='Y' AND ROWNUM=1), "
//	           + "PriceList = " 
//	           + "(SELECT currencyConvert(pp.PriceList,pl.C_Currency_ID,T_InventoryValue.C_Currency_ID,T_InventoryValue.DateValue, null, T_InventoryValue.AD_Client_ID, T_InventoryValue.AD_Org_ID) "
//	           + "FROM M_PriceList pl, M_PriceList_Version plv, M_ProductPrice pp "
//	           + "WHERE pp.M_Product_ID=T_InventoryValue.M_Product_ID AND pp.M_PriceList_Version_ID=T_InventoryValue.M_PriceList_Version_ID "
//	           + "AND pp.M_PriceList_Version_ID=plv.M_PriceList_Version_ID "
//	           + "AND plv.M_PriceList_ID=pl.M_PriceList_ID), "
//	           + "PriceStd = " 
//	           + "(SELECT currencyConvert(pp.PriceStd,pl.C_Currency_ID,T_InventoryValue.C_Currency_ID,T_InventoryValue.DateValue, null, T_InventoryValue.AD_Client_ID, T_InventoryValue.AD_Org_ID) "
//	           + "FROM M_PriceList pl, M_PriceList_Version plv, M_ProductPrice pp "
//	           + "WHERE pp.M_Product_ID=T_InventoryValue.M_Product_ID AND pp.M_PriceList_Version_ID=T_InventoryValue.M_PriceList_Version_ID "
//	           + "AND pp.M_PriceList_Version_ID=plv.M_PriceList_Version_ID "
//	           + "AND plv.M_PriceList_ID=pl.M_PriceList_ID), " 
//	           + "PriceLimit = " 
//	           + "(SELECT currencyConvert(pp.PriceLimit,pl.C_Currency_ID,T_InventoryValue.C_Currency_ID,T_InventoryValue.DateValue, null, T_InventoryValue.AD_Client_ID, T_InventoryValue.AD_Org_ID) "
//	           + "FROM M_PriceList pl, M_PriceList_Version plv, M_ProductPrice pp "
//	           + "WHERE pp.M_Product_ID=T_InventoryValue.M_Product_ID AND pp.M_PriceList_Version_ID=T_InventoryValue.M_PriceList_Version_ID "
//	           + "AND pp.M_PriceList_Version_ID=plv.M_PriceList_Version_ID "
//	           + "AND plv.M_PriceList_ID=pl.M_PriceList_ID), "
//	           + "CostStandard = " 
//	           + "(SELECT currencyConvert(pc.CurrentCostPrice,acs.C_Currency_ID,T_InventoryValue.C_Currency_ID,T_InventoryValue.DateValue, null, T_InventoryValue.AD_Client_ID, T_InventoryValue.AD_Org_ID) "
//	           + "FROM AD_ClientInfo ci, C_AcctSchema acs, M_Product_Costing pc "
//	           + "WHERE T_InventoryValue.AD_Client_ID=ci.AD_Client_ID AND ci.C_AcctSchema1_ID=acs.C_AcctSchema_ID "
//	           + "AND acs.C_AcctSchema_ID=pc.C_AcctSchema_ID "
//	           + "AND T_InventoryValue.M_Product_ID=pc.M_Product_ID) "
//	           + "WHERE	T_InventoryValue.M_Warehouse_ID = " + p_M_Warehouse_ID;		
//		cntu = DB.executeUpdate(sqlupd);
//		if (cntu < 0) {
//			raiseError("GetPrices:ERROR", sqlupd);
//		}
//		
//		// Update Values
//		sqlupd = "UPDATE T_InventoryValue " 
//	           + "SET PricePOAmt = QtyOnHand * PricePO, " 
//	           + "PriceListAmt = QtyOnHand * PriceList, " 
//	           + "PriceStdAmt = QtyOnHand * PriceStd, " 
//	           + "PriceLimitAmt = QtyOnHand * PriceLimit, " 
//	           + "CostStandardAmt = QtyOnHand * CostStandard "
//	           + "WHERE	M_Warehouse_ID = " + p_M_Warehouse_ID;
//		cntu = DB.executeUpdate(sqlupd);
//		if (cntu < 0) {
//			raiseError("UpdateValue:ERROR", sqlupd);
//		}
//		
		DB.commit(true, get_TrxName());
		return "@Created@ = " + cnti;		
	}	//	doIt
	
//	private void raiseError(String string, String sql) throws Exception {
//		DB.rollback(false, get_TrxName());
//		String msg = string;
//		ValueNamePair pp = CLogger.retrieveError();
//		if (pp != null)
//			msg = pp.getName() + " - ";
//		msg += sql;
//		throw new CompiereUserError (msg);
//	}
	
}	//	T_ComponentCheck
