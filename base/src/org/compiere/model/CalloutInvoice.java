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
import javax.swing.JOptionPane;
import org.compiere.util.*;

import sun.rmi.runtime.GetThreadPoolAction;

/**
 *	Invoice Callouts	
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutInvoice.java,v 1.27 2005/11/25 21:57:24 jjanke Exp $
 */
public class CalloutInvoice extends CalloutEngine
{

	/**
	 *	Invoice Header - DocType.
	 *		- PaymentRule
	 *		- temporary Document
	 *  Context:
	 *  	- DocSubTypeSO
	 *		- HasCharges
	 *	- (re-sets Business Partner info of required)
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 */
	public String docType (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		Integer C_DocType_ID = (Integer)value;
		if (C_DocType_ID == null || C_DocType_ID.intValue() == 0)
			return "";

		String sql = "SELECT d.HasCharges,'N',d.IsDocNoControlled,"
			+ "s.CurrentNext, d.DocBaseType "
			+ "FROM C_DocType d, AD_Sequence s "
			+ "WHERE C_DocType_ID=?"		//	1
			+ " AND d.DocNoSequence_ID=s.AD_Sequence_ID(+)";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_DocType_ID.intValue());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				//	Charges - Set Context
				Env.setContext(ctx, WindowNo, "HasCharges", rs.getString(1));
				//	DocumentNo
				if (rs.getString(3).equals("Y"))
					mTab.setValue("DocumentNo", "<" + rs.getString(4) + ">");
				//  DocBaseType - Set Context
				String s = rs.getString(5);
				Env.setContext(ctx, WindowNo, "DocBaseType", s);
				//  AP Check & AR Credit Memo
				if (s.startsWith("AP"))
					mTab.setValue("PaymentRule", "S");    //  Check
				else if (s.endsWith("C"))
					mTab.setValue("PaymentRule", "P");    //  OnCredit
			}
			rs.close();
			pstmt.close();
			mTab.setValue("C_DocType_ID", C_DocType_ID);
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
	 *	Invoice Header- BPartner.
	 *		- M_PriceList_ID (+ Context)
	 *		- C_BPartner_Location_ID
	 *		- AD_User_ID
	 *		- POReference
	 *		- SO_Description
	 *		- IsDiscountPrinted
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
		if (isCalloutActive())
			return "";
		
		Integer C_BPartner_ID = (Integer)value;
		if (C_BPartner_ID == null || C_BPartner_ID.intValue() == 0)
			return "";
		setCalloutActive(true);

		boolean IsSOTrx = "Y".equals(Env.getContext(ctx, WindowNo, "IsSOTrx"));
		MBPartner partner = new MBPartner(ctx,C_BPartner_ID,null);
		
		if (IsSOTrx && !partner.isCustomer())	{
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
			+ " p.SO_CreditLimit, p.SO_CreditLimit-p.SO_CreditUsed AS CreditAvailable,"
			+ " locship.C_Location_ID AS ShipLocation,c.AD_User_ID,"
			+ " COALESCE(p.PO_PriceList_ID,g.PO_PriceList_ID) AS PO_PriceList_ID, p.PaymentRulePO,p.PO_PaymentTerm_ID, "
			+ " locbill.C_Location_ID AS BillLocation "
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
			//
			if (rs.next())
			{
				//	PriceList & IsTaxIncluded & Currency
				Integer ii = new Integer(rs.getInt(IsSOTrx ? "M_PriceList_ID" : "PO_PriceList_ID"));
				if (!rs.wasNull())
					mTab.setValue("M_PriceList_ID", ii);
				else
				{	//	get default PriceList
					int i = Env.getContextAsInt(ctx, "#M_PriceList_ID");
					if (i != 0)
						mTab.setValue("M_PriceList_ID", new Integer(i));
				}

				//	PaymentRule
				String s = rs.getString(IsSOTrx ? "PaymentRule" : "PaymentRulePO");
				if (s != null && s.length() != 0)
				{
					if (Env.getContext(ctx, WindowNo, "DocBaseType").endsWith("C"))	//	Credits are Payment Term
						s = "P";
					else if (IsSOTrx && (s.equals("S") || s.equals("U")))	//	No Check/Transfer for SO_Trx
						s = "P";											//  Payment Term
					mTab.setValue("PaymentRule", s);
				}
				//  Payment Term
				ii = new Integer(rs.getInt(IsSOTrx ? "C_PaymentTerm_ID" : "PO_PaymentTerm_ID"));
				if (!rs.wasNull())
					mTab.setValue("C_PaymentTerm_ID", ii);

				//	Location
				int locShip = rs.getInt("ShipLocation");
				if (rs.wasNull())
					mTab.setValue("C_Location_ID", null);
				else
					mTab.setValue("C_Location_ID", locShip);
				
				int locBill = new Integer(rs.getInt("BillLocation"));
				if (rs.wasNull())
					mTab.setValue("Bill_Location_ID", null);
				else
					mTab.setValue("Bill_Location_ID", locBill);
				
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
				
				//	PO Reference
				s = rs.getString("POReference");
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
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "bPartner", e);
			setCalloutActive(false);
			return e.getLocalizedMessage();
		}
		
		if (IsSOTrx)	{
			MBPartner bp = new MBPartner(Env.getCtx(),C_BPartner_ID,null);
			try {
				sql = "SELECT C_DocType_ID FROM C_DocType WHERE PRINTNAME = ?";
				PreparedStatement pst = DB.prepareStatement(sql, null);
				if(bp.getCondicionIVACode().equals(MBPartner.CIVA_RESPINSCRIPTO))
					pst.setString(1, "FC A");
				else
		        	if(!bp.getCondicionIVACode().equals(MBPartner.CIVA_EXPORTACION))
		        		//FC Factura B
		        		pst.setString(1, "FC B");
		        	else
		        		//FC Factura E
		        		pst.setString(1, "FC E");
				
				ResultSet rs = pst.executeQuery();
				if (rs.next()){
					mTab.setValue("C_DocTypeTarget_ID", rs.getInt(1));
					mTab.setValue("C_DocType_ID", rs.getInt(1));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		setCalloutActive(false);
		return "";
	}	//	bPartner

	/**
	 *	Set Payment Term.
	 *	Payment Term has changed 
	 */
	public String paymentTerm (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		Integer C_PaymentTerm_ID = (Integer)value;
		int C_Invoice_ID = Env.getContextAsInt(ctx, WindowNo, "C_Invoice_ID");
		if (C_PaymentTerm_ID == null || C_PaymentTerm_ID.intValue() == 0
			|| C_Invoice_ID == 0)	//	not saved yet
			return "";
		//
		MPaymentTerm pt = new MPaymentTerm (ctx, C_PaymentTerm_ID.intValue(), null);
		if (pt.get_ID() == 0)
			return "PaymentTerm not found";
		
		boolean valid = pt.apply (C_Invoice_ID);
		mTab.setValue("IsPayScheduleValid", valid ? "Y" : "N");
		
		return "";
	}	//	paymentTerm

	
	/**************************************************************************
	 *	Invoice Line - Product.
	 *		- reset C_Charge_ID / M_AttributeSetInstance_ID
	 *		- PriceList, PriceStd, PriceLimit, C_Currency_ID, EnforcePriceLimit
	 *		- UOM
	 *	Calls Tax
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
		setCalloutActive(true);
		mTab.setValue("C_Charge_ID", null);
		
		//	Set Attribute
		if (Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_Product_ID") == M_Product_ID.intValue()
			&& Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_AttributeSetInstance_ID") != 0)
			mTab.setValue("M_AttributeSetInstance_ID", new Integer(Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_AttributeSetInstance_ID")));
		else
			mTab.setValue("M_AttributeSetInstance_ID", null);

		/*****	Price Calculation see also qty	****/
		boolean IsSOTrx = Env.getContext(ctx, WindowNo, "IsSOTrx").equals("Y");
		int C_BPartner_ID = Env.getContextAsInt(ctx, WindowNo, WindowNo, "C_BPartner_ID");
		BigDecimal Qty = (BigDecimal)mTab.getValue("QtyInvoiced");
		MProductPricing pp = new MProductPricing (M_Product_ID.intValue(), C_BPartner_ID, Qty, IsSOTrx);
		//
		int M_PriceList_ID = Env.getContextAsInt(ctx, WindowNo, "M_PriceList_ID");
		pp.setM_PriceList_ID(M_PriceList_ID);
		int M_PriceList_Version_ID = Env.getContextAsInt(ctx, WindowNo, "M_PriceList_Version_ID");
		pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
		Timestamp date = Env.getContextAsDate(ctx, WindowNo, "DateInvoiced");
		pp.setPriceDate(date);
		//		
		mTab.setValue("PriceList", pp.getPriceList());
		mTab.setValue("PriceLimit", pp.getPriceLimit());
		mTab.setValue("PriceActual", pp.getPriceStd());
		mTab.setValue("PriceEntered", pp.getPriceStd());
		mTab.setValue("C_Currency_ID", new Integer(pp.getC_Currency_ID()));
		mTab.setValue("Discount", pp.getDiscount());
		mTab.setValue("C_UOM_ID", new Integer(pp.getC_UOM_ID()));
		
		int C_Location_ID = Env.getContextAsInt(ctx, WindowNo, "Bill_Location_ID");
		MLocation location = new MLocation(Env.getCtx(),C_Location_ID,null);
		//mTab.setValue("C_Tax_ID", MProductTax.getCondicionIVA(M_Product_ID.intValue(), C_BPartner_ID, location.getC_Jurisdiccion_ID()));
		
                /*
                 *  Zynnia 21/09/2012
                 *  Verificamos si el Socio de Negocio es exento, creamos las lineas exentas, sino
                 *  verificamos si el producto es o no exento.(Decide este ultimo)
                 * 
                 */
                MBPartner bp = new MBPartner (Env.getCtx(), C_BPartner_ID, null);
                int condIVAsocio = bp.getCONDICIONIVA_ID();
                BigDecimal porcentaje = bp.getIVA();
                int EXENTO = 1000055;
                
                /*
                 *  Zynnia 30/11/2012
                 *  Modificación para que el excento alcance a Responsable Monotributo y a Consumidor Final
                 *  condIVAsocio = 1000010 para Excento
                 *  condIVAsocio = 1000000 para Responsable Monotributo
                 *  condIVAsocio = 1000011 para Consumidor Final
                 *  condIVAsocio = 1000100 para Exportación
                 * 
                 */
                
                if (condIVAsocio==1000010 || condIVAsocio==1000000 || condIVAsocio==1000011 || condIVAsocio==1000100){
                    mTab.setValue("C_Tax_ID", EXENTO);
                }
                else
                {
                    MProduct prod = MProduct.get(Env.getCtx(), M_Product_ID.intValue());
                    int condIVAprod = prod.getC_TaxCategory_ID();
                    if (condIVAprod == 1000021){
                        mTab.setValue("C_Tax_ID", EXENTO);
                    }
                    else
                    {
                        if (porcentaje.intValue() == 21){
                            mTab.setValue("C_Tax_ID",1000052);
                        }
                        if (porcentaje.intValue() == 27){
                            mTab.setValue("C_Tax_ID",1000053);
                        }
                        if (porcentaje.intValue() == 10.5){
                            mTab.setValue("C_Tax_ID",1000054);
                        }
                    }
                }
		Env.setContext(ctx, WindowNo, "EnforcePriceLimit", pp.isEnforcePriceLimit() ? "Y" : "N");
		Env.setContext(ctx, WindowNo, "DiscountSchema", pp.isDiscountSchema() ? "Y" : "N");
		//
		setCalloutActive(false);
		return tax (ctx, WindowNo, mTab, mField, value);
	}	//	product

	/**
	 *	Invoice Line - Charge.
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
			log.log(Level.SEVERE, sql + e);
			return e.getLocalizedMessage();
		}
		//  CREADO POR BISION - MATIAS MAENZA - 17/04/08
		// para completar el combo codigo de regimen 
                    boolean IsSOTrx = Env.getContext(ctx, WindowNo, "IsSOTrx").equals("Y");
                    if(!IsSOTrx){
                        String sqlQuery = "select taxtype from c_charge where c_charge_id="+C_Charge_ID.intValue()+" and istax='Y'";
                        Integer valueReg;
                        try {
                            PreparedStatement pstmt = DB.prepareStatement(sqlQuery);
                            ResultSet rs = pstmt.executeQuery();
                            if(rs.next()){
                               ((MLookup)mTab.getField("C_REGIM_RETEN_PERCEP_RECIB_ID").getLookup()).removeAllElements();
                               sql = "select C_REGIM_RETEN_PERCEP_RECIB_id,codigo_regimen from C_REGIM_RETEN_PERCEP_RECIB where C_REGIM_RETEN_PERCEP_RECIB.tipo_percep='"+rs.getString(1)+"'";
                               PreparedStatement pstmt2 = DB.prepareStatement(sql);
                               ResultSet rs2 = pstmt2.executeQuery();
                               KeyNamePair k=null;
                               while (rs2.next()) { 
                                    valueReg= new Integer (((Long)rs2.getLong(1)).intValue()); 
                                    k= new KeyNamePair(valueReg.intValue(),rs2.getString(2));
                                    ((MLookup)mTab.getField("C_REGIM_RETEN_PERCEP_RECIB_ID").getLookup()).addElement(k);
                                }

                               ((MLookup)mTab.getField("C_REGIM_RETEN_PERCEP_RECIB_ID").getLookup()).refresh(true);
                            }

                        }catch (Exception exception) {
                            exception.printStackTrace();
                            return "error";
                      }
                    }                 
                //
	    
                int C_BPartner_ID = Env.getContextAsInt(ctx, WindowNo, WindowNo, "C_BPartner_ID");
                int C_Location_ID = Env.getContextAsInt(ctx, WindowNo, "Bill_Location_ID");
                MLocation location = new MLocation(Env.getCtx(),C_Location_ID,null);
                mTab.setValue("C_Tax_ID", MProductTax.getCondicionIVA(0, C_BPartner_ID, location.getC_Jurisdiccion_ID()));
                /*
                 *  15/10/2012 Zynnia
                 *  Agregamos para que ponga la condicion del IVA en base a la del Socio de negocio y a la del cargo.
                 *  Si la del socio o la del cargo es Exento, queda Exento, sino, queda la condicion del socio de negocio.
                 * 
                 */
                MBPartner bp = new MBPartner (Env.getCtx(), C_BPartner_ID, null);
                int condIVAsocio = bp.getCONDICIONIVA_ID();
                BigDecimal porcentaje = bp.getIVA();
                int EXENTO = 1000055;
                
                /*
                 *  Zynnia 30/11/2012
                 *  Modificación para que el excento alcance a Responsable Monotributo y a Consumidor Final
                 *  condIVAsocio = 1000010 para Excento
                 *  condIVAsocio = 1000000 para Responsable Monotributo
                 *  condIVAsocio = 1000011 para Consumidor Final
                 *  condIVAsocio = 1000100 para Exportación
                 * 
                 */
                
                if (condIVAsocio==1000010 || condIVAsocio==1000000 || condIVAsocio==1000011 || condIVAsocio==1000100){
                    mTab.setValue("C_Tax_ID", EXENTO);
                }
                else
                {
                    MCharge charge = MCharge.get(Env.getCtx(), C_Charge_ID.intValue());
                    int condIVAcharge = charge.getC_TaxCategory_ID();
                    if (condIVAcharge == 1000021){
                        mTab.setValue("C_Tax_ID", EXENTO);
                    }
                    else
                    {
                        if (porcentaje.intValue() == 21){
                            mTab.setValue("C_Tax_ID",1000052);
                        }
                        if (porcentaje.intValue() == 27){
                            mTab.setValue("C_Tax_ID",1000053);
                        }
                        if (porcentaje.intValue() == 10.5){
                            mTab.setValue("C_Tax_ID",1000054);
                        }
                    }
                }
                
                
                return tax (ctx, WindowNo, mTab, mField, value);
	}	//	charge
        
        public String impuesto(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value){
            //  CREADO POR BISION - MATIAS MAENZA - 19/05/08
		// requerimiento 34 para organismos de control
            if(value!=null){
                boolean IsSOTrx = Env.getContext(ctx, WindowNo, "IsSOTrx").equals("Y");
                if(!IsSOTrx){
                    Integer C_Charge_ID = (Integer)value;    
                    String sqlQuery = "select * from c_charge where c_charge_id="+C_Charge_ID.intValue()+" and istax='Y' and (C_TAXCATEGORY_ID=1000021 or C_TAXCATEGORY_ID=1000023)";
                    try {
                            PreparedStatement pstmt = DB.prepareStatement(sqlQuery);
                            ResultSet rs = pstmt.executeQuery();
                            if(rs.next()){
                                mTab.setValue("C_Tax_ID", 1000056);
                                
                            }

                        }catch (Exception exception) {
                            exception.printStackTrace();
                            return "error";
                      }                
                }
            }            
            return "";
        }
        
        
         public String codeRegim(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value){
            //Bision 14/07/08
            // Setea el codigo de regimen q se encuentra oculto, segun el codigo de jurisdiccion selecionado 
            // requerimiento 34 segunda parte para organismos de control
            Integer value1= ((Integer)mTab.getField("C_Charge_ID").getValue());
            if(value1!=null){
                boolean IsSOTrx = Env.getContext(ctx, WindowNo, "IsSOTrx").equals("Y");
                if(!IsSOTrx){
                    Integer C_Charge_ID = (Integer)value1;    
                    String sqlQuery = "select * from c_charge where c_charge_id="+C_Charge_ID.intValue()+" and istax='Y' and (C_TAXCATEGORY_ID=1000021 or C_TAXCATEGORY_ID=1000023)";
                    try {
                            PreparedStatement pstmt = DB.prepareStatement(sqlQuery);
                            ResultSet rs = pstmt.executeQuery();
                            if(rs.next()){
                                //mTab.setValue("C_Tax_ID", 1000056);
                                if (value != null ){
                                    String cod=(String)value;
                                    Integer codigo_jurisdiccion= Integer.valueOf(cod);
                                    if ( codigo_jurisdiccion != null ){
                                        sqlQuery ="select C_REGIM_RETEN_PERCEP_RECIB_ID from C_REGIM_RETEN_PERCEP_RECIB where C_REGIM_RETEN_PERCEP_RECIB.T_CODIGOJURISDICCION_ID="+codigo_jurisdiccion ;
                                        PreparedStatement pstmt1 = DB.prepareStatement(sqlQuery);
                                        ResultSet rs1 = pstmt1.executeQuery();
                                        if (rs1.next()){
                                            int cod_regim=((Long)rs1.getLong(1)).intValue();
                                            mTab.setValue("C_REGIM_RETEN_PERCEP_RECIB_ID",cod_regim);

                                        }
                                    }
                               } 
                            }

                        }catch (Exception exception) {
                            exception.printStackTrace();
                            return "error";
                      }                
                }
            }            
            return "";
        }

	/**
	 *	Invoice Line - Tax.
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
			return amt (ctx, WindowNo, mTab, mField, value);	//

		//	Check Partner Location
		int shipC_BPartner_Location_ID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_Location_ID");
		if (shipC_BPartner_Location_ID == 0)
			return amt (ctx, WindowNo, mTab, mField, value);	//
		log.fine("Ship BP_Location=" + shipC_BPartner_Location_ID);
		int billC_BPartner_Location_ID = shipC_BPartner_Location_ID;
		log.fine("Bill BP_Location=" + billC_BPartner_Location_ID);

		//	Dates
		Timestamp billDate = Env.getContextAsDate(ctx, WindowNo, "DateInvoiced");
		log.fine("Bill Date=" + billDate);
		Timestamp shipDate = billDate;
		log.fine("Ship Date=" + shipDate);

		int AD_Org_ID = Env.getContextAsInt(ctx, WindowNo, "AD_Org_ID");
		log.fine("Org=" + AD_Org_ID);

		int M_Warehouse_ID = Env.getContextAsInt(ctx, "#M_Warehouse_ID");
		log.fine("Warehouse=" + M_Warehouse_ID);
		
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
		
		int C_Tax_ID = Tax.get(ctx, M_Product_ID, C_Charge_ID, billDate, shipDate,
			AD_Org_ID, M_Warehouse_ID, billC_BPartner_Location_ID, shipC_BPartner_Location_ID,
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
	 *		- called from QtyInvoiced, PriceActual
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

	//	log.log(Level.WARNING,"amt - init");
		int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, "C_UOM_ID");
		int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, "M_Product_ID");
		int M_PriceList_ID = Env.getContextAsInt(ctx, WindowNo, "M_PriceList_ID");
		int StdPrecision = MPriceList.getStandardPrecision(ctx, M_PriceList_ID);
		BigDecimal QtyEntered, QtyInvoiced, PriceEntered, PriceActual, PriceLimit, Discount, PriceList;
		//	get values
		QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
		QtyInvoiced = (BigDecimal)mTab.getValue("QtyInvoiced");
		log.fine("QtyEntered=" + QtyEntered + ", Invoiced=" + QtyInvoiced + ", UOM=" + C_UOM_To_ID);
		//
		PriceEntered = (BigDecimal)mTab.getValue("PriceEntered");
		PriceActual = (BigDecimal)mTab.getValue("PriceActual");
		Discount = (BigDecimal)mTab.getValue("Discount");
		PriceLimit = (BigDecimal)mTab.getValue("PriceLimit");
		PriceList = (BigDecimal)mTab.getValue("PriceList");
		log.fine("PriceList=" + PriceList + ", Limit=" + PriceLimit + ", Precision=" + StdPrecision);
		log.fine("PriceEntered=" + PriceEntered + ", Actual=" + PriceActual);// + ", Discount=" + Discount);

		//	Qty changed - recalc price
		if ((mField.getColumnName().equals("QtyInvoiced") 
			|| mField.getColumnName().equals("QtyEntered")
			|| mField.getColumnName().equals("M_Product_ID")) 
			&& !"N".equals(Env.getContext(ctx, WindowNo, "DiscountSchema")))
		{
			int C_BPartner_ID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID");
			if (mField.getColumnName().equals("QtyEntered"))
				QtyInvoiced = MUOMConversion.convertProductTo (ctx, M_Product_ID, 
					C_UOM_To_ID, QtyEntered);
			if (QtyInvoiced == null)
				QtyInvoiced = QtyEntered;
			boolean IsSOTrx = Env.getContext(ctx, WindowNo, "IsSOTrx").equals("Y");
			MProductPricing pp = new MProductPricing (M_Product_ID, C_BPartner_ID, QtyInvoiced, IsSOTrx);
			pp.setM_PriceList_ID(M_PriceList_ID);
			int M_PriceList_Version_ID = Env.getContextAsInt(ctx, WindowNo, "M_PriceList_Version_ID");
			pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
			Timestamp date = (Timestamp)mTab.getValue("DateInvoiced");
			pp.setPriceDate(date);
			//
			PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, pp.getPriceStd());
			if (PriceEntered == null)
				PriceEntered = pp.getPriceStd();
			//
			log.fine("amt - QtyChanged -> PriceActual=" + pp.getPriceStd() 
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
			log.fine("amt - PriceActual=" + PriceActual 
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
			log.fine("amt - PriceEntered=" + PriceEntered 
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
				Discount = Discount.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
			mTab.setValue("Discount", Discount);
		}
		log.fine("amt = PriceEntered=" + PriceEntered + ", Actual" + PriceActual + ", Discount=" + Discount);
		/* */

		//	Check PriceLimit
		if (!(mField.getColumnName().equals("QtyInvoiced") 
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
				log.fine("amt =(under) PriceEntered=" + PriceEntered + ", Actual" + PriceLimit);
				mTab.setValue ("PriceActual", PriceLimit);
				mTab.setValue ("PriceEntered", PriceEntered);
				mTab.fireDataStatusEEvent ("UnderLimitPrice", "", false);

				//	Repeat Discount calc
				if (PriceList.intValue() != 0)
				{
					Discount = new BigDecimal ((PriceList.doubleValue () - PriceActual.doubleValue ()) / PriceList.doubleValue () * 100.0);
					if (Discount.scale () > 2)
						Discount = Discount.setScale (2, BigDecimal.ROUND_HALF_UP);
				//	mTab.setValue ("Discount", Discount);
				}
			}
		}

		//	Line Net Amt
		BigDecimal LineNetAmt = QtyInvoiced.multiply(PriceActual);
		if (LineNetAmt.scale() > StdPrecision)
			LineNetAmt = LineNetAmt.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
		log.info("amt = LineNetAmt=" + LineNetAmt);
		mTab.setValue("LineNetAmt", LineNetAmt);

		//	Calculate Tax Amount for PO
		boolean IsSOTrx = "Y".equals(Env.getContext(Env.getCtx(), WindowNo, "IsSOTrx"));
		if (!IsSOTrx)
		{
			BigDecimal TaxAmt = null;
			if (mField.getColumnName().equals("TaxAmt"))
			{
				TaxAmt = (BigDecimal)mTab.getValue("TaxAmt");
				System.err.println("Opción 1 : "+ TaxAmt);
			}
			else
			{
				Integer taxID = (Integer)mTab.getValue("C_Tax_ID");
				if (taxID != null)
				{
					int C_Tax_ID = taxID.intValue();
					MTax tax = new MTax (ctx, C_Tax_ID, null);
					TaxAmt = tax.calculateTax(LineNetAmt, isTaxIncluded(WindowNo), StdPrecision);
					mTab.setValue("TaxAmt", TaxAmt);
					System.err.println("Opción 2 : "+ TaxAmt);
				}
			}
			//	Add it up
			//begin vpj-cd e-evolution 25 Ene 2006
			if (TaxAmt != null) {
                            BigDecimal totalAmt = LineNetAmt.add(TaxAmt);
                            if (totalAmt.scale() > StdPrecision)
                                totalAmt = totalAmt.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
                            mTab.setValue("LineTotalAmt", totalAmt);
                        }
			
			//end vpj-cd e-evolution 25 Ene 2006
		}

		setCalloutActive(false);
		return "";
	}	//	amt

	/**
	 * 	Is Tax Included
	 *	@param WindowNo window no
	 *	@return tax included (default: false)
	 */
	private boolean isTaxIncluded (int WindowNo)
	{
		String ss = Env.getContext(Env.getCtx(), WindowNo, "IsTaxIncluded");
		//	Not Set Yet
		if (ss.length() == 0)
		{
			int M_PriceList_ID = Env.getContextAsInt(Env.getCtx(), WindowNo, "M_PriceList_ID");
			if (M_PriceList_ID == 0)
				return false;
			ss = DB.getSQLValueString(null,
				"SELECT IsTaxIncluded FROM M_PriceList WHERE M_PriceList_ID=?", 
				M_PriceList_ID);
			if (ss == null)
				ss = "N";
			Env.setContext(Env.getCtx(), WindowNo, "IsTaxIncluded", ss);
		}
		return "Y".equals(ss);
	}	//	isTaxIncluded

	/**
	 *	Invoice Line - Quantity.
	 *		- called from C_UOM_ID, QtyEntered, QtyInvoiced
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
	//	log.log(Level.WARNING,"qty - init - M_Product_ID=" + M_Product_ID);
		BigDecimal QtyInvoiced, QtyEntered, PriceActual, PriceEntered;
		
		//	No Product
		if (M_Product_ID == 0)
		{
			QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
			mTab.setValue("QtyInvoiced", QtyEntered);
		}
		//	UOM Changed - convert from Entered -> Product
		else if (mField.getColumnName().equals("C_UOM_ID"))
		{
			int C_UOM_To_ID = ((Integer)value).intValue();
			QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
			QtyInvoiced = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, QtyEntered);
			if (QtyInvoiced == null)
				QtyInvoiced = QtyEntered;
			boolean conversion = QtyEntered.compareTo(QtyInvoiced) != 0;
			PriceActual = (BigDecimal)mTab.getValue("PriceActual");
			PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, PriceActual);
			if (PriceEntered == null)
				PriceEntered = PriceActual; 
			log.fine("qty - UOM=" + C_UOM_To_ID 
				+ ", QtyEntered/PriceActual=" + QtyEntered + "/" + PriceActual
				+ " -> " + conversion 
				+ " QtyInvoiced/PriceEntered=" + QtyInvoiced + "/" + PriceEntered);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyInvoiced", QtyInvoiced);
			mTab.setValue("PriceEntered", PriceEntered);
		}
		//	QtyEntered changed - calculate QtyInvoiced
		else if (mField.getColumnName().equals("QtyEntered"))
		{
			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, "C_UOM_ID");
			QtyEntered = (BigDecimal)value;
			QtyInvoiced = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, QtyEntered);
			if (QtyInvoiced == null)
				QtyInvoiced = QtyEntered;
			boolean conversion = QtyEntered.compareTo(QtyInvoiced) != 0;
			log.fine("qty - UOM=" + C_UOM_To_ID 
				+ ", QtyEntered=" + QtyEntered
				+ " -> " + conversion 
				+ " QtyInvoiced=" + QtyInvoiced);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyInvoiced", QtyInvoiced);
		}
		//	QtyInvoiced changed - calculate QtyEntered
		else if (mField.getColumnName().equals("QtyInvoiced"))
		{
			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, "C_UOM_ID");
			QtyInvoiced = (BigDecimal)value;
			QtyEntered = MUOMConversion.convertProductTo (ctx, M_Product_ID, 
				C_UOM_To_ID, QtyInvoiced);
			if (QtyEntered == null)
				QtyEntered = QtyInvoiced;
			boolean conversion = QtyInvoiced.compareTo(QtyEntered) != 0;
			log.fine("qty - UOM=" + C_UOM_To_ID 
				+ ", QtyInvoiced=" + QtyInvoiced
				+ " -> " + conversion 
				+ " QtyEntered=" + QtyEntered);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyEntered", QtyEntered);
		}
		//
		setCalloutActive(false);
		return "";
	}	//	qty
	
        public String netInvoiced (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
                MOrderLine oLine = new MOrderLine (ctx, (Integer)mTab.getValue("C_OrderLine_ID"), null);
                BigDecimal montoOC = oLine.getLineNetAmt(); 
                BigDecimal montoLine = (BigDecimal)mTab.getValue("LineNetAmt");
                BigDecimal porc = new BigDecimal(0.05);
                porc = porc.setScale(2,BigDecimal.ROUND_DOWN);
                BigDecimal montoOCporc= (montoOC.multiply(porc)).add(montoOC);
                if (montoLine.compareTo(montoOCporc)!=(-1)){
                        JOptionPane.showMessageDialog(null, "El valor ingresado en la linea supera el umbral aceptado.", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
		
		//
		setCalloutActive(false);
		return "";
	}	//	qtyInvoiced
	
}	//	CalloutInvoice