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

package org.eevolution.process;

import java.util.logging.*;
import java.math.*;
import java.sql.*;



//import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.process.*;
import compiere.model.*;
import org.eevolution.model.MMPCCostElement;
import org.eevolution.model.MMPCProductCosting;


/**
 *	Re-Open Order Process (from Closed to Completed)
 *	
 *  @author Victor P�rez, e-Evolution, S.C.
 *  @version $Id: CreateCostElement.java,v 1.1 2004/06/22 05:24:03 vpj-cd Exp $
 */
public class CreateCostElement extends SvrProcess
{
	/**					*/
        private int			  p_AD_Org_ID = 0;
        private int             p_C_AcctSchema_ID = 0;
        private int             p_M_Warehouse_ID = 0;
        private int             p_S_Resource_ID = 0 ;
        private int             p_MPC_Cost_Group_ID = 0;
        private int             p_M_Product_ID = 0;
	
        
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
			else if (name.equals("AD_Org_ID"))
                        {    
				p_AD_Org_ID = ((BigDecimal)para[i].getParameter()).intValue();
                                
                        }
                        else if (name.equals("C_AcctSchema_ID"))
                        {    
				p_C_AcctSchema_ID = ((BigDecimal)para[i].getParameter()).intValue();
                                
                        }
                        else if (name.equals("M_Warehouse_ID"))
                        {    
				p_M_Warehouse_ID = ((BigDecimal)para[i].getParameter()).intValue();
                                
                        }
                        else if (name.equals("S_Resource_ID"))
                        {    
				p_S_Resource_ID = ((BigDecimal)para[i].getParameter()).intValue();
                                
                        }
                        else if (name.equals("MPC_Cost_Group_ID"))
                        {    
				p_MPC_Cost_Group_ID = ((BigDecimal)para[i].getParameter()).intValue();
                                
                        }
                        else if (name.equals("M_Product_ID"))
                        {    
				p_M_Product_ID = ((BigDecimal)para[i].getParameter()).intValue();
                                
                        }
                        else
				log.log(Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}
	}	//	prepare
  
    protected String doIt() throws Exception                
	{
            
        int AD_Client_ID = getAD_Client_ID();
        String sql = "SELECT p.M_Product_ID FROM M_Product p where AD_Client_ID=" +AD_Client_ID;
        if (p_M_Product_ID!=0)
        sql = sql + " and p.M_Product_ID =" +p_M_Product_ID;
        boolean existe = false;
		MMPCCostElement[] ce =  MMPCCostElement.getElements(getAD_Client_ID());
		try
		{
			PreparedStatement pstmt = DB.prepareStatement (sql);
                        
                        int m_MPC_Cost_Element_ID = 0 ;
                        
                        ResultSet rs = pstmt.executeQuery ();
                        while (rs.next())
                        {
                            int m_M_Product_ID = rs.getInt(1); 
                            
                            for (int j = 0 ; j < ce.length ; j ++)
                            {                                                          
                                                                                               
                                m_MPC_Cost_Element_ID = ce[j].getMPC_Cost_Element_ID();                   
                                MMPCProductCosting pc = new MMPCProductCosting(getCtx(),0,null);
                                
                                if (!pc.getElement(m_M_Product_ID , p_C_AcctSchema_ID , p_MPC_Cost_Group_ID , m_MPC_Cost_Element_ID , p_M_Warehouse_ID, p_S_Resource_ID) ) // && !existe)
                                {                            
                                    log.info("Create Cost Element for Product"  +m_M_Product_ID + " Warehouse:" +p_M_Warehouse_ID + " Plant: " +p_S_Resource_ID);
                                    pc.setM_Product_ID(m_M_Product_ID);
                                    pc.setC_AcctSchema_ID(p_C_AcctSchema_ID);
                                    pc.setMPC_Cost_Group_ID(p_MPC_Cost_Group_ID);
                                    pc.setMPC_Cost_Element_ID(m_MPC_Cost_Element_ID);
                                    pc.setM_Warehouse_ID(p_M_Warehouse_ID);
                                    pc.setS_Resource_ID(p_S_Resource_ID);                                    
                                    pc.save(get_TrxName());  
                                }
                                
                                
                            }
                        }
                        rs.close();
                        pstmt.close();

		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"doIt - " + sql, e);
		}

            return "ok";
     }
                                                                
}	//	Create Cost Element
