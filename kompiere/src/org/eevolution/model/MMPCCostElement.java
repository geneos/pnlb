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

import java.sql.*;
import java.util.*;
import java.util.logging.*;


import org.compiere.util.*;
import org.compiere.model.*;

/**
 *	Cost Element
 *	
 *  @author Jorg Janke
 *  @version $Id: MProductCosting.java,v 1.4 2004/05/13 06:05:22 jjanke Exp $
 */
public class MMPCCostElement extends X_MPC_Cost_Element
{
	
    
	/**	Cache						*/
	//private static CCache	s_cache = new CCache ("M_Product_Costing", 20);
	
	private static CLogger log = CLogger.getCLogger(MMPCCostElement.class);
    MMPCCostElement[] m_lines = null;
	
	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param M_Product_Costing_ID id
	 */
	public MMPCCostElement(Properties ctx, int MPC_Cost_Element_ID,String trxName)
	{
		super(ctx, MPC_Cost_Element_ID,trxName);
		if (MPC_Cost_Element_ID == 0)
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
	public MMPCCostElement(Properties ctx, ResultSet rs,String trxName)
	{
		super(ctx, rs,trxName);
	} 
        
        
        /**
	 * 	Get Element Cost
	 * 	@return lines
	 */
	public static MMPCCostElement[] getElements (int AD_Client_ID)
	{
		//if (m_lines != null && !requery)
		//return m_lines;		
		ArrayList list = new ArrayList();
		String sql = "SELECT * FROM MPC_Cost_Element WHERE AD_Client_ID ="+  AD_Client_ID;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql);			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new  MMPCCostElement(Env.getCtx(), rs,null));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE,"getLines", ex);
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
		MMPCCostElement[] retValue = new  MMPCCostElement[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getCostElement
        
        /** BISion - 06/01/2009 - Santiago Ibañez
         * Metodo creado para retornar el elemento de costo con el nombre dado.
         * @return Elemento de costo asociado al nombre dado.
         */
        public static MMPCCostElement getCostElementByName(String name){
            String sql = "SELECT * FROM MPC_COST_ELEMENT WHERE NAME = '"+name+"'";
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            try {
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new MMPCCostElement(Env.getCtx(), rs, null);
                }
            } catch (SQLException ex) {
                Logger.getLogger(MMPCCostElement.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }
}	//	Cost Element
