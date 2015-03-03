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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.*;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.compiere.model.CalloutEngine;
import org.compiere.model.MField;
import org.compiere.model.MLocator;
import org.compiere.model.MProduct;
import org.compiere.model.MSequence;
import org.compiere.model.MTab;
import org.compiere.model.MWarehouse;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.eevolution.model.MMPCOrder;
import org.eevolution.model.MMPCOrderBOMLine;


/**
 *  UpdateLocation
 *
 *	@author JF
 *	@fecha 03/08/2012 Actualiza direccion en pago.
 * 
 *
 */


public class ValidarPartidasDuplicadas {
    
    public static void main(String[] args)
    {
        org.compiere.Compiere.startupEnvironment(true);
        
        String sql1;
        String sql2;
        String sql3;
        
        int attrId = 0;
        int inoutId = 0;
        int count = 0;
        String docAnt = "";

        sql1 = "select m_attributesetinstance_id, description from m_attributesetinstance order by created";
        sql2 = "select inout.documentno, prod.value, prod.name from m_inoutline linein "
                + "inner join m_inout inout on (linein.m_inout_id = inout.m_inout_id) "
                + "inner join m_product prod on (linein.m_product_id = prod.m_product_id) "
                + "where inout.docstatus = 'CO' and inout.c_doctype_id = 1000142 "
                + "and linein.m_attributesetinstance_id = ?";
        
        PreparedStatement pstmt = DB.prepareStatement(sql1,null);
        ResultSet rs;
        
        PreparedStatement pstmtInOut;
        ResultSet rsInOut;
                

        try {
            
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
                
                attrId = rs.getInt(1);
                

                
                pstmtInOut = DB.prepareStatement(sql2,null);
                pstmtInOut.setInt(1, attrId);
                rsInOut = pstmtInOut.executeQuery();

                while(rsInOut.next()) {
                    count = count + 1;
                    if(count > 1){
                        if (!(rs.getString(2) == null))
                            if(!rs.getString(2).equals("---")){
                                System.out.println("Repetidos para " + rs.getString(2) + " producto " + rsInOut.getString(2) + " - " + rsInOut.getString(2));
                                System.out.println("Anterior " + docAnt);
                                System.out.println("Actual " + rsInOut.getString(1)); 
                                System.out.println("-------------------------------------");
                            }
                    }
                    docAnt = rsInOut.getString(1);
                }
                
                count = 0;

                rsInOut.close();
                pstmtInOut.close();                
         
            }
            
            pstmt.close();
            rs.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(UpdateValidCombination.class.getName()).log(Level.SEVERE, null, ex);
        }

            
 
        
        
    }
}