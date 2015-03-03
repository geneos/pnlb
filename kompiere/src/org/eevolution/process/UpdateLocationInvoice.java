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
import org.compiere.util.DB;


/*
 *      UpdateLocationLocation
 *
 *	@author Maria Jesus Martin
 *	@fecha 28/12/2012 Actualiza direccion en Facturas.
 *      Actualizada para el tema de los reportes de cuentas corrientes.
 *
 */


public class UpdateLocationInvoice {
    
    public static void main(String[] args)
    {
        org.compiere.Compiere.startupEnvironment(true);
        
        String sql;
        String sqlUpdate;
        String sqlLocation;
        
        int invoiceId = 0;
        int partnerId = 0;

        sql = "select c_invoice_id, c_bpartner_id from c_invoice where c_bpartner_location_id is null";

        PreparedStatement pstmt = DB.prepareStatement(sql,null);
        PreparedStatement pstmtUpdate;
        PreparedStatement pstmtLocation;
        ResultSet rs;
        ResultSet rsLocation;
        

        try {
            
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
                
                invoiceId = rs.getInt(1);
                partnerId = rs.getInt(2);

                System.out.println("Datos: Factura  " + invoiceId + " - socio " + partnerId);

                sqlLocation = "select loc.C_BPartner_Location_id from c_bpartner part "
                        + "inner join C_BPartner_Location loc on (part.C_BPartner_ID = loc.C_BPartner_ID "
                        + "AND loc.IsBillTo='Y' AND loc.IsActive='Y') where part.c_bpartner_id = " + partnerId;

                pstmtLocation = DB.prepareStatement(sqlLocation,null);
                rsLocation = pstmtLocation.executeQuery();

                if(rsLocation.next()) 
                {
                    sqlUpdate = "update c_invoice set c_bpartner_location_id = " + rsLocation.getInt(1) + " where c_invoice_id = " + invoiceId;
                    System.out.println("Localización - " + rsLocation.getInt(1));
                    pstmtUpdate = DB.prepareStatement(sqlUpdate,null);
                    pstmtUpdate.executeQuery();
                    pstmtUpdate.close();
                } 
                else 
                {
                    System.out.println("Sin localización - " + partnerId);
                }
                pstmtLocation.close();
                rsLocation.close();
            }
            pstmt.close();
            rs.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(UpdateValidCombination.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
}