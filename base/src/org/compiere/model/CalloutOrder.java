/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express  implied. See the License for
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
import javax.swing.JOptionPane;
import org.compiere.util.*;

/**
 *	Order Callouts.
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutOrder.java,v 1.2 2006/11/21 18:42:23 SIGArg-01 Exp $
 */
public class CalloutOrder extends CalloutEngine
{
	/**	Debug Steps			*/
	private boolean steps = false;

	/**
	 *	Order Header Change - DocType.
	 *		- InvoiceRuld/DeliveryRule/PaymentRule
	 *		- temporary Document
	 *  Context:
	 *  	- DocSubTypeSO
	 *		- HasCharges
	 *	- (re-sets Business Partner info of required)
	 *
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @return Error message or ""
	 */
	public String docType (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		Integer C_DocType_ID = (Integer)value;		//	Actually C_DocTypeTarget_ID
		if (C_DocType_ID == null || C_DocType_ID.intValue() == 0)
			return "";

		//	Re-Create new DocNo, if there is a doc number already
		//	and the existing source used a different Sequence number
		String oldDocNo = (String)mTab.getValue("DocumentNo");
		boolean newDocNo = (oldDocNo == null);
		if (!newDocNo && oldDocNo.startsWith("<") && oldDocNo.endsWith(">"))
			newDocNo = true;
		Integer oldC_DocType_ID = (Integer)mTab.getValue("C_DocType_ID");

		String sql = "SELECT d.DocSubTypeSO,d.HasCharges,'N',"			//	1..3
			+ "d.IsDocNoControlled,s.CurrentNext,s.CurrentNextSys,"     //  4..6
			+ "s.AD_Sequence_ID,d.IsSOTrx "                             //	7..8
			+ "FROM C_DocType d, AD_Sequence s "
			+ "WHERE C_DocType_ID=?"	//	#1
			+ " AND d.DocNoSequence_ID=s.AD_Sequence_ID(+)";
		try
		{
			int AD_Sequence_ID = 0;

			//	Get old AD_SeqNo for comparison
			if (!newDocNo && oldC_DocType_ID.intValue() != 0)
			{
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt(1, oldC_DocType_ID.intValue());
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
					AD_Sequence_ID = rs.getInt(6);
				rs.close();
				pstmt.close();
			}

			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_DocType_ID.intValue());
			ResultSet rs = pstmt.executeQuery();
			String DocSubTypeSO = "";
			boolean IsSOTrx = true;
			if (rs.next())		//	we found document type
			{
				//	Set Context:	Document Sub Type for Sales Orders
				DocSubTypeSO = rs.getString(1);
				if (DocSubTypeSO == null)
					DocSubTypeSO = "--";
				Env.setContext(ctx, WindowNo, "OrderType", DocSubTypeSO);
				//	No Drop Ship other than Standard
				if (!DocSubTypeSO.equals(MOrder.DocSubTypeSO_Standard))
					mTab.setValue ("IsDropShip", "N");
				
				//	Delivery Rule
				if (DocSubTypeSO.equals(MOrder.DocSubTypeSO_POS))
					mTab.setValue ("DeliveryRule", MOrder.DELIVERYRULE_Force);
				else if (DocSubTypeSO.equals(MOrder.DocSubTypeSO_Prepay))
					mTab.setValue ("DeliveryRule", MOrder.DELIVERYRULE_AfterReceipt);
				else
					mTab.setValue ("DeliveryRule", MOrder.DELIVERYRULE_Availability);
				
				//	Invoice Rule
				if (DocSubTypeSO.equals(MOrder.DocSubTypeSO_POS)
					|| DocSubTypeSO.equals(MOrder.DocSubTypeSO_Prepay)
					|| DocSubTypeSO.equals(MOrder.DocSubTypeSO_OnCredit) )
					mTab.setValue ("InvoiceRule", MOrder.INVOICERULE_Immediate);
				else
					mTab.setValue ("InvoiceRule", MOrder.INVOICERULE_AfterDelivery);
				
				//	Payment Rule - POS Order
				if (DocSubTypeSO.equals(MOrder.DocSubTypeSO_POS))
					mTab.setValue("PaymentRule", MOrder.PAYMENTRULE_Cash);
				else
					mTab.setValue("PaymentRule", MOrder.PAYMENTRULE_OnCredit);

				//	IsSOTrx
				if ("N".equals(rs.getString(8)))
					IsSOTrx = false;

				//	Set Context:
				Env.setContext(ctx, WindowNo, "HasCharges", rs.getString(2));
				//	DocumentNo
				if (rs.getString(4).equals("Y"))			//	IsDocNoControlled
				{
					if (!newDocNo && AD_Sequence_ID != rs.getInt(7))
						newDocNo = true;
					if (newDocNo)
						if (Ini.isPropertyBool(Ini.P_COMPIERESYS) && Env.getAD_Client_ID(Env.getCtx()) < 1000000)
							mTab.setValue("DocumentNo", "<" + rs.getString(6) + ">");					
						else
						//begin e-evolution vpj-c 01 MAR 2006 no cambiar folio por distinto tipo de documento Propalma	
						{	
							if(mTab.getRecord_ID() == -1)
							mTab.setValue("DocumentNo", "<" + rs.getString(5) + ">");
						}	
						//end e-evolution vpj-cd 01 MAR 2006
				}
			}
			rs.close();
			pstmt.close();
			//  When BPartner is changed, the Rules are not set if
			//  it is a POS or Credit Order (i.e. defaults from Standard BPartner)
			//  This re-reads the Rules and applies them.
			if (DocSubTypeSO.equals(MOrder.DocSubTypeSO_POS) 
				|| DocSubTypeSO.equals(MOrder.DocSubTypeSO_Prepay))    //  not for POS/PrePay
				;
			else
			{
				sql = "SELECT PaymentRule,C_PaymentTerm_ID,"            //  1..2
					+ "InvoiceRule,DeliveryRule,"                       //  3..4
					+ "FreightCostRule,DeliveryViaRule, "               //  5..6
					+ "PaymentRulePO,PO_PaymentTerm_ID "
					+ "FROM C_BPartner "
					+ "WHERE C_BPartner_ID=?";		//	#1
				pstmt = DB.prepareStatement(sql, null);
				int C_BPartner_ID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID");
				pstmt.setInt(1, C_BPartner_ID);
				//
				rs = pstmt.executeQuery();
				if (rs.next())
				{
					//	PaymentRule
					String s = rs.getString(IsSOTrx ? "PaymentRule" : "PaymentRulePO");
					if (s != null && s.length() != 0)
					{
						if (IsSOTrx && (s.equals("B") || s.equals("S") || s.equals("U")))	//	No Cash/Check/Transfer for SO_Trx
							s = "P";										//  Payment Term
						if (!IsSOTrx && (s.equals("B")))					//	No Cash for PO_Trx
							s = "P";										//  Payment Term
						mTab.setValue("PaymentRule", s);
					}
					//	Payment Term
					Integer ii =new Integer(rs.getInt(IsSOTrx ? "C_PaymentTerm_ID" : "PO_PaymentTerm_ID"));
					if (!rs.wasNull())
						mTab.setValue("C_PaymentTerm_ID", ii);
					//	InvoiceRule
					s = rs.getString(3);
					if (s != null && s.length() != 0)
						mTab.setValue("InvoiceRule", s);
					//	DeliveryRule
					s = rs.getString(4);
					if (s != null && s.length() != 0)
						mTab.setValue("DeliveryRule", s);
					//	FreightCostRule
					s = rs.getString(5);
					if (s != null && s.length() != 0)
						mTab.setValue("FreightCostRule", s);
					//	DeliveryViaRule
					s = rs.getString(6);
					if (s != null && s.length() != 0)
						mTab.setValue("DeliveryViaRule", s);
				}
				rs.close();
				pstmt.close();
			}   //  re-read customer rules
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return e.getLocalizedMessage();
		}

