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
package org.eevolution.model;

import java.sql.*;
import java.math.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Product Cost Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MCost.java,v 1.19 2005/12/20 04:21:02 jjanke Exp $
 */
public class MCost extends org.compiere.model.MCost
{
	
	
	/**	Logger	*/
	private static CLogger 	s_log = CLogger.getCLogger (MCost.class);
	 static MCost[] m_lines = null;
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param ignored multi-key
	 *	@param trxName trx
	 */
	public MCost (Properties ctx, int ignored, String trxName)
	{
		super (ctx, ignored, trxName);
	}	//	MCost

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MCost (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MCost

	/**
	 * 	Parent Constructor
	 *	@param product Product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param as Acct Schema
	 *	@param AD_Org_ID org
	 *	@param M_CostElement_ID cost element
	 */
	public MCost (MProduct product, int M_AttributeSetInstance_ID, 
		MAcctSchema as, int AD_Org_ID, int M_CostElement_ID)
	{
		super(product,M_AttributeSetInstance_ID, as,  AD_Org_ID,M_CostElement_ID);
	}	//	MCost
        
        /**
	 * 	Get Element Cost
	 * 	@return lines
	 */
        
        public static MCost[] getElements (int M_Product_ID, int C_AcctSchema_ID, int M_CostType_ID)
	{
		
		ArrayList list = new ArrayList();
                
                
		String sql = "SELECT * FROM M_Cost WHERE AD_Client_ID = ? AND M_Product_ID=? AND  C_Acctschema_ID = ? AND M_CostType_ID = ? ";
		PreparedStatement pstmt = null;
		try
		{
			int AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));			
			pstmt = DB.prepareStatement(sql);
			pstmt.setInt(1, AD_Client_ID);
			pstmt.setInt(2, M_Product_ID);
                        pstmt.setInt(3, C_AcctSchema_ID);
                        pstmt.setInt(4, M_CostType_ID);
                        //pstmt.setInt(5, M_Warehouse_ID);
                        //pstmt.setInt(6, S_Resource_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
                        {    
			   list.add(new  MCost(Env.getCtx(), rs,"M_Cost"));                          
                        }
                        
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			//log.error("getLines", ex);
                        s_log.fine("getLines" +  ex);
                        //System.out.println("getLines" +  ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		//
		m_lines = new  MCost[list.size()];
		list.toArray(m_lines);
		return m_lines;
	}	//	getMInOutLines
        
        /* Bision - 29/07/2008 - Ibañez Santiago
         * Modificacion realizada para retornar todos los MCosts sin tener
         * en cuenta el Tipo de costo (M_Cost_Type_ID)
         * */
        public static MCost[] getElements (int M_Product_ID, int C_AcctSchema_ID)
	{
		
		ArrayList list = new ArrayList();
                
                
		String sql = "SELECT * FROM M_Cost WHERE AD_Client_ID = ? AND M_Product_ID=? AND  C_Acctschema_ID = ?";
		PreparedStatement pstmt = null;
		try
		{
			int AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));			
			pstmt = DB.prepareStatement(sql);
			pstmt.setInt(1, AD_Client_ID);
			pstmt.setInt(2, M_Product_ID);
                        pstmt.setInt(3, C_AcctSchema_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
                        {    
			   list.add(new  MCost(Env.getCtx(), rs,"M_Cost"));                          
                        }
                        
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			//log.error("getLines", ex);
                        s_log.fine("getLines" +  ex);
                        //System.out.println("getLines" +  ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		//
		m_lines = new  MCost[list.size()];
		list.toArray(m_lines);
		return m_lines;
	}

}	//	MCost
