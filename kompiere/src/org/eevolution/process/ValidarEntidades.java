/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eevolution.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
 * @author Jos√© Fantasia
 */
public class ValidarEntidades extends SvrProcess {

    int p_instance;
    private Timestamp p_fecha;
    private Timestamp p_fecha_to;
    private Long entity = 0L;
    private Long user = 0L;
    
    int flag = 0;
    
    protected String doIt() throws Exception {
        if(flag == 0){
            UtilProcess.initViewer("Permisos de Acceso Elemento - Roles", p_instance, this.getProcessInfo());
            return "success";        
        }
        
        // Agregar mensaje
        throw new Exception ("Debe ingresar solo 1 par·metro");
        
    }
    

    protected void prepare() {
        int flagProcess = 0;
        int flagWindow = 0;
        int flagForm = 0;
        int flagWorkflow = 0;
                
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (name.equals("AD_Process_ID")) {
                flagProcess = 1;
            } else if (name.equals("AD_Window_ID")) {
                flagWindow = 1;
            } else if (name.equals("AD_Form_ID")) {
                flagForm = 1;
            } else if (name.equals("AD_Workflow_ID")) {
                flagWorkflow = 1;
            }
        }
        
        if(flagProcess == 1  && (flagWindow == 1 || flagForm == 1 || flagWorkflow == 1))
                flag = 1;
        else if(flagWindow == 1  && (flagProcess == 1 || flagForm == 1 || flagWorkflow == 1))
                flag = 1;
        else if(flagForm == 1  && (flagProcess == 1 || flagWindow == 1 || flagWorkflow == 1))
                flag = 1;
        else if(flagWorkflow == 1  && (flagProcess == 1 || flagForm == 1 || flagWindow == 1))
                flag = 1;
        
        p_instance = getAD_PInstance_ID();
    
    }
}