		return "";
	}	//	docType


	public String billLocation (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		if (isCalloutActive())
			return "";
		/*
		 * 21/10/2010 - Mariano modificado Bill_BPartner_ID = LOCATION_BILL_ID -> Bill_BPartner_ID = C_Location_Id
		 */
		boolean IsSOTrx = "Y".equals(Env.getContext(ctx, WindowNo, "IsSOTrx"));
		Integer Bill_BPartner_ID = (Integer)mTab.getValue("C_Location_ID");
		Integer C_BPartner_ID = (Integer)mTab.getValue("C_BPartner_ID");
		if (Bill_BPartner_ID == null || Bill_BPartner_ID.intValue() == 0 || C_BPartner_ID == null || C_BPartner_ID.intValue() == 0 || !IsSOTrx)
			return "";
		setCalloutActive(true);
		
		String sql =  " SELECT SellResp_ID "
					+ " FROM C_BPartner_Location "
					+ " WHERE C_Location_ID = " + Bill_BPartner_ID + " AND C_BPartner_ID = " + C_BPartner_ID; 
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				mTab.setValue("SalesRep_ID", rs.getInt(1));

			rs.close();
			pstmt.close();
		}
		catch (SQLException e){	}
		
		setCalloutActive(false);
		return "";
	}	//	billLocation
	
	/**
	 *	Order Header - BPartner.
	 *		- M_PriceList_ID (+ Context)
	 *		- C_BPartner_Location_ID
	 *		- Bill_BPartner_ID/Bill_Location_ID
	 *		- AD_User_ID
	 *		- POReference
	 *		- SO_Description
	 *		- IsDiscountPrinted
	 *		- InvoiceRule/DeliveryRule/PaymentRule/FreightCost/DeliveryViaRule
	 *		- C_PaymentTerm_ID
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @return Error message or ""
	 */
	public String bPartner (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		if (isCalloutActive())
			return "";
		
		Integer C_BPartner_ID = (Integer)value;
		if (C_BPartner_ID == null || C_BPartner_ID.intValue() == 0)
			return "";
		setCalloutActive(true);

		boolean IsSOTrx = "Y".equals(Env.getContext(ctx, WindowNo, "IsSOTrx"));
		MBPartner partner = new MBPartner(ctx,C_BPartner_ID,null);
		
		if (IsSOTrx && !partner.isCustomer() && C_BPartner_ID!=1002032)	{ // USUARIO ESTANDAR
			setCalloutActive(false);
			mTab.setValue("C_BPartner_ID", null);
			return "ERROR - No ha ingresado un Cliente.";
		}
		
		if (!IsSOTrx && !partner.isVendor())	{
			setCalloutActive(false);
			mTab.setValue("C_BPartner_ID", null);
			return "ERROR - No ha ingresado un Proveedor.";
		}
		
		String sql = "SELECT p.AD_Language,p.C_PaymentTerm_ID,"
			+ " COALESCE(p.M_PriceList_ID,g.M_PriceList_ID) AS M_PriceList_ID, p.PaymentRule,p.POReference,"
			+ " p.SO_Description,p.IsDiscountPrinted,"
			+ " p.InvoiceRule,p.DeliveryRule,p.FreightCostRule,DeliveryViaRule,"
			+ " p.SO_CreditLimit, p.SO_CreditLimit-p.SO_CreditUsed AS CreditAvailable,"
			+ " locship.C_Location_ID AS ShipLocation,c.AD_User_ID,"
			+ " COALESCE(p.PO_PriceList_ID,g.PO_PriceList_ID) AS PO_PriceList_ID, p.PaymentRulePO,p.PO_PaymentTerm_ID," 
			+ " locbill.C_Location_ID AS Bill_Location_ID, p.SOCreditStatus "
			+ "FROM C_BPartner p"
			+ " INNER JOIN C_BP_Group g ON (p.C_BP_Group_ID=g.C_BP_Group_ID)"			
			+ " LEFT OUTER JOIN C_BPartner_Location lbill ON (p.C_BPartner_ID=lbill.C_BPartner_ID AND lbill.IsBillTo='Y' AND lbill.IsActive='Y')"
			+ " LEFT OUTER JOIN C_BPartner_Location lship ON (p.C_BPartner_ID=lship.C_BPartner_ID AND lship.IsShipTo='Y' AND lship.IsActive='Y')"
			+ " LEFT OUTER JOIN C_Location locbill ON (lbill.C_Location_ID=locbill.C_Location_ID)"
			+ " LEFT OUTER JOIN C_Location locship ON (lship.C_Location_ID=locship.C_Location_ID)"
			+ " LEFT OUTER JOIN AD_User c ON (p.C_BPartner_ID=c.C_BPartner_ID) "
			+ "WHERE p.C_BPartner_ID=? AND p.IsActive='Y'";		//	#1

		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_BPartner_ID.intValue());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				//	PriceList (indirect: IsTaxIncluded & Currency)
				Integer ii = new Integer(rs.getInt(IsSOTrx ? "M_PriceList_ID" : "PO_PriceList_ID"));
				if (!rs.wasNull())
					mTab.setValue("M_PriceList_ID", ii);
				else
				{	//	get default PriceList
					int i = Env.getContextAsInt(ctx, "#M_PriceList_ID");
					if (i != 0)
						mTab.setValue("M_PriceList_ID", new Integer(i));
				}

				//	Bill-To
				mTab.setValue("LOCATION_BILL_ID", C_BPartner_ID);
				int bill_Location_ID = rs.getInt("Bill_Location_ID");
				if (bill_Location_ID == 0)
					mTab.setValue("LOCATION_BILL_ID", null);
				else
					mTab.setValue("LOCATION_BILL_ID", new Integer(bill_Location_ID));
				// Ship-To Location
				int shipTo_ID = rs.getInt("ShipLocation");
				
				if (shipTo_ID == 0)
					mTab.setValue("C_Location_ID", null);
				else
					mTab.setValue("C_Location_ID", new Integer(shipTo_ID));

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
				{
					mTab.setValue("AD_User_ID", new Integer(contID));
					mTab.setValue("Bill_User_ID", new Integer(contID));
				}

				//	CreditAvailable 
				if (IsSOTrx)
				{
					double CreditLimit = rs.getDouble("SO_CreditLimit");
					String SOCreditStatus = rs.getString("SOCreditStatus");
					if (CreditLimit != 0)
					{
						double CreditAvailable = rs.getDouble("CreditAvailable");
						if (!rs.wasNull() && CreditAvailable < 0)
							mTab.fireDataStatusEEvent("CreditLimitOver",
								DisplayType.getNumberFormat(DisplayType.Amount).format(CreditAvailable),
								false);
					}
				}

				//	PO Reference
				String s = rs.getString("POReference");
				if (s != null && s.length() != 0)
					mTab.setValue("POReference", s);
				else
					mTab.setValue("POReference", null);
				//	SO Description
				s = rs.getString("SO_Description");
				if (s != null && s.trim().length() != 0)
					mTab.setValue("Description", s);
				//	IsDiscountPrinted
				s = rs.getString("IsDiscountPrinted");
				if (s != null && s.length() != 0)
					mTab.setValue("IsDiscountPrinted", s);
				else
					mTab.setValue("IsDiscountPrinted", "N");

				//	Defaults, if not Walkin Receipt or Walkin Invoice
				
				mTab.setValue("InvoiceRule", MOrder.INVOICERULE_AfterDelivery);
				mTab.setValue("DeliveryRule", MOrder.DELIVERYRULE_Availability);
				mTab.setValue("PaymentRule", MOrder.PAYMENTRULE_OnCredit);
				
				//	PaymentRule
				s = rs.getString(IsSOTrx ? "PaymentRule" : "PaymentRulePO");
				if (s != null && s.length() != 0)
				{
					if (s.equals("B"))				//	No Cache in Non POS
						s = "P";
					if (IsSOTrx && (s.equals("S") || s.equals("U")))	//	No Check/Transfer for SO_Trx
						s = "P";										//  Payment Term
					mTab.setValue("PaymentRule", s);
				}
				//	Payment Term
				ii = new Integer(rs.getInt(IsSOTrx ? "C_PaymentTerm_ID" : "PO_PaymentTerm_ID"));
				if (!rs.wasNull())
					mTab.setValue("C_PaymentTerm_ID", ii);
				//	InvoiceRule
				s = rs.getString("InvoiceRule");
				if (s != null && s.length() != 0)
					mTab.setValue("InvoiceRule", s);
				//	DeliveryRule
				s = rs.getString("DeliveryRule");
				if (s != null && s.length() != 0)
					mTab.setValue("DeliveryRule", s);
				//	FreightCostRule
				s = rs.getString("FreightCostRule");
				if (s != null && s.length() != 0)
					mTab.setValue("FreightCostRule", s);
				//	DeliveryViaRule
				s = rs.getString("DeliveryViaRule");
				if (s != null && s.length() != 0)
					mTab.setValue("DeliveryViaRule", s);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			setCalloutActive(false);
			return e.getLocalizedMessage();
		}
		setCalloutActive(false);
		return "";
	}	//	bPartner

	/**
	 *	Order Header - Invoice BPartner.
	 *		- M_PriceList_ID (+ Context)
	 *		- Bill_Location_ID
	 *		- Bill_User_ID
	 *		- POReference
	 *		- SO_Description
	 *		- IsDiscountPrinted
	 *		- InvoiceRule/PaymentRule
	 *		- C_PaymentTerm_ID
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @return Error message or ""
	 */
	public String bPartnerBill (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		if (isCalloutActive())
			return "";
		Integer bill_BPartner_ID = (Integer)value;
		if (bill_BPartner_ID == null || bill_BPartner_ID.intValue() == 0)
			return "";

		String sql = "SELECT p.AD_Language,p.C_PaymentTerm_ID,"
			+ "p.M_PriceList_ID,p.PaymentRule,p.POReference,"
			+ "p.SO_Description,p.IsDiscountPrinted,"
			+ "p.InvoiceRule,p.DeliveryRule,p.FreightCostRule,DeliveryViaRule,"
			+ "p.SO_CreditLimit, p.SO_CreditLimit-p.SO_CreditUsed AS CreditAvailable,"
			+ "c.AD_User_ID,"
			+ "p.PO_PriceList_ID, p.PaymentRulePO, p.PO_PaymentTerm_ID,"
			+ "locbill.C_Location_ID AS Bill_Location_ID "
			+ "FROM C_BPartner p"
			+ " LEFT OUTER JOIN C_BPartner_Location lbill ON (p.C_BPartner_ID=lbill.C_BPartner_ID AND lbill.IsBillTo='Y' AND lbill.IsActive='Y')"
			+ " LEFT OUTER JOIN C_Location locbill ON (lbill.C_Location_ID=locbill.C_Location_ID)"
			+ " LEFT OUTER JOIN AD_User c ON (p.C_BPartner_ID=c.C_BPartner_ID) "
			+ "WHERE p.C_BPartner_ID=? AND p.IsActive='Y'";		//	#1

		boolean IsSOTrx = "Y".equals(Env.getContext(ctx, WindowNo, "IsSOTrx"));

		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, bill_BPartner_ID.intValue());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				//	PriceList (indirect: IsTaxIncluded & Currency)
				Integer ii = new Integer(rs.getInt(IsSOTrx ? "M_PriceList_ID" : "PO_PriceList_ID"));
				if (!rs.wasNull())
					mTab.setValue("M_PriceList_ID", ii);
				else
				{	//	get default PriceList
					int i = Env.getContextAsInt(ctx, "#M_PriceList_ID");
					if (i != 0)
						mTab.setValue("M_PriceList_ID", new Integer(i));
				}

				int bill_Location_ID = rs.getInt("Bill_Location_ID");

				if (bill_Location_ID == 0)
					mTab.setValue("LOCATION_BILL_ID", null);
				else
					mTab.setValue("LOCATION_BILL_ID", new Integer(bill_Location_ID));

				//	Contact - overwritten by InfoBP selection
				int contID = rs.getInt("AD_User_ID");
				if (bill_BPartner_ID.toString().equals(Env.getContext(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "C_BPartner_ID")))
				{
					String cont = Env.getContext(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "AD_User_ID");
					if (cont.length() > 0)
						contID = Integer.parseInt(cont);
				}
				if (contID == 0)
					mTab.setValue("Bill_User_ID", null);
				else
					mTab.setValue("Bill_User_ID", new Integer(contID));

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

				//	PO Reference
				String s = rs.getString("POReference");
				if (s != null && s.length() != 0)
					mTab.setValue("POReference", s);
				else
					mTab.setValue("POReference", null);
				//	SO Description
				s = rs.getString("SO_Description");
				if (s != null && s.trim().length() != 0)
					mTab.setValue("Description", s);
				//	IsDiscountPrinted
				s = rs.getString("IsDiscountPrinted");
				if (s != null && s.length() != 0)
					mTab.setValue("IsDiscountPrinted", s);
				else
					mTab.setValue("IsDiscountPrinted", "N");

				//	Defaults, if not Walkin Receipt or Walkin Invoice
				String OrderType = Env.getContext(ctx, WindowNo, "OrderType");
				mTab.setValue("InvoiceRule", MOrder.INVOICERULE_AfterDelivery);
				mTab.setValue("PaymentRule", MOrder.PAYMENTRULE_OnCredit);
				if (OrderType.equals(MOrder.DocSubTypeSO_Prepay))
					mTab.setValue("InvoiceRule", MOrder.INVOICERULE_Immediate);
				else if (OrderType.equals(MOrder.DocSubTypeSO_POS))	//  for POS
					mTab.setValue("PaymentRule", MOrder.PAYMENTRULE_Cash);
				else
				{
					//	PaymentRule
					s = rs.getString(IsSOTrx ? "PaymentRule" : "PaymentRulePO");
					if (s != null && s.length() != 0)
					{
						if (s.equals("B"))				//	No Cache in Non POS
							s = "P";
						if (IsSOTrx && (s.equals("S") || s.equals("U")))	//	No Check/Transfer for SO_Trx
							s = "P";										//  Payment Term
						mTab.setValue("PaymentRule", s);
					}
					//	Payment Term
					ii = new Integer(rs.getInt(IsSOTrx ? "C_PaymentTerm_ID" : "PO_PaymentTerm_ID"));
					if (!rs.wasNull())
						mTab.setValue("C_PaymentTerm_ID", ii);
					//	InvoiceRule
					s = rs.getString("InvoiceRule");
					if (s != null && s.length() != 0)
						mTab.setValue("InvoiceRule", s);
				}
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "bPartnerBill", e);
			return e.getLocalizedMessage();
		}

		return "";
	}	//	bPartnerBill


	/**
	 *	Order Header - PriceList.
	 *	(used also in Invoice)
	 *		- C_Currency_ID
	 *		- IsTaxIncluded
	 *	Window Context:
	 *		- EnforcePriceLimit
	 *		- StdPrecision
	 *		- M_PriceList_Version_ID
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 */
	public String priceList (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		Integer M_PriceList_ID = (Integer)value;
		if (M_PriceList_ID == null || M_PriceList_ID.intValue()== 0)
			return "";
		if (steps) log.warning("init");

		String sql = "SELECT pl.IsTaxIncluded,pl.EnforcePriceLimit,pl.C_Currency_ID,c.StdPrecision,"
			+ "plv.M_PriceList_Version_ID,plv.ValidFrom "
			+ "FROM M_PriceList pl,C_Currency c,M_PriceList_Version plv "
			+ "WHERE pl.C_Currency_ID=c.C_Currency_ID"
			+ " AND pl.M_PriceList_ID=plv.M_PriceList_ID"
			+ " AND pl.M_PriceList_ID=? "						//	1
			+ "ORDER BY plv.ValidFrom DESC";
		//	Use newest price list - may not be future
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, M_PriceList_ID.intValue());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				//	Tax Included
				mTab.setValue("IsTaxIncluded", new Boolean("Y".equals(rs.getString(1))));
				//	Price Limit Enforce
				Env.setContext(ctx, WindowNo, "EnforcePriceLimit", rs.getString(2));
				//	Currency
				Integer ii = new Integer(rs.getInt(3));
				mTab.setValue("C_Currency_ID", ii);
				//	PriceList Version
				Env.setContext(ctx, WindowNo, "M_PriceList_Version_ID", rs.getInt(5));
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return e.getLocalizedMessage();
		}
		if (steps) log.warning("fini");

		return "";
	}	//	priceList

	
	/*************************************************************************
	 *	Order Line - Product.
	 *		- reset C_Charge_ID / M_AttributeSetInstance_ID
	 *		- PriceList, PriceStd, PriceLimit, C_Currency_ID, EnforcePriceLimit
	 *		- UOM
	 *	Calls Tax
	 *
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 */
	public String product (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		Integer M_Product_ID = (Integer)value;
		if (M_Product_ID == null || M_Product_ID.intValue() == 0)
			return "";
		if (steps) log.warning("init");
		setCalloutActive(true);
		//
		mTab.setValue("C_Charge_ID", null);
		//	Set Attribute
		if (Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_Product_ID") == M_Product_ID.intValue()
			&& Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_AttributeSetInstance_ID") != 0)
			mTab.setValue("M_AttributeSetInstance_ID", new Integer(Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_AttributeSetInstance_ID")));
		else
			mTab.setValue("M_AttributeSetInstance_ID", null);
			
		/*****	Price Calculation see also qty	****/
		int C_BPartner_ID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID");
		BigDecimal Qty = (BigDecimal)mTab.getValue("QtyOrdered");
		boolean IsSOTrx = Env.getContext(ctx, WindowNo, "IsSOTrx").equals("Y");
		MProductPricing pp = new MProductPricing (M_Product_ID.intValue(), C_BPartner_ID, Qty, IsSOTrx);
		//
		int M_PriceList_ID = Env.getContextAsInt(ctx, WindowNo, "M_PriceList_ID");
		pp.setM_PriceList_ID(M_PriceList_ID);
		/** PLV is only accurate if PL selected in header */
		int M_PriceList_Version_ID = Env.getContextAsInt(ctx, WindowNo, "M_PriceList_Version_ID");
		pp.setM_PriceList_Version_ID(M_PriceList_Version_ID); 
		Timestamp orderDate = (Timestamp)mTab.getValue("DateOrdered");
		pp.setPriceDate(orderDate);
		//		
                
		mTab.setValue("PriceList", pp.getPriceList());
		mTab.setValue("PriceLimit", pp.getPriceLimit());
		mTab.setValue("PriceActual", pp.getPriceStd());
		mTab.setValue("PriceEntered", pp.getPriceStd());
		mTab.setValue("C_Currency_ID", new Integer(pp.getC_Currency_ID()));
		mTab.setValue("Discount", pp.getDiscount());
		mTab.setValue("C_UOM_ID", new Integer(pp.getC_UOM_ID()));
		mTab.setValue("QtyOrdered", mTab.getValue("QtyEntered"));
                
                MProduct product = MProduct.get (ctx, M_Product_ID.intValue());
                if (product.getDescription() != null){
                    if (mTab.getValue("Description") != null)
                        mTab.setValue("Description", mTab.getValue("Description")+" | "+product.getDescription());
                    else
                        mTab.setValue("Description", product.getDescription());

                }
                Env.setContext(ctx, WindowNo, "EnforcePriceLimit", pp.isEnforcePriceLimit() ? "Y" : "N");
		Env.setContext(ctx, WindowNo, "DiscountSchema", pp.isDiscountSchema() ? "Y" : "N");
		
		//	Check/Update Warehouse Setting
		//	int M_Warehouse_ID = Env.getContextAsInt(ctx, Env.WINDOW_INFO, "M_Warehouse_ID");
		//	Integer wh = (Integer)mTab.getValue("M_Warehouse_ID");
		//	if (wh.intValue() != M_Warehouse_ID)
		//	{
		//		mTab.setValue("M_Warehouse_ID", new Integer(M_Warehouse_ID));
		//		ADialog.warn(,WindowNo, "WarehouseChanged");
		//	}
		
		int C_Location_ID = Env.getContextAsInt(ctx, WindowNo, "LOCATION_BILL_ID");
		MLocation location = new MLocation(Env.getCtx(),C_Location_ID,null);
		mTab.setValue("C_Tax_ID", MProductTax.getCondicionIVA(M_Product_ID.intValue(), C_BPartner_ID, location.getC_Jurisdiccion_ID()));
		
		if ("Y".equals(Env.getContext(ctx, WindowNo, "IsSOTrx")))
		{
			if (product.isStocked())
			{
				int M_Warehouse_ID = Env.getContextAsInt(ctx, WindowNo, "M_Warehouse_ID");
				BigDecimal available = MStorage.getQtyAvailable
					(M_Warehouse_ID, M_Product_ID.intValue(), null);
				if (available == null)
					mTab.fireDataStatusEEvent ("NoQtyAvailable", "0", true);
				else if (available.compareTo(Env.ZERO) <= 0)
					mTab.fireDataStatusEEvent ("NoQtyAvailable", available.toString(), true);
			}
		}
		//
		setCalloutActive(false);
		if (steps) log.warning("fini");
		return tax (ctx, WindowNo, mTab, mField, value);
	}	//	product

	/**
	 *	Order Line - Charge.
	 * 		- updates PriceActual from Charge
	 * 		- sets PriceLimit, PriceList to zero
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
		//	No Product defined
		if (mTab.getValue("M_Product_ID") != null)
		{
			mTab.setValue("C_Charge_ID", null);
			return "ChargeExclusively";
		}
		mTab.setValue("M_AttributeSetInstance_ID", null);
		mTab.setValue("S_ResourceAssignment_ID", null);
		mTab.setValue("C_UOM_ID", new Integer(100));	//	EA
		
		Env.setContext(ctx, WindowNo, "DiscountSchema", "N");
		String sql = "SELECT ChargeAmt FROM C_Charge WHERE C_Charge_ID=?";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_Charge_ID.intValue());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				mTab.setValue ("PriceEntered", rs.getBigDecimal (1));
				mTab.setValue ("PriceActual", rs.getBigDecimal (1));
				mTab.setValue ("PriceLimit", Env.ZERO);
				mTab.setValue ("PriceList", Env.ZERO);
				mTab.setValue ("Discount", Env.ZERO);
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
	 *	Order Line - Tax.
	 *		- basis: Product, Charge, BPartner Location
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
		if (steps) log.warning("init");
		
		//	Check Product
		int M_Product_ID = 0;
		if (column.equals("M_Product_ID"))
			M_Product_ID = ((Integer)value).intValue();
		else
			M_Product_ID = Env.getContextAsInt(ctx, WindowNo, "M_Product_ID");
		int C_Charge_ID = 0;
		if (column.equals("C_Charge_ID"))
			C_Charge_ID = ((Integer)value).intValue();
		else
			C_Charge_ID = Env.getContextAsInt(ctx, WindowNo, "C_Charge_ID");
		log.fine("Product=" + M_Product_ID + ", C_Charge_ID=" + C_Charge_ID);
		if (M_Product_ID == 0 && C_Charge_ID == 0)
			return amt(ctx, WindowNo, mTab, mField, value);		//

		//	Check Partner Location
		int shipC_BPartner_Location_ID = 0;
		if (column.equals("C_BPartner_Location_ID"))
			shipC_BPartner_Location_ID = ((Integer)value).intValue();
		else
			shipC_BPartner_Location_ID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_Location_ID");
		if (shipC_BPartner_Location_ID == 0)
			return amt(ctx, WindowNo, mTab, mField, value);		//
		log.fine("Ship BP_Location=" + shipC_BPartner_Location_ID);

		//
		Timestamp billDate = Env.getContextAsDate(ctx, WindowNo, "DateOrdered");
		log.fine("Bill Date=" + billDate);

		Timestamp shipDate = Env.getContextAsDate(ctx, WindowNo, "DatePromised");
		log.fine("Ship Date=" + shipDate);

		int AD_Org_ID = Env.getContextAsInt(ctx, WindowNo, "AD_Org_ID");
		log.fine("Org=" + AD_Org_ID);

		int M_Warehouse_ID = Env.getContextAsInt(ctx, WindowNo, "M_Warehouse_ID");
		log.fine("Warehouse=" + M_Warehouse_ID);

		int billC_BPartner_Location_ID = Env.getContextAsInt(ctx, WindowNo, "Bill_Location_ID");
		if (billC_BPartner_Location_ID == 0)
			billC_BPartner_Location_ID = shipC_BPartner_Location_ID;
		log.fine("Bill BP_Location=" + billC_BPartner_Location_ID);

		//

		// Begin e-Evolution ogi-cd 29sep06
		MProduct product = new MProduct(ctx,M_Product_ID,null);
		System.err.println("....1 ............. " + new MTaxCategory(ctx,product.getC_TaxCategory_ID(),null).isDefault());
		if ( ! new MTaxCategory(ctx,product.getC_TaxCategory_ID(),null).isDefault() ) {
			int C_Tax_ID_Definition = 0;
			// new X_AD_OrgInfo(ctx,AD_Org_ID,null).getAD_OrgType_ID();
			int AD_OrgType_ID = DB.getSQLValue("AD_Org_Info", 
					"SELECT AD_OrgType_ID FROM AD_OrgInfo WHERE AD_Org_ID = " + AD_Org_ID); 
			int C_BPartner_ID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID");
			int C_Contributor_Type_ID = DB.getSQLValue("C_Contribution_Type", 
					"SELECT C_Contributor_Type_ID FROM C_BPartner WHERE C_BPartner_ID="+C_BPartner_ID);
			int C_RetentionConcept_ID = DB.getSQLValue("C_RetentionConcept", 
					"SELECT C_RetentionConcept_ID FROM M_Product WHERE M_Product_ID="+M_Product_ID);
			int C_RetentionConcept_Group_ID = DB.getSQLValue("C_RetentionConcept_Group", 
					"SELECT MAX(C_RetentionConcept_Group_ID) FROM C_RetentionConcept " +
					" WHERE C_RetentionConcept_ID = " + C_RetentionConcept_ID);
			System.err.println("....2 ............. " + AD_OrgType_ID + " --- " + 
					C_Contributor_Type_ID + " --- " + C_RetentionConcept_Group_ID );
			System.err.println("Suma.................... " + (AD_OrgType_ID+C_Contributor_Type_ID+C_RetentionConcept_Group_ID));
			String sql= "SELECT C_Tax_ID FROM E_TaxDefinition " +
				" WHERE AD_Org_ID = " + AD_Org_ID;
				String validContributor = C_Contributor_Type_ID > 0 ? 
						("C_Contributor_Type_ID = " + C_Contributor_Type_ID) : 
						"C_Contributor_Type_ID IS NULL";
				String validRetention = C_RetentionConcept_Group_ID > 0 ? 
						("C_RetentionConcept_Group_ID = " + C_RetentionConcept_Group_ID) : 
						"C_RetentionConcept_Group_ID IS NULL";
					sql = sql +" AND "+ validContributor +" AND "+ validRetention;
			C_Tax_ID_Definition = DB.getSQLValue("E_TaxDefinition", sql);
			System.err.println("SQL ..............." + sql + " -------> " + C_Tax_ID_Definition);
			
			if( C_Tax_ID_Definition > 0) {
				mTab.setValue("C_Tax_ID", new Integer(C_Tax_ID_Definition));
				return "";					
			}
			//info.getAD_OrgType_ID()
		}
		// End e-Evolution ogi-cd 29Sep06







		int C_Tax_ID = Tax.get (ctx, M_Product_ID, C_Charge_ID, billDate, shipDate,
			AD_Org_ID, M_Warehouse_ID, billC_BPartner_Location_ID, shipC_BPartner_Location_ID,
			"Y".equals(Env.getContext(ctx, WindowNo, "IsSOTrx")));
		log.info("Tax ID=" + C_Tax_ID);
		//
		if (C_Charge_ID!=0)
		{
			if (C_Tax_ID == 0)
				mTab.fireDataStatusEEvent(CLogger.retrieveError());
			else
				mTab.setValue("C_Tax_ID", new Integer(C_Tax_ID));
		}
		//
		if (steps) log.warning("fini");
		return amt(ctx, WindowNo, mTab, mField, value);
	}	//	tax


	/**
	 *	Order Line - Amount.
	 *		- called from QtyOrdered, Discount and PriceActual
	 *		- calculates Discount or Actual Amount
	 *		- calculates LineNetAmt
	 *		- enforces PriceLimit
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

		if (steps) log.warning("init");
		int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, "C_UOM_ID");
		int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, "M_Product_ID");
		int M_PriceList_ID = Env.getContextAsInt(ctx, WindowNo, "M_PriceList_ID");
		int StdPrecision = MPriceList.getStandardPrecision(ctx, M_PriceList_ID);
		Integer SQtyEntered, SQtyOrdered, SPriceEntered, SPriceActual, SPriceLimit, SDiscount, SPriceList;
		BigDecimal QtyEntered, QtyOrdered, PriceEntered, PriceActual, PriceLimit, Discount, PriceList;
		//	get values
                
                // OJO QUE ES UN PARCHE PORQUE QtyEntered tira error en el casting !!!!

                //SQtyEntered = (Integer)mTab.getValue("QtyEntered");	

                QtyEntered = BigDecimal.valueOf(((Number)mTab.getValue("QtyEntered")).doubleValue());

                //QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");

		QtyOrdered = (BigDecimal)mTab.getValue("QtyOrdered");
		log.fine("QtyEntered=" + QtyEntered + ", Ordered=" + QtyOrdered + ", UOM=" + C_UOM_To_ID);
		
                //
		
        PriceEntered = (BigDecimal)mTab.getValue("PriceEntered");
		PriceActual = (BigDecimal)mTab.getValue("PriceActual");
		Discount = (BigDecimal)mTab.getValue("Discount");
		PriceLimit = (BigDecimal)mTab.getValue("PriceLimit");
		PriceList = (BigDecimal)mTab.getValue("PriceList");
		log.fine("PriceList=" + PriceList + ", Limit=" + PriceLimit + ", Precision=" + StdPrecision);
		log.fine("PriceEntered=" + PriceEntered + ", Actual=" + PriceActual + ", Discount=" + Discount);

		//	Qty changed - recalc price
		if ((mField.getColumnName().equals("QtyOrdered") 
			|| mField.getColumnName().equals("QtyEntered")
			|| mField.getColumnName().equals("M_Product_ID")) 
			&& !"N".equals(Env.getContext(ctx, WindowNo, "DiscountSchema")))
		{
			int C_BPartner_ID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID");
			if (mField.getColumnName().equals("QtyEntered"))
				QtyOrdered = MUOMConversion.convertProductTo (ctx, M_Product_ID, 
					C_UOM_To_ID, QtyEntered);
			if (QtyOrdered == null)
				QtyOrdered = QtyEntered;
			boolean IsSOTrx = Env.getContext(ctx, WindowNo, "IsSOTrx").equals("Y");
			MProductPricing pp = new MProductPricing (M_Product_ID, C_BPartner_ID, QtyOrdered, IsSOTrx);
			pp.setM_PriceList_ID(M_PriceList_ID);
			int M_PriceList_Version_ID = Env.getContextAsInt(ctx, WindowNo, "M_PriceList_Version_ID");
			pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
			Timestamp date = (Timestamp)mTab.getValue("DateOrdered");
			pp.setPriceDate(date);
			//
			PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, pp.getPriceStd());
			if (PriceEntered == null)
				PriceEntered = pp.getPriceStd();
			//
			log.fine("QtyChanged -> PriceActual=" + pp.getPriceStd() 
				+ ", PriceEntered=" + PriceEntered + ", Discount=" + pp.getDiscount());
			//mTab.setValue("PriceActual", pp.getPriceStd());
			//mTab.setValue("Discount", pp.getDiscount());
			//mTab.setValue("PriceEntered", PriceEntered);
			Env.setContext(ctx, WindowNo, "DiscountSchema", pp.isDiscountSchema() ? "Y" : "N");
		}
		else if (mField.getColumnName().equals("PriceActual"))
		{
			PriceActual = (BigDecimal)value;
			PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, PriceActual);
			if (PriceEntered == null)
				PriceEntered = PriceActual;
			//
			log.fine("PriceActual=" + PriceActual 
				+ " -> PriceEntered=" + PriceEntered);
			mTab.setValue("PriceEntered", PriceEntered);
		}
		else if (mField.getColumnName().equals("PriceEntered"))
		{
			PriceEntered = (BigDecimal)value;
			PriceActual = MUOMConversion.convertProductTo (ctx, M_Product_ID, 
				C_UOM_To_ID, PriceEntered);
			if (PriceActual == null)
				PriceActual = PriceEntered;
			//
			log.fine("PriceEntered=" + PriceEntered 
				+ " -> PriceActual=" + PriceActual);
			mTab.setValue("PriceActual", PriceActual);
		}
		
		//  Discount entered - Calculate Actual/Entered
		if (mField.getColumnName().equals("Discount"))
		{
			PriceActual = new BigDecimal ((100.0 - Discount.doubleValue()) / 100.0 * PriceList.doubleValue());
			if (PriceActual.scale() > StdPrecision)
				PriceActual = PriceActual.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
			PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, PriceActual);
			if (PriceEntered == null)
				PriceEntered = PriceActual;
			mTab.setValue("PriceActual", PriceActual);
			mTab.setValue("PriceEntered", PriceEntered);
		}
		else if (mField.getColumnName().equals("PriceList"))
		{
			PriceEntered = PriceList.subtract(PriceList.multiply(Discount).divide(new BigDecimal(100)));
			
			PriceActual = MUOMConversion.convertProductTo (ctx, M_Product_ID, 
				C_UOM_To_ID, PriceEntered);
			if (PriceActual == null)
				PriceActual = PriceEntered;
			
			mTab.setValue("PriceActual", PriceActual);
			mTab.setValue("PriceEntered", PriceEntered);
		}
		//	calculate Discount
		else
		{
			if (PriceList.intValue() == 0)
				Discount = Env.ZERO;
			else
				Discount = new BigDecimal ((PriceList.doubleValue() - PriceActual.doubleValue()) / PriceList.doubleValue() * 100.0);
			if (Discount.scale() > 2)
				Discount = Discount.setScale(2, BigDecimal.ROUND_HALF_UP);
			mTab.setValue("Discount", Discount);
		}
		log.fine("PriceEntered=" + PriceEntered + ", Actual=" + PriceActual + ", Discount=" + Discount);

		//	Check PriceLimit
		if (!(mField.getColumnName().equals("QtyOrdered") 
			|| mField.getColumnName().equals("QtyEntered")))
		{
			String epl = Env.getContext(ctx, WindowNo, "EnforcePriceLimit");
			boolean check = epl != null && epl.equals("Y");
			if (check && MRole.getDefault().isOverwritePriceLimit())
				check = false;
			//	Check Price Limit?
			if (check && PriceLimit.doubleValue() != 0.0
			  && PriceActual.compareTo(PriceLimit) < 0)
			{
				PriceActual = PriceLimit;
				PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
					C_UOM_To_ID, PriceLimit);
				if (PriceEntered == null)
					PriceEntered = PriceLimit;
				log.fine("(under) PriceEntered=" + PriceEntered + ", Actual" + PriceLimit);
				mTab.setValue ("PriceActual", PriceLimit);
				mTab.setValue ("PriceEntered", PriceEntered);
				mTab.fireDataStatusEEvent ("UnderLimitPrice", "", false);

				//	Repeat Discount calc
				if (PriceList.intValue() != 0)
				{
					Discount = new BigDecimal ((PriceList.doubleValue () - PriceActual.doubleValue ()) / PriceList.doubleValue () * 100.0);
					if (Discount.scale () > 2)
						Discount = Discount.setScale (2, BigDecimal.ROUND_HALF_UP);
					mTab.setValue ("Discount", Discount);
				}
			}
		}

		//	Line Net Amt
		//BigDecimal LineNetAmt = QtyOrdered.multiply(PriceActual);
                
                BigDecimal LineNetAmt = QtyEntered.multiply(PriceActual);

                
		if (LineNetAmt.scale() > StdPrecision)
			LineNetAmt = LineNetAmt.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
		log.info("LineNetAmt=" + LineNetAmt);
		mTab.setValue("LineNetAmt", LineNetAmt);
		//
		setCalloutActive(false);
		return "";
	}	//	amt

	/**
	 *	Order Line - Quantity.
	 *		- called from C_UOM_ID, QtyEntered, QtyOrdered
	 *		- enforces qty UOM relationship
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 */
	public String qty (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);

		int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, "M_Product_ID");
		if (steps) log.warning("init - M_Product_ID=" + M_Product_ID + " - " );
		BigDecimal QtyOrdered, QtyEntered, PriceActual, PriceEntered;
		
		//	No Product
		if (M_Product_ID == 0)
		{
			QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
			mTab.setValue("QtyOrdered", QtyEntered);
		}
		//	UOM Changed - convert from Entered -> Product
		else if (mField.getColumnName().equals("C_UOM_ID"))
		{
			int C_UOM_To_ID = ((Integer)value).intValue();
			QtyEntered = BigDecimal.valueOf(((Number)mTab.getValue("QtyEntered")).doubleValue());
			QtyOrdered = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, QtyEntered);
			if (QtyOrdered == null)
				QtyOrdered = QtyEntered;
			boolean conversion = QtyEntered.compareTo(QtyOrdered) != 0;
			PriceActual = (BigDecimal)mTab.getValue("PriceActual");
			PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, PriceActual);
			if (PriceEntered == null)
				PriceEntered = PriceActual; 
			log.fine("UOM=" + C_UOM_To_ID 
				+ ", QtyEntered/PriceActual=" + QtyEntered + "/" + PriceActual
				+ " -> " + conversion 
				+ " QtyOrdered/PriceEntered=" + QtyOrdered + "/" + PriceEntered);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyOrdered", QtyOrdered);
			mTab.setValue("PriceEntered", PriceEntered);
		}
		//	QtyEntered changed - calculate QtyOrdered
		else if (mField.getColumnName().equals("QtyEntered"))
		{
			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, "C_UOM_ID");
			QtyEntered = (BigDecimal)value;
			QtyOrdered = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, QtyEntered);
			if (QtyOrdered == null)
				QtyOrdered = QtyEntered;
			boolean conversion = QtyEntered.compareTo(QtyOrdered) != 0;
			log.fine("UOM=" + C_UOM_To_ID 
				+ ", QtyEntered=" + QtyEntered
				+ " -> " + conversion 
				+ " QtyOrdered=" + QtyOrdered);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyOrdered", QtyOrdered);
		}
		else if (mField.getColumnName().equals("QTYCALC"))
		{
			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, "C_UOM_ID");
			QtyEntered = BigDecimal.valueOf(((Number)mTab.getValue("QtyEntered")).doubleValue());

			QtyOrdered = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, QtyEntered);
			if (QtyOrdered == null)
				QtyOrdered = QtyEntered;
			boolean conversion = QtyEntered.compareTo(QtyOrdered) != 0;
			log.fine("UOM=" + C_UOM_To_ID 
				+ ", QtyEntered=" + QtyEntered
				+ " -> " + conversion 
				+ " QtyOrdered=" + QtyOrdered);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyOrdered", QtyOrdered);
		}
		else if (mField.getColumnName().equals("QTYADVANTAGE"))
		{
			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, "C_UOM_ID");
			QtyEntered = BigDecimal.valueOf(((Number)mTab.getValue("QtyEntered")).doubleValue());

			QtyOrdered = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, QtyEntered);
			if (QtyOrdered == null)
				QtyOrdered = QtyEntered;
			boolean conversion = QtyEntered.compareTo(QtyOrdered) != 0;
			log.fine("UOM=" + C_UOM_To_ID 
				+ ", QtyEntered=" + QtyEntered
				+ " -> " + conversion 
				+ " QtyOrdered=" + QtyOrdered);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyOrdered", QtyOrdered);
		}
		//	QtyOrdered changed - calculate QtyEntered
		else if (mField.getColumnName().equals("QtyOrdered"))
		{
			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, "C_UOM_ID");
			QtyOrdered = (BigDecimal)value;
			QtyEntered = MUOMConversion.convertProductTo (ctx, M_Product_ID, 
				C_UOM_To_ID, QtyOrdered);
			if (QtyEntered == null)
				QtyEntered = QtyOrdered;
			boolean conversion = QtyOrdered.compareTo(QtyEntered) != 0;
			log.fine("UOM=" + C_UOM_To_ID 
				+ ", QtyOrdered=" + QtyOrdered
				+ " -> " + conversion 
				+ " QtyEntered=" + QtyEntered);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyEntered", QtyEntered);
		}
		//
		setCalloutActive(false);
		return "";
	}	//	qty
	

       /**
	 *	calculateAdvantage - Vit4B.
	 *		- verifica que la politica de bonificaciones se cumpla
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 */
        /*
	public String calculateAdvantage (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		
                setCalloutActive(true);

		int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, "M_Product_ID");
		int C_BPartner_ID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID");
                Timestamp DateOrdered = Env.getContextAsDate(ctx, WindowNo, "DateOrdered");

                BigDecimal cantidad_parcial = (BigDecimal)mTab.getValue("QtyCalc");
                BigDecimal cantidad_bonificada = (BigDecimal)mTab.getValue("QtyAdvantage");
                
                   
		if (M_Product_ID != 0 && C_BPartner_ID != 0 && cantidad_parcial.intValue() != 0 && cantidad_bonificada.intValue() != 0)
		{
                    String sql = "SELECT quantity, quantity_advantage FROM M_Advantage WHERE C_BPartner_ID = ? AND " +
                                 "M_Product_ID = ? AND datefrom <= ? AND dateto >= ?";
                    try
                    {
                            PreparedStatement pstmt = DB.prepareStatement(sql, null);
                            
                            pstmt.setInt(1, C_BPartner_ID);
                            pstmt.setInt(2, M_Product_ID);
                            pstmt.setString(3, DateOrdered.toString());
                            pstmt.setString(4, DateOrdered.toString());
                            
                            ResultSet rs = pstmt.executeQuery();
                            if (rs.next())
                            {
                                    BigDecimal dif = cantidad_parcial.divide(rs.getBigDecimal(1));
                                    BigDecimal mul = dif.multiply(rs.getBigDecimal(2));
                                    
                                    if(mul.compareTo(cantidad_bonificada) == -1)
                                    {
                                        mTab.setValue ("Quantity", cantidad_parcial.add(cantidad_bonificada));
                                        return "";                                            
                                    }
                                    else
                                    {
                                        JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"Asignaci�n de bonificaci�n inv�lida"), "Info" , JOptionPane.INFORMATION_MESSAGE);
                                        mTab.setValue ("QtyCalcule", "");
                                        mTab.setValue ("QtyAdvantage", "");
                                        
                                        return "";
                                    }

                            }
                            rs.close();
                            pstmt.close();
                    }
                    catch (SQLException e)
                    {
                            log.log(Level.SEVERE, sql, e);
                            return "";
                    }

                    sql = "SELECT quantity, quantity_advantage FROM M_Advantage WHERE C_BPartner_ID = 0 AND " +
                                 "M_Product_ID = ? AND datefrom <= ? AND dateto >= ?";
                    try
                    {
                            PreparedStatement pstmt = DB.prepareStatement(sql, null);
                            
                            pstmt.setInt(1, M_Product_ID);
                            pstmt.setString(2, DateOrdered.toString());
                            pstmt.setString(3, DateOrdered.toString());
                            
                            ResultSet rs = pstmt.executeQuery();
                            if (rs.next())
                            {
                                    BigDecimal dif = cantidad_parcial.divide(rs.getBigDecimal(1));
                                    BigDecimal mul = dif.multiply(rs.getBigDecimal(2));
                                    
                                    if(mul.compareTo(cantidad_bonificada) == -1)
                                    {
                                        mTab.setValue ("Quantity", cantidad_parcial.add(cantidad_bonificada));
                                        return "";                                            
                                    }
                                    else
                                    {
                                        JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"Asignaci�n de bonificaci�n inv�lida"), "Info" , JOptionPane.INFORMATION_MESSAGE);
                                        mTab.setValue ("QtyCalcule", "");
                                        mTab.setValue ("QtyAdvantage", "");
                                        
                                        return "";
                                    }

                            }
                            rs.close();
                            pstmt.close();
                    }
                    catch (SQLException e)
                    {
                            log.log(Level.SEVERE, sql, e);
                            return "";
                    }

                                    
                    sql = "SELECT quantity, quantity_advantage FROM M_Advantage WHERE C_BPartner_ID = ? AND " +
                                 "M_Product_ID = 0 AND datefrom <= ? AND dateto >= ?";
                    try
                    {
                            PreparedStatement pstmt = DB.prepareStatement(sql, null);
                            
                            pstmt.setInt(1, C_BPartner_ID);
                            pstmt.setString(2, DateOrdered.toString());
                            pstmt.setString(3, DateOrdered.toString());
                            
                            ResultSet rs = pstmt.executeQuery();
                            if (rs.next())
                            {
                                    BigDecimal dif = cantidad_parcial.divide(rs.getBigDecimal(1));
                                    BigDecimal mul = dif.multiply(rs.getBigDecimal(2));
                                    
                                    if(mul.compareTo(cantidad_bonificada) == -1)
                                    {
                                        mTab.setValue ("Quantity", cantidad_parcial.add(cantidad_bonificada));
                                        return "";                                            
                                    }
                                    else
                                    {
                                        JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"Asignaci�n de bonificaci�n inv�lida"), "Info" , JOptionPane.INFORMATION_MESSAGE);
                                        mTab.setValue ("QtyCalcule", "");
                                        mTab.setValue ("QtyAdvantage", "");
                                        
                                        return "";
                                    }

                            }
                            rs.close();
                            pstmt.close();
                    }
                    catch (SQLException e)
                    {
                            log.log(Level.SEVERE, sql, e);
                            return "";
                    }
                    
                    sql = "SELECT quantity, quantity_advantage FROM M_Advantage WHERE C_BPartner_ID = 0 AND " +
                                 "M_Product_ID = 0 AND datefrom <= ? AND dateto >= ?";
                    try
                    {
                            PreparedStatement pstmt = DB.prepareStatement(sql, null);

                            pstmt.setString(1, DateOrdered.toString());
                            pstmt.setString(2, DateOrdered.toString());
                            
                            ResultSet rs = pstmt.executeQuery();
                            if (rs.next())
                            {
                                    BigDecimal dif = cantidad_parcial.divide(rs.getBigDecimal(1));
                                    BigDecimal mul = dif.multiply(rs.getBigDecimal(2));
                                    
                                    if(mul.compareTo(cantidad_bonificada) == -1)
                                    {
                                        mTab.setValue ("Quantity", cantidad_parcial.add(cantidad_bonificada));
                                        return "";                                            
                                    }
                                    else
                                    {
                                        JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"Asignaci�n de bonificaci�n inv�lida"), "Info" , JOptionPane.INFORMATION_MESSAGE);
                                        mTab.setValue ("QtyCalcule", "");
                                        mTab.setValue ("QtyAdvantage", "");
                                        
                                        return "";
                                    }

                            }
                            rs.close();
                            pstmt.close();
                    }
                    catch (SQLException e)
                    {
                            log.log(Level.SEVERE, sql, e);
                            return "";
                    }                    

                }
                return "";
		
	}
        */
        
       /**
	 *	calculateAdvantageBon - Vit4B.
	 *		- verifica que la politica de bonificaciones se cumpla
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 */
        
	public String calculateAdvantageBon (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		
                setCalloutActive(true);

		int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, "M_Product_ID");
		int C_BPartner_ID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID");
                int C_Order_ID = Env.getContextAsInt(ctx, WindowNo, "C_Order_ID");                

                /*
                **  En este punto podria hacer la verificacion del contexto pero no funciona en el caso de que haga una modificacion
                **  ya que no tendria el contexto correctamente modificado por no pasar por la 
                **  cabecera antes y setear si la orden es bonificada.
                **
                **  Lo que deberia es de usar una funcion y avisar en caso que use el campo de bonificacion
                **  VIT4B
                */

                //int isAdvantage = Env.getContextAsInt(ctx, "isadvantage");
                
                boolean orderAdvantage = isOrderAdvantage(C_Order_ID);

                if(!orderAdvantage && M_Product_ID != 0 && C_BPartner_ID != 0)
                {
                    JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"Orden sin Bonificación"), "Info" , JOptionPane.INFORMATION_MESSAGE);
                    mTab.setValue ("QtyAdvantage", 0);
                    setCalloutActive(false);
                    return "";
                }
                

                Timestamp DateOrdered = Env.getContextAsDate(ctx, WindowNo, "DateOrdered");

                int cantidad_parcial = ((Number)mTab.getValue("QtyEntered")).intValue();
                int cantidad_bonificada = ((Number)mTab.getValue("QtyAdvantage")).intValue();
                
                   
		if (M_Product_ID != 0 && C_BPartner_ID != 0)
                {
                    
                    if(cantidad_parcial != 0)
                    {
                        
                        if(cantidad_bonificada != 0)
                        {
                            
                        	String sql = "SELECT quantity, quantity_advantage FROM M_Advantage WHERE C_BPartner_ID = 0 AND " +
                            "M_Product_ID = " + M_Product_ID + " AND datefrom <= to_date('" + DateOrdered.toString().substring(0,11) + "','YYYY-MM-DD') AND " +
                            "dateto >= to_date('" + DateOrdered.toString().substring(0,11) + "','YYYY-MM-DD')";
                
                
			                System.out.println("--------------------------");
			                System.out.println(sql);
			                System.out.println("--------------------------");
			                
			                try
			                {
			                        PreparedStatement pstmt = DB.prepareStatement(sql, null);
			
			                        ResultSet rs = pstmt.executeQuery();
			                        if (rs.next())
			                        {
			                                if(rs.getInt(1) != 0)
			                                {
			                                int dif = cantidad_parcial / rs.getInt(1);
			                                int mul = dif * rs.getInt(2);
			
			                                mTab.setValue ("QtyEntered", cantidad_parcial);
			                                if(mul >= cantidad_bonificada)
			                                {
			                                	mTab.setValue ("QtyOrdered", cantidad_parcial + cantidad_bonificada);
			                                	setCalloutActive(false);
			                                    return "";                                            
			                                }
			                                else
			                                {
			                                    JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"Asignación de bonificación inválida"), "Info" , JOptionPane.INFORMATION_MESSAGE);
			                                    mTab.setValue ("QtyAdvantage", 0);
			                                    mTab.setValue ("QtyOrdered", cantidad_parcial);
			                                    setCalloutActive(false);
			                                    return "";
			                                }
			                                }
			
			                        }
			                        rs.close();
			                        pstmt.close();
			                }
			                catch (SQLException e)
			                {
			                        log.log(Level.SEVERE, sql, e);
			                        setCalloutActive(false);
			                        return "";
			                }
                        	
                            
                             sql = "SELECT quantity, quantity_advantage FROM M_Advantage WHERE C_BPartner_ID = " + C_BPartner_ID +  " AND " +
                                         "M_Product_ID = " + M_Product_ID + " AND datefrom <= to_date('" + DateOrdered.toString().substring(0,11) + "','YYYY-MM-DD') AND " +
                                         "dateto >= to_date('" + DateOrdered.toString().substring(0,11) + "','YYYY-MM-DD')";
                            
                            System.out.println("--------------------------");
                            System.out.println(sql);
                            System.out.println("--------------------------");
                            
                            try
                            {
                                    PreparedStatement pstmt = DB.prepareStatement(sql, null);

                                    ResultSet rs = pstmt.executeQuery();
                                    if (rs.next())
                                    {
                                            if(rs.getInt(1) != 0)
                                            {
                                            int dif = cantidad_parcial / rs.getInt(1);
                                            int mul = dif * rs.getInt(2);

                                            mTab.setValue ("QtyEntered", cantidad_parcial);
                                            if(mul >= cantidad_bonificada)
                                            {
                                            	mTab.setValue ("QtyOrdered", cantidad_parcial + cantidad_bonificada);
                                            	setCalloutActive(false);
                                                return "";                                            
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"Asignación de bonificación inválida"), "Info" , JOptionPane.INFORMATION_MESSAGE);
                                                mTab.setValue ("QtyAdvantage", 0);
                                                mTab.setValue ("QtyOrdered", cantidad_parcial);
                                                setCalloutActive(false);

                                                return "";
                                            }
                                            }

                                    }
                                    rs.close();
                                    pstmt.close();
                            }
                            catch (SQLException e)
                            {
                                    log.log(Level.SEVERE, sql, e);
                                    setCalloutActive(false);
                                    return "";
                            }
                            
                            sql = "SELECT quantity, quantity_advantage FROM M_Advantage WHERE C_BPartner_ID = " + C_BPartner_ID + " AND " +
                                         "M_Product_ID = 0 AND datefrom <= to_date('" + DateOrdered.toString().substring(0,11) + "','YYYY-MM-DD') AND " +
                                         "dateto >= to_date('" + DateOrdered.toString().substring(0,11) + "','YYYY-MM-DD')";

                            System.out.println("--------------------------");
                            System.out.println(sql);
                            System.out.println("--------------------------");
                            
                            
                            try
                            {
                                    PreparedStatement pstmt = DB.prepareStatement(sql, null);



                                    ResultSet rs = pstmt.executeQuery();
                                    if (rs.next())
                                    {
                                            if(rs.getInt(1) != 0)
                                            {
                                            int dif = cantidad_parcial / rs.getInt(1);
                                            int mul = dif * rs.getInt(2);

                                            mTab.setValue ("QtyEntered", cantidad_parcial);
                                            if(mul >= cantidad_bonificada)
                                            {
                                            	mTab.setValue ("QtyOrdered", cantidad_parcial + cantidad_bonificada);
                                            	setCalloutActive(false);
                                                return "";                                            
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"Asignación de bonificación inválida"), "Info" , JOptionPane.INFORMATION_MESSAGE);
                                                mTab.setValue ("QtyAdvantage", 0);
                                                mTab.setValue ("QtyOrdered", cantidad_parcial);
                                                setCalloutActive(false);
                                                return "";
                                            }
                                            }

                                    }
                                    rs.close();
                                    pstmt.close();
                            }
                            catch (SQLException e)
                            {
                                    log.log(Level.SEVERE, sql, e);
                                    setCalloutActive(false);
                                    return "";
                            }

                            sql = "SELECT quantity, quantity_advantage FROM M_Advantage WHERE C_BPartner_ID = 0 AND " +
                                         "M_Product_ID = 0 AND datefrom <= to_date('" + DateOrdered.toString().substring(0,11) + "','YYYY-MM-DD') AND " +
                                         "dateto >= to_date('" + DateOrdered.toString().substring(0,11) + "','YYYY-MM-DD')";
                            
                            System.out.println("--------------------------");
                            System.out.println(sql);
                            System.out.println("--------------------------");
                            
                            
                            
                            try
                            {
                                    PreparedStatement pstmt = DB.prepareStatement(sql, null);


                                    ResultSet rs = pstmt.executeQuery();
                                    if (rs.next())
                                    {
                                            if(rs.getInt(1) != 0)
                                            {
                                            int dif = cantidad_parcial / rs.getInt(1);
                                            int mul = dif * rs.getInt(2);

                                            mTab.setValue ("QtyEntered", cantidad_parcial);
                                            if(mul >= cantidad_bonificada)
                                            {
                                            	mTab.setValue ("QtyOrdered", cantidad_parcial + cantidad_bonificada);
                                            	setCalloutActive(false);
                                                return "";                                            
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"Asignación de bonificación inválida"), "Info" , JOptionPane.INFORMATION_MESSAGE);
                                                mTab.setValue ("QtyAdvantage", 0);
                                                mTab.setValue ("QtyOrdered", cantidad_parcial);
                                                setCalloutActive(false);
                                                return "";
                                            }
                                            }

                                    }
                                    rs.close();
                                    pstmt.close();
                            }
                            catch (SQLException e)
                            {
                                    log.log(Level.SEVERE, sql, e);
                                    setCalloutActive(false);
                                    return "";
                            }

                            JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"Asignación de bonificación inválida"), "Info" , JOptionPane.INFORMATION_MESSAGE);
                            mTab.setValue ("QtyAdvantage", 0);
                            mTab.setValue ("QtyEntered", cantidad_parcial);
                            mTab.setValue ("QtyOrdered", cantidad_parcial);
                            setCalloutActive(false);
                            return ""; 
                        
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"Asignación de bonificación inválida"), "Info" , JOptionPane.INFORMATION_MESSAGE);
                            mTab.setValue ("QtyAdvantage", 0);
                            mTab.setValue ("QtyEntered", cantidad_parcial);
                            mTab.setValue ("QtyOrdered", cantidad_parcial);
                            setCalloutActive(false);
                            return ""; 
                        }
                        
                    }

                }
                setCalloutActive(false);
                return "";
		
	}

       /**
	 *	calculateAdvantage - Vit4B.
	 *		- verifica que la politica de bonificaciones se cumpla
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 */
        
	public String calculateAdvantage (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		
                setCalloutActive(true);

		int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, "M_Product_ID");
		int C_BPartner_ID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID");
                
                Timestamp DateOrdered = Env.getContextAsDate(ctx, WindowNo, "DateOrdered");

                int cantidad_parcial = ((Number)mTab.getValue("QtyCalc")).intValue();
                int cantidad_bonificada = ((Number)mTab.getValue("QtyAdvantage")).intValue();
                
                   
		if (M_Product_ID != 0 && C_BPartner_ID != 0)
                {
                    
                    if(cantidad_parcial != 0)
                    {
                        
                        if(cantidad_bonificada != 0)
                        {
                            
                            
                            String sql = "SELECT quantity, quantity_advantage FROM M_Advantage WHERE C_BPartner_ID = " + C_BPartner_ID +  " AND " +
                                         "M_Product_ID = " + M_Product_ID + " AND datefrom <= to_date('" + DateOrdered.toString().substring(0,11) + "','YYYY-MM-DD') AND " +
                                         "dateto >= to_date('" + DateOrdered.toString().substring(0,11) + "','YYYY-MM-DD')";
                            
                            System.out.println("--------------------------");
                            System.out.println(sql);
                            System.out.println("--------------------------");
                            
                            try
                            {
                                    PreparedStatement pstmt = DB.prepareStatement(sql, null);

                                    ResultSet rs = pstmt.executeQuery();
                                    if (rs.next())
                                    {
                                            if(rs.getInt(1) != 0)
                                            {
                                            int dif = cantidad_parcial / rs.getInt(1);
                                            int mul = dif * rs.getInt(2);

                                            mTab.setValue ("QtyEntered", cantidad_parcial);
                                            if(mul >= cantidad_bonificada)
                                            {
                                            	mTab.setValue ("QtyOrdered", cantidad_parcial + cantidad_bonificada);
                                            	setCalloutActive(false);
                                                return "";                                            
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"Asignación de bonificación inválida"), "Info" , JOptionPane.INFORMATION_MESSAGE);
                                                mTab.setValue ("QtyAdvantage", 0);
                                                mTab.setValue ("QtyOrdered", cantidad_parcial);
                                                setCalloutActive(false);
                                                return "";
                                            }
                                            }

                                    }
                                    rs.close();
                                    pstmt.close();
                            }
                            catch (SQLException e)
                            {
                                    log.log(Level.SEVERE, sql, e);
                                    setCalloutActive(false);
                                    return "";
                            }

                            sql = "SELECT quantity, quantity_advantage FROM M_Advantage WHERE C_BPartner_ID = 0 AND " +
                                         "M_Product_ID = " + M_Product_ID + " AND datefrom <= to_date('" + DateOrdered.toString().substring(0,11) + "','YYYY-MM-DD') AND " +
                                         "dateto >= to_date('" + DateOrdered.toString().substring(0,11) + "','YYYY-MM-DD')";
                            
                            
                            System.out.println("--------------------------");
                            System.out.println(sql);
                            System.out.println("--------------------------");
                            
                            try
                            {
                                    PreparedStatement pstmt = DB.prepareStatement(sql, null);

                                    ResultSet rs = pstmt.executeQuery();
                                    if (rs.next())
                                    {
                                            if(rs.getInt(1) != 0)
                                            {
                                            int dif = cantidad_parcial / rs.getInt(1);
                                            int mul = dif * rs.getInt(2);

                                            mTab.setValue ("QtyEntered", cantidad_parcial);
                                            if(mul >= cantidad_bonificada)
                                            {
                                            	mTab.setValue ("QtyOrdered", cantidad_parcial + cantidad_bonificada);
                                            	setCalloutActive(false);
                                                return "";                                            
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"Asignación de bonificación inválida"), "Info" , JOptionPane.INFORMATION_MESSAGE);
                                                mTab.setValue ("QtyAdvantage", 0);
                                                mTab.setValue ("QtyOrdered", cantidad_parcial);
                                                setCalloutActive(false);
                                                return "";
                                            }
                                            }

                                    }
                                    rs.close();
                                    pstmt.close();
                            }
                            catch (SQLException e)
                            {
                                    log.log(Level.SEVERE, sql, e);
                                    setCalloutActive(false);
                                    return "";
                            }
                            
                            sql = "SELECT quantity, quantity_advantage FROM M_Advantage WHERE C_BPartner_ID = " + C_BPartner_ID + " AND " +
                                         "M_Product_ID = 0 AND datefrom <= to_date('" + DateOrdered.toString().substring(0,11) + "','YYYY-MM-DD') AND " +
                                         "dateto >= to_date('" + DateOrdered.toString().substring(0,11) + "','YYYY-MM-DD')";

                            System.out.println("--------------------------");
                            System.out.println(sql);
                            System.out.println("--------------------------");
                            
                            
                            try
                            {
                                    PreparedStatement pstmt = DB.prepareStatement(sql, null);



                                    ResultSet rs = pstmt.executeQuery();
                                    if (rs.next())
                                    {
                                            if(rs.getInt(1) != 0)
                                            {
                                            int dif = cantidad_parcial / rs.getInt(1);
                                            int mul = dif * rs.getInt(2);

                                            mTab.setValue ("QtyEntered", cantidad_parcial);
                                            if(mul >= cantidad_bonificada)
                                            {
                                            	mTab.setValue ("QtyOrdered", cantidad_parcial + cantidad_bonificada);
                                            	setCalloutActive(false);
                                                return "";                                            
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"Asignación de bonificación inválida"), "Info" , JOptionPane.INFORMATION_MESSAGE);
                                                mTab.setValue ("QtyAdvantage", 0);
                                                mTab.setValue ("QtyOrdered", cantidad_parcial);
                                                setCalloutActive(false);
                                                return "";
                                            }
                                            }

                                    }
                                    rs.close();
                                    pstmt.close();
                            }
                            catch (SQLException e)
                            {
                                    log.log(Level.SEVERE, sql, e);
                                    setCalloutActive(false);
                                    return "";
                            }

                            sql = "SELECT quantity, quantity_advantage FROM M_Advantage WHERE C_BPartner_ID = 0 AND " +
                                         "M_Product_ID = 0 AND datefrom <= to_date('" + DateOrdered.toString().substring(0,11) + "','YYYY-MM-DD') AND " +
                                         "dateto >= to_date('" + DateOrdered.toString().substring(0,11) + "','YYYY-MM-DD')";
                            
                            System.out.println("--------------------------");
                            System.out.println(sql);
                            System.out.println("--------------------------");
                            
                            
                            
                            try
                            {
                                    PreparedStatement pstmt = DB.prepareStatement(sql, null);


                                    ResultSet rs = pstmt.executeQuery();
                                    if (rs.next())
                                    {
                                            if(rs.getInt(1) != 0)
                                            {
                                            int dif = cantidad_parcial / rs.getInt(1);
                                            int mul = dif * rs.getInt(2);

                                            mTab.setValue ("QtyEntered", cantidad_parcial);
                                            if(mul >= cantidad_bonificada)
                                            {
                                            	mTab.setValue ("QtyOrdered", cantidad_parcial + cantidad_bonificada);
                                            	setCalloutActive(false);
                                                return "";                                            
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"Asignación de bonificación inválida"), "Info" , JOptionPane.INFORMATION_MESSAGE);
                                                mTab.setValue ("QtyAdvantage", 0);
                                                mTab.setValue ("QtyOrdered", cantidad_parcial);
                                                setCalloutActive(false);
                                                return "";
                                            }
                                            }

                                    }
                                    rs.close();
                                    pstmt.close();
                            }
                            catch (SQLException e)
                            {
                                    log.log(Level.SEVERE, sql, e);
                                    setCalloutActive(false);
                                    return "";
                            }

                            JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"Asignación de bonificación inválida"), "Info" , JOptionPane.INFORMATION_MESSAGE);
                            mTab.setValue ("QtyAdvantage", 0);
                            mTab.setValue ("QtyEntered", cantidad_parcial);
                            mTab.setValue ("QtyOrdered", cantidad_parcial);
                            setCalloutActive(false);
                            return ""; 
                        
                        }
                        else
                        {
                                                        
                            //JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"Asignaci�n de bonificaci�n inv�lida"), "Info" , JOptionPane.INFORMATION_MESSAGE);
                            mTab.setValue ("QtyAdvantage", 0);
                            mTab.setValue ("QtyEntered", cantidad_parcial);
                            mTab.setValue ("QtyOrdered", cantidad_parcial);
                            setCalloutActive(false);
                            return "";
                            
                        }
                        
                    }

                }
                setCalloutActive(false);
                return "";
		
	}

       /**
	 *	setEnaableAdvantage - Vit4B.
	 *		- habilita o deshabilita las bonificaciones
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 */
	public String enableAdvantage (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
            int status = Env.getContextAsInt(ctx, "isadvantage");

            
            if(status == 1)
            {
                Env.setContext(ctx,"isadvantage",0);
            }
            else
            {
                Env.setContext(ctx,"isadvantage",1);
            }
            
            
            return "";
        }       

        private boolean isOrderAdvantage(int C_Order_ID) {
            
            String sql = "SELECT isAdvantage FROM C_Order WHERE C_Order_ID = " + C_Order_ID;
                            
            System.out.println("--------------------------");
            System.out.println(sql);
            System.out.println("--------------------------");



            try
            {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);


                    ResultSet rs = pstmt.executeQuery();
                    boolean retorno  = false;

                    if (rs.next())
                    {
                        if(rs.getString(1).equals("Y"))
                            retorno = true;
                    }
                    rs.close();
                    pstmt.close();

                    return retorno;

            }
            catch (SQLException e)
            {
                    log.log(Level.SEVERE, sql, e);
                    return false;
            }            

        }
                
      
}	//	CalloutOrder

