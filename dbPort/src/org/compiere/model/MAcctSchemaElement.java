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
 *  Account Schema Element Object
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: MAcctSchemaElement.java,v 1.14 2005/11/25 21:58:31 jjanke Exp $
 */
public final class MAcctSchemaElement extends X_C_AcctSchema_Element
{
	/**
	 *  Factory: Return ArrayList of Account Schema Elements
	 * 	@param as Accounting Schema
	 *  @return ArrayList with Elements
	 */
	public static MAcctSchemaElement[] getAcctSchemaElements (MAcctSchema as)
	{
		Integer key = new Integer (as.getC_AcctSchema_ID());
		MAcctSchemaElement[] retValue = (MAcctSchemaElement[]) s_cache.get (key);
		if (retValue != null)
			return retValue;

		s_log.fine("C_AcctSchema_ID=" + as.getC_AcctSchema_ID());
		ArrayList<MAcctSchemaElement> list = new ArrayList<MAcctSchemaElement>();
		//
		String sql = "SELECT * FROM C_AcctSchema_Element "
			+ "WHERE C_AcctSchema_ID=? AND IsActive='Y' ORDER BY SeqNo";

		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, as.get_TrxName());
			pstmt.setInt(1, as.getC_AcctSchema_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MAcctSchemaElement ase = new MAcctSchemaElement(as.getCtx(), rs, as.get_TrxName());
				s_log.fine(" - " + ase);
				if (ase.isMandatory() && ase.getDefaultValue() == 0)
					s_log.log(Level.SEVERE, "No default value for " + ase.getName());
				list.add(ase);
				//
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		
		retValue = new MAcctSchemaElement[list.size()];
		list.toArray(retValue);
		s_cache.put (key, retValue);
		return retValue;
	}   //  getAcctSchemaElements

	/**
	 *  Get Column Name of ELEMENTTYPE 
	 * 	@param elementType ELEMENTTYPE 
	 *  @return column name
	 */
	public static String getColumnName(String elementType)
	{
		if (elementType.equals(ELEMENTTYPE_Organization))
			return "AD_Org_ID";
		else if (elementType.equals(ELEMENTTYPE_Account))
			return "Account_ID";
		else if (elementType.equals(ELEMENTTYPE_BPartner))
			return "C_BPartner_ID";
		else if (elementType.equals(ELEMENTTYPE_Product))
			return "M_Product_ID";
		else if (elementType.equals(ELEMENTTYPE_Activity))
			return "C_Activity_ID";
		else if (elementType.equals(ELEMENTTYPE_LocationFrom))
			return "C_LocFrom_ID";
		else if (elementType.equals(ELEMENTTYPE_LocationTo))
			return "C_LocTo_ID";
		else if (elementType.equals(ELEMENTTYPE_Campaign))
			return "C_Campaign_ID";
		else if (elementType.equals(ELEMENTTYPE_OrgTrx))
			return "AD_OrgTrx_ID";
		else if (elementType.equals(ELEMENTTYPE_Project))
			return "C_Project_ID";
		else if (elementType.equals(ELEMENTTYPE_SalesRegion))
			return "C_SalesRegion_ID";
		else if (elementType.equals(ELEMENTTYPE_UserList1))
			return "User1_ID";
		else if (elementType.equals(ELEMENTTYPE_UserList2))
			return "User2_ID";
		else if (elementType.equals(ELEMENTTYPE_UserElement1))
			return "UserElement1_ID";
		else if (elementType.equals(ELEMENTTYPE_UserElement2))
			return "UserElement2_ID";
		//
		return "";
	}   //  getColumnName

