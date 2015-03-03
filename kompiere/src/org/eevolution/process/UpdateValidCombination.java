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
 *  TestVit4b
 *
 *	@author Vit4B
 *	@fecha 02/07/2007 Version para actualizar la base de datos con los reservados.
 *
 */


public class UpdateValidCombination {
    
    public static void main(String[] args)
    {
        org.compiere.Compiere.startupEnvironment(true);
        
        String sql;
        String sqlInsert;
        

        sql = "select c_elementvalue_id,created, value, name from c_elementvalue "
                + "where c_elementvalue_id not in (select account_id from C_ValidCombination) "
                + "and ad_client_id = 1000002";

        PreparedStatement pstmtobl = DB.prepareStatement(sql,null);
        PreparedStatement pstmtInsert;
        ResultSet rsobl;
        int contador = 1;
        try {
            rsobl = pstmtobl.executeQuery();
            while(rsobl.next()) {
                
                MSequence seq = MSequence.get(Env.getCtx(), "C_ValidCombination");
                int next = seq.getCurrentNext();
                seq.setCurrentNext(next+1);
                seq.save();
                
                sqlInsert = "insert into C_ValidCombination (C_VALIDCOMBINATION_ID,AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,CREATED,CREATEDBY,"
                        + "UPDATED,UPDATEDBY,ALIAS,COMBINATION,DESCRIPTION,ISFULLYQUALIFIED,C_ACCTSCHEMA_ID,ACCOUNT_ID) "
                        + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                
                pstmtInsert = DB.prepareStatement(sqlInsert,null);
                pstmtInsert.setLong(1, next);
                pstmtInsert.setLong(2, 1000002);
                pstmtInsert.setLong(3, 1000033);
                pstmtInsert.setString(4, "Y");
                pstmtInsert.setDate(5, rsobl.getDate(2));
                pstmtInsert.setLong(6, 1000684);
                pstmtInsert.setDate(7, rsobl.getDate(2));
                pstmtInsert.setLong(8, 1000684);
                pstmtInsert.setString(9, "");
                pstmtInsert.setString(10, "Famatina-" + rsobl.getString(3) + "-_");
                System.out.println("Cuenta " + contador + ": " + rsobl.getString(3));
                contador++;
                pstmtInsert.setString(11, "PANALAB S.A.-" + rsobl.getString(4) + "-_");
                pstmtInsert.setString(12, "Y");
                pstmtInsert.setLong(13, 1000002);
                pstmtInsert.setLong(14, rsobl.getLong(1));
                pstmtInsert.executeQuery();
                pstmtInsert.close();
         
            }
            pstmtobl.close();
            rsobl.close();
        } catch (SQLException ex) {
            Logger.getLogger(UpdateValidCombination.class.getName()).log(Level.SEVERE, null, ex);
        }

            
 
        
        
    }
}