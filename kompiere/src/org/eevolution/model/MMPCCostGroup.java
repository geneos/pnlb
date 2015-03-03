/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2003 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * created by Victor Perez are Copyright (C) e-Evolution,SC. All Rights Reserved.
 * Contributor(s): Victor Perez
 *****************************************************************************/
//package org.compiere.mfg.model;
package org.eevolution.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import java.util.logging.*;

import org.compiere.process.*;
import org.compiere.util.*;
import org.compiere.model.*;
import org.compiere.wf.*;

/**
 *	Cost Element
 *	
 *  @author Jorg Janke
 *  @version $Id: MProductCosting.java,v 1.4 2004/05/13 06:05:22 jjanke Exp $
 */
public class MMPCCostGroup extends X_MPC_Cost_Group
{
	/**
	 * 	Get from Cache
	 *	@param ctx context
	 *	@param M_Product_Costing_ID id
	 *	@return
	 */
        /*
	public static MMPCProductCosting get (Properties ctx, int MPC_Product_Costing_ID)
	{
		Integer ii = new Integer (MPC_Product_Costing_ID);
		MMPCProductCosting pc = (MMPCProductCosting)s_cache.get(ii);
		if (pc == null)
			pc = new MMPCProductCosting (ctx, MPC_Product_Costing_ID);
		return pc;
	}	//	get
	*/
    
	/**	Cache						*/
	//private static CCache	s_cache = new CCache ("M_Product_Costing", 20);
	
    
         MMPCCostGroup[] m_lines = null;
         private static CLogger	log	= CLogger.getCLogger (MMPCCostGroup.class);
	
	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param M_Product_Costing_ID id
	 */
	public MMPCCostGroup(Properties ctx, int MPC_Cost_Group_ID,String trxName)
	{
		super(ctx, MPC_Cost_Group_ID,trxName);
		if (MPC_Cost_Group_ID == 0)
		{
                /*    
		setC_AcctSchema_ID(0);
                setCostCumAmt(); 
                setCostCumQty();
                setCostLLAmt();
                setCostTLAmt();
                setM_Product_ID();
                setM_Warehouse_ID();
                setMPC_Cost_Element_ID();
                stS_Resource_ID();*/
		}
	}	//	MPCCostElement

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MMPCCostGroup(Properties ctx, ResultSet rs,String trxName)
	{
		super(ctx, rs,trxName);
	} 
        
        
        /**
	 * 	Get Element Cost
	 * 	@return lines
	 */
	public MMPCCostGroup[] getCostGroups()
	{
		//if (m_lines != null && !requery)
		//return m_lines;
		ArrayList list = new ArrayList();
		String sql = "SELECT * FROM MPC_Cost_Group WHERE AD_Client_ID = " + getAD_Client_ID();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql);			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new  MMPCCostGroup(getCtx(), rs,"MPC_Cost_Group"));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE,"getCostGroups" + sql, ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
		}
		catch (SQLException ex1)
		{
			log.log(Level.SEVERE,"getCostGroups" + sql, ex1);
		}
		pstmt = null;
		//
		m_lines = new  MMPCCostGroup[list.size()];
		list.toArray(m_lines);
		return m_lines;
	}	//	getCostElement
        
                /**
	 * 	Get Element Cost
	 * 	@return lines
	 */
	public static int getGLCostGroup()
	{			
		int MPC_Cost_Group_ID = 0;
		ArrayList list = new ArrayList();
		String sql = "SELECT cg.MPC_Cost_Group_ID FROM MPC_Cost_Group cg WHERE cg.isGL = 'Y' AND cg.AD_Client_ID =" + Env.getContextAsInt(Env.getCtx(),"AD_Client_ID");
		log.info ("getGLCostGroup() sql:" + sql);
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql);			
			ResultSet rs = pstmt.executeQuery();
            
			while(rs.next())
			{	
                        MPC_Cost_Group_ID = rs.getInt(1);
			}			
			rs.close();
			pstmt.close();
			pstmt = null;
			
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE,"getCostGroups" + sql, ex);
            return 0;
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
		
		return MPC_Cost_Group_ID;
	}	//	getCostElement
        
        
        
}	//	Cost Element