	/**
	 *  Get Value Query for ELEMENTTYPE Type
	 * 	@param elementType ELEMENTTYPE type
	 *  @return query "SELECT Value,Name FROM Table WHERE ID="
	 */
	public static String getValueQuery (String elementType)
	{
		if (elementType.equals(ELEMENTTYPE_Organization))
			return "SELECT Value,Name FROM AD_Org WHERE AD_Org_ID=";
		else if (elementType.equals(ELEMENTTYPE_Account))
			return "SELECT Value,Name FROM C_ElementValue WHERE C_ElementValue_ID=";
		else if (elementType.equals(ELEMENTTYPE_SubAccount))
			return "SELECT Value,Name FROM C_SubAccount WHERE C_SubAccount_ID=";
		else if (elementType.equals(ELEMENTTYPE_BPartner))
			return "SELECT Value,Name FROM C_BPartner WHERE C_BPartner_ID=";
		else if (elementType.equals(ELEMENTTYPE_Product))
			return "SELECT Value,Name FROM M_Product WHERE M_Product_ID=";
		else if (elementType.equals(ELEMENTTYPE_Activity))
			return "SELECT Value,Name FROM C_Activity WHERE C_Activity_ID=";
		else if (elementType.equals(ELEMENTTYPE_LocationFrom))
			return "SELECT City,Address1 FROM C_Location WHERE C_Location_ID=";
		else if (elementType.equals(ELEMENTTYPE_LocationTo))
			return "SELECT City,Address1 FROM C_Location WHERE C_Location_ID=";
		else if (elementType.equals(ELEMENTTYPE_Campaign))
			return "SELECT Value,Name FROM C_Campaign WHERE C_Campaign_ID=";
		else if (elementType.equals(ELEMENTTYPE_OrgTrx))
			return "SELECT Value,Name FROM AD_Org WHERE AD_Org_ID=";
		else if (elementType.equals(ELEMENTTYPE_Project))
			return "SELECT Value,Name FROM C_Project WHERE C_Project_ID=";
		else if (elementType.equals(ELEMENTTYPE_SalesRegion))
			return "SELECT Value,Name FROM C_SalesRegion WHERE C_SalesRegion_ID";
		else if (elementType.equals(ELEMENTTYPE_UserList1))
			return "SELECT Value,Name FROM C_ElementValue WHERE C_ElementValue_ID=";
		else if (elementType.equals(ELEMENTTYPE_UserList2))
			return "SELECT Value,Name FROM C_ElementValue WHERE C_ElementValue_ID=";
		//
		else if (elementType.equals(ELEMENTTYPE_UserElement1))
			return null;
		else if (elementType.equals(ELEMENTTYPE_UserElement2))
			return null;
		//
		return "";
	}   //  getColumnName

	/**	Logger						*/
	private static CLogger		s_log = CLogger.getCLogger (MAcctSchemaElement.class);

