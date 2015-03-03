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
import org.compiere.util.*;


/**
 *  Process to fill T_InventoryValue
 *
 *  @author     Jorg Janke
 *  @version    $Id: InventoryValue.java,v 1.12 2005/04/03 06:46:22 jjanke Exp $
 */
public class InventoryValue extends SvrProcess
{
	/** Price List Used         */
	private int         m_M_PriceList_Version_ID;
	/** Valuation Date          */
	private Timestamp   m_DateValue;
	/** Warehouse               */
	private int         m_M_Warehouse_ID;
	/** Currency                */
	private int         m_C_Currency_ID;

	/**
	 *  Prepare - get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("M_PriceList_Version_ID"))
				m_M_PriceList_Version_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("DateValue"))
				m_DateValue = (Timestamp)para[i].getParameter();
			else if (name.equals("M_Warehouse_ID"))
				m_M_Warehouse_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("C_Currency_ID"))
				m_C_Currency_ID = ((BigDecimal)para[i].getParameter()).intValue();
		}
		if (m_DateValue == null)
			m_DateValue = new Timestamp (System.currentTimeMillis());
	}   //  prepare

	/**
	 *  Perrform process.
	 *  <pre>
	 *  - Fill Table with QtyOnHand for Warehouse and Valuation Date
	 *  - Perform Price Calculations
	 *  </pre>
	 * @return Message
	 * @throws Exception
	 */
	protected String doIt() throws Exception
	{
		//  Delete (just to be sure)
		StringBuffer sql = new StringBuffer ("DELETE T_InventoryValue WHERE M_Warehouse_ID=");
		sql.append(m_M_Warehouse_ID);
		int no = DB.executeUpdate(sql.toString(), get_TrxName());

		//  Insert Products
		sql = new StringBuffer ("INSERT INTO T_InventoryValue "
			+ "(AD_Client_ID,AD_Org_ID,M_Warehouse_ID,M_Product_ID)");
		sql.append("SELECT AD_Client_ID,AD_Org_ID,")
			.append(m_M_Warehouse_ID).append(",M_Product_ID")
			.append(" FROM M_Product WHERE IsStocked='Y'");
		int noPrd = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Inserted=" + noPrd);
		if (noPrd == 0)
			return "No Products";

		//  Update Constants
		sql = new StringBuffer ("UPDATE T_InventoryValue SET ");
		//  YYYY-MM-DD HH24:MI:SS.mmmm  JDBC Timestamp format
		String myDate = m_DateValue.toString();
		sql.append("DateValue=TO_DATE('").append(myDate.substring(0,10))
			.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS'),")
			.append("M_PriceList_Version_ID=").append(m_M_PriceList_Version_ID).append(",")
			.append("C_Currency_ID=").append(m_C_Currency_ID);
		no = DB.executeUpdate(sql.toString(), get_TrxName());

		//  Get current QtyOnHand
		no = DB.executeUpdate ("UPDATE T_InventoryValue iv "
			+ "SET QtyOnHand = (SELECT SUM(QtyOnHand) FROM M_Storage s, M_Locator l"
			+ " WHERE iv.M_Product_ID=s.M_Product_ID"
			+ " AND l.M_Locator_ID=s.M_Locator_ID"
			+ " AND l.M_Warehouse_ID=iv.M_Warehouse_ID)", get_TrxName());

		//  Adjust for Valuation Date
		no = DB.executeUpdate ("UPDATE T_InventoryValue iv SET QtyOnHand = "
			+ "(SELECT iv.QtyOnHand - NVL(SUM(t.MovementQty), 0) FROM M_Transaction t, M_Locator l"
			+ " WHERE t.M_Product_ID=iv.M_Product_ID AND t.MovementDate > iv.DateValue"
			+ " AND t.M_Locator_ID=l.M_Locator_ID AND l.M_Warehouse_ID=iv.M_Warehouse_ID)", get_TrxName());

		//  Delete Recotds w/o OnHand Qty
		int noQty = DB.executeUpdate ("DELETE T_InventoryValue WHERE QtyOnHand=0 OR QtyOnHand IS NULL", get_TrxName());
		log.fine("NoQty=" + noQty);
		if (noPrd == noQty)
			return "No OnHand";

		//  Update Prices
		no = DB.executeUpdate ("UPDATE T_InventoryValue iv "
			+ "SET PricePO = "
				+ "(SELECT currencyConvert (po.PriceList,po.C_Currency_ID,iv.C_Currency_ID,iv.DateValue,null, po.AD_Client_ID,po.AD_Org_ID)"
				+ " FROM M_Product_PO po WHERE po.M_Product_ID=iv.M_Product_ID"
				+ " AND po.IsCurrentVendor='Y' AND RowNum=1), "
			+ "PriceList = "
				+ "(SELECT currencyConvert(pp.PriceList,pl.C_Currency_ID,iv.C_Currency_ID,iv.DateValue,null, pl.AD_Client_ID,pl.AD_Org_ID)"
				+ " FROM M_PriceList pl, M_PriceList_Version plv, M_ProductPrice pp"
				+ " WHERE pp.M_Product_ID=iv.M_Product_ID AND pp.M_PriceList_Version_ID=iv.M_PriceList_Version_ID"
				+ " AND pp.M_PriceList_Version_ID=plv.M_PriceList_Version_ID"
				+ " AND plv.M_PriceList_ID=pl.M_PriceList_ID), "
			+ "PriceStd = "
				+ "(SELECT currencyConvert(pp.PriceStd,pl.C_Currency_ID,iv.C_Currency_ID,iv.DateValue,null, pl.AD_Client_ID,pl.AD_Org_ID)"
				+ " FROM M_PriceList pl, M_PriceList_Version plv, M_ProductPrice pp"
				+ " WHERE pp.M_Product_ID=iv.M_Product_ID AND pp.M_PriceList_Version_ID=iv.M_PriceList_Version_ID"
				+ " AND pp.M_PriceList_Version_ID=plv.M_PriceList_Version_ID"
				+ " AND plv.M_PriceList_ID=pl.M_PriceList_ID), "
			+ "PriceLimit = "
				+ "(SELECT currencyConvert(pp.PriceLimit,pl.C_Currency_ID,iv.C_Currency_ID,iv.DateValue,null, pl.AD_Client_ID,pl.AD_Org_ID)"
				+ " FROM M_PriceList pl, M_PriceList_Version plv, M_ProductPrice pp"
				+ " WHERE pp.M_Product_ID=iv.M_Product_ID AND pp.M_PriceList_Version_ID=iv.M_PriceList_Version_ID"
				+ " AND pp.M_PriceList_Version_ID=plv.M_PriceList_Version_ID"
				+ " AND plv.M_PriceList_ID=pl.M_PriceList_ID), "
			+ "CostStandard = "
				+ "(SELECT currencyConvert(pc.CurrentCostPrice,acs.C_Currency_ID,iv.C_Currency_ID,iv.DateValue,null, pc.AD_Client_ID,pc.AD_Org_ID)"
				+ " FROM AD_ClientInfo ci, C_AcctSchema acs, M_Product_Costing pc"
				+ " WHERE iv.AD_Client_ID=ci.AD_Client_ID AND ci.C_AcctSchema1_ID=acs.C_AcctSchema_ID"
				+ " AND acs.C_AcctSchema_ID=pc.C_AcctSchema_ID"
				+ " AND iv.M_Product_ID=pc.M_Product_ID)", get_TrxName());
		String msg = "";
		if (no == 0)
			msg = "No Prices";

		//  Update Values
		no = DB.executeUpdate("UPDATE T_InventoryValue SET "
			+ "PricePOAmt = QtyOnHand * PricePO, "
			+ "PriceListAmt = QtyOnHand * PriceList, "
			+ "PriceStdAmt = QtyOnHand * PriceStd, "
			+ "PriceLimitAmt = QtyOnHand * PriceLimit, "
			+ "CostStandardAmt = QtyOnHand * CostStandard", get_TrxName());
		log.fine("Valued=" + no);
		//
		return msg;
	}   //  doIt

}   //  InventoryValue
