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
 * Portions created by Carlos Ruiz are Copyright (C) 2005 QSS Ltda.
 * Add e-Evolution by Perez Juarez
 * Contributor(s): Carlos Ruiz (globalqss)
 *****************************************************************************/
package org.eevolution.process;


import java.sql.*;
import java.math.*;
import java.util.logging.*;

import org.compiere.util.*;
import org.compiere.process.*;

/**
 * Title:	Inventory Valuation Temporary Table
 *	
 *  @author Carlos Ruiz (globalqss)
 *  @version $Id: T_InventoryValue_Create.java,v 1.0 2005/09/21 20:29:00 globalqss Exp $
 */
public class MRPRequiered extends SvrProcess
{

	/** The Parameters		*/
	private int p_M_Product_ID;
	
	/** The Record						*/
	private int		p_Record_ID = 0;
	/** The Instance					*/
	private int     p_PInstance_ID;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else if (name.equals("M_Product_ID"))
				p_M_Product_ID = para[i].getParameterAsInt();
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_Record_ID = getRecord_ID();
		p_PInstance_ID = getAD_PInstance_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
		String sqlupd;
	
		int cntu = 0;
		int cnti = 0;

		log.info("MRP Requiered");
                
                
                
                
		// Clear
		//	v_ResultStr := 'ClearTable';
		//	DELETE T_InventoryValue WHERE M_Warehouse_ID=p_M_Warehouse_ID;
		//	COMMIT;		
		
		String sqlins="";

                int q_M_Product_ID=0;
                int q_M_Warehouse_ID=0;
                int q_MPC_Order_ID=0;
                BigDecimal req1=Env.ZERO;
                BigDecimal requp=Env.ZERO;
		StringBuffer sqlobl = new StringBuffer("Select obl.M_Product_ID, o.M_Warehouse_ID, o.MPC_Order_ID, obl.MPC_Order_BOM_ID, obl.MPC_Order_BOMLine_ID, obl.QtyRequiered,obl.QtyBom ,obl.isQtyPercentage, obl.QtyBatch , o.datestartSchedule from MPC_Order_BOMLine obl  INNER Join MPC_Order o ON(o.MPC_Order_ID=obl.MPC_Order_ID) where obl.M_Product_ID=" +p_M_Product_ID +" order by obl.M_Product_ID, o.datestartSchedule");
                System.out.println("***** Imprime primer sql" +sqlobl.toString());
                try
                    {
                        
                            PreparedStatement pstmtobl = DB.prepareStatement(sqlobl.toString(),null);
                            //pstmt.setInt(1, AD_Client_ID);
                            ResultSet rsobl = pstmtobl.executeQuery();
                            //
                            while (rsobl.next())
                            {
                                System.out.println("***** entra primer sql" +rsobl.getInt(1));
                                q_M_Product_ID = rsobl.getInt(1);
                                q_M_Warehouse_ID = rsobl.getInt(2);
                                q_MPC_Order_ID=rsobl.getInt(3);
                                req1=rsobl.getBigDecimal(6);
                                StringBuffer sql = new StringBuffer("SELECT s.AD_Client_ID, s.AD_Org_ID,s.M_Product_ID , s.QtyOnHand, s.Updated, p.Name ,p.Value, masi.Description, l.Value, w.Value, w.M_warehouse_ID, p.C_UOM_ID,s.M_Locator_ID, s.M_AttributeSetInstance_ID, l.x,l.y,l.z ");
                             sql.append("  FROM M_Storage s ");
                             sql.append(" INNER JOIN M_Product p ON (s.M_Product_ID = p.M_Product_ID) ");
                             sql.append(" INNER JOIN M_AttributeSetInstance masi ON (masi.M_AttributeSetInstance_ID = s.M_AttributeSetInstance_ID) ");
                             sql.append(" INNER JOIN M_Warehouse w ON (w.M_Warehouse_ID = " +q_M_Warehouse_ID +") ");
                             sql.append(" Inner Join M_Locator l ON(l.M_Warehouse_ID=w.M_Warehouse_ID and s.M_Locator_ID=l.M_Locator_ID) ");
                             sql.append(" WHERE s.M_Product_ID = " +q_M_Product_ID + " and s.QtyOnHand <> 0 Order by masi.Updated " );
                
		log.log(Level.INFO, "TComponentCheck.executeQuery - SQL", sql.toString());                
		//  reset table
		//  Execute
		
                
			PreparedStatement pstmt1 = DB.prepareStatement(sql.toString(),null);
			//pstmt.setInt(1, AD_Client_ID);
			ResultSet rs1 = pstmt1.executeQuery();
			//
			while (rs1.next())
			{
                             if (req1.compareTo(rs1.getBigDecimal(4)) <0)
                            {
                                 if (req1.compareTo(Env.ZERO)<=0)
                                     requp=Env.ZERO;
                                 else
                                     requp=req1;
                                req1=req1.subtract(rs1.getBigDecimal(4));
                            }
                            else
                            {
                                
                                requp=rs1.getBigDecimal(4);
                                req1=req1.subtract(rs1.getBigDecimal(4));
                            }
                            String sqlins2="";
                            sqlins2 = sqlins +"Values("+rs1.getInt(1) +"," +rs1.getInt(2) +"," +p_PInstance_ID +"," +q_MPC_Order_ID +"," +rsobl.getInt(4) +"," +rsobl.getInt(5) +"," +q_M_Product_ID +",'" +rs1.getString(6) +"','" +rs1.getString(7) +"',"
                            +rs1.getInt(12) +"," +rs1.getBigDecimal(4) +"," +requp +"," +rsobl.getBigDecimal(7) +"," +rsobl.getBigDecimal(8) +"," +q_M_Warehouse_ID +"," +rsobl.getBigDecimal(9) +"," +rsobl.getBigDecimal(11) +"," +rs1.getInt(13) +"," +rs1.getInt(14) +"," +rs1.getString(15) +"," +rs1.getString(16) +"," +rs1.getString(17)+")";
                            
                            System.out.println("***** inserta lineas " +sqlins2 );
                            cnti = DB.executeUpdate(sqlins2,null);
                       
                        
                            System.out.println("*****  Created=" +cnti);
                            
                            
                        }
                        rs1.close();
                        pstmt1.close();
                                
                            }
                            rsobl.close();
                            pstmtobl.close();
                    }
                    catch(SQLException obl)
                    {
                    }
                return "";
        }
        
	
}	//	MRPRequiered
