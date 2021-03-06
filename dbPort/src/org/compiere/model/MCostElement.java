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

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;
//import org.eevolution.model.MMPCCostElement;

/**
 * 	Cost Element Model
 *  @author Jorg Janke
 *  @version $Id: MCostElement.java,v 1.10 2005/11/28 03:35:24 jjanke Exp $
 */
public class MCostElement extends X_M_CostElement
{
	/**
	 * 	Get Material Cost Element or create it
	 *	@param po parent
	 *	@param CostingMethod method
	 *	@return cost element
	 */
	public static MCostElement getMaterialCostElement (PO po, String CostingMethod)
	{
		if (CostingMethod == null || CostingMethod.length() == 0)
		{
			s_log.severe("No CostingMethod");
			return null;
		}
		//
		MCostElement retValue = null;
		String sql = "SELECT * FROM M_CostElement WHERE AD_Client_ID=? AND CostingMethod=? ORDER BY AD_Org_ID";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, po.get_TrxName());
			pstmt.setInt (1, po.getAD_Client_ID());
			pstmt.setString(2, CostingMethod);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MCostElement (po.getCtx(), rs, po.get_TrxName());
			if (rs.next())
				s_log.warning("More then one Material Cost Element for CostingMethod=" + CostingMethod);
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
		if (retValue != null)
			return retValue;
		
