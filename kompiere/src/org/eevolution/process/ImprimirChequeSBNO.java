/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eevolution.process;

import org.compiere.process.*;
import org.eevolution.tools.UtilProcess;
import org.compiere.model.MVALORPAGO;

/**
 *
 * @author José Fantasia
 */
public class ImprimirChequeSBNO extends SvrProcess {

    int p_instance;
    private int cheque = 0;
    
    protected String doIt() throws Exception {
        
        MVALORPAGO vpay = new MVALORPAGO(getCtx(),cheque,get_TrxName());  
        vpay.setEstado("I");
        vpay.save();

        UtilProcess.initViewer("Impresion de Cheques ICBC No a la orden", p_instance, this.getProcessInfo());
        return "success";        

    }
    

    protected void prepare() {

        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            
            String name = para[i].getParameterName();
            if (name.equals("RV_IMPRESION_CHEQUE_ID")) {
                cheque = para[i].getParameterAsInt();
            }
            
        }
        
        p_instance = getAD_PInstance_ID();
    
    }
}