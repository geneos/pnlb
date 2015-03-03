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
import java.util.*;


import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.process.*;
import compiere.model.*;
import org.eevolution.model.MMPCCostElement;
import org.eevolution.model.MMPCProductCosting;

/**
 *	CopyPriceToStandard 
 *	
 *  @author Victor Perez, e-Evolution, S.C.
 *  @version $Id: CopyPriceToStandard.java,v 1.1 2004/06/22 05:24:03 vpj-cd Exp $
 */
public class CopyPriceToStandard extends SvrProcess
{
	/**					*/
        private int		      p_AD_Org_ID = 0;
        private int             p_C_AcctSchema_ID = 0;
        private int             p_M_Warehouse_ID = 0;
        private int             p_S_Resource_ID = 0 ;
        private int             p_MPC_Cost_Group_ID = 0;
        private int             p_MPC_Cost_Element_ID = 0;
        private int             p_M_PriceList_Version_ID =0;
        private Properties ctx = Env.getCtx();
        
	
        
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
            else if (name.equals("MPC_Cost_Group_ID"))
            {    
				p_MPC_Cost_Group_ID = ((BigDecimal)para[i].getParameter()).intValue();
                               
            }
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
                       
            else if (name.equals("MPC_Cost_Element_ID"))
            {    
				p_MPC_Cost_Element_ID = ((BigDecimal)para[i].getParameter()).intValue();
                                
            }
            else if (name.equals("M_PriceList_Version_ID"))
            {    
				p_M_PriceList_Version_ID = ((BigDecimal)para[i].getParameter()).intValue();
            }
                        else
				log.log(Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	
        
    protected String doIt() throws Exception                
	{
            //System.out.println("PARAMETROS :  p_C_AcctSchema_ID" + p_C_AcctSchema_ID + "p_MPC_Cost_Group_ID" + p_MPC_Cost_Group_ID + "p_M_Warehouse_ID" +  p_M_Warehouse_ID + "p_S_Resource_ID" + p_S_Resource_ID);
            BigDecimal price = Env.ZERO;
            BigDecimal convrate = Env.ZERO;
            int M_PriceList_ID =0;
            int M_PriceList_Version_ID = 0;
            int M_Product_ID =0;
            int C_Currency_ID = 0;
            BigDecimal list = Env.ZERO;
            MAcctSchema schema = new  MAcctSchema(ctx,p_C_AcctSchema_ID ,null);
            StringBuffer sql = new StringBuffer("SELECT M_Product_ID,M_PriceList_Version_ID, PriceStd FROM M_ProductPrice WHERE M_PriceList_Version_ID =" +p_M_PriceList_Version_ID +" AND PriceStd <> 0");
            try
            {                
                //System.out.println("query " +sql.toString()); 
                PreparedStatement pstmt = DB.prepareStatement(sql.toString());
                ResultSet rs = pstmt.executeQuery();

                                    //
                while (rs.next())
                {
                	M_Product_ID = rs.getInt(1);
                	M_PriceList_Version_ID = rs.getInt(2);
                	
                    //System.out.println("M_Product_ID" + product_id + "p_C_AcctSchema_ID" + p_C_AcctSchema_ID + "p_MPC_Cost_Group_ID" + p_MPC_Cost_Group_ID + "p_M_Warehouse_ID" +  p_M_Warehouse_ID + "p_S_Resource_ID" + p_S_Resource_ID);
                	M_PriceList_ID = DB.getSQLValue(get_TrxName(),"SELECT M_PriceList_ID FROM M_PriceList_Version WHERE M_PriceList_Version_ID = ? " ,M_PriceList_Version_ID );
                	 C_Currency_ID = DB.getSQLValue(get_TrxName() , "SELECT C_Currency_ID FROM M_PriceList WHERE M_PriceList_ID = ?",M_PriceList_ID);
                	 
                	 	if (C_Currency_ID!=schema.getC_Currency_ID())
                	 	{                     	
                     	price = MConversionRate.convert(ctx,rs.getBigDecimal(3),C_Currency_ID,schema.getC_Currency_ID(),getAD_Client_ID(),p_AD_Org_ID);                     	
                	 	}
                	 	else
                	 	price = rs.getBigDecimal(3);
                   
                	 	   MMPCProductCosting[]  pc = MMPCProductCosting.getElements( M_Product_ID , p_C_AcctSchema_ID , p_MPC_Cost_Group_ID , p_M_Warehouse_ID , p_S_Resource_ID);            
                           if (pc != null)
                           {
                                for (int e = 0 ; e < pc.length ; e ++ )
                                {                                  
                                   MMPCCostElement element = new MMPCCostElement(getCtx(), p_MPC_Cost_Element_ID,null);                                  
                                   if (element.getMPC_ElementType().equals(element.MPC_ELEMENTTYPE_Material)) 
                                   {                                                                     
                                    pc[0].setCostTLAmt(price);
                                    pc[0].save(get_TrxName());
                                    break;
                                   }                                                                      
                                }
                           }
                }
                 rs.close();
                 pstmt.close();
            }
    		catch (SQLException e)
			{
				log.log(Level.SEVERE, "doIt - " + sql, e);
			}
   
            return "ok";
     }
}	
