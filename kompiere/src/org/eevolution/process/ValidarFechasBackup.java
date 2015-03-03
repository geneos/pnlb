/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eevolution.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import org.compiere.process.*;
import org.eevolution.tools.UtilProcess;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.compiere.apps.ADialog;
import org.compiere.model.X_T_ZADCHANGELOG;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 *
 * @author JosÃ© Fantasia
 */
public class ValidarFechasBackup extends SvrProcess {

    int p_instance;
    private Timestamp p_fecha;
    private Timestamp p_fecha_to;
    private Long entity = 0L;
    private Long user = 0L;
    

    protected String doIt() throws Exception {

        if (p_fecha.getTime() > p_fecha_to.getTime()) {
            String AD_Message = "DatabaseClientBackupError";
            JOptionPane.showMessageDialog(null, Msg.getMsg(Env.getCtx(), AD_Message), "Info", JOptionPane.INFORMATION_MESSAGE);
            throw new Exception("DatabaseClientVersionError");
        }

        String sql = "SELECT * FROM Z_ADCHANGELOG";
        
        /*
        
        if (entity != 0) {
            sql += " WHERE AD_Table_ID = ?";
        }

        if (user != 0) {
            sql += sql.contains("WHERE") ? " AND" : " WHERE";
            sql += "  AD_User_ID = ?";
        }

        if (p_fecha != null) {
            sql += sql.contains("WHERE") ? " AND" : " WHERE";
            sql += " DATECREATED >= ?";
        }

        if (p_fecha_to != null) {
            sql += sql.contains("WHERE") ? " AND" : " WHERE";
            sql += " DATECREATED <= ?";
        }
        
         */
        
        
        PreparedStatement pstmt = null;
        try {
            
            int idxP = 1;
            if (entity != 0) {
                //pstmt.setLong(idxP++, entity);
                sql += " WHERE AD_Table_ID = " + entity;
            }

            if (user != 0) {
                //pstmt.setLong(idxP++, user);
                sql += sql.contains("WHERE") ? " AND" : " WHERE";
                sql += "  AD_User_ID = " + user;                
            }

            if (p_fecha != null) {
                //pstmt.setTimestamp(idxP++, p_fecha);
                sql += sql.contains("WHERE") ? " AND" : " WHERE";
                sql += " DATECREATED >= to_date('"
				+ getFechaFromTimeStamp(p_fecha, "/") + " 00:00:00', 'dd,mm,yyyy  HH24:MI:SS')";                
            }

            if (p_fecha_to != null) {
                //pstmt.setTimestamp(idxP++, p_fecha_to);
                sql += sql.contains("WHERE") ? " AND" : " WHERE";
                sql += " DATECREATED <= to_date('"
				+ getFechaFromTimeStamp(p_fecha_to, "/") + " 23:59:59', 'dd,mm,yyyy  HH24:MI:SS')";  
            }
            
            pstmt = DB.prepareStatement(sql, null);
            

            ResultSet rs = pstmt.executeQuery();
            X_T_ZADCHANGELOG ch = null;

            String sqlins = "insert into T_ZADCHANGELOG (AD_CLIENT_ID, AD_ORG_ID, "
                    + "ISACTIVE, T_ZADCHANGELOG_ID, AD_PINSTANCE_ID, AD_TABLE_ID, TABLENAME, RECORD, "
                    + "COLUMNNAME, OLDVALUE, NEWVALUE, AD_USER_ID, "
                    + "NAME, DATECREATED) values (";

            int ch_id = 1;

            String sqlrecord = "select name from ? where ? = ?";
            String sqlrecordcol = "select name from ? where ? = ? or ? = ?";
            String valuerecord = "";
            
            DB.executeUpdate("DELETE FROM T_ZADCHANGELOG", null);

            int cnti = 0;

            while (rs.next()) {
                
                sqlins = "insert into T_ZADCHANGELOG (AD_CLIENT_ID, AD_ORG_ID, "
                    + "ISACTIVE, T_ZADCHANGELOG_ID, AD_PINSTANCE_ID, AD_TABLE_ID, TABLENAME, RECORD, "
                    + "COLUMNNAME, OLDVALUE, NEWVALUE, AD_USER_ID, "
                    + "NAME, DATECREATED) values (";
                
                sqlrecord = "select name from ? where ? = ?";

                sqlrecordcol = "select name from ? where ? = ? or ? = ?";
                
                sqlins += rs.getInt(1) + ",";
                sqlins += rs.getInt(2) + ",";
                sqlins += "'" + rs.getString(3) + "',";
                sqlins += ch_id + ",";
                ch_id++;
                sqlins += p_instance + ",";
                sqlins += rs.getInt(9) + ",";
                sqlins += "'" + rs.getString(11) + "',";

                // Obtengo la clave vale - name de la tabla logueada con el registro modificado

                PreparedStatement pstmt2 = null;
                ResultSet rs2 = null;
                try {
                    sqlrecord = "select name from " + rs.getString(10) + " where "
                            + rs.getString(10) + "_ID = " + rs.getString(12);
                    pstmt2 = DB.prepareStatement(sqlrecord, null);
                    System.out.println(rs.getString(10) + " = " + rs.getString(12));

                    rs2 = pstmt2.executeQuery();
                    while (rs2.next()) {
                        valuerecord = rs2.getString(1);
                    }
                    rs2.close();

                    pstmt2.close();
                    pstmt2 = null;
                } catch (Exception e2) {
                    
                    // en el caso que no existe la columna name capturo la excepción pero sigo 
                    // porque imprimo el id
                    
                    //pstmt2 = null;
                    System.out.println(e2.getMessage());
                    //return e2.getMessage();
                }

                // si puedo obtener la clave del registro ok de lo contrario inserto el id
                if (!valuerecord.equals("")) {
                    sqlins += "'" + valuerecord + "',";
                } else {
                    sqlins += "'" + rs.getInt(12) + "',";
                }
                sqlins += "'" + rs.getString(13) + "',";
                // valido si la columna es clave extranjera y saco el valor

                String fin = "";
                if (rs.getString(13).length() > 3) {
                    fin = rs.getString(13).substring(rs.getString(13).length() - 3, rs.getString(13).length());
                }

                System.out.println("fin de registro:" + fin);

                if (fin.equals("_ID")) {
                    pstmt2 = null;
                    rs2 = null;
                    try {
                        
                        sqlrecordcol = "select name from " +
                                rs.getString(13).substring(0, rs.getString(13).length() - 3) + 
                                " where " + rs.getString(13) + " = " + rs.getString(15) +  " or " + 
                                rs.getString(13) + " = " + rs.getString(16);
                        
                        pstmt2 = DB.prepareStatement(sqlrecordcol, null);
                        System.out.println(rs.getString(13));
                        System.out.println(rs.getString(13).substring(0, rs.getString(13).length() - 3));
                        
                        //pstmt2.setString(1, rs.getString(13).substring(0, rs.getString(13).length() - 3));
                        //pstmt2.setString(2, rs.getString(13));
                        //pstmt2.setString(3, rs.getString(15));
                        //pstmt2.setString(4, rs.getString(13));
                        //pstmt2.setString(5, rs.getString(16));

                        rs2 = pstmt2.executeQuery();
                        while (rs2.next()) {
                            sqlins += "'" + rs2.getString(1) + "',";
                        }
                        rs2.close();

                        pstmt2.close();
                        pstmt2 = null;
                    } catch (Exception e2) {
                        //pstmt2 = null;
                        //System.out.println(e2.getMessage());
                        //return e2.getMessage();
                        
                        /*
                         *  Si viene por la excepción quiere decir que no existe la columna NAME por lo tanto tomo
                         *  el valor del ID que viene como si fuera el caso de una tabla que no termina en _ID
                         * 
                         */
                        
                        pstmt2.close();
                        pstmt2 = null;
                        sqlins += "'" + rs.getString(15) + "',";
                        sqlins += "'" + rs.getString(16) + "',";
                        
                        
                    }
                } else {
                    sqlins += "'" + rs.getString(15) + "',";
                    sqlins += "'" + rs.getString(16) + "',";
                }

                sqlins += rs.getInt(17) + ",";
                sqlins += "'" + rs.getString(18) + "',";
                String[] fins = rs.getString(19).toString().substring(0, 10).split("-");
                System.out.println(rs.getString(19));
                System.out.println(rs.getString(19).toString().substring(0, 10));
                sqlins += " to_date('" + fins[2] + "/" + fins[1] + "/" + fins[0] + "', 'dd,mm,yyyy'))";

                System.out.println("***** inserta lineas: " + sqlins);
                cnti = DB.executeUpdate(sqlins, null);
            }

            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            pstmt = null;
            return e.getMessage();
        }

        UtilProcess.initViewer("Registros de Cambios 2", p_instance, this.getProcessInfo());
        return "success";

    }
    
    /**
     * 
     * @param ts
     * @return
     */
    private String getFechaFromTimeStamp(Timestamp ts, String separador) {
            Calendar gc = new GregorianCalendar();
            gc.setTimeInMillis(ts.getTime());
            String fecha = "";
            int mes = gc.get(Calendar.MONTH) + 1;
            if (gc.get(Calendar.DAY_OF_MONTH) < 10)
                    fecha = "0" + gc.get(Calendar.DAY_OF_MONTH);
            else
                    fecha += gc.get(Calendar.DAY_OF_MONTH);
            fecha += separador;
            if (mes < 10)
                    fecha += "0" + mes;
            else
                    fecha += mes;
            fecha += separador;
            fecha += gc.get(Calendar.YEAR);
            return fecha;
    }       

    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (name.equals("DATECREATED")) {
                p_fecha = (Timestamp) para[i].getParameter();
                p_fecha_to = (Timestamp) para[i].getParameter_To();
            } else if (name.equals("AD_Table_ID")) {
                entity = ((BigDecimal) para[i].getParameter()).longValue();
            } else if (name.equals("AD_User_ID")) {
                user = ((BigDecimal) para[i].getParameter()).longValue();
            }
        }
        p_instance = getAD_PInstance_ID();
    }
}