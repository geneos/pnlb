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
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.model;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;


/**
 *	Callouts for Invoice Batch
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutInvoiceBatch.java,v 1.4 2005/11/25 21:57:26 jjanke Exp $
 */
public class CalloutInvoiceBatch extends CalloutEngine
{
	/**
	 *	Invoice Batch Line - DateInvoiced.
	 * 		- updates DateAcct
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 */
	public String date (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		if (value == null)
			return "";
		mTab.setValue ("DateAcct", value);
		//
		setDocumentNo(ctx, WindowNo, mTab);
		return "";
	}	//	date

	
	
	/**
	 *	Invoice Batch Line - BPartner.
	 *		- C_BPartner_Location_ID
	 *		- AD_User_ID
	 *		- PaymentRule
	 *		- C_PaymentTerm_ID
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 */
	public String bPartner (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		Integer C_BPartner_ID = (Integer)value;
		if (C_BPartner_ID == null || C_BPartner_ID.intValue() == 0)
			return "";

		String sql = "SELECT p.AD_Language,p.C_PaymentTerm_ID,"
			+ " COALESCE(p.M_PriceList_ID,g.M_PriceList_ID) AS M_PriceList_ID, p.PaymentRule,p.POReference,"
			+ " p.SO_Description,p.IsDiscountPrinted,"
			+ " p.SO_CreditLimit, p.SO_CreditLimit-p.SO_CreditUsed AS CreditAvailable,"
			+ " l.C_BPartner_Location_ID,c.AD_User_ID,"
			+ " COALESCE(p.PO_PriceList_ID,g.PO_PriceList_ID) AS PO_PriceList_ID, p.PaymentRulePO,p.PO_PaymentTerm_ID " 
			+ "FROM C_BPartner p"
			+ " INNER JOIN C_BP_Group g ON (p.C_BP_Group_ID=g.C_BP_Group_ID)"
			+ " LEFT OUTER JOIN C_BPartner_Location l ON (p.C_BPartner_ID=l.C_BPartner_ID AND l.IsBillTo='Y' AND l.IsActive='Y')"
			+ " LEFT OUTER JOIN AD_User c ON (p.C_BPartner_ID=c.C_BPartner_ID) "
			+ "WHERE p.C_BPartner_ID=? AND p.IsActive='Y'";		//	#1

		boolean IsSOTrx = Env.getContext(ctx, WindowNo, "IsSOTrx").equals("Y");
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_BPartner_ID.intValue());
			ResultSet rs = pstmt.executeQuery();
			//
			if (rs.next())
			{
				//	PaymentRule
				String s = rs.getString(IsSOTrx ? "PaymentRule" : "PaymentRulePO");
				if (s != null && s.length() != 0)
				{
					if (Env.getContext(ctx, WindowNo, "DocBaseType").endsWith("C"))	//	Credits are Payment Term
						s = "P";
					else if (IsSOTrx && (s.equals("S") || s.equals("U")))	//	No Check/Transfer for SO_Trx
						s = "P";											//  Payment Term
			//		mTab.setValue("PaymentRule", s);
				}
				//  Payment Term
				Integer ii = new Integer(rs.getInt(IsSOTrx ? "C_PaymentTerm_ID" : "PO_PaymentTerm_ID"));
				if (!rs.wasNull())
					mTab.setValue("C_PaymentTerm_ID", ii);

				//	Location
				int locID = rs.getInt("C_BPartner_Location_ID");
				//	overwritten by InfoBP selection - works only if InfoWindow
				//	was used otherwise creates error (uses last value, may belong to differnt BP)
				if (C_BPartner_ID.toString().equals(Env.getContext(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "C_BPartner_ID")))
				{
					String loc = Env.getContext(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "C_BPartner_Location_ID");
					if (loc.length() > 0)
						locID = Integer.parseInt(loc);
				}
				if (locID == 0)
					mTab.setValue("C_BPartner_Location_ID", null);
				else
					mTab.setValue("C_BPartner_Location_ID", new Integer(locID));

				//	Contact - overwritten by InfoBP selection
				int contID = rs.getInt("AD_User_ID");
				if (C_BPartner_ID.toString().equals(Env.getContext(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "C_BPartner_ID")))
				{
					String cont = Env.getContext(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "AD_User_ID");
					if (cont.length() > 0)
						contID = Integer.parseInt(cont);
				}
				if (contID == 0)
					mTab.setValue("AD_User_ID", null);
				else
					mTab.setValue("AD_User_ID", new Integer(contID));

				//	CreditAvailable
				if (IsSOTrx)
				{
					double CreditLimit = rs.getDouble("SO_CreditLimit");
					if (CreditLimit != 0)
					{
						double CreditAvailable = rs.getDouble("CreditAvailable");
						if (!rs.wasNull() && CreditAvailable < 0)
							mTab.fireDataStatusEEvent("CreditLimitOver",
								DisplayType.getNumberFormat(DisplayType.Amount).format(CreditAvailable),
								false);
					}
				}
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return e.getLocalizedMessage();
		}
		//
		setDocumentNo(ctx, WindowNo, mTab);
		return tax (ctx, WindowNo, mTab, mField, value);
	}	//	bPartner

	/**
	 *	Document Type.
	 *		- called from DocType
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 */
	public String docType (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		setDocumentNo(ctx, WindowNo, mTab);
		return "";
	}	//	docType

	/**
	 *	Set Document No (increase existing)
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 */
	private void setDocumentNo (Properties ctx, int WindowNo, MTab mTab)
	{
		//	Get last line
		int C_InvoiceBatch_ID = Env.getContextAsInt(ctx, WindowNo, "C_InvoiceBatch_ID");
		String sql = "SELECT COALESCE(MAX(C_InvoiceBatchLine_ID),0) FROM C_InvoiceBatchLine WHERE C_InvoiceBatch_ID=?";
		int C_InvoiceBatchLine_ID = DB.getSQLValue(null, sql, C_InvoiceBatch_ID);
		if (C_InvoiceBatchLine_ID == 0)
			return;
		MInvoiceBatchLine last = new MInvoiceBatchLine(Env.getCtx(), C_InvoiceBatchLine_ID, null);
		
		//	Need to Increase when different DocType or BP
		int C_DocType_ID = Env.getContextAsInt(ctx, WindowNo, "C_DocType_ID");
		int C_BPartner_ID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID");
		if (C_DocType_ID == last.getC_DocType_ID()
			&& C_BPartner_ID == last.getC_BPartner_ID())
			return;

		//	New Number
		String oldDocNo = last.getDocumentNo();
		if (oldDocNo == null)
			return;
		int docNo = 0;
		try
		{
			docNo = Integer.parseInt(oldDocNo);
		}
		catch (Exception e)
		{
		}
		if (docNo == 0)
			return;
		String newDocNo = String.valueOf(docNo+1);
		mTab.setValue("DocumentNo", newDocNo);
	}	//	setDocumentNo
	
	/**
	 *	Invoice Batch Line - Charge.
	 * 		- updates PriceEntered from Charge
	 * 	Calles tax
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 */
	public String charge (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		Integer C_Charge_ID = (Integer)value;
		if (C_Charge_ID == null || C_Charge_ID.intValue() == 0)
			return "";

		String sql = "SELECT ChargeAmt FROM C_Charge WHERE C_Charge_ID=?";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_Charge_ID.intValue());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				mTab.setValue ("PriceEntered", rs.getBigDecimal (1));
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return e.getLocalizedMessage();
		}
		//
		return tax (ctx, WindowNo, mTab, mField, value);
	}	//	charge
	
	/**
	 *	Invoice Line - Tax.
	 *		- basis: Charge, BPartner Location
	 *		- sets C_Tax_ID
	 *  Calles Amount
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 */
	public String tax (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		String column = mField.getColumnName();
		if (value == null)
			return "";

		int C_Charge_ID = 0;
		if (column.equals("C_Charge_ID"))
			C_Charge_ID = ((Integer)value).intValue();
		else
			C_Charge_ID = Env.getContextAsInt(ctx, WindowNo, "C_Charge_ID");
		log.fine("C_Charge_ID=" + C_Charge_ID);
		if (C_Charge_ID == 0)
			return amt (ctx, WindowNo, mTab, mField, value);	//

		//	Check Partner Location
		int C_BPartner_Location_ID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_Location_ID");
		if (C_BPartner_Location_ID == 0)
			return amt (ctx, WindowNo, mTab, mField, value);	//
		log.fine("BP_Location=" + C_BPartner_Location_ID);

		//	Dates
		Timestamp billDate = Env.getContextAsDate(ctx, WindowNo, "DateInvoiced");
		log.fine("Bill Date=" + billDate);
		Timestamp shipDate = billDate;
		log.fine("Ship Date=" + shipDate);

		int AD_Org_ID = Env.getContextAsInt(ctx, WindowNo, "AD_Org_ID");
		log.fine("Org=" + AD_Org_ID);

		int M_Warehouse_ID = Env.getContextAsInt(ctx, "#M_Warehouse_ID");
		log.fine("Warehouse=" + M_Warehouse_ID);

		//
		int C_Tax_ID = Tax.get(ctx, 0, C_Charge_ID, billDate, shipDate,
			AD_Org_ID, M_Warehouse_ID, C_BPartner_Location_ID, C_BPartner_Location_ID,
			Env.getContext(ctx, WindowNo, "IsSOTrx").equals("Y"));
		log.info("Tax ID=" + C_Tax_ID);
		//
		if (C_Tax_ID == 0)
			mTab.fireDataStatusEEvent(CLogger.retrieveError());
		else
			mTab.setValue("C_Tax_ID", new Integer(C_Tax_ID));
		//
		return amt (ctx, WindowNo, mTab, mField, value);
	}	//	tax


	/**
	 *	Invoice - Amount.
	 *		- called from QtyEntered, PriceEntered
	 *		- calculates LineNetAmt
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 */
	public String amt (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);
		
		int StdPrecision = 2;		//	temporary

		//	get values
		BigDecimal QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
		BigDecimal PriceEntered = (BigDecimal)mTab.getValue("PriceEntered");
		log.fine("QtyEntered=" + QtyEntered + ", PriceEntered=" + PriceEntered);
		if (QtyEntered == null)
			QtyEntered = Env.ZERO;
		if (PriceEntered == null)
			PriceEntered = Env.ZERO;

		//	Line Net Amt
		BigDecimal LineNetAmt = QtyEntered.multiply(PriceEntered);
		if (LineNetAmt.scale() > StdPrecision)
			LineNetAmt = LineNetAmt.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);

		//	Calculate Tax Amount
		boolean IsSOTrx = "Y".equals(Env.getContext(Env.getCtx(), WindowNo, "IsSOTrx"));
		boolean IsTaxIncluded = "Y".equals(Env.getContext(Env.getCtx(), WindowNo, "IsTaxIncluded"));
		
		BigDecimal TaxAmt = null;
		if (mField.getColumnName().equals("TaxAmt"))
		{
			TaxAmt = (BigDecimal)mTab.getValue("TaxAmt");
		}
		else
		{
			Integer taxID = (Integer)mTab.getValue("C_Tax_ID");
			if (taxID != null)
			{
				int C_Tax_ID = taxID.intValue();
				MTax tax = new MTax (ctx, C_Tax_ID, null);
				TaxAmt = tax.calculateTax(LineNetAmt, IsTaxIncluded, StdPrecision);
				mTab.setValue("TaxAmt", TaxAmt);
			}
		}
		
		//	
		if (IsTaxIncluded)
		{
			mTab.setValue("LineTotalAmt", LineNetAmt);
			mTab.setValue("LineNetAmt", LineNetAmt.subtract(TaxAmt));
		}
		else
		{
			mTab.setValue("LineNetAmt", LineNetAmt);
			mTab.setValue("LineTotalAmt", LineNetAmt.add(TaxAmt));
		}
		setCalloutActive(false);
		return "";
	}	//	amt

	
	
}	//	CalloutInvoiceBatch
