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


import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.process.*;
import compiere.model.*;
import org.compiere.util.QueryDB;


/**
 *	Component Change into BOM
 *	
 *  @author Victor Perez
 *  @version $Id: ComponentChange.java,v 1.1 2004/01/17 05:24:03 jjanke Exp $
 */
public class ComponentChange extends SvrProcess
{
	/**	The Order				*/
		private int			 p_M_Product_ID = 0;     
        private Timestamp       p_ValidTo = null;
        private Timestamp       p_ValidFrom = null;
        private String          p_Action;
        private int             p_New_M_Product_ID =0;
        private BigDecimal      p_Qty = null;
        private int             morepara = 0;
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
			else if (name.equals("M_Product_ID") && morepara == 0)
                        {    
				p_M_Product_ID = ((BigDecimal)para[i].getParameter()).intValue();
                                morepara = 1;
                        }
                        else if (name.equals("ValidTo"))
				p_ValidTo = ((Timestamp)para[i].getParameter());
                        else if (name.equals("ValidFrom"))
				p_ValidFrom = ((Timestamp)para[i].getParameter());
                        else if (name.equals("Action"))
				p_Action = ((String)para[i].getParameter());
                        else if (name.equals("M_Product_ID"))
				p_New_M_Product_ID = ((BigDecimal)para[i].getParameter()).intValue();
                        else if (name.equals("Qty"))
				p_Qty = ((BigDecimal)para[i].getParameter());
			else
				log.log(Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
                StringBuffer result =  new StringBuffer("");
                /*System.out.println("Existing Product" + p_M_Product_ID );
                System.out.println("ValidTo" + p_ValidTo );
                System.out.println("ValidFrom" + p_ValidFrom );
                System.out.println("Action" + p_Action );
                System.out.println("New Product" + p_New_M_Product_ID);
                System.out.println("Qty" + p_Qty );*/
                
                    QueryDB query = new QueryDB("org.compiere.mfg.model.X_MPC_Product_BOMLine");
                    StringBuffer filter = new StringBuffer("M_Product_ID = " + p_M_Product_ID);
                    if (p_ValidFrom != null)
                    {
                      filter.append(" AND TRUNC(ValidFrom) >= ").append(DB.TO_DATE(p_ValidTo, true));
                    }
                    if (p_ValidTo !=null)
                    {
                      filter.append(" AND TRUNC(ValidTo) <= ").append(DB.TO_DATE(p_ValidTo, true));
                    }
                    
                    

                    java.util.List results = query.execute(filter.toString());
                    Iterator select = results.iterator();
                    while (select.hasNext())
                    {
                    X_MPC_Product_BOMLine bomline =  (X_MPC_Product_BOMLine) select.next();
               
                    
                    if (p_Action.equals("A"))
                    {
                        X_MPC_Product_BOMLine newbomline = new X_MPC_Product_BOMLine(Env.getCtx(), 0,null);
                        newbomline.setAssay(bomline.getAssay());
                        newbomline.setBackflushGroup(bomline.getBackflushGroup());
                        newbomline.setQtyBatch(bomline.getQtyBatch());
                        newbomline.setC_UOM_ID(bomline.getC_UOM_ID());
                        newbomline.setDescription(bomline.getDescription());
                        newbomline.setHelp(bomline.getHelp());
                        newbomline.setM_ChangeNotice_ID(bomline.getM_ChangeNotice_ID());  
                        newbomline.setForecast(bomline.getForecast());
                        newbomline.setQtyBOM(p_Qty);     
                        newbomline.setComponentType(bomline.getComponentType());
                        newbomline.setIsQtyPercentage(bomline.isQtyPercentage());
                        newbomline.setIsCritical(bomline.isCritical());
                        newbomline.setIssueMethod(bomline.getIssueMethod());
                        newbomline.setLine(25);
                        newbomline.setLeadTimeOffset(bomline.getLeadTimeOffset());
                        newbomline.setM_AttributeSetInstance_ID(bomline.getM_AttributeSetInstance_ID());
                        newbomline.setM_Product_ID(p_New_M_Product_ID);                     
                        newbomline.setMPC_Product_BOM_ID(bomline.getMPC_Product_BOM_ID());
                        newbomline.setScrap(bomline.getScrap());
                        newbomline.setValidFrom(newbomline.getUpdated());
                        newbomline.save(get_TrxName());
                        result.append("Component add");
                    }
                    else if (p_Action.equals("D"))
                    {
                        bomline.setIsActive(false);
                        bomline.save(get_TrxName());
                        result.append("Deactivate ");                        
                    }
                    else if (p_Action.equals("E"))
                    {
                        bomline.setValidTo(bomline.getUpdated());
                        bomline.save(get_TrxName());
                        result.append("Expire ");                        
                    }
                    else  if (p_Action.equals("R"))
                    {
                        X_MPC_Product_BOMLine newbomline = new X_MPC_Product_BOMLine(Env.getCtx(), 0,null);
                        newbomline.setAssay(bomline.getAssay());
                        newbomline.setBackflushGroup(bomline.getBackflushGroup());
                        newbomline.setQtyBatch(bomline.getQtyBatch());
                        newbomline.setComponentType(bomline.getComponentType());
                        newbomline.setC_UOM_ID(bomline.getC_UOM_ID());
                        newbomline.setDescription(bomline.getDescription());
                        newbomline.setM_ChangeNotice_ID(bomline.getM_ChangeNotice_ID());  
                        newbomline.setHelp(bomline.getHelp());
                        newbomline.setForecast(bomline.getForecast());
                        newbomline.setQtyBOM(p_Qty);
                        newbomline.setIsQtyPercentage(bomline.isQtyPercentage());
                        newbomline.setIsCritical(bomline.isCritical());
                        newbomline.setIssueMethod(bomline.getIssueMethod());
                        newbomline.setLine(25);
                        newbomline.setLeadTimeOffset(bomline.getLeadTimeOffset());
                        newbomline.setM_AttributeSetInstance_ID(bomline.getM_AttributeSetInstance_ID());
                        newbomline.setM_Product_ID(p_New_M_Product_ID);                     
                        newbomline.setMPC_Product_BOM_ID(bomline.getMPC_Product_BOM_ID());
                        newbomline.setScrap(bomline.getScrap());
                        newbomline.setValidFrom(newbomline.getUpdated());
                        newbomline.save(get_TrxName());
                        bomline.setIsActive(false);
                        bomline.save(get_TrxName());
                        result.append("Replace");
                    }
                    else  if (p_Action.equals("RE"))
                    {
                        X_MPC_Product_BOMLine newbomline = new X_MPC_Product_BOMLine(Env.getCtx(), 0,null);
                        newbomline.setAssay(bomline.getAssay());
                        newbomline.setBackflushGroup(bomline.getBackflushGroup());
                        newbomline.setQtyBatch(bomline.getQtyBatch());
                        newbomline.setComponentType(bomline.getComponentType());
                        newbomline.setC_UOM_ID(bomline.getC_UOM_ID());
                        newbomline.setDescription(bomline.getDescription());
                        newbomline.setHelp(bomline.getHelp());
                        newbomline.setM_ChangeNotice_ID(bomline.getM_ChangeNotice_ID());                        
                        newbomline.setHelp(bomline.getHelp());
                        newbomline.setForecast(bomline.getForecast());
                        newbomline.setQtyBOM(p_Qty);
                        newbomline.setIsQtyPercentage(bomline.isQtyPercentage());
                        newbomline.setIsCritical(bomline.isCritical());
                        newbomline.setIssueMethod(bomline.getIssueMethod());
                        newbomline.setLine(25);
                        newbomline.setLeadTimeOffset(bomline.getLeadTimeOffset());
                        newbomline.setM_AttributeSetInstance_ID(bomline.getM_AttributeSetInstance_ID());
                        newbomline.setM_Product_ID(p_New_M_Product_ID);                     
                        newbomline.setMPC_Product_BOM_ID(bomline.getMPC_Product_BOM_ID());                     
                        newbomline.setScrap(bomline.getScrap());
                        newbomline.setValidFrom(newbomline.getUpdated());
                        newbomline.save(get_TrxName());
                        bomline.setValidTo(bomline.getUpdated());
                        bomline.save(get_TrxName());
                        result.append("Replace & Expire");
                    }
                    }                    
            return result.toString();
	}	//	doIt
	
}	//	Componet Change
