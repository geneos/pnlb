/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Trx;

/**
 * Proceso invidual que actualiza los reservados para las Ordenes de manufactura
 * Ejecuta el mismo algoritmo que le proceso de IntegratedMRP
 * @author GENEOS
 */
public class ActualizacionDeReservados extends SvrProcess{

    @Override
    protected void prepare() {
       return;
    }

    @Override
    protected String doIt() throws Exception {
        //1- Actualizcion de Reservados
        MRP mrp = new MRP();
        if (!mrp.executeUpdateValues())
              return "No se pudo completar la actualización de reservados.";
        return "ok";
    }

}
