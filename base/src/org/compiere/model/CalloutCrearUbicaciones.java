/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.compiere.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.compiere.util.DB;

/** BISion - 11/01/2010 - Santiago Ibañez
 * Callout que crea la nueva distribucion de ubicaciones de Aprobado Famatina
 * @author santiago
 */
public class CalloutCrearUbicaciones extends CalloutEngine{

    public String crear(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value){
        int warehouseID = getWarehouseID("AprobadoFamatina");
        MWarehouse warehouse = new MWarehouse(ctx, warehouseID, null);
        for (int i=1;i<=7;i++){
            //Rack 01
            if (i==1)
                for (int j=1;j<=33;j++){
                    crearLocation(ctx, warehouseID, i, j);
                }
            //Rack 02
            else if (i==2)
                for (int j=1;j<=30;j++){
                    crearLocation(ctx, warehouseID, i, j);
                }
            //Rack 03
            else if (i==3){
                for (int j=1;j<=18;j++){
                    crearLocation(ctx, warehouseID, i, j);
                }
            }
            //Rack 04
            else if (i==4){
                for (int j=1;j<=12;j++){
                    crearLocation(ctx, warehouseID, i, j);
                }
            }
            //Rack 05
            else if (i==5){
                for (int j=1;j<=18;j++){
                    crearLocation(ctx, warehouseID, i, j);
                }
            }
            //Rack 06
            else if (i==6){
                for (int j=1;j<=18;j++){
                    crearLocation(ctx, warehouseID, i, j);
                }
            }
            //Rack 07
            else
                for (int j=1;j<=24;j++){
                    crearLocation(ctx, warehouseID, i, j);
                }
        }//fin for

        return "";
    }

    private int getWarehouseID(String value){
        int ret = 0;
        try {
            String sql = "select m_warehouse_id from m_warehouse where value = ?";
            PreparedStatement ps = DB.prepareStatement(sql, null);
            ps.setString(1, value);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                ret = rs.getInt(1);
            rs.close();
            ps.close();

        } catch (SQLException sQLException) {

        }
        return ret;
    }

    private void crearLocation(Properties ctx, int warehouseID, int i,int j){
        MLocator loc = new MLocator(ctx, 0, null);
        loc.setValue("Aprobado Famatina*"+i+"*"+j+"*1");
        loc.setM_Warehouse_ID(warehouseID);
        loc.setX(""+i);
        loc.setY(""+j);
        loc.setZ(""+1);
        loc.save();
        System.out.println("Ubicacion creada: "+loc.getValue());
    }
}
