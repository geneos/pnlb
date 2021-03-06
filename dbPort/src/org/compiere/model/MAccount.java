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
 *  Account Object Entity to maintain all segment values.
 * 	C_ValidCombination
 *
 *  @author		Jorg Janke
 *  @version 	$Id: MAccount.java,v 1.17 2005/12/19 01:20:41 jjanke Exp $
 */
public class MAccount extends X_C_ValidCombination
{
	/**
	 * 	Get existing Account or create it 
	 *	@return account or null
	 */
	public static MAccount get (Properties ctx, 
		int AD_Client_ID, int AD_Org_ID, int C_AcctSchema_ID, 
		int Account_ID, int C_SubAcct_ID,
		int M_Product_ID, int C_BPartner_ID, int AD_OrgTrx_ID, 
		int C_LocFrom_ID, int C_LocTo_ID, int C_SalesRegion_ID, 
		int C_Project_ID, int C_Campaign_ID, int C_Activity_ID,
		int User1_ID, int User2_ID, int UserElement1_ID, int UserElement2_ID)
	{
		MAccount existingAccount = null;
		//
		StringBuffer info = new StringBuffer();
		StringBuffer sql = new StringBuffer("SELECT * FROM C_ValidCombination "
			//	Mandatory fields
			+ "WHERE AD_Client_ID=?"		//	#1
			+ " AND AD_Org_ID=?"
			+ " AND C_AcctSchema_ID=?"
			+ " AND Account_ID=?");			//	#4
		//	Optional fields
		if (C_SubAcct_ID == 0)
			sql.append(" AND C_SubAcct_ID IS NULL");
		else
			sql.append(" AND C_SubAcct_ID=?");
		if (M_Product_ID == 0)
			sql.append(" AND M_Product_ID IS NULL");
		else
			sql.append(" AND M_Product_ID=?");
		if (C_BPartner_ID == 0)
			sql.append(" AND C_BPartner_ID IS NULL");
		else
			sql.append(" AND C_BPartner_ID=?");
		if (AD_OrgTrx_ID == 0)
			sql.append(" AND AD_OrgTrx_ID IS NULL");
		else
			sql.append(" AND AD_OrgTrx_ID=?");
		if (C_LocFrom_ID == 0)
			sql.append(" AND C_LocFrom_ID IS NULL");
		else
			sql.append(" AND C_LocFrom_ID=?");
		if (C_LocTo_ID == 0)
			sql.append(" AND C_LocTo_ID IS NULL");
		else
			sql.append(" AND C_LocTo_ID=?");
		if (C_SalesRegion_ID == 0)
			sql.append(" AND C_SalesRegion_ID IS NULL");
		else
			sql.append(" AND C_SalesRegion_ID=?");
		if (C_Project_ID == 0)
			sql.append(" AND C_Project_ID IS NULL");
		else
			sql.append(" AND C_Project_ID=?");
		if (C_Campaign_ID == 0)
			sql.append(" AND C_Campaign_ID IS NULL");
		else
			sql.append(" AND C_Campaign_ID=?");
		if (C_Activity_ID == 0)
			sql.append(" AND C_Activity_ID IS NULL");
		else
			sql.append(" AND C_Activity_ID=?");
		if (User1_ID == 0)
			sql.append(" AND User1_ID IS NULL");
		else
			sql.append(" AND User1_ID=?");
		if (User2_ID == 0)
			sql.append(" AND User2_ID IS NULL");
		else
			sql.append(" AND User2_ID=?");
		if (UserElement1_ID == 0)
			sql.append(" AND UserElement1_ID IS NULL");
		else
			sql.append(" AND UserElement1_ID=?");
		if (UserElement2_ID == 0)
			sql.append(" AND UserElement2_ID IS NULL");
		else
			sql.append(" AND UserElement2_ID=?");
		sql.append(" AND IsActive='Y'");
	//	sql.append(" ORDER BY IsFullyQualified DESC");
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			//  --  Mandatory Accounting fields
			int index = 1;
			pstmt.setInt(index++, AD_Client_ID);
			pstmt.setInt(index++, AD_Org_ID);
			info.append("AD_Client_ID=").append(AD_Client_ID).append(",AD_Org_ID=").append(AD_Org_ID);
			//	Schema
			pstmt.setInt(index++, C_AcctSchema_ID);
			info.append(",C_AcctSchema_ID=").append(C_AcctSchema_ID);
			//	Account
			pstmt.setInt(index++, Account_ID);
			info.append(",Account_ID=").append(Account_ID).append(" ");
			
			//	--  Optional Accounting fields
			if (C_SubAcct_ID != 0)
				pstmt.setInt(index++, C_SubAcct_ID);
			if (M_Product_ID != 0)
				pstmt.setInt(index++, M_Product_ID);
			if (C_BPartner_ID != 0)
				pstmt.setInt(index++, C_BPartner_ID);
			if (AD_OrgTrx_ID != 0)
				pstmt.setInt(index++, AD_OrgTrx_ID);
			if (C_LocFrom_ID != 0)
				pstmt.setInt(index++, C_LocFrom_ID);
			if (C_LocTo_ID != 0)
				pstmt.setInt(index++, C_LocTo_ID);
			if (C_SalesRegion_ID != 0)
				pstmt.setInt(index++, C_SalesRegion_ID);
			if (C_Project_ID != 0)
				pstmt.setInt(index++, C_Project_ID);
			if (C_Campaign_ID != 0)
				pstmt.setInt(index++, C_Campaign_ID);
			if (C_Activity_ID != 0)
				pstmt.setInt(index++, C_Activity_ID);
			if (User1_ID != 0)
				pstmt.setInt(index++, User1_ID);
			if (User2_ID != 0)
				pstmt.setInt(index++, User2_ID);
			if (UserElement1_ID != 0)
				pstmt.setInt(index++, UserElement1_ID);
			if (UserElement2_ID != 0)
				pstmt.setInt(index++, UserElement2_ID);
			//
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				existingAccount = new MAccount (ctx, rs, null);
			rs.close();
			pstmt.close();
		}
		catch(SQLException e)
		{
			s_log.log(Level.SEVERE, info + "\n" + sql, e);
		}
		//	Existing
		if (existingAccount != null)
			return existingAccount;