	/**	Cache						*/
	private static CCache<Integer,MAcctSchemaElement[]> s_cache = new CCache<Integer,MAcctSchemaElement[]>("C_AcctSchema_Element", 10);
	
	
	/*************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_AcctSchema_Element_ID id
	 */
	public MAcctSchemaElement (Properties ctx, int C_AcctSchema_Element_ID, String trxName)
	{
		super (ctx, C_AcctSchema_Element_ID, trxName);
		if (C_AcctSchema_Element_ID == 0)
		{
		//	setC_AcctSchema_Element_ID (0);
		//	setC_AcctSchema_ID (0);
		//	setC_Element_ID (0);
		//	setElementType (null);
			setIsBalanced (false);
			setIsMandatory (false);
		//	setName (null);
		//	setOrg_ID (0);
		//	setSeqNo (0);
		}
	}	//	MAcctSchemaElement

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */	
	public MAcctSchemaElement (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MAcctSchemaElement

	/**
	 * 	Parent Constructor
	 *	@param as accounting schema
	 */
	public MAcctSchemaElement (MAcctSchema as)
	{
		this (as.getCtx(), 0, as.get_TrxName());
		setClientOrg(as);
		setC_AcctSchema_ID (as.getC_AcctSchema_ID());
		
		//	setC_Element_ID (0);
		//	setElementType (null);
		//	setName (null);
		//	setSeqNo (0);
		
	}	//	MAcctSchemaElement

	/** User Element Column Name		*/
	private String		m_ColumnName = null;
	
	/**
	 * 	Set Organization Type
	 *	@param SeqNo sequence
	 *	@param Name name 
	 *	@param Org_ID id
	 */
	public void setTypeOrg (int SeqNo, String Name, int Org_ID)
	{
		setElementType (ELEMENTTYPE_Organization);
		setSeqNo(SeqNo);
		setName (Name);
		setOrg_ID(Org_ID);
	}	//	setTypeOrg
	
	public void setTypeAccount (int SeqNo, String Name, int C_Element_ID, int C_ElementValue_ID)
	{
		setElementType (ELEMENTTYPE_Account);
		setSeqNo(SeqNo);
		setName (Name);
		setC_Element_ID (C_Element_ID);
		setC_ElementValue_ID(C_ElementValue_ID);
	}

	public void setTypeBPartner (int SeqNo, String Name, int C_BPartner_ID)
	{
		setElementType (ELEMENTTYPE_BPartner);
		setSeqNo(SeqNo);
		setName (Name);
		setC_BPartner_ID(C_BPartner_ID);
	}

	public void setTypeProduct (int SeqNo, String Name, int M_Product_ID)
	{
		setElementType (ELEMENTTYPE_Product);
		setSeqNo(SeqNo);
		setName (Name);
		setM_Product_ID(M_Product_ID);
	}
	
	public void setTypeProject (int SeqNo, String Name, int C_Project_ID)
	{
		setElementType (ELEMENTTYPE_Project);
		setSeqNo(SeqNo);
		setName (Name);
		setC_Project_ID(C_Project_ID);
	}

	/**
	 *  Is Element Type
	 *  @param elementType type
	 *	@return ELEMENTTYPE type
	 */
	public boolean isElementType (String elementType)
	{
		if (elementType == null)
			return false;
		return elementType.equals(getElementType());
	}   //  isElementType

	/**
	 * 	Get Default element value
	 *	@return default
	 */
	public int getDefaultValue()
	{
		String elementType = getElementType();
		int defaultValue = 0;
		if (elementType.equals(ELEMENTTYPE_Organization))
			defaultValue = getOrg_ID();
		else if (elementType.equals(ELEMENTTYPE_Account))
			defaultValue = getC_ElementValue_ID();
		else if (elementType.equals(ELEMENTTYPE_BPartner))
			defaultValue = getC_BPartner_ID();
		else if (elementType.equals(ELEMENTTYPE_Product))
			defaultValue = getM_Product_ID();
		else if (elementType.equals(ELEMENTTYPE_Activity))
			defaultValue = getC_Activity_ID();
		else if (elementType.equals(ELEMENTTYPE_LocationFrom))
			defaultValue = getC_Location_ID();
		else if (elementType.equals(ELEMENTTYPE_LocationTo))
			defaultValue = getC_Location_ID();
		else if (elementType.equals(ELEMENTTYPE_Campaign))
			defaultValue = getC_Campaign_ID();
		else if (elementType.equals(ELEMENTTYPE_OrgTrx))
			defaultValue = getOrg_ID();
		else if (elementType.equals(ELEMENTTYPE_Project))
			defaultValue = getC_Project_ID();
		else if (elementType.equals(ELEMENTTYPE_SalesRegion))
			defaultValue = getC_SalesRegion_ID();
		else if (elementType.equals(ELEMENTTYPE_UserList1))
			defaultValue = getC_ElementValue_ID();
		else if (elementType.equals(ELEMENTTYPE_UserList2))
			defaultValue = getC_ElementValue_ID();
		else if (elementType.equals(ELEMENTTYPE_UserElement1))
			defaultValue = 0;
		else if (elementType.equals(ELEMENTTYPE_UserElement2))
			defaultValue = 0;
		return defaultValue;
	}	//	getDefault


	/**
	 *  Get ColumnName
	 *  @return column name
	 */
	public String getColumnName()
	{
		String et = getElementType();
		if (ELEMENTTYPE_UserElement1.equals(et) || ELEMENTTYPE_UserElement2.equals(et))
		{
			if (m_ColumnName == null)
				m_ColumnName = M_Column.getColumnName(getCtx(), getAD_Column_ID());
			return m_ColumnName;
		}
		return getColumnName(et);
	}	//	getColumnName

	
	/**
	 *  String representation
	 *  @return info
	 */
	public String toString()
	{
		return "AcctSchemaElement[" + get_ID() 
			+ "-" + getName() 
			+ "(" + getElementType() + ")=" + getDefaultValue()  
			+ ",Pos=" + getSeqNo() + "]";
	}   //  toString

	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true if it can be saved
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (getAD_Org_ID() != 0)
			setAD_Org_ID(0);
		String et = getElementType();
		if (isMandatory() &&
			(ELEMENTTYPE_UserList1.equals(et) || ELEMENTTYPE_UserList2.equals(et)
			|| ELEMENTTYPE_UserElement1.equals(et) || ELEMENTTYPE_UserElement2.equals(et)))
			setIsMandatory(false);
		else if (isMandatory())
		{
			String errorField = null;
			if (ELEMENTTYPE_Account.equals(et) && getC_ElementValue_ID() == 0)
				errorField = "C_ElementValue_ID";
			else if (ELEMENTTYPE_Activity.equals(et) && getC_Activity_ID() == 0)
				errorField = "C_Activity_ID";
			else if (ELEMENTTYPE_BPartner.equals(et) && getC_BPartner_ID() == 0)
				errorField = "C_BPartner_ID";
			else if (ELEMENTTYPE_Campaign.equals(et) && getC_Campaign_ID() == 0)
				errorField = "C_Campaign_ID";
			else if (ELEMENTTYPE_LocationFrom.equals(et) && getC_Location_ID() == 0)
				errorField = "C_Location_ID";
			else if (ELEMENTTYPE_LocationTo.equals(et) && getC_Location_ID() == 0)
				errorField = "C_Location_ID";
			else if (ELEMENTTYPE_Organization.equals(et) && getOrg_ID() == 0)
				errorField = "Org_ID";
			else if (ELEMENTTYPE_OrgTrx.equals(et) && getOrg_ID() == 0)
				errorField = "Org_ID";
			else if (ELEMENTTYPE_Product.equals(et) && getM_Product_ID() == 0)
				errorField = "M_Product_ID";
			else if (ELEMENTTYPE_Project.equals(et) && getC_Project_ID() == 0)
				errorField = "C_Project_ID";
			else if (ELEMENTTYPE_SalesRegion.equals(et) && getC_SalesRegion_ID() == 0)
				errorField = "C_SalesRegion_ID";
			if (errorField != null)
			{
				log.saveError("Error", Msg.parseTranslation(getCtx(), "@IsMandatory@: @" + errorField + "@"));
				return false;
			}
		}
		//
		if (getAD_Column_ID() == 0
			&& (ELEMENTTYPE_UserElement1.equals(et) || ELEMENTTYPE_UserElement2.equals(et)))
		{
			log.saveError("Error", Msg.parseTranslation(getCtx(), "@IsMandatory@: @AD_Column_ID@"));
			return false;
		}
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	Default Value
		if (isMandatory() && is_ValueChanged("IsMandatory"))
		{
			if (ELEMENTTYPE_Activity.equals(getElementType()))
				updateData ("C_Activity_ID", getC_Activity_ID());
			else if (ELEMENTTYPE_BPartner.equals(getElementType()))
				updateData ("C_BPartner_ID", getC_BPartner_ID());
			else if (ELEMENTTYPE_Product.equals(getElementType()))
				updateData ("M_Product_ID", getM_Product_ID());
			else if (ELEMENTTYPE_Project.equals(getElementType()))
				updateData ("C_Project_ID", getC_Project_ID());
		}
		//	Resequence
		if (newRecord || is_ValueChanged("SeqNo"))
			MAccount.updateValueDescription(getCtx(), 
				"AD_Client_ID=" + getAD_Client_ID(), get_TrxName());
		//	Clear Cache
		s_cache.clear();
		return success;
	}	//	afterSave
	
	/**
	 * 	Update ValidCombination and Fact with mandatory value
	 *	@param element element
	 *	@param id new default
	 */
	private void updateData (String element, int id)
	{
		MAccount.updateValueDescription(getCtx(), 
			element + "=" + id, get_TrxName());
		//
		String sql = "UPDATE C_ValidCombination SET " + element + "=" + id
			+ " WHERE " + element + " IS NULL AND AD_Client_ID=" + getAD_Client_ID();
		int noC = DB.executeUpdate(sql, get_TrxName());
		//
		sql = "UPDATE Fact_Acct SET " + element + "=" + id
			+ " WHERE " + element + " IS NULL AND C_AcctSchema_ID=" + getC_AcctSchema_ID();
		int noF = DB.executeUpdate(sql, get_TrxName());
		//
		log.fine("ValidCombination=" + noC + ", Fact=" + noF);
	}	//	updateData
	
	
	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterDelete (boolean success)
	{
		//	Update Account Info
		MAccount.updateValueDescription(getCtx(), 
			"AD_Client_ID=" + getAD_Client_ID(), get_TrxName());
		//
		s_cache.clear();
		return success;
	}	//	afterDelete
	
}   //  AcctSchemaElement
