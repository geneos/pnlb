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
import org.compiere.util.QueryDB;

/**
 *  Order Model.
 * 	Please do not set DocStatus and C_DocType_ID directly. 
 * 	They are set in the process() method. 
 * 	Use DocAction and C_DocTypeTarget_ID instead.
 *
 *  @author Jorg Janke
 *  @version $Id: MOrder.java,v 1.40 2004/04/13 04:19:30 jjanke Exp $
 */
public class MMPCOrderBOM extends X_MPC_Order_BOM
{
	/**
	 *  Default Constructor
	 *  @param ctx context
	 *  @param  C_Order_ID    order to load, (0 create new order)
	 */
	public MMPCOrderBOM(Properties ctx, int MPC_Order_BOM_ID,String trxName)
	{
		super (ctx,  MPC_Order_BOM_ID,trxName);
		//  New
		if ( MPC_Order_BOM_ID == 0)
		{
			setProcessing(false);
		}
	}	//	MOrder
        
       

	
	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 */
	public MMPCOrderBOM(Properties ctx, ResultSet rs,String trxName)
	{
		super (ctx, rs,trxName);
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




	/*************************************************************************/

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MMPCOrderBOM[")
			.append(get_ID()).append("-").append(getDocumentNo())
			.append ("]");
		return sb.toString ();
	}	//	toString


	
	/**************************************************************************
	 * 	Get Invoices of Order
	 * 	@return invoices
	 */
	public MMPCOrderBOMLine[] getLines()
	{
		return getLines (getMPC_Order_BOM_ID());
	}	//	getLines

	/**
	 * 	Get Invoices of Order
	 * 	@param C_Order_ID id
	 * 	@return invoices
	 */
	public  MMPCOrderBOMLine[] getLines (int MPC_Order_ID)
	{
		ArrayList list = new ArrayList();
                
                QueryDB query = new QueryDB("org.compiere.model.X_MPC_Order_BOMLine");
                String filter = "MPC_Order_ID = " + MPC_Order_ID;
                List results = query.execute(filter);
                Iterator select = results.iterator();
                while (select.hasNext())
                {
                   X_MPC_Order_BOMLine bomline = (X_MPC_Order_BOMLine) select.next();
                   System.out.println("linea de product bom2 ************ " + bomline.getMPC_Order_BOMLine_ID());
                   list.add(new MMPCOrderBOMLine(getCtx(), bomline.getMPC_Order_BOM_ID(),"MPC_Order_BOM_Line"));
                   //list.add(bomline);
                }
                MMPCOrderBOMLine[] retValue = new MMPCOrderBOMLine[list.size()];
                list.toArray(retValue);
                return retValue; 
                
                
             
	}	//	getLines



}	//	MOrder
