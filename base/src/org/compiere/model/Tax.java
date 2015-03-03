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
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Tax Handling
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: Tax.java,v 1.21 2005/12/17 19:55:33 jjanke Exp $
 */
public class Tax
{
	/**	Logger							*/
	static private CLogger			log = CLogger.getCLogger (Tax.class);

	
	/**************************************************************************
	 *	Get Tax ID - converts parameters to call Get Tax.
	 *  <pre>
	 *		M_Product_ID/C_Charge_ID	->	C_TaxCategory_ID
	 *		billDate, shipDate			->	billDate, shipDate
	 *		AD_Org_ID					->	billFromC_Location_ID
	 *		M_Warehouse_ID				->	shipFromC_Location_ID
	 *		billC_BPartner_Location_ID  ->	billToC_Location_ID
	 *		shipC_BPartner_Location_ID 	->	shipToC_Location_ID
	 *
	 *  if IsSOTrx is false, bill and ship are reversed
	 *  </pre>
	 * 	@param ctx	context
	 * 	@param M_Product_ID product
	 * 	@param C_Charge_ID product
	 * 	@param billDate invoice date
	 * 	@param shipDate ship date
	 * 	@param AD_Org_ID org
	 * 	@param M_Warehouse_ID warehouse
	 * 	@param billC_BPartner_Location_ID invoice location
	 * 	@param shipC_BPartner_Location_ID ship location
	 * 	@param IsSOTrx is a sales trx
	 * 	@return C_Tax_ID
	 *  If error it returns 0 and sets error log (TaxCriteriaNotFound)
	 */
	public static int get (Properties ctx, int M_Product_ID, int C_Charge_ID,
		Timestamp billDate, Timestamp shipDate,
		int AD_Org_ID, int M_Warehouse_ID,
		int billC_BPartner_Location_ID, int shipC_BPartner_Location_ID,
		boolean IsSOTrx)
	{
		if (M_Product_ID != 0)
			return getProduct (ctx, M_Product_ID, billDate, shipDate, AD_Org_ID, M_Warehouse_ID,
				billC_BPartner_Location_ID, shipC_BPartner_Location_ID, IsSOTrx);
		else if (C_Charge_ID != 0)
			return getCharge (ctx, C_Charge_ID, billDate, shipDate, AD_Org_ID, M_Warehouse_ID,
				billC_BPartner_Location_ID, shipC_BPartner_Location_ID, IsSOTrx);
		else
			return getExemptTax (ctx, AD_Org_ID);
	}	//	get

