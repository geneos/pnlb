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

import java.awt.HeadlessException;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;
import javax.swing.JOptionPane;

/**
 *	Business Partner Model
 *
 *  @author Jorg Janke
 *  @version $Id: MBPartner.java,v 1.58 2006/01/04 05:39:21 jjanke Exp $
 */
public class MBPartner extends X_C_BPartner
{
	/**
	 * 	Get Empty Template Business Partner
	 * 	@param ctx context
	 * 	@param AD_Client_ID client
	 * 	@return Template Business Partner or null
	 */
	public static MBPartner getTemplate (Properties ctx, int AD_Client_ID)
	{
		MBPartner template = getBPartnerCashTrx (ctx, AD_Client_ID);
		if (template == null)
			template = new MBPartner (ctx, 0, null);
		//	Reset
		if (template != null)
		{
			template.set_ValueNoCheck ("C_BPartner_ID", new Integer(0));
			template.setValue ("");
			template.setName ("");
			template.setName2 (null);
			template.setDUNS("");
			template.setFirstSale(null);
			//
			template.setSO_CreditLimit (Env.ZERO);
			template.setSO_CreditUsed (Env.ZERO);
			template.setTotalOpenBalance (Env.ZERO);
		//	s_template.setRating(null);
			//
			template.setActualLifeTimeValue(Env.ZERO);
			template.setPotentialLifeTimeValue(Env.ZERO);
			template.setAcqusitionCost(Env.ZERO);
			template.setShareOfCustomer(0);
			template.setSalesVolume(0);
		}
		return template;
	}	//	getTemplate

