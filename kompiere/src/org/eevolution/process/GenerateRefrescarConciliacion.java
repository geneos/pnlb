/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import org.compiere.grid.VCreateFromConciliacion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import org.compiere.model.MCONCILIACIONBANCARIA;
import org.compiere.model.MMOVIMIENTOCONCILIACION;
import org.compiere.model.MMOVIMIENTOFONDOS;
import org.compiere.model.MPAYMENTVALORES;
import org.compiere.model.MVALORPAGO;
import org.compiere.process.*;
import org.compiere.util.*;

/**
 * @author Daniel Gini Bision
 */
public class GenerateRefrescarConciliacion extends SvrProcess{

    int p_instance;
    long org;

     protected String doIt() throws Exception{

    	 MCONCILIACIONBANCARIA concBancaria = new MCONCILIACIONBANCARIA(Env.getCtx(),getRecord_ID(),null);

        // Actualizar Movimientos Pendientes
        VCreateFromConciliacion.actualizarPendientes(concBancaria);

        concBancaria.refrescarSaldos();
        concBancaria.save();

        // Actualizar Movimientos Posteriores
        if (concBancaria.deleteMovPosteriores())
			MCONCILIACIONBANCARIA.completarMovPosteriores(concBancaria);

        return "success";
    }
    
     /*
      * Genera las conciliaciones para items que todavia no esten en la conciliacion.
      */
     

    protected void prepare() {
       	p_instance = getAD_PInstance_ID();
    }

}