	/**
	 *	Get Tax ID - converts parameters to call Get Tax.
	 *  <pre>
	 *		C_Charge_ID					->	C_TaxCategory_ID
	 *		billDate, shipDate			->	billDate, shipDate
	 *		AD_Org_ID					->	billFromC_Location_ID
	 *		M_Warehouse_ID				->	shipFromC_Location_ID
	 *		billC_BPartner_Location_ID  ->	billToC_Location_ID
	 *		shipC_BPartner_Location_ID 	->	shipToC_Location_ID
	 *
	 *  if IsSOTrx is false, bill and ship are reversed
	 *  </pre>
	 * 	@param ctx	context
	 * 	@param C_Charge_ID product
	 * 	@param billDate invoice date
	 * 	@param shipDate ship date
	 * 	@param AD_Org_ID org
	 * 	@param M_Warehouse_ID warehouse
	 * 	@param billC_BPartner_Location_ID invoice location
	 * 	@param shipC_BPartner_Location_ID ship location
	 * 	@param IsSOTrx is a sales trx
	 * 	@return C_Tax_ID
	 *  If error it returns 0 and sets error log (TaxCriteriaNotFound)
	 */
	public static int getCharge (Properties ctx, int C_Charge_ID,
		Timestamp billDate, Timestamp shipDate,
		int AD_Org_ID, int M_Warehouse_ID,
		int billC_BPartner_Location_ID, int shipC_BPartner_Location_ID,
		boolean IsSOTrx)
	{
		if (M_Warehouse_ID == 0)
			M_Warehouse_ID = Env.getContextAsInt(ctx, "M_Warehouse_ID");
		if (M_Warehouse_ID == 0)
		{
			log.warning("No Warehouse - C_Charge_ID=" + C_Charge_ID);
			return 0;
		}
		String variable = "";
		int C_TaxCategory_ID = 0;
		int shipFromC_Location_ID = 0;
		int shipToC_Location_ID = 0;
		int billFromC_Location_ID = 0;
		int billToC_Location_ID = 0;
		String IsTaxExempt = null;

		//	Get all at once
		String sql = "SELECT c.C_TaxCategory_ID, o.C_Location_ID, il.C_Location_ID, b.IsTaxExempt,"
			 + " w.C_Location_ID, sl.C_Location_ID "
			 + "FROM C_Charge c, AD_OrgInfo o,"
			 + " C_BPartner_Location il INNER JOIN C_BPartner b ON (il.C_BPartner_ID=b.C_BPartner_ID),"
			 + " M_Warehouse w, C_BPartner_Location sl "
			 + "WHERE c.C_Charge_ID=?"
			 + " AND o.AD_Org_ID=?"
			 + " AND il.C_BPartner_Location_ID=?"
			 + " AND w.M_Warehouse_ID=?"
			 + " AND sl.C_BPartner_Location_ID=?";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, C_Charge_ID);
			pstmt.setInt (2, AD_Org_ID);
			pstmt.setInt (3, billC_BPartner_Location_ID);
			pstmt.setInt (4, M_Warehouse_ID);
			pstmt.setInt (5, shipC_BPartner_Location_ID);
			ResultSet rs = pstmt.executeQuery ();
			boolean found = false;
			if (rs.next ())
			{
				C_TaxCategory_ID = rs.getInt (1);
				billFromC_Location_ID = rs.getInt (2);
				billToC_Location_ID = rs.getInt (3);
				IsTaxExempt = rs.getString (4);
				shipFromC_Location_ID = rs.getInt (5);
				shipToC_Location_ID = rs.getInt (6);
				found = true;
			}
			rs.close ();
			pstmt.close ();
			//
			if (!found)
			{
				log.warning("Not found for C_Charge_ID=" + C_Charge_ID 
					+ ", AD_Org_ID=" + AD_Org_ID + ", M_Warehouse_ID=" + M_Warehouse_ID
					+ ", C_BPartner_Location_ID=" + billC_BPartner_Location_ID 
					+ "/" + shipC_BPartner_Location_ID);
				return 0;
			}
			else if ("Y".equals (IsTaxExempt))
				return getExemptTax (ctx, AD_Org_ID);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
			return 0;
		}

		//	Reverese for PO
		if (!IsSOTrx)
		{
			int temp = billFromC_Location_ID;
			billFromC_Location_ID = billToC_Location_ID;
			billToC_Location_ID = temp;
			temp = shipFromC_Location_ID;
			shipFromC_Location_ID = shipToC_Location_ID;
			shipToC_Location_ID = temp;
		}
		//
		log.fine("getCharge - C_TaxCategory_ID=" + C_TaxCategory_ID
		  + ", billFromC_Location_ID=" + billFromC_Location_ID
		  + ", billToC_Location_ID=" + billToC_Location_ID
		  + ", shipFromC_Location_ID=" + shipFromC_Location_ID
		  + ", shipToC_Location_ID=" + shipToC_Location_ID);
		return get (ctx, C_TaxCategory_ID, IsSOTrx,
		  shipDate, shipFromC_Location_ID, shipToC_Location_ID,
		  billDate, billFromC_Location_ID, billToC_Location_ID);
	}	//	getCharge


	/**
	 *	Get Tax ID - converts parameters to call Get Tax.
	 *  <pre>
	 *		M_Product_ID				->	C_TaxCategory_ID
	 *		billDate, shipDate			->	billDate, shipDate
	 *		AD_Org_ID					->	billFromC_Location_ID
	 *		M_Warehouse_ID				->	shipFromC_Location_ID
	 *		billC_BPartner_Location_ID  ->	billToC_Location_ID
	 *		shipC_BPartner_Location_ID 	->	shipToC_Location_ID
	 *
	 *  if IsSOTrx is false, bill and ship are reversed
	 *  </pre>
	 * 	@param ctx	context
	 * 	@param M_Product_ID product
	 * 	@param billDate invoice date
	 * 	@param shipDate ship date
	 * 	@param AD_Org_ID org
	 * 	@param M_Warehouse_ID warehouse
	 * 	@param billC_BPartner_Location_ID invoice location
	 * 	@param shipC_BPartner_Location_ID ship location
	 * 	@param IsSOTrx is a sales trx
	 * 	@return C_Tax_ID
	 *  If error it returns 0 and sets error log (TaxCriteriaNotFound)
	 */
	public static int getProduct (Properties ctx, int M_Product_ID,
		Timestamp billDate, Timestamp shipDate,
		int AD_Org_ID, int M_Warehouse_ID,
		int billC_BPartner_Location_ID, int shipC_BPartner_Location_ID,
		boolean IsSOTrx)
	{
		String variable = "";
		int C_TaxCategory_ID = 0;
		int shipFromC_Location_ID = 0;
		int shipToC_Location_ID = 0;
		int billFromC_Location_ID = 0;
		int billToC_Location_ID = 0;
		String IsTaxExempt = null;

		try
		{
			//	Get all at once
			String sql = "SELECT p.C_TaxCategory_ID, o.C_Location_ID, il.C_Location_ID, b.IsTaxExempt,"
				+ " w.C_Location_ID, sl.C_Location_ID "
				+ "FROM M_Product p, AD_OrgInfo o,"
				+ " C_BPartner_Location il INNER JOIN C_BPartner b ON (il.C_BPartner_ID=b.C_BPartner_ID),"
				+ " M_Warehouse w, C_BPartner_Location sl "
				+ "WHERE p.M_Product_ID=?"
				+ " AND o.AD_Org_ID=?"
				+ " AND il.C_BPartner_Location_ID=?"
				+ " AND w.M_Warehouse_ID=?"
				+ " AND sl.C_BPartner_Location_ID=?";
			PreparedStatement  pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, M_Product_ID);
			pstmt.setInt(2, AD_Org_ID);
			pstmt.setInt(3, billC_BPartner_Location_ID);
			pstmt.setInt(4, M_Warehouse_ID);
			pstmt.setInt(5, shipC_BPartner_Location_ID);
			ResultSet rs = pstmt.executeQuery();
			boolean found = false;
			if (rs.next())
			{
				C_TaxCategory_ID = rs.getInt(1);
				billFromC_Location_ID = rs.getInt(2);
				billToC_Location_ID = rs.getInt(3);
				IsTaxExempt = rs.getString(4);
				shipFromC_Location_ID = rs.getInt(5);
				shipToC_Location_ID = rs.getInt(6);
				found = true;
			}
			rs.close();
			pstmt.close();
			//
			if (found && "Y".equals(IsTaxExempt))
			{
				log.fine("getProduct - Business Partner is Tax exempt");
				return getExemptTax(ctx, AD_Org_ID);
			}
			else if (found)
			{
				if (!IsSOTrx)
				{
					int temp = billFromC_Location_ID;
					billFromC_Location_ID = billToC_Location_ID;
					billToC_Location_ID = temp;
					temp = shipFromC_Location_ID;
					shipFromC_Location_ID = shipToC_Location_ID;
					shipToC_Location_ID = temp;
				}
				log.fine("getProduct - C_TaxCategory_ID=" + C_TaxCategory_ID
					+ ", billFromC_Location_ID=" + billFromC_Location_ID
					+ ", billToC_Location_ID=" + billToC_Location_ID
					+ ", shipFromC_Location_ID=" + shipFromC_Location_ID
					+ ", shipToC_Location_ID=" + shipToC_Location_ID);
				return get(ctx, C_TaxCategory_ID, IsSOTrx,
					shipDate, shipFromC_Location_ID, shipToC_Location_ID,
					billDate, billFromC_Location_ID, billToC_Location_ID);
			}

			// ----------------------------------------------------------------

			//	Detail for error isolation

		//	M_Product_ID				->	C_TaxCategory_ID
			sql = "SELECT C_TaxCategory_ID FROM M_Product "
				+ "WHERE M_Product_ID=?";
			variable = "M_Product_ID";
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, M_Product_ID);
			rs = pstmt.executeQuery();
			found = false;
			if (rs.next())
			{
				C_TaxCategory_ID = rs.getInt(1);
				found = true;
			}
			rs.close();
			pstmt.close();
			if (C_TaxCategory_ID == 0)
			{
				log.saveError("TaxCriteriaNotFound", Msg.translate(ctx, variable)
					+ (found ? "" : " (Product=" + M_Product_ID + " not found)"));
				return 0;
			}
			log.fine("getProduct - C_TaxCategory_ID=" + C_TaxCategory_ID);

		//	AD_Org_ID					->	billFromC_Location_ID
			sql = "SELECT C_Location_ID FROM AD_OrgInfo "
				+ "WHERE AD_Org_ID=?";
			variable = "AD_Org_ID";
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Org_ID);
			rs = pstmt.executeQuery();
			found = false;
			if (rs.next())
			{
				billFromC_Location_ID = rs.getInt (1);
				found = true;
			}
			rs.close();
			pstmt.close();
			if (billFromC_Location_ID == 0)
			{
				log.saveError("TaxCriteriaNotFound", Msg.translate(Env.getAD_Language(ctx), variable)
				  + (found ? "" : " (Info/Org=" + AD_Org_ID + " not found)"));
				return 0;
			}

		//	billC_BPartner_Location_ID  ->	billToC_Location_ID
			sql = "SELECT l.C_Location_ID, b.IsTaxExempt "
				+ "FROM C_BPartner_Location l INNER JOIN C_BPartner b ON (l.C_BPartner_ID=b.C_BPartner_ID) "
				+ "WHERE C_BPartner_Location_ID=?";
			variable = "BillTo_ID";
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, billC_BPartner_Location_ID);
			rs = pstmt.executeQuery();
			found = false;
			if (rs.next())
			{
				billToC_Location_ID = rs.getInt(1);
				IsTaxExempt = rs.getString(2);
				found = true;
			}
			rs.close();
			pstmt.close();
			if (billToC_Location_ID == 0)
			{
				log.saveError("TaxCriteriaNotFound", Msg.translate(Env.getAD_Language(ctx), variable)
					+ (found ? "" : " (BPLocation=" + billC_BPartner_Location_ID + " not found)"));
				return 0;
			}
			if ("Y".equals(IsTaxExempt))
				return getExemptTax(ctx, AD_Org_ID);

			//  Reverse for PO
			if (!IsSOTrx)
			{
				int temp = billFromC_Location_ID;
				billFromC_Location_ID = billToC_Location_ID;
				billToC_Location_ID = temp;
			}
			log.fine("getProduct - billFromC_Location_ID = " + billFromC_Location_ID);
			log.fine("getProduct - billToC_Location_ID = " + billToC_Location_ID);

			//-----------------------------------------------------------------

		//	M_Warehouse_ID				->	shipFromC_Location_ID
			sql = "SELECT C_Location_ID FROM M_Warehouse "
				+ "WHERE M_Warehouse_ID=?";
			variable = "M_Warehouse_ID";
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, M_Warehouse_ID);
			rs = pstmt.executeQuery();
			found = false;
			if (rs.next())
			{
				shipFromC_Location_ID = rs.getInt (1);
				found = true;
			}
			rs.close();
			pstmt.close();
			if (shipFromC_Location_ID == 0)
			{
				log.saveError("TaxCriteriaNotFound", Msg.translate(Env.getAD_Language(ctx), variable)
					+ (found ? "" : " (Warehouse=" + M_Warehouse_ID + " not found)"));
				return 0;
			}

		//	shipC_BPartner_Location_ID 	->	shipToC_Location_ID
			sql = "SELECT C_Location_ID FROM C_BPartner_Location "
				+ "WHERE C_BPartner_Location_ID=?";
			variable = "C_BPartner_Location_ID";
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, shipC_BPartner_Location_ID);
			rs = pstmt.executeQuery();
			found = false;
			if (rs.next())
			{
				shipToC_Location_ID = rs.getInt (1);
				found = true;
			}
			rs.close();
			pstmt.close();
			if (shipToC_Location_ID == 0)
			{
				log.saveError("TaxCriteriaNotFound", Msg.translate(Env.getAD_Language(ctx), variable)
					+ (found ? "" : " (BPLocation=" + shipC_BPartner_Location_ID + " not found)"));
				return 0;
			}

			//  Reverse for PO
			if (!IsSOTrx)
			{
				int temp = shipFromC_Location_ID;
				shipFromC_Location_ID = shipToC_Location_ID;
				shipToC_Location_ID = temp;
			}
			log.fine("getProduct - shipFromC_Location_ID = " + shipFromC_Location_ID);
			log.fine("getProduct - shipToC_Location_ID = " + shipToC_Location_ID);
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "getProduct (" + variable + ")", e);
		}

		return get (ctx, C_TaxCategory_ID, IsSOTrx,
			shipDate, shipFromC_Location_ID, shipToC_Location_ID,
			billDate, billFromC_Location_ID, billToC_Location_ID);
	}	//	getProduct

	/**
	 * 	Get Exempt Tax Code
	 * 	@param ctx context
	 * 	@param AD_Org_ID org to find client
	 * 	@return C_Tax_ID
	 */
	private static int getExemptTax (Properties ctx, int AD_Org_ID)
	{
		int C_Tax_ID = 0;
		String sql = "SELECT t.C_Tax_ID "
			+ "FROM C_Tax t"
			+ " INNER JOIN AD_Org o ON (t.AD_Client_ID=o.AD_Client_ID) "
			+ "WHERE t.IsTaxExempt='Y' AND o.AD_Org_ID=? "
			+ "ORDER BY t.Rate DESC";
		boolean found = false;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Org_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				C_Tax_ID = rs.getInt (1);
				found = true;
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "getExemptTax", e);
		}
		log.fine("getExemptTax - TaxExempt=Y - C_Tax_ID=" + C_Tax_ID);
		if (C_Tax_ID == 0)
			log.saveError("TaxCriteriaNotFound", Msg.getMsg(ctx, "TaxNoExemptFound")
				+ (found ? "" : " (Tax/Org=" + AD_Org_ID + " not found)"));
		return C_Tax_ID;
	}	//	getExemptTax

	
	/**************************************************************************
	 *	Get Tax ID (Detail).
	 *  If error return 0 and set error log (TaxNotFound)
	 *	@param C_TaxCategory_ID tax category
	 * 	@param IsSOTrx Sales Order Trx
	 *	@param shipDate ship date (ignored)
	 *	@param shipFromC_Locction_ID ship from (ignored)
	 *	@param shipToC_Location_ID ship to (ignored)
	 *	@param billDate invoice date
	 *	@param billFromC_Location_ID invoice from
	 *	@param billToC_Location_ID invoice to
	 *	@return C_Tax_ID
	 */
	protected static int get (Properties ctx,
		int C_TaxCategory_ID, boolean IsSOTrx,
		Timestamp shipDate, int shipFromC_Locction_ID, int shipToC_Location_ID,
		Timestamp billDate, int billFromC_Location_ID, int billToC_Location_ID)
	{
		//	C_TaxCategory contains CommodityCode
		
		//	API to Tax Vendor comes here

		if (CLogMgt.isLevelFine())
		{
			log.info("get(Detail) - Category=" + C_TaxCategory_ID 
				+ ", SOTrx=" + IsSOTrx);
			log.config("get(Detail) - BillFrom=" + billFromC_Location_ID 
				+ ", BillTo=" + billToC_Location_ID + ", BillDate=" + billDate);
		}

		MTax[] taxes = MTax.getAll (ctx);
		MLocation lFrom = new MLocation (ctx, billFromC_Location_ID, null); 
		MLocation lTo = new MLocation (ctx, billToC_Location_ID, null); 
		log.finer("From=" + lFrom);
		log.finer("To=" + lTo);
		
		for (int i = 0; i < taxes.length; i++)
		{
			MTax tax = taxes[i];
			log.finest(tax.toString());
			//
			if (tax.getC_TaxCategory_ID() != C_TaxCategory_ID
				|| !tax.isActive() 
				|| tax.getParent_Tax_ID() != 0)	//	user parent tax
				continue;
			if (IsSOTrx && MTax.SOPOTYPE_PurchaseTax.equals(tax.getSOPOType()))
				continue;
			if (!IsSOTrx && MTax.SOPOTYPE_SalesTax.equals(tax.getSOPOType()))
				continue;
			
			log.finest("From Country - " + (tax.getC_Country_ID() == lFrom.getC_Country_ID() 
				|| tax.getC_Country_ID() == 0));
			log.finest("From Region - " + (tax.getC_Region_ID() == lFrom.getC_Region_ID() 
				|| tax.getC_Region_ID() == 0));
			log.finest("To Country - " + (tax.getTo_Country_ID() == lTo.getC_Country_ID() 
				|| tax.getTo_Country_ID() == 0));
			log.finest("To Region - " + (tax.getTo_Region_ID() == lTo.getC_Region_ID() 
				|| tax.getTo_Region_ID() == 0));
			log.finest("Date valid - " + (!tax.getValidFrom().after(billDate)));
			
				//	From Country
			if ((tax.getC_Country_ID() == lFrom.getC_Country_ID() 
					|| tax.getC_Country_ID() == 0)
				//	From Region
				&& (tax.getC_Region_ID() == lFrom.getC_Region_ID() 
					|| tax.getC_Region_ID() == 0)
				//	To Country
				&& (tax.getTo_Country_ID() == lTo.getC_Country_ID() 
					|| tax.getTo_Country_ID() == 0)
				//	To Region
				&& (tax.getTo_Region_ID() == lTo.getC_Region_ID() 
					|| tax.getTo_Region_ID() == 0)
				//	Date
				&& !tax.getValidFrom().after(billDate)
				)
			{
				if (!tax.isPostal())
					return tax.getC_Tax_ID();
				//
				MTaxPostal[] postals = tax.getPostals(false);
				for (int j = 0; j < postals.length; j++)
				{
					MTaxPostal postal = postals[j];
					if (postal.isActive()
						//	Postal From is mandatory
						&& postal.getPostal().startsWith(lFrom.getPostal())
						//	Postal To is optional
						&& (postal.getPostal_To() == null 
							|| postal.getPostal_To().startsWith(lTo.getPostal()))
						)
						return tax.getC_Tax_ID();
				}	//	for all postals
			}
		}	//	for all taxes

		//	Default Tax
		for (int i = 0; i < taxes.length; i++)
		{
			MTax tax = taxes[i];
			if (!tax.isDefault() || !tax.isActive()
				|| tax.getParent_Tax_ID() != 0)	//	user parent tax
				continue;
			if (IsSOTrx && MTax.SOPOTYPE_PurchaseTax.equals(tax.getSOPOType()))
				continue;
			if (!IsSOTrx && MTax.SOPOTYPE_SalesTax.equals(tax.getSOPOType()))
				continue;
			log.fine("get (default) - " + tax);
			return tax.getC_Tax_ID();
		}	//	for all taxes
		
		log.saveError("TaxNotFound", "");
		return 0;
	}	//	get
	
}	//	Tax
