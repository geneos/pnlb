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

import org.compiere.util.*;
import org.compiere.model.*;

/**
 *  Order Model.
 * 	Please do not set DocStatus and C_DocType_ID directly. 
 * 	They are set in the process() method. 
 * 	Use DocAction and C_DocTypeTarget_ID instead.
 *
 *  @author Jorg Janke
 *  @version $Id: MOrder.java,v 1.40 2004/04/13 04:19:30 jjanke Exp $
 */
public class MMPCOrderCost extends X_MPC_Order_Cost
{
	/**
	 *  Default Constructor 
	 *  @param ctx context
	 *  @param  C_Order_ID    order to load, (0 create new order)
	 */
	public MMPCOrderCost(Properties ctx, int MPC_Order_Cost_ID,String trxName)
	{
		super (ctx,  MPC_Order_Cost_ID,trxName);
		//  New
		if ( MPC_Order_Cost_ID == 0)
		{

		}
	}	//	MOrder
        
        	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 */
	public MMPCOrderCost(Properties ctx, MMPCProductCosting MPC_Product_Costing, int MPC_Order_ID, String trxName)
	{
		super (ctx, 0,trxName);
                
                setC_AcctSchema_ID(MPC_Product_Costing.getC_AcctSchema_ID());
//                setCostCumAmt(MPC_Product_Costing.getCostCumAmt());
             //   setCostCumQty(MPC_Product_Costing.getCostCumQty());
                //setCostLLAmt(MPC_Product_Costing.getCostLLAmt());
             //   setCostTLAmt(MPC_Product_Costing.getCostTLAmt());
                setM_Product_ID(MPC_Product_Costing.getM_Product_ID());
            //    setM_Warehouse_ID(MPC_Product_Costing.getM_Warehouse_ID());
              //  setMPC_Cost_Element_ID(MPC_Product_Costing.getMPC_Cost_Element_ID());
               // setS_Resource_ID(MPC_Product_Costing.getS_Resource_ID());
                save(get_TrxName());
                
	}	//	MOrder

        
        
	
	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 */
	public MMPCOrderCost(Properties ctx, ResultSet rs,String trxName)
	{
		super (ctx, rs,"MPC_Order_Cost");
	}	//	MOrder

	/**
	 * 	Overwrite Client/Org if required
	 * 	@param AD_Client_ID client
	 * 	@param AD_Org_ID org
	 */
	public void setClientOrg (int AD_Client_ID, int AD_Org_ID)
	{
		super.setClientOrg(AD_Client_ID, AD_Org_ID);
	}	//	setClientOrg


	//	save

	
	

}	//	MOrder
