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

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.compiere.model.MSequence;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 *  UpdateFacturadosEntregados
 *
 *	@author Zynnia
 *	@fecha 26/03/2012 Version para actualizar la cantidad 
 *      entregada borrada por errror desde el facturar.
 *
 */

/*
 * Consultas de apoyo
 * 
select ord.documentno, ol.c_orderline_id, ol.qtydelivered, ol.qtyinvoiced, ol.qtyentered, pl.value 
from c_orderline ol inner join c_order ord on (ol.c_order_id = ord.c_order_id)
inner join m_product pl on (ol.m_product_id = pl.m_product_id) 
where ol.qtyinvoiced > 0 and ol.qtydelivered = 0 and pl.value like '%MA%' and c_orderline_id not in 
(
select mpc_mrp.c_orderline_id from mpc_mrp where c_orderline_id in (
select ol.c_orderline_id from c_orderline ol inner join c_order ord on (ol.c_order_id = ord.c_order_id)
inner join m_product pl on (ol.m_product_id = pl.m_product_id) 
where ol.qtyinvoiced > 0 and ol.qtydelivered = 0 and pl.value like '%MA%'
)
)
 * 
 * 
 * 
 */

public class UpdateFacturadosEntregados {
    
    public static void main(String[] args)
    {
        org.compiere.Compiere.startupEnvironment(true);
        
        String sql1;
        String sql2;
        

        sql1 = "select ol.c_orderline_id, ol.qtyinvoiced, ol.qtyentered "
                + "from c_orderline ol "
                + "inner join m_product pl on (ol.m_product_id = pl.m_product_id) "
                + "where ol.qtyinvoiced > 0 and ol.qtydelivered = 0 and pl.value like '%GL%'";

        PreparedStatement pstm1 = DB.prepareStatement(sql1,null);
        
        ResultSet rs1;
        int refOrder = 0;
        int updateValue = 0;
        try {
            rs1 = pstm1.executeQuery();
            while(rs1.next()) {
                
                refOrder = rs1.getInt(1);
                
                sql2 = "update c_orderline set qtydelivered = qtyinvoiced where c_orderline_id = " + refOrder;		
				
                DB.executeUpdate(sql2, null);
                
                if(rs1.getInt(3)-rs1.getInt(2) < 0)
                    updateValue = 0;
                else
                    updateValue = rs1.getInt(3)-rs1.getInt(2);
                
                sql2 = "update mpc_mrp set qty = " + updateValue + " where c_orderline_id = " + refOrder;		
				
                DB.executeUpdate(sql2, null);
         
            }
            pstm1.close();
            rs1.close();
        } catch (SQLException ex) {
            Logger.getLogger(UpdateFacturadosEntregados.class.getName()).log(Level.SEVERE, null, ex);
        }

            
 
        
        
    }
}