		//	Create New
		retValue = new MCostElement (po.getCtx(), 0, po.get_TrxName());
		retValue.setClientOrg(po.getAD_Client_ID(), 0);
		String name = MRefList.getListName(po.getCtx(), COSTINGMETHOD_AD_Reference_ID, CostingMethod);
		if (name == null || name.length() == 0)
			name = CostingMethod;
		retValue.setName(name);
		retValue.setCostElementType(COSTELEMENTTYPE_Material);
		retValue.setCostingMethod(CostingMethod);
		retValue.save();
		//
		return retValue;
	}	//	getMaterialCostElement

	/**
	 * 	Get first Material Cost Element
	 *	@param ctx context
	 *	@param CostingMethod costing method
	 *	@return Cost Element or null
	 */
	public static MCostElement getMaterialCostElement(Properties ctx, String CostingMethod)
	{
		MCostElement retValue = null;
		String sql = "SELECT * FROM M_CostElement WHERE AD_Client_ID=? AND CostingMethod=? ORDER BY AD_Org_ID";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, Env.getAD_Client_ID(ctx));
			pstmt.setString(2, CostingMethod);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MCostElement (ctx, rs, null);
			if (rs.next())
				s_log.info("More then one Material Cost Element for CostingMethod=" + CostingMethod);
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
	}	//	getMaterialCostElement
	
	
	/**
	 * 	Get active Material Cost Element for client 
	 *	@param po parent
	 *	@return cost element array
	 */
	public static MCostElement[] getCostingMethods (PO po)
	{
		ArrayList<MCostElement> list = new ArrayList<MCostElement>();
		String sql = "SELECT * FROM M_CostElement "
			+ "WHERE AD_Client_ID=?"
			+ " AND IsActive='Y' AND CostElementType='M' AND CostingMethod IS NOT NULL";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, po.get_TrxName());
			pstmt.setInt (1, po.getAD_Client_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MCostElement (po.getCtx(), rs, po.get_TrxName()));
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
		//
		MCostElement[] retValue = new MCostElement[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getMaterialCostElement

	
	/**
	 * 	Get Cost Element from Cache
	 *	@param ctx context
	 *	@param M_CostElement_ID id
	 *	@return Cost Element
	 */
	public static MCostElement get (Properties ctx, int M_CostElement_ID)
	{
		Integer key = new Integer (M_CostElement_ID);
		MCostElement retValue = (MCostElement) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MCostElement (ctx, M_CostElement_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer,MCostElement>	s_cache	= new CCache<Integer,MCostElement>("M_CostElement", 20);
	
	/**	Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MCostElement.class);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_CostElement_ID id
	 *	@param trxName trx
	 */
	public MCostElement (Properties ctx, int M_CostElement_ID, String trxName)
	{
		super (ctx, M_CostElement_ID, trxName);
		if (M_CostElement_ID == 0)
		{
		//	setName (null);
			setCostElementType (COSTELEMENTTYPE_Material);
			setIsCalculated (false);
		}
	}	//	MCostElement

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MCostElement (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MCostElement
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Check Unique Costing Method
		if (COSTELEMENTTYPE_Material.equals(getCostElementType())
			&& (newRecord || is_ValueChanged("CostingMethod")))
		{
			String sql = "SELECT  COALESCE(MAX(M_CostElement_ID),0) FROM M_CostElement "
				+ "WHERE AD_Client_ID=? AND CostingMethod=?";
			int id = DB.getSQLValue(get_TrxName(), sql, getAD_Client_ID(), getCostingMethod());
			if (id > 0 && id != get_ID())
			{
				log.saveError("AlreadyExists", Msg.getElement(getCtx(), "CostingMethod"));
				return false;
			}
		}

		//	Maintain Calclated
		if (COSTELEMENTTYPE_Material.equals(getCostElementType()))
		{
			String cm = getCostingMethod();
			if (cm == null || cm.length() == 0
				|| COSTINGMETHOD_StandardCosting.equals(cm))
				setIsCalculated(false);
			else
				setIsCalculated(true);
		}
		else
		{
			if (isCalculated())
				setIsCalculated(false);
			if (getCostingMethod() != null)
				setCostingMethod(null);
		}
		
		if (getAD_Org_ID() != 0)
			setAD_Org_ID(0);
		return true;
	}	//	beforeSave
	
	/**
	 * 	Before Delete
	 *	@return true if can be deleted
	 */
	protected boolean beforeDelete ()
	{
		String cm = getCostingMethod();
		if (cm == null
			|| !COSTELEMENTTYPE_Material.equals(getCostElementType()))
			return true;
		
		//	Costing Methods on AS level
		MAcctSchema[] ass = MAcctSchema.getClientAcctSchema(getCtx(), getAD_Client_ID());
		for (int i = 0; i < ass.length; i++)
		{
			if (ass[i].getCostingMethod().equals(getCostingMethod()))
			{
				log.saveError("CannotDeleteUsed", Msg.getElement(getCtx(), "C_AcctSchema_ID") 
					+ " - " + ass[i].getName());
				return false;
			}
		}
		
		//	Costing Methods on PC level
		String sql = "SELECT M_Product_Category_ID FROM M_Product_Category_Acct WHERE AD_Client_ID=? AND CostingMethod=?";
		int M_Product_Category_ID = 0;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, getAD_Client_ID());
			pstmt.setString (2, getCostingMethod());
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				M_Product_Category_ID = rs.getInt(1);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
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
		if (M_Product_Category_ID != 0)
		{
			log.saveError("CannotDeleteUsed", Msg.getElement(getCtx(), "M_Product_Category_ID") 
				+ " (ID=" + M_Product_Category_ID + ")");
			return false;
		}
		return true;
	}	//	beforeDelete
	
	/**
	 * 	Is this a Costing Method
	 *	@return true if not Material cost or no costing method.
	 */
	public boolean isCostingMethod()
	{
		return COSTELEMENTTYPE_Material.equals(getCostElementType())
			&& getCostingMethod() != null;
	}	//	isCostingMethod
	
	/**
	 * 	Is Avg Invoice Costing Method
	 *	@return true if AverageInvoice
	 */
	public boolean isAverageInvoice()
	{
		String cm = getCostingMethod();
		return cm != null 
			&& cm.equals(COSTINGMETHOD_AverageInvoice)
			&& COSTELEMENTTYPE_Material.equals(getCostElementType());
	}	//	isAverageInvoice
	
	/**
	 * 	Is Avg PO Costing Method
	 *	@return true if AveragePO
	 */
	public boolean isAveragePO()
	{
		String cm = getCostingMethod();
		return cm != null 
			&& cm.equals(COSTINGMETHOD_AveragePO)
			&& COSTELEMENTTYPE_Material.equals(getCostElementType());
	}	//	isAveragePO
	/**
	 * 	Is FiFo Costing Method
	 *	@return true if Fifo
	 */
	public boolean isFifo()
	{
		String cm = getCostingMethod();
		return cm != null 
			&& cm.equals(COSTINGMETHOD_Fifo)
			&& COSTELEMENTTYPE_Material.equals(getCostElementType());
	}	//	isFifo
	/**
	 * 	Is Last Invoice Costing Method
	 *	@return true if LastInvoice
	 */
	public boolean isLastInvoice()
	{
		String cm = getCostingMethod();
		return cm != null 
			&& cm.equals(COSTINGMETHOD_LastInvoice)
			&& COSTELEMENTTYPE_Material.equals(getCostElementType());
	}	//	isLastInvoice
	/**
	 * 	Is Last PO Costing Method
	 *	@return true if LastPOPrice
	 */
	public boolean isLastPOPrice()
	{
		String cm = getCostingMethod();
		return cm != null 
			&& cm.equals(COSTINGMETHOD_LastPOPrice)
			&& COSTELEMENTTYPE_Material.equals(getCostElementType());
	}	//	isLastPOPrice
	/**
	 * 	Is LiFo Costing Method
	 *	@return true if Lifo
	 */
	public boolean isLifo()
	{
		String cm = getCostingMethod();
		return cm != null 
			&& cm.equals(COSTINGMETHOD_Lifo)
			&& COSTELEMENTTYPE_Material.equals(getCostElementType());
	}	//	isLiFo
	/**
	 * 	Is Std Costing Method
	 *	@return true if StandardCosting
	 */
	public boolean isStandardCosting()
	{
		String cm = getCostingMethod();
		return cm != null 
			&& cm.equals(COSTINGMETHOD_StandardCosting)
			&& COSTELEMENTTYPE_Material.equals(getCostElementType());
	}	//	isStandardCosting
	/**
	 * 	Is User Costing Method
	 *	@return true if User Defined
	 */
	public boolean isUserDefined()
	{
		String cm = getCostingMethod();
		return cm != null 
			&& cm.equals(COSTINGMETHOD_UserDefined)
			&& COSTELEMENTTYPE_Material.equals(getCostElementType());
	}	//	isAveragePO
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MCostElement[");
		sb.append (get_ID ())
			.append ("-").append (getName())
			.append(",Type=").append(getCostElementType())
			.append(",Method=").append(getCostingMethod())
			.append ("]");
		return sb.toString ();
	} //	toString
	
	public static MCostElement getCostElementByName(String name){
        String sql = "SELECT * FROM M_COSTELEMENT WHERE NAME = '"+name+"'";
        PreparedStatement pstmt = DB.prepareStatement(sql, null);
        try {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new MCostElement(Env.getCtx(), rs, null);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MCostElement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
	
}	//	MCostElement
