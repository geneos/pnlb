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

/**
 *  KillProcess
 *
 *	@author Maria Jesus Martin
 *	@fecha 21/01/2013 Desbloquea las tablas que estan bloqueadas.
 *
 */


public class KillProcess {
    
    public static void main(String[] args)
    {
        org.compiere.Compiere.startupEnvironment(true);
        
        String sql;
        String sqlUpdate;
        
        int SERIAL = 0;
        int SID = 0;

        sql = "select e.sid ,e.serial# "+
              " from v$locked_object a "+
                ", dba_objects b "+
                ", dba_rollback_segs c "+
                ", v$transaction d "+
                ", v$session e "+
                ", v$process p "+
                "where a.object_id = b.object_id "+
                "and a.xidusn = c.segment_id "+
                "and a.xidusn = d.xidusn "+
                "and a.xidslot = d.xidslot "+
                "and d.addr = e.taddr "+
                "and p.addr = e.paddr ";

        PreparedStatement pstmt = DB.prepareStatement(sql,null);
        PreparedStatement pstmtUpdate;
        ResultSet rs;
        try {
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
                SID = rs.getInt(1);
                SERIAL = rs.getInt(2);
                
                System.out.println("Desbloqueando: " + SID + " - " + SERIAL);

                sqlUpdate = "ALTER SYSTEM KILL SESSION '"+SID+","+SERIAL+"';";
                    
                pstmtUpdate = DB.prepareStatement(sqlUpdate,null);
                pstmtUpdate.executeQuery();
                pstmtUpdate.close();
            }
            pstmt.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(KillProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
  
    }
}