		//	New
		MAccount newAccount = new MAccount (ctx, 0, null);
		newAccount.setClientOrg(AD_Client_ID, AD_Org_ID);
		newAccount.setC_AcctSchema_ID(C_AcctSchema_ID);
		newAccount.setAccount_ID(Account_ID);
		//	--  Optional Accounting fields
		newAccount.setC_SubAcct_ID(C_SubAcct_ID);
		newAccount.setM_Product_ID(M_Product_ID);
		newAccount.setC_BPartner_ID(C_BPartner_ID);
		newAccount.setAD_OrgTrx_ID(AD_OrgTrx_ID);
		newAccount.setC_LocFrom_ID(C_LocFrom_ID);
		newAccount.setC_LocTo_ID(C_LocTo_ID);
		newAccount.setC_SalesRegion_ID(C_SalesRegion_ID);
		newAccount.setC_Project_ID(C_Project_ID);
		newAccount.setC_Campaign_ID(C_Campaign_ID);
		newAccount.setC_Activity_ID(C_Activity_ID);
		newAccount.setUser1_ID(User1_ID);
		newAccount.setUser2_ID(User2_ID);
		newAccount.setUserElement1_ID(UserElement1_ID);
		newAccount.setUserElement2_ID(UserElement2_ID);
		//
		if (!newAccount.save())
		{
			s_log.log(Level.SEVERE, "Could not create new account - " + info);
			return null;
		}
		s_log.fine("New: " + newAccount);
		return newAccount;
	}	//	get
	
	/**
	 * 	Get first with Alias
	 *	@param ctx context
	 *	@param C_AcctSchema_ID as
 	 *	@param alias alias
	 *	@return account
	 */
	public static MAccount get (Properties ctx, int C_AcctSchema_ID, String alias)
	{
		MAccount retValue = null;
		String sql = "SELECT * FROM C_ValidCombination WHERE C_AcctSchema_ID=? AND Alias=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt(1, C_AcctSchema_ID);
			pstmt.setString (2, alias);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MAccount (ctx, rs, null);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
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
	 * 	Get from existing Accounting fact
	 *	@param fa accounting fact
	 *	@return account
	 */
	public static MAccount get (X_Fact_Acct fa)
	{
		MAccount acct = get (fa.getCtx(),
			fa.getAD_Client_ID(), fa.getAD_Org_ID(), fa.getC_AcctSchema_ID(), 
			fa.getAccount_ID(), fa.getC_SubAcct_ID(),
			fa.getM_Product_ID(), fa.getC_BPartner_ID(), fa.getAD_OrgTrx_ID(), 
			fa.getC_LocFrom_ID(), fa.getC_LocTo_ID(), fa.getC_SalesRegion_ID(), 
			fa.getC_Project_ID(), fa.getC_Campaign_ID(), fa.getC_Activity_ID(),
			fa.getUser1_ID(), fa.getUser2_ID(), fa.getUserElement1_ID(), fa.getUserElement2_ID());
		return acct;
	}	//	get
	
	/**************************************************************************
	 *  Factory: default combination
	 *  @param C_AcctSchema_ID accounting schema
	 * 	@param optionalNull if true the optional values are null
	 *  @return Account
	 */
	public static MAccount getDefault (Properties ctx, int C_AcctSchema_ID, boolean optionalNull, String trxName)
	{
		MAcctSchema acctSchema = new MAcctSchema (ctx, C_AcctSchema_ID, trxName);
		return getDefault (acctSchema, optionalNull);
	}   //  getDefault

	/**
	 *  Factory: default combination
	 *  @param acctSchema accounting schema
	 * 	@param optionalNull if true, the optional values are null
	 *  @return Account
	 */
	public static MAccount getDefault (MAcctSchema acctSchema, boolean optionalNull)
	{
		MAccount vc = new MAccount(acctSchema);
		//  Active Elements
		MAcctSchemaElement[] elements = acctSchema.getAcctSchemaElements();
		for (int i = 0; i < elements.length; i++)
		{
			MAcctSchemaElement ase = elements[i];
			String elementType = ase.getElementType();
			int defaultValue = ase.getDefaultValue();
			boolean setValue = ase.isMandatory() || (!ase.isMandatory() && !optionalNull);
			//
			if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_Organization))
				vc.setAD_Org_ID(defaultValue);
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_Account))
				vc.setAccount_ID(defaultValue);
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_SubAccount) && setValue)
				vc.setC_SubAcct_ID(defaultValue);
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_BPartner) && setValue)
				vc.setC_BPartner_ID(defaultValue);
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_Product) && setValue)
				vc.setM_Product_ID(defaultValue);
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_Activity) && setValue)
				vc.setC_Activity_ID(defaultValue);
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_LocationFrom) && setValue)
				vc.setC_LocFrom_ID(defaultValue);
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_LocationTo) && setValue)
				vc.setC_LocTo_ID(defaultValue);
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_Campaign) && setValue)
				vc.setC_Campaign_ID(defaultValue);
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_OrgTrx) && setValue)
				vc.setAD_OrgTrx_ID(defaultValue);
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_Project) && setValue)
				vc.setC_Project_ID(defaultValue);
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_SalesRegion) && setValue)
				vc.setC_SalesRegion_ID(defaultValue);
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_UserList1) && setValue)
				vc.setUser1_ID(defaultValue);
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_UserList2) && setValue)
				vc.setUser2_ID(defaultValue);
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_UserElement1) && setValue)
				vc.setUserElement1_ID(defaultValue);
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_UserElement2) && setValue)
				vc.setUserElement2_ID(defaultValue);
		}
		s_log.fine("Client_ID="
			+ vc.getAD_Client_ID() + ", Org_ID=" + vc.getAD_Org_ID()
			+ " - AcctSchema_ID=" + vc.getC_AcctSchema_ID() + ", Account_ID=" + vc.getAccount_ID());
		return vc;
	}   //  getDefault

	
	/**
	 *  Get Account
	 *  @param C_ValidCombination_ID combination
	 *  @return Account
	 */
	public static MAccount get (Properties ctx, int C_ValidCombination_ID)
	{
		//	Maybe later cache
		return new MAccount(ctx, C_ValidCombination_ID, null);
	}   //  getAccount

	/**
	 * 	Update Value/Description after change of 
	 * 	account element value/description.
	 *	@param ctx context
	 *	@param where where clause
	 */
	public static void updateValueDescription (Properties ctx, String where, String trxName)
	{
		String sql = "SELECT * FROM C_ValidCombination";
		if (where != null && where.length() > 0)
			sql += " WHERE " + where;
		sql += " ORDER BY C_ValidCombination_ID";
		int count = 0;
		int errors = 0;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MAccount account = new MAccount (ctx, rs, trxName);
				account.setValueDescription();
				if (account.save())
					count++;
				else
					errors++;
			}
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
		s_log.info(where + " #" + count + ", Errors=" + errors);
	}	//	updateValueDescription
	
	/**	Logger						*/
	private static CLogger		s_log = CLogger.getCLogger (MAccount.class);


	
	/**************************************************************************
	 *  Default constructor
	 * 	@param ctx context
	 *  @param C_ValidCombination_ID combination
	 */
	public MAccount (Properties ctx, int C_ValidCombination_ID, String trxName)
	{
		super (ctx, C_ValidCombination_ID, trxName);
		if (C_ValidCombination_ID == 0)
		{
		//	setAccount_ID (0);
		//	setC_AcctSchema_ID (0);
			setIsFullyQualified (false);
		}
	}   //  MAccount

	/**
	 *  Load constructor
	 * 	@param ctx context
	 *  @param rs result set
	 */
	public MAccount (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}   //  MAccount

	/**
	 * 	Parent Constructor
	 *	@param as account schema
	 */
	public MAccount (MAcctSchema as)
	{
		this (as.getCtx(), 0, as.get_TrxName());
		setClientOrg(as);
		setC_AcctSchema_ID(as.getC_AcctSchema_ID());
	}	//	Account

	/**	Account Segment				*/
	private MElementValue	m_accountEV = null;

	
	/**************************************************************************
	 * Return String representation
	 * @return String
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MAccount=[");
		sb.append(getC_ValidCombination_ID());
		if (getCombination() != null)
			sb.append(",")
				.append(getCombination());
		else
		{
			//	.append(",Client=").append(getAD_Client_ID())
			sb.append(",Schema=").append(getC_AcctSchema_ID())
				.append(",Org=").append(getAD_Org_ID())
				.append(",Acct=").append(getAccount_ID())
				.append(" ");
			if (getC_SubAcct_ID() != 0)
				sb.append(",C_SubAcct_ID=").append(getC_SubAcct_ID());
			if (getM_Product_ID() != 0)
				sb.append(",M_Product_ID=").append(getM_Product_ID());
			if (getC_BPartner_ID() != 0)
				sb.append(",C_BPartner_ID=").append(getC_BPartner_ID());
			if (getAD_OrgTrx_ID() != 0)
				sb.append(",AD_OrgTrx_ID=").append(getAD_OrgTrx_ID());
			if (getC_LocFrom_ID() != 0)
				sb.append(",C_LocFrom_ID=").append(getC_LocFrom_ID());
			if (getC_LocTo_ID() != 0)
				sb.append(",C_LocTo_ID=").append(getC_LocTo_ID());
			if (getC_SalesRegion_ID() != 0)
				sb.append(",C_SalesRegion_ID=").append(getC_SalesRegion_ID());
			if (getC_Project_ID() != 0)
				sb.append(",C_Project_ID=").append(getC_Project_ID());
			if (getC_Campaign_ID() != 0)
				sb.append(",C_Campaign_ID=").append(getC_Campaign_ID());
			if (getC_Activity_ID() != 0)
				sb.append(",C_Activity_ID=").append(getC_Activity_ID());
			if (getUser1_ID() != 0)
				sb.append(",User1_ID=").append(getUser1_ID());
			if (getUser2_ID() != 0)
				sb.append(",User2_ID=").append(getUser2_ID());
			if (getUserElement1_ID() != 0)
				sb.append(",UserElement1_ID=").append(getUserElement1_ID());
			if (getUserElement2_ID() != 0)
				sb.append(",UserElement2_ID=").append(getUserElement2_ID());
		}
		sb.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Set Account_ID
	 */
	public void setAccount_ID (int Account_ID)
	{
		m_accountEV = null;	//	reset
		super.setAccount_ID(Account_ID);
	}	//	setAccount
	
	/**
	 * 	Set Account_ID
	 */
	public MElementValue getAccount ()
	{
		if (m_accountEV == null)
		{
			if (getAccount_ID() != 0)
				m_accountEV = new MElementValue(getCtx(), getAccount_ID(), get_TrxName());
		}
		return m_accountEV;
	}	//	setAccount


	/**
	 * 	Get Account Type
	 *	@return Account Type of Account Element
	 */
	public String getAccountType()
	{
		if (m_accountEV == null)
			getAccount();
		if (m_accountEV == null)
		{
			log.log(Level.SEVERE, "No ElementValue for Account_ID=" + getAccount_ID());
			return "";
		}
		return m_accountEV.getAccountType();
	}	//	getAccountType

	/**
	 * Is this a Balance Sheet Account
	 * @return boolean
	 */
	public boolean isBalanceSheet()
	{
		String accountType = getAccountType();
		return (MElementValue.ACCOUNTTYPE_Asset.equals(accountType)
			|| MElementValue.ACCOUNTTYPE_Liability.equals(accountType)
			|| MElementValue.ACCOUNTTYPE_OwnerSEquity.equals(accountType));
	}	//	isBalanceSheet

	/**
	 * Is this an Activa Account
	 * @return boolean
	 */
	public boolean isActiva()
	{
		return MElementValue.ACCOUNTTYPE_Asset.equals(getAccountType());
	}	//	isActive

	/**
	 * Is this a Passiva Account
	 * @return boolean
	 */
	public boolean isPassiva()
	{
		String accountType = getAccountType();
		return (MElementValue.ACCOUNTTYPE_Liability.equals(accountType)
			|| MElementValue.ACCOUNTTYPE_OwnerSEquity.equals(accountType));
	}	//	isPassiva

	/**
	 * 	Set Value and Description and Fully Qualified Flag for Combination
	 */
	public void setValueDescription()
	{
		StringBuffer combi = new StringBuffer();
		StringBuffer descr = new StringBuffer();
		boolean fullyQualified = true;
		//
		MAcctSchema as = new MAcctSchema(getCtx(), getC_AcctSchema_ID(), get_TrxName());	//	In Trx!
		MAcctSchemaElement[] elements = MAcctSchemaElement.getAcctSchemaElements(as);
		for (int i = 0; i < elements.length; i++)
		{
			if (i > 0)
			{
				combi.append(as.getSeparator());
				descr.append(as.getSeparator());
			}
			MAcctSchemaElement element = elements[i];
			String combiStr = "_";		//	not defined
			String descrStr = "_";

			if (MAcctSchemaElement.ELEMENTTYPE_Organization.equals(element.getElementType()))
			{
				if (getAD_Org_ID() != 0)
				{
					MOrg org = new MOrg(getCtx(), getAD_Org_ID(), get_TrxName());	//	in Trx!
					combiStr = org.getValue();
					descrStr = org.getName();
				}
				else
				{
					combiStr = "*";
					descrStr = "*";
					fullyQualified = false;
				}
			}
			else if (MAcctSchemaElement.ELEMENTTYPE_Account.equals(element.getElementType()))
			{
				if (getAccount_ID() != 0)
				{
					if (m_accountEV == null)
						m_accountEV = new MElementValue(getCtx(), getAccount_ID(), get_TrxName());
					combiStr = m_accountEV.getValue();
					descrStr = m_accountEV.getName();
				}
				else if (element.isMandatory())
				{
					log.warning("Mandatory Element missing: Account");
					fullyQualified = false;
				}
			}
			else if (MAcctSchemaElement.ELEMENTTYPE_SubAccount.equals(element.getElementType()))
			{
				if (getC_SubAcct_ID() != 0)
				{
					X_C_SubAcct sa = new X_C_SubAcct(getCtx(), getC_SubAcct_ID(), get_TrxName());
					combiStr = sa.getValue();
					descrStr = sa.getName();
				}
			}
			else if (MAcctSchemaElement.ELEMENTTYPE_Product.equals(element.getElementType()))
			{
				if (getM_Product_ID() != 0)
				{
					X_M_Product product = new X_M_Product (getCtx(), getM_Product_ID(), get_TrxName());
					combiStr = product.getValue();
					descrStr = product.getName();
				}
				else if (element.isMandatory())
				{
					log.warning("Mandatory Element missing: Product");
					fullyQualified = false;
				}
			}
			else if (MAcctSchemaElement.ELEMENTTYPE_BPartner.equals(element.getElementType()))
			{
				if (getC_BPartner_ID() != 0)
				{
					X_C_BPartner partner = new X_C_BPartner (getCtx(), getC_BPartner_ID(),get_TrxName());
					combiStr = partner.getValue();
					descrStr = partner.getName();
				}
				else if (element.isMandatory())
				{
					log.warning("Mandatory Element missing: Business Partner");
					fullyQualified = false;
				}
			}
			else if (MAcctSchemaElement.ELEMENTTYPE_OrgTrx.equals(element.getElementType()))
			{
				if (getAD_OrgTrx_ID() != 0)
				{
					MOrg org = new MOrg(getCtx(), getAD_OrgTrx_ID(), get_TrxName());	// in Trx!
					combiStr = org.getValue();
					descrStr = org.getName();
				}
				else if (element.isMandatory())
				{
					log.warning("Mandatory Element missing: Trx Org");
					fullyQualified = false;
				}
			}
			else if (MAcctSchemaElement.ELEMENTTYPE_LocationFrom.equals(element.getElementType()))
			{
				if (getC_LocFrom_ID() != 0)
				{
					MLocation loc = new MLocation(getCtx(), getC_LocFrom_ID(), get_TrxName());	//	in Trx!
					combiStr = loc.getPostal();
					descrStr = loc.getCity();
				}
				else if (element.isMandatory())
				{
					log.warning("Mandatory Element missing: Location From");
					fullyQualified = false;
				}
			}
			else if (MAcctSchemaElement.ELEMENTTYPE_LocationTo.equals(element.getElementType()))
			{
				if (getC_LocTo_ID() != 0)
				{
					MLocation loc = new MLocation(getCtx(), getC_LocFrom_ID(), get_TrxName());	//	in Trx!
					combiStr = loc.getPostal();
					descrStr = loc.getCity();
				}
				else if (element.isMandatory())
				{
					log.warning("Mandatory Element missing: Location To");
					fullyQualified = false;
				}
			}
			else if (MAcctSchemaElement.ELEMENTTYPE_SalesRegion.equals(element.getElementType()))
			{
				if (getC_SalesRegion_ID() != 0)
				{
					MSalesRegion loc = new MSalesRegion(getCtx(), getC_SalesRegion_ID(), get_TrxName());
					combiStr = loc.getValue();
					descrStr = loc.getName();
				}
				else if (element.isMandatory())
				{
					log.warning("Mandatory Element missing: SalesRegion");
					fullyQualified = false;
				}
			}
			else if (MAcctSchemaElement.ELEMENTTYPE_Project.equals(element.getElementType()))
			{
				if (getC_Project_ID() != 0)
				{
					X_C_Project project = new X_C_Project (getCtx(), getC_Project_ID(), get_TrxName());
					combiStr = project.getValue();
					descrStr = project.getName();
				}
				else if (element.isMandatory())
				{
					log.warning("Mandatory Element missing: Project");
					fullyQualified = false;
				}
			}
			else if (MAcctSchemaElement.ELEMENTTYPE_Campaign.equals(element.getElementType()))
			{
				if (getC_Campaign_ID() != 0)
				{
					X_C_Campaign campaign = new X_C_Campaign (getCtx(), getC_Campaign_ID(), get_TrxName());
					combiStr = campaign.getValue();
					descrStr = campaign.getName();
				}
				else if (element.isMandatory())
				{
					log.warning("Mandatory Element missing: Campaign");
					fullyQualified = false;
				}
			}
			else if (MAcctSchemaElement.ELEMENTTYPE_Activity.equals(element.getElementType()))
			{
				if (getC_Activity_ID() != 0)
				{
					X_C_Activity act = new X_C_Activity (getCtx(), getC_Activity_ID(), get_TrxName());
					combiStr = act.getValue();
					descrStr = act.getName();
				}
				else if (element.isMandatory())
				{
					log.warning("Mandatory Element missing: Campaign");
					fullyQualified = false;
				}
			}
			else if (MAcctSchemaElement.ELEMENTTYPE_UserList1.equals(element.getElementType()))
			{
				if (getUser1_ID() != 0)
				{
					MElementValue ev = new MElementValue(getCtx(), getUser1_ID(), get_TrxName());
					combiStr = ev.getValue();
					descrStr = ev.getName();
				}
			}
			else if (MAcctSchemaElement.ELEMENTTYPE_UserList2.equals(element.getElementType()))
			{
				if (getUser2_ID() != 0)
				{
					MElementValue ev = new MElementValue(getCtx(), getUser2_ID(), get_TrxName());
					combiStr = ev.getValue();
					descrStr = ev.getName();
				}
			}
			else if (MAcctSchemaElement.ELEMENTTYPE_UserElement1.equals(element.getElementType()))
			{
				if (getUserElement1_ID() != 0)
				{
				}
			}
			else if (MAcctSchemaElement.ELEMENTTYPE_UserElement2.equals(element.getElementType()))
			{
				if (getUserElement2_ID() != 0)
				{
				}
			}
			combi.append(combiStr);
			descr.append(descrStr);
		}
		//	Set Values
		super.setCombination(combi.toString());
		super.setDescription(descr.toString());
		if (fullyQualified != isFullyQualified())
			setIsFullyQualified(fullyQualified);
		log.fine("Combination=" + getCombination() 
			+ " - " + getDescription()
			+ " - FullyQualified=" + fullyQualified);
	}	//	setValueDescription
	
	/**
	 * 	Validate combination
	 *	@return true if valid
	 */
	public boolean validate()
	{
		boolean ok = true;
		//	Validate Sub-Account
		if (getC_SubAcct_ID() != 0)
		{
			X_C_SubAcct sa = new X_C_SubAcct(getCtx(), getC_SubAcct_ID(), get_TrxName());
			if (sa.getC_ElementValue_ID() != getAccount_ID())
			{
				log.saveError("Error", "C_SubAcct.C_ElementValue_ID=" + sa.getC_ElementValue_ID()
					+ "<>Account_ID=" + getAccount_ID());
				ok = false;
			}
		}
		return ok;
	}	//	validate
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		setValueDescription();
		return validate();
	}	//	beforeSave
	
	
	/**
	 * 	Test
	 *	@param args
	 */
	public static void main (String[] args)
	{
		org.compiere.Compiere.startup(true);
		MAccount acct = get (Env.getCtx(), 11, 11, 101, 600, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		System.out.println(acct);
		MAccount acct2 = get (Env.getCtx(), 11, 12, 101, 600, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		System.out.println(acct2);
		
	}	//	main
	
}	//	Account

