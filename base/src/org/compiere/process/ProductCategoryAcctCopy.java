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
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Copy Product Catergory Default Accounts
 *	
 *  @author Jorg Janke
 *  @version $Id: ProductCategoryAcctCopy.java,v 1.4 2005/10/26 00:37:42 jjanke Exp $
 */
public class ProductCategoryAcctCopy extends SvrProcess
{
	/** Product Categpory			*/
	private int 		p_M_Product_Category_ID = 0;
	/**	Acct Schema					*/
	private int			p_C_AcctSchema_ID = 0;


	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("M_Product_Category_ID"))
				p_M_Product_Category_ID = para[i].getParameterAsInt();
			else if (name.equals("C_AcctSchema_ID"))
				p_C_AcctSchema_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		log.info("C_AcctSchema_ID=" + p_C_AcctSchema_ID);
		if (p_C_AcctSchema_ID == 0)
			throw new CompiereSystemError("C_AcctSchema_ID=0");
		MAcctSchema as = MAcctSchema.get(getCtx(), p_C_AcctSchema_ID);
		if (as.get_ID() == 0)
			throw new CompiereSystemError("Not Found - C_AcctSchema_ID=" + p_C_AcctSchema_ID);

		//	Update
		String sql = "UPDATE M_Product_Acct pa "
			+ "SET (P_Revenue_Acct,P_Expense_Acct,P_CostAdjustment_Acct,P_InventoryClearing_Acct,P_Asset_Acct,P_COGS_Acct,"
			+ " P_PurchasePriceVariance_Acct,P_InvoicePriceVariance_Acct,"
			+ " P_TradeDiscountRec_Acct,P_TradeDiscountGrant_Acct)="
			 + " (SELECT P_Revenue_Acct,P_Expense_Acct,P_CostAdjustment_Acct,P_InventoryClearing_Acct,P_Asset_Acct,P_COGS_Acct,"
			 + " P_PurchasePriceVariance_Acct,P_InvoicePriceVariance_Acct,"
			 + " P_TradeDiscountRec_Acct,P_TradeDiscountGrant_Acct"
			 + " FROM M_Product_Category_Acct pca"
			 + " WHERE pca.M_Product_Category_ID=" + p_M_Product_Category_ID
			 + " AND pca.C_AcctSchema_ID=" + p_C_AcctSchema_ID
			 + "), Updated=SysDate, UpdatedBy=0 "
			+ "WHERE pa.C_AcctSchema_ID=" + p_C_AcctSchema_ID
			+ " AND EXISTS (SELECT * FROM M_Product p "
				+ "WHERE p.M_Product_ID=pa.M_Product_ID"
				+ " AND p.M_Product_Category_ID=" + p_M_Product_Category_ID + ")";
		int updated = DB.executeUpdate(sql, get_TrxName());
		addLog(0, null, new BigDecimal(updated), "@Updated@");

		//	Insert new Products
		sql = "INSERT INTO M_Product_Acct "
			+ "(M_Product_ID, C_AcctSchema_ID,"
			+ " AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,"
			+ " P_Revenue_Acct, P_Expense_Acct, P_CostAdjustment_Acct, P_InventoryClearing_Acct, P_Asset_Acct, P_CoGs_Acct,"
			+ " P_PurchasePriceVariance_Acct, P_InvoicePriceVariance_Acct,"
			+ " P_TradeDiscountRec_Acct, P_TradeDiscountGrant_Acct) "
			+ "SELECT p.M_Product_ID, acct.C_AcctSchema_ID,"
			+ " p.AD_Client_ID, p.AD_Org_ID, 'Y', SysDate, 0, SysDate, 0,"
			+ " acct.P_Revenue_Acct, acct.P_Expense_Acct, acct.P_CostAdjustment_Acct, acct.P_InventoryClearing_Acct, acct.P_Asset_Acct, acct.P_CoGs_Acct,"
			+ " acct.P_PurchasePriceVariance_Acct, acct.P_InvoicePriceVariance_Acct,"
			+ " acct.P_TradeDiscountRec_Acct, acct.P_TradeDiscountGrant_Acct "
			+ "FROM M_Product p"
			+ " INNER JOIN M_Product_Category_Acct acct ON (acct.M_Product_Category_ID=p.M_Product_Category_ID)"
			+ "WHERE acct.C_AcctSchema_ID=" + p_C_AcctSchema_ID			//	#
			+ " AND p.M_Product_Category_ID=" + p_M_Product_Category_ID	//	#
			+ " AND NOT EXISTS (SELECT * FROM M_Product_Acct pa "
				+ "WHERE pa.M_Product_ID=p.M_Product_ID"
				+ " AND pa.C_AcctSchema_ID=acct.C_AcctSchema_ID)";
		int created = DB.executeUpdate(sql, get_TrxName());
		addLog(0, null, new BigDecimal(created), "@Created@");

		return "@Created@=" + created + ", @Updated@=" + updated;
	}	//	doIt
	
}	//	ProductCategoryAcctCopy