	/**
	 * 	Get Cash Trx Business Partner
	 * 	@param ctx context
	 * 	@param AD_Client_ID client
	 * 	@return Cash Trx Business Partner or null
	 */
	public static MBPartner getBPartnerCashTrx (Properties ctx, int AD_Client_ID)
	{
		MBPartner retValue = null;
		String sql = "SELECT * FROM C_BPartner "
			+ "WHERE C_BPartner_ID=(SELECT C_BPartnerCashTrx_ID FROM AD_ClientInfo WHERE AD_Client_ID=?)";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				retValue = new MBPartner (ctx, rs, null);
			else
				s_log.log(Level.SEVERE, "Not found for AD_Client_ID=" + AD_Client_ID);
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e)
			{}
			pstmt = null;
		}
		return retValue;
	}	//	getBPartnerCashTrx

	/**
	 * 	Get BPartner with Value
	 *	@param ctx context 
	 *	@param Value value
	 *	@return BPartner or null
	 */
	public static MBPartner get (Properties ctx, String Value)
	{
		if (Value == null || Value.length() == 0)
			return null;
		MBPartner retValue = null;
		int AD_Client_ID = Env.getAD_Client_ID(ctx);
		String sql = "SELECT * FROM C_BPartner WHERE Value=? AND AD_Client_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setString(1, Value);
			pstmt.setInt(2, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MBPartner(ctx, rs, null);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
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
	 * 	Get Not Invoiced Shipment Value
	 *	@param C_BPartner_ID partner
	 *	@return value in accounting currency
	 */
	public static BigDecimal getNotInvoicedAmt (int C_BPartner_ID)
	{
		BigDecimal retValue = null;
		/*
		 * 18-02-2011 Camarzana Mariano
		 * Modificado para que solo tome en cuentas las OV que esten en estado "CO" - "CL"
		 * agregado (o.docstatus in ('CO','CL')) 
		 */
		String sql = "SELECT SUM(COALESCE("
			+ "currencyBase((ol.QtyDelivered-ol.QtyInvoiced)*ol.PriceActual,o.C_Currency_ID,o.DateOrdered, o.AD_Client_ID,o.AD_Org_ID) ,0)) "
			+ "FROM C_OrderLine ol"
			+ " INNER JOIN C_Order o ON (ol.C_Order_ID=o.C_Order_ID) "
			+ "WHERE o.IsSOTrx='Y' AND  o.docstatus in ('CO','CL') AND Bill_BPartner_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, C_BPartner_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = rs.getBigDecimal(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
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
	}	//	getNotInvoicedAmt

	
	/**	Static Logger				*/
	private static CLogger		s_log = CLogger.getCLogger (MBPartner.class);

	
	/*
	 * Cambiar para no referenciar directamente los ID
	 */
	public static String CIVA_EXENTO = "EX";
	public static String CIVA_MONOTRIBUTISTA = "RM";
	public static String CIVA_RESPINSCRIPTO = "RI";
	public static String CIVA_CONSUMIDORFINAL = "CF"; 
	public static String CIVA_EXPORTACION = "OE";
	
	public static int CGAN_EXENTO =	1000010;
	public static int CGAN_NOCATEGORIZADO = 1000011;
	public static int CGAN_RESPINSCRIPTO = 1000001;
	
	/**************************************************************************
	 * 	Constructor for new BPartner from Template
	 * 	@param ctx context
	 */
	public MBPartner (Properties ctx)
	{
		this (ctx, -1, null);
	}	//	MBPartner

	/**
	 * 	Default Constructor
	 * 	@param ctx context
	 * 	@param rs ResultSet to load from
	 */
	public MBPartner (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MBPartner

	/**
	 * 	Default Constructor
	 * 	@param ctx context
	 * 	@param C_BPartner_ID partner or 0 or -1 (load from template)
	 * 	@param trxName trx name
	 */
	public MBPartner (Properties ctx, int C_BPartner_ID, String trxName)
	{
		super (ctx, C_BPartner_ID, trxName);
		//
		if (C_BPartner_ID == -1)
		{
			initTemplate (Env.getContextAsInt(ctx, "AD_Client_ID"));
			C_BPartner_ID = 0;
		}
		if (C_BPartner_ID == 0)
		{
		//	setValue ("");
		//	setName ("");
		//	setName2 (null);
		//	setDUNS("");
			//
			setIsCustomer (true);
			setIsProspect (true);
			//
			setSendEMail (false);
			setIsOneTime (false);
			setIsVendor (false);
			setIsSummary (false);
			setIsEmployee (false);
			setIsSalesRep (false);
			setIsTaxExempt(false);
			setIsDiscountPrinted(false);
			//
			setSO_CreditLimit (Env.ZERO);
			setSO_CreditUsed (Env.ZERO);
			setTotalOpenBalance (Env.ZERO);
			setSOCreditStatus(SOCREDITSTATUS_NoCreditCheck);
			//
			setFirstSale(null);
			setActualLifeTimeValue(Env.ZERO);
			setPotentialLifeTimeValue(Env.ZERO);
			setAcqusitionCost(Env.ZERO);
			setShareOfCustomer(0);
			setSalesVolume(0);
		}
		log.fine(toString());
	}	//	MBPartner

	/**
	 * 	Import Contstructor
	 *	@param impBP import
	 */
	public MBPartner (X_I_BPartner impBP)
	{
		this (impBP.getCtx(), 0, impBP.get_TrxName());
		setClientOrg(impBP);
		setUpdatedBy(impBP.getUpdatedBy());
		//
		String value = impBP.getValue();
		if (value == null || value.length() == 0)
			value = impBP.getEMail();
		if (value == null || value.length() == 0)
			value = impBP.getContactName();
		setValue(value);
		String name = impBP.getName();
		if (name == null || name.length() == 0)
			name = impBP.getContactName();
		if (name == null || name.length() == 0)
			name = impBP.getEMail();
		setName(name);
		setName2(impBP.getName2());
		setDescription(impBP.getDescription());
	//	setHelp(impBP.getHelp());
		setDUNS(impBP.getDUNS());
		setTaxID(impBP.getTaxID());
		setNAICS(impBP.getNAICS());
		setC_BP_Group_ID(impBP.getC_BP_Group_ID());
	}	//	MBPartner
	
	
	/** Users							*/
	private MUser[]					m_contacts = null;
	/** Addressed						*/
	private MBPartnerLocation[]		m_locations = null;
	/** BP Bank Accounts				*/
	private MBPBankAccount[]		m_accounts = null;
	/** Prim Address					*/
	private Integer					m_primaryC_BPartner_Location_ID = null;
	/** Prim User						*/
	private Integer					m_primaryAD_User_ID = null;
	/** Credit Limit recently calcualted		*/
	private boolean 				m_TotalOpenBalanceSet = false;
	/** BP Group						*/
	private MBPGroup				m_group = null;
	
	/**
	 * 	Load Default BPartner
	 * 	@param AD_Client_ID client
	 */
	private boolean initTemplate (int AD_Client_ID)
	{
		if (AD_Client_ID == 0)
			throw new IllegalArgumentException ("Client_ID=0");

		boolean success = true;
		String sql = "SELECT * FROM C_BPartner "
			+ "WHERE C_BPartner_ID=(SELECT C_BPartnerCashTrx_ID FROM AD_ClientInfo WHERE AD_Client_ID=?)";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				success = load (rs);
			else
			{
				load(0, null);
				success = false;
				log.severe ("None found");
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e)
			{}
			pstmt = null;
		}
		setStandardDefaults();
		//	Reset
		set_ValueNoCheck ("C_BPartner_ID", I_ZERO);
		setValue ("");
		setName ("");
		setName2(null);
		return success;
	}	//	getTemplate


	/**
	 * 	Get All Contacts
	 * 	@param reload if true users will be requeried
	 *	@return contacts
	 */
	public MUser[] getContacts (boolean reload)
	{
		if (reload || m_contacts == null || m_contacts.length == 0)
			;
		else
			return m_contacts;
		//
		ArrayList<MUser> list = new ArrayList<MUser>();
		String sql = "SELECT * FROM AD_User WHERE C_BPartner_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, getC_BPartner_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MUser (getCtx(), rs, null));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e)
			{}
			pstmt = null;
		}

		m_contacts = new MUser[list.size()];
		list.toArray(m_contacts);
		return m_contacts;
	}	//	getContacts

	/**
	 * 	Get specified or first Contact
	 * 	@param AD_User_ID optional user
	 *	@return contact or null
	 */
	public MUser getContact (int AD_User_ID)
	{
		MUser[] users = getContacts(false);
		if (users.length == 0)
			return null;
		for (int i = 0; AD_User_ID != 0 && i < users.length; i++)
		{
			if (users[i].getAD_User_ID() == AD_User_ID)
				return users[i];
		}
		return users[0];
	}	//	getContact
	
	
	/**
	 * 	Get All Locations
	 * 	@param reload if true locations will be requeried
	 *	@return locations
	 */
	public MBPartnerLocation[] getLocations (boolean reload)
	{
		if (reload || m_locations == null || m_locations.length == 0)
			;
		else
			return m_locations;
		//
		ArrayList<MBPartnerLocation> list = new ArrayList<MBPartnerLocation>();
		String sql = "SELECT * FROM C_BPartner_Location WHERE C_BPartner_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getC_BPartner_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MBPartnerLocation (getCtx(), rs, get_TrxName()));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e)
			{}
			pstmt = null;
		}

		m_locations = new MBPartnerLocation[list.size()];
		list.toArray(m_locations);
		return m_locations;
	}	//	getLocations

	/**
	 * 	Get explicit or first bill Location
	 * 	@param C_BPartner_Location_ID optional explicit location
	 *	@return location or null
	 */
	public MBPartnerLocation getLocation(int C_BPartner_Location_ID)
	{
		MBPartnerLocation[] locations = getLocations(false);
		if (locations.length == 0)
			return null;
		MBPartnerLocation retValue = null;
		for (int i = 0; i < locations.length; i++)
		{
			if (locations[i].getC_BPartner_Location_ID() == C_BPartner_Location_ID)
				return locations[i];
			if (retValue == null && locations[i].isBillTo())
				retValue = locations[i];
		}
		if (retValue == null)
			return locations[0];
		return retValue;
	}	//	getLocation
	
	
	/**
	 * 	Get Bank Accounts
	 * 	@param requery requery
	 *	@return Bank Accounts
	 */
	public MBPBankAccount[] getBankAccounts (boolean requery)
	{
		if (m_accounts != null && m_accounts.length >= 0 && !requery)	//	re-load
			return m_accounts;
		//
		ArrayList<MBPBankAccount> list = new ArrayList<MBPBankAccount>();
		String sql = "SELECT * FROM C_BP_BankAccount WHERE C_BPartner_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getC_BPartner_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MBPBankAccount (getCtx(), rs, get_TrxName()));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e)
			{}
			pstmt = null;
		}

		m_accounts = new MBPBankAccount[list.size()];
		list.toArray(m_accounts);
		return m_accounts;
	}	//	getBankAccounts

	
	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MBPartner[ID=")
			.append(get_ID())
			.append(",Value=").append(getValue())
			.append(",Name=").append(getName())
			.append(",Open=").append(getTotalOpenBalance())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Set Client/Org
	 *	@param AD_Client_ID client
	 *	@param AD_Org_ID org
	 */
	public void setClientOrg (int AD_Client_ID, int AD_Org_ID)
	{
		super.setClientOrg(AD_Client_ID, AD_Org_ID);
	}	//	setClientOrg

	/**
	 * 	Set Linked Organization.
	 * 	(is Button)
	 *	@param AD_OrgBP_ID 
	 */
	public void setAD_OrgBP_ID (int AD_OrgBP_ID)
	{
		if (AD_OrgBP_ID == 0)
			super.setAD_OrgBP_ID (null);
		else
			super.setAD_OrgBP_ID (String.valueOf(AD_OrgBP_ID));
	}	//	setAD_OrgBP_ID
	
	/** 
	 * 	Get Linked Organization.
	 * 	(is Button)
	 * 	The Business Partner is another Organization 
	 * 	for explicit Inter-Org transactions 
	 */
	public int getAD_OrgBP_ID_Int() 
	{
		String org = super.getAD_OrgBP_ID();
		if (org == null)
			return 0;
		int AD_OrgBP_ID = 0;
		try
		{
			AD_OrgBP_ID = Integer.parseInt (org);
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, org, ex);
		}
		return AD_OrgBP_ID;
	}	//	getAD_OrgBP_ID_Int

	
	
	/**
	 * 	Get Primary C_BPartner_Location_ID
	 *	@return C_BPartner_Location_ID
	 */
	public int getPrimaryC_BPartner_Location_ID()
	{
		if (m_primaryC_BPartner_Location_ID == null)
		{
			MBPartnerLocation[] locs = getLocations(false);
			for (int i = 0; m_primaryC_BPartner_Location_ID == null && i < locs.length; i++)
			{
				if (locs[i].isBillTo())
				{
					setPrimaryC_BPartner_Location_ID (locs[i].getC_BPartner_Location_ID());
					break;
				}
			}
			//	get first
			if (m_primaryC_BPartner_Location_ID == null && locs.length > 0)
				setPrimaryC_BPartner_Location_ID (locs[0].getC_BPartner_Location_ID()); 
		}
		if (m_primaryC_BPartner_Location_ID == null)
			return 0;
		return m_primaryC_BPartner_Location_ID.intValue();
	}	//	getPrimaryC_BPartner_Location_ID
	
	/**
	 * 	Get Primary AD_User_ID
	 *	@return AD_User_ID
	 */
	public int getPrimaryAD_User_ID()
	{
		if (m_primaryAD_User_ID == null)
		{
			MUser[] users = getContacts(false);
		//	for (int i = 0; i < users.length; i++)
		//	{
		//	}
			if (m_primaryAD_User_ID == null && users.length > 0)
				setPrimaryAD_User_ID(users[0].getAD_User_ID());
		}
		if (m_primaryAD_User_ID == null)
			return 0;
		return m_primaryAD_User_ID.intValue();
	}	//	getPrimaryAD_User_ID

	/**
	 * 	Set Primary C_BPartner_Location_ID
	 *	@param C_BPartner_Location_ID id
	 */
	public void setPrimaryC_BPartner_Location_ID(int C_BPartner_Location_ID)
	{
		m_primaryC_BPartner_Location_ID = new Integer (C_BPartner_Location_ID);
	}	//	setPrimaryC_BPartner_Location_ID
	
	/**
	 * 	Set Primary AD_User_ID
	 *	@param AD_User_ID id
	 */
	public void setPrimaryAD_User_ID(int AD_User_ID)
	{
		m_primaryAD_User_ID = new Integer (AD_User_ID);
	}	//	setPrimaryAD_User_ID
	
	
	/**
	 * 	Calculate Total Open Balance and SO_CreditUsed.
	 *  (includes drafted invoices)
	 */
	public void setTotalOpenBalance ()
	{
		BigDecimal SO_CreditUsed = null;
		BigDecimal TotalOpenBalance = null;
		
                String sql = "SELECT "
			//	SO Credit Used
			+ " COALESCE((SELECT SUM(currencyBase(invoiceOpen(i.C_Invoice_ID,i.C_InvoicePaySchedule_ID),i.C_Currency_ID,i.DateOrdered, i.AD_Client_ID,i.AD_Org_ID)) FROM C_Invoice_v i "
				+ " WHERE i.DocStatus IN ('CO','CL') AND i.C_BPartner_ID=bp.C_BPartner_ID AND i.IsSOTrx='Y' AND i.IsPaid='N'),0), "
			//	Balance (incl. unallocated payments)
			+ " COALESCE((SELECT SUM(currencyBase(invoiceOpen(i.C_Invoice_ID,i.C_InvoicePaySchedule_ID),i.C_Currency_ID,i.DateOrdered, i.AD_Client_ID,i.AD_Org_ID)*i.MultiplierAP) FROM C_Invoice_v i "
				+ " WHERE i.DocStatus IN ('CO','CL') AND i.C_BPartner_ID=bp.C_BPartner_ID AND i.IsPaid='N'),0) - "
			+ " COALESCE((SELECT SUM(currencyBase(p.PAYAMT-paymentAllocated(p.C_Payment_ID, p.C_Currency_ID),p.C_Currency_ID,p.DateTrx,p.AD_Client_ID,p.AD_Org_ID)) FROM C_Payment_v p "
				+ " WHERE p.DocStatus IN ('CO','CL') AND p.C_BPartner_ID=bp.C_BPartner_ID AND p.IsAllocated='N'),0) "
			+ " FROM C_BPartner bp "
			+ " WHERE C_BPartner_ID=?";
                /******************
                 **      Vit4B 14/01/2008
                 **      de la consulta de actualizacion del saldo del proveedor/ cliente se reemplazo por el valor neto del pago
                 **      PayAmt => PAYNET
                String sql = "SELECT "
			//	SO Credit Used
			+ "COALESCE((SELECT SUM(currencyBase(invoiceOpen(i.C_Invoice_ID,i.C_InvoicePaySchedule_ID),i.C_Currency_ID,i.DateOrdered, i.AD_Client_ID,i.AD_Org_ID)) FROM C_Invoice_v i "
				+ "WHERE i.C_BPartner_ID=bp.C_BPartner_ID AND i.IsSOTrx='Y' AND i.IsPaid='N'),0), "
			//	Balance (incl. unallocated payments)
			+ "COALESCE((SELECT SUM(currencyBase(invoiceOpen(i.C_Invoice_ID,i.C_InvoicePaySchedule_ID),i.C_Currency_ID,i.DateOrdered, i.AD_Client_ID,i.AD_Org_ID)*i.MultiplierAP) FROM C_Invoice_v i "
				+ "WHERE i.C_BPartner_ID=bp.C_BPartner_ID AND i.IsPaid='N'),0) - "
			+ "COALESCE((SELECT SUM(currencyBase(p.PayAmt,p.C_Currency_ID,p.DateTrx,p.AD_Client_ID,p.AD_Org_ID)) FROM C_Payment_v p "
				+ "WHERE p.C_BPartner_ID=bp.C_BPartner_ID AND p.IsAllocated='N'"
				+ " AND p.C_Charge_ID IS NULL AND NOT EXISTS (SELECT * FROM C_AllocationLine al WHERE p.C_Payment_ID=al.C_Payment_ID)),0) "
			+ "FROM C_BPartner bp "
			+ "WHERE C_BPartner_ID=?";
                 ***********************/
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getC_BPartner_ID());
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				SO_CreditUsed = rs.getBigDecimal(1);
				TotalOpenBalance = rs.getBigDecimal(2);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
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
		//
		m_TotalOpenBalanceSet = true;
		if (SO_CreditUsed != null)
			super.setSO_CreditUsed (SO_CreditUsed);
		if (TotalOpenBalance != null)
			super.setTotalOpenBalance(TotalOpenBalance);
		setSOCreditStatus();
	}	//	setTotalOpenBalance

	/**
	 * 	Set Actual Life Time Value from DB
	 */
	public void setActualLifeTimeValue()
	{
		BigDecimal ActualLifeTimeValue = null;
		String sql = "SELECT "
			+ "COALESCE ((SELECT SUM(currencyBase(i.GrandTotal,i.C_Currency_ID,i.DateOrdered, i.AD_Client_ID,i.AD_Org_ID)) FROM C_Invoice_v i "
				+ "WHERE i.C_BPartner_ID=bp.C_BPartner_ID AND i.IsSOTrx='Y'),0) " 
			+ "FROM C_BPartner bp "
			+ "WHERE C_BPartner_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getC_BPartner_ID());
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				ActualLifeTimeValue = rs.getBigDecimal(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
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
		if (ActualLifeTimeValue != null)
			super.setActualLifeTimeValue (ActualLifeTimeValue);
	}	//	setActualLifeTimeValue
	
	/**
	 * 	Get Total Open Balance
	 * 	@param calculate if null calculate it
	 *	@return Open Balance
	 */
	public BigDecimal getTotalOpenBalance (boolean calculate)
	{
		if (getTotalOpenBalance().signum() == 0 && calculate)
			setTotalOpenBalance();
		return super.getTotalOpenBalance ();
	}	//	getTotalOpenBalance
	
	
	/**
	 * 	Set Credit Status
	 */
	public void setSOCreditStatus ()
	{
		BigDecimal creditLimit = getSO_CreditLimit(); 
		//	Nothing to do
		if (SOCREDITSTATUS_NoCreditCheck.equals(getSOCreditStatus())
			|| SOCREDITSTATUS_CreditStop.equals(getSOCreditStatus())
			|| Env.ZERO.compareTo(creditLimit) == 0)
			return;

		//	Above Credit Limit
		if (creditLimit.compareTo(getTotalOpenBalance(!m_TotalOpenBalanceSet)) < 0)
			setSOCreditStatus(SOCREDITSTATUS_CreditHold);
		else
		{
			//	Above Watch Limit
			BigDecimal watchAmt = creditLimit.multiply(getCreditWatchRatio());
			if (watchAmt.compareTo(getTotalOpenBalance()) < 0)
				setSOCreditStatus(SOCREDITSTATUS_CreditWatch);
			else	//	is OK
				setSOCreditStatus (SOCREDITSTATUS_CreditOK);
		}
	}	//	setSOCreditStatus
	
	
	/**
	 * 	Get SO CreditStatus with additional amount
	 * 	@param additionalAmt additional amount in Accounting Currency
	 *	@return sinulated credit status
	 */
	public String getSOCreditStatus (BigDecimal additionalAmt)
	{
		if (additionalAmt == null || additionalAmt.signum() == 0)
			return getSOCreditStatus();
		//
		BigDecimal creditLimit = getSO_CreditLimit(); 
		//	Nothing to do
		if (SOCREDITSTATUS_NoCreditCheck.equals(getSOCreditStatus())
			|| SOCREDITSTATUS_CreditStop.equals(getSOCreditStatus())
			|| Env.ZERO.compareTo(creditLimit) == 0)
			return getSOCreditStatus();

		//	Above (reduced) Credit Limit
		creditLimit = creditLimit.subtract(additionalAmt);
		if (creditLimit.compareTo(getTotalOpenBalance(!m_TotalOpenBalanceSet)) < 0)
			return SOCREDITSTATUS_CreditHold;
		
		//	Above Watch Limit
		BigDecimal watchAmt = creditLimit.multiply(getCreditWatchRatio());
		if (watchAmt.compareTo(getTotalOpenBalance()) < 0)
			return SOCREDITSTATUS_CreditWatch;
		
		//	is OK
		return SOCREDITSTATUS_CreditOK;
	}	//	getSOCreditStatus
	
	/**
	 * 	Get Credit Watch Ratio
	 *	@return BP Group ratio or 0.9
	 */
	public BigDecimal getCreditWatchRatio()
	{
		return getGroup().getCreditWatchRatio();
	}	//	getCreditWatchRatio
		
	/**
	 * 	Credit Status is Stop or Hold.
	 *	@return true if Stop/Hold
	 */
	public boolean isCreditStopHold()
	{
		String status = getSOCreditStatus();
		return SOCREDITSTATUS_CreditStop.equals(status)
			|| SOCREDITSTATUS_CreditHold.equals(status);
	}	//	isCreditStopHold
	
	/**
	 * 	Set Total Open Balance
	 *	@param TotalOpenBalance
	 */
	public void setTotalOpenBalance (BigDecimal TotalOpenBalance)
	{
		m_TotalOpenBalanceSet = false;
		super.setTotalOpenBalance (TotalOpenBalance);
	}	//	setTotalOpenBalance
	
	/**
	 * 	Get Group
	 *	@return group
	 */
	public MBPGroup getGroup()
	{
		if (m_group == null)
			m_group = MBPGroup.get(getCtx(), getC_BP_Group_ID());
		return m_group;
	}	//	getGroup
	
	/**
	 * 	Get PriceList
	 *	@return price List
	 */
	public int getM_PriceList_ID ()
	{
		int ii = super.getM_PriceList_ID();
		if (ii == 0)
			ii = getGroup().getM_PriceList_ID();
		return ii;
	}	//	getM_PriceList_ID
	
	/**
	 * 	Get PO PriceList
	 *	@return price list
	 */
	public int getPO_PriceList_ID ()
	{
		int ii = super.getPO_PriceList_ID();
		if (ii == 0)
			ii = getGroup().getPO_PriceList_ID();
		return ii;
	}	//
	
	/**
	 * 	Get DiscountSchema
	 *	@return Discount Schema
	 */
	public int getM_DiscountSchema_ID ()
	{
		int ii = super.getM_DiscountSchema_ID ();
		if (ii == 0)
			ii = getGroup().getM_DiscountSchema_ID();
		return ii;
	}	//	getM_DiscountSchema_ID
	
	/**
	 * 	Get PO DiscountSchema
	 *	@return po discount
	 */
	public int getPO_DiscountSchema_ID ()
	{
		int ii = super.getPO_DiscountSchema_ID ();
		if (ii == 0)
			ii = getGroup().getPO_DiscountSchema_ID();
		return ii;
	}	//	getPO_DiscountSchema_ID
	
	/**************************************************************************
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord & success)
		{
			//	Trees
			insert_Tree(MTree_Base.TREETYPE_BPartner);
			//	Accounting
			insert_Accounting("C_BP_Customer_Acct", "C_BP_Group_Acct", 
				"p.C_BP_Group_ID=" + getC_BP_Group_ID());
			insert_Accounting("C_BP_Vendor_Acct", "C_BP_Group_Acct", 
				"p.C_BP_Group_ID=" + getC_BP_Group_ID());
			insert_Accounting("C_BP_Employee_Acct", "C_AcctSchema_Default", null);
		}

		//	Value/Name change
		if (success && !newRecord 
			&& (is_ValueChanged("Value") || is_ValueChanged("Name")))
			MAccount.updateValueDescription(getCtx(), "C_BPartner_ID=" + getC_BPartner_ID(), get_TrxName());

		return success;
	}	//	afterSave
	
	/**
	 * 	Before Save
	 *  @param newRecord
	 */
	protected boolean beforeSave (boolean newRecord) throws HeadlessException
	{

	try {
	
		// Si es nacional, realizar verificaciones.
		if (!isExtranjero())
		{
			String cuit = getTaxID();
			if (cuit == null)
			{
				JOptionPane.showMessageDialog(null,"No a ingreseado el CUIT ","Error - Formato CUIT", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			
			if (!Cuit.validar(cuit))
			{
				JOptionPane.showMessageDialog(null,"El CUIT ingresado no es correcto: No se corresponde con el formato esperado: 99-99999999-9 \n Dígito verificador incorrecto.","Error - Formato CUIT", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			String query = " Select C_BPartner_Id FROM C_BPARTNER" +
						   " WHERE TAXID = '" + cuit + "' AND ISACTIVE='Y' " +
						   "   AND AD_Org_ID = ? AND AD_Client_ID = ?";
					
			PreparedStatement pstmt = DB.prepareStatement(query, null);
			pstmt.setInt(1, getAD_Org_ID());
			pstmt.setInt(2, getAD_Client_ID());
			ResultSet rs = pstmt.executeQuery();
				
			if (rs.next() && (getC_BPartner_ID() != rs.getLong(1)))
			{
				JOptionPane.showMessageDialog(null,"El CUIT ingresado ya existe para otro Socio de Negocio.","Error - CUIT duplicado", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		else
			if (getNro_Extranjero()==null)
			{
				JOptionPane.showMessageDialog(null,"Ingrese C�digo de Identificaci�n.","Error - Parámetro Faltante", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			else
			{
				setCONDICIONGAN_ID(CGAN_EXENTO);
				setCondicionIVACode(CIVA_EXPORTACION);
				setEXENCIONPERIB(true);
				setEXENCIONPERIVA(true);
				setIsTaxExempt(true);
			}
			
		
		setSOCreditStatus();
		
		/*	CALCULO ANTERIOR
		String query = "select VFORMAT from AD_Column co join AD_table ta on (co.ad_table_Id=ta.ad_table_Id) where co.ColumnName like 'TaxID' and ta.TABLENAME like 'C_BPartner'";
		
		PreparedStatement pstmt = DB.prepareStatement(query, null);
		ResultSet rs = pstmt.executeQuery();
		
		if (rs.next())
		{	if (!ValueFormat.validFormat(cuit,rs.getString(1)))
			{
				JOptionPane.showMessageDialog(null,"El CUIT ingresado no se corresponde con el formato esperado: " + rs.getString(1),"Error - Formato CUIT", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			query = " Select C_BPartner_Id FROM C_BPARTNER" +
					" WHERE TAXID = '" + cuit + "' AND ISACTIVE='Y' " +
					"	AND AD_Org_ID = ? AND AD_Client_ID = ?";
				
			pstmt = DB.prepareStatement(query, null);
			pstmt.setInt(1, getAD_Org_ID());
			pstmt.setInt(2, getAD_Client_ID());
			rs = pstmt.executeQuery();
			
			if (rs.next() && (getC_BPartner_ID() != rs.getLong(1)))
			{
				JOptionPane.showMessageDialog(null,"El CUIT ingresado ya existe para otro Socio de Negocio.","Error - CUIT duplicado", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			
			setSOCreditStatus();
		}
		*/
		
		// CONFIGURACIÓN DE RETENCIONES
		
		if (getCondicionIVACode().equals(CIVA_MONOTRIBUTISTA))
			setCONDICIONGAN_ID(CGAN_EXENTO);
		
		boolean EXENCIONGAN = isEXENCIONGANANCIAS();
		boolean EXENCIONIVA = isEXENCIONIVA();
		boolean EXENCIONSUSS = isEXENCIONSUSS();
		boolean EXENCIONIB = isEXENCIONIB();
		boolean EXENCIONPERIB = isEXENCIONPERIB();
		boolean EXENCIONPERIVA = isEXENCIONPERIVA();
		
		if (getCONDICIONGAN_ID() == CGAN_EXENTO)
			EXENCIONGAN = true;
		
		if ((!isEMPLEADOR()) || getCondicionIVACode().equals(CIVA_MONOTRIBUTISTA) || getCondicionIVACode().equals(CIVA_EXENTO))
			EXENCIONSUSS = true;
		
		if (getCondicionIVACode().equals(CIVA_MONOTRIBUTISTA) || getCondicionIVACode().equals(CIVA_EXENTO))
		{	EXENCIONIVA = true;
			EXENCIONPERIVA = true;
		}
		
		setEXENCIONGANANCIAS(EXENCIONGAN);
		setEXENCIONIVA(EXENCIONIVA);
		setEXENCIONIB(EXENCIONIB);
		setEXENCIONSUSS(EXENCIONSUSS);
		
		setEXENCIONPERIB(EXENCIONPERIB);
		setEXENCIONPERIVA(EXENCIONPERIVA);
			
	}
	catch (SQLException esql){
			
	}
	return true;

	}	//	beforeSave

	/**
	 * 	Before Delete
	 *	@return true
	 */
	protected boolean beforeDelete ()
	{
		return delete_Accounting("C_BP_Customer_Acct") 
			&& delete_Accounting("C_BP_Vendor_Acct")
			&& delete_Accounting("C_BP_Employee_Acct");
	}	//	beforeDelete

	/**
	 * 	After Delete
	 *	@param success
	 *	@return deleted
	 */
	protected boolean afterDelete (boolean success)
	{
		if (success)
			delete_Tree(MTree_Base.TREETYPE_BPartner);
		return success;
	}	//	afterDelete
	
	public String getCondicionIVACode()	{
		
		String query = "select IVACODE from CondicionIVA where CondicionIVA_ID = ?";
		try{
			PreparedStatement pstmt = DB.prepareStatement(query, null);
			pstmt.setInt(1, getCONDICIONIVA_ID());
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next())
				return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "";
	}

	public void setCondicionIVACode(String condicion)	{
		
		String query = "select CondicionIVA_ID from CondicionIVA where IVACODE = ?";
		try{
			PreparedStatement pstmt = DB.prepareStatement(query, null);
			pstmt.setString(1, condicion);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next())
				setCONDICIONIVA_ID(rs.getInt(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}	//	MBPartner
