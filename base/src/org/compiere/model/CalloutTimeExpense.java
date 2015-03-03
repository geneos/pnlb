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
 *	Time & Expense Report Callout 
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutTimeExpense.java,v 1.5 2005/11/06 01:17:27 jjanke Exp $
 */
public class CalloutTimeExpense extends CalloutEngine
{
	/**
	 *	Expense Report Line
	 *		- called from M_Product_ID, S_ResourceAssignment_ID
	 *		- set ExpenseAmt
	 */
	public String product (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		Integer M_Product_ID = (Integer)value;
		if (M_Product_ID == null || M_Product_ID.intValue() == 0)
			return "";
		setCalloutActive(true);
		BigDecimal priceActual = null;

		//	get expense date - or default to today's date
		Timestamp DateExpense = Env.getContextAsDate(ctx, WindowNo, "DateExpense");
		if (DateExpense == null)
			DateExpense = new Timestamp(System.currentTimeMillis());

		String sql = null;
		try
		{
			boolean noPrice = true;

			//	Search Pricelist for current version
			sql = "SELECT bomPriceStd(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceStd,"
				+ "bomPriceList(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceList,"
				+ "bomPriceLimit(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceLimit,"
				+ "p.C_UOM_ID,pv.ValidFrom,pl.C_Currency_ID "
				+ "FROM M_Product p, M_ProductPrice pp, M_Pricelist pl, M_PriceList_Version pv "
				+ "WHERE p.M_Product_ID=pp.M_Product_ID"
				+ " AND pp.M_PriceList_Version_ID=pv.M_PriceList_Version_ID"
				+ " AND pv.M_PriceList_ID=pl.M_PriceList_ID"
				+ " AND pv.IsActive='Y'"
				+ " AND p.M_Product_ID=?"		//	1
				+ " AND pl.M_PriceList_ID=?"	//	2
				+ " ORDER BY pv.ValidFrom DESC";
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, M_Product_ID.intValue());
			pstmt.setInt(2, Env.getContextAsInt(ctx, WindowNo, "M_PriceList_ID"));
			ResultSet rs = pstmt.executeQuery();
			while (rs.next() && noPrice)
			{
				java.sql.Date plDate = rs.getDate("ValidFrom");
				//	we have the price list
				//	if order date is after or equal PriceList validFrom
				if (plDate == null || !DateExpense.before(plDate))
				{
					noPrice = false;
					//	Price
					priceActual = rs.getBigDecimal("PriceStd");
					if (priceActual == null)
						priceActual = rs.getBigDecimal("PriceList");
					if (priceActual == null)
						priceActual = rs.getBigDecimal("PriceLimit");
					//	Currency
					Integer ii = new Integer(rs.getInt("C_Currency_ID"));
					if (!rs.wasNull())
						mTab.setValue("C_Currency_ID", ii);
				}
			}
			rs.close();
			pstmt.close();

			//	no prices yet - look base pricelist
			if (noPrice)
			{
				//	Find if via Base Pricelist
				sql = "SELECT bomPriceStd(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceStd,"
					+ "bomPriceList(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceList,"
					+ "bomPriceLimit(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceLimit,"
					+ "p.C_UOM_ID,pv.ValidFrom,pl.C_Currency_ID "
					+ "FROM M_Product p, M_ProductPrice pp, M_Pricelist pl, M_Pricelist bpl, M_PriceList_Version pv "
					+ "WHERE p.M_Product_ID=pp.M_Product_ID"
					+ " AND pp.M_PriceList_Version_ID=pv.M_PriceList_Version_ID"
					+ " AND pv.M_PriceList_ID=bpl.M_PriceList_ID"
					+ " AND pv.IsActive='Y'"
					+ " AND bpl.M_PriceList_ID=pl.BasePriceList_ID"	//	Base
					+ " AND p.M_Product_ID=?"		//  1
					+ " AND pl.M_PriceList_ID=?"	//	2
					+ " ORDER BY pv.ValidFrom DESC";

				pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt(1, M_Product_ID.intValue());
				pstmt.setInt(2, Env.getContextAsInt(ctx, WindowNo, "M_PriceList_ID"));
				rs = pstmt.executeQuery();
				while (rs.next() && noPrice)
				{
					java.sql.Date plDate = rs.getDate("ValidFrom");
					//	we have the price list
					//	if order date is after or equal PriceList validFrom
					if (plDate == null || !DateExpense.before(plDate))
					{
						noPrice = false;
						//	Price
						priceActual = rs.getBigDecimal("PriceStd");
						if (priceActual == null)
							priceActual = rs.getBigDecimal("PriceList");
						if (priceActual == null)
							priceActual = rs.getBigDecimal("PriceLimit");
						//	Currency
						Integer ii = new Integer(rs.getInt("C_Currency_ID"));
						if (!rs.wasNull())
							mTab.setValue("C_Currency_ID", ii);
					}
				}
				rs.close();
				pstmt.close();
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			setCalloutActive(false);
			return e.getLocalizedMessage();
		}

		//	finish
		setCalloutActive(false);	//	calculate amount
		if (priceActual == null)
			priceActual = Env.ZERO;
		mTab.setValue("ExpenseAmt", priceActual);
		return "";
	}	//	Expense_Product

	/**
	 *	Expense - Amount.
	 *		- called from ExpenseAmt, C_Currency_ID
	 *		- calculates ConvertedAmt
	 */
	public String amount (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		if (isCalloutActive())
			return "";
		setCalloutActive(true);

		//	get values
		BigDecimal ExpenseAmt = (BigDecimal)mTab.getValue("ExpenseAmt");
		Integer C_Currency_From_ID = (Integer)mTab.getValue("C_Currency_ID");
		int C_Currency_To_ID = Env.getContextAsInt(ctx, "$C_Currency_ID");
		Timestamp DateExpense = Env.getContextAsDate(ctx, WindowNo, "DateExpense");
		//
		log.fine("Amt=" + ExpenseAmt + ", C_Currency_ID=" + C_Currency_From_ID);
		//	Converted Amount = Unit price
		BigDecimal ConvertedAmt = ExpenseAmt;
		//	convert if required
		if (!ConvertedAmt.equals(Env.ZERO) && C_Currency_To_ID != C_Currency_From_ID.intValue())
		{
			int AD_Client_ID = Env.getContextAsInt (ctx, WindowNo, "AD_Client_ID");
			int AD_Org_ID = Env.getContextAsInt (ctx, WindowNo, "AD_Org_ID");
			ConvertedAmt = MConversionRate.convert (ctx,
				ConvertedAmt, C_Currency_From_ID.intValue(), C_Currency_To_ID, 
				DateExpense, 0, AD_Client_ID, AD_Org_ID);
		}
		mTab.setValue("ConvertedAmt", ConvertedAmt);
		log.fine("= ConvertedAmt=" + ConvertedAmt);

		setCalloutActive(false);
		return "";
	}	//	Expense_Amount

}	//	CalloutTimeExpense
