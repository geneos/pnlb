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
 * Copyright (C) 2004 Victor Perez, e-Evolution, S.C.
 * All Rights Reserved.
 * Contributor(s): Victor Perez, e-Evolution, S.C.
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
import org.eevolution.model.MMPCProductBOMLine;


/**
 *	CalculateLowLevel for MRP
 *	
 *  @author Victor Perez, e-Evolution, S.C.
 *  @version $Id: CalculateLowLevel.java,v 1.1 2004/06/22 05:24:03 vpj-cd Exp $
 */
public class CalculateLowLevel extends SvrProcess
{
	/**					*/
        
	private int AD_Client_ID = 0 ;
        
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
                ProcessInfoParameter[] para = getParameter();
	}	//	prepare

        
     protected String doIt() throws Exception                
	{
            
                 
        String sql = "SELECT p.M_Product_ID FROM M_Product p WHERE AD_Client_ID = " +  AD_Client_ID;                    
		PreparedStatement pstmt = null;
		try
		{
                        pstmt = DB.prepareStatement (sql,get_TrxName());                       
                        ResultSet rs = pstmt.executeQuery ();
                        while (rs.next())
                        {
                            int m_M_Product_ID = rs.getInt(1); 
                            if (m_M_Product_ID != 0)
                            { 
                                MProduct product = new MProduct(getCtx(), m_M_Product_ID,get_TrxName());
                                int lowlevel = MMPCProductBOMLine.getlowLevel(m_M_Product_ID);
                                //System.out.println("Low Level" + lowlevel);
                                product.setLowLevel(lowlevel);
                                product.save(get_TrxName());
                            }
                        }
                        rs.close();
                        pstmt.close();

		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "doIt - " + sql, e);
                        /** BISion - 19/11/2008 - Santiago Ibañez
                         * Modificacion realizada para deshacer los cambios
                         * hechos a la BD en la clase SvrProcess
                         */
                        throw e;
		}

            return "ok";
     }
                                                                